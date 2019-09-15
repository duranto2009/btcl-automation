package common.bill;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;


@TableName("at_bill_multiple_mapping")
@Data
public class MultipleBillMappingDTO {


    @PrimaryKey
    @ColumnName("id")
    long id;
    @ColumnName("common_bill_id")
    long commonBillId;
    @ColumnName("individual_bill_id")
    long individualBillId;
}
