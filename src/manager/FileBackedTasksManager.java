package manager;

import domain.Epic;
import domain.Subtask;
import domain.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {

    protected static TaskToCSVConverter taskToCSV = new TaskToCSVConverter(); // класс для преобразования тасок в строку и обратно
/*    private static final String FILE = "src/files/history.csv"; // относительная ссылка

    private static File file;

    public FileBackedTasksManager(File file) { // конструктор
        this.file = file;
    }*/

    protected final String FILE;
    private static final String SOURCE = "http://localhost:8078/";

    public FileBackedTasksManager(String source) {
        this.FILE = source;
    }


    protected void save() { // сохраняем задачи в файл
        try (FileWriter fileWriter = new FileWriter(FILE)) {
            fileWriter.write("ID,TYPE,NAME,STATUS,DESCRIPTION,EPIC_ID,DURATION,START_TIME,END_TIME" + "\n");

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
            fileWriter.write(TaskToCSVConverter.historyToString(historyManager));

        } catch (IOException | ManagerSaveException exception) {
            throw new ManagerSaveException("Ошибка записи " + exception.getMessage());
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) { // восстанавливаем задачи из файла
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(SOURCE);
        try {
            List<String> strings = Files.readAllLines(file.toPath()); // считали все строки файла и сохранили в коллекцию
            List<Integer> loadHistory = new ArrayList<>();
            for (int i = 1; i < strings.size(); i++) { // пропустил первую строку т.к. заголовок
                String line = strings.get(i);
                if (line.isEmpty()){ // если строка пустая,
                    loadHistory = TaskToCSVConverter.historyFromString(strings.get(i+1)); // то переходим на следующую и парсим историю
                    break;
                }
                Task task = taskToCSV.fromString(line);
                fileBackedTasksManager.addTask(task); // восстановили задачи
            }

        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка чтения файла.");
        }
        return fileBackedTasksManager;
    }

    // Task
    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = null;
        try {
            task = super.getTaskById(id);
            save();
        } catch (NullPointerException exception) {
            System.out.println("Задачи с" + id + "нет.");
        }
        return task;
    }

    @Override
    public int addTask(Task task) {
        super.addTask(task);
        save();
        return task.getIdTask();
    }

     @Override
     public void updateTask(Task task) {
        super.updateTask(task);
        save();
     }

    @Override
    public void removeTaskById(int id) {
        try {
            super.removeTaskById(id);
            save();
        } catch (NullPointerException exception) {
            System.out.println("Задачи с" + id + "нет.");
        }
    }

    // Epic
    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = null;
        try {
            epic = super.getEpicById(id);
            save();
        } catch (NullPointerException exception) {
            System.out.println("Задачи с" + id + "нет.");
        }
        return epic;
    }

    @Override
    public int addEpic(Epic epic) {
        super.addEpic(epic);
        save();
        return epic.getIdTask();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        try {
            super.removeEpicById(id);
            save();
        } catch (NullPointerException exception) {
            System.out.println("Эпика с" + id + "нет");
        }
    }

    // Subtask
    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = null;
        try {
            subtask = super.getSubtaskById(id);
            save();
        } catch (NullPointerException exception) {
            System.out.println("Эпика с" + id + "нет");
        }
        return subtask;
    }

    @Override
    public int addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
        return subtask.getIdTask();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void removeSubtaskById(int id) {
        try {
            super.removeSubtaskById(id);
            save();
        } catch (NullPointerException exception) {
            System.out.println("Подзадачи с" + id + "нет");
        }
    }
}
