package data_struct.stack_queue_02.stack;

/**
 * @Classname Stack
 * @Description TODO
 * @Date 2021/12/27 12:53
 * @Created by zhq
 */

//	栈中元素的类型为E
public interface Stack<E> {
    int getSize();     //栈中元素数量

    boolean isEmpty(); //判断栈是否为空

    void push(E e);    //往栈中添加元素

    E pop();           //出栈

    E peek();          //查看栈顶的元素
}


