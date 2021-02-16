package material.tree.narytree;

import javafx.geometry.Pos;
import material.Position;
import material.tree.Tree;

public interface NAryTree<E> extends Tree<E> {

    E replace (E element, Position<E> p);

    void swapElements (Position<E> p1, Position<E> p2);

    void moveSubtree (Position<E> pOrig, Position<E> pDest) throws RuntimeException;

    Position<E> add(E element, Position<E> parent);

    E remove(Position<E> p);

}
