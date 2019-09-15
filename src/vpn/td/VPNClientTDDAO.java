package vpn.td;

import common.ClientDTOConditionBuilder;
import login.LoginDTO;
import util.TimeConverter;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.List;
import static util.ModifiedSqlGenerator.*;

public class VPNClientTDDAO {


    Class<VPNClientTD> classObject = VPNClientTD.class;

    public List<VPNClientTD> getDTOListByIDList(List<Long> idList) throws Exception{
        return getObjectListByIDList(classObject, idList);
    }
    public void updateClientTD(VPNClientTD vpnClientTD) throws Exception{
        updateEntity(vpnClientTD);
    }
    public void insertClientTD(VPNClientTD vpnClientTD) throws Exception{
        insert(vpnClientTD);
    }
    public boolean existsAnyClientTDByClientID(long clientID) throws Exception{

        ResultSet rs = getResultSetBySqlPair(
                new VPNClientTDConditionBuilder()
                        .selectID()
                        .fromVPNClientTD()
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
                new VPNClientTDConditionBuilder()
                        .selectID()
                        .fromVPNClientTD()
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

    public VPNClientTD getVPNClientTDByClientID(long clientID) throws Exception{
        List<VPNClientTD> clientTDList = getAllObjectList(classObject,
                new VPNClientTDConditionBuilder()
                        .Where()
                        .clientIDEquals(clientID)
                        .tdToEquals(Long.MAX_VALUE)
                        .getCondition()
        );

        return clientTDList.isEmpty()?null: clientTDList.get(0);
    }
    public List<VPNClientTD> getTDHistoryByClientID(long clientID) throws Exception {
        List<VPNClientTD> clientTDList = getAllObjectList(classObject,
                new VPNClientTDConditionBuilder()
                        .Where()
                        .clientIDEquals(clientID)
                        .getCondition()
        );

        return clientTDList;
    }
}
