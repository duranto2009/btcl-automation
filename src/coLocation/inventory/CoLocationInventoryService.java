package coLocation.inventory;

import annotation.DAO;
import annotation.Transactional;
import coLocation.CoLocationConstants;
import coLocation.connection.CoLocationConnectionDTO;
import common.RequestFailureException;
import login.LoginDTO;
import requestMapping.Service;
import util.NavigationService;
import util.TransactionType;

import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class CoLocationInventoryService implements NavigationService {

    @DAO private CoLocationInventoryDAO coLocationInventoryDAO;
    @DAO private CoLocationInventoryInUseDAO coLocationInventoryInUseDAO;
    @DAO private CoLocationInventoryTemplateDAO coLocationInventoryTemplateDAO;

    @Service private CoLocationInventoryInUseService coLocationInventoryInUseService;
    @Service private CoLocationInventoryTemplateService coLocationInventoryTemplateService;

    @Override
    @Transactional(transactionType = TransactionType.READONLY)
    public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
        return getIDsWithSearchCriteria(new Hashtable<>(), loginDTO, objects);
    }

    @Override
    @Transactional(transactionType = TransactionType.READONLY)
    public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects) throws Exception {
        return (List<CoLocationInventoryDTO>) coLocationInventoryDAO.getIDsWithSearchCriteria(searchCriteria, loginDTO);
    }

    @Override
    @Transactional(transactionType = TransactionType.READONLY)
    public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
        int elementsToSkip = (int) objects[1];
        int elementsToConsider = (int) objects[2];

        Map<Long, CoLocationInventoryTemplateDTO> map = coLocationInventoryTemplateService.getCoLocationInventoryTemplateMap();
        return coLocationInventoryDAO.getDTOListByIDList((List<Long>) recordIDs)
                .stream()
                .peek(t->{
                    try {
                        CoLocationInventoryTemplateDTO dto = map.getOrDefault(t.getTemplateID(), null);
                        t.setType(dto!=null ?dto.getValue():"N/A");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                })
                .skip(elementsToSkip)
                .limit(elementsToConsider)
                .collect(Collectors.toList());


    }


    @Transactional
    public void updateCoLocationInventory(CoLocationInventoryDTO coLocationInventoryDTO) throws Exception {
        coLocationInventoryDAO.updateCoLocationInventory(coLocationInventoryDTO);
    }


    @Transactional
    public void insertCoLocationInventory(CoLocationInventoryDTO coLocationInventoryDTO) throws Exception {
        coLocationInventoryDAO.insertCoLocationInventory(coLocationInventoryDTO);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public CoLocationInventoryDTO getInventoryByID(long id) throws Exception {
        return coLocationInventoryDAO.getInventoryByID(id);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<CoLocationInventoryDTO> getInventoryByConnectionID(long conID) throws Exception {
        return coLocationInventoryInUseService.getInventoryInUseByConnection(conID)
                .stream()
                .map(t-> {
                    try {
                        return getInventoryByID(t.getInventoryID());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<CoLocationInventoryDTO> getFreeInventory(long catType, long templateType, double availableAmount) throws Exception {
        List<CoLocationInventoryDTO> coLocationInventoryDTOS = coLocationInventoryDAO.getFreeInventory(catType, templateType, availableAmount);
        if (coLocationInventoryDTOS.size() < 1) {
            if(catType==CoLocationConstants.INVENTORY_RACK){

                throw new RequestFailureException("No Free Rack is present in inventory for the specified configuration");
            }
            else if(catType==CoLocationConstants.INVENTORY_POWER){

                throw new RequestFailureException("No Free Power amount is present in inventory for the specified configuration");
            }
            else if(catType==CoLocationConstants.INVENTORY_FIBER){

                throw new RequestFailureException("No Free Fiber amount is present in inventory for the specified configuration");
            }else if(catType==CoLocationConstants.INVENTORY_CATEGORY_FLOOR_SPACE){

                throw new RequestFailureException("No Free Floor Space is present in inventory for the specified configuration");
            }
        }
        return coLocationInventoryDTOS;

    }


    @Transactional(transactionType = TransactionType.READONLY)
    public List<CoLocationInventoryDTO> getUnusedFreeInventory(long catType, long templateType, double availableAmount) throws Exception {

        List<CoLocationInventoryDTO> coLocationInventoryDTOS = coLocationInventoryDAO.getUnusedFreeInventory(catType, templateType, availableAmount);
        if (coLocationInventoryDTOS.size() < 1) {
            throw new RequestFailureException("No Data found ");
        }
        return coLocationInventoryDTOS;
    }

    public void allocateInventory(CoLocationConnectionDTO coLocationConnectionDTO, int catagoryType, double amount) throws Exception {

        long templateType = 0;
        if (catagoryType == CoLocationConstants.INVENTORY_RACK) {
            templateType = coLocationConnectionDTO.getRackSize();
        } else if (catagoryType == CoLocationConstants.INVENTORY_POWER) {
            templateType = coLocationConnectionDTO.getPowerType();

        } else if (catagoryType == CoLocationConstants.INVENTORY_FIBER) {
            templateType = coLocationConnectionDTO.getFiberType();
        } else if (catagoryType == CoLocationConstants.INVENTORY_CATEGORY_FLOOR_SPACE) {
            templateType = coLocationConnectionDTO.getFloorSpaceType();
        }
        List<CoLocationInventoryDTO> coLocationInventoryDTOList = getFreeInventory(catagoryType, templateType, amount);
        if(coLocationInventoryDTOList.size()<1){

        }
        CoLocationInventoryDTO coLocationInventoryDTO = coLocationInventoryDTOList.get(0);
        coLocationInventoryDTO.setAvailableAmount(coLocationInventoryDTO.getAvailableAmount() - amount);
        coLocationInventoryDTO.setUsed(true);
        CoLocationInventoryInUseDTO coLocationInventoryInUseDTO = CoLocationInventoryInUseDTO.builder()
                .inventoryID(coLocationInventoryDTO.getId())
                .clientID(coLocationConnectionDTO.getClientID())
                .connectionID(coLocationConnectionDTO.getID())
                .occupiedDate(System.currentTimeMillis())
                .status(1)
                .build();

        coLocationInventoryDAO.updateCoLocationInventory(coLocationInventoryDTO);
        coLocationInventoryInUseDAO.insertCoLocationInventoryinUse(coLocationInventoryInUseDTO);
    }

    public void deallocateInventory(CoLocationConnectionDTO coLocationConnectionDTO) throws Exception {


        List<CoLocationInventoryDTO> coLocationInventoryDTOs = getInventoryByConnectionID(coLocationConnectionDTO.getID());
        List<CoLocationInventoryInUseDTO> locationInventoryInUseDTOS = coLocationInventoryInUseDAO.getInventoryInUseByConnection(coLocationConnectionDTO.getID());

        for (CoLocationInventoryDTO coLocationInventoryDTO : coLocationInventoryDTOs
        ) {

            double amount=0;
            if (coLocationInventoryDTO.getCatagoryID() == CoLocationConstants.INVENTORY_RACK) {
                amount = Double.parseDouble(coLocationInventoryTemplateDAO.getInventoryTemplateByID(coLocationConnectionDTO.getRackSpace()).getValue());
            } else if (coLocationInventoryDTO.getCatagoryID() == CoLocationConstants.INVENTORY_POWER) {
                amount = coLocationConnectionDTO.getPowerAmount();
            } else if (coLocationInventoryDTO.getCatagoryID() == CoLocationConstants.INVENTORY_FIBER) {
                amount = coLocationConnectionDTO.getFiberCore();
            }else if (coLocationInventoryDTO.getCatagoryID() == CoLocationConstants.INVENTORY_CATEGORY_FLOOR_SPACE) {
                amount = coLocationConnectionDTO.getFloorSpaceAmount();
            }
            coLocationInventoryDTO.setAvailableAmount(coLocationInventoryDTO.getAvailableAmount()+amount);
            updateCoLocationInventory(coLocationInventoryDTO);
        }
        for (CoLocationInventoryInUseDTO coLocationInventoryInUseDTO:locationInventoryInUseDTOS
             ) {
            coLocationInventoryInUseDTO.setStatus(0);
            coLocationInventoryInUseDAO.updateCoLocationInventoryInUse(coLocationInventoryInUseDTO);
        }
//        coLocationInventoryDAO.updateCoLocationInventory(coLocationInventoryDTO);
//        coLocationInventoryInUseDAO.insertCoLocationInventoryinUse(coLocationInventoryInUseDTO);
    }


}
