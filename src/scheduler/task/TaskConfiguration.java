package scheduler.task;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;

@TableName("task_configuration")
@Data
public class TaskConfiguration {
    @PrimaryKey
    @ColumnName("id") int id;

    @ColumnName("name") String name;

    @ColumnName("class_name") String className;

    @ColumnName("method_name") String methodName;

    @ColumnName("last_running_time") long lastRunningTime;

    @ColumnName("next_running_time") long nextRunningTime;

    @ColumnName("interval_type") IntervalType intervalType;

    @ColumnName("scheduling_hour") int schedulingHour;

    @ColumnName("scheduling_minute") int schedulingMinute;

    @ColumnName("allow_execution") boolean allowExecution;

    @ColumnName("is_deleted") boolean isDeleted;

    @ColumnName("summary") String summary;

    @ColumnName("result") String result;

    @ColumnName ("last_summary_update_time") long lastSummaryUpdateTime;
}
