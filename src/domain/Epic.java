package domain;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    protected TaskStatus taskStatus;
    protected List<Subtask> subtaskList = new ArrayList<>();

    public Epic(String taskName, String descriptionTask) {
        super(taskName, descriptionTask, null);
    }

    public List<Subtask> getSubtaskList() {
        return subtaskList;
    }


    @Override
    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Subtask addSubtask(Subtask subtask) {
        subtaskList.add(subtask);
        return subtask;
    }
}
