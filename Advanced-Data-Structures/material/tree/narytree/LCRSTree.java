package material.tree.narytree;

import material.Position;
import material.tree.Tree;
import material.tree.iterators.BFSIterator;

import java.util.*;

public class LCRSTree<E> implements NAryTree<E> {

    private class LCRSNode<E> implements Position<E> {

        private E elem;
        private LCRSNode<E> lChild;
        private LCRSNode<E> rSibling;
        private LCRSNode<E> parent;
        private LCRSTree<E> myTree;

        public LCRSNode(E elem, LCRSNode<E> lChild, LCRSNode<E> rSibling, LCRSNode<E> p, LCRSTree<E> myTree) {
            this.elem = elem; this.lChild = lChild; this.rSibling = rSibling; this.parent = p; this.myTree = myTree;
        }

        @Override
        public E getElement() { return this.elem; }

        public LCRSNode<E> getLeftChild() { return this.lChild; }

        public LCRSNode<E> getRightSibling() { return this.rSibling; }

        public List<LCRSNode<E>> getChildren() {
            List<LCRSNode<E>> l = new ArrayList<>();
            LCRSNode<E> child = this.lChild;
            while (child != null) {
                l.add(child);
                child = child.rSibling;
            }
            return l;
        }

        public LCRSNode<E> getParent() { return this.parent; }

        public LCRSTree<E> getMyTree() { return this.myTree; }

        public void setElem(E elem) { this.elem = elem; }

        public void setlChild(LCRSNode<E> lChild) { this.lChild = lChild; }

        public void setrSibling(LCRSNode<E> rSibling) { this.rSibling = rSibling; }

        public void setParent(LCRSNode<E> parent) { this.parent = parent; }

        public void setMyTree(LCRSTree<E> myTree) { this.myTree = myTree; }
    }

    private LCRSNode<E> root;
    private int size;

    private LCRSNode<E> checkPosition (Position<E> p) throws RuntimeException {
        if (!(p instanceof LCRSNode)) {
            throw new RuntimeException("Invalid position");
        }
        LCRSNode<E> n = (LCRSNode<E>) p;
        if (n.getMyTree() != this) {
            throw new RuntimeException("The node is not from this tree");
        }
        return n;
    }

    @Override
    public Position<E> root() throws RuntimeException {
        if (this.size == 0) {
            throw new RuntimeException("The tree is empty");
        }
        return this.root;
    }

    @Override
    public Position<E> parent(Position<E> p) throws RuntimeException {
        LCRSNode<E> n = this.checkPosition(p);
        if (this.isRoot(n)) {
            throw new RuntimeException("The node has not parent");
        }
        return n.getParent();
    }

    @Override
    public Position<E> addRoot(E element) throws RuntimeException {
        if (this.size != 0) {
            throw new RuntimeException("The tree already has a root");
        }
        this.root = new LCRSNode<>(element, null, null, null, this);
        this.size = 1;
        return this.root;
    }

    @Override
    public Position<E> add(E element, Position<E> parent) {
        LCRSNode<E> np = this.checkPosition(parent);
        LCRSNode<E> n = new LCRSNode<>(element, null, null, np, this);
        LCRSNode<E> mySibling = np.getLeftChild();
        if (mySibling == null) {
            np.setlChild(n);
        }
        else {
            while (mySibling.getRightSibling() != null) {
                mySibling = mySibling.getRightSibling();
            }
            mySibling.setrSibling(n);
        }
        this.size++;
        return n;
    }

    @Override
    public Iterable<? extends Position<E>> children(Position<E> p) {
        LCRSNode<E> n = this.checkPosition(p);
        return n.getChildren();
    }

    @Override
    public boolean isEmpty() { return this.size == 0; }

    @Override
    public boolean isInternal(Position<E> p) { return !this.isLeaf(p); }

    @Override
    public boolean isLeaf(Position<E> p) {
        LCRSNode<E> n = this.checkPosition(p);
        return n.getLeftChild() == null;
    }

