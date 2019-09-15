package datamigrationLLIConnections;

import annotation.Transactional;
import datamigrationLLIClient.LLIClientMigrationService;
import global.GlobalService;
import inventory.InventoryService;
import ip.IPConstants;
import ip.ipUsage.IPBlockUsage;
import ip.ipVsLLIConnection.IPvsConnection;
import lli.Application.LocalLoop.LocalLoop;
import lli.Application.Office.Office;
import lli.LLIConnectionInstance;
import lli.LLIConnectionInstanceConditionBuilder;
import lli.connection.LLIConnectionConstants;
import requestMapping.Service;
import user.UserDTO;
import user.UserRepository;
import user.UserService;
import util.*;
import vpn.client.ClientDetailsDTO;
import vpn.clientContactDetails.ClientContactDetailsDTO;
import vpn.clientContactDetails.ClientContactDetailsDTOConditionBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;


public class LLIConnectionMigrationService {

    @Service
    GlobalService globalService;

    InventoryService inventoryService = ServiceDAOFactory.getService(InventoryService.class);
    UserService userService = ServiceDAOFactory.getService(UserService.class);
    LLIClientMigrationService lliClientMigrationService = ServiceDAOFactory.getService(LLIClientMigrationService.class);

    long defaultRegionId = 2;

    @Transactional
    public void addConnection(List<String> connectionAttributes) throws Exception {

        CurrentTimeFactory.initializeCurrentTimeFactory();
        // lli connection table insertion
        LLIConnectionInstance lliConnectionInstance = new LLIConnectionInstance();

        //TODO- check if connection already exists
        //this one will not work
        long connectionId = connectionExists(
                connectionAttributes.get(LLIConnectionExistingDataColumnNameMap.CONNECTION_NAME));

        long connectionHistoryId = DatabaseConnectionFactory.getCurrentDatabaseConnection()
                .getNextIDWithoutIncrementing(ModifiedSqlGenerator.getTableName(LLIConnectionInstance.class));

        if (connectionId == -1) {
            lliConnectionInstance.setID(connectionHistoryId);
        } else {
            lliConnectionInstance.setID(connectionId);
        }

        long clientId = clientExists(connectionAttributes.get(LLIConnectionExistingDataColumnNameMap.CLIENT_NAME));

        if (clientId == -1)
            return;

        lliConnectionInstance.setClientID(clientId);
        lliConnectionInstance.setName(connectionAttributes.get(LLIConnectionExistingDataColumnNameMap.CONNECTION_NAME));

        String connectionType = connectionAttributes.get(LLIConnectionExistingDataColumnNameMap.CONNECTION_TYPE);

        if (connectionType.equalsIgnoreCase("regular"))
            lliConnectionInstance.setConnectionType(LLIConnectionConstants.CONNECTION_TYPE_REGULAR);
        else if (connectionType.equalsIgnoreCase("cache"))
            lliConnectionInstance.setConnectionType(LLIConnectionConstants.CONNECTION_TYPE_CACHE);
        else if (connectionType.equalsIgnoreCase("temporary"))
            lliConnectionInstance.setConnectionType(LLIConnectionConstants.CONNECTION_TYPE_TEMPORARY);
        else if (connectionType.equalsIgnoreCase("regular long"))
            lliConnectionInstance.setConnectionType(LLIConnectionConstants.CONNECTION_TYPE_REGULAR_LONG);

        lliConnectionInstance.setCostChartID(0);

        if (connectionAttributes.get(LLIConnectionExistingDataColumnNameMap.ACTIVATION_DATE).length() > 2) {
            lliConnectionInstance.setActiveFrom((getLongFromDate(
                    connectionAttributes.get(LLIConnectionExistingDataColumnNameMap.ACTIVATION_DATE))));
            lliConnectionInstance.setStartDate((getLongFromDate(
                    connectionAttributes.get(LLIConnectionExistingDataColumnNameMap.ACTIVATION_DATE))));
            lliConnectionInstance.setValidFrom(lliConnectionInstance.getActiveFrom());
        }

        lliConnectionInstance.setActiveTo(Long.MAX_VALUE);
        lliConnectionInstance.setValidTo(Long.MAX_VALUE);


        if (connectionAttributes.get(LLIConnectionExistingDataColumnNameMap.BANDWIDTH) != "")
            lliConnectionInstance.setBandwidth(
                    Double.parseDouble(connectionAttributes.get(LLIConnectionExistingDataColumnNameMap.BANDWIDTH)));

        lliConnectionInstance.setStatus(1);
        lliConnectionInstance.setIncident(1);
        lliConnectionInstance.setDiscountRate(0);
        lliConnectionInstance.setZoneID(0);

        ModifiedSqlGenerator.insert(lliConnectionInstance, LLIConnectionInstance.class, false);
        // lli office table insertion
        addLLIOffice(connectionAttributes, connectionHistoryId);
        addMandatroyIpUsage(connectionAttributes, connectionHistoryId);
        addAdditionalIpUsage(connectionAttributes, connectionHistoryId);

        CurrentTimeFactory.destroyCurrentTimeFactory();
    }


