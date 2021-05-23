package academy.pocu.comp3500.assignment2.datastructure;

import java.util.LinkedList;

public class Queue<E> {
    private final LinkedList<E> list;

    public Queue() {
        list = new LinkedList<>();
    }

    public void enqueue(final E e) {
        this.list.addFirst(e);
    }

    public E dequeue() {
        return this.list.removeLast();
    }

    public E peek() {
        return this.list.getLast();
    }

    public int getSize() {
        return this.list.size();
    }
}
