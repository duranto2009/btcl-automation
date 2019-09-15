package ip.ipRouting;

import annotation.CurrentTime;
import annotation.DAO;
import annotation.Transactional;
import common.RequestFailureException;
import ip.Generators.RoutingInfoGenerator;
import ip.IPUtility;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import util.CurrentTimeFactory;
import util.ServiceDAOFactory;
import util.TransactionType;

@Slf4j
public class IPRoutingService {

    @DAO
    IPRoutingDAO routingDAO;

    public static void main(String [] args) throws Exception {
        String fromIP = "103.102.11.0";
        String toIP = "103.102.11.255";
        CurrentTimeFactory.initializeCurrentTimeFactory();

        IPRoutingService routingService = ServiceDAOFactory.getService(IPRoutingService.class);
        IPRoutingInfo routingInfo = routingService.getIPRoutingByIPRange(0, 255);
        log.info("Suggestion");
        log.info(routingInfo.toString());

        log.info("Saving---");
        routingService.saveIPRoutingInfo(routingInfo);

        log.info(routingInfo.toString());

        long id = routingInfo.getId();

        log.info("Get from DB");
        log.info(routingService.getIPRoutingInfoById(id).toString());

        CurrentTimeFactory.destroyCurrentTimeFactory();
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public IPRoutingInfo getIPRoutingInfoById(long id) throws Exception {
        IPRoutingInfo routingInfo = routingDAO.getById(id);
        if(routingInfo == null) {
            throw new RequestFailureException("No Routing Info found for routing info id: " + id);
        }
        return routingInfo;
    }
    @Transactional
    public void saveIPRoutingInfo(IPRoutingInfo routingInfo) throws Exception {
        validateRoutingInfo(routingInfo);
//        long currentTime = CurrentTimeFactory.getCurrentTime();
//        routingInfo
//                .builder()
//                .activeFrom(currentTime)
//                .lastModificationTime(currentTime)
//                .creationTime(currentTime).build();
        routingDAO.save(routingInfo);
    }

    private void validateRoutingInfo(IPRoutingInfo routingInfo) {
    //TODO
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public IPRoutingInfo getIPRoutingByIPRange(long fromIP, long toIP) {
        return RoutingInfoGenerator.getRoutingInfoGenerator(fromIP, toIP);
    }
}
