package material.linear;

public interface Queue<E> {

    int size ();

    boolean isEmpty ();

    E front ();

    void enqueue (E element);

    E dequeue ();
}
