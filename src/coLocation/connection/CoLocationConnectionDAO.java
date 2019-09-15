package coLocation.connection;

import coLocation.CoLocationConstants;
import coLocation.inventory.CoLocationInventoryService;
import coLocation.inventory.CoLocationInventoryTemplateDTO;
import coLocation.inventory.CoLocationInventoryTemplateService;
import com.google.gson.Gson;
import common.ClientDTOConditionBuilder;
import common.repository.AllClientRepository;
import login.LoginDTO;
import util.KeyValuePair;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;

import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static util.ModifiedSqlGenerator.*;

public class CoLocationConnectionDAO {

    private Class<CoLocationConnectionDTO> classObject = CoLocationConnectionDTO.class;
     CoLocationInventoryTemplateService coLocationInventoryTemplateService= ServiceDAOFactory.getService(CoLocationInventoryTemplateService.class);
      CoLocationInventoryService coLocationInventoryService =ServiceDAOFactory.getService(CoLocationInventoryService.class);


    void updateCoLocationConnection(CoLocationConnectionDTO coLocationConnectionDTO) throws Exception {
        ModifiedSqlGenerator.updateEntity(coLocationConnectionDTO);
    }

    void insertCoLocationConnection(CoLocationConnectionDTO coLocationConnectionDTO) throws Exception {
        insert(coLocationConnectionDTO);
    }

