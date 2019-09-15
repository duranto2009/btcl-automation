package ip;

import annotation.ForwardedAction;
import annotation.JsonPost;
import client.module.ClientModuleDTO;
import client.module.ClientModuleService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import common.ObjectPair;
import common.RequestFailureException;
import exception.NoDataFoundException;
import ip.IPInventory.IPBlockInventory;
import ip.IPInventory.IPInventoryService;
import ip.ipRegion.IPRegion;
import ip.ipRegion.IPRegionDistrictMappingService;
import ip.ipRegion.IPRegionService;
import ip.ipRegion.IPRegionToDistrict;
import ip.ipRouting.IPRoutingInfo;
import ip.ipUsage.IPBlockUsage;
import ip.ipUsage.IPUsageService;
import ip.privateIP.PrivateIPBlock;
import ip.privateIP.PrivateIPService;
import ip.subRegion.IPSubRegion;
import location.District;
import location.DistrictService;
import login.LoginDTO;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import sessionmanager.SessionConstants;
import util.KeyValuePair;
import util.RecordNavigationManager;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@ActionRequestMapping("ip/")
public class IPAction extends AnnotatedRequestMappingAction {

    @Service
    ClientModuleService clientModuleService;

    @Service
    IPInventoryService ipInventoryService;

    @Service
    IPRegionService ipRegionService;

    @Service
    DistrictService districtService;

    @Service
    IPRegionDistrictMappingService ipRegionDistrictMappingService;

    @Service
    IPService ipService;

    @Service
    IPUsageService ipUsageService;

    @Service
    PrivateIPService privateIPService;

    /***
     * NAT
     */
    @ForwardedAction
    @RequestMapping(mapping = "inventory/nat", requestMethod = RequestMethod.GET)
    public String getNATAssignmentPage() {
        return "assign-nat-page";
    }

    /***
     * Private IP
     */

    @JsonPost
    @RequestMapping(mapping = "privateIP/add", requestMethod = RequestMethod.POST)
    public void addNewPrivateIP(@RequestParameter(value = "block", isJsonBody = true) PrivateIPBlock block) throws Exception {
        ipService.addNewPrivateIP(block);
    }

    @RequestMapping(mapping = "privateIP/getPrivateIPsBySubRegionId", requestMethod = RequestMethod.GET)
    public List<PrivateIPBlock> getIPsBySubRegionId(@RequestParameter("subRegionId") long subRegionId) throws Exception {
        return privateIPService.getPrivateIPsBySubRegionId(subRegionId);
    }

    @RequestMapping(mapping = "privateIP/getPrivateIPsByModuleId", requestMethod = RequestMethod.GET)
    public List<PrivateIPBlock> getIPsByModuleId(@RequestParameter("moduleId") int moduleId) throws Exception {
        return privateIPService.getPrivateIPsByModuleId(moduleId);
    }

    @RequestMapping(mapping = "privateIP/getPrivateIPsBySubRegionIdAndModuleId", requestMethod = RequestMethod.GET)
    public List<PrivateIPBlock> getPrivateIPsBySubRegionIdAndModuleId(@RequestParameter("subRegionId") long subRegionId, @RequestParameter("moduleId") int moduleId) throws Exception {
        return privateIPService.getPrivateIPsBySubRegionIdAndModuleId(subRegionId, moduleId);
    }

    @RequestMapping(mapping = "privateIP/deletePrivateIPsBySubRegionId", requestMethod = RequestMethod.GET)
    public void deleteIPsBySubRegionId(@RequestParameter("subRegionId") long subRegionId) throws Exception {
        privateIPService.deletePrivateIPsBySubRegionId(subRegionId);
    }

    @RequestMapping(mapping = "privateIP/deletePrivateIPsByModuleId", requestMethod = RequestMethod.GET)
    public void deletePrivateIPsByModuleId(@RequestParameter("moduleId") int moduleId) throws Exception {
        privateIPService.deletePrivateIPsByModuleId(moduleId);
    }

    @RequestMapping(mapping = "privateIP/deletePrivateIPsById", requestMethod = RequestMethod.GET)
    public void deletePrivateIPsById(@RequestParameter("id") long id) throws Exception {
        privateIPService.deletePrivateIPsById(id);
    }

