package material.linear;

import material.Position;

public class DoubleLinkedList<E> implements List<E> {

    private class DNode<T> implements Position<T> {

        DNode<T> prev, next;
        T element;
        DoubleLinkedList<T> myList;

        public DNode(DNode<T> prev, DNode<T> next, T element, DoubleLinkedList<T> myList) {
            this.prev = prev;
            this.next = next;
            this.element = element;
            this.myList = myList;
        }

        @Override
        public T getElement() { return this.element; }

        public void setElement(T element) { this.element = element; }

        public DNode<T> getPrev() { return prev; }

        public void setPrev(DNode<T> prev) { this.prev = prev; }

        public DNode<T> getNext() { return next; }

        public void setNext(DNode<T> next) { this.next = next; }

        public DoubleLinkedList<T> getMyList() { return myList; }

        public void setMyList(DoubleLinkedList<T> myList) { this.myList = myList; }
    }

    protected int size; // Number of elements in the list
    protected DNode<E> header, trailer; // Special sentinels

    private DNode<E> checkPosition (Position<E> p) {
        if (!(p instanceof DNode)) throw new RuntimeException("Invalid position");
        DNode<E> n = (DNode<E>) p;
        if (n.getMyList() != this) throw new RuntimeException("Position not corresponding to this tree");
        return n;
    }

    @Override
    public int size() { return this.size; }

    @Override
    public boolean isEmpty() { return this.size == 0; }

    @Override
    public Position<E> first() throws RuntimeException {
        if (this.isEmpty()) throw new RuntimeException("he list is empty");
        return this.header;
    }

    @Override
    public Position<E> last() {
        if (this.isEmpty()) throw new RuntimeException("List is empty");
        return this.trailer;
    }

    @Override
    public Position<E> next(Position<E> p) throws RuntimeException {
        DNode<E> n = this.checkPosition(p);
        if (n.equals(this.trailer)) throw new RuntimeException("Cannot advance past the end of the list");
        return n.getNext();
    }

    @Override
    public Position<E> prev(Position<E> p) throws RuntimeException {
        DNode<E> n = this.checkPosition(p);
        if (n.equals(this.header)) throw new RuntimeException("Cannot advance past the beginning of the list");
        return n.getPrev();
    }

    @Override
    public Position<E> addFirst(E e) {
        this.header = new DNode<>(null, this.header, e, this);
        if (this.isEmpty()) this.trailer = this.header;
        else this.header.getNext().setPrev(this.header);
        this.size++;
        return this.header;
    }

    @Override
    public Position<E> addLast(E e) {
        this.trailer = new DNode<>(this.trailer, null, e, this);
        if (this.isEmpty()) this.header = this.trailer;
        else this.trailer.getPrev().setNext(this.trailer);
        this.size++;
        return this.trailer;
    }

    @Override
    public Position<E> addAfter(Position<E> p, E e) {
        DNode<E> m = this.checkPosition(p);
        DNode<E> n = new DNode<>(m, null, e, this);
        if (m.equals(this.trailer)) this.trailer = n;
        else {
            n.setNext(m.getNext());
            m.getNext().setPrev(n);
        }
        m.setNext(n);
        this.size++;
        return n;
    }

    @Override
    public Position<E> addBefore(Position<E> p, E e) {
        DNode<E> m = this.checkPosition(p);
        DNode<E> n = new DNode<>(null, m, e, this);
        if (m.equals(this.header)) this.header = n;
        else {
            n.setPrev(m.getPrev());
            m.getPrev().setNext(n);
        }
        m.setPrev(n);
        this.size++;
        return n;
    }

    @Override
    public E remove(Position<E> p) {
       DNode<E> n = this.checkPosition(p);
       if (this.size() == 1) {
           this.header = null;
           this.trailer = null;
       }
       else {
           if (n.equals(this.header)) this.header = n.getNext();
           else n.getPrev().setNext(n.getNext());

           if (n.equals(this.trailer)) this.trailer = n.getPrev();
           else n.getNext().setPrev(n.getPrev());
       }
       this.size--;
       n.setMyList(null);
       return n.getElement();
    }

    @Override
    public E set(Position<E> p, E e) {
        DNode<E> n = this.checkPosition(p);
        E oldElem = n.getElement();
        n.setElement(e);
        return oldElem;
    }

    public boolean isFirst (Position<E> p) {
        DNode<E> n = this.checkPosition(p);
        return n.equals(this.header);
    }

    public boolean isLast (Position<E> p) {
        DNode<E> n = this.checkPosition(p);
        return n.equals(this.trailer);
    }

    public void swapElements (Position<E> p1, Position<E> p2) {
        DNode<E> n1 = this.checkPosition(p1);
        DNode<E> n2 = this.checkPosition(p2);
        E temp = n1.getElement();
        n1.setElement(n2.getElement());
        n2.setElement(temp);
    }
}
