package common.client;

import annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.TransactionType;
import vpn.client.ClientDetailsDTO;

public class ClientService {

    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    @Transactional
    public ClientDTO getClientInfoFromApplicationId(long applicationId) {
        try {
            return new ClientDAO().getClientByApplicationId(applicationId);
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
            ex.printStackTrace();
        }

        return null;
    }

    @Transactional
    public ClientDTO getClientByNIXApplicationId(long applicationId) {
        try {
            return new ClientDAO().getClientByNIXApplicationId(applicationId);
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
            ex.printStackTrace();
        }

        return null;
    }

    @Transactional
    public ClientDTO getClientInfoByReviseApplication(long applicationId) {
        try {
            return new ClientDAO().getClientByReviseApplication(applicationId);
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
            ex.printStackTrace();
        }

        return null;
    }

    @Transactional
    public ClientDetails getClientInfoFromClientID(long clientID) {
        try {
            return new ClientDAO().getClientDetails(clientID);
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
            ex.printStackTrace();
        }

        return null;
    }


}
