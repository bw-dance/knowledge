# JAVA线程

- [创建和运行线程](#_3)
- - [方法一：直接使用Thread](#Thread_5)
  - [方法二：使用 Runnable 配合 Thread](#_Runnable__Thread_39)
  - - [原理之 Thread 与 Runnable 的关系](#_Thread__Runnable__103)
  - [方法三：FutureTask 配合 Thread](#FutureTask__Thread_135)
- [观察多个线程同时运行](#_181)
- [查看进程线程的方法](#_211)
- - [windows](#windows_213)
  - [linux](#linux_222)
  - [Java](#Java_236)
- [原理之线程运行](#_284)
- - [栈与栈帧](#_286)
  - [多线程的栈帧](#_345)
  - [线程上下文切换（Thread Context Switch）](#Thread_Context_Switch_353)
- [常用方法](#_372)
- - [start 与 run](#start__run_396)
  - - [调用run()](#run_398)
    - [调用 start](#_start_432)
    - [小结](#_474)
  - [sleep 与 yield](#sleep__yield_481)
  - - [引例](#_483)
    - [sleep（睡）](#sleep_578)
    - [yield（让）](#yield_590)
    - [线程优先级](#_599)
    - [应用-sleep防止cpu占用100%](#sleepcpu100_634)
  - [join 方法详解](#join__654)
  - - [为什么需要 join](#_join_656)
    - [应用-同步](#_732)
    - [有时效的 join](#_join_785)
  - [interrupt 方法详解](#interrupt__839)
  - - [打断 sleep，wait，join 的线程](#_sleepwaitjoin__841)
    - [打断正常运行的线程](#_874)
    - [模式之两阶段终止](#_909)
    - [打断 park 线程](#_park__983)
  - [不推荐的方法](#_1059)
- [主线程与守护线程](#_1071)
- [五种状态](#_1110)
- [六种状态](#_1130)
- [习题](#_1244)
- - [应用之统筹（烧水泡茶）](#_1290)
- [本章小结](#_1332)



# 创建和运行线程

## 方法一：直接使用Thread

```java
// 创建线程对象
Thread t = new Thread() {
  public void run() {
    // 要执行的任务
  }
};
// 启动线程
1234567
```

例如：

```java
// 构造方法的参数是给线程指定名字，推荐
Thread t1 = new Thread("t1") {
	@Override
	// run 方法内实现了要执行的任务
	public void run() {
		log.debug("hello");
 	}
};
t1.start();
123456789
```

输出：

```bash
19:19:00 [t1] c.ThreadStarter - hello
1
```

## 方法二：使用 Runnable 配合 Thread

把【线程】和【任务】（要执行的代码）分开

- Thread 代表线程
- Runnable 可运行的任务（线程要执行的代码）

```java
Runnable runnable = new Runnable() {
	public void run(){
	// 要执行的任务
 	}
};
// 创建线程对象
Thread t = new Thread( runnable );
// 启动线程
t.start();
123456789
```

例如：

```java
// 创建任务对象
Runnable task2 = new Runnable() {
	@Override
	public void run() {
		log.debug("hello");
 	}
};
// 参数1 是任务对象; 参数2 是线程名字，推荐
Thread t2 = new Thread(task2, "t2");
t2.start();
12345678910
```

输出：

```bash
19:19:00 [t2] c.ThreadStarter - hello
1
```

Java 8 以后可以使用 lambda 精简代码

```java
// 创建任务对象
Runnable task2 = () -> log.debug("hello");

// 参数1 是任务对象; 参数2 是线程名字，推荐
Thread t2 = new Thread(task2, "t2");
t2.start();
123456
```

更加精简的写法：

因为整个lambda表达式就是一个接口对象，所以可以直接写在参数列表

```java
Thread t = new Thread(() -> log.debug("running"), "t1");
t.start();
12
```

### 原理之 Thread 与 Runnable 的关系

分析 Thread 的源码，理清它与 Runnable 的关系

Thread源码：

![image-20211011172246712](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/a75c974b787e5104eda09d4af717116e.png)

Thread的init()方法：

![image-20211011172405658](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/6d4fcc0928e9ab16438067f8a290e4e6.png)

Thread的run()方法：

![image-20211011172622025](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/a599260e99d552e6cee7e67eae379314.png)

如果没有传入Runnable对象，自然不会有target，所以用方法一直接使用Thread创建线程时，需要重写run()方法。

- 方法1 是把线程和任务合并在了一起，方法2 是把线程和任务分开了
- 用 Runnable 更容易与线程池等高级 [API](https://so.csdn.net/so/search?q=API&spm=1001.2101.3001.7020) 配合
- 用 Runnable 让任务类脱离了 Thread 继承体系，更灵活

## 方法三：FutureTask 配合 Thread

FutureTask 能够接收 Callable 类型的参数，用来处理有返回结果的情况

```java
// 创建任务对象
FutureTask<Integer> task3 = new FutureTask<>(() -> {
	log.debug("hello");
	return 100;
});

// 参数1 是任务对象; 参数2 是线程名字，推荐
new Thread(task3, "t3").start();

// 主线程阻塞，同步等待 task 执行完毕的结果
Integer result = task3.get();
log.debug("结果是:{}", result);
123456789101112
```

输出：

```bash
19:22:27 [t3] c.ThreadStarter - hello
19:22:27 [main] c.ThreadStarter - 结果是:100
12
```

Callable的call()方法有返回值，且可以抛出异常；Runnable的没有返回值。

Callable接口源码

![image-20211011173200924](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/3dc1d5ef399b2634190785b81dfaea15.png)

FutureTask源码

![image-20211011173320586](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/e177dabbea29e2b613a06b542a9f6d20.png)

![image-20211011173308634](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/d1e45ff9113aa0d74d8fe45a3ab2fcdc.png)

# 观察多个线程同时运行

主要是理解

- 交替执行
- 谁先谁后，不由我们控制

```java
@Slf4j(topic = "c.Test3")
public class Test3 {
    public static void main(String[] args) {
        new Thread(() -> {
            while(true) {
                log.debug("running...");
            }
        }, "t1").start();

        new Thread(() -> {
            while(true) {
                log.debug("running...");
            }
        }, "t2").start();
    }
}
12345678910111213141516
```

![image-20211011174124575](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/06a2b6fa783c9220ef244e25e3adcf71.png)

# 查看进程线程的方法

## windows

任务管理器可以查看进程和线程数，也可以用来杀死进程

- tasklist 查看进程
- taskkill 杀死进程

## linux

- ps -fe 查看所有进程
- ps -fT -p PID 查看某个进程（PID）的所有线程
- kill 杀死进程
- top 按大写 H 切换是否显示线程
- top -H -p PID 查看某个进程（PID）的所有线程

## Java

- jps 命令查看所有 Java 进程
- jstack 查看某个 Java 进程（PID）的所有线程状态
- jconsole 来查看某个 Java 进程中线程的运行情况（图形界面）

```bash
top -H -p PID
1
```

![image-20211011174848343](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/562b2575a9e77a6d088297a127f8ba0a.png)

```bash
jstack pid
1
```

快照信息

![image-20211011174941395](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/d0e973eb9aa0254431d407310cf5f149.png)

jconsole 远程监控配置

- 需要以如下方式运行你的 java 类

```bash
java -Djava.rmi.server.hostname=`ip地址` -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=`连接端口` -Dcom.sun.management.jmxremote.ssl=是否安全连接 -Dcom.sun.management.jmxremote.authenticate=是否认证 java类
1
```

- 修改 /etc/hosts 文件将 127.0.0.1 映射至主机名

如果要认证访问，还需要做如下步骤

- 复制 jmxremote.password 文件
- 修改 jmxremote.password 和 jmxremote.access 文件的权限为 600 即文件所有者可读写
- 连接时填入 controlRole（用户名），R&D（密码）

![image-20211011175838368](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/794d4a9081a4b0423dc7e3c1a59739a3.png)

# 原理之线程运行

## 栈与栈帧

Java Virtual Machine Stacks （Java 虚拟机栈）

我们都知道 JVM 中由堆、栈、方法区所组成，其中栈内存是给谁用的呢？其实就是线程，每个线程启动后，虚拟机就会为其分配一块栈内存。

- 每个栈由多个栈帧（Frame）组成，对应着每次方法调用时所占用的内存
- 每个线程只能有一个活动栈帧，对应着当前正在执行的那个方法

示例代码

```java
public class TestFrames {
    public static void main(String[] args) {
        Thread t1 = new Thread(){
            @Override
            public void run() {
                method1(20);
            }
        };
        t1.setName("t1");
        t1.start();
        method1(10);
    }

    private static void method1(int x) {
        int y = x + 1;
        Object m = method2();
        System.out.println(m);
    }

    private static Object method2() {
        Object n = new Object();
        return n;
    }
}
123456789101112131415161718192021222324
```

![image-20211011193839792](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/2f22de7ffafa1a46516413751a1b62a1.png)

![image-20211011193920748](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/eaa4e641f6449bb62d30a4aa1308fced.png)

方法执行完毕后，栈帧按顺序先入后出销毁，销毁前会记录返回地址，回到上一个栈帧里，继续执行。

![image-20211011194912278](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/ae146952bf02e3cddf250d610426aef8.png)

![image-20211011194939805](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/8bfc4b1413ffc9ca141084710badefca.png)

- 程序计数器告诉cpu该执行那一行代码
- 对象的创建都会放在堆中（new Object等）
- 当执行到方法的调用时，会产生新的栈帧，操作新的局部变量（Object m = method2()等）
- 当方法执行完后，释放内存，销毁栈帧，回到上一个栈帧

## 多线程的栈帧

![image-20211011195846488](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/a5545404899b51b4897944df7c7b9ec6.png)

多线程运行，栈帧是线程私有的，栈帧内存互不干扰，可能t1线程执行到了method1，main线程已经执行到了method2。

## 线程上下文切换（Thread Context Switch）

因为以下一些原因导致 cpu 不再执行当前的线程，转而执行另一个线程的代码

- 线程的 cpu 时间片用完
- 垃圾回收
- 有更高优先级的线程需要运行
- 线程自己调用了 sleep、yield、wait、join、park、synchronized、lock 等方法

当 Context Switch 发生时，需要由操作系统保存当前线程的状态，并恢复另一个线程的状态，Java 中对应的概念就是程序计数器（Program Counter Register），它的作用是记住下一条 jvm 指令的执行地址，是线程私有的

- 状态包括程序计数器、虚拟机栈中每个栈帧的信息，如局部变量、操作数栈、返回地址等
- Context Switch 频繁发生会影响性能

# 常用方法

| 方法名           | static | 功能说明                                                     | 注意                                                         |
| ---------------- | ------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| start()          |        | 启动一个新线程，在新的线程运行 run 方法中的代码              | start 方法只是让线程进入就绪，里面代码不一定立刻运行（CPU 的时间片还没分给它）。每个线程对象的start方法只能调用一次，如果调用了多次会出现IllegalThreadStateException |
| run()            |        | 新线程启动后会调用的方法                                     | 如果在构造 Thread 对象时传递了 Runnable 参数，则线程启动后会调用Runnable 中的 run 方法，否则默认不执行任何操作。但可以创建 Thread 的子类对象，来覆盖默认行为 |
| join()           |        | 等待线程运行结束                                             |                                                              |
| join(long n)     |        | 等待线程运行结束,最多等待 n 毫秒                             |                                                              |
| getId()          |        | 获取线程长整型的 id                                          | id 唯一                                                      |
| getName()        |        | 获取线程名                                                   |                                                              |
| setName(String)  |        | 修改线程名                                                   |                                                              |
| getPriority()    |        | 获取线程优先级                                               |                                                              |
| setPriority(int) |        | 修改线程优先级                                               | java中规定线程优先级是1~10 的整数，较大的优先级能提高该线程被 CPU 调度的机率 |
| getState()       |        | 获取线程状态                                                 | Java 中线程状态是用 6 个 enum 表示，分别为：NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING, TERMINATED |
| isInterrupted()  |        | 判断是否被打断                                               | 不会清除 打断标记                                            |
| isAlive()        |        | 线程是否存活（还没有运行完毕）                               |                                                              |
| interrupt()      |        | 打断线程                                                     | 如果被打断线程正在 sleep，wait，join 会导致被打断的线程抛出 InterruptedException，并清除 打断标记 ；如果打断的正在运行的线程，则会设置 打断标 |
| interrupted()    | static | 判断当前线程是否被打断                                       | 会清除 打断标记                                              |
| currentThread()  | static | 获取当前正在执                                               |                                                              |
| sleep(long n)    | static | 让当前执行的线程休眠n毫秒，休眠时让出 cpu 的时间片给其它线程 |                                                              |
| yield()          | static | 提示线程调度器让出当前线程对CPU的使用                        | 主要是为了测试和调试                                         |

## start 与 run

### 调用run()

```java
@Slf4j(topic = "c.Test4")
public class Test4 {

    public static void main(String[] args) {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                log.debug("running...");
                FileReader.read(Constants.MP4_FULL_PATH);
            }
        };

        t1.run();
        log.debug("do other things...");
    }
}
12345678910111213141516
```

输出

```bash
20:01:47.138 c.Test4 [main] - running...
20:01:47.152 c.FileReader [main] - read [4_154884890029038.mp4] start ...
20:01:47.354 c.FileReader [main] - read [4_154884890029038.mp4] end ... cost: 202 ms
20:01:47.354 c.Test4 [main] - do other things...
1234
```

程序仍在 main 线程运行， FileReader.read() 方法调用还是同步的

### 调用 start

将上述代码的 t1.run() 改为

```java
t1.start();
1
@Slf4j(topic = "c.Test4")
public class Test4 {

    public static void main(String[] args) {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                log.debug("running...");
                FileReader.read(Constants.MP4_FULL_PATH);
            }
        };

        t1.start();
        log.debug("do other things...");
    }
}
12345678910111213141516
```

输出

```bash
20:00:12.415 c.Test4 [t1] - running...
20:00:12.415 c.Test4 [main] - do other things...
20:00:12.419 c.FileReader [t1] - read [4_154884890029038.mp4] start ...
20:00:12.485 c.FileReader [t1] - read [4_154884890029038.mp4] end ... cost: 66 ms
1234
```

程序在 t1 线程运行， FileReader.read() 方法调用是异步的

### 小结

- 直接调用 run 是在主线程中执行了 run，没有启动新的线程
- 使用 start 是启动新的线程，通过新的线程间接执行 run 中的代码

## sleep 与 yield

### 引例

> 调用 sleep 会让当前线程从 *Running* 进入 *Timed Waiting* 状态（阻塞）

```java
@Slf4j(topic = "c.Test6")
public class Test6 {

    public static void main(String[] args) {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        t1.start();
        log.debug("t1 state: {}", t1.getState());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("t1 state: {}", t1.getState());
    }
}
1234567891011121314151617181920212223242526
```

输出

![image-20211013200836426](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/cc1e04994d48ba881adcab708fb0030b.png)

> 其它线程可以使用 interrupt 方法打断正在睡眠的线程，这时 sleep 方法会抛出 InterruptedException

```java
@Slf4j(topic = "c.Test7")
public class Test7 {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                log.debug("enter sleep...");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    log.debug("wake up...");
                    e.printStackTrace();
                }
            }
        };
        t1.start();
        
        // 让当前线程（main）睡眠
        Thread.sleep(1000);
        log.debug("interrupt...");
        t1.interrupt();
    }
}
123456789101112131415161718192021222324
```

输出

![image-20211013201127059](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/3d45745dd352a4aa5db5070e0ece1c40.png)

> 建议用 TimeUnit 的 sleep 代替 Thread 的 sleep 来获得更好的可读性

```java
@Slf4j(topic = "c.Test8")
public class Test8 {

    public static void main(String[] args) throws InterruptedException {
        log.debug("enter");
        TimeUnit.SECONDS.sleep(1);
        log.debug("end");
//        Thread.sleep(1000);
    }
}
12345678910
```

输出

![image-20211013201319546](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/8156ae9384075b7c8d4de85f4b0cb94c.png)

### sleep（睡）

1. 调用 sleep 会让当前线程从 *Running* 进入 *Timed Waiting* 状态（阻塞）
2. 其它线程可以使用 interrupt 方法打断正在睡眠的线程，这时 sleep 方法会抛出 InterruptedException
3. 睡眠结束后的线程未必会立刻得到执行
4. 建议用 TimeUnit 的 sleep 代替 Thread 的 sleep 来获得更好的可读性

### yield（让）

1. 调用 yield 会让当前线程从 *Running* 进入 *Runnable* 就绪状态，然后调度执行其它线程
2. 具体的实现依赖于操作系统的任务调度器
3. 与sleep不同，yield只是把cpu使用权让出去了，但是让出去后可能又被cpu分配了时间片；而sleep直接阻塞了，cpu不会分配时间片

### 线程优先级

- 线程优先级会提示（hint）调度器优先调度该线程，但它仅仅是一个提示，调度器可以忽略它
- 如果 cpu 比较忙，那么优先级高的线程会获得更多的时间片，但 cpu 闲时，优先级几乎没作用

```java
@Slf4j(topic = "c.Test9")
public class Test9 {

    public static void main(String[] args) {
        Runnable task1 = () -> {
            int count = 0;
            for (;;) {
                System.out.println("---->1 " + count++);
            }
        };
        Runnable task2 = () -> {
            int count = 0;
            for (;;) {
//                Thread.yield();
                System.out.println("              ---->2 " + count++);
            }
        };
        Thread t1 = new Thread(task1, "t1");
        Thread t2 = new Thread(task2, "t2");
        t1.setPriority(Thread.MIN_PRIORITY);
        t2.setPriority(Thread.MAX_PRIORITY);
        t1.start();
        t2.start();
    }
}
12345678910111213141516171819202122232425
```

### 应用-sleep防止cpu占用100%

在没有利用 cpu 来计算时，不要让 while(true) 空转浪费 cpu，这时可以使用 yield 或 sleep 来让出 cpu 的使用权给其他程序

```java
while(true) {
	try {
		Thread.sleep(50);
 	} catch (InterruptedException e) {
		e.printStackTrace();
 	}
}
1234567
```

- 可以用 wait 或 条件变量达到类似的效果
- 不同的是，后两种都需要加锁，并且需要相应的唤醒操作，一般适用于要进行同步的场景
- sleep 适用于无需锁同步的场景

## join 方法详解

### 为什么需要 join

下面的代码执行，打印 r 是什么？

```java
@Slf4j(topic = "c.Test10")
public class Test10 {
    static int r = 0;
    public static void main(String[] args) throws InterruptedException {
        test1();
    }
    private static void test1() throws InterruptedException {
        log.debug("开始");
        Thread t1 = new Thread(() -> {
            log.debug("开始");
            sleep(1);
            log.debug("结束");
            r = 10;
        },"t1");
        t1.start();
//        t1.join();
        log.debug("结果为:{}", r);
        log.debug("结束");
    }
}
1234567891011121314151617181920
```

输出

![image-20211013203633621](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/cf9994852b8c130c1c358d9bf3598894.png)

分析

- 因为主线程和线程 t1 是并行执行的，t1 线程需要 1 秒之后才能算出 r=10
- 而主线程一开始就要打印 r 的结果，所以只能打印出 r=0

解决方法

- 用 sleep 行不行？为什么？
  - 用sleep也可以，但是不知道让main线程睡多久才行
- 用 join，加在 t1.start() 之后即可
  - 让主线程阻塞，等待t1线程完成

```java
@Slf4j(topic = "c.Test10")
public class Test10 {
    static int r = 0;
    public static void main(String[] args) throws InterruptedException {
        test1();
    }
    private static void test1() throws InterruptedException {
        log.debug("开始");
        Thread t1 = new Thread(() -> {
            log.debug("开始");
            sleep(1);
            log.debug("结束");
            r = 10;
        },"t1");
        t1.start();
        t1.join();
        log.debug("结果为:{}", r);
        log.debug("结束");
    }
}
1234567891011121314151617181920
```

输出

![image-20211013203859916](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/e8e6ee0056981926bf45af80972dec97.png)

![image-20211013204544649](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211013204544649.png)

### 应用-同步

以调用方角度来讲，如果

- 需要等待结果返回，才能继续运行就是同步
- 不需要等待结果返回，就能继续运行就是异步

**等待多个结果**

问，下面代码 cost 大约多少秒？

```java
private static void test2() throws InterruptedException {
    Thread t1 = new Thread(() -> {
        sleep(1);
        r1 = 10;
    });
    Thread t2 = new Thread(() -> {
        sleep(2);
        r2 = 20;
    });
    t1.start();
    t2.start();
    long start = System.currentTimeMillis();
    log.debug("join begin");

    t1.join();
    log.debug("t1 join end");
    t2.join();
    log.debug("t2 join end");
    long end = System.currentTimeMillis();
    log.debug("r1: {} r2: {} cost: {}", r1, r2, end - start);
}
123456789101112131415161718192021
```

分析如下

- 第一个 join：等待 t1 时, t2 并没有停止, 而在运行
- 第二个 join：1s 后, 执行到此, t2 也运行了 1s, 因此也只需再等待 1s

如果颠倒两个 join 呢？

最终都是输出

![image-20211013205005692](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/375a8eb40c27b1a4836bd7dad9d991bc.png)

![image-20211013205107457](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/c079d69eb08fc09b0fb347f0f6151c4e.png)

### 有时效的 join

等够时间

```java
static int r1 = 0;
static int r2 = 0;
public static void test3() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            sleep(2);
            r1 = 10;
        });

        long start = System.currentTimeMillis();
        t1.start();

        // 线程执行结束会导致 join 结束
        log.debug("join begin");
        t1.join(3000);
        long end = System.currentTimeMillis();
        log.debug("r1: {} r2: {} cost: {}", r1, r2, end - start);
    }
1234567891011121314151617
```

![image-20211013205552281](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/a378cb7902e3c57d9f8e079cdacf24a0.png)

没等够时间

```java
static int r1 = 0;
static int r2 = 0;
public static void test3() throws InterruptedException {
    Thread t1 = new Thread(() -> {
        sleep(2);
        r1 = 10;
    });

    long start = System.currentTimeMillis();
    t1.start();

    // 线程执行结束会导致 join 结束
    log.debug("join begin");
    t1.join(1500);
    long end = System.currentTimeMillis();
    log.debug("r1: {} r2: {} cost: {}", r1, r2, end - start);
}
1234567891011121314151617
```

![image-20211013205817835](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/42753a5a84bc1e7a434b8ed294a93d0b.png)

## interrupt 方法详解

### 打断 sleep，wait，join 的线程

这几个方法都会让线程进入阻塞状态

打断 sleep 的线程, 会清空打断状态，以 sleep 为例

```java
public class Test11 {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.debug("sleep...");
            try {
                Thread.sleep(5000); // wait, join
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t1");

        t1.start();
        // 等待t1线程进入sleep状态
        Thread.sleep(1000);
        log.debug("interrupt");
        t1.interrupt();
        log.debug("打断标记:{}", t1.isInterrupted());
    }
}
1234567891011121314151617181920
```

![image-20211013210640310](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/fb88d00fc98cd88e7e74b6ca70f9fdc1.png)

### 打断正常运行的线程

打断正常运行的线程, 不会清空打断状态

```java
@Slf4j(topic = "c.Test12")
public class Test12 {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while(true) {
                boolean interrupted = Thread.currentThread().isInterrupted();
                if(interrupted) {
                    log.debug("被打断了, 退出循环");
                    break;
                }
            }
        }, "t1");
        t1.start();

        Thread.sleep(1000);
        log.debug("interrupt");
        t1.interrupt();
    }
}
1234567891011121314151617181920
```

![image-20211013211435080](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/1dcc4ae8145f70252ce02155f6765f66.png)

- 值得注意的是，线程被打断后，线程并不会直接停止。
- 被打断后打断标记为true
- 线程知道了我现在被打断了，那么就可以去料理后事，做一些善后工作，处理完一些东西再结束，不会结束得那么突然。

### 模式之两阶段终止

Two Phase Termination

在一个线程 T1 中如何“优雅”终止线程 T2？这里的【优雅】指的是给 T2 一个料理后事的机会。

错误思路

- 使用线程对象的 stop() 方法停止线程

  stop 方法会真正杀死线程，如果这时线程锁住了共享资源，那么当它被杀死后就再也没有机会释放锁，其它线程将永远无法获取锁

- 使用 System.exit(int) 方法停止线程

  目的仅是停止一个线程，但这种做法会让整个程序都停止

两阶段终止模式

![image-20211014103704743](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/1d260e80fa99cd990d9e7dd8fa203cc7.png)

```java
@Slf4j(topic = "c.Test13")
public class Test13 {
    public static void main(String[] args) throws InterruptedException {
        TwoPhaseTermination tpt = new TwoPhaseTermination();
        tpt.start();

        Thread.sleep(3000);
        tpt.stop();
    }
}

@Slf4j(topic = "c.TwoPhaseTermination")
class TwoPhaseTermination {
    private Thread monitor;

    // 启动监控线程
    public void start() {
        monitor = new Thread(() -> {
            while (true) {
                Thread currentThread = Thread.currentThread();
                if (currentThread.isInterrupted()) {
                    log.debug("料理后事");
                    break;
                }
                try {
                    Thread.sleep(1000); // 情况1：打断不会被设置打断标记
                    log.debug("执行监控记录");  // 情况2：正常打断，会被设置打断标记
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    // 处理sleep被打断的情况,重新设置打断标记
                    currentThread.interrupt();
                }
            }
        });

        monitor.start();
    }

    // 停止监控线程
    public void stop() {
        monitor.interrupt();
    }
}
12345678910111213141516171819202122232425262728293031323334353637383940414243
```

输出

![image-20211014110828488](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/dc6803aa72fd00314774a7c1001b3e1c.png)

### 打断 park 线程

> 打断 park 线程, 不会清空打断状态

```java
private static void test3() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.debug("park...");
            LockSupport.park();
            log.debug("unpark...");
            log.debug("打断状态：{}", Thread.currentThread().isInterrupted());
        }, "t1");
        t1.start();

        sleep(1);
        t1.interrupt();

    }
12345678910111213
```

输出

![image-20211014111316792](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/cf779f2dfe720fa3a40f0c36a9a7746d.png)

> 如果打断标记已经是 true, 则 park 会失效

```java
private static void test4() {
    Thread t1 = new Thread(() -> {
        for (int i = 0; i < 5; i++) {
            log.debug("park...");
            LockSupport.park();
            log.debug("打断状态：{}", Thread.currentThread().isInterrupted());
        }
    });
    t1.start();


    sleep(1);
    t1.interrupt();
}
1234567891011121314
```

![image-20211014111554967](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/547318c5d48e51ba8ffe0bae068d3aaf.png)

如果park生效，会停在第二个park。因为第一次调用LockSupport.park();程序会停住，但是由于调用了Thread.interrupted()，清除了打断标记，在返回当前线程的打断标记后，会重新把打断标记置为false，如下例

> 可以使用 Thread.interrupted() 清除打断状态

```java
private static void test4() {
    Thread t1 = new Thread(() -> {
        for (int i = 0; i < 5; i++) {
            log.debug("park...");
            LockSupport.park();
            log.debug("打断状态：{}", Thread.interrupted());
        }
    });
    t1.start();


    sleep(1);
    t1.interrupt();
}
1234567891011121314
```

输出

![image-20211014111828017](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/b61539ac0b627121a6d9f061f1dc694e.png)

## 不推荐的方法

还有一些不推荐使用的方法，这些方法已过时，容易破坏同步代码块，造成线程死锁

| 方法名    | static | 功能说明             | 替代方法       |
| --------- | ------ | -------------------- | -------------- |
| stop()    |        | 停止线程运行         | 两阶段模式终止 |
| suspend() |        | 挂起（暂停）线程运行 | wait()         |
| resume()  |        | 恢复线程运行         | notify()       |

# 主线程与守护线程

默认情况下，Java 进程需要等待所有线程都运行结束，才会结束。有一种特殊的线程叫做守护线程，只要其它非守护线程运行结束了，即使守护线程的代码没有执行完，也会强制结束。

例：

```java
public class Test15 {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
            }
            log.debug("结束");
        }, "t1");
        t1.setDaemon(true);
        t1.start();

        Thread.sleep(1000);
        log.debug("结束");
    }
}
1234567891011121314151617
```

输出

![image-20211014113602641](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/a83712d0815aec76eed35c1e30181e74.png)

> **注意**
>
> - 垃圾回收器线程就是一种守护线程
> - Tomcat 中的 Acceptor 和 Poller 线程都是守护线程，所以 Tomcat 接收到 shutdown 命令后，不会等待它们处理完当前请求

# 五种状态

这是从 **操作系统** 层面来描述的

![image-20211014113813699](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/86e724473aacb6ec6da83c6e371544a7.png)

- 【初始状态】仅是在语言层面创建了线程对象，还未与操作系统线程关联
- 【可运行状态】（就绪状态）指该线程已经被创建（与操作系统线程关联），可以由 CPU 调度执行
- 【运行状态】指获取了 CPU 时间片运行中的状态
  - 当 CPU 时间片用完，会从【运行状态】转换至【可运行状态】，会导致线程的上下文切换
- 【阻塞状态】
  - 如果调用了阻塞 API，如 BIO 读写文件，这时该线程实际不会用到 CPU，会导致线程上下文切换，进入【阻塞状态】
  - 等 BIO 操作完毕，会由操作系统唤醒阻塞的线程，转换至【可运行状态】
  - 与【可运行状态】的区别是，对【阻塞状态】的线程来说只要它们一直不唤醒，调度器就一直不会考虑调度它们
- 【终止状态】表示线程已经执行完毕，生命周期已经结束，不会再转换为其它状态

# 六种状态

这是从 **Java API** 层面来描述的

根据 Thread.State 枚举，分为六种状态

![image-20211014114148122](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/dd3afd73905fd79ea007f45ba2078e73.png)

- **NEW** 线程刚被创建，但是还没有调用 start() 方法
- **RUNNABLE** 当调用了 start() 方法之后，注意，**Java API** 层面的 RUNNABLE 状态涵盖了 **操作系统** 层面的【可运行状态】、【运行状态】和【阻塞状态】（由于 BIO 导致的线程阻塞，在 Java 里无法区分，仍然认为是可运行）。对应着的状态为，可能分到时间片、可能没有分配到时间片，或者操作系统IO时的阻塞状态。
- **BLOCKED** ， **WAITING** ， **TIMED_WAITING** 都是 Java API 层面对【阻塞状态】的细分，后面会在状态转换一节详述
- **TERMINATED** 当线程代码运行结束

```java
@Slf4j(topic = "c.TestState")
public class TestState {
    public static void main(String[] args) throws IOException {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                log.debug("running...");
            }
        };

        Thread t2 = new Thread("t2") {
            @Override
            public void run() {
                while(true) { // runnable

                }
            }
        };
        t2.start();

        Thread t3 = new Thread("t3") {
            @Override
            public void run() {
                log.debug("running...");
            }
        };
        t3.start();

        Thread t4 = new Thread("t4") {
            @Override
            public void run() {
                synchronized (TestState.class) {
                    try {
                        Thread.sleep(1000000); // timed_waiting
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t4.start();

        Thread t5 = new Thread("t5") {
            @Override
            public void run() {
                try {
                    t2.join(); // waiting
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t5.start();

        Thread t6 = new Thread("t6") {
            @Override
            public void run() {
                synchronized (TestState.class) { // blocked
                    // t4线程先把锁拿到了，t6拿不到锁，一直等着拿锁
                    try {
                        Thread.sleep(1000000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t6.start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("t1 state {}", t1.getState());
        log.debug("t2 state {}", t2.getState());
        log.debug("t3 state {}", t3.getState());
        log.debug("t4 state {}", t4.getState());
        log.debug("t5 state {}", t5.getState());
        log.debug("t6 state {}", t6.getState());
        System.in.read();
    }
}
1234567891011121314151617181920212223242526272829303132333435363738394041424344454647484950515253545556575859606162636465666768697071727374757677787980818283
```

输出

![image-20211014115554255](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/8567b1c3472510a8d3266b330e4465c4.png)

- t1声明了但是没有start
- t2 start了，但是while(true)一直运行
- t3 正常结束
- t4 sleep了，等待睡醒
- t5 被join了，必须等别人join完才能接着运行
- t6 想要锁，没有锁，等着别人释放锁

# 习题

阅读华罗庚《统筹方法》，给出烧水泡茶的多线程解决方案，提示

- 参考图二，用两个线程（两个人协作）模拟烧水泡茶过程
  - 文中办法乙、丙都相当于任务串行
  - 而图一相当于启动了 4 个线程，有点浪费
- 用 sleep(n) 模拟洗茶壶、洗水壶等耗费的时间

附：华罗庚《统筹方法》

统筹方法，是一种安排工作进程的数学方法。它的实用范围极广泛，在企业管理和基本建设中，以及关系复杂的科研项目的组织与管理中，都可以应用。

怎样应用呢？主要是把工序安排好。

比如，想泡壶茶喝。当时的情况是：开水没有；水壶要洗，茶壶、茶杯要洗；火已生了，茶叶也有了。怎么办？

- 办法甲：洗好水壶，灌上凉水，放在火上；在等待水开的时间里，洗茶壶、洗茶杯、拿茶叶；等水开了，泡茶喝。
- 办法乙：先做好一些准备工作，洗水壶，洗茶壶茶杯，拿茶叶；一切就绪，灌水烧水；坐待水开了，泡茶喝。
- 办法丙：洗净水壶，灌上凉水，放在火上，坐待水开；水开了之后，急急忙忙找茶叶，洗茶壶茶杯，泡茶喝。

哪一种办法省时间？我们能一眼看出，第一种办法好，后两种办法都窝了工。

这是小事，但这是引子，可以引出生产管理等方面有用的方法来。

水壶不洗，不能烧开水，因而洗水壶是烧开水的前提。没开水、没茶叶、不洗茶壶茶杯，就不能泡茶，因而这些又是泡茶的前提。它们的相互关系，可以用下边的箭头图来表示：

![image-20211014161945161](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/ce208153c3b9c7602415c3e9c9f086da.png)

从这个图上可以一眼看出，办法甲总共要16分钟（而办法乙、丙需要20分钟）。如果要缩短工时、提高工作效率，应当主要抓烧开水这个环节，而不是抓拿茶叶等环节。同时，洗茶壶茶杯、拿茶叶总共不过4分钟，大可利用“等水开”的时间来做。

是的，这好像是废话，卑之无甚高论。有如走路要用两条腿走，吃饭要一口一口吃，这些道理谁都懂得。但稍有变化，临事而迷的情况，常常是存在的。在近代工业的错综复杂的工艺过程中，往往就不是像泡茶喝这么简单了。任务多了，几百几千，甚至有好几万个任务。关系多了，错综复杂，千头万绪，往往出现“万事俱备，只欠东风”的情况。由于一两个零件没完成，耽误了一台复杂机器的出厂时间。或往往因为抓的不是关键，连夜三班，急急忙忙，完成这一环节之后，还得等待旁的环节才能装配。

洗茶壶，洗茶杯，拿茶叶，或先或后，关系不大，而且同是一个人的活儿，因而可以合并成为：

![image-20211014162030286](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/6ce057d5a66771ef090b100c49c7bed7.png)

看来这是“小题大做”，但在工作环节太多的时候，这样做就非常必要了。

这里讲的主要是时间方面的事，但在具体生产实践中，还有其他方面的许多事。这种方法虽然不一定能直接解决所有问题，但是，我们利用这种方法来考虑问题，也是不无裨益的。

## 应用之统筹（烧水泡茶）

```java
@Slf4j(topic = "c.Test16")
public class Test16 {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            log.debug("洗水壶");
            sleep(1);
            log.debug("烧开水");
            sleep(5);
        },"老王");

        Thread t2 = new Thread(() -> {
            log.debug("洗茶壶");
            sleep(1);
            log.debug("洗茶杯");
            sleep(2);
            log.debug("拿茶叶");
            sleep(1);
            try {
                t1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("泡茶");
        },"小王");

        t1.start();
        t2.start();
    }
}
123456789101112131415161718192021222324252627282930
```

输出

![image-20211014162307906](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/98dd6a5eaed053f08dfbce24dba0b8c2.png)

- 上面模拟的是小王等老王的水烧开了，小王泡茶，如果反过来要实现老王等小王的茶叶拿来了，老王泡茶呢？代码最好能适应两种情况
- 上面的两个线程其实是各执行各的，如果要模拟老王把水壶交给小王泡茶，或模拟小王把茶叶交给老王泡茶呢

# 本章小结

本章的重点在于掌握

- 线程创建
- 线程重要 api，如 start，run，sleep，join，interrupt 等
- 线程状态
- 应用方面
  - 异步调用：主线程执行期间，其它线程异步执行耗时操作
  - 提高效率：并行计算，缩短运算时间
  - 同步等待：join
  - 统筹规划：合理使用线程，得到最优效果
- 原理方面
  - 线程运行流程：栈、栈帧、上下文切换、程序计数器
  - Thread 两种创建方式 的源码
- 模式方面
  - 终止模式之两阶段终止