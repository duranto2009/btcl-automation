package vpn.adviceNote;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LinkInfo {
    String name;
    String bw;
    String p2p;
    String lprovider;
    String rprovider;
    String loc;
    String roc;
    String ldistance;
    String rdistance;

}
