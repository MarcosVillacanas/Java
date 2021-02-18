package material.linear;

import material.Position;

public interface List<E> {

    int size ();

    boolean isEmpty ();

    Position<E> first ();

    Position<E> last ();

    Position<E> next (Position<E> p) throws RuntimeException;

    Position<E> prev (Position<E> p) throws RuntimeException;

    Position<E> addFirst (E e);

    Position<E> addLast (E e);

    Position<E> addAfter (Position<E> p, E e) throws RuntimeException;

    Position<E> addBefore (Position<E> p, E e) throws RuntimeException;

    E remove (Position<E> p) throws RuntimeException;

    // Replaces the element at the given position with the new element; O(1) time
    E set(Position<E> p, E e) throws RuntimeException;
}
