package data_struct.b栈和队列02;

import data_struct.b栈和队列02.queue.LoopQueue;

/**
 * @Classname LoopArrayMain
 * @Description TODO
 * @Date 2021/12/28 0:14
 * @Created by zhq
 */
public class LoopQueueMain {
    public static void main(String[] args) {
        LoopQueue<Integer> loopQueue = new LoopQueue<>();
        for (int i = 0; i < 10; i++) {
            loopQueue.enqueue(i);
            if (i%3==2){
                loopQueue.dequeue();
            }
            System.out.println(loopQueue);
        }

    }
}
