package ip.ipVsNIXConnection;

import annotation.DAO;
import annotation.Transactional;
import com.google.gson.JsonArray;
import lombok.extern.log4j.Log4j;
import util.TransactionType;

import java.util.List;

@Log4j
public class NIXIPvsConnectionService {

    @DAO
    NIXIPvsConnectionDAO NIXIPvsConnectionDAO;

    @Transactional
    public void save(NIXIPvsConnection NIXIPvsConnection) throws Exception {
        try{
            NIXIPvsConnectionDAO.save(NIXIPvsConnection);
        }catch (Exception e){e.printStackTrace();}
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<NIXIPvsConnection> getIPVsConnectionByConnectionId(long connectionId) throws Exception {
        return  NIXIPvsConnectionDAO.getIPVsConnectionByConnectionId(connectionId);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public JsonArray getIPBlocksByConnectionId(long connectionId) throws Exception{
        return NIXIPvsConnectionDAO.getIPBlocksByConnectionId(connectionId);
    }

    public void update(NIXIPvsConnection NIXIPvsConnection)  {
        try {
            NIXIPvsConnectionDAO.update(NIXIPvsConnection);
        } catch (Exception e) {
            log.fatal("error updating ip_vs_connection for id " + NIXIPvsConnection.getId(), e);
        }
    }

    @Transactional
    public NIXIPvsConnection getIPVsConnectionByUsageId(long usageId)throws Exception {
        NIXIPvsConnection NIXIPvsConnection = NIXIPvsConnectionDAO.getIPVsConnectionByUsageId(usageId);
        return NIXIPvsConnection;
    }
    @Transactional(transactionType = TransactionType.READONLY)
    public List<NIXIPvsConnection> getByConnectionId(long connectionId) throws Exception {
        return NIXIPvsConnectionDAO.getByConnectioNId(connectionId);
    }
}
