# 共享模型之JUC

[toc]



# AQS 原理

## 概述

全称是 AbstractQueuedSynchronizer，是阻塞式锁和相关的同步器工具的框架

特点：

- 用 state 属性来表示资源的状态（分独占模式和共享模式），子类需要定义如何维护这个状态，控制如何获取锁和释放锁
  - getState - 获取 state 状态
  - setState - 设置 state 状态
  - compareAndSetState - cas 机制设置 state 状态
  - 独占模式是只有一个线程能够访问资源，而共享模式可以允许多个线程访问资源
- 提供了基于 FIFO 的等待队列，类似于 Monitor 的 EntryList
- 条件变量来实现等待、唤醒机制，支持多个条件变量，类似于 Monitor 的 WaitSet

子类主要实现这样一些方法（默认抛出 UnsupportedOperationException）

- tryAcquire
- tryRelease
- tryAcquireShared
- tryReleaseShared
- isHeldExclusively

获取锁的姿势

```java
// 如果获取锁失败
if (!tryAcquire(arg)) {
  // 入队, 可以选择阻塞当前线程 park unpark
}
1234
```

释放锁的姿势

```java
// 如果释放锁成功
if (tryRelease(arg)) {
  // 让阻塞线程恢复运行
}
1234
```

## 实现不可重入锁

```java
// 自定义锁（不可重入锁）
class MyLock implements Lock {

    // 独占锁  同步器类
    class MySync extends AbstractQueuedSynchronizer {
        @Override
        protected boolean tryAcquire(int arg) {
            if(compareAndSetState(0, 1)) {
                // 加上了锁，并设置 owner 为当前线程
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        @Override // 是否持有独占锁
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        public Condition newCondition() {
            return new ConditionObject();
        }
    }

    private MySync sync = new MySync();

    @Override // 加锁（不成功会进入等待队列）
    public void lock() {
      // 内部实现，在tryAcquire失败后会加入entryList
        sync.acquire(1);
    }

    @Override // 加锁，可打断
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override // 尝试加锁（一次）
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override // 尝试加锁，带超时
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override // 解锁
    public void unlock() {
        sync.release(1);
    }

    @Override // 创建条件变量
    public Condition newCondition() {
        return sync.newCondition();
    }
}
```

测试一下

```java
@Slf4j(topic = "c.TestAqs")
public class TestAqs {
    public static void main(String[] args) {
        MyLock lock = new MyLock();
        new Thread(() -> {
            lock.lock();
            try {
                log.debug("locking...");
                sleep(1);
            } finally {
                log.debug("unlocking...");
                lock.unlock();
            }
        },"t1").start();

        new Thread(() -> {
            lock.lock();
            try {
                log.debug("locking...");
            } finally {
                log.debug("unlocking...");
                lock.unlock();
            }
        },"t2").start();
    }
}
1234567891011121314151617181920212223242526
```

![image-20211107104148605](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/f2facaf83a80b7eca78955612ca7b22c.png)

不可重入测试

如果改为下面代码，会发现自己也会被挡住（只会打印一次 locking）

```java
@Slf4j(topic = "c.TestAqs")
public class TestAqs {
    public static void main(String[] args) {
        MyLock lock = new MyLock();
        new Thread(() -> {
            lock.lock();
            log.debug("locking...");
            lock.lock();
            log.debug("locking...");
            try {
                log.debug("locking...");
                sleep(1);
            } finally {
                log.debug("unlocking...");
                lock.unlock();
            }
        },"t1").start(); 
    }
}
12345678910111213141516171819
```

 ![image-20211107104259489](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/ac5186f6246fffa343d1fcc9954d1851.png)

# ReentrantLock 原理

![image-20211107104748050](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/0e060a9b13d2a0b6c288fad78d5d4153.png)

## 非公平锁实现原理

### 加锁解锁流程

先从构造器开始看，默认为非公平锁实现

```java
public ReentrantLock() {
  sync = new NonfairSync();
}
```

NonfairSync 继承自 AQS

没有竞争时

![image-20211107104906807](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/795dd620ea3a0a78b55edc8788990a7d.png)

第一个竞争出现时

![image-20211107105640748](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/8028403c145a7225d24d8cb1041cfebd.png)

Thread-1 执行了

1. CAS 尝试将 state 由 0 改为 1，结果失败
2. 进入 tryAcquire 逻辑，这时 state 已经是1，结果仍然失败
3. 接下来进入 addWaiter 逻辑，构造 Node 队列
   - 图中黄色三角表示该 Node 的 waitStatus 状态，其中 0 为默认正常状态
   - Node 的创建是懒惰的
   - 其中第一个 Node 称为 Dummy（哑元）或哨兵，用来占位，并不关联线程

#### new NonfairSync();源码

```java
static final class NonfairSync extends Sync {
    private static final long serialVersionUID = 7316153563782823691L;

    /**
     * Performs lock.  Try immediate barge, backing up to normal
     * acquire on failure.
     */
    final void lock() {
        if (compareAndSetState(0, 1))
            setExclusiveOwnerThread(Thread.currentThread());
        else
            acquire(1);
    }

    protected final boolean tryAcquire(int acquires) {
        return nonfairTryAcquire(acquires);
    }
}
123456789101112131415161718
```

#### acquire()源码

```java
public final void acquire(int arg) {
    if (!tryAcquire(arg) &&
        acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
        selfInterrupt();
}
12345
```

![image-20211107110100797](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/fdd670c7516178c820f1fb52eeb01db1.png)

当前线程进入 acquireQueued 逻辑

1. acquireQueued 会在一个死循环中不断尝试获得锁，失败后进入 park 阻塞
2. 如果自己是紧邻着 head（排第二位），那么再次 tryAcquire 尝试获取锁，当然这时 state 仍为 1，失败
3. 进入 shouldParkAfterFailedAcquire 逻辑，将前驱 node，即 head 的 waitStatus 改为 -1，这次返回 false

#### acquireQueued()源码

```java
final boolean acquireQueued(final Node node, int arg) {
    boolean failed = true;
    try {
        boolean interrupted = false;
        for (;;) {
            final Node p = node.predecessor();
            if (p == head && tryAcquire(arg)) {
                setHead(node);
                p.next = null; // help GC
                failed = false;
                return interrupted;
            }
            if (shouldParkAfterFailedAcquire(p, node) &&
                parkAndCheckInterrupt())
                interrupted = true;
        }
    } finally {
        if (failed)
            cancelAcquire(node);
    }
}
123456789101112131415161718192021
```

![image-20211107110233016](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/b038c1e4751aaedb66d1825c793a695b.png)

1. shouldParkAfterFailedAcquire 执行完毕回到 acquireQueued ，再次 tryAcquire 尝试获取锁，当然这时state 仍为 1，失败
2. 当再次进入 shouldParkAfterFailedAcquire 时，这时因为其前驱 node 的 waitStatus 已经是 -1，这次返回true。等于-1是因为此时node已经park了，需要一个node将来把它唤醒，而这个职责就落到了这个waitStatus == -1的前驱node上了。
3. 进入 parkAndCheckInterrupt， Thread-1 park（灰色表示）

![image-20211107110456042](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/c9c291af27b0b79c5510a15aefbe7d6d.png)

再次有多个线程经历上述过程竞争失败，变成这个样子

![image-20211107110515574](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/34f6b202164df6f75679082b35acaebb.png)

如果加锁成功（没有竞争），会设置

- exclusiveOwnerThread 为 Thread-1，state = 1
- head 指向刚刚 Thread-1 所在的 Node，该 Node 清空 Thread
- 原本的 head 因为从链表断开，而可被垃圾回收

Thread-0解锁，调用ReentrantLock的unlock方法，释放开锁，让其他线程竞争加锁

