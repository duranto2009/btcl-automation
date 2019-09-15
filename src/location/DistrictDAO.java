package location;

import util.ModifiedSqlGenerator;
import static util.ModifiedSqlGenerator.getAllObjectList;
import static util.ModifiedSqlGenerator.getObjectByID;
import java.util.ArrayList;
import java.util.List;

public class DistrictDAO {

	Class<District> classObject = District.class;
	
    public List<District> getDistrict(int division) throws Exception{

        if(division>0){
            return ModifiedSqlGenerator.getAllObjectList(classObject,
                    new DistrictConditionBuilder()
                            .Where()
                            .divisionEquals(division)

                            .getCondition()
                    );

        }else{
            return ModifiedSqlGenerator.getAllObjectList(District.class);
        }

    }
    
    
    public List<District> getDistrictsByIds(List<Long> districts)throws Exception {
        List<District> list = getAllObjectList(classObject,new DistrictConditionBuilder()
                .Where()
                .idIn(districts)
                .getCondition());

        return list;
    }
    
    public District getDistrictById(Long district_id) throws Exception{
        District district = getObjectByID(classObject,district_id);
        return  district;
    }


    public List<District> getAllDistrictsByQuery (String query) throws Exception{
        List<District> list = getAllObjectList(classObject,new DistrictConditionBuilder()
                .Where()
                .nameEngBothLike(query)
                .getCondition());
        return list;
    }
}


