package data_struct.b栈和队列02.queue;

import java.util.Arrays;

/**
 * @Classname Array
 * @Description TODO
 * @Date 2021/12/27 12:50
 * @Created by zhq
 */
public class Array<E> {
    // 定义数组
    private E[] data;
    // 定义数组中的元素个数
    // size在运用中既充当了数组中元素的个数（非长度），又可以间接充当元素的索引
    private int size;

    // 构造函数，传入数组的容量capacity构造Array
    public Array(int capacity) {
        //因为 new一个 E[]的数组会报错，所以这里new了一个Object，然后进行强制转换
        data = (E[]) new Object[capacity];
        size = 0;
    }

    // 无参构造函数，默认容器的容量
    public Array() {
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
    public void addFirst(E e) {
        //直接调用add()方法即可实现
        this.add(0, e);
    }

    //数组末尾添加元素
    public void addLast(E e) {
        this.add(size, e);
    }

    // 在第index位置插入元素e
    // 根据插入位置不同，时间复杂度不同，如果从中间插入，时间复杂度为O（n/2）=O（n）
    public void add(int index, E e) {
        //如果index > size   说明索引的插入位置大于数组中已存在的元素的个数，说明数据不是紧密排列的，所以要杜绝这种情况的发生
        if (index < 0 || index > size) {
            throw new IllegalArgumentException("Add fail. Require index>=0 and index<=size.");
        }
        if (size == data.length) {
            //数组重置,进行二倍扩容
            resize(2 * data.length);
        }
        //指定位置插入元素
        for (int i = size - 1; i >= index; i--) {
            data[i + 1] = data[i];
        }
        data[index] = e;
        size++;
    }

    //数组重置方法   O（n）
    public void resize(int newCapacity) {
        //新建一个数组对象
        E[] newData = (E[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            //将之前的数组遍历到新数组中
            newData[i] = data[i];
        }
        //更换为类数组
        data = newData;
    }

    //获取index索引位置的元素
    //O（1）
    public E get(int index) {
        if (index < 0 || index >= size)
            throw new IllegalArgumentException("Get fail. Require index>=0 and index<size.");
        return data[index];
    }

    //修改index索引位置的元素为e
    public void set(int index, E e) {
        if (index < 0 || index > size)
            throw new IllegalArgumentException("Set fail. Require index>=0 and index<=size.");
        data[index] = e;
    }

    //查找数组中是否包含某个元素e
    //O（n）
    public boolean contains(E e) {
        for (int i = 0; i < data.length; i++) {
            if (data[i] == e) return true;
        }
        //如果循环结束，还没有找到，则说明不包含，return false即可
        return false;
    }

    //查找数组中是否包含某个元素e
    //O（n）
    public int find(E e) {
        for (int i = 0; i < data.length; i++) {
            if (data[i] == e) return i;
        }
        //如果此数组中没有元素e，则返回-1  无效索引
        return -1;
    }

    //删除某个位置的元素 并返回对应的值  1、删除某个索引位置的元素  2、删除内容为***的元素
    //O（2/n）即O（n）  因为要考虑删除的是否是第一个或最后一个位置的元素
    public E remove(int index) {
        if (index < 0 || index >= size)
            throw new IllegalArgumentException("Remove failed. Index is illegal." + "Require index>=0 and index<size.");
        E ret = data[index];
        //注意临界值是<size-1,size-1对应索引是4，我们删除一个元素之后，这个位置上的元素就没有意义了，因此不用<=size-1;
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        size--;
        //当元素的个数小于数组长度的1/2时，则数组长度也/2
        if (size < data.length / 2) {
            resize(data.length / 2);
        }
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
        //当元素的个数小于数组长度的1/2时，则数组长度也/2
        if (size < data.length / 2) {
            resize(data.length / 2);
        }
    }

    //删除第一个元素
    //O（1）
    public E removeFirst() {
        return remove(0);

    }

    //删除最后一个元素
    //O（n）
    public E removeLast() {
        return remove(size - 1);
    }

    //删除指定元素
    public void removeElement(E e) {
        //方法一：
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (data[i] == e) index = i;
            break;
        }
        if (index > -1) remove(index);
    }

    //删除多个元素
    public void removeElements(E... elements) {
        for (int i = 0; i < elements.length; i++) {
            for (int j = 0; j < size; j++) {
                if (elements[i] == data[j]) {
                    remove(j);
                    j--;
                }
            }
        }
    }

    @Override
    //添加@Override的原因是：因为toString属于Object类的方法，此时要覆盖object类的同toString方法，
    //如果toString拼写错误时，Override会检索不到Object中的方法，就会提示写法有错误
    public String toString() {
        //拼接一个字符串
        StringBuilder res = new StringBuilder();
        res.append(String.format("Array:size=%d,capacity=%d\n", size, data.length));
        res.append('[');
        for (int i = 0; i < size; i++) {
            res.append(data[i]);

            //每遍历一个元素，往后面添加一个逗号
            if (i != size - 1) {
                res.append(",");
            }

        }
        res.append(']');
        return res.toString();
    }
}