package coLocation.inventory;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.*;


@TableName("colocation_inventory")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoLocationInventoryDTO {
    @PrimaryKey
    @ColumnName("id") long id;
    @ColumnName("cat_type_id") Long catagoryID;
    @ColumnName("template_type_id") Long templateID;
    @ColumnName("total_amount") Long totalAmount;
    @ColumnName("available_amount") double availableAmount;
    @ColumnName("name")@Builder.Default String  name="";
    @ColumnName("model")@Builder.Default  String  model="";
    @ColumnName("is_used")@Builder.Default  boolean isUsed=false;

    String type;

}
