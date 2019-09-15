package ip.ipRegion;

import annotation.DAO;
import annotation.Transactional;
import login.LoginDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import requestMapping.Service;
import util.NavigationService;
import util.TransactionType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

public class IPRegionService{

    public Logger logger = LoggerFactory.getLogger(this.getClass());



    @DAO
    IPRegionDAO ipRegionDAO;
    @Service
    IPRegionDistrictMappingService ipRegionDistrictMappingService;


    @Transactional
    public void save(IPRegion object) {

        try {
            if(object.getId() == null || object.getId() == 0)
                ipRegionDAO.insertItem(object);
            else
                ipRegionDAO.updateItem(object);

        } catch (Exception e) {
            logger.error("error while saving ", e);
        }
        return;
    }

    @Transactional
    public List<IPRegion> getAllIPRegion() {
        try {
            return ipRegionDAO.getAllIPRegion();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Transactional
    public List<IPRegion> getRegionsBySearchString(String query)throws Exception{

        return ipRegionDAO.getRegionsBySearchString(query);
    }
    @Transactional
    public IPRegion getIPRegionByID(Long region_id)throws Exception {
       return ipRegionDAO.getIPRegionByID(region_id);
    }

    @Transactional()
    public List<IPRegion> getAllIPRegionByQuery(String query) throws Exception {
        return ipRegionDAO.getAllIPRegionByQuery(query);
    }
}