    /***
     * Module
     */

    @RequestMapping(mapping = "module/getModules", requestMethod = RequestMethod.GET)
    public List<ObjectPair<Long, String>> getAllModules(@RequestParameter("query") String query) throws Exception {
        List<ClientModuleDTO> clientModuleDTOS = clientModuleService.getAllModules();

        List<ObjectPair<Long, String>> objectPairs = new ArrayList<>();

        for (ClientModuleDTO clientModuleDTO : clientModuleDTOS) {
            ObjectPair<Long, String> objectPair = new ObjectPair<>(clientModuleDTO.getModuleId(), clientModuleDTO.getName());
            objectPairs.add(objectPair);
        }
        return objectPairs;
    }


    /***
     * subnet
     */
    @RequestMapping(mapping = "subnet/v4", requestMethod = RequestMethod.GET)
    @ForwardedAction
    public String getV4SubnetToolPage() {
        return "subnet-tool-v4";
    }

    @RequestMapping(mapping = "subnet/v6", requestMethod = RequestMethod.GET)
    @ForwardedAction
    public String getV6SubnetToolPage() {
        return "subnet-tool-v6";
    }

    /***
     *
     * IPBlock
     */
    @JsonPost
    @RequestMapping(mapping = "inventory/add", requestMethod = RequestMethod.POST)
    public void saveIPBlockInInventory(@RequestParameter(value = "block", isJsonBody = true) IPBlockInventory block) throws Exception {
        ipService.saveIPBlockInventory(block);
    }

    @ForwardedAction
    @RequestMapping(mapping = "inventory/search", requestMethod = RequestMethod.All)
    public String searchIPInventory(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        RecordNavigationManager rnManager = new RecordNavigationManager(
                SessionConstants.NAV_IP_INVENTORY,
                request,
                ipInventoryService,
                SessionConstants.VIEW_IP_INVENTORY,
                SessionConstants.SEARCH_IP_INVENTORY);
        rnManager.doJob(loginDTO);
        return "ip-inventory-search";
    }

    @JsonPost
    @RequestMapping(mapping = "inventory/suggestion", requestMethod = RequestMethod.POST)
    public List<KeyValuePair<IPBlock, String>> getInventoryIPBlocks(@RequestParameter(isJsonBody = true, value = "params") FreeIPBlockParams params) throws Exception {
        return ipService.getIPBlockSuggestion(params);
    }

    @ForwardedAction
    @RequestMapping(mapping = "inventory/add", requestMethod = RequestMethod.GET)
    public String getIPBlockAddPage() throws Exception {
        return "inventory-ipblock-add";
    }

    /***
     * misc api calls
     */
    @RequestMapping(mapping = "get-ip-versions", requestMethod = RequestMethod.GET)
    public List<KeyValuePair> getIPVersions() {

        return Arrays.stream(IPConstants.Version.values())
                .map(t -> MethodReferences.newKeyValueString_String.apply(t.name(), t.name()))
                .collect(Collectors.toList());
    }

    @RequestMapping(mapping = "get-ip-types", requestMethod = RequestMethod.GET)
    public List<KeyValuePair> getIPTypes() {
        return Arrays.stream(IPConstants.Type.values())
                .map(t -> MethodReferences.newKeyValueString_String.apply(t.name(), t.name()))
                .collect(Collectors.toList());
    }

    /***
     * IP Sub- Region
     */

    @RequestMapping(mapping = "region/showAllSubRegions", requestMethod = RequestMethod.GET)
    @ForwardedAction
    public String allSubRegionDisplayPage() {
        return "show-all-sub-regions";
    }


    @RequestMapping(mapping = "region/ipSubRegionCreatePage", requestMethod = RequestMethod.GET)
    @ForwardedAction
    public String getIPSubRegionPage() {
        return "ip-sub-region-create-page";
    }

    @RequestMapping(mapping = "region/getAllSubRegions", requestMethod = RequestMethod.GET)
    public List<IPSubRegion> getAllIPSubRegions() throws Exception {
        return ipService.getAllSubRegions();
    }

