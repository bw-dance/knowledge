### 共享模型之内存

[toc]

> 上一章讲解的 Monitor 主要关注的是访问共享变量时，保证临界区代码的【原子性】
>
> 这一章我们进一步深入学习共享变量在多线程间的【可见性】问题与多条指令执行时的【有序性】问题

# Java 内存模型

JMM 即 Java Memory Model，它定义了**主存、工作内存**抽象概念，底层对应着 CPU 寄存器、缓存、硬件内存、CPU 指令优化等。

JMM 体现在以下几个方面

- 原子性 - 保证指令不会受到线程上下文切换的影响
- 可见性 - 保证指令不会受 cpu 缓存的影响
- 有序性 - 保证指令不会受 cpu 指令并行优化的影响

# 可见性

## 退不出的循环

先来看一个现象，main 线程对 run 变量的修改对于 t 线程不可见，导致了 t 线程无法停止：

```java
@Slf4j(topic = "c.Test32")
public class Test32 {
    // 易变
    static boolean run = true;

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(()->{
            while(run){
              
            }
        });
        t.start();

        sleep(1);
        log.debug("修改run，欲停止 t");
        run = false; // 线程t不会如预想的停下来
    }
}
123456789101112131415161718
```

输出

![image-20211101154436274](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/1e01dd1c207349ace517d6d40ae7b037.png)

为什么呢？分析一下：

1. 初始状态， t 线程刚开始从主内存读取了 run 的值到工作内存。

![image-20211101154529429](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/8938729521548ed3081f7ecf59ca6834.png)

1. 因为 t 线程要频繁从主内存中读取 run 的值，JIT 编译器会将 run 的值缓存至自己工作内存中的高速缓存中，减少对主存中 run 的访问，提高效率

![image-20211101154558567](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/8563b4852729c268c0b8b26510b7ca95.png)

1. 1 秒之后，main 线程修改了 run 的值，并同步至主存，而 t 是从自己工作内存中的高速缓存中读取这个变量的值，结果永远是旧值

![image-20211101154625104](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/ea0cfd0a53fcbb418be425ba96a7f743.png)

## 解决方法

> volatile（易变关键字）

它可以用来修饰成员变量和静态成员变量，他可以避免线程从自己的工作缓存中查找变量的值，必须到主存中获取它的值，线程操作 volatile 变量都是直接操作主存

```java
// 易变
volatile static boolean run = true;
12
```

输出（正常结束）

![image-20211101154854787](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/04e144139170b6da68f79e8f97b98aed.png)

> synchronized（比较重量级）

```java
@Slf4j(topic = "c.Test32")
public class Test32 {
    // 易变
    static boolean run = true;

    final static Object lock = new Object();
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(()->{
            while(true){
                synchronized (lock) {
                    if(!run) {
                        break;
                    }
                }
            }
        });
        t.start();

        sleep(1);
        log.debug("修改run，欲停止 t");
        synchronized (lock) {
            run = false; 
        }
    }
}
12345678910111213141516171819202122232425
```

输出

![image-20211101155305472](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/dbc2603a4151c91af535dfd121935d95.png)

## 可见性 vs 原子性

前面例子体现的实际就是可见性，它保证的是在多个线程之间，一个线程对 volatile 变量的修改对另一个线程可见， 不能保证原子性，仅用在一个写线程，多个读线程的情况： 上例从字节码理解是这样的：

```java
getstatic run // 线程 t 获取 run true 
getstatic run // 线程 t 获取 run true 
getstatic run // 线程 t 获取 run true 
getstatic run // 线程 t 获取 run true 
putstatic run // 线程 main 修改 run 为 false， 仅此一次
getstatic run // 线程 t 获取 run false
123456
```

比较一下之前我们将线程安全时举的例子：两个线程一个 i++ 一个 i-- ，只能保证看到最新值，不能解决指令交错

```java
// 假设i的初始值为0 
getstatic i // 线程2-获取静态变量i的值 线程内i=0 
getstatic i // 线程1-获取静态变量i的值 线程内i=0 
iconst_1 // 线程1-准备常量1 
iadd // 线程1-自增 线程内i=1 
putstatic i // 线程1-将修改后的值存入静态变量i 静态变量i=1 
iconst_1 // 线程2-准备常量1 
isub // 线程2-自减 线程内i=-1 
putstatic i // 线程2-将修改后的值存入静态变量i 静态变量i=-1
123456789
```

