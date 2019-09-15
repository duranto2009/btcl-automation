package vpn.monthlyOutsourceBill;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.*;
import util.DateUtils;

@Data
@Builder
@TableName("vpn_monthly_outsource_bill")
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
public class VPNMonthlyOutsourceBill {

    @PrimaryKey
    @ColumnName("id")
    Long id;

    @ColumnName("vendorId")
    long vendorId;

    @ColumnName("createdDate")
    @Builder.Default
    long createdDate = DateUtils.getCurrentTime();

    @CurrentTime
    @ColumnName("lastModifiedDate")
    @Builder.Default
    long lastModificationTime = DateUtils.getCurrentTime();

    @ColumnName("loopLengthSingle")
    @Builder.Default
    double loopLengthSingle = 0.0;    //total single loop distance covered by vendor

    @ColumnName("loopLengthDouble")
    @Builder.Default
    double loopLengthDouble = 0.0;    //total dual loop distance covered by vendor

    @ColumnName("loopLength")
    @Builder.Default
    double loopLength = 0.0;    //total loop distance covered by vendor

    @ColumnName("totalDue")
    @Builder.Default
    double totalDue = 0.0;

    @ColumnName("totalPayable")
    @Builder.Default
    double totalPayable = 0.0;

    @ColumnName("year")
    int year;

    @ColumnName("month")
    int month;

    @ColumnName("status")
    OutsourceBillStatus status;

}
