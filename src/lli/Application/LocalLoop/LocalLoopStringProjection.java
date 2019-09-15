package lli.Application.LocalLoop;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocalLoopStringProjection {
    String popName;
    String switchOrRouterName;
    String portName;
    String vlanName;
    String ocName;
    long ocDistance;
    long clientDistance;
    long btclDistance;

}
