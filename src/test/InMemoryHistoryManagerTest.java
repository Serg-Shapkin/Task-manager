import domain.Task;
import domain.TaskStatus;
import history.InMemoryHistoryManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class InMemoryHistoryManagerTest {
    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    Task task = new Task("Test task name", "Test description task", TaskStatus.NEW);

    @DisplayName("Проверка добавления истории")
    @Test
    void add() {
        historyManager.addTask(task);
        final List<Task> history = historyManager.getHistory();
        Assertions.assertNotNull(history, "История не пустая.");
        Assertions.assertEquals(1, history.size(), "История не пустая.");
    }
}
