package history;

import domain.Task;

import java.util.Collection;

public interface HistoryManager {

    Collection<Task> getHistory();

    void addTask(Task task);

    void remove(int id);
}
