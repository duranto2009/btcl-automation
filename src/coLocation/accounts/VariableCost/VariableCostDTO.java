package coLocation.accounts.VariableCost;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("colocation_cost_config")
public class VariableCostDTO {



    @ColumnName("id")
    @PrimaryKey
    long ID;
    @ColumnName("type_id")
    int typeID;
    @ColumnName("price")
    double price;
    @ColumnName("quantity_id")
    int quantityID;

}
