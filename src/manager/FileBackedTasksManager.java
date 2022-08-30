package manager;

import domain.Epic;
import domain.Subtask;
import domain.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import domain.TaskStatus;


public class FileBackedTasksManager extends InMemoryTaskManager{

    protected TaskToCSVConverter taskToCSV = new TaskToCSVConverter(); // класс для преобразования тасок в строку и обратно
    private static final String FILE = "/Users/Serg/Desktop/dev/java-kanban/src/history.csv"; // как сделать относительную ссылку?

    private static final File file = new File(FILE);

    public static void main(String[] args) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        fileBackedTasksManager.loadFromFile(file);

        Task task1 = new Task("Напоминание 1", "Сдать показания счетчиков", TaskStatus.NEW);
        Task task2 = new Task("Напоминание 2", "Оплатить квитанции КУ", TaskStatus.IN_PROGRESS);

        fileBackedTasksManager.addTask(task1);
        fileBackedTasksManager.addTask(task2);

        System.out.println(fileBackedTasksManager.getAllTasks());
        System.out.println("- - - - - - - - - - - - - - -");

        System.out.println("Запросили задачу №1 по id");
        fileBackedTasksManager.getTaskById(1);
        System.out.println("Запросили задачу №2 по id");
        fileBackedTasksManager.getTaskById(2);
        System.out.println("История №1: " + fileBackedTasksManager.getHistory());

        System.out.println("Еще раз запросили задачу №1 по id");
        fileBackedTasksManager.getTaskById(1);
        System.out.println("Еще раз запросили задачу №2 по id");
        fileBackedTasksManager.getTaskById(2);
        System.out.println("История №2: " + fileBackedTasksManager.getHistory());

        System.out.println("Удаляем задачу №2 по id");
        fileBackedTasksManager.removeTaskById(2);
        System.out.println("История после удаления: " + fileBackedTasksManager.getHistory());

        System.out.println("- - - - - - - - - - - - - - -");

        Epic epic1 = new Epic("Эпик 1", "Большой переезд."); // id Task 3
        Epic epic2 = new Epic("Эпик 2", "Записать цели по обучению на курсе."); // id Task 4
        Epic epic3 = new Epic("Эпик 3", "Сдать финальное ТЗ 5-го спринта"); // id Task 5
        fileBackedTasksManager.addEpic(epic1);
        fileBackedTasksManager.addEpic(epic2);
        fileBackedTasksManager.addEpic(epic3);

        System.out.println("Все Эпики:");
        System.out.println(fileBackedTasksManager.getAllEpics() + "\n"); // Получить все эпики

        System.out.println("Эпик: \"Большой переезд\"");
        Subtask subtask11 = new Subtask("Подзадача 1-1", "Собрать коробки",
                TaskStatus.NEW, 3); // id Subtask 6
        Subtask subtask12 = new Subtask("Подзадача 1-2", "Позвать друзей",
                TaskStatus.NEW, 3);  // id Subtask 7
        Subtask subtask13 = new Subtask("Подзадача 1-3", "Загрузить все в машину",
                TaskStatus.NEW, 3);  // id Subtask 8

        fileBackedTasksManager.addSubtask(subtask11);
        fileBackedTasksManager.addSubtask(subtask12);
        fileBackedTasksManager.addSubtask(subtask13);

        System.out.println(fileBackedTasksManager.getEpicById(3)); // Получить эпик "Большой переезд"
        System.out.println(fileBackedTasksManager.getSubtaskById(6));
        System.out.println(fileBackedTasksManager.getSubtaskById(7));
        System.out.println(fileBackedTasksManager.getSubtaskById(8));

        System.out.println("История: " + fileBackedTasksManager.getHistory() + "\n");

        System.out.println("Эпик: \"Цели по обучению\"");
        Subtask subtask21 = new Subtask("Подзадача 2-1", "Взять лист бумаги и ручку",
                TaskStatus.DONE, 4); // id Subtask 9
        Subtask subtask22 = new Subtask("Подзадача 2-2", "Сформулировать цели",
                TaskStatus.IN_PROGRESS, 4); // id Subtask 10
        Subtask subtask23 = new Subtask("Подзадача 2-3", "Повесить на видное место рядом с рабочим местом",
                TaskStatus.NEW, 4); // id Subtask 11

