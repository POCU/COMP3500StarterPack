package academy.pocu.comp3500.assignment2.datastructure;

public class Stack<E> {
    private java.util.Stack<E> stack;

    public Stack() {
        this.stack = new java.util.Stack<>();
    }

    public void push(final E e) {
        this.stack.push(e);
    }

    public E pop() {
        return this.stack.pop();
    }

    public E peek() {
        return this.stack.peek();
    }

    public int getSize() {
        return this.stack.size();
    }
}
