package manager;

import domain.Epic;
import domain.Subtask;
import domain.Task;
import domain.TaskStatus;
import history.HistoryManager;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected int nexId = 1;
    private HashMap<Integer, Task> taskMap = new HashMap<>();
    private HashMap<Integer, Epic> epicMap = new HashMap<>(); // большая задача
    private HashMap<Integer, Subtask> subtaskMap = new HashMap<>(); // подзадача в большой задаче

    protected HistoryManager historyService;

    //Task
    @Override
    public Collection<Task> getAllTasks() { // 2.1 получить список всех задач
        return new ArrayList<>(taskMap.values()); // возвращаем копию списка всех задач
    }

    @Override
    public void removeAllTasks() { // 2.2 удалить все задачи
        taskMap.clear();
    }

    @Override
    public Task getTaskById(int id) { // 2.3 получить задачу по id
        Task task = taskMap.get(id);
        historyService.addTask(task); // записать задачу в историю
        return task;
    }

    @Override
    public void addTask(Task task) { // 2.4 новая задача, сохранить данные
        task.setIdTask(nexId++);
        taskMap.put(task.getIdTask(), task);
    }

    @Override
    public void updateTask(Task task) {    // 2.5 Обновление. Новая версия с существующим id
        taskMap.put(task.getIdTask(), task);
    }

    @Override
    public void removeTaskById(int id) { // 2.6 Удалить по id
        if (taskMap.containsKey(id)) {
            taskMap.remove(id);
        } else {
            System.out.println("Такого id нет.");
        }
    }

    //Epic
    @Override
    public Collection<Epic> getAllEpics() { // получили все Эпики
        return new ArrayList<>(epicMap.values()); // возвращаем копию списка всех Эпиков
    }

    @Override
    public void removeAllEpics() {
        epicMap.clear();
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epicMap.get(id);
        historyService.addTask(epic); // записать эпик в историю
        return epic;
    }

    @Override
    public void addEpic(Epic epic) { // epic может добавляться без subtask, наоборот нельзя
        epic.setIdTask(nexId++);
        epicMap.put(epic.getIdTask(), epic);
    }

    @Override
    public void updateEpic(Epic epic) {
        epicMap.put(epic.getIdTask(), epic);
    }

    @Override
    public void removeEpicById(int id) {
        if (epicMap.containsKey(id)) {
            epicMap.remove(id);
        }
    }

    //Subtask
    @Override
    public Collection<Subtask> getAllSubtask() {
        return new ArrayList<>(subtaskMap.values()); // возвращаем копию списка всех сабтасков
    }

    @Override
    public void removeAllSubtasks() {
        subtaskMap.clear(); // очистили все сабтаски
        for (Epic value : epicMap.values()) {
            value.getSubtaskList().clear(); // очистили сабтаски в эпиках
        }
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtaskMap.get(id);
        historyService.addTask(subtask); // записать сабтаску в историю
        return subtask;
    }

    @Override
    public void addSubtask(Subtask subtask) { // subtask нельзя добавить если нет epic
        subtask.setIdTask(nexId++);
        subtaskMap.put(subtask.getIdTask(), subtask);  // добавили в коллекцию подзадачу
        Epic epic = epicMap.get(subtask.getEpicId());  // достаем задачу, в которой лежит подзадача
        epic.addSubtask(subtask); // добавляем в задачу c этим id нашу подзадач
        epic.setTaskStatus(calculateStatus(epic)); // обновили статус эпика
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Epic epic = epicMap.get(subtask.getEpicId()); // достаем задачу, в которой лежит подзадача
        epic.addSubtask(subtask); // обновляем подзадачу
        epic.setTaskStatus(calculateStatus(epic)); // обновили статус эпика
    }

    @Override
    public void removeSubtaskById(int id) {
        Subtask subtask = subtaskMap.get(id); // достали подзадачу
        int epicId = subtask.getEpicId();     // получили id эпика
        Epic epic = epicMap.get(epicId);      // получили эпик
        epic.getSubtaskList().remove(subtask); //удалили подзадачу

        epic.setTaskStatus(calculateStatus(epic)); // обновили статус эпика
        subtaskMap.remove(id);
    }

    @Override
    public Collection<Subtask> subtaskOfTheEpic(Epic epic) { // получить список подзадач определенного эпика
        Collection<Subtask> subtaskOfTheEpic = new ArrayList<>();
        for (Subtask subtask : epic.getSubtaskList()) {
            if (subtask.getEpicId() == epic.getIdTask()) {
                subtaskOfTheEpic.add(subtask);
            }
        }
        return subtaskOfTheEpic;
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

    @Override
    public Collection<Task> getHistory() {
        return historyService.getHistory();
    }
}
