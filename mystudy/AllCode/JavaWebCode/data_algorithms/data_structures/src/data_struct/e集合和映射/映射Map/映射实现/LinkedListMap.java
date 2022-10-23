package data_struct.e集合和映射.映射Map.映射实现;

import data_struct.e集合和映射.映射Map.Map;

import java.util.PriorityQueue;
import java.util.prefs.BackingStoreException;

/**
 * @Classname LinkedListMap
 * @Description TODO
 * @Date 2022/2/8 21:12
 * @Created by zhq
 */
public class LinkedListMap<K, V> implements Map<K, V> {

    private class Node {
        public K key;
        public V value;
        public Node next;

        public Node(K key, V value, Node next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public Node(K key, V value) {
            this(key, value, null);
        }

        public Node() {
            this(null, null, null);
        }

        @Override
        public String toString() {
            return key.toString() + " : " + value.toString();
        }
    }

    private Node dummyHead;
    private int size;


    private Node getNode(K key) {
        //获取节点时，获取符合要求的节点，直接返回即可。
        Node cur = dummyHead.next;
        while (cur != null) {
            if (cur.key.equals(key))
                return cur;
            cur = cur.next;
        }
        return null;
    }

    @Override
    public void add(K key, V value) {
        Node node = getNode(key);
        if (node == null) {
            //将新添加的节点作为第一个节点
            dummyHead.next = new Node(key, value, dummyHead.next);
            size++;
        }
    }

    @Override
    public V remove(K key) {

        //删除节点时，需要获取指定节点的前一个节点。切换指向。
        Node prev = dummyHead;
        while (prev.next != null) {
            if (prev.next.key.equals(key))
                break;
            prev = prev.next;
        }
        //如果有符合删除要求的节点，则测试获取的是它的前一个节点。
        //如果没有符合要求的节点，那么此时prev是最后一个节点，是null。
        if (prev.next != null) {
            Node delNode = prev.next;
            prev.next = delNode.next;
            delNode.next = null;
            size--;
            return delNode.value;
        }
        return null;
    }


    @Override
    public void set(K key, V newValue) {
        Node node = getNode(key);
        if (node == null)
            throw new IllegalArgumentException(key + " doesn't exist!");
        node.value = newValue;
    }

    @Override
    public boolean contains(K key) {
        return getNode(key) != null;
    }

    @Override
    public V get(K key) {
        Node node = getNode(key);
        return node == null ? null : node.value;
    }


    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return getSize() == 0;
    }
}
