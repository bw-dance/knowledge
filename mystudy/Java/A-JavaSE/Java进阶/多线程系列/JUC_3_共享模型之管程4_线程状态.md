# 共享模型之管程4_线程状态

[toc]

# 重新理解线程状态转换

![image-20211028103856331](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/09dbf63f5d5519d7adef46c40d356f25.png)

假设有线程 Thread t

## 情况 1 NEW --> RUNNABLE

当调用 t.start() 方法时，由 NEW --> RUNNABLE

## 情况 2 RUNNABLE <–> WAITING

**t** **线程**用 synchronized(obj) 获取了对象锁后

- 调用 obj.wait() 方法时，**t** **线程**从 RUNNABLE --> WAITING
- 调用 obj.notify() ， obj.notifyAll() ， t.interrupt() 时
  - 竞争锁成功，**t** **线程**从 WAITING --> RUNNABLE
  - 竞争锁失败，**t** **线程**从 WAITING --> BLOCKED

```java
@Slf4j(topic = "c.TestWaitNotify")
public class TestWaitNotify {
    final static Object obj = new Object();

    public static void main(String[] args) {

        new Thread(() -> {
            synchronized (obj) {
                log.debug("执行....");
                try {
                    obj.wait(); // 让线程在obj上一直等待下去
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("其它代码....");
            }
        },"t1").start();

        new Thread(() -> {
            synchronized (obj) {
                log.debug("执行....");
                try {
                    obj.wait(); // 让线程在obj上一直等待下去
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("其它代码....");
            }
        },"t2").start();

        // 主线程两秒后执行
        sleep(0.5);
        log.debug("唤醒 obj 上其它线程");
        synchronized (obj) {
            obj.notifyAll(); // 唤醒obj上所有等待线程
        }
    }
}
272829303132333435363738
```

Monitor示意图：

![image-20211028105438481](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/6406259ff59b75d0d6e3ff421c31529b.png)

### 示例调试步骤

顺序执行代码，线程t1、t2都wait了，断点来到主线程的notify()

![image-20211028105858991](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/a38079579d9073fcdf6d88edf4c7ee82.png)

向下执行，主线程notifyAll()唤醒所有waiting的线程，这些线程从锁对象obj的waitSet移动到EntryList竞争锁

![image-20211028110313592](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/ada6e992bd036b4444e8eabba102fc90.png)

主线程向下执行，出了synchronized块，释放了锁。t1、t2可以竞争锁。可以看到t2竞争锁成功，成为锁对象obj的owner，t1仍然在entryList中阻塞。

![image-20211028110601840](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/6cdce3e171a3a6b894cbcec5ec45d37b.png)

t2线程向下执行，出了synchronized块，释放锁，t1线程拿到锁

![image-20211028110809523](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/7254da423c32d99a4b4d7b595d066720.png)

## 情况 3 RUNNABLE <–> WAITING

- **当前线程**调用 **t.join()** 方法时，**当前线程**从 RUNNABLE --> WAITING
  - 注意是**当前线程**在**t** **线程对象**的监视器上等待
- **t 线程**运行结束，或调用了**当前线程**的 **interrupt()** 时，**当前线程**从 WAITING --> RUNNABLE

## 情况 4 RUNNABLE <–> WAITING

- 当前线程调用 **LockSupport.park()** 方法会让当前线程从 RUNNABLE --> WAITING
- 调用 **LockSupport.unpark**(目标线程) 或调用了线程 的 **interrupt()** 会让目标线程从 WAITING --> RUNNABLE

## 情况 5 RUNNABLE <–> TIMED_WAITING

**t 线程**用 synchronized(obj) 获取了对象锁后

- 调用 obj.wait(long n) 方法时，**t** **线程**从 RUNNABLE --> TIMED_WAITING

- t 线程

  等待时间超过了 n 毫秒，或调用

   

  obj.notify()

   

  ，

  obj.notifyAll()

   

  ，

   

  t.interrupt()

   

  时

  - 竞争锁成功，**t** **线程**从 TIMED_WAITING --> RUNNABLE
  - 竞争锁失败，**t** **线程**从 TIMED_WAITING --> BLOCKED

## 情况 6 RUNNABLE <–> TIMED_WAITING

- **当前线程**调用 t.join(long n) 方法时，**当前线程**从 RUNNABLE --> TIMED_WAITING
  - 注意是**当前线程**在**t** **线程对象**的监视器上等待
