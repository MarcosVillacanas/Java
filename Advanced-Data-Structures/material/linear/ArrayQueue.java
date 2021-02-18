package material.linear;

import material.Position;

public class ArrayQueue<E> implements Queue<E> {

    private E[] queue;
    private int front, back, size, maxSize;

    public ArrayQueue() {
        this.maxSize = 16;
        this.queue = (E[]) new Object[this.maxSize];
    }

    @Override
    public int size() { return this.size; }

    @Override
    public boolean isEmpty() { return this.size == 0; }

    @Override
    public E front() throws RuntimeException {
        if (this.isEmpty()) throw new RuntimeException("Queue is empty");
        return this.queue[front];
    }

    @Override
    public void enqueue(E element) {
        if (this.size >= this.maxSize) this.resize();
        if (!this.isEmpty()) this.back = (this.back + 1) % this.maxSize;
        this.queue[this.back] = element;
        this.size++;
    }

    private void resize() {
        E[] newQueue = (E[]) new Object[this.maxSize * 2];
        System.arraycopy(this.queue, front, newQueue, 0, this.maxSize - front);
        System.arraycopy(this.queue, 0, newQueue, this.maxSize - front, front);
        this.front = 0;
        this.back = this.maxSize - 1;
        this.maxSize *= 2;
        this.queue = newQueue;
    }

    @Override
    public E dequeue() throws RuntimeException {
        if (this.isEmpty()) throw new RuntimeException("Queue is empty");
        E toReturn = this.queue[this.front];
        this.size--;
        if (!this.isEmpty()) this.front = (this.front + 1) % this.maxSize;
        return toReturn;
    }
}
