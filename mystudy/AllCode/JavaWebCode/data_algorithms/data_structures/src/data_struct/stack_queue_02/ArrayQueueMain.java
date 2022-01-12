package data_struct.stack_queue_02;

import data_struct.stack_queue_02.queue.ArrayQueue;

/**
 * @Classname ArrayQueueMain
 * @Description TODO
 * @Date 2021/12/27 23:08
 * @Created by zhq
 */
public class ArrayQueueMain {
    public static void main(String[] args) {
        ArrayQueue<Integer> arrayQueue = new ArrayQueue<>();
        for (int a = 0; a < 5; a++) {
            arrayQueue.enqueue(a);
            System.out.println(arrayQueue);
        }
        //栈中的元素数量
        System.out.println(arrayQueue.getSize());//5
        //移除队首的元素
        System.out.println(arrayQueue.dequeue());//4
        //查看队中的第一个元素
        System.out.println(arrayQueue.getFront());//0
        //判断队是否为空
        System.out.println(arrayQueue.isEmpty());//false
    }
}
