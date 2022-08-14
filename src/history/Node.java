package history;

import domain.Task;

public class Node {
    public Node previous;
    public Task task;
    public Node next;

    public Node(Node previous, Task task, Node next) {
        this.previous = previous;
        this.task = task;
        this.next = next;
    }

    @Override
    public String toString() {
        return "Node{" +
                "previous=" + previous +
                ", task=" + task +
                ", next=" + next +
                '}';
    }
}