- **当前线程**等待时间超过了 n 毫秒，或**t** **线程**运行结束，或调用了**当前线程**的 interrupt() 时，**当前线程**从TIMED_WAITING --> RUNNABLE

## 情况 7 RUNNABLE <–> TIMED_WAITING

- 当前线程调用 Thread.sleep(long n) ，当前线程从 RUNNABLE --> TIMED_WAITING
- 当前线程等待时间超过了 n 毫秒，当前线程从 TIMED_WAITING --> RUNNABLE

## 情况 8 RUNNABLE <–> TIMED_WAITING

- 当前线程调用 LockSupport.parkNanos(long nanos) LockSupport.parkUntil(long millis) 时，当前线程从 RUNNABLE --> TIMED_WAITING
- 调用 LockSupport.unpark(目标线程) 或调用了线程 的 interrupt() ，或是等待超时，会让目标线程从TIMED_WAITING–> RUNNABLE

## 情况 9 RUNNABLE <–> BLOCKED

- t 线程用 synchronized(obj) 获取了对象锁时如果竞争失败，从 RUNNABLE --> BLOCKED
- 持 obj 锁线程的同步代码块执行完毕，会唤醒该对象上所有 BLOCKED 的线程重新竞争，如果其中 t 线程竞争成功，从 BLOCKED --> RUNNABLE ，其它失败的线程仍然 BLOCKED

## 情况 10 RUNNABLE <–> TERMINATED

当前线程所有代码运行完毕，进入 TERMINATED

# 多把锁

**多把不相干的锁**

一间大屋子有两个功能：睡觉、学习，互不相干。

现在小南要学习，小女要睡觉，但如果只用一间屋子（一个对象锁）的话，那么并发度很低

解决方法是准备多个房间（多个对象锁）

例如

```java
class BigRoom {
    public void sleep() {
        synchronized (this) {
            log.debug("sleeping 2 小时");
            Sleeper.sleep(2);
        }
    }
    public void study() {
        synchronized (this) {
            log.debug("study 1 小时");
            Sleeper.sleep(1);
        }
    }
}
1234567891011121314
```

执行

```java
public class TestMultiLock {
    public static void main(String[] args) {
        BigRoom bigRoom = new BigRoom();
        new Thread(() -> {
            bigRoom.study();
        },"小南").start();
        new Thread(() -> {
            bigRoom.sleep();
        },"小女").start();
    }
}
1234567891011
```

某次结果

![image-20211028113654124](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/e427ab46d7003746aa92623a9beeae06.png)

改进

```java
@Slf4j(topic = "c.BigRoom")
class BigRoom {

    private final Object studyRoom = new Object();

    private final Object bedRoom = new Object();

    public void sleep() {
        synchronized (bedRoom) {
            log.debug("sleeping 2 小时");
            Sleeper.sleep(2);
        }
    }

    public void study() {
        synchronized (studyRoom) {
            log.debug("study 1 小时");
            Sleeper.sleep(1);
        }
    }

}
12345678910111213141516171819202122
```

某次执行结果

![image-20211028113600787](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/3b522f81dfddfbeb3932bd6f25e0c0e6.png)

将锁的粒度细分

- 好处，是可以增强并发度
- 坏处，如果一个线程需要同时获得多把锁，就容易发生死锁

# 活跃性

## 死锁

有这样的情况：一个线程需要同时获取多把锁，这时就容易发生死锁

t1 线程 获得 A对象 锁，接下来想获取 B对象 的锁

t2 线程 获得 B对象 锁，接下来想获取 A对象 的锁

例：

```java
@Slf4j(topic = "c.TestDeadLock")
public class TestDeadLock {
    public static void main(String[] args) {
        test1();
    }

    private static void test1() {
        Object A = new Object();
        Object B = new Object();
        Thread t1 = new Thread(() -> {
            synchronized (A) {
                log.debug("lock A");
                sleep(1);
                synchronized (B) {
                    log.debug("lock B");
                    log.debug("操作...");
                }
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            synchronized (B) {
                log.debug("lock B");
                sleep(0.5);
                synchronized (A) {
                    log.debug("lock A");
                    log.debug("操作...");
                }
            }
        }, "t2");
        t1.start();
        t2.start();
    }
}
2728293031323334
```

