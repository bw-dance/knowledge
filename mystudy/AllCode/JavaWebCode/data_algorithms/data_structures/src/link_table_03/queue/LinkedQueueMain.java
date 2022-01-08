package link_table_03;

import link_table_03.queue.LinkedQueue;
import stack_queue_02.queue.Queue;

/**
 * @Classname LinkedQueueMain
 * @Description TODO
 * @Date 2021/12/31 16:34
 * @Created by zhq
 */
public class LinkedQueueMain {
    public static void main(String[] args) {
        Queue<Integer> linkedQueue = new LinkedQueue<>();
        for (int i = 0; i < 10; i++) {
            linkedQueue.enqueue(i);
            System.out.println(linkedQueue);
            if (i%3==2){
                linkedQueue.dequeue();
            }
        }
        for (int i = 0; i < 10; i++) {
            linkedQueue.dequeue();
            System.out.println(linkedQueue);
        }
        System.out.println(linkedQueue);
    }
}
