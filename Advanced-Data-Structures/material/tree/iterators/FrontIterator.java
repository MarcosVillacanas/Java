package material.tree.iterators;

import material.Position;
import material.tree.Tree;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class FrontIterator<E> implements Iterator<Position<E>> {

    private Queue<Position<E>> q;
    private Tree<E> t;
    private Position<E> next;

    public FrontIterator (Tree<E> t) {
        this.q = new LinkedList<>();
        this.t = t;
        if (!this.t.isEmpty()) {
            this.q.add(this.t.root());
            this.next = this.getNext();
        }
    }

    public FrontIterator (Tree<E> t, Position<E> p) {
        this.q = new LinkedList<>();
        this.t = t;
        if (!this.t.isEmpty()) {
            this.q.add(p);
            this.next = this.getNext();
        }
    }

    private Position<E> getNext() {
        if (this.q.isEmpty()) return null;
        Position<E> c = this.q.poll();
        if (this.t.isLeaf(c)) return c;
        else {
            this.t.children(c).forEach(this.q::add);
            return getNext();
        }
    }

    @Override
    public boolean hasNext() {
        return this.next != null;
    }

    @Override
    public Position<E> next() {
        Position<E> toReturn = this.next;
        this.next = this.getNext();
        return toReturn;
    }
}