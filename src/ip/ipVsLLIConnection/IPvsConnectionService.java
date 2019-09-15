package ip.ipVsLLIConnection;

import annotation.DAO;
import annotation.Transactional;
import com.google.gson.JsonArray;
import lli.connection.LLIConnectionConstants;
import lombok.extern.log4j.Log4j;
import util.CurrentTimeFactory;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;
import util.TransactionType;

import java.util.List;
@Log4j
public class IPvsConnectionService {

    @DAO
    IPvsConnectionDAO iPvsConnectionDAO;

    @Transactional
    public void save(IPvsConnection iPvsConnection) throws Exception {
        try{
            iPvsConnectionDAO.save(iPvsConnection);
        }catch (Exception e){e.printStackTrace();}
    }
    public static void main(String [] args) throws Exception {
        IPvsConnectionService iPvsConnectionService = ServiceDAOFactory.getService(IPvsConnectionService.class);
        CurrentTimeFactory.initializeCurrentTimeFactory();
        long currentTime = CurrentTimeFactory.getCurrentTime();
        IPvsConnection iPvsConnection = IPvsConnection
                .builder()
                .connectionId(1001L)
                .ipUsageId(11001L)
                .usageType(LLIConnectionConstants.IPUsageType.MANDATORY)
                .routingInfoId(111L)
                .creationTime(currentTime)
                .lastModificationTime(currentTime)
                .activeFrom(currentTime)
                .build();
        iPvsConnectionService.save(iPvsConnection);
        CurrentTimeFactory.destroyCurrentTimeFactory();
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<IPvsConnection> getIPVsConnectionByConnectionId(long connectionId) throws Exception {
        return  iPvsConnectionDAO.getIPVsConnectionByConnectionId(connectionId);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public JsonArray getIPBlocksByConnectionId(long connectionId) throws Exception{
        return iPvsConnectionDAO.getIPBlocksByConnectionId(connectionId);
    }

    public void update(IPvsConnection iPvsConnection)  {
        try {
            iPvsConnectionDAO.update(iPvsConnection);
        } catch (Exception e) {
            log.fatal("error updating ip_vs_connection for id " + iPvsConnection.getId(), e);
        }
    }

    @Transactional
    public IPvsConnection getIPVsConnectionByUsageId(long usageId)throws Exception {
        IPvsConnection iPvsConnection = iPvsConnectionDAO.getIPVsConnectionByUsageId(usageId);
        return iPvsConnection;
    }
    @Transactional(transactionType = TransactionType.READONLY)
    public List<IPvsConnection> getByConnectionId(long connectionId) throws Exception {
        return iPvsConnectionDAO.getByConnectioNId(connectionId);
    }
}
