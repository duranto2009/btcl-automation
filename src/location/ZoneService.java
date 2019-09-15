package location;

import annotation.DAO;
import annotation.Transactional;
import common.RequestFailureException;
import user.UserDTO;
import util.TransactionType;

import java.util.List;

public class ZoneService {

    @DAO
    ZoneDAO zoneDAO;

    @Transactional(transactionType=TransactionType.READONLY)
    public List<Zone> getZone(int district) throws Exception {
        List<Zone> zones= zoneDAO.getZone(district);
        if(zones == null) {
            throw new RequestFailureException("No Zone found ");
        }

        return zones;
    }


    public List<Integer> getUserZone(int userId)throws Exception{
        List<Integer> zones= zoneDAO.getUserZone(userId);
        if(zones == null) {
            throw new RequestFailureException("No Zone found ");
        }

        return zones;
    }

    @Transactional(transactionType=TransactionType.READONLY)
    public Zone getZonebyId(int id) throws Exception {
        List<Zone> zones= zoneDAO.getZoneByID(id);
        if(zones == null) {
            throw new RequestFailureException("No Zone found ");
        }

        if(zones.size()>0){

            return zones.get(0);
        }else{
            return new Zone();
        }
    }
    @Transactional(transactionType=TransactionType.READONLY)
    public List<Zone> getZoneByChar(String search) throws Exception {
        List<Zone> zones= zoneDAO.getZoneByChar(search);
        if(zones == null) {
            throw new RequestFailureException("No Zone found ");
        }

        return zones;
    }

    /**
     * start: getAllZone
     */
    @Transactional(transactionType=TransactionType.READONLY)
    public List<Zone> getAllZone() throws Exception {
        List<Zone> zones= zoneDAO.getAllZone();
        if(zones == null) {
            throw new RequestFailureException("No Zone found ");
        }
        return zones;
    }
    @Transactional
    public void setZonesForUser(long userId, List<Zone>zones) throws Exception{
        zoneDAO.setZonesForUser(userId,zones);
    }

    @Transactional
    public void deleteZoneForUser(long userId, long zoneId) throws Exception{
        zoneDAO.deleteZoneForUser(userId,zoneId);
    }

    /**
     * end: getAllZone
     */
}

