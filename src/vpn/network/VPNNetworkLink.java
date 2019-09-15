package vpn.network;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import common.RequestFailureException;
import entity.facade.DemandNoteAutofillFacade;
import entity.localloop.LocalLoopConsumerMap;
import entity.office.Office;
import entity.office.OfficeService;
import global.GlobalService;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import util.ServiceDAOFactory;

import java.util.List;
@Log4j
@Data
@TableName("vpn_network_link")

public class VPNNetworkLink {
    @PrimaryKey @ColumnName("id") long id;
    @ColumnName("history_id") long historyId;
    @ColumnName("network_history_id") long networkHistoryId;
    @ColumnName("clientId") long clientId;
    @ColumnName("incident_type") String incidentType; // TODO enum
    @ColumnName("link_name") String linkName;
    @ColumnName("link_status") String linkStatus; // TODO Change to enum
    @ColumnName("link_type") String linkType;
    @ColumnName("link_distance") long linkDistance;


    @ColumnName("active_from") long activeFrom; // TODO Change to enum
    @ColumnName("active_to") long activeTo;
    @ColumnName("valid_from") long validFrom;
    @ColumnName("valid_to") long validTo;



    @ColumnName("link_bandwidth") int linkBandwidth;
    @ColumnName("local_office_id") long localOfficeId;
    @ColumnName("remote_office_id") long remoteOfficeId;

    @ColumnName("local_zone_id") long localZoneId;
    @ColumnName("remote_zone_id") long remoteZoneId;
    @ColumnName("application_id") long applicationId;

    @ColumnName("local_office_terminal_device_provider") long localOfficeTerminalDeviceProvider;
    @ColumnName("remote_office_terminal_device_provider") long remoteOfficeTerminalDeviceProvider;


    List<LocalLoopConsumerMap> localLoopConsumerMaps;
    Office localEndOffice;
    Office remoteEndOffice;

    public void setOffices() {
        VPNLinkOfficePair pair;
        try {
            pair = ServiceDAOFactory.getService(VPNNetworkLinkService.class)
                    .getOfficesByLinkId(this.id);
            pair.getLocalEndOffice().setLocalLoops(ServiceDAOFactory.getService(VPNNetworkLinkService.class)
                    .getLocalLoopsByOfficeId( pair.getLocalEndOffice().getId()));
            pair.getRemoteEndOffice().setLocalLoops(ServiceDAOFactory.getService(VPNNetworkLinkService.class).
                    getLocalLoopsByOfficeId(pair.getRemoteEndOffice().getId()));

            this.setLocalEndOffice(pair.getLocalEndOffice());
            this.setRemoteEndOffice(pair.getRemoteEndOffice());
        } catch (Exception e) {
            log.fatal(e.getMessage(),e);
        }
    }

    public void setLinkDistance() throws Exception {
        Office localEndOffice = ServiceDAOFactory.getService(GlobalService.class).findByPK(Office.class, this.localOfficeId);
        Office remoteEndOffice = ServiceDAOFactory.getService(GlobalService.class).findByPK(Office.class, this.remoteOfficeId);

        long localEndPopId = ServiceDAOFactory.getService(OfficeService.class).getLocalLoopsByOfficeId(localEndOffice.getId()).get(0).getPopId();
        long remoteEndPopId = ServiceDAOFactory.getService(OfficeService.class).getLocalLoopsByOfficeId(remoteEndOffice.getId()).get(0).getPopId();

        double distance = ServiceDAOFactory.getService(DemandNoteAutofillFacade.class).getPopToPopDistance(localEndPopId, remoteEndPopId);
        this.linkDistance = (long)distance;
    }

}