输出

![image-20211028153437242](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/9b10516347cd5b771138eed6c1acbc51.png)

### 定位死锁

检测死锁可以使用 jconsole工具，或者使用 jps 定位进程 id，再用 jstack 定位死锁：

```bash
jps 
1
```

![image-20211028154358581](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/c1e075e5c621c2a710dcce8e70f64cb2.png)

```bash
jstack 80270
1
```

部分输出信息

![image-20211028154303343](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/f56975971869e812f649bd0402eb7363.png)

- 避免死锁要注意加锁顺序
- 另外如果由于某个线程进入了死循环，导致其它线程一直等待，对于这种情况 linux 下可以通过 top 先定位到CPU 占用高的 Java 进程，再利用 top -Hp 进程id 来定位是哪个线程，最后再用 jstack 排查

### 哲学家就餐问题

![image-20211028154557418](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/9cfea5993a024d1e1bd1b741e3df1fd5.png)

有五位哲学家，围坐在圆桌旁。

- 他们只做两件事，思考和吃饭，思考一会吃口饭，吃完饭后接着思考。
- 吃饭时要用两根筷子吃，桌上共有 5 根筷子，每位哲学家左右手边各有一根筷子。
- 如果筷子被身边的人拿着，自己就得等待

筷子类

```java
class Chopstick {
    String name;

    public Chopstick(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "筷子{" + name + '}';
    }
}
123456789101112
```

哲学家类

```java
@Slf4j(topic = "c.Philosopher")
class Philosopher extends Thread {
    Chopstick left;
    Chopstick right;

    public Philosopher(String name, Chopstick left, Chopstick right) {
        super(name);
        this.left = left;
        this.right = right;
    }

    private void eat() {
        log.debug("eating...");
        Sleeper.sleep(1);
    }

    @Override
    public void run() {
        while (true) {
            // 获得左手筷子
            synchronized (left) {
                // 获得右手筷子
                synchronized (right) {
                    // 吃饭
                    eat();
                }
                // 放下右手筷子
            }
            // 放下左手筷子
        }
    }
}
272829303132
```

就餐

```java
@Slf4j(topic = "c.TestDeadLock")
public class MyTest {
    public static void main(String[] args) {

        Chopstick c1 = new Chopstick("1");
        Chopstick c2 = new Chopstick("2");
        Chopstick c3 = new Chopstick("3");
        Chopstick c4 = new Chopstick("4");
        Chopstick c5 = new Chopstick("5");
        new Philosopher("苏格拉底", c1, c2).start();
        new Philosopher("柏拉图", c2, c3).start();
        new Philosopher("亚里士多德", c3, c4).start();
        new Philosopher("赫拉克利特", c4, c5).start();
        new Philosopher("阿基米德", c5, c1).start();
    }
}
12345678910111213141516
```

执行不多会，就执行不下去了

![image-20211028160950774](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/7fe58684ed02c67c48fee2ad096e73e0.png)

使用 jconsole 检测死锁，发现

![image-20211028160910708](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/f9e04951f1617438dbc39e1bde601e91.png)

这种线程没有按预期结束，执行不下去的情况，归类为【活跃性】问题，除了死锁以外，还有活锁和饥饿者两种情况

## 活锁

活锁出现在两个线程互相改变对方的结束条件，最后谁也无法结束，（死锁是谁也无法执行）例如

```java
@Slf4j(topic = "c.TestLiveLock")
public class TestLiveLock {
    static volatile int count = 10;
    static final Object lock = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            // 期望减到 0 退出循环
            while (count > 0) {
                sleep(0.2);
                count--;
                log.debug("count: {}", count);
            }
        }, "t1").start();
        new Thread(() -> {
            // 期望超过 20 退出循环
            while (count < 20) {
                sleep(0.2);
                count++;
                log.debug("count: {}", count);
            }
        }, "t2").start();
    }
}
123456789101112131415161718192021222324
```

## 饥饿

很多教程中把饥饿定义为，一个线程由于优先级太低，始终得不到 CPU 调度执行，也不能够结束，饥饿的情况不易演示，讲读写锁时会涉及饥饿问题

下面我讲一下我遇到的一个线程饥饿的例子，先来看看使用顺序加锁的方式解决之前的死锁问题

![image-20211028161551764](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/0082d5fea67dae5cc70255e4f304e0bb.png)

