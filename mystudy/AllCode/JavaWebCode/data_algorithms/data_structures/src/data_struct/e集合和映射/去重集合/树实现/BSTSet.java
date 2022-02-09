package data_struct.e集合和映射.去重集合.树实现;

import data_struct.d二分搜索树_04.base.BST;
import data_struct.e集合和映射.去重集合.Set;

/**
 * @Classname BSTSet
 * @Description TODO
 * @Date 2022/1/27 17:01
 * @Created by zhq
 */
public class BSTSet<E extends Comparable<E>> implements Set<E> {
    private BST<E> bst;

    public BSTSet() {
        this.bst = new BST<>();
    }

    @Override
    public void add(E e) {
        bst.add(e);
    }

    @Override
    public void remove(E e) {
        bst.remove(e);
    }

    @Override
    public boolean contains(E e) {
        return bst.contains(e);
    }

    @Override
    public int getSize() {
        return bst.size();
    }

    @Override
    public boolean isEmpty() {
        return bst.isEmpty();
    }


}
