package domain;

public class Subtask extends Task {
    protected int epicId; // id нашего Epic

    public Subtask(String taskName, String descriptionTask, int idSubtask, TaskStatus taskStatus, int epicId) {
        super(taskName, descriptionTask, idSubtask, taskStatus);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
