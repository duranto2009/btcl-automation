package entity.localloop;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@TableName("local_loop")
public class LocalLoop {

    @PrimaryKey @ColumnName("id") long id;
    @ColumnName("office_id") long officeId;
    @ColumnName("ofc_type") int ofcType;
    @ColumnName("vlan_type") int vlanType; //1. Regular Vlan 2. Global Vlan
    @ColumnName("district_id") long districtId;
    @ColumnName("pop_id") long popId;
    @ColumnName("vlan_id") long vlanId;
    @ColumnName("router_or_switch_id") long routerOrSwitchId;
    @ColumnName("port_id") long portId;
    @ColumnName("port_type") int portType;
//    @ColumnName("client_distance") long clientDistance;
    @ColumnName("btcl_distance") long btclDistance;
    @ColumnName("oc_distance") long ocDistance;
    @ColumnName("vendor_id") long vendorId;
    @ColumnName("application_id") long applicationId;
    @ColumnName("loop_provider") int loopProvider; // TODO enum
    @ColumnName("ifr_feasibility") boolean ifrFeasibility; // TODO enum
    @ColumnName("is_completed") boolean isCompleted; // TODO enum
    @ColumnName("is_distributed") boolean isDistributed; // TODO enum
    @ColumnName("zone_id") int zoneId; // TODO enum
    @ColumnName("adjusted_btcl_distance") int adjustedBTCLDistance; // TODO enum
    @ColumnName("adjusted_oc_distance") int adjustedOCDistance; // TODO enum
    @ColumnName("adjustment_approved_by") long adjustmentApprovedBy; // TODO enum
    @ColumnName("is_adjusted") boolean isAdjusted; // TODO enum

    @Override
    public String toString() {
        return "LocalLoop{" +
                "id=" + id +
                ", officeId=" + officeId +
                ", ofcType=" + ofcType +
                ", districtId=" + districtId +
                ", popId=" + popId +
                ", vlanId=" + vlanId +
                ", routerOrSwitchId=" + routerOrSwitchId +
                ", portId=" + portId +
                ", portType=" + portType +
                ", btclDistance=" + btclDistance +
                ", ocDistance=" + ocDistance +
                ", vendorId=" + vendorId +
                ", applicationId=" + applicationId +
                ", loopProvider=" + loopProvider +
                ", ifrFeasibility=" + ifrFeasibility +
                ", isCompleted=" + isCompleted +
                ", isDistributed=" + isDistributed +
                ", zoneId=" + zoneId +
                ", adjustedBTCLDistance=" + adjustedBTCLDistance +
                ", adjustedOCDistance=" + adjustedOCDistance +
                ", adjustmentApprovedBy=" + adjustmentApprovedBy +
                ", isAdjusted=" + isAdjusted +
                ", popName='" + popName + '\'' +
                '}';
    }

    String popName;
    String rsName;
    String portName;
    String vlanName;

    boolean changePOP = false;


}