    public void addLLIOffice(List<String> connectionAttributes, long connectionHistoryId) throws Exception {

        Office office = new Office();

        long officeId = officeExists(
                connectionAttributes.get(LLIConnectionExistingDataColumnNameMap.CONNECTION_ADDRESS));

        long officeHistoryId = DatabaseConnectionFactory.getCurrentDatabaseConnection()
                .getNextIDWithoutIncrementing(ModifiedSqlGenerator.getTableName(Office.class));

        if (officeId == -1) {
            office.setId(officeHistoryId);
        } else {
            office.setId(officeId);
        }

        office.setConnectionID(connectionHistoryId);
        office.setOfficeName(connectionAttributes.get(LLIConnectionExistingDataColumnNameMap.CONNECTION_NAME));
        office.setOfficeAddress(connectionAttributes.get(LLIConnectionExistingDataColumnNameMap.CONNECTION_ADDRESS));
        office.setApplicationId(0);


        ModifiedSqlGenerator.insert(office, Office.class, false);

        // lli local loop table insertion
        addLLILocalLoop(connectionAttributes, officeHistoryId);

    }

    public void addLLILocalLoop(List<String> connectionAttributes, long officeHistoryId) throws Exception {

        LocalLoop localLoop = new LocalLoop();

        long localLoopHistoryId = DatabaseConnectionFactory.getCurrentDatabaseConnection()
                .getNextIDWithoutIncrementing(ModifiedSqlGenerator.getTableName(LocalLoop.class));

        localLoop.setId(localLoopHistoryId);
        localLoop.setOfficeID(officeHistoryId);


        if (check(connectionAttributes.get(LLIConnectionExistingDataColumnNameMap.DISTANCE_COVERED_BY_CLIENT))) {
            localLoop.setClientDistances(Integer
                    .parseInt(connectionAttributes.get(LLIConnectionExistingDataColumnNameMap.DISTANCE_COVERED_BY_CLIENT)));
        }


        if (check(connectionAttributes.get(LLIConnectionExistingDataColumnNameMap.DISTANCE_COVERED_BY_BTCL))) {
            localLoop.setBTCLDistances(Integer
                    .parseInt(connectionAttributes.get(LLIConnectionExistingDataColumnNameMap.DISTANCE_COVERED_BY_BTCL)));
        }

        if (check(connectionAttributes
                .get(LLIConnectionExistingDataColumnNameMap.DISTANCE_COVERED_BY_OUTSOURCING_COMPANY))) {
            localLoop.setOCDistances(Integer.parseInt(connectionAttributes
                    .get(LLIConnectionExistingDataColumnNameMap.DISTANCE_COVERED_BY_OUTSOURCING_COMPANY)));
        }

        UserDTO user = null;
        //OCId
        if (!connectionAttributes.get(LLIConnectionExistingDataColumnNameMap.OUTSOURCING_COMPANY_NAME).trim().equals("") &&
                connectionAttributes.get(LLIConnectionExistingDataColumnNameMap.OUTSOURCING_COMPANY_NAME) != null) {

            user = UserRepository.getInstance().getUserDTOByUserName(connectionAttributes.
                    get(LLIConnectionExistingDataColumnNameMap.OUTSOURCING_COMPANY_NAME));
            if (user != null)
                localLoop.setOCID(user.getUserID());
        }
        //vlanId
        localLoop.setVLANID(inventoryService.getItemIdByItemName(connectionAttributes.get(LLIConnectionExistingDataColumnNameMap.MANDATORY_VLAN)));
        //numOfCore in database
        String ofcType = connectionAttributes.get(LLIConnectionExistingDataColumnNameMap.OFC_TYPE);

        if (ofcType.equalsIgnoreCase("single")) {
            localLoop.setOfcType(LLIConnectionConstants.OFC_TYPE_SINGLE);

        } else if (ofcType.equalsIgnoreCase("dual")) {
            localLoop.setOfcType(LLIConnectionConstants.OFC_TYPE_DUAL);

        } else {
            localLoop.setOfcType(0);
        }

        //applicationId
        localLoop.setApplicationID(0);

        //popId
        localLoop.setPopID(inventoryService.getItemIdByItemName(connectionAttributes.get(LLIConnectionExistingDataColumnNameMap.POP)));

        //bandwidth
        if (connectionAttributes.get(LLIConnectionExistingDataColumnNameMap.BANDWIDTH) != "")
            localLoop.setBandwidth(
                    Long.parseLong(connectionAttributes.get(LLIConnectionExistingDataColumnNameMap.BANDWIDTH)));

        //router_switch_id
        localLoop.setPopID(inventoryService.getItemIdByItemName(connectionAttributes.get(LLIConnectionExistingDataColumnNameMap.ROUTER_SWITCH)));

        //portid
        localLoop.setPopID(inventoryService.getItemIdByItemName(connectionAttributes.get(LLIConnectionExistingDataColumnNameMap.PORT_NUMBER)));

        //adjustedBtclDistance
        localLoop.setAdjustedBTClDistance(0);

        //adjustedOCDistance
        localLoop.setAdjustedOCDistance(0);


        ModifiedSqlGenerator.insert(localLoop, LocalLoop.class, false);

    }

