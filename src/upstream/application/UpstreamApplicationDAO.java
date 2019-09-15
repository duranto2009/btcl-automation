package upstream.application;

import common.ClientDTO;
import common.repository.AllClientRepository;
import login.LoginDTO;
import util.ModifiedSqlGenerator;
import util.TimeConverter;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

public class UpstreamApplicationDAO {

    public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO) throws Exception {
        ResultSet resultSet
                = ModifiedSqlGenerator.getResultSetBySqlPair(
                new UpstreamApplicationConditionBuilder()
                        .selectApplicationId()
                        .fromUpstreamApplication()
                        .Where()
                        .applicationIdEqualsString((String) searchCriteria.get("applicationId"))
                        .typeOfBandwidthIdEqualsString((String) searchCriteria.get("Type Of Bandwidth"))
                        .btclServiceLocationIdEqualsString((String) searchCriteria.get("BTCL Service Location"))
                        .selectedProviderIdEqualsString((String) searchCriteria.get("Selected Provider"))
                        .mediaIdEqualsString((String) searchCriteria.get("Media"))
                        .providerLocationIdEqualsString((String) searchCriteria.get("Provider Location"))
//                        .submissionDateGreaterThanEquals(TimeConverter.getDateFromString(criteria.get("fromDate")))
//                        .submissionDateLessThanEquals(TimeConverter.getDateFromString(criteria.get("toDate")))
//                        .applicationStatusEquals((String) searchCriteria.get("applicationStatus"))
                        .orderByapplicationIdDesc()
                        .getSqlPair()
        );
        return ModifiedSqlGenerator.getSingleColumnListByResultSet(resultSet, Long.class);
    }

    public List<UpstreamApplication> getDTOs(List<Long> recordIDs) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(UpstreamApplication.class,
                new UpstreamApplicationConditionBuilder()
                        .Where()
                        .applicationIdIn(recordIDs)
                        .getCondition()

        );
    }


}
