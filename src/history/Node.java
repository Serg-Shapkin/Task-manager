package history;

import domain.Task;

public class Node<E> {
    public Node<E> previous;
    public E task;
    public Node<E> next;

    public Node(Node<E> previous, E task, Node<E> next) {
        this.previous = previous;
        this.task = task;
        this.next = next;
    }
}
