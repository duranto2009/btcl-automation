package location;

import connection.DatabaseConnection;
import databasemanager.DatabaseManager;
import lombok.extern.log4j.Log4j;
import user.UserDTO;
import util.DatabaseConnectionFactory;
import util.ModifiedSqlGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class ZoneDAO {


    public List<Zone> getZone(int district) throws Exception{

        if(district>0){


            ArrayList<Integer> zoneIds=getZoneIDByDistrictID(district);

            return ModifiedSqlGenerator.getAllObjectList(Zone.class,
                    new ZoneConditionBuilder()
                            .Where()
                            .idIn(zoneIds)

                            .getCondition()
            );

        }else{
            return ModifiedSqlGenerator.getAllObjectList(Zone.class);
        }

    }

    public  List<Zone> getZoneByID(int id) throws Exception{



           List<Zone> zones= ModifiedSqlGenerator.getAllObjectList(Zone.class,
                    new ZoneConditionBuilder()
                            .Where()
                            .idEquals((long) id)

                            .getCondition()
            );

           return zones;


    }

    /**
     * Added By forhad
     * @method getAllZone
     * @param
     * @return All zones
     * @throws Exception
     */
    public  List<Zone> getAllZone() throws Exception{
        List<Zone> zones= ModifiedSqlGenerator.getAllObjectList(Zone.class);
        return zones;
    }
    /** end of getAllZone() **/


    public List<Zone> getZoneByChar(String search) throws Exception{


            return ModifiedSqlGenerator.getAllObjectList(Zone.class,
                    new ZoneConditionBuilder()
                            .Where()
                            .nameEngBothLike(search)

                            .getCondition()
            );



    }

    public ArrayList<Integer> getZoneIDByDistrictID(int districtId)throws Exception{
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        Connection cn=null;
        ArrayList<Integer> zoneIds=new ArrayList<>();
        String sql="Select zone_id from geo_zone_district_mapping where district_id= ? ";
        try{
            cn=DatabaseManager.getInstance().getConnection();
            preparedStatement=cn.prepareStatement(sql);
            preparedStatement.setInt(1,districtId);
            resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                zoneIds.add(resultSet.getInt("zone_id"));
            }
        }catch (Exception e){
            e.printStackTrace();

        }finally {
            if(cn!=null){cn.close();}
            if(preparedStatement!=null){preparedStatement.close();}
            if(resultSet!=null){resultSet.close();}
        }
        return zoneIds;
    }

    public ArrayList<Integer> getUserZone(int userId)throws Exception{
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        Connection cn=null;
        ArrayList<Integer> zoneIds=new ArrayList<>();
        String sql="Select zone_id from geo_zone_user_mapping where user_id = ? ";
        try{
            cn=DatabaseManager.getInstance().getConnection();
            preparedStatement=cn.prepareStatement(sql);
            preparedStatement.setInt(1,userId);
            resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                zoneIds.add(resultSet.getInt("zone_id"));
            }
        }catch (Exception e){
            e.printStackTrace();

        }finally {
            if(cn!=null){cn.close();}
            if(preparedStatement!=null){preparedStatement.close();}
            if(resultSet!=null){resultSet.close();}
        }
        return zoneIds;
    }

    public void setZonesForUser(long userId, List<Zone>zones) throws Exception{

        DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
        String sql = "insert into geo_zone_user_mapping (id, zone_id, user_id) values (?,?,?)";

        PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);
        for(Zone zone: zones)
        {
            int k = 1;
            ps.setLong(k++, databaseConnection.getNextID("geo_zone_district_mapping"));
            ps.setLong(k++,zone.getId());
            ps.setLong(k++, userId);
            ps.addBatch();
        }
        log.info(ps);
        ps.executeBatch();
        ps.close();
    }


    public void deleteZoneForUser(long userId, long zoneId) throws Exception{
        DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
        String sql = "delete from geo_zone_user_mapping where zone_id =? and  user_id = ?";

        PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);
        int k = 1;
        ps.setLong(k++,zoneId);
        ps.setLong(k++, userId);
        ps.addBatch();
        log.info(ps);
        ps.executeBatch();
        ps.close();
    }
}
