package manager;

import history.HistoryManager;
import history.InMemoryHistoryManager;

// не понимаю для чего и как, но вроде бы так обсуждали на встрече


public final class Managers {    // понял что он должен быть final

    public static TaskManager getDefaultTaskManager() {    // ... и методы public static
        return new InMemoryTaskManager(getDefaultHistoryManager());
    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }
}
