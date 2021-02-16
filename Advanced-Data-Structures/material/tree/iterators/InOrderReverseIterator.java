package material.tree.iterators;

import material.Position;
import material.tree.binarytree.BinaryTree;

import java.util.*;

public class InOrderReverseIterator<E> implements Iterator<Position<E>> {

    private Queue<Position<E>> q;
    private BinaryTree<E> bt;
    private Set<Position<E>> s;

    public InOrderReverseIterator (BinaryTree<E> bt) {
        this.q = new LinkedList<>();
        this.bt = bt;
        this.s = new HashSet<>();
        if (!this.bt.isEmpty()) this.q.add(this.bt.root());
    }

    @Override
    public boolean hasNext() { return !this.q.isEmpty(); }

    @Override
    public Position<E> next() {
        Position<E> c = this.q.poll();
        if (this.s.contains(c)) return c;
        else {
            Queue<Position<E>> q2 = new LinkedList<>();
            if (this.bt.hasRight(c)) q2.add(this.bt.right(c));
            q2.add(c);
            if (this.bt.hasLeft(c)) q2.add(this.bt.left(c));
            q2.addAll(this.q);
            this.q = q2;
            this.s.add(c);
            return next();
        }
    }
}