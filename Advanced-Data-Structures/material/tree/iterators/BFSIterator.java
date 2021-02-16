package material.tree.iterators;

import material.Position;
import material.tree.Tree;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class BFSIterator<E> implements Iterator<Position<E>> {

    private Queue<Position<E>> q;
    private Tree<E> t;

    public BFSIterator (Tree<E> t) {
        this.q = new LinkedList<>();
        this.t = t;
        if (!this.t.isEmpty()) this.q.add(this.t.root());
    }

    public BFSIterator (Tree<E> t, Position<E> p) {
        this.q = new LinkedList<>();
        this.t = t;
        if (!this.t.isEmpty()) this.q.add(p);
    }

    @Override
    public boolean hasNext() {
        return !this.q.isEmpty();
    }

    @Override
    public Position<E> next() {
        Position<E> c = this.q.poll();
        this.t.children(c).forEach(this.q::add);
        return c;
    }
}