package vpn.demandNote;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;

@Data
@TableName("vpn_loop_charge_eligibility")
public class VPNLoopChargeDiscountEligibility {
    @PrimaryKey@ColumnName("id") long id;
    @ColumnName("vpn_app_id") long vpnApplicationId;
    @ColumnName("vpn_app_link_id") long vpnApplicationLinkId;
    @ColumnName("demand_note_id") long demandNoteId;
    @ColumnName("is_verified") boolean isVerified;
    @ColumnName("is_skipped") boolean isSkipped;
}
