package location;

import util.ModifiedSqlGenerator;

import java.util.List;

public class AreaDAO {
    public List<Area> getArea(long zone) throws Exception{

        if(zone>0){
            return ModifiedSqlGenerator.getAllObjectList(Area.class,
                    new AreaConditionBuilder()
                            .Where()
                            .zoneIdEquals(zone)

                            .getCondition()
            );

        }else{
            return ModifiedSqlGenerator.getAllObjectList(Area.class);
        }


    }
}