    List<CoLocationConnectionDTO> getColocationConnections(long conID) throws Exception {

        Map<Long, CoLocationInventoryTemplateDTO> inventoryTemplateMap = coLocationInventoryTemplateService.getCoLocationInventoryTemplateMap();
        return ModifiedSqlGenerator.getAllObjectList(CoLocationConnectionDTO.class,
                new CoLocationConnectionDTOConditionBuilder()
                        .Where()
                        .IDEquals(conID)
                        .activeToEquals(Long.MAX_VALUE) // with latest history
//                        .validToEquals(Long.MAX_VALUE) // for active connection; not td not close
                        .getCondition()
        ).stream()
                .peek(t -> {
                    try {
                        modifyDTO(t, inventoryTemplateMap);
                        t.setConTypeDescription(
                                new Gson()
                                        .toJsonTree(
                                                new CoLocationInventoryTemplateDTO(
                                                        t.getConnectionType(),
                                                        CoLocationConstants.connectionTypeNameMap.get(t.getConnectionType())
                                                )
                                        )
                        );
                        t.setCoLocationInventoryDTOList(coLocationInventoryService.getInventoryByConnectionID(t.getID()));//todo: set inventory list
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .collect(Collectors.toList());
        



    }

    private void  modifyDTO(CoLocationConnectionDTO connection, Map<Long, CoLocationInventoryTemplateDTO> inventoryTemplateMap) {


        if(connection.isRackNeeded()){
            CoLocationInventoryTemplateDTO rackSize = inventoryTemplateMap.getOrDefault((long)connection.getRackSize(), null);
            connection.setRackSizeDescription(rackSize != null ? rackSize.getValue() : "N/A");
        }
        if(connection.isRackNeeded()){
            CoLocationInventoryTemplateDTO rackSpace = inventoryTemplateMap.getOrDefault((long)connection.getRackSpace(), null);

            connection.setRackSpaceDescription(rackSpace != null ? rackSpace.getValue() : "N/A");
        }
        if(connection.isPowerNeeded()){
            CoLocationInventoryTemplateDTO coreType = inventoryTemplateMap.getOrDefault((long)connection.getFiberType(), null);

            connection.setPowerTypeDescription(coreType != null ? coreType.getValue() : "N/A");
        }
        if(connection.isFiberNeeded()){
            CoLocationInventoryTemplateDTO powerType = inventoryTemplateMap.getOrDefault((long)connection.getPowerType(), null);

            connection.setOfcTypeDescription(powerType != null ? powerType.getValue() : "N/A");
        }
        if(connection.isFloorSpaceNeeded())
        {
            CoLocationInventoryTemplateDTO floorSpaceType = inventoryTemplateMap.getOrDefault((long)connection.getFloorSpaceType(), null);

            connection.setFloorSpaceTypeDescription(floorSpaceType != null ? floorSpaceType.getValue() : "N/A");
        }

    }

    /**
     * @author forhad
     * @param historyId
     * @return
     * @throws Exception
     */
     List<CoLocationConnectionDTO> getColocationConnectionsByHistoryId(long historyId) throws Exception {

         Map<Long, CoLocationInventoryTemplateDTO> inventoryTemplateMap = coLocationInventoryTemplateService.getCoLocationInventoryTemplateMap();
        return ModifiedSqlGenerator.getAllObjectList(CoLocationConnectionDTO.class,
                new CoLocationConnectionDTOConditionBuilder()
                        .Where()
                        .historyIDEquals(historyId)
                        .orderByactiveToDesc()
                        .getCondition()
        ).stream()
                .peek(t -> {
                    try {
                        modifyDTO(t, inventoryTemplateMap);
                        t.setConTypeDescription(new Gson().toJsonTree(new CoLocationInventoryTemplateDTO(t.getConnectionType(), CoLocationConstants.connectionTypeNameMap.get(t.getConnectionType()))));
                        t.setCoLocationInventoryDTOList(coLocationInventoryService.getInventoryByConnectionID(t.getID()));//todo: set inventory list
                        t.setIncidentDescription(CoLocationConstants.applicationTypeNameMap.get(t.getIncident()));
                        Gson gson = new Gson();
                        t.setClient(gson.toJsonTree( new KeyValuePair<>(t.getClientID(), AllClientRepository.getInstance().getClientByClientID(t.getClientID()).getLoginName())));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .collect(Collectors.toList());




    }

    List<CoLocationConnectionDTO> getColocationConnectionByClientID(long clientID) throws Exception {


        List<CoLocationConnectionDTO> coLocationConnectionDTOS = ModifiedSqlGenerator.getAllObjectList(CoLocationConnectionDTO.class,
                new CoLocationConnectionDTOConditionBuilder()
                        .Where()
                        .clientIDEquals(clientID)
                        .activeToGreaterThanEquals(System.currentTimeMillis())
                        .getCondition()

        ).stream()
                .peek(t -> {
                    try {
                        if(t.isRackNeeded()) {
                            t.setRackSizeDescription(coLocationInventoryTemplateService.getInventoryTemplateByID(t.getRackSize()).getValue());
                            t.setRackSpaceDescription(coLocationInventoryTemplateService.getInventoryTemplateByID(t.getRackSpace()).getDescription());
                        }
                        if(t.isPowerNeeded()) {
                            t.setPowerTypeDescription(coLocationInventoryTemplateService.getInventoryTemplateByID(t.getPowerType()).getValue());
                        }
                        if(t.isFiberNeeded()) {
                            t.setOfcTypeDescription(coLocationInventoryTemplateService.getInventoryTemplateByID(t.getFiberType()).getValue());
                        }
                        if(t.isFloorSpaceNeeded()) {
                            t.setFloorSpaceTypeDescription(coLocationInventoryTemplateService.getInventoryTemplateByID(t.getFloorSpaceType()).getValue());
                        }
//                        t.setConTypeDescription(CoLocationConstants.connectionTypeNameMap.get(t.getConnectionType()));
                        t.setConTypeDescription(new Gson().toJsonTree(new CoLocationInventoryTemplateDTO(t.getConnectionType(), CoLocationConstants.connectionTypeNameMap.get(t.getConnectionType()))));

                        t.setCoLocationInventoryDTOList(coLocationInventoryService.getInventoryByConnectionID(t.getID()));//todo: set inventory list

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .collect(Collectors.toList());;

        if (coLocationConnectionDTOS.size() > 0) {
            return coLocationConnectionDTOS;
        } else {
            return null;
        }


    }

    List<CoLocationConnectionDTO> getColocationReconnectConnectionListByClientID(long clientID) throws Exception {


        List<CoLocationConnectionDTO> coLocationConnectionDTOS = ModifiedSqlGenerator.getAllObjectList(CoLocationConnectionDTO.class,
                new CoLocationConnectionDTOConditionBuilder()
                        .Where()
                        .clientIDEquals(clientID)
                        .incidentEquals(CoLocationConstants.TD)
                        .activeToGreaterThanEquals(System.currentTimeMillis())
                        .getCondition()

        ).stream()
                .peek(t -> {
                    try {
                        if(t.isRackNeeded()) {
                            t.setRackSizeDescription(coLocationInventoryTemplateService.getInventoryTemplateByID(t.getRackSize()).getValue());
                            t.setRackSpaceDescription(coLocationInventoryTemplateService.getInventoryTemplateByID(t.getRackSpace()).getValue());
                        }
                        if(t.isPowerNeeded()) {
                            t.setPowerTypeDescription(coLocationInventoryTemplateService.getInventoryTemplateByID(t.getPowerType()).getValue());
                        }
                        if(t.isFiberNeeded()) {
                            t.setOfcTypeDescription(coLocationInventoryTemplateService.getInventoryTemplateByID(t.getFiberType()).getValue());
                        }
                        if(t.isFloorSpaceNeeded()) {
                            t.setOfcTypeDescription(coLocationInventoryTemplateService.getInventoryTemplateByID(t.getFloorSpaceType()).getValue());
                        }
//                        t.setConTypeDescription(CoLocationConstants.connectionTypeNameMap.get(t.getConnectionType()));
                        t.setConTypeDescription(new Gson().toJsonTree(new CoLocationInventoryTemplateDTO(t.getConnectionType(), CoLocationConstants.connectionTypeNameMap.get(t.getConnectionType()))));

                        t.setCoLocationInventoryDTOList(coLocationInventoryService.getInventoryByConnectionID(t.getID()));//todo: set inventory list

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .collect(Collectors.toList());;

        if (coLocationConnectionDTOS.size() > 0) {
            return coLocationConnectionDTOS;
        } else {
            return null;
        }


    }


    List<CoLocationConnectionDTO> getAllColocationConnection() throws Exception {


        return ModifiedSqlGenerator.getAllObjectList(CoLocationConnectionDTO.class).stream()
                .map(t -> {
                    try {
                        t.setRackSizeDescription(coLocationInventoryTemplateService.getInventoryTemplateByID(t.getRackSize()).getValue());
                        t.setRackSpaceDescription(coLocationInventoryTemplateService.getInventoryTemplateByID(t.getRackSpace()).getValue());
                        t.setPowerTypeDescription(coLocationInventoryTemplateService.getInventoryTemplateByID(t.getPowerType()).getValue());
                        t.setFloorSpaceTypeDescription(coLocationInventoryTemplateService.getInventoryTemplateByID(t.getFloorSpaceType()).getValue());
//                                t.setConTypeDescription(CoLocationConstants.connectionTypeNameMap.get(t.getConnectionType()));
//                                t.setConTypeDescription(CoLocationConstants.connectionTypeNameMap.get(t.getConnectionType()));
                        t.setConTypeDescription(new Gson().toJsonTree(new CoLocationInventoryTemplateDTO(t.getConnectionType(), CoLocationConstants.connectionTypeNameMap.get(t.getConnectionType()))));


                        t.setOfcTypeDescription(coLocationInventoryTemplateService.getInventoryTemplateByID(t.getFiberType()).getValue());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return t;
                })
                .collect(Collectors.toList());
    }


    public List<Long> getIDsWithSearchCriteria(Hashtable<String, String> hashtable, LoginDTO loginDTO) throws Exception {
        ResultSet rs = getResultSetBySqlPair(
                new CoLocationConnectionDTOConditionBuilder()
                        .selectID()
                        .fromCoLocationConnectionDTO()
                        .Where()
                        .activeToGreaterThan(System.currentTimeMillis())
                        .clientIDEquals(!loginDTO.getIsAdmin() ? loginDTO.getAccountID() : null)
//                        .nameLeftLike(hashtable.get("connectionName"))
                        .clientIDInSqlPair(
                                new ClientDTOConditionBuilder()
                                        .selectClientID()
                                        .fromClientDTO()
                                        .Where()
                                        .loginNameBothLike(hashtable.get("clientName"))
                                        .getNullableSqlPair()
                        ).getSqlPair()
        );

        return getSingleColumnListByResultSet(rs, Long.class);
    }

    public List<CoLocationConnectionDTO> getDTOListByIDList(List<Long> idList) throws Exception {
        return getObjectListByIDList(classObject, idList);
    }

    List<CoLocationConnectionDTO> getAnyColocationConnection(long conID) throws Exception {

        Map<Long, CoLocationInventoryTemplateDTO> inventoryTemplateMap = coLocationInventoryTemplateService.getCoLocationInventoryTemplateMap();
        return ModifiedSqlGenerator.getAllObjectList(CoLocationConnectionDTO.class,
                new CoLocationConnectionDTOConditionBuilder()
                        .Where()
                        .IDEquals(conID)
//                        .activeToEquals(Long.MAX_VALUE) // with latest history
//                        .validToEquals(Long.MAX_VALUE) // for active connection; not td not close
                        .getCondition()
        ).stream()
                .peek(t -> {
                    try {
                        modifyDTO(t, inventoryTemplateMap);

                        t.setConTypeDescription(
                                new Gson()
                                        .toJsonTree(
                                                new CoLocationInventoryTemplateDTO(
                                                        t.getConnectionType(),
                                                        CoLocationConstants.connectionTypeNameMap.get(t.getConnectionType())
                                                )
                                        )
                        );
                        t.setCoLocationInventoryDTOList(coLocationInventoryService.getInventoryByConnectionID(t.getID()));//todo: set inventory list
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .collect(Collectors.toList());
    }
}
