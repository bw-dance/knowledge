# JavaSE

## 集合

### HashMap

#### 源码剖析

##### put方法

1. 首先根据 key 的值计算 hash 值，找到该元素在数组中存储的下标；

2. 如果数组是空的，则调用 resize 进行初始化；

3. 如果没有哈希冲突直接放在对应的数组下标里；

4. 如果冲突了，且 key 已经存在，就覆盖掉 value；

5. 如果冲突后，发现该节点是红黑树，就将这个节点挂在树上；

6. 如果冲突后是链表，判断该链表是否大于 8 ，如果大于 8 并且数组容量小于 64，就进行扩容；如果链表节点大于 8 并且数组的容量大于 64，则将这个结构转换为红黑树；否则，链表插入键值对，若 key 存在，就覆盖掉 value。

```java
  public V put(K key, V value) {
        //1. 首先根据 key 的值计算 hash 值，找到该元素在数组中存储的下标；
        return putVal(hash(key), key, value, false, true);
    }    

   final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
        Node<K,V>[] tab; Node<K,V> p; int n, i;
       //2. 如果数组是空的，则调用 resize 进行初始化；
        if ((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
       //3. 如果没有哈希冲突直接放在对应的数组下标里；
        if ((p = tab[i = (n - 1) & hash]) == null)
            tab[i] = newNode(hash, key, value, null);
        else {
            Node<K,V> e; K k;
            //4. 如果冲突了，且 key 已经存在，就覆盖掉 value；
            if (p.hash == hash &&
                ((k = p.key) == key || (key != null && key.equals(k))))
                e = p;
            else if (p instanceof TreeNode)
                //5. 如果冲突后，发现该节点是红黑树，就将这个节点挂在树上；
                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
            else {
                //6. 如果冲突后是链表，判断该链表是否大于 8
                for (int binCount = 0; ; ++binCount) {
                    if ((e = p.next) == null) {
                        p.next = newNode(hash, key, value, null);
                        //判断链表长度是否大于8
                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                            //如果大于 8 尝试进行扩容
                            treeifyBin(tab, hash);
                        break;
                    }
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }
            key 存在，就覆盖掉 value。
            if (e != null) { // existing mapping for key
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                afterNodeAccess(e);
                return oldValue;
            }
        }
        ++modCount;
        if (++size > threshold)
            resize();
        afterNodeInsertion(evict);
        return null;
    }

    final void treeifyBin(Node<K,V>[] tab, int hash) {
        int n, index; Node<K,V> e;
        //如果大于 8 并且数组容量小于 64，就进行扩容；如果链表节点大于 8 并且数组的容量大于 64，则将这个结构转换为红黑树；
        if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY)
            resize();
        else if ((e = tab[index = (n - 1) & hash]) != null) {
            TreeNode<K,V> hd = null, tl = null;
            do {
                TreeNode<K,V> p = replacementTreeNode(e, null);
                if (tl == null)
                    hd = p;
                else {
                    p.prev = tl;
                    tl.next = p;
                }
                tl = p;
            } while ((e = e.next) != null);
            if ((tab[index] = hd) != null)
                hd.treeify(tab);
        }
    }
```

##### hash方法

```java
 public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
    }
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16); 1.获取hash值 2. 取模算hash值
}
3.计算下表。
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);
    else {
```

1. 获取key的hashcode值
2. 将hashcode值与hashcode又移16位的值进行^ 异或操作（相同为0，不同为1）
3. 通过取模计算：(n - 1) & hash 和 n%m相等（m为map的容量）

计算原理：

[(347条消息) h = key.hashCode()) ^ (h ＞＞＞ 16) 详细解读以及为什么要将hashCode值右移16位并且与原来的hashCode值进行异或操作_小爽帅到拖网速的博客-CSDN博客_key.hashcode](https://blog.csdn.net/weixin_46195957/article/details/125274802)

[(347条消息) HashMap(一)——(n - 1) & hash_誓杀小黄泉的博客-CSDN博客_(n - 1) & hash](https://blog.csdn.net/weixin_45692584/article/details/115310762)

![image-20221022084418698](https://mynotepicbed.oss-cn-beijing.aliyuncs.com/img/image-20221022084418698.png)

##### resize方法

[(359条消息) HashMap的最大容量为什么是2的30次方(1左移30)?_sayWhat_sayHello的博客-CSDN博客](https://yuwang.blog.csdn.net/article/details/83120324?spm=1001.2101.3001.6650.1&utm_medium=distribute.pc_relevant.none-task-blog-2~default~CTRLIST~Rate-1-83120324-blog-106938380.pc_relevant_recovery_v2&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2~default~CTRLIST~Rate-1-83120324-blog-106938380.pc_relevant_recovery_v2&utm_relevant_index=2)

### ConcurrentHashMap

#### key不能为null和value不能为null

会产生二义性

