package ip.ipUsage;

import common.StringUtils;
import ip.MethodReferences;
import util.DatabaseConnectionFactory;
import util.ModifiedSqlGenerator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

import static util.ModifiedSqlGenerator.*;

public class IPUsageDAO {
    private final Class <IPBlockUsage> classObject = IPBlockUsage.class;
    void saveIPBlockUsage(IPBlockUsage usage) throws Exception {
        ModifiedSqlGenerator.insert(usage);
    }

    List<IPBlockUsage> getAllIPBlockUsage() throws Exception {
       return ModifiedSqlGenerator.getAllObjectList(IPBlockUsage.class);

    }

    public Collection<Long> getIDsWithSearchCriteria(Hashtable<String, String> criteria) throws Exception {


        String []keys = new String[]         {"Version", "Region", "SubRegion", "Purpose", "Status", "fromDate"   , "toDate"   , "activeTo"  , "isDeleted"};
        String []operators = new String[]    {"="      , "="     , "="         , "="      ,  "="  , ">="          , "<="       , "="        , "="};
        String []columnNames = new String [] {"version", "regionId","subRegionId", "purpose", "status", "activeFrom", "activeFrom", "activeTo", "isDeleted"};
        String fixedCondition = "";
        String fromIPStr = criteria.get("fromIP");
        String toIPStr = criteria.get("toIP");

        if(!StringUtils.isBlank(fromIPStr)){
            fixedCondition += " INET_ATON ( " + getColumnName(IPBlockUsage.class, "fromIP") + " ) >= INET_ATON ( '" + fromIPStr + "' ) ";
        }
        if(!StringUtils.isBlank(toIPStr)) {
            if(!StringUtils.isBlank(fromIPStr)){
                fixedCondition += " AND ";
            }else {
                fixedCondition += " INET_ATON ( " + getColumnName(IPBlockUsage.class, "toIP") + " ) <= INET_ATON ( '" + toIPStr + "' ) ";
            }

        }
        criteria.put("activeTo", String.valueOf(Long.MAX_VALUE));
        criteria.put("isDeleted", "0");

        return ModifiedSqlGenerator.getIDListFromSearchCriteria(IPBlockUsage.class, keys, operators, columnNames, criteria, fixedCondition);


    }

    Collection getAllIPUsageByIDs(List<Long> recordIDs) throws Exception {
        return  ModifiedSqlGenerator.getAllObjectList(IPBlockUsage.class,
                new IPBlockUsageConditionBuilder()
                        .Where()
                        .idIn(recordIDs)
                        .isDeleted(false)
                        .getCondition()
        )
                .stream()
                .sorted(Comparator.comparingLong(o -> MethodReferences.getLongFromIPString.apply(o.getFromIP())))
                .collect(Collectors.toList());
    }

    List<IPBlockUsage> getIPUsageInIPRange(String fromIP, String toIP) throws Exception {
        String sql = " SELECT * from " + getTableName(classObject)
                + " WHERE "
                + "INET_ATON ( " + getColumnName(classObject, "fromIP") + " ) "
                + " >= "
                + "INET_ATON ( ? ) "
                + " AND "
                + "INET_ATON ( " + getColumnName(classObject, "toIP") + " ) "
                + " <= "
                + "INET_ATON ( ? ) "
                +" AND " + getColumnName(classObject, "isDeleted")
                + " = 0"
                + " AND " + getColumnName(classObject, "activeTo")
                + " = " + Long.MAX_VALUE
                ;
        PreparedStatement ps = DatabaseConnectionFactory.getCurrentDatabaseConnection().getNewPrepareStatement(sql);
        ps.setObject(1, fromIP);
        ps.setObject(2, toIP);

        ResultSet rs = ps.executeQuery();
        return  getObjectListByResultSet(classObject, rs);


    }

    public void update(IPBlockUsage usage) throws Exception {
        ModifiedSqlGenerator.updateEntity(usage);
    }

    public IPBlockUsage getById(long ipUsageId) throws Exception {
        return ModifiedSqlGenerator.getObjectByID(IPBlockUsage.class, ipUsageId);
    }

    void deletePublicIpUsageById(long id) throws Exception {
        ModifiedSqlGenerator.deleteEntityByID(IPBlockUsage.class, id);
    }
}
