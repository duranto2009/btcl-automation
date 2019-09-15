package global;

import annotation.DAO;
import annotation.Transactional;
import application.Application;
import common.ServiceDAO;
import common.StringUtils;
import common.bill.BillDTO;
import common.bill.BillService;
import databasemanager.DatabaseManagerImplementation;
import exception.ExceptionMsgCustomizer;
import exception.NoDataFoundException;
import lli.Application.AdditionalIP.LLIAdditionalIP;
import lli.Application.AdditionalIP.LLIAdditionalIPApplication;
import lli.Application.LLIApplication;
import lli.Application.LLIApplicationConditionBuilder;
import lli.demandNote.LLISingleConnectionCommonDemandNote;
import lombok.extern.log4j.Log4j;
import util.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Log4j
public class GlobalService {
    @DAO private GlobalDAO globalDAO;

    @Transactional
    public void save(Object object) throws Exception {
        globalDAO.save(object);
        log.info(object + " is saved");
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public<T> T findByPK(Class<T>classObject, long id) throws Exception {
        T object = globalDAO.findByPK(classObject, id);
        if(object == null){
            throw new NoDataFoundException("No data found with type: " + classObject + " id: " + id);
        }
//        log.info("Found " + classObject + " with id " + id);
        return object;
    }

    @Transactional
    public void update(Object object) {
        try {
            globalDAO.update(object);
            log.info(object.getClass().getCanonicalName() + " is updated");
        }catch (Exception e) {
            log.fatal(object.getClass().getCanonicalName() + " UPDATE FAILED");
            log.fatal(ExceptionMsgCustomizer.getMessage(e));
        }

    }

    @Transactional(transactionType = TransactionType.READONLY)
    public<T> List<T> getAllObjectListByCondition(Class<T> classObject, Object ... condition) throws Exception {
        return globalDAO.getAllObjectListByCondition(classObject, condition);
    }

    @Transactional(transactionType = TransactionType.PART_OF_PREVIOUS_TRANSACTION_FOR_READONLY)
    public<T> List<T> getAllObjectListByConditionForBatchOps(Class<T> classObject, Object ... condition) throws Exception {
        return globalDAO.getAllObjectListByCondition(classObject, condition);
    }


    @Transactional(transactionType = TransactionType.READONLY)
    public <T> int getCount(Class<T> classObject,  Object ... condition) {
        int count = 0;
        try {
            String sql = "SELECT COUNT( * )  FROM " + ModifiedSqlGenerator.getTableName(classObject) + (condition.length>0? condition[0]:"");

            SqlPair sqlPair = new SqlPair();
            sqlPair.sql = sql;
            sqlPair.objectList = new ArrayList<>();
            if(condition.length>1){
                for(int i=1;i<condition.length;i++){
                    sqlPair.objectList.add(i-1, condition[i]);
                }
            }


            ResultSet rs = ModifiedSqlGenerator.getResultSetBySqlPair(sqlPair);
            rs.next();
            count = rs.getInt(1);

        } catch (Exception e) {
            log.fatal(ExceptionMsgCustomizer.getMessage(e));
        }
        return count;
    }

    @SuppressWarnings("unchecked")
    @Transactional(transactionType = TransactionType.READONLY)
    public <T, U, V> Map<U, V> getMapOfCountByKey(Class<T> classObject, String columnName ,  Object ... condition) throws Exception {
        Map<U, V> map = new ConcurrentHashMap<>();

        String sql = "SELECT COUNT( * )  as count, " + columnName + " as column_name from " + ModifiedSqlGenerator.getTableName(classObject)
                + (condition.length > 0 ? condition[0] : "")
                + " GROUP BY column_name";
        SqlPair sqlPair = new SqlPair();
        sqlPair.sql = sql;
        sqlPair.objectList = new ArrayList<>();

        ResultSet rs = ModifiedSqlGenerator.getResultSetBySqlPair(sqlPair);
        while (rs.next()) {
            map.putIfAbsent((U) rs.getObject("column_name"), (V) rs.getObject("count"));
        }
        return map;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public <T, U> List<List<?>> join( Class<T> leftClass, Class<U> rightClass,
                               String leftAlias,
                               String rightAlias,
                               String leftDBColumnName,
                               String rightDBColumnName,
                               String comparisonType,
                               String joinType,
                               String fixedCondition,
                               List<Class<?>> desiredClassObjects) throws Exception {


        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(leftAlias)
                .append(".*, ")
                .append(rightAlias)
                .append(".* ")
                .append("from " )
                .append(ModifiedSqlGenerator.getTableName(leftClass))
                .append(" ")
                .append(leftAlias)
                .append(" ")
                .append(joinType)
                .append(" ")
                .append(ModifiedSqlGenerator.getTableName(rightClass))
                .append(" ")
                .append(rightAlias)
                .append(" ")
                .append("on ")
                .append(leftAlias)
                .append(".")
                .append(leftDBColumnName)
                .append(" ")
                .append(comparisonType)
                .append(" ")
                .append(rightAlias)
                .append(".")
                .append(rightDBColumnName)
                .append(" ")
                .append(fixedCondition);

        log.info(sb.toString());
        PreparedStatement ps = DatabaseConnectionFactory.getCurrentDatabaseConnection().getNewPrepareStatement(sb.toString());
        ResultSet rs = ps.executeQuery();
        List< List<?>> list = new ArrayList<>();
        for(Class<?> cl : desiredClassObjects) {
            list.add(getObjectListFromResultSet(rs, cl));
            rs.first();
        }
        return list;
    }

    public static void main(String[] args) throws Exception {
        GlobalService globalService = ServiceDAOFactory.getService(GlobalService.class);
        List<List<?>> lists =  globalService.join(
                BillDTO.class, LLISingleConnectionCommonDemandNote.class, "bill",
                "common", "blID", "lli_common_dn_parent_bill_id", "=",
                "JOIN", "WHERE bill.blID IN " + StringUtils.getCommaSeparatedString(Arrays.asList(669003, 669004, 669005, 669006, 669007)),
                Collections.singletonList(LLISingleConnectionCommonDemandNote.class)
        );
        List<LLISingleConnectionCommonDemandNote> bills = (List<LLISingleConnectionCommonDemandNote>)lists.get(0);
        BillDTO billDTO = ServiceDAOFactory.getService(BillService.class).getBillByBillID(669006);
        DatabaseManagerImplementation.getInstance().closeAllConnections();
    }

    @Transactional(transactionType = TransactionType.READONLY)
    static<T> List<T> getObjectListFromResultSet(ResultSet rs, Class<T> classObject)throws Exception {
        List<T> desiredInstances = new ArrayList<>();
        while(rs.next()) {
            T desiredInstance = classObject.newInstance();
            try{
                ModifiedSqlGenerator.populateObjectFromDB(desiredInstance, rs, classObject);
                desiredInstances.add(desiredInstance);
            }catch(Exception ex){
                log.info("fatal:",ex);
                log.info("primary key = "+rs.getObject(ModifiedSqlGenerator.getPrimaryKeyColumnName(classObject)));

            }
        }
        return desiredInstances;
    }
}
