package material.tree.binarytree;

import material.Position;
import material.tree.iterators.BFSIterator;
import material.tree.iterators.InOrderIterator;

import java.util.*;

public class ArrayBinaryTree<E> implements BinaryTree<E> {

    private static class ABTNode<E> implements Position<E> {

        E element;
        int pos, lChild, rChild, parent;

        public ABTNode(E element, int pos, int lChild, int rChild, int parent) {
            this.element = element; this.pos = pos; this.lChild = lChild;
            this.rChild = rChild; this.parent = parent;
        }

        @Override
        public E getElement() { return element; }

        public void setElement(E element) { this.element = element; }

        public int getPos() { return pos; }

        public void setPos(int pos) { this.pos = pos; }

        public int getlChild() { return lChild; }

        public void setlChild(int lChild) { this.lChild = lChild; }

        public int getrChild() { return rChild; }

        public void setrChild(int rChild) { this.rChild = rChild; }

        public int getParent() { return parent; }

        public void setParent(int parent) { this.parent = parent; }

    }

    ABTNode<E> [] tree;

    public ArrayBinaryTree() { this.tree = new ABTNode[64]; }

    private ABTNode<E> checkPosition (Position<E> p) throws RuntimeException {
        if (!(p instanceof ABTNode)) {
            throw new RuntimeException("Invalid position");
        }
        return (ABTNode<E>) p;
    }

    @Override
    public boolean hasLeft(Position<E> p) {
        ABTNode<E> n = this.checkPosition(p);
        return n.getlChild() != -1;
    }

    @Override
    public boolean hasRight(Position<E> p) {
        ABTNode<E> n = this.checkPosition(p);
        return n.getrChild() != -1;
    }

    @Override
    public boolean isComplete() {
        Iterator<Position<E>> it = this.iterator();
        while(it.hasNext()) {
            Position<E> p = it.next();
            if (!this.isLeaf(p) && !(this.hasLeft(p) && this.hasRight(p))) return false;
        }
        return true;
    }

    @Override
    public Position<E> insertLeft(Position<E> p, E element) throws RuntimeException {
        ABTNode<E> np = this.checkPosition(p);
        if (!this.hasLeft(np)) {
            int lChildPos = np.getPos() * 2;
            this.tree[lChildPos] = new ABTNode<>(element, lChildPos, -1, -1, np.getPos());
            np.setlChild(lChildPos);
            return this.tree[lChildPos];
        }
        throw new RuntimeException("Node already has a left child");
    }

    @Override
    public Position<E> insertRight(Position<E> p, E element) throws RuntimeException {
        ABTNode<E> np = this.checkPosition(p);
        if (!this.hasRight(np)) {
            int rChildPos = (np.getPos() * 2) + 1;
            this.tree[rChildPos] = new ABTNode<>(element, rChildPos, -1, -1, np.getPos());
            np.setrChild(rChildPos);
            return this.tree[rChildPos];
        }
        throw new RuntimeException("Node already has a right child");
    }

    @Override
    public Position<E> left(Position<E> p) throws RuntimeException {
        ABTNode<E> n = this.checkPosition(p);
        if (this.hasLeft(n)) {
            return this.tree[n.getlChild()];
        }
        throw new RuntimeException("No left child");
    }

    @Override
    public Position<E> right(Position<E> p) throws RuntimeException {
        ABTNode<E> n = this.checkPosition(p);
        if (this.hasRight(n)) {
            return this.tree[n.getrChild()];
        }
        throw new RuntimeException("No right child");
    }

    @Override
    public Position<E> sibling(Position<E> p) throws RuntimeException {
        ABTNode<E> n = this.checkPosition(p);
        if (this.isRoot(n) || !(this.hasLeft(this.tree[n.getParent()]) && this.hasRight(this.tree[n.getParent()]))) {
            throw new RuntimeException("No sibling");
        }
        return (this.tree[n.getParent()].getlChild() == n.getPos())?
                this.tree[this.tree[n.getParent()].getrChild()] : this.tree[this.tree[n.getParent()].getlChild()];
    }

