package upstream.inventory;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;
import upstream.UpstreamConstants;

@Data
@TableName("upstream_inventory")
public class UpstreamInventoryItem {
    @PrimaryKey
    @ColumnName("id")
    long id;

    @ColumnName("item_type")
    UpstreamConstants.INVENTORY_ITEM_TYPE itemType;

    @ColumnName("item_name")
    String itemName;
}
