package data_struct.c链表_03.queue;

import data_struct.b栈和队列02.queue.Queue;

/**
 * @Classname LinkedQueue
 * @Description 有虚拟头结点,链表实现队列
 * @Date 2021/12/30 22:13
 * @Created by zhq
 */
public class LinkedQueue<E> implements Queue<E> {
    //节点类
    private class Node<E> {
        E e;
        Node next;

        public Node(E e, Node next) {
            this.e = e;
            this.next = next;
        }

        public Node(E e) {
            this(e, null);
        }

        public Node() {
            this(null, null);
        }

        @Override
        public String toString() {
            return e.toString();
        }
    }

    //头结点
    private Node dummyHead;
    //尾结点
    private Node tail;
    //链表存储的元素数量
    private int size;

    public LinkedQueue() {
        dummyHead = new Node(null, null);
        tail = null;
        size = 0;
    }


    //入队操作
    @Override
    public void enqueue(E e) {
        Node cur = new Node(e, null);
        if (isEmpty()) {
            dummyHead.next = cur;
            tail = cur;
        } else {
            tail.next = cur;
            tail = cur;
        }
        size++;
    }

    //出队操作
    @Override
    public E dequeue() {
        if (isEmpty()) throw new IllegalArgumentException("empty");
        Node retNode = dummyHead.next;
        dummyHead.next = retNode.next;
        retNode.next = null;
        if (dummyHead.next == null)
            tail = null;
        size--;
        return (E) retNode.e;
    }

    //获取头结点
    @Override
    public E getFront() {
        if (isEmpty()) throw new IllegalArgumentException("empty");
        return (E) dummyHead.next.e;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return getSize() == 0;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (Node cur = dummyHead.next; cur != null; cur = cur.next) {
            res.append(cur + "->");
        }
        res.append("NULL");
        return res.toString();
    }
}
