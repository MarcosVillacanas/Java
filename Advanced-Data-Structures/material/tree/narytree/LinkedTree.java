package material.tree.narytree;

import material.Position;
import material.tree.iterators.BFSIterator;

import java.util.*;

public class LinkedTree<E> implements NAryTree<E> {

    private static class LTNode<T> implements Position<T> {

        T element;
        LTNode<T> parent;
        List<LTNode<T>> children;
        LinkedTree<T> myTree;

        public LTNode(T element, LTNode<T> parent, List<LTNode<T>> children, LinkedTree<T> myTree) {
            this.element = element;
            this.parent = parent;
            this.children = children;
            this.myTree = myTree;
        }

        @Override
        public T getElement() { return element; }

        public void setElement(T element) { this.element = element; }

        public LTNode<T> getParent() { return parent; }

        public void setParent(LTNode<T> parent) { this.parent = parent; }

        public List<LTNode<T>> getChildren() { return children; }

        public void setChildren(List<LTNode<T>> children) { this.children = children; }

        public LinkedTree<T> getMyTree() { return myTree; }

        public void setMyTree(LinkedTree<T> myTree) { this.myTree = myTree; }
    }

    LTNode<E> root;
    int size;

    public LinkedTree () {
        this.root = null;
        this.size = 0;
    }

    private LTNode<E> checkPosition (Position<E> p) throws RuntimeException {
        if (!(p instanceof LTNode)) throw new RuntimeException("Invalid position");
        LTNode<E> n = (LTNode<E>) p;
        if (n.getMyTree() != this) throw new RuntimeException("Position not corresponding to this tree");
        return n;
    }

    @Override
    public Position<E> root() throws RuntimeException {
        if (this.isEmpty()) throw new RuntimeException("Empty tree");
        return this.root;
    }

    @Override
    public Position<E> parent(Position<E> p) throws RuntimeException {
        LTNode<E> n = this.checkPosition(p);
        if (this.isRoot(n)) throw new RuntimeException("No parent");
        return n.getParent();
    }

    @Override
    public Position<E> addRoot(E element) throws RuntimeException {
        if (!this.isEmpty()) throw new RuntimeException("Root already exists");
        this.root = new LTNode<>(element, null, new LinkedList<>(), this);
        this.size++;
        return this.root;
    }

    @Override
    public Iterable<? extends Position<E>> children(Position<E> p) {
        LTNode<E> n = this.checkPosition(p);
        return n.getChildren();
    }

    @Override
    public boolean isEmpty() {
        return this.root == null;
    }

    @Override
    public boolean isInternal(Position<E> p) {
        return !this.isLeaf(p);
    }

    @Override
    public boolean isLeaf(Position<E> p) {
        LTNode<E> n = this.checkPosition(p);
        return n.getChildren().isEmpty();
    }