> **注意**
>
> synchronized 语句块既可以保证代码块的原子性，也同时保证代码块内变量的可见性。但缺点是synchronized 是属于重量级操作，性能相对更低

如果在前面示例的死循环中加入 System.out.println() 会发现即使不加 volatile 修饰符，线程 t 也能正确看到对 run 变量的修改了，想一想为什么？

![image-20211101160314315](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211101160314315.png)

因为方法加了synchronized。

## 模式之两阶段终止

Two Phase Termination

在一个线程 T1 中如何“优雅”终止线程 T2？这里的【优雅】指的是给 T2 一个料理后事的机会。

### 错误思路

- 使用线程对象的 stop() 方法停止线程
  - stop 方法会真正杀死线程，如果这时线程锁住了共享资源，那么当它被杀死后就再也没有机会释放锁，其它线程将永远无法获取锁
- 使用 System.exit(int) 方法停止线程
  - 目的仅是停止一个线程，但这种做法会让整个程序都停止

### 两阶段终止模式

![image-20211101161539687](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/28a50a3254f0d0bd8e835ab64d997705.png)

#### 利用 isInterrupted

interrupt 可以打断正在执行的线程，无论这个线程是在 sleep，wait，还是正常运行

```java
@Slf4j(topic = "c.TwoPhaseTermination")
public class Test13 {
    public static void main(String[] args) throws InterruptedException {
        TwoPhaseTermination tpt = new TwoPhaseTermination();
        tpt.start();

        Thread.sleep(3500);
        log.debug("停止监控");
        tpt.stop();
    }
}

@Slf4j(topic = "c.TwoPhaseTermination")
class TwoPhaseTermination {
    // 监控线程
    private Thread monitorThread;

    // 启动监控线程
    public void start() {
        monitorThread = new Thread(() -> {
            while (true) {
                Thread current = Thread.currentThread();
                // 是否被打断
                if (current.isInterrupted()) {
                    log.debug("料理后事");
                    break;
                }
                try {
                    Thread.sleep(1000);
                    log.debug("执行监控记录");
                } catch (InterruptedException e) {
                    // 因为sleep出现异常后，会清除打断标记
                    // 需要重置打断标记
                    current.interrupt();
                }
            }
        }, "monitor");
        monitorThread.start();
    }

    // 停止监控线程
    public void stop() {
        stop = true;
        monitorThread.interrupt();
    }
}
12345678910111213141516171819202122232425262728293031323334353637383940414243444546
```

输出

![image-20211101162043453](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/79b508298a8b267320827c86e46a75f1.png)

#### 利用停止标记

```java
@Slf4j(topic = "c.TwoPhaseTermination")
public class Test13 {
    public static void main(String[] args) throws InterruptedException {
        TwoPhaseTermination tpt = new TwoPhaseTermination();
        tpt.start();

        Thread.sleep(3500);
        log.debug("停止监控");
        tpt.stop();
    }
}


@Slf4j(topic = "c.TwoPhaseTermination")
class TwoPhaseTermination {
    // 监控线程
    private Thread monitorThread;
    // 停止标记
    private volatile boolean stop = false;

    // 启动监控线程
    public void start() {
        monitorThread = new Thread(() -> {
            while (true) {
                Thread current = Thread.currentThread();
                // 是否被打断
                if (stop) {
                    log.debug("料理后事");
                    break;
                }
                try {
                    Thread.sleep(1000);
                    log.debug("执行监控记录");
                } catch (InterruptedException ignored) {
                }
            }
        }, "monitor");
        monitorThread.start();
    }

    // 停止监控线程
    public void stop() {
        stop = true;
        // 直接打断，不让执行sleep
        monitorThread.interrupt();
    }
}
1234567891011121314151617181920212223242526272829303132333435363738394041424344454647
```

输出

![image-20211101162521226](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/fcfaedc0cd0eebdeddf6f8a92b900af7.png)

