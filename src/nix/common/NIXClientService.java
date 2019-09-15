package nix.common;

import accounting.AccountType;
import accounting.AccountingEntryService;
import annotation.Transactional;
import client.RegistrantCategoryConstants;
import client.RegistrantTypeConstants;
import common.ClientDTO;
import common.EntityTypeConstant;
import common.ModuleConstants;
import common.repository.AllClientRepository;
import lli.LLIDropdownClient;
import lli.LLIDropdownPair;
import login.LoginDTO;
import request.StateRepository;
import user.UserDTO;
import user.UserRepository;
import util.ServiceDAOFactory;
import util.TransactionType;
import vpn.client.ClientDetailsDTO;
import vpn.client.ClientForm;

import java.util.*;

public class NIXClientService {
    public ClientDTO getNIXClient(long clientId) {

        // TODO: 12/11/2018 we consider NIX clients do same for Nix
        ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().
                getVpnClientByClientID(clientId, ModuleConstants.Module_ID_NIX);
        if(isActiveNIXClient(clientDetailsDTO)
                && isActiveClient(clientDetailsDTO)
                && clientDetailsDTO.getModuleID() == ModuleConstants.Module_ID_NIX)
        {
            return clientDetailsDTO;
        }
        return null;
    }

    public List<ClientDetailsDTO> getAllNIXClient() {

        List<ClientDetailsDTO> clientList = new ArrayList<>();

        ArrayList<ClientDetailsDTO> allClientDetailsDTOList = AllClientRepository.getInstance().getAllVpnCleint();

        for(ClientDetailsDTO clientDetailsDTO : allClientDetailsDTOList)
        {
            if(isActiveNIXClient(clientDetailsDTO)
                    && isActiveClient(clientDetailsDTO)
                    && clientDetailsDTO.getModuleID() == ModuleConstants.Module_ID_NIX)
            {
                clientList.add(clientDetailsDTO);
            }
        }
        return clientList;
    }


    // TODO: 12/11/2018 will be renamed to isActiveNIXClient
    private boolean isActiveNIXClient(ClientDTO clientDTO) {
        ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getModuleClientByClientIDAndModuleID(clientDTO.getClientID(),
                ModuleConstants.Module_ID_NIX);
        if (clientDetailsDTO != null) {
            if (StateRepository.getInstance().getStateDTOByStateID(clientDetailsDTO.getCurrentStatus())
                    .getActivationStatus() == EntityTypeConstant.STATUS_ACTIVE) {
                return true;
            }
            return false;
        }
        return false;
    }

    @SuppressWarnings("serial")
    public List<LLIDropdownPair> getClientListByPartialName(String partialName, LoginDTO loginDTO) {
        if(loginDTO.getIsAdmin()) {
            List<LLIDropdownPair> clientList = new ArrayList<LLIDropdownPair>();

            ArrayList<ClientDetailsDTO> allClientDetailsDTOList = AllClientRepository.getInstance().getAllVpnCleint();
            for(ClientDetailsDTO clientDetailsDTO : allClientDetailsDTOList) {
                if(clientDetailsDTO.getLoginName().startsWith(partialName)
                        && isActiveNIXClient(clientDetailsDTO)
                        && isActiveClient(clientDetailsDTO)
                        && clientDetailsDTO.getModuleID() == ModuleConstants.Module_ID_NIX) {
                    clientList.add(new LLIDropdownPair(clientDetailsDTO.getClientID(), clientDetailsDTO.getLoginName()));
                }
            }
            return clientList;
        } else {
            return new ArrayList<LLIDropdownPair>(new ArrayList<LLIDropdownPair>() {{
                add(new LLIDropdownPair(loginDTO.getAccountID(), loginDTO.getUsername()));
            }});
        }
    }

    public LLIDropdownPair getClientByID(Long clientID, LoginDTO loginDTO) {
        if(!loginDTO.getIsAdmin() || clientID == null) {
            return new LLIDropdownPair(loginDTO.getAccountID(), loginDTO.getUsername());
        }
        return new LLIDropdownPair(clientID, AllClientRepository.getInstance().
                getClientByClientID(clientID).getLoginName());
    }


