package lli.demandNote;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
class LoopBreakdownEssentialData {


        long popID;
        int ofcType;
        long BTCLDistances;
        long OCDistances;
        long adjustedBTClDistance;
        long adjustedOCDistance;


}
