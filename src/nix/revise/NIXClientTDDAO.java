package nix.revise;

import common.ClientDTOConditionBuilder;
import login.LoginDTO;
import util.TimeConverter;

import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.List;

import static util.ModifiedSqlGenerator.*;

public class NIXClientTDDAO {


    Class<NIXClientTD> classObject = NIXClientTD.class;

    public List<NIXClientTD> getDTOListByIDList(List<Long> idList) throws Exception{
        return getObjectListByIDList(classObject, idList);
    }
    public void updateClientTD(NIXClientTD lliClientTD) throws Exception{
        updateEntity(lliClientTD);
    }
    public void insertClientTD(NIXClientTD lliClientTD) throws Exception{
        insert(lliClientTD);
    }
    public boolean existsAnyClientTDByClientID(long clientID) throws Exception{

        ResultSet rs = getResultSetBySqlPair(
                new NIXClientTDConditionBuilder()
                        .selectID()
                        .fromNIXClientTD()
                        .Where()
                        .clientIDEquals(clientID)
                        .limit(1)
                        .getSqlPair()
        );

        List<Long> idList = getSingleColumnListByResultSet(rs, Long.class);

        return idList.isEmpty()?false:true;
    }

    public List<Long> getIDsWithSearchCriteria(Hashtable<String, String> hashtable, LoginDTO loginDTO) throws Exception{


        ResultSet rs = getResultSetBySqlPair(
                new NIXClientTDConditionBuilder()
                        .selectID()
                        .fromNIXClientTD()
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
                        .tdFromGreaterThanEquals(TimeConverter.getDateFromString( hashtable.get("fromDate")))
                        .tdFromLessThan(TimeConverter.getNextDateFromString(hashtable.get("toDate")))
                        .getSqlPair()
        );

        return getSingleColumnListByResultSet(rs, Long.class);
    }

    public NIXClientTD getNIXClientTDByClientID(long clientID) throws Exception{
        List<NIXClientTD> clientTDList = getAllObjectList(classObject,
                new NIXClientTDConditionBuilder()
                        .Where()
                        .clientIDEquals(clientID)
                        .tdToEquals(Long.MAX_VALUE)
                        .getCondition()
        );

        return clientTDList.isEmpty()?null: clientTDList.get(0);
    }
    public List<NIXClientTD> getTDHistoryByClientID(long clientID) throws Exception {
        List<NIXClientTD> clientTDList = getAllObjectList(classObject,
                new NIXClientTDConditionBuilder()
                        .Where()
                        .clientIDEquals(clientID)
                        .getCondition()
        );

        return clientTDList;
    }


    public List<NIXClientTD> getAllTD() throws Exception{
        return getAllObjectList(NIXClientTD.class);
    }
}
