package material.tree.binarytree;

import material.Position;

import java.util.Map;

public class QuickBinaryTree<E> extends LinkedBinaryTree<E> {

    private Map<E, Position<E>> map;

    @Override
    public Position<E> addRoot (E element) {
        Position<E> p = super.addRoot(element);
        this.map.put(element, p);
        return p;
    }

    @Override
    public Position<E> insertLeft(Position<E> p, E e) throws RuntimeException {
        Position<E> l = super.insertLeft(p, e);
        this.map.put(e, l);
        return l;
    }

    @Override
    public Position<E> insertRight(Position<E> p, E e) throws RuntimeException {
        Position<E> r = super.insertRight(p, e);
        this.map.put(e, r);
        return r;
    }

    @Override
    public E remove(Position<E> p) throws RuntimeException {
        E r = super.remove(p);
        this.map.remove(r);
        return r;
    }

    public Position<E> search (E element) throws RuntimeException {
        if (this.map.containsKey(element)) return this.map.get(element);
        throw new RuntimeException("Not stored key");
    }
}