    private void addMandatroyIpUsage(List<String> connectionAttributes, long connectionId) throws Exception {

        IPBlockUsage ipBlockUsage = new IPBlockUsage();

        List<String> ips = getIpStartAndEndsFromString(connectionAttributes.get(LLIConnectionExistingDataColumnNameMap.MANDATORY_IP_RANGE));

        if (ips == null)
            return;

        ipBlockUsage.setFromIP(ips.get(0));
        ipBlockUsage.setToIP(ips.get(1));

        ipBlockUsage.setVersion(determineIpType(ipBlockUsage.getFromIP()));
        ipBlockUsage.setPurpose(IPConstants.Purpose.LLI_CONNECTION);
        ipBlockUsage.setRegionId(defaultRegionId);
        ipBlockUsage.setCreationTime(System.currentTimeMillis());
        ipBlockUsage.setLastModificationTime(System.currentTimeMillis());
        ipBlockUsage.setActiveFrom(System.currentTimeMillis());
        ipBlockUsage.setActiveTo(Long.MAX_VALUE);

        ipBlockUsage.setDeleted(false);
        ipBlockUsage.setSubRegionId(0);
        ipBlockUsage.setType(IPConstants.Type.PUBLIC);

        ModifiedSqlGenerator.insert(ipBlockUsage, IPBlockUsage.class, false);

        long ipUsageId = ipBlockUsage.getId();

        addIpVsConnectionMandatory(connectionAttributes, ipUsageId, connectionId);
    }


