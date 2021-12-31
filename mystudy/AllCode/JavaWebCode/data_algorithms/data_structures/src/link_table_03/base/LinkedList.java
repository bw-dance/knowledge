package link_table_03.base;

/**
 * @Classname LinkList
 * @Description 链表：头节点为首元素
 * @Date 2021/12/29 20:48
 * @Created by zhq
 */
public class LinkedList<E> {

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
    //链表存储的元素数量
    private int size;

    public LinkedList() {
        head = null;
        size = 0;
    }

    //获取链表元素数量
    public int getSize() {
        return size;
    }

    //判断链表是否为null
    public boolean isEmpty() {
        return size == 0;
    }

    //向头结点插入元素
    public void addFirst(E e) {
        //方法一
//        Node node = new Node(e);
//        node.next = head;
//        head = node;
        //方法二
        Node node = new Node(e, head);
        head = node;
        size++;
    }

    //添加到指定位置
    public void add(int index, E e) {
        if (index < 0 || index > size)
            throw new IllegalArgumentException("add fail index out of");
        if (index == 0) {
            addFirst(e);
        } else {
            //1. 获取头节点
            Node prev = head;
            //2. 获取指定索引位置的前一个节点
            for (int i = 0; i < index - 1; i++) {
                prev = prev.next;
            }
            //3. 节点指向变更
            //3.1 方式一
            // Node node = new Node(e, prev.next);
            //prev.next = node;
            //3.2 方式二
            prev.next = new Node(e, prev.next);
            size++;
        }
    }

    //向尾结点插入元素
    public void addLast(E e) {
        add(size, e);
    }

    //移除指定位置的节点
    public void remove(int index) {
        if (index < 0 || index >= size)
            throw new IllegalArgumentException("remove fail index out of");
        if (index == 0) head = head.next;
        //1. 获取指定索引位置的前一个节点
        Node prev = head;
        for (int i = 0; i < index - 1; i++) {
            prev = prev.next;
        }
        //2.要删除元素的前一个节点指向要删除元素指向的节点
        prev.next = prev.next.next;
        size--;
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        return "LinkedList{" +
                "head=" + head +
                ", size=" + size +
                '}';
    }
}
