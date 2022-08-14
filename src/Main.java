
import domain.Epic;
import domain.Subtask;
import domain.Task;
import domain.TaskStatus;
import manager.Managers;
import manager.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefaultTaskManager();

        // создать две задачи
        Task task1 = new Task("Напоминание 1", "Сдать показания счетчиков", TaskStatus.NEW);
        Task task2 = new Task("Напоминание 2", "Оплатить квитанции КУ", TaskStatus.IN_PROGRESS);
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        System.out.println(taskManager.getAllTasks());
        System.out.println("- - - - - - - - - - - - - - -");

        System.out.println("Запросили задачу №1 по id");
        taskManager.getTaskById(1);
        System.out.println("Запросили задачу №2 по id");
        taskManager.getTaskById(2);
        System.out.println("История №1: " + taskManager.getHistory());

        System.out.println("Еще раз запросили задачу №1 по id");
        taskManager.getTaskById(1);
        System.out.println("Еще раз запросили задачу №2 по id");
        taskManager.getTaskById(2);
        System.out.println("История №2: " + taskManager.getHistory());

        System.out.println("Удаляем задачу №2 по id");
        taskManager.removeTaskById(2);
        System.out.println("История после удаления: " + taskManager.getHistory());


        System.out.println("- - - - - - - - - - - - - - -");

        Epic epic1 = new Epic("Эпик 1", "Большой переезд."); // id Task 3
        Epic epic2 = new Epic("Эпик 2", "Записать цели по обучению на курсе."); // id Task 4
        Epic epic3 = new Epic("Эпик 3", "Сдать финальное ТЗ 5-го спринта"); // id Task 5
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addEpic(epic3);

        System.out.println("Все Эпики:");
        System.out.println(taskManager.getAllEpics() + "\n"); // Получить все эпики

        System.out.println("Эпик: \"Большой переезд\"");
        Subtask subtask11 = new Subtask("Подзадача 1-1", "Собрать коробки",
                TaskStatus.NEW, 3); // id Subtask 6
        Subtask subtask12 = new Subtask("Подзадача 1-2", "Позвать друзей",
                TaskStatus.NEW, 3);  // id Subtask 7
        Subtask subtask13 = new Subtask("Подзадача 1-3", "Загрузить все в машину",
                TaskStatus.NEW, 3);  // id Subtask 8

        taskManager.addSubtask(subtask11);
        taskManager.addSubtask(subtask12);
        taskManager.addSubtask(subtask13);

        System.out.println(taskManager.getEpicById(3)); // Получить эпик "Большой переезд"
        System.out.println(taskManager.getSubtaskById(6));
        System.out.println(taskManager.getSubtaskById(7));
        System.out.println(taskManager.getSubtaskById(8));

        System.out.println("История: " + taskManager.getHistory() + "\n");

        System.out.println("Эпик: \"Цели по обучению\"");
        Subtask subtask21 = new Subtask("Подзадача 2-1", "Взять лист бумаги и ручку",
                TaskStatus.DONE, 4); // id Subtask 9
        Subtask subtask22 = new Subtask("Подзадача 2-2", "Сформулировать цели",
                TaskStatus.IN_PROGRESS, 4); // id Subtask 10
        Subtask subtask23 = new Subtask("Подзадача 2-3", "Повесить на видное место рядом с рабочим местом",
                TaskStatus.NEW, 4); // id Subtask 11

        taskManager.addSubtask(subtask21);
        taskManager.addSubtask(subtask22);
        taskManager.addSubtask(subtask23);

        System.out.println(taskManager.getEpicById(4)); // Получить эпик "Цели по обучению"

        System.out.println(taskManager.getSubtaskById(9));
        System.out.println(taskManager.getSubtaskById(10));
        System.out.println(taskManager.getSubtaskById(11));

        taskManager.removeSubtaskById(9); // удалили сабтаску из второго эпика
        System.out.println("\n" + taskManager.subtaskOfTheEpic(epic2));

        System.out.println("История: " + taskManager.getHistory() + "\n");


        System.out.println("Эпик: \"ТЗ-5\"");
        Subtask subtask31 = new Subtask("Подзадача 3-1", "Собраться с силами =)",
                TaskStatus.DONE, 5); // id Subtask 12
        Subtask subtask32 = new Subtask("Подзадача 3-2", "Внимательно прочитать ТЗ. Лучше несколько раз.",
                TaskStatus.DONE, 5); // id Subtask 13
        Subtask subtask33 = new Subtask("Подзадача 3-3", "Неспеша написать код и сдать в установленный дедлайн.",
                TaskStatus.DONE, 5); // id Subtask 14

        taskManager.addSubtask(subtask31);
        taskManager.addSubtask(subtask32);
        taskManager.addSubtask(subtask33);

        System.out.println(taskManager.getEpicById(5)); // Получить эпик "ТЗ-5"
        System.out.println(taskManager.getSubtaskById(12));
        System.out.println(taskManager.getSubtaskById(13));
        System.out.println(taskManager.getSubtaskById(14));

        System.out.println("История: " + taskManager.getHistory() + "\n");

        System.out.println("Удаляем эпик: \"Большой переезд\"");
        taskManager.removeEpicById(3);

        System.out.println("Все Эпики:");
        System.out.println(taskManager.getAllEpics() + "\n"); // Получить все эпики
        System.out.println("История: " + taskManager.getHistory() + "\n");
    }
}