    @RequestMapping(mapping = "region/getAllSubRegionsWithParentRegion", requestMethod = RequestMethod.GET)
    public List<ObjectPair<String, String>> getAllSubRegionsWithParentRegion() throws Exception {
        return ipService.getAllSubRegionsWithParentRegion();
    }




    @RequestMapping(mapping = "region/getAllSubRegionsByRegionId", requestMethod = RequestMethod.GET)
    public List<IPSubRegion> getAllSubRegionsByRegionId(@RequestParameter("regionId") long regionId) throws Exception {
        return ipService.getAllSubRegionsByRegionId(regionId);
    }


    @RequestMapping(mapping = "region/getAllRegions", requestMethod = RequestMethod.GET)
    public List<IPRegion> getAllIpRegions(@RequestParameter("query") String query) throws Exception {
        return ipRegionService.getAllIPRegionByQuery(query);
    }


    @JsonPost
    @RequestMapping(mapping = "region/addSubRegion", requestMethod = RequestMethod.POST)
    public IPSubRegion createNewSubRegion(
            HttpServletRequest request,
            @RequestParameter(isJsonBody = true, value = "subRegionName") String subRegionName,
            @RequestParameter(isJsonBody = true, value = "regionId") long regionId) throws Exception {

        ipService.insertNewSubRegion(regionId, subRegionName);
        return null;
    }


    /***
     * IP Region
     */
    @RequestMapping(mapping = "region/ipRegionCreatePage", requestMethod = RequestMethod.GET)
    @ForwardedAction
    public String getIPRegionPage() {
        return "ip-region-create-page";
    }

    @JsonPost
    @RequestMapping(mapping = "region/createIPRegion", requestMethod = RequestMethod.POST)
    public IPRegion createIPRegion(

            HttpServletRequest request,
            @RequestParameter(isJsonBody = true, value = "regionName") String regionName,
            @RequestParameter(isJsonBody = true, value = "districts") List<Long> districts

    ) throws Exception {

        login.LoginDTO loginDTO = (login.LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);

        List<IPRegionToDistrict> allRegions = ipRegionDistrictMappingService.getAllAssignedDistricts();
        List<Long> allAssignedDistricts = allRegions.stream().map(IPRegionToDistrict::getDistrict_id).collect(Collectors.toList());

        List<Long> common = new ArrayList<Long>(allAssignedDistricts);
        common.retainAll(districts);

        if (common.size() > 0) {
            throw new NoDataFoundException("This district is already in another zone");
        }

        IPRegion ipRegion = new IPRegion();

        List<District> ipRegionWithDistricts = districtService.getDistrictsByIds(districts);
        ipRegion.setDistricts(ipRegionWithDistricts);
        ipRegion.setAvailability(false);
        ipRegion.setName_en(regionName);
        ipRegion.setCreatedBy(loginDTO.getUsername());
        ipRegion.setLastModifiedBy(loginDTO.getUsername());

        ipRegionService.save(ipRegion);

        if (ipRegion.getId() > 0) {
            ipRegionDistrictMappingService.saveDistrictsToRegion(ipRegion.getId(), ipRegion.getDistricts());
            return ipRegion;
        }

        return null;
    }

    @JsonPost
    @RequestMapping(mapping = "region/insertDistrictToIPRegion", requestMethod = RequestMethod.POST)
    public IPRegion insertDistrictToIPRegion(

            HttpServletRequest request,
            @RequestParameter(isJsonBody = true, value = "id") Long id,
            @RequestParameter(isJsonBody = true, value = "districts") List<Long> districts

    ) throws Exception {

        List<IPRegionToDistrict> allRegions = ipRegionDistrictMappingService.getAllAssignedDistricts();
        List<Long> allAssignedDistricts = allRegions
                .stream()
                .map(IPRegionToDistrict::getDistrict_id)
                .collect(Collectors.toList());

        List<Long> common = new ArrayList<Long>(allAssignedDistricts);
        common.retainAll(districts);
        if (common.size() > 0) {
            throw new NoSuchElementException();
        }

        IPRegion ipRegion = ipRegionService.getIPRegionByID(id);
        List<District> districtList = districtService.getDistrictsByIds(districts);

        ipRegion.setDistricts(districtList);
        ipRegionService.save(ipRegion);

        if (ipRegion.getId() > 0) {
            ipRegionDistrictMappingService.saveDistrictsToRegion(ipRegion.getId(), ipRegion.getDistricts());
            return ipRegion;
        }

        return null;
    }

