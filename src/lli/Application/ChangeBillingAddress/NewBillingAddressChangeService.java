package lli.Application.ChangeBillingAddress;

import annotation.DAO;
import annotation.Transactional;
import api.ClientAPI;
import common.ModuleConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.KeyValuePair;
import vpn.client.ClientDetailsDTO;
import vpn.clientContactDetails.ClientContactDetailsDTO;

public class NewBillingAddressChangeService {

    private static final Logger logger = LoggerFactory.getLogger(NewBillingAddressChangeService.class);

    @DAO
    NewBillingAddressChangeDao newBillingAddressChangeDao;

    @Transactional
    public void insertNewBillingAddress(LLINewBillingAddressApplication lliChangeBillingAddressApplication) throws Exception{
        try {
            if(lliChangeBillingAddressApplication.getId() == 0)
                newBillingAddressChangeDao.insertNewBillingAddress(lliChangeBillingAddressApplication);
            else
                newBillingAddressChangeDao.updateItem(lliChangeBillingAddressApplication);
        } catch (Exception e) {
            logger.error("error while saving ", e);
        }
        return;
    }

    @Transactional
    public LLINewBillingAddressApplication getNewBillingAddressByAppId(long appId) throws Exception{
        return newBillingAddressChangeDao.getNewBillAddressByAppId(appId);
    }

    public ClientContactDetailsDTO deserialize_To_clientContactDetails(LLINewBillingAddressApplication lliNewBillingAddressApplication) throws Exception {

//        ClientContactDetailsDTO clientContactDetailsDTO = new ClientContactDetailsDTO();

        KeyValuePair<ClientDetailsDTO, ClientContactDetailsDTO> pair = ClientAPI.getInstance()
                .getPairOfClientDetailsAndClientContactDetails(lliNewBillingAddressApplication.getClientID(), ModuleConstants.Module_ID_LLI, ClientContactDetailsDTO.BILLING_CONTACT);

        ClientDetailsDTO details = pair.getKey();
        ClientContactDetailsDTO clientContactDetailsDTO = pair.getValue();

        clientContactDetailsDTO.setEmail(lliNewBillingAddressApplication.getEmail());
        clientContactDetailsDTO.setCity(lliNewBillingAddressApplication.getCity());
        clientContactDetailsDTO.setPostCode(lliNewBillingAddressApplication.getPostCode());
        clientContactDetailsDTO.setPhoneNumber(lliNewBillingAddressApplication.getMobileNumber());
        clientContactDetailsDTO.setFaxNumber(lliNewBillingAddressApplication.getFaxNumber());
        clientContactDetailsDTO.setLandlineNumber(lliNewBillingAddressApplication.getTelephoneNumber());
        clientContactDetailsDTO.setRegistrantsLastName(lliNewBillingAddressApplication.getLastName());
        clientContactDetailsDTO.setRegistrantsName(lliNewBillingAddressApplication.getFirstName());
        clientContactDetailsDTO.setAddress(lliNewBillingAddressApplication.getAddress());
        clientContactDetailsDTO.setLastModificationTime(System.currentTimeMillis());

        return clientContactDetailsDTO;
    }
}
