package link_table_03.queue_02;

import stack_queue_02.queue.Queue;

/**
 * @Classname LinkedQueue
 * @Description 无虚拟头结点
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
    private Node head;
    //尾结点
    private Node tail;
    //链表存储的元素数量
    private int size;

    public LinkedQueue() {
        head = null;
        tail = null;
        size = 0;
    }


    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (Node cur = head; cur != null; cur = cur.next) {
            res.append(cur + "->");
        }
        res.append("NULL");
        return res.toString();
    }


    @Override
    public void enqueue(E e) {
        if (isEmpty()) {
            head = new Node(e);
            tail = head;
        } else {
            tail.next = new Node(e);
            tail = tail.next;
        }
        size++;
    }

    @Override
    public E dequeue() {
        if (isEmpty()) throw new IllegalArgumentException("empty");
        Node retNode = head;
        head = head.next;
        retNode.next = null;
        //当头结点为null时，说明此时衣帽间没有元素
        if (head == null)
            tail = null;
        size--;
        return (E) retNode.e;
    }

    @Override
    public E getFront() {
        if (isEmpty()) throw new IllegalArgumentException("empty");
        return (E) head.e;
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
