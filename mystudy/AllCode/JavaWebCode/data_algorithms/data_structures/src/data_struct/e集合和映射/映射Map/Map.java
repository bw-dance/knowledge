package data_struct.e集合和映射.映射Map;

/**
 * @Classname Map
 * @Description TODO
 * @Date 2022/2/8 21:12
 * @Created by zhq
 */
public interface Map<K, V> {
    void add(K key, V value);

    V remove(K key);

    boolean contains(K key);

    V get(K key);

    void set(K key, V newValue);

    int getSize();

    boolean isEmpty();
}