#### unlock、release源码

```java
public void unlock() {
    sync.release(1);
}

public final boolean release(int arg) {
        if (tryRelease(arg)) {
            Node h = head;
            if (h != null && h.waitStatus != 0)
                unparkSuccessor(h);
            return true;
        }
        return false;
    }
12345678910111213
```

#### tryRelease()源码

```java
protected final boolean tryRelease(int releases) {
    int c = getState() - releases;
    if (Thread.currentThread() != getExclusiveOwnerThread())
        throw new IllegalMonitorStateException();
    boolean free = false;
    if (c == 0) {
        free = true;
      // 锁的拥有者置空
        setExclusiveOwnerThread(null);
    }
  // 此时state 设置为0
    setState(c);
    return free;
}
1234567891011121314
```

如果这时候有其它线程（非等待队列的）来竞争（非公平的体现），例如这时有 Thread-4 来了

![image-20211107111916982](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/3ffb6ea142a6e8c482fb0fe9d23f837d.png)

如果不巧又被 Thread-4 占了先

- Thread-4 被设置为 exclusiveOwnerThread，state = 1
- Thread-1 再次进入 acquireQueued 流程，获取锁失败，重新进入 park 阻塞

### 加锁源码

```java
// Sync 继承自 AQS
static final class NonfairSync extends Sync {
    private static final long serialVersionUID = 7316153563782823691L;
// 加锁实现
final void lock() {
// 首先用 cas 尝试（仅尝试一次）将 state 从 0 改为 1, 如果成功表示获得了独占锁
    if (compareAndSetState(0, 1))
        setExclusiveOwnerThread(Thread.currentThread());
    else
// 如果尝试失败，进入 ㈠
        acquire(1);
}
    // ㈠ AQS 继承过来的方法, 方便阅读, 放在此处
    public final void acquire(int arg) {
// ㈡ tryAcquire
        if (
                !tryAcquire(arg) &&
// 当 tryAcquire 返回为 false 时, 先调用 addWaiter ㈣, 接着 acquireQueued ㈤
                        acquireQueued(addWaiter(Node.EXCLUSIVE), arg)
        ) {
            selfInterrupt();
        }
    }
    // ㈡ 进入 ㈢
    protected final boolean tryAcquire(int acquires) {
        return nonfairTryAcquire(acquires);
    }
    // ㈢ Sync 继承过来的方法, 方便阅读, 放在此处
    final boolean nonfairTryAcquire(int acquires) {
        final Thread current = Thread.currentThread();
        int c = getState();
// 如果还没有获得锁
        if (c == 0) {
// 尝试用 cas 获得, 这里体现了非公平性: 不去检查 AQS 队列
            if (compareAndSetState(0, acquires)) {
                setExclusiveOwnerThread(current);
                return true;
            }
        }
// 如果已经获得了锁, 线程还是当前线程, 表示发生了锁重入
        else if (current == getExclusiveOwnerThread()) {
// state++
            int nextc = c + acquires;
            if (nextc < 0) // overflow
                throw new Error("Maximum lock count exceeded");
            setState(nextc);
            return true;
        }
// 获取失败, 回到调用处
        return false;
    }
    // ㈣ AQS 继承过来的方法, 方便阅读, 放在此处
    private Node addWaiter(Node mode) {// 将当前线程关联到一个 Node 对象上, 模式为独占模式
        Node node = new Node(Thread.currentThread(), mode);
// 如果 tail 不为 null, cas 尝试将 Node 对象加入 AQS 队列尾部
        Node pred = tail;
        if (pred != null) {
            node.prev = pred;
            if (compareAndSetTail(pred, node)) {
// 双向链表
                pred.next = node;
                return node;
            }
        }
// 尝试将 Node 加入 AQS, 进入 ㈥
        enq(node);
        return node;
    }
    // ㈥ AQS 继承过来的方法, 方便阅读, 放在此处
    private Node enq(final Node node) {
        for (;;) {
            Node t = tail;
            if (t == null) {
// 还没有, 设置 head 为哨兵节点（不对应线程，状态为 0）
                if (compareAndSetHead(new Node())) {
                    tail = head;
                }
            } else {
// cas 尝试将 Node 对象加入 AQS 队列尾部
                node.prev = t;
                if (compareAndSetTail(t, node)) {
                    t.next = node;
                    return t;
                }
            }
        }
    }
    // ㈤ AQS 继承过来的方法, 方便阅读, 放在此处
    final boolean acquireQueued(final Node node, int arg) {
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
                final Node p = node.predecessor();
// 上一个节点是 head, 表示轮到自己（当前线程对应的 node）了, 尝试获取
                if (p == head && tryAcquire(arg)) {
// 获取成功, 设置自己（当前线程对应的 node）为 head
                    setHead(node);
// 上一个节点 help GC
                    p.next = null;
                    failed = false;
// 返回中断标记 false
                    return interrupted;}
                if (
// 判断是否应当 park, 进入 ㈦
                        shouldParkAfterFailedAcquire(p, node) &&
// park 等待, 此时 Node 的状态被置为 Node.SIGNAL ㈧
                                parkAndCheckInterrupt()
                ) {
                    interrupted = true;
                }
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }
    // ㈦ AQS 继承过来的方法, 方便阅读, 放在此处
    private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
// 获取上一个节点的状态
        int ws = pred.waitStatus;
        if (ws == Node.SIGNAL) {
// 上一个节点都在阻塞, 那么自己也阻塞好了
            return true;
        }
// > 0 表示取消状态
        if (ws > 0) {
// 上一个节点取消, 那么重构删除前面所有取消的节点, 返回到外层循环重试
            do {
                node.prev = pred = pred.prev;
            } while (pred.waitStatus > 0);
            pred.next = node;
        } else {
// 这次还没有阻塞
// 但下次如果重试不成功, 则需要阻塞，这时需要设置上一个节点状态为 Node.SIGNAL
            compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
        }
        return false;
    }
    // ㈧ 阻塞当前线程
    private final boolean parkAndCheckInterrupt() {
        LockSupport.park(this);
        return Thread.interrupted();
    }
}
123456789101112131415161718192021222324252627282930313233343536373839404142434445464748495051525354555657585960616263646566676869707172737475767778798081828384858687888990919293949596979899100101102103104105106107108109110111112113114115116117118119120121122123124125126127128129130131132133134135136137138139140141142143144145
```

> **注意**
>
> 是否需要 unpark 是由当前节点的前驱节点的 waitStatus == Node.SIGNAL 来决定，而不是本节点的waitStatus 决定

### 解锁源码

```java
// Sync 继承自 AQS
static final class NonfairSync extends Sync {
    // 解锁实现
    public void unlock() {
        sync.release(1);
    }
    // AQS 继承过来的方法, 方便阅读, 放在此处
    public final boolean release(int arg) {
// 尝试释放锁, 进入 ㈠
        if (tryRelease(arg)) {
// 队列头节点 unpark
            Node h = head;
            if (
// 队列不为 null
                    h != null &&
// waitStatus == Node.SIGNAL 才需要 unpark
                            h.waitStatus != 0
            ) {
// unpark AQS 中等待的线程, 进入 ㈡
                unparkSuccessor(h);
            }
            return true;
        }
        return false;
    }
    // ㈠ Sync 继承过来的方法, 方便阅读, 放在此处
    protected final boolean tryRelease(int releases) {
// state--
        int c = getState() - releases;
        if (Thread.currentThread() != getExclusiveOwnerThread())
            throw new IllegalMonitorStateException();
        boolean free = false;
// 支持锁重入, 只有 state 减为 0, 才释放成功
        if (c == 0) {
            free = true;
            setExclusiveOwnerThread(null);
        }
        setState(c);
        return free;
    }
    // ㈡ AQS 继承过来的方法, 方便阅读, 放在此处
    private void unparkSuccessor(Node node) {
// 如果状态为 Node.SIGNAL 尝试重置状态为 0
// 不成功也可以
        int ws = node.waitStatus;
        if (ws < 0) {
            compareAndSetWaitStatus(node, ws, 0);
        }
// 找到需要 unpark 的节点, 但本节点从 AQS 队列中脱离, 是由唤醒节点完成的
        Node s = node.next;
// 不考虑已取消的节点, 从 AQS 队列从后至前找到队列最前面需要 unpark 的节点
        if (s == null || s.waitStatus > 0) {
            s = null;
            for (Node t = tail; t != null && t != node; t = t.prev)
                if (t.waitStatus <= 0) s = t;
        }
        if (s != null)
            LockSupport.unpark(s.thread);
    }
}
123456789101112131415161718192021222324252627282930313233343536373839404142434445464748495051525354555657585960
```

