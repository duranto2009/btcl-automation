package vpn.td;

import common.ClientDTOConditionBuilder;
import login.LoginDTO;
import util.ModifiedSqlGenerator;
import util.TimeConverter;
import vpn.VPNConstants;

import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.List;

import static util.ModifiedSqlGenerator.*;

public class VPNProbableTDDAO {
    Class<VPNProbableTD> classObject = VPNProbableTD.class;

    public List<VPNProbableTD> getDTOListByIDList(List<Long> idList) throws Exception{
        return getObjectListByIDList(classObject, idList);
    }

    public List<Long> getIDsWithSearchCriteria(Hashtable<String, String> hashtable,LoginDTO loginDTO) throws Exception{

        ResultSet rs = getResultSetBySqlPair(
                new VPNProbableTDConditionBuilder()
                        .selectID()
                        .fromVPNProbableTD()
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
                        .isCompletedEquals(VPNConstants.TD_STATE)
                        .orderBytdDateAsc()
                        .getSqlPair()
        );

        return getSingleColumnListByResultSet(rs, Long.class);
    }

    public VPNProbableTD getVPNProbableTDClientByClientID(long clientID) throws Exception {
        List<VPNProbableTD> vpnProbableTDClientList = getAllObjectList(classObject,
                new VPNProbableTDConditionBuilder()
                        .Where()
                        .clientIDEquals(clientID)
                        .getCondition()
        );

        return vpnProbableTDClientList.isEmpty()?null: vpnProbableTDClientList.get(0);
    }
    public void insertVPNProbableTDClient(VPNProbableTD vpnProbableTD) throws Exception {
        ModifiedSqlGenerator.insert(vpnProbableTD);
    }
    public void updateVPNProbableTDClient(VPNProbableTD vpnProbableTD) throws Exception {
        ModifiedSqlGenerator.updateEntity(vpnProbableTD);
    }

    public List<VPNProbableTD> getProbableTDByClientID(long clientId)throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(VPNProbableTD.class,new VPNProbableTDConditionBuilder()
                .Where()
                .clientIDEquals(clientId)
                .getCondition());
    }
}
