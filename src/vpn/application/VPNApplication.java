package vpn.application;

import annotation.ColumnName;
import annotation.ForeignKeyName;
import annotation.PrimaryKey;
import annotation.TableName;
import application.Application;
import lombok.Data;
import lombok.EqualsAndHashCode;

import lombok.*;

import java.util.List;

@TableName("vpn_application")
@ForeignKeyName("parent_application_id")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class VPNApplication extends Application {

    @PrimaryKey @ColumnName("vpn_application_id")
    long vpnApplicationId;

    @ColumnName("network_type")
    int networkType;

    @ColumnName("network_id")
    long networkId;

    @ColumnName("layer_type")
    int layerType;  //

    @ColumnName("demand_note_id")
    Long demandNoteId;


    @ColumnName("skip_payment")
    boolean skipPayment; //

    @ColumnName("is_demand_note_aggregated")
    boolean isDemandNoteAggregated;

    List<VPNApplicationLink> vpnApplicationLinks;

}
