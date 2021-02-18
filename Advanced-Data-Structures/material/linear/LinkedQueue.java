package material.linear;

import material.Position;

public class LinkedQueue<E> implements Queue<E> {

    private class Node<T> implements Position<T> {

        T elem;
        Node<T> next;
        LinkedQueue<T> myQueue;

        public Node(T elem, Node<T> next, LinkedQueue<T> myQueue) {
            this.elem = elem;
            this.next = next;
            this.myQueue = myQueue;
        }

        @Override
        public T getElement() { return elem; }

        public void setElem(T elem) { this.elem = elem; }

        public Node<T> getNext() { return next; }

        public void setNext(Node<T> next) { this.next = next; }

        public LinkedQueue<T> getMyQueue() { return myQueue; }

        public void setMyQueue(LinkedQueue<T> myQueue) { this.myQueue = myQueue; }
    }

    Node<E> front;
    Node<E> back;
    int size;

    @Override
    public int size() { return this.size; }

    @Override
    public boolean isEmpty() { return this.size == 0; }

    @Override
    public E front() throws RuntimeException {
        if (this.isEmpty()) throw new RuntimeException("Queue is empty");
        return this.front.getElement();
    }

    @Override
    public void enqueue(E element) {
        Node<E> n = new Node<>(element, null, this);
        if (this.isEmpty()) this.front = n;
        else this.back.setNext(n);
        this.back = n;
        this.size++;
    }

    @Override
    public E dequeue() throws RuntimeException {
        if (this.isEmpty()) throw new RuntimeException("Queue is empty");
        E elem = this.front.getElement();
        this.front = this.front.getNext();
        this.size--;
        return elem;
    }
}
