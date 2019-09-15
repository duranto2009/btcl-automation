package scheduler.common;

import lombok.extern.log4j.Log4j;
import scheduler.task.TaskConfiguration;
import scheduler.task.TaskResult;
import scheduler.task.TaskService;
import util.ServiceDAOFactory;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j
class TaskManager {
//    private final static int NUMBER_OF_ALIVE_THREADS = 5;
//    private final ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_ALIVE_THREADS);

    void runPendingTask(ExecutorService executorService) throws Exception {
        log.info("***************** Running Pending Task *******************");
        TaskService taskService = ServiceDAOFactory.getService(TaskService.class);
        List<TaskConfiguration> pendingTasks = taskService.getAllPendingTasks();
        TaskResult taskResult = new TaskResult();

        pendingTasks.forEach(t->executorService.execute(()->{
            String result = "N/A";
            String summary = "Running";
            taskResult.setTaskConfigurationId(t.getId());
            try {
                t.setNextRunningTime(calculateNextRunningTime(t));
                t.setNextRunningTime(System.currentTimeMillis() + 60 *1000);// TODO : comment out this line after testing complete.
                t.setLastRunningTime(System.currentTimeMillis());

                t.setSummary(summary);
                t.setResult(result);
                t.setLastSummaryUpdateTime((System.currentTimeMillis()));
                taskService.updateTask(t);

                Class <?> classObject = Class.forName(t.getClassName());
                Object serviceObject = ServiceDAOFactory.getService(classObject);
                Method method = classObject.getMethod(t.getMethodName());
                Object returnObject = method.invoke(serviceObject);
                result = (returnObject == null ? "N/A" : returnObject.toString());
                summary = "Success :: " + t.getName();
            } catch (Exception e) {
                log.fatal(e.getMessage(), e);
                result = "Exception";
                summary = "Failure :: " + t.getName() + " :: " + e.getMessage();
            }finally {


                t.setSummary(summary);
                t.setResult(result);
                t.setLastSummaryUpdateTime((System.currentTimeMillis()));
                try {
                    taskService.updateTask(t);
                } catch (Exception e) {
                    log.fatal(e.getMessage(), e);
                    e.printStackTrace();
                }
            }
        }));

    }

    private long calculateNextRunningTime(TaskConfiguration configuration) {

        LocalDateTime time = LocalDateTime.now(ZoneId.systemDefault());
        log.info("Server Time: " + time);
        LocalDateTime nextRunTime = LocalDateTime.of(time.getYear(), time.getMonth(), time.getDayOfMonth(),
                configuration.getSchedulingHour(), configuration.getSchedulingMinute(), 0, 0 );
        log.info("Next Run Time First Estimate: " + nextRunTime);
        if(time.isAfter(nextRunTime)){
            nextRunTime = nextRunTime.plusDays(1);
            log.info("Next Run Time Corrected: " + nextRunTime);
        }

        return nextRunTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
