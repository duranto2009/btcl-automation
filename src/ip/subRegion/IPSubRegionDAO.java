package ip.subRegion;

//import javafx.collections.ModifiableObservableListBase;

import common.ObjectPair;
import ip.ipRegion.IPRegion;
import util.ModifiedSqlGenerator;

import java.util.ArrayList;
import java.util.List;

public class IPSubRegionDAO {

    public void insert(IPSubRegion ipSubRegion) throws Exception {
        ModifiedSqlGenerator.insert(ipSubRegion);
    }

    public List<IPSubRegion> getAllSubRegions() throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(IPSubRegion.class);
    }

    public List<IPSubRegion> getAllSubRegionsByRegionId(long regionId) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(IPSubRegion.class, " Where " +
                ModifiedSqlGenerator.getColumnName(IPSubRegion.class, "regionId") +
                " = " +
                regionId
        );
    }

    public List<ObjectPair<String, String>> getAllSubRegionsWithParentRegion() throws Exception {
        List<IPSubRegion> subRegions = this.getAllSubRegions();

        List<ObjectPair<String, String>> list = new ArrayList<>();

        for (IPSubRegion subRegion : subRegions) {
            IPRegion ipRegion = ModifiedSqlGenerator.getObjectByID(IPRegion.class, subRegion.getRegionId());
            ObjectPair<String, String> pair = new ObjectPair<>(subRegion.getSubRegionName(), ipRegion.getName_en());
            list.add(pair);
        }
        return list;
    }
}
