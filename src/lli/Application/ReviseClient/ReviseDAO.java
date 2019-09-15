package lli.Application.ReviseClient;

import common.ClientDTOConditionBuilder;
import login.LoginDTO;
import util.ModifiedSqlGenerator;
import util.TimeConverter;

import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.List;

import static util.ModifiedSqlGenerator.*;

public class ReviseDAO {

    Class<ReviseDTO> classObject = ReviseDTO.class;

    public List<ReviseDTO> getAllApplication() throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(ReviseDTO.class);
    }


    public ReviseDTO getAppByID(long applicationID) throws Exception {
        return ModifiedSqlGenerator.getObjectByID(ReviseDTO.class, applicationID);
    }

    public ReviseDTO getAppByClientAndAppTypeNotCompleted(long clientID,int appType) throws Exception {
        List<ReviseDTO> reviseDTOS = ModifiedSqlGenerator.getAllObjectList(
                ReviseDTO.class,
                new ReviseDTOConditionBuilder()
                        .Where()
                        .clientIDEquals(clientID)
                        .applicationTypeEquals(appType)
                        .isCompleted(false)
                        .getCondition()
        );
        if (reviseDTOS.size()>0){

            return reviseDTOS.get(0);
        }else {
            return new ReviseDTO();
        }
    }

    List<ReviseDTO> getAppByDemandNoteId(long dnId) throws Exception {
       return ModifiedSqlGenerator.getAllObjectList(
                ReviseDTO.class,
                new ReviseDTOConditionBuilder()
                        .Where()
                        .demandNoteIDEquals(dnId)
                        .getCondition()
        );
    }

    public List<Long> getIDsWithSearchCriteria(Hashtable<String, String> hashtable, LoginDTO loginDTO) throws Exception {
        ResultSet rs = getResultSetBySqlPair(
                new ReviseDTOConditionBuilder()
                        .selectId()
                        .fromReviseDTO()
                        .Where()
                        .clientIDEquals(!loginDTO.getIsAdmin() ? loginDTO.getAccountID() : null)
                        .suggestedDateGreaterThanEquals(TimeConverter.getDateFromString(hashtable.get("fromDate")))
                        .suggestedDateLessThanEquals(TimeConverter.getDateFromString(hashtable.get("toDate")))
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

    public List<ReviseDTO> getDTOListByIDList(List<Long> idList) throws Exception {
        return getObjectListByIDList(classObject, idList);
    }

    public void insertApp(ReviseDTO reviseDTO) throws Exception {
        insert(reviseDTO);
    }

    public void updateApplication(ReviseDTO reviseDTO) throws Exception {
        ModifiedSqlGenerator.updateEntity(reviseDTO);
    }

    public void updateApplicationState(long applicatonID, int applicationSate) {
        try {
            ReviseDTO reviseDTO = getAppByID(applicatonID);
            reviseDTO.setState(applicationSate);
            ModifiedSqlGenerator.updateEntity(reviseDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}