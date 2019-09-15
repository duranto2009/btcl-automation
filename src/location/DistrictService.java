package location;

import annotation.DAO;
import annotation.Transactional;
import common.RequestFailureException;
import util.TransactionType;


import java.util.List;

public class DistrictService {

    @DAO
    DistrictDAO districtDAO;

    @Transactional(transactionType=TransactionType.READONLY)
    public List<District> getDistrict(int division) throws Exception {
        List<District> districts= districtDAO.getDistrict(division);
        if(districts == null) {
            throw new RequestFailureException("No Division found ");
        }

        return districts;
    }
    
    @Transactional(transactionType=TransactionType.READONLY)
    public List<District> getDistrictsByIds(List<Long> districts) throws Exception{
        return districtDAO.getDistrictsByIds(districts);

    }
    
    @Transactional(transactionType=TransactionType.READONLY)
    public District getDistrictById(Long district_id)throws Exception {
        return districtDAO.getDistrictById(district_id);
    }

    @Transactional
    public List<District> getAllDistrictsByQuery(String query)throws Exception{
        return districtDAO.getAllDistrictsByQuery(query);
    }



}
