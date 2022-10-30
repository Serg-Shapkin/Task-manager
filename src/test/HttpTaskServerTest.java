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

    private final String source = "http://localhost:8081/";

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
    void addTask() {
        try {
            HttpResponse<String> responsePost = sendPOST("tasks/task", defaultTask);
            assertEquals(200, responsePost.statusCode());

            HttpResponse<String> responseGET = sendGET("tasks/task",0);
            assertEquals(200, responseGET.statusCode());

            List<Task> tasksList = gson.fromJson(responseGET.body(), taskType);
            Task task = tasksList.get(0);
            assertNotNull(task, "Задача не возвращается.");

            defaultTask.setIdTask(task.getIdTask());
            assertEquals(defaultTask, task, "Задачи не совпадают.");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
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

}
