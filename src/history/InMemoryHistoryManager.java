package history;

import domain.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final Deque<Task> tasksHistory = new LinkedList<>(); //двунаправленная очередь

    @Override
    public Collection<Task> getHistory() { // получить историю
        return tasksHistory;
    }

    @Override
    public void addTask(Task task) { // добавить таску в историю
        tasksHistory.addLast(task);  // добавляем в историю в конец
        if (tasksHistory.size() > 10) { // если количество элементов в списке больше 10
            tasksHistory.removeFirst(); // то удаляем самый старый элемент
        }
    }
}
