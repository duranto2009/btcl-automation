package nix.demandnote;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.TableName;
import lli.demandNote.adjustment.DNAdjustStatus;
import lombok.*;
import util.DateUtils;

@Data
@Builder
@TableName("nix_demand_note_adjustment")
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
public class NIXDemandNoteAdjustment {
    @PrimaryKey
    @ColumnName("id")
    Long id;

    @ColumnName("clientId")
    long clientId;

    @ColumnName("billId")
    long billId;

    @ColumnName("connectionType")
    int connectionType;

    @ColumnName("createdDate")
    @Builder.Default
    long createdDate = DateUtils.getCurrentTime();

    @CurrentTime
    @ColumnName("lastModifiedDate")
    @Builder.Default
    long lastModificationTime = DateUtils.getCurrentTime();

    @ColumnName("activeFrom")
    @Builder.Default
    long activeFrom = DateUtils.getCurrentTime();

    @ColumnName("loopCharge")
    @Builder.Default
    double loopCharge = 0.0;


    @ColumnName("totalDue")
    @Builder.Default
    double totalDue = 0.0;	//without vat

    @ColumnName("vatRate")
    @Builder.Default
    double vatRate = 0.0;

    @ColumnName("vat")
    @Builder.Default
    double vat = 0.0;

    @ColumnName("status")
    @Builder.Default
    DNAdjustStatus status = DNAdjustStatus.PENDING;
}
