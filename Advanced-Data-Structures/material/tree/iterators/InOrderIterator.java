package material.tree.iterators;

import material.Position;
import material.tree.Tree;
import material.tree.binarytree.BinaryTree;

import java.util.*;

public class InOrderIterator<E> implements Iterator<Position<E>> {

    private Queue<Position<E>> q;
    private BinaryTree<E> t;
    private Set<Position<E>> s;

    public InOrderIterator (BinaryTree<E> t) {
        this.q = new LinkedList<>();
        this.t = t;
        this.s = new HashSet<>();
        if (!this.t.isEmpty()) this.q.add(this.t.root());
    }

    public InOrderIterator (BinaryTree<E> t, Position<E> p) {
        this.q = new LinkedList<>();
        this.t = t;
        this.s = new HashSet<>();
        if (!this.t.isEmpty()) this.q.add(p);
    }

    @Override
    public boolean hasNext() {
        return !this.q.isEmpty();
    }

    @Override
    public Position<E> next() {
        Position<E> c = this.q.poll();
        if (this.s.contains(c)) return c;
        else {
            Queue<Position<E>> q2 = new LinkedList<>();
            if (this.t.hasLeft(c)) q2.add(this.t.left(c));
            q2.add(c);
            if (this.t.hasRight(c)) q2.add(this.t.right(c));
            q2.addAll(this.q);
            this.q = q2;
            this.s.add(c);
            return next();
        }
    }
}
