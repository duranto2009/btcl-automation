package scheduler.common;

import common.RequestFailureException;
import common.UniversalDTOService;
import lombok.extern.log4j.Log4j;
import scheduler.entity.SchedulerConfig;
import util.ServiceDAOFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.ZoneId;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
@Log4j
public class SchedulerExecutor {
    private static SchedulerExecutor ourInstance = new SchedulerExecutor();
    public static SchedulerExecutor getInstance() {
        return ourInstance;
    }
    private ScheduledExecutorService executorService;
    private final TaskManager taskManager = new TaskManager();
    private final SchedulerService schedulerService = new SchedulerService();
    private static ScheduledFuture<?> executorRunner = null;

    private SchedulerExecutor() {
        executorService = Executors.newScheduledThreadPool(5);
    }
    void startScheduler() throws Exception {
        SchedulerProperty property = schedulerService.getCurrentSchedulerProperty();
        if(property.isRunning()){
            throw new RequestFailureException("Scheduler is Already Running");
        }
        property.setRunning(true);
        property.setLastRunningTime(Instant.now().toEpochMilli());
        schedulerService.updateSchedulerProperty(property);
        log.info("Scheduler is Running");
        executorRunner = executorService.scheduleAtFixedRate(() -> {
            try {
                taskManager.runPendingTask(executorService);
            } catch (Exception e) {
                throw new RequestFailureException(e.getMessage());
            }
        }, property.getInitialDelay(), property.getSuccessiveDelay(), TimeUnit.SECONDS);


    }

    void stopScheduler() throws Exception {
        SchedulerProperty property = schedulerService.getCurrentSchedulerProperty();
        if(!property.isRunning()) {
            throw new RequestFailureException("Scheduler is Already Terminated");
        }
        executorRunner.cancel(true);
        property.setRunning(false);
        schedulerService.updateSchedulerProperty(property);
        log.info("Scheduler has terminated");
    }

    public void destroyScheduler() throws Exception {
        SchedulerProperty property = schedulerService.getCurrentSchedulerProperty();
        executorService.shutdownNow();
        try {
            if(!executorService.awaitTermination(property.getTimeoutForTermination(), TimeUnit.SECONDS)){
                executorService.shutdownNow();
                log.info("ShutdownNow Called try");
            }
        } catch (InterruptedException e) {
            log.fatal(e.getMessage());
            executorService.shutdownNow();
            log.info("ShutdownNow Called Catch");
        }finally {
            executorService.shutdownNow();
            log.info("ShutdownNow Called Finally");
        }
        if(property.isRunning()){
            property.setRunning(false);
            schedulerService.updateSchedulerProperty(property);
        }
        log.info("Scheduler is destroyed");
    }
}
