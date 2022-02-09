package data_struct.b栈和队列02.queue;

/**
 * @Classname ArrayQueue
 * @Description TODO
 * @Date 2021/12/27 22:42
 * @Created by zhq
 */
public class ArrayQueue<E> implements Queue<E> {
    private Array<E> array;

    public ArrayQueue() {
        array = new Array<>();
    }

    public ArrayQueue(int capacity) {
        array = new Array<>(capacity);
    }

    @Override
    public void enqueue(E e) {
        array.addLast(e);
    }

    @Override
    public E dequeue() {
        return array.removeFirst();
    }

    @Override
    public E getFront() {
        return array.get(0);
    }

    @Override
    public int getSize() {
        return array.getSize();
    }

    @Override
    public boolean isEmpty() {
        return array.isEmpty();
    }

    //重写toString方法
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("Queue:");
        res.append("front[");
        for (int i = 0; i < array.getSize(); i++) {
            res.append(array.get(i));
            if (i!=array.getSize()-1){
                res.append(",");
            }
        }
        res.append("] tail");
        return res.toString();
    }
}
