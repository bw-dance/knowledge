package data_struct.f堆和优先队列.base;

import data_struct.b栈和队列02.queue.Queue;

public class PriorityQueue<E extends Comparable<E>> implements Queue<E> {
    private MaxHeap maxHeap ;

    public PriorityQueue() {
        maxHeap = new MaxHeap();
    }

    @Override
    public void enqueue(E e) {
        maxHeap.add(e);
    }

    @Override
    public E dequeue() {
        return (E) maxHeap.extractMax();
    }

    @Override
    public E getFront() {
        return (E) maxHeap.findMax();
    }

    @Override
    public int getSize() {
        return maxHeap.size();
    }

    @Override
    public boolean isEmpty() {
        return maxHeap.isEmpty();
    }
}
