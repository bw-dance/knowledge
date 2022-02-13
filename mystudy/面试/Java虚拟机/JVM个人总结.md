# JVM内存分配

![image-20220211215221472](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220211215221472.png)

## 程序计数器

**解释：**

编写好的Java程序先进行编译，编译成二进制的字节码文件。字节码解释器依次来读取这些文件内容。

在读取的过程中，如遇到程序控制等相关的执行语句的时候，需要跳跃读取字节码内容，读完继续之前的位置进行读取，此时程序计数器用于记录之前读取的文件的位置。

在多线程的情况下，如果遇到线程切换，需要用程序计数器记录之前的线程执行的位置，等线程切换完毕后，继续之前之前线程执行的位置。

**作用：**

1. 记住下一条jvm指令的执行地址
2. 多线程切换时，记录当前线程的执行位置。

**特点：**

1. 线程私有，随线程的消亡而消亡
2. 不会存在内存溢出

注意：程序计数器是CPU中寄存器实现的。

## 虚拟机栈

![image-20220211215757738](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220211215757738.png)

**作用：**

每开辟一条线程，都会创建一个虚拟机栈,用于当前线程运行所需的内存空间。当我们调用方法的时候，此时该方法就会进行压栈操作，作为栈帧，每个方法都有一定的内存空间，用于存储（方法的参数，局部变量表，返回地址等）当方法执行完毕后，就会执行出栈操作。

**特点：**

1. 线程私有，生命周期和线程相同
2. 会出现 `StackOverFlowError` 和 `OutOfMemoryError` 两种错误

**相关问题：**

1. 垃圾回收是否涉及到栈内存

   不涉及。栈内存是方法调用是分配的，在方法结束调用后，就将栈帧弹出栈了，释放了内存。

2. 栈内存分配越大越好吗

   并不是。系统的物理内存是一定的，栈空间越大，会导致线程数越少。

   栈空间越大，也并不会让程序更快，只是有更大的栈空间，能让你做更多次的递归调用。

3. 方法内的局部变量是否线程安全

   1. 判断是否安全，即看这些变量对于多个线程是共享的还是私有的。
   2. 如果方法内局部变量没有逃离方法的作用范围（无return或传参），它是线程安全的
   3. 如果是局部变量引用了对象，并逃离方法的作用方法（return或传参），需要考虑线程安全
   3. ![image-20220121100921799](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220121100921799.png)

### **栈内存溢出**

栈空间调整参数

```java
-Xss空间大小
-Xss8M
```

1. 栈帧过多导致栈内存溢出
   1. 当程序递归调用次数太多时，会超出栈的空间，导致栈内存溢出。
   2. ![image-20220211193702855](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220211193702855.png)
2. 栈帧过大导致栈内存溢出
   1. 变量过大（一般不会出现）
   2. 方法携带的参数等占用内存太多，导致栈帧过大，使栈内存溢出。
   3. ![image-20210828172432094](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20210828172432094.png)

### 线程运行诊断

案例1：cpu占用过多[【全网独家】解读大厂高并发设计20问，收藏学习进大厂！_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV13S4y177u3?spm_id_from=333.999.0.0)

定位

- 用top定位哪个进程对cpu的占用过高
  - top
- 用ps命令进一步定位是哪个线程引起的cpu占用过高
  - ps H -eo pid,tid,%cpu | grep 32655
- jstack根据线程id找到有问题的线程，进一步定位到问题代码的源码行数。
  - jstack 进程id

案例2：程序运行很长时间没有结果

#### 演示1（cpu占用过多）

```bash
# 使用top命令查看当前cup运行情况
top
```

![image-20210828175513175](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/1a11adec12621d19015fba74b3ab6d02.png)

```bash
# 使用ps查看线程的运行情况
# -eo 后的参数是想要查看的参数信息，pid进程号，tid线程号，%cpu cpu占用率
ps H -eo pid,tid,%cpu | grep 32655
```

![image-20210828180026634](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/876621e6a2e29c187363da94d7c0501e.png)

32665线程有大问题。

