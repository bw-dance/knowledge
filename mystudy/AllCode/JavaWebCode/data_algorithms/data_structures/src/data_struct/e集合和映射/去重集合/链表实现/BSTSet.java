package data_struct.e集合和映射.去重集合.链表实现;

import data_struct.e集合和映射.去重集合.Set;

import java.awt.*;

/**
 * @Classname BSTSet
 * @Description TODO
 * @Date 2022/1/27 17:16
 * @Created by zhq
 */
public class BSTSet<E> implements Set<E> {
    private LinkedList list;

    public BSTSet() {
        this.list = new LinkedList();
    }

    @Override
    public void add(E e) {
        //注意：如果链表中已存在，则不能进行添加。
        if (!contains(e)) list.addFirst(e);
    }

    @Override
    public void remove(E e) {
        list.removeElement(e);
    }

    @Override
    public boolean contains(E e) {
        return list.contains(e);
    }

    @Override
    public int getSize() {
        return list.getSize();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }
}