## 模式之 Balking

### 定义

Balking （犹豫）模式用在一个线程发现另一个线程或本线程已经做了某一件相同的事，那么本线程就无需再做了，直接结束返回

```java
public class MonitorService {
  // 用来表示是否已经有线程已经在执行启动了
  private volatile boolean starting;

  public void start() {
    log.info("尝试启动监控线程...");
    synchronized (this) {
      if (starting) {
        return;
      }
      starting = true;
    }
    // 真正启动监控线程...
  }
}
123456789101112131415
```

### 实现

如上例，如果多次调用监控线程的start方法，会创建多个监控线程，但实际上，监控线程一个就够了。

```java
@Slf4j(topic = "c.TwoPhaseTermination")
public class Test13 {
    public static void main(String[] args) throws InterruptedException {
        TwoPhaseTermination tpt = new TwoPhaseTermination();
        tpt.start();
        tpt.start();
        tpt.start();

        Thread.sleep(3500);
        log.debug("停止监控");
        tpt.stop();
    }
}
12345678910111213
```

当前端页面多次点击按钮调用 start 时

输出

![image-20211101163930609](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/d6d2a2daefb5d15f8167ad3bf88bb458.png)

修改后

```java
@Slf4j(topic = "c.TwoPhaseTermination")
public class Test13 {
    public static void main(String[] args) throws InterruptedException {
        TwoPhaseTermination tpt = new TwoPhaseTermination();
        tpt.start();
        tpt.start();
        tpt.start();

        Thread.sleep(3500);
        log.debug("停止监控");
        tpt.stop();
    }
}

@Slf4j(topic = "c.TwoPhaseTermination")
class TwoPhaseTermination {
    // 监控线程
    private Thread monitorThread;
    // 停止标记
    private volatile boolean stop = false;
    // 判断是否执行过 start 方法
    private boolean starting = false;

    // 启动监控线程
    public void start() {
        synchronized (this) {
            if (starting) {  // false
                return;
            }
            starting = true;
        }

        monitorThread = new Thread(() -> {
            while (true) {
                Thread current = Thread.currentThread();
                // 是否被打断
                if (stop) {
                    log.debug("料理后事");
                    break;
                }
                try {
                    Thread.sleep(1000);
                    log.debug("执行监控记录");
                } catch (InterruptedException ignored) {
                }
            }
        }, "monitor");
        monitorThread.start();
    }

    // 停止监控线程
    public void stop() {
        stop = true;
        // 直接打断，不让执行sleep
        monitorThread.interrupt();
    }
}
123456789101112131415161718192021222324252627282930313233343536373839404142434445464748495051525354555657
```

输出

![image-20211101164054970](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/ab4db9bcb619a12d2fc0304a37aff69d.png)

它还经常用来实现线程安全的单例

```java
public final class Singleton {

  private Singleton() {
  }

  private static Singleton INSTANCE = null;
  
  public static synchronized Singleton 
    getInstance() {
    if (INSTANCE != null) {
      return INSTANCE;
    }
    
    INSTANCE = new Singleton();

    return INSTANCE;
  }
}
123456789101112131415161718
```

对比一下保护性暂停模式：保护性暂停模式用在一个线程等待另一个线程的执行结果，当条件不满足时线程等待。

# 有序性

JVM 会在不影响正确性的前提下，可以调整语句的执行顺序，思考下面一段代码

```java
static int i;
static int j;

// 在某个线程内执行如下赋值操作
i = ...; 
j = ...;
123456
```

可以看到，至于是先执行 i 还是 先执行 j ，对最终的结果不会产生影响。所以，上面代码真正执行时，既可以是

```java
i = ...; 
j = ...;
12
```

也可以是

```java
j = ...;
i = ...;
12
```

这种特性称之为『指令重排』，多线程下『指令重排』会影响正确性。为什么要有重排指令这项优化呢？从 CPU执行指令的原理来理解一下吧

## 原理之指令级并行

### 名词

**Clock Cycle Time**

