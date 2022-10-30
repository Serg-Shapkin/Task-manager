package manager;

import domain.Epic;
import domain.Subtask;
import domain.TaskStatus;
import history.HistoryManager;
import domain.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TaskToCSVConverter {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static String toString(Task task) { // сохраняем задачи в CSV

        if (task instanceof Epic) {
            return task.getIdTask() + "," +                        // 1 - ID
                    ((Epic)task).getEpicType() + "," +             // 2 - TYPE
                    task.getTaskName() + "," +                     // 3 - NAME
                    task.getTaskStatus() + "," +                   // 4 - STATUS
                    task.getDescriptionTask() + "," +              // 5 - DESCRIPTION
                    "не применимо" + "," +                         // 6 - EPIC_ID
                    task.getDuration() + "," +                     // 7 - DURATION
                    task.getStartTime() + "," +                    // 8 - START_TIME
                    getEndTime(task) + "\n";                       // 9 - END_TIME
        } else if (task instanceof Subtask) {
            return task.getIdTask() + "," +                        // 1 - ID
                    ((Subtask)task).getSubtaskType() + "," +       // 2 - TYPE
                    task.getTaskName() + "," +                     // 3 - NAME
                    task.getTaskStatus() + "," +                   // 4 - STATUS
                    task.getDescriptionTask() + "," +              // 5 - DESCRIPTION
                    ((Subtask)task).getEpicId() + "," +            // 6 - EPIC_ID
                    task.getDuration() + "," +                     // 7 - DURATION
                    task.getStartTime() + "," +                    // 8 - START_TIME
                    getEndTime(task) + "\n";                       // 9 - END_TIME
        }
        return task.getIdTask() + "," +                            // 1 - ID
                task.getTaskType() + "," +                         // 2 - TYPE
                task.getTaskName() + "," +                         // 3 - NAME
                task.getTaskStatus() + "," +                       // 4 - STATUS
                task.getDescriptionTask() + "," +                  // 5 - DESCRIPTION
                "не применимо" + "," +                             // 6 - EPIC_ID
                task.getDuration() + "," +                         // 7 - DURATION
                task.getStartTime() + "," +                        // 8 - START_TIME
                getEndTime(task) + "\n";                           // 9 - END_TIME
    }

    private static String getEndTime(Task task) {
        if (task.getStartTime() == null) {
            return "невозможно рассчитать";
        } else {
            return task.getStartTime().plusMinutes(task.getDuration()).format(formatter);
        }
    }

    public static Task fromString(String value) { // создание задачи из истории (из CSV)
        final String[] line = value.split(",");

        final int idTask = Integer.parseInt(line[0]);              // 0 - ID
        final TaskType type = TaskType.valueOf(line[1]);           // 1 - TYPE
        final String taskName = line[2];                           // 2 - NAME
        final TaskStatus taskStatus = TaskStatus.valueOf(line[3]); // 3 - STATUS
        final String descriptionTask = line[4];                    // 4 - DESCRIPTION
        final String epicIdString = line[5];                       // 5 - EPIC_ID (String)

        int oldEpicId = 0;
        if (!epicIdString.contains("не применимо")) {
            oldEpicId = Integer.parseInt(epicIdString);
        }

        final long duration = Long.parseLong(line[6]);             // 6 - DURATION

        final String startTimeString = line[7];                    // 7 - START_TIME (String)

        LocalDateTime startTime = null;
        if (!startTimeString.contains("null")) {
            startTime = LocalDateTime.parse(line[7]);
        }


        if (type == TaskType.TASK) {
            if (duration == 0 && startTimeString.contains("null")) { // если продолжительность и время начала не заданы
                return new Task(idTask, taskName, descriptionTask, taskStatus);
            }
            return new Task (idTask, taskName, descriptionTask, taskStatus, duration, startTime); // если заданы
        }

        if (type == TaskType.EPIC) {
            if (duration == 0 && startTimeString.contains("null")) {
                return new Epic(idTask, taskName, descriptionTask, taskStatus);
            }
            return new Epic(idTask, taskName, descriptionTask, taskStatus, duration, startTime);

        }

        if (type == TaskType.SUBTASK) {
            if (duration == 0 && startTimeString.contains("null")) {
                return new Subtask(idTask, taskName, descriptionTask, taskStatus, oldEpicId);
            }
        }
        return new Subtask(idTask, taskName, descriptionTask, taskStatus, oldEpicId, duration, startTime);
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

        // добавил: перед тем как преобразовать в String удалил последнюю запятую
        return stringBuilder.substring(0, stringBuilder.length()-1); // преобразовали в String и вернули
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
