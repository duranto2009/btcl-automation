package coLocation.inventory;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@TableName("colocation_inventory_template")
@Getter
@Setter
@NoArgsConstructor
public class CoLocationInventoryTemplateDTO {
    @PrimaryKey
    @ColumnName("id") long id;
    @ColumnName("type") int type;
    @ColumnName("value") String  value;
    @ColumnName("description") String  description;
    public CoLocationInventoryTemplateDTO(int id,String value){
        this.id = id;
        this.value = value;
    }
}
