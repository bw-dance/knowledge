package link_table_03.queue;

import stack_queue_02.queue.Queue;

/**
 * @Classname LinkedQueue
 * @Description TODO
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
        tail = dummyHead;
        size = 0;
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


    @Override
    public void enqueue(E e) {
        Node prev = new Node(e, null);
        tail.next = prev;
//        tail++;
        size++;
    }

    @Override
    public E dequeue() {
        if (size == 0) throw new IllegalArgumentException("empty");
        Node res = dummyHead.next;
        dummyHead.next = res.next;
        res.next = null;
        size--;
        return (E) res.e;
    }

    @Override
    public E getFront() {
        if (size == 0) throw new IllegalArgumentException("empty");
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
}
