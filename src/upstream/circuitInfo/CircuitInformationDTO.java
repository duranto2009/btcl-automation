package upstream.circuitInfo;


import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;
import upstream.UpstreamConstants;

@Data
@TableName("upstream_circuit_info")
public class CircuitInformationDTO {
    @PrimaryKey
    @ColumnName("id")
    long id;

    @ColumnName("info_type")
    UpstreamConstants.CIRCUIT_INFO_TYPE  circuitInfoType;

//    @ColumnName("application_id")
//    long applicationId;

    @ColumnName("circuit_id")
    long circuitId;

    @ColumnName("location")
    String location = "";

    @ColumnName("device_name")
    String deviceName="";

    @ColumnName("shelf_number")
    long shelfNumber;

    @ColumnName("card_number")
    long cardNumber;

    @ColumnName("port_number")
    long portNumber;

    @ColumnName("contract_id")
    long contractId;
}
