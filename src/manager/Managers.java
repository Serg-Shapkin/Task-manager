package manager;

import history.HistoryManager;
import history.InMemoryHistoryManager;

import java.io.File;

// до сих пор не понимая для чего нужен этот класс

public final class Managers {    // понял что он должен быть final

    public static TaskManager getDefaultTaskManager() {    // ... и методы public static
        return new FileBackedTasksManager(new File("src/files/history.csv"));
    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }

}
