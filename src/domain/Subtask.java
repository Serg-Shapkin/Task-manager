package domain;

import manager.TaskType;

import java.time.LocalDateTime;

public class Subtask extends Task {
    protected int epicId; // id нашего Epic
    protected TaskType subtaskType = TaskType.SUBTASK;

    // первый базовый конструктор
    public Subtask(String taskName, String descriptionTask, TaskStatus taskStatus, int epicId) {
        super(taskName, descriptionTask, taskStatus);
        this.epicId = epicId;
    }

    // второй конструктор с продолжительностью, временем старта и временем окончания
    public Subtask(String taskName, String descriptionTask, TaskStatus taskStatus, int epicId, long duration, LocalDateTime startTime) {
        super(taskName, descriptionTask, taskStatus, duration, startTime);
        this.epicId = epicId;
    }

    // для восстановления из истории, если НЕ заданы продолжительность и дата
    public Subtask(int idTask, String taskName, String descriptionTask, TaskStatus taskStatus, int epicId) {
        super(idTask, taskName, descriptionTask, taskStatus);
        this.epicId = epicId;
    }

    // для восстановления из истории, если заданы продолжительность и дата
    public Subtask(int idTask, String taskName, String descriptionTask, TaskStatus taskStatus, int epicId, long duration, LocalDateTime startTime) {
        super(idTask, taskName, descriptionTask, taskStatus, duration, startTime);
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
