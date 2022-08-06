package history;

import domain.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList<Task> historyList = new CustomLinkedList<>();


    private static class CustomLinkedList<E extends Task> {
        private final Map<Integer, Node> nodeHistory = new HashMap<>(); // key - id задачи, value - узел связного списка
        private Node first; // ссылка на первый элемент
        private Node last;  // ссылка на последний элемент
        private int size = 0;

        public void linkLast(E element) { // добавляем задачу в конец списка
            int id = element.getIdTask();
            if (nodeHistory.get(id) != null) {
                removeNode(nodeHistory.get(id));
            }

            final Node<E> newNode = new Node(last, element, null);
            last = newNode;

            if (last == null) {
                first = newNode;
            } else {
                last.next = newNode;
            }

            nodeHistory.put(id, newNode);
            size++;
        }

        public Collection<E> getTasks() { // получаем задачи в обычный ArrayList
            final List<E> allTasks = new ArrayList<>();
            for (Node<E> i = first; i != null; i = i.next) {
                allTasks.add(i.task);
            }
            return allTasks;
        }

        public void removeNode(Node<E> id) { // принимает id ноды
            final E element = id.task;
            final Node<E> next = id.next;
            final Node<E> previous = id.previous;

            if (previous == null) {
                first = next;
            } else {
                previous.next = next;
                id.previous = null;
            }

            if (next == null) {
                last = previous;
            } else {
                next.previous = previous;
                id.next = null;
            }
            id.task = null;
            size--;
        }
    }

    @Override
    public Collection<Task> getHistory() { // получить историю
        return historyList.getTasks();
    }

    @Override
    public void addTask(Task task) {
        historyList.linkLast(task);
    }

    @Override
    public void remove(int id) {
        historyList.removeNode(historyList.nodeHistory.get(id));
    }
}


    // private final Deque<Task> tasksHistory = new LinkedList<>(); //двунаправленная очередь

/*
    @Override
    public void addTask(Task task) { // добавить таску в историю
        tasksHistory.addLast(task);  // добавляем в историю в конец
        if (tasksHistory.size() > 10) { // если количество элементов в списке больше 10
            tasksHistory.removeFirst(); // то удаляем самый старый элемент
        }
    }
     */


