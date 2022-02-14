package likou.z_suanfa_miji.队列和栈;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

/**
 * @Classname 用队列实现栈_225
 * @Description TODO
 * @Date 2022/2/14 11:45
 * @Created by zhq
 */
public class 用队列实现栈_225 {
    public static void main(String[] args) {
        MyStack01 myStack = new MyStack01();
        myStack.push(1);
        myStack.push(2);
        System.out.println(myStack.top());
        System.out.println(myStack.pop());
    }
}


//方式一：保持一个队列有元素，一个队列为null。添加元素添加到有元素的队列，删除元素的时候进行队列元素转移，最后一个元素不发生转移。
class MyStack01 {
    private Queue<Integer> deque1;
    private Queue<Integer> deque2;
    private int size;

    public MyStack01() {
        deque1 = new ArrayDeque();
        deque2 = new ArrayDeque();
    }

    //队列中添加元素，往有元素的队列中添加元素
    public void push(int x) {
        boolean push = deque1.isEmpty() ? deque2.offer(x) : deque1.offer(x);
        size++;
    }

    //出队列  trans：将有元素的队列中的元素转移到无元素的队列中。最后一个元素不进行转入。
    public int pop() {
        int top;
        if (deque1.isEmpty()) {
            top = trans(deque1, deque2);
            size--;
            return top;
        } else {
            top = trans(deque2, deque1);
            size--;
            return top;
        }
    }

    //调用trans：因为最后一个元素没有转入，因此需要重新添加入队。
    public int top() {
        int top;
        if (deque1.isEmpty()) {
            top = trans(deque1, deque2);
            deque1.offer(top);
        } else {
            top = trans(deque2, deque1);
            deque2.offer(top);
        }
        return top;
    }


    //trans：将有元素的队列中的元素转移到无元素的队列中。最后一个元素不进行转入。
    public int trans(Queue<Integer> emptyQueue, Queue<Integer> queue) {
        while (!queue.isEmpty()) {
            Integer poll = queue.poll();
            if (queue.peek() == null)
                return poll;
            emptyQueue.offer(poll);
        }
        return 0;
    }

    public boolean empty() {
        return size == 0;
    }
}

//方式二：一个消费队列，一个缓冲序列。每次往消费队列中添加元素的时候，将元素放入到缓冲队列。（添加元素的时候，如果缓冲队列有元素，将缓冲队列的元素放入消费队列。） 保证消费队列永远为null
class MyStack02 {
    //消费队列
    private Queue<Integer> deque1;
    //缓冲队列
    private Queue<Integer> deque2;
    private int size;

    public MyStack02() {
        deque1 = new ArrayDeque();
        deque2 = new ArrayDeque();
    }

    //消费队列放入元素，将缓冲列的元素放入消费队列。
    public void push(int x) {
        deque1.offer(x);
        size++;
        while (!deque2.isEmpty()) {
            deque1.offer(deque2.poll());
        }
        //交换  保证消费队列一致为null
        Queue<Integer> temp = deque1;
        deque1 = deque2;
        deque2 = temp;
    }

    public int pop() {
        size--;
        return deque2.isEmpty() ? 0 : deque2.poll();
    }

    public int top() {
        return deque2.isEmpty() ? 0 : deque2.peek();
    }


    public boolean empty() {
        return size == 0;
    }
}
