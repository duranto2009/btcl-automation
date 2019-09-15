package ip.Generators;


import ip.MethodReferences;
import ip.ipRouting.IPRoutingInfo;

public class RoutingInfoGenerator {
    public static IPRoutingInfo getRoutingInfoGenerator(long from, long to) {
        IPRoutingInfo routingInfo = null;
        if(to - from > 1){
            routingInfo = generateDefault(from,  to);
            return routingInfo;
        }else if( to - from == 1) {
            routingInfo = generateExceptional_2_IP_Case(from,  to);
        }
        // more logic yet to come
        return  routingInfo;

    }

    private static IPRoutingInfo generateExceptional_2_IP_Case(long from,  long to) {
        String fromIP = MethodReferences.getIPStringFromLong.apply(from);

        String toIP = MethodReferences.getIPStringFromLong.apply(to);

        IPRoutingInfo routingInfo = IPRoutingInfo
                .builder()
                .fromIP(fromIP)
                .toIP(toIP)
                .networkIP(fromIP)
                .gatewayIP(fromIP)
                .broadcastIP(toIP)
                .toIP(toIP)
                .build();

        return routingInfo;
    }

    private static IPRoutingInfo generateDefault(long from, long to) {
        String fromIP = MethodReferences.getIPStringFromLong.apply(from);

        String toIP = MethodReferences.getIPStringFromLong.apply(to);

        return IPRoutingInfo
                .builder()
                .fromIP(fromIP)
                .toIP(toIP)
                .networkIP(fromIP)
                .gatewayIP(MethodReferences.getIPStringFromLong.apply(from+1))
                .broadcastIP(toIP)
                .build();
    }
}
