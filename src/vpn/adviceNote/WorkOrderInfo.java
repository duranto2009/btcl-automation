package vpn.adviceNote;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class WorkOrderInfo {
    String woNumber;
    String officeAddress;
    String vendorName;
    String source;
    String sourceType;
    String destination;
    String destinationType;
    String distance;

}
