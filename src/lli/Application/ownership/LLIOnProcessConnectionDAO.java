package lli.Application.ownership;

import util.ModifiedSqlGenerator;

import java.util.List;

public class LLIOnProcessConnectionDAO {

    public List<LLIOnProcessConnection> getConnectionByAppId(long id) throws Exception{
        return ModifiedSqlGenerator.getAllObjectList(LLIOnProcessConnection.class,
                new LLIOnProcessConnectionConditionBuilder()
                    .Where()
                    .applicationEquals(id)
                    .getCondition()
        );
    }
}