## 可重入原理

```java
static final class NonfairSync extends Sync {
    // ...
// Sync 继承过来的方法, 方便阅读, 放在此处
    final boolean nonfairTryAcquire(int acquires) {
        final Thread current = Thread.currentThread();
        int c = getState();
        if (c == 0) {
            if (compareAndSetState(0, acquires)) {
                setExclusiveOwnerThread(current);
                return true;
            }
        }
// 如果已经获得了锁, 线程还是当前线程, 表示发生了锁重入
        else if (current == getExclusiveOwnerThread()) {
// state++
            int nextc = c + acquires;
            if (nextc < 0) // overflow
                throw new Error("Maximum lock count exceeded");
            setState(nextc);
            return true;
        }
        return false;
    }
    // Sync 继承过来的方法, 方便阅读, 放在此处
    protected final boolean tryRelease(int releases) {
// state--
        int c = getState() - releases;
        if (Thread.currentThread() != getExclusiveOwnerThread())
            throw new IllegalMonitorStateException();
        boolean free = false;
// 支持锁重入, 只有 state 减为 0, 才释放成功
        if (c == 0) {
            free = true;
            setExclusiveOwnerThread(null);
        }
        setState(c);
        return free;
    }
}
123456789101112131415161718192021222324252627282930313233343536373839
```

## 可打断原理

### 不可打断模式

在此模式下，即使它被打断，仍会驻留在 AQS 队列中，一直要等到获得锁后方能得知自己被打断了

```java
// Sync 继承自 AQS
static final class NonfairSync extends Sync {
    // ...
    private final boolean parkAndCheckInterrupt() {
// 如果打断标记已经是 true, 则 park 会失效
        LockSupport.park(this);
// interrupted 会清除打断标记
        return Thread.interrupted();
    }
    final boolean acquireQueued(final Node node, int arg) {
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
                final Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null;
                    failed = false;
// 还是需要获得锁后, 才能返回打断状态
                    return interrupted;
                }
                if (
                        shouldParkAfterFailedAcquire(p, node) &&
                                parkAndCheckInterrupt()
                ) {
// 如果是因为 interrupt 被唤醒, 返回打断状态为 true
                    interrupted = true;
                }
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }
    public final void acquire(int arg) {
        if (
            !tryAcquire(arg) &&
                    acquireQueued(addWaiter(Node.EXCLUSIVE), arg)
    ) {
// 如果打断状态为 true
        selfInterrupt();
    }
    }
    static void selfInterrupt() {
// 重新产生一次中断
        Thread.currentThread().interrupt();
    }
}
12345678910111213141516171819202122232425262728293031323334353637383940414243444546474849
```

### 可打断模式

```java
static final class NonfairSync extends Sync {
    public final void acquireInterruptibly(int arg) throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
// 如果没有获得到锁, 进入 ㈠
        if (!tryAcquire(arg))
            doAcquireInterruptibly(arg);
    }
    // ㈠ 可打断的获取锁流程
    private void doAcquireInterruptibly(int arg) throws InterruptedException {
        final Node node = addWaiter(Node.EXCLUSIVE);
        boolean failed = true;
        try {
            for (;;) {
                final Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return;
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                        parkAndCheckInterrupt()) {
// 在 park 过程中如果被 interrupt 会进入此
// 这时候抛出异常, 而不会再次进入 for (;;)
                    throw new InterruptedException();
                }
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }
}
12345678910111213141516171819202122232425262728293031323334
```

## 公平锁实现原理

```java
static final class FairSync extends Sync {
    private static final long serialVersionUID = -3000897897090466540L;
    final void lock() {
        acquire(1);
    }
    // AQS 继承过来的方法, 方便阅读, 放在此处
    public final void acquire(int arg) {
        if (
                !tryAcquire(arg) &&
                        acquireQueued(addWaiter(Node.EXCLUSIVE), arg)
        ) {
            selfInterrupt();
        }
    }
    // 与非公平锁主要区别在于 tryAcquire 方法的实现
    protected final boolean tryAcquire(int acquires) {
        final Thread current = Thread.currentThread();
        int c = getState();
        if (c == 0) {
// 先检查 AQS 队列中是否有前驱节点, 没有才去竞争
            if (!hasQueuedPredecessors() &&
                    compareAndSetState(0, acquires)) {
                setExclusiveOwnerThread(current);
                return true;
            }
        }
        else if (current == getExclusiveOwnerThread()) {
            int nextc = c + acquires;
            if (nextc < 0)
                throw new Error("Maximum lock count exceeded");
            setState(nextc);
            return true;
        }
        return false;
    }
    // ㈠ AQS 继承过来的方法, 方便阅读, 放在此处
    public final boolean hasQueuedPredecessors() {
        Node t = tail;
        Node h = head;
        Node s;
// h != t 时表示队列中有 Node
        return h != t &&
                (
// (s = h.next) == null 表示队列中还有没有老二
                        (s = h.next) == null ||
                                // 或者队列中老二线程不是此线程
                                s.thread != Thread.currentThread()
                );
    }
}
1234567891011121314151617181920212223242526272829303132333435363738394041424344454647484950
```

## 条件变量实现原理

每个条件变量其实就对应着一个等待队列，其实现类是 ConditionObject

### await 流程

开始 Thread-0 持有锁，调用 await，进入 ConditionObject 的 addConditionWaiter 流程

创建新的 Node 状态为 -2（Node.CONDITION），关联 Thread-0，加入等待队列尾部

![image-20211107114729606](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/eff0bdb2a8a90f921ceb4aafff391cf9.png)

```java
public final void await() throws InterruptedException {
    if (Thread.interrupted())
        throw new InterruptedException();
  // 等待
    Node node = addConditionWaiter();
  // 释放锁
    int savedState = fullyRelease(node);
    int interruptMode = 0;
    while (!isOnSyncQueue(node)) {
        LockSupport.park(this);
        if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
            break;
    }
    if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
        interruptMode = REINTERRUPT;
    if (node.nextWaiter != null) // clean up if cancelled
        unlinkCancelledWaiters();
    if (interruptMode != 0)
        reportInterruptAfterWait(interruptMode);
}

private Node addConditionWaiter() {
            Node t = lastWaiter;
            // If lastWaiter is cancelled, clean out.
            if (t != null && t.waitStatus != Node.CONDITION) {
                unlinkCancelledWaiters();
                t = lastWaiter;
            }
            Node node = new Node(Thread.currentThread(), Node.CONDITION);
            if (t == null)
                firstWaiter = node;
            else
                t.nextWaiter = node;
            lastWaiter = node;
            return node;
        }

final int fullyRelease(Node node) {
        boolean failed = true;
        try {
          // 保证可重入锁也能释放
            int savedState = getState();
            if (release(savedState)) {
                failed = false;
                return savedState;
            } else {
                throw new IllegalMonitorStateException();
            }
        } finally {
            if (failed)
                node.waitStatus = Node.CANCELLED;
        }
    }
1234567891011121314151617181920212223242526272829303132333435363738394041424344454647484950515253
```

