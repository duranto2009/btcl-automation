package client;

import common.ClientConstants;
import common.ModuleConstants;
import common.client.ClientContactDetails;
import common.client.ClientContactDetailsConditionBuilder;
import common.repository.AllClientRepository;
import global.GlobalService;
import login.LoginDTO;
import requestMapping.Service;
import vpn.client.ClientDetailsDTO;
import vpn.client.ClientForm;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientService {

    /**
     * @author dhrubo
     */

    @Service
    GlobalService globalService;

    public static Boolean isCopyingToThisModuleAllowed(Integer moduleID) {
        return (ClientConstants.moduleIDListSupportingCloning.contains(moduleID)) ? true : false;
    }

    /**
     * @author dhrubo
     */
    public String generateIdentityFromClientAddRequest(Map<String, String[]> requestParamterMap) {
        String identity = "";

        List<String> requestKeySet = new ArrayList<>(requestParamterMap.keySet());
        for (String requestKey : requestKeySet) {
            if (requestKey.startsWith("identityType_")) {
                if (requestKey.split("_")[1].equals("dependent")) {
                    identity += requestParamterMap.get(requestKey.replace("identityType", "radio"))[0] + ":" + requestParamterMap.get(requestKey)[0] + ",";
                } else {
                    identity += requestKey.split("_")[1] + ":" + requestParamterMap.get(requestKey)[0] + ",";
                }
            }
            if (requestKey.equals("clientDetailsDTO.identityNo")) {
                identity += requestParamterMap.get("clientDetailsDTO.identityType")[0] + ":" + requestParamterMap.get(requestKey)[0] + ",";
            }
        }
        return identity;
    }

    /**
     * @author dhrubo
     */
    public boolean hasNewIdentityDesign(int moduleID) {
        switch (moduleID) {
            default:
                return true;
        }
    }

    /**
     * @author dhrubo
     */
    public Integer getClientAddMode(ClientDetailsDTO clientDetailsDTO, LoginDTO loginDTO) {
        if (loginDTO == null) {
            return ClientConstants.ClientAddMode.Client_Add_NoAccount;
        } else {
            if (clientDetailsDTO.getExistingClientID() != -1) {
                return ClientConstants.ClientAddMode.Client_Add_Account_Existing;
            } else {
                return ClientConstants.ClientAddMode.Client_Add_Account_New;
            }
        }
    }

    /**
     * @author dhrubo
     */
    public Boolean isIndividualClientAccepted(Integer moduleID) {
        switch (moduleID) {
            case ModuleConstants.Module_ID_DOMAIN:
                return true;
            default:
                return false;
        }
    }

    /**
     * @author dhrubo
     */
    public Boolean isCompanyClientAccepted(Integer moduleID) {
        switch (moduleID) {
            default:
                return true;
        }
    }

    /**
     * @author dhrubo
     */
    public boolean isModuleIDAndAccountTypeValid(int moduleID, int accountType) {
        switch (accountType) {
            case 1:
                return isIndividualClientAccepted(moduleID);
            case 2:
                return isCompanyClientAccepted(moduleID);
            default:
                return false;
        }
    }

    /**
     * @author Dhrubo
     */
    public List<ClientContactDetailsDTO> getClientContactDetailsDTOListByClientIDAndModuleID(long clientID, int moduleID) throws Exception {
        ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getModuleClientByClientIDAndModuleID(clientID, moduleID);
        return new vpn.client.ClientService().getVpnContactDetailsListByClientID(clientDetailsDTO.getId());
    }


    /**
     * @author Touhid
     * Written for the getting client using module id as input unlike before
     */
    public Map<String, String> getClientDetailsByClientIdAndModuleId(long clientID, int moduleId) throws Exception {
        Map<String, String> clientDetails = new HashMap<>();

        ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getModuleClientByClientIDAndModuleID(clientID,
                moduleId);

        ClientContactDetails clientContactDetails=new ClientContactDetails();

        List<ClientContactDetails> clientContactDetailsList = globalService.getAllObjectListByCondition(ClientContactDetails.class,
                new ClientContactDetailsConditionBuilder()
                        .Where()
                        .vpnClientIdEquals(clientDetailsDTO.getId())
                        .detailsTypeEquals(ClientConstants.DETAILS_TYPE_REGISTRANT)
                        .orderByidDesc()
                        .getCondition()
        );
        if (clientContactDetailsList!=null){
            clientContactDetails=clientContactDetailsList.get(0);
        }
        clientDetails.put("clientID", String.valueOf(clientID));
        clientDetails.put("name", String.valueOf(clientContactDetails.getName()));
        clientDetails.put("contact",clientContactDetails.getPhone().length()>0? clientContactDetails.getPhone():"Not Found");
        clientDetails.put("loginName", clientDetailsDTO.getLoginName());
        clientDetails.put("registrantType", RegistrantTypeConstants.RegistrantTypeName.get(clientDetailsDTO.getRegistrantType()));
        clientDetails.put("registrantCategory", RegistrantCategoryConstants.RegistrantCategoryName.get(clientDetailsDTO.getRegistrantCategory()));
        clientDetails.put("clientType", ClientForm.CLIENT_TYPE_STR.get(clientDetailsDTO.getClientCategoryType()));
        return clientDetails;
    }


}
