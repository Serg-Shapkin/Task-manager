package history;

import domain.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList historyList = new CustomLinkedList();

    private static class CustomLinkedList {
        private final Map<Integer, Node> nodeHistory = new HashMap<>(); // key - id задачи, value - узел связного списка
        private Node first; // ссылка на первый элемент
        private Node last;  // ссылка на последний элемент
        private int size = 0;

        public void linkLast(Task task) { // добавляем задачу в конец списка
            final Node newNode = new Node(last, task, null);
            last = newNode;
            if (last == null) {
                first = newNode;
            } else {
                last.next = newNode;
            }
            size++;
        }

        public Collection<Task> getTasks() { // получаем задачи в обычный ArrayList
            final List<Task> allTasks = new ArrayList<>();
            Node node = first;
            while (node != null) {
                allTasks.add(node.task);
                node = node.next;
            }
            return allTasks;
        }

        public void removeNode(int id) { // принимает id ноды
            Node node = getNode(id); // ветка, которую будем удалять
            Node nodeNext = node.next; // следующий индекс
            Node nodePrevious = node.previous; // предыдущий индекс

            if (nodeNext != null) {
                nodeNext.previous = nodePrevious; // переписали ссылки
            } else {
                last = nodePrevious;
            }

            if (nodePrevious != null) {
                nodePrevious.next = nodeNext;
            } else {
                first = nodeNext;
            }
            size--;
        }


        // если обращаемся к индексу ноль, то node хранит индекс первого элемента и далее мы его вернем
        private Node getNode(int index) { // приватный метод, кот. возвращает Node по индексу
            Node node = first; // node = первому элементу
            for (int i = 0; i < index; i++) {
                node = node.next;
            }
            return node;
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