    @Override
    public boolean isRoot(Position<E> p) {
        LTNode<E> n = this.checkPosition(p);
        return n.getParent() == null;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public int level() {
        Map<Position<E>, Integer> levels = new HashMap<>();
        Queue<Position<E>> q = new LinkedList<>();
        int maxLevel = 0;
        if (!this.isEmpty()) {
            levels.put(this.root, 1);
            this.root.getChildren().forEach(q::add);
        }
        while (!q.isEmpty()) {
            Position<E> c = q.poll();
            int level = levels.get(c) + 1;
            maxLevel = Math.max(level, maxLevel);
            levels.put(c, level);
            this.children(c).forEach(q::add);
        }
        return maxLevel;
    }

    @Override
    public Iterator<Position<E>> iterator() {
        return new BFSIterator<>(this);
    }

    @Override
    public Iterator<Position<E>> iterator(Position<E> p) {
        return new BFSIterator<>(this, p);
    }

    @Override
    public E replace(E element, Position<E> p) {
        LTNode<E> n = this.checkPosition(p);
        E oldElement = n.getElement();
        n.setElement(element);
        return oldElement;
    }

    @Override
    public void swapElements(Position<E> p1, Position<E> p2) {
        LTNode<E> n1 = this.checkPosition(p1);
        LTNode<E> n2 = this.checkPosition(p2);
        LTNode<E> n1Parent = n1.getParent();

        n1.getParent().getChildren().remove(n1);
        n1.getParent().getChildren().add(n2);
        n1.setParent(n2.getParent());
        n2.getParent().getChildren().remove(n2);
        n2.getParent().getChildren().add(n1);
        n2.setParent(n1Parent);
    }

    @Override
    public void moveSubtree(Position<E> pOrig, Position<E> pDest) throws RuntimeException {
        LTNode<E> n1 = this.checkPosition(pOrig);
        LTNode<E> n2 = this.checkPosition(pDest);

        if (this.isRoot(n1)) throw new RuntimeException("Root cannot be moved");
        else if (n1.equals(n2)) throw new RuntimeException("The nodes are the same");
        else if (this.isAncestor(n1, n2)) throw new RuntimeException("Origin cannot be destination's ancestor");

        n1.getParent().getChildren().remove(n1);
        n2.getChildren().add(n1);
        n1.setParent(n2);
    }

    @Override
    public Position<E> add(E element, Position<E> parent) {
        LTNode<E> p = this.checkPosition(parent);
        LTNode<E> n = new LTNode<>(element, p, new LinkedList<>(), this);
        p.getChildren().add(n);
        return n;
    }

    @Override
    public E remove(Position<E> p) {
        LTNode<E> n = this.checkPosition(p);
        if (!this.isRoot(n)) n.getParent().getChildren().remove(n);
        Queue<Position<E>> q = new LinkedList<>();
        q.add(n);
        while (!q.isEmpty()) {
            Position<E> c = q.poll();
            this.children(c).forEach(q::add);
            this.checkPosition(c).setMyTree(null);
            this.size--;
        }
        return n.getElement();
    }

    private void copyTree (LinkedTree<E> origin, Position<E> originP, LinkedTree<E> dest, Position<E> destP) {

        Queue<Position<E>> origins = new LinkedList<>();
        Queue<Position<E>> destinations = new LinkedList<>();
        origins.add(originP); destinations.add(destP);

        while (!origins.isEmpty()) {
            Position<E> originC = origins.poll();
            Position<E> destC = destinations.poll();

            for (Position<E> c : origin.children(originC)) {
                destinations.add(dest.add(c.getElement(), destC));
                origins.add(c);
            }
        }
    }

    private boolean isAncestor(LTNode<E> nOrig, LTNode<E> nDest) {
        Position<E> c = nOrig;
        while (!this.isRoot(c) && !c.equals(nDest)) {
            c = this.parent(c);
        }
        return c.equals(nDest);
    }

    public boolean equals (LinkedTree<E> t2) {
        if (this.size() != t2.size()) return false;
        return this.equals(this.root(), t2.root(), t2);
    }

    private boolean equals(Position<E> p1, Position<E> p2, LinkedTree<E> t2) {
        if (p1.getElement().equals(p2.getElement())) {
            List<Position<E>> t1Children = new LinkedList<>();
            List<Position<E>> t2Children = new LinkedList<>();
            this.children(p1).forEach(t1Children::add);
            t2.children(p2).forEach(t2Children::add);
            if (t1Children.size() == t2Children.size()) {
                if (t1Children.containsAll(t2Children) && t2Children.containsAll(t1Children)) {
                    for (Position<E> c : t1Children) {
                        if (!equals(c, t2Children.get(this.indexElement(t2Children, c)), t2)) return false;
                    }
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    private int indexElement(List<Position<E>> l, Position<E> p) {
        for (int i = 0; i < l.size(); i++) {
            if (l.get(i).getElement().equals(p.getElement())) return i;
        }
        return -1;
    }

}
