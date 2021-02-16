package material.tree.narytree;

import material.Position;

import java.util.*;

public class FastLeafAccessTree<E> extends LinkedTree<E> {

    private Set<Position<E>> leaves;

    public FastLeafAccessTree () {
        super();
        this.leaves = new HashSet<>();
    }

    @Override
    public Position<E> addRoot(E element) throws RuntimeException {
        Position<E> p = super.addRoot(element);
        this.leaves.add(p);
        return p;
    }

    @Override
    public Position<E> add(E element, Position<E> parent) {
        Position<E> p = super.add(element, parent);
        this.leaves.remove(parent);
        this.leaves.add(p);
        return p;
    }

    @Override
    public E remove(Position<E> p) {
        if (!super.isRoot(p)) {
            Position<E> father = super.parent(p);
            List<Position<E>> children = new LinkedList<>();
            super.children(father).forEach(children::add);
            if (children.size() == 1) this.leaves.add(father);
        }
        this.leaves.remove(p);
        return super.remove(p);
    }

    public Iterable<Position<E>> getLeaves () { return this.leaves; }
}