```bash
# 输出进程内的所有信息，线程号用16进制表示的
# 32665线程换算成16进制为7f99
jstack 32655
```

![image-20210828180606154](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/0aeba05a8b91bb864a23cf9303f28221.png)

![image-20220121103117415](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220121103117415.png)

#### 演示2（死锁）

在多线程编程中，我们为了防止多线程竞争共享资源而导致数据错乱，都会在操作共享资源之前加上互斥锁，只有成功获得到锁的线程，才能操作共享资源，获取不到锁的线程就只能等待，直到锁被释放。

当两个线程为了保护两个不同的共享资源而使用了两个互斥锁，那么这两个互斥锁应用不当的时候，可能会造成**两个线程都在等待对方释放锁**，在没有外力的作用下，这些线程会一直相互等待，就没办法继续运行，这种情况就是发生了**死锁**。

```bash
jstack 32275
```

![image-20220121103219662](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220121103219662.png)

```java
package cn.itcast.jvm.t1.stack;

/**
 * 演示线程死锁
 */
class A{};
class B{};
public class Demo1_3 {
    static A a = new A();
    static B b = new B();


    public static void main(String[] args) throws InterruptedException {
        new Thread(()->{
            synchronized (a) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (b) {
                    System.out.println("我获得了 a 和 b");
                }
            }
        }).start();
        Thread.sleep(1000);
        new Thread(()->{
            synchronized (b) {
                synchronized (a) {
                    System.out.println("我获得了 a 和 b");
                }
            }
        }).start();
    }

}
```

## 本地方法栈

native修饰的方法。

java类并不是所有的方法都是java代码编写的，有些底层的方法就是通过c/c++实现的。而java可以调用这些底层方法来完成一些功能。在java调用这些底层方法时，就是运行在本地方法栈中。

和虚拟机栈所发挥的作用非常相似，区别是： **虚拟机栈为虚拟机执行 Java 方法 （也就是字节码）服务，而本地方法栈则为虚拟机使用到的 Native 方法服务。** 在 HotSpot 虚拟机中和 Java 虚拟机栈合二为一。

本地方法被执行的时候，在本地方法栈也会创建一个栈帧，用于存放该本地方法的局部变量表、操作数栈、动态链接、出口信息。

方法执行完毕后相应的栈帧也会出栈并释放内存空间。

**特点：**

1. 线程私有，随线程的消亡而消亡
2. 会出现 `StackOverFlowError` 和 `OutOfMemoryError` 两种错误

## 堆

new 关键字创建的对象会占用堆内存。

**特点**

- 它是线程共享的，堆中对象都需要考虑线程安全的问题
- 会产生OutOfMemoryError错误。
- 有垃圾回收机制

**堆空间调整参数**

堆内存分配:

JVM初始分配的内存由-Xms指定，默认是物理内存的1/64；JVM最大分配的内存由-Xmx指定，默认是物理内存的1/4。默认空余堆内存小于40%时，JVM就会增大堆直到-Xmx的最大限制；空余堆内存大于70%时，JVM会减少堆直到-Xms的最小限制。**因此服务器一般设置-Xms、-Xmx相等以避免在每次GC后调整堆的大小。**

非堆内存分配（方法区）:

（1.8之前）JVM使用-XX:PermSize设置非堆内存初始值，默认是物理内存的1/64；由XX:MaxPermSize设置最大非堆内存的大小，默认是物理内存的1/4。

（1.8之后）MetaspaceSize

查看虚拟机内存：

java -XshowSettings:vm

```java
初始分配内存
-Xms   
最大分配内存
-Xmx
-Xmx4G
```

**案例代码**

```java
package cn.itcast.jvm.t1.heap;

import java.util.ArrayList;
import java.util.List;

/**
 * 演示堆内存溢出 java.lang.OutOfMemoryError: Java heap space
 * -Xmx8m
 */
public class Demo1_5 {

    public static void main(String[] args) {
        int i = 0;
        try {
            List<String> list = new ArrayList<>();
            String a = "hello";
            while (true) {
                list.add(a); // hello, hellohello, hellohellohellohello ...
                a = a + a;  // hellohellohellohello
                i++;
            }
        } catch (Throwable e) {
            e.printStackTrace();
            System.out.println(i);
        }
    }
}
```

