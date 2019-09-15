package coLocation.accounts.commonCost;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("colocation_common_cost_config")
public class CommonCostDTO {

    @ColumnName("id")
    @PrimaryKey
    long ID;
    @ColumnName("type_id")
    int typeID;
    @ColumnName("price")
    double price;


}
