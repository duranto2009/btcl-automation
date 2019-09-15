package ip;

import lli.connection.LLIConnectionConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class IPBlockForConnection {
    String fromIP;
    String toIP;

    IPConstants.Version version;
    IPConstants.Type type;
    IPConstants.Purpose purpose;

    boolean realIP;
    String parentIP;

    long connectionId;
    long regionId;
    long subRegionId;
    LLIConnectionConstants.IPUsageType usageType;

}
