package domain;

public class Task {
    private String taskName; // имя задачи
    private String descriptionTask; // описание задачи
    private int idTask; // id задачи по которому ее можно найти
    private TaskStatus taskStatus; //статус задачи

    public Task(String taskName, String descriptionTask) {
        this.taskName = taskName;
        this.descriptionTask = descriptionTask;
        //this.taskStatus = taskStatus;
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

    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\'' +
                ", descriptionTask='" + descriptionTask + '\'' +
                ", idTask=" + idTask +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