### 顺序加锁的解决方案

![image-20211028161622226](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/c08e5a7212508552e0b75ed71592dea1.png)

虽然不会发生死锁了，但是会发生饥饿

```java
public class TestDeadLock {
    public static void main(String[] args) {
        Chopstick c1 = new Chopstick("1");
        Chopstick c2 = new Chopstick("2");
        Chopstick c3 = new Chopstick("3");
        Chopstick c4 = new Chopstick("4");
        Chopstick c5 = new Chopstick("5");
        new Philosopher("苏格拉底", c1, c2).start();
        new Philosopher("柏拉图", c2, c3).start();
        new Philosopher("亚里士多德", c3, c4).start();
        new Philosopher("赫拉克利特", c4, c5).start();
        new Philosopher("阿基米德", c1, c5).start();
    }
}
1234567891011121314
```

由之前的

```java
new Philosopher("阿基米德", c5, c1).start();
1
```

改为了

```java
new Philosopher("阿基米德", c1, c5).start();
1
```

![image-20211028161947307](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/07eb9962c2ec01e298126a35984117cc.png)

可以看到，如果先拿到筷子的人不放下筷子，其他人很难吃上饭

# ReentrantLock

相对于 synchronized 它具备如下特点

- 可中断
- 可以设置超时时间
- 可以设置为公平锁
- 支持多个条件变量（多个waitSet）
- 与 synchronized 一样，都支持可重入

基本语法

```java
// 获取锁
reentrantLock.lock();
try {
  // 临界区     
} finally {
  // 释放锁      
  reentrantLock.unlock();        
}
12345678
```

## 可重入

> lock.lock();

可重入是指同一个线程如果首次获得了这把锁，那么因为它是这把锁的拥有者，因此有权利再次获取这把锁

如果是不可重入锁，那么第二次获得锁时，自己也会被锁挡住

```java
public class MyTest {
    static ReentrantLock lock = new ReentrantLock();
    public static void main(String[] args) {
        method1();
    }
    public static void method1() {
        lock.lock();
        try {
            log.debug("execute method1");
            method2();
        } finally {
            lock.unlock();
        }
    }
    public static void method2() {
        lock.lock();
        try {
            log.debug("execute method2");
            method3();
        } finally {
            lock.unlock();
        }
    }
    public static void method3() {
        lock.lock();
        try {
            log.debug("execute method3");
        } finally {
            lock.unlock();
        }
    }
}
272829303132
```

输出

![image-20211028163832015](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/f21a00a7297113b11120766bfecd44ea.png)

## 可打断

> lock.lockInterruptibly();

示例

```java
@Slf4j(topic = "c.Test")
public class MyTest {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Thread t1 = new Thread(() -> {
            log.debug("启动...");
            try {
                lock.lockInterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.debug("等锁的过程中被打断");
                return;
            }
            try {
                log.debug("获得了锁");
            } finally {
                lock.unlock();
            }
        }, "t1");
        lock.lock();
        log.debug("获得了锁");
        t1.start();
        try {
            sleep(1);
            t1.interrupt();
            log.debug("执行打断");
        } finally {
            lock.unlock();
        }
    }
}
```

输出（等锁时，t1处于waiting）

![image-20211028164851325](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/04b583f3e53680fc547e4c8f057987f3.png)

注意如果是不可中断模式，那么即使使用了 interrupt 也不会让等待中断

## 锁超时

> lock.tryLock()
>
> lock.tryLock(1, TimeUnit.SECONDS)

立刻失败

```java
@Slf4j(topic = "c.Test")
public class MyTest {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Thread t1 = new Thread(() -> {
            log.debug("启动...");
            if (!lock.tryLock()) {
                log.debug("获取立刻失败，返回");
                return;
            }
            try {
                log.debug("获得了锁");
            } finally {
                lock.unlock();
            }
        }, "t1");
        lock.lock();
        log.debug("获得了锁");
        t1.start();
        try {
            sleep(2);
        } finally {
            lock.unlock();
        }
    }
}

```

输出

![image-20211028170038874](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/2161feafdccb9c9416916047e8decf09.png)

超时失败

