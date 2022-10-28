# 共享模型之[线程池](https://so.csdn.net/so/search?q=线程池&spm=1001.2101.3001.7020)

[toc]

# 推荐资料

[Java线程池实现原理及其在美团业务中的实践 - 美团技术团队 (meituan.com)](https://tech.meituan.com/2020/04/02/java-pooling-pratice-in-meituan.html)

# 自定义线程池

![image-20211104110718784](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/603b16c84ba3b0dd1ffbabec9c83b8d7.png)

## 步骤1：自定义拒绝策略接口

```java
@FunctionalInterface // 拒绝策略
interface RejectPolicy<T> {
    void reject(BlockingQueue<T> queue, T task);
}
```

## 步骤2：自定义任务队列

```java
@Slf4j(topic = "c.BlockingQueue")
class BlockingQueue<T> {
    // 1. 任务队列
    private Deque<T> queue = new ArrayDeque<>();

    // 2. 锁
    private ReentrantLock lock = new ReentrantLock();

    // 3. 生产者条件变量
    private Condition fullWaitSet = lock.newCondition();

    // 4. 消费者条件变量
    private Condition emptyWaitSet = lock.newCondition();

    // 5. 容量
    private int capcity;

    public BlockingQueue(int capcity) {
        this.capcity = capcity;
    }

    // 带超时阻塞获取
    public T poll(long timeout, TimeUnit unit) {
        lock.lock();
        try {
            // 将 timeout 统一转换为 纳秒
            long nanos = unit.toNanos(timeout);
            while (queue.isEmpty()) {
                try {
                    // 返回值是剩余时间
                    if (nanos <= 0) {
                        return null;
                    }
                    nanos = emptyWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            fullWaitSet.signal();
            return t;
        } finally {
            lock.unlock();
        }
    }

    // 阻塞获取
    public T take() {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                try {
                    emptyWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            fullWaitSet.signal();
            return t;
        } finally {
            lock.unlock();
        }
    }

    // 阻塞添加
    public void put(T task) {
        lock.lock();
        try {
            while (queue.size() == capcity) {
                try {
                    log.debug("等待加入任务队列 {} ...", task);
                    fullWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("加入任务队列 {}", task);
            queue.addLast(task);
            emptyWaitSet.signal();
        } finally {
            lock.unlock();
        }
    }

    // 带超时时间阻塞添加
    public boolean offer(T task, long timeout, TimeUnit timeUnit) {
        lock.lock();
        try {
            long nanos = timeUnit.toNanos(timeout);
            while (queue.size() == capcity) {
                try {
                    if(nanos <= 0) {
                        return false;
                    }
                    log.debug("等待加入任务队列 {} ...", task);
                    nanos = fullWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("加入任务队列 {}", task);
            queue.addLast(task);
            emptyWaitSet.signal();
            return true;
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }

    public void tryPut(RejectPolicy<T> rejectPolicy, T task) {
        lock.lock();
        try {
            // 判断队列是否满
            if(queue.size() == capcity) {
                rejectPolicy.reject(this, task);
            } else {  // 有空闲
                log.debug("加入任务队列 {}", task);
                queue.addLast(task);
                emptyWaitSet.signal();
            }
        } finally {
            lock.unlock();
        }
    }
}
```

## 步骤3：自定义线程池

```java
@Slf4j(topic = "c.ThreadPool")
class ThreadPool {
    // 任务队列
    private BlockingQueue<Runnable> taskQueue;

    // 线程集合
    private HashSet<Worker> workers = new HashSet<>();

    // 核心线程数
    private int coreSize;

    // 获取任务时的超时时间
    private long timeout;

    private TimeUnit timeUnit;

    private RejectPolicy<Runnable> rejectPolicy;

    // 执行任务
    public void execute(Runnable task) {
        // 当任务数没有超过 coreSize 时，直接交给 worker 对象执行
        // 如果任务数超过 coreSize 时，加入任务队列暂存
        synchronized (workers) {
            if(workers.size() < coreSize) {
                Worker worker = new Worker(task);
                log.debug("新增 worker{}, {}", worker, task);
                workers.add(worker);
                worker.start();
            } else {
//                taskQueue.put(task);
                // 1) 死等
                // 2) 带超时等待
                // 3) 让调用者放弃任务执行
                // 4) 让调用者抛出异常
                // 5) 让调用者自己执行任务
                taskQueue.tryPut(rejectPolicy, task);
            }
        }
    }

    public ThreadPool(int coreSize, long timeout, TimeUnit timeUnit, int queueCapcity, RejectPolicy<Runnable> rejectPolicy) {
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.taskQueue = new BlockingQueue<>(queueCapcity);
        this.rejectPolicy = rejectPolicy;
    }

    class Worker extends Thread{
        private Runnable task;

        public Worker(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            // 执行任务
            // 1) 当 task 不为空，执行任务
            // 2) 当 task 执行完毕，再接着从任务队列获取任务并执行
//            while(task != null || (task = taskQueue.take()) != null) {
            while(task != null || (task = taskQueue.poll(timeout, timeUnit)) != null) {
                try {
                    log.debug("正在执行...{}", task);
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    task = null;
                }
            }
            synchronized (workers) {
                log.debug("worker 被移除{}", this);
                workers.remove(this);
            }
        }
    }
}
```

## 步骤4：测试

```java
@Slf4j(topic = "c.TestPool")
public class TestPool {
    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool(1,
                1000, TimeUnit.MILLISECONDS, 1, (queue, task)->{
            // 1. 死等
//            queue.put(task);
            // 2) 带超时等待
//            queue.offer(task, 1500, TimeUnit.MILLISECONDS);
            // 3) 让调用者放弃任务执行
//            log.debug("放弃{}", task);
            // 4) 让调用者抛出异常
//            throw new RuntimeException("任务执行失败 " + task);
            // 5) 让调用者自己执行任务
            task.run();
        });
        for (int i = 0; i < 4; i++) {
            int j = i;
            threadPool.execute(() -> {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("{}", j);
            });
        }
    }
}
```

## 测试结果

### 死等

```java
ThreadPool threadPool = new ThreadPool(1,1000, TimeUnit.MILLISECONDS, 1, 
                                       (queue, task)->{
            // 1. 死等
            queue.put(task);
                                       });
12345
```

![image-20211104111157597](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/990d1a541db22457af3e8008c14a57c6.png)

### 带超时等待

```java
ThreadPool threadPool = new ThreadPool(1,1000, TimeUnit.MILLISECONDS, 1, 
                                       (queue, task)->{
            // 2) 带超时等待
            queue.offer(task, 1500, TimeUnit.MILLISECONDS);
                                       });
12345
```

![image-20211104111452465](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/2782b50938ac85fa79b9b8842d59ef86.png)

### 让调用者放弃任务执行

```java
ThreadPool threadPool = new ThreadPool(1,1000, TimeUnit.MILLISECONDS, 1, 
                                       (queue, task)->{
            // 3) 让调用者放弃任务执行
            log.debug("放弃{}", task);
                                       });
12345
```

![image-20211104111743983](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/854a1e0f37c09a1344a7116b7b04b720.png)

放弃执行打印2、3的任务

### 让调用者抛出异常

```java
ThreadPool threadPool = new ThreadPool(1,1000, TimeUnit.MILLISECONDS, 1, 
                                       (queue, task)->{
            // 4) 让调用者抛出异常
            throw new RuntimeException("任务执行失败 " + task);
                                       });
12345
```

![image-20211104111918854](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/3f0bf09871eeccb4e3ceffbe572979f8.png)

主线程在分配第3个任务（打印2）的时候，等待队列满了，选择直接抛出异常，导致主线程停止，不能分配第4个任务（打印3）。但是其他线程不受影响，继续执行。

### 让调用者自己执行任务

```java
ThreadPool threadPool = new ThreadPool(1,1000, TimeUnit.MILLISECONDS, 1, 
                                       (queue, task)->{
            // 5) 让调用者自己执行任务
            task.run();
                                       });
12345
```

![image-20211104112208154](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/8e9af315e1fe556f9441172ab1dc56bc.png)

# ThreadPoolExecutor

## 线程池状态

ThreadPoolExecutor 使用 int 的高 3 位来表示线程池状态，低 29 位表示线程数量

![image-20211104113012296](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/326603390e7d92be5b45c3bb4eb448be.png)

从数字上比较，TERMINATED > TIDYING > STOP > SHUTDOWN > RUNNING

这些信息存储在一个原子变量 ctl 中，目的是将线程池状态与线程个数合二为一，这样就可以用一次 cas 原子操作进行赋值

```java
// c 为旧值， ctlOf 返回结果为新值
ctl.compareAndSet(c, ctlOf(targetState, workerCountOf(c))));
// rs 为高 3 位代表线程池状态， wc 为低 29 位代表线程个数，ctl 是合并它们
private static int ctlOf(int rs, int wc) { return rs | wc; }
1234
```

## 构造方法

### ThreadPoolExecutor

```java
public ThreadPoolExecutor(int corePoolSize,
                          int maximumPoolSize,
                          long keepAliveTime,
                          TimeUnit unit,
                          BlockingQueue<Runnable> workQueue,
                          ThreadFactory threadFactory,
                          RejectedExecutionHandler handler)
```

- corePoolSize 核心线程数目 (最多保留的线程数)
- maximumPoolSize 最大线程数目
- keepAliveTime 生存时间 - 针对救急线程
- unit 时间单位 - 针对救急线程
- workQueue 阻塞队列
- threadFactory 线程工厂 - 可以为线程创建时起个好名字
- handler 拒绝策略

工作方式：

![image-20211104114529784](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/eca587ccbf6b1d31938f3db307ff37d3.png)

- 线程池中刚开始没有线程，当一个任务提交给线程池后，线程池会创建一个新线程来执行任务。
- 当线程数达到 corePoolSize 并没有线程空闲，这时再加入任务，新加的任务会被加入workQueue 队列排队，直到有空闲的线程。
- 如果队列选择了有界队列，那么任务超过了队列大小时，会创建 maximumPoolSize - corePoolSize 数目的线程来救急。
- 如果线程到达 maximumPoolSize 仍然有新任务这时会执行拒绝策略。拒绝策略 jdk 提供了 4 种实现，其它著名框架也提供了实现
  - AbortPolicy 让调用者抛出 RejectedExecutionException 异常，这是默认策略
  - CallerRunsPolicy 让调用者运行任务
  - DiscardPolicy 放弃本次任务
  - DiscardOldestPolicy 放弃队列中最早的任务，本任务取而代之
  - Dubbo 的实现，在抛出 RejectedExecutionException 异常之前会记录日志，并 dump 线程栈信息，方便定位问题
  - Netty 的实现，是创建一个新线程来执行任务
  - ActiveMQ 的实现，带超时等待（60s）尝试放入队列，类似我们之前自定义的拒绝策略
  - PinPoint 的实现，它使用了一个拒绝策略链，会逐一尝试策略链中每种拒绝策略
- 当高峰过去后，超过corePoolSize 的救急线程如果一段时间没有任务做，需要结束节省资源，这个时间由keepAliveTime 和 unit 来控制。

![image-20211104114616642](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/2370e36753509be9eb8bef750e383366.png)

根据这个构造方法，JDK Executors 类中提供了众多工厂方法来创建各种用途的线程池

### newFixedThreadPool

```java
public static ExecutorService newFixedThreadPool(int nThreads) {
    return new ThreadPoolExecutor(nThreads, nThreads,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());
}
12345
```

特点

- 核心线程数 == 最大线程数（没有救急线程被创建），因此也无需超时时间
- 阻塞队列是无界的，可以放任意数量的任务

**评价** 适用于任务量已知，相对耗时的任务

可以多加一个参数，传入一个ThreadFactory，可以自定义名称

```java
private static void test1() {
    ExecutorService pool = Executors.newFixedThreadPool(2, new ThreadFactory() {
        private AtomicInteger t = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "mypool_t" + t.getAndIncrement());
        }
    });

    pool.execute(() -> {
        log.debug("1");
    });

    pool.execute(() -> {
        log.debug("2");
    });

    pool.execute(() -> {
        log.debug("3");
    });
}
```

输出

![image-20211104160142431](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211104160142431.png)

- 线程池大小为2，3个任务，t2线程执行完2，就去执行3了
- 创建出的线程默认都为非守护线程，main线程执行完也没有结束

### newCachedThreadPool

```java
public static ExecutorService newCachedThreadPool() {
    return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
            60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>());
}
12345
```

特点

- 核心线程数是 0， 最大线程数是 Integer.MAX_VALUE，救急线程的空闲生存时间是 60s，意味着
  - 全部都是救急线程（60s 后可以回收）
  - 救急线程可以无限创建
- 队列采用了 SynchronousQueue 实现特点是，它没有容量，没有线程来取是放不进去的（一手交钱、一手交货）

#### SynchronousQueue演示

```java
@Slf4j(topic = "c.TestSynchronousQueue")
public class TestSynchronousQueue {
    public static void main(String[] args) {
        SynchronousQueue<Integer> integers = new SynchronousQueue<>();
        new Thread(() -> {
            try {
                log.debug("putting {} ", 1);
                integers.put(1);
                log.debug("{} putted...", 1);

                log.debug("putting...{} ", 2);
                integers.put(2);
                log.debug("{} putted...", 2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t1").start();

        sleep(1);

        new Thread(() -> {
            try {
                log.debug("taking {}", 1);
                integers.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t2").start();

        sleep(1);

        new Thread(() -> {
            try {
                log.debug("taking {}", 2);
                integers.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t3").start();
    }
}
```

输出

![image-20211104161310231](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211104161310231.png)

- 用newCachedThreadPool()创建出线程池，其阻塞队列的实现是SynchronousQueue。
- 线程池初始的最大线程数是Integer的最大值，但是全是救急线程，且线程是懒惰初始化的（即一开始不会真的全部创建出来，但是用到了就能创建这么多）
- 然后阻塞队列的容量为空，没有线程来取就存放不进去，起到了一个缓冲作用，根本也无需阻塞，因为救急线程相当于是没有上限的，很快就能来把你这个线程取走。
- 等到线程池里的线程任务执行完成后，空闲1分钟就会释放之前创建的线程。

**评价** 整个线程池表现为线程数会根据任务量不断增长，没有上限，当任务执行完毕，空闲 1分钟后释放线程。 适合任务数比较密集，但每个任务执行时间较短的情况。时间较长的话，任务一来就会创建救急线程，然后执行时间又长，有新的任务来，又不能加入阻塞队列（容量为0），导致创建更多的救急线程。

### newSingleThreadExecutor

```java
public static void test2() {
    ExecutorService pool = Executors.newSingleThreadExecutor();
    pool.execute(() -> {
        log.debug("1");
        int i = 1 / 0;
    });

    pool.execute(() -> {
        log.debug("2");
    });

    pool.execute(() -> {
        log.debug("3");
    });
}
```

输出

![image-20211104162643634](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/980e44c5f1bbd8af44a07b971ba5585c.png)

线程1挂掉了，有新建了个线程2来执行任务，始终保证线程池中有一个可用的线程。

使用场景：

希望多个任务排队执行。线程数固定为 1，任务数多于 1 时，会放入无界队列排队。任务执行完毕，这唯一的线程也不会被释放。

区别：

- 自己创建一个单线程串行执行任务，如果任务执行失败而终止那么没有任何补救措施，而线程池还会新建一个线程，保证池的正常工作
- Executors.newSingleThreadExecutor() 线程个数始终为1，不能修改
  - FinalizableDelegatedExecutorService 应用的是装饰器模式，只对外暴露了 ExecutorService 接口，因此不能调用 ThreadPoolExecutor 中特有的方法
- Executors.newFixedThreadPool(1) 初始时为1，以后还可以修改
  - 对外暴露的是 ThreadPoolExecutor 对象，可以强转后调用 setCorePoolSize 等方法进行修改

## 提交任务

```java
// 执行任务
void execute(Runnable command);

// 提交任务 task，用返回值 Future 获得任务执行结果
<T> Future<T> submit(Callable<T> task);

// 提交 tasks 中所有任务
<T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
        throws InterruptedException;

// 提交 tasks 中所有任务，带超时时间
<T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks,
                              long timeout, TimeUnit unit)
        throws InterruptedException;

// 提交 tasks 中所有任务，哪个任务先成功执行完毕，返回此任务执行结果，其它任务取消
<T> T invokeAny(Collection<? extends Callable<T>> tasks)

  // 提交 tasks 中所有任务，哪个任务先成功执行完毕，返回此任务执行结果，其它任务取消，带超时时间
<T> T invokeAny(Collection<? extends Callable<T>> tasks,
                long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException;
12345678910111213141516171819202122
```

### submit

```java
private static void method1(ExecutorService pool) throws InterruptedException, ExecutionException {
    Future<String> future = pool.submit(() -> {
        log.debug("running");
        Thread.sleep(1000);
        return "ok";
    });

    log.debug("{}", future.get());
}
123456789
```

![image-20211104164906397](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211104164906397.png)

### invokeAll

```java
private static void method2(ExecutorService pool) throws InterruptedException {
    List<Future<String>> futures = pool.invokeAll(Arrays.asList(
            () -> {
                log.debug("begin");
                Thread.sleep(1000);
                return "1";
            },
            () -> {
                log.debug("begin");
                Thread.sleep(500);
                return "2";
            },
            () -> {
                log.debug("begin");
                Thread.sleep(2000);
                return "3";
            }
    ));
123456789101112131415161718
```

![image-20211104165019869](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211104165019869.png)

### invokeAny

```java
private static void method3(ExecutorService pool) throws InterruptedException, ExecutionException {
    String result = pool.invokeAny(Arrays.asList(
            () -> {
                log.debug("begin 1");
                Thread.sleep(1000);
                log.debug("end 1");
                return "1";
            },
            () -> {
                log.debug("begin 2");
                Thread.sleep(500);
                log.debug("end 2");
                return "2";
            },
            () -> {
                log.debug("begin 3");
                Thread.sleep(2000);
                log.debug("end 3");
                return "3";
            }
    ));
    log.debug("{}", result);
}
1234567891011121314151617181920212223
```

![image-20211104165104597](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211104165104597.png)

## 关闭线程池

### shutdown

```java
/*
线程池状态变为 SHUTDOWN
- 不会接收新任务
- 但已提交任务会执行完
- 此方法不会阻塞调用线程的执行
*/
void shutdown();
1234567
```

源码

```java
public void shutdown() {
    final ReentrantLock mainLock = this.mainLock;
    mainLock.lock();
    try {
        checkShutdownAccess();
      // 修改线程池状态
        advanceRunState(SHUTDOWN);
      // 仅会打断空闲线程
        interruptIdleWorkers();
       // 扩展点 ScheduledThreadPoolExecutor
        onShutdown(); // hook for ScheduledThreadPoolExecutor
    } finally {
        mainLock.unlock();
    }
  // 尝试终结(没有运行的线程可以立刻终结，如果还有运行的线程也不会等)
    tryTerminate();
}
1234567891011121314151617
```

### shutdownNow

```java
/*
线程池状态变为 STOP
- 不会接收新任务
- 会将队列中的任务返回
- 并用 interrupt 的方式中断正在执行的任务
*/
List<Runnable> shutdownNow();
1234567
```

源码

```java
public List<Runnable> shutdownNow() {
    List<Runnable> tasks;
    final ReentrantLock mainLock = this.mainLock;
    mainLock.lock();
    try {
        checkShutdownAccess();
      // 修改线程池状态
        advanceRunState(STOP);
      // 打断所有线程
        interruptWorkers();
      // 获取队列中剩余任务
        tasks = drainQueue();
    } finally {
        mainLock.unlock();
    }
  // 尝试终结
    tryTerminate();
    return tasks;
}
12345678910111213141516171819
```

### 其它方法

```java
// 不在 RUNNING 状态的线程池，此方法就返回 true
boolean isShutdown();

// 线程池状态是否是 TERMINATED
boolean isTerminated();

// 调用 shutdown 后，由于调用线程并不会等待所有任务运行结束，因此如果它想在线程池 TERMINATED 后做些事情，可以利用此方法等待
boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException;
12345678
```

### 运行示例

```java
@Slf4j(topic = "c.TestShutDown")
public class TestShutDown {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        Future<Integer> result1 = pool.submit(() -> {
            log.debug("task 1 running...");
            Thread.sleep(1000);
            log.debug("task 1 finish...");
            return 1;
        });

        Future<Integer> result2 = pool.submit(() -> {
            log.debug("task 2 running...");
            Thread.sleep(1000);
            log.debug("task 2 finish...");
            return 2;
        });

        Future<Integer> result3 = pool.submit(() -> {
            log.debug("task 3 running...");
            Thread.sleep(1000);
            log.debug("task 3 finish...");
            return 3;
        });

        log.debug("shutdown");
        pool.shutdown();
        pool.awaitTermination(3, TimeUnit.SECONDS);
//        List<Runnable> runnables = pool.shutdownNow();
        log.debug("other.... {}");
    }
}
12345678910111213141516171819202122232425262728293031323334
```

shutdown输出

![image-20211104172315384](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211104172315384.png)

shotdownNow输出

![image-20211104172440110](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/5f2f3f3481c4d3d5d2677fb61299591d.png)

## 任务调度线程池

### Timer

在『任务调度线程池』功能加入之前，可以使用 java.util.Timer 来实现定时功能，Timer 的优点在于简单易用，但由于所有任务都是由同一个线程来调度，因此所有任务都是串行执行的，同一时间只能有一个任务在执行，前一个任务的延迟或异常都将会影响到之后的任务。

```java
private static void method1() {
    Timer timer = new Timer();
    TimerTask task1 = new TimerTask() {
        @Override
        public void run() {
            log.debug("task 1");
            sleep(2);
        }
    };
    TimerTask task2 = new TimerTask() {
        @Override
        public void run() {
            log.debug("task 2");
        }
    };

    log.debug("start...");
  // 使用 timer 添加两个任务，希望它们都在 1s 后执行
	// 但由于 timer 内只有一个线程来顺序执行队列中的任务，因此『任务1』的延时，影响了『任务2』的执行
    timer.schedule(task1, 1000);
    timer.schedule(task2, 1000);
}
12345678910111213141516171819202122
```

输出

![image-20211105090542180](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/e3142205c25e8b408bf532316140243a.png)

### ScheduledExecutorService

使用 ScheduledExecutorService 改写：

```java
ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
// 添加两个任务，希望它们都在 1s 后执行
        executor.schedule(() -> {
            System.out.println("任务1，执行时间：" + new Date());
            try { Thread.sleep(2000); } catch (InterruptedException e) { }
        }, 1000, TimeUnit.MILLISECONDS);
        executor.schedule(() -> {
            System.out.println("任务2，执行时间：" + new Date());
        }, 1000, TimeUnit.MILLISECONDS);
123456789
```

输出

![image-20211105091220394](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/b700bfc28a4aa26870505ea66f73eca1.png)

- 两个线程执行互不干扰
- 当然，如果线程池大小为1，任务之间仍然是串行执行

要让任务隔一段时间就执行一次，可以调用scheduleAtFixedRate()、scheduleWithFixedDelay()

#### scheduleAtFixedRate

```java
private static void method3() {
    ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
    log.debug("start...");
    pool.scheduleAtFixedRate(() -> {
        log.debug("running...");
    }, 1, 1, TimeUnit.SECONDS);
}
1234567
```

输出

![image-20211105092519142](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/d236499924785e8cb398de63cb68e997.png)

如果任务执行时间超过了间隔时间

```java
private static void method3() {
    ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
    log.debug("start...");
    pool.scheduleAtFixedRate(() -> {
        log.debug("running...");
        sleep(2);
    }, 1, 1, TimeUnit.SECONDS);
}
12345678
```

![image-20211105092721298](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/fc53ebfa4c013eebf3b36d519b68d928.png)

输出分析：一开始，延时 1s，接下来，由于任务执行时间 > 间隔时间，间隔被『撑』到了 2s

#### scheduleWithFixedDelay

```java
private static void method3() {
    ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
    log.debug("start...");
    pool.scheduleWithFixedDelay(() -> {
        log.debug("running...");
        sleep(2);
    }, 1, 1, TimeUnit.SECONDS);
}
12345678
```

![image-20211105092911572](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/6d509bb4c6d007a73d923ec758257544.png)

输出分析：一开始，延时 1s，scheduleWithFixedDelay 的间隔是 上一个任务结束 <-> 延时 <-> 下一个任务开始 所以间隔都是 3s

> **评价** 整个线程池表现为：线程数固定，任务数多于线程数时，会放入无界队列排队。任务执行完毕，这些线程也不会被释放。用来执行延迟或反复执行的任务

## 正确处理执行任务异常

### 方法1：主动捉异常

```java
ExecutorService pool = Executors.newFixedThreadPool(1);
pool.submit(() -> {
    try {
        log.debug("task1");
        int i = 1 / 0;
    } catch (Exception e) {
        log.error("error:", e);
    }
});
123456789
```

![image-20211105093604986](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/9a30a5dbce092785407ee8c8f07c9ff1.png)

### 方法2：使用 Future

```java
ExecutorService pool = Executors.newFixedThreadPool(1);
Future<Boolean> future = pool.submit(() -> {
    log.debug("task1");
    int i = 1 / 0;
    return true;
});
log.debug("result:{}", future.get());
1234567
```

![image-20211105094224046](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/941a0658fd4234777becbd84c14f9dd8.png)

- 使用Future接收返回值，如果正常则接受Callable接口返回值
- 有异常的话，就会捕捉异常接收

# Tomcat 线程池

Tomcat 在哪里用到了线程池呢

![image-20211105100037497](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/f461f865f767c31987c484673b091461.png)

- LimitLatch 用来限流，可以控制最大连接个数，类似 J.U.C 中的 Semaphore 后面再讲
- Acceptor 只负责【接收新的 socket 连接】
- Poller 只负责监听 socket channel 是否有【可读的 I/O 事件】
- 一旦可读，封装一个任务对象（socketProcessor），提交给 Executor 线程池处理
- Executor 线程池中的工作线程最终负责【处理请求】

Tomcat 线程池扩展了 ThreadPoolExecutor，行为稍有不同

- 如果总线程数达到 maximumPoolSize
  - 这时不会立刻抛 RejectedExecutionException 异常
  - 而是再次尝试将任务放入队列，如果还失败，才抛出 RejectedExecutionException 异常

源码 tomcat-7.0.42

```java
public void execute(Runnable command, long timeout, TimeUnit unit) {
    submittedCount.incrementAndGet();
    try {
        super.execute(command);
    } catch (RejectedExecutionException rx) {
        if (super.getQueue() instanceof TaskQueue) {
            final TaskQueue queue = (TaskQueue)super.getQueue();
            try {
                if (!queue.force(command, timeout, unit)) {
                    submittedCount.decrementAndGet();
                    throw new RejectedExecutionException("Queue capacity is full.");
                }
            } catch (InterruptedException x) {
                submittedCount.decrementAndGet();
                Thread.interrupted();
                throw new RejectedExecutionException(x);
            }
        } else {
            submittedCount.decrementAndGet();
            throw rx;
        }
    }
}
1234567891011121314151617181920212223
```

TaskQueue.java

```java
public boolean force(Runnable o, long timeout, TimeUnit unit) throws InterruptedException {
    if ( parent.isShutdown() )
        throw new RejectedExecutionException(
                "Executor not running, can't force a command into the queue"
        );
    return super.offer(o,timeout,unit); //forces the item onto the queue, to be used if the task 
    is rejected
}
12345678
```

Connector 配置

![image-20211105100842038](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/2ae550f9b7d86392ac6664f7a4eaef75.png)

Executor 线程配置

![image-20211105100855107](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/7ec5ecad60e6d208d0e80d766e8bff38.png)

![image-20211105100905126](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/dc9de78e0caa2bb815ecba0f5513e902.png)

# Fork/Join

## 概念

Fork/Join 是 JDK 1.7 加入的新的线程池实现，它体现的是一种分治思想，适用于能够进行任务拆分的 cpu 密集型运算

所谓的任务拆分，是将一个大任务拆分为算法上相同的小任务，直至不能拆分可以直接求解。跟递归相关的一些计算，如归并排序、斐波那契数列、都可以用分治思想进行求解

Fork/Join 在分治的基础上加入了多线程，可以把每个任务的分解和合并交给不同的线程来完成，进一步提升了运算效率

Fork/Join 默认会创建与 cpu 核心数大小相同的线程池

## 使用

提交给 Fork/Join 线程池的任务需要继承 RecursiveTask（有返回值）或 RecursiveAction（没有返回值），例如下面定义了一个对 1~n 之间的整数求和的任务

```java
@Slf4j(topic = "c.AddTask")
class AddTask1 extends RecursiveTask<Integer> {
    int n;
    public AddTask1(int n) {
        this.n = n;
    }
    @Override
    public String toString() {
        return "{" + n + '}';
    }
    @Override
    protected Integer compute() {
// 如果 n 已经为 1，可以求得结果了
        if (n == 1) {
            log.debug("join() {}", n);
            return n;
        }
// 将任务进行拆分(fork)
        AddTask1 t1 = new AddTask1(n - 1);
        t1.fork();
        log.debug("fork() {} + {}", n, t1);
// 合并(join)结果
        int result = n + t1.join();
        log.debug("join() {} + {} = {}", n, t1, result);
        return result;
    }
}
123456789101112131415161718192021222324252627
```

然后提交给 ForkJoinPool 来执行

```java
public static void main(String[] args) {
  ForkJoinPool pool = new ForkJoinPool(4);
  System.out.println(pool.invoke(new AddTask1(5)));
}
1234
```

输出

![image-20211105103438755](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/d1f1fa8632c768f030efa4da0888524d.png)

用图来表示

![image-20211105103456288](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/a1ed6e1d3bd8bf07be1186f18e7dc622.png)

改进

```java
@Slf4j(topic = "c.AddTask")
class AddTask3 extends RecursiveTask<Integer> {

    int begin;
    int end;

    public AddTask3(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    public String toString() {
        return "{" + begin + "," + end + '}';
    }

    @Override
    protected Integer compute() {
        if (begin == end) {
            log.debug("join() {}", begin);
            return begin;
        }
        if (end - begin == 1) {
            log.debug("join() {} + {} = {}", begin, end, end + begin);
            return end + begin;
        }
        int mid = (end + begin) / 2;

        AddTask3 t1 = new AddTask3(begin, mid);
        t1.fork();
        AddTask3 t2 = new AddTask3(mid + 1, end);
        t2.fork();
        log.debug("fork() {} + {} = ?", t1, t2);

        int result = t1.join() + t2.join();
        log.debug("join() {} + {} = {}", t1, t2, result);
        return result;
    }
}
123456789101112131415161718192021222324252627282930313233343536373839
```

然后提交给 ForkJoinPool 来执行

```java
public static void main(String[] args) {
  ForkJoinPool pool = new ForkJoinPool(4);
  System.out.println(pool.invoke(new AddTask3(1, 10)));
}
1234
```

结果

![image-20211105103947844](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/8edc4205fa27d81e2b38eb0749755508.png)

用图来表示

![image-20211105104004632](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/076fe35a84bc53073d17220139634bea.png)

# 应用之定时任务

如何让每周四 18:00:00 定时执行任务？

```java
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestSchedule {

    // 如何让每周四 18:00:00 定时执行任务？
    public static void main(String[] args) {
        //  获取当前时间
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        // 获取周四时间
        LocalDateTime time = now.withHour(18).withMinute(0).withSecond(0).withNano(0).with(DayOfWeek.THURSDAY);
        // 如果 当前时间 > 本周周四，必须找到下周周四
        if(now.compareTo(time) > 0) {
            time = time.plusWeeks(1);
        }
        System.out.println(time);
        // initailDelay 代表当前时间和周四的时间差
        // period 一周的间隔时间
        long initailDelay = Duration.between(now, time).toMillis();
        long period = 1000 * 60 * 60 * 24 * 7;
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
        pool.scheduleAtFixedRate(() -> {
            System.out.println("running...");
        }, initailDelay, period, TimeUnit.MILLISECONDS);
    }
}
12345678910111213141516171819202122232425262728293031
```

# 模式之 Worker Thread

## 定义

让有限的工作线程（Worker Thread）来轮流异步处理无限多的任务。也可以将其归类为分工模式，它的典型实现就是线程池，也体现了经典设计模式中的享元模式。

例如，海底捞的服务员（线程），轮流处理每位客人的点餐（任务），如果为每位客人都配一名专属的服务员，那么成本就太高了（对比另一种多线程设计模式：Thread-Per-Message）

注意，不同任务类型应该使用不同的线程池，这样能够避免饥饿，并能提升效率

例如，如果一个餐馆的工人既要招呼客人（任务类型A），又要到后厨做菜（任务类型B）显然效率不咋地，分成服务员（线程池A）与厨师（线程池B）更为合理，当然你能想到更细致的分工

## 饥饿

固定大小线程池会有饥饿现象

- 两个工人是同一个线程池中的两个线程
- 他们要做的事情是：为客人点餐和到后厨做菜，这是两个阶段的工作
  - 客人点餐：必须先点完餐，等菜做好，上菜，在此期间处理点餐的工人必须等待
  - 后厨做菜：没啥说的，做就是了
- 比如工人A 处理了点餐任务，接下来它要等着 工人B 把菜做好，然后上菜，他俩也配合的蛮好
- 但现在同时来了两个客人，这个时候工人A 和工人B 都去处理点餐了，这时没人做饭了，饥饿

### 饥饿实例

```java
public class TestDeadLock {
    static final List<String> MENU = Arrays.asList("地三鲜", "宫保鸡丁", "辣子鸡丁", "烤鸡翅");
    static Random RANDOM = new Random();

    static String cooking() {
        return MENU.get(RANDOM.nextInt(MENU.size()));
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(() -> {
            log.debug("处理点餐...");
            Future<String> f = executorService.submit(() -> {
                log.debug("做菜");
                return cooking();
            });
            try {
                log.debug("上菜: {}", f.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
//        executorService.execute(() -> {
//            log.debug("处理点餐...");
//            Future<String> f = executorService.submit(() -> {
//                log.debug("做菜");
//                return cooking();
//            });
//            try {
//                log.debug("上菜: {}", f.get());
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
//        });
    }
}
```

输出

![image-20211104174406861](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/df4f56b1977768f35f3dffa87a52e25b.png)

当注释取消后，可能的输出

![image-20211104174424045](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/c9e2d63211919c88cb2c508b67620c64.png)

### 饥饿解决

解决方法可以增加线程池的大小，不过不是根本解决方案，还是前面提到的，不同的任务类型，采用不同的线程池，例如：

```java
@Slf4j(topic = "c.TestStarvation")
public class TestStarvation {

    static final List<String> MENU = Arrays.asList("地三鲜", "宫保鸡丁", "辣子鸡丁", "烤鸡翅");
    static Random RANDOM = new Random();

    static String cooking() {
        return MENU.get(RANDOM.nextInt(MENU.size()));
    }

    public static void main(String[] args) {
        ExecutorService waiterPool = Executors.newFixedThreadPool(1);
        ExecutorService cookPool = Executors.newFixedThreadPool(1);

        waiterPool.execute(() -> {
            log.debug("处理点餐...");
            Future<String> f = cookPool.submit(() -> {
                log.debug("做菜");
                return cooking();
            });
            try {
                log.debug("上菜: {}", f.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        waiterPool.execute(() -> {
            log.debug("处理点餐...");
            Future<String> f = cookPool.submit(() -> {
                log.debug("做菜");
                return cooking();
            });
            try {
                log.debug("上菜: {}", f.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

    }
}
```

![image-20211104174813885](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/414f1c8673b2c0f4fb80379b45fe32d4.png)

## 创建多少线程池合适

- 过小会导致程序不能充分地利用系统资源、容易导致饥饿
- 过大会导致更多的线程上下文切换，占用更多内存

### CPU 密集型运算

通常采用 cpu 核数 + 1 能够实现最优的 CPU 利用率，+1 是保证当线程由于页缺失故障（操作系统）或其它原因导致暂停时，额外的这个线程就能顶上去，保证 CPU 时钟周期不被浪费

### I/O 密集型运算

CPU 不总是处于繁忙状态，例如，当你执行业务计算时，这时候会使用 CPU 资源，但当你执行 I/O 操作时、远程RPC 调用时，包括进行数据库操作时，这时候 CPU 就闲下来了，你可以利用多线程提高它的利用率。

经验公式如下

```java
线程数 = 核数 * 期望 CPU 利用率 * 总时间(CPU计算时间+等待时间) / CPU 计算时间
```

例如 4 核 CPU 计算时间是 50% ，其它等待时间是 50%，期望 cpu 被 100% 利用，套用公式

```java
4 * 100% * 100% / 50% = 8
```

例如 4 核 CPU 计算时间是 10% ，其它等待时间是 90%，期望 cpu 被 100% 利用，套用公式

```java
4 * 100% * 100% / 10% = 40
```

## 自定义线程池

在文章一开始已经实现。