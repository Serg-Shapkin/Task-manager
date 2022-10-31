package domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    private String taskName; // имя задачи
    private String descriptionTask; // описание задачи
    private int idTask; // id задачи по которому ее можно найти
    private TaskStatus taskStatus; // статус задачи
    protected TaskType taskType = TaskType.TASK; // тип задачи

    protected long duration; // продолжительность задачи в минутах
    protected LocalDateTime startTime; // дата и время начала выполнения задачи

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd, HH:mm");

    // первый конструктор для задач без даты
    public Task(String taskName, String descriptionTask, TaskStatus taskStatus) {
        this.taskName = taskName;
        this.descriptionTask = descriptionTask;
        this.taskStatus = taskStatus;
    }

    // второй конструктор с продолжительностью задачи и временем старта задачи
    public Task(String taskName, String descriptionTask, TaskStatus taskStatus, long duration, LocalDateTime startTime) {
        this.taskName = taskName;
        this.descriptionTask = descriptionTask;
        this.taskStatus = taskStatus;
        this.duration = duration;
        this.startTime = startTime;
    }

    // третий конструктор для создания задачи из истории, если НЕ заданы продолжительность и дата
    public Task(int idTask, String taskName, String descriptionTask, TaskStatus taskStatus) {
        this.idTask = idTask;
        this.taskName = taskName;
        this.descriptionTask = descriptionTask;
        this.taskStatus = taskStatus;
    }

    // четвертый конструктор для создания задачи из истории, если заданы продолжительность и дата
    public Task(int idTask, String taskName, String descriptionTask, TaskStatus taskStatus, long duration, LocalDateTime startTime) {
        this.idTask = idTask;
        this.taskName = taskName;
        this.descriptionTask = descriptionTask;
        this.taskStatus = taskStatus;
        this.duration = duration;
        this.startTime = startTime;
    }


    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescriptionTask() {
        return descriptionTask;
    }

    public void setDescriptionTask(String descriptionTask) {
        this.descriptionTask = descriptionTask;
    }

    public int getIdTask() { return idTask; }

    public void setIdTask(int idTask) {
        this.idTask = idTask;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }
    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "{" +
                " - название задачи: \"" + taskName + "\"\n" +
                "\t- описание задачи: \"" + descriptionTask + "\"\n" +
                "\t- id задачи: " + idTask + "\n" +
                "\t- статус задачи: " + taskStatus + "\n" +
                "\t- тип задачи: " + taskType + "\n" +
                "\t" + durationToString() + "\n" +
                "\t" + startTimeToString() + "\n" +
                "}\n";
    }

    public String durationToString() {
        if (duration != 0) {
            return "- время необходимое на выполнение: " + duration + " минут";
        }
        return "- продолжительность не задана";
    }

    public String startTimeToString() {
        if (startTime != null) {
            return "- дата и время начала выполнения: " + startTime.format(FORMATTER);
        }
            return "- дата и время начала выполнения не заданы";
    }

}
