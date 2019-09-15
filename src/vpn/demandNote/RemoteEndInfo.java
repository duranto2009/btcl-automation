package vpn.demandNote;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class RemoteEndInfo {

    String serial;
    String address;
    String popToPopDistance;
    String bw;
    String btclDistance;
    String ocDistance;
    String localLoopLength;
    String localLoopCost;
    String bwCost;
    String discount;
    String bwMinusDiscount;
    String registrationCharge;
    String subTotal;

}