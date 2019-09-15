package coLocation.FlowConnectionManager;

import annotation.Transactional;
import coLocation.CoLocationConstants;
import coLocation.application.CoLocationApplicationDTO;
import coLocation.application.CoLocationApplicationService;
import coLocation.connection.CoLocationConnectionDTO;
import coLocation.connection.CoLocationConnectionService;
import coLocation.demandNote.CoLocationDemandNoteService;
import coLocation.inventory.CoLocationInventoryInUseService;
import coLocation.inventory.CoLocationInventoryService;
import coLocation.inventory.CoLocationInventoryTemplateService;
import com.google.gson.JsonObject;
import common.RequestFailureException;
import login.LoginDTO;
import util.*;

public class CoLocationApplicationFlowConnectionManagerService {


    CoLocationApplicationService coLocationApplicationService=ServiceDAOFactory.getService(CoLocationApplicationService.class);
    CoLocationConnectionService coLocationConnectionService=ServiceDAOFactory.getService(CoLocationConnectionService.class);
    CoLocationInventoryTemplateService coLocationInventoryTemplateService=ServiceDAOFactory.getService(CoLocationInventoryTemplateService.class);
    CoLocationInventoryService coLocationInventoryService=ServiceDAOFactory.getService(CoLocationInventoryService.class);
    CoLocationInventoryInUseService coLocationInventoryInUseService=ServiceDAOFactory.getService(CoLocationInventoryInUseService.class);
    CoLocationDemandNoteService coLocationDemandNoteService=ServiceDAOFactory.getService(CoLocationDemandNoteService.class);

    @Transactional
    public void connectionCreatorOrUpdatorManager(JsonObject jsonObject,LoginDTO loginDTO) throws Exception {

        long appID = jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0;
        int state = jsonObject.get("nextState").getAsInt();

        CoLocationApplicationDTO coLocationApplicationDTO=coLocationApplicationService.getColocationApplication(appID);
        String connectionName = coLocationApplicationDTO.getConnectionId()==0?jsonObject.get("application").getAsJsonObject().get("connectionName").getAsString():coLocationConnectionService.getColocationConnection(coLocationApplicationDTO.getConnectionId()).getName();
        if (coLocationApplicationDTO.getApplicationType() == CoLocationConstants.NEW_CONNECTION) {
//            newConnectionInsert(jsonObject);
            newConnectionInsert(coLocationApplicationDTO,connectionName,state,loginDTO);

        } else if(coLocationApplicationDTO.getApplicationType() == CoLocationConstants.TD){
            //logic
            //istd completed true korte hobe (x)
            //is Service started true korte hobe(x)
            //new connectino instance insert hobe incident td, ager ta deactivate(x)

            revise(coLocationApplicationDTO,connectionName,state,loginDTO);
            //
        }
        else if(coLocationApplicationDTO.getApplicationType() == CoLocationConstants.CLOSE){
            revise(coLocationApplicationDTO,connectionName,state,loginDTO);
        }
        else {
            KeyValuePair<CoLocationConnectionDTO, CoLocationConnectionDTO> pair =revise(coLocationApplicationDTO,connectionName,state,loginDTO);
            // dn adjustment logic
            coLocationDemandNoteService.saveDemandNoteAdjustment(pair.getKey(),pair.getValue());
        }


    }


