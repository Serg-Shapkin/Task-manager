package domain;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    protected List<Subtask> subtaskList = new ArrayList<>();

    public Epic(String taskName, String descriptionTask) {
        super(taskName, descriptionTask, TaskStatus.NEW);
    }

    public List<Subtask> getSubtaskList() {
        return subtaskList;
    }

    public Subtask addSubtask(Subtask subtask) {
        subtaskList.add(subtask);
        return subtask;
    }
}
