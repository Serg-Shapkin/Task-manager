package history;

import domain.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> nodeHistory = new HashMap<>(); // key - id задачи, value - узел связного списка
    private Node first; // ссылка на первый элемент
    private Node last;  // ссылка на последний элемент
    private int size = 0;

    public void linkLast(Task task) {
        final Node node = new Node(last, task, null);
        if (first == null) {
            first = node;
        } else {
            last.next = node;
        }
        last = node;
    }

    public List<Task> getTasks() { // получаем задачи в обычный ArrayList
        final List<Task> allTasks = new ArrayList<>();
        Node node = first;
        while (node != null) {
            allTasks.add(node.task);
            node = node.next;
        }
        return allTasks;
    }

    public void removeNode(int id) { // принимает id ноды
        final Node node = nodeHistory.remove(id);
        if (node == null) {
            return;
        }
        if (node.previous != null) {
            node.previous.next = node.next;
            if (node.next == null) {
                last = node.previous;
            } else {
                node.next.previous = node.previous;
            }
        } else {
            first = node.next;
            if (first == null) {
                last = null;
            } else {
                first.previous = null;
            }
        }
        System.out.println(nodeHistory);
    }

    @Override
    public List<Task> getHistory() { // получить историю
        return getTasks();
    }

    @Override
    public void addTask(Task task) {
        if (task == null) {
            return;
        } else {
            int id = task.getIdTask();
            removeNode(id);
            linkLast(task);
            nodeHistory.put(id, last);
        }
    }

    @Override
    public void remove(int id) {
        removeNode(id);
    }
}



/*
    private final Deque<Task> tasksHistory = new LinkedList<>(); //двунаправленная очередь
    @Override
    public void addTask(Task task) { // добавить таску в историю
        tasksHistory.addLast(task);  // добавляем в историю в конец
        if (tasksHistory.size() > 10) { // если количество элементов в списке больше 10
            tasksHistory.removeFirst(); // то удаляем самый старый элемент
        }
    }
*/

