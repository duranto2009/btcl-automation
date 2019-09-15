package vpn.network;

import entity.office.Office;
import lombok.Data;

@Data
public class VPNLinkOfficePair {

    Office localEndOffice;
    Office remoteEndOffice;
}
