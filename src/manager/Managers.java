package manager;

import history.HistoryManager;
import history.InMemoryHistoryManager;

// до сих пор не понимая для чего нужен этот класс


public final class Managers {    // понял что он должен быть final

    public static TaskManager getDefaultTaskManager() {    // ... и методы public static
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }

}
