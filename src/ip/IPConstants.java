package ip;

import lombok.Data;
@Data
public class IPConstants {
    public enum Purpose {
        LLI_CONNECTION,
        NIX_CONNECTION,
        ADSL_BRAS,
        NAT,
        EQUIPMENT_TO_EQUIPMENT,
        ROUTER_LOOPBACK,
        BTCL_INTERNAL,
        INTERNAL_EXTERNAL_TESTING;

    }


    public enum Version { IPv4, IPv6 }

    public  enum Type {
        PUBLIC, PRIVATE
    }

    public enum Status { ACTIVE, NOT_ACTIVE }


}
