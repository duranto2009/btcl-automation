package ip.ipRegion;

import annotation.DAO;
import annotation.Transactional;
import location.District;
import location.DistrictService;
import requestMapping.Service;

import java.util.ArrayList;
import java.util.List;

public class IPRegionDistrictMappingService {


    @Service
    DistrictService districtService;

    @Service
    IPRegionService ipRegionService;

    @DAO
    IPReegionToDistrictDAO ipReegionToDistrictDAO;


    @Transactional
    public void saveDistrictToRegion(Long id, District district) throws  Exception {

        IPRegionToDistrict ipRegionToDistrict = new IPRegionToDistrict();

        List<IPRegionToDistrict> districts = ipReegionToDistrictDAO.getDistrictsByRegionId(id);

        boolean isFree = true;
        for(IPRegionToDistrict d:districts){
            if(d.getDistrict_id() == district.getId()){
                isFree = false ;
                break;
            }
        }

        if(isFree) {
            ipRegionToDistrict.setIsDeleted(0);
            ipRegionToDistrict.setDistrict_id(district.getId());
            ipRegionToDistrict.setRegion_id(id);
            ipReegionToDistrictDAO.saveRegionToDistrictMap(ipRegionToDistrict);
        }

    }


    public void saveDistrictsToRegion (Long id, List<District>districts) throws Exception {
        for(District d:districts){
            saveDistrictToRegion(id,d);
        }
    }

    @Transactional
    public List<IPRegionToDistrict> getAllAssignedDistricts() throws Exception{
        return ipReegionToDistrictDAO.getAllAssignedDistricts();
    }

    @Transactional
    public IPRegion getIPRegionByDistrict (Long districtId)throws Exception{
        IPRegionToDistrict ipRegionToDistrict = ipReegionToDistrictDAO.getIPRegionByDistrict(districtId);
        IPRegion ipRegion = ipRegionService.getIPRegionByID(ipRegionToDistrict.getRegion_id());
        List<IPRegionToDistrict>ipRegionToDistricts = ipReegionToDistrictDAO.getDistrictsByRegionId(ipRegionToDistrict.getRegion_id());
        List<District> districts = new ArrayList<>();
        for(IPRegionToDistrict d:ipRegionToDistricts ){
            District district = districtService.getDistrictById(d.getDistrict_id());
            districts.add(district);
        }
        ipRegion.setDistricts(districts);
        return  ipRegion;
    }
    @Transactional
    public IPRegion getIPRegionById(Long regionId) throws Exception{

        IPRegion ipRegion = ipRegionService.getIPRegionByID(regionId);
        List<IPRegionToDistrict>ipRegionToDistricts = ipReegionToDistrictDAO.getDistrictsByRegionId(regionId);
        List<District> districts = new ArrayList<>();
        for(IPRegionToDistrict d:ipRegionToDistricts ){
            District district = districtService.getDistrictById(d.getDistrict_id());
            districts.add(district);
        }
        ipRegion.setDistricts(districts);
        return ipRegion;
    }

    @Transactional
    public List<District> getDistrictsByRegionId(Long id) throws Exception{
        List<IPRegionToDistrict>ipRegionToDistricts = ipReegionToDistrictDAO.getDistrictsByRegionId(id);
        List<District>districts = new ArrayList<>();
        for (IPRegionToDistrict i: ipRegionToDistricts){
            District d = districtService.getDistrictById(i.getDistrict_id());
            districts.add(d);
        }
        return  districts;
    }
    @Transactional
    public int deleteDistrictFromRegion(Long districtId) throws  Exception{
        IPRegionToDistrict ipRegionToDistrict = ipReegionToDistrictDAO.getIPRegionByDistrict(districtId);
        return ipReegionToDistrictDAO.deleteDistrictFromRegion(ipRegionToDistrict);
    }

    @Transactional
    public List<IPRegion> getAllRegion()throws Exception {
        List<IPRegion>ipRegions = ipRegionService.getAllIPRegion();

        for(IPRegion ipRegion :ipRegions){
            List<IPRegionToDistrict> districts = ipReegionToDistrictDAO.getDistrictsByRegionId(ipRegion.getId());
            List<District>dList = new ArrayList<>();
            for (IPRegionToDistrict i: districts){
                District d = districtService.getDistrictById(i.getDistrict_id());
                dList.add(d);
            }
            ipRegion.setDistricts(dList);
        }
       // List<IPRegionToDistrict>ipRegionToDistricts = ipReegionToDistrictDAO.getAllRegion();

        return ipRegions;
    }
}
