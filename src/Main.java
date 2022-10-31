import domain.Epic;
import domain.Subtask;
import domain.Task;
import domain.TaskStatus;
import manager.Managers;
import manager.TaskManager;
import server.KVServer;

import java.io.IOException;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        KVServer kvServer = new KVServer();
        kvServer.start();

        TaskManager taskManager = Managers.getDefault();
        // id=1
        Task task1 = new Task("Напоминание 1", "Сдать показания счетчиков", TaskStatus.NEW);
        // id=2
        Task task2 = new Task("Напоминание 2", "Оплатить квитанции КУ", TaskStatus.IN_PROGRESS,
                15, LocalDateTime.of(2022, 10, 25, 14, 0));
        //id=3
        Task task3 = new Task("Напоминание 3", "Почесать кота", TaskStatus.DONE,
                15, LocalDateTime.of(2022, 10, 25, 14, 0));

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);

        // id=4
        Epic epic1 = new Epic("Эпик 1", "Большой переезд");
        // id=5
        Epic epic2 = new Epic("Эпик 2", "Записать цели по обучению на курсе");
        // id=6
        Epic epic3 = new Epic("Эпик 3", "Сдать финальное ТЗ 7-го спринта");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addEpic(epic3);

        // Epic id=4
        // id=7
        Subtask subtask11 = new Subtask("Подзадача 1-1", "Собрать коробки", TaskStatus.NEW,
                4, 30, LocalDateTime.of(2022, 10, 4, 10, 0));
        // id=8
        Subtask subtask12 = new Subtask("Подзадача 1-2", "Позвать друзей", TaskStatus.NEW,
                4, 15, LocalDateTime.of(2022, 10, 6, 11, 0));
        // id=9
        Subtask subtask13 = new Subtask("Подзадача 1-3", "Загрузить все в машину", TaskStatus.NEW,
                4, 45, LocalDateTime.of(2022, 10, 8, 13, 0));  // id Subtask 9

        taskManager.addSubtask(subtask11);
        taskManager.addSubtask(subtask12);
        taskManager.addSubtask(subtask13);

        // Epic id=5
        // id=10
        Subtask subtask21 = new Subtask("Подзадача 2-1", "Взять лист бумаги и ручку",
                TaskStatus.DONE, 5);
        // id=11
        Subtask subtask22 = new Subtask("Подзадача 2-2", "Сформулировать цели",
                TaskStatus.IN_PROGRESS, 5);
        // id=12
        Subtask subtask23 = new Subtask("Подзадача 2-3", "Повесить на видное место рядом с рабочим местом",
                TaskStatus.NEW, 5); // id Subtask 12

        taskManager.addSubtask(subtask21);
        taskManager.addSubtask(subtask22);
        taskManager.addSubtask(subtask23);

        // Epic id=6
        // id=13
        Subtask subtask31 = new Subtask("Подзадача 3-1", "Собраться с силами =)",
                TaskStatus.DONE, 6, 10, LocalDateTime.of(2022, 10, 10, 19, 0));
        // id=14
        Subtask subtask32 = new Subtask("Подзадача 3-2", "Внимательно прочитать ТЗ. Лучше несколько раз",
                TaskStatus.DONE, 6, 30, LocalDateTime.of(2022, 10, 15, 19, 30));
        // id=15
        Subtask subtask33 = new Subtask("Подзадача 3-3", "Написать код и сдать в установленный дедлайн",
                TaskStatus.DONE, 6, 180, LocalDateTime.of(2022, 10, 17, 17, 45));

        taskManager.addSubtask(subtask31);
        taskManager.addSubtask(subtask32);
        taskManager.addSubtask(subtask33);

        taskManager.getTaskById(1);
        taskManager.getTaskById(3);
        taskManager.getTaskById(2);

        taskManager.getEpicById(6);
        taskManager.getEpicById(5);
        taskManager.getEpicById(4);

        taskManager.getSubtaskById(13);
        taskManager.getSubtaskById(10);
        taskManager.getSubtaskById(7);

        System.out.println(taskManager.getHistory());



