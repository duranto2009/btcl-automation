package upstream.application;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import application.ApplicationState;
import application.ApplicationType;
import lombok.Data;
import upstream.UpstreamConstants;
import upstream.circuitInfo.CircuitInformationDTO;

import java.util.List;

@Data
@TableName("upstream_application")
public class UpstreamApplication {

    @PrimaryKey
    @ColumnName("application_id")
    long applicationId;

    @ColumnName("application_type")
    ApplicationType applicationType;

    @ColumnName("type_of_bw_id")
    long typeOfBandwidthId;

    @ColumnName("bw_capacity")
    double bandwidthCapacity;

    @ColumnName("media_id")
    long mediaId;

    @ColumnName("btcl_service_location_id")
    long btclServiceLocationId;

    @ColumnName("provider_location_id")
    long providerLocationId;

    @ColumnName("selected_provider_id")
    long selectedProviderId;

    @ColumnName("bw_price")
    double bandwidthPrice;

    @ColumnName("otc")
    double otc;

    @ColumnName("mrc")
    double mrc;

    @ColumnName("contract_duration")
    double contractDuration;

    @ColumnName("srf_date")
    long srfDate;

    @ColumnName("circuit_info_link")
    String circuitInfoLink;

    @ColumnName("application_date")
    long applicationDate = System.currentTimeMillis();

    @ColumnName("state")
    long state;

    @ColumnName("application_status")
    String applicationStatus;

    @PrimaryKey @ColumnName("contract_id") long contractId;

    String comment;

    //only needed in search page
    String stateColor;
    boolean hasPermission;

    List<CircuitInformationDTO> circuitInformationDTOs;
}
