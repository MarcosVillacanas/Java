package material.tree.binarytree;

import material.Position;
import material.tree.Tree;

public interface BinaryTree<E> extends Tree<E> {

    boolean hasLeft(Position<E> p);

    boolean hasRight(Position<E> p);

    boolean isComplete();

    Position<E> insertLeft(Position<E> p, E element) throws RuntimeException;

    Position<E> insertRight(Position<E> p, E element) throws RuntimeException;

    Position<E> left(Position<E> p) throws RuntimeException;

    Position<E> right(Position<E> p) throws RuntimeException;

    Position<E> sibling(Position<E> p) throws RuntimeException;

    E replace (Position<E> p, E element);

    E remove (Position<E> p) throws RuntimeException;

    void swap (Position<E> p1, Position<E> p2);

    void attachLeft(Position<E> p, BinaryTree<E> tree) throws RuntimeException;

    void attachRight(Position<E> p, BinaryTree<E> tree) throws RuntimeException;

    // Removes the subtree of v from this tree a creates a new tree with it.
    BinaryTree<E> subTree (Position<E> p);
}
