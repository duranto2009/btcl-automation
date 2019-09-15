package lli.Application.FlowConnectionManager;

import annotation.Transactional;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import common.ModuleConstants;
import common.RequestFailureException;
import common.bill.BillDTO;
import common.bill.BillService;
import global.GlobalService;
import ip.IPBlockForConnection;
import ip.IPConstants;
import ip.IPService;
import ip.ipVsLLIConnection.IPvsConnection;
import ip.ipVsLLIConnection.IPvsConnectionService;
import lli.Application.LLIApplication;
import lli.Application.LLIApplicationService;
import lli.Application.LocalLoop.LocalLoop;
import lli.Application.LocalLoop.LocalLoopConditionBuilder;
import lli.Application.LocalLoop.LocalLoopDeserializer;
import lli.Application.LocalLoop.LocalLoopService;
import lli.Application.NewLocalLoop.NewLocalLoop;
import lli.Application.NewLocalLoop.NewLocalLoopConditionBuilder;
import lli.Application.NewLocalLoop.NewLocalLoopDeserializer;
import lli.Application.Office.Office;
import lli.Application.Office.OfficeService;
import lli.Application.newOffice.NewOffice;
import lli.Application.newOffice.NewOfficeService;
import lli.connection.LLIConnectionConstants;
import login.LoginDTO;
import notification.NotificationService;
import util.ServiceDAOFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class LLIApplicationFlowConnectionManagerService {

    OfficeService officeService = ServiceDAOFactory.getService(OfficeService.class);
    LLIFlowConnectionService lliFlowConnectionService = ServiceDAOFactory.getService(LLIFlowConnectionService.class);
    LLIApplicationService lliApplicationService = ServiceDAOFactory.getService(LLIApplicationService.class);
    IPService ipService=ServiceDAOFactory.getService(IPService.class);
    LocalLoopService localLoopService=ServiceDAOFactory.getService(LocalLoopService.class);
    BillService billService=ServiceDAOFactory.getService(BillService.class);
    NotificationService notificationService=ServiceDAOFactory.getService(NotificationService.class);
    GlobalService globalService=ServiceDAOFactory.getService(GlobalService.class);

    @Transactional
    public void newConnectionInsertManager(JsonElement jsonElement,LoginDTO loginDTO) throws Exception {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();
        JsonObject app = jsonElement.getAsJsonObject();
        JsonElement jsonElement2 = app.get("application");
        JsonObject ineerapp = jsonElement2.getAsJsonObject();
        JsonObject appType = (JsonObject) ineerapp.get("applicationType");
        long appTypeID = appType.get("ID")!=null?appType.get("ID").getAsLong():0;
        List<LocalLoop> localLoops = new LocalLoopDeserializer().deserialize_custom(jsonElement);

        if(appTypeID!=LLIConnectionConstants.UPGRADE_BANDWIDTH) {
            for (LocalLoop localLoop : localLoops) {
                localLoopService.updateApplicaton(localLoop);
            }
        }


        connectionCreatorOrUpdatorManager(jsonObject, appTypeID,loginDTO);
        lliApplicationService.updateApplicatonState(appID, state);

    }

    @Transactional
    public void connectionCreatorOrUpdatorManager(JsonElement jsonElement, long applicationType, LoginDTO loginDTO)throws Exception {

        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long appID=jsonObject.get("applicationID").getAsLong();
        if(appID>0) {


            notificationService.markNotificationAsActionTaken(
                    ModuleConstants.Module_ID_LLI,
                    appID,
                    loginDTO.getRoleID(),
                    false,
                    loginDTO.getUserID()

            );
        }
        if (applicationType == LLIConnectionConstants.NEW_CONNECTION) {
            newConnectionInsert(jsonObject);
        } else if (applicationType == LLIConnectionConstants.UPGRADE_BANDWIDTH) {
            connectionReviseBW(jsonElement);
        }else if(applicationType==LLIConnectionConstants.DOWNGRADE_BANDWIDTH){
            connectionReviseBW(jsonElement);

        }else if(applicationType==LLIConnectionConstants.TEMPORARY_UPGRADE_BANDWIDTH){

        }else if(applicationType==LLIConnectionConstants.CLOSE_CONNECTION){
            connectionClose(jsonObject);

        }else if(applicationType==LLIConnectionConstants.SHIFT_BANDWIDTH){
            shiftBW(appID);

        }else if(applicationType==LLIConnectionConstants.SHIFT_BANDWIDTH_NEW_CONNECTION){
            newConnectionInsert(jsonObject);
        }
        else if(applicationType == LLIConnectionConstants.ADDITIONAL_IP){
            newIpForConnection(jsonObject,appID);
        }

        else if(applicationType == LLIConnectionConstants.ADDITIONAL_LOCAL_LOOP||
                applicationType == LLIConnectionConstants.ADDITIONAL_PORT){
            localLoopOrPortForConnection(jsonObject,appID);
        }
    }

    private void localLoopOrPortForConnection(JsonObject jsonObject, long appID)throws Exception {

        JsonElement jsonElement2 = jsonObject.get("application");
        if(jsonElement2==null){
            throw new RequestFailureException("It is not a valid JSON");
        }
        int isReuse = jsonElement2.getAsJsonObject().get("loopType")!=null?jsonElement2.getAsJsonObject().get("loopType").getAsInt():-1;
        LLIApplication lliReviseConnectionApplication = lliApplicationService.getFlowLLIApplicationByApplicationID(appID);
        if (lliReviseConnectionApplication.getConnectionId() > 0) {
            LLIConnection lliConnection = new LLIConnection();
            long oldConnection =-1;
            //todo:change logic
            try {
                lliConnection = lliFlowConnectionService.getConnectionByID(lliReviseConnectionApplication.getConnectionId());
                oldConnection=lliConnection.getHistoryID();
                lliConnection.setActiveTo(System.currentTimeMillis());
                lliFlowConnectionService.updateConnection(lliConnection);
                lliConnection.setActiveFrom(System.currentTimeMillis());
                lliConnection.setValidFrom(System.currentTimeMillis());
                lliConnection.setActiveTo(Long.MAX_VALUE);
                lliConnection.setValidTo(Long.MAX_VALUE);
                if(lliReviseConnectionApplication.getApplicationType()==LLIConnectionConstants.ADDITIONAL_LOCAL_LOOP){
                    lliConnection.setIncident(LLIConnectionConstants.ADDITIONAL_LOCAL_LOOP);
                }else {
                    lliConnection.setIncident(LLIConnectionConstants.ADDITIONAL_PORT);
                }
                lliFlowConnectionService.insertConnection(lliConnection,lliReviseConnectionApplication.getApplicationID());

//                officeService.insertSnapshotOfficeNew(oldConnection,lliConnection.getHistoryID(),lliReviseConnectionApplication.getApplicationID());
//
                List<NewLocalLoop> localLoops = new NewLocalLoopDeserializer().deserialize_custom(jsonObject);


                for (NewLocalLoop newLocalLoop:localLoops
                     ) {
                    globalService.update(newLocalLoop);

                }


                List<Office> oldOffices=officeService.getOldOfficeByCON(oldConnection);
                List<NewOffice> newOffices = ServiceDAOFactory.getService(NewOfficeService.class)
                        .getOffice(lliReviseConnectionApplication.getApplicationID());


                List<Long>oldOffceIds=new ArrayList<>();


                for(NewOffice newOffice:newOffices) {

                    List<NewLocalLoop> newLocalLoopList=globalService.getAllObjectListByCondition(NewLocalLoop.class,
                            new NewLocalLoopConditionBuilder()
                                    .Where()
                                    .officeIDEquals(newOffice.getId())
                                    .getCondition());

                    if(newOffice.getOld_office_id()>0){

                        Office office=ServiceDAOFactory.getService(OfficeService.class).getOfficeByHistoryId(newOffice.getOld_office_id());
                        List<LocalLoop>localLoopList=globalService.getAllObjectListByCondition(LocalLoop.class,
                                new LocalLoopConditionBuilder()
                                .Where()
                                .officeIDEquals(office.getId())
                                .getCondition()
                                );

                        List<Long>loopRemoveIds=new ArrayList<>();
                        for (NewLocalLoop newLocalLoop:newLocalLoopList
                             ) {

                            if(newLocalLoop.getOldLoopId()>0&&isReuse == LLIConnectionConstants.REPLACE_ADDITIONAL_LOOP ){

                                loopRemoveIds.add(newLocalLoop.getOldLoopId());
                            }

                        }
                        localLoopList=localLoopList
                                .stream()
                                .filter(t-> !loopRemoveIds.contains(t.getHistoryID()))
                                .collect(Collectors.toList());


                        office.setConnectionID(lliConnection.getHistoryID());
                        office.setApplicationId(lliReviseConnectionApplication.getApplicationID());
                        globalService.save(office);

                        for (NewLocalLoop newLocalLoop:newLocalLoopList
                             ) {
                            LocalLoop localLoop1 = new NewLocalLoopDeserializer().deserializeToLocalLoop(newLocalLoop, office.getId());
                            globalService.save(localLoop1);
                        }
                        for (LocalLoop localLoop:localLoopList
                        ) {
                            localLoop.setOfficeID(office.getId());
                            globalService.save(localLoop);
                        }

                        oldOffceIds.add(newOffice.getOld_office_id());
                        //modifiedOffice.add(office);

                    }else{

                        Office office=ServiceDAOFactory.getService(NewOfficeService.class).getOfficeByNewOffice(newOffice);
                        office.setConnectionID(lliConnection.getHistoryID());
                        office.setApplicationId(lliReviseConnectionApplication.getApplicationID());
                        globalService.save(office);

                        for (NewLocalLoop newLocalLoop:newLocalLoopList
                        ) {
                            LocalLoop localLoop1 = new NewLocalLoopDeserializer().deserializeToLocalLoop(newLocalLoop, office.getId());
                            globalService.save(localLoop1);
                        }

                    }

                }
                oldOffices =oldOffices
                        .stream()
                        .filter(t-> !oldOffceIds.contains(t.getId()))
                        .collect(Collectors.toList());

                for(Office oldOffice:oldOffices){
                    oldOffice.setConnectionID(lliConnection.getHistoryID());
                    oldOffice.setApplicationId(lliReviseConnectionApplication.getApplicationID());
                    List<LocalLoop>loopList = oldOffice.getLoops();
                    globalService.save(oldOffice);
                    for(LocalLoop localLoop:loopList){
                        localLoop.setOfficeID(oldOffice.getId());
                        globalService.save(localLoop);
                    }
                }
               /* for (NewLocalLoop localLoop : localLoops) {
                    NewOffice newOffice = ServiceDAOFactory.getService(NewOfficeService.class).getOneOfficeById(localLoop.getOfficeID());
                    long officeid=newOffice.getOld_office_id();
                    if(officeid==0) {
                        // TODO: new office mean new loop entry
                            Office office = ServiceDAOFactory.getService(NewOfficeService.class).getOfficeByNewOffice(newOffice);
                            office.setConnectionID(lliConnection.getHistoryID());
                            officeService.insertOffice(office);
                            officeid = office.getId();
                            LocalLoop localLoop1 = new NewLocalLoopDeserializer().deserializeToLocalLoop(localLoop, officeid);
                            localLoopService.insertApplication(localLoop1);

                    }
                    else{
                        // TODO: means old loop reuse of replace
                        if(isReuse==LLIConnectionConstants.ADD_NEW_ADDITIONAL_LOOP){
                            LocalLoop localLoop1 = new NewLocalLoopDeserializer().deserializeToLocalLoop(localLoop, officeid);
                            localLoopService.insertApplication(localLoop1);
                        }
                        else {
                            LocalLoop localLoopOld = localLoopService.getLocalLoopById(localLoop.getOldLoopId());
                            localLoopOld.setDeleted(true);
                            localLoopService.updateApplicaton(localLoopOld);
                            LocalLoop localLoop1 = new NewLocalLoopDeserializer().deserializeToLocalLoop(localLoop, localLoopOld.getOfficeID());
                            localLoopService.insertApplication(localLoop1);
                        }
                    }

                }*/
               // lliReviseConnectionApplication.setServiceStarted(true);
                //lliApplicationService.updateApplicaton(lliReviseConnectionApplication);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void newIpForConnection(JsonObject jsonObject,long appID)throws Exception {
        LLIApplication lliApplication=lliApplicationService.getFlowLLIApplicationByApplicationID(appID);
        LLIConnection lliConnection=lliFlowConnectionService.getConnectionByID(lliApplication.getConnectionId());
        List<IPvsConnection> ipListOld = new ArrayList<>();
        long oldConnection;
        try {
            oldConnection=lliConnection.getHistoryID();
            lliConnection.setActiveTo(System.currentTimeMillis());
            ipListOld = ServiceDAOFactory.getService(IPvsConnectionService.class).getIPVsConnectionByConnectionId(lliConnection.getID());
            lliFlowConnectionService.updateConnection(lliConnection);
            lliConnection.setActiveFrom(System.currentTimeMillis());
            lliConnection.setValidFrom(System.currentTimeMillis());
            lliConnection.setActiveTo(Long.MAX_VALUE);
            lliConnection.setValidTo(Long.MAX_VALUE);
            lliConnection.setIncident(LLIConnectionConstants.ADDITIONAL_IP);
            lliFlowConnectionService.insertConnection(lliConnection,lliApplication.getApplicationID());
            officeService.insertSnapshotOfficeNew(oldConnection,lliConnection.getHistoryID(),lliApplication.getApplicationID());
            lliApplication.setServiceStarted(true);
            lliApplicationService.updateApplicaton(lliApplication);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ipListOld.forEach(s->{
            s.setConnectionId(lliConnection.getID());
            ServiceDAOFactory.getService(IPvsConnectionService.class).update(s);
        });
        List<IPBlockForConnection> ipList = new ArrayList<>();
        JsonArray ipJsonArray = jsonObject.get("ip").getAsJsonArray();
        for(JsonElement element: ipJsonArray){
            JsonObject object = element.getAsJsonObject();
            if(object==null){
                throw new RequestFailureException("Invalid IP Information.");
            }
            String fromIP = object.get("fromIP").getAsString();if(fromIP==null)throw new RequestFailureException("From IP not valid");
            String toIP = object.get("toIP").getAsString();if(toIP==null)throw new RequestFailureException("To IP not valid");
            Long regionId = object.get("regionId")==null?0L:object.get("regionId").getAsLong();if(regionId==0L)throw new RequestFailureException("From IP not valid");
            if(object.get("version")==null)throw new RequestFailureException("Version is not valid.");
            IPConstants.Version version = IPConstants.Version.valueOf(object.get("version").getAsString());
            if( object.get("type")==null)throw new RequestFailureException("Type is not valid");
            IPConstants.Type  type = IPConstants.Type.valueOf( object.get("type").getAsString());
            IPBlockForConnection block = IPBlockForConnection.builder()
                    .fromIP(fromIP)
                    .toIP(toIP)
                    .version(version)
                    .type(type)
                    .purpose(IPConstants.Purpose.LLI_CONNECTION)
                    .realIP(true)
                    .parentIP(null)
                    .regionId(regionId)
                    .subRegionId(0L)
                    .usageType(LLIConnectionConstants.IPUsageType.MANDATORY)
                    .connectionId(lliConnection.getID())
                    .build();
            ipList.add(block);
        }
        ipService.allocateIPAddress(ipList);
    }

    private void destinationBWShift(LLIConnection destination,LLIApplication lliApplication) throws Exception {
        LLIConnection oldDestination=destination;
        oldDestination.setActiveTo(System.currentTimeMillis());
        oldDestination.setStatus(LLIConnectionConstants.STATUS_INACTIVE);
        lliFlowConnectionService.updateConnection(oldDestination);

        destination.setActiveFrom(System.currentTimeMillis());
        destination.setValidFrom(System.currentTimeMillis());
        destination.setBandwidth(destination.getBandwidth()+lliApplication.getBandwidth());
        destination.setActiveTo(Long.MAX_VALUE);
        destination.setValidTo(Long.MAX_VALUE);
        destination.setStatus(LLIConnectionConstants.STATUS_ACTIVE);
        destination.setIncident(LLIConnectionConstants.SHIFT_BANDWIDTH);
        lliFlowConnectionService.insertConnection(destination,lliApplication.getApplicationID());
        officeService.insertSnapshotOffice(oldDestination,destination.getHistoryID(),lliApplication.getApplicationID());

    }

    private void sourceBWShift(LLIConnection source,LLIApplication lliApplication) throws Exception {
        long sourceHistoryId=source.getHistoryID();
        LLIConnection oldsource=source;
        oldsource.setActiveTo(System.currentTimeMillis());
        oldsource.setStatus(LLIConnectionConstants.STATUS_INACTIVE);
        lliFlowConnectionService.updateConnection(oldsource);

        source.setActiveFrom(System.currentTimeMillis());
        source.setValidFrom(System.currentTimeMillis());
        source.setBandwidth(source.getBandwidth()-lliApplication.getBandwidth());
        source.setActiveTo(Long.MAX_VALUE);
        source.setValidTo(Long.MAX_VALUE);
        source.setStatus(LLIConnectionConstants.STATUS_ACTIVE);
        source.setIncident(LLIConnectionConstants.SHIFT_BANDWIDTH);
        lliFlowConnectionService.insertConnection(source,lliApplication.getApplicationID());
        officeService.insertSnapshotOffice(oldsource,source.getHistoryID(),lliApplication.getApplicationID());
    }

    private void shiftBW(long appID){
        try {
            LLIApplication lliApplication=lliApplicationService.getFlowLLIApplicationByApplicationID(appID);
            LLIConnection source=lliFlowConnectionService.getConnectionByID(lliApplication.getSourceConnectionID());
            LLIConnection destination=lliFlowConnectionService.getConnectionByID(lliApplication.getConnectionId());
            if((source.getBandwidth()-lliApplication.getBandwidth())<0){
                throw new RequestFailureException(" Source Connection Bandwidth can not be Negetive");
            }else if((source.getBandwidth()-lliApplication.getBandwidth())==0){
                lliFlowConnectionService.closeConnectionByConnectionID(source.getID(),System.currentTimeMillis(),appID);
                destinationBWShift(destination,lliApplication);
            }else{
                sourceBWShift(source,lliApplication);
                destinationBWShift(destination,lliApplication);
            }
            lliApplication.setServiceStarted(true);
            lliApplicationService.updateApplicaton(lliApplication);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void connectionClose(JsonObject jsonObject) {
        //TODO IP Need to be freed!!! -> Bony Vai
        JsonElement jsonElement2 = jsonObject.get("application");
        JsonObject innerapp = jsonElement2.getAsJsonObject();
        long appID=jsonElement2.getAsJsonObject().get("applicationID").getAsLong();
        LLIApplication lliReviseConnectionApplication = newConnectionCreator(innerapp);
        if (lliReviseConnectionApplication.getConnectionId() > 0) {
            LLIConnection lliConnection = new LLIConnection();
            try {
                lliFlowConnectionService.closeConnectionByConnectionID(lliReviseConnectionApplication.getConnectionId(),System.currentTimeMillis(),appID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    private void newConnectionInsert(JsonObject jsonObject) {
        JsonElement connectionName = jsonObject.get("connectionName");
        JsonElement jsonElement2 = jsonObject.get("application");
        JsonObject innerapp = jsonElement2.getAsJsonObject();
        if(jsonObject.has("connectionName")) {
            if (jsonObject.get("connectionName").getAsString().equals("")) {
                throw new RequestFailureException("Connection Name is Mandatory :");
            }
        }
//        JsonElement ip=jsonObject.get("ip");
//        JsonObject ipObject=ip.getAsJsonObject();
        LLIConnection lliConnection = new LLIConnection();
        try {

            LLIApplication lliNewConnectionApplication = newConnectionCreator(innerapp);

            if(lliNewConnectionApplication.getApplicationType()==LLIConnectionConstants.SHIFT_BANDWIDTH_NEW_CONNECTION){

                LLIApplication lliApplication=lliApplicationService.getFlowLLIApplicationByApplicationID(lliNewConnectionApplication.getApplicationID());
                LLIConnection source=lliFlowConnectionService.getConnectionByID(lliApplication.getSourceConnectionID());

                if((source.getBandwidth()-lliApplication.getBandwidth())<0){
                    throw new RequestFailureException(" Source Connection Bandwidth can not be Negative");
                }else if((source.getBandwidth()-lliApplication.getBandwidth())==0){
                    lliFlowConnectionService.closeConnectionByConnectionID(source.getID(),System.currentTimeMillis(),lliApplication.getApplicationID());
                }else{
                    sourceBWShift(source,lliApplication);
                }
            }

            lliConnection = connectionInstanceCreate(lliNewConnectionApplication, connectionName.getAsString());
            lliFlowConnectionService.insertNewLLIConnectionFLOW(lliConnection, lliNewConnectionApplication.getApplicationID());

            List<IPBlockForConnection> ipList = new ArrayList<>();

            //todo : connection Id fetch
            JsonArray ipJsonArray = jsonObject.get("ip").getAsJsonArray();
            for(JsonElement element: ipJsonArray){
                JsonObject object = element.getAsJsonObject();
                if(object==null){
                    throw new RequestFailureException("Invalid IP Information.");
                }
                String fromIP = object.get("fromIP").getAsString();if(fromIP==null)throw new RequestFailureException("From IP not valid");
                String toIP = object.get("toIP").getAsString();if(toIP==null)throw new RequestFailureException("To IP not valid");
                Long regionId = object.get("regionId")==null?0L:object.get("regionId").getAsLong();if(regionId==0L)throw new RequestFailureException("From IP not valid");
                if(object.get("version")==null)throw new RequestFailureException("Version is not valid.");
                IPConstants.Version version = IPConstants.Version.valueOf(object.get("version").getAsString());
                if( object.get("type")==null)throw new RequestFailureException("Type is not valid");
                IPConstants.Type  type = IPConstants.Type.valueOf( object.get("type").getAsString());


                IPBlockForConnection block = IPBlockForConnection.builder()
                        .fromIP(fromIP)
                        .toIP(toIP)
                        .version(version)
                        .type(type)
                        .purpose(IPConstants.Purpose.LLI_CONNECTION)
                        .realIP(true)
                        .parentIP(null)
                        .regionId(regionId)
                        .subRegionId(0L)
                        .usageType(LLIConnectionConstants.IPUsageType.MANDATORY)
                        .connectionId(lliConnection.getID())
                        .build();

                ipList.add(block);
            }
            ipService.allocateIPAddress(ipList);
            int i=0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RequestFailureException("Process Not Competed ");
        }
    }

    @Transactional
    private void connectionReviseBW(JsonElement jsonElement) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonElement jsonElement2 = jsonObject.get("application");
        JsonObject innerapp = jsonElement2.getAsJsonObject();
        LLIApplication lliReviseConnectionApplication = newConnectionCreator(innerapp);
        List<LocalLoop> localLoops = new LocalLoopDeserializer().deserialize_custom(jsonElement);
        if (lliReviseConnectionApplication.getConnectionId() > 0) {
            LLIConnection lliConnection = new LLIConnection();
            LLIConnection oldConnection=new LLIConnection();

            //todo:change logic
            try {
                lliConnection = lliFlowConnectionService.getConnectionByID(lliReviseConnectionApplication.getConnectionId());

                oldConnection=lliConnection;
                oldConnection.setActiveTo(System.currentTimeMillis());

                lliFlowConnectionService.updateConnection(oldConnection);
                lliConnection.setActiveFrom(System.currentTimeMillis());
                lliConnection.setValidFrom(System.currentTimeMillis());
                lliConnection.setActiveTo(Long.MAX_VALUE);
                lliConnection.setValidTo(Long.MAX_VALUE);
                if(lliReviseConnectionApplication.getApplicationType()==LLIConnectionConstants.UPGRADE_BANDWIDTH){
                    double x=lliConnection.getBandwidth() + lliReviseConnectionApplication.getBandwidth();
                    lliConnection.setBandwidth(x);
                    lliConnection.setIncident(LLIConnectionConstants.UPGRADE_BANDWIDTH);
                }else if(lliReviseConnectionApplication.getApplicationType()==LLIConnectionConstants.DOWNGRADE_BANDWIDTH){
                    lliConnection.setBandwidth(lliConnection.getBandwidth() - lliReviseConnectionApplication.getBandwidth());
                    lliConnection.setIncident(LLIConnectionConstants.DOWNGRADE_BANDWIDTH);
                }

                lliFlowConnectionService.insertConnection(lliConnection,lliReviseConnectionApplication.getApplicationID());
//                lliReviseConnectionApplication.setConnectionId((int) lliConnection.getHistoryID());
                if(lliReviseConnectionApplication.getApplicationType()==LLIConnectionConstants.UPGRADE_BANDWIDTH){

                    officeService.insertSnapshotOffice(localLoops ,oldConnection,lliConnection.getHistoryID(),lliReviseConnectionApplication.getApplicationID());
                }else {
                    officeService.insertSnapshotOffice(localLoops,oldConnection,lliConnection.getHistoryID(),lliReviseConnectionApplication.getApplicationID());

                }

                lliReviseConnectionApplication.setServiceStarted(true);
                lliApplicationService.updateApplicaton(lliReviseConnectionApplication);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private LLIApplication newConnectionCreator(JsonObject jsonObject) {
        JsonObject client = (JsonObject) jsonObject.get("client");

        JsonObject type = (JsonObject) jsonObject.get("connectionType");
        long conTypeID=type.get("ID").getAsLong();

        JsonObject appType = (JsonObject) jsonObject.get("applicationType");
        long appTypeID = appType.get("ID").getAsLong();

        LLIApplication lliNewConnectionApplication = new LLIApplication();
        lliNewConnectionApplication.setApplicationID(jsonObject.get("applicationID").getAsLong());
        lliNewConnectionApplication.setClientID(client.get("ID").getAsLong());
        lliNewConnectionApplication.setApplicationType((int) appTypeID);
        lliNewConnectionApplication.setDemandNoteID(jsonObject.get("demandNoteID")!=null?jsonObject.get("demandNoteID").getAsLong():0);
        lliNewConnectionApplication.setConnectionType(jsonObject.get("connectionType")!=null?jsonObject.get("connectionType").getAsJsonObject().get("ID").getAsInt():0);
        if (conTypeID == LLIConnectionConstants.CONNECTION_TYPE_REGULAR
                ||conTypeID==LLIConnectionConstants.CONNECTION_TYPE_CACHE
                ||conTypeID==LLIConnectionConstants.CONNECTION_TYPE_REGULAR_LONG
        ) {
            lliNewConnectionApplication.setDuration(0);


        } else if(conTypeID==LLIConnectionConstants.CONNECTION_TYPE_TEMPORARY) {

            long durationInMillis= TimeUnit.DAYS.toMillis( jsonObject.get("duration").getAsLong());

            lliNewConnectionApplication.setDuration(durationInMillis);

        }
        lliNewConnectionApplication.setConnectionId(jsonObject.get("connectionID")!=null?(int) jsonObject.get("connectionID").getAsLong():0);
        lliNewConnectionApplication.setSuggestedDate(jsonObject.get("suggestedDate").getAsLong());
        lliNewConnectionApplication.setSubmissionDate(jsonObject.get("submissionDate") != null ? jsonObject.get("submissionDate").getAsLong() : System.currentTimeMillis());
        lliNewConnectionApplication.setBandwidth(jsonObject.get("bandwidth").getAsLong());
        if( jsonObject.get("zone")!=null && jsonObject.get("zone").getAsJsonObject().has("id")){
            lliNewConnectionApplication.setZoneId(jsonObject.get("zone").getAsJsonObject().get("id").getAsInt());
        }else {

            lliNewConnectionApplication.setZoneId(0);
        }
        return lliNewConnectionApplication;

    }

    private LLIConnection connectionInstanceCreate(LLIApplication lliNewConnectionApplication, String connectionName)
            throws Exception {
        LLIConnection lliConnectionInstance = new LLIConnection();
        lliConnectionInstance.setClientID(lliNewConnectionApplication.getClientID());
        lliConnectionInstance.setConnectionType(
                lliNewConnectionApplication.getConnectionType()==LLIConnectionConstants.CONNECTION_TYPE_REGULAR_LONG
                        ?LLIConnectionConstants.CONNECTION_TYPE_REGULAR:lliNewConnectionApplication.getConnectionType());
        lliConnectionInstance.setActiveFrom(System.currentTimeMillis());
        if(lliNewConnectionApplication.getDuration()>0){

            lliConnectionInstance.setActiveTo(System.currentTimeMillis() + lliNewConnectionApplication.getDuration());// problem
        }else{
            lliConnectionInstance.setActiveTo(Long.MAX_VALUE);
        }
        lliConnectionInstance.setValidFrom(System.currentTimeMillis());
//        lliConnectionInstance.setValidTo(System.currentTimeMillis() + lliNewConnectionApplication.getDuration());// problem
        lliConnectionInstance.setValidTo(Long.MAX_VALUE);// problem
        lliConnectionInstance.setBandwidth(lliNewConnectionApplication.getBandwidth());
        lliConnectionInstance.setStatus(1);
        lliConnectionInstance.setStartDate(System.currentTimeMillis());
        lliConnectionInstance.setIncident(lliNewConnectionApplication.getApplicationType());
        lliConnectionInstance.setName(connectionName);

        double discountPercentage;
        if(lliNewConnectionApplication.getDemandNoteID()>0){
            BillDTO demandNote = billService.getBillByBillID(lliNewConnectionApplication.getDemandNoteID());
            discountPercentage= demandNote.getDiscountPercentage();
        }else{
            discountPercentage=0;
        }


        //set demand note discount rate into connection
        lliConnectionInstance.setDiscountRate(discountPercentage);

        lliConnectionInstance.setZoneID(lliNewConnectionApplication.getZoneId());
        List<Office> offices = officeService.getOffice(lliNewConnectionApplication.getApplicationID());
        lliConnectionInstance.setOffices(offices);
        return lliConnectionInstance;
    }
}
