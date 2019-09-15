package coLocation.inventory;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.*;


@TableName("colocation_inventory_in_use")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoLocationInventoryInUseDTO {
    @PrimaryKey
    @ColumnName("id") long id;
    @ColumnName("inventory_id") Long inventoryID;
    @ColumnName("client_id") Long clientID;
    @ColumnName("connection_id") Long connectionID;
    @ColumnName("occupied_date") Long occupiedDate;
    @ColumnName("status") int status;


}
