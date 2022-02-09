package data_struct.b栈和队列02.other.两个栈实现队列;

import java.util.LinkedList;

/**
 * @Classname StackQueue
 * @Description TODO
 * @Date 2022/1/11 23:01
 * @Created by zhq
 */
public class StackQueue {
    private LinkedList<Integer> stack01 = new LinkedList<>();
    private LinkedList<Integer> stack02 = new LinkedList<>();

    public void enqueue(Integer val) {
        stack01.addLast(val);
    }

    //注意点：1.出队时，必须将第二个栈的元素全部出栈，否则将会出现错误。
    public void dequeue() {
        //将stack01中的数据都放入stack02
        while (stack01.peek() != null)
            stack02.push(stack01.pop());
        while (stack02.peek() != null)
            System.out.println(stack02.pop());
    }

    public int peek() {
        return stack02.peek();
    }

    public boolean empty() {
        return stack02.size() == 0;
    }

    public static void main(String[] args) {
        StackQueue stackQueue = new StackQueue();
        for (int i = 1; i < 5; i++) {
            stackQueue.enqueue(i);
            if (i <= 2) {
                stackQueue.dequeue();
            } else {
                stackQueue.dequeue();
            }
        }
    }

}