主频的概念大家接触的比较多，而 CPU 的 Clock Cycle Time（时钟周期时间），等于主频的倒数，意思是 CPU 能够识别的最小时间单位，比如说 4G 主频的 CPU 的 Clock Cycle Time 就是 0.25 ns，作为对比，我们墙上挂钟的Cycle Time 是 1s

例如，运行一条加法指令一般需要一个时钟周期时间

**CPI**

有的指令需要更多的时钟周期时间，所以引出了 CPI （Cycles Per Instruction）指令平均时钟周期数

**IPC**

IPC（Instruction Per Clock Cycle） 即 CPI 的倒数，表示每个时钟周期能够运行的指令数

**CPU** **执行时间**

程序的 CPU 执行时间，即我们前面提到的 user + system 时间，可以用下面的公式来表示

```java
程序 CPU 执行时间 = 指令数 * CPI * Clock Cycle Time
1
```

### 鱼罐头的故事

加工一条鱼需要 50 分钟，只能一条鱼、一条鱼顺序加工…

![image-20211101173341148](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/57277649e71290ca8ff066e175dc1e2c.png)

可以将每个鱼罐头的加工流程细分为 5 个步骤：

- 去鳞清洗 10分钟
- 蒸煮沥水 10分钟
- 加注汤料 10分钟
- 杀菌出锅 10分钟
- 真空封罐 10分钟

![image-20211101173523995](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/a2a3b17a501f75aaf058b8479659a3a2.png)

即使只有一个工人，最理想的情况是：他能够在 10 分钟内同时做好这 5 件事，因为对第一条鱼的真空装罐，不会影响对第二条鱼的杀菌出锅…

### 指令重排序优化

事实上，现代处理器会设计为一个时钟周期完成一条执行时间最长的 CPU 指令。为什么这么做呢？可以想到指令还可以再划分成一个个更小的阶段，例如，每条指令都可以分为：

取指令 - 指令译码 - 执行指令 - 内存访问 - 数据写回

这 5 个阶段

![image-20211101173620800](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/a6bc6df3f958db9e3113f939ceb7db93.png)

**术语参考：**

- instruction fetch (IF)
- instruction decode (ID)
- execute (EX)
- memory access (MEM)
- register write back (WB)

在不改变程序结果的前提下，这些指令的各个阶段可以通过**重排序**和**组合**来实现**指令级并行**，这一技术在 80’s 中叶到 90’s 中叶占据了计算架构的重要地位。

> **提示：**
>
> 分阶段，分工是提升效率的关键！

指令重排的前提是，重排指令不能影响结果，例如

```java
// 可以重排的例子
int a = 10; // 指令1
int b = 20; // 指令2
System.out.println( a + b );

// 不能重排的例子
int a = 10; // 指令1
int b = a - 5; // 指令2
12345678
```

### 支持流水线的处理器

现代 CPU 支持**多级指令流水线**，例如支持同时执行 取指令 - 指令译码 - 执行指令 - 内存访问 - 数据写回 的处理器，就可以称之为**五级指令流水线**。这时 CPU 可以在一个时钟周期内，同时运行五条指令的不同阶段（相当于一条执行时间最长的复杂指令），IPC = 1，本质上，流水线技术并不能缩短单条指令的执行时间，但它变相地提高了指令地吞吐率。

> **提示：**
>
> 奔腾四（Pentium 4）支持高达 35 级流水线，但由于功耗太高被废弃

![image-20211101173910706](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/e252f732414f16134b75fff96f481a20.png)

### SuperScalar 处理器

大多数处理器包含多个执行单元，并不是所有计算功能都集中在一起，可以再细分为整数运算单元、浮点数运算单元等，这样可以把多条指令也可以做到并行获取、译码等，CPU 可以在一个时钟周期内，执行多于一条指令，IPC> 1

![image-20211101173954940](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/58078a480561fd4c3b86ca62df8ffd10.png)

![image-20211101174009148](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/5befe048c825e51662b12bc6e32754d4.png)

## 诡异的结果

```java
int num = 0;
boolean ready = false;

// 线程1 执行此方法
public void actor1(I_Result r) {
    if(ready) {
        r.r1 = num + num;
    } else {
        r.r1 = 1;
    }
}

// 线程2 执行此方法
public void actor2(I_Result r) {
    num = 2;
    ready = true;
}
1234567891011121314151617
```

