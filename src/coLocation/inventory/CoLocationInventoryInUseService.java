package coLocation.inventory;

import annotation.DAO;
import annotation.Transactional;
import common.RequestFailureException;
import util.TransactionType;

import java.util.List;


public class CoLocationInventoryInUseService {


    @DAO
    CoLocationInventoryInUseDAO coLocationInventoryInUseDAO;
    @Transactional
    public void updateCoLocationInventoryInUse(CoLocationInventoryInUseDTO coLocationInventoryInUseDTO) throws Exception {
        coLocationInventoryInUseDAO.updateCoLocationInventoryInUse(coLocationInventoryInUseDTO);
    }


    @Transactional
    public void insertCoLocationInventoryinUse(CoLocationInventoryInUseDTO coLocationInventoryInUseDTO) throws Exception {
        coLocationInventoryInUseDAO.insertCoLocationInventoryinUse(coLocationInventoryInUseDTO);
    }

    @Transactional(transactionType= TransactionType.READONLY)
    public List<CoLocationInventoryInUseDTO> getInventoryInUseByClient(long clientID) throws Exception {

        List<CoLocationInventoryInUseDTO> locationInventoryInUseDTOS = coLocationInventoryInUseDAO.getInventoryInUseByClient(clientID);
        if (locationInventoryInUseDTOS.size() < 1) {
            throw new RequestFailureException("No Data found ");
        }
        return locationInventoryInUseDTOS;



    }


    @Transactional(transactionType= TransactionType.READONLY)
    List<CoLocationInventoryInUseDTO> getInventoryInUseByConnection(long conID) throws Exception {

        List<CoLocationInventoryInUseDTO> locationInventoryInUseDTOS = coLocationInventoryInUseDAO.getInventoryInUseByConnection(conID);
        if (locationInventoryInUseDTOS.size() < 1) {
            throw new RequestFailureException("No Data found ");
        }
        return locationInventoryInUseDTOS;
    }




}
