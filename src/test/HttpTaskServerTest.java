import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import domain.Epic;
import domain.Subtask;
import domain.Task;
import domain.TaskStatus;
import http.HttpTaskServer;
import manager.FileBackedTasksManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskServerTest {
    private HttpTaskServer httpTaskServer;
    private TaskManager taskManager;
    private HttpClient client;
    private final Gson gson = Managers.getGson();

    private Task defaultTask;
    private Epic defaultEpic;
    private Subtask defaultSubtask;

    private final String source = "http://localhost:8080/";

    Type taskType = new TypeToken<ArrayList<Task>>() {}.getType();
    Type epicType = new TypeToken<ArrayList<Epic>>() {}.getType();
    Type subtaskType = new TypeToken<ArrayList<Subtask>>() {}.getType();

    @BeforeEach // каждый раз перед
    void setUp() throws IOException {
        taskManager = new FileBackedTasksManager("src/files/history.csv");
        client = HttpClient.newHttpClient();
        httpTaskServer = new HttpTaskServer(taskManager);

        defaultTask = new Task("Напоминание 1", "Сдать показания счетчиков", TaskStatus.NEW);
        defaultEpic = new Epic("Эпик 1", "Большой переезд");
        defaultSubtask = new Subtask("Подзадача 1-1", "Собрать коробки", TaskStatus.NEW,
                0);

        httpTaskServer.start();
    }

    @AfterEach
    void serverStop() {
        httpTaskServer.stop();
        System.out.println("Сервер остановлен.");
    }

    @Test
    void getAllTasks() throws IOException, InterruptedException {
        Task task1 = new Task("Напоминание 1", "Сдать показания счетчиков", TaskStatus.NEW);
        Task task2 = new Task("Напоминание 2", "Оплатить квитанции КУ", TaskStatus.IN_PROGRESS);

        sendPOST("tasks/task", task1);
        sendPOST("tasks/task", task2);

        HttpResponse<String> responseGet = sendGET("tasks/task", 0);
        List<Task> tasksList = gson.fromJson(responseGet.body(), taskType);
        assertEquals(2, tasksList.size(), "Неверное количество задач.");
    }

    @Test
    void removeAllTasks() throws IOException, InterruptedException {
        Task task1 = new Task("Напоминание 1", "Сдать показания счетчиков", TaskStatus.NEW);
        Task task2 = new Task("Напоминание 2", "Оплатить квитанции КУ", TaskStatus.IN_PROGRESS);

        sendPOST("tasks/task", task1);
        sendPOST("tasks/task", task2);

        HttpResponse<String> responseDelete = sendDELETE("tasks/task", 0);
        List<Task> tasksList = gson.fromJson(responseDelete.body(), taskType);
        assertNull(tasksList, "Задачи не удалены.");
    }

    @Test
    void getTaskById() throws IOException, InterruptedException {
        sendPOST("tasks/task", defaultTask);
        int idTask = taskManager.getAllTasks().get(0).getIdTask();

        HttpResponse<String> responseGet = sendGET("tasks/task", idTask);
        assertEquals(200, responseGet.statusCode());

        Task task = gson.fromJson(responseGet.body(), Task.class);
        assertNotNull(task, "Задача не возвращается.");
        assertNotNull(task, "Задача не найдена.");
        assertEquals(taskManager.getAllTasks().get(0), task, "Задачи не совпадают.");
    }

    @Test
    void addTask() {
        try {
            HttpResponse<String> responsePost = sendPOST("tasks/task", defaultTask);
            assertEquals(200, responsePost.statusCode());

            HttpResponse<String> responseGet = sendGET("tasks/task",0);
            assertEquals(200, responseGet.statusCode());

            List<Task> tasksList = gson.fromJson(responseGet.body(), taskType);
            Task task = tasksList.get(0);
            assertNotNull(task, "Задача не возвращается.");

            defaultTask.setIdTask(task.getIdTask());
            assertEquals(defaultTask, task, "Задачи не совпадают.");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void updateTask() throws IOException, InterruptedException {
        sendPOST("tasks/task", defaultTask);
        Task defaultTaskUpdate = new Task("Обновленное напоминание 1", "Сдать показания счетчиков",
                TaskStatus.NEW, 10, defaultTask.getStartTime());
        defaultTaskUpdate.setIdTask(taskManager.getAllTasks().get(0).getIdTask());

        HttpResponse<String> updateResponsePost = sendPOST("tasks/task", defaultTaskUpdate);
        assertEquals(200, updateResponsePost.statusCode());

        HttpResponse<String> responseGet = sendGET("tasks/task", 0);
        assertEquals(200, responseGet.statusCode());

        List<Task> tasksList = gson.fromJson(responseGet.body(), taskType);
        Task task = tasksList.get(0);
        assertNotNull(task, "Задача не возвращается.");

        defaultTaskUpdate.setIdTask(task.getIdTask());
        assertEquals(defaultTaskUpdate, task, "Задачи не совпадают.");
    }

    @Test
    void removeTaskById() throws IOException, InterruptedException {
        sendPOST("tasks/task", defaultTask);

        int idTask = taskManager.getAllTasks().get(0).getIdTask();
        sendDELETE("tasks/task/?id=", idTask);
        assertNull(taskManager.getTaskById(idTask), "Задача не удалена.");
    }

    @Test
    void getAllEpics() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Эпик 1", "Большой переезд");
        Epic epic2 = new Epic("Эпик 2", "Записать цели по обучению на курсе");

        sendPOST("tasks/epic", epic1);
        sendPOST("tasks/epic", epic2);

        HttpResponse<String> responseGet = sendGET("tasks/epic", 0);
        List<Epic> epicList = gson.fromJson(responseGet.body(), epicType);
        assertEquals(2, epicList.size(), "Неверное количество задач.");
    }

    @Test
    void removeAllEpics() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Эпик 1", "Большой переезд");
        Epic epic2 = new Epic("Эпик 2", "Записать цели по обучению на курсе");

        sendPOST("tasks/epic", epic1);
        sendPOST("tasks/epic", epic2);

        HttpResponse<String> responseDelete = sendGET("tasks/epic", 0);
        List<Epic> epicList = gson.fromJson(responseDelete.body(), epicType);
        assertNull(epicList, "Задачи не удалены.");
    }

    @Test
    void getEpicById() throws IOException, InterruptedException {
        sendPOST("tasks/epic", defaultEpic);
        int idEpic = taskManager.getAllEpics().get(0).getIdTask();

        HttpResponse<String> responseGet = sendGET("tasks/epic", idEpic);

        assertEquals(200, responseGet.statusCode());
        Epic epic = gson.fromJson(responseGet.body(), Epic.class);
        assertNotNull(epic, "Эпик не возвращается.");
        assertNotNull(epic, "Эпик не найден.");
        assertEquals(taskManager.getAllEpics().get(0), epic, "Эпики не совпадают.");
    }

    @Test
    void addEpic() throws IOException, InterruptedException {
        HttpResponse<String> responsePost = sendPOST("tasks/epic", defaultEpic);
        assertEquals(200, responsePost.statusCode());

        HttpResponse<String> responseGet = sendGET("tasks/epic", 0);
        assertEquals(200, responseGet.statusCode());

        List<Epic> epicList = gson.fromJson(responseGet.body(), epicType);
        Epic epic = epicList.get(0);
        assertNotNull(epic, "Эпик не возвращается.");

        defaultEpic.setIdTask(epic.getIdTask());
        assertEquals(defaultEpic, epic, "Эпики не совпадают.");
    }

    @Test
    void updateEpic() throws IOException, InterruptedException {
        sendPOST("tasks/epic", defaultEpic);

        Epic defaultEpicUpdate = new Epic("Обновленный эпик 1", "Большой переезд");
        defaultEpicUpdate.setIdTask(taskManager.getAllEpics().get(0).getIdTask());

        HttpResponse<String> updateResponsePost = sendPOST("tasks/epic", defaultEpicUpdate);
        assertEquals(200, updateResponsePost.statusCode());

        HttpResponse<String> responseGet = sendGET("tasks/epic", 0);
        assertEquals(200, responseGet.statusCode());

        List<Epic> epicList = gson.fromJson(responseGet.body(), epicType);
        Epic epic = epicList.get(0);
        assertNotNull(epic, "Эпик не возвращается.");

        defaultEpicUpdate.setIdTask(epic.getIdTask());
        assertEquals(defaultEpicUpdate, epic, "Эпики не совпадают.");
    }

    @Test
    void removeEpicById() throws IOException, InterruptedException {
        sendPOST("tasks/epic", defaultEpic);

        int idEpic = taskManager.getAllEpics().get(0).getIdTask();
        sendDELETE("tasks/epic/?id=", idEpic);
        assertNull(taskManager.getEpicById(idEpic), "Эпик не удален.");
    }

    @Test
    void getAllSubtask() throws IOException, InterruptedException {
        addEpicToTaskManager(defaultEpic);

        int idEpic = taskManager.getAllEpics().get(0).getIdTask();

        Subtask subtask1 = new Subtask("Подзадача 1-1", "Собрать коробки", TaskStatus.NEW, 0);
        Subtask subtask2 = new Subtask("Подзадача 1-2", "Позвать друзей", TaskStatus.NEW, 0);

        subtask1.setIdTask(idEpic);
        subtask2.setIdTask(idEpic);

        sendPOST("tasks/subtask", subtask1);
        sendPOST("tasks/subtask", subtask2);

        HttpResponse<String> responseGet = sendGET("tasks/subtask", 0);
        List<Subtask> subtaskList = gson.fromJson(responseGet.body(), subtaskType);
        assertEquals(2, subtaskList.size(), "Неверное количество подзадач.");
    }

    @Test
    void removeAllSubtasks() throws IOException, InterruptedException {
        addEpicToTaskManager(defaultEpic);
        int idEpic = taskManager.getAllEpics().get(0).getIdTask();

        Subtask subtask1 = new Subtask("Подзадача 1-1", "Собрать коробки", TaskStatus.NEW, idEpic);
        Subtask subtask2 = new Subtask("Подзадача 1-2", "Позвать друзей", TaskStatus.NEW, idEpic);

        sendPOST("tasks/subtask", subtask1);
        sendPOST("tasks/subtask", subtask2);

        HttpResponse<String> responseDelete = sendDELETE("tasks/subtask", 0);
        List<Subtask> subtaskList = gson.fromJson(responseDelete.body(), subtaskType);
        assertNull(subtaskList, "Задачи не удалены.");
    }

    @Test
    void getSubtaskById() throws IOException, InterruptedException {
        addEpicToTaskManager(defaultEpic);
        int idEpic = taskManager.getAllEpics().get(0).getIdTask();
        defaultSubtask.setIdTask(idEpic);

        sendPOST("tasks/subtask", defaultSubtask);
        int idSubtask = taskManager.getAllSubtask().get(0).getIdTask();

        HttpResponse<String> responseGet = sendGET("tasks/subtask", idSubtask);

        assertEquals(200, responseGet.statusCode());
        Subtask subtask = gson.fromJson(responseGet.body(), Subtask.class);

        assertNotNull(subtask, "Подзадача не возвращается.");
        assertNotNull(subtask, "Подзадача не найдена.");
        assertEquals(taskManager.getAllTasks().get(0), subtask, "Подзадачи не совпадают.");
    }

    @Test
    void addSubtask() throws IOException, InterruptedException {
        addEpicToTaskManager(defaultEpic);
        int idEpic = taskManager.getAllEpics().get(0).getIdTask();
        defaultSubtask.setIdTask(idEpic); // задали id подзадаче

        HttpResponse<String> responsePost = sendPOST("tasks/subtask", defaultSubtask);
        assertEquals(200, responsePost.statusCode());

        HttpResponse<String> responseGet = sendGET("tasks/subtask", 0);
        assertEquals(200, responseGet.statusCode());

        List<Subtask> subtaskList = gson.fromJson(responseGet.body(), subtaskType);
        Subtask subtask = subtaskList.get(0);
        assertNotNull(subtask, "Подзадача не возвращается.");

        defaultSubtask.setIdTask(subtask.getIdTask());
        assertEquals(defaultSubtask, subtask, "Подзадачи не совпадают.");
    }

    @Test
    void updateSubtask() throws IOException, InterruptedException {
        addEpicToTaskManager(defaultEpic);

        int idEpic = taskManager.getAllEpics().get(0).getIdTask();
        defaultSubtask.setIdTask(idEpic); // задали id подзадаче

        sendPOST("tasks/subtask", defaultSubtask);
        Subtask defaultSubtaskUpdate = new Subtask("Обновленная подзадача 1-1", "Собрать коробки",
                TaskStatus.NEW, 0);

        defaultSubtaskUpdate.setIdTask(taskManager.getAllSubtask().get(0).getIdTask());
        defaultSubtaskUpdate.setIdTask(idEpic);

        HttpResponse<String> updateResponsePost = sendPOST("tasks/subtask", defaultSubtaskUpdate);
        assertEquals(200, updateResponsePost.statusCode());

        HttpResponse<String> responseGet = sendGET("tasks/subtask", 0);
        assertEquals(200, responseGet.statusCode());

        List<Subtask> subtaskList = gson.fromJson(responseGet.body(), subtaskType);
        Subtask subtask = subtaskList.get(0);
        assertNotNull(subtask, "Подзадача не возвращается.");

        defaultSubtaskUpdate.setIdTask(subtask.getIdTask());
        assertEquals(defaultSubtaskUpdate, subtask, "Подзадачи не совпадают.");
    }

    @Test
    void removeSubtaskById() throws IOException, InterruptedException {
        addEpicToTaskManager(defaultEpic);

        int idEpic = taskManager.getAllEpics().get(0).getIdTask();
        defaultSubtask.setIdTask(idEpic);

        sendPOST("task/subtask", defaultSubtask);

        int idSubtask = taskManager.getAllSubtask().get(0).getIdTask();
        sendDELETE("tasks/subtask/?id=", idSubtask);
        assertNull(taskManager.getSubtaskById(idSubtask), "Подзадача не удалена.");
    }

    @Test
    void getSubtaskOfTheEpic() throws IOException, InterruptedException {
        Epic newDefaultEpic = defaultEpic;
        addEpicToTaskManager(defaultEpic);
        addEpicToTaskManager(newDefaultEpic);

        int idNewEpic = taskManager.getAllEpics().get(1).getIdTask();
        Subtask subtask1 = new Subtask("Подзадача 1-1", "Собрать коробки", TaskStatus.NEW, idNewEpic);
        Subtask subtask2 = new Subtask("Подзадача 1-2", "Позвать друзей", TaskStatus.NEW, idNewEpic);

        sendPOST("tasks/subtask", subtask1);
        sendPOST("tasks/subtask", subtask2);

        HttpResponse<String> responseGet = sendGET("tasks/subtask/epic", idNewEpic);
        List<Subtask> subtaskList = gson.fromJson(responseGet.body(), subtaskType);
        assertNotNull(subtaskList, "Подзадачи не возвращаются.");
        assertEquals(2, subtaskList.size(), "Неверное количество подзадач");
    }

    @Test
    void getHistory() throws IOException, InterruptedException {
        Task task1 = new Task("Напоминание 1", "Оплатить квитанции КУ", TaskStatus.IN_PROGRESS);
        Task task2 = new Task("Напоминание 2", "Оплатить квитанции КУ", TaskStatus.IN_PROGRESS);
        Task task3 = new Task("Напоминание 3", "Оплатить квитанции КУ", TaskStatus.IN_PROGRESS);

        sendPOST("tasks/task", task1);
        sendPOST("tasks/task", task2);
        sendPOST("tasks/task", task3);

        int idTask1 = taskManager.getTaskById(1).getIdTask();
        int idTask2 = taskManager.getTaskById(2).getIdTask();
        int idTask3 = taskManager.getTaskById(3).getIdTask();

        sendGET("tasks/task", idTask1);
        sendGET("tasks/task", idTask2);
        sendGET("tasks/task", idTask3);

        HttpResponse<String> responseGetHistory = sendGET("tasks/history", 0);
        List<Task> taskList = gson.fromJson(responseGetHistory.body(), taskType);

        assertNotNull(taskList, "Задачи не возвращаются.");
        assertEquals(3, taskList.size(), "Неверное количество задач.");
        assertEquals(taskManager.getAllTasks().get(1), taskList.get(0), "Задачи не совпадают.");
    }

    @Test
    void getPrioritizedTasks() throws IOException, InterruptedException {
        Task task1 = new Task("Напоминание 1", "Сдать показания счетчиков", TaskStatus.NEW,
                5, LocalDateTime.of(2022, 10, 25, 14, 0));
        // id=2
        Task task2 = new Task("Напоминание 2", "Оплатить квитанции КУ", TaskStatus.IN_PROGRESS,
                5, LocalDateTime.of(2022, 10, 25, 15, 0));
        //id=3
        Task task3 = new Task("Напоминание 3", "Почесать кота", TaskStatus.DONE,
                5, LocalDateTime.of(2022, 10, 25, 16, 0));

        sendPOST("tasks/task", task1);
        sendPOST("tasks/task", task2);
        sendPOST("tasks/task", task3);

        HttpResponse<String> responseGet = sendGET("tasks",0);
        List<Task> taskList = gson.fromJson(responseGet.body(), taskType);
        assertNotNull(taskList, "Задачи не возвращаются.");
        assertEquals(3, taskList.size(), "Неверное количество задач.");
        assertEquals(taskManager.getAllTasks().get(0), taskList.get(1), "Задачи не совпадают.");
    }

    private HttpResponse<String> sendPOST(String path, Task task) throws IOException, InterruptedException {
        URI uri = URI.create(source + path);
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(body)
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> sendGET(String path, int id) throws IOException, InterruptedException {
        HttpRequest request;
        URI uri;
        if (id == 0) {
            uri = URI.create(source + path);
        } else {
            uri = URI.create(source + path + "/?id=" + id);
        }
        request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> sendDELETE(String path, int id) throws IOException, InterruptedException {
        HttpRequest request;
        URI uri;
        if (id != 0) {
            uri = URI.create(source + path + id);
        } else {
            uri = URI.create(source + path);
        }
        request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    // добавили Epic в TaskManager
    private void addEpicToTaskManager(Task task) throws IOException, InterruptedException {
        URI uriEpic = URI.create(source + "tasks/epic");
        String jsonEpic = gson.toJson(task);
        final HttpRequest.BodyPublisher bodyEpic = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest requestEpic = HttpRequest.newBuilder()
                .uri(uriEpic)
                .POST(bodyEpic)
                .build();
        client.send(requestEpic, HttpResponse.BodyHandlers.ofString());
    }
}