    @SuppressWarnings("serial")
    public List<LLIDropdownClient> getClientListByPartialNameByResistantType(String partialName, LoginDTO loginDTO) {
        if(loginDTO.getIsAdmin()) {
            List<LLIDropdownClient> clientList = new ArrayList<LLIDropdownClient>();

            ArrayList<ClientDetailsDTO> allClientDetailsDTOList = AllClientRepository.getInstance().getAllVpnCleint();
            for(ClientDetailsDTO clientDetailsDTO : allClientDetailsDTOList) {
                if(clientDetailsDTO.getLoginName().startsWith(partialName)
                        && isActiveNIXClient(clientDetailsDTO)
                        && isActiveClient(clientDetailsDTO)
                        && clientDetailsDTO.getModuleID() == ModuleConstants.Module_ID_NIX) {
                    clientList.add(new LLIDropdownClient(clientDetailsDTO.getClientID(),
                            clientDetailsDTO.getLoginName(),clientDetailsDTO.getRegistrantType()));
                }
            }
            return clientList;
        } else {
            return new ArrayList<LLIDropdownClient>(new ArrayList<LLIDropdownClient>() {{
                //TODO need to check
                add(new LLIDropdownClient(loginDTO.getAccountID(), loginDTO.getUsername(),-1));
            }});
        }
    }


    private boolean isActiveClient(ClientDTO clientDTO) {
        if (StateRepository.getInstance().getStateDTOByStateID(clientDTO.getCurrentStatus())
                .getActivationStatus() == EntityTypeConstant.STATUS_ACTIVE) {
            return true;
        }
        return false;
    }

    public List<LLIDropdownPair> getUserListByPartialName(String partialName, LoginDTO loginDTO) {
        List<LLIDropdownPair> userList = new ArrayList<LLIDropdownPair>();
        List<UserDTO> allUserList = UserRepository.getInstance().getUserList();

        for(UserDTO user : allUserList) {
            userList.add(new LLIDropdownPair(user.getUserID(), user.getUsername()));
        }

        return userList;
    }

    @Transactional(transactionType= TransactionType.READONLY)
    public List<Map<String, String>> getAccountListByClientID(long clientID) throws Exception {
        AccountingEntryService accountingEntryService = ServiceDAOFactory.getService(AccountingEntryService.class);

        List<Map<String, String>> accountList = new ArrayList<>();

        //Adding Adjustable
        Map<String,String> adjustableAccount = new HashMap<>();
        adjustableAccount.put("name", "Adjustable");
        adjustableAccount.put("balance", String.valueOf(accountingEntryService.
                getBalanceByClientIDAndAccountID(clientID, AccountType.ADJUSTABLE.getID())));
        accountList.add(adjustableAccount);

        //Adding Security
        Map<String,String> securityAccount = new HashMap<>();
        securityAccount.put("name", "Security");
        securityAccount.put("balance", String.valueOf(accountingEntryService
                .getBalanceByClientIDAndAccountID(clientID, AccountType.SECURITY.getID())));
        accountList.add(securityAccount);

        return accountList;
    }

    public long validateOwnClientID(long clientID, LoginDTO loginDTO) {
        if(loginDTO.getIsAdmin()) {
            return clientID;
        } else {
            return loginDTO.getAccountID();
        }
    }

    public Map<String, String> getClientDetailsByClient(long clientID) {
        Map<String, String> clientDetails = new HashMap<>();
        ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getModuleClientByClientIDAndModuleID(
                clientID, ModuleConstants.Module_ID_NIX);
        clientDetails.put("clientID", String.valueOf(clientID));
        clientDetails.put("loginName", clientDetailsDTO.getLoginName());
        clientDetails.put("registrantType", RegistrantTypeConstants.RegistrantTypeName.
                get(clientDetailsDTO.getRegistrantType()));
        clientDetails.put("registrantCategory", RegistrantCategoryConstants.RegistrantCategoryName.
                get(clientDetailsDTO.getRegistrantCategory()));
        clientDetails.put("clientType", ClientForm.CLIENT_TYPE_STR.get(clientDetailsDTO.getClientCategoryType()));
        return clientDetails;
    }


    public List<ClientDetailsDTO> getNIXClients(Collection recordIDs) {

        List<ClientDetailsDTO> clientList = new ArrayList<>();

        ArrayList<ClientDetailsDTO> allClientDetailsDTOList = AllClientRepository.getInstance().getAllVpnCleint();

        for(ClientDetailsDTO clientDetailsDTO : allClientDetailsDTOList)
        {
            if(isActiveNIXClient(clientDetailsDTO)
                    && isActiveClient(clientDetailsDTO)
                    && clientDetailsDTO.getModuleID() == ModuleConstants.Module_ID_NIX
                    && recordIDs.contains(clientDetailsDTO.getClientID()))
            {
                clientList.add(clientDetailsDTO);
            }
        }
        return clientList;
    }
}
