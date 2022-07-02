package manager;

import domain.Epic;
import domain.Subtask;
import domain.Task;
import domain.TaskStatus;

import java.util.*;

public class TaskManager {
    protected int nexId = 1;
    private HashMap<Integer, Task> taskMap = new HashMap<>();
    private HashMap<Integer, Epic> epicMap = new HashMap<>(); // большая задача
    private HashMap<Integer, Subtask> subtaskMap = new HashMap<>(); // подзадача в большой задаче

    //Task
    public Collection<Task> getAllTasks() { // 2.1 получить список всех задач
        return taskMap.values();
    }

    public void removeAllTask() { // 2.2 удалить все задачи
        taskMap.clear();
    }

    public Task getTaskById(int id) { // 2.3 получить задачу по id
        Task task = taskMap.get(id);
        return task;
    }

    public void addTask(Task task) { // 2.4 новая задача, сохранить данные
        task.setIdTask(nexId++);
        taskMap.put(task.getIdTask(), task);
    }

    public void updateTask(Task task) {    // 2.5 Обновление. Новая версия с существующим id
        taskMap.put(task.getIdTask(), task);
    }

    public void removeTaskById(int id) { // 2.6 Удалить по id
        if (taskMap.containsKey(id)) {
            taskMap.remove(id);
        } else {
            System.out.println("Такого id нет.");
        }
    }

    //Epic
    public Collection<Epic> getAllEpic() { //получили все Эпики
        return epicMap.values();
    }

    public void removeAllEpic() {
        epicMap.clear();
    }

    public Epic getEpicById(int id) {
        Epic epic = epicMap.get(id);
        return epic;
    }

    public void addEpic(Epic epic) { // epic может добавляться без subtask, наоборот нельзя
        epic.setIdTask(nexId++);
        epicMap.put(epic.getIdTask(), epic);
    }

    public void update(Epic epic) {
        epicMap.put(epic.getIdTask(), epic);
    }

    public void removeEpicById(int id) {
        if (epicMap.containsKey(id)) {
            epicMap.remove(id);
        }
    }

    //Subtask
    public Collection<Subtask> getAllSubtask() {
        return subtaskMap.values();
    }

    public void removeAllSubtask() {
        subtaskMap.clear();
        for (Epic value : epicMap.values()) {
            value.getSubtaskList().clear();
        }
    }

    public Subtask getSubtaskById(int id) {
        return subtaskMap.get(id);
    }

    public void addSubtask(Subtask subtask) { // subtask нельзя добавить если нет epic
        subtask.setIdTask(nexId++);
        subtaskMap.put(subtask.getIdTask(), subtask);  // добавили в коллекцию подзадачу
        Epic epic = epicMap.get(subtask.getEpicId());  // достаем задачу, в которой лежит подзадача
        epic.addSubtask(subtask); // добавляем в задачу c этим id нашу подзадач
        epic.setTaskStatus(calculateStatus(epic));
    }

    public void update(Subtask subtask) {
        Epic epic = epicMap.get(subtask.getEpicId()); // достаем задачу, в которой лежит подзадача
        epic.addSubtask(subtask); // обновляем подзадачу
    }

    public void removeSubtaskById(int id) {
        Subtask subtask = subtaskMap.get(id); // достали подзадачу
        int epicId = subtask.getEpicId();     // получили id эпика
        Epic epic = epicMap.get(epicId);      // получили эпик
        epic.getSubtaskList().remove(subtask); //удалили подзадачу

        epic.setTaskStatus(calculateStatus(epic)); // обновили статус эпика
        subtaskMap.remove(id);
    }

    public void subtaskOfTheEpic(Epic epic) {
        for (Subtask subtask : epic.getSubtaskList()) {
            if (subtask.getEpicId() == epic.getIdTask()) {
                System.out.println(subtask);
            }
        }
    }

    private TaskStatus calculateStatus(Epic epic) {
        boolean isNew = false;
        boolean inProgress = false;
        boolean isDone = false;

        for (Subtask subtask1 : epic.getSubtaskList()) {
            // если у эпика нет подзадач или статус NEW
            if(subtask1 == null || subtask1.getTaskStatus() == TaskStatus.NEW) {
                isNew = true;
            } else if (subtask1.getTaskStatus() == TaskStatus.DONE) {
                isDone = true;
            } else {
                inProgress = true;
            }
        }

        // если NEW == true, статус будет NEW
        if(isNew && !inProgress && !isDone) {
            return TaskStatus.NEW;
            // если DONE == true, статус будет DONE
        } else if(!isNew && !inProgress && isDone) {
            return TaskStatus.DONE;
        } else {
            return TaskStatus.IN_PROGRESS;
        }
    }
}