```java
@Slf4j(topic = "c.Test")
public class MyTest {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Thread t1 = new Thread(() -> {
            log.debug("启动...");
            try {
                if (!lock.tryLock(1, TimeUnit.SECONDS)) {
                    log.debug("获取等待 1s 后失败，返回");
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                log.debug("获取等待 1s 后成功，获得了锁");
            } finally {
                lock.unlock();
            }
        }, "t1");
        lock.lock();
        log.debug("获得了锁");
        t1.start();
        try {
            sleep(0.5);
        } finally {
            lock.unlock();
        }
    }
}
```

输出

![image-20211028170323059](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/7d4bc0271f46d7aacac67aba66c5f42f.png)

### 使用 tryLock 解决哲学家就餐问题

```java
public class TestDeadLock {
    public static void main(String[] args) {
        Chopstick c1 = new Chopstick("1");
        Chopstick c2 = new Chopstick("2");
        Chopstick c3 = new Chopstick("3");
        Chopstick c4 = new Chopstick("4");
        Chopstick c5 = new Chopstick("5");
        new Philosopher("苏格拉底", c1, c2).start();
        new Philosopher("柏拉图", c2, c3).start();
        new Philosopher("亚里士多德", c3, c4).start();
        new Philosopher("赫拉克利特", c4, c5).start();
        new Philosopher("阿基米德", c5, c1).start();
    }
}

@Slf4j(topic = "c.Philosopher")
class Philosopher extends Thread {
    Chopstick left;
    Chopstick right;

    public Philosopher(String name, Chopstick left, Chopstick right) {
        super(name);
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        while (true) {
            //　尝试获得左手筷子
            if (left.tryLock()) {
                try {
                    // 尝试获得右手筷子
                    if (right.tryLock()) {
                        try {
                            eat();
                        } finally {
                            right.unlock();
                        }
                    }
                } finally {
                    // 获取右手筷子失败，会放弃已经拿到的左手筷子
                    left.unlock();
                }
            }
        }
    }

    private void eat() {
        log.debug("eating...");
        Sleeper.sleep(1);
    }
}



class Chopstick extends ReentrantLock {
    String name;

    public Chopstick(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "筷子{" + name + '}';
    }
}
272829303132333435363738394041424344454647484950515253545556575859606162636465666768
```

输出，都吃上饭了

![image-20211028171239139](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/ef319ac235b715ee7df842ce4f102d1a.png)

## 公平锁

ReentrantLock 默认是不公平的

```java
@Slf4j(topic = "c.Test")
public class MyTest {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock(false);
        lock.lock();
        for (int i = 0; i < 500; i++) {
            new Thread(() -> {
                lock.lock();
                try {
                    System.out.println(Thread.currentThread().getName() + " running...");
                } finally {
                    lock.unlock();
                }
            }, "t" + i).start();
        }
// 1s 之后去争抢锁
        Thread.sleep(1000);
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " start...");
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " running...");
            } finally {
                lock.unlock();
            }
        }, "强行插入").start();
        lock.unlock();
    }
}

27282930
```

强行插入，有机会在中间输出

**注意**：该实验不一定总能复现

![image-20211028172008225](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211028172008225.png)

改为公平锁后

```java
ReentrantLock lock = new ReentrantLock(true);
```

强行插入，总是在最后输出

![image-20211028172240766](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211028172240766.png)

公平锁一般没有必要，会降低并发度，后面分析原理时会讲解

## 条件变量

synchronized 中也有条件变量，就是我们讲原理时那个 waitSet 休息室，当条件不满足时进入 waitSet 等待

ReentrantLock 的条件变量比 synchronized 强大之处在于，它是支持多个条件变量的，这就好比

- synchronized 是那些不满足条件的线程都在一间休息室等消息
- 而 ReentrantLock 支持多间休息室，有专门等烟的休息室、专门等早餐的休息室、唤醒时也是按休息室来唤醒

使用要点：

- await 前需要获得锁
- await 执行后，会释放锁，进入 conditionObject 等待
- await 的线程被唤醒（或打断、或超时）取重新竞争 lock 锁
- 竞争 lock 锁成功后，从 await 后继续执行