接下来进入 AQS 的 fullyRelease 流程，释放同步器上的锁

![image-20211107115228353](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/8c30dded8523c39a7f5f1298a7bde1d5.png)

unpark AQS 队列中的下一个节点，竞争锁，假设没有其他竞争线程，那么 Thread-1 竞争成功

![image-20211107115340211](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/23ff1a514ec231bab1c0bf036aef9680.png)

park 阻塞 Thread-0

![image-20211107115403053](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/817393a6e525bbecf2e0196be309f80f.png)

### signal 流程

假设 Thread-1 要来唤醒 Thread-0

![image-20211107120038220](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/f725eec5338f4da91800f4a0501fb634.png)

进入 ConditionObject 的 doSignal 流程，取得等待队列中第一个 Node，即 Thread-0 所在 Node

![image-20211107120113043](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/6a27f0f2cfa6e9a9d1b76fd295a43028.png)

```java
public final void signal() {
  // 判断当前线程是否是锁主
    if (!isHeldExclusively())
        throw new IllegalMonitorStateException();
    Node first = firstWaiter;
    if (first != null)
        doSignal(first);
}

private void doSignal(Node first) {
            do {
                if ( (firstWaiter = first.nextWaiter) == null)
                    lastWaiter = null;
                first.nextWaiter = null;
            } while (!transferForSignal(first) &&
                     (first = firstWaiter) != null);
        }
1234567891011121314151617
```

执行 transferForSignal 流程，将该 Node 加入 AQS 队列尾部，将 Thread-0 的 waitStatus 改为 0，Thread-3 的waitStatus 改为 -1

![image-20211107120302017](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/d1d9970953f7d4be253ddcb2030b5fa7.png)

```java
final boolean transferForSignal(Node node) {
    /*
     * If cannot change waitStatus, the node has been cancelled.
     */
  // 先把状态码从等待状态-2改为队列状态0
    if (!compareAndSetWaitStatus(node, Node.CONDITION, 0))
        return false;

    /*
     * Splice onto queue and try to set waitStatus of predecessor to
     * indicate that thread is (probably) waiting. If cancelled or
     * attempt to set waitStatus fails, wake up to resync (in which
     * case the waitStatus can be transiently and harmlessly wrong).
     */
  // 进入队列的尾部
    Node p = enq(node);
  // 返回它前驱节点的状态码
    int ws = p.waitStatus;
  // 试图把前驱节点的状态码修改为-1，因为要让他之后去唤醒node
    if (ws > 0 || !compareAndSetWaitStatus(p, ws, Node.SIGNAL))
        LockSupport.unpark(node.thread);
    return true;
}
1234567891011121314151617181920212223
```

Thread-1 释放锁，进入 unlock 流程，略

### 源码

```java
public class ConditionObject implements Condition, java.io.Serializable {
    private static final long serialVersionUID = 1173984872572414699L;
    // 第一个等待节点
    private transient Node firstWaiter;
    // 最后一个等待节点
    private transient Node lastWaiter;
    public ConditionObject() { }
    // ㈠ 添加一个 Node 至等待队列
    private Node addConditionWaiter() {
        Node t = lastWaiter;
// 所有已取消的 Node 从队列链表删除, 见 ㈡
        if (t != null && t.waitStatus != Node.CONDITION) {
            unlinkCancelledWaiters();
            t = lastWaiter;
        }
// 创建一个关联当前线程的新 Node, 添加至队列尾部
        Node node = new Node(Thread.currentThread(), Node.CONDITION);
        if (t == null)
            firstWaiter = node;
        else
            t.nextWaiter = node;
        lastWaiter = node;
        return node;
    }
    // 唤醒 - 将没取消的第一个节点转移至 AQS 队列
    private void doSignal(Node first) {
        do {
// 已经是尾节点了
            if ( (firstWaiter = first.nextWaiter) == null) {
                lastWaiter = null;
            }
            first.nextWaiter = null;
        } while (
// 将等待队列中的 Node 转移至 AQS 队列, 不成功且还有节点则继续循环 ㈢ !transferForSignal(first) &&
// 队列还有节点
                (first = firstWaiter) != null
        );
    }
    // 外部类方法, 方便阅读, 放在此处
// ㈢ 如果节点状态是取消, 返回 false 表示转移失败, 否则转移成功
    final boolean transferForSignal(Node node) {
// 如果状态已经不是 Node.CONDITION, 说明被取消了
        if (!compareAndSetWaitStatus(node, Node.CONDITION, 0))
            return false;
// 加入 AQS 队列尾部
        Node p = enq(node);
        int ws = p.waitStatus;
        if (
// 上一个节点被取消
                ws > 0 ||
// 上一个节点不能设置状态为 Node.SIGNAL
                        !compareAndSetWaitStatus(p, ws, Node.SIGNAL)
        ) {
// unpark 取消阻塞, 让线程重新同步状态
            LockSupport.unpark(node.thread);
        }
        return true;
    }
// 全部唤醒 - 等待队列的所有节点转移至 AQS 队列
private void doSignalAll(Node first) {
    lastWaiter = firstWaiter = null;
    do {
        Node next = first.nextWaiter;
        first.nextWaiter = null;
        transferForSignal(first);
        first = next;
    } while (first != null);
}
    // ㈡
    private void unlinkCancelledWaiters() {
// ...
    }
    // 唤醒 - 必须持有锁才能唤醒, 因此 doSignal 内无需考虑加锁
    public final void signal() {
        if (!isHeldExclusively())
            throw new IllegalMonitorStateException();
        Node first = firstWaiter;
        if (first != null)
            doSignal(first);
    }
    // 全部唤醒 - 必须持有锁才能唤醒, 因此 doSignalAll 内无需考虑加锁
    public final void signalAll() {
        if (!isHeldExclusively())
            throw new IllegalMonitorStateException();
        Node first = firstWaiter;
        if (first != null)
            doSignalAll(first);
    }
    // 不可打断等待 - 直到被唤醒
    public final void awaitUninterruptibly() {
// 添加一个 Node 至等待队列, 见 ㈠
        Node node = addConditionWaiter();
// 释放节点持有的锁, 见 ㈣
        int savedState = fullyRelease(node);
        boolean interrupted = false;
// 如果该节点还没有转移至 AQS 队列, 阻塞
        while (!isOnSyncQueue(node)) {
// park 阻塞
            LockSupport.park(this);
// 如果被打断, 仅设置打断状态
            if (Thread.interrupted())
                interrupted = true;
        }
// 唤醒后, 尝试竞争锁, 如果失败进入 AQS 队列
        if (acquireQueued(node, savedState) || interrupted)
            selfInterrupt();
    }
    // 外部类方法, 方便阅读, 放在此处
// ㈣ 因为某线程可能重入，需要将 state 全部释放
    final int fullyRelease(Node node) {
        boolean failed = true;
        try {
            int savedState = getState();
            if (release(savedState)) {
                failed = false;
                return savedState;
            } else {
                throw new IllegalMonitorStateException();
            }
        } finally {
            if (failed)
                node.waitStatus = Node.CANCELLED;
        }
    }
    // 打断模式 - 在退出等待时重新设置打断状态
    private static final int REINTERRUPT = 1;
    // 打断模式 - 在退出等待时抛出异常
    private static final int THROW_IE = -1;
    // 判断打断模式
    private int checkInterruptWhileWaiting(Node node) {
        return Thread.interrupted() ?
                (transferAfterCancelledWait(node) ? THROW_IE : REINTERRUPT) :
                0;
    }
    // ㈤ 应用打断模式
    private void reportInterruptAfterWait(int interruptMode)
            throws InterruptedException {
        if (interruptMode == THROW_IE)
            throw new InterruptedException();
        else if (interruptMode == REINTERRUPT)
            selfInterrupt();
    }
    // 等待 - 直到被唤醒或打断
    public final void await() throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
// 添加一个 Node 至等待队列, 见 ㈠
        Node node = addConditionWaiter();
// 释放节点持有的锁
        int savedState = fullyRelease(node);
        int interruptMode = 0;
// 如果该节点还没有转移至 AQS 队列, 阻塞
        while (!isOnSyncQueue(node)) {
// park 阻塞
            LockSupport.park(this);
            // 如果被打断, 退出等待队列
            if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                break;
        }
// 退出等待队列后, 还需要获得 AQS 队列的锁
        if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
            interruptMode = REINTERRUPT;
// 所有已取消的 Node 从队列链表删除, 见 ㈡
        if (node.nextWaiter != null)
            unlinkCancelledWaiters();
// 应用打断模式, 见 ㈤
        if (interruptMode != 0)
            reportInterruptAfterWait(interruptMode);
    }
    // 等待 - 直到被唤醒或打断或超时
    public final long awaitNanos(long nanosTimeout) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
// 添加一个 Node 至等待队列, 见 ㈠
        Node node = addConditionWaiter();
// 释放节点持有的锁
        int savedState = fullyRelease(node);
// 获得最后期限
        final long deadline = System.nanoTime() + nanosTimeout;
        int interruptMode = 0;
// 如果该节点还没有转移至 AQS 队列, 阻塞
        while (!isOnSyncQueue(node)) {
// 已超时, 退出等待队列
            if (nanosTimeout <= 0L) {
                transferAfterCancelledWait(node);
                break;
            }
// park 阻塞一定时间, spinForTimeoutThreshold 为 1000 ns
            if (nanosTimeout >= spinForTimeoutThreshold)
                LockSupport.parkNanos(this, nanosTimeout);
// 如果被打断, 退出等待队列
            if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                break;
            nanosTimeout = deadline - System.nanoTime();
        }
// 退出等待队列后, 还需要获得 AQS 队列的锁
        if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
            interruptMode = REINTERRUPT;
// 所有已取消的 Node 从队列链表删除, 见 ㈡
        if (node.nextWaiter != null)
            unlinkCancelledWaiters();
// 应用打断模式, 见 ㈤
        if (interruptMode != 0)
            reportInterruptAfterWait(interruptMode);
        return deadline - System.nanoTime();
    }
    // 等待 - 直到被唤醒或打断或超时, 逻辑类似于 awaitNanos
    public final boolean awaitUntil(Date deadline) throws InterruptedException {
// ...
    }
    // 等待 - 直到被唤醒或打断或超时, 逻辑类似于 awaitNanos
    public final boolean await(long time, TimeUnit unit) throws InterruptedException {
// ...
    }
// 工具方法 省略 ...
}
123456789101112131415161718192021222324252627282930313233343536373839404142434445464748495051525354555657585960616263646566676869707172737475767778798081828384858687888990919293949596979899100101102103104105106107108109110111112113114115116117118119120121122123124125126127128129130131132133134135136137138139140141142143144145146147148149150151152153154155156157158159160161162163164165166167168169170171172173174175176177178179180181182183184185186187188189190191192193194195196197198199200201202203204205206207208209210211212213214215216217218
```

