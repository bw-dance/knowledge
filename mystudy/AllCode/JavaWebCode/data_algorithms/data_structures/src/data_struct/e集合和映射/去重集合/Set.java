package data_struct.e集合和映射.去重集合;

/**
 * @Classname Set
 * @Description TODO
 * @Date 2022/1/27 17:00
 * @Created by zhq
 */
public interface Set<E> {
    void add(E e);

    void remove(E e);

    boolean contains(E e);

    int getSize();

    boolean isEmpty();
}
