package data_struct.b栈和队列02.stack;


/**
 * @Classname ArrayStack
 * @Description TODO
 * @Date 2021/12/27 12:53
 * @Created by zhq
 */
public class ArrayStack<E> implements Stack<E> {
    private Array<E> array = null;

    public ArrayStack() {
        this.array = new Array<E>();
    }

    public ArrayStack(int capacity) {
        this.array = new Array<E>(capacity);
    }


    @Override
    public int getSize() {
        return array.getSize();
    }

    @Override
    public boolean isEmpty() {
        return array.isEmpty();
    }

    @Override
    public void push(E e) {
        array.addLast(e);
    }

    @Override
    public E pop() {
        return array.removeLast();
    }

    @Override
    public E peek() {
        return array.get(array.getSize() - 1);
    }

    //重写toString方法
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("Strack[");
        for (int a = 0; a < array.getSize(); a++) {
            res.append(array.get(a));
            res.append("、");
        }
        res.append("]");
        return res.toString();
    }

}
