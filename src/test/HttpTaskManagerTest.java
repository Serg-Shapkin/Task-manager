import domain.Epic;
import domain.Subtask;
import domain.Task;
import domain.TaskStatus;
import http.HttpTaskManager;
import manager.FileBackedTasksManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import server.KVServer;

import java.io.IOException;
import java.util.List;

public class HttpTaskManagerTest extends TaskManagerTest <HttpTaskManager> {
    private final KVServer server = new KVServer();
    private final String url = "http://localhost:8078/";
    private String key;

    public HttpTaskManagerTest() throws IOException {
    }

    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        server.start();
        taskManager = new HttpTaskManager(url);
        key = taskManager.getKey();
    }

    @AfterEach
    void serverStop() {
        server.stop();
    }

    @Test
    void saveAndRecoveryTask() throws IOException, InterruptedException {
        Task task = new Task("Напоминание 1", "Сдать показания счетчиков", TaskStatus.NEW);
        taskManager.addTask(task);
        taskManager.removeAllTasks();

        HttpTaskManager httpTaskManager = HttpTaskManager.loadFromServer(url, key);
        final List<Task> tasks = httpTaskManager.getAllTasks();
        assertEquals(0, tasks.size(), "Задач быть не должно");
    }

    @Test
    void saveAndRecoveryEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Большой переезд");
        taskManager.addEpic(epic);

        FileBackedTasksManager fileBackedTasksManager = HttpTaskManager.loadFromServer(url, key);
        final List<Subtask> subtasks = fileBackedTasksManager.getAllSubtask();
        assertEquals(0, subtasks.size(),"Подзадачи быть не должно");

    }

}
