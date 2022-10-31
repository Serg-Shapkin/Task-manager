package manager;

import domain.Epic;
import domain.Subtask;
import domain.Task;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface TaskManager {

    // Task
    List<Task> getAllTasks();
    void removeAllTasks();
    Task getTaskById(int id) throws IOException;
    int addTask(Task task) throws IOException;
    void updateTask(Task task);
    void removeTaskById(int id);

    //Epic
    List<Epic> getAllEpics();
    void removeAllEpics();
    Epic getEpicById(int id) throws IOException;
    void addEpic(Epic epic);
    void updateEpic(Epic epic);
    void removeEpicById(int id);

    //Subtask
    List<Subtask> getAllSubtask();
    void removeAllSubtasks();
    Subtask getSubtaskById(int id) throws IOException;
    int addSubtask(Subtask subtask) throws IOException;
    void updateSubtask(Subtask subtask);
    void removeSubtaskById(int id);
    List<Subtask> getSubtaskOfTheEpic(Epic epic);

    // History
    List<Task> getHistory();

    // Prioritized
    Set<Task> getPrioritizedTasks();
}
