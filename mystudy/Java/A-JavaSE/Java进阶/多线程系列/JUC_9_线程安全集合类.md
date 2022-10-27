### [线程安全](https://so.csdn.net/so/search?q=线程安全&spm=1001.2101.3001.7020)集合类

- [线程安全集合类概述](#_2)
- [ConcurrentHashMap](#ConcurrentHashMap_41)
- - [练习：单词计数](#_43)
  - [ConcurrentHashMap 原理](#ConcurrentHashMap__187)
  - - [JDK 7 HashMap 并发死链](#JDK_7_HashMap__199)
    - - [测试代码](#_201)
      - [死链复现](#_270)
      - [源码分析](#_377)
      - [小结](#_436)
  - [JDK 8 ConcurrentHashMap](#JDK_8_ConcurrentHashMap_443)
  - - [重要属性和内部类](#_445)
    - [重要方法](#_492)
    - [构造器分析](#_507)
    - [get 流程](#get__531)
    - [put 流程](#put__565)
    - [size 计算流程](#size__742)
  - [JDK 7 ConcurrentHashMap](#JDK_7_ConcurrentHashMap_785)
  - - [构造器分析](#_794)
    - [put 流程](#put__849)
    - [rehash 流程](#rehash__932)
    - [get 流程](#get__988)
    - [size 计算流程](#size__1016)
- [LinkedBlockingQueue 原理](#LinkedBlockingQueue__1063)
- - [基本的入队出队](#_1065)
  - [加锁分析](#_1132)
  - [put 操作](#put__1159)
  - [take 操作](#take__1194)
  - [性能比较](#_1227)
- [ConcurrentLinkedQueue](#ConcurrentLinkedQueue_1239)
- [CopyOnWriteArrayList](#CopyOnWriteArrayList_1256)
- - [get 弱一致性](#get__1295)
  - [迭代器弱一致性](#_1305)



# 线程安全集合类概述

![image-20211109091829503](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/04d639ad676492d8fbefaf24b8a87b0c.png)

线程安全集合类可以分为三大类：

- 遗留的线程安全集合如 Hashtable ， Vector。线程安全的实现无非直接加synchronized
- 使用 Collections 装饰的线程安全集合，如：
  - 使用装饰器模式，把所装饰的类的所有方法，套了一个synchronized
  - Collections.synchronizedCollection
  - Collections.synchronizedList
  - Collections.synchronizedMap
  - Collections.synchronizedSet
  - Collections.synchronizedNavigableMap
  - Collections.synchronizedNavigableSet
  - Collections.synchronizedSortedMap
  - Collections.synchronizedSortedSet
- java.util.concurrent.*

重点介绍 java.util.concurrent.* 下的线程安全集合类，可以发现它们有规律，里面包含三类关键词：Blocking、CopyOnWrite、Concurrent

- Blocking 大部分实现基于锁，并提供用来阻塞的方法
- CopyOnWrite 之类容器修改开销相对较重
- Concurrent 类型的容器
  - 内部很多操作使用 cas 优化，一般可以提供较高吞吐量
  - 弱一致性
    - 遍历时弱一致性，例如，当利用迭代器遍历时，如果容器发生修改，迭代器仍然可以继续进行遍历，这时内容是旧的
    - 求大小弱一致性，size 操作未必是 100% 准确
    - 读取弱一致性

> 遍历时如果发生了修改，对于非安全容器来讲，使用 **fail-fast** 机制也就是让遍历立刻失败，抛出ConcurrentModifificationException，不再继续遍历

# [ConcurrentHashMap](https://so.csdn.net/so/search?q=ConcurrentHashMap&spm=1001.2101.3001.7020)

## 练习：单词计数

生成测试数据

```java
static final String ALPHA = "abcedfghijklmnopqrstuvwxyz";
public static void main(String[] args) {
    int length = ALPHA.length();
    int count = 200;
    List<String> list = new ArrayList<>(length * count);
    for (int i = 0; i < length; i++) {
        char ch = ALPHA.charAt(i);
        for (int j = 0; j < count; j++) {
            list.add(String.valueOf(ch));
        }
    }
    Collections.shuffle(list);
    for (int i = 0; i < 26; i++) {
        try (PrintWriter out = new PrintWriter(
                new OutputStreamWriter(
                        new FileOutputStream("tmp/" + (i+1) + ".txt")))) {
            String collect = list.subList(i * count, (i + 1) * count).stream()
                    .collect(Collectors.joining("\n"));
            out.print(collect);
        } catch (IOException e) {
        }
    }
}
1234567891011121314151617181920212223
```

模版代码，模版代码中封装了多线程读取文件的代码

```java
private static <V> void demo(Supplier<Map<String, V>> supplier, BiConsumer<Map<String, V>, List<String>> consumer) {
    Map<String, V> counterMap = supplier.get();
    // key value
    // a   200
    // b   200
    List<Thread> ts = new ArrayList<>();
    for (int i = 1; i <= 26; i++) {
        int idx = i;
        Thread thread = new Thread(() -> {
            List<String> words = readFromFile(idx);
            consumer.accept(counterMap, words);
        });
        ts.add(thread);
    }

    ts.forEach(t -> t.start());
    ts.forEach(t -> {
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    });

    System.out.println(counterMap);
}

public static List<String> readFromFile(int i) {
    ArrayList<String> words = new ArrayList<>();
    try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("tmp/" + i + ".txt")))) {
        while (true) {
            String word = in.readLine();
            if (word == null) {
                break;
            }
            words.add(word);
        }
        return words;
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
123456789101112131415161718192021222324252627282930313233343536373839404142
```

你要做的是实现两个参数

- 一是提供一个 map 集合，用来存放每个单词的计数结果，key 为单词，value 为计数
- 二是提供一组操作，保证计数的安全性，会传递 map 集合以及 单词 List

正确结果输出应该是每个单词出现 200 次

![image-20211109093815405](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/22322dd83bce9cba47d2b3655e340c30.png)

下面的实现为：

```java
public static void main(String[] args) {
        demo(
                ()->new HashMap<String, Integer>(),
                (map, words) -> {
                    for (String word : words) {
                        // 检查 key 有没有
                        Integer counter = map.get(word);
                        int newValue = counter == null ? 1 : counter + 1;
                        // 没有 则 put
                        map.put(word, newValue);
                    }
                }
        );
    }
1234567891011121314
```

输出

![image-20211109093931229](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/34a9d58d608f8542ec7e101afec1e8cd.png)

参考解答1

```java
demo( 
() -> new ConcurrentHashMap<String, LongAdder>(),
  (map, words) -> {
    for (String word : words) {
      // 注意不能使用 putIfAbsent，此方法返回的是上一次的 value，首次调用返回 null
      // 即使这里设置key-value和value自增不是原子性操作，LongAdder的CAS会获取到最新的值再进行加一的，这一点可以放心
      map.computeIfAbsent(word, (key) -> new LongAdder()).increment();
    }
 }
);
12345678910
```

参考解答2

```java
demo(
                () -> new ConcurrentHashMap<String, Integer>(),
                (map, words) -> {
                    for (String word : words) {
// 函数式编程，无需原子变量
                        map.merge(word, 1, Integer::sum);
                    }
                }
        );
123456789
```

## ConcurrentHashMap 原理

HashMap排放元素时，计算hash值

![image-20211109095818456](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/04d639ad676492d8fbefaf24b8a87b0c.png)

扩容后，重新计算桶下标

![image-20211109095849225](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/76d7ae684321c593815e9be4fe45341d.png)

### JDK 7 HashMap [并发](https://so.csdn.net/so/search?q=并发&spm=1001.2101.3001.7020)死链

#### 测试代码

注意要在 JDK 7 下运行，否则扩容机制和 hash 的计算方法都变了

```java
public class TestDeadLink {
    public static void main(String[] args) {
        // 测试 java 7 中哪些数字的 hash 结果相等
        System.out.println("长度为16时，桶下标为1的key");
        for (int i = 0; i < 64; i++) {
            if (hash(i) % 16 == 1) {
                System.out.println(i);
            }
        }
        System.out.println("长度为32时，桶下标为1的key");
        for (int i = 0; i < 64; i++) {
            if (hash(i) % 32 == 1) {
                System.out.println(i);
            }
        }
        // 1, 35, 16, 50 当大小为16时，它们在一个桶内
        final HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        // 放 12 个元素
        map.put(2, null);
        map.put(3, null);
        map.put(4, null);
        map.put(5, null);
        map.put(6, null);
        map.put(7, null);
        map.put(8, null);
        map.put(9, null);
        map.put(10, null);
        map.put(16, null);
        map.put(35, null);
        map.put(1, null);

        System.out.println("扩容前大小[main]:"+map.size());
        new Thread() {
            @Override
            public void run() {
                // 放第 13 个元素, 发生扩容
                map.put(50, null);
                System.out.println("扩容后大小[Thread-0]:"+map.size());
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                // 放第 13 个元素, 发生扩容
                map.put(50, null);
                System.out.println("扩容后大小[Thread-1]:"+map.size());
            }
        }.start();
    }

    final static int hash(Object k) {
        int h = 0;
        if (0 != h && k instanceof String) {
            return sun.misc.Hashing.stringHash32((String) k);
        }
        h ^= k.hashCode();
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }
}
123456789101112131415161718192021222324252627282930313233343536373839404142434445464748495051525354555657585960
```

#### 死链复现

调试工具使用 idea

在 HashMap 源码 590 行加断点

```java
int newCapacity = newTable.length;
1
```

![image-20211109101859084](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/a7eb07627876b853952bd99865966b4f.png)

断点的条件如下，目的是让 HashMap 在扩容为 32 时，并且线程为 Thread-0 或 Thread-1 时停下来(因为程序运行起来，其他线程也可能会扩容，这里我们只关注我们的测试代码)

```java
newTable.length==32 &&
 (
  Thread.currentThread().getName().equals("Thread-0")||
  Thread.currentThread().getName().equals("Thread-1")
 )
12345
```

断点暂停方式选择 Thread，否则在调试 Thread-0 时，Thread-1 无法恢复运行

运行代码，程序在预料的断点位置停了下来，输出

```java
长度为16时，桶下标为1的key 
1 
16 
35 
50 
长度为32时，桶下标为1的key 
1 
35 
扩容前大小[main]:12
123456789
```

接下来进入扩容流程调试

在 HashMap 源码 594 行加断点

```java
Entry<K,V> next = e.next; // 593
if (rehash) // 594
// ...
123
```

这是为了观察 e 节点和 next 节点的状态，Thread-0 单步执行到 594 行，再 594 处再添加一个断点（条件Thread.currentThread().getName().equals(“Thread-0”)），Thread-0停住，让Thread-1先执行进行扩容。

这时可以在 Variables 面板观察到 e 和 next 变量，使用 view as -> Object 查看节点状态

```java
e (1)->(35)->(16)->null 
next (35)->(16)->null
12
```

在 Threads 面板选中 Thread-1 恢复运行，可以看到控制台输出新的内容如下，Thread-1 扩容已完成

```java
newTable[1] (35)->(1)->null
扩容后大小:13 
12
```

这时 Thread-0 还停在 594 处， Variables 面板变量的状态已经变化为

```java
e (1)->null 
next (35)->(1)->null
12
```

为什么呢，因为 Thread-1 扩容时链表也是后加入的元素放入链表头，因此链表就倒过来了，但 Thread-1 虽然结果正确，但它结束后 Thread-0 还要继续运行

接下来就可以单步调试（F8）观察死链的产生了

下一轮循环到 594，将 e 搬迁到 newTable 链表头

```java
newTable[1] (1)->null 
e (35)->(1)->null 
next (1)->null
123
```

下一轮循环到 594，将 e 搬迁到 newTable 链表头

```java
newTable[1] (35)->(1)->null 
e (1)->null 
next null
123
```

再看看源码

```java
e.next = newTable[1];
// 这时 e (1,35)
// 而 newTable[1] (35,1)->(1,35) 因为是同一个对象

newTable[1] = e; 
// 再尝试将 e 作为链表头, 死链已成

e = next;
// 虽然 next 是 null, 会进入下一个链表的复制, 但死链已经形成了
123456789
```

#### 源码分析

HashMap 的并发死链发生在扩容时

```java
// 将 table 迁移至 newTable
void transfer(Entry[] newTable, boolean rehash) {
        int newCapacity = newTable.length;
        for (Entry<K,V> e : table) {
            while(null != e) {
                Entry<K,V> next = e.next;
// 1 处
                if (rehash) {
                    e.hash = null == e.key ? 0 : hash(e.key);
                }
                int i = indexFor(e.hash, newCapacity);
// 2 处
// 将新元素加入 newTable[i], 原 newTable[i] 作为新元素的 next
                e.next = newTable[i];
                newTable[i] = e; 
              	e = next;
            }
        }
    }
12345678910111213141516171819
```

假设 map 中初始元素是

```java
原始链表，格式：[下标] (key,next) 
[1] (1,35)->(35,16)->(16,null)

线程 a 执行到 1 处 ，此时局部变量 e 为 (1,35)，而局部变量 next 为 (35,16) 线程 a 挂起

线程 b 开始执行
第一次循环
[1] (1,null)

第二次循环
[1] (35,1)->(1,null)

第三次循环
[1] (35,1)->(1,null) [17] (16,null)

切换回线程 a，此时局部变量 e 和 next 被恢复，引用没变但内容变了：e 的内容被改为 (1,null)，而 next 的内容被改为 (35,1) 并链向 (1,null)
第一次循环
[1] (1,null)

第二次循环，注意这时 e 是 (35,1) 并链向 (1,null) 所以 next 又是 (1,null) 
[1] (35,1)->(1,null)

第三次循环，e 是 (1,null)，而 next 是 null，但 e 被放入链表头，这样 e.next 变成了 35 （2 处）
[1] (1,35)->(35,1)->(1,35)

已经是死链了
1234567891011121314151617181920212223242526
```

#### 小结

- 究其原因，是因为在多线程环境下使用了非线程安全的 map 集合
- JDK 8 虽然将扩容算法做了调整，不再将元素加入链表头（而是保持与扩容前一样的顺序），但仍不意味着能够在多线程环境下能够安全扩容，还会出现其它问题（如扩容丢数据）

## JDK 8 ConcurrentHashMap

### 重要属性和内部类

```java
// 默认为 0
// 当初始化时, 为 -1
// 当扩容时, 为 -(1 + 扩容线程数)
// 当初始化或扩容完成后，为 下一次的扩容的阈值大小
private transient volatile int sizeCtl;

// 整个 ConcurrentHashMap 就是一个 Node[]
static class Node<K,V> implements Map.Entry<K,V> {}

// hash 表
transient volatile Node<K,V>[] table;

// 扩容时的 新 hash 表
private transient volatile Node<K,V>[] nextTable;

// 扩容时如果某个 bin 迁移完毕, 用 ForwardingNode 作为旧 table bin 的头结点
static final class ForwardingNode<K,V> extends Node<K,V> {}

// 用在 compute 以及 computeIfAbsent 时, 用来占位, 计算完成后替换为普通 Node
static final class ReservationNode<K,V> extends Node<K,V> {}

// 作为 treebin 的头节点, 存储 root 和 first
static final class TreeBin<K,V> extends Node<K,V> {}

// 作为 treebin 的节点, 存储 parent, left, right
static final class TreeNode<K,V> extends Node<K,V> {}
1234567891011121314151617181920212223242526
```

![image-20211109110455315](https://img-blog.csdnimg.cn/img_convert/049857377b9aa138d5a702f6968e57e1.png)

对于ForwardingNode

- 带有ForwardingNode的为处理过的，当其他线程到这里时，就不会再对其操作
- 其他线程要获取这里的值时，也是通过ForwardingNode找到扩容后新表中的节点

对于TreeBin和TreeNode，即红黑树的实现。

- 当多个节点的桶位置一样时，这些节点会串成一个链表。在jdk8中，新加节点，加在链表尾部。但是如果链表过长，会进行扩容。
- 如果扩容效果不好，那么将会把链表进行一个向红黑树的一个转换。

### 重要方法

```java
// 获取 Node[] 中第 i 个 Node
static final <K,V> Node<K,V> tabAt(Node<K,V>[] tab, int i)

// cas 修改 Node[] 中第 i 个 Node 的值, c 为旧值, v 为新值
static final <K,V> boolean casTabAt(Node<K,V>[] tab, int i, Node<K,V> c, Node<K,V> v)

// 直接修改 Node[] 中第 i 个 Node 的值, v 为新值
static final <K,V> void setTabAt(Node<K,V>[] tab, int i, Node<K,V> v)
12345678
```

### 构造器分析

可以看到实现了懒惰初始化，在构造方法中仅仅计算了 table 的大小，以后在第一次使用时才会真正创建

```java
public ConcurrentHashMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
        if (!(loadFactor > 0.0f) || initialCapacity < 0 || concurrencyLevel <= 0)
            throw new IllegalArgumentException();
        if (initialCapacity < concurrencyLevel) // Use at least as many bins
            initialCapacity = concurrencyLevel; // as estimated threads（保证并发度）
        long size = (long) (1.0 + (long) initialCapacity / loadFactor);
// tableSizeFor 仍然是保证计算的大小是 2^n, 即 16,32,64 ...（哈希算法的要求） 
        int cap = (size >= (long) MAXIMUM_CAPACITY) ?
                MAXIMUM_CAPACITY : tableSizeFor((int) size);
        this.sizeCtl = cap;
    }
1234567891011
```

- initialCapacity 初始容量
- loadFactor 负载因子，即扩容阈值，默认容量的3/4
- concurrencyLevel 并发度

### get 流程

```java
public V get(Object key) {
        Node<K,V>[] tab; Node<K,V> e, p; int n, eh; K ek;
// spread 方法能确保返回结果是正数
        int h = spread(key.hashCode());
        if ((tab = table) != null && (n = tab.length) > 0 &&
                (e = tabAt(tab, (n - 1) & h)) != null) {
// 如果头结点已经是要查找的 key
            if ((eh = e.hash) == h) {
                if ((ek = e.key) == key || (ek != null && key.equals(ek)))
                    return e.val;
            }
// hash 为负数表示该 bin 在扩容中（-1）或是 treebin（-2）, 这时调用 find 方法来查找
          // 在扩容中，根据ForwardingNode到新表去找
          // 如果是红黑树，就用红黑树的查找算法找
            else if (eh < 0)
                return (p = e.find(h, key)) != null ? p.val : null;
// 正常遍历链表, 用 equals 比较
            while ((e = e.next) != null) {
                if (e.hash == h &&
                        ((ek = e.key) == key || (ek != null && key.equals(ek))))
                    return e.val;
            }
        }
        return null; 
    }
12345678910111213141516171819202122232425
```

没有使用锁，并发度高

### put 流程

以下数组简称（table），链表简称（bin）

```java
public V put(K key, V value) {
        return putVal(key, value, false);
    }

    final V putVal(K key, V value, boolean onlyIfAbsent) {
        if (key == null || value == null) throw new NullPointerException();
// 其中 spread 方法会综合高位低位, 具有更好的 hash 性
        int hash = spread(key.hashCode());
        int binCount = 0;
        for (Node<K, V>[] tab = table; ; ) {
// f 是链表头节点
// fh 是链表头结点的 hash
// i 是链表在 table 中的下标
            Node<K, V> f;
            int n, i, fh;
// 要创建 table
            if (tab == null || (n = tab.length) == 0)
// 初始化 table 使用了 cas, 无需 synchronized 创建成功, 进入下一轮循环
                tab = initTable();
// 要创建链表头节点
            else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
// 添加链表头使用了 cas, 无需 synchronized
                if (casTabAt(tab, i, null,
                        new Node<K, V>(hash, key, value, null)))
                    break;
            }
// 帮忙扩容
            else if ((fh = f.hash) == MOVED)
// 帮忙之后, 进入下一轮循环
                tab = helpTransfer(tab, f);
            else {
                V oldVal = null;
// 锁住链表头节点
                synchronized (f) {
// 再次确认链表头节点没有被移动
                    if (tabAt(tab, i) == f) {
// 链表
                        if (fh >= 0) {
                            binCount = 1;
// 遍历链表
                            for (Node<K, V> e = f; ; ++binCount) {
                                K ek;
// 找到相同的 key
                                if (e.hash == hash &&
                                        ((ek = e.key) == key ||
                                                (ek != null && key.equals(ek)))) {
                                    oldVal = e.val;
// 更新
                                    if (!onlyIfAbsent) e.val = value;
                                    break;
                                }
                                Node<K, V> pred = e;
// 已经是最后的节点了, 新增 Node, 追加至链表尾
                                if ((e = e.next) == null) {
                                    pred.next = new Node<K, V>(hash, key,
                                            value, null);
                                    break;
                                }
                            }
                        }
// 红黑树
                        else if (f instanceof TreeBin) {
                            Node<K, V> p;
                            binCount = 2;
// putTreeVal 会看 key 是否已经在树中, 是, 则返回对应的 TreeNode
                            if ((p = ((TreeBin<K, V>) f).putTreeVal(hash, key,
                                    value)) != null) {
                                oldVal = p.val;
                                if (!onlyIfAbsent) p.val = value;
                            }
                        }
                    }
// 释放链表头节点的锁
                }
                if (binCount != 0) {
                    if (binCount >= TREEIFY_THRESHOLD)
// 如果链表长度 >= 树化阈值(8), 进行链表转为红黑树
                        treeifyBin(tab, i);
                    if (oldVal != null)
                        return oldVal;
                    break;
                }
            }
        }
// 增加 size 计数
        addCount(1L, binCount);
        return null;
    }

    private final Node<K, V>[] initTable() {
        Node<K, V>[] tab;
        int sc;
        while ((tab = table) == null || tab.length == 0) {
            if ((sc = sizeCtl) < 0)
                Thread.yield();
// 尝试将 sizeCtl 设置为 -1（表示初始化 table）
            else if (U.compareAndSwapInt(this, SIZECTL, sc, -1)) {
// 获得锁, 创建 table, 这时其它线程会在 while() 循环中 yield 直至 table 创建
                try {
                    if ((tab = table) == null || tab.length == 0) {
                        int n = (sc > 0) ? sc : DEFAULT_CAPACITY;
                        Node<K, V>[] nt = (Node<K, V>[]) new Node<?, ?>[n];
                        table = tab = nt;
                        sc = n - (n >>> 2);
                    }
                } finally {
                    sizeCtl = sc;
                }
                break;
            }
        }
        return tab;
    }

    // check 是之前 binCount 的个数
    private final void addCount(long x, int check) {
        CounterCell[] as;
        long b, s;
        if (
// 已经有了 counterCells, 向 cell 累加
                (as = counterCells) != null ||
// 还没有, 向 baseCount 累加
                        !U.compareAndSwapLong(this, BASECOUNT, b = baseCount, s = b + x)
        ) {
            CounterCell a;
            long v;
            int m;
            boolean uncontended = true;
            if (
// 还没有 counterCells
                    as == null || (m = as.length - 1) < 0 ||
// 还没有 cell
                            (a = as[ThreadLocalRandom.getProbe() & m]) == null ||
// cell cas 增加计数失败
                            !(uncontended = U.compareAndSwapLong(a, CELLVALUE, v = a.value, v + x))
            ) {
// 创建累加单元数组和cell, 累加重试
                fullAddCount(x, uncontended);
                return;
            }
            if (check <= 1)
                return;
// 获取元素个数
            s = sumCount();
        }
        if (check >= 0) {
            Node<K, V>[] tab, nt;
            int n, sc;
            while (s >= (long) (sc = sizeCtl) && (tab = table) != null &&
                    (n = tab.length) < MAXIMUM_CAPACITY) {
                int rs = resizeStamp(n);
                if (sc < 0) {
                    if ((sc >>> RESIZE_STAMP_SHIFT) != rs || sc == rs + 1 ||
                            sc == rs + MAX_RESIZERS || (nt = nextTable) == null ||
                            transferIndex <= 0)
                        break;
// newtable 已经创建了，帮忙扩容
                    if (U.compareAndSwapInt(this, SIZECTL, sc, sc + 1))
                        transfer(tab, nt);
                }
// 需要扩容，这时 newtable 未创建
                else if (U.compareAndSwapInt(this, SIZECTL, sc,
                        (rs << RESIZE_STAMP_SHIFT) + 2))
                    transfer(tab, null);
                s = sumCount();
            }
        }
    }
123456789101112131415161718192021222324252627282930313233343536373839404142434445464748495051525354555657585960616263646566676869707172737475767778798081828384858687888990919293949596979899100101102103104105106107108109110111112113114115116117118119120121122123124125126127128129130131132133134135136137138139140141142143144145146147148149150151152153154155156157158159160161162163164165166167168
```

### size 计算流程

size 计算实际发生在 put，remove 改变集合元素的操作之中

- 没有竞争发生，向 baseCount 累加计数
- 有竞争发生，新建 counterCells，向其中的一个 cell 累加计数
  - counterCells 初始有两个 cell
  - 如果计数竞争比较激烈，会创建新的 cell 来累加计数

```java
public int size() {
        long n = sumCount();
        return ((n < 0L) ? 0 :
                (n > (long)Integer.MAX_VALUE) ? Integer.MAX_VALUE :
                        (int)n);
    }
    final long sumCount() {
        CounterCell[] as = counterCells; CounterCell a;
// 将 baseCount 计数与所有 cell 计数累加
        long sum = baseCount;
        if (as != null) {
            for (int i = 0; i < as.length; ++i) {
                if ((a = as[i]) != null)
                    sum += a.value;
            }
        }
        return sum; 
}
123456789101112131415161718
```

**Java 8** 数组（Node） +（ 链表 Node | 红黑树 TreeNode ） 以下数组简称（table），链表简称（bin）

- 初始化，使用 cas 来保证并发安全，懒惰初始化 table
- 树化，当 table.length < 64 时，先尝试扩容，超过 64 时，并且 bin.length > 8 时，会将链表树化，树化过程会用 synchronized 锁住链表头
- put，如果该 bin 尚未创建，只需要使用 cas 创建 bin；如果已经有了，锁住链表头进行后续 put 操作，元素添加至 bin 的尾部
- get，无锁操作仅需要保证可见性，扩容过程中 get 操作拿到的是 ForwardingNode 它会让 get 操作在新table 进行搜索
- 扩容，扩容时以 bin 为单位进行，需要对 bin 进行 synchronized，但这时妙的是其它竞争线程也不是无事可做，它们会帮助把其它 bin 进行扩容，扩容时平均只有 1/6 的节点会把复制到新 table 中
- size，元素个数保存在 baseCount 中，并发时的个数变动保存在 CounterCell[] 当中。最后统计数量时累加即可

## JDK 7 ConcurrentHashMap

它维护了一个 segment 数组，每个 segment 对应一把锁

- 优点：如果多个线程访问不同的 segment，实际是没有冲突的，这与 jdk8 中是类似的
- 缺点：Segments 数组默认大小为16，这个容量初始化指定后就不能改变了，并且不是懒惰初始化

### 构造器分析

```java
public ConcurrentHashMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
        if (!(loadFactor > 0) || initialCapacity < 0 || concurrencyLevel <= 0)
            throw new IllegalArgumentException();
        if (concurrencyLevel > MAX_SEGMENTS)
            concurrencyLevel = MAX_SEGMENTS;
// ssize 必须是 2^n, 即 2, 4, 8, 16 ... 表示了 segments 数组的大小
        int sshift = 0;
        int ssize = 1;
        while (ssize < concurrencyLevel) {
            ++sshift;
            ssize <<= 1;
        }
// segmentShift 默认是 32 - 4 = 28
        this.segmentShift = 32 - sshift;
// segmentMask 默认是 15 即 0000 0000 0000 1111
        this.segmentMask = ssize - 1;
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        int c = initialCapacity / ssize;
        if (c * ssize < initialCapacity)
            ++c;
        int cap = MIN_SEGMENT_TABLE_CAPACITY;
        while (cap < c)
            cap <<= 1;
// 创建 segments and segments[0]
        Segment<K,V> s0 =
                new Segment<K,V>(loadFactor, (int)(cap * loadFactor),
                        (HashEntry<K,V>[])new HashEntry[cap]);
        Segment<K,V>[] ss = (Segment<K,V>[])new Segment[ssize];
        UNSAFE.putOrderedObject(ss, SBASE, s0); // ordered write of segments[0]
        this.segments = ss; 
}
1234567891011121314151617181920212223242526272829303132
```

构造完成，如下图所示

![image-20211109154542271](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20211109154542271.png)

可以看到 ConcurrentHashMap 没有实现懒惰初始化，空间占用不友好

其中 this.segmentShift 和 this.segmentMask 的作用是决定将 key 的 hash 结果匹配到哪个 segment

例如，根据某一 hash 值求 segment 位置，先将高位向低位移动 this.segmentShift 位

![image-20211109154616050](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/a99f2a59456747e5fdea19bf7dde313b.png)

结果再与 this.segmentMask 做位于运算，最终得到 1010 即下标为 10 的 segment

![image-20211109154636281](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/fe4a6f647e1b456ca934e30867597367.png)

### put 流程

```java
public V put(K key, V value) {
        Segment<K,V> s;
        if (value == null)
            throw new NullPointerException();
        int hash = hash(key);
// 计算出 segment 下标
        int j = (hash >>> segmentShift) & segmentMask;
// 获得 segment 对象, 判断是否为 null, 是则创建该 segment
        if ((s = (Segment<K,V>)UNSAFE.getObject
                (segments, (j << SSHIFT) + SBASE)) == null) {
// 这时不能确定是否真的为 null, 因为其它线程也发现该 segment 为 null,
// 因此在 ensureSegment 里用 cas 方式保证该 segment 安全性
            s = ensureSegment(j);
        }
// 进入 segment 的put 流程
        return s.put(key, hash, value, false);
    }
1234567891011121314151617
```

segment 继承了可重入锁（ReentrantLock），它的 put 方法为

```java
final V put(K key, int hash, V value, boolean onlyIfAbsent) {
// 尝试加锁
        HashEntry<K,V> node = tryLock() ? null :
// 如果不成功, 进入 scanAndLockForPut 流程
// 如果是多核 cpu 最多 tryLock 64 次, 进入 lock 流程
// 在尝试期间, 还可以顺便看该节点在链表中有没有, 如果没有顺便创建出来
                scanAndLockForPut(key, hash, value);
// 执行到这里 segment 已经被成功加锁, 可以安全执行
        V oldValue;
        try {
            HashEntry<K,V>[] tab = table;
            int index = (tab.length - 1) & hash;
            HashEntry<K,V> first = entryAt(tab, index);
            for (HashEntry<K,V> e = first;;) {
                if (e != null) {
// 更新
                    K k;
                    if ((k = e.key) == key ||
                            (e.hash == hash && key.equals(k))) {
                        oldValue = e.value;
                        if (!onlyIfAbsent) {
                            e.value = value;
                            ++modCount;
                        }
                        break;
                    }
                    e = e.next;
                }
                else {
// 新增
// 1) 之前等待锁时, node 已经被创建, next 指向链表头
                    if (node != null)
                        node.setNext(first);
                    else
// 2) 创建新 node
                        node = new HashEntry<K,V>(hash, key, value, first);
                    int c = count + 1;
// 3) 扩容
                    if (c > threshold && tab.length < MAXIMUM_CAPACITY)
                        rehash(node);
                    else
// 将 node 作为链表头
                        setEntryAt(tab, index, node);
                    ++modCount;
                    count = c;
                    oldValue = null;
                    break;
                }
            }
        } finally {
            unlock();
        }
        return oldValue; 
    }
123456789101112131415161718192021222324252627282930313233343536373839404142434445464748495051525354
```

### rehash 流程

发生在 put 中，因为此时已经获得了锁，因此 rehash 时不需要考虑线程安全

```java
private void rehash(HashEntry<K,V> node) {
        HashEntry<K,V>[] oldTable = table;
        int oldCapacity = oldTable.length;
        int newCapacity = oldCapacity << 1;
        threshold = (int)(newCapacity * loadFactor);
        HashEntry<K,V>[] newTable =
                (HashEntry<K,V>[]) new HashEntry[newCapacity];
        int sizeMask = newCapacity - 1;
        for (int i = 0; i < oldCapacity ; i++) {
            HashEntry<K,V> e = oldTable[i];
            if (e != null) {
                HashEntry<K,V> next = e.next;
                int idx = e.hash & sizeMask;
                if (next == null) // Single node on list
                    newTable[idx] = e;
                else { // Reuse consecutive sequence at same slot
                    HashEntry<K,V> lastRun = e;
                    int lastIdx = idx;
// 过一遍链表, 尽可能把 rehash 后 idx 不变的节点重用
                    for (HashEntry<K,V> last = next;
                         last != null;
                         last = last.next) {
                        int k = last.hash & sizeMask;
                        if (k != lastIdx) {
                            lastIdx = k;
                            lastRun = last;
                        }
                    }
                    newTable[lastIdx] = lastRun;
// 剩余节点需要新建
                    for (HashEntry<K,V> p = e; p != lastRun; p = p.next) {
                        V v = p.value;
                        int h = p.hash;
                        int k = h & sizeMask;
                        HashEntry<K,V> n = newTable[k];
                        newTable[k] = new HashEntry<K,V>(h, p.key, v, n);
                    }
                }
            }
        }
// 扩容完成, 才加入新的节点
        int nodeIndex = node.hash & sizeMask; // add the new node
        node.setNext(newTable[nodeIndex]);
        newTable[nodeIndex] = node;
// 替换为新的 HashEntry table
        table = newTable; 
    }
1234567891011121314151617181920212223242526272829303132333435363738394041424344454647
```

### get 流程

get 时并未加锁，用了 UNSAFE 方法保证了可见性，扩容过程中，get 先发生就从旧表取内容，get 后发生就从新表取内容

```java
public V get(Object key) {
        Segment<K,V> s; // manually integrate access methods to reduce overhead
        HashEntry<K,V>[] tab;
        int h = hash(key);
// u 为 segment 对象在数组中的偏移量
        long u = (((h >>> segmentShift) & segmentMask) << SSHIFT) + SBASE;
// s 即为 segment
        if ((s = (Segment<K,V>)UNSAFE.getObjectVolatile(segments, u)) != null &&
                (tab = s.table) != null) {
            for (HashEntry<K,V> e = (HashEntry<K,V>) UNSAFE.getObjectVolatile
                    (tab, ((long)(((tab.length - 1) & h)) << TSHIFT) + TBASE);
                 e != null; e = e.next) {
                K k;
                if ((k = e.key) == key || (e.hash == h && key.equals(k)))
                    return e.value;
            }
        }
        return null; 
}
12345678910111213141516171819
```

### size 计算流程

```java
public int size() {
// Try a few times to get accurate count. On failure due to
// continuous async changes in table, resort to locking.
        final Segment<K,V>[] segments = this.segments;
        int size;
        boolean overflow; // true if size overflows 32 bits
        long sum; // sum of modCounts
        long last = 0L; // previous sum
        int retries = -1; // first iteration isn't retry
        try {
            for (;;) {
                if (retries++ == RETRIES_BEFORE_LOCK) {
// 超过重试次数, 需要创建所有 segment 并加锁
                    for (int j = 0; j < segments.length; ++j)
                        ensureSegment(j).lock(); // force creation
                }
                sum = 0L;
                size = 0;
                overflow = false;
                for (int j = 0; j < segments.length; ++j) {
                    Segment<K,V> seg = segmentAt(segments, j);
                    if (seg != null) {
                        sum += seg.modCount;
                        int c = seg.count;
                        if (c < 0 || (size += c) < 0)
                            overflow = true;
                    }
                }
                if (sum == last)
                    break;
                last = sum;
            }
        } finally {
            if (retries > RETRIES_BEFORE_LOCK) {
                for (int j = 0; j < segments.length; ++j)
                    segmentAt(segments, j).unlock();
            }
        }
        return overflow ? Integer.MAX_VALUE : size; 
    }
12345678910111213141516171819202122232425262728293031323334353637383940
```

# LinkedBlockingQueue 原理

## 基本的入队出队

```java
public class LinkedBlockingQueue<E> extends AbstractQueue<E>
        implements BlockingQueue<E>, java.io.Serializable {
    static class Node<E> {
        E item;
        /**
         * 下列三种情况之一
         * - 真正的后继节点
         * - 自己, 发生在出队时
         * - null, 表示是没有后继节点, 是最后了
         */
        Node<E> next;
        Node(E x) { item = x; }
    }
}
1234567891011121314
```

初始化链表 last = head = new Node(null); Dummy 节点用来占位，item 为 null

![image-20211109161711301](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/7a55f8c1797aabb4c4de58539dd56a06.png)

当一个节点入队 last = last.next = node;

![image-20211109161819078](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/e8c2e9e4d00c700f285db8c453a9aaea.png)

再来一个节点入队 last = last.next = node;

![image-20211109161842184](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/1cec7b8d30fc9a27af01b9d108b944b9.png)

出队

```java
Node<E> h = head;
Node<E> first = h.next; h.next = h; // help GC
head = first; E x = first.item;
first.item = null;
return x;
12345
```

h = head

![image-20211109161924641](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/4664e803707c11740f820d416b8a320e.png)

first = h.next

![image-20211109161942945](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/e1809fbafdaca8f4540f799bbc19f57a.png)

h.next = h

![image-20211109162003798](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/4e2fccfbc257c3606aacbe46e9767985.png)

head = first

![image-20211109162023339](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/adb406b3511b6e45d64c929002750739.png)

```java
E x = first.item;
first.item = null;
return x;
123
```

![image-20211109162047710](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/9766b7f775732e7dae09bd5187ca324b.png)

## 加锁分析

高明之处在于用了两把锁和 dummy 节点

- 用一把锁，同一时刻，最多只允许有一个线程（生产者或消费者，二选一）执行
- 用两把锁，同一时刻，可以允许两个线程同时（一个生产者与一个消费者）执行
  - 消费者与消费者线程仍然串行
  - 生产者与生产者线程仍然串行

线程安全分析

- 当节点总数大于 2 时（包括 dummy 节点），putLock 保证的是 last 节点的线程安全，takeLock 保证的是head 节点的线程安全。两把锁保证了入队和出队没有竞争
- 当节点总数等于 2 时（即一个 dummy 节点，一个正常节点）这时候，仍然是两把锁锁两个对象，不会竞争
- 当节点总数等于 1 时（就一个 dummy 节点）这时 take 线程会被 notEmpty 条件阻塞，有竞争，会阻塞

```java
// 用于 put(阻塞) offer(非阻塞)
private final ReentrantLock putLock = new ReentrantLock();

// 用户 take(阻塞) poll(非阻塞)
private final ReentrantLock takeLock = new ReentrantLock();
12345
```

## put 操作

```java
public void put(E e) throws InterruptedException {
        if (e == null) throw new NullPointerException();
        int c = -1;
        Node<E> node = new Node<E>(e);
        final ReentrantLock putLock = this.putLock;
// count 用来维护元素计数
        final AtomicInteger count = this.count;
        putLock.lockInterruptibly();
        try {
// 满了等待
            while (count.get() == capacity) {
// 倒过来读就好: 等待 notFull
                notFull.await();
            }
// 有空位, 入队且计数加一
            enqueue(node);
            c = count.getAndIncrement();
// 除了自己 put 以外, 队列还有空位, 由自己叫醒其他 put 线程
            if (c + 1 < capacity)
                notFull.signal();
        } finally {
            putLock.unlock();
        }
// 如果队列中有一个元素, 叫醒 take 线程
        if (c == 0)
// 这里调用的是 notEmpty.signal() 而不是 notEmpty.signalAll() 是为了减少竞争
            signalNotEmpty();
    }
12345678910111213141516171819202122232425262728
```

## take 操作

```java
public E take() throws InterruptedException {
        E x;
        int c = -1;
        final AtomicInteger count = this.count;
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lockInterruptibly();
        try {
            while (count.get() == 0) {
                notEmpty.await();
            }
            x = dequeue();
            c = count.getAndDecrement();
            if (c > 1)
                notEmpty.signal();
        } finally {
            takeLock.unlock();
        }
// 如果队列中只有一个空位时, 叫醒 put 线程
// 如果有多个线程进行出队, 第一个线程满足 c == capacity, 但后续线程 c < capacity
        if (c == capacity)
// 这里调用的是 notFull.signal() 而不是 notFull.signalAll() 是为了减少竞争
            signalNotFull();
        return x; 
}
123456789101112131415161718192021222324
```

> 由 put 唤醒 put 是为了避免信号不足

## 性能比较

主要列举 LinkedBlockingQueue 与 ArrayBlockingQueue 的性能比较

- Linked 支持有界，Array 强制有界
- Linked 实现是链表，Array 实现是数组
- Linked 是懒惰的，而 Array 需要提前初始化 Node 数组
- Linked 每次入队会生成新 Node，而 Array 的 Node 是提前创建好的
- Linked 两把锁，Array 一把锁

# [ConcurrentLinkedQueue](https://so.csdn.net/so/search?q=ConcurrentLinkedQueue&spm=1001.2101.3001.7020)

ConcurrentLinkedQueue 的设计与 LinkedBlockingQueue 非常像，也是

- 两把【锁】，同一时刻，可以允许两个线程同时（一个生产者与一个消费者）执行
- dummy 节点的引入让两把【锁】将来锁住的是不同对象，避免竞争
- 只是这【锁】使用了 cas 来实现

事实上，ConcurrentLinkedQueue 应用还是非常广泛的

例如之前讲的 Tomcat 的 Connector 结构时，Acceptor 作为生产者向 Poller 消费者传递事件信息时，正是采用了ConcurrentLinkedQueue 将 SocketChannel 给 Poller 使用

![image-20211109164450652](https://img-blog.csdnimg.cn/img_convert/239bc0cedfe11bf60054e09f27df6709.png)

# CopyOnWriteArrayList

CopyOnWriteArraySet 是它的马甲 底层实现采用了 写入时拷贝 的思想，增删改操作会将底层数组拷贝一份，更改操作在新数组上执行，这时不影响其它线程的**并发读**，**读写分离**。 以新增为例：

```java
public boolean add(E e) {
        synchronized (lock) {
// 获取旧的数组
            Object[] es = getArray();
            int len = es.length;
// 拷贝新的数组（这里是比较耗时的操作，但不影响其它读线程）
            es = Arrays.copyOf(es, len + 1);
// 添加新元素
            es[len] = e;
// 替换旧的数组
            setArray(es);
            return true;
        }
    }
1234567891011121314
```

> 这里的源码版本是 Java 11，在 Java 1.8 中使用的是可重入锁而不是 synchronized

其它读操作并未加锁，例如：

```java
public void forEach(Consumer<? super E> action) {
    Objects.requireNonNull(action);
    for (Object x : getArray()) {
        @SuppressWarnings("unchecked") E e = (E) x;
        action.accept(e);
    }
}
1234567
```

适合『读多写少』的应用场景

## get 弱一致性

![image-20211109164636391](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/29f8569684d809f3f3497f4f4c1b85db.png)

![image-20211109164649845](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/9744f6dfc5f4b909219c85c17544c008.png)

> 不容易测试，但问题确实存在

## 迭代器弱一致性

```java
CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>();
list.add(1);
list.add(2);
list.add(3);
Iterator<Integer> iter = list.iterator();
new Thread(() -> {
    list.remove(0);
    System.out.println(list);
}).start();
sleep1s();
while (iter.hasNext()) {
    System.out.println(iter.next());
}
12345678910111213
```

不要觉得弱一致性就不好

- 数据库的 MVCC 都是弱一致性的表现
- 并发高和一致性是矛盾的，需要权衡