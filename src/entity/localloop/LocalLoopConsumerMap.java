package entity.localloop;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@TableName("local_loop_consumer_mapping")
public class LocalLoopConsumerMap {

    @PrimaryKey
    @ColumnName("id") long id;
    @ColumnName("local_loop_id") long localLoopId;
    @ColumnName("consumer_id") long consumerId;
    @ColumnName("consumer_type") String consumerType;
    @ColumnName("consumer_module_id") long consumerModuleId;
    @ColumnName("from_date") long fromDate;
    @ColumnName("to_date") long toDate;
    @ColumnName("is_active") boolean isActive;
    @ColumnName("bill_applicable") boolean isBillApplicable;
}
