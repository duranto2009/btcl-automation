package ip.IPInventory;

import common.StringUtils;
import ip.IPConstants;
import ip.MethodReferences;
import util.ModifiedSqlGenerator;

import java.util.Collection;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

public class IPInventoryDAO {

    public void saveIPBlockInventory(IPBlockInventory block) throws Exception {
        ModifiedSqlGenerator.insert(block);
    }

    public IPBlockInventory getOneById(long id) throws Exception {
        return ModifiedSqlGenerator.getObjectByID(IPBlockInventory.class, id);
    }

    public List<IPBlockInventory> getAllValidIPBlocksFromInventoryByCriteria(long regionId, IPConstants.Version version, IPConstants.Type type, int moduleId) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(IPBlockInventory.class, new IPBlockInventoryConditionBuilder()
                .Where()
                .isDeleted(false)
                .activeToEquals(Long.MAX_VALUE)
                .regionIdEquals(regionId)
                .versionLike(version.name())
                .typeLike(type.name())
                .moduleIdEquals(moduleId)
                //.limit(20)
                //.orderByfromIPAsc()
                .getCondition());

    }

    public List<Long> getIDsWithSearchCriteria(Hashtable<String, String> searchCriteria) throws Exception {
        String []keys = new String[]         {"Version", "Type", "Region", "isDeleted" , "activeTo"  };
        String []operators = new String[]    {"="      , "="   , "="     , "="         , "="};
        String []columnNames = new String [] {"version", "type", "regionId", "isDeleted", "activeTo"};
        String fixedCondition = "";
        String ip = searchCriteria.get("ip");
        if(!StringUtils.isBlank(ip)) {
            fixedCondition += " INET_ATON ( '" + ip + "' ) >= " + "INET_ATON ( " + ModifiedSqlGenerator.getColumnName(
                    IPBlockInventory.class, "fromIP") + " ) AND INET_ATON ( '" + ip + "' ) <= INET_ATON ( " +
                    ModifiedSqlGenerator.getColumnName(IPBlockInventory.class, "toIP") + " ) ";
        }
        searchCriteria.put("isDeleted", "0");
        searchCriteria.put("activeTo", String.valueOf(Long.MAX_VALUE));

        return ModifiedSqlGenerator.getIDListFromSearchCriteria(IPBlockInventory.class, keys, operators, columnNames, searchCriteria, fixedCondition);
    }

    public List<IPBlockInventory> getAllInventoriesByIds(List<Long> recordIDs) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(IPBlockInventory.class,
                new IPBlockInventoryConditionBuilder()
                .Where()
                .idIn(recordIDs)
                .getCondition()
                )

                .stream()
                .sorted(Comparator.comparingLong(t->MethodReferences.getLongFromIPString.apply(t.getFromIP())))
                .collect(Collectors.toList());

    }
}
