package material.tree.binarytree;

import javafx.geometry.Pos;
import material.Position;
import material.tree.narytree.LinkedTree;
import material.tree.narytree.NAryTree;

import java.util.*;

public class BinaryTreeUtils<E> {

    private BinaryTree<E> tree;

    public BinaryTreeUtils (BinaryTree<E> bTree) { this.tree = bTree; }

    public BinaryTree<E> mirror() {
        BinaryTree<E> mirrored = new LinkedBinaryTree<>();
        this.mirror(this.tree.root(), this.tree, mirrored.addRoot(this.tree.root().getElement()), mirrored);
        return mirrored;
    }

    private void mirror(Position<E> pO, BinaryTree<E> tO, Position<E> pD, BinaryTree<E> tD) {
        Queue<Position<E>> qO = new LinkedList<>();
        Queue<Position<E>> qD = new LinkedList<>();
        qO.add(pO);
        qD.add(pD);
        while (!qO.isEmpty()) {
            Position<E> cO = qO.poll();
            Position<E> cD = qD.poll();
            if (tO.hasLeft(cO)) {
                qO.add(tO.left(cO));
                qD.add(tD.insertRight(cD, tO.left(cO).getElement()));
            }
            if (tO.hasRight(cO)) {
                qO.add(tO.right(cO));
                qD.add(tD.insertLeft(cD, tO.right(cO).getElement()));
            }
        }
    }

    public Iterable<? extends Integer> levelsIncomplete() {
        Map<Position<E>, Integer> m = new HashMap<>();
        Set<Integer> s = new HashSet<>();
        Queue<Position<E>> q = new LinkedList<>();

        q.add(this.tree.root());

        while (!q.isEmpty()) {
            Position<E> c = q.poll();
            int level = (this.tree.isRoot(c))? 1 : m.get(this.tree.parent(c)) + 1;
            if (!this.tree.hasLeft(c) && this.tree.hasRight(c)) {
                s.add(level);
            }
            this.tree.children(c).forEach(q::add);
            m.put(c, level);
        }
        return s;
    }

    public Iterable<? extends Integer> levelsComplete() {
        Map<Position<E>, Integer> m = new HashMap<>();
        Set<Integer> s = new HashSet<>();
        Queue<Position<E>> q = new LinkedList<>();

        q.add(this.tree.root());
        for (int i = 1; i < this.tree.level(); i++) { s.add(i); }

        while (!q.isEmpty()) {
            Position<E> c = q.poll();
            int level = (this.tree.isRoot(c))? 1 : m.get(this.tree.parent(c)) + 1;
            if (!this.tree.hasLeft(c) && this.tree.hasRight(c)) {
                s.remove(level);
            }
            this.tree.children(c).forEach(q::add);
            m.put(c, level);
        }
        return s;
    }

    public boolean contains (E e) {
        Queue<Position<E>> q = new LinkedList<>();
        q.add(this.tree.root());
        while (!q.isEmpty()) {
            Position<E> c = q.poll();
            if (c.getElement().equals(e)) return true;
            this.tree.children(c).forEach(q::add);
        }
        return false;
    }

    public int sumBinaryTreeLevels (Integer[] levels) {
        int sum = 0;
        Set<Integer> s = new HashSet<>(Arrays.asList(levels));
        Map<Position<E>, Integer> m = new HashMap<>();
        Queue<Position<E>> q = new LinkedList<>();
        q.add(this.tree.root());
        while (!q.isEmpty()) {
            Position<E> c = q.poll();
            int level = (this.tree.isRoot(c))? 1 : m.get(this.tree.parent(c)) + 1;
            if (s.contains(level)) sum += (Integer) c.getElement();
            m.put(c, level);
            this.tree.children(c).forEach(q::add);
        }
        return sum;
    }

    public boolean isOdd () {
        if (this.tree.isEmpty() || !this.tree.hasLeft(this.tree.root())) return false;
        Queue<Position<E>> q = new LinkedList<>();
        int leftDescendants = 0;
        q.add(this.tree.left(this.tree.root()));
        while (!q.isEmpty()) {
            Position<E> c = q.poll();
            leftDescendants++;
            this.tree.children(c).forEach(q::add);
        }
        return leftDescendants > (this.tree.size() / 2);
    }

    public int level (Position<E> p) {
        int level = 1;
        while (!this.tree.isRoot(p)) {
            p = this.tree.parent(p);
            level++;
        }
        return level;
    }

    public int diameter () {
        Iterator<Position<E>> ite = this.tree.iterator();
        Integer[] maxDiameter = {Integer.MIN_VALUE};
        ite.forEachRemaining(p -> {
            Iterator<Position<E>> ite2 = this.tree.iterator();
            ite2.forEachRemaining(p2 -> maxDiameter[0] = Math.max(maxDiameter[0], this.diameter(p, p2)));
        });
        return maxDiameter[0];
    }

    public int diameter (Position<E> n1, Position<E> n2) {
        Set<Position<E>> s = new HashSet<>();
        Map<Position<E>, Integer> m = new HashMap<>();
        int jumps = 0;
        while (!this.tree.isRoot(n1)) {
            m.put(n1, jumps++);
            n1 = this.tree.parent(n1);
            s.add(n1);
        }
        s.add(this.tree.root());
        m.put(this.tree.root(), jumps);
        jumps = 0;
        while (!s.contains(n2)) {
            jumps++;
            n2 = this.tree.parent(n2);
        }
        return jumps + m.get(n2);
    }

    public Iterable<Position<E>> findHalf (){
        int h = this.tree.level() / 2;

        Map<Position<E>, Integer> m = new HashMap<>();
        m.put(this.tree.root(), 1);

        Queue<Position<E>> q = new LinkedList<>();
        this.tree.children(this.tree.root()).forEach(q::add);

        List<Position<E>> sol = new LinkedList<>();
        if (1 <= h) sol.add(this.tree.root());

        while (!q.isEmpty()) {
            Position<E> c = q.poll();
            int level = m.get(this.tree.parent(c)) + 1;
            if (level <= h) {
                m.put(c, level);
                sol.add(c);
                this.tree.children(c).forEach(q::add);
            }
        }
        return sol;
    }

    public NAryTree<E> convertToNAryTree () {
        NAryTree<E> nt = new LinkedTree<>();
        if (!this.tree.isEmpty()) {
            Position<E> r = this.tree.root();
            if (this.tree.hasLeft(r) && this.tree.hasRight(r)) throw new RuntimeException("Impossible transformation");
            if (this.tree.hasLeft(r)) convertToNAryTree(nt, nt.addRoot(r.getElement()), this.tree.left(r));
        }
        return nt;
    }

    private void convertToNAryTree(NAryTree<E> nt, Position<E> ntPos, Position<E> btPos) {
        Position<E> current = nt.add(btPos.getElement(), ntPos);
        if (this.tree.hasLeft(btPos)) convertToNAryTree(nt, current, this.tree.left(btPos));
        if (this.tree.hasRight(btPos)) convertToNAryTree(nt, ntPos, this.tree.right(btPos));
    }

}