        fileBackedTasksManager.addSubtask(subtask21);
        fileBackedTasksManager.addSubtask(subtask22);
        fileBackedTasksManager.addSubtask(subtask23);

        System.out.println(fileBackedTasksManager.getEpicById(4)); // Получить эпик "Цели по обучению"

        System.out.println(fileBackedTasksManager.getSubtaskById(9));
        System.out.println(fileBackedTasksManager.getSubtaskById(10));
        System.out.println(fileBackedTasksManager.getSubtaskById(11));

        System.out.println("История: " + fileBackedTasksManager.getHistory() + "\n");

        fileBackedTasksManager.removeSubtaskById(9); // удалили сабтаску из второго эпика // ???
        System.out.println("\n" + fileBackedTasksManager.subtaskOfTheEpic(epic2));

        System.out.println("История: " + fileBackedTasksManager.getHistory() + "\n");


        System.out.println("Эпик: \"ТЗ-5\"");
        Subtask subtask31 = new Subtask("Подзадача 3-1", "Собраться с силами =)",
                TaskStatus.DONE, 5); // id Subtask 12
        Subtask subtask32 = new Subtask("Подзадача 3-2", "Внимательно прочитать ТЗ. Лучше несколько раз.",
                TaskStatus.DONE, 5); // id Subtask 13
        Subtask subtask33 = new Subtask("Подзадача 3-3", "Неспеша написать код и сдать в установленный дедлайн.",
                TaskStatus.DONE, 5); // id Subtask 14

        fileBackedTasksManager.addSubtask(subtask31);
        fileBackedTasksManager.addSubtask(subtask32);
        fileBackedTasksManager.addSubtask(subtask33);

        System.out.println(fileBackedTasksManager.getEpicById(5)); // Получить эпик "ТЗ-5"
        System.out.println(fileBackedTasksManager.getSubtaskById(12));
        System.out.println(fileBackedTasksManager.getSubtaskById(13));
        System.out.println(fileBackedTasksManager.getSubtaskById(14));

        System.out.println("История: " + fileBackedTasksManager.getHistory() + "\n");

        System.out.println("Удаляем эпик: \"Большой переезд\""); // ???
        fileBackedTasksManager.removeEpicById(3);

        System.out.println("Все Эпики:");
        System.out.println(fileBackedTasksManager.getAllEpics() + "\n"); // Получить все эпики
        System.out.println("История: " + fileBackedTasksManager.getHistory() + "\n");
    }


    protected void save() { // сохраняем задачи в файл
        try (FileWriter fileWriter = new FileWriter(FILE)) {
            fileWriter.write("id,type,name,status,description,epic" + "\n");

            for(Task task : taskMap.values()) {
                fileWriter.write(taskToCSV.toString(task));
            }

            for (Epic epic : epicMap.values()) {
                fileWriter.write(taskToCSV.toString(epic));
            }

            for (Subtask subtask : subtaskMap.values()) {
                fileWriter.write(taskToCSV.toString(subtask));
            }

            fileWriter.write("\n"); // пустая строка перед историей просмотров
            fileWriter.write(taskToCSV.historyToString(historyManager));

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи.");
        }
    }

    protected FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        try {
            List<String> strings = Files.readAllLines(file.toPath()); // считали все строки файла и сохранили в коллекцию
            List<Integer> loadHistory = new ArrayList<>();
            for (int i = 1; i < strings.size(); i++) { // 1 - пропустил первую строку т.к. заголовок
                String line = strings.get(i);
                if (line.isEmpty()){
                    loadHistory = taskToCSV.historyFromString(strings.get(i+1)); // если строка пустая, то переходим на следующую и парсим историю
                    break;
                }
                Task task = taskToCSV.fromString(line);
                fileBackedTasksManager.addTask(task); // восстановили задачи
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла.");
        }
        return fileBackedTasksManager;
    }

    // Task
    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    // Epic
    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    // Subtask
    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }
}
