package academy.pocu.comp3500.lab2.datastructure;

public final class Node {
    private final int data;
    private Node next;

    public Node(final int data) {
        this.data = data;
    }

    public int getData() {
        return this.data;
    }

    public Node getNextOrNull() {
        return this.next;
    }

    public void setNext(final Node node) {
        this.next = node;
    }
}