    private void addAdditionalIpUsage(List<String> connectionAttributes, long connectionId) throws Exception {

        IPBlockUsage ipBlockUsage = new IPBlockUsage();

        List<String> ips = getIpStartAndEndsFromString(connectionAttributes.get(LLIConnectionExistingDataColumnNameMap.ADDITIONAL_IP_RANGE));

        if (ips == null) return;

        ipBlockUsage.setFromIP(ips.get(0));
        ipBlockUsage.setToIP(ips.get(1));

        ipBlockUsage.setVersion(determineIpType(ipBlockUsage.getFromIP()));
        ipBlockUsage.setPurpose(IPConstants.Purpose.LLI_CONNECTION);
        ipBlockUsage.setRegionId(defaultRegionId);
        ipBlockUsage.setCreationTime(System.currentTimeMillis());
        ipBlockUsage.setLastModificationTime(System.currentTimeMillis());
        ipBlockUsage.setActiveFrom(System.currentTimeMillis());
        ipBlockUsage.setActiveTo(Long.MAX_VALUE);

        ipBlockUsage.setDeleted(false);
        ipBlockUsage.setSubRegionId(0);
        ipBlockUsage.setType(IPConstants.Type.PUBLIC);

        ModifiedSqlGenerator.insert(ipBlockUsage, IPBlockUsage.class, false);

        long ipUsageId = ipBlockUsage.getId();

        addIpVsConnectionAdditional(connectionAttributes, ipUsageId, connectionId);
    }


    private void addIpVsConnectionMandatory(List<String> connectionAttributes, long ipUsageId, long connectionId) throws Exception {

        IPvsConnection iPvsConnection = new IPvsConnection();

        iPvsConnection.setConnectionId(connectionId);
        iPvsConnection.setIpUsageId(ipUsageId);
        iPvsConnection.setRoutingInfoId(0);
        iPvsConnection.setCreationTime(System.currentTimeMillis());
        iPvsConnection.setLastModificationTime(System.currentTimeMillis());
        iPvsConnection.setActiveFrom(System.currentTimeMillis());
        iPvsConnection.setActiveTo(Long.MAX_VALUE);
        iPvsConnection.setDeleted(false);
        iPvsConnection.setUsageType(LLIConnectionConstants.IPUsageType.MANDATORY);

        ModifiedSqlGenerator.insert(iPvsConnection, IPvsConnection.class, false);
    }

    private void addIpVsConnectionAdditional(List<String> connectionAttributes, long ipUsageId, long connectionId) throws Exception {

        IPvsConnection iPvsConnection = new IPvsConnection();

        iPvsConnection.setConnectionId(connectionId);
        iPvsConnection.setIpUsageId(ipUsageId);
        iPvsConnection.setRoutingInfoId(0);
        iPvsConnection.setCreationTime(System.currentTimeMillis());
        iPvsConnection.setLastModificationTime(System.currentTimeMillis());
        iPvsConnection.setActiveFrom(System.currentTimeMillis());
        iPvsConnection.setActiveTo(Long.MAX_VALUE);
        iPvsConnection.setDeleted(false);
        iPvsConnection.setUsageType(LLIConnectionConstants.IPUsageType.ADDITIONAL);

        ModifiedSqlGenerator.insert(iPvsConnection, IPvsConnection.class, false);
    }

    private IPConstants.Version determineIpType(String ip) {

        if (ip.length() > 15)
            return IPConstants.Version.IPv6;
        return IPConstants.Version.IPv4;

    }

