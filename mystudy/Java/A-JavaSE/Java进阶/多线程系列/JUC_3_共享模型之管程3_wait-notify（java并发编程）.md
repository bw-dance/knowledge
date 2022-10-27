### wait-notify、park-unpark

- [小故事 - 为什么需要 wait](#___wait_2)
- [原理之 wait / notify](#_wait__notify_18)
- [API 介绍](#API__29)
- - [notify与notifyAll](#notifynotifyAll_81)
- [wait notify 的正确姿势](#wait_notify__153)
- - [step 1](#step_1_204)
  - [step 2](#step_2_280)
  - [step 3](#step_3_340)
  - [step 4](#step_4_417)
  - [step 5](#step_5_439)
  - [正确姿势](#_495)
- [模式之保护性暂停](#_513)
- - [定义](#_515)
  - [实现](#_530)
  - [带超时版 GuardedObject](#_GuardedObject_602)
  - [原理之 join](#_join_683)
  - - [join源码](#join_716)
  - [多任务版 GuardedObject](#_GuardedObject_755)
- [模式之生产者消费者](#_924)
- - [定义](#_928)
  - [实现](#_942)
- [Park & Unpark](#Park__Unpark_1055)
- - [基本使用](#_1057)
  - [特点](#_1121)
  - [原理之 park & unpark](#_park__unpark_1131)



# 小故事 - 为什么需要 wait

- 由于条件不满足，小南不能继续进行计算
- 但小南如果一直占用着锁，其它人就得一直阻塞，效率太低

![image-20211027093058220](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/25b74c9eb6795ac21955a06ba04c5dba.png)

- 于是老王单开了一间休息室（调用 wait 方法），让小南到休息室（WaitSet）等着去了，但这时锁释放开，其它人可以由老王随机安排进屋
- 直到小M将烟送来，大叫一声 [ 你的烟到了 ] （调用 notify 方法）

![image-20211027093125392](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/00c603487cabf159f13615c7b19a96c4.png)

- 小南于是可以离开休息室，重新进入竞争锁的队列

# 原理之 wait / notify

![image-20211027093451220](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/f3f449d56444f830c438eb99af4de43f.png)

- Owner 线程发现条件不满足，调用 wait 方法，即可进入 WaitSet 变为 WAITING 状态
- BLOCKED 和 WAITING 的线程都处于阻塞状态，不占用 CPU 时间片
- BLOCKED 线程会在 Owner 线程释放锁时唤醒
- WAITING 线程会在 Owner 线程调用 notify 或 notifyAll 时唤醒，但唤醒后并不意味者立刻获得锁，仍需进入EntryList 重新竞争

# API 介绍

- obj.wait() 让进入 object 监视器的线程到 waitSet 等待
- obj.notify() 在 object 上正在 waitSet 等待的线程中挑一个唤醒
- obj.notifyAll() 让 object 上正在 waitSet 等待的线程全部唤醒

它们都是线程之间进行协作的手段，都属于 Object 对象的方法。必须获得此对象的锁，才能调用这几个方法

```java
@Slf4j(topic = "c.Test18")
public class Test18 {
    static final Object lock = new Object();
    public static void main(String[] args) {

            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        
    }
}
12345678910111213
```

输出

![image-20211027094140712](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/a1e7a4ba41374a93ceb7bd7adeb09b39.png)

因为还没有获得对象的锁，直接调用会报错。

应该

```java
@Slf4j(topic = "c.Test18")
public class Test18 {
    static final Object lock = new Object();
    public static void main(String[] args) {
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
12345678910111213
```

## notify与notifyAll

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
//            obj.notify(); // 唤醒obj上一个线程
            obj.notifyAll(); // 唤醒obj上所有等待线程
        }
    }
}
123456789101112131415161718192021222324252627282930313233343536373839
```

notify 的一种结果（t1、t2都有可能，看哪个线程先执行）

```java
20:00:53.096 [Thread-0] c.TestWaitNotify - 执行.... 
20:00:53.099 [Thread-1] c.TestWaitNotify - 执行.... 
20:00:55.096 [main] c.TestWaitNotify - 唤醒 obj 上其它线程
20:00:55.096 [Thread-0] c.TestWaitNotify - 其它代码....
1234
```

notifyAll 的结果

```java
19:58:15.457 [Thread-0] c.TestWaitNotify - 执行.... 
19:58:15.460 [Thread-1] c.TestWaitNotify - 执行.... 
19:58:17.456 [main] c.TestWaitNotify - 唤醒 obj 上其它线程
19:58:17.456 [Thread-1] c.TestWaitNotify - 其它代码.... 
19:58:17.456 [Thread-0] c.TestWaitNotify - 其它代码....
12345
```

- wait() 方法会释放对象的锁，进入 WaitSet 等待区，从而让其他线程就机会获取对象的锁。无限制等待，直到notify 为止
- wait(long n) 有时限的等待, 到 n 毫秒后结束等待，或是被 notify

# wait notify 的正确姿势

开始之前先看看

**sleep(long n)** **和** **wait(long n)** **的区别**

1. sleep 是 Thread 方法，而 wait 是 Object 的方法
2. sleep 不需要强制和 synchronized 配合使用，但 wait 需要和 synchronized 一起用
3. sleep 在睡眠的同时，不会释放对象锁的，但 wait 在等待的时候会释放对象锁
4. 它们状态都是 TIMED_WAITING

```java
@Slf4j(topic = "c.Test19")
public class Test19 {

    static final Object lock = new Object();
    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (lock) {
                log.debug("获得锁");
                try {
//                    Thread.sleep(20000);
                    lock.wait(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t1").start();

        Sleeper.sleep(1);
        synchronized (lock) {
            log.debug("获得锁");
        }
    }
}
1234567891011121314151617181920212223
```

Thread.sleep(20000);睡眠期间不会释放锁，睡醒了才给你：

![image-20211027100126324](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/5e71e405ebbc3853ce687e9979564ea0.png)

lock.wait(20000);等待期间会释放锁：

![image-20211027100201615](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/2d1598662a9960deb70d8daae5254de1.png)

## step 1

```java
@Slf4j(topic = "c.TestCorrectPosture")
public class TestCorrectPostureStep1 {
    static final Object room = new Object();
    static boolean hasCigarette = false; // 有没有烟
    static boolean hasTakeout = false;

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (room) {
                log.debug("有烟没？[{}]", hasCigarette);
                if (!hasCigarette) {
                    log.debug("没烟，先歇会！");
                    sleep(2);
                }
                log.debug("有烟没？[{}]", hasCigarette);
                if (hasCigarette) {
                    log.debug("可以开始干活了");
                }
            }
        }, "小南").start();

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                synchronized (room) {
                    log.debug("可以开始干活了");
                }
            }, "其它人").start();
        }

        sleep(1);
        new Thread(() -> {
          // 这里能不能加 synchronized (room)？
          hasCigarette = true;
          log.debug("烟到了噢！");
        }, "送烟的").start();
    }

}
1234567891011121314151617181920212223242526272829303132333435363738
```

输出

![image-20211027101306202](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/6a46a9803ec06d8541d6dc0a910be01a.png)

如果送烟的主线程也加了synchronized，小南不走，烟送不到；小南就走，烟能到了，但是人还没干活就走了

```java
sleep(1);
    new Thread(() -> {
        // 这里能不能加 synchronized (room)？
        synchronized (room) {
            hasCigarette = true;
            log.debug("烟到了噢！");
        }
    }, "送烟的").start();
}
123456789
```

输出

![image-20211027101600408](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/1794011a8ded5c38bfca64a4d5c6d039.png)

- 其它干活的线程，都要一直阻塞，效率太低
- 小南线程必须睡足 2s 后才能醒来，就算烟提前送到，也无法立刻醒来
- 加了 synchronized (room) 后，就好比小南在里面反锁了门睡觉，烟根本没法送进门，main 没加synchronized 就好像 main 线程是翻窗户进来的
- 解决方法，使用 wait - notify 机制

## step 2

思考下面的实现行吗，为什么？

```java
@Slf4j(topic = "c.TestCorrectPosture")
public class TestCorrectPostureStep2 {
    static final Object room = new Object();
    static boolean hasCigarette = false;
    static boolean hasTakeout = false;

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (room) {
                log.debug("有烟没？[{}]", hasCigarette);
                if (!hasCigarette) {
                    log.debug("没烟，先歇会！");
                    try {
                        room.wait(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("有烟没？[{}]", hasCigarette);
                if (hasCigarette) {
                    log.debug("可以开始干活了");
                }
            }
        }, "小南").start();

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                synchronized (room) {
                    log.debug("可以开始干活了");
                }
            }, "其它人").start();
        }

        sleep(1);
        new Thread(() -> {
            synchronized (room) {
                hasCigarette = true;
                log.debug("烟到了噢！");
                room.notify();
            }
        }, "送烟的").start();
    }

}
1234567891011121314151617181920212223242526272829303132333435363738394041424344
```

输出

![image-20211027102039540](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/9c31dd164bb8ee164665d978ca61f004.png)

- 解决了其它干活的线程阻塞的问题
- 但如果有其它线程也在等待条件呢？送烟的主线程notify会不会错误地叫醒其他线程呢？

## step 3

```java
@Slf4j(topic = "c.TestCorrectPosture")
public class TestCorrectPostureStep3 {
    static final Object room = new Object();
    static boolean hasCigarette = false;
    static boolean hasTakeout = false;

    // 虚假唤醒
    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (room) {
                log.debug("有烟没？[{}]", hasCigarette);
                if (!hasCigarette) {
                    log.debug("没烟，先歇会！");
                    try {
                        room.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("有烟没？[{}]", hasCigarette);
                if (hasCigarette) {
                    log.debug("可以开始干活了");
                } else {
                    log.debug("没干成活...");
                }
            }
        }, "小南").start();

        new Thread(() -> {
            synchronized (room) {
                Thread thread = Thread.currentThread();
                log.debug("外卖送到没？[{}]", hasTakeout);
                if (!hasTakeout) {
                    log.debug("没外卖，先歇会！");
                    try {
                        room.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("外卖送到没？[{}]", hasTakeout);
                if (hasTakeout) {
                    log.debug("可以开始干活了");
                } else {
                    log.debug("没干成活...");
                }
            }
        }, "小女").start();

        sleep(1);
        new Thread(() -> {
            synchronized (room) {
                hasTakeout = true;
                log.debug("外卖到了噢！");
//                room.notifyAll();
                room.notify();
            }
        }, "送外卖的").start();


    }

}
123456789101112131415161718192021222324252627282930313233343536373839404142434445464748495051525354555657585960616263
```

输出

![image-20211027102933602](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/4d49fe4e7257907b389ab0d6a6216128.png)

- notify 只能随机唤醒一个 WaitSet 中的线程，这时如果有其它线程也在等待，那么就可能唤醒不了正确的线程，称之为【虚假唤醒】
- 解决方法，改为 notifyAll

## step 4

```java
sleep(1);
new Thread(() -> {
    synchronized (room) {
        hasTakeout = true;
        log.debug("外卖到了噢！");
        room.notifyAll();
    }
}, "送外卖的").start();
12345678
```

输出

![image-20211027103226961](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/0eb17b9616c7dc94b311e8c74dcd92d8.png)

- 用 notifyAll 仅解决某个线程的唤醒问题，但使用 if + wait 判断仅有一次机会，一旦条件不成立，就没有重新判断的机会了
- 解决方法，用 while + wait，当条件不成立，再次 wait

## step 5

将 if 改为 while

```java
new Thread(() -> {
    synchronized (room) {
        log.debug("有烟没？[{}]", hasCigarette);
        if (!hasCigarette) {
            log.debug("没烟，先歇会！");
            try {
                room.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.debug("有烟没？[{}]", hasCigarette);
        if (hasCigarette) {
            log.debug("可以开始干活了");
        } else {
            log.debug("没干成活...");
        }
    }
}, "小南").start();
12345678910111213141516171819
```

改动后

```java
new Thread(() -> {
    synchronized (room) {
        log.debug("有烟没？[{}]", hasCigarette);
        while (!hasCigarette) {
            log.debug("没烟，先歇会！");
            try {
                room.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.debug("有烟没？[{}]", hasCigarette);
        if (hasCigarette) {
            log.debug("可以开始干活了");
        } else {
            log.debug("没干成活...");
        }
    }
}, "小南").start();
12345678910111213141516171819
```

输出

![image-20211027103630403](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/5ddeeac3c8e656daf570c78db29b84ac.png)

## 正确姿势

```java
synchronized(lock) {
  while(条件不成立) {
    lock.wait();
  }
  // 干活
}

//另一个线程
synchronized(lock) {
  lock.notifyAll();
}
1234567891011
```

# 模式之保护性暂停

## 定义

> 即 Guarded Suspension，用在一个线程等待另一个线程的执行结果

要点

- 有一个结果需要从一个线程传递到另一个线程，让他们关联同一个 GuardedObject
- 如果有结果不断从一个线程到另一个线程那么可以使用消息队列（见生产者/消费者）
- JDK 中，join 的实现、Future 的实现，采用的就是此模式
- 因为要等待另一方的结果，因此归类到同步模式

![image-20211027110112414](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/72c0c22a544f23c083c96d952f277f3c.png)

## 实现

```java
class GuardedObject {
  private Object response;
  private final Object lock = new Object();
  public Object get() {
    synchronized (lock) {
      // 条件不满足则等待
      while (response == null) {
        try {
          lock.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        } 
      }
      return response; 
    }
  }

  public void complete(Object response) {
    synchronized (lock) {
      // 条件满足，通知等待线程
      this.response = response;
      lock.notifyAll();
    }
  }
}
12345678910111213141516171819202122232425
```

**应用**

一个线程等待另一个线程的执行结果

```java
public static void main(String[] args) {
  GuardedObject guardedObject = new GuardedObject();

  new Thread(() -> {
    try {
      // 子线程执行下载
      List<String> response = download();
      log.debug("download complete...");
      guardedObject.complete(response);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }).start();

  log.debug("waiting...");
  // 主线程阻塞等待
  Object response = guardedObject.get();
  log.debug("get response: [{}] lines", ((List<String>) response).size());
}
12345678910111213141516171819
```

执行结果

```java
08:42:18.568 [main] c.TestGuardedObject - waiting...
08:42:23.312 [Thread-0] c.TestGuardedObject - download complete...
08:42:23.312 [main] c.TestGuardedObject - get response: [3] lines
123
```

- join也是保护性暂停，join是要等待调用线程执行完了才会继续执行其他

## 带超时版 GuardedObject

如果要控制超时时间呢

```java
class GuardedObjectV2 {
    private Object response;
    private final Object lock = new Object();
    public Object get(long millis) {
        synchronized (lock) {
// 1) 记录最初时间
            long begin = System.currentTimeMillis();
// 2) 已经经历的时间
            long timePassed = 0;
            while (response == null) {
// 4) 假设 millis 是 1000，结果在 400 时唤醒了，那么还有 600 要等
                long waitTime = millis - timePassed;
                log.debug("waitTime: {}", waitTime);
                if (waitTime <= 0) {
                    log.debug("break...");
                    break; }
                try {
                    lock.wait(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
// 3) 如果提前被唤醒，这时已经经历的时间假设为 400
                timePassed = System.currentTimeMillis() - begin;
                log.debug("timePassed: {}, object is null {}",
                        timePassed, response == null);
            }
            return response; }
    }
    public void complete(Object response) {
        synchronized (lock) {
// 条件满足，通知等待线程
            this.response = response;
            log.debug("notify...");
            lock.notifyAll();
        }
    }
}
12345678910111213141516171819202122232425262728293031323334353637
```

测试，没有超时

```java
public static void main(String[] args) {
    GuardedObjectV2 v2 = new GuardedObjectV2();
    new Thread(() -> {
        sleep(1);
        v2.complete(null);
        sleep(1);
        v2.complete(Arrays.asList("a", "b", "c"));
    }).start();
    Object response = v2.get(2500);
    if (response != null) {
        log.debug("get response: [{}] lines", ((List<String>) response).size());
    } else {
        log.debug("can't get response");
    }
}
123456789101112131415
```

输出

![image-20211027113044777](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/ab91909499b89acb48af96eba10665d0.png)

测试，超时

```java
// 等待时间不足
List<String> lines = v2.get(1500);
12
```

输出

![image-20211027113122449](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/046d950f61bf500442a36d9a2f7e59bc.png)

## 原理之 join

> t.join()方法**阻塞调用此方法的线程**(calling thread)进入 **TIMED_WAITING** 状态，**直到线程t完成，此线程再继续**；
>
> 通常用于在main()主线程内，等待其它线程完成再结束main()主线程

是调用者轮询检查线程 alive 状态

```java
t1.join();
1
```

等价于下面的代码

```java
synchronized (t1) {
  // 调用者线程进入 t1 的 waitSet 等待, 直到 t1 运行结束
  // 此处t1线程对象作为了锁
  while (t1.isAlive()) {
    // 调用线程进了锁t1的waitSet
    // 注意，调用线程不是t1，t1此处是作为锁而不是作为线程
    // 调用线程是其他线程，一般是主线程
    t1.wait(0);
  }
}
12345678910
```

> **注意**
>
> join 体现的是【保护性暂停】模式，请参考之

### join源码

```java
public final void join() throws InterruptedException {
    join(0);
}

public final synchronized void join(long millis)
    throws InterruptedException {
        long base = System.currentTimeMillis();
        long now = 0;

        if (millis < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }

        if (millis == 0) {
            while (isAlive()) {
                wait(0);
            }
        } else {
            while (isAlive()) {
                long delay = millis - now;
                if (delay <= 0) {
                    break;
                }
                wait(delay);
                now = System.currentTimeMillis() - base;
            }
        }
    }
12345678910111213141516171819202122232425262728
```

- 从代码中，我们可以发现。当millis==0时，会进入while( isAlive() )循环；即只要子线程是活的，主线程就不停的等待。
- wait()的作用是让“当前线程”等待，而这里的“当前线程”是指当前运行的线程。虽然是调用子线程的wait()方法，但是它是通过“主线程”去调用的；所以，休眠的是主线程，而不是“子线程”！
- 这样理解: 例子中的Thread t只是一个对象 , isAlive()判断当前对象(例子中的t对象)是否存活, wait()阻塞的是当前执行的线程(一般是main方法)
- 可以看出，Join方法实现是通过wait()。 当main线程调用t.join时候，main线程会获得线程对象t的锁（wait 意味着拿到该对象的锁)，调用该对象的wait()，直到该对象唤醒main线程 ，比如退出后。这就意味着main 线程调用t.join时，必须能够拿到线程t对象的锁。

## 多任务版 GuardedObject

图中 Futures 就好比居民楼一层的信箱（每个信箱有房间编号），左侧的 t0，t2，t4 就好比等待邮件的居民，右侧的 t1，t3，t5 就好比邮递员

如果需要在多个类之间使用 GuardedObject 对象，作为参数传递不是很方便，因此设计一个用来解耦的中间类，这样不仅能够解耦【结果等待者】和【结果生产者】，还能够同时支持多个任务的管理

![image-20211027151457008](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/3bda11de234cae56ca4b882f407cc592.png)

新增 id 用来标识 Guarded Object

```java
// 增加超时效果
class GuardedObject {

    // 标识 Guarded Object
    private int id;
    public GuardedObject(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
  

    // 结果
    private Object response;

    // 获取结果
    // timeout 表示要等待多久 2000
    public Object get(long timeout) {
        synchronized (this) {
            // 开始时间 15:00:00
            long begin = System.currentTimeMillis();
            // 经历的时间
            long passedTime = 0;
            while (response == null) {
                // 这一轮循环应该等待的时间
                long waitTime = timeout - passedTime;
                // 经历的时间超过了最大等待时间时，退出循环
                if (timeout - passedTime <= 0) {
                    break;
                }
                try {
                    this.wait(waitTime); // 虚假唤醒 15:00:01
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 求得经历时间
                passedTime = System.currentTimeMillis() - begin; // 15:00:02  1s
            }
            return response;
        }
    }

    // 产生结果
    public void complete(Object response) {
        synchronized (this) {
            // 给结果成员变量赋值
            this.response = response;
            this.notifyAll();
        }
    }
}
12345678910111213141516171819202122232425262728293031323334353637383940414243444546474849505152
```

中间解耦类（Mailboxes只是用来生产带id的GuardedObject，以便在多个类之间使用 GuardedObject 对象，让多个对象即邮递员和收信人能一一对应）

```java
class Mailboxes {
    private static Map<Integer, GuardedObject> boxes = new Hashtable<>();

    private static int id = 1;
    // 产生唯一 id
    private static synchronized int generateId() {
        return id++;
    }

  	// HashTable线程安全的，不用加synchronized
    public static GuardedObject getGuardedObject(int id) {
        return boxes.remove(id);
    }

  	// HashTable线程安全的，不用加synchronized
    public static GuardedObject createGuardedObject() {
        GuardedObject go = new GuardedObject(generateId());
        boxes.put(go.getId(), go);
        return go;
    }

    public static Set<Integer> getIds() {
        return boxes.keySet();
    }
}
12345678910111213141516171819202122232425
```

业务相关类

```java
@Slf4j(topic = "c.People")
class People extends Thread{
    @Override
    public void run() {
        // 收信
        GuardedObject guardedObject = Mailboxes.createGuardedObject();
        log.debug("开始收信 id:{}", guardedObject.getId());
        Object mail = guardedObject.get(5000);
        log.debug("收到信 id:{}, 内容:{}", guardedObject.getId(), mail);
    }
}
1234567891011
@Slf4j(topic = "c.Postman")
class Postman extends Thread {
    private int id;
    private String mail;

    public Postman(int id, String mail) {
        this.id = id;
        this.mail = mail;
    }

    @Override
    public void run() {
        GuardedObject guardedObject = Mailboxes.getGuardedObject(id);
        log.debug("送信 id:{}, 内容:{}", id, mail);
        guardedObject.complete(mail);
    }
}
1234567891011121314151617
```

测试

```java
@Slf4j(topic = "c.Test20")
public class Test20 {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            new People().start();
        }
        Sleeper.sleep(1);
        for (Integer id : Mailboxes.getIds()) {
            new Postman(id, "内容" + id).start();
        }

    }
}
12345678910111213
```

某次运行结果

![image-20211027154258663](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/545d63bf0581af06ff617d94d498c335.png)

# 模式之生产者消费者

异步模式之生产者/消费者

## 定义

要点

- 与前面的保护性暂停中的 GuardObject 不同，不需要产生结果和消费结果的线程一一对应
- 消费队列可以用来平衡生产和消费的线程资源
- 生产者仅负责产生结果数据，不关心数据该如何处理，而消费者专心处理结果数据
- 消息队列是有容量限制的，满时不会再加入数据，空时不会再消耗数据
- JDK 中各种阻塞队列，采用的就是这种模式

![image-20211027155946778](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/4440482f3a56cc82d52d9f52a44c067f.png)

## 实现

```java
@Slf4j(topic = "c.Test21")
public class Test21 {

    public static void main(String[] args) {
        MessageQueue queue = new MessageQueue(2);

        for (int i = 0; i < 3; i++) {
            int id = i;
            new Thread(() -> {
                queue.put(new Message(id , "值"+id));
            }, "生产者" + i).start();
        }

        new Thread(() -> {
            while(true) {
                sleep(1);
                Message message = queue.take();
            }
        }, "消费者").start();
    }

}

// 消息队列类 ， java 线程之间通信
@Slf4j(topic = "c.MessageQueue")
class MessageQueue {
    // 消息的队列集合
    private LinkedList<Message> list = new LinkedList<>();
    // 队列容量
    private int capcity;

    public MessageQueue(int capcity) {
        this.capcity = capcity;
    }

    // 获取消息
    public Message take() {
        // 检查队列是否为空
        synchronized (list) {
            while(list.isEmpty()) {
                try {
                    log.debug("队列为空, 消费者线程等待");
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 从队列头部获取消息并返回
            Message message = list.removeFirst();
            log.debug("已消费消息 {}", message);
            list.notifyAll();
            return message;
        }
    }

    // 存入消息
    public void put(Message message) {
        synchronized (list) {
            // 检查对象是否已满
            while(list.size() == capcity) {
                try {
                    log.debug("队列已满, 生产者线程等待");
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 将消息加入队列尾部
            list.addLast(message);
            log.debug("已生产消息 {}", message);
            list.notifyAll();
        }
    }
}

final class Message {
    private int id;
    private Object value;

    public Message(int id, Object value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}
123456789101112131415161718192021222324252627282930313233343536373839404142434445464748495051525354555657585960616263646566676869707172737475767778798081828384858687888990919293949596979899100
```

输出

![image-20211027170023698](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/c97c1c1b3eba7c6b8f068eae5d152f8e.png)

# Park & Unpark

## 基本使用

它们是 LockSupport 类中的方法

```java
// 暂停当前线程
LockSupport.park(); 
// 恢复某个线程的运行
LockSupport.unpark(暂停线程对象)
1234
```

先 park 再 unpark

```java
Thread t1 = new Thread(() -> {
  log.debug("start...");
  sleep(1);
  log.debug("park...");
  LockSupport.park();
  log.debug("resume...");
},"t1");
t1.start();

sleep(2);
log.debug("unpark...");
LockSupport.unpark(t1);
123456789101112
```

输出

![image-20211027170954584](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/14e5642b0fa1d745049dc148e6339eb7.png)

先 unpark 再 park

```java
@Slf4j(topic = "c.TestParkUnpark")
public class TestParkUnpark {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            log.debug("start...");
            sleep(1);
            log.debug("park...");
            LockSupport.park();
            log.debug("resume...");
        }, "t1");
        t1.start();

        sleep(2);
        log.debug("unpark...");
        LockSupport.unpark(t1);
    }
}
1234567891011121314151617
```

输出

![image-20211027170914660](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/0daf03c45e01fcdb05e4f4803da0e3b0.png)

## 特点

与 Object 的 wait & notify 相比

- wait，notify 和 notifyAll 必须配合 Object Monitor 一起使用，而 park，unpark 不必
- park & unpark 是以线程为单位来【阻塞】和【唤醒】线程，而 notify 只能随机唤醒一个等待线程，notifyAll 是唤醒所有等待线程，就不那么【精确】
- park & unpark 可以先 unpark，而 wait & notify 不能先 notify

## 原理之 park & unpark

每个线程都有自己的一个 Parker 对象，由三部分组成 _counter ， _cond 和 _mutex 打个比喻

- 线程就像一个旅人，Parker 就像他随身携带的背包，条件变量就好比背包中的帐篷。_counter 就好比背包中的备用干粮（0 为耗尽，1 为充足）
- 调用 park 就是要看需不需要停下来歇息
  - 如果备用干粮耗尽，那么钻进帐篷歇息
  - 如果备用干粮充足，那么不需停留，继续前进
- 调用 unpark，就好比令干粮充足
  - 如果这时线程还在帐篷，就唤醒让他继续前进
  - 如果这时线程还在运行，那么下次他调用 park 时，仅是消耗掉备用干粮，不需停留继续前进
  - 因为背包空间有限，多次调用 unpark 仅会补充一份备用干粮

![image-20211027172237458](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/4493f0a508c82d8c0c79d4caee46775c.png)

1. 当前线程调用 Unsafe.park() 方法
2. 检查 _counter ，本情况为 0，这时，获得 _mutex 互斥锁
3. 线程进入 _cond 条件变量阻塞
4. 设置 _counter = 0

![image-20211027172317226](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/947827cad9c607c6e1a3dd8b8d8316e3.png)

1. 调用 Unsafe.unpark(Thread_0) 方法，设置 _counter 为 1
2. 唤醒 _cond 条件变量中的 Thread_0
3. Thread_0 恢复运行
4. 设置 _counter 为 0

![image-20211027172345232](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/6f8e75eaf1b2d686e1967f9a162c3c0d.png)

1. 调用 Unsafe.unpark(Thread_0) 方法，设置 _counter 为 1
2. 当前线程调用 Unsafe.park() 方法
3. 检查 _counter ，本情况为 1，这时线程无需阻塞，继续运行
4. 设置 _counter 为 0