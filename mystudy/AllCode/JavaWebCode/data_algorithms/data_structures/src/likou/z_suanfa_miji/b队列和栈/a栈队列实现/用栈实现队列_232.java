package likou.z_suanfa_miji.b队列和栈.a栈队列实现;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;

/**
 * @Classname 用栈实现队列_232
 * @Description TODO
 * @Date 2022/2/14 10:51
 * @Created by zhq
 */
public class 用栈实现队列_232 {
}


//stack实现01
class MyQueue {
    private Stack<Integer> stack1;
    private Stack<Integer> stack2;
    private int size;

    public MyQueue() {
        stack1 = new Stack<>();
        stack2 = new Stack();
    }

    public void push(int x) {
        stack1.push(x);
        size++;
    }

    //出队
    public int pop() {
        //如果栈2为null，则将栈1中的所有元素压入栈2
        if (stack2.isEmpty()) {
            while (!stack1.isEmpty())
                stack2.push(stack1.pop());
        }
        //栈2出栈，即出队
        Integer pop = stack2.pop();
        size--;
        return pop;
    }

    public int peek() {
        //队列内没有元素，返回0
        if (size == 0) return 0;
        //取栈2内的元素
        if (stack2.isEmpty()) {
            while (!stack1.isEmpty())
                stack2.push(stack1.pop());
        }
        return stack2.peek();
    }

    public boolean empty() {
        return size == 0;
    }
}

//LinkedList实现
class MyQueue02 {
    private Deque<Integer> stack1;
    private Deque<Integer> stack2;
    private int size;

    public MyQueue02() {
        stack1 = new LinkedList();
        stack2 = new LinkedList();
    }

    public void push(int x) {
        stack1.push(x);
        size++;
    }

    //出队
    public int pop() {
        //如果栈2为null，则将栈1中的所有元素压入栈2
        if (stack2.isEmpty()) {
            while (!stack1.isEmpty())
                stack2.push(stack1.pop());
        }
        //栈2出栈，即出队
        Integer pop = stack2.pop();
        size--;
        return pop;
    }

    public int peek() {
        //队列内没有元素，返回0
        if (size == 0) return 0;
        //取栈2内的元素
        if (stack2.isEmpty()) {
            while (!stack1.isEmpty())
                stack2.push(stack1.pop());
        }
        return stack2.peek();
    }

    public boolean empty() {
        return size == 0;
    }
}
