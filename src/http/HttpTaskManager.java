package http;

import domain.Epic;
import domain.Subtask;
import domain.Task;
import manager.FileBackedTasksManager;
import manager.ManagerSaveException;
import manager.TaskToCSVConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient client;
    public String key;

    public HttpTaskManager(String source, String key) throws IOException, InterruptedException {
        super(source);
        this.key = key;
        this.client = new KVTaskClient(this.FILE);
    }

    public HttpTaskManager(String source) throws IOException, InterruptedException {
        this(source, "defaultKey");
    }

    public String getKey() {
        return key;
    }

    public void save() {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("id,type,name,status,description,epic,duration,startTime,endTime\n");

            for(Task task : taskMap.values()) {
                stringBuilder.append(TaskToCSVConverter.toString(task));
            }

            for (Epic epic : epicMap.values()) {
                stringBuilder.append(TaskToCSVConverter.toString(epic));
            }

            for (Subtask subtask : subtaskMap.values()) {
                stringBuilder.append(TaskToCSVConverter.toString(subtask));
            }

            stringBuilder.append("\n");
            stringBuilder.append(TaskToCSVConverter.historyToString(historyManager));

            client.put(key, stringBuilder.toString());
        } catch (IOException | InterruptedException | ManagerSaveException exception) {
            throw new ManagerSaveException("Ошибка записи " + exception.getMessage());
        }
    }

    // восстанавливаем задачи с сервера
    public static HttpTaskManager loadFromServer(String source, String key) throws IOException, InterruptedException {
        HttpTaskManager httpTaskManager = new HttpTaskManager(source, key);
        String loadHistory = httpTaskManager.client.load(key);

        List<String> historyList = new ArrayList<>(List.of(loadHistory.split("\n\n")));
        List<String> tasksList = List.of(historyList.get(0).split("\n"));
        List<Task> tasks = new ArrayList<>();
        for (int i = 1; i < tasksList.size(); i ++) {
            tasks.add(TaskToCSVConverter.fromString(tasksList.get(i)));
        }
        loadTaskFromList(tasks, httpTaskManager);

        if (historyList.size() > 1) {
            String historyString = historyList.get(1);
            httpTaskManager.loadHistoryFromList(historyString);
        }
        httpTaskManager.save();
        return httpTaskManager;
    }

    private static HttpTaskManager loadTaskFromList(List<Task> tasks, HttpTaskManager httpTaskManager) {
        for (Task task : tasks) {
            if (task instanceof Epic) {
                httpTaskManager.addEpicFromFile((Epic) task);
            } else if (task instanceof Subtask) {
                httpTaskManager.addSubtaskFromFile((Subtask) task);
            } else {
                httpTaskManager.addTaskFromFile(task);
            }
        }
        return httpTaskManager;
    }

    private void addTaskFromFile(Task task) {
        super.addTask(task);
    }

    private void addEpicFromFile(Epic epic) {
        super.addTask(epic);
    }

    private void addSubtaskFromFile(Subtask subtask) {
        super.addTask(subtask);
    }

    private void loadHistoryFromList(String historyString) {
        List<Integer> tasksList = historyFromString(historyString);
        for (Integer tasks : tasksList) {
            super.getTaskById(tasks);
            super.getEpicById(tasks);
            super.getSubtaskById(tasks);
        }
    }

    private List<Integer> historyFromString(String historyString) {
        List<Integer> taskList = new ArrayList<>();
        String[] historyTask = historyString.split(",");
        for (String tasks : historyTask) {
            taskList.add(Integer.parseInt(tasks));
        }
        return taskList;
    }
}
