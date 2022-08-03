import domain.Epic;
import domain.Subtask;
import domain.Task;
import domain.TaskStatus;
import history.HistoryManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefaultTaskManager();
        HistoryManager historyManager = Managers.getDefaultHistoryManager();

        // создать две задачи
        Task task1 = new Task("Напоминание 1", "Сдать показания счетчиков", 1, TaskStatus.NEW);
        Task task2 = new Task("Напоминание 2", "Оплатить квитанции КУ", 2, TaskStatus.IN_PROGRESS);
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        System.out.println(taskManager.getTaskById(1));
        System.out.println(taskManager.getTaskById(2));


        // создать эпик с тремя подзадачами
        Epic epic1 = new Epic("Эпик 1", "Переезд планируется завтра", 6);
        Subtask subtask1 = new Subtask("Подзадача 1", "Собрать коробки",
                3,TaskStatus.NEW, 6);
        Subtask subtask2 = new Subtask("Подзадача 2", "Позвать друзей",
                4,TaskStatus.NEW, 6);
        Subtask subtask3 = new Subtask("Подзадача 3", "Загрузить все в машину",
                5,TaskStatus.NEW, 6);


        // создать эпик без подзадач
        Epic epic2 = new Epic("Эпик 2", "Записать цели по обучению", 7);

        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);

    }
}
