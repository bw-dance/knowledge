### 共享模型之JUC工具包

- [Semaphore](#Semaphore_2)
- - [基本使用](#_4)
  - [Semaphore 应用](#Semaphore__42)
  - [Semaphore 原理](#Semaphore__111)
  - - [加锁解锁流程](#_113)
    - [源码分析](#_135)
- [CountdownLatch](#CountdownLatch_243)
- - [基本使用](#_245)
  - [应用之同步等待多线程准备完毕](#_332)
  - [应用之同步等待多个远程调用结束](#_373)
- [CyclicBarrier](#CyclicBarrier_462)



# [Semaphore](https://so.csdn.net/so/search?q=Semaphore&spm=1001.2101.3001.7020)

## 基本使用

[?s?m??f?r] 信号量，用来限制能同时访问共享资源的线程上限。

```java
@Slf4j(topic = "c.TestSemaphore")
public class TestSemaphore {
    public static void main(String[] args) {
        // 1. 创建 semaphore 对象
        Semaphore semaphore = new Semaphore(3);

        // 2. 10个线程同时运行
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    log.debug("running...");
                    sleep(1);
                    log.debug("end...");
                } finally {
                    semaphore.release();
                }
            }).start();
        }
    }
}
12345678910111213141516171819202122232425
```

输出，最多3个线程同时运行

![image-20211108151013285](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/5e95f3d77c32b88c18055475d0cfa3b8.png)

## Semaphore 应用

- 限制对共享资源的使用
- 使用 Semaphore 限流，在访问高峰期时，让请求线程阻塞，高峰期过去再释放许可，当然它只适合限制单机线程数量，并且仅是限制线程数，而不是限制资源数（例如连接数，请对比 Tomcat LimitLatch 的实现）
- 用 Semaphore 实现简单连接池，对比『享元模式』下的实现（用wait notify），性能和可读性显然更好，注意下面的实现中线程数和数据库连接数是相等的。且这里也是一个线程对应一个资源，适用于Semaphore。

```java
@Slf4j(topic = "c.Pool")
class Pool {
    // 1. 连接池大小
    private final int poolSize;

    // 2. 连接对象数组
    private Connection[] connections;

    // 3. 连接状态数组 0 表示空闲， 1 表示繁忙
    private AtomicIntegerArray states;

    private Semaphore semaphore;

    // 4. 构造方法初始化
    public Pool(int poolSize) {
        this.poolSize = poolSize;
        // 让许可数与资源数一致
        this.semaphore = new Semaphore(poolSize);
        this.connections = new Connection[poolSize];
        this.states = new AtomicIntegerArray(new int[poolSize]);
        for (int i = 0; i < poolSize; i++) {
            connections[i] = new MockConnection("连接" + (i+1));
        }
    }

    // 5. 借连接
    public Connection borrow() {// t1, t2, t3
        // 获取许可
        try {
            semaphore.acquire(); // 没有许可的线程，在此等待
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < poolSize; i++) {
            // 获取空闲连接
            if(states.get(i) == 0) {
                if (states.compareAndSet(i, 0, 1)) {
                    log.debug("borrow {}", connections[i]);
                    return connections[i];
                }
            }
        }
        // 不会执行到这里
        return null;
    }
    // 6. 归还连接
    public void free(Connection conn) {
        for (int i = 0; i < poolSize; i++) {
            if (connections[i] == conn) {
                states.set(i, 0);
                log.debug("free {}", conn);
                semaphore.release();
                break;
            }
        }
    }
}
123456789101112131415161718192021222324252627282930313233343536373839404142434445464748495051525354555657
```

## Semaphore 原理

### 加锁解锁流程

Semaphore 有点像一个停车场，permits 就好像停车位数量，当线程获得了 permits 就像是获得了停车位，然后停车场显示空余车位减一

刚开始，permits（state）为 3，这时 5 个线程来获取资源

![image-20211108152940929](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/8d358dcc845e6b81c21aa699638a7fd6.png)

假设其中 Thread-1，Thread-2，Thread-4 cas 竞争成功，而 Thread-0 和 Thread-3 竞争失败，进入 AQS 队列park 阻塞

![image-20211108153005901](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/6d7d7173b527bd37f39fd911ccbfc475.png)

这时 Thread-4 释放了 permits，状态如下

![image-20211108153021787](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/5214b0c6842966e5485418e67067e0d3.png)

接下来 Thread-0 竞争成功，permits 再次设置为 0，设置自己为 head 节点，断开原来的 head 节点，unpark 接下来的 Thread-3 节点，但由于 permits 是 0，因此 Thread-3 在尝试不成功后再次进入 park 状态

![image-20211108153046099](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/fbd5b83fba1d064036f931db921413ed.png)

### 源码分析

```java
static final class NonfairSync extends Sync {
    private static final long serialVersionUID = -2694183684443567898L;

    NonfairSync(int permits) {
// permits 即 state
        super(permits);
    }

    // Semaphore 方法, 方便阅读, 放在此处
    public void acquire() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    // AQS 继承过来的方法, 方便阅读, 放在此处
    public final void acquireSharedInterruptibly(int arg)
            throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        if (tryAcquireShared(arg) < 0)
            doAcquireSharedInterruptibly(arg);
    }

    // 尝试获得共享锁
    protected int tryAcquireShared(int acquires) {
        return nonfairTryAcquireShared(acquires);
    }

    // Sync 继承过来的方法, 方便阅读, 放在此处
    final int nonfairTryAcquireShared(int acquires) {
        for (; ; ) {
            int available = getState();
            int remaining = available - acquires;
            if (
// 如果许可已经用完, 返回负数, 表示获取失败, 进入 doAcquireSharedInterruptibly
                    remaining < 0 ||
// 如果 cas 重试成功, 返回正数, 表示获取成功
                            compareAndSetState(available, remaining)
            ) {
                return remaining;
            }
        }
    }

    // AQS 继承过来的方法, 方便阅读, 放在此处
    private void doAcquireSharedInterruptibly(int arg) throws InterruptedException {
        final Node node = addWaiter(Node.SHARED);
        boolean failed = true;
        try {
            for (; ; ) {
                final Node p = node.predecessor();
                if (p == head) {
// 再次尝试获取许可
                    int r = tryAcquireShared(arg);
                    if (r >= 0) {
// 成功后本线程出队（AQS）, 所在 Node设置为 head
// 如果 head.waitStatus == Node.SIGNAL ==> 0 成功, 下一个节点 unpark
// 如果 head.waitStatus == 0 ==> Node.PROPAGATE
// r 表示可用资源数, 为 0 则不会继续传播
                        setHeadAndPropagate(node, r);
                        p.next = null; // help GC
                        failed = false;
                        return;
                    }
                }
// 不成功, 设置上一个节点 waitStatus = Node.SIGNAL, 下轮进入 park 阻塞
                if (shouldParkAfterFailedAcquire(p, node) &&
                        parkAndCheckInterrupt())
                    throw new InterruptedException();
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }

    // Semaphore 方法, 方便阅读, 放在此处
    public void release() {
        sync.releaseShared(1);
    }

    // AQS 继承过来的方法, 方便阅读, 放在此处
    public final boolean releaseShared(int arg) {
        if (tryReleaseShared(arg)) {
            doReleaseShared();
            return true;
        }
        return false;
    }

    // Sync 继承过来的方法, 方便阅读, 放在此处
    protected final boolean tryReleaseShared(int releases) {
        for (; ; ) {
            int current = getState();
            int next = current + releases;
            if (next < current) // overflow
                throw new Error("Maximum permit count exceeded");
            if (compareAndSetState(current, next))
                return true;
        }
    }
}
123456789101112131415161718192021222324252627282930313233343536373839404142434445464748495051525354555657585960616263646566676869707172737475767778798081828384858687888990919293949596979899100101
```

# [CountdownLatch](https://so.csdn.net/so/search?q=CountdownLatch&spm=1001.2101.3001.7020)

## 基本使用

用来进行线程同步协作，等待所有线程完成倒计时。

其中构造参数用来初始化等待计数值，await() 用来等待计数归零，countDown() 用来让计数减一

```java
private static void test4() throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(3);

    new Thread(() -> {
        log.debug("begin...");
        sleep(1);
        latch.countDown();
        log.debug("end...{}", latch.getCount());
    }).start();

    new Thread(() -> {
        log.debug("begin...");
        sleep(2);
        latch.countDown();
        log.debug("end...{}", latch.getCount());
    }).start();

    new Thread(() -> {
        log.debug("begin...");
        sleep(1.5);
        latch.countDown();
        log.debug("end...{}", latch.getCount());
    }).start();

    log.debug("waiting...");
    latch.await();
    log.debug("wait end...");
}
12345678910111213141516171819202122232425262728
```

输出

![image-20211108155108650](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/b500a01a839ad6ad0641f64101c3c845.png)

可以配合线程池使用，改进如下

线程池线程保证线程一直活跃，要实现让一个线程等待其他线程执行完，然后做汇总。这里不适合用join，join去等线程结束，这里只需要等任务结束，线程回归线程池不必结束。用CountdownLatch较为合适。

```java
private static void test5() {
    CountDownLatch latch = new CountDownLatch(3);
    ExecutorService service = Executors.newFixedThreadPool(4);
    service.submit(() -> {
        log.debug("begin...");
        sleep(1);
        latch.countDown();
        log.debug("end...{}", latch.getCount());
    });
    service.submit(() -> {
        log.debug("begin...");
        sleep(1.5);
        latch.countDown();
        log.debug("end...{}", latch.getCount());
    });
    service.submit(() -> {
        log.debug("begin...");
        sleep(2);
        latch.countDown();
        log.debug("end...{}", latch.getCount());
    });
    service.submit(()->{
        try {
            log.debug("waiting...");
            latch.await();
            log.debug("wait end...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    });
}
12345678910111213141516171819202122232425262728293031
```

输出

![image-20211108160038275](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/9bcaa372fed9a903b0b1592ec03b6b5f.png)

## 应用之同步等待多线程准备完毕

```java
private static void test2() throws InterruptedException {
    AtomicInteger num = new AtomicInteger(0);
    ExecutorService service = Executors.newFixedThreadPool(10, (r) -> {
        return new Thread(r, "t" + num.getAndIncrement());
    });
    CountDownLatch latch = new CountDownLatch(10);
    String[] all = new String[10];
    Random r = new Random();
    for (int j = 0; j < 10; j++) {
        int x = j;
        service.submit(() -> {
            for (int i = 0; i <= 100; i++) {
                try {
                    Thread.sleep(r.nextInt(100));
                } catch (InterruptedException e) {
                }
                all[x] = Thread.currentThread().getName() + "(" + (i + "%") + ")";
                System.out.print("\r" + Arrays.toString(all));
            }
            latch.countDown();
        });
    }
    latch.await();
    System.out.println("\n游戏开始...");
    service.shutdown();
}
1234567891011121314151617181920212223242526
```

中间输出

![image-20211108171953927](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/79278890096e3f6c98fe96bcab00b08d.png)

最后输出

![image-20211108172005748](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/4ffdaaade94998d6615523af7a7d4d0e.png)

## 应用之同步等待多个远程调用结束

```java
@RestController
public class TestCountDownlatchController {

    @GetMapping("/order/{id}")
    public Map<String, Object> order(@PathVariable int id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("total", "2300.00");
        sleep(2000);
        return map;
    }

    @GetMapping("/product/{id}")
    public Map<String, Object> product(@PathVariable int id) {
        HashMap<String, Object> map = new HashMap<>();
        if (id == 1) {
            map.put("name", "小爱音箱");
            map.put("price", 300);
        } else if (id == 2) {
            map.put("name", "小米手机");
            map.put("price", 2000);
        }
        map.put("id", id);
        sleep(1000);
        return map;
    }

    @GetMapping("/logistics/{id}")
    public Map<String, Object> logistics(@PathVariable int id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", "中通快递");
        sleep(2500);
        return map;
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
1234567891011121314151617181920212223242526272829303132333435363738394041424344
```

rest 远程调用

```java
private static void test3() throws InterruptedException, ExecutionException {
    RestTemplate restTemplate = new RestTemplate();
    log.debug("begin");
    ExecutorService service = Executors.newCachedThreadPool();
    CountDownLatch latch = new CountDownLatch(4);
    Future<Map<String,Object>> f1 = service.submit(() -> {
        Map<String, Object> response = restTemplate.getForObject("http://localhost:8080/order/{1}", Map.class, 1);
        return response;
    });
    Future<Map<String, Object>> f2 = service.submit(() -> {
        Map<String, Object> response1 = restTemplate.getForObject("http://localhost:8080/product/{1}", Map.class, 1);
        return response1;
    });
    Future<Map<String, Object>> f3 = service.submit(() -> {
        Map<String, Object> response1 = restTemplate.getForObject("http://localhost:8080/product/{1}", Map.class, 2);
        return response1;
    });
    Future<Map<String, Object>> f4 = service.submit(() -> {
        Map<String, Object> response3 = restTemplate.getForObject("http://localhost:8080/logistics/{1}", Map.class, 1);
        return response3;
    });

    log.debug("{}", f1.get());
    log.debug("{}", f2.get());
    log.debug("{}", f3.get());
    log.debug("{}", f4.get());
    log.debug("执行完毕");
    service.shutdown();
}
1234567891011121314151617181920212223242526272829
```

执行结果

![image-20211108174213652](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/8c3f39977cc2ee7812397400ab6c4c03.png)

# [CyclicBarrier](https://so.csdn.net/so/search?q=CyclicBarrier&spm=1001.2101.3001.7020)

[?sa?kl?k ?b?ri?] 循环栅栏，用来进行线程协作，等待线程满足某个计数。构造时设置『计数个数』，每个线程执行到某个需要“同步”的时刻调用 await() 方法进行等待，当等待的线程数满足『计数个数』时，继续执行

```java
@Slf4j(topic = "c.TestCyclicBarrier")
public class TestCyclicBarrier {

    public static void main(String[] args) {
      // 线程池线程个数，要与计数个数一致。不然如果为3，那么也有可能是第1，3个线程获得了锁
        ExecutorService service = Executors.newFixedThreadPool(2);
        CyclicBarrier barrier = new CyclicBarrier(2, ()-> {
            log.debug("task1, task2 finish...");
        });
        for (int i = 0; i < 3; i++) { // task1  task2  task1
            service.submit(() -> {
                log.debug("task1 begin...");
                sleep(1);
                try {
                    barrier.await(); // 2-1=1
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
            service.submit(() -> {
                log.debug("task2 begin...");
                sleep(2);
                try {
                    barrier.await(); // 1-1=0
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
        service.shutdown();

    }

  // 而如果用CountDownLatch，每次都要新建对象没，无法复用
    private static void test1() {
        ExecutorService service = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 3; i++) {
            CountDownLatch latch = new CountDownLatch(2);
            service.submit(() -> {
                log.debug("task1 start...");
                sleep(1);
                latch.countDown();
            });
            service.submit(() -> {
                log.debug("task2 start...");
                sleep(2);
                latch.countDown();
            });
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("task1 task2 finish...");
        }
        service.shutdown();
    }
}
12345678910111213141516171819202122232425262728293031323334353637383940414243444546474849505152535455565758
```

输出

![image-20211108195036611](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/609470e51fe8e5bbec549685955e1ad1.png)

> **注意** CyclicBarrier 与 CountDownLatch 的主要区别在于 CyclicBarrier 是可以重用的 CyclicBarrier 可以被比喻为『人满发车』