在实际生产中，对于堆内存溢出问题，可能并不是那么容易检测出来。因为堆内存空间比较大，在运行时，一时间还不会使其溢出。

所以为了使堆内存问题尽早暴露出来，可以在测试时，将堆内存空间调整小一些。

### 堆内存诊断

1. jps工具
   - 查看当前系统中有哪些java进程
2. jmap工具
   - 查看**某一时刻**堆内存占用情况
   - jmap -heap -pid 进程id
3. jconsole工具
   - 图形界面的，多功能的监测工具，可以**连续**监测
4. 堆内存调整指令参数
   - -Xmx容量大小

#### jmp诊断堆内存

案例代码

```java
package cn.itcast.jvm.t1.heap;

/**
 * 演示堆内存
 */
public class Demo1_4 {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("1...");
        Thread.sleep(30000);
        byte[] array = new byte[1024 * 1024 * 10]; // 10 Mb
        System.out.println("2...");
        Thread.sleep(20000);
        array = null;
        System.gc();
        System.out.println("3...");
        Thread.sleep(1000000L);
    }
}
```

- Thread.sleep 是为了留有时间间隔执行命令，监控进程状态
- 程序打印 **1…** 后，执行jps查看该进程的进程号
- jmap -heap 进程id，查看这一时刻进程堆空间使用情况
- 程序打印 2… 后，再次执行 jmap 指令查看内存情况
- 程序打印 3… 后，再次执行 jmap 指令查看内存情况

程序运行后

jps

![image-20220121110855217](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220121110855217.png)

1580为该进程的pid，调用命令

```bash
jmap -heap  1580 
```

具体的堆内存占用在Heap Usage

![image-20220121111211630](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220121111211630.png)

在程序打印了 **2…** 后，再次

```bash
jmap -heap  1580 
```

![image-20220121111303324](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220121111303324.png)

按理说应该增加10M，此处有些疑惑

在打印了 3… 之后，代表着已经被垃圾回收了

```bash
jmap -heap  1580 
```

![image-20220121111358032](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220121111358032.png)

#### jconsole诊断堆内存

控制台输入：jconsole

![image-20220121112027218](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220121112027218.png)

但是在jconsole里面可以看出，在给array初始化后，堆内存使用量增加了10M，在垃圾回收后，堆内存使用量又迅速下降。

#### jvisualvm诊断堆内存

控制台输入：jvisualvm

问题：程序执行过GC之后，内存占用空间还是居高不下。比如没GC之前是250，GC之后230的现象。

jvisualvm是功能更加强大的图形化jvm管理软件。可以进行堆转储，拿到进程某一时刻的快照dump进行分析。

案例代码：

```java
package cn.itcast.jvm.t1.heap;

import java.util.ArrayList;
import java.util.List;

/**
 * 演示查看对象个数 堆转储 dump
 */
public class Demo1_13 {

    public static void main(String[] args) throws InterruptedException {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            students.add(new Student());
//            Student student = new Student();
        }
        Thread.sleep(1000000000L);
    }
}
class Student {
    private byte[] big = new byte[1024*1024];
}
```

![image-20220121113516182](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220121113516182.png)

经过测试，在执行了垃圾回收后，堆内存占用还是居高不下。

于是点击 堆dump 拿取快照，分析详情

![image-20220121113620666](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220121113620666.png)

点击查看

![image-20220121113742495](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220121113742495.png)

由源代码可知，确实是Student类的原因。

```java
class Student {
    private byte[] big = new byte[1024 * 1024 * 10];
}
```

student数组一直在循环引用，没有被垃圾回收。

## 方法区

**方法区存储的内容：**

1. 类结构相关的内容：版本，成员变量，方法，构造器，接口，字面量（字符串，常量）以及相关的代码。
2. 运行时常量池
   1. 常量池表中的相关内容，在类加载后会存放在运行时常量池中。
   2. 受到方法区内存的限制，当常量池无法再申请到内存时会抛出 OutOfMemoryError 错误。


