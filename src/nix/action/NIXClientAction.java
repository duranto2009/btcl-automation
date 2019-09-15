package nix.action;

import annotation.ForwardedAction;
import client.ClientService;
import common.ModuleConstants;
import lli.LLIDropdownClient;
import lli.LLIDropdownPair;
import login.LoginDTO;
import nix.common.NIXClientService;
import nix.connection.NIXConnection;
import nix.connection.NIXConnectionService;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import user.UserDTO;
import user.UserService;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ActionRequestMapping("nix/client")
public class NIXClientAction extends AnnotatedRequestMappingAction {

    @Service
    NIXClientService nixClientService;
    @Service
    UserService userService;
    @Service
    NIXConnectionService nixConnectionService;

    @RequestMapping(mapping="/get-client-old", requestMethod=RequestMethod.All)
    public List<LLIDropdownPair> getNIXClientOld(@RequestParameter("val") String partialName, LoginDTO loginDTO) throws Exception{
        return nixClientService.getClientListByPartialName(partialName, loginDTO);
    }

    @RequestMapping(mapping="/get-client", requestMethod=RequestMethod.All)
    public List<LLIDropdownClient> getNIXClient(@RequestParameter("val") String partialName, LoginDTO loginDTO) throws Exception{
        return nixClientService.getClientListByPartialNameByResistantType(partialName, loginDTO);
    }

    @RequestMapping(mapping="/get-client-by-id", requestMethod=RequestMethod.All)
    public LLIDropdownPair getNIXClientByID(@RequestParameter("id") Long clientID, LoginDTO loginDTO) throws Exception{
        return nixClientService.getClientByID(clientID, loginDTO);
    }

    @RequestMapping(mapping="/get-user", requestMethod=RequestMethod.All)
    public List<LLIDropdownPair> getUserList(@RequestParameter("val") String partialName, LoginDTO loginDTO) throws Exception{
        return nixClientService.getUserListByPartialName(partialName, loginDTO);
    }

    @RequestMapping(mapping="/get-loop-provider", requestMethod=RequestMethod.All)
    public List<LLIDropdownPair> getLoopProviderList() throws Exception{
        List<LLIDropdownPair> list = new ArrayList<>();
        List<UserDTO> listOfUserDTO = userService.getLocalLoopProviderList();
        for(UserDTO userDTO : listOfUserDTO) {
            list.add(new LLIDropdownPair(userDTO.getUserID(), userDTO.getUsername()));
        }
        return list;
    }

    /*NIX Client Dashboard Begins*/
    @ForwardedAction
    @RequestMapping(mapping="/board", requestMethod=RequestMethod.All)
    public String getClientBoardNew() throws Exception {
        return "nix-client-board";
    }

    //ToDo Raihan: Client Validation Annotation begins
    @RequestMapping(mapping="/get-client-details", requestMethod=RequestMethod.All)
    public Map<String, String> getClientDetailsByClient(@RequestParameter("id") long clientID, LoginDTO loginDTO) throws Exception {
        clientID = nixClientService.validateOwnClientID(clientID, loginDTO);
        return nixClientService.getClientDetailsByClient(clientID);
    }
    @RequestMapping(mapping="/get-contact-details-list", requestMethod=RequestMethod.All)
    public List<ClientContactDetailsDTO> getContactDetailsListByClient(@RequestParameter("id") long clientID, LoginDTO loginDTO) throws Exception {
        clientID = nixClientService.validateOwnClientID(clientID, loginDTO);
        return new ClientService().getClientContactDetailsDTOListByClientIDAndModuleID(clientID, ModuleConstants.Module_ID_NIX);
    }
    @RequestMapping(mapping="/get-connection-list", requestMethod=RequestMethod.All)
    public List<NIXConnection> getNIXConnectionListByClient(@RequestParameter("id") long clientID, LoginDTO loginDTO) throws Exception {
        clientID = nixClientService.validateOwnClientID(clientID, loginDTO);
        return nixConnectionService.getConnectionByClientID(clientID);
    }

    @RequestMapping(mapping="/get-account-list", requestMethod=RequestMethod.All)
    public List<Map<String, String>> getAccountListByClient(@RequestParameter("id") long clientID, LoginDTO loginDTO) throws Exception {
        clientID = nixClientService.validateOwnClientID(clientID, loginDTO);
        return nixClientService.getAccountListByClientID(clientID);
    }
}
