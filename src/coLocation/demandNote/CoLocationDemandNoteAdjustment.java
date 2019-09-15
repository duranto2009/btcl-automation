package coLocation.demandNote;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("colocation_dn_adjustment")
public class CoLocationDemandNoteAdjustment {
    @ColumnName("id")@PrimaryKey
    long id;

    @ColumnName("connection_id")
    long connection_id;

    @ColumnName("connection_history_id")
    long connection_history_id;


    @ColumnName("adjustment_amount")
    double adjustmentAmount;

}