# [读写锁](https://so.csdn.net/so/search?q=读写锁&spm=1001.2101.3001.7020)

## ReentrantReadWriteLock

当读操作远远高于写操作时，这时候使用 读写锁 让 读-读 可以并发，提高性能。 类似于数据库中的 select …from … lock in share mode

提供一个 数据容器类 内部分别使用读锁保护数据的 read() 方法，写锁保护数据的 write() 方法

```java
@Slf4j(topic = "c.DataContainer")
class DataContainer {
    private Object data;
    private ReentrantReadWriteLock rw = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock r = rw.readLock();
    private ReentrantReadWriteLock.WriteLock w = rw.writeLock();

    public Object read() {
        log.debug("获取读锁...");
        r.lock();
        try {
            log.debug("读取");
            sleep(1);
            return data;
        } finally {
            log.debug("释放读锁...");
            r.unlock();
        }
    }

    public void write() {
        log.debug("获取写锁...");
        w.lock();
        try {
            log.debug("写入");
            sleep(1);
        } finally {
            log.debug("释放写锁...");
            w.unlock();
        }
    }
}
1234567891011121314151617181920212223242526272829303132
```

测试 读锁-读锁 可以并发

```java
@Slf4j(topic = "c.TestReadWriteLock")
public class TestReadWriteLock {
    public static void main(String[] args) throws InterruptedException {
        DataContainer dataContainer = new DataContainer();
        new Thread(() -> {
            dataContainer.read();
        }, "t1").start();

        new Thread(() -> {
            dataContainer.read();
        }, "t2").start();
    }
}
12345678910111213
```

输出，从这里可以看到 Thread-0 锁定期间，Thread-1 的读操作不受影响

![image-20211107165957754](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/ec70dfda413b907e41ed92785d6a2530.png)

测试 读锁-写锁 相互阻塞

```java
@Slf4j(topic = "c.TestReadWriteLock")
public class TestReadWriteLock {
    public static void main(String[] args) throws InterruptedException {
        DataContainer dataContainer = new DataContainer();
        new Thread(() -> {
            dataContainer.read();
        }, "t1").start();

        new Thread(() -> {
            dataContainer.write();
        }, "t2").start();
    }
}
12345678910111213
```

输出

![image-20211107170146123](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/68dad5a792c60863917f0986e3570925.png)

写锁-写锁 也是相互阻塞的，这里就不测试了

注意事项

读锁不支持条件变量

重入时升级不支持：即持有读锁的情况下去获取写锁，会导致获取写锁永久等待

```java
r.lock();
try {
  // ...
  w.lock();
  try {
    // ...
  } finally{ 
    w.unlock();  
  }
} finally{ 
  r.unlock();
}
123456789101112
```

重入时降级支持：即持有写锁的情况下去获取读锁

```java
class CachedData {
    Object data;
    // 是否有效，如果失效，需要重新计算 data
    volatile boolean cacheValid;
    final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    void processCachedData() {
        rwl.readLock().lock();
        if (!cacheValid) {
// 获取写锁前必须释放读锁
            rwl.readLock().unlock();
            rwl.writeLock().lock();
            try {
// 判断是否有其它线程已经获取了写锁、更新了缓存, 避免重复更新（双重检查）
                if (!cacheValid) {
                    data = ...
                    cacheValid = true;
                }
// 降级为读锁, 释放写锁, 这样能够让其它线程读取缓存
                rwl.readLock().lock();
            } finally {
                rwl.writeLock().unlock();
            }
        }

      // 拿到读锁，自己用完数据, 释放读锁（防止自己读的时候别人写）
      // ①数据没失效，拿到读锁，可以直接读
      // ②数据失效后，那就重新计算data，然后释放写锁前拿到读锁，继续读
        try {
            use(data);
        } finally {
            rwl.readLock().unlock();
        }
    }
}
12345678910111213141516171819202122232425262728293031323334
```

## StampedLock

### 概念

该类自 JDK 8 加入，是为了进一步优化读性能，它的特点是在使用读锁、写锁时都必须配合【戳】使用

加解读锁

