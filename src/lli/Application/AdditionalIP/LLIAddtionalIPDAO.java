package lli.Application.AdditionalIP;

import util.ModifiedSqlGenerator;

import java.util.List;

public class LLIAddtionalIPDAO {
    public void insertAdditionalIPApplication(LLIAdditionalIP additionalIP)throws Exception {

        ModifiedSqlGenerator.insert(additionalIP);
    }

    public LLIAdditionalIP getAdditionalIPByApplication(long applicationID)throws Exception {
        List<LLIAdditionalIP> lLIAdditionalIP= ModifiedSqlGenerator.getAllObjectList(LLIAdditionalIP.class,
                new LLIAdditionalIPConditionBuilder()
                        .Where()
                        .applicationIdEquals(applicationID)

                        .getCondition()
        );
        if(lLIAdditionalIP.size()>0){
           return lLIAdditionalIP.get(0);
        }
        return null;
    }
}