    @JsonPost
    @RequestMapping(mapping = "region/deleteDistrictFromRegion", requestMethod = RequestMethod.All)
    public int deleteDistrictFromRegion(
            HttpServletRequest request,
            @RequestParameter(isJsonBody = true, value = "districtId") Long districtId

    ) throws Exception {
        return ipRegionDistrictMappingService.deleteDistrictFromRegion(districtId);
    }

    @RequestMapping(mapping = "region/searchIPRegion", requestMethod = RequestMethod.All)
    @ForwardedAction
    public String searchIPRegionPage(LoginDTO loginDTO) throws Exception {
        return "ip-region-search-page";
    }


    @RequestMapping(mapping = "region/getRegions", requestMethod = RequestMethod.All)
    public List<ObjectPair<Long, String>> getAllRegions(@RequestParameter("query") String query) throws Exception {
        List<IPRegion> ipRegionList = new ArrayList<>();
        if (query == null || query.isEmpty()) {
            ipRegionList = ipRegionService.getAllIPRegion();
        } else {
            ipRegionList = ipRegionService.getRegionsBySearchString(query);
        }
        List<ObjectPair<Long, String>> regionIdNameMap = new ArrayList<>();
        for (IPRegion ipRegion : ipRegionList) {
            ObjectPair<Long, String> pair = new ObjectPair<Long, String>(ipRegion.getId(), ipRegion.getName_en());
            regionIdNameMap.add(pair);
        }
        return regionIdNameMap;
    }


    @RequestMapping(mapping = "region/getRegionByDistrict", requestMethod = RequestMethod.All)
    public IPRegion getRegionByDistrict(@RequestParameter("dId") Long districtId) throws Exception {

        IPRegion ipRegion = ipRegionDistrictMappingService.getIPRegionByDistrict(districtId);
        return ipRegion;
    }

    @RequestMapping(mapping = "region/getRegionById", requestMethod = RequestMethod.All)
    public List<IPRegion> getRegionById(@RequestParameter("rid") Long regionId) throws Exception {
        List<IPRegion> ipRegionList = new ArrayList<>();

        if (regionId == -1) {
            ipRegionList = ipRegionDistrictMappingService.getAllRegion();
        } else {
            IPRegion ipRegion = ipRegionDistrictMappingService.getIPRegionById(regionId);
            ipRegionList.add(ipRegion);
        }

        return ipRegionList;
    }

    @RequestMapping(mapping = "region/getDistrictsNotAssignedToRegion", requestMethod = RequestMethod.GET)
    public List<District> getDistrictsNotAssignedToRegion(@RequestParameter("query") String query) throws Exception {

        List<IPRegionToDistrict> createdRegions = ipRegionDistrictMappingService.getAllAssignedDistricts();
        List<Long> allAssignedDistricts = createdRegions.stream().map(IPRegionToDistrict::getDistrict_id).collect(Collectors.toList());

        List<District> districts = districtService.getAllDistrictsByQuery(query);

        List<District> result = new ArrayList<>();
        for (District d : districts) {
            if (!allAssignedDistricts.contains(d.getId())) {
                result.add(d);
            }
        }
        return result;
    }


    @JsonPost
    @RequestMapping(mapping = "allocate", requestMethod = RequestMethod.POST)
    public void allocateIPAddress(@RequestParameter(isJsonBody = true, value = "params") List<IPBlockForConnection> blocks) throws Exception {
        ipService.allocateIPAddress(blocks);
    }

    @RequestMapping(mapping = "save/usage", requestMethod = RequestMethod.POST)
    @JsonPost
    public void saveIPUsage(@RequestParameter(isJsonBody = true, value = "block") IPBlockUsage usage) throws Exception {
        ipService.saveIPBlockUsage(usage);
    }