```java
@Slf4j(topic = "c.Test24")
public class Test24 {
    static final Object room = new Object();
    static boolean hasCigarette = false;
    static boolean hasTakeout = false;
    static ReentrantLock ROOM = new ReentrantLock();
    // 等待烟的休息室
    static Condition waitCigaretteSet = ROOM.newCondition();
    // 等外卖的休息室
    static Condition waitTakeoutSet = ROOM.newCondition();

    public static void main(String[] args) {


        new Thread(() -> {
            ROOM.lock();
            try {
                log.debug("有烟没？[{}]", hasCigarette);
                while (!hasCigarette) {
                    log.debug("没烟，先歇会！");
                    try {
                        waitCigaretteSet.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("可以开始干活了");
            } finally {
                ROOM.unlock();
            }
        }, "小南").start();

        new Thread(() -> {
            ROOM.lock();
            try {
                log.debug("外卖送到没？[{}]", hasTakeout);
                while (!hasTakeout) {
                    log.debug("没外卖，先歇会！");
                    try {
                        waitTakeoutSet.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("可以开始干活了");
            } finally {
                ROOM.unlock();
            }
        }, "小女").start();

        sleep(1);
        new Thread(() -> {
            ROOM.lock();
            try {
                hasTakeout = true;
                waitTakeoutSet.signal();
            } finally {
                ROOM.unlock();
            }
        }, "送外卖的").start();

        sleep(1);

        new Thread(() -> {
            ROOM.lock();
            try {
                hasCigarette = true;
                waitCigaretteSet.signal();
            } finally {
                ROOM.unlock();
            }
        }, "送烟的").start();
    }

}
```

输出

