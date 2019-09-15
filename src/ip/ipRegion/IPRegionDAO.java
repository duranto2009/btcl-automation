package ip.ipRegion;

import util.ModifiedSqlGenerator;

import java.util.List;

import static util.ModifiedSqlGenerator.getAllObjectList;
import static util.ModifiedSqlGenerator.getObjectByID;

public class IPRegionDAO {

    Class<IPRegion> classObject = IPRegion.class;


    public void insertItem(IPRegion object) throws Exception {
        ModifiedSqlGenerator.insert(object);
    }

    public void updateItem(IPRegion object) throws Exception {
        ModifiedSqlGenerator.updateEntity(object);
    }

    public List<IPRegion> getDTOs(List<Long> recordIDs) throws Exception {

        List<IPRegion> list = getAllObjectList(classObject, new IPRegionConditionBuilder()
                .Where()
                .idIn(recordIDs)
                .getCondition());

        return list;
    }

    public List<IPRegion> getAllIPRegion() throws Exception {
        List<IPRegion> list = getAllObjectList(classObject);
        return list;
    }

    public List<IPRegion> getIPRegionByAvailability(boolean b) throws Exception {
        List<IPRegion> list = getAllObjectList(classObject, new IPRegionConditionBuilder()
                .Where()
                .availability(b)
                .getCondition());
        return list;
    }

    public List<IPRegion> getRegionsBySearchString(String query) throws Exception {
        List<IPRegion> list = getAllObjectList(classObject, new IPRegionConditionBuilder()
                .Where()
                .name_enBothLike(query)
                .getCondition());
        return list;
    }

    public IPRegion getIPRegionByID(Long region_id) throws Exception {
        IPRegion ipRegion = getObjectByID(classObject, region_id);
        return ipRegion;
    }

    public List<IPRegion> getAllIPRegionByQuery(String query) throws Exception {
        List<IPRegion> list = ModifiedSqlGenerator.getAllObjectList(IPRegion.class,
                new IPRegionConditionBuilder()
                        .Where()
                        .name_enBothLike(query)
                        .getCondition());

        return list;
    }
}
