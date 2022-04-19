package data_struct.f堆和优先队列.base;

public class MaxHeap<E extends Comparable<E>> {

    private Array<E> data;

    public MaxHeap(int capacity) {
        data = new Array<>(capacity);
    }

    public MaxHeap() {
        data = new Array<>();
    }

    // 返回堆中的元素个数
    public int size() {
        return data.getSize();
    }

    // 返回一个布尔值, 表示堆中是否为空
    public boolean isEmpty() {
        return data.isEmpty();
    }

    // 返回完全二叉树的数组表示中，一个索引所表示的元素的父亲节点的索引
    private int parent(int index) {
        if (index == 0)
            throw new IllegalArgumentException("index-0 doesn't have parent.");
        return (index - 1) / 2;
    }

    // 返回完全二叉树的数组表示中，一个索引所表示的元素的左孩子节点的索引
    private int leftChild(int index) {
        return index * 2 + 1;
    }

    // 返回完全二叉树的数组表示中，一个索引所表示的元素的右孩子节点的索引
    private int rightChild(int index) {
        return index * 2 + 2;
    }


    //添加元素
    public void add(E e) {
        data.addLast(e);
        //上浮元素
        siftUp(data.getSize() - 1);
    }

    //元素上浮操作。
    private void siftUp(int k) {
        while (k > 0 && data.get(parent(k)).compareTo(data.get(k)) < 0) {
            data.swap(k, parent(k));
            k = parent(k);
        }
    }


    //移除元素:只能移除根节点。
    public void remove() {
        if (size() == 0) throw new IllegalArgumentException("tree is empty");
        //将最后一个元素作为根节点
        data.swap(0, data.getSize() - 1);
        data.removeLast();
        //下浮元素
        siftdown(0);
    }

    private void siftdown(int k) {
        E left = data.get(leftChild(k));
        E right = data.get(rightChild(k));
        E cur = data.get(k);
        if (left==null&&right==null) return;
        if (left == null) {
            data.swap(k, rightChild(k));
            return;
        }
        if (right==null){
            data.swap(k,leftChild(k));
            return;
        }
        while (left != null && right != null && (cur.compareTo(left) < 0 || cur.compareTo(right) < 0)) {
            if (left.compareTo(right) > 0) {
                data.swap(k, leftChild(k));
                k = leftChild(k);
            } else {
                data.swap(k, rightChild(k));
                k = rightChild(k);
            }
        }
    }
}