I_Result 是一个对象，有一个属性 r1 用来保存结果，问，可能的结果有几种？

有同学这么分析

- 情况1：线程1 先执行，这时 ready = false，所以进入 else 分支结果为 1
- 情况2：线程2 先执行 num = 2，但没来得及执行 ready = true，线程1 执行，还是进入 else 分支，结果为1
- 情况3：线程2 执行到 ready = true，线程1 执行，这回进入 if 分支，结果为 4（因为 num 已经执行过了）

但我告诉你，结果还有可能是 0 ??????，信不信吧！

这种情况下是：线程2 执行 ready = true，切换到线程1，进入 if 分支，相加为 0，再切回线程2 执行 num = 2

相信很多人已经晕了 ??????

这种现象叫做指令重排，是 JIT 编译器在运行时的一些优化，这个现象需要通过大量测试才能复现：借助 java 并发压测工具 jcstress https://wiki.openjdk.java.net/display/CodeTools/jcstress

```bash
mvn archetype:generate -DinteractiveMode=false -DarchetypeGroupId=org.openjdk.jcstress -DarchetypeArtifactId=jcstress-java-test-archetype -DarchetypeVersion=0.5 -DgroupId=cn.itcast -DartifactId=ordering -Dversion=1.0
1
```

创建 maven 项目，提供如下测试类

```java
@JCStressTest
@Outcome(id = {"1", "4"}, expect = Expect.ACCEPTABLE, desc = "ok")
@Outcome(id = "0", expect = Expect.ACCEPTABLE_INTERESTING, desc = "!!!!")
@State
public class ConcurrencyTest {

    int num = 0;
    volatile boolean ready = false;
    @Actor
    public void actor1(I_Result r) {
        if(ready) {
            r.r1 = num + num;
        } else {
            r.r1 = 1;
        }
    }

    @Actor
    public void actor2(I_Result r) {
        num = 2;
        ready = true;
    }

}
123456789101112131415161718192021222324
```

执行

```bash
mvn clean install 
java -jar target/jcstress.jar
12
```

会输出我们感兴趣的结果，摘录其中一次结果：

![image-20211101175542529](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/d8463cd4faa00f1c9b099a9dd53bacc0.png)

可以看到，出现结果为 0 的情况虽然次数相对很少，但毕竟是出现了。

## 解决方法

> volatile 修饰的变量，可以禁用指令重排

```java
volatile boolean ready = false;
1
```

结果为：

![image-20211101180146082](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/91751a06c45484925adefb82202befa2.png)

## 原理之 volatile

volatile 的底层实现原理是内存屏障，Memory Barrier（Memory Fence）

- 对 volatile 变量的写指令后会加入写屏障
- 对 volatile 变量的读指令前会加入读屏障

### 如何保证可见性

写屏障（sfence）保证在该屏障之前的，对共享变量的改动，都同步到主存当中

```java
public void actor2(I_Result r) {
    num = 2;
    ready = true; // ready 是 volatile 赋值带写屏障
    // 写屏障
}
12345
```

而读屏障（lfence）保证在该屏障之后，对共享变量的读取，加载的是主存中最新数据

```java
public void actor1(I_Result r){
				// 读屏障
				// ready 是 volatile 读取值带读屏障
        if(ready){
            r.r1=num+num;
        }else{
            r.r1=1;
        }
    }
123456789
```

![image-20211102084207713](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/5ebc6c1b455ad92bb6b9b90dec930703.png)

### 如何保证有序性

写屏障会确保指令重排序时，不会将写屏障之前的代码排在写屏障之后

```java
public void actor2(I_Result r) {
    num = 2;
    ready = true; // ready 是 volatile 赋值带写屏障
    // 写屏障
}
12345
```

读屏障会确保指令重排序时，不会将读屏障之后的代码排在读屏障之前

```java
public void actor1(I_Result r){
				// 读屏障
				// ready 是 volatile 读取值带读屏障
        if(ready){
            r.r1=num+num;
        }else{
            r.r1=1;
        }
    }
123456789
```