    @JsonPost
    @RequestMapping(mapping = "get-free-block", requestMethod = RequestMethod.POST)
    public List<IPBlock> getFreeIPBlocks(@RequestParameter(isJsonBody = true, value = "block") IPBlock block,
                                         @RequestParameter(isJsonBody = true, value = "size") int blockSize) throws Exception {
        return ipService.getAvailableIPBlock(block, blockSize);
    }

    @JsonPost
    @RequestMapping(mapping = "routing-info/suggestion", requestMethod = RequestMethod.POST)
    public IPRoutingInfo getIPRoutingInfoSuggestion(
            @RequestParameter(isJsonBody = true, value = "fromIP") String fromIP,
            @RequestParameter(isJsonBody = true, value = "toIP") String toIP,
            @RequestParameter(isJsonBody = true, value = "blockSize") int blockSize) {
        long from = MethodReferences.getLongFromIPString.apply(fromIP);
        long to = MethodReferences.getLongFromIPString.apply(toIP);
        if (to - from + 1 < blockSize) {
            throw new RequestFailureException("Routing Info generation failed: Reason: Block Size is greater than [from - to] ip range");
        }

        long actualToIP = from + blockSize - 1;
        return ipService.getIPRoutingSuggestion(from, actualToIP);
    }

//    @RequestMapping(mapping = "routing-info/search", requestMethod = RequestMethod.All)
//    public IPRoutingInfo getRoutingInfoByLLIConnectionId(@RequestParameter("id") long connectionId) throws Exception {
//        return ipService.getRoutingInfoByLLIConnectionId(connectionId);
//    }

    @ForwardedAction
    @RequestMapping(mapping = "usage/search", requestMethod = RequestMethod.All)
    public String searchIPUsage(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        RecordNavigationManager rnManager = new RecordNavigationManager(
                SessionConstants.NAV_IP_USAGE,
                request,
                ipUsageService,
                SessionConstants.VIEW_IP_USAGE,
                SessionConstants.SEARCH_IP_USAGE);
        rnManager.doJob(loginDTO);
        return "ip-usage-search";
    }

    @RequestMapping(mapping = "connection/details", requestMethod = RequestMethod.GET)
    public JsonArray getIPDetailsOfConnection(@RequestParameter("id") long connectionId) throws Exception {
        return ipService.getIPBlocksByConnectionId(connectionId);
    }

    @RequestMapping(mapping = "deallocate", requestMethod = RequestMethod.GET)
    public void deallocateIP(@RequestParameter("id") long connectionId) throws Exception {
        ipService.deallocateIPsByConnectionId(connectionId);
    }

    @JsonPost
    @RequestMapping(mapping = "get-block-usage", requestMethod = RequestMethod.POST)
    public List<IPBlock> getIPBlockUsageBySearchCriteria(@RequestParameter(value = "param", isJsonBody = true) String jsonString, HttpServletRequest request) throws Exception {
        LoginDTO loginDTO = (LoginDTO) request.getSession().getAttribute(SessionConstants.USER_LOGIN);
        JsonObject jsonObject;
        try {
            jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
        } catch (Exception e) {
            throw new RequestFailureException("Parsing Error " + e.getMessage());
        }
        if (jsonObject != null) {
            Type type = new TypeToken<Hashtable<String, Object>>() {
            }.getType();
            Hashtable<String, Object> map = new Gson().fromJson(jsonString, type);
            List<Long> ids = ipUsageService.getIDsWithSearchCriteria(map, loginDTO);
            if (!ids.isEmpty()) {
                List<IPBlockUsage> usages = ipUsageService.getDTOs(ids);

                return usages.stream()
                        .map(MethodReferences.getIPBlockFromUsage)
                        .collect(Collectors.toList());
            }

        }
        return Collections.emptyList();

    }


    //public ip deletion for NAT
    @RequestMapping(mapping = "publicIP/deletePublicIpUsageById", requestMethod = RequestMethod.GET)
    public void deletePublicIpUsageById(@RequestParameter("id") long id) throws Exception {
        ipUsageService.deletePublicIpUsageById(id);

    }
}
