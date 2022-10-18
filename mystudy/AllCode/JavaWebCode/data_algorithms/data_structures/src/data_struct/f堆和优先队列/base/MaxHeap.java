package data_struct.f堆和优先队列.base;

import java.util.Arrays;

public class MaxHeap<E extends Comparable<E>> {
    @Override
    public String toString() {
        return "MaxHeap{" +
                "data=" + data +
                '}';
    }

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
        if (index == 0) {
            throw new IllegalArgumentException("index-0 doesn't have parent.");
        }
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

    public E findMax() {
        return data.get(0);
    }

    //取出堆中的最大元素
    public E extractMax() {
        if (size() == 0) {
            throw new IllegalArgumentException("tree is empty");
        }
        E max = findMax();
        //将最后一个元素作为根节点
        data.swap(0, data.getSize() - 1);
        data.removeLast();
        //下浮元素
        shiftDown(0);
        return max;
    }

    //有问题
    private void shiftDownOne(int k) {
        //左节点
        int leftIndex = leftChild(k);
        //右节点
        int rightIndex = rightChild(k);
        //如果当前分支只有一个节点，那么一定是左节点。
        //此时将子节点和当前节点做判断，如果子节点大，则作为当前分支的根节点。
        if (rightIndex >= size()) {
            if (leftIndex >= size()) {
                return;
            } else {
                E left = data.get(leftIndex);
                if (left.compareTo(data.get(k)) > 0) {
                    data.swap(k, leftIndex);
                    return;
                }
            }
        }
        //如果当前分支有两个子节点，判断两个子节点大小，当前元素和子节点大的切换位置，大的子节点作为当前分支的根节点。
        E left = data.get(leftIndex);
        E right = data.get(rightIndex);
        if (left.compareTo(right) > 0) {
            data.swap(k, leftIndex);
            shiftDown(leftIndex);
        } else {
            data.swap(k, rightIndex);
            shiftDown(rightIndex);
        }
    }

    //有问题
    private void shiftDownTwo(int k) {
        //当前节点
        int now = k;
        //左节点
        int leftIndex = leftChild(k);
        //右节点
        int rightIndex = rightChild(k);
        //如果当前分支只有一个节点，那么一定是左节点。
        //此时将子节点和当前节点做判断，如果子节点大，则作为当前分支的根节点。
        while (leftIndex < size()) {
            if (rightIndex >= size()) {
                E left = data.get(leftIndex);
                if (left.compareTo(data.get(now)) > 0) {
                    data.swap(now, leftIndex);
                    return;
                }
                return;
            }
            //如果当前分支有两个子节点，判断两个子节点大小，当前元素和子节点大的切换位置，大的子节点作为当前分支的根节点。
            E left = data.get(leftIndex);
            E right = data.get(rightIndex);
            if (left.compareTo(right) > 0) {
                data.swap(now, leftIndex);
                now = leftIndex;
                rightIndex = rightChild(leftIndex);
                leftIndex = leftChild(leftIndex);

            } else {
                data.swap(now, rightIndex);
                now = rightIndex;
                leftIndex = leftChild(rightIndex);
                rightIndex = rightChild(rightIndex);
            }
        }
    }

    //最清晰的思路：获取两个子节点，取出值最大的那个节点与当前节点比较，如果当前节点大，break，如果当前节点小，交换位置。
    private void shiftDown(int k) {
        while (leftChild(k) < size()) {
            //存储左右孩子较大的那一个索引
            int j = leftChild(k);
            //获取左右两个孩子
            if (j + 1 < size() && data.get(j + 1).compareTo(data.get(j)) > 0) {
                j = j + 1;//j+1为右孩子索引。  或者这样写（rightChild(k)）
            }
            if (data.get(k).compareTo(data.get(j)) >= 0) {
                break;
            }
            data.swap(k, j);
            k = j;
        }
    }
}
