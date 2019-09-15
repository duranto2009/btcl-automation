package vpn;

import lombok.Getter;

@Getter
public class VPNOTC {
    double registrationChargePerLink;
    int registrationChargeSpecialCaseLinkCount;
    double registrationChargePerLinkSpecialCase;
    double vatPercentage;
    double minimumOwnershipChangeCharge;
    double maximumOwnershipChangeCharge;
    double reconnectChargePerLink;

    double bwShiftingOTC,
            addressShiftingOTC,
            localLoopShiftingOTC;
    double layer3Percentage;

}
