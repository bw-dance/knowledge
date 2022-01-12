package data_struct.stack_queue_02.queue;

/**
 * @Classname ArrayQueue
 * @Description TODO
 * @Date 2021/12/27 22:42
 * @Created by zhq
 */
public class LoopQueue<E> implements Queue<E> {
    private E[] data;
    private int front;
    private int tail;

    public LoopQueue() {
        this(10);
    }

    public LoopQueue(int capacity) {
        //多添加一个位置，用于判满处理
        this.data = (E[]) new Object[capacity + 1];
        front = 0;
        tail = 0;
    }

    //获取数组容量
    public int getCapacity() {
        //有一个单位被我们有意识浪费
        return data.length - 1;
    }

    //入队
    @Override
    public void enqueue(E e) {
        //1.判断队列是否已满
        if ((tail + 1) % data.length == front)
            resize(getCapacity() * 2);
        //2. 元素入队
        data[tail] = e;
        //3. 尾指针移动。 假如当前tail为数组的最后一个元素，再次添加一个元素时，如果头索引没有元素时，即添加到头元素位置。即%data.length
        tail = (tail + 1) % data.length;
    }

    //出队
    @Override
    public E dequeue() {
        //1.判断队列是否为null
        if (isEmpty())
            throw new IllegalArgumentException("queue is empty");
        //2.获取收位置元素
        E res = data[front];
        //3. 移除data[front]位置的元素引用，释放内存空间
        data[front] = null;
        //4. front位置+1
        front = (front + 1) % data.length;
        if ((tail + data.length - front) % data.length <= getCapacity() / 4)
            resize(getCapacity() / 2);
        return res;
    }

    //扩容/缩容队列
    private void resize(int capacity) {
        //浪费一个元素空间
        E[] newData = (E[]) new Object[capacity + 1];
        for (int i = 0; i < (tail + data.length - front) % data.length; i++) {
            newData[i] = data[(front + i) % data.length];
        }
        data = newData;
        //扩容后，元素重新排列
        tail = (tail + data.length - front) % data.length;
        front = 0;
    }


    //获取头元素
    @Override
    public E getFront() {
        return data[front];
    }

    //获取队列元素数量
    @Override
    public int getSize() {
        return (tail + data.length - front) % data.length;
    }

    //判断队列是否为空
    @Override
    public boolean isEmpty() {
        return front == tail;
    }

    //toString 1
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append(String.format("LoopQueue: size = %s , capacity = %s \n", (tail + data.length - front) % data.length, getCapacity()));
        res.append("front[");
        for (int i = 0; i < (tail + data.length - front) % data.length; i++) {
            res.append(data[(front + i) % data.length]);
            if (i != (tail + data.length - front) % data.length - 1) {
                res.append(",");
            }
        }
        res.append("] tail");
        return res.toString();
    }


    public String toStringTwo() {
        StringBuilder res = new StringBuilder();
        res.append(String.format("LoopQueue: size = %d , capacity = %d"), (tail + data.length - front) % data.length, getCapacity());
        res.append("front[");
        for (int i = front; i != tail; i = (i + 1) % data.length) {
            res.append(data[front]);
            if ((i + 1) % data.length != tail) {
                res.append(",");
            }
        }
        res.append("] tail");
        return res.toString();
    }
}
