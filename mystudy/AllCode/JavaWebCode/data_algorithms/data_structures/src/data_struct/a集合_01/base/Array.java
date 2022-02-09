package data_struct.a集合_01.base;

import java.util.Arrays;

/**
 * @Classname Array
 * @Description TODO
 * @Date 2021/12/25 21:40
 * @Created by zhq
 */
public class Array {

    //定义数组
    private int[] data;
    //定义数组中的元素个数
    private int size;


    //构造函数，传入数组的容量capacity构造Array
    public Array(int capacity) {
        data = new int[capacity];
        size = 0;
    }

    //无参数的构造函数，默认数组的容量 capacity=10
    public Array() {
        //这里调用的是上方的Array
        //10为参数
        this(10);
    }

    //获取数组中的元素个数
    public int getSize() {
        return size;
    }

    //获取数组的容量
    public int getCapacity() {
        return data.length;
    }

    //返回数组是否为空
    public boolean isEmpty() {
        return size == 0;
    }

    //向数组中的首位添加元素
    //时间复杂度O（n），其他位置元素都往后挪
    //数组头部添加元素
    public void addFirst(int e) {
        //直接调用add()方法即可实现
        this.add(0, e);
    }

    //数组末尾添加元素
    public void addLast(int e) {
        //当元素个数等于数组的长度时，是不能进行元素添加的，会出现错误。在此处现抛出异常
        if (size == data.length) {
            //抛出了一个传入参数有问题的异常
            throw new IllegalArgumentException("AddLast fail. Array is full.");
        }
        data[size] = e;
        size++;
    }

    // 在第index位置插入元素e
    // 根据插入位置不同，时间复杂度不同，如果从中间插入，时间复杂度为O（n/2）=O（n）
    public void add(int index, int e) {
        if (size == data.length) {
            //抛出了一个传入参数有问题的异常
            throw new IllegalArgumentException("Add fail. Array is full.");
        }
        //如果index > size   说明索引的插入位置大于数组中已存在的元素的个数，说明数据不是紧密排列的，所以要杜绝这种情况的发生
        if (index < 0 || index > size) {
            throw new IllegalArgumentException("Add fail. Require index>=0 and index<=size.");
        }
        //指定位置插入元素
        for (int i = size - 1; i >= index; i--) {
            data[i + 1] = data[i];
        }
        data[index] = e;
        size++;
    }

    //获取index索引位置的元素
    //O（1）
    public int get(int index) {
        if (index < 0 || index >= size)
            throw new IllegalArgumentException("Get fail. Require index>=0 and index<size.");
        return data[index];
    }

    //修改index索引位置的元素为e
    public void set(int index, int e) {
        if (index < 0 || index > size)
            throw new IllegalArgumentException("Set fail. Require index>=0 and index<=size.");
        data[index] = e;
    }

    //查找数组中是否包含某个元素e
    //O（n）
    public boolean contains(int e) {
        for (int i = 0; i < data.length; i++) {
            if (data[i] == e) return true;
        }
        //如果循环结束，还没有找到，则说明不包含，return false即可
        return false;
    }

    //查找数组中是否包含某个元素e
    //O（n）
    public int find(int e) {
        for (int i = 0; i < data.length; i++) {
            if (data[i] == e) return i;
        }
        //如果此数组中没有元素e，则返回-1  无效索引
        return -1;
    }

    //删除某个位置的元素 并返回index  1、删除某个索引位置的元素  2、删除内容为***的元素
    //O（2/n）即O（n）  因为要考虑删除的是否是第一个或最后一个位置的元素
    public int remove(int index) {
        if (index < 0 || index >= size)
            throw new IllegalArgumentException("Remove failed. Index is illegal." + "Require index>=0 and index<size.");
        int ret = data[index];
        //注意临界值是<size-1,size-1对应索引是4，我们删除一个元素之后，这个位置上的元素就没有意义了，因此不用<=size-1;
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        size--;
        return ret;
    }


    //移除多个索引
    public void removeSelIndex(int... selIndex) {
        //排序:正序
        Arrays.sort(selIndex);
        //排序:反序
        for (int i = 0; i < selIndex.length / 2; i++) {
            int temp = selIndex[i];
            selIndex[i] = selIndex[selIndex.length - 1 - i];
            selIndex[selIndex.length - 1 - i] = temp;
        }
        //移除指定位置元素
        for (int i = 0; i < selIndex.length; i++) {
            //只删除在元素索引范围内的元素,超出范围的不删除
            if (selIndex[i] >= 0 && selIndex[i] < size) {
                for (int j = selIndex[i]; j < size - 1; j++) {
                    data[j] = data[j + 1];
                }
                size--;
            }
        }
    }

    //删除第一个元素
    //O（1）
    public int removeFirst() {
        return remove(0);

    }

    //删除最后一个元素
    //O（n）
    public int removeLast() {
        return remove(size - 1);
    }

    //删除指定元素
    public void removeElement(int e) {
        //方法一：
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (data[i] == e) index = i;
            break;
        }
        if (index > -1) remove(index);
        //方法二：
//        int index = find(e); //find(e)返回的为e的索引
//        if (index != -1) remove(index);
    }

    //删除多个元素
    public void removeElements(int... elements) {
        for (int i = 0; i < elements.length; i++) {
            for (int j = 0; j < size; j++) {
                if (elements[i] == data[j]) {
                    remove(j);
                    j--;
                }
            }
        }
    }

    //重写toString()方法
    @Override
    public String toString() {
        if (data == null)
            return "null";
        int iMax = size - 1;
        if (iMax == -1)
            return "[]";
        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(data[i]);
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }
}