    @Override
    public boolean isRoot(Position<E> p) {
        LCRSNode<E> n = this.checkPosition(p);
        return (this.root.equals(n));
    }

    @Override
    public E remove(Position<E> p) {
        LCRSNode<E> n = this.checkPosition(p);
        if (!this.isRoot(n)) {
            if (n.getParent().getLeftChild().equals(n)) n.getParent().setlChild(n.getRightSibling());
            else {
                LCRSNode<E> descendant = n.getParent().getLeftChild();
                while (!descendant.getRightSibling().equals(n)) {
                    descendant = descendant.getRightSibling();
                }
                descendant.setrSibling(n.getRightSibling());
            }
        }
        Queue<LCRSNode<E>> q = new LinkedList<>();
        q.add(n);
        while (!q.isEmpty()) {
            LCRSNode<E> c = q.poll();
            this.size--;
            c.setMyTree(null);
            q.addAll(c.getChildren());
        }
        return n.getElement();
    }

    @Override
    public int size() { return this.size; }

    @Override
    public int level() {
        int level = 0;
        if (!this.isEmpty()) {
            Map<LCRSNode<E>, Integer> m = new HashMap<>();
            m.put(this.root, 1);
            Queue<LCRSNode<E>> q = new LinkedList<>(this.root.getChildren());
            while (!q.isEmpty()) {
                LCRSNode<E> c = q.poll();
                int currentLevel = m.get(c) + 1;
                level = Math.max(level, currentLevel);
                m.put(c, currentLevel);
                q.addAll(c.getChildren());
            }
        }
        return level;
    }

    @Override
    public Iterator<Position<E>> iterator() { return new BFSIterator<>(this); }

    @Override
    public Iterator<Position<E>> iterator(Position<E> p) { return new BFSIterator<>(this, p); }

    @Override
    public E replace(E element, Position<E> p) {
        LCRSNode<E> n = this.checkPosition(p);
        E oldElement = n.getElement();
        n.setElem(element);
        return oldElement;
    }

    @Override
    public void swapElements(Position<E> p1, Position<E> p2) {
        LCRSNode<E> n1 = this.checkPosition(p1);
        LCRSNode<E> n2 = this.checkPosition(p2);
        E n1Elem = n1.getElement();
        n1.setElem(n2.getElement());
        n2.setElem(n1Elem);
    }

    @Override
    public void moveSubtree(Position<E> pOrig, Position<E> pDest) throws RuntimeException {
        LCRSNode<E> nOrig = this.checkPosition(pOrig);
        LCRSNode<E> nDest = this.checkPosition(pDest);

        if (this.isRoot(nOrig)) throw new RuntimeException("Root node can't be moved");
        else if (nOrig.equals(nDest)) throw new RuntimeException("Both positions are the same");
        else if (this.isAncestor(nOrig, nDest)) throw new RuntimeException("Origin node is a dest node ancestor");
        else {

            if (nOrig.getParent().getLeftChild().equals(nOrig)) nOrig.getParent().setlChild(nOrig.getRightSibling());
            else {
                LCRSNode<E> descendant = nOrig.getParent().getLeftChild();
                while (!descendant.getRightSibling().equals(nOrig)) {
                    descendant = descendant.getRightSibling();
                }
                descendant.setrSibling(nOrig.getRightSibling());
            }

            if (nDest.getLeftChild() == null) nDest.setlChild(nOrig);
            else {
                LCRSNode<E> descendant = nDest.getLeftChild();
                while (descendant.getRightSibling() != null) {
                    descendant = descendant.getRightSibling();
                }
                descendant.setrSibling(nOrig);
            }
            nOrig.setParent(nDest);
        }
    }

    private boolean isAncestor(LCRSNode<E> nOrig, LCRSNode<E> nDest) {
        LCRSNode<E> nDestAncestor = nDest.getParent();
        while (nDestAncestor != null) {
            if (nDestAncestor.equals(nOrig)) return true;
            nDestAncestor = nDestAncestor.getParent();
        }
        return false;
    }
}
