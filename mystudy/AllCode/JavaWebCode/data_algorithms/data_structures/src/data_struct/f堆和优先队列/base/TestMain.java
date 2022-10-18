package data_struct.f堆和优先队列.base;

import java.util.Random;

public class TestMain {
    public static void main(String[] args) {
//        int[] arr = {61,52,30,28,41,22,13,19,17,16,15};
//        for (int i = 0; i < arr.length; i++) {
//            maxHeap.add(arr[i]);
//        }
//        maxHeap.extractMax();
//        int a = 1;
//        System.out.println(a);
        MaxHeap<Integer> maxHeap = new MaxHeap<>();
        Random random    = new Random();
        int n = 100000;
        for (int i = 0; i < n; i++) {
            maxHeap.add(random.nextInt(Integer.MAX_VALUE));
        }
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i]=maxHeap.extractMax();
        }
        for (int i = 1; i < n; i++) {
            if (arr[i-1]<arr[i])
                throw new IllegalArgumentException("Error");
        }
        System.out.println("complete");
    }
}
