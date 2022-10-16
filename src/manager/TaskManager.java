package manager;

import domain.Epic;
import domain.Subtask;
import domain.Task;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface TaskManager {

    //Task
    List<Task> getAllTasks(); // 2.1 получить список всех задач

    void removeAllTasks(); // 2.2 удалить все задачи

    Task getTaskById(int id) throws IOException; // 2.3 получить задачу по id

    int addTask(Task task) throws IOException; // 2.4 новая задача, сохранить данные

    void updateTask(Task task); // 2.5 Обновление. Новая версия с существующим id

    void removeTaskById(int id); // 2.6 Удалить по id

    //Epic
    List<Epic> getAllEpics(); // получили все Эпики

    void removeAllEpics(); // удалить все Эпики

    Epic getEpicById(int id) throws IOException; // получить Эпик по id

    int addEpic(Epic epic); // добавить эпик, может добавляться без subtask, наоборот нельзя

    void updateEpic(Epic epic); // Обновить эпик

    void removeEpicById(int id); // удалить эпик по id

    //Subtask
    List<Subtask> getAllSubtask(); // получить все сабтаски

    void removeAllSubtasks(); // удалить все сабтаски

    Subtask getSubtaskById(int id) throws IOException; // получить сабтаску по id

    int addSubtask(Subtask subtask) throws IOException; // добавить сабтаску. Если нет эпика, то добавить нельзя.

    void updateSubtask(Subtask subtask); // обновить сабтаску

    void removeSubtaskById(int id); // удалить сабтаску по id

    List<Subtask> subtaskOfTheEpic(Epic epic); // получить список подзадач определенного эпика

    // History
    List<Task> getHistory(); // получить историю

    // Prioritized
    Set<Task> getPrioritizedTasks(); // вернуть отсортированный список задач
}
