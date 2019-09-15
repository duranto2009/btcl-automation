package upstream.contract;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import application.ApplicationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import upstream.UpstreamConstants;
import upstream.circuitInfo.CircuitInformationDTO;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("upstream_contract")
public class UpstreamContract {

    @PrimaryKey @ColumnName("id") long contractId;

    @ColumnName("contract_history_id") long contractHistoryId;

    @ColumnName("application_id") long applicationId;

    @ColumnName("application_type") ApplicationType applicationType;

    @ColumnName("type_of_bw_id") long typeOfBandwidthId;

    @ColumnName("bw_capacity") double bandwidthCapacity;

    @ColumnName("media_id") long mediaId;

    @ColumnName("btcl_service_location_id") long btclServiceLocationId;

    @ColumnName("provider_location_id") long providerLocationId;

    @ColumnName("selected_provider_id") long selectedProviderId;

    @ColumnName("bw_price") double bandwidthPrice;

    @ColumnName("otc") double otc;

    @ColumnName("mrc") double mrc;

    @ColumnName("contract_duration") double contractDuration;

    @ColumnName("srf_date") long srfDate;

    @ColumnName("circuit_info_link") String circuitInfoLink;

    @ColumnName("active_from") long activeFrom;
    @ColumnName("active_to") long activeTo;
    @ColumnName("valid_from") long validFrom;
    @ColumnName("valid_to") long validTo;

    @ColumnName("contract_name") String contractName;


    List<CircuitInformationDTO> circuitInformationDTOs;
}