```java
long stamp = lock.readLock();
lock.unlockRead(stamp);
12
```

加解写锁

```java
long stamp = lock.writeLock();
lock.unlockWrite(stamp);
12
```

乐观读，StampedLock 支持 tryOptimisticRead() 方法（乐观读），读取完毕后需要做一次 戳校验 如果校验通过，表示这期间确实没有写操作，数据可以安全使用，如果校验没通过，需要重新获取读锁，保证数据安全。

```java
long stamp = lock.tryOptimisticRead();
// 验戳
if(!lock.validate(stamp)){
  // 锁升级
}
12345
```

### 示例

提供一个 数据容器类 内部分别使用读锁保护数据的 read() 方法，写锁保护数据的 write() 方法

```java
@Slf4j(topic = "c.DataContainerStamped")
class DataContainerStamped {
    private int data;
    private final StampedLock lock = new StampedLock();

    public DataContainerStamped(int data) {
        this.data = data;
    }

    public int read(int readTime) {
        long stamp = lock.tryOptimisticRead();
        log.debug("optimistic read locking...{}", stamp);
        sleep(readTime);
        if (lock.validate(stamp)) {
            log.debug("read finish...{}, data:{}", stamp, data);
            return data;
        }
        // 锁升级 - 读锁
        log.debug("updating to read lock... {}", stamp);
        try {
            stamp = lock.readLock();
            log.debug("read lock {}", stamp);
            sleep(readTime);
            log.debug("read finish...{}, data:{}", stamp, data);
            return data;
        } finally {
            log.debug("read unlock {}", stamp);
            lock.unlockRead(stamp);
        }
    }

    public void write(int newData) {
        long stamp = lock.writeLock();
        log.debug("write lock {}", stamp);
        try {
            sleep(2);
            this.data = newData;
        } finally {
            log.debug("write unlock {}", stamp);
            lock.unlockWrite(stamp);
        }
    }
}
12345678910111213141516171819202122232425262728293031323334353637383940414243
```

测试 读-读 可以优化

```java
@Slf4j(topic = "c.TestStampedLock")
public class TestStampedLock {
    public static void main(String[] args) {
        DataContainerStamped dataContainer = new DataContainerStamped(1);
        new Thread(() -> {
            dataContainer.read(1);
        }, "t1").start();
        sleep(0.5);
        new Thread(() -> {
            dataContainer.read(0);
        }, "t2").start();
    }
}
12345678910111213
```

输出结果，可以看到实际没有加读锁

![image-20211108111929505](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/4366850ee8f3ae3101559d76a66511b7.png)

测试 读-写 时优化读补加读锁

```java
@Slf4j(topic = "c.TestStampedLock")
public class TestStampedLock {
    public static void main(String[] args) {
        DataContainerStamped dataContainer = new DataContainerStamped(1);
        new Thread(() -> {
            dataContainer.read(1);
        }, "t1").start();
        sleep(0.5);
        new Thread(() -> {
            dataContainer.write(100);
        }, "t2").start();
    }
}
12345678910111213
```

输出结果

![image-20211108112310643](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/ab984aa6e398f299a0c036894f4bf4b1.png)

> **注意**
>
> StampedLock 不支持条件变量
>
> StampedLock 不支持可重入

## 应用之缓存

### 缓存更新策略

更新时，是先清缓存还是先更新数据库

先清缓存

![image-20211107173107273](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/6cc735b0ab49d91db6916e0c7a156e33.png)

先更新数据库

![image-20211107173133052](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/8628d4e1c7c5c4fc872f12525f3a1346.png)

补充一种情况，假设查询线程 A 查询数据时恰好缓存数据由于时间到期失效，或是第一次查询

![image-20211107173157245](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/dda57099f96eb29057d7201a1d80e394.png)

这种情况的出现几率非常小

由此可见，要保证这里的数据的一致性，就要保证更新数据库和清除缓存的原子性。

### 读写锁实现一致性缓存

使用读写锁实现一个简单的按需加载缓存

```java
public class TestGenericDao {
    public static void main(String[] args) {
        GenericDao dao = new GenericDaoCached();
        System.out.println("============> 查询");
        String sql = "select * from emp where empno = ?";
        int empno = 7369;
        Emp emp = dao.queryOne(Emp.class, sql, empno);
        System.out.println(emp);
        emp = dao.queryOne(Emp.class, sql, empno);
        System.out.println(emp);
        emp = dao.queryOne(Emp.class, sql, empno);
        System.out.println(emp);

        System.out.println("============> 更新");
        dao.update("update emp set sal = ? where empno = ?", 800, empno);
        emp = dao.queryOne(Emp.class, sql, empno);
        System.out.println(emp);
    }
}

class GenericDaoCached extends GenericDao {
    private GenericDao dao = new GenericDao();
    private Map<SqlPair, Object> map = new HashMap<>();
    private ReentrantReadWriteLock rw = new ReentrantReadWriteLock();

    @Override
    public <T> List<T> queryList(Class<T> beanClass, String sql, Object... args) {
        return dao.queryList(beanClass, sql, args);
    }

    @Override
    public <T> T queryOne(Class<T> beanClass, String sql, Object... args) {
        // 先从缓存中找，找到直接返回
        SqlPair key = new SqlPair(sql, args);;
        rw.readLock().lock();
        try {
            T value = (T) map.get(key);
            if(value != null) {
                return value;
            }
        } finally {
            rw.readLock().unlock();
        }
        rw.writeLock().lock();
        try {
            // 多个线程
            T value = (T) map.get(key);
            if(value == null) {
                // 缓存中没有，查询数据库
                value = dao.queryOne(beanClass, sql, args);
                map.put(key, value);
            }
            return value;
        } finally {
            rw.writeLock().unlock();
        }
    }

    @Override
    public int update(String sql, Object... args) {
        rw.writeLock().lock();
        try {
            // 先更新库
            int update = dao.update(sql, args);
            // 清空缓存
            map.clear();
            return update;
        } finally {
            rw.writeLock().unlock();
        }
    }

    class SqlPair {
        private String sql;
        private Object[] args;

        public SqlPair(String sql, Object[] args) {
            this.sql = sql;
            this.args = args;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            SqlPair sqlPair = (SqlPair) o;
            return Objects.equals(sql, sqlPair.sql) &&
                    Arrays.equals(args, sqlPair.args);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(sql);
            result = 31 * result + Arrays.hashCode(args);
            return result;
        }
    }

}
123456789101112131415161718192021222324252627282930313233343536373839404142434445464748495051525354555657585960616263646566676869707172737475767778798081828384858687888990919293949596979899100101102103
```

注意

以上实现体现的是读写锁的应用，保证缓存和数据库的一致性，但有下面的问题没有考虑

- 适合读多写少，如果写操作比较频繁，以上实现性能低
- 没有考虑缓存容量，只存不删
- 没有考虑缓存过期，长期不用的数据没有清理
- 只适合单机，这里是在一个java进程实现的，不适合分布式的
- 并发性还是低，目前只会用一把锁。
  - 如果查询多个表，表1、表2的读写操作不相关，但是由于用的一把锁，表1操作时，表2动不了。
- 更新方法太过简单粗暴，清空了所有 key（考虑按类型分区或重新设计 key）
  - 表1的key清空，表2不应该受影响

乐观锁实现：用 CAS 去更新

## 读写锁原理

### 图解流程

读写锁用的是同一个 Sycn 同步器，因此等待队列、state 等也是同一个

#### t1 w.lock，t2 r.lock

1. t1 成功上锁，流程与 ReentrantLock 加锁相比没有特殊之处，不同是写锁状态占了 state 的低 16 位，而读锁使用的是 state 的高 16 位