- 常量池，就是一张表，虚拟机指令根据这张常量表找到要执行的类名、方法名、参数类型、字面量等信息
- 运行时常量池，常量池是 *.class 文件中的，当该类被加载（运行的时候），它的常量池信息就会放入运行时常量池（类的信息放入内存中），并把里面的符号地址变为真实地址。

**特点：**

1. 线程共享
2. 如果方法区申请的内存空间不足，也会抛出OOM 异常。

**变化历程：**

**方法区**是一种规范，**永久代**和**元空间**都只是它的实现。

jdk1.6时，方法区使用的是堆的一部分。（待确定）

jdk1.7时，将字符串常量池和静态变量移出。（注意：字符串常量池是肯定移动到了堆里面，静态变量待确定（虚拟机规范说是进堆了））

JDK1.8时，方法区不直接占用JVM虚拟机内存，而是占用操作系统内存。（除了字符串常量池和静态变量，其他的还在方法区）。

```java
1.8之前
-XX:PermSize=N //方法区 (永久代) 初始大小
-XX:MaxPermSize=N //方法区 (永久代) 最大大小,超过这个值将会抛出 OutOfMemoryError 异常:java.lang.OutOfMemoryError: PermGen
1.8
-XX:MetaspaceSize=N //设置 Metaspace 的初始（和最小大小）
-XX:MaxMetaspaceSize=N //设置 Metaspace 的最大大小
```

**问题：为什么要将永久代 (PermGen) 替换为元空间 (MetaSpace) 呢?**

整个永久代有一个 JVM 本身设置的固定大小上限，无法进行调整，而元空间使用的是直接内存，受本机可用内存的限制，虽然元空间仍旧可能溢出，但是比原来出现的几率会更小。

**常量池表：**

```java
// 二进制字节码（类基本信息，常量池，类方法定义，包含了虚拟机指令）
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("hello world");
    }
}
```

```java
javap -v ConstantPool.class
```

![image-20220211212735989](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20220211212735989.png)



## 直接内存

不是虚拟机的内存，是系统内存。Direct Memory

- 常见于NIO操作时，用于数据缓存区（ByteBuffer）
- 分配回收成本过高，但读写性能高
- 不受JVM内存回收管理
- 会产生**OutOfMemoryError** 异常

**直接内存调整参数**

可以通过 ***\*-XX:MaxDirectMemorySize\**** 参数来设置最大可用直接内存，如果启动时未设置则默认为最大堆内存大小，即与 -Xmx 相同。即假如最大堆内存为1G，则默认直接内存也为1G，那么 JVM 最大需要的内存大小为2G多一些。当直接内存达到最大限制时就会触发GC，如果回收失败则会引起OutOfMemoryError。

```java
-XX:MaxDirectMemorySize  设置最大可用直接内存
```

### java操作磁盘文件

![image-20210908192753325](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/8eb7265043c741e2e3e25f4760369539.png)

当java读取磁盘文件时，会从用户态切换到内核态，才能去操作系统内存。读取时，系统内存先开辟一块缓存空间，磁盘文件分块读取。然后java虚拟机内存再开辟缓存空间new Byte[]来读取系统内存的文件。由于有从系统内存读取到java虚拟机的内存，所以效率较低。

### NIO操作磁盘文件

![image-20210908192818850](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/9c3afe74a106b3a826a034d31ff16e8c.png)

读取磁盘文件时，会有一块直接内存，java虚拟机和视同内存都能访问使用，所以效率更高。

### 内存溢出

每次开辟100MB的直接内存，并且添加到集合中，不进行释放。

allocateDirect：开辟一块直接内存空间。

```java
public class demo1_24 {

    static int _100MB = 1024 * 1024 * 100;

    public static void main(String[] args) {
        List<ByteBuffer> list = new ArrayList<>();
        int i = 0;
        try {
            while (true) {
                ByteBuffer byteBuffer = ByteBuffer.allocateDirect(_100MB);
                list.add(byteBuffer);
                i++;
            }
        } finally {
            System.out.println(i);
        }
    }

}
```



