package scheduler.task;

import annotation.DAO;
import annotation.Transactional;
import common.RequestFailureException;
import lombok.extern.log4j.Log4j;
import util.CurrentTimeFactory;
import util.TransactionType;

import java.util.List;
@Log4j
public class TaskService {
    @DAO
    TaskDAO taskDAO;
    @Transactional(transactionType = TransactionType.READONLY)
    public List<TaskConfiguration> getAvailableTasks() throws Exception {
        return taskDAO.getAllAvailableTasks();
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<TaskConfiguration> getAllPendingTasks() throws Exception {
        return taskDAO.getAllPendingTasks();
    }

    @Transactional
    public void updateTask(TaskConfiguration configuration) throws Exception {
        CurrentTimeFactory.initializeCurrentTimeFactory();
        taskDAO.updateTask(configuration);
        CurrentTimeFactory.destroyCurrentTimeFactory();
    }

    @Transactional
    public void insertTaskResult(TaskResult taskResult) throws Exception {
        CurrentTimeFactory.initializeCurrentTimeFactory();
        taskDAO.insertTaskResult(taskResult);
        CurrentTimeFactory.destroyCurrentTimeFactory();

    }

    @Transactional
    public void updateTaskResult(TaskResult taskResult) {
        CurrentTimeFactory.initializeCurrentTimeFactory();
        try {
            taskDAO.updateTaskResult(taskResult);
        } catch (Exception e) {
            log.fatal(e.getMessage());
        }
        CurrentTimeFactory.destroyCurrentTimeFactory();
    }

    @Transactional
    public void manageTask(int taskId, boolean toRun) throws Exception {

        TaskConfiguration taskConfiguration=taskDAO.getTaskById(taskId);
        if(taskConfiguration.isAllowExecution() ^ toRun) {
            taskConfiguration.setAllowExecution(toRun);
            taskDAO.updateTask(taskConfiguration);
        }else {
            throw new RequestFailureException("Task Already " + (toRun? "Executable" : "Not Executable"));
        }
    }


}