![image-20211108084440077](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/84d41a60259daccdd8d84156ed278d5f.png)

源码对应【[写锁上锁流程](#writeLock)】

1. t2 执行 r.lock，这时进入读锁的 sync.acquireShared(1) 流程，首先会进入 tryAcquireShared 流程。如果有写锁占据，那么 tryAcquireShared 返回 -1 表示失败

> tryAcquireShared 返回值表示
>
> - -1 表示失败
> - 0 表示成功，但后继节点不会继续唤醒
> - 正数表示成功，而且数值是还有几个后继节点需要唤醒，读写锁返回 1

![image-20211108101912360](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/44fe6596febf3472af25f63c886419f2.png)

1. 这时会进入 sync.doAcquireShared(1) 流程，首先也是调用 addWaiter 添加节点，不同之处在于节点被设置为Node.SHARED 模式而非 Node.EXCLUSIVE 模式，注意此时 t2 仍处于活跃状态

![image-20211108102002309](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/89cd732adf426432eacb62b5c5a32052.png)

1. t2 会看看自己的节点是不是老二，如果是，还会再次调用 tryAcquireShared(1) 来尝试获取锁
2. 如果没有成功，在 doAcquireShared 内 for (;?? 循环一次，把前驱节点的 waitStatus 改为 -1，再 for (;?? 循环一次尝试 tryAcquireShared(1) 如果还不成功，那么在 parkAndCheckInterrupt() 处 park

![image-20211108102107278](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/d03ad81892c9c3804d8de848b8bffc78.png)

源码对应【[读锁上锁流程](#readLock)】

#### t3 r.lock，t4 w.lock

这种状态下，假设又有 t3 加读锁和 t4 加写锁，这期间 t1 仍然持有锁，就变成了下面的样子

- 读锁 Share 共享模式
- 写锁 Ex 独占模式

![image-20211108102901815](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/7d750f3e7b011cb95cb5ab9b7b2b40d6.png)

#### t1 w.unlock

这时会走到写锁的 sync.release(1) 流程，调用 sync.tryRelease(1) 成功，变成下面的样子

![image-20211108103529801](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/1853b26df9bc83d245edea8827cc053e.png)

接下来执行唤醒流程 sync.unparkSuccessor，即让老二恢复运行，这时 t2 在 doAcquireShared 内parkAndCheckInterrupt() 处恢复运行

这回再来一次 for (;?? 执行 tryAcquireShared 成功则让读锁计数加一（即下图代码位置）

![image-20211108103455042](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/c183dece1dba5613d2a8932a15f88c66.png)

![image-20211108103617527](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/01a0e5cc556dda2a062bb846a7f2dfad.png)

这时 t2 已经从节点取出来恢复运行，接下来 t2 调用 setHeadAndPropagate(node, 1)，它原本所在节点被置为头节点

![image-20211108103640499](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/8187a856062c6722ca8163d46ba95173.png)

事情还没完，在 setHeadAndPropagate 方法内还会检查下一个节点是否是 shared，如果是则调用doReleaseShared() 将 head 的状态从 -1 改为 0 并唤醒老二，这时 t3 在 doAcquireShared 内parkAndCheckInterrupt() 处恢复运行

![image-20211108104520258](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/e521864dcdc843b72917397b3c803105.png)

这回再来一次 for (;?? 执行 tryAcquireShared 成功则让读锁计数加一

![image-20211108104545814](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/701d3e7393c5b2c1e9f1a179f726c373.png)

这时 t3 已经恢复运行，接下来 t3 调用 setHeadAndPropagate(node, 1)，它原本所在节点被置为头节点

![image-20211108104620678](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/0ac02700e8c1728e66a85b35834d8c1a.png)

下一个节点不是 shared 了，因此不会继续唤醒 t4 所在节点

![image-20211108104447243](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/350c54b48faeaf9c06a1879858194c2b.png)

可以看到，当下一个节点是share类型的，就会继续执行释放，直到遇到Ex独占型的。这也是读-读可以并发的原因，遇到读锁shared线程就让他执行，然后只是让state++来计数。

源码对应【[写锁释放流程](#writeRelease)】、【[读锁上锁流程](#readLock)】

#### t2 r.unlock，t3 r.unlock

t2 进入 sync.releaseShared(1) 中，调用 tryReleaseShared(1) 让计数减一，但由于计数还不为零

![image-20211108105828730](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/5d44975449873ae8c568ecf72eadaaf6.png)

t3 进入 sync.releaseShared(1) 中，调用 tryReleaseShared(1) 让计数减一，这回计数为零了，进入doReleaseShared() 将头节点从 -1 改为 0 并唤醒老二，即

![image-20211108105859085](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/d089b674534bb158320e11d4484b83ab.png)

之后 t4 在 acquireQueued 中 parkAndCheckInterrupt 处恢复运行，再次 for (;?? 这次自己是老二，并且没有其他竞争，tryAcquire(1) 成功，修改头结点，流程结束

![image-20211108105927312](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/dbb62659528f126846633e477a9b43a2.png)

源码对应【[读锁释放流程](#readRelease)】

### 源码分析

#### 写锁上锁流程

```java
static final class NonfairSync extends Sync {
    // ... 省略无关代码
// 外部类 WriteLock 方法, 方便阅读, 放在此处
    public void lock() {
        sync.acquire(1);
    }

    // AQS 继承过来的方法, 方便阅读, 放在此处
    public final void acquire(int arg) {
        if (
// 尝试获得写锁失败
                !tryAcquire(arg) &&
// 将当前线程关联到一个 Node 对象上, 模式为独占模式
// 进入 AQS 队列阻塞
                        acquireQueued(addWaiter(Node.EXCLUSIVE), arg)
        ) {
            selfInterrupt();
        }
    }

    // Sync 继承过来的方法, 方便阅读, 放在此处
    protected final boolean tryAcquire(int acquires) {
// 获得低 16 位, 代表写锁的 state 计数
        Thread current = Thread.currentThread();
        int c = getState();
        int w = exclusiveCount(c);
        if (c != 0) {
            if (
// c != 0 and w == 0 表示有读锁, 或者
                    w == 0 ||
// 如果 exclusiveOwnerThread 不是自己
                            current != getExclusiveOwnerThread()
            ) {
// 获得锁失败
                return false;
            }
// 写锁计数超过低 16 位, 报异常
            if (w + exclusiveCount(acquires) > MAX_COUNT)
                throw new Error("Maximum lock count exceeded");
// 写锁重入, 获得锁成功
          // 此时state高位（读锁）为0，直接加1即整体加1
            setState(c + acquires);
            return true;
        }
        if (
// 判断写锁是否该阻塞, 或者
                writerShouldBlock() ||
// 尝试更改计数失败
                        !compareAndSetState(c, c + acquires)
        ) {
// 获得锁失败
            return false;
        }
// 获得锁成功
        setExclusiveOwnerThread(current);
        return true;
    }

    // 非公平锁 writerShouldBlock 总是返回 false, 无需阻塞
    final boolean writerShouldBlock() {
        return false;
    }
}
123456789101112131415161718192021222324252627282930313233343536373839404142434445464748495051525354555657585960616263
```

#### 写锁释放流程

```java
static final class NonfairSync extends Sync {
    // ... 省略无关代码
// WriteLock 方法, 方便阅读, 放在此处
    public void unlock() {
        sync.release(1);
    }
    // AQS 继承过来的方法, 方便阅读, 放在此处
    public final boolean release(int arg) {
// 尝试释放写锁成功
        if (tryRelease(arg)) {
// unpark AQS 中等待的线程
            Node h = head;
            if (h != null && h.waitStatus != 0)
                unparkSuccessor(h);
            return true;
        }
        return false;
    }
    // Sync 继承过来的方法, 方便阅读, 放在此处
    protected final boolean tryRelease(int releases) {
        if (!isHeldExclusively())
            throw new IllegalMonitorStateException();
        int nextc = getState() - releases;
// 因为可重入的原因, 写锁计数为 0, 才算释放成功
        boolean free = exclusiveCount(nextc) == 0;
        if (free) {
            setExclusiveOwnerThread(null);
        }
        setState(nextc);
        return free;
    }
}
1234567891011121314151617181920212223242526272829303132
```

#### 读锁上锁流程

```java
static final class NonfairSync extends Sync {
    // ReadLock 方法, 方便阅读, 放在此处
    public void lock() {
        sync.acquireShared(1);
    }
    // AQS 继承过来的方法, 方便阅读, 放在此处
    public final void acquireShared(int arg) {
// tryAcquireShared 返回负数, 表示获取读锁失败
        if (tryAcquireShared(arg) < 0) {
            doAcquireShared(arg);
        }
    }
    // Sync 继承过来的方法, 方便阅读, 放在此处
    protected final int tryAcquireShared(int unused) {
        Thread current = Thread.currentThread();
        int c = getState();
// 如果是其它线程持有写锁, 获取读锁失败
        if (
                exclusiveCount(c) != 0 &&
                        getExclusiveOwnerThread() != current
        ) {
            return -1;
        }
        int r = sharedCount(c);
        if (
// 读锁不该阻塞(如果老二是写锁，读锁该阻塞), 并且
                !readerShouldBlock() &&
// 小于读锁计数, 并且
                        r < MAX_COUNT &&
// 尝试增加计数成功
          // 只让state的高位（读锁）加1
                        compareAndSetState(c, c + SHARED_UNIT)
        ) {
// ... 省略不重要的代码
            return 1;
        }
        return fullTryAcquireShared(current);
    }
    // 非公平锁 readerShouldBlock 看 AQS 队列中第一个节点是否是写锁
// true 则该阻塞, false 则不阻塞
    final boolean readerShouldBlock() {
        return apparentlyFirstQueuedIsExclusive();
    }
    // AQS 继承过来的方法, 方便阅读, 放在此处
// 与 tryAcquireShared 功能类似, 但会不断尝试 for (;;) 获取读锁, 执行过程中无阻塞
    final int fullTryAcquireShared(Thread current) {
        HoldCounter rh = null;
        for (;;) {
            int c = getState();
            if (exclusiveCount(c) != 0) {
                if (getExclusiveOwnerThread() != current)
                    return -1;
            } else if (readerShouldBlock()) {
// ... 省略不重要的代码
            }
            if (sharedCount(c) == MAX_COUNT)
                throw new Error("Maximum lock count exceeded");
            if (compareAndSetState(c, c + SHARED_UNIT)) {
// ... 省略不重要的代码
                return 1;
            }
        }
    }
    // AQS 继承过来的方法, 方便阅读, 放在此处
    private void doAcquireShared(int arg) {
// 将当前线程关联到一个 Node 对象上, 模式为共享模式
        final Node node = addWaiter(Node.SHARED);
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
                final Node p = node.predecessor();
                if (p == head) {
                    // 再一次尝试获取读锁
                    int r = tryAcquireShared(arg);
// 成功
                    if (r >= 0) {
// ㈠
// r 表示可用资源数, 在这里总是 1 允许传播
//（唤醒 AQS 中下一个 Share 节点）
                        setHeadAndPropagate(node, r);
                        p.next = null; // help GC
                        if (interrupted)
                            selfInterrupt();
                        failed = false;
                        return;
                    }
                }
                if (
// 是否在获取读锁失败时阻塞（前一个阶段 waitStatus == Node.SIGNAL）
                        shouldParkAfterFailedAcquire(p, node) &&
// park 当前线程
                                parkAndCheckInterrupt()
                ) {
                    interrupted = true;
                }
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }
    // ㈠ AQS 继承过来的方法, 方便阅读, 放在此处
    private void setHeadAndPropagate(Node node, int propagate) {
        Node h = head; // Record old head for check below
// 设置自己为 head
        setHead(node);
// propagate 表示有共享资源（例如共享读锁或信号量）
// 原 head waitStatus == Node.SIGNAL 或 Node.PROPAGATE
// 现在 head waitStatus == Node.SIGNAL 或 Node.PROPAGATE
        if (propagate > 0 || h == null || h.waitStatus < 0 ||
                (h = head) == null || h.waitStatus < 0) {
            Node s = node.next;
// 如果是最后一个节点或者是等待共享读锁的节点
            if (s == null || s.isShared()) {
// 进入 ㈡
                doReleaseShared();
            }
        }
    }
    // ㈡ AQS 继承过来的方法, 方便阅读, 放在此处
    private void doReleaseShared() {
// 如果 head.waitStatus == Node.SIGNAL ==> 0 成功, 下一个节点 unpark
// 如果 head.waitStatus == 0 ==> Node.PROPAGATE, 为了解决 bug, 见后面分析
        for (;;) {
            Node h = head;
// 队列还有节点
            if (h != null && h != tail) {
                int ws = h.waitStatus;
                if (ws == Node.SIGNAL) {
                    if (!compareAndSetWaitStatus(h, Node.SIGNAL, 0))
                        continue; // loop to recheck cases
// 下一个节点 unpark 如果成功获取读锁
// 并且下下个节点还是 shared, 继续 doReleaseShared
                    unparkSuccessor(h);
                }
                else if (ws == 0 &&
                        !compareAndSetWaitStatus(h, 0, Node.PROPAGATE))
                    continue; // loop on failed CAS
            }
            if (h == head) // loop if head changed
                break;
        }
    }
}
123456789101112131415161718192021222324252627282930313233343536373839404142434445464748495051525354555657585960616263646566676869707172737475767778798081828384858687888990919293949596979899100101102103104105106107108109110111112113114115116117118119120121122123124125126127128129130131132133134135136137138139140141142143144145
```

#### 读锁释放流程

```java
static final class NonfairSync extends Sync {
    // ReadLock 方法, 方便阅读, 放在此处
    public void unlock() {
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
    protected final boolean tryReleaseShared(int unused) {
// ... 省略不重要的代码
        for (;;) {
            int c = getState();
            int nextc = c - SHARED_UNIT;
            if (compareAndSetState(c, nextc)) {
// 读锁的计数不会影响其它获取读锁线程, 但会影响其它获取写锁线程
// 计数为 0 才是真正释放
                return nextc == 0;
            }
        }
    }
    // AQS 继承过来的方法, 方便阅读, 放在此处
    private void doReleaseShared() {
// 如果 head.waitStatus == Node.SIGNAL ==> 0 成功, 下一个节点 unpark
// 如果 head.waitStatus == 0 ==> Node.PROPAGATE 
        for (;;) {
            Node h = head;
            if (h != null && h != tail) {
                int ws = h.waitStatus;
// 如果有其它线程也在释放读锁，那么需要将 waitStatus 先改为 0
// 防止 unparkSuccessor 被多次执行
                if (ws == Node.SIGNAL) {
                    if (!compareAndSetWaitStatus(h, Node.SIGNAL, 0))
                        continue; // loop to recheck cases
                    unparkSuccessor(h);
                }
// 如果已经是 0 了，改为 -3，用来解决传播性，见后文信号量 bug 分析
                else if (ws == 0 &&
                        !compareAndSetWaitStatus(h, 0, Node.PROPAGATE))
                    continue; // loop on failed CAS
            }
            if (h == head) // loop if head changed
                break;
        }
    }
}
123456789101112131415161718192021222324252627282930313233343536373839404142434445464748495051
```