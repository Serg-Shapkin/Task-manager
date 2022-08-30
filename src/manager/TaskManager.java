package manager;

import domain.Epic;
import domain.Subtask;
import domain.Task;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface TaskManager {

    //Task
    Collection<Task> getAllTasks(); // 2.1 получить список всех задач

    void removeAllTasks(); // 2.2 удалить все задачи

    Task getTaskById(int id) throws IOException; // 2.3 получить задачу по id

    void addTask(Task task) throws IOException; // 2.4 новая задача, сохранить данные

    void updateTask(Task task); // 2.5 Обновление. Новая версия с существующим id

    void removeTaskById(int id); // 2.6 Удалить по id

    //Epic
    Collection<Epic> getAllEpics(); // получили все Эпики

    void removeAllEpics(); // удалить все Эпики

    Epic getEpicById(int id) throws IOException; // получить Эпик по id

    void addEpic(Epic epic); // добавить эпик, может добавляться без subtask, наоборот нельзя

    void updateEpic(Epic epic); // Обновить эпик

    void removeEpicById(int id); // удалить эпик по id

    //Subtask
    Collection<Subtask> getAllSubtask(); // получить все сабтаски

    void removeAllSubtasks(); // удалить все сабтаски

    Subtask getSubtaskById(int id) throws IOException; // получить сабтаску по id

    void addSubtask(Subtask subtask) throws IOException; // добавить сабтаску. Если нет эпика, то добавить нельзя.

    void updateSubtask(Subtask subtask); // обновить сабтаску

    void removeSubtaskById(int id); // удалить сабтаску по id

    Collection<Subtask> subtaskOfTheEpic(Epic epic); // получить список подзадач определенного эпика


    Collection<Task> getHistory(); // получить историю
}
