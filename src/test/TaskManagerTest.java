import domain.Epic;
import domain.Subtask;
import domain.Task;
import domain.TaskStatus;
import manager.TaskManager;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    // Task
    // getAllTasks()
    @Test
    void getAllTasks() throws IOException {
        Task task1 = new Task("Напоминание 1", "Сдать показания счетчиков", TaskStatus.NEW);
        Task task2 = new Task("Напоминание 2", "Оплатить квитанции КУ", TaskStatus.IN_PROGRESS);

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи не возвращаются");
        assertEquals(2, tasks.size(), "Неверное количество задач");
        assertEquals(task1, tasks.get(0), "Задачи не совпадают");
    }

    // removeAllTasks()
    @Test
    void removeAllTasks() throws IOException {
        Task task1 = new Task("Напоминание 1", "Сдать показания счетчиков", TaskStatus.NEW);
        Task task2 = new Task("Напоминание 2", "Оплатить квитанции КУ", TaskStatus.IN_PROGRESS);
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        taskManager.removeAllTasks();

        final List<Task> tasks = taskManager.getAllTasks();
        assertEquals(0,tasks.size(), "Задачи не удалены");
    }

    // getTaskById()
    @Test
    void getTaskById() throws IOException {
        Task task1 = new Task("Напоминание 1", "Сдать показания счетчиков", TaskStatus.NEW);
        taskManager.addTask(task1);

        final int taskId = task1.getIdTask();
        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(taskId, savedTask.getIdTask(), "Id задач не равны");
    }

    // addTask()
    @Test
    void addTask() throws IOException {
        Task task1 = new Task("Напоминание 1", "Сдать показания счетчиков", TaskStatus.NEW);
        taskManager.addTask(task1);

        final int taskId = task1.getIdTask(); // получили id задачи
        final Task savedTask = taskManager.getTaskById(taskId); // сохранили задачу по Id

        assertNotNull(savedTask, "Задача не найдена"); // пройден, если найдена
        assertEquals(task1, savedTask, "Задачи не совпадают"); // пройден, если совпадают

        final List<Task> tasks = taskManager.getAllTasks(); // сохранили все задачи
        assertNotNull(tasks, "Задачи не возвращаются");
        assertEquals(1, tasks.size(),"Неверное количество задач");
        assertEquals(task1, tasks.get(0),"Задачи не совпадают");
    }

    // updateTask()
    @Test
    void updateTask() throws IOException {
        Task task1 = new Task("Напоминание 1", "Сдать показания счетчиков", TaskStatus.NEW);
        taskManager.addTask(task1);

        final int taskId = task1.getIdTask(); // получили id первой задачи
        Task savedTask = taskManager.getTaskById(taskId); // сохранили задачу по id

        Task newTask = new Task("Напоминание 2", "Оплатить квитанции КУ", TaskStatus.IN_PROGRESS);
        newTask.setIdTask(savedTask.getIdTask()); // установили для новой задачи id первой

        savedTask = newTask; // присвоили сохраненной задаче вторую задачу

        taskManager.updateTask(savedTask); // обновили задачу

        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(newTask, savedTask, "Задачи не совпадают");

        final List<Task> tasks = taskManager.getAllTasks(); // сохранили все задачи
        assertNotNull(tasks, "Задачи не возвращаются");
        assertEquals(1, tasks.size(),"Неверное количество задач");
        assertEquals(newTask, tasks.get(0),"Задачи не совпадают");
    }

    // removeTaskById()
    @Test
    void removeTaskById() throws IOException {
        Task task1 = new Task("Напоминание 1", "Сдать показания счетчиков", TaskStatus.NEW);
        Task task2 = new Task("Напоминание 2", "Оплатить квитанции КУ", TaskStatus.IN_PROGRESS);
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        final int taskId = task1.getIdTask(); // получили id задачи, которую собираемся удалять
        taskManager.removeTaskById(taskId); // удалили задачу

        final List<Task> tasks = taskManager.getAllTasks(); // сохранили оставшиеся задачи

        assertNotNull(tasks, "Задачи не возвращаются");
        assertEquals(1, tasks.size(),"Неверное количество задач");
        assertEquals(task2, tasks.get(0),"Задачи не совпадают"); // т.к. первую удалили
    }

    // Epic
    // getAllEpics()
    @Test
    void getAllEpics() {
        Epic epic1 = new Epic("Эпик 1", "Большой переезд."); // id Task 3
        Epic epic2 = new Epic("Эпик 2", "Записать цели по обучению на курсе."); // id Task 4
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        final List<Epic> epics = taskManager.getAllEpics(); // сохранили все эпики
        assertNotNull(epics, "Задачи не возвращаются");
        assertEquals(2, epics.size(), "Неверное количество задач");
        assertEquals(epic1, epics.get(0), "Задачи не совпадают");
    }

    // removeAllEpics()
    @Test
    void removeAllEpics() {
        Epic epic1 = new Epic("Эпик 1", "Большой переезд."); // id Task 3
        Epic epic2 = new Epic("Эпик 2", "Записать цели по обучению на курсе."); // id Task 4
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        taskManager.removeAllEpics();

        final List<Epic> epics = taskManager.getAllEpics();
        assertEquals(0,epics.size(), "Задачи не удалены");
    }

    // getEpicById()
    @Test
    void getEpicById() throws IOException {
        Epic epic1 = new Epic("Эпик 1", "Большой переезд."); // id Task 3
        taskManager.addEpic(epic1);

        final int idTask = epic1.getIdTask();
        final Epic savedEpic = taskManager.getEpicById(idTask);

        assertNotNull(savedEpic, "Задача не найдена");
        assertEquals(idTask, savedEpic.getIdTask(), "Id задач не равны");
    }

    // addEpic()
    @Test
    void addEpic() throws IOException {
        Epic epic1 = new Epic("Эпик 1", "Большой переезд."); // id Task 3
        taskManager.addEpic(epic1);

        final int idTask = epic1.getIdTask();
        final Epic savedEpic = taskManager.getEpicById(idTask);

        assertNotNull(savedEpic, "Задача не найдена");
        assertEquals(epic1, savedEpic, "Задачи не совпадают");

        final List<Epic> epics = taskManager.getAllEpics(); // сохранили все эпики
        assertNotNull(epics, "Задачи не возвращаются");
        assertEquals(1, epics.size(),"Неверное количество задач");
        assertEquals(epic1, epics.get(0),"Задачи не совпадают");
    }

    // updateEpic()
    @Test
    void updateEpic() throws IOException {
        Epic epic1 = new Epic("Эпик 1", "Большой переезд."); // id Task 3
        taskManager.addEpic(epic1);

        final int idTask = epic1.getIdTask();
        Epic savedEpic = taskManager.getEpicById(idTask);

        Epic newEpic = new Epic("Эпик 2", "Записать цели по обучению на курсе."); // id Task 4
        newEpic.setIdTask(savedEpic.getIdTask()); // присвоили новой задаче id первой

        savedEpic = newEpic;

        taskManager.updateEpic(savedEpic);

        assertNotNull(savedEpic, "Задача не найдена");
        assertEquals(newEpic, savedEpic, "Задачи не совпадают");

        final List<Epic> epics = taskManager.getAllEpics();
        assertNotNull(epics, "Задачи не возвращаются");
        assertEquals(1, epics.size(),"Неверное количество задач");
        assertEquals(newEpic, epics.get(0),"Задачи не совпадают");
    }

    // removeEpicById
    @Test
    void removeEpicById() {
        Epic epic1 = new Epic("Эпик 1", "Большой переезд."); // id Task 3
        Epic epic2 = new Epic("Эпик 2", "Записать цели по обучению на курсе."); // id Task 4
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        final int idTask = epic1.getIdTask();
        taskManager.removeEpicById(idTask);

        final List<Epic> epics = taskManager.getAllEpics();
        assertNotNull(epics, "Задачи не возвращаются");
        assertEquals(1, epics.size(),"Неверное количество задач");
        assertEquals(epic2, epics.get(0),"Задачи не совпадают");
    }

    // Subtask
    // getAllSubtask()
    @Test
    void getAllSubtask() throws IOException {
        Epic epic1 = new Epic("Эпик 1", "Большой переезд."); // id Task 3
        taskManager.addEpic(epic1);

        Subtask subtask11 = new Subtask("Подзадача 1-1", "Собрать коробки",
                TaskStatus.NEW, epic1.getIdTask());
        Subtask subtask12 = new Subtask("Подзадача 1-2", "Позвать друзей",
                TaskStatus.NEW, epic1.getIdTask());
        Subtask subtask13 = new Subtask("Подзадача 1-3", "Загрузить все в машину",
                TaskStatus.NEW, epic1.getIdTask());

        taskManager.addSubtask(subtask11);
        taskManager.addSubtask(subtask12);
        taskManager.addSubtask(subtask13);

        final List<Subtask> subtasks = taskManager.getAllSubtask(); //сохранили все подзадачи в список

        assertNotNull(subtasks, "Подзадачи не возвращаются");
        assertEquals(3, subtasks.size(),"Неверное количество подзадач");
        assertEquals(subtask11, subtasks.get(0), "Подзадачи не совпадают");
    }

    // removeAllSubtasks()
    @Test
    void removeAllSubtask() throws IOException {
        Epic epic1 = new Epic("Эпик 1", "Большой переезд."); // id Task 3
        taskManager.addEpic(epic1);

        Subtask subtask11 = new Subtask("Подзадача 1-1", "Собрать коробки",
                TaskStatus.NEW, epic1.getIdTask());
        Subtask subtask12 = new Subtask("Подзадача 1-2", "Позвать друзей",
                TaskStatus.NEW, epic1.getIdTask());
        Subtask subtask13 = new Subtask("Подзадача 1-3", "Загрузить все в машину",
                TaskStatus.NEW, epic1.getIdTask());

        taskManager.addSubtask(subtask11);
        taskManager.addSubtask(subtask12);
        taskManager.addSubtask(subtask13);

        taskManager.removeAllSubtasks(); // удалили все подзадачи

        final List<Subtask> subtasks = taskManager.getAllSubtask(); //сохранили все подзадачи в список

        assertEquals(0,subtasks.size(), "Подзадачи не удалены");
    }

    // getSubtaskById()
    @Test
    void getSubtaskById() throws IOException {
        Epic epic1 = new Epic("Эпик 1", "Большой переезд."); // id Task 3
        taskManager.addEpic(epic1);

        Subtask subtask11 = new Subtask("Подзадача 1-1", "Собрать коробки",
                TaskStatus.NEW, epic1.getIdTask());
        taskManager.addSubtask(subtask11);

        final int idTask = subtask11.getIdTask();
        final Subtask savedSubtask = taskManager.getSubtaskById(idTask);

        assertNotNull(savedSubtask, "Подзадача не найдена");
        assertEquals(subtask11, savedSubtask, "Подзадачи не совпадают");
    }

    // addSubtask()
    @Test
    void addSubtask() throws IOException {
        Epic epic1 = new Epic("Эпик 1", "Большой переезд."); // id Task 3
        taskManager.addEpic(epic1);

        Subtask subtask11 = new Subtask("Подзадача 1-1", "Собрать коробки",
                TaskStatus.NEW, epic1.getIdTask());
        Subtask subtask12 = new Subtask("Подзадача 1-2", "Позвать друзей",
                TaskStatus.NEW, epic1.getIdTask());
        Subtask subtask13 = new Subtask("Подзадача 1-3", "Загрузить все в машину",
                TaskStatus.NEW, epic1.getIdTask());

        taskManager.addSubtask(subtask11);
        taskManager.addSubtask(subtask12);
        taskManager.addSubtask(subtask13);

        final int idTask = subtask11.getIdTask();
        final Subtask savedSubtask = taskManager.getSubtaskById(idTask);

        assertNotNull(savedSubtask, "Подзадача не найдена");
        assertEquals(subtask11, savedSubtask, "Подзадачи не совпадают");

        final List<Subtask> subtasks = taskManager.getAllSubtask();

        assertNotNull(subtasks, "Подзадачи не возвращаются");
        assertEquals(3, subtasks.size(),"Неверное количество подзадач");
        assertEquals(subtask11, subtasks.get(0),"Подзадачи не совпадают");
    }

    // updateSubtask()
    @Test
    void updateSubtask() throws IOException {
        Epic epic1 = new Epic("Эпик 1", "Большой переезд."); // id Task 3
        taskManager.addEpic(epic1);

        Subtask subtask11 = new Subtask("Подзадача 1-1", "Собрать коробки",
                TaskStatus.NEW, epic1.getIdTask());
        taskManager.addSubtask(subtask11);

        final int idTask = subtask11.getIdTask();
        Subtask savedSubtask = taskManager.getSubtaskById(idTask);

        Subtask newSubtask = new Subtask("Подзадача 1-2", "Позвать друзей",
                TaskStatus.NEW, epic1.getIdTask());
        newSubtask.setIdTask(savedSubtask.getIdTask()); // присвоили новой подзадаче id первой подзадачи

        savedSubtask = newSubtask;
        taskManager.updateSubtask(savedSubtask);

        assertNotNull(savedSubtask, "Подзадача не найдена");
        assertEquals(newSubtask, savedSubtask, "Подзадачи не совпадают");

        final List<Subtask> subtasks = taskManager.getAllSubtask();

        assertNotNull(subtasks, "Подзадачи не возвращаются");
        assertEquals(1, subtasks.size(),"Неверное количество подзадач");
        assertEquals(subtask11, subtasks.get(0),"Подзадачи не совпадают");
    }

    // removeSubtaskById()
    @Test
    void removeSubtaskById() throws IOException {
        Epic epic1 = new Epic("Эпик 1", "Большой переезд."); // id Task 3
        taskManager.addEpic(epic1);

        Subtask subtask11 = new Subtask("Подзадача 1-1", "Собрать коробки",
                TaskStatus.NEW, epic1.getIdTask());
        taskManager.addSubtask(subtask11);

        final int idSubtask = subtask11.getIdTask();
        taskManager.removeSubtaskById(idSubtask);

        assertNull(taskManager.getSubtaskById(idSubtask), "Подзадача не удалена");
    }

    // subtaskOfTheEpic()
    @Test
    void subtaskOfTheEpic() throws IOException {
        Epic epic1 = new Epic("Эпик 1", "Большой переезд."); // id Task 3
        taskManager.addEpic(epic1);

        Subtask subtask11 = new Subtask("Подзадача 1-1", "Собрать коробки",
                TaskStatus.NEW, epic1.getIdTask());
        Subtask subtask12 = new Subtask("Подзадача 1-2", "Позвать друзей",
                TaskStatus.NEW, epic1.getIdTask());
        Subtask subtask13 = new Subtask("Подзадача 1-3", "Загрузить все в машину",
                TaskStatus.NEW, epic1.getIdTask());

        taskManager.addSubtask(subtask11);
        taskManager.addSubtask(subtask12);
        taskManager.addSubtask(subtask13);

        final List<Subtask> subtasks = taskManager.getSubtaskOfTheEpic(epic1);

        assertNotNull(subtasks, "Подзадачи не возвращаются");
        assertEquals(3, subtasks.size(),"Неверное количество подзадач");
        assertEquals(subtask11, subtasks.get(0),"Подзадачи не совпадают");
    }

    // CalculateStatus
    @Test
    void calculateStatusIfThreeSubtasksAreNew() throws IOException {
        Epic epic1 = new Epic("Эпик 1", "Большой переезд."); // id Task 3
        taskManager.addEpic(epic1);

        Subtask subtask11 = new Subtask("Подзадача 1-1", "Собрать коробки",
                TaskStatus.NEW, epic1.getIdTask());
        Subtask subtask12 = new Subtask("Подзадача 1-2", "Позвать друзей",
                TaskStatus.NEW, epic1.getIdTask());
        Subtask subtask13 = new Subtask("Подзадача 1-3", "Загрузить все в машину",
                TaskStatus.NEW, epic1.getIdTask());

        taskManager.addSubtask(subtask11);
        taskManager.addSubtask(subtask12);
        taskManager.addSubtask(subtask13);

        assertEquals(epic1.getTaskStatus(), TaskStatus.NEW, "Неверный статус задачи");
    }

    @Test
    void calculateStatusIfThreeSubtasksAreInProgress() throws IOException {
        Epic epic1 = new Epic("Эпик 1", "Большой переезд."); // id Task 3
        taskManager.addEpic(epic1);

        Subtask subtask11 = new Subtask("Подзадача 1-1", "Собрать коробки",
                TaskStatus.IN_PROGRESS, epic1.getIdTask());
        Subtask subtask12 = new Subtask("Подзадача 1-2", "Позвать друзей",
                TaskStatus.IN_PROGRESS, epic1.getIdTask());
        Subtask subtask13 = new Subtask("Подзадача 1-3", "Загрузить все в машину",
                TaskStatus.IN_PROGRESS, epic1.getIdTask());

        taskManager.addSubtask(subtask11);
        taskManager.addSubtask(subtask12);
        taskManager.addSubtask(subtask13);

        assertEquals(epic1.getTaskStatus(), TaskStatus.IN_PROGRESS, "Неверный статус задачи");
    }

    @Test
    void calculateStatusIfThreeSubtasksAreDone() throws IOException {
        Epic epic1 = new Epic("Эпик 1", "Большой переезд."); // id Task 3
        taskManager.addEpic(epic1);

        Subtask subtask11 = new Subtask("Подзадача 1-1", "Собрать коробки",
                TaskStatus.DONE, epic1.getIdTask());
        Subtask subtask12 = new Subtask("Подзадача 1-2", "Позвать друзей",
                TaskStatus.DONE, epic1.getIdTask());
        Subtask subtask13 = new Subtask("Подзадача 1-3", "Загрузить все в машину",
                TaskStatus.DONE, epic1.getIdTask());

        taskManager.addSubtask(subtask11);
        taskManager.addSubtask(subtask12);
        taskManager.addSubtask(subtask13);

        assertEquals(epic1.getTaskStatus(), TaskStatus.DONE, "Неверный статус задачи");
    }

    @Test
    void calculateStatusIfTwoNewAndOneDoneSubtask() throws IOException {
        Epic epic1 = new Epic("Эпик 1", "Большой переезд."); // id Task 3
        taskManager.addEpic(epic1);

        Subtask subtask11 = new Subtask("Подзадача 1-1", "Собрать коробки",
                TaskStatus.NEW, epic1.getIdTask());
        Subtask subtask12 = new Subtask("Подзадача 1-2", "Позвать друзей",
                TaskStatus.NEW, epic1.getIdTask());
        Subtask subtask13 = new Subtask("Подзадача 1-3", "Загрузить все в машину",
                TaskStatus.DONE, epic1.getIdTask());

        taskManager.addSubtask(subtask11);
        taskManager.addSubtask(subtask12);
        taskManager.addSubtask(subtask13);

        assertEquals(epic1.getTaskStatus(), TaskStatus.IN_PROGRESS, "Неверный статус задачи");
    }

    // getHistory
    @Test
    void getHistory() throws IOException {
        Task task1 = new Task("Напоминание 1", "Сдать показания счетчиков", TaskStatus.NEW);
        Task task2 = new Task("Напоминание 2", "Оплатить квитанции КУ", TaskStatus.IN_PROGRESS);

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        taskManager.getTaskById(task1.getIdTask());
        taskManager.getTaskById(task2.getIdTask());

        final List<Task> historyTask = taskManager.getHistory();

        assertNotNull(historyTask, "История не возвращается");
        assertEquals(2, historyTask.size(), "Неверное количество записей в истории");
        assertEquals(task1, historyTask.get(0), "Задачи не совпадают");
    }

    // getPrioritizedTasks
    @Test
    void getPrioritizedTasks() throws IOException {
        Task task1 = new Task("Напоминание 1", "Сдать показания счетчиков", TaskStatus.NEW);
        Task task2 = new Task("Напоминание 2", "Оплатить квитанции КУ", TaskStatus.IN_PROGRESS,
                15, LocalDateTime.of(2022, 10, 25, 14, 0));
        Task task3 = new Task("Напоминание 3", "Почесать кота", TaskStatus.DONE,
                15, LocalDateTime.of(2022, 10, 25, 14, 0));

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);

        Task[] tasks = new Task[3];
        tasks = taskManager.getPrioritizedTasks().toArray(tasks);

        assertNotNull(tasks, "задачи не возвращаются");
        assertEquals(3, tasks.length, "Неверное количество задач");
        assertEquals(task2, tasks[0], "Задачи не совпадают");
    }
}

