package likou.z_suanfa_miji.b队列和栈.fLRU;


import java.util.HashMap;
import java.util.Map;

/**
 * @Classname LRU缓存_146
 * @Description TODO
 * @Date 2022/3/2 16:38
 * @Created by zhq
 */
public class LRU缓存_146 {
    public static void main(String[] args) {
        LRUCache lruCache = new LRUCache(2);
        lruCache.put(2, 1);
        lruCache.put(1, 1);
        lruCache.put(2, 3);
        lruCache.put(4, 1);
        System.out.println(lruCache.get(1));
        System.out.println(lruCache.get(2));

    }
}


class LRUCache {
    Map<Integer, Node> map;
    DoubleLinkedList doubleLinkedList;
    int capacity;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();
        this.doubleLinkedList = new DoubleLinkedList();
    }

    //获取元素：存在元素时，将当前元素移动到链表末尾
    public int get(int key) {
        Node node = map.get(key);
        if (node == null) return -1;
        //切换链表等级，并将元素添加到元素末尾。
        changeRank(node);
        return node.value;
    }

    //存放元素：
    // 1. 如果链表中已存在，更新元素的值，并将元素移动到链表末尾。
    //2. 如果链表中不存在，在容量足够的情况下，在链表尾部添加元素
    //3.  如果链表中不存在，在容量不足够的情况下，先淘汰链表首元素，再在链表尾部添加元素
    public void put(int key, int value) {
        if (map.containsKey(key)) {
            Node node = map.get(key);
            node.value = value;
            //切换链表等级，并将元素添加到元素末尾。
            changeRank(node);
        } else {
            if (capacity > 0) {
                //添加新的元素
                addNode(key, value);
            } else {
                //移除最久不使用元素
                remove();
                //添加新的元素
                addNode(key, value);
            }
        }
    }

    //切换节点位置：最近添加和最近使用的放入链表尾部指向
    private void changeRank(Node node) {
        //切换元素当前位置
        node.prev.next = node.next;
        node.next.prev = node.prev;
        //将元素添加到链表末尾
        addNodeToTail(node);
    }

    //移除节点：移除头结点指向的节点
    public void remove() {
        Node headEle = doubleLinkedList.head;
        map.remove(headEle.next.key);
        headEle.next = headEle.next.next;
        headEle.next.prev = headEle;
        capacity++;
    }

    //添加节点：添加节点到末尾
    public void addNode(int key, int value) {
        Node node = new Node(key, value);
        addNodeToTail(node);
        map.put(key, node);
        capacity--;
    }

    //将元素添加至链表末尾
    public void addNodeToTail(Node node) {
        //尾元素
        Node tailNode = doubleLinkedList.tail;
        //尾元素前一个元素
        Node beforeNode = tailNode.prev;
        tailNode.prev = node;
        node.next = tailNode;
        node.prev = beforeNode;
        beforeNode.next = node;
    }
}

//创建双向链表
class DoubleLinkedList {
    Node head;
    Node tail;

    public DoubleLinkedList() {
        head = new Node(-1, -1);
        tail = new Node(-1, -1);
        head.next = tail;
        tail.prev = head;
    }

}

//创建节点
class Node {
    int key;
    int value;
    Node prev;
    Node next;

    public Node(int key, int value) {
        this.key = key;
        this.value = value;
    }
}