![image-20211102084657293](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/c0db8db7287bc3b0cec83b1d18c69026.png)

还是那句话，不能解决指令交错：

- 写屏障仅仅是保证之后的读能够读到最新的结果，但不能保证读跑到它前面去
- 而有序性的保证也只是保证了本线程内相关代码不被重排序
- 不同线程之前代码的执行先后顺序无法控制，由cpu时间片影响。即无法保证原子性。
- volatile保证可见性、有序性，无法保证原子性。

![image-20211102084839501](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/ed0a42cfc31daac02795f8f4648b7422.png)

### double-checked locking 问题

以著名的 double-checked locking 单例模式为例

```java
public final class Singleton {
    private Singleton() { }
    private static Singleton INSTANCE = null;
    public static Singleton getInstance() {
        if(INSTANCE == null) { // t2
						// 首次访问会同步，而之后的使用没有 synchronized
            synchronized(Singleton.class) {
                if (INSTANCE == null) { // t1
                    INSTANCE = new Singleton();
                }
            }
        }
        return INSTANCE;
    }
}
123456789101112131415
```

以上的实现特点是：

- 懒惰实例化
- 首次使用 getInstance() 才使用 synchronized 加锁，后续使用时无需加锁
- 有隐含的，但很关键的一点：第一个 if 使用了 INSTANCE 变量，是在同步块之外

但在多线程环境下，上面的代码是有问题的，getInstance 方法对应的字节码为：

![image-20211102090804042](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/b55cd488c1fb8d06f225a682fc82d4c6.png)

其中

- 17 表示创建对象，将对象引用入栈 // new Singleton
- 20 表示复制一份对象引用 // 引用地址
- 21 表示利用一个对象引用，调用构造方法
- 24 表示利用一个对象引用，赋值给 static INSTANCE

也许 jvm 会优化为：先执行 24，再执行 21。如果两个线程 t1，t2 按如下时间序列执行：

![image-20211102090905341](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/dddc9c2e1c36b373aa2da71c9f3945a2.png)

- 关键在于 0: getstatic 这行代码在 monitor 控制之外，它就像之前举例中不守规则的人，可以越过 monitor 读取INSTANCE 变量的值
- 这时 t1 还未完全将构造方法执行完毕，如果在构造方法中要执行很多初始化操作，那么 t2 拿到的是将是一个未初始化完毕的单例
- 对 INSTANCE 使用 volatile 修饰即可，可以禁用指令重排，但要注意在 JDK 5 以上的版本的 volatile 才会真正有效

### double-checked locking 解决

```java
public final class Singleton {
    private Singleton() { }
    private static volatile Singleton INSTANCE = null;
    public static Singleton getInstance() {
        // 实例没创建，才会进入内部的 synchronized代码块
        if (INSTANCE == null) {
            synchronized (Singleton.class) { // t2
                // 也许有其它线程已经创建实例，所以再判断一次
                if (INSTANCE == null) { // t1
                    INSTANCE = new Singleton();
                }
            }
        }
        return INSTANCE;
    }
}
12345678910111213141516
```

字节码上看不出来 volatile 指令的效果

![image-20211102091820047](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/25a3094ba0239c5bbbc53c5a6541bc77.png)

如上面的注释内容所示，读写 volatile 变量时会加入内存屏障（Memory Barrier（Memory Fence）），保证下面两点：

- 可见性
  - 写屏障（sfence）保证在该屏障之前的 t1 对共享变量的改动，都同步到主存当中
  - 而读屏障（lfence）保证在该屏障之后 t2 对共享变量的读取，加载的是主存中最新数据
- 有序性
  - 写屏障会确保指令重排序时，不会将写屏障之前的代码排在写屏障之后
  - 读屏障会确保指令重排序时，不会将读屏障之后的代码排在读屏障之前
- 更底层是读写变量时使用 lock 指令来多核 CPU 之间的可见性与有序性

![image-20211102091629284](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/d649a557a76739d46bd8068e1c915b12.png)

有了写屏障，21行无法到24行下面去。

如果是0行代码跑到24之前去执行，即t2线程打断了t1线程去执行，发生指令交错。如下图

![image-20211102092015632](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/dca492b420d407bb98874a81eba6655a.png)

