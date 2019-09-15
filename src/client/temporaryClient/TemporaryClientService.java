package client.temporaryClient;

import annotation.Transactional;
import common.ClientConstants;
import common.RequestFailureException;
import global.GlobalService;
import requestMapping.Service;
import util.ModifiedSqlGenerator;
import vpn.client.ClientService;

import java.util.List;

public class TemporaryClientService {

    @Service
    private GlobalService globalService;
    @Service
    private ClientService clientService;

    @Transactional
    public long addTemporaryClient(String emailId,
                                   String mobileNumber,
                                   String countryCode,
                                   int moduleId,
                                   int accountType) throws Exception {

        if (!clientService.isEmailIdFree(emailId)) {
            throw new RequestFailureException("Email Id already exists");
        }

        if(!clientService.isMobileNumberFree(mobileNumber)){
            throw new RequestFailureException("Mobile Number already exists");
        }

        TemporaryClient temporaryClient = new TemporaryClient();
        temporaryClient.setEmailId(emailId);
        temporaryClient.setMobileNumber(mobileNumber);
        temporaryClient.setCountryCode(countryCode);
        temporaryClient.setModuleId(moduleId);
        temporaryClient.setClientType(accountType);
        globalService.save(temporaryClient);

        return temporaryClient.getId();
    }


    @Transactional
    public void deleteByEmailIdIfExists(String email) throws Exception {
        List<TemporaryClient> clients = globalService.getAllObjectListByCondition(TemporaryClient.class,
                new TemporaryClientConditionBuilder()
                        .Where()
                        .emailIdEquals(email)
                        .getCondition());

        if (clients.isEmpty()) {
            System.out.println("no client to delete");
            return;
        }
        ModifiedSqlGenerator.deleteHardEntityByID(TemporaryClient.class, clients.get(0).getId());
    }


    @Transactional
    public void deleteTemporaryClientsOlderThanAnHour() throws Exception {
        long presentTime = System.currentTimeMillis();

        List<TemporaryClient> temporaryClients = globalService
                .getAllObjectListByCondition(TemporaryClient.class);

        for (TemporaryClient client : temporaryClients) {
            long lifetime = presentTime - client.getActivationTime();

            if (lifetime > ClientConstants.MAX_LIFETIME_FOR_TEMP_CLIENT) {
                ModifiedSqlGenerator.deleteHardEntityByID(TemporaryClient.class, client.getId());
            }
        }
    }


}