![image-20211028173838416](https://img-blog.csdnimg.cn/img_convert/21d8b61541f0e83eed5e4d6241e816ec.png)

# 同步模式之顺序控制

## 固定运行顺序

比如，必须先 2 后 1 打印

### wait notify 版

```java
@Slf4j(topic = "c.Test25")
public class Test25 {
    static final Object lock = new Object();
    // 表示 t2 是否运行过
    static boolean t2runned = false;

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                while (!t2runned) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("1");
            }
        }, "t1");


        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                log.debug("2");
                t2runned = true;
                lock.notify();
            }
        }, "t2");

        t1.start();
        t2.start();
    }
}
27282930313233
```

输出

![image-20211028185824557](https://img-blog.csdnimg.cn/img_convert/3a918a753423f1f06878f0c5b2cb3e6d.png)

### Park Unpark 版

可以看到，实现上很麻烦：

- 首先，需要保证先 wait 再 notify，否则 wait 线程永远得不到唤醒。因此使用了『运行标记』来判断该不该wait
- 第二，如果有些干扰线程错误地 notify 了 wait 线程，条件不满足时还要重新等待，使用了 while 循环来解决此问题
- 最后，唤醒对象上的 wait 线程需要使用 notifyAll，因为『同步对象』上的等待线程可能不止一个

可以使用 LockSupport 类的 park 和 unpark 来简化上面的题目：

```java
@Slf4j(topic = "c.Test26")
public class Test26 {
    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            LockSupport.park();
            log.debug("1");
        }, "t1");
        t1.start();

        new Thread(() -> {
            log.debug("2");
            LockSupport.unpark(t1);
        },"t2").start();
    }
}
12345678910111213141516
```

输出

![image-20211028190228488](https://img-blog.csdnimg.cn/img_convert/e6b0aa659a6ed903990dad6247f24c88.png)

- park 和 unpark 方法比较灵活，他俩谁先调用，谁后调用无所谓。并且是以线程为单位进行『暂停』和『恢复』，不需要『同步对象』和『运行标记』
- 因为wait-notify其实是运用了保护性暂停模式，而park-unpark的底层实现其实就是保护性暂停的体现

## 交替输出

线程 1 输出 a 5 次，线程 2 输出 b 5 次，线程 3 输出 c 5 次。现在要求输出 abcabcabcabcabc 怎么实现

### wait notify 版

```java
@Slf4j(topic = "c.Test27")
public class Test27 {
    public static void main(String[] args) {
        WaitNotify wn = new WaitNotify(1, 5);
        new Thread(() -> {
            wn.print("a", 1, 2);
        }).start();
        new Thread(() -> {
            wn.print("b", 2, 3);
        }).start();
        new Thread(() -> {
            wn.print("c", 3, 1);
        }).start();
    }
}

/*
输出内容       等待标记     下一个标记
   a           1             2
   b           2             3
   c           3             1
 */
class WaitNotify {
    // 打印               a           1             2
    public void print(String str, int waitFlag, int nextFlag) {
        for (int i = 0; i < loopNumber; i++) {
            synchronized (this) {
                while(flag != waitFlag) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print(str);
                flag = nextFlag;
                this.notifyAll();
            }
        }
    }

    // 等待标记
    private int flag; // 2
    // 循环次数
    private int loopNumber;

    public WaitNotify(int flag, int loopNumber) {
        this.flag = flag;
        this.loopNumber = loopNumber;
    }
}
27282930313233343536373839404142434445464748495051
```

输出

![image-20211028191605713](https://img-blog.csdnimg.cn/img_convert/f16485c0a304da64020c61f32a7f5ba4.png)

### Lock 条件变量版

```java
public class Test30 {
    public static void main(String[] args) throws InterruptedException {
        AwaitSignal awaitSignal = new AwaitSignal(5);
        Condition a = awaitSignal.newCondition();
        Condition b = awaitSignal.newCondition();
        Condition c = awaitSignal.newCondition();
        new Thread(() -> {
            awaitSignal.print("a", a, b);
        }).start();
        new Thread(() -> {
            awaitSignal.print("b", b, c);
        }).start();
        new Thread(() -> {
            awaitSignal.print("c", c, a);
        }).start();

        Thread.sleep(1000);
        awaitSignal.lock();
        try {
            System.out.println("开始...");
            a.signal();
        } finally {
            awaitSignal.unlock();
        }

    }
}

class AwaitSignal extends ReentrantLock{
    private int loopNumber;

    public AwaitSignal(int loopNumber) {
        this.loopNumber = loopNumber;
    }
    //            参数1 打印内容， 参数2 进入哪一间休息室, 参数3 下一间休息室
    public void print(String str, Condition current, Condition next) {
        for (int i = 0; i < loopNumber; i++) {
            lock();
            try {
                current.await();
                System.out.print(str);
                next.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                unlock();
            }
        }
    }
}
272829303132333435363738394041424344454647484950
```

输出

![image-20211028192639553](https://img-blog.csdnimg.cn/img_convert/8d5500f1fca86b234fb60f6239f4e17c.png)

如果要防止线程被虚假唤醒，可以加while（），但是这里没有其他线程，就这3个，且顺序固定，没有这个问题。

### Park Unpark 版

```java
@Slf4j(topic = "c.Test31")
public class Test31 {

    static Thread t1;
    static Thread t2;
    static Thread t3;
    public static void main(String[] args) {
        ParkUnpark pu = new ParkUnpark(5);
        t1 = new Thread(() -> {
            pu.print("a", t2);
        });
        t2 = new Thread(() -> {
            pu.print("b", t3);
        });
        t3 = new Thread(() -> {
            pu.print("c", t1);
        });
        t1.start();
        t2.start();
        t3.start();

        LockSupport.unpark(t1);
    }
}

class ParkUnpark {
    public void print(String str, Thread next) {
        for (int i = 0; i < loopNumber; i++) {
            LockSupport.park();
            System.out.print(str);
            LockSupport.unpark(next);
        }
    }

    private int loopNumber;

    public ParkUnpark(int loopNumber) {
        this.loopNumber = loopNumber;
    }
}
2728293031323334353637383940
```

输出

![image-20211028193147823](https://img-blog.csdnimg.cn/img_convert/d604c55ceb7b8adaccf1bf44794e2120.png)

# 本章小结

本章我们需要重点掌握的是

- 分析多线程访问共享资源时，哪些代码片段属于临界区
- 使用 synchronized 互斥解决临界区的线程安全问题
  - 掌握 synchronized 锁对象语法
  - 掌握 synchronzied 加载成员方法和静态方法语法
  - 掌握 wait/notify 同步方法
- 使用 lock 互斥解决临界区的线程安全问题
  - 掌握 lock 的使用细节：可打断、锁超时、公平锁、条件变量
- 学会分析变量的线程安全性、掌握常见线程安全类的使用
- 了解线程活跃性问题：死锁、活锁、饥饿

应用方面

- 互斥：使用 synchronized 或 Lock 达到共享资源互斥效果
- 同步：使用 wait/notify 或 Lock 的条件变量来达到线程间通信效果

原理方面

- monitor、synchronized 、wait/notify 原理
- synchronized 进阶原理
- park & unpark 原理

模式方面

- 同步模式之保护性暂停（一对一）
- 异步模式之生产者消费者（一对多）
- 同步模式之顺序控制