那也是没问题的，因为此时INSTANCE == null，往下执行，被挡在了synchronized那里，等待t1执行完（t1有锁）。

可能会有疑问，为什么加了synchronized或者volatile之后，会发生指令重排了。

1. INSTANCE没有完全受synchronized保护，还有一个if(INSTANCE==null)在代码块之外，其他线程自然能去执行到，又不会被挡住。
2. volatile保证有序性，也只是保证**本线程**的相关代码的有序，可以看到，t1、t2线程的相关代码都是有序的，但是线程之间进行了指令交错。

### happens-before

happens-before 规定了对共享变量的写操作对其它线程的读操作可见，它是可见性与有序性的一套规则总结，抛开以下 happens-before 规则，JMM 并不能保证一个线程对共享变量的写，对于其它线程对该共享变量的读可见线程

- 解锁 m 之前对变量的写，对于接下来对 m 加锁的其它线程对该变量的读可见

```java
static int x;
static Object m = new Object();
new Thread(()->{
    synchronized(m) {
        x = 10;
    }
},"t1").start();
new Thread(()->{
    synchronized(m) {
        System.out.println(x);
    }
},"t2").start();
123456789101112
```

- 线程对 volatile 变量的写，对接下来其它线程对该变量的读可见

```java
volatile static int x;
new Thread(()->{ x = 10;
},"t1").start();
new Thread(()->{
    System.out.println(x);
},"t2").start();
123456
```

- 线程 start 前对变量的写，对该线程开始后对该变量的读可见

```java
static int x; x = 10;
new Thread(()->{
    System.out.println(x);
},"t2").start();
1234
```

- 线程结束前对变量的写，对其它线程得知它结束后的读可见（比如其它线程调用 t1.isAlive() 或 t1.join()等待它结束）

```java
static int x;
Thread t1 = new Thread(()->{ x = 10;
},"t1");
t1.start();
t1.join();
System.out.println(x);
123456
```

- 线程 t1 打断 t2（interrupt）前对变量的写，对于其他线程得知 t2 被打断后对变量的读可见（通过t2.interrupted 或 t2.isInterrupted）

```java
static int x;
public static void main(String[] args) {
    Thread t2 = new Thread(()->{
        while(true) {
            if(Thread.currentThread().isInterrupted()) {
                System.out.println(x);
                break;
            }
        }
    },"t2");
    t2.start();
    new Thread(()->{
        sleep(1);
        x = 10;
        t2.interrupt();
    },"t1").start();
    while(!t2.isInterrupted()) {
        Thread.yield();
    }
    System.out.println(x);
}
123456789101112131415161718192021
```

- 对变量默认值（0，false，null）的写，对其它线程对该变量的读可见
- 具有传递性，如果 x hb-> y 并且 y hb-> z 那么有 x hb-> z ，配合 volatile 的防指令重排，有下面的例子

```java
volatile static int x;
static int y;

new Thread(()->{ 
  y = 10; 
  x = 20;
},"t1").start();

new Thread(()->{
  // x=20 对 t2 可见, 同时 y=10 也对 t2 可见
  System.out.println(x); 
},"t2").start();
123456789101112
```

> 变量都是指成员变量或静态成员变量

## 习题

### balking 模式习题

希望 doInit() 方法仅被调用一次，下面的实现是否有问题，为什么？

```java
public class TestVolatile {
    volatile boolean initialized = false;
    void init() {
        if (initialized) {
            return;
        }
        doInit();
        initialized = true;
    }
    private void doInit() {
    }
}
123456789101112
```

- 有问题，init()方法中，涉及到共享变量initialized的读和写操作，要保证其原子性，应该用synchronized

### 线程安全单例习题

单例模式有很多实现方法，饿汉、懒汉、静态内部类、枚举类，试分析每种实现下获取单例对象（即调用getInstance）时的线程安全，并思考注释中的问题

> 饿汉式：类加载就会导致该单实例对象被创建
>
> 懒汉式：类加载不会导致该单实例对象被创建，而是首次使用该对象时才会创建

#### 实现一（饿汉式）

