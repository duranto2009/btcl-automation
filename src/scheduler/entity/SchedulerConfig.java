package scheduler.entity;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;


import java.time.LocalDateTime;

@Data
@TableName("scheduler_config")
public class SchedulerConfig {
    @PrimaryKey
    @ColumnName("id")
    int id;

    @ColumnName("scheduler_name")
    String schedulerName;

    @ColumnName("class_name")
    String className;

    @ColumnName("method_name")
    String methodName;

    @ColumnName("last_running_time")
    long lastRunningTime;

    @ColumnName("next_running_time")
    long nextRunningTime;

    @ColumnName("result")
    String result;

    @ColumnName("summary")
    String summary;



    @ColumnName("initial_delay")
    int initialDelay;

    @ColumnName("successive_interval")
    int successiveInterval;

    @ColumnName("status")
    boolean status;

    @ColumnName("executable")
    boolean executable;


}
