package vpn.adviceNote;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class InventoryInfo {
    String name;
    String localPop;
    String remotePop;
    String localRouterSwitch;
    String remoteRouterSwitch;
    String localPort;
    String remotePort;
    String localVlan;
    String remoteVlan;


}
