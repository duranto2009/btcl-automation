package nix.nixflowconnectionmanger;

import annotation.Transactional;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import common.ModuleConstants;
import common.RequestFailureException;
import global.GlobalService;
import inventory.InventoryAllocationHistoryService;
import ip.IPConstants;
import ip.IPService;
import ip.NIXIPBlockForConnection;
import lli.connection.LLIConnectionConstants;
import login.LoginDTO;
import nix.application.NIXApplication;
import nix.application.NIXApplicationService;
import nix.application.downgrade.NIXDowngradeApplication;
import nix.application.downgrade.NIXDowngradeApplicationService;
import nix.application.localloop.NIXApplicationLocalLoop;
import nix.application.localloop.NIXApplicationLocalLoopConditionBuilder;
import nix.application.localloop.NIXApplicationLocalLoopService;
import nix.application.office.NIXApplicationOffice;
import nix.application.office.NIXApplicationOfficeService;
import nix.application.upgrade.NIXUpgradeApplication;
import nix.application.upgrade.NIXUpgradeApplicationService;
import nix.connection.NIXConnection;
import nix.connection.NIXConnectionService;
import nix.constants.NIXConstants;
import nix.localloop.NIXLocalLoop;
import nix.localloop.NIXLocalLoopService;
import nix.office.NIXOffice;
import nix.office.NIXOfficeService;
import notification.NotificationService;
import util.KeyValuePair;
import util.ServiceDAOFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NIXApplicationFlowConnectionManagerService {

   //fgfg
    NIXOfficeService nixOfficeService = ServiceDAOFactory.getService(NIXOfficeService.class);
    NIXConnectionService nixConnectionService = ServiceDAOFactory.getService(NIXConnectionService.class);
    NIXApplicationService nixApplicationService = ServiceDAOFactory.getService(NIXApplicationService.class);
    IPService ipService = ServiceDAOFactory.getService(IPService.class);
    NIXApplicationOfficeService nixApplicationOfficeService = ServiceDAOFactory.getService(NIXApplicationOfficeService.class);
    NIXLocalLoopService nixLocalLoopService = ServiceDAOFactory.getService(NIXLocalLoopService.class);
    NotificationService notificationService = ServiceDAOFactory.getService(NotificationService.class);

    GlobalService globalService = ServiceDAOFactory.getService(GlobalService.class);

    @Transactional
    public void connectionCreatorOrUpdatorManager(JsonObject jsonObject, long applicationType, LoginDTO loginDTO) throws Exception {
        long appID = jsonObject.get("applicationID").getAsLong();

        if (appID > 0) {


            notificationService.markNotificationAsActionTaken(
                    ModuleConstants.Module_ID_NIX,
                    appID,
                    loginDTO.getRoleID(),
                    false,
                    loginDTO.getUserID()

            );
        }

        if (applicationType == NIXConstants.NEW_CONNECTION_APPLICATION) {
            newConnectionInsert(jsonObject, appID);
        }
        if (applicationType == NIXConstants.NIX_UPGRADE_APPLICATION ||
                applicationType == NIXConstants.NIX_DOWNGRADE_APPLICATION) {
            upgradeConnection(jsonObject, appID);

        }
    }

    private void upgradeConnection(JsonObject jsonObject, long appID) throws Exception {
        try {
            long newPortID = jsonObject.get("newPortId").getAsLong();
            NIXApplication nixApplication = nixApplicationService.getApplicationById(appID);


            // TODO: 7/12/2019 fetch all office and local loop create two list one old and one new for both office and local loop keep new ones and delete common ones


            KeyValuePair<Long, Long> portAndOffice = getPrevPortAndOffice(nixApplication);
            long oldPortID = portAndOffice.key;
            int newPortType = getPortType(nixApplication);
            try {
                ServiceDAOFactory.getService(InventoryAllocationHistoryService.class).
                        deallocationInventoryItem(oldPortID, ModuleConstants.Module_ID_NIX, nixApplication.getClient());
            } catch (Exception e) {
                try {
                    ServiceDAOFactory.getService(InventoryAllocationHistoryService.class).
                            allocateInventoryItem(oldPortID, ModuleConstants.Module_ID_NIX, nixApplication.getClient());
                    ServiceDAOFactory.getService(InventoryAllocationHistoryService.class).
                            deallocationInventoryItem(oldPortID, ModuleConstants.Module_ID_NIX, nixApplication.getClient());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            //old connection info update and new inserted
            NIXConnection oldnixConnection = ServiceDAOFactory.getService(NIXConnectionService.class).
                    getLatestNIXConnectionByConnectionHistoryId(nixApplication.getConnection());
            List<NIXOffice> nixOldOffices = oldnixConnection.getNixOffices();

            oldnixConnection.setActiveTo(System.currentTimeMillis());
            globalService.update(oldnixConnection);


            oldnixConnection.setActiveTo(Long.MAX_VALUE);
            oldnixConnection.setApplication(nixApplication.getId());
            if (nixApplication.getType() == NIXConstants.NIX_UPGRADE_APPLICATION)
                oldnixConnection.setIncidentId(NIXConstants.NIX_UPGRADE_APPLICATION);
            else oldnixConnection.setIncidentId(NIXConstants.NIX_DOWNGRADE_APPLICATION);
            oldnixConnection.setStartDate(System.currentTimeMillis());
            globalService.save(oldnixConnection);
            //end new connection instance insert

            List<NIXApplicationOffice> nixNewOffice = ServiceDAOFactory.getService(NIXApplicationOfficeService.class).
                    getOfficesByApplicationId(nixApplication.getId());
            if (nixNewOffice.size() == 0) throw new RequestFailureException("No Connection Office found");

            NIXApplicationOffice newOffice = nixNewOffice.get(0);
            NIXOffice office = new NIXOffice();
            office.setApplication_offfice(newOffice.getId());
            office.setAppication(nixApplication.getId());
            office.setConnection(oldnixConnection.getId());
            office.setName(newOffice.getName());
            globalService.save(office);
            List<NIXApplicationLocalLoop> nixApplicationLocalLoops = ServiceDAOFactory.getService(
                    NIXApplicationLocalLoopService.class).getLocalLoopByOffice(newOffice.getId());

            List<Long> newLoopPopIds = new ArrayList<>();

            //save new loop
            for (NIXApplicationLocalLoop nixApplicationLocalLoop : nixApplicationLocalLoops) {
                nixApplicationLocalLoop.setPortId(newPortID);
                nixApplicationLocalLoop.setApplication(nixApplication.getId());
                nixApplicationLocalLoop.setPortType(newPortType);
                globalService.update(nixApplicationLocalLoop);
                ServiceDAOFactory.getService(InventoryAllocationHistoryService.class).allocateInventoryItem(
                        newPortID, ModuleConstants.Module_ID_NIX, nixApplication.getClient());
                NIXLocalLoop nixLocalLoop = new NIXLocalLoop();
                nixLocalLoop.setConnection(oldnixConnection.getId());
                nixLocalLoop.setOffice(office.getId());
                nixLocalLoop.setApplicationLocalLoop(nixApplicationLocalLoop.getId());
                globalService.save(nixLocalLoop);


                newLoopPopIds.add(nixApplicationLocalLoop.getPopId());
            }

            //keep other old loop

            List<NIXApplicationLocalLoop> oldOfficeLoops = globalService.getAllObjectListByCondition(NIXApplicationLocalLoop.class,
                    new NIXApplicationLocalLoopConditionBuilder()
                            .Where()
                            .officeIdEquals(nixOldOffices.get(0).getApplication_offfice())
                            .getCondition()
            );


            List<NIXApplicationLocalLoop> oldLoopNotChanged = oldOfficeLoops
                    .stream()
                    .filter(
                            t -> !newLoopPopIds.contains(t.getPopId())
                    ).collect(Collectors.toList());


            for (NIXApplicationLocalLoop nixApplicationLocalLoop : oldLoopNotChanged) {

                nixApplicationLocalLoop.setOfficeId(newOffice.getId());
                globalService.save(nixApplicationLocalLoop);


                NIXLocalLoop nixLocalLoop=new NIXLocalLoop();
                nixLocalLoop.setConnection(oldnixConnection.getId());
                nixLocalLoop.setApplicationLocalLoop(nixApplicationLocalLoop.getId());
                nixLocalLoop.setOffice(office.getId());
                globalService.save(nixLocalLoop);



            }

            nixApplication.setIsServiceStarted(1);
            nixApplicationService.updateApplicaton(nixApplication);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getPortType(NIXApplication nixApplication) throws Exception {
        if (nixApplication.getType() == NIXConstants.NIX_UPGRADE_APPLICATION) {
            NIXUpgradeApplication nixUpgradeApplication = ServiceDAOFactory.getService(
                    NIXUpgradeApplicationService.class).getApplicationByParent(nixApplication.getId());
            return nixUpgradeApplication.getNewPortType();
        } else {
            NIXDowngradeApplication nixDowngradeApplication = ServiceDAOFactory.getService(
                    NIXDowngradeApplicationService.class).getApplicationByParent(nixApplication.getId());
            return nixDowngradeApplication.getNewPortType();
        }
    }

    private KeyValuePair<Long, Long> getPrevPortAndOffice(NIXApplication nixApplication) throws Exception {
        KeyValuePair<Long, Long> keyValuePair = new KeyValuePair<>();

        if (nixApplication.getType() == NIXConstants.NIX_UPGRADE_APPLICATION) {
            NIXUpgradeApplication nixUpgradeApplication = ServiceDAOFactory.getService(
                    NIXUpgradeApplicationService.class).getApplicationByParent(nixApplication.getId());
            keyValuePair.key = nixUpgradeApplication.getOldPortId();
            keyValuePair.value = nixUpgradeApplication.getOffice();
            return keyValuePair;
        } else {
            NIXDowngradeApplication nixDowngradeApplication = ServiceDAOFactory.getService(
                    NIXDowngradeApplicationService.class).getApplicationByParent(nixApplication.getId());
            keyValuePair.key = nixDowngradeApplication.getOldPortId();
            keyValuePair.value = nixDowngradeApplication.getOffice();
            return keyValuePair;
        }
    }

    @Transactional
    public void newConnectionInsert(JsonObject jsonObject, long appID) {
        JsonElement connectionName = jsonObject.get("connectionName");
        JsonElement jsonElement2 = jsonObject.get("application");


        JsonObject innerapp = jsonElement2.getAsJsonObject();
        NIXConnection nixConnection = new NIXConnection();
        try {

            NIXApplication nixNewConnectionApplication = nixApplicationService.getApplicationById(appID);
            nixConnection = connectionInstanceCreate(nixNewConnectionApplication, connectionName.getAsString());
            nixConnectionService.insertConnection(nixConnection);
            if (nixNewConnectionApplication.getType() == NIXConstants.NEW_CONNECTION_APPLICATION) {
                nixConnection.setConnectionId(nixConnection.getId());
                nixConnection.setApplication(nixNewConnectionApplication.getId());
                nixConnection.setZone(nixNewConnectionApplication.getZone());
                nixConnectionService.updateConnection(nixConnection);
            }
            nixNewConnectionApplication.setConnection(nixConnection.getId());
            nixNewConnectionApplication.setIsServiceStarted(1);
            nixApplicationService.updateApplicaton(nixNewConnectionApplication);
            List<NIXApplicationOffice> nixApplicationOffices = nixApplicationOfficeService.getOfficesByApplicationId(nixNewConnectionApplication.getId());

            for (NIXApplicationOffice nixApplicationOffice : nixApplicationOffices) {
                List<NIXApplicationLocalLoop> nixLocalLoops = nixApplicationOffice.getLoops();
                NIXOffice nixOffice = new NIXOffice();
                nixOffice.setAppication(nixNewConnectionApplication.getId());
                nixOffice.setConnection(nixConnection.getId());
                nixOffice.setName(nixApplicationOffice.getName());
                nixOffice.setApplication_offfice(nixApplicationOffice.getId());
                nixOfficeService.insertOffice(nixOffice);
                //insert new local loop
                for (NIXApplicationLocalLoop nixApplicationLocalLoop : nixLocalLoops) {
                    NIXLocalLoop nixLocalLoop = new NIXLocalLoop();
                    nixLocalLoop.setApplicationLocalLoop(nixApplicationLocalLoop.getId());
                    nixLocalLoop.setConnection(nixConnection.getId());
                    nixLocalLoop.setOffice(nixOffice.getId());
                    nixLocalLoopService.insertLocalLoop(nixLocalLoop);
                    ServiceDAOFactory.getService(InventoryAllocationHistoryService.class).allocateInventoryItem(
                            nixApplicationLocalLoop.getPortId(), 9, nixNewConnectionApplication.getClient());
                }
            }

            List<NIXIPBlockForConnection> ipList = new ArrayList<>();
            //todo : connection Id fetch
            JsonArray ipJsonArray = jsonObject.get("ip").getAsJsonArray();
            for (JsonElement element : ipJsonArray) {
                JsonObject object = element.getAsJsonObject();
                if (object == null) {
                    throw new RequestFailureException("Invalid IP Information.");
                }
                String fromIP = object.get("fromIP").getAsString();
                if (fromIP == null) throw new RequestFailureException("From IP not valid");
                String toIP = object.get("toIP").getAsString();
                if (toIP == null) throw new RequestFailureException("To IP not valid");
                Long regionId = object.get("regionId") == null ? 0L : object.get("regionId").getAsLong();
                if (regionId == 0L) throw new RequestFailureException("From IP not valid");
                if (object.get("version") == null) throw new RequestFailureException("Version is not valid.");
                IPConstants.Version version = IPConstants.Version.valueOf(object.get("version").getAsString());
                if (object.get("type") == null) throw new RequestFailureException("Type is not valid");
                IPConstants.Type type = IPConstants.Type.valueOf(object.get("type").getAsString());
                NIXIPBlockForConnection block = NIXIPBlockForConnection.builder()
                        .fromIP(fromIP)
                        .toIP(toIP)
                        .version(version)
                        .type(type)
                        .purpose(IPConstants.Purpose.NIX_CONNECTION)
                        .realIP(true)
                        .parentIP(null)
                        .regionId(regionId)
                        .subRegionId(0L)
                        .usageType(LLIConnectionConstants.IPUsageType.MANDATORY)
                        .connectionId(nixConnection.getId())
                        .build();
                ipList.add(block);
            }
            ipService.allocateNIXIPAddress(ipList);
            int i = 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RequestFailureException("Process Not Competed ");

        }
    }

    private NIXApplication newConnectionCreator(JsonObject jsonObject) {
        JsonObject client = (JsonObject) jsonObject.get("client");

        JsonObject appType = (JsonObject) jsonObject.get("applicationType");
        long appTypeID = appType.get("ID").getAsLong();

        NIXApplication nixNewConnectionApplication = new NIXApplication();
        nixNewConnectionApplication.setId(jsonObject.get("applicationID").getAsLong());
        nixNewConnectionApplication.setClient(client.get("ID").getAsLong());
        nixNewConnectionApplication.setType((int) appTypeID);
        nixNewConnectionApplication.setDemandNote(jsonObject.get("demandNoteID") != null ? jsonObject.get("demandNoteID").getAsLong() : 0);
        nixNewConnectionApplication.setSuggestedDate(jsonObject.get("suggestedDate").getAsLong());
        nixNewConnectionApplication.setZone(jsonObject.get("zone") != null ? jsonObject.get("zone").getAsJsonObject().get("id").getAsInt() : 0);
        return nixNewConnectionApplication;

    }

    private NIXConnection connectionInstanceCreate(NIXApplication nixNewConnectionApplication, String connectionName)
            throws Exception {
        NIXConnection nixConnectionInstance = new NIXConnection();
        nixConnectionInstance.setClient(nixNewConnectionApplication.getClient());
        nixConnectionInstance.setActiveFrom(System.currentTimeMillis());
        nixConnectionInstance.setActiveTo(Long.MAX_VALUE);
        nixConnectionInstance.setValidFrom(System.currentTimeMillis());
        nixConnectionInstance.setValidTo(Long.MAX_VALUE);// problem
        nixConnectionInstance.setStatus(1);
        nixConnectionInstance.setStartDate(System.currentTimeMillis());
        nixConnectionInstance.setIncidentId(nixNewConnectionApplication.getType());
        nixConnectionInstance.setName(connectionName);
        return nixConnectionInstance;
    }

}
