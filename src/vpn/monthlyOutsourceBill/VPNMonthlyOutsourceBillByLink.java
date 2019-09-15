package vpn.monthlyOutsourceBill;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.*;

@Data
@Builder
@TableName("vpn_monthly_outsource_bill_link")
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
public class VPNMonthlyOutsourceBillByLink {

    @PrimaryKey
    @ColumnName("id")
    Long id;

    @ColumnName("outsourceBillId")
    long outsourceBillId;

    @ColumnName("linkId")
    long linkId;

    @ColumnName("loopLengthSingle")
    @Builder.Default
    double loopLengthSingle = 0.0;    //total single loop distance covered by vendor

    @ColumnName("loopLengthDouble")
    @Builder.Default
    double loopLengthDouble = 0.0;    //total dual loop distance covered by vendor

    @ColumnName("btclLength")
    @Builder.Default
    double btclLength = 0.0;    //total loop distance covered by btcl

    @ColumnName("vendorLength")
    @Builder.Default
    double vendorLength = 0.0;    //total loop distance covered by vendor

    @ColumnName("totalDue")
    @Builder.Default
    double totalDue = 0.0;

    @ColumnName("totalPayable")
    @Builder.Default
    double totalPayable = 0.0;

    long adviceNoteNo;
    long demandNoteNo;
    long activeFrom;


    String clientName;


}
