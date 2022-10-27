# 共享模型之无锁

- [问题提出](#_3)
- - [解决思路-锁](#_71)
  - [解决思路-无锁](#_102)
- [CAS 与 volatile](#CAS__volatile_136)
- - [CAS](#CAS_138)
  - [volatile](#volatile_176)
  - [为什么无锁效率高](#_190)
  - [CAS 的特点](#CAS__202)
- [原子整数](#_216)
- [原子引用](#_286)
- - [不安全实现](#_330)
  - [安全实现-使用锁](#_355)
  - [安全实现-使用 CAS](#_CAS_383)
  - [ABA 问题及解决](#ABA__434)
  - - [ABA 问题](#ABA__436)
    - [AtomicStampedReference](#AtomicStampedReference_476)
    - [AtomicMarkableReference](#AtomicMarkableReference_524)
- [原子数组](#_577)
- - [不安全的数组](#_627)
  - [安全的数组](#_646)
- [字段更新器](#_665)
- [原子累加器](#_712)
- - [累加器性能比较](#_714)
  - [源码之 LongAdder](#_LongAdder_773)
  - [模拟cas 锁](#cas__794)
  - [原理之伪共享](#_847)
  - [LongAdder的add()累加源码](#LongAdderadd_894)
  - [longAccumulate()源码](#longAccumulate_931)
  - [sum()源码](#sum_1049)
- [Unsafe](#Unsafe_1069)
- - [概述](#_1071)
  - [Unsafe CAS 操作](#Unsafe_CAS__1095)
  - [Unsafe案例实现](#Unsafe_1133)
- [本章小结](#_1228)



# 问题提出

有如下需求，保证 account.withdraw 取款方法的线程安全

```java
interface Account {
    // 获取余额
    Integer getBalance();

    // 取款
    void withdraw(Integer amount);

    /**
     * 方法内会启动 1000 个线程，每个线程做 -10 元 的操作
     * 如果初始余额为 10000 那么正确的结果应当是 0
     */
    static void demo(Account account) {
        List<Thread> ts = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            ts.add(new Thread(() -> {
                account.withdraw(10);
            }));
        }
        long start = System.nanoTime();
        ts.forEach(Thread::start);
        ts.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long end = System.nanoTime();
        System.out.println(account.getBalance()
                + " cost: " + (end-start)/1000_000 + " ms");
    }
}
1234567891011121314151617181920212223242526272829303132
```

原有实现并不是线程安全的

```java
class AccountUnsafe implements Account {

    private Integer balance;

    public AccountUnsafe(Integer balance) {
        this.balance = balance;
    }

    @Override
    public Integer getBalance() {      
      	return this.balance;
    }

    @Override
    public void withdraw(Integer amount) {   
      	this.balance -= amount;
    }
}
123456789101112131415161718
```

- 单核的指令交错
- 多核的指令交错

## 解决思路-锁

首先想到的是给 Account 对象加锁

```java
class AccountUnsafe implements Account {

    private Integer balance;

    public AccountUnsafe(Integer balance) {
        this.balance = balance;
    }

    @Override
    public Integer getBalance() {
        synchronized (this) {
            return this.balance;
        }
    }

    @Override
    public void withdraw(Integer amount) {
        synchronized (this) {
            this.balance -= amount;
        }
    }
}
12345678910111213141516171819202122
```

## 解决思路-无锁

```java
class AccountCas implements Account {
    private AtomicInteger balance;

    public AccountCas(int balance) {
        this.balance = new AtomicInteger(balance);
    }

    @Override
    public Integer getBalance() {
        return balance.get();
    }

    @Override
    public void withdraw(Integer amount) {
        /*while(true) {
            // 获取余额的最新值
            int prev = balance.get();
            // 要修改的余额
            int next = prev - amount;
            // 真正修改
            if(balance.compareAndSet(prev, next)) {
                break;
            }
        }*/
        balance.getAndAdd(-1 * amount);
    }
}
123456789101112131415161718192021222324252627
```

# [CAS](https://so.csdn.net/so/search?q=CAS&spm=1001.2101.3001.7020) 与 volatile

## CAS

前面看到的 AtomicInteger 的解决方法，内部并没有用锁来保护共享变量的线程安全。那么它是如何实现的呢？

```java
public void withdraw(Integer amount) {
    while (true) {
        // 需要不断尝试，直到成功为止
        while (true) {
            // 比如拿到了旧值 1000
            int prev = balance.get();
            // 在这个基础上 1000-10 = 990
            int next = prev - amount;
            /*
                compareAndSet 正是做这个检查，在 set 前，先比较 prev 与当前值不一致了，next 作废，返回 false 表示失败
                比如，别的线程已经做了减法，当前值已经被减成了 990
                那么本线程的这次 990 就作废了，进入 while 下次循环重试一致，以 next 设置为新值，返回 true 表示成功
            */
            if (balance.compareAndSet(prev, next)) {
                break;
            }
        }
    }
}
12345678910111213141516171819
```

其中的关键是 compareAndSet，它的简称就是 CAS （也有 Compare And Swap 的说法），它必须是原子操作。

![image-20211102161711113](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/fe43e5392d4e4d2d78d37a87a26bde2e.png)

> **注意**
>
> 其实 CAS 的底层是 lock cmpxchg 指令（X86 架构），在单核 CPU 和多核 CPU 下都能够保证【比较-交换】的原子性。
>
> 在多核状态下，某个核执行到带 lock 的指令时，CPU 会让总线锁住，当这个核把此指令执行完毕，再开启总线。这个过程中不会被线程的调度机制所打断，保证了多个线程对内存操作的准确性，是原子的。

## [volatile](https://so.csdn.net/so/search?q=volatile&spm=1001.2101.3001.7020)

获取共享变量时，为了保证该变量的可见性，需要使用 volatile 修饰。

它可以用来修饰成员变量和静态成员变量，他可以避免线程从自己的工作缓存中查找变量的值，必须到主存中获取它的值，线程操作 volatile 变量都是直接操作主存。即一个线程对 volatile 变量的修改，对另一个线程可见。

> **注意**
>
> volatile 仅仅保证了共享变量的可见性，让其它线程能够看到最新值，但不能解决指令交错问题（不能保证原子性）

CAS 必须借助 volatile 才能读取到共享变量的最新值来实现【比较并交换】的效果

## 为什么无锁效率高

- 无锁情况下，即使重试失败，线程始终在高速运行，没有停歇，而 synchronized 会让线程在没有获得锁的时候，发生上下文切换，进入阻塞。打个比喻
- 线程就好像高速跑道上的赛车，高速运行时，速度超快，一旦发生上下文切换，就好比赛车要减速、熄火，等被唤醒又得重新打火、启动、加速… 恢复到高速运行，代价比较大
- 但无锁情况下，因为线程要保持运行，需要额外 CPU 的支持，CPU 在这里就好比高速跑道，没有额外的跑道，线程想高速运行也无从谈起，虽然不会进入阻塞，但由于没有分到时间片，仍然会进入可运行状态，还是会导致上下文切换。（线程数少于cpu核心数时效果好）

[外链图片转存失败,源站可能有防盗链机制,建议将图片保存下来直接上传(img-eUeMuvh6-1635912858045)(https://chasing1874.oss-cn-chengdu.aliyuncs.com/image-20211102162700594.png)]

## CAS 的特点

结合 CAS 和 volatile 可以实现无锁[并发](https://so.csdn.net/so/search?q=并发&spm=1001.2101.3001.7020)，适用于线程数少、多核 CPU 的场景下。

- CAS 是基于乐观锁的思想：最乐观的估计，不怕别的线程来修改共享变量，就算改了也没关系，我吃亏点再重试呗。
- synchronized 是基于悲观锁的思想：最悲观的估计，得防着其它线程来修改共享变量，我上了锁你们都别想改，我改完了解开锁，你们才有机会。
- CAS 体现的是无锁并发、无阻塞并发，请仔细体会这两句话的意思
  - 因为没有使用 synchronized，所以线程不会陷入阻塞，这是效率提升的因素之一
  - 但如果竞争激烈，可以想到重试必然频繁发生，反而效率会受影响

# 原子整数

J.U.C 并发包提供了：

- AtomicBoolean
- AtomicInteger
- AtomicLong

以 AtomicInteger 为例

```java
AtomicInteger i = new AtomicInteger(0);

// 获取并自增（i = 0, 结果 i = 1, 返回 0），类似于 i++
System.out.println(i.getAndIncrement());

// 自增并获取（i = 1, 结果 i = 2, 返回 2），类似于 ++i
System.out.println(i.incrementAndGet());

// 自减并获取（i = 2, 结果 i = 1, 返回 1），类似于 --i
System.out.println(i.decrementAndGet());

// 获取并自减（i = 1, 结果 i = 0, 返回 1），类似于 i--
System.out.println(i.getAndDecrement());

// 获取并加值（i = 0, 结果 i = 5, 返回 0）
System.out.println(i.getAndAdd(5));

// 加值并获取（i = 5, 结果 i = 0, 返回 0）
System.out.println(i.addAndGet(-5));

// 获取并更新（i = 0, p 为 i 的当前值, 结果 i = -2, 返回 0）
// 其中函数中的操作能保证原子，但函数需要无副作用
System.out.println(i.getAndUpdate(p -> p - 2));

// 更新并获取（i = -2, p 为 i 的当前值, 结果 i = 0, 返回 0）
// 其中函数中的操作能保证原子，但函数需要无副作用
System.out.println(i.updateAndGet(p -> p + 2));

// 获取并计算（i = 0, p 为 i 的当前值, x 为参数1, 结果 i = 10, 返回 0）
// 其中函数中的操作能保证原子，但函数需要无副作用
// getAndUpdate 如果在 lambda 中引用了外部的局部变量，要保证该局部变量是 final 的
// getAndAccumulate 可以通过 参数1 来引用外部的局部变量，但因为其不在 lambda 中因此不必是 final
System.out.println(i.getAndAccumulate(10, (p, x) -> p + x));

// 计算并获取（i = 10, p 为 i 的当前值, x 为参数1, 结果 i = 0, 返回 0）
// 其中函数中的操作能保证原子，但函数需要无副作用
System.out.println(i.accumulateAndGet(-10, (p, x) -> p + x));
12345678910111213141516171819202122232425262728293031323334353637
```

updateAndGet[源码](https://so.csdn.net/so/search?q=源码&spm=1001.2101.3001.7020)

```java
public final int updateAndGet(IntUnaryOperator updateFunction) {
    int prev, next;
    do {
        prev = get();
        next = updateFunction.applyAsInt(prev);
    } while (!compareAndSet(prev, next));
    return next;
}
12345678
```

- IntUnaryOperator是函数式接口，可以用lambda表达式表示具体实现里面的方法int applyAsInt(int operand);
- 如i.updateAndGet(p -> p + 2)，p -> p + 2就是int applyAsInt(int operand);的lambda实现。就是实现+2的操作

# 原子引用

为什么需要原子引用类型？

- AtomicReference
- AtomicMarkableReference
- AtomicStampedReference

有如下方法

```java
interface DecimalAccount {
    // 获取余额
    BigDecimal getBalance();

    // 取款
    void withdraw(BigDecimal amount);

    /**
     * 方法内会启动 1000 个线程，每个线程做 -10 元 的操作
     * 如果初始余额为 10000 那么正确的结果应当是 0
     */
    static void demo(DecimalAccount account) {
        List<Thread> ts = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            ts.add(new Thread(() -> {
                account.withdraw(BigDecimal.TEN);
            }));
        }
        ts.forEach(Thread::start);
        ts.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(account.getBalance());
    }
}
1234567891011121314151617181920212223242526272829
```

试着提供不同的 DecimalAccount 实现，实现安全的取款操作

## 不安全实现

```java
class  DecimalAccountUnsafe implements DecimalAccount {
    BigDecimal balance;

  public DecimalAccountUnsafe(BigDecimal balance) {
    this.balance = balance;
  }

    @Override
    public BigDecimal getBalance() {
        return balance.get();
    }

    @Override
    public void withdraw(BigDecimal amount) {
        BigDecimal balance = this.getBalance();
				this.balance = balance.subtract(amount);
    }
}
123456789101112131415161718
```

## 安全实现-使用锁

```java
class  DecimalAccountUnsafe implements DecimalAccount {
  private final Object lock = new Object();
  BigDecimal balance;

  public DecimalAccountUnsafe(BigDecimal balance) {
    this.balance = balance;
  }

    @Override
    public BigDecimal getBalance() {
        return balance.get();
    }

    @Override
    public void withdraw(BigDecimal amount) {
        synchronized (lock) {
          BigDecimal balance = this.getBalance();
          this.balance = balance.subtract(amount);
        }
    }
}
123456789101112131415161718192021
```

## 安全实现-使用 CAS

```java
class DecimalAccountCas implements DecimalAccount {
    private AtomicReference<BigDecimal> balance;

    public DecimalAccountCas(BigDecimal balance) {
//        this.balance = balance;
        this.balance = new AtomicReference<>(balance);
    }

    @Override
    public BigDecimal getBalance() {
        return balance.get();
    }

    @Override
    public void withdraw(BigDecimal amount) {
        while(true) {
            BigDecimal prev = balance.get();
            BigDecimal next = prev.subtract(amount);
            if (balance.compareAndSet(prev, next)) {
                break;
            }
        }
    }
}
123456789101112131415161718192021222324
```

测试代码

```java
DecimalAccount.demo(new DecimalAccountUnsafe(new BigDecimal("10000")));
DecimalAccount.demo(new DecimalAccountSafeLock(new BigDecimal("10000")));
DecimalAccount.demo(new DecimalAccountSafeCas(new BigDecimal("10000")));
123
```

运行结果

```java
4310 cost: 425 ms 
0 cost: 285 ms 
0 cost: 274 ms
123
```

## ABA 问题及解决

### ABA 问题

```java
@Slf4j(topic = "c.Test36")
public class Test36 {

    static AtomicReference<String> ref=new AtomicReference<>("A");
    public static void main(String[]args)throws InterruptedException{
        log.debug("main start...");
// 获取值 A
// 这个共享变量被它线程修改过？
        String prev=ref.get();
        other();
        sleep(1);
// 尝试改为 C
        log.debug("change A->C {}",ref.compareAndSet(prev,"C"));
    }
    private static void other(){
        new Thread(()->{
            log.debug("change A->B {}",ref.compareAndSet(ref.get(),"B"));
        },"t1").start();
        sleep(0.5);

        new Thread(()->{
            log.debug("change B->A {}",ref.compareAndSet(ref.get(),"A"));
        },"t2").start();
    }
}
12345678910111213141516171819202122232425
```

输出

![image-20211102174332441](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/3f711159701fe93e8d9b75205bd7e34a.png)

主线程仅能判断出共享变量的值与最初值 A 是否相同，不能感知到这种从 A 改为 B 又 改回 A 的情况，如果主线程希望：

只要有其它线程【动过了】共享变量，那么自己的 cas 就算失败，这时，仅比较值是不够的，需要再加一个版本号

### AtomicStampedReference

```java
@Slf4j(topic = "c.Test36")
public class Test36 {

    static AtomicStampedReference<String> ref = new AtomicStampedReference<>("A", 0);

    public static void main(String[] args) throws InterruptedException {
        log.debug("main start...");
        // 获取值 A
        String prev = ref.getReference();
        // 获取版本号
        int stamp = ref.getStamp();
        log.debug("版本 {}", stamp);
        // 如果中间有其它线程干扰，发生了 ABA 现象
        other();
        sleep(1);
        // 尝试改为 C
        log.debug("change A->C {}", ref.compareAndSet(prev, "C", stamp, stamp + 1));
    }

    private static void other() {
        new Thread(() -> {
            log.debug("change A->B {}", ref.compareAndSet(ref.getReference(), "B", ref.getStamp(), ref.getStamp() + 1));
            log.debug("更新版本为 {}", ref.getStamp());
        }, "t1").start();
        sleep(0.5);
        new Thread(() -> {
            log.debug("change B->A {}", ref.compareAndSet(ref.getReference(), "A", ref.getStamp(), ref.getStamp() + 1));
            log.debug("更新版本为 {}", ref.getStamp());
        }, "t2").start();
    }
}
12345678910111213141516171819202122232425262728293031
```

输出为

![image-20211102174952859](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/680f53d0fb3200ce9071ac3120497b20.png)

AtomicStampedReference 可以给原子引用加上版本号，追踪原子引用整个的变化过程，如： A -> B -> A -> C ，通过AtomicStampedReference，我们可以知道，引用变量中途被更改了几次。

但是有时候，并不关心引用变量更改了几次，只是单纯的关心**是否更改过**，所以就有了AtomicMarkableReference

![image-20211102175034756](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211102175034756.png)

### AtomicMarkableReference

```java
@Slf4j(topic = "c.Test38")
public class Test38 {
    public static void main(String[] args) throws InterruptedException {
        GarbageBag bag = new GarbageBag("装满了垃圾");
        // 参数2 mark 可以看作一个标记，表示垃圾袋满了
        AtomicMarkableReference<GarbageBag> ref = new AtomicMarkableReference<>(bag, true);

        log.debug("start...");
        GarbageBag prev = ref.getReference();
        log.debug(prev.toString());

        new Thread(() -> {
            log.debug("start...");
            bag.setDesc("空垃圾袋");
            ref.compareAndSet(bag, bag, true, false);
            log.debug(bag.toString());
        },"保洁阿姨").start();

        sleep(1);
        log.debug("想换一只新垃圾袋？");
        boolean success = ref.compareAndSet(prev, new GarbageBag("空垃圾袋"), true, false);
        log.debug("换了么？" + success);
        log.debug(ref.getReference().toString());
    }
}

class GarbageBag {
    String desc;

    public GarbageBag(String desc) {
        this.desc = desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return super.toString() + " " + desc;
    }
}
123456789101112131415161718192021222324252627282930313233343536373839404142
```

输出

![image-20211102175632525](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/8cca95a73ecedaec6db9890a806077dc.png)

# 原子数组

- AtomicIntegerArray
- AtomicLongArray
- AtomicReferenceArray

有如下方法

```java
 /**
     参数1，提供数组、可以是线程不安全数组或线程安全数组
     参数2，获取数组长度的方法
     参数3，自增方法，回传 array, index
     参数4，打印数组的方法
     */
    // supplier 提供者 无中生有  ()->结果
    // function 函数   一个参数一个结果   (参数)->结果  ,  BiFunction (参数1,参数2)->结果
    // consumer 消费者 一个参数没结果  (参数)->void,      BiConsumer (参数1,参数2)->
    private static <T> void demo(
            Supplier<T> arraySupplier,
            Function<T, Integer> lengthFun,
            BiConsumer<T, Integer> putConsumer,
            Consumer<T> printConsumer ) {
        List<Thread> ts = new ArrayList<>();
        T array = arraySupplier.get();
        int length = lengthFun.apply(array);
        for (int i = 0; i < length; i++) {
            // 每个线程对数组作 10000 次操作
            ts.add(new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    putConsumer.accept(array, j%length);
                }
            }));
        }

        ts.forEach(t -> t.start()); // 启动所有线程
        ts.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });     // 等所有线程结束
        printConsumer.accept(array);
    }
}
12345678910111213141516171819202122232425262728293031323334353637
```

## 不安全的数组

```java
public static void main(String[] args) {
    demo(
            ()->new int[10],
            (array)->array.length,
            (array, index) -> array[index]++,
            array-> System.out.println(Arrays.toString(array))
    );
}
12345678
```

结果

![image-20211103103323685](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/8ea90307a060c02d4091daf4e038a46c.png)

## 安全的数组

```java
public static void main(String[] args) {
    demo(
            ()-> new AtomicIntegerArray(10),
            (array) -> array.length(),
            (array, index) -> array.getAndIncrement(index),
            array -> System.out.println(array)
    );
}
12345678
```

结果

![image-20211103103406962](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/4b140493eaa7f0ccfd92d43260658e44.png)

# 字段更新器

- AtomicReferenceFieldUpdater // 域 字段
- AtomicIntegerFieldUpdater
- AtomicLongFieldUpdater

利用字段更新器，可以针对对象的某个域（Field）进行原子操作，只能配合 volatile 修饰的字段使用，否则会出现异常

```java
Exception in thread "main" java.lang.IllegalArgumentException: Must be volatile type
1
@Slf4j(topic = "c.Test40")
public class Test40 {

    public static void main(String[] args) {
        Student stu = new Student();

        AtomicReferenceFieldUpdater updater =
                AtomicReferenceFieldUpdater.newUpdater(Student.class, String.class, "name");

        System.out.println(updater.compareAndSet(stu, null, "张三"));
        System.out.println(stu);
    }
}

class Student {
    volatile String name;

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                '}';
    }
}
123456789101112131415161718192021222324
```

输出

![image-20211103104121985](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211103104121985.png)

# 原子累加器

## 累加器性能比较

```java
public class Test41 {
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            demo(
                    () -> new AtomicLong(0),
                    (adder) -> adder.getAndIncrement()
            );
        }

        for (int i = 0; i < 5; i++) {
            demo(
                    () -> new LongAdder(),
                    adder -> adder.increment()
            );
        }
    }

    /*
    () -> 结果    提供累加器对象
    (参数) ->     执行累加操作
     */
    private static <T> void demo(Supplier<T> adderSupplier, Consumer<T> action) {
        T adder = adderSupplier.get();
        List<Thread> ts = new ArrayList<>();
        // 4 个线程，每人累加 50 万
        for (int i = 0; i < 4; i++) {
            ts.add(new Thread(() -> {
                for (int j = 0; j < 500000; j++) {
                    action.accept(adder);
                }
            }));
        }
        long start = System.nanoTime();
        ts.forEach(t -> t.start());
        ts.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        long end = System.nanoTime();
        System.out.println(adder + " cost:" + (end - start) / 1000_000);
    }
}
12345678910111213141516171819202122232425262728293031323334353637383940414243444546
```

输出

![image-20211103105133691](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211103105133691.png)

性能提升的原因很简单，就是在有竞争时，设置多个累加单元，Therad-0 累加 Cell[0]，而 Thread-1 累加Cell[1]… 最后将结果汇总。这样它们在累加时操作的不同的 Cell 变量，因此减少了 CAS 重试失败，从而提高性能。

## 源码之 LongAdder

LongAdder 是并发大师 @author Doug Lea （大哥李）的作品，设计的非常精巧

LongAdder 类有几个关键域

```java
// 累加单元数组, 懒惰初始化
transient volatile Cell[] cells;

// 基础值, 如果没有竞争, 则用 cas 累加这个域
transient volatile long base;

// 在 cells 创建或扩容时, 置为 1, 表示加锁
transient volatile int cellsBusy;
12345678
```

无锁并发，这个怎么用了锁呢。这里并没有真正加锁，而是用了CAS实现锁

## 模拟cas 锁

```java
public class LockCas {
    // 0 没加锁
    // 1 加锁
    private AtomicInteger state = new AtomicInteger(0);

    public void lock() {
        while (true) {
            if (state.compareAndSet(0, 1)) {
                break;
            }
        }
    }

    public void unlock() {
        log.debug("unlock...");
        state.set(0);
    }

    public static void main(String[] args) {
        LockCas lock = new LockCas();
        new Thread(() -> {
            log.debug("begin...");
            lock.lock();
            try {
                log.debug("lock...");
                sleep(1);
            } finally {
                lock.unlock();
            }
        }).start();

        new Thread(() -> {
            log.debug("begin...");
            lock.lock();
            try {
                log.debug("lock...");
            } finally {
                lock.unlock();
            }
        }).start();
    }
}
123456789101112131415161718192021222324252627282930313233343536373839404142
```

输出

![image-20211103110113929](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211103110113929.png)

## 原理之伪共享

其中 Cell 即为累加单元

```java
// 防止缓存行伪共享
@sun.misc.Contended
static final class Cell {
    volatile long value;
    Cell(long x) { value = x; }
    // 最重要的方法, 用来 cas 方式进行累加, prev 表示旧值, next 表示新值
    final boolean cas(long prev, long next) {
        return UNSAFE.compareAndSwapLong(this, valueOffset, prev, next);
    }
// 省略不重要代码
}
1234567891011
```

得从缓存说起

缓存与内存的速度比较

![image-20211103111345146](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/d115a1b16ba96bf1527c803c1e8129fd.png)

![image-20211103111357526](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/1a91bf219ee41d605d752bc058700bec.png)

- 因为 CPU 与 内存的速度差异很大，需要靠预读数据至缓存来提升效率。
- 而缓存以缓存行为单位，每个缓存行对应着一块内存，一般是 64 byte（8 个 long）
- 缓存的加入会造成数据副本的产生，即同一份数据会缓存在不同核心的缓存行中
- CPU 要保证数据的一致性，如果某个 CPU 核心更改了数据，其它 CPU 核心对应的整个缓存行必须失效

![image-20211103111429290](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/1c5d0023509cb8bdbe182dd99b3ec182.png)

因为 Cell 是数组形式，在内存中是连续存储的，一个 Cell 为 24 字节（16 字节的对象头和 8 字节的 value），因此缓存行可以存下 2 个的 Cell 对象。这样问题来了：

- Core-0 要修改 Cell[0]
- Core-1 要修改 Cell[1]

无论谁修改成功，都会导致对方 Core 的缓存行失效，比如 Core-0 中 Cell[0]=6000, Cell[1]=8000 要累加Cell[0]=6001, Cell[1]=8000 ，这时会让 Core-1 的缓存行失效

@sun.misc.Contended 用来解决这个问题，它的原理是在使用此注解的对象或字段的前后各增加 128 字节大小的padding，从而让 CPU 将对象预读至缓存时占用不同的缓存行，这样，不会造成对方缓存行的失效

![image-20211103111521604](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/b2ce2ba12b3f705d49bb37ca10d955af.png)

## LongAdder的add()累加源码

累加主要调用下面的方法

```java
public void add(long x) {
  // as 为累加单元数组
  // b 为基础值      
  // x 为累加值      
  Cell[] as; long b, v; int m; Cell a;
  // 进入 if 的两个条件
  // 1. as 有值, 表示已经发生过竞争, 进入 if
  // 2. cas 给 base 累加时失败了, 表示 base 发生了竞争, 进入 if
  if ((as = cells) != null || !casBase(b = base, b + x)) {
    // uncontended 表示 cell 没有竞争
    boolean uncontended = true;
    if (
      // as 还没有创建
      as == null || (m = as.length - 1) < 0 ||
      // 当前线程对应的 cell 还没有
      (a = as[getProbe() & m]) == null ||
      // cas 给当前线程的 cell 累加失败 uncontended=false ( a 为当前线程的 cell )
      !(uncontended = a.cas(v = a.value, v + x))) 
    {
      // 进入 cell 数组创建、cell 创建的流程
      longAccumulate(x, null, uncontended);
    }
  }
}
123456789101112131415161718192021222324
```

add 流程图

![image-20211103112411307](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/0438787fe3036a4bf4ddd05379f08cd8.png)

## longAccumulate()源码

```java
final void longAccumulate(long x, LongBinaryOperator fn,
                          boolean wasUncontended) {
    int h;
  // 当前线程还没有对应的 cell, 需要随机生成一个 h 值用来将当前线程绑定到 cell
    if ((h = getProbe()) == 0) {
      // 初始化 probe
        ThreadLocalRandom.current(); // force initialization
      // h 对应新的 probe 值, 用来对应 cell
        h = getProbe();
        wasUncontended = true;
    }
  // collide 为 true 表示需要扩容
    boolean collide = false;                // True if last slot nonempty
    for (;;) {
        Cell[] as; Cell a; int n; long v;
      // 已经有了 cells
        if ((as = cells) != null && (n = as.length) > 0) {
          // 还没有 cell
            if ((a = as[(n - 1) & h]) == null) {
              // 为 cellsBusy 加锁, 创建 cell, cell 的初始累加值为 x
							// 成功则 break, 否则继续 continue 循环
                if (cellsBusy == 0) {       // Try to attach new Cell
                    Cell r = new Cell(x);   // Optimistically create
                    if (cellsBusy == 0 && casCellsBusy()) {
                        boolean created = false;
                        try {               // Recheck under lock
                            Cell[] rs; int m, j;
                            if ((rs = cells) != null &&
                                (m = rs.length) > 0 &&
                                rs[j = (m - 1) & h] == null) {
                                rs[j] = r;
                                created = true;
                            }
                        } finally {
                            cellsBusy = 0;
                        }
                        if (created)
                            break;
                        continue;           // Slot is now non-empty
                    }
                }
                collide = false;
            }
          // 有竞争, 改变线程对应的 cell 来重试 cas
            else if (!wasUncontended)       // CAS already known to fail
                wasUncontended = true;      // Continue after rehash
          // cas 尝试累加, fn 配合 LongAccumulator 不为 null, 配合 LongAdder 为 null
            else if (a.cas(v = a.value, ((fn == null) ? v + x :
                                         fn.applyAsLong(v, x))))
                break;
          // 如果 cells 长度已经超过了最大长度, 或者已经扩容, 改变线程对应的 cell 来重试 cas
            else if (n >= NCPU || cells != as)
                collide = false;            // At max size or stale
          // 确保 collide 为 false 进入此分支, 就不会进入下面的 else if 进行扩容了
            else if (!collide)
                collide = true;
          // 加锁
            else if (cellsBusy == 0 && casCellsBusy()) {
                try {
                    if (cells == as) {      // Expand table unless stale
                        Cell[] rs = new Cell[n << 1];
                        for (int i = 0; i < n; ++i)
                            rs[i] = as[i];
                        cells = rs;
                    }
                } finally {
                    cellsBusy = 0;
                }
                collide = false;
              // 加锁成功, 扩容
                continue;                   // Retry with expanded table
            }
          // 改变线程对应的 cell
            h = advanceProbe(h);
        }
      // 还没有 cells, 尝试给 cellsBusy 加锁
        else if (cellsBusy == 0 && cells == as && casCellsBusy()) {
          // 加锁成功, 初始化 cells, 最开始长度为 2, 并填充一个 cell
					// 成功则 break;
            boolean init = false;
            try {                           // Initialize table
                if (cells == as) {
                    Cell[] rs = new Cell[2];
                    rs[h & 1] = new Cell(x);
                    cells = rs;
                    init = true;
                }
            } finally {
                cellsBusy = 0;
            }
            if (init)
                break;
        }
      // 上两种情况失败, 尝试给 base 累加
        else if (casBase(v = base, ((fn == null) ? v + x :
                                    fn.applyAsLong(v, x))))
            break;                          // Fall back on using base
    }
}
123456789101112131415161718192021222324252627282930313233343536373839404142434445464748495051525354555657585960616263646566676869707172737475767778798081828384858687888990919293949596979899
```

![image-20211103114603511](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/745dba07c3f068eee5e3234e099bdcef.png)

![image-20211103114616426](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/f21820470f31b45d31521f1a1b7dfa8b.png)

每个线程刚进入 longAccumulate 时，会尝试对应一个 cell 对象（找到一个坑位）

![image-20211103114638528](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/78a0ea16d87509eec9e022ff39c3c789.png)

## sum()源码

获取最终结果通过 sum 方法

```java
public long sum() {
    Cell[] as = cells; Cell a;
    long sum = base;
    if (as != null) {
        for (int i = 0; i < as.length; ++i) {
            if ((a = as[i]) != null)
                sum += a.value;
        }
    }
    return sum;
}
1234567891011
```

# [Unsafe](https://so.csdn.net/so/search?q=Unsafe&spm=1001.2101.3001.7020)

## 概述

Unsafe 对象提供了非常底层的，操作内存、线程的方法，Unsafe 对象不能直接调用，只能通过反射获得

```java
public class UnsafeAccessor {
    static Unsafe unsafe;
    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            unsafe = (Unsafe) theUnsafe.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new Error(e);
        }
    }
    static Unsafe getUnsafe() {
        return unsafe;
    }
}
123456789101112131415
```

## Unsafe CAS 操作

```java
public class TestUnsafe {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        Unsafe unsafe = (Unsafe) theUnsafe.get(null);

        System.out.println(unsafe);

        // 1. 获取域的偏移地址
        long idOffset = unsafe.objectFieldOffset(Teacher.class.getDeclaredField("id"));
        long nameOffset = unsafe.objectFieldOffset(Teacher.class.getDeclaredField("name"));

        Teacher t = new Teacher();
        // 2. 执行 cas 操作
        unsafe.compareAndSwapInt(t, idOffset, 0, 1);
        unsafe.compareAndSwapObject(t, nameOffset, null, "张三");

        // 3. 验证
        System.out.println(t);
    }
}
@Data
class Teacher {
    volatile int id;
    volatile String name;
}
123456789101112131415161718192021222324252627
```

输出

![image-20211103115855928](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211103115855928.png)

## Unsafe案例实现

使用自定义的 AtomicData 实现之前线程安全的原子整数 Account 实现

```java
package cn.itcast.test;

import cn.itcast.n4.UnsafeAccessor;
import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

@Slf4j(topic = "c.Test42")
public class Test42 {
    public static void main(String[] args) {
        Account.demo(new MyAtomicInteger(10000));
    }
}

class MyAtomicInteger implements Account {
    private volatile int value;
    private static final long valueOffset;
    private static final Unsafe UNSAFE;
    static {
        UNSAFE = UnsafeAccessor.getUnsafe();
        try {
            valueOffset = UNSAFE.objectFieldOffset(MyAtomicInteger.class.getDeclaredField("value"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public int getValue() {
        return value;
    }

    public void decrement(int amount) {
        while(true) {
            int prev = this.value;
            int next = prev - amount;
            if (UNSAFE.compareAndSwapInt(this, valueOffset, prev, next)) {
                break;
            }
        }
    }

    public MyAtomicInteger(int value) {
        this.value = value;
    }

    @Override
    public Integer getBalance() {
        return getValue();
    }

    @Override
    public void withdraw(Integer amount) {
        decrement(amount);
    }
  
  /**
     * 方法内会启动 1000 个线程，每个线程做 -10 元 的操作
     * 如果初始余额为 10000 那么正确的结果应当是 0
     */
    static void demo(Account account) {
        List<Thread> ts = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            ts.add(new Thread(() -> {
                account.withdraw(10);
            }));
        }
        long start = System.nanoTime();
        ts.forEach(Thread::start);
        ts.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long end = System.nanoTime();
        System.out.println(account.getBalance()
                + " cost: " + (end-start)/1000_000 + " ms");
    }
}
1234567891011121314151617181920212223242526272829303132333435363738394041424344454647484950515253545556575859606162636465666768697071727374757677787980
```

输出

![image-20211103120645918](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211103120645918.png)

# 本章小结

- CAS 与 volatile
- [API](https://so.csdn.net/so/search?q=API&spm=1001.2101.3001.7020)
  - 原子整数
  - 原子引用
  - 原子数组
  - 字段更新器
  - 原子累加器
- Unsafe

- 原理方面
  - LongAdder 源码
  - 伪共享