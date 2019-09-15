package scheduler.task;

import static util.ModifiedSqlGenerator.*;

import java.util.List;

public class TaskDAO {
    Class<TaskConfiguration> classObject = TaskConfiguration.class;

    public List<TaskConfiguration> getAllAvailableTasks() throws Exception {
        return getAllObjectList(classObject, " WHERE "
            + getColumnName(classObject, "isDeleted") + " = 0"

        );
    }

    public List<TaskConfiguration> getAllPendingTasks() throws Exception {
        return getAllObjectList(classObject, " WHERE "
                + getColumnName(classObject, "allowExecution") + " = 1"
                + " AND "
                + getColumnName(classObject, "nextRunningTime") + " <= " + System.currentTimeMillis()

        );
    }

    public void updateTask(TaskConfiguration configuration) throws Exception {
        updateEntity(configuration);
    }

    public void insertTaskResult(TaskResult taskResult) throws Exception {
        insert(taskResult);
    }

    public void updateTaskResult(TaskResult taskResult) throws Exception {
        updateEntity(taskResult);
    }

    public TaskConfiguration getTaskById(int taskId) throws Exception {
        return getObjectByID(classObject, taskId);
    }
}
