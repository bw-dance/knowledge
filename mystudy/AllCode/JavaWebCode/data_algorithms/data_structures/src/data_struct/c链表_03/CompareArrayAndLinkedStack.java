package data_struct.c链表_03;


import data_struct.c链表_03.stack.LinkedStack;
import data_struct.b栈和队列02.stack.ArrayStack;
import data_struct.b栈和队列02.stack.Stack;

/**
 * @Classname CompareArrayAndLinkedStack
 * @Description TODO
 * @Date 2021/12/30 21:39
 * @Created by zhq
 */
public class CompareArrayAndLinkedStack {
    public static void main(String[] args) {
        int opCount = 100000;
        Stack<Integer> arrayStack = new ArrayStack<>();
        Stack<Integer> linkedStack = new LinkedStack<>();
        double v1 = testStack(arrayStack, opCount);
        System.out.println("Array:" + v1 + "s");
        double v2 = testStack(linkedStack, opCount);
        System.out.println("Linked:" + v2 + "s");
        System.out.println(arrayStack);
        System.out.println(linkedStack);
    }

    public static double testStack(Stack<Integer> stack, int opCount) {
        long begin = System.nanoTime();
        for (int i = 0; i < opCount; i++) {
            stack.push(i);
        }
        long end = System.nanoTime();
        return (end - begin) / 1000000000.0;
    }
}
