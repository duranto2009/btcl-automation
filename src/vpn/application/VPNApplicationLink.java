package vpn.application;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import application.ApplicationState;
import entity.office.Office;
import lombok.Data;

@Data @TableName("vpn_application_link")
public class VPNApplicationLink {
    @PrimaryKey @ColumnName("id") long id;

    @ColumnName("link_name") String linkName;
    @ColumnName("link_bandwidth") int linkBandwidth;

    @ColumnName("network_link_id") long networkLinkId;
    @ColumnName("local_office_id") long localOfficeId;
    @ColumnName("local_office_loop_id") long localOfficeLoopId;
    @ColumnName("remote_office_id") long remoteOfficeId;
    @ColumnName("remote_office_loop_id") long remoteOfficeLoopId;

    @ColumnName("link_state") ApplicationState linkState;

    @ColumnName("local_zone_id") long localZoneId;
    @ColumnName("remote_zone_id") long remoteZoneId;

    @ColumnName("vpn_application_id") long vpnApplicationId;

    @ColumnName("skip_payment") boolean skipPayment;

    @ColumnName("ifr_feasibility") boolean ifrFeasibility;

    @ColumnName("local_office_terminal_device_provider") long localOfficeTerminalDeviceProvider; // enum
    @ColumnName("remote_office_terminal_device_provider") long remoteOfficeTerminalDeviceProvider;

    @ColumnName("is_forwarded") boolean isForwarded;
    @ColumnName("is_service_started") boolean isServiceStarted;

    @ColumnName("demand_note_id") Long demandNoteId;
    @ColumnName("advice_note_id") Long adviceNoteId;
    @ColumnName("demand_note_official_letter_id") Long demandNoteOfficialLetterId;



    Office localOffice;
    Office remoteOffice;

    boolean isLocalOfficeLoopNotNeeded;
    boolean isRemoteOfficeLoopNotNeeded;
    // is it possible to add a arary of action here
}