```java
// 问题1：为什么加 final
/* 防止子类覆盖方法，导致单例被破坏*/

// 问题2：如果实现了序列化接口, 还要做什么来防止反序列化破坏单例
/* 如下，声明readResolve()方法，直接返回已有的INSTANCE*/
public final class Singleton implements Serializable {
    // 问题3：为什么设置为私有? 是否能防止反射创建新的实例?
  	/* 防止其他地方直接调用构造方法，破坏单例。无法防止反射*/
    private Singleton() {}
  
    // 问题4：这样初始化是否能保证单例对象创建时的线程安全?
  	/* 可以保证，静态final变量的初始化，是在类加载时期完成，由jvm控制其线程安全*/
    private static final Singleton INSTANCE = new Singleton();
  
    // 问题5：为什么提供静态方法而不是直接将 INSTANCE 设置为 public, 说出你知道的理由
  	/* 可以扩展其他操作，如加泛型、实现懒汉式等*/
    public static Singleton getInstance() {
        return INSTANCE;
    }
    public Object readResolve() {
        return INSTANCE;
    }
}
```

静态成员变量的初始化是在类加载时期，属于饿汉式。

#### 实现二（枚举类）

```java
// 问题1：枚举单例是如何限制实例个数的 : INSTANCE是静态成员变量，类加载时就初始化
// 问题2：枚举单例在创建时是否有并发问题 ： 没有
// 问题3：枚举单例能否被反射破坏单例 ： 不能
// 问题4：枚举单例能否被反序列化破坏单例 ： 不能
// 问题5：枚举单例属于懒汉式还是饿汉式 ：饿汉式（静态成员变量初始化）
// 问题6：枚举单例如果希望加入一些单例创建时的初始化逻辑该如何做 ：添加构造方法
enum Singleton { 
	INSTANCE; 
}
```

饿汉式

#### 实现三（懒汉式）

```java
public final class Singleton {
    private Singleton() { }
    private static Singleton INSTANCE = null;
    // 分析这里的线程安全, 并说明有什么缺点
  	/* 线程安全的，但是每次调用都会加锁，性能不好*/
    public static synchronized Singleton getInstance() {
        if( INSTANCE != null ){
            return INSTANCE;
        }
        INSTANCE = new Singleton();
        return INSTANCE;
    }
}
12345678910111213
```

#### 实现四（DCL）

```java
public final class Singleton {
    private Singleton() { }
    // 问题1：解释为什么要加 volatile ?
  	/* 保证该线程的相关代码的有序性*/
    private static volatile Singleton INSTANCE = null;
  
    // 问题2：对比实现3, 说出这样做的意义
  	/* 不用每次调用此方法都加锁，性能提升*/
    public static Singleton getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        synchronized (Singleton.class) {
          
// 问题3：为什么还要在这里加为空判断, 之前不是判断过了吗
/* 保证线程安全，t1线程进入且加锁后，但是还没有完成INSTANCE的创建。t2就能到synchronized块等待，等t1创建好了INSTANCE，t2进入synchronized，如果不判断，那么又会创建新的INSTANCE。*/
            if (INSTANCE != null) { // t2 
                return INSTANCE;
            }
            INSTANCE = new Singleton();
            return INSTANCE;
        }
    }
}
```

#### 实现五（静态内部类）

```java
public final class Singleton {
    private Singleton() { }
    // 问题1：属于懒汉式还是饿汉式
  	/* 懒汉式，类加载是懒惰的，用到这个类才会去加载*/
    private static class LazyHolder {
        static final Singleton INSTANCE = new Singleton();
    }
    // 问题2：在创建时是否有并发问题
  	/* 没有，INSTANCE是LazyHolder的静态成员变量，类加载的时候初始化，由jvm保证线程安全*/
    public static Singleton getInstance() {
        return LazyHolder.INSTANCE;
    }
}
```

# 本章小结

本章重点讲解了 JMM 中的

- 可见性 - 由 JVM 缓存优化引起
- 有序性 - 由 JVM 指令重排序优化引起
- happens-before 规则

原理方面

- CPU 指令并行
- volatile

模式方面

- 两阶段终止模式的 volatile 改进
- 同步模式之 balking