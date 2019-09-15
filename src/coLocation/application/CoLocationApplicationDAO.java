package coLocation.application;

import common.ClientDTOConditionBuilder;
import login.LoginDTO;
import util.ModifiedSqlGenerator;

import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.List;

import static util.ModifiedSqlGenerator.*;

public class CoLocationApplicationDAO {

    Class<CoLocationApplicationDTO> classObject = CoLocationApplicationDTO.class;

    public void updateCoLocationApplication(CoLocationApplicationDTO coLocationApplicationDTO) throws Exception {
        ModifiedSqlGenerator.updateEntity(coLocationApplicationDTO);
    }


    public void insertCoLocationApplication(CoLocationApplicationDTO coLocationApplicationDTO) throws Exception {
        insert(coLocationApplicationDTO);
    }

    public List<CoLocationApplicationDTO> getColocationApplication(long applicationID) throws Exception {

        return ModifiedSqlGenerator.getAllObjectList(CoLocationApplicationDTO.class,
                new CoLocationApplicationDTOConditionBuilder()
                        .Where()
                        .applicationIDEquals(applicationID)
                        .getCondition()
        );
    }
    public List<CoLocationApplicationDTO> getAllColocationApplication() throws Exception {


        List<CoLocationApplicationDTO> coLocationApplicationDTOS =
                ModifiedSqlGenerator.getAllObjectList(CoLocationApplicationDTO.class);


        return coLocationApplicationDTOS;
    }

    public List<CoLocationApplicationDTO> getColocationApplicationByDemandNoteId(long demandNoteId) throws Exception {

        return ModifiedSqlGenerator.getAllObjectList(CoLocationApplicationDTO.class,
                new CoLocationApplicationDTOConditionBuilder()
                        .Where()
                        .demandNoteIDEquals(demandNoteId)
                        .getCondition()
        );
    }


    public List<Long> getIDsWithSearchCriteria(Hashtable<String, String> hashtable, LoginDTO loginDTO) throws Exception {
        ResultSet rs = getResultSetBySqlPair(
                new CoLocationApplicationDTOConditionBuilder()
                        .selectApplicationID()
                        .fromCoLocationApplicationDTO()
                        .Where()
                        .clientIDEquals(!loginDTO.getIsAdmin() ? loginDTO.getAccountID() : null)
                        .clientIDInSqlPair(
                                new ClientDTOConditionBuilder()
                                        .selectClientID()
                                        .fromClientDTO()
                                        .Where()
                                        .loginNameBothLike(hashtable.get("clientName"))
                                        .getNullableSqlPair())
                        .orderByapplicationIDDesc()
                        .getSqlPair()
        );

        return getSingleColumnListByResultSet(rs, Long.class);
    }

    public List<CoLocationApplicationDTO> getDTOListByIDList(List<Long> idList) throws Exception {
        return getObjectListByIDList(classObject, idList);
    }


    List<CoLocationApplicationDTO> getColocationApplicationByConnectionId(long connectionId) throws Exception {
        return getAllObjectList(classObject,
                new CoLocationApplicationDTOConditionBuilder()
                .Where()
                .connectionIdEquals(connectionId)
                .getCondition()

        );
    }
}
