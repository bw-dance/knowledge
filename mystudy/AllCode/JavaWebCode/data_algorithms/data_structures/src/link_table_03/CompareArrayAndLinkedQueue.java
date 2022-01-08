package link_table_03;


import link_table_03.queue.LinkedQueue;
import link_table_03.stack.LinkedStack;
import stack_queue_02.queue.LoopQueue;
import stack_queue_02.queue.Queue;
import stack_queue_02.stack.ArrayStack;
import stack_queue_02.stack.Stack;

/**
 * @Classname CompareArrayAndLinkedStack
 * @Description TODO
 * @Date 2021/12/30 21:39
 * @Created by zhq
 */
public class CompareArrayAndLinkedQueue {
    public static void main(String[] args) {
        int opCount = 1000000;
        Queue<Integer> arrayQueue = new LoopQueue<>();
        Queue<Integer> linkedQueue = new LinkedQueue<>();
        double v1 = testStack(arrayQueue, opCount);
        System.out.println("Array:" + v1 + "s");
        double v2 = testStack(linkedQueue, opCount);
        System.out.println("Linked:" + v2 + "s");
    }

    public static double testStack(Queue<Integer> queue, int opCount) {
        long begin = System.nanoTime();
        for (int i = 0; i < opCount; i++) {
            queue.enqueue(i);
        }
        long end = System.nanoTime();
        return (end - begin) / 1000000000.0;
    }
}
