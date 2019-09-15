package lli.Application.Office;

import annotation.DAO;
import annotation.Transactional;
import common.RequestFailureException;
import lli.Application.FlowConnectionManager.LLIConnection;
import lli.Application.FlowConnectionManager.LLIFlowConnectionService;
import lli.Application.LocalLoop.LocalLoop;
import lli.Application.LocalLoop.LocalLoopService;
import lli.LLIConnectionService;
import requestMapping.Service;
import util.TransactionType;

import java.util.List;
import java.util.stream.Collectors;

public class OfficeService {

    @DAO
    OfficeDAO officeDAO;

    @Service
    LLIConnectionService lliConnectionService;

    @Service
    LocalLoopService localLoopService;

    @Service
    LLIFlowConnectionService lliFlowConnectionService;

    @Transactional(transactionType=TransactionType.READONLY)
    public List<Office> getOffice(long applicationID) throws Exception {
        List<Office> office= officeDAO.getOffice(applicationID);
        if(office == null) {
            throw new RequestFailureException("No Office found ");
        }

        return office;
    }


    @Transactional(transactionType=TransactionType.READONLY)
    public List<Office> getOfficeByCON(long conID) throws Exception {
        LLIConnection lliConnection=lliFlowConnectionService.getConnectionByID(conID);
        //newly added on 9-12-18

        return officeDAO.getOfficeByCon(lliConnection.getHistoryID());
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<Office> getOfficeByConnectionHistoryId(long historyId) throws Exception {
        return officeDAO.getOfficeByCon(historyId);
    }


    @Transactional(transactionType=TransactionType.READONLY)
    public List<Office> getOfficeByCONWithoutLoop(long conID) throws Exception {
        LLIConnection lliConnection=lliFlowConnectionService.getConnectionByID(conID);
        //newly added on 9-12-18

        if(lliConnection.getHistoryID() <= 0) {
            throw new RequestFailureException("Connection History Id is : Zero: Connection found by conID: " + conID + " has some problems.");
        }
        return officeDAO.getOfficeByConWithoutLoop(lliConnection.getHistoryID());
    }


    @Transactional(transactionType=TransactionType.READONLY)
    public List<Office> getOldOfficeByCON(long conID) throws Exception {
        //newly added on 9-12-18

        List<Office> office= officeDAO.getOfficeByCon(conID);
        if(office == null) {
            throw new RequestFailureException("No Office found ");
        }

        return office;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<Office> getOfficeById(long officeId) throws Exception {
        List<Office> office = officeDAO.getOfficeById(officeId);
        if (office == null) {
            throw new RequestFailureException("No Office found ");
        }

        return office;
    }

    @Transactional
    public void insertOffice(Office office) throws Exception{
        officeDAO.insertOffice(office);
    }

    @Transactional
    public void insertSnapshotOffice(LLIConnection oldConnection,long newConID,long appID) throws Exception{
        List<Office> officeList=getOldOfficeByCON(oldConnection.getID());
        for (Office office : officeList) {
            office.setHistoryId(office.getId());
            office.setConnectionID(newConID);
            office.setApplicationId(appID);
            long oldOfficeID=office.getId();
            insertOffice(office);
            localLoopService.insertSnapshotLoop(oldOfficeID,office.getId(),appID);

        }
    }

    @Transactional
    public void insertSnapshotOffice(List<LocalLoop> localLoops, LLIConnection oldConnection, long newConID, long appID) throws Exception{

        int i=0;
        LLIConnection lliConnection=lliFlowConnectionService.getLastDeactivatedConnectionByID(oldConnection.getID());
        List<Office> officeList=getOldOfficeByCON(lliConnection.getHistoryID());
        for (Office office : officeList) {
            if(office.getHistoryId()==0){

                office.setHistoryId(office.getId());
            }else{
                office.setHistoryId(office.getHistoryId());

            }
            office.setConnectionID(newConID);
            office.setApplicationId(appID);
            long oldOfficeID=office.getId();
            localLoops=localLoops
                    .stream()
                    .filter(t->t.getOfficeID()==oldOfficeID)
                    .collect(Collectors.toList());
            insertOffice(office);

            for (LocalLoop localLoop:localLoops
                 ) {
                localLoop.setOfficeID(office.getId());
                if(localLoop.getId()>0){
                    localLoopService.insertApplication(localLoop);
                }else{
                    localLoopService.insertApplication(localLoop);

                }

            }

        }


    }


    @Transactional
    public void insertSnapshotOfficeNew(long oldId,long newConID,long appID) throws Exception{
        List<Office> officeList=getOldOfficeByCON(oldId);
        for (Office office : officeList) {
            office.setHistoryId(office.getId());
            office.setConnectionID(newConID);
            office.setApplicationId(appID);
            long oldOfficeID=office.getId();
            insertOffice(office);
            localLoopService.insertSnapshotLoop(oldOfficeID,office.getId(),appID);

        }
    }

    @Transactional
    public void updateOffice(Office office) throws Exception{
        officeDAO.updateOffice(office);
    }

    @Transactional
    public void insertNewOffice(Office office) throws Exception{
        officeDAO.insertOffice(office);
    }

    @Transactional
    public Office getOfficeByHistoryId(long officeid)throws Exception {
        return officeDAO.getOfficeById(officeid)
                .stream()
                .findFirst()
                .orElseThrow(()->new RequestFailureException("No Office Found"));
    }
}
