package location;

import util.ModifiedSqlGenerator;

import java.util.ArrayList;
import java.util.List;

public class DivisionDAO {


    public List<Division> getDivision(Long id) throws Exception{

        List<Long> ids=new ArrayList<>();
        ids.add(id);
        if(id>0){
            return ModifiedSqlGenerator.getAllObjectListFullyPopulatedByIDList(Division.class,ids);
        }else{
            return ModifiedSqlGenerator.getAllObjectList(Division.class);
        }

    }
}


