package lli.Application.newOffice;

import annotation.DAO;
import annotation.Transactional;
import common.RequestFailureException;
import lli.Application.NewLocalLoop.NewLocalLoop;
import lli.Application.NewLocalLoop.NewLocalLoopService;
import lli.Application.Office.Office;
import requestMapping.Service;
import util.TransactionType;

import java.util.List;

public class NewOfficeService {

    @DAO
    NewOfficeDAO newOfficeDAO;

    @Service
    NewLocalLoopService newLocalLoopService;

    @Transactional(transactionType=TransactionType.READONLY)
    public List<NewOffice> getOffice(long applicationID) throws Exception {
        List<NewOffice> office= newOfficeDAO.getOffice(applicationID);
        if(office!=null &&office.size()>0){
            for (NewOffice newOffice:office
                 ) {
                List<NewLocalLoop> newLocalLoops=newLocalLoopService.getLocalLoopByOffice(newOffice.getId());
                newOffice.setLoops(newLocalLoops);

            }
        }
        if(office == null) {
            throw new RequestFailureException("No Office found ");
        }

        return office;
    }


    @Transactional(transactionType=TransactionType.READONLY)
    public List<NewOffice> getOfficeByCON(long conID) throws Exception {
        List<NewOffice> office= newOfficeDAO.getOfficeByCon(conID);
        if(office == null) {
            throw new RequestFailureException("No Office found ");
        }

        return office;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<NewOffice> getOfficeById(long officeId) throws Exception {
        List<NewOffice> office = newOfficeDAO.getOfficeById(officeId);
        if (office == null) {
            throw new RequestFailureException("No Office found ");
        }

        return office;
    }
    @Transactional(transactionType = TransactionType.READONLY)
    public NewOffice getOneOfficeById(long officeId) throws Exception {
        NewOffice office = newOfficeDAO.getOneOfficeById(officeId);
        if (office == null) {
            throw new RequestFailureException("No Office found ");
        }
        return office;
    }

    @Transactional
    public void insertOffice(NewOffice office) throws Exception{
        newOfficeDAO.insertOffice(office);
    }
    @Transactional
    public void updateOffice(NewOffice office) throws Exception{
        newOfficeDAO.updateOffice(office);
    }

    public Office getOfficeByNewOffice(NewOffice newOffice) {
        Office office = new Office();
        //office.setConnectionID(newOffice.getConnectionID());
        office.setOfficeAddress(newOffice.getOfficeAddress());
        office.setOfficeName(newOffice.getOfficeName());
        office.setApplicationId(newOffice.getApplicationId());
        //office.setLoops(newOffice.getLoops());
        return office;


    }

    @Transactional
    public List<NewOffice> getOfficeByApplication(long applicationID) throws  Exception{
        return  newOfficeDAO.getOfficeByApplication(applicationID);
    }
}