    @Override
    public E replace(Position<E> p, E element) {
        ABTNode<E> n = this.checkPosition(p);
        E oldElem = n.getElement();
        n.setElement(element);
        return oldElem;
    }

    @Override
    public Position<E> root() throws RuntimeException {
        if (this.tree[1] != null) {
            return this.tree[1];
        }
        throw new RuntimeException("The tree is empty");
    }

    @Override
    public Position<E> parent(Position<E> p) throws RuntimeException {
        ABTNode<E> n = this.checkPosition(p);
        if (!this.isRoot(n)) {
            return this.tree[n.getParent()];
        }
        throw new RuntimeException("No parent");
    }

    @Override
    public Position<E> addRoot(E element) throws RuntimeException {
        if (this.isEmpty()) {
            this.tree[1] = new ABTNode<>(element, 1, -1, -1, -1);
            return this.tree[1];
        }
        throw new RuntimeException("Tree already has a root");
    }

    @Override
    public Iterable<? extends Position<E>> children(Position<E> p) {
        ABTNode<E> n = this.checkPosition(p);
        List<ABTNode<E>> l = new ArrayList<>();
        if (this.hasLeft(n)) {
            l.add(this.tree[n.getlChild()]);
        }
        if (this.hasRight(n)) {
            l.add(this.tree[n.getrChild()]);
        }
        return l;
    }

    @Override
    public boolean isEmpty() { return this.tree[1] == null; }

    @Override
    public boolean isInternal(Position<E> p) { return !this.isLeaf(p); }

    @Override
    public boolean isLeaf(Position<E> p) {
        ABTNode<E> n = this.checkPosition(p);
        return !(this.hasLeft(n) || this.hasRight(n));
    }

    @Override
    public boolean isRoot(Position<E> p) {
        ABTNode<E> n = this.checkPosition(p);
        return n.equals(this.tree[1]);
    }

    @Override
    public E remove(Position<E> p) throws RuntimeException {
        ABTNode<E> n = this.checkPosition(p);

        if (this.hasRight(n) && this.hasLeft(n)) throw new RuntimeException("Cannot remove node with two children");

        ABTNode<E> child = (this.hasLeft(n))? this.tree[n.lChild]: (this.hasRight(n))? this.tree[n.rChild]: null;

        Queue<Position<E>> q = new LinkedList<>();
        if (child != null) {
            child.setParent( n.getParent() );
            child.setPos( n.getPos() );
            this.tree[n.getPos()] = child;
            if (this.hasLeft(child)) {
                q.add(this.tree[child.getlChild()]);
                child.setlChild( child.getlChild() / 2 );
            }
            if (this.hasRight(child)){
                q.add(this.tree[child.getrChild()]);
                child.setrChild( ((child.getrChild() - 1) / 2) + 1 );
            }
        }
        else
            if (n.getPos() % 2 == 0) this.tree[n.getParent()].setlChild(-1);
            else this.tree[n.getParent()].setrChild(-1);

        while (!q.isEmpty()) {
            Position<E> c = q.poll();
            this.children(c).forEach(q::add);
            ABTNode<E> x = this.checkPosition(c);
            x.setParent( (x.getParent() % 2 == 0)? x.getParent() / 2 : ((x.getParent() - 1) / 2) + 1 );
            x.setPos( (x.getPos() % 2 == 0)? x.getPos() / 2 : ((x.getPos() - 1) / 2) + 1 );
            this.tree[x.getPos()] = x;
            if (this.hasLeft(x)) x.setlChild( x.getlChild() / 2 );
            if (this.hasRight(x)) x.setrChild( ((x.getrChild() - 1) / 2) + 1 );
        }

        return n.getElement();
    }

    @Override
    public int size() {
        int size = 0;
        Iterator<Position<E>> it = this.iterator();
        while (it.hasNext()) {
            Position<E> elem = it.next();
            size++;
        }
        return size;
    }

