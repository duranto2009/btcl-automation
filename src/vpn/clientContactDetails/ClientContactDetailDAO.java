package vpn.clientContactDetails;

import databasemanager.DatabaseManager;
import util.ModifiedSqlGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ClientContactDetailDAO {
    public void updateClientContactDetails_Custom(ClientContactDetailsDTO clientContactDetailsDTO) {

        String sql = "UPDATE at_client_contact_details SET "+
                        " vclcName = ?, vclcLastName = ? vclcEmail = ?, vclcFaxNumber = ? "+
                        " vclcPhoneNumber = ?, vclcLandlineNumber = ? vclcCity = ?, vclcPostCode = ? "+
                        " vclcAddress = ? WHERE vclcID = ? ";

        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn =  DatabaseManager.getInstance().getConnection();
            ps = cn.prepareStatement(sql);

            ps.setString(1, clientContactDetailsDTO.getRegistrantsName());
            ps.setString(2, clientContactDetailsDTO.getRegistrantsLastName());
            ps.setString(3, clientContactDetailsDTO.getEmail());
            ps.setString(4, clientContactDetailsDTO.getFaxNumber());
            ps.setString(5, clientContactDetailsDTO.getPhoneNumber());
            ps.setString(6, clientContactDetailsDTO.getLandlineNumber());
            ps.setString(7, clientContactDetailsDTO.getCity());
            ps.setString(8, clientContactDetailsDTO.getPostCode());
            ps.setString(9, clientContactDetailsDTO.getAddress());
            ps.setLong(10, clientContactDetailsDTO.getID());

            ps.execute();
            ps.close();
            cn.close();
        }catch (Exception e){
            try {
                ps.close();
                cn.close();
                new Throwable("Update Failed");
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }


    }
    public void updateClientContactDetails(ClientContactDetailsDTO clientContactDetailsDTO)throws Exception{
        ModifiedSqlGenerator.updateEntity(clientContactDetailsDTO);
    }
}
