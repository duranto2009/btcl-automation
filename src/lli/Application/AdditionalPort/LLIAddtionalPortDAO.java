package lli.Application.AdditionalPort;

import lli.Application.AdditionalIP.LLIAdditionalIP;
import util.ModifiedSqlGenerator;

import java.util.ArrayList;
import java.util.List;

import static util.ModifiedSqlGenerator.getAllObjectList;

public class LLIAddtionalPortDAO {
    public void insertAdditionalPortApplication(AdditionalPort additionalPort)throws Exception {

        ModifiedSqlGenerator.insert(additionalPort);
    }

    public AdditionalPort getAdditionalPortByApplication(long applicationID)throws Exception {
        List<AdditionalPort> lLIAdditionalPort= getAllObjectList(AdditionalPort.class,
              new AdditionalPortConditionBuilder()
                        .Where()
                        .applicationIdEquals(applicationID)
                        .getCondition()
        );
        if(lLIAdditionalPort.size()>0){
            return lLIAdditionalPort.get(0);
        }
        return null;
    }
}
