package history;

import domain.Task;

import java.util.List;

public interface HistoryManager {

    List<Task> getHistory();

    void addTask(Task task);

    void remove(int id);
}
