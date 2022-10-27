### 共享模型之管程

- [本章内容](#_3)
- [共享带来的问题](#_16)
- - [小故事](#_18)
  - [Java 的体现](#Java__52)
  - [问题分析](#_84)
  - [临界区 Critical Section](#_Critical_Section_116)
  - [竞态条件 Race Condition](#_Race_Condition_149)
- [synchronized 解决方案](#synchronized__155)
- - [synchronized](#synchronized_173)
  - [思考](#_241)
  - [面向对象改进](#_261)
- [方法上的 synchronized](#_synchronized_309)
- - [不加 synchronized 的方法](#_synchronized__349)
  - [所谓的“线程八锁”](#_355)
- [变量的线程安全分析](#_680)
- - [成员变量和静态变量是否线程安全？](#_682)
  - [局部变量是否线程安全？](#_692)
  - [局部变量线程安全分析](#_704)
  - - [list是成员变量](#list_725)
    - [list 修改为局部变量](#list__780)
    - [暴露list](#list_817)
  - [常见线程安全类](#_909)
  - - [线程安全类方法的组合](#_937)
    - [不可变类线程安全性](#_953)
  - [实例分析](#_1000)
- [习题](#_1230)
- - [卖票练习](#_1232)
  - - [解决方法](#_1348)
  - [转账练习](#_1391)
  - - [解决方法](#_1499)



# 本章内容

- 共享问题
- synchronized
- 线程安全分析
- Monitor
- wait/notify
- 线程状态转换
- 活跃性
- Lock

# 共享带来的问题

## 小故事

- 老王（操作系统）有一个功能强大的算盘（CPU），现在想把它租出去，赚一点外快

![image-20211019203141258](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/03a55d59070fb4addbf41b94d704a638.png)

- 小南、小女（线程）来使用这个算盘来进行一些计算，并按照时间给老王支付费用
- 但小南不能一天24小时使用算盘，他经常要小憩一会（sleep），又或是去吃饭上厕所（阻塞 io 操作），有时还需要一根烟，没烟时思路全无（wait）这些情况统称为（阻塞）

![image-20211019203230118](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/e94f543fe418dc2cdec1f289f0a5f9ea.png)

- 在这些时候，算盘没利用起来（不能收钱了），老王觉得有点不划算
- 另外，小女也想用用算盘，如果总是小南占着算盘，让小女觉得不公平
- 于是，老王灵机一动，想了个办法 [ 让他们每人用一会，轮流使用算盘 ]
- 这样，当小南阻塞的时候，算盘可以分给小女使用，不会浪费，反之亦然
- 最近执行的计算比较复杂，需要存储一些中间结果，而学生们的脑容量（工作内存）不够，所以老王申请了一个笔记本（主存），把一些中间结果先记在本上
- 计算流程是这样的

![image-20211019203308183](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/5bf6a840f4b4b1c0562a619d7ff687fc.png)

- 但是由于分时系统，有一天还是发生了事故
- 小南刚读取了初始值 0 做了个 +1 运算，还没来得及写回结果
- 老王说 [ 小南，你的时间到了，该别人了，记住结果走吧 ]，于是小南念叨着 [ 结果是1，结果是1…] 不甘心地到一边待着去了（上下文切换）
- 老王说 [ 小女，该你了 ]，小女看到了笔记本上还写着 0 做了一个 -1 运算，将结果 -1 写入笔记本
- 这时小女的时间也用完了，老王又叫醒了小南：[小南，把你上次的题目算完吧]，小南将他脑海中的结果 1 写入了笔记本

![image-20211019203347846](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/bb031f1c1d256d0eddc22ebc551e21da.png)

小南和小女都觉得自己没做错，但笔记本里的结果是 1 而不是 0

## Java 的体现

两个线程对初始值为 0 的静态变量一个做自增，一个做自减，各做 5000 次，结果是 0 吗？

```java
@Slf4j(topic = "c.Test17")
public class Test17 {
    static int count = 0;
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                count++;
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                count--;
            }
        }, "t2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        log.debug("{}", count);
    }
}
1234567891011121314151617181920212223
```

## 问题分析

以上的结果可能是正数、负数、零。为什么呢？因为 Java 中对静态变量的自增，自减并不是原子操作，要彻底理解，必须从字节码来进行分析

例如对于 i++ 而言（i 为静态变量），实际会产生如下的 JVM 字节码指令：

![image-20211019204243041](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/4f70814563d04c71cc56c355d64a05ff.png)

而对应 i-- 也是类似：

![image-20211019204302835](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/12e45bcb8ee19d00c4b1f50a8b1d84cd.png)

而 Java 的内存模型如下，完成静态变量的自增，自减需要在主存和工作内存中进行数据交换：

![image-20211019204324457](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/e1c5c74552f5f2e8768c9c3c06743e11.png)

如果是单线程以上 8 行代码是顺序执行（不会交错）没有问题：

![image-20211019204441023](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/2371f30426ac85c2b013a669d04ce5b9.png)

但多线程下这 8 行代码可能交错运行：

出现负数的情况：

![image-20211019204508037](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/41096e6e529f71f2d6672055616b3d18.png)

出现正数的情况：

![image-20211019204527311](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/16d227366ada1489a2c97707b9a1ba6d.png)

## 临界区 Critical Section

- 一个程序运行多个线程本身是没有问题的

- 问题出在多个线程访问**共享资源**

  多个线程读**共享资源**其实也没有问题

  在多个线程对**共享资源**读写操作时发生指令交错，就会出现问题

- 一段代码块内如果存在对**共享资源**的多线程读写操作，称这段代码块为**临界区**

例如，下面代码中的临界区

```java
static int counter = 0;
static void increment() 
// 临界区
{ 
	counter++; 
}

static void decrement() 
// 临界区
{ 
	counter--; 
}
123456789101112
```

## 竞态条件 Race Condition

多个线程在临界区内执行，由于代码的**执行序列不同**而导致结果无法预测，称之为发生了**竞态条件**

# synchronized 解决方案

为了避免临界区的竞态条件发生，有多种手段可以达到目的。

- 阻塞式的解决方案：synchronized，Lock
- 非阻塞式的解决方案：原子变量

本次使用阻塞式的解决方案：synchronized，来解决上述问题，即俗称的【对象锁】，它采用互斥的方式让同一时刻至多只有一个线程能持有【对象锁】，其它线程再想获取这个【对象锁】时就会阻塞住。这样就能保证拥有锁的线程可以安全的执行临界区内的代码，不用担心线程上下文切换

> **注意**
>
> 虽然 java 中互斥和同步都可以采用 synchronized 关键字来完成，但它们还是有区别的：
>
> 1. 互斥是保证临界区的竞态条件发生，同一时刻只能有一个线程执行临界区代码
> 2. 同步是由于线程执行的先后、顺序不同、需要一个线程等待其它线程运行到某个点

## synchronized

语法

```java
synchronized(对象) // 线程1， 线程2(blocked)
{
	临界区
}
1234
```

解决

```java
public class Test17 {
    static int count = 0;
    static final Object room = new Object();
    
  public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                synchronized (room){
                    count++;
                }
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                synchronized (room){
                    count--;
                }
            }
        }, "t2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        log.debug("{}", count);
    }
}
12345678910111213141516171819202122232425262728
```

![image-20211019210150030](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/72efc952abe9277aa475048fc554aaa2.png)

可以做这样的类比：

- synchronized(对象) 中的对象，可以想象为一个房间（room），有唯一入口（门）房间只能一次进入一人进行计算，线程 t1，t2 想象成两个人
- 当线程 t1 执行到 synchronized(room) 时就好比 t1 进入了这个房间，并锁住了门拿走了钥匙，在门内执行count++ 代码
- 这时候如果 t2 也运行到了 synchronized(room) 时，它发现门被锁住了，只能在门外等待，发生了上下文切换，阻塞住了
- 这中间即使 t1 的 cpu 时间片不幸用完，被踢出了门外（不要错误理解为锁住了对象就能一直执行下去哦），这时门还是锁住的，t1 仍拿着钥匙，t2 线程还在阻塞状态进不来，只有下次轮到 t1 自己再次获得时间片时才能开门进入
- 当 t1 执行完 synchronized{} 块内的代码，这时候才会从 obj 房间出来并解开门上的锁，唤醒 t2 线程把钥匙给他。t2 线程这时才可以进入 obj 房间，锁住了门拿上钥匙，执行它的 count-- 代码

用图来表示

![image-20211019210443976](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/6ae9f7cea2ec147f42029846ec1d153b.png)

## 思考

synchronized 实际是用**对象锁**保证了**临界区内代码的原子性**，临界区内的代码对外是不可分割的，不会被线程切换所打断。

为了加深理解，请思考下面的问题

- 如果把 synchronized(obj) 放在 for 循环的外面，如何理解？-- 原子性

  对整个循环内5000次操作都上锁了。保证了原子性

- 如果 t1 synchronized(obj1) 而 t2 synchronized(obj2) 会怎样运作？-- 锁对象

  对象不同，相当于拿到的锁不同，不能保证原子性。

- 如果 t1 synchronized(obj) 而 t2 没有加会怎么样？如何理解？-- 锁对象

  对于同一个共享资源，所有对其的操作都要加锁，不然t1加锁，但是t2不加锁，那么t2运行到临界区时，就不会去尝试拿锁，自然不会被阻塞。

## 面向对象改进

把需要保护的共享变量放入一个类

```java
@Slf4j(topic = "c.Test17")
public class Test17 {
    public static void main(String[] args) throws InterruptedException {
        Room room = new Room();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                room.increment();
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                room.decrement();
            }
        }, "t2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        log.debug("{}", room.getCounter());
    }
}

class Room {
    private int counter = 0;

    public synchronized void increment() {
        counter++;
    }

    public synchronized void decrement() {
        counter--;
    }

    public synchronized int getCounter() {
        return counter;
    }
}
123456789101112131415161718192021222324252627282930313233343536373839
```

# 方法上的 synchronized

```java
class Test{
	public synchronized void test() {
 
  }
}

// 等价于
class Test{
	public void test() {
		synchronized(this) {
 		
    }
 	}
}
1234567891011121314
class Test{
  public synchronized static void test() {
 
  }
}

// 等价于
class Test{
  public static void test() {
    synchronized(Test.class) {
      
    }
  }
}
1234567891011121314
```

## 不加 synchronized 的方法

不加 synchronzied 的方法就好比不遵守规则的人，不去老实排队（好比翻窗户进去的）

## 所谓的“线程八锁”

其实就是考察 synchronized 锁住的是哪个对象

> 情况1：12 或 21

```java
@Slf4j(topic = "c.Test8Locks")
public class Test8Locks {
    public static void main(String[] args) {
        Number n1 = new Number();
        new Thread(() -> {
            log.debug("begin");
            n1.a();
        }).start();
        new Thread(() -> {
            log.debug("begin");
            n1.b();
        }).start();
    }
}
@Slf4j(topic = "c.Number")
class Number{
    public synchronized void a() {
        log.debug("1");
    }
    public synchronized void b() {
        log.debug("2");
    }
}
1234567891011121314151617181920212223
```

![image-20211020153709101](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/d78a6d2a3bc3d564b2522871fb6375d9.png)

> 情况2：1s后12，或 2 1s后 1

```java
@Slf4j(topic = "c.Test8Locks")
public class Test8Locks {
    public static void main(String[] args) {
        Number n1 = new Number();
        new Thread(() -> {
            log.debug("begin");
            n1.a();
        }).start();
        new Thread(() -> {
            log.debug("begin");
            n1.b();
        }).start();
    }
}
@Slf4j(topic = "c.Number")
class Number{
    public synchronized void a() {
        sleep(1);
        log.debug("1");
    }
    public synchronized void b() {
        log.debug("2");
    }
}
123456789101112131415161718192021222324
```

![image-20211020154147863](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/0b6eb647acfb85c14c4e702850fa7723.png)

![image-20211020154303179](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/00c9c5577d677e458cea812d540b02d7.png)

- a方法睡了1秒。
- 可能是线程1先拿到锁，执行a方法，但是睡了1秒，然后打印1，接着马上打印2。所以结果12
- 可能线程2先拿到锁，执行b方法打印a，然后t1线程马上执行a方法，先睡1秒，然后打印1。所以结果2 1s后 1

> 情况3：3 1s 12 或 23 1s 1 或 32 1s 1

```java
@Slf4j(topic = "c.Test8Locks")
public class Test8Locks {
    public static void main(String[] args) {
        Number n1 = new Number();
        new Thread(() -> {
            log.debug("begin");
            n1.a();
        }).start();
        new Thread(() -> {
            log.debug("begin");
            n1.b();
        }).start();
        new Thread(() -> {
            log.debug("begin");
            n1.c();
        }).start();
    }
}
@Slf4j(topic = "c.Number")
class Number{
    public synchronized void a() {
        sleep(1);
        log.debug("1");
    }
    public synchronized void b() {
        log.debug("2");
    }
    public  void c() {
        log.debug("3");
    }
}
12345678910111213141516171819202122232425262728293031
```

![image-20211020155310634](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/d13497240f005d42d1b9a4d4eb0e5c69.png)

![image-20211020155446999](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/97442c3f0935f99042c1bbf216ef874a.png)

![image-20211020155618895](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/3e3dc2801a23710c84cdb563ab70b1ad.png)

- c方法没有synchronized，不会被锁住，所以3一定会在1之前打印（因为1会先睡1秒）。
- 打印2或3的顺序不一定，看谁先分到时间片执行

> 情况4：2 1s 后 1

```java
@Slf4j(topic = "c.Test8Locks")
public class Test8Locks {
    public static void main(String[] args) {
        Number n1 = new Number();
        Number n2 = new Number();
        new Thread(() -> {
            log.debug("begin");
            n1.a();
        }).start();
        new Thread(() -> {
            log.debug("begin");
            n2.b();
        }).start();

    }
}
@Slf4j(topic = "c.Number")
class Number{
    public synchronized void a() {
        sleep(1);
        log.debug("1");
    }
    public synchronized void b() {
        log.debug("2");
    }

}
123456789101112131415161718192021222324252627
```

![image-20211020160012901](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/ef3f8ac8fb773f4c4d02c36536d9afbe.png)

- 两个方法锁的不是同一个对象，线程1锁的是n1，线程2是n2。
- 由于线程1打印1之前始终要睡1s。而线程2由于不用等1的锁，所以可以直接打印2。所以始终是打印2的1s后打印1。

> 情况5：2 1s 后 1

```java
@Slf4j(topic = "c.Test8Locks")
public class Test8Locks {
    public static void main(String[] args) {
        Number n1 = new Number();
        new Thread(() -> {
            log.debug("begin");
            n1.a();
        }).start();
        new Thread(() -> {
            log.debug("begin");
            n1.b();
        }).start();

    }
}
@Slf4j(topic = "c.Number")
class Number{
    public static synchronized void a() {
        sleep(1);
        log.debug("1");
    }
    public synchronized void b() {
        log.debug("2");
    }

}
1234567891011121314151617181920212223242526
```

![image-20211020160352775](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/ce562889df3daa279397df0660bd0d66.png)

- a方法是static静态方法，锁的对象是Number.class
- b方法是成员方法，锁的对象的this
- 两者锁的对象不同，由于a打印1之前始终要睡1s。所以结果为2的1s后1

> 情况6：1s 后12， 或 2 1s后 1

```java
@Slf4j(topic = "c.Test8Locks")
public class Test8Locks {
    public static void main(String[] args) {
        Number n1 = new Number();
        new Thread(() -> {
            log.debug("begin");
            n1.a();
        }).start();
        new Thread(() -> {
            log.debug("begin");
            n1.b();
        }).start();

    }
}
@Slf4j(topic = "c.Number")
class Number{
    public static synchronized void a() {
        sleep(1);
        log.debug("1");
    }
    public static synchronized void b() {
        log.debug("2");
    }

}
1234567891011121314151617181920212223242526
```

![image-20211020160736340](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/be436eb3bd73a891795bf523fa8815cb.png)

![image-20211020160805315](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/86101b7db3b5ff791b5f1da08bf8103c.png)

- a和b方法都是static静态方法，锁的对象都是Number.class
- 所以两者同一把锁

> 情况7：2 1s 后 1

```java
@Slf4j(topic = "c.Test8Locks")
public class Test8Locks {
    public static void main(String[] args) {
        Number n1 = new Number();
        Number n2 = new Number();
        new Thread(() -> {
            log.debug("begin");
            n1.a();
        }).start();
        new Thread(() -> {
            log.debug("begin");
            n2.b();
        }).start();

    }
}
@Slf4j(topic = "c.Number")
class Number{
    public static synchronized void a() {
        sleep(1);
        log.debug("1");
    }
    public synchronized void b() {
        log.debug("2");
    }

}
123456789101112131415161718192021222324252627
```

![image-20211020161055685](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/51e05d3cc0d7fad8c469846bd9e7fe90.png)

- a方法的锁为Number.class
- b方法的锁为n2
- 两者的锁不同

> 情况8：1s 后12， 或 2 1s后 1

```java
@Slf4j(topic = "c.Test8Locks")
public class Test8Locks {
    public static void main(String[] args) {
        Number n1 = new Number();
        Number n2 = new Number();
        new Thread(() -> {
            log.debug("begin");
            n1.a();
        }).start();
        new Thread(() -> {
            log.debug("begin");
            n2.b();
        }).start();

    }
}
@Slf4j(topic = "c.Number")
class Number{
    public static synchronized void a() {
        sleep(1);
        log.debug("1");
    }
    public static synchronized void b() {
        log.debug("2");
    }

}
123456789101112131415161718192021222324252627
```

![image-20211020161351112](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/b92748cb4fcf9191635dc89cf73c46d6.png)

![image-20211020161452936](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/270be8c5bb37ea48f6aa5bca90fd2a9c.png)

- a方法的锁为Number.class，b方法的锁也为Number.class
- 虽然是通过n1、n2两个不同的对象调用，但是类只有一种。所以还是一个锁

# 变量的[线程安全](https://so.csdn.net/so/search?q=线程安全&spm=1001.2101.3001.7020)分析

## 成员变量和静态变量是否线程安全？

- 如果它们没有共享，则线程安全
- 如果它们被共享了，根据它们的状态是否能够改变，又分两种情况
  - 如果只有读操作，则线程安全
  - 如果有读写操作，则这段代码是临界区，需要考虑线程安全

## 局部变量是否线程安全？

- 局部变量是线程安全的
- 但局部变量引用的对象（也可以被其他引用）则未必
  - 如果该对象没有逃离方法的作用访问，它是线程安全的
  - 如果该对象逃离方法的作用范围，如return，需要考虑线程安全

## 局部变量线程安全分析

```java
public static void test1() {
  int i = 10; 
  i++; 
}
1234
```

每个线程调用 test1() 方法时局部变量 i，会在每个线程的栈帧内存中被创建多份，因此不存在共享，局部变量是线程私有的

![image-20211020162301779](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/5e7f0a82ebd9f53c15741bc2b2c722e9.png)

如图

![image-20211020162437245](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/11d3ddbe06fc4aa50fa8c01740570e7e.png)

局部变量的引用稍有不同，先看一个成员变量的例子

### list是成员变量

```java
class ThreadUnsafe {
    ArrayList<String> list = new ArrayList<>();
    public void method1(int loopNumber) {
        for (int i = 0; i < loopNumber; i++) {
          // { 临界区, 会产生竞态条件
            method2();
            method3();
          // } 临界区
        }
    }

    private void method2() {
        list.add("1");
    }

    private void method3() {
        list.remove(0);
    }
}
12345678910111213141516171819
```

执行

```java
public class TestThreadSafe {
    static final int THREAD_NUMBER = 2;
    static final int LOOP_NUMBER = 200;
    public static void main(String[] args) {
        ThreadUnsafe test = new ThreadUnsafe();
        for (int i = 0; i < THREAD_NUMBER; i++) {
            new Thread(() -> {
                test.method1(LOOP_NUMBER);
            }, "Thread" + (i+1)).start();
        }
    }
}
123456789101112
```

其中一种情况是，如果线程2 还未 add，线程1 remove 就会报错：

![image-20211020163213563](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/908bc849c2c6b57a90479e3be1eb7e46.png)

分析：

- 无论哪个线程中的 method2 引用的都是堆中同一个对象中的 list 成员变量
- method3 与 method2 分析相同

![image-20211020163314314](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/0a7bc75c1c6a36fb03d4e2a0a232d3fd.png)

### list 修改为局部变量

```java
class ThreadSafe {
    public final void method1(int loopNumber) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < loopNumber; i++) {
            method2(list);
            method3(list);
        }
    }

    private void method2(ArrayList<String> list) {
        list.add("1");
    }

    private void method3(ArrayList<String> list) {
        list.remove(0);
    }
}
1234567891011121314151617
```

那么就不会有上述问题了

分析：

- list 是局部变量，每个线程调用时会创建其不同实例，没有共享
- 而 method2 的参数是从 method1 中传递过来的，与 method1 中引用同一个对象
- method3 的参数分析与 method2 相同

![image-20211020163944040](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/c85fba65b4adbf8a59fbce4bb739100f.png)

### 暴露list

方法访问修饰符带来的思考，如果把 method2 和 method3 的方法修改为 public 会不会代理线程安全问题？

- 情况1：有其它线程调用 method2 和 method3
  - 并不会产生线程安全问题
- 情况2：在 情况1 的基础上，为 ThreadSafe 类添加子类，子类覆盖 method2 或 method3 方法，即
  - 可能会产生线程安全问题

```java
class ThreadSafe {
    public void method1(int loopNumber) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < loopNumber; i++) {
            method2(list);
            method3(list);
        }
    }

    private void method2(ArrayList<String> list) {
        list.add("1");
    }

    public void method3(ArrayList<String> list) {
        list.remove(0);
    }
}

class ThreadSafeSubClass extends ThreadSafe{
    @Override
    public void method3(ArrayList<String> list) {
        System.out.println(2);
        new Thread(() -> {
            list.remove(0);
        }).start();
    }
}
123456789101112131415161718192021222324252627
```

![image-20211020165235186](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/f9cc25d26f0a2cb34dbb2a2ebd30edcb.png)

- 从这个例子可以看出 private 或 fifinal 提供【安全】的意义所在，请体会开闭原则中的【闭】
- 可以给method1加上final防止被重写，给method3加上private防止被子类重写

```java
class ThreadSafe {
    public final void method1(int loopNumber) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < loopNumber; i++) {
            method2(list);
            method3(list);
        }
    }

    private void method2(ArrayList<String> list) {
        list.add("1");
    }

    private void method3(ArrayList<String> list) {
        list.remove(0);
    }
}

class ThreadSafeSubClass extends ThreadSafe{
//    @Override
    public void method3(ArrayList<String> list) {
        new Thread(() -> {
            list.remove(0);
        }).start();
    }
}

public class TestThreadSafe {

    static final int THREAD_NUMBER = 2;
    static final int LOOP_NUMBER = 200;
    public static void main(String[] args) {
        ThreadSafeSubClass test = new ThreadSafeSubClass();
        for (int i = 0; i < THREAD_NUMBER; i++) {
            new Thread(() -> {
                test.method1(LOOP_NUMBER);
            }, "Thread" + (i+1)).start();
        }
    }
}
12345678910111213141516171819202122232425262728293031323334353637383940
```

- 加上private后，子类无法重写method3，子类的method3只是作为自己的一个成员方法。

## 常见线程安全类

- String
- Integer
- StringBuffffer
- Random
- Vector
- Hashtable
- java.util.concurrent 包下的类

这里说它们是线程安全的是指，多个线程调用它们同一个实例的某个方法时，是线程安全的。也可以理解为

```java
Hashtable table = new Hashtable();
new Thread(()->{
	table.put("key", "value1");
}).start();

new Thread(()->{
	table.put("key", "value2");
}).start();
12345678
```

- 它们的每个方法是原子的（因为直接加了synchronized在方法上）
- 但**注意**它们多个方法的组合不是原子的，见后面分析

### 线程安全类方法的组合

分析下面代码是否线程安全？

```java
Hashtable table = new Hashtable();
// 线程1，线程2
if( table.get("key") == null) {
	table.put("key", value);
}
12345
```

![image-20211020170028457](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/16809a4121d80c1fd69704c90d76a25f.png)

### 不可变类线程安全性

String、Integer 等都是不可变类，因为其内部的状态不可以改变，因此它们的方法都是线程安全的

有同学或许有疑问，String 有 replace，substring 等方法【可以】改变值啊，那么这些方法又是如何保证线程安全的呢？

如String的substring源码

```java
public String substring(int beginIndex) {
    if (beginIndex < 0) {
        throw new StringIndexOutOfBoundsException(beginIndex);
    }
    int subLen = value.length - beginIndex;
    if (subLen < 0) {
        throw new StringIndexOutOfBoundsException(subLen);
    }
    return (beginIndex == 0) ? this : new String(value, beginIndex, subLen);
}
12345678910
public String(char value[], int offset, int count) {
    if (offset < 0) {
        throw new StringIndexOutOfBoundsException(offset);
    }
    if (count <= 0) {
        if (count < 0) {
            throw new StringIndexOutOfBoundsException(count);
        }
        if (offset <= value.length) {
            this.value = "".value;
            return;
        }
    }
    // Note: offset or count might be near -1>>>1.
    if (offset > value.length - count) {
        throw new StringIndexOutOfBoundsException(offset + count);
    }
    this.value = Arrays.copyOfRange(value, offset, offset+count);
}
12345678910111213141516171819
```

- 直接new一个新的String对象，保证原对象的不可变

## 实例分析

例1

```java
public class MyServlet extends HttpServlet {
  // 是否安全？no	HashTable线程安全
	Map<String,Object> map = new HashMap<>();
	// 是否安全？yes
	String S1 = "...";
	// 是否安全？yes
	final String S2 = "...";
	// 是否安全？no
	Date D1 = new Date();
	// 是否安全？no
  // D2的引用不会变，但是日期对象内的属性可以变
	final Date D2 = new Date();
	public void doGet(HttpServletRequest request,	HttpServletResponse response) {
  // 使用上述变量
 	}
}
12345678910111213141516
```

例2：

```java
public class MyServlet extends HttpServlet {
	// 是否安全？no
  private UserService userService = new UserServiceImpl();
	
  public void doGet(HttpServletRequest request, HttpServletResponse response) {
		userService.update(...);
  }
}

public class UserServiceImpl implements UserService {
	// 记录调用次数
	private int count = 0;

  public void update() {
		// ...
		count++;
 	}
}
123456789101112131415161718
```

- userService为成员变量，是共享资源，多个线程调用会出现线程安全问题。

例3：

```java
@Aspect
@Component
public class MyAspect {
	// 是否安全？
	private long start = 0L;
	
  @Before("execution(* *(..))")
	public void before() {
		start = System.nanoTime();
	}

  @After("execution(* *(..))")
	public void after() {
		long end = System.nanoTime();
		System.out.println("cost time:" + (end-start));
 	}
}
1234567891011121314151617
```

- spring中的对象，没有额外说明的话，都是单例的。说明这个类的对象会被共享，对象的成员变量也会被共享。
- 解决方法，最好把前置、后置通知变为环绕通知，把成员变量做成局部变量。

例4：

```java
public class MyServlet extends HttpServlet {
  // 是否安全 yes
	private UserService userService = new UserServiceImpl();
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		userService.update(...);
  }
}

public class UserServiceImpl implements UserService {
  // 是否安全 yes
  private UserDao userDao = new UserDaoImpl();

  public void update() {
    userDao.update();
  }
}

public class UserDaoImpl implements UserDao { 
  public void update() {
    String sql = "update user set password = ? where username = ?";
    // 是否安全 yes
    try (Connection conn = DriverManager.getConnection("","","")){
      // ...
    } catch (Exception e) {
      // ...
    }
  }
}
12345678910111213141516171819202122232425262728
```

- UserDaoImpl是线程安全的，没有成员变量，资源变量Connection也是作为方法内局部变量出现
- UserServiceImpl是线程安全的，成员变量UserDao是线程安全的，内部状态也不会改变。
- UserService是线程安全的，并不会改变它的内部状态。

例5：

```java
public class MyServlet extends HttpServlet {
  // 是否安全
  private UserService userService = new UserServiceImpl();
  
  public void doGet(HttpServletRequest request, HttpServletResponse response) {
    userService.update(...);
  }
}

public class UserServiceImpl implements UserService {
  // 是否安全
  private UserDao userDao = new UserDaoImpl();

  public void update() {
    userDao.update();
  }
}

public class UserDaoImpl implements UserDao {
  // 是否安全
  private Connection conn = null;

  public void update() throws SQLException {
    String sql = "update user set password = ? where username = ?";
    conn = DriverManager.getConnection("","","");
    // ...
    conn.close();
  }
}
1234567891011121314151617181920212223242526272829
```

- UserDaoImpl是线程不安全的，因为作为了成员变量，在多线程下，可能其中一个线程刚开启Connection，就被另一个给关了。
- UserServiceImpl、UserService也变得线程不安全了。

例6：

```java
public class MyServlet extends HttpServlet {
  // 是否安全
  private UserService userService = new UserServiceImpl();
  public void doGet(HttpServletRequest request, HttpServletResponse response) {
    userService.update(...);
  }
}

public class UserServiceImpl implements UserService { 
  public void update() {
    UserDao userDao = new UserDaoImpl();
    userDao.update();
  }
}

public class UserDaoImpl implements UserDao {
  // 是否安全
  private Connection = null;
  public void update() throws SQLException {
    String sql = "update user set password = ? where username = ?";
    conn = DriverManager.getConnection("","","");
    // ...
    conn.close();
  }
}
12345678910111213141516171819202122232425
```

- 由于UserServiceImpl中每次都新创建userDao，且作为局部变量。而每次都创建新的，那么UserDaoImpl中的成员变量Connection也是新的。不会出现线程安全问题。

例7：

```java
public abstract class Test {
  public void bar() {
    // 是否安全
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    foo(sdf); 
  }

  public abstract foo(SimpleDateFormat sdf);

  public static void main(String[] args) {
    new Test().bar();
  }
}
12345678910111213
```

其中 foo 的行为是不确定的，可能导致不安全的发生，被称之为**外星方法**

```java
public void foo(SimpleDateFormat sdf) {
  String dateStr = "1999-10-11 00:00:00";
  for (int i = 0; i < 20; i++) {
    new Thread(() -> {
      try {
        sdf.parse(dateStr);
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }).start();
  }
}
123456789101112
```

# 习题

## 卖票练习

测试下面代码是否存在线程安全问题，并尝试改正

```java
@Slf4j(topic = "c.ExerciseSell")
public class ExerciseSell {
    public static void main(String[] args) throws InterruptedException {
        // 模拟多人买票
        TicketWindow window = new TicketWindow(1000);

        // 所有线程的集合
        List<Thread> threadList = new ArrayList<>();
        // 卖出的票数统计
        List<Integer> amountList = new Vector<>();
        for (int i = 0; i < 2000; i++) {
            Thread thread = new Thread(() -> {
                // 买票
                int amount = window.sell(random(5));
                // 统计买票数
                amountList.add(amount);
            });
            threadList.add(thread);
            thread.start();
        }

        for (Thread thread : threadList) {
            thread.join();
        }

        // 统计卖出的票数和剩余票数
        log.debug("余票：{}",window.getCount());
        log.debug("卖出的票数：{}", amountList.stream().mapToInt(i-> i).sum());
    }

    // Random 为线程安全
    static Random random = new Random();

    // 随机 1~5
    public static int random(int amount) {
        return random.nextInt(amount) + 1;
    }
}

// 售票窗口
class TicketWindow {
    private int count;

    public TicketWindow(int count) {
        this.count = count;
    }

    // 获取余票数量
    public int getCount() {
        return count;
    }

    // 售票
    public  int sell(int amount) {
        if (this.count >= amount) {
            this.count -= amount;
            return amount;
        } else {
            return 0;
        }
    }
}
1234567891011121314151617181920212223242526272829303132333435363738394041424344454647484950515253545556575859606162
```

结果

![image-20211024104948890](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/2e18cc755ac3b6bd81f7812c2fa9680f.png)

- 窗口是共享变量，所有线程都会访问，进行买票操作

```java
// 模拟多人买票
TicketWindow window = new TicketWindow(1000);
12
```

- sell操作中，涉及到余票变量的读写操作

![image-20211024105817187](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/0b883dc1fd8238d2dfa071769a523884.png)

- TicketWindow中的余票数count是共享变量

- ```java
  // 售票窗口
  class TicketWindow {
      private int count;
  
      public TicketWindow(int count) {
          this.count = count;
      }
  
      // 获取余票数量
      public int getCount() {
          return count;
      }
  
      // 售票
      public  int sell(int amount) {
          if (this.count >= amount) {
              this.count -= amount;
              return amount;
          } else {
              return 0;
          }
      }
  }
  1234567891011121314151617181920212223
  ```

- 每一个线程买票时，都是先读count，再修改count，在多线程情况下，容易发生指令交错，线程不安全

### 解决方法

- 在sell方法上加synchronized，锁住TicketWindow的实例对象，也就锁住count，就对count进行了保护，线程安全

```java
// 售票
public synchronized int sell(int amount) {
    if (this.count >= amount) {
        this.count -= amount;
        return amount;
    } else {
        return 0;
    }
}
123456789
```

另外，用下面的代码行不行，为什么？

```java
List<Integer> sellCount = new ArrayList<>();
1
```

- 不行，原例中是new Vector，Vector是线程安全的，换成ArrayList那么对票的相加操作也会变得线程不安全。

![image-20211024105748661](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/0de44fce2bc5c9f7b80bfc10c6aa868c.png)

现在

```java
// 买票
int amount = window.sell(random(5));
// 统计买票数
amountList.add(amount);
1234
```

两个方法分别都是线程安全的了，那么组合在一起了，也是线程安全的吗？其实仍然是线程安全的，因为两个方法虽然组合了，没有在组合的方法上加synchronized，在多线程下可能会发生指令交错。但是即使发生了也不影响，因为两个方法操作的并不是同一个共享变量。是线程安全的。

## 转账练习

测试下面代码是否存在线程安全问题，并尝试改正

```java
@Slf4j(topic = "c.ExerciseTransfer")
public class ExerciseTransfer {
    public static void main(String[] args) throws InterruptedException {
        Account a = new Account(1000);
        Account b = new Account(1000);
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                a.transfer(b, randomAmount());
            }
        }, "t1");
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                b.transfer(a, randomAmount());
            }
        }, "t2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        // 查看转账2000次后的总金额
        log.debug("total:{}", (a.getMoney() + b.getMoney()));
    }

    // Random 为线程安全
    static Random random = new Random();

    // 随机 1~100
    public static int randomAmount() {
        return random.nextInt(100) + 1;
    }
}

// 账户
class Account {
    private int money;

    public Account(int money) {
        this.money = money;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    // 转账
    public void transfer(Account target, int amount) {
            if (this.money >= amount) {
                this.setMoney(this.getMoney() - amount);
                target.setMoney(target.getMoney() + amount);
            }
    }
}
1234567891011121314151617181920212223242526272829303132333435363738394041424344454647484950515253545556
```

结果

![image-20211024110605536](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/480326aea4f786c5969272f58cf4fb84.png)

- 有两个账户对象，每个账户对象里有自己的余额变量money作为共享变量。

```java
Account a = new Account(1000);
Account b = new Account(1000);
12
```

![image-20211024110748004](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211024110748004.png)

- 账户里有一个transfer方法，设计到对共享变量money的读写操作

![image-20211024110925537](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/9db36e479f972bd16f99d3dbd4ee0d22.png)

- 然后分别开1000个线程去让a给b转账，开1000个线程让b给a转账，调用transfer方法。
- 即对共享变量money做读写的方法transfer，暴露在了多线程环境下。

```java
Thread t1 = new Thread(() -> {
    for (int i = 0; i < 1000; i++) {
        a.transfer(b, randomAmount());
    }
}, "t1");
Thread t2 = new Thread(() -> {
    for (int i = 0; i < 1000; i++) {
        b.transfer(a, randomAmount());
    }
}, "t2");
12345678910
```

- 在多线程下，发生指令交错，发生了线程安全问题，导致总金额为2054 > 2000

```bash
11:05:15.743 c.ExerciseTransfer [main] - total:2054

Process finished with exit code 0
123
```

### 解决方法

> 对transfer()方法上锁

直接对transfer()方法加synchronized？很遗憾，这样做并不能解决问题。

```java
// 转账
public synchronized void transfer(Account target, int amount) {
        if (this.money >= amount) {
            this.setMoney(this.getMoney() - amount);
            target.setMoney(target.getMoney() + amount);
        }
}
1234567
```

结果，依然发生了线程安全问题

```bash
11:15:35.814 c.ExerciseTransfer [main] - total:423

Process finished with exit code 0
123
```

- 这是因为，transfer方法中，并不只是操作了自己的共享变量this.money，还操作了转账对象的共享变量target.money。
- 而synchronize直接加在成员方法上，等同于synchronized(this)，对自己的成员变量this.money加锁。
- 所以应该把两个共享变量都锁上，那么可以使用synchronized(Account.class)，对Account.class加锁，那么Account类所有的实例对象都会被加上锁，即实现了锁住了两个对象的money

```java
// 转账
public void transfer(Account target, int amount) {
       synchronized (Account.class) {
           if (this.money >= amount) {
               this.setMoney(this.getMoney() - amount);
               target.setMoney(target.getMoney() + amount);
           }
       }
}
123456789
```

结果

```bash
11:24:38.288 c.ExerciseTransfer [main] - total:2000

Process finished with exit code 0
123
```