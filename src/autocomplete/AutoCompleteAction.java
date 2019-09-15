package autocomplete;

import api.ClientAPI;
import com.google.gson.Gson;
import common.ClientDTO;
import common.EntityTypeConstant;
import common.ModuleConstants;
import common.RequestFailureException;
import common.bill.BillDTO;
import common.bill.BillService;
import common.repository.AllClientRepository;
import connection.DatabaseConnection;
import databasemanager.DatabaseManager;
import login.LoginDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import permission.ActionStateDTO;
import request.CommonRequestDTO;
import request.RequestActionStateRepository;
import request.StateDTO;
import request.StateRepository;
import sessionmanager.SessionConstants;
import user.UserDTO;
import user.UserRepository;
import util.KeyValuePair;
import vpn.client.ClientDetailsDTO;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class AutoCompleteAction extends Action {

    Logger logger = Logger.getLogger(getClass());
    LoginDTO loginDTO = null;

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
        String need = request.getParameter("need");
        if ("vpnConnection".equalsIgnoreCase("")) {
            getConnectionResult(mapping, form, request, response);
        } else if ("client".equalsIgnoreCase(need)) {
            getAllClientResult(mapping, form, request, response);
        } else if ("bill".equalsIgnoreCase(need)) {
            getBillList(mapping, form, request, response);
        } else if ("domainList".equalsIgnoreCase(need)) {
            getDomainList(mapping, form, request, response);
        } else if ("user".equalsIgnoreCase(need)) {
            getUserListByName(mapping, form, request, response);
        } else if ("allclient".equalsIgnoreCase(need)) {
            getAllClientsDataToCopyByName(mapping, form, request, response);
        } else if ("modulesForClient".equalsIgnoreCase(need)) {
            getAllModulesForRegisteredClient(mapping, form, request, response);
        } else if ("usersAndClient".equalsIgnoreCase(need)) { //Used In Request Search
            getAllUsersAndClientResult(mapping, form, request, response);
        } else if ("moduleClient".equalsIgnoreCase(need)) {
            getModuleClientResult(mapping, form, request, response);
        } else if ("userForRequestSearch".equalsIgnoreCase(need)) { //Used In Request Search
            getUserResult(mapping, form, request, response);
        }
        return null;
    }


    private void getUserResult(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (loginDTO.getIsAdmin()) {
            String searchKey = StringUtils.trimToEmpty(request.getParameter("name"));

            List<UserDTO> userList = UserRepository.getInstance().getUserList();
            ArrayList<AutoCompleteAction.Data> list = new ArrayList<AutoCompleteAction.Data>();
            for (UserDTO userDTO : userList) {
                if (userDTO.getUsername().startsWith(searchKey)) {
                    AutoCompleteAction.Data item = new AutoCompleteAction.Data();
                    item.id = (int) userDTO.getUserID();
                    item.id = 0 - item.id;
                    item.data = userDTO.getUsername();
                    list.add(item);
                }
            }
            response.getWriter().write(new Gson().toJson(list));
        }
        return;
    }


    private void getModuleClientResult(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        int moduleID = Integer.parseInt((String) request.getParameter("moduleID"));
        String searchKey = StringUtils.trimToEmpty(request.getParameter("name"));
        String result = "";
        response.setContentType("application/json");

        ArrayList<ClientDetailsDTO> vpnClientList = AllClientRepository.getInstance().getAllVpnCleint();
        ArrayList<ClientDetailsDTO> vpnClientListMatchingModuleID = new ArrayList<ClientDetailsDTO>();
        for (ClientDetailsDTO clientDetailsDTO : vpnClientList) {
            if (clientDetailsDTO.getModuleID() == moduleID) {
                vpnClientListMatchingModuleID.add(clientDetailsDTO);
            }
        }
        ArrayList<AutoCompleteAction.Data> matchedVpnClientIdDataList = new ArrayList<AutoCompleteAction.Data>();

        for (ClientDetailsDTO vpnClient : vpnClientListMatchingModuleID) {
            if (vpnClient.getLoginName().startsWith(searchKey)) {
                AutoCompleteAction.Data item = new AutoCompleteAction.Data();
                item.id = (int) vpnClient.getId();
                item.data = vpnClient.getLoginName();
                matchedVpnClientIdDataList.add(item);
            }
        }
        result = new Gson().toJson(matchedVpnClientIdDataList);

        response.getWriter().write(result);
        return;
    }


    private void getAllUsersAndClientResult(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                            HttpServletResponse response) {

        if (loginDTO.getIsAdmin()) {
            String searchKey = StringUtils.trimToEmpty(request.getParameter("name"));

            String result = "";
            response.setContentType("application/json");

            List<UserDTO> userList = UserRepository.getInstance().getUserList();
            ArrayList<AutoCompleteAction.Data> list = new ArrayList<AutoCompleteAction.Data>();
            for (UserDTO userDTO : userList) {
                if (userDTO.getUsername().startsWith(searchKey)) {
                    AutoCompleteAction.Data item = new AutoCompleteAction.Data();
                    item.id = (int) userDTO.getUserID();
                    item.id = 0 - item.id;
                    item.data = userDTO.getUsername() + " (Admin)";
                    list.add(item);
                }
            }

            int moduleID = Integer.parseInt((String) request.getParameter("moduleID"));
            String status = (String) request.getParameter("status");

            ArrayList<ClientDetailsDTO> clientList = AllClientRepository.getInstance().getAllVpnCleint();
            ClientDetailsDTO dto;

            HashSet<Long> uniqueList = new HashSet<Long>();
            for (ClientDTO client : clientList) {
                if (client.getLoginName().startsWith(searchKey)) {
                    if (moduleID == -1) {
                        if (!uniqueList.contains(client.getClientID())) {
                            AutoCompleteAction.Data item = new AutoCompleteAction.Data();
                            item.id = (int) client.getClientID();
                            item.data = client.getLoginName();

                            list.add(item);

                            if (list.size() >= 10) {
                                break;
                            }
                            uniqueList.add(client.getClientID());
                        }
                    } else {
                        dto = AllClientRepository.getInstance()
                                .getModuleClientByClientIDAndModuleID(client.getClientID(), moduleID);
                        if (dto != null && dto.getModuleID() == moduleID
                                && (!uniqueList.contains(client.getClientID()))) {
                            AutoCompleteAction.Data item = new AutoCompleteAction.Data();
                            item.id = (int) client.getClientID();
                            item.data = client.getLoginName() + " (Client)";
                            item.clientType = dto.getClientCategoryType();
                            item.registrantType = dto.getRegistrantType();

                            if (StringUtils.isNotBlank(status) && Data.STATUS_ACTIVE.equals(status)) {
                                if (StateRepository.getInstance().getStateDTOByStateID(dto.getCurrentStatus())
                                        .getActivationStatus() == EntityTypeConstant.STATUS_ACTIVE) {
                                    list.add(item);
                                }
                            } else {
                                list.add(item);
                            }
                            if (list.size() >= 10) {
                                break;
                            }
                            uniqueList.add(client.getClientID());
                        }
                    }
                }
            }
            result = new Gson().toJson(list);
            try {
                response.getWriter().write(result);
            } catch (Exception e) {
            }
        }
        return;
    }


    private void getAllModulesForRegisteredClient(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                                  HttpServletResponse response) throws Exception {
        List<HashMap<String, String>> moduleListByClientID = AllClientRepository.getInstance().getModuleListByClientID(Long.parseLong(request.getParameter("clientID")));
        List<HashMap<String, String>> modulesAlreadyRegisteredByClient = new ArrayList<HashMap<String, String>>();

        ClientDetailsDTO moduleClient;
        int statusOfModuleClient;
        StateDTO stateDTO;

        for (HashMap<String, String> module : moduleListByClientID) {
            moduleClient = AllClientRepository.getInstance().getVpnClientByClientID(Long.parseLong(request.getParameter("clientID")), Integer.parseInt(module.get("moduleID")));
            statusOfModuleClient = moduleClient.getCurrentStatus();
            stateDTO = StateRepository.getInstance().getStateDTOByStateID(statusOfModuleClient);
            if (stateDTO != null) {

                KeyValuePair<ClientDetailsDTO, ClientContactDetailsDTO> pair = ClientAPI.getInstance()
                        .getPairOfClientDetailsAndClientContactDetails(
                                Long.parseLong(request.getParameter("clientID")), Integer.parseInt(module.get("moduleID")),
                                ClientContactDetailsDTO.BILLING_CONTACT
                        );

                ClientContactDetailsDTO contactDetailsDTO = pair.value;

                if (stateDTO.getActivationStatus() != EntityTypeConstant.STATUS_NOT_ACTIVE &&
                        !contactDetailsDTO.getRegistrantsName().equals("")) {
                    modulesAlreadyRegisteredByClient.add(module);
                }
            }
        }

        try {
            String result = new Gson().toJson(modulesAlreadyRegisteredByClient);
            response.getWriter().write(result);
        } catch (Exception e) {
        }
        return;
    }

    //Dhrubo
    private void getAllClientsDataToCopyByName(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                               HttpServletResponse response) {
        ArrayList<AutoCompleteAction.Data> list = new ArrayList<AutoCompleteAction.Data>();
        try {
            String searchKey = StringUtils.trimToEmpty(request.getParameter("name"));

            if (loginDTO.getIsAdmin()) {
                ArrayList<ClientDetailsDTO> clientList = AllClientRepository.getInstance().getAllVpnCleint();

                HashSet<Long> uniqueList = new HashSet<Long>();
                for (ClientDTO client : clientList) {
                    if (client.getLoginName().startsWith(searchKey)) {
                        if (!uniqueList.contains(client.getClientID())) {
                            AutoCompleteAction.Data item = new AutoCompleteAction.Data();
                            item.id = (int) client.getClientID();
                            item.data = client.getLoginName();

                            list.add(item);

                            if (list.size() >= 10) {
                                break;
                            }
                            uniqueList.add(client.getClientID());
                        }
                    }
                }
            } else {
                AutoCompleteAction.Data item = new AutoCompleteAction.Data();
                item.id = (int) loginDTO.getAccountID();
                item.data = loginDTO.getUsername();
                list.add(item);
            }
        } catch (Exception e) {
        }
        try {
            String result = new Gson().toJson(list);
            response.getWriter().write(result);
        } catch (Exception e) {

        }
        return;
    }

    private void getClientResult(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        String requestFrom = request.getParameter("page");
        String need = request.getParameter("need");
        String searchKey = StringUtils.trimToEmpty(request.getParameter("name"));
        String result = "";
        response.setContentType("application/json");
        if (requestFrom.equalsIgnoreCase("addVpnLink")) {
            if (need.equalsIgnoreCase("vpnClient")) {
                /*
                 * String sql =
                 * "select vcID, vcConName from at_vpn_con where vcClientID="
                 * +loginDTO.getAccountID()+" and vcConName like '%" + searchKey
                 * + "%' limit 10"; logger.debug(sql); result =
                 * getSearchResult(sql,loginDTO);
                 */

                ArrayList<ClientDetailsDTO> clientList = AllClientRepository.getInstance().getAllVpnCleint();
                ArrayList<AutoCompleteAction.Data> list = new ArrayList<AutoCompleteAction.Data>();
                for (ClientDTO client : clientList) {
                    if (client.getLoginName().startsWith(searchKey)) {
                        AutoCompleteAction.Data item = new AutoCompleteAction.Data();
                        item.id = (int) client.getClientID();
                        item.data = client.getLoginName();
                        list.add(item);
                    }
                }
                result = new Gson().toJson(list);
            }
        }
        response.getWriter().write(result);
        return;
    }

    private void getUserListByName(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {
        String need = request.getParameter("need");
        String searchKey = StringUtils.trimToEmpty(request.getParameter("name"));
        String result = "";
        response.setContentType("application/json");
        if (need.equalsIgnoreCase("user")) {

            List<UserDTO> userList = UserRepository.getInstance().getUserList();
            ArrayList<AutoCompleteAction.Data> list = new ArrayList<AutoCompleteAction.Data>();
            for (UserDTO userDTO : userList) {
                if (userDTO.getUsername().startsWith(searchKey)) {
                    AutoCompleteAction.Data item = new AutoCompleteAction.Data();
                    item.id = (int) userDTO.getUserID();
                    item.data = userDTO.getUsername();
                    list.add(item);
                }
            }
            result = new Gson().toJson(list);
        }
        response.getWriter().write(result);
        return;
    }

    private void getAllClientResult(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                    HttpServletResponse response) {
        ArrayList<AutoCompleteAction.Data> list = new ArrayList<AutoCompleteAction.Data>();
        try {
            String searchKey = StringUtils.trimToEmpty(request.getParameter("name"));
            int moduleID = Integer.parseInt((String) request.getParameter("moduleID"));
            String status = (String) request.getParameter("status");

            if (loginDTO.getIsAdmin()) {
                ArrayList<ClientDetailsDTO> clientList = AllClientRepository.getInstance().getAllVpnCleint();
                ClientDetailsDTO dto;

                HashSet<Long> uniqueList = new HashSet<Long>();
                for (ClientDTO client : clientList) {
                    if (client.getLoginName().startsWith(searchKey)) {
                        if (moduleID == -1) {
                            if (!uniqueList.contains(client.getClientID())) {
                                AutoCompleteAction.Data item = new AutoCompleteAction.Data();
                                item.id = (int) client.getClientID();
                                item.data = client.getLoginName();

                                list.add(item);

                                //if (list.size() >= 10) {break;}
                                uniqueList.add(client.getClientID());
                            }
                        } else {
                            dto = AllClientRepository.getInstance()
                                    .getModuleClientByClientIDAndModuleID(client.getClientID(), moduleID);
                            if (dto != null && dto.getModuleID() == moduleID
                                    && (!uniqueList.contains(client.getClientID()))) {
                                AutoCompleteAction.Data item = new AutoCompleteAction.Data();
                                item.id = (int) client.getClientID();
                                item.data = client.getLoginName();
                                item.clientType = dto.getClientCategoryType();
                                item.registrantType = dto.getRegistrantType();

                                if (StringUtils.isNotBlank(status) && Data.STATUS_ACTIVE.equals(status)) {
                                    if (StateRepository.getInstance().getStateDTOByStateID(dto.getCurrentStatus())
                                            .getActivationStatus() == EntityTypeConstant.STATUS_ACTIVE) {
                                        list.add(item);
                                    }
                                } else {
                                    list.add(item);
                                }
                                uniqueList.add(client.getClientID());
                            }
                        }
                    }
                }
            } else {
                AutoCompleteAction.Data item = new AutoCompleteAction.Data();
                item.id = (int) loginDTO.getAccountID();
                item.data = loginDTO.getUsername();
                list.add(item);
            }
        } catch (Exception e) {
            logger.debug("fatal", e);
        }
        try {
            String result = new Gson().toJson(list);
            response.getWriter().write(result);
        } catch (Exception e) {

        }

        return;
    }

    private void getConnectionResult(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {
        String requestFrom = request.getParameter("page");
        String need = request.getParameter("need");
        String searchKey = StringUtils.trimToEmpty(request.getParameter("name"));
        String result = "";
        response.setContentType("application/json");
        if (requestFrom.equalsIgnoreCase("addVpnLink")) {
            if (need.equalsIgnoreCase("vpnConnection")) {
                String sql = "select vcID, vcConName from at_vpn_con where vcClientID=" + loginDTO.getAccountID()
                        + " and vcConName like '%" + searchKey + "%' limit 20";
                logger.debug(sql);
                result = getSearchResult(sql, loginDTO);
            }
        }
        response.getWriter().write(result);
        return;
    }

    private void getBillList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
        int moduleID = Integer.parseInt((String) request.getParameter("moduleID"));
        int clientID = Integer.parseInt((String) request.getParameter("clientID"));
        BillService billService = new BillService();


        response.setContentType("application/json");
        ArrayList<BillDTO> billList = (ArrayList<BillDTO>) billService
                .getUnPaidBillDTOListByModuleIDAndClientID(moduleID, clientID);
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        ArrayList<Long> reqList = new ArrayList<Long>();
        if (billList != null) {
            logger.debug(billList.size());
            for (BillDTO dto : billList) {
                reqList.add(dto.getReqID());
            }
        }
        HashMap<Long, CommonRequestDTO> reqIDtoObjMap = billService.getRequestIDtoObject(reqList);
        logger.debug(reqIDtoObjMap);
        for (BillDTO dto : billList) {
            HashMap<String, String> obj = new HashMap<String, String>();
            obj.put("ID", dto.getID() + "");
            logger.debug("dto " + dto);
            ActionStateDTO actionStateDTO = RequestActionStateRepository.getInstance()
                    .getActionStateDTOActionTypeID(reqIDtoObjMap.get(dto.getReqID()).getRequestTypeID());
            obj.put("billName", RequestActionStateRepository.getInstance()
                    .getActionStateDTOActionTypeID(actionStateDTO.getRootActionTypeID()).getDescription());
            if (moduleID == ModuleConstants.Module_ID_DOMAIN) {


            } else {
                obj.put("entityName", "");
            }

            list.add(obj);
        }
        String result = new Gson().toJson(list);
        response.getWriter().write(result);
        return;
    }

    private void getDomainList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
        int moduleID = Integer.parseInt((String) request.getParameter("moduleID"));
        int clientID = Integer.parseInt((String) request.getParameter("clientID"));
//		DomainDAO domainDAO = new DomainDAO();
        List listOfActiveStatus = StateRepository.getInstance().getStatusListByActivationStatus(EntityTypeConstant.STATUS_ACTIVE);

//		List<DomainDTO> list = null;

        response.setContentType("application/json");
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();

            String activeStatusStr = common.StringUtils.getCommaSeparatedString(listOfActiveStatus);

            String conditionString = "";
            conditionString += " WHERE  domDomainClientID = " + clientID;
            if (listOfActiveStatus.size() > 2) {
                conditionString += " and domCurrentStatus in " + activeStatusStr;
                conditionString += " and domLatestStatus in " + activeStatusStr;
            }


            //list = domainDAO.getAllDomainDTOsByClientID(conditionString, databaseConnection);

        } catch (Exception e) {
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception e2) {
            }
            logger.error("Exception Package Type Edit", e);
            if (e instanceof RequestFailureException) {
                throw (RequestFailureException) e;
            }
            throw e;
        } finally {
            databaseConnection.dbClose();
        }
//		String result = new Gson().toJson(list);
//		response.getWriter().write(result);
        return;
    }

    private String getSearchResult(String sql, LoginDTO loginDTO) throws Exception {
        Connection cn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String searchList = "";
        Data data;
        try {

            ArrayList<AutoCompleteAction.Data> list = new ArrayList<AutoCompleteAction.Data>();
            cn = DatabaseManager.getInstance().getConnection();
            stmt = cn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                data = new AutoCompleteAction.Data();
                data.id = rs.getInt(1);
                data.data = rs.getString(2);
                list.add(data);
            }
            rs.close();
            searchList = new Gson().toJson(list);

        } catch (Exception ex) {
            logger.debug("Ex AutoComplateAction", ex);
            throw ex;

        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
                stmt = null;
            }
            if (cn != null) {
                try {
                    cn.close();
                } catch (SQLException e) {
                }
                cn = null;
            }
        }
        return searchList;
    }

    public class Data {
        public int id;
        public String data;
        public int clientType;
        public int registrantType;
        public static final String STATUS_ACTIVE = "active";

        @Override
        public String toString() {
            return "Data [id=" + id + ", data=" + data + ", clientType=" + clientType + ", registrantType="
                    + registrantType + "]";
        }

    }
}
