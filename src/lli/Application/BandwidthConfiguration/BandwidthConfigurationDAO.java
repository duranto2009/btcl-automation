package lli.Application.BandwidthConfiguration;

import databasemanager.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BandwidthConfigurationDAO {
    public ArrayList<BandwidthConfiguration> getBandwidthConfiguration(long bandwidth)throws Exception{
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        Connection cn=null;
        ArrayList<Integer> zoneIds=new ArrayList<>();
        ArrayList<BandwidthConfiguration> bandwidthConfiguration = new ArrayList<>();

        String sql="SELECT  * FROM at_lli_bandwidth_configuration order by abs(bandwidth - ?) asc limit 1";
        try{
            cn= DatabaseManager.getInstance().getConnection();
            preparedStatement=cn.prepareStatement(sql);
            preparedStatement.setLong(1,bandwidth);
            resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                BandwidthConfiguration bc  = BandwidthConfiguration.builder()
                        .id(resultSet.getLong("id"))
                        .bandwidth(resultSet.getLong("bandwidth"))
                        .duration(resultSet.getLong("duration"))
                        .build();
                bandwidthConfiguration.add(bc);
            }
        }catch (Exception e){
            e.printStackTrace();

        }finally {
            if(cn!=null){cn.close();}
            if(preparedStatement!=null){preparedStatement.close();}
            if(resultSet!=null){resultSet.close();}
        }
        return bandwidthConfiguration;
    }
}
