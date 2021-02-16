package material.tree.narytree;

import material.Position;
import material.tree.binarytree.BinaryTree;
import material.tree.binarytree.LinkedBinaryTree;

import java.util.*;

public class NAryTreeUtils<E> {

    private NAryTree<E> tree;

    public NAryTreeUtils (NAryTree<E> t) { this.tree = t; }

    public void removeFrontier () { this.getLeaves().forEach(l -> this.tree.remove(l)); }

    private Iterable<Position<E>> getLeaves() {
        Queue<Position<E>> q = new LinkedList<>();
        List<Position<E>> l = new LinkedList<>();
        if (!this.tree.isEmpty()) q.add(this.tree.root());
        while (!q.isEmpty()) {
            Position<E> c = q.poll();
            if (this.tree.isLeaf(c)) l.add(c);
            else this.tree.children(c).forEach(q::add);
        }
        return l;
    }

    public BinaryTree<E> convertToBinaryTree () {
        BinaryTree<E> bt = new LinkedBinaryTree<>();
        convertToBinaryTree(this.tree.root(), bt.addRoot(this.tree.root().getElement()), bt);
        return bt;
    }

    private void convertToBinaryTree (Position<E> origin, Position<E> dest, BinaryTree<E> bt) {

        Position<E> current = null;
        for (Position<E> child : this.tree.children(origin)) {
            current = (current == null)
                    ? bt.insertLeft(dest, child.getElement())
                    : bt.insertRight(current, child.getElement());
            convertToBinaryTree(child, current, bt);
        }
    }
}
