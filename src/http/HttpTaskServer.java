package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import domain.Epic;
import domain.Subtask;
import domain.Task;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;


public class HttpTaskServer {
    private static final int PORT = 8081;
    private final HttpServer server;
    private final Gson gson;
    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handler);
        System.out.println("Сервер запущен на " + PORT + " порту");
    }

    public HttpTaskServer() throws IOException, InterruptedException {
        this.taskManager = Managers.getDefault();
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handler);
        System.out.println("Сервер запущен на " + PORT + " порту");
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        HttpTaskServer server = new HttpTaskServer();
        server.start();
    }

    private void handler(HttpExchange httpExchange) {
        try {
            System.out.println("Обработка запроса: " + httpExchange.getRequestMethod() + " " + httpExchange.getRequestURI());
            String requestMethod = httpExchange.getRequestMethod(); // получили HTTP-метод
            String path = httpExchange.getRequestURI().getPath();   // получили путь из экземпляра URI
            String query = httpExchange.getRequestURI().getQuery(); // получили запрос из экземпляра URI
            switch (requestMethod) {
                case "GET": {
                    handleGet(httpExchange, path, query);
                    break;
                }
                case "POST": {
                    handlePost(httpExchange, path);
                    break;
                }
                case "DELETE": {
                    handleDelete(httpExchange, path, query);
                    break;
                }
                default: {
                    System.out.println("Используется запрос " + requestMethod + ".\n" +
                            "Возможны только GET, POST, DELETE запросы");
                    httpExchange.sendResponseHeaders(405, 0);
                }
            }
        } catch (Exception exception) {
            System.out.println("Ошибка обработки запроса /tasks");
        }
    }

    private void handleGet(HttpExchange httpExchange, String path, String query) throws IOException {
        // getAllTasks()
        if (Pattern.matches("^/tasks/task", path)) {
            final String response = gson.toJson(taskManager.getAllTasks());
            sendMessage(httpExchange, response);
            return;
        }

        // getTaskById()
        if (Pattern.matches("^/tasks/task/", path)) {
            String idString = query.replaceFirst("id=", "");
            int id = idParse(idString);
            final String response = gson.toJson(taskManager.getTaskById(id));
            sendMessage(httpExchange, response);
            return;
        }

        // getAllEpics()
            if (Pattern.matches("^/tasks/epic", path)) {
            final String response = gson.toJson(taskManager.getAllEpics());
            sendMessage(httpExchange, response);
            return;
        }

        // getEpicById()
        if (Pattern.matches("^/tasks/epic/", path)) {
            String idString = query.replaceFirst("id=", "");
            int id = idParse(idString);
            final String response = gson.toJson(taskManager.getEpicById(id));
            sendMessage(httpExchange, response);
            return;
        }

        // getAllSubtask()
        if (Pattern.matches("^/tasks/subtask", path)) {
            final String response = gson.toJson(taskManager.getAllSubtask());
            sendMessage(httpExchange, response);
            return;
        }

        // getSubtaskById()
        if (Pattern.matches("^/tasks/subtask/", path)) {
            String idString = query.replaceFirst("id=", "");
            int id = idParse(idString);
            final String response = gson.toJson(taskManager.getSubtaskById(id));
            sendMessage(httpExchange, response);
            return;
        }

        // getSubtaskOfTheEpic()
        if (Pattern.matches("^/tasks/subtask/epic/", path)) {
            String idString = query.replaceFirst("id=", "");
            int id = idParse(idString);
            final String response = gson.toJson(taskManager.getSubtaskOfTheEpic(taskManager.getEpicById(id)));
            sendMessage(httpExchange, response);
            return;
        }

        // getHistory()
        if (Pattern.matches("^/tasks/history", path)) {
            final String response = gson.toJson(taskManager.getHistory());
            sendMessage(httpExchange, response);
            return;
        }

        // getPrioritizedTasks()
        if (Pattern.matches("^/tasks", path)) {
            final String response = gson.toJson(taskManager.getPrioritizedTasks());
            sendMessage(httpExchange, response);
            return;
        }

        System.out.println("Ошибка при обработке GET запроса.");
        httpExchange.sendResponseHeaders(404, 0);
    }

    private void handlePost(HttpExchange httpExchange, String path) throws IOException {
        // addTask(), updateTask()
        if (Pattern.matches("^/tasks/task", path)) {
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(),StandardCharsets.UTF_8);

            try {
                Task task = gson.fromJson(body, Task.class);
                if (taskManager.getTaskById(task.getIdTask()) != null) {
                    taskManager.updateTask(task);
                    System.out.println("Была обновлена задача с id=" + task.getIdTask());
                    httpExchange.sendResponseHeaders(200, 0);
                } else {
                    taskManager.addTask(task);
                    System.out.println("Была добавлена задача с id=" + task.getIdTask());

                    System.out.println();

                    httpExchange.sendResponseHeaders(200, 0);
                }
            } catch(Exception exception) {
                System.out.println("Ошибка при обновлении задачи");
                httpExchange.sendResponseHeaders(404, 0);
            }
            return;
        }

        // addEpic(), updateEpic()
        if (Pattern.matches("^/tasks/epic", path)) {
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(),StandardCharsets.UTF_8);

            try {
                Epic epic = gson.fromJson(body, Epic.class);
                if (taskManager.getEpicById(epic.getIdTask()) != null) {
                    taskManager.updateEpic(epic);
                    System.out.println("Был обновлен эпик с id=" + epic.getIdTask());
                    httpExchange.sendResponseHeaders(200, 0);
                } else {
                    taskManager.addEpic(epic);
                    System.out.println("Была добавлен эпик с id=" + epic.getIdTask());
                    httpExchange.sendResponseHeaders(200, 0);
                }
            } catch(Exception exception) {
                System.out.println("Ошибка при обновлении эпика'");
                httpExchange.sendResponseHeaders(404, 0);
            }
            return;
        }

        // addSubtask(), updateSubtask()
        if (Pattern.matches("^/tasks/subtask", path)) {
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(),StandardCharsets.UTF_8);

            try {
                Subtask subtask = gson.fromJson(body, Subtask.class);
                if (taskManager.getSubtaskById(subtask.getIdTask()) != null) {
                    taskManager.updateSubtask(subtask);
                    System.out.println("Была обновлена подзадача с id=" + subtask.getIdTask());
                    httpExchange.sendResponseHeaders(200, 0);
                } else {
                    taskManager.addSubtask(subtask);
                    System.out.println("Была добавлена подзадача с id=" + subtask.getIdTask());
                    httpExchange.sendResponseHeaders(200, 0);
                }
            } catch(Exception exception) {
                System.out.println("Ошибка при обновлении подзадачи");
                httpExchange.sendResponseHeaders(404, 0);
            }
            return;
        }

    }

    private void handleDelete(HttpExchange httpExchange, String path, String query) throws IOException {

        // removeAllTasks()
        if (Pattern.matches("^/tasks/task", path)) {
            try {
                taskManager.removeAllTasks();
            } catch(Exception exception) {
                System.out.println("Ошибка при удалении задачи");
                httpExchange.sendResponseHeaders(404, 0);
            }
        }

        // removeTaskById()
        if (Pattern.matches("^/tasks/task", path)) {
            String idString = query.replaceFirst("id=", "");
            int id = idParse(idString);
            try {
                taskManager.removeTaskById(id);
            } catch(Exception exception) {
                System.out.println("Ошибка при удалении задачи по id");
                httpExchange.sendResponseHeaders(404, 0);
            }
        }

        // removeAllEpics()
        if (Pattern.matches("^/tasks/epic", path)) {
            try {
                taskManager.removeAllEpics();
            } catch(Exception exception) {
                System.out.println("Ошибка при удалении эпика'");
                httpExchange.sendResponseHeaders(404, 0);
            }
        }

        // removeEpicById()
        if (Pattern.matches("^/tasks/epic", path)) {
            String idString = query.replaceFirst("id=", "");
            int id = idParse(idString);
            try {
                taskManager.removeEpicById(id);
            } catch(Exception exception) {
                System.out.println("Ошибка при удалении эпика по id");
                httpExchange.sendResponseHeaders(404, 0);
            }
        }

        // removeAllSubtasks()
        if (Pattern.matches("^/tasks/subtask", path)) {
            try {
                taskManager.removeAllSubtasks();
            } catch(Exception exception) {
                System.out.println("Ошибка при удалении подзадачи'");
                httpExchange.sendResponseHeaders(404, 0);
            }
        }

        // removeSubtaskById()
        if (Pattern.matches("^/tasks/subtask", path)) {
            String idString = query.replaceFirst("id=", "");
            int id = idParse(idString);
            try {
                taskManager.removeSubtaskById(id);
            } catch(Exception exception) {
                System.out.println("Ошибка при удалении подзадачи по id");
                httpExchange.sendResponseHeaders(404, 0);
            }
        }
    }

    private void sendMessage(HttpExchange httpExchange, String responseText) throws IOException {
        byte[] response = responseText.getBytes(StandardCharsets.UTF_8);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(200, response.length);
        httpExchange.getResponseBody().write(response);
    }

    private int idParse(String idString) {
        try {
            return Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Сервер остановлен.");
    }
}
