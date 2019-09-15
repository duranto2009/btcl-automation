package scheduler.task;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;

@TableName("task_result")
@Data
public class TaskResult {
    @PrimaryKey
    @ColumnName("id") long id;

    @ColumnName("task_configuration_id") int taskConfigurationId;

    @ColumnName("result_update_time") long resultUpdateTime;

    @ColumnName("result") String result;

    @ColumnName("summary") String summary;
}