    public static List<String> getIpStartAndEndsFromString(String ipString) {

        if (ipString.length() < 4) {
            return null;
        }

        List<String> strings = new ArrayList<>();

        StringTokenizer tokenizer =
                new StringTokenizer(ipString, "-");

        while (tokenizer.hasMoreTokens())
            strings.add(tokenizer.nextToken().trim());

        if (strings.isEmpty()) return null;

        String fromIp = strings.get(0);

        List<String> partsOfFromIp = new ArrayList<>();

        tokenizer = new StringTokenizer(fromIp, ".");

        while (tokenizer.hasMoreTokens())
            partsOfFromIp.add(tokenizer.nextToken().trim());

        String toIp = "";
        for (int i = 0; i < 3; i++) {
            toIp += partsOfFromIp.get(i);
            toIp += ".";
        }

        toIp += strings.get(1);

        strings.clear();
        strings.add(fromIp);
        strings.add(toIp);

        return strings;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    long officeExists(String connectionAddress) throws Exception {

        List<Office> list = ModifiedSqlGenerator.getAllObjectList(Office.class,
                " Where " +
                        ModifiedSqlGenerator.getColumnName(Office.class, "officeAddress") +
                        " = '" +
                        connectionAddress
                        + "'");

        if (list.isEmpty())
            return -1;
        else
            return list.get(0).getHistoryId();
    }

    @Transactional(transactionType = TransactionType.READONLY)
    long connectionExists(String connectionName) throws Exception {

        List<LLIConnectionInstance> list = ModifiedSqlGenerator.getAllObjectList(LLIConnectionInstance.class,
                new LLIConnectionInstanceConditionBuilder().Where().nameEquals(connectionName).getCondition());

        if (list.isEmpty())
            return -1;
        else
            return list.get(0).getID();
    }

    @Transactional(transactionType = TransactionType.READONLY)
    long clientExists(String clientFullName) throws Exception {

        List<ClientContactDetailsDTO> clientContactDetailsDTOS = globalService
                .getAllObjectListByCondition(ClientContactDetailsDTO.class,
                        new ClientContactDetailsDTOConditionBuilder()
                                .Where()
//                                .registrantsNameBothLike(clientFullName)
                                .detailsTypeEquals(ClientContactDetailsDTO.REGISTRANT_CONTACT)
                                .getCondition());

        clientContactDetailsDTOS = clientContactDetailsDTOS.stream()
                .filter(t ->
                {
                    return t.getRegistrantsName().trim().toLowerCase().equals(clientFullName.trim().toLowerCase());
                }).collect(Collectors.toList());

        if (clientContactDetailsDTOS.isEmpty())
            return -1;

        long detailsDTOId = clientContactDetailsDTOS.get(0).getVpnClientID();

        ClientDetailsDTO clientDetailsDTO = globalService.findByPK(ClientDetailsDTO.class, detailsDTOId);

        long clientId = clientDetailsDTO.getClientID();

        return clientId;
    }

    public static String fixDateFormat(String givenDate) {

        String formattedDate = "";
        if (givenDate == "") {
            // handle here
            // what happens if the activation date is not provided
            // Either current system time can be set or it can be kept empty
            return formattedDate;
        }
        // fix the separators
        formattedDate = givenDate.replaceAll("/", "-");

        String[] dateParameters = formattedDate.split("-");
        // fix the month
        if (dateParameters[1].length() == 1) {
            dateParameters[1] = "0" + dateParameters[1];
        }
        // fix the four digit years
        if (dateParameters[2].length() == 4) {
            dateParameters[2] = dateParameters[2].substring(2);
        }

        formattedDate = dateParameters[0] + "-" + dateParameters[1] + "-" + dateParameters[2];

        System.out.println(formattedDate);
        return formattedDate;
    }

    public static long getLongFromDate(String dateString) throws ParseException {
        System.out.println(dateString);
        dateString = fixDateFormat(dateString);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
        Date date = (Date) formatter.parse(dateString);
        long mills = date.getTime();
        return mills;
    }


    private boolean check(String s) {
        if (s.equals("") || s == null)
            return false;
        return true;
    }


}
