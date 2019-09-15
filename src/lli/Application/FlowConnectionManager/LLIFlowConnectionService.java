package lli.Application.FlowConnectionManager;

import annotation.DAO;
import annotation.Transactional;
import common.ModuleConstants;
import common.RequestFailureException;
import inventory.InventoryAllocationHistory;
import inventory.InventoryAllocationHistoryService;
import ip.IPService;
import lli.Application.LLIApplication;
import lli.Application.LLIApplicationService;
import lli.Application.LocalLoop.LocalLoop;
import lli.Application.Office.Office;
import lli.Application.Office.OfficeService;
import lli.Application.ReviseClient.ReviseDTO;
import lli.Application.ReviseClient.ReviseService;
import lli.LLIConnectionInstance;
import lli.LLIOffice;
import lli.connection.LLIConnectionConstants;
import requestMapping.Service;
import util.DatabaseConnectionFactory;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;
import util.TransactionType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LLIFlowConnectionService {


    @Service
    InventoryAllocationHistoryService inventoryAllocationHistoryService;
    @DAO
    LLIFlowConnectionDAO lliFlowConnectionDAO;

    OfficeService officeService = ServiceDAOFactory.getService(OfficeService.class);

    IPService ipService=ServiceDAOFactory.getService(IPService.class);
    LLIApplicationService lliApplicationService = ServiceDAOFactory.getService(LLIApplicationService.class);
    ReviseService reviseService=ServiceDAOFactory.getService(ReviseService.class);


    @Transactional(transactionType=TransactionType.READONLY)
    public LLIConnection getConnectionByID(long conID) throws Exception {
        return lliFlowConnectionDAO.getConnectionByConnectionID(conID);
    }

    @Transactional(transactionType=TransactionType.READONLY)
    public List<LLIConnection> getConnectionByClient(long clientId) throws Exception {
        List<LLIConnection> lliConnections= lliFlowConnectionDAO.getConnectionByClient(clientId);
        return lliConnections;
    }

    @Transactional(transactionType=TransactionType.READONLY)
    public LLIConnection getLastDeactivatedConnectionByID(long conID) throws Exception {
        LLIConnection lliConnection= lliFlowConnectionDAO.getLastDeactivatedConnectionByConnectionID(conID);
        return lliConnection;
    }

    @Transactional(transactionType=TransactionType.READONLY)
    public LLIConnection getLastConnectionByID(long conID) throws Exception {
        LLIConnection lliConnection= lliFlowConnectionDAO.getConnectionByConnectionID(conID);


        return lliConnection;
    }

    @Transactional
    public long insertNewLLIConnectionFLOW(LLIConnection lliConnection, long appID) throws Exception {
        long connectionID = DatabaseConnectionFactory.getCurrentDatabaseConnection()
                .getNextIDWithoutIncrementing(
                        ModifiedSqlGenerator.getTableName(LLIConnection.class));

        lliConnection.setID(connectionID);
        lliConnection.setHistoryID(connectionID);
        lliConnection.setStartDate(System.currentTimeMillis());
        lliConnection.setStatus(LLIConnectionInstance.STATUS_ACTIVE);
        insertLLIConnectionSnapshotFlow(lliConnection, appID);

        //List<Long> vlanIDsToBeAllocated = getVlanIDListByConnectionInstance(connectionInstance);
        //allocateVlansByVlanIDListAndClientIDAndConnectionID(vlanIDsToBeAllocated,connectionInstance.getClientID(),connectionInstance.getID());

        return lliConnection.getID();
    }

    @Transactional
    private long insertLLIConnectionSnapshotFlow(LLIConnection lliConnection, long appID) throws Exception {
        lliFlowConnectionDAO.insertLLIConnectionFlow(lliConnection);
        LLIApplication lliApplication=lliApplicationService.getLLIApplicationByApplicationID(appID);

        if(lliApplication.getConnectionType() == LLIConnectionConstants.CONNECTION_TYPE_REGULAR_LONG){
            ReviseDTO reviseDTO = new ReviseDTO();
            reviseDTO.setSuggestedDate(System.currentTimeMillis());
            reviseDTO.setBandwidth(lliApplication.getBandwidth());
            reviseDTO.setClientID(lliApplication.getClientID());
            reviseService.completeNewLongTerm(reviseDTO);
        }


        lliApplication.setConnectionId((int) lliConnection.getID());
        lliApplication.setServiceStarted(true);
        lliApplicationService.updateApplicaton(lliApplication);
        List<Office> offices = officeService.getOffice(appID);
        for (Office office : offices) {


            if(office.getConnectionID()>0){

            }else{

                office.setConnectionID(lliConnection.getHistoryID());
            }
            for (LocalLoop localLoop: office.getLoops()
                 ) {
                long VlanID=localLoop.getVLANID();
                long portID = localLoop.getPortID();
                inventoryAllocationHistoryService.allocateInventoryItem(VlanID, ModuleConstants.Module_ID_LLI, lliApplication.getClientID());
                inventoryAllocationHistoryService.allocateInventoryItem(portID, ModuleConstants.Module_ID_LLI, lliApplication.getClientID());
                inventoryAllocationHistoryService.allocateInventoryItem(localLoop.getRouter_switchID(), ModuleConstants.Module_ID_LLI, lliApplication.getClientID());

            }

            officeService.updateOffice(office);

        }

        return lliConnection.getID();
    }

    @Transactional
    public void closeConnectionByConnectionID(long connectionID, long closingDate,long appID) throws Exception {
        LLIConnection lliConnectionInstance = getConnectionByID(connectionID);
        if (lliConnectionInstance == null) {
            throw new RequestFailureException("No connection found with connection ID " + connectionID);
        }
        if (lliConnectionInstance.getStatus() != LLIConnectionInstance.STATUS_ACTIVE
                && lliConnectionInstance.getStatus() != LLIConnectionInstance.STATUS_TD) {
            throw new RequestFailureException("The connection with connection ID " + connectionID + " is not in a status to be closed");
        }


        // todo:deallocate ip address
        //todo : free vlan
//        lliConnectionInstance.setActiveTo(closingDate);

        closeConnectionDeallocateInventoryAndIP(lliConnectionInstance);




        LLIApplication lliApplication=lliApplicationService.getLLIApplicationByApplicationID(appID);
        lliApplication.setServiceStarted(true);
        lliApplicationService.updateApplicaton(lliApplication);

    }






    public void closeConnectionDeallocateInventoryAndIP(LLIConnection lliConnectionInstance) throws Exception {




        List<Office> offices = officeService.getOfficeByCON(lliConnectionInstance.getID());
        for (Office office : offices) {

            for (LocalLoop localLoop: office.getLoops()
            ) {
                long VlanID=localLoop.getVLANID();
                long portID = localLoop.getPortID();
                List<InventoryAllocationHistory> vlanHistories=inventoryAllocationHistoryService.getUsageById(VlanID)
                        .stream()
                        .filter(t-> t.getClientId()==lliConnectionInstance.getClientID())
                        .collect(Collectors.toList());
                if(vlanHistories!=null&& vlanHistories.size()>0){
                    inventoryAllocationHistoryService.deallocationInventoryItem(VlanID, ModuleConstants.Module_ID_LLI, lliConnectionInstance.getClientID());

                }

                List<InventoryAllocationHistory> portHistories=inventoryAllocationHistoryService.getUsageById(portID)
                        .stream()
                        .filter(t-> t.getClientId()==lliConnectionInstance.getClientID())
                        .collect(Collectors.toList());;
                if(portHistories!=null&& portHistories.size()>0){
                    inventoryAllocationHistoryService.deallocationInventoryItem(portID, ModuleConstants.Module_ID_LLI, lliConnectionInstance.getClientID());

                }
                List<InventoryAllocationHistory> rsHistories=inventoryAllocationHistoryService.getUsageById(localLoop.getRouter_switchID())
                        .stream()
                        .filter(t-> t.getClientId()==lliConnectionInstance.getClientID())
                        .collect(Collectors.toList());

                if(rsHistories!=null&& rsHistories.size()>0){
                    inventoryAllocationHistoryService.deallocationInventoryItem(localLoop.getRouter_switchID(), ModuleConstants.Module_ID_LLI, lliConnectionInstance.getClientID());

                }

            }


        }


        ipService.deallocateIPsByConnectionId(lliConnectionInstance.getID());
        lliConnectionInstance.setActiveTo(System.currentTimeMillis());
        lliConnectionInstance.setStatus(LLIConnectionInstance.STATUS_CLOSED);
        lliConnectionInstance.setIncident(LLIConnectionConstants.CLOSE_CONNECTION);
        lliConnectionInstance.setOffices(new ArrayList<>());
        lliConnectionInstance.setBandwidth(0);
        updateConnection(lliConnectionInstance);


    }

    @Transactional
    public void updateConnection(LLIConnection lliConnection) throws Exception{
        lliFlowConnectionDAO.updateLLIConnection(lliConnection);
    }


    @Transactional
    public void insertConnection(LLIConnection lliConnection,long appID) throws Exception{
        lliFlowConnectionDAO.insertLLIConnection(lliConnection);
        LLIApplication lliApplication=lliApplicationService.getLLIApplicationByApplicationID(appID);
        lliApplication.setServiceStarted(true);
        lliApplicationService.updateApplicaton(lliApplication);
    }

    @Transactional
    public void insertOfficeAndLoopToOldTable(List<Office> officeList,long connectionID) throws Exception{
        List<LLIOffice> lliOffices=new ArrayList<>();
        for (Office office:officeList
             ) {






//            lliOffice.

        }
    }


    @Transactional
    public void insertConnectionForNewOwner(LLIConnection lliConnectionInstanceNew)throws Exception {
        lliFlowConnectionDAO.insertLLIConnection(lliConnectionInstanceNew);
    }
}
