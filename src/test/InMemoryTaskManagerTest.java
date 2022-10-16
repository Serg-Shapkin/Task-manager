import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    // Task
    @DisplayName("Проверка получения всех задач")
    @Test
    void getAllTasks() throws IOException {
        super.getAllTasks();
    }

    @DisplayName("Проверка удаления всех задач")
    @Test
    void removeAllTasks() throws IOException {
        super.removeAllTasks();
    }

    @DisplayName("Проверка получения задачи по id")
    @Test
    void getTaskById() throws IOException {
        super.getTaskById();
    }

    @DisplayName("Проверка добавления задачи")
    @Test
    void addTask() throws IOException {
        super.addTask();
    }

    @DisplayName("Проверка обновления задачи")
    @Test
    void updateTask() throws IOException {
        super.updateTask();
    }

    @DisplayName("Проверка удаления задачи по id")
    @Test
    void removeTaskById() throws IOException {
        super.removeTaskById();
    }

    // Epic
    @DisplayName("Проверка получения всех эпиков")
    @Test
    void getAllEpics() {
        super.getAllEpics();
    }

    @DisplayName("Проверка удаления всех эпиков")
    @Test
    void removeAllEpics() {
        super.removeAllEpics();
    }

    @DisplayName("Проверка получения эпика по id")
    @Test
    void getEpicById() throws IOException {
        super.getEpicById();
    }

    @DisplayName("Проверка добавления эпика")
    @Test
    void addEpic() throws IOException {
        super.addEpic();
    }

    @DisplayName("Проверка обновления эпика")
    @Test
    void updateEpic() throws IOException {
        super.updateEpic();
    }

    @DisplayName("Проверка удаления эпика по id")
    @Test
    void removeEpicById() {
        super.removeEpicById();
    }

    // Subtask
    @DisplayName("Проверка получения всех подзадач")
    @Test
    void getAllSubtask() throws IOException {
        super.getAllSubtask();
    }

    @DisplayName("Проверка удаления всех подзадач")
    @Test
    void removeAllSubtask() throws IOException {
        super.removeAllSubtask();
    }

    @DisplayName("Проверка получения подзадачи по id")
    @Test
    void getSubtaskById() throws IOException {
        super.getSubtaskById();
    }

    @DisplayName("Проверка добавления подзадачи")
    @Test
    void addSubtask() throws IOException {
        super.addSubtask();
    }

    @DisplayName("Проверка обновления подзадачи")
    @Test
    void updateSubtask() throws IOException {
        super.updateSubtask();
    }

    @DisplayName("Проверка удаления подзадачи по id")
    @Test
    void removeSubtaskById() throws IOException {
        super.removeSubtaskById();
    }

    @DisplayName("Проверка получения списка подзадач эпика")
    @Test
    void subtaskOfTheEpic() throws IOException {
        super.subtaskOfTheEpic();
    }

    // CalculateStatus
    @DisplayName("Проверка статуса эпика если у трех подзадач статус NEW")
    @Test
    void calculateStatusIfThreeSubtasksAreNew() throws IOException {
        super.calculateStatusIfThreeSubtasksAreNew();
    }

    @DisplayName("Проверка статуса эпика если у трех подзадач статус IN_PROGRESS")
    @Test
    void calculateStatusIfThreeSubtasksAreInProgress() throws IOException {
        super.calculateStatusIfThreeSubtasksAreInProgress();
    }

    @DisplayName("Проверка статуса эпика если у трех подзадач статус DONE")
    @Test
    void calculateStatusIfThreeSubtasksAreDone() throws IOException {
        super.calculateStatusIfThreeSubtasksAreDone();
    }

    @DisplayName("Проверка статуса эпика если у двух подзадач статус NEW и у одной DONE")
    @Test
    void calculateStatusIfTwoNewAndOneDoneSubtask() throws IOException {
        super.calculateStatusIfTwoNewAndOneDoneSubtask();
    }

    // getHistory
    @DisplayName("Проверка получения истории")
    @Test
    void getHistory() throws IOException {
        super.getHistory();
    }
}

