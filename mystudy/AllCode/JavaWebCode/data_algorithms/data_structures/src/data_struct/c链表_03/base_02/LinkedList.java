package data_struct.c链表_03.base_02;

/**
 * @Classname LinkList
 * @Description 链表：头节点为虚拟元素
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
    private Node dummyHead;
    //链表存储的元素数量
    private int size;

    public LinkedList() {
        dummyHead = new Node(null, null);
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


    //添加到指定位置
    public void add(int index, E e) {
        if (index < 0 || index > size)
            throw new IllegalArgumentException("Add failed.  Illegal index.");
        //1. 获取头节点
        Node prev = dummyHead;
        //2. 获取指定索引位置的前一个节点
        for (int i = 0; i < index; i++) {
            prev = prev.next;
        }
        //3. 节点指向变更
        prev.next = new Node(e, prev.next);
        size++;
    }

    //向头结点插入元素
    public void addFirst(E e) {
        add(0, e);
    }

    //向尾结点插入元素
    public void addLast(E e) {
        add(size, e);
    }


    //获取指定索引结点的值（不常用）
    public E get(int index) {
        if (index < 0 || index >= size)
            throw new IllegalArgumentException("Get failed.  Illegal index.");
        Node cur = dummyHead.next;
        for (int i = 0; i < index; i++) {
            cur = cur.next;
        }
        return (E) cur.e;
    }

    //获取头结点
    public E getFront() {
        return get(0);
    }

    //获取尾结点
    public E getLast() {
        return get(size - 1);
    }

    //更新某个结点的值
    public void set(int index, E e) {
        if (index < 0 || index >= size)
            throw new IllegalArgumentException("Set failed. Illegal index.");
        Node cur = dummyHead.next;
        for (int i = 0; i < index; i++)
            cur = cur.next;
        cur.e = e;
    }

    //查找包含元素e
    public boolean contains(E e) {
        Node cur = dummyHead.next;
        //方式一：使用for循环
//        for (int i = 0; i < size; i++) {
//            if (cur.e.equals(e))
//                return true;
//            cur = cur.next;
//        }
        //方式二：使用for循环
//        for (Node cur = dummyHead.next;cur!=null;cur=cur.next){
//            if (cur.e.equals(e))
//                return true;
//        }
        //方式三：使用while循环
        while (cur != null) {
            if (cur.e.equals(e))
                return true;
            cur = cur.next;
        }
        return false;
    }


    //移除指定位置的节点
    public E remove(int index) {
        if (index < 0 || index >= size)
            throw new IllegalArgumentException("Remove failed.  Illegal index.");
        //1. 获取指定索引位置的前一个节点
        Node prev = dummyHead;
        for (int i = 0; i < index; i++) {
            prev = prev.next;
        }
        //2.要删除元素的前一个节点指向要删除元素指向的节点

        //2.1获取要删除的元素
        Node retNode = prev.next;
        //2.2切换指向
        prev.next = retNode.next;
        //2.3要删除的元素的next置空
        retNode.next = null;
        size--;
        return (E) retNode.e;
    }


    //从链表中删除元素e
    public void removeElement(E e) {
        Node prev = dummyHead;
        while (prev.next != null) {
            if (prev.next.e.equals(e)) break;
            prev = prev.next;
        }
        if (prev.next != null) {
            Node delNode = prev.next;
            prev.next = delNode.next;
            delNode.next = null;
        }
    }

    //从链表中删除所有值为e的元素
    public void removeElements(E e) {
        Node prev = dummyHead;
        while (prev!=null&&prev.next != null) {
            if (prev.next.e.equals(e)) {
                Node delNode = prev.next;
                prev.next = delNode.next;
                delNode.next = null;
            }
            prev = prev.next;
        }
    }

    //移除第一个节点
    public E removeFist() {
        return remove(0);
    }

    //移除最后一个节点
    public E removeLast() {
        return remove(size - 1);
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
