package data_struct.b栈和队列02.queue;

/**
 * @Classname Queue
 * @Description TODO
 * @Date 2021/12/27 22:40
 * @Created by zhq
 */
public interface Queue<E> {
    //入队
    void enqueue(E e);

    //出队
    E dequeue();

    //获取队首元素
    E getFront();

    //获取队列元素数量
    int getSize();

    //判断队列元素数量是否为0
    boolean isEmpty();
}
