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
@TableName("colocation_common_cost_config_template")
public class CommonCostTemplateDTO {

    @ColumnName("id")
    @PrimaryKey
    long ID;
    @ColumnName("name")
    String name;
    @ColumnName("application_type")
    int applicationType;

}
