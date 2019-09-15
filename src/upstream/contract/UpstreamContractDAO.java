package upstream.contract;

import login.LoginDTO;
import util.ModifiedSqlGenerator;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

public class UpstreamContractDAO {

    public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO) throws Exception {
        ResultSet resultSet
                = ModifiedSqlGenerator.getResultSetBySqlPair(
                new UpstreamContractConditionBuilder()
                        .selectContractId()
                        .fromUpstreamContract()
                        .Where()
                        .contractIdEqualsString((String) searchCriteria.get("contractId"))
                        .typeOfBandwidthIdEqualsString((String) searchCriteria.get("Type Of Bandwidth"))
                        .btclServiceLocationIdEqualsString((String) searchCriteria.get("BTCL Service Location"))
                        .selectedProviderIdEqualsString((String) searchCriteria.get("Selected Provider"))
                        .mediaIdEqualsString((String) searchCriteria.get("Media"))
                        .providerLocationIdEqualsString((String) searchCriteria.get("Provider Location"))
                        .orderBycontractIdDesc()
                        .getSqlPair()
        );
        return ModifiedSqlGenerator.getSingleColumnListByResultSet(resultSet, Long.class);
    }

    public List<UpstreamContract> getDTOs(List<Long> recordIDs) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(UpstreamContract.class,
                new UpstreamContractConditionBuilder()
                        .Where()
                        .contractIdIn(recordIDs)
                        .activeToGreaterThan(System.currentTimeMillis())
                        .getCondition()
        );
    }


}
