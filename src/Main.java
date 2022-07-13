import domain.Epic;
import domain.Subtask;
import domain.Task;
import domain.TaskStatus;
import manager.InMemoryTaskManager;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task = new Task("Напоминание 1", "Сдать показания счетчиков", 3, TaskStatus.NEW);

        Epic epic = new Epic("Переезд", "Переезд планируется завтра", 1);
        Epic epic2 = new Epic("Переезд", "Переезд планируется завтра", 2);
        Epic epic3 = new Epic("Переезд", "Переезд планируется завтра", 3);

        Subtask subtask11 = new Subtask("Подзадача 1", "Собрать коробки",
                1,TaskStatus.NEW, 1);
        Subtask subtask12 = new Subtask("Подзадача 2", "Позвать друзей",
                2,TaskStatus.NEW, 1);
        Subtask subtask13 = new Subtask("Подзадача 3", "Загрузить все в машину",
                3,TaskStatus.NEW, 1);

        Subtask subtask21 = new Subtask("Подзадача 1", "Собрать коробки",
                1,TaskStatus.DONE, 2);
        Subtask subtask22 = new Subtask("Подзадача 2", "Позвать друзей",
                2,TaskStatus.DONE, 2);
        Subtask subtask23 = new Subtask("Подзадача 3", "Загрузить все в машину",
                3,TaskStatus.DONE, 2);

        taskManager.addEpic(epic);
        taskManager.addEpic(epic2);
        taskManager.addEpic(epic3);

        taskManager.addSubtask(subtask11);
        taskManager.addSubtask(subtask12);
        taskManager.addSubtask(subtask13);

        taskManager.addSubtask(subtask21);
        taskManager.addSubtask(subtask22);

    }
}