    @Transactional(transactionType = TransactionType.INDIVIDUAL_TRANSACTION)
    public KeyValuePair<CoLocationConnectionDTO, CoLocationConnectionDTO> revise(CoLocationApplicationDTO coLocationApplicationDTO, String conName, int state, LoginDTO loginDTO) throws Exception {
        CoLocationConnectionDTO oldConnection=coLocationConnectionService.getColocationConnection(coLocationApplicationDTO.getConnectionId());
        oldConnection.setActiveTo(System.currentTimeMillis());
        oldConnection.setHistoryID(oldConnection.getHistoryID());
        oldConnection.setStatus(CoLocationConstants.STATUS_INACTIVE);
        long oldID=oldConnection.getHistoryID();
        //close logic
        // start: added by forhad
        if(coLocationApplicationDTO.getApplicationType()==CoLocationConstants.CLOSE){

            oldConnection.setStatus(CoLocationConstants.STATUS_CLOSED);

            oldConnection.setValidTo(System.currentTimeMillis());

            oldConnection.setIncident(CoLocationConstants.CLOSE);

        } // end: added by forhad


        coLocationConnectionService.updateConnection(oldConnection);
        coLocationInventoryService.deallocateInventory(oldConnection);
        CoLocationConnectionDTO coLocationConnectionDTO =createConnectionFromApplication(coLocationApplicationDTO,conName);
//        coLocationConnectionDTO.setHistoryID(oldID);
        if(coLocationApplicationDTO.getApplicationType()==CoLocationConstants.UPGRADE_CONNECTION)
        {
            coLocationConnectionDTO.setIncident(CoLocationConstants.UPGRADE_CONNECTION);
        }else if(coLocationApplicationDTO.getApplicationType()==CoLocationConstants.DOWNGRADE_CONNECTION){
            coLocationConnectionDTO.setIncident(CoLocationConstants.DOWNGRADE_CONNECTION);
        }else if(coLocationApplicationDTO.getApplicationType()==CoLocationConstants.TD){

            coLocationConnectionDTO.setStatus(CoLocationConstants.STATUS_TD); // deactivate the connection
            coLocationConnectionDTO.setIncident(CoLocationConstants.TD);
            coLocationConnectionDTO.setValidTo(System.currentTimeMillis());

        }
        else if(coLocationApplicationDTO.getApplicationType() == CoLocationConstants.RECONNECT_CONNECTION){
            coLocationConnectionDTO.setStatus(CoLocationConstants.STATUS_ACTIVE);
            coLocationConnectionDTO.setIncident(CoLocationConstants.RECONNECT_CONNECTION);
            coLocationConnectionDTO.setValidTo(Long.MAX_VALUE);

        }

        coLocationConnectionDTO.setHistoryID(oldID);

        // start: added by forhad
        if(coLocationApplicationDTO.getApplicationType()!=CoLocationConstants.CLOSE){
            inventoryAllocationManager(coLocationConnectionDTO);
            coLocationConnectionService.insertConnection(coLocationConnectionDTO);

            coLocationApplicationDTO.setConnectionId((int) coLocationConnectionDTO.getID());

        }else{
//            coLocationConnectionService.insertConnection(coLocationConnectionDTO);
        }
        // end: added by forhad

        coLocationApplicationDTO.setState(state);
        coLocationApplicationDTO.setServiceStarted(true);
        coLocationApplicationService.updateApplicaton(coLocationApplicationDTO,loginDTO);

//        // dn adjustment logic
//        if(
//                coLocationApplicationDTO.getApplicationType()!=CoLocationConstants.TD
//                        && coLocationApplicationDTO.getApplicationType()!=CoLocationConstants.CLOSE
//        )
//            coLocationDemandNoteService.saveDemandNoteAdjustment(coLocationConnectionDTO,oldConnection);



        return new KeyValuePair<>(coLocationConnectionDTO, oldConnection);


    }

