package coLocation.accounts.VariableCost;

import annotation.DAO;
import annotation.Transactional;
import coLocation.accounts.commonCost.AllVariableUnitCharge;
import coLocation.application.CoLocationApplicationDTO;
import coLocation.application.CoLocationApplicationService;
import coLocation.connection.CoLocationConnectionDTO;
import com.itextpdf.tool.xml.exceptions.NoDataException;
import common.RequestFailureException;
import lombok.extern.log4j.Log4j;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;
import util.TransactionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Log4j
public class VariableCostService {

    @DAO
    VariableCostDAO variableCostDAO;

    @Transactional
    public void insertCostConfig(VariableCostDTO costDTO) throws Exception {
        variableCostDAO.insertCostConfig(costDTO);
    }

    @Transactional
    public void updateCostConfig(VariableCostDTO costDTO) throws Exception {
        variableCostDAO.updateCostConfig(costDTO);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public VariableCostDTO getCostByType(int typeID) throws Exception {
        List<VariableCostDTO> variableCostDTOs   = variableCostDAO.getCostByType(typeID);
        if(variableCostDTOs.size() > 1){
            throw new RequestFailureException("Multiple cost config found for type " + typeID + " in colocation cost config");
        }
        return variableCostDTOs.stream()
                .findFirst()
                .orElseThrow(()->new RequestFailureException("No Data found in colocation cost config for type " + typeID));


    }
    @Transactional(transactionType = TransactionType.READONLY)
    public VariableCostDTO getCostByTypeAndAmount(int typeID,int quantityID) throws Exception {
        List<VariableCostDTO> list = variableCostDAO.getCostByTypeAndAmount(typeID,quantityID);
        if(list.size()>1){
            throw new RequestFailureException("Multiple cost config found for type " + typeID + " quantity id " + quantityID + " in colocation cost config");
        }
        return  list
                .stream()
                .findFirst()
                .orElseThrow(()->new RequestFailureException("No Data found in colocation cost config for type " + typeID + ", quantity id " + quantityID));
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public Map<Integer, Map<Integer, List<VariableCostDTO>>> getVariableCostConfigMap() throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(VariableCostDTO.class)
                .stream()
                .collect(Collectors.groupingBy(VariableCostDTO::getTypeID,
                            Collectors.groupingBy(VariableCostDTO::getQuantityID)
                        )
                );
    }

    private VariableCostDTO getRackUnitCharge(
            Map<Integer, Map<Integer, List<VariableCostDTO>>> mapOfVariableCostsToQuantityToType,
            int rackTypeID,
            int rackSpace) throws Exception {

        return mapOfVariableCostsToQuantityToType.getOrDefault(rackTypeID, new HashMap<>())
                .getOrDefault(rackSpace, new ArrayList<>())
                .stream()
                .findFirst()
                .orElseThrow(()->new RequestFailureException(
                        "No Rack Unit Charge Found by rack type " + rackTypeID + " and space type " + rackSpace));

    }
    private VariableCostDTO getPowerCharge(
            Map<Integer, Map<Integer, List<VariableCostDTO>>> mapOfVariableCostsToQuantityToType,
            int powerType, double powerAmount) {
        VariableCostDTO power = mapOfVariableCostsToQuantityToType.getOrDefault(powerType, new HashMap<>())
                .getOrDefault(1, new ArrayList<>()) // 1 is for unit charge ; AC 1 unit ; DC 1 unit
                .stream()
                .findFirst()
                .orElseThrow(()->new RequestFailureException(
                        "No Power Unit Charge Found by power type " + powerType + " quantity id " + 1));
        power.setPrice(powerAmount * power.getPrice());
        return power;
    }

    private VariableCostDTO getFiberCharge(
            Map<Integer, Map<Integer, List<VariableCostDTO>>> mapOfVariableCostsToQuantityToType,
            int fiberType, int fiberCore) {
        VariableCostDTO fiber = mapOfVariableCostsToQuantityToType.getOrDefault(fiberType, new HashMap<>())
                .getOrDefault(1, new ArrayList<>())// 1 is for unit charge ; 1 core Single Fiber ; 1 core Double Fiber
                .stream()
                .findFirst()
                .orElseThrow(()->new RequestFailureException("No Fiber Unit Charge Found by fiber type " + fiberType+ " quantity id " + 1));
        fiber.setPrice(fiberCore * fiber.getPrice());
        return fiber;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(ServiceDAOFactory.getService(VariableCostService.class).getAllVariableChargeByCoLocationApplication(
                ServiceDAOFactory.getService(CoLocationApplicationService.class).getColocationApplication(22001)
        ));
    }


    @Transactional(transactionType = TransactionType.READONLY)
    public AllVariableUnitCharge getAllVariableChargeByCoLocationApplication(CoLocationApplicationDTO application) throws Exception {
        Map<Integer, Map<Integer, List<VariableCostDTO>>> mapOfVariableCostsToQuantityToType = getVariableCostConfigMap();


        VariableCostDTO rackUnitCharge = new VariableCostDTO();
        if(application.isRackNeeded()) {
            rackUnitCharge = getVariableCost(mapOfVariableCostsToQuantityToType,
                    application.getRackTypeID(), application.getRackSpace(), 1);
        }

        VariableCostDTO fiberUnitCharge = new VariableCostDTO();
        if(application.isFiberNeeded()){
           fiberUnitCharge = getVariableCost(mapOfVariableCostsToQuantityToType,
                   application.getFiberType(), 1, application.getFiberCore());
        }

        VariableCostDTO powerUnitCharge = new VariableCostDTO();
        if(application.isPowerNeeded()) {
          powerUnitCharge = getVariableCost(mapOfVariableCostsToQuantityToType,
                  application.getPowerType(), 1, application.getPowerAmount());
        }

        VariableCostDTO floorSpaceUnitCharge = new VariableCostDTO();
        if(application.isFloorSpaceNeeded()) {
            floorSpaceUnitCharge = getVariableCost(mapOfVariableCostsToQuantityToType,
                    application.getFloorSpaceType(), 1, application.getFloorSpaceAmount());
        }
        return new AllVariableUnitCharge(rackUnitCharge, powerUnitCharge, fiberUnitCharge, floorSpaceUnitCharge);
    }


    @Transactional(transactionType = TransactionType.READONLY)
    public AllVariableUnitCharge getAllVariableUnitChargeByCoLocation(CoLocationConnectionDTO connectionDTO) throws Exception {
        Map<Integer, Map<Integer, List<VariableCostDTO>>> mapOfVariableCostsToQuantityToType = getVariableCostConfigMap();

        VariableCostDTO rackUnitCharge = new VariableCostDTO();
        if(connectionDTO.isRackNeeded()){
            rackUnitCharge = getVariableCost(mapOfVariableCostsToQuantityToType,
                    connectionDTO.getRackSize(), connectionDTO.getRackSpace(), 1);
        }

        VariableCostDTO fiberUnitCharge = new VariableCostDTO();
        if(connectionDTO.isFiberNeeded()) {
            fiberUnitCharge = getVariableCost(mapOfVariableCostsToQuantityToType, connectionDTO.getFiberType(), 1, connectionDTO.getFiberCore());
        }

        VariableCostDTO powerUnitCharge = new VariableCostDTO();
        if(connectionDTO.isPowerNeeded()) {
           powerUnitCharge = getVariableCost(mapOfVariableCostsToQuantityToType,  connectionDTO.getPowerType(),1, connectionDTO.getPowerAmount());
        }

        VariableCostDTO floorSpaceUnitCharge = new VariableCostDTO();
        if(connectionDTO.isFloorSpaceNeeded()) {
            floorSpaceUnitCharge = getVariableCost(mapOfVariableCostsToQuantityToType,
                    connectionDTO.getFloorSpaceType(), 1, connectionDTO.getFloorSpaceAmount());
        }
        AllVariableUnitCharge allVariableUnitCharge = new AllVariableUnitCharge(rackUnitCharge,powerUnitCharge ,fiberUnitCharge, floorSpaceUnitCharge );
        log.info(allVariableUnitCharge);
        return allVariableUnitCharge;
    }

    private<T extends Number> VariableCostDTO getVariableCost(Map<Integer, Map<Integer, List<VariableCostDTO>>> map,
                                                              int firstKey, int secondKey, double amount ) throws Exception {


        VariableCostDTO costDTO = map.getOrDefault(firstKey, new HashMap<>())
                .getOrDefault(secondKey, new ArrayList<>())
                .stream()
                .findFirst()
                .orElse(null);
        if(costDTO == null){
            throw new NoDataException("No variable cost found for first parameter type: " + firstKey + ", second parameter type: " + secondKey );
        }
        costDTO.setPrice(costDTO.getPrice() * amount);

        return costDTO;
    }

}
