package likou.z_suanfa_miji.b队列和栈.gFLU;


import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @Classname LFU缓存_460
 * @Description TODO
 * @Date 2022/3/3 17:30
 * @Created by zhq
 */
public class LFU缓存_460 {

}

class LFUCache {
    //存储节点，key为键
    Map<Integer, Node> dataMap;
    //存储链表，key为数量
    Map<Integer, DoubleLinkedList> countMap;
    //缓存大小  如果capacity为-1，说明传入的capacity为0,不能存储元素。
    int capacity;
    //最小出现元素频率的索引
    int minCount;

    public LFUCache(int capacity) {
        if (this.capacity<=0) throw new IllegalArgumentException("容量必须大于0");
        this.capacity = capacity == 0 ? -1 : capacity;
        this.dataMap = new HashMap<>();
        this.countMap = new HashMap<>();
    }

    public int get(int key) {
        Node node = dataMap.get(key);
        int resVal = -1;
        if (node != null && capacity != -1) {
            //1.删除元素在原频次链表中的位置
            delEleAtOriginList(node);
            //2.使用频率增加
            node.count++;
            //3.移动该删除元素到新的频次链表中，新添加的元素要添加到链表的末尾
            addEleAtNewList(node);
            resVal = node.value;
        }
        return resVal;
    }

    public void put(int key, int value) {
        if (capacity == -1) return;
        if (dataMap.containsKey(key)) {
            //1.节点内容更新
            Node node = dataMap.get(key);
            node.value = value;
            //2.删除元素在原频次链表中的位置
            delEleAtOriginList(node);
            //3.添加已存在元素，不需要进行minCount判断，使用频率增加
            node.count++;
            //4.移动该删除元素到新的频次链表中，新添加的元素要添加到链表的末尾
            addEleAtNewList(node);
        } else {
            //容量足
            if (capacity > 0) {
                Node node = new Node(key, value);
                //新添加元素放入dataMap
                dataMap.put(key, node);
                //更新最小使用频率
                minCount = 1;
                //新添加的元素放置末尾
                addEleAtNewList(node);
                //缓存大小-1
                capacity--;
            } else if (capacity == 0) {//容量不足
                //移除元素
                del();
                Node node = new Node(key, value);
                //元素添加到map
                dataMap.put(key, node);
                //更新最小使用频率
                minCount = 1;
                //新添加的元素放置末尾
                addEleAtNewList(node);
                //总容量减少
                capacity--;
            }
        }


    }

    //删除元素
    private void del() {
        //获取使用频率最小的双向链表
        DoubleLinkedList list = countMap.get(minCount);
        //移除链表中的第一个元素
        Node head = list.head;
        Node delEle = list.head.next;
        head.next = delEle.next;
        delEle.next.pre = head;
        dataMap.remove(delEle.key);
        list.NodeNum--;
        if (list.NodeNum == 0) {
            countMap.remove(minCount);
        }
        //总容量变大
        capacity++;
    }

    //删除元素在原频次链表的位置
    private void delEleAtOriginList(Node node) {
        DoubleLinkedList originList = countMap.get(node.count);
        node.pre.next = node.next;
        node.next.pre = node.pre;
        originList.NodeNum--;
        if (originList.NodeNum == 0) {
            countMap.remove(node.count);
            if (minCount == node.count) minCount++;
        }
    }

    //将元素添加到链表末尾
    private void addEleAtNewList(Node node) {
        DoubleLinkedList nowList = countMap.getOrDefault(node.count, new DoubleLinkedList());
        Node tailEle = nowList.tail;
        Node tailPreEle = tailEle.pre;
        tailPreEle.next = node;
        node.pre = tailPreEle;
        node.next = tailEle;
        tailEle.pre = node;
        nowList.NodeNum++;
        countMap.put(node.count, nowList);
    }
}

class Node {
    int key;
    int value;
    int count;
    Node pre;
    Node next;

    public Node(int key, int value) {
        this.key = key;
        this.value = value;
        this.count = 1;
    }
}

class DoubleLinkedList {

    Node head;
    Node tail;
    int NodeNum;

    public DoubleLinkedList() {
        this.head = new Node(-1, -1);
        this.tail = new Node(-1, -1);
        head.next = tail;
        tail.pre = head;
        this.NodeNum = 0;
    }
}
