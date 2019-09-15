package lli.Application.FlowConnectionManager;
import util.ModifiedSqlGenerator;

import java.util.ArrayList;
import java.util.List;

import static util.ModifiedSqlGenerator.insert;

public class LLIFlowConnectionDAO {

    public LLIConnection getConnectionByConnectionID(long connectionID) throws Exception{
        List<LLIConnection> connection=ModifiedSqlGenerator.getAllObjectList(LLIConnection.class,
                new LLIConnectionConditionBuilder()
                        .Where()
                        .IDEquals(connectionID)
                        .activeToGreaterThan(System.currentTimeMillis())
                        .orderByhistoryIDDesc()
                        .getCondition()
        );
        if(connection.size()>0){
            return connection.get(0);
        }else{
            return new LLIConnection(); // ????
        }

    }


    public List<LLIConnection> getConnectionByClient(long clientId) throws Exception{


        List<LLIConnection> connections=new ArrayList<>();
         connections=ModifiedSqlGenerator.getAllObjectList(LLIConnection.class,
                new LLIConnectionConditionBuilder()
                        .Where()
                        .clientIDEquals(clientId)
                        .activeToGreaterThan(System.currentTimeMillis())
                        .orderByhistoryIDDesc()
                        .getCondition()
        );
            return connections;
    }

    public LLIConnection getLastDeactivatedConnectionByConnectionID(long connectionID) throws Exception{
        List<LLIConnection> connection=ModifiedSqlGenerator.getAllObjectList(LLIConnection.class,
                new LLIConnectionConditionBuilder()
                        .Where()
                        .IDEquals(connectionID)
                        .activeToLessThan(System.currentTimeMillis())
                        .orderByhistoryIDDesc()
                        .limit(1)
                        .getCondition()
        );
        if(connection.size()>0){
            return connection.get(0);
        }else{
            return new LLIConnection();
        }

    }


//    public LLIConnection getLastConnectionByConnectionID(long connectionID) throws Exception{
//        List<LLIConnection> connection=ModifiedSqlGenerator.getAllObjectList(LLIConnection.class,
//                new LLIConnectionConditionBuilder()
//                        .Where()
//                        .IDEquals(
//
//                        )
//                        .orderByIDDesc()
//                        .getCondition()
//        );
//        if(connection.size()>0){
//            return connection.get(0);
//        }else{
//            return new LLIConnection();
//        }
//
//    }


    public void insertLLIConnectionFlow(LLIConnection lliConnection) throws Exception{
        insert(lliConnection);
    }

    public void updateLLIConnection(LLIConnection lliConnection) throws Exception{
        ModifiedSqlGenerator.updateEntity(lliConnection);
    }

    public void insertLLIConnection(LLIConnection lliConnection) throws Exception{
        ModifiedSqlGenerator.insert(lliConnection);
    }


}
