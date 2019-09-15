package vpn.demandNote;


import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.TableName;
import lli.demandNote.adjustment.DNAdjustStatus;
import lombok.*;
import util.DateUtils;

@Data
@Builder
@TableName("vpn_dn_adjustment")
@NoArgsConstructor
@AllArgsConstructor
public class VPNDemandNoteAdjustment {


    @PrimaryKey
    @ColumnName("id")
    Long id;

    @ColumnName("clientId")
    long clientId;

    @ColumnName("billId")
    long billId;

    @ColumnName("linkType")
    String linkType="";

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

    @ColumnName("bwCharge")
    @Builder.Default
    double bandWidthCharge = 0.0;

    @ColumnName("bwDiscount")
    @Builder.Default
    double bandWidthDiscount = 0.0;

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
