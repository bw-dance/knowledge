package data_struct.f堆和优先队列.base;

import java.util.Arrays;
import java.util.Random;

public class 堆排序 {


    public static void main(String[] args) {
//       int[] source = {91, 60, 96, 13, 35, 65, 46, 65, 10, 30, 20, 31, 77, 81, 22};
//        Heap heap = new Heap(Arrays.copyOf(source, source.length));
//        //排序
//        int[] res = heap.sort();
//        System.out.println(Arrays.toString(source));
//        System.out.println(Arrays.toString(res));

        int n = 100;
        int[] source2 =new int[n];
        Random random    = new Random();
        for (int i = 0; i < n; i++) {
            source2[i]=random.nextInt(100);
        }
        Heap heap = new Heap(Arrays.copyOf(source2, source2.length));
        System.out.println(Arrays.toString(heap.sort()));

    }
}

class Heap {
    int[] source;//待排序数据
    int size;//数组中元素的长度

    public Heap(int[] source) {
        this.source = source;
        this.size = source.length;
        //构建最大堆
        buildMaxHeap();
    }

    //排序
    public int[] sort() {
        int[] target = new int[size];
        int tarCap = 0;
        while (size > 0) {
            target[tarCap] = extractMax();
            tarCap++;
        }
        return target;
    }

    //传入的元素不规范，将其构建为最大堆
    private void buildMaxHeap() {
        for (int i = getParentIndex(source.length - 1); i >= 0; i--) {
//            int j = leftChildIndex(i);
//            if (j >= size) {
//                continue;
//            }
//            if (j + 1 < size && getData(j + 1) > getData(j)) {
//                j = j + 1;//即右侧的节点大，获取右侧的节点
//            }
//            int maxChild = getData(j);
//            //进行位置交换
//            int cur = getData(i);
//            if (maxChild > cur) {
//                swap(j, i);
//                //进行元素下浮。原因：交换位置后，有可能当前位置的元素值还小于maxChild的child
//                shiftDown(j);
//            }
            //直接进行下浮操作即可。
            shiftDown(i);
        }
    }
    private  int getParentIndex(int index){
        return (index-1)/2;
    }

    //获取元素的值
    private int getData(int index) {
        return source[index];
    }

    //获取左侧子节点
    private int leftChildIndex(int cur) {
        return cur * 2 + 1;
    }

    //获取右侧子节点（左侧子节点+1）
    private int rightChildIndex(int cur) {
        return cur * 2 + 2;
    }

    //交换两个索引位置的元素
    private void swap(int index, int target) {
        int i = source[index];
        source[index] = source[target];
        source[target] = i;
    }

    //取出根节点
    public int extractMax() {
        int res = source[0];
        int lastData = source[size - 1];
        source[0] = lastData;
        size--;
        //下浮元素。
        shiftDown(0);
        return res;
    }

    //下浮元素
    private void shiftDown(int i) {
        while (leftChildIndex(i) < size) {
            int j = leftChildIndex(i);
            if (j + 1 < size && getData(j + 1) > getData(j)) {
                j = j + 1;
            }
            int maxChild = getData(j);
            int cur = getData(i);
            if (maxChild > cur) {
                swap(i, j);
                i = j;
            } else {
                break;
            }
        }
    }
}