    @Override
    public int level() {
        int level = 0;
        if (!this.isEmpty()) {
            Map<ABTNode<E>, Integer> m = new HashMap<>();
            m.put(this.tree[1], 1);
            Queue<Position<E>> q = new LinkedList<>();
            this.children(this.tree[1]).forEach(q::add);
            while (!q.isEmpty()) {
                ABTNode<E> c = this.checkPosition(q.poll());
                int currentLevel = m.get(this.tree[c.getParent()]) + 1;
                m.put(c, currentLevel);
                level = Math.max(level, currentLevel);
                this.children(c).forEach(q::add);
            }
        }
        return level;
    }

    @Override
    public Iterator<Position<E>> iterator() { return new InOrderIterator<>(this); }

    @Override
    public Iterator<Position<E>> iterator(Position<E> p) { return new InOrderIterator<>(this, p); }

    @Override
    public void swap(Position<E> p1, Position<E> p2) {
        ABTNode<E> n1 = this.checkPosition(p1);
        ABTNode<E> n2 = this.checkPosition(p2);

        ABTNode<E> copyN1 = new ABTNode<>(n1.element, n1.pos, n1.lChild, n1.rChild, n1.parent);

        n1.parent = (n2.parent == n1.pos)? n2.pos: n2.parent;
        n1.lChild = (n2.lChild == n1.pos)? n2.pos: n2.lChild;
        n1.rChild = (n2.rChild == n1.pos)? n2.pos: n2.rChild;

        n2.parent = (copyN1.parent == n2.pos)? copyN1.pos: copyN1.parent;
        n2.lChild = (copyN1.lChild == n2.pos)? copyN1.pos: copyN1.lChild;
        n2.rChild = (copyN1.rChild == n2.pos)? copyN1.pos: copyN1.rChild;

        this.tree[n2.pos] = n1;
        n1.pos = n2.pos;
        this.tree[copyN1.pos] = n2;
        n2.pos = copyN1.pos;
    }

    @Override
    public void attachLeft(Position<E> p, BinaryTree<E> tree) throws RuntimeException {
        ABTNode<E> n = this.checkPosition(p);
        if (this.hasLeft(n)) {
            throw new RuntimeException("The node already has a left child");
        }
        this.copyTree(tree.root(), tree, this.insertLeft(n, tree.root().getElement()), this);
    }

    @Override
    public void attachRight(Position<E> p, BinaryTree<E> tree) throws RuntimeException {
        ABTNode<E> n = this.checkPosition(p);
        if (!this.hasRight(n)) {
            throw new RuntimeException("The node already has a right child");
        }
        this.copyTree(tree.root(), tree, this.insertRight(n, tree.root().getElement()), this);
    }

    @Override
    public BinaryTree<E> subTree(Position<E> p) {
        ABTNode<E> nO = this.checkPosition(p);
        ArrayBinaryTree<E> newTree = new ArrayBinaryTree<>();
        this.copyTree(nO, this, newTree.addRoot(nO.getElement()), newTree);

        if (!this.isRoot(nO))
            if (nO.getPos() % 2 == 0) this.tree[nO.getParent()].setlChild(-1);
            else this.tree[nO.getParent()].setrChild(-1);
        return newTree;
    }

    private void copyTree(Position<E> nO, BinaryTree<E> tO, Position<E> nD, BinaryTree<E> tD) {
        Queue<Position<E>> qO = new LinkedList<>();
        Queue<Position<E>> qD = new LinkedList<>();
        qO.add(nO);
        qD.add(nD);

        while (!qO.isEmpty()) {
            Position<E> cO = qO.poll();
            Position<E> cD = qD.poll();

            if (tO.hasLeft(cO)) {
                qO.add(tO.left(cO));
                qD.add(tD.insertLeft(cD, tO.left(cO).getElement()));
            }

            if (tO.hasRight(cO)) {
                qO.add(tO.right(cO));
                qD.add(tD.insertRight(cD, tO.right(cO).getElement()));
            }
        }
    }
}
