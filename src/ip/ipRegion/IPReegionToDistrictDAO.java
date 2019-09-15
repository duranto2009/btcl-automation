package ip.ipRegion;

import util.ModifiedSqlGenerator;

import java.util.List;

import static util.ModifiedSqlGenerator.getAllObjectList;
import static util.ModifiedSqlGenerator.deleteEntity;

public class IPReegionToDistrictDAO {

    Class<IPRegionToDistrict> classObject = IPRegionToDistrict.class;

    public void saveRegionToDistrictMap(IPRegionToDistrict ipRegionToDistrict) throws Exception{

        ModifiedSqlGenerator.insert(ipRegionToDistrict);

    }

    public List<IPRegionToDistrict> getDistrictsByRegionId(Long regionId)throws  Exception{
        List<IPRegionToDistrict> list = getAllObjectList(classObject,new IPRegionToDistrictConditionBuilder()
        .Where()
        .isDeletedEqualsString(String.valueOf(0))
        .region_idEquals(regionId)
        .getCondition());

        return list;
    }



    public List<IPRegionToDistrict> getAllAssignedDistricts() throws  Exception {
        List<IPRegionToDistrict> list = getAllObjectList(classObject,new IPRegionToDistrictConditionBuilder()
        .Where()
        .isDeletedEqualsString(String.valueOf(0))
        .getCondition());
        return  list;
    }

    public IPRegionToDistrict getIPRegionByDistrict(Long districtId) throws Exception {

        List<IPRegionToDistrict> list = getAllObjectList(classObject,new IPRegionToDistrictConditionBuilder()
                .Where()
                .isDeletedEqualsString(String.valueOf(0))
                .district_idEquals(districtId)
                .getCondition());

        if(list.isEmpty()){
            return null;
        }
        return (IPRegionToDistrict) list.get(0);

    }

    public int deleteDistrictFromRegion(IPRegionToDistrict deletedObject)throws Exception {
        return deleteEntity(deletedObject,classObject,false);
    }
}
