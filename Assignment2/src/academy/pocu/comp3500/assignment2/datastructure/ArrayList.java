package academy.pocu.comp3500.assignment2.datastructure;

import java.util.Iterator;

public class ArrayList<E> implements Iterable<E> {
    private final java.util.ArrayList<E> list;

    public ArrayList() {
        this.list = new java.util.ArrayList<E>();
    }

    public ArrayList(final int capacity) {
        this.list = new java.util.ArrayList<E>(capacity);
    }

    public int getSize() {
        return this.list.size();
    }

    public E get(final int index) {
        return this.list.get(index);
    }

    public E set(final int index, E e) {
        return this.list.set(index, e);
    }

    public int getIndexOf(E e) {
        return this.list.indexOf(e);
    }

    public boolean add(E e) {
        return this.list.add(e);
    }

    public void add(final int index, E e) {
        this.list.add(index, e);
    }

    public E remove(final int index) {
        return this.list.remove(index);
    }

    public boolean remove(final Object o) {
        return this.list.remove(o);
    }

    public void clear() {
        this.list.clear();
    }

    @Override
    public Iterator<E> iterator() {
        return this.list.iterator();
    }
}
