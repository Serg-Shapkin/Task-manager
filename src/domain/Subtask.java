package domain;

import manager.TaskType;

public class Subtask extends Task {
    protected int epicId; // id нашего Epic
    protected TaskType subtaskType = TaskType.SUBTASK;

    public Subtask(String taskName, String descriptionTask, TaskStatus taskStatus, int epicId) {
        super(taskName, descriptionTask, taskStatus);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public TaskType getSubtaskType() {
        return subtaskType;
    }
}
