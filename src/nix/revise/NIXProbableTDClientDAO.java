package nix.revise;

import common.ClientDTOConditionBuilder;
import login.LoginDTO;
import util.ModifiedSqlGenerator;
import util.TimeConverter;

import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.List;

import static util.ModifiedSqlGenerator.*;

public class NIXProbableTDClientDAO {
    Class<NIXProbableTDClient> classObject = NIXProbableTDClient.class;

    public List<NIXProbableTDClient> getDTOListByIDList(List<Long> idList) throws Exception{
        return getObjectListByIDList(classObject, idList);
    }

    public List<Long> getIDsWithSearchCriteria(Hashtable<String, String> hashtable,LoginDTO loginDTO) throws Exception{

        ResultSet rs = getResultSetBySqlPair(
                new NIXProbableTDClientConditionBuilder()
                        .selectID()
                        .fromNIXProbableTDClient()
                        .Where()
                        .clientIDEquals(!loginDTO.getIsAdmin()?loginDTO.getAccountID():null)
                        .clientIDInSqlPair(
                                new ClientDTOConditionBuilder()
                                        .selectClientID()
                                        .fromClientDTO()
                                        .Where()
                                        .loginNameBothLike(hashtable.get("clientName"))
                                        .getNullableSqlPair()
                        )
                        .tdDateGreaterThanEquals(TimeConverter.getDateFromString( hashtable.get("fromDate")))
                        .tdDateLessThan(TimeConverter.getNextDateFromString(hashtable.get("toDate")))
                        .orderBytdDateAsc()
                        .getSqlPair()
        );

        return getSingleColumnListByResultSet(rs, Long.class);
    }

    public NIXProbableTDClient getNIXProbableTDClientByClientID(long clientID) throws Exception {
        List<NIXProbableTDClient> nixProbableTDClientList = getAllObjectList(classObject,
                new NIXProbableTDClientConditionBuilder()
                        .Where()
                        .clientIDEquals(clientID)
                        .getCondition()
        );

        return nixProbableTDClientList.isEmpty()?null: nixProbableTDClientList.get(0);
    }
    public void insertNIXProbableTDClient(NIXProbableTDClient nixProbableTDClient) throws Exception {
        ModifiedSqlGenerator.insert(nixProbableTDClient);
    }
    public void updateNIXProbableTDClient(NIXProbableTDClient nixProbableTDClient) throws Exception {
        ModifiedSqlGenerator.updateEntity(nixProbableTDClient);
    }
}
