package manager;

import domain.Epic;
import domain.Subtask;
import domain.TaskStatus;

public class CalculateStatus {
    public TaskStatus calculate(Epic epic) {
        boolean isNew = false;
        boolean isProgress = false;
        boolean isDone = false;

        for (Subtask subtask : epic.getSubtaskList()) {
            // если у эпика нет подзадач или статус NEW
            if(subtask == null || subtask.getTaskStatus() == TaskStatus.NEW) {
                isNew = true;
            } else if (subtask.getTaskStatus() == TaskStatus.DONE) {
                isDone = true;
            } else {
                isProgress = true;
            }
        }

        // если NEW == true, статус будет NEW
        if(isNew && !isProgress && !isDone) {
            return TaskStatus.NEW;
            // если DONE == true, статус будет DONE
        } else if(!isNew && !isProgress && isDone) {
            return TaskStatus.DONE;
        } else {
            return TaskStatus.IN_PROGRESS;
        }
    }
}
