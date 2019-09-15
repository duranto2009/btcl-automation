package nix.revise;

import common.ClientDTOConditionBuilder;
import login.LoginDTO;
import util.ModifiedSqlGenerator;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.List;
import static util.ModifiedSqlGenerator.*;

public class NIXReviseDAO {

    Class<NIXReviseDTO> classObject = NIXReviseDTO.class;

    public List<NIXReviseDTO> getAllApplication() throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(NIXReviseDTO.class);
    }


    NIXReviseDTO getAppByID(long applicationID) throws Exception {
        return ModifiedSqlGenerator.getObjectByID(NIXReviseDTO.class, applicationID);
    }

    public List<NIXReviseDTO> getAppByClientAndAppTypeNotCompleted(long clientID,int appType) throws Exception {
        List<NIXReviseDTO> reviseDTOS = ModifiedSqlGenerator.getAllObjectList(
                NIXReviseDTO.class,
                new NIXReviseDTOConditionBuilder()
                        .Where()
                        .clientIDEquals(clientID)
                        .applicationTypeEquals(appType)
                        .isCompleted(false)
                        .getCondition()
        );
        return reviseDTOS;
    }

    List<NIXReviseDTO> getAppByDemandNoteId(long dnId) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(
            NIXReviseDTO.class,
            new NIXReviseDTOConditionBuilder()
                .Where()
                .demandNoteIDEquals(dnId)
                .getCondition()
        );
    }

    public List<Long> getIDsWithSearchCriteria(Hashtable<String, String> hashtable, LoginDTO loginDTO) throws Exception {
        ResultSet rs = getResultSetBySqlPair(
                new NIXReviseDTOConditionBuilder()
                        .selectId()
                        .fromNIXReviseDTO()
                        .Where()
                        .clientIDEquals(!loginDTO.getIsAdmin() ? loginDTO.getAccountID() : null)
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

    public List<NIXReviseDTO> getDTOListByIDList(List<Long> idList) throws Exception {
        return getObjectListByIDList(classObject, idList);
    }

    public void insertApp(NIXReviseDTO reviseDTO) throws Exception {
        insert(reviseDTO);
    }

    public void updateApplication(NIXReviseDTO reviseDTO) throws Exception {
        ModifiedSqlGenerator.updateEntity(reviseDTO);
    }

    public void updateApplicationState(long applicatonID, int applicationSate) {
        try {
            NIXReviseDTO reviseDTO = getAppByID(applicatonID);
            reviseDTO.setState(applicationSate);
            ModifiedSqlGenerator.updateEntity(reviseDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
