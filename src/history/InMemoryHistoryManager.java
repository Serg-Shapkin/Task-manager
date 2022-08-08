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

        public void linkLast(Task task) {
            final Node node = new Node(last, task, null);
            if (first == null) {
                first = node;
            } else {
                last.next = node;
            }
            last = node;
        }

        /*private void linkLast(E element) { // добавляем задачу в конец списка
            int id = element.getIdTask();
            if (nodeHistory.get(id) != null) {
                removeNode(nodeHistory.get(id));
            }
            final Node<E> l = last;
            final Node<E> newNode = new Node(l, element, null);
            last = newNode;

            if (l == null) {
                first = newNode;
            } else {
                l.next = newNode;
            }

            nodeHistory.put(id, newNode);
            size++;
        }*/

        public Collection<Task> getTasks() { // получаем задачи в обычный ArrayList
            final List<Task> allTasks = new ArrayList<>();
            Node node = first;
            while (node != null) {
                allTasks.add(node.task);
                node = node.next;
            }
            return allTasks;
        }

        /*private Collection<E> getTasks() { // получаем задачи в обычный ArrayList
            final List<E> allTasks = new ArrayList<>();
            for (Node<E> i = first; i != null; i = i.next) {
                allTasks.add(i.task);
            }
            return allTasks;
        }*/

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
        /*private void removeNode(Node<E> id) { // принимает id ноды
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
        }*/
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
        historyList.removeNode(id);
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


