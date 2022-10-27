# JUC_1_进程与线程

- [本章内容](#_4)
- [进程与线程](#_12)
- - [进程](#_14)
  - [线程](#_22)
  - [二者对比](#_31)
- [并行与并发](#_44)
- - [并发](#_46)
  - [并行](#_56)
  - [例子](#_73)
- [应用](#_82)
- - [应用之异步调用（案例1）](#1_84)
  - [应用之提高效率（案例1）](#1_139)
  - - [设计](#_154)
    - - [环境搭建](#_158)
      - [双核 CPU（4个逻辑CPU）](#_CPU4CPU_248)
      - [单核 CPU](#_CPU_256)
    - [结论](#_264)



# 本章内容

- 进程和线程的概念
- 并行和并发的概念
- 线程基本应用

# 进程与线程

## 进程

- 程序由指令和数据组成，但这些指令要运行，数据要读写，就必须将指令加载至 CPU，数据加载至内存。在指令运行过程中还需要用到磁盘、网络等设备。进程就是用来加载指令、管理内存、管理 IO 的
- 当一个程序被运行，从磁盘加载这个程序的代码至内存，这时就开启了一个进程。
- 进程就可以视为程序的一个实例。大部分程序可以同时运行多个实例进程（例如记事本、画图、浏览器等），也有的程序只能启动一个实例进程（例如网易云音乐、360 安全卫士等）

## 线程

- 一个进程之内可以分为一到多个线程。
- 一个线程就是一个指令流，将指令流中的一条条指令以一定的顺序交给 CPU 执行
- Java 中，线程作为最小调度单位，进程作为资源分配的最小单位。 在 windows 中进程是不活动的，只是作为线程的容器

## 二者对比

- 进程基本上相互独立的，而线程存在于进程内，是进程的一个子集
- 进程拥有共享的资源，如内存空间等，供其内部的线程共享
- 进程间通信较为复杂
  - 同一台计算机的进程通信称为 IPC（Inter-process communication）
  - 不同计算机之间的进程通信，需要通过网络，并遵守共同的协议，例如 HTTP
- 线程通信相对简单，因为它们共享进程内的内存，一个例子是多个线程可以访问同一个共享变量
- 线程更轻量，线程上下文切换成本一般上要比进程上下文切换低

# 并行与并发

## 并发

单核 cpu 下，线程实际还是 串行执行 的。操作系统中有一个组件叫做任务调度器，将 cpu 的时间片（windows下时间片最小约为 15 毫秒）分给不同的程序使用，只是由于 cpu 在线程间（时间片很短）的切换非常快，人类感觉是同时运行的 。总结为一句话就是： 微观串行，宏观并行 ，一般会将这种 线程轮流使用 CPU 的做法称为并发， concurrent

![image-20211001100855045](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/3d9e28a00a455ade63035252be709e15.png)

![image-20211001100915530](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/a0f08f52230a4992cca90661575f1b93.png)

## 并行

多核 cpu下，每个 核（core） 都可以调度运行线程，这时候线程可以是并行的。

![image-20211001100954596](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/23d2434428f7122d4309c6c4c9d4e316.png)

![image-20211001101010465](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/3072f483844d3c99f77d8b49fe351783.png)

引用 Rob Pike 的一段描述：

- 并发（concurrent）是同一时间应对（dealing with）多件事情的能力
- 并行（parallel）是同一时间动手做（doing）多件事情的能力

## 例子

- 家庭主妇做饭、打扫卫生、给孩子喂奶，她一个人轮流交替做这多件事，这时就是并发
- 家庭主妇雇了个保姆，她们一起这些事，这时既有并发，也有并行（这时会产生竞争，例如锅只有一口，一个人用锅时，另一个人就得等待）
- 雇了3个保姆，一个专做饭、一个专打扫卫生、一个专喂奶，互不干扰，这时是并行

# 应用

## 应用之异步调用（案例1）

以调用方角度来讲，如果

- 需要等待结果返回，才能继续运行就是同步
- 不需要等待结果返回，就能继续运行就是异步

1. 设计

   多线程可以让方法执行变为异步的（即不要巴巴干等着）比如说读取磁盘文件时，假设读取操作花费了 5 秒钟，如果没有线程调度机制，这 5 秒 cpu 什么都做不了，其它代码都得暂停…

2. 结论

   - 比如在项目中，视频文件需要转换格式等操作比较费时，这时开一个新线程处理视频转换，避免阻塞主线程
   - tomcat 的异步 servlet 也是类似的目的，让用户线程处理耗时较长的操作，避免阻塞 tomcat 的工作线程
   - ui 程序中，开线程进行其他操作，避免阻塞 ui 线程

同步

```java
@Slf4j(topic = "c.Sync")
public class Sync {

    public static void main(String[] args) {
        FileReader.read(Constants.MP4_FULL_PATH);
        log.debug("do other things ...");
    }

}
123456789
```

![image-20211001105103860](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/7f1d656ef1bf453433560fc269a08f04.png)

异步

```java
@Slf4j(topic = "c.Async")
public class Async {

    public static void main(String[] args) {
        new Thread(() -> FileReader.read(Constants.MP4_FULL_PATH)).start();
        log.debug("do other things ...");
    }

}
123456789
```

![image-20211001105211721](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/73d382ea86b7c0ffd1f4ebde8cc55176.png)

## 应用之提高效率（案例1）

充分利用多核 cpu 的优势，提高运行效率。想象下面的场景，执行 3 个计算，最后将计算结果汇总。

![image-20211001111342528](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/be956d7def857829db428f47adc9073a.png)

- 如果是串行执行，那么总共花费的时间是 10 + 11 + 9 + 1 = 31ms
- 但如果是四核 cpu，各个核心分别使用线程 1 执行计算 1，线程 2 执行计算 2，线程 3 执行计算 3，那么 3 个线程是并行的，花费时间只取决于最长的那个线程运行的时间，即 11ms 最后加上汇总时间只会花费 12ms

> **注意**
>
> 需要在多核 cpu 才能提高效率，单核仍然时是轮流执行

### 设计

使用多线程充分利用 CPU

#### 环境搭建

- 基准测试工具选择，使用了比较靠谱的 JMH，它会执行程序预热，执行多次测试并平均
- cpu 核数限制，有两种思路
  1. 使用虚拟机，分配合适的核
  2. 使用 msconfifig，分配合适的核，需要重启比较麻烦
- 并行计算方式的选择
  1. 最初想直接使用 parallel stream，后来发现它有自己的问题
  2. 改为了自己手动控制 thread，实现简单的并行计算
- 测试代码如下

```bash
mvn archetype:generate -DinteractiveMode=false -DarchetypeGroupId=org.openjdk.jmh -DarchetypeArtifactId=jmh-java-benchmark-archetype -DgroupId=org.sample -DartifactId=test -Dversion=1.0
1
package com.itcast;

import org.openjdk.jmh.annotations.*;

import java.util.Arrays;
import java.util.concurrent.FutureTask;

@Fork(1)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations=3)
@Measurement(iterations=5)
public class MyBenchmark {
    static int[] ARRAY = new int[1000_000_00];
    static {
        Arrays.fill(ARRAY, 1);
    }
    @Benchmark
    public int c() throws Exception {
        int[] array = ARRAY;
        FutureTask<Integer> t1 = new FutureTask<>(()->{
            int sum = 0;
            for(int i = 0; i < 250_000_00;i++) {
                sum += array[0+i];
            }
            return sum;
        });
        FutureTask<Integer> t2 = new FutureTask<>(()->{
            int sum = 0;
            for(int i = 0; i < 250_000_00;i++) {
                sum += array[250_000_00+i];
            }
            return sum;
        });
        FutureTask<Integer> t3 = new FutureTask<>(()->{
            int sum = 0;
            for(int i = 0; i < 250_000_00;i++) {
                sum += array[500_000_00+i];
            }
            return sum;
        });
        FutureTask<Integer> t4 = new FutureTask<>(()->{
            int sum = 0;
            for(int i = 0; i < 250_000_00;i++) {
                sum += array[750_000_00+i];
            }
            return sum;
        });
        new Thread(t1).start();
        new Thread(t2).start();
        new Thread(t3).start();
        new Thread(t4).start();
        return t1.get() + t2.get() + t3.get()+ t4.get();
    }
    @Benchmark
    public int d() throws Exception {
        int[] array = ARRAY;
        FutureTask<Integer> t1 = new FutureTask<>(()->{
            int sum = 0;
            for(int i = 0; i < 1000_000_00;i++) {
                sum += array[0+i];
            }
            return sum;
        });
        new Thread(t1).start();
        return t1.get();
    }
}
12345678910111213141516171819202122232425262728293031323334353637383940414243444546474849505152535455565758596061626364656667
```

#### 双核 CPU（4个逻辑CPU）

![image-20211001112015990](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/c1877905390535a0252fb8a923fc6dec.png)

可以看到多核下，效率提升还是很明显的，快了一倍左右

#### 单核 CPU

![image-20211001112043311](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/ce0b2e6cd6191f30ae00d30e8d22ff2a.png)

性能几乎是一样的，而且由于并发切换线程时，需要进行上下文切换，耗费一些时间。

### 结论

1. 单核 cpu 下，多线程不能实际提高程序运行效率，只是为了能够在不同的任务之间切换，不同线程轮流使用cpu ，不至于一个线程总占用 cpu，别的线程没法干活
2. 多核 cpu 可以并行跑多个线程，但能否提高程序运行效率还是要分情况的
   - 有些任务，经过精心设计，将任务拆分，并行执行，当然可以提高程序的运行效率。但不是所有计算任务都能拆分（参考后文的【阿姆达尔定律】）
   - 也不是所有任务都需要拆分，任务的目的如果不同，谈拆分和效率没啥意义，比如任务二就是要等待任务一的结果才运行。
3. IO 操作不占用 cpu，只是我们一般拷贝文件使用的是【阻塞 IO】，这时相当于线程虽然不用 cpu，但需要一直等待 IO 结束，没能充分利用线程。所以才有后面的【非阻塞 IO】和【异步 IO】优化