/*

        TaskManager taskManager = Managers.getDefaultTaskManager();
        FileBackedTasksManager.loadFromFile(new File("src/files/history.csv"));

        // создать задачи
        Task task1 = new Task("Напоминание 1", "Сдать показания счетчиков", TaskStatus.NEW);
        Task task2 = new Task("Напоминание 2", "Оплатить квитанции КУ", TaskStatus.IN_PROGRESS,
                15, LocalDateTime.of(2022, 10, 25, 14, 0));
        Task task3 = new Task("Напоминание 3", "Почесать кота", TaskStatus.DONE,
                15, LocalDateTime.of(2022, 10, 25, 14, 0));

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);

        System.out.println(taskManager.getAllTasks());
        System.out.println("- - - - - - - - - - - - - - -");

        System.out.println("Запросили задачи c id = 1, id = 2, id = 3 по id");
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(3);
        System.out.println("История №1:\n" + taskManager.getHistory());

        System.out.println("Еще раз запросили задачи c id = 1, id = 2, id = 3 по id");
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(3);
        System.out.println("История №2:\n" + taskManager.getHistory());

        System.out.println("Удаляем задачу №2 по id");
        //taskManager.removeTaskById(2);
        System.out.println("История после удаления задачи №2:\n" + taskManager.getHistory());

        System.out.println("- - - - - - - - - - - - - - -");


        Epic epic1 = new Epic("Эпик 1", "Большой переезд"); // id == 4
        Epic epic2 = new Epic("Эпик 2", "Записать цели по обучению на курсе"); // id == 5
        Epic epic3 = new Epic("Эпик 3", "Сдать финальное ТЗ 7-го спринта"); // id == 6
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addEpic(epic3);

        System.out.println("Все эпики:");
        System.out.println(taskManager.getAllEpics() + "\n"); // Получить все эпики

        System.out.println("Подзадачи эпика \"Большой переезд\":");
        Subtask subtask11 = new Subtask("Подзадача 1-1", "Собрать коробки", TaskStatus.NEW,
                4, 30, LocalDateTime.of(2022, 10, 4, 10, 0)); // id Subtask 7
        Subtask subtask12 = new Subtask("Подзадача 1-2", "Позвать друзей", TaskStatus.NEW,
                4, 15, LocalDateTime.of(2022, 10, 6, 11, 0));  // id Subtask 8
        Subtask subtask13 = new Subtask("Подзадача 1-3", "Загрузить все в машину", TaskStatus.NEW,
                4, 45, LocalDateTime.of(2022, 10, 8, 13, 0));  // id Subtask 9

        taskManager.addSubtask(subtask11);
        taskManager.addSubtask(subtask12);
        taskManager.addSubtask(subtask13);

        System.out.println(taskManager.getEpicById(4)); // Получить эпик "Большой переезд"
        System.out.println(taskManager.getSubtaskById(7));
        System.out.println(taskManager.getSubtaskById(8));
        System.out.println(taskManager.getSubtaskById(9));

        System.out.println("История: \n" + taskManager.getHistory() + "\n");

        System.out.println("Подзадачи эпика \"Цели по обучению\":");
        Subtask subtask21 = new Subtask("Подзадача 2-1", "Взять лист бумаги и ручку",
                TaskStatus.DONE, 5); // id Subtask 10
        Subtask subtask22 = new Subtask("Подзадача 2-2", "Сформулировать цели",
                TaskStatus.IN_PROGRESS, 5); // id Subtask 11
        Subtask subtask23 = new Subtask("Подзадача 2-3", "Повесить на видное место рядом с рабочим местом",
                TaskStatus.NEW, 5); // id Subtask 12

        taskManager.addSubtask(subtask21);
        taskManager.addSubtask(subtask22);
        taskManager.addSubtask(subtask23);

        System.out.println(taskManager.getEpicById(5)); // Получить эпик "Цели по обучению"
        System.out.println(taskManager.getSubtaskById(10));
        System.out.println(taskManager.getSubtaskById(11));
        System.out.println(taskManager.getSubtaskById(12));


        System.out.println("История: \n" + taskManager.getHistory() + "\n");

        System.out.println("Удаляем подзадачи №2-1, №2-2 по id");
        taskManager.removeSubtaskById(10);
        taskManager.removeSubtaskById(11);
        System.out.println("\n" + taskManager.getSubtaskOfTheEpic(epic2));

        System.out.println("История: \n" + taskManager.getHistory() + "\n");

        System.out.println("Подзадачи эпика \"ТЗ-7\":");
        Subtask subtask31 = new Subtask("Подзадача 3-1", "Собраться с силами =)",
                TaskStatus.DONE, 6, 10, LocalDateTime.of(2022, 10, 10, 19, 0)); // id Subtask 13
        Subtask subtask32 = new Subtask("Подзадача 3-2", "Внимательно прочитать ТЗ. Лучше несколько раз",
                TaskStatus.DONE, 6, 30, LocalDateTime.of(2022, 10, 15, 19, 30)); // id Subtask 14
        Subtask subtask33 = new Subtask("Подзадача 3-3", "Написать код и сдать в установленный дедлайн",
                TaskStatus.DONE, 6, 180, LocalDateTime.of(2022, 10, 17, 17, 45)); // id Subtask 15

        taskManager.addSubtask(subtask31);
        taskManager.addSubtask(subtask32);
        taskManager.addSubtask(subtask33);

        System.out.println(taskManager.getEpicById(6)); // Получить эпик "ТЗ-5"
        System.out.println(taskManager.getSubtaskById(13));
        System.out.println(taskManager.getSubtaskById(14));
        System.out.println(taskManager.getSubtaskById(15));

        System.out.println("История: " + taskManager.getHistory() + "\n");

        System.out.println("Удаляем эпик: \"Большой переезд\"");
        taskManager.removeEpicById(4);

        System.out.println("Все эпики:");
        System.out.println(taskManager.getAllEpics() + "\n"); // Получить все эпики
        System.out.println("История: " + taskManager.getHistory() + "\n");

        System.out.println("- - - - - - - - - - - - - - -");
        System.out.println("Проверяем PrioritizedTasks");
        System.out.println(taskManager.getPrioritizedTasks());*/

    }
}
