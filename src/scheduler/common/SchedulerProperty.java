package scheduler.common;

import lombok.Data;
@Data
public class SchedulerProperty {

    private int timeoutForTermination;
    private int initialDelay;
    private int successiveDelay;
    private long lastRunningTime;
    private boolean isRunning;
}
