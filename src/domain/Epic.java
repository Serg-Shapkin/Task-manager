package domain;

import manager.TaskType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    protected TaskType epicType = TaskType.EPIC;


    protected List<Subtask> subtaskList = new ArrayList<>();
    protected LocalDateTime endTime;

    // первый базовый конструктор
    public Epic(String taskName, String descriptionTask) {
        super(taskName, descriptionTask, TaskStatus.NEW);
    }

    // второй конструктор с продолжительностью и временем старта
    public Epic(String taskName, String descriptionTask, long duration, LocalDateTime startTime) {
        super(taskName, descriptionTask, TaskStatus.NEW, duration, startTime);
    }

    // для восстановления из истории, если НЕ заданы продолжительность и дата
    public Epic(int idTask, String taskName, String descriptionTask, TaskStatus taskStatus) {
        super(idTask, taskName, descriptionTask, taskStatus);
    }

    // для восстановления из истории, если заданы продолжительность и дата
    public Epic(int idTask, String taskName, String descriptionTask, TaskStatus taskStatus, long duration, LocalDateTime startTime) {
        super(idTask, taskName,descriptionTask, taskStatus, duration, startTime);
    }


    public List<Subtask> getSubtaskList() {
        return subtaskList;
    }

    public Subtask addSubtask(Subtask subtask) {
        subtaskList.add(subtask);
        return subtask;
    }

    public TaskType getEpicType() {
        return epicType;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
