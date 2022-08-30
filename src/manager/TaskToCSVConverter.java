package manager;

import domain.Epic;
import domain.Subtask;
import domain.Task;
import domain.TaskStatus;
import history.HistoryManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TaskToCSVConverter {

    protected String toString(Task task) {
        if (task instanceof Epic) {
            return task.getIdTask() + "," +
                    ((Epic)task).getEpicType() + "," +
                    task.getTaskName() + "," +
                    task.getTaskStatus() + "," +
                    task.getDescriptionTask() + "," + "\n";
        } else if (task instanceof Subtask) {
            return task.getIdTask() + "," +
                    ((Subtask)task).getSubtaskType() + "," +
                    task.getTaskName() + "," +
                    task.getTaskStatus() + "," +
                    task.getDescriptionTask() + "," +
                    ((Subtask)task).getEpicId() + "\n";
        }
        return task.getIdTask() + "," +
                task.getTaskType() + "," +
                task.getTaskName() + "," +
                task.getTaskStatus() + "," +
                task.getDescriptionTask() + "," + "\n";
    }

    protected Task fromString(String value) { // создание задачи из истории
        final String[] line = value.split(",");
        final int idTask = Integer.parseInt(line[0]);
        final TaskType type = TaskType.valueOf(line[1]);
        final String taskName = line[2];
        final TaskStatus taskStatus = TaskStatus.valueOf(line[3]);
        final String descriptionTask = line[4];

        if (type == TaskType.TASK) {
            return new Task(taskName, descriptionTask, taskStatus);
        }

        if (type == TaskType.EPIC) {
            return new Epic(taskName, descriptionTask);
        }
        // если type == Subtask
        return new Subtask(taskName, descriptionTask, taskStatus, idTask);
    }

    public static String historyToString(HistoryManager historyManager) { // сохранение менеджера истории
        Collection<Task> history = historyManager.getHistory();
        StringBuilder stringBuilder = new StringBuilder();

        if (history.isEmpty()) {
            return "";
        }

        for (Task historyId : history) {
            stringBuilder.append(historyId.getIdTask() + ","); // получили stringBuilder, добавили ","
        }

        return stringBuilder.toString(); // преобразовали в String и вернули
    }

    public static List<Integer> historyFromString(String value) { // восстановление менеджера из CSV
        List<Integer> list = new ArrayList<>();
        String[] line = value.split(",");
        if (value.isEmpty()) {
            list.add(0); // если пришла пустая строка, то добавить в список ноль
        } else {
            for (String str : line) {
                int idHistory = Integer.parseInt(str);
                list.add(idHistory);
            }
        }
        return list;
    }
}
