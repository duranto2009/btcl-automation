package ip.ipVsLLIConnection;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ip.IPBlock;
import ip.ipUsage.IPBlockUsage;
import org.bouncycastle.math.raw.Mod;
import util.DatabaseConnectionFactory;
import util.ModifiedSqlGenerator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class IPvsConnectionDAO {

    private final Class<IPvsConnection> iPvsConnectionClass = IPvsConnection.class;
    private final Class<IPBlockUsage> blockUsageClass = IPBlockUsage.class;

    public void save(IPvsConnection iPvsConnection) throws Exception{
        ModifiedSqlGenerator.insert(iPvsConnection);
    }

    public List<IPvsConnection> getIPVsConnectionByConnectionId(long connectionId) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(IPvsConnection.class, new IPvsConnectionConditionBuilder()
                .Where()
                .connectionIdEquals(connectionId)
                .getCondition()

        );
    }

    public JsonArray getIPBlocksByConnectionId(long connectionId) throws Exception {
        String sql = "SELECT u." + ModifiedSqlGenerator.getColumnName(blockUsageClass, "fromIP") + " as fromIP,"
                + " u." + ModifiedSqlGenerator.getColumnName(blockUsageClass, "toIP") + " as toIP,"
                + " u." + ModifiedSqlGenerator.getColumnName(blockUsageClass, "version") + " as version,"
                + " u." + ModifiedSqlGenerator.getColumnName(blockUsageClass, "type") + " as type,"
                + " u." + ModifiedSqlGenerator.getColumnName(blockUsageClass, "purpose") + " as purpose,"
//                + " u." + ModifiedSqlGenerator.getColumnName(blockUsageClass, "status") + " as status,"
                + " u." + ModifiedSqlGenerator.getColumnName(blockUsageClass, "regionId") + " as regionId,"
                + " u." + ModifiedSqlGenerator.getColumnName(blockUsageClass, "subRegionId") + " as subRegionId,"
                + " u." + ModifiedSqlGenerator.getColumnName(blockUsageClass, "activeFrom") + " as activeFrom,"
                + " u." + ModifiedSqlGenerator.getColumnName(blockUsageClass, "id") + " as usageId,"
                + " c." + ModifiedSqlGenerator.getColumnName(iPvsConnectionClass, "usageType") + " as usageType"
                + " from " + ModifiedSqlGenerator.getTableName(blockUsageClass) + " u"
                + " , " + ModifiedSqlGenerator.getTableName(iPvsConnectionClass) + " c"
                + " WHERE"
                + " c." + ModifiedSqlGenerator.getColumnName(iPvsConnectionClass, "connectionId") + " = ?"
                + " and"
                + " u." + ModifiedSqlGenerator.getColumnName(blockUsageClass, "id") + " = " + "c." + ModifiedSqlGenerator.getColumnName(iPvsConnectionClass, "ipUsageId")
                + " and "
                + " u." + ModifiedSqlGenerator.getColumnName(blockUsageClass, "activeTo") + " = " + Long.MAX_VALUE
                + " and"
                + " u." + ModifiedSqlGenerator.getColumnName(blockUsageClass, "isDeleted") + " = 0";
//        log.info(sql);
        PreparedStatement ps = DatabaseConnectionFactory.getCurrentDatabaseConnection().getNewPrepareStatement(sql);
        ps.setObject(1, connectionId);
        ResultSet rs = ps.executeQuery();
        JsonArray jsonArray = new JsonArray();
        while(rs.next()){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("usageId", rs.getLong("usageId"));
            jsonObject.addProperty("fromIP", rs.getString("fromIP"));
            jsonObject.addProperty("toIP", rs.getString("toIP"));
            jsonObject.addProperty("version", rs.getString("version"));
            jsonObject.addProperty("type", rs.getString("type"));
            jsonObject.addProperty("purpose", rs.getString("purpose"));
//            jsonObject.addProperty("status", rs.getString("status"));
            jsonObject.addProperty("regionId", rs.getString("regionId"));
            jsonObject.addProperty("subRegionId", rs.getString("subRegionId"));
            jsonObject.addProperty("activeFrom", rs.getString("activeFrom"));
            jsonObject.addProperty("usageType", rs.getString("usageType"));


            jsonArray.add(jsonObject);

        }
        return jsonArray;
    }

    public void update(IPvsConnection iPvsConnection) throws Exception {
        ModifiedSqlGenerator.updateEntity(iPvsConnection);
    }

    public IPvsConnection getIPVsConnectionByUsageId(long usageId)throws Exception {
        List<IPvsConnection>list= ModifiedSqlGenerator.getAllObjectList(IPvsConnection.class, new IPvsConnectionConditionBuilder()
                .Where()
                .ipUsageIdEquals(usageId)
                .getCondition()

        );
        if(list.size()>0)return list.get(0);
        return null;
    }

    public List<IPvsConnection> getByConnectioNId(long connectionId) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(IPvsConnection.class,
                new IPvsConnectionConditionBuilder()
                .Where()
                .connectionIdEquals(connectionId)
                        .isDeleted(false)
                .getCondition()
        );
    }
}
