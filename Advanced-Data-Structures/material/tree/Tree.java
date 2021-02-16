package material.tree;

import material.Position;
import java.util.Iterator;

public interface Tree<E> extends Iterable<Position<E>> {

    Position<E> root() throws RuntimeException;

    Position<E> parent(Position<E> p) throws RuntimeException;

    Position<E> addRoot(E element) throws RuntimeException;

    Iterable<? extends Position<E>> children(Position<E> p);

    boolean isEmpty();

    boolean isInternal(Position<E> p);

    boolean isLeaf(Position<E> p);

    boolean isRoot(Position<E> p);

    int size();

    int level();

    Iterator<Position<E>> iterator();

    Iterator<Position<E>> iterator(Position<E> p);

}
