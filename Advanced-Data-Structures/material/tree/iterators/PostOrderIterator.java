package material.tree.iterators;

import javafx.geometry.Pos;
import material.Position;
import material.tree.Tree;

import java.util.*;

public class PostOrderIterator<E> implements Iterator<Position<E>> {

    private Queue<Position<E>> q;
    private Tree<E> t;
    private Set<Position<E>> s;

    public PostOrderIterator (Tree<E> t) {
        this.q = new LinkedList<>();
        this.t = t;
        this.s = new HashSet<>();
        if (!this.t.isEmpty()) this.q.add(this.t.root());
    }

    public PostOrderIterator (Tree<E> t, Position<E> p) {
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
            this.t.children(c).forEach(q2::add);
            q2.add(c);
            q2.addAll(this.q);
            this.q = q2;
            this.s.add(c);
            return next();
        }
    }
}
