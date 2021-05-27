package academy.pocu.comp3500.assignment2.datastructure;

import java.util.Iterator;

public class LinkedList<E> implements Iterable<E> {
    private final java.util.LinkedList<E> list;

    public LinkedList() {
        this.list = new java.util.LinkedList<>();
    }

    public E getFirst() {
        return this.list.getFirst();
    }

    public E getLast() {
        return this.list.getLast();
    }

    public E removeFirst() {
        return this.list.removeFirst();
    }

    public E removeLast() {
        return this.list.removeLast();
    }

    public void addFirst(final E e) {
        this.list.addFirst(e);
    }

    public void addLast(final E e) {
        this.list.addLast(e);
    }

    public boolean contains(final E e) {
        return this.list.contains(e);
    }

    public boolean add(final E e) {
        return this.list.add(e);
    }

    public boolean remove(final E e) {
        return this.list.remove(e);
    }

    public void clear() {
        this.list.clear();
    }

    public E get(final int index) {
        return this.list.get(index);
    }

    public E set(final int index, final E e) {
        return this.list.set(index, e);
    }

    public void add(final int index, E e) {
        this.list.add(index, e);
    }

    public E remove(final int index) {
        return this.list.remove(index);
    }

    public int getSize() {
        return this.list.size();
    }

    @Override
    public Iterator<E> iterator() {
        return this.list.iterator();
    }
}
