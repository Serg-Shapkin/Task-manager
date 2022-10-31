package manager;

import domain.Epic;
import domain.Subtask;
import domain.Task;
import history.HistoryManager;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected int nexId = 1;
    protected final Map<Integer, Task> taskMap = new HashMap<>();
    protected final Map<Integer, Epic> epicMap = new HashMap<>(); // большая задача
    protected final Map<Integer, Subtask> subtaskMap = new HashMap<>(); // подзадача в большой задаче

    protected final Map<LocalDateTime, Task> duplicatesDateMap = new HashMap<>();

    protected final CalculateStatusEpics calculateStatus = new CalculateStatusEpics();
    protected final HistoryManager historyManager = Managers.getDefaultHistoryManager();

    protected final LocalDateTimeComparator comparator = new LocalDateTimeComparator(); // создали компаратор для сортировки задач
    protected final Set<Task> prioritizedTasks = new TreeSet<>(comparator); // для отсортированных задач

    //Task
    @Override
    public List<Task> getAllTasks() { // 2.1 получить список всех задач
        return new ArrayList<>(taskMap.values()); // возвращаем копию списка всех задач
    }

    @Override
    public void removeAllTasks() { // 2.2 удалить все задачи
        for (Integer keysToHistory : taskMap.keySet()) // получили ключи задач
            historyManager.remove(keysToHistory);     // и удалили их из истории
        taskMap.clear(); // очистили полностью задачи
    }

    @Override
    public Task getTaskById(int id) { // 2.3 получить задачу по id
        Task task = taskMap.get(id);
        historyManager.addTask(task); // записать задачу в историю
        return task;
    }

    @Override
    public int addTask(Task task) {
        task.setIdTask(nexId++);
        taskMap.put(task.getIdTask(), task);
        prioritizedTasks.add(task); // добавили в сортировку
        CheckingTimeIntersections(task); // проверяем на пересечение по времени
        return task.getIdTask();
    }

    @Override
    public void updateTask(Task task) {    // 2.5 Обновление. Новая версия с существующим id
        taskMap.put(task.getIdTask(), task);
        prioritizedTasks.add(task); // добавили в сортировку
        CheckingTimeIntersections(task); // проверяем на пересечение по времени
    }

    @Override
    public void removeTaskById(int id) { // 2.6 Удалить по id
        if (taskMap.containsKey(id)) {
            taskMap.remove(id);
            historyManager.remove(id); // удалить задачу из истории просмотров
        } else {
            System.out.println("Такого id нет.");
        }
    }

    //Epic
    @Override
    public List<Epic> getAllEpics() { // получили все Эпики
        return new ArrayList<>(epicMap.values()); // возвращаем копию списка всех эпиков
    }

    @Override
    public void removeAllEpics() {
        for (Integer keysToHistory : epicMap.keySet()) { // получили ключи эпиков
            historyManager.remove(keysToHistory); // и удалили их из истории
        }

        for (Integer keysToHistory : subtaskMap.keySet()) { // получили ключи сабтасок
            historyManager.remove(keysToHistory); // их тоже удалили из истории
        }
        epicMap.clear(); // очистили эпики
        subtaskMap.clear(); // очистили сабтаски, т.к не могут существовать без эпиков
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epicMap.get(id);
        historyManager.addTask(epic); // записать эпик в историю
        return epic;
    }

    @Override
    public void addEpic(Epic epic) { // epic может добавляться без subtask, наоборот нельзя
        epic.setIdTask(nexId++);
        epicMap.put(epic.getIdTask(), epic);
        CheckingTimeIntersections(epic); // проверяем на пересечение по времени
    }

    @Override
    public void updateEpic(Epic epic) {
        epicMap.put(epic.getIdTask(), epic);
        CheckingTimeIntersections(epic); // проверяем на пересечение по времени
    }

    @Override
    public void removeEpicById(int id) { // переделал, т.к. раньше не удалял подзадачи удаляемого эпика
        Epic epic = epicMap.get(id);
        List<Subtask> allSubtasksOfTheEpic = getSubtaskOfTheEpic(epic);
        for (Subtask subtask : allSubtasksOfTheEpic) {
            subtaskMap.remove(subtask.getIdTask()); // удаляем сначала подзадачи конкретного эпика
            historyManager.remove(subtask.getIdTask()); // удаляем подзадачи из истории
        }
        epicMap.remove(id);        // после удаляю эпик
        historyManager.remove(id); // и в последнюю очередь удаляю его из истории
    }

    //Subtask
    @Override
    public List<Subtask> getAllSubtask() {
        return new ArrayList<>(subtaskMap.values()); // возвращаем копию списка всех сабтасков
    }

    @Override
    public void removeAllSubtasks() {
        for (Epic value : epicMap.values()) {
            value.getSubtaskList().clear(); // очистили подзадачи в эпиках
        }

        for (Integer keysToHistory : subtaskMap.keySet()) // получили ключи подзадач
            historyManager.remove(keysToHistory);     // и удалили их из истории

        subtaskMap.clear(); // очистили все подзадачи
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtaskMap.get(id);
        historyManager.addTask(subtask); // записать подзадачу в историю
        return subtask;
    }

    @Override
    public int addSubtask(Subtask subtask) { // subtask нельзя добавить если нет epic
        subtask.setIdTask(nexId++);
        subtaskMap.put(subtask.getIdTask(), subtask);  // добавили в коллекцию подзадачу
        Epic epic = epicMap.get(subtask.getEpicId());  // достаем задачу, в которой лежит подзадача
        epic.addSubtask(subtask); // добавляем в задачу c этим id нашу подзадач
        epic.setTaskStatus(calculateStatus.calculateStatus(epic)); // обновили статус эпика

        prioritizedTasks.add(subtask); //добавили в сортировку
        CheckingTimeIntersections(subtask); // проверяем на пересечение по времени

        if (subtask.getStartTime() != null) {
            calculateTime(epic.getIdTask()); // обновили время эпика
        }

        return subtask.getIdTask();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Epic epic = epicMap.get(subtask.getEpicId()); // достаем задачу, в которой лежит подзадача
        epic.addSubtask(subtask); // обновляем подзадачу
        epic.setTaskStatus(calculateStatus.calculateStatus(epic)); // обновили статус эпика
        prioritizedTasks.add(subtask); // добавили в сортировку
        CheckingTimeIntersections(subtask); // проверяем на пересечение по времени
    }

    @Override
    public void removeSubtaskById(int id) {
        Subtask subtask = subtaskMap.get(id); // достали подзадачу
        int epicId = subtask.getEpicId();     // получили id эпика
        Epic epic = epicMap.get(epicId);      // получили эпик
        historyManager.remove(id); // удалить сабтаску из истории просмотров
        epic.getSubtaskList().remove(subtask); //удалили подзадачу
        epic.setTaskStatus(calculateStatus.calculateStatus(epic)); // обновили статус эпика
        subtaskMap.remove(id);
    }

    @Override
    public List<Subtask> getSubtaskOfTheEpic(Epic epic) { // получить список подзадач определенного эпика
        List<Subtask> allSubtaskOfTheEpic = new ArrayList<>();
        for (Subtask subtask : epic.getSubtaskList()) {
            if (subtask.getEpicId() == epic.getIdTask()) {
                allSubtaskOfTheEpic.add(subtask);
            }
        }
        return allSubtaskOfTheEpic;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    public void calculateTime(int id) {
        Epic epic = epicMap.get(id);

        List<Subtask> allSubtasksOfTheEpic = getSubtaskOfTheEpic(epic);

        if (allSubtasksOfTheEpic.isEmpty()) {
            epic.setStartTime(null);
            epic.setEndTime(null);
            epic.setDuration(0);
        } else {
            LocalDateTime epicMinTime = LocalDateTime.MAX; // максимально возможная дата
            long epicDuration = 0;
            for (Subtask subtask : allSubtasksOfTheEpic) {
                if (subtask.getStartTime() != null) {
                    epicDuration = subtask.getDuration() + epicDuration;
                    if (subtask.getStartTime().isBefore(epicMinTime)) { // если время подзадачи раньше
                        epicMinTime = subtask.getStartTime();
                    }
                }
            }
            epic.setStartTime(epicMinTime);
            epic.setEndTime(epicMinTime.plusMinutes(epicDuration));
            epic.setDuration(epicDuration);
        }
    }

    protected void CheckingTimeIntersections(Task task) { // проверка на пересечение задач по времени
        String message = null;

        if (task.getStartTime() != null) {
            LocalDateTime startTime = task.getStartTime();
            LocalDateTime endTime = task.getStartTime().plusMinutes(task.getDuration());

            for (Map.Entry<LocalDateTime, Task> entry : duplicatesDateMap.entrySet()) {
                final Task currentTask = entry.getValue();
                final LocalDateTime currentStartTime = currentTask.getStartTime();
                final LocalDateTime currentEndTime = currentTask.getStartTime().plusMinutes(task.getDuration());

                if (!endTime.isAfter(currentStartTime)) {
                    continue;
                }
                if (!currentEndTime.isAfter(startTime)) {
                    continue;
                }
                message = "Найдено пересечение по времени задачи id=" + currentTask.getIdTask() + " с задачей id=" + task.getIdTask() + "\n"+
                        "Дата и время начала выполнения задачи id=" + currentTask.getIdTask() + ": " + currentStartTime +
                        ", окончание: " + currentEndTime + "\n" +
                        "Дата и время начала выполнения задачи id=" + task.getIdTask() + ": " + startTime +
                        ", окончание: " + endTime + "\n";
            }
            duplicatesDateMap.put(startTime, task);
        }
        if (message != null) {
            System.out.println(message);
        }
    }
}
