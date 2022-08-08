package domain;

public class Subtask extends Task {
    TaskStatus taskStatus;
    protected int epicId; // id нашего Epic

    public Subtask(String taskName, String descriptionTask, TaskStatus taskStatus, int epicId) {
        super(taskName, descriptionTask);
        this.taskStatus = taskStatus; // сабтаске задаем статус вручную
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