    private void inventoryAllocationManager(CoLocationConnectionDTO coLocationConnectionDTO) throws Exception {
        if(coLocationConnectionDTO.isRackNeeded()){
            coLocationInventoryService.allocateInventory
                    (coLocationConnectionDTO,CoLocationConstants.INVENTORY_RACK,Double.parseDouble(coLocationInventoryTemplateService.getInventoryTemplateByID(coLocationConnectionDTO.getRackSpace()).getValue()));
        }
        if(coLocationConnectionDTO.isPowerNeeded()){
            coLocationInventoryService.allocateInventory
                    (coLocationConnectionDTO,CoLocationConstants.INVENTORY_POWER,coLocationConnectionDTO.getPowerAmount());
        }
        if(coLocationConnectionDTO.isFiberNeeded()){
            coLocationInventoryService.allocateInventory
                    (coLocationConnectionDTO,CoLocationConstants.INVENTORY_FIBER,coLocationConnectionDTO.getFiberCore());
        }
        //TODO is floor space needed
        if(coLocationConnectionDTO.isFloorSpaceNeeded()){
            coLocationInventoryService.allocateInventory
                    (coLocationConnectionDTO,CoLocationConstants.INVENTORY_CATEGORY_FLOOR_SPACE,coLocationConnectionDTO.getFloorSpaceAmount());
        }



    }

    private void newConnectionInsert(CoLocationApplicationDTO coLocationApplicationDTO,String connectionName,int state,LoginDTO loginDTO) throws Exception {
        CoLocationConnectionDTO coLocationConnectionDTO = null;
        try {

           coLocationConnectionDTO=createConnectionFromApplication(coLocationApplicationDTO,connectionName);
            coLocationConnectionService.insertConnection(coLocationConnectionDTO);
            inventoryAllocationManager(coLocationConnectionDTO);
            coLocationApplicationDTO.setConnectionId((int) coLocationConnectionDTO.getID());
            coLocationApplicationDTO.setState(state);
            coLocationApplicationDTO.setServiceStarted(true);
            coLocationApplicationService.updateApplicaton(coLocationApplicationDTO,loginDTO);


        } catch (Exception e) {
            e.printStackTrace();
            throw new RequestFailureException(e.getMessage());

        }


        // dn adjustment logic
        coLocationDemandNoteService.saveDemandNoteAdjustment(coLocationConnectionDTO,null);
    }


    private CoLocationConnectionDTO createConnectionFromApplication(CoLocationApplicationDTO coLocationApplicationDTO,String connectionName) throws Exception {
        long connectionID = DatabaseConnectionFactory.getCurrentDatabaseConnection()
                .getNextIDWithoutIncrementing(
                        ModifiedSqlGenerator.getTableName(CoLocationConnectionDTO.class));
        CoLocationConnectionDTO coLocationConnectionDTO = CoLocationConnectionDTO.builder()
                .ID(connectionID)
                .historyID(connectionID)
                .clientID(coLocationApplicationDTO.getClientID())
                .name(connectionName)
                .connectionType(coLocationApplicationDTO.getConnectionType())
                .rackNeeded(coLocationApplicationDTO.isRackNeeded())
                .rackSize(coLocationApplicationDTO.getRackTypeID())
                .rackSpace(coLocationApplicationDTO.getRackSpace())
                .powerNeeded(coLocationApplicationDTO.isPowerNeeded())
                .powerAmount(coLocationApplicationDTO.getPowerAmount())
                .powerType(coLocationApplicationDTO.getPowerType())
                .fiberNeeded(coLocationApplicationDTO.isFiberNeeded())
                .fiberType(coLocationApplicationDTO.getFiberType())
                .fiberCore(coLocationApplicationDTO.getFiberCore())

                .floorSpaceNeeded(coLocationApplicationDTO.isFloorSpaceNeeded())
                .floorSpaceType(coLocationApplicationDTO.getFloorSpaceType())
                .floorSpaceAmount(coLocationApplicationDTO.getFloorSpaceAmount())


                .activeFrom(System.currentTimeMillis())
                .activeTo(Long.MAX_VALUE)
                .validFrom(System.currentTimeMillis())
                .validTo(Long.MAX_VALUE)
                .startDate(System.currentTimeMillis())
                .status(CoLocationConstants.STATUS_ACTIVE)
                .incident(CoLocationConstants.NEW_CONNECTION)
                .discountRate(0)
                .build();
        return coLocationConnectionDTO;
    }

}
