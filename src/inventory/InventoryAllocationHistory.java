package inventory;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import util.DateUtils;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("at_inventory_allocation")
public class InventoryAllocationHistory {

    @PrimaryKey
    @ColumnName("id")
    long ID;

    @ColumnName("itemId")
    long itemId;

    @ColumnName("moduleId")
    int moduleId;

    @ColumnName("activeFrom")
    @Builder.Default
    long allocatedFrom = DateUtils.getCurrentTime();

    @ColumnName("activeTo")
    @Builder.Default
    long allocatedTo = Long.MAX_VALUE; // exclusive

    @ColumnName("clientId")
    @Builder.Default
    long clientId = 0L;

	@ColumnName("count")
    @Builder.Default
	int count = 1;

}
