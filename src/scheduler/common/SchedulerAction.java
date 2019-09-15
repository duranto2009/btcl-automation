package scheduler.common;

import annotation.ForwardedAction;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import scheduler.task.TaskConfiguration;
import scheduler.task.TaskService;
import util.ServiceDAOFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ActionRequestMapping(SchedulerConstants.SCHEDULER_BASE_URL)
public class SchedulerAction extends AnnotatedRequestMappingAction {

    @Service private TaskService taskService;

    @ForwardedAction
    @RequestMapping(mapping = SchedulerConstants.SCHEDULER_ALL, requestMethod = RequestMethod.GET)
    public String getViewSchedulerPage(HttpServletRequest request) {
        request.setAttribute(SchedulerConstants.REQUESTED_URI, SchedulerConstants.SCHEDULER_ALL );
        return SchedulerConstants.SCHEDULER_PAGE;
    }

    @RequestMapping(mapping = SchedulerConstants.RUN_SCHEDULER, requestMethod = RequestMethod.GET)
    public void startScheduler() throws Exception {
        SchedulerExecutor.getInstance().startScheduler();
    }

    @RequestMapping(mapping = SchedulerConstants.STOP_SCHEDULER, requestMethod = RequestMethod.GET)
    public void stopScheduler() throws Exception {
        SchedulerExecutor.getInstance().stopScheduler();
    }

    @RequestMapping(mapping = SchedulerConstants.TASK_ALL, requestMethod = RequestMethod.GET)
    public List<TaskConfiguration> getAllTaskConfig() throws Exception {
        return taskService.getAvailableTasks();
    }

    @RequestMapping(mapping = SchedulerConstants.SCHEDULER_STATUS, requestMethod = RequestMethod.GET)
    public boolean isSchedulerRunning() throws Exception {
        return ServiceDAOFactory.getService(SchedulerService.class).getCurrentSchedulerProperty().isRunning();
    }

    @RequestMapping(mapping = SchedulerConstants.TASK_MANAGE, requestMethod = RequestMethod.GET)
    public void manageTask(@RequestParameter("taskId") int taskId, @RequestParameter("run")boolean toRun) throws Exception {
        taskService.manageTask(taskId, toRun);
    }

}
