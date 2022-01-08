package link_table_03;

import link_table_03.stack.LinkedStack;

/**
 * @Classname LinkedStackMain
 * @Description TODO
 * @Date 2021/12/30 21:20
 * @Created by zhq
 */
public class LinkedStackMain {
    public static void main(String[] args) {
        LinkedStack linkedStack = new LinkedStack();
        for (int i = 0; i < 10; i++) {
            linkedStack.push(i);
            if (i % 3 == 2) {
                linkedStack.pop();
            }
            System.out.println(linkedStack);
        }
        System.out.println(linkedStack.peek());
        System.out.println(linkedStack);

    }
}
