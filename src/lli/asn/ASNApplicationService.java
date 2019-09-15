package lli.asn;

import annotation.Transactional;

import application.Application;
import application.ApplicationService;
import application.ApplicationState;
import application.ApplicationType;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import common.ModuleConstants;
import common.RequestFailureException;
import flow.FlowService;
import flow.entity.FlowState;
import flow.repository.FlowRepository;
import global.GlobalService;
import login.LoginDTO;
import login.LoginService;
import lombok.extern.log4j.Log4j;
import requestMapping.Service;
import util.NavigationService;
import util.ServiceDAOFactory;
import util.TransactionType;

import java.util.*;
import java.util.stream.Collectors;

import static util.ModifiedSqlGenerator.getAllObjectList;
@Log4j
public class ASNApplicationService implements NavigationService {

    @Service private ApplicationService applicationService;
    @Service private GlobalService globalService;
    @Service private FlowService flowService;

    @Transactional
    void insertBatchOperation(String application, LoginDTO loginDTO)throws Exception {

        JsonElement jsonElement = new JsonParser().parse(application);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long client = jsonObject.get("client").getAsJsonObject().get("key").getAsLong();
        int asnNo = jsonObject.get("asnNo").getAsInt();
        long suggestedDate = jsonObject.get("suggestedDate").getAsLong();
        String comment = jsonObject.get("comment") != null ? jsonObject.get("comment").getAsString() : "";
        /*create asn Application*/
        List<String>ipList2 =new ArrayList<>();
        List<String>ipList3 =new ArrayList<>();
        JsonArray jsonArray = jsonObject.get("ipV4List").getAsJsonArray();
        JsonArray jsonArray1 = jsonObject.get("ipV6List").getAsJsonArray();
        if (jsonArray != null) {
            for (JsonElement j : jsonArray) {
                ipList2.add(j.getAsString());
            }
        }
        if (jsonArray1 != null) {
            for (JsonElement j : jsonArray1) {
                ipList3.add(j.getAsString());
            }
        }

        checkDuplicateASNByASNNo(asnNo);

        ASNApplication asnApplication = new ASNApplication();
        asnApplication.setAsnNo(asnNo);
        asnApplication.setSuggestedDate(suggestedDate);
        asnApplication.setClientId(client);
        asnApplication.setComment(comment);
        asnApplication.setClassName(ASNApplication.class.getCanonicalName());
        asnApplication.setApplicationState(ApplicationState.ASN_SUBMITTED); // TODO need to be changed
        asnApplication.setSubmissionDate(System.currentTimeMillis());
        asnApplication.setApplicationType(ApplicationType.ASN_APPLICATION); // TODO need to be changed
        asnApplication.setModuleId(ModuleConstants.Module_ID_LLI);
        asnApplication.setUserId(loginDTO.getUserID()>0?loginDTO.getUserID():loginDTO.getAccountID());
        globalService.save(asnApplication);

        /* TODO: 3/24/2019 check the desing terminologies as it would not be wise to store 1000 ips
            in the process table at the same time add later to a main table
        */


        ASN asn = new ASN();
        asn.setApplicationId(asnApplication.getAsnAppId());
        asn.setAsnNo(asnNo);
        asn.setClient(client);
        asn.setCreatedDate(System.currentTimeMillis());
        asn.setStatus(ASNConstant.ON_PROCESS);
        asn.setState(ApplicationState.ASN_SUBMITTED.getState());
        asn.setCreated_by(loginDTO.getUserID()>0?loginDTO.getUserID():loginDTO.getAccountID());
        asn.setLastModifyTime(System.currentTimeMillis());
        globalService.save(asn);
        /*requested map of asn to ips*/
        ipList2.forEach(s->{
            ASNmapToIP asNmapToIP = new ASNmapToIP();
            s= s.replaceAll("^\"|\"$", "");
            asNmapToIP.setIp(s);
            asNmapToIP.setAsnId(asn.getId());
            asNmapToIP.setIpVersion(1);
            asNmapToIP.setLastModifyTime(System.currentTimeMillis());
            try{
                globalService.save(asNmapToIP);
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        ipList3.forEach(s->{
            ASNmapToIP asNmapToIP = new ASNmapToIP();
            s= s.replaceAll("^\"|\"$", "");

            asNmapToIP.setIp(s);

            asNmapToIP.setAsnId(asn.getId());
            asNmapToIP.setIpVersion(2);
            asNmapToIP.setLastModifyTime(System.currentTimeMillis());
            try{
                globalService.save(asNmapToIP);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    @Transactional
    private void checkDuplicateASNByASNNo(int asnNo)throws Exception {
        List<ASN> asn =globalService.getAllObjectListByCondition(ASN.class,
                new ASNConditionBuilder()
                        .Where()
                        .asnNoEquals(asnNo)
                        .getCondition());
        if(asn.size()>0){
            throw new RequestFailureException("There is already an ASN entry with this number");
        }
    }

    @Override
    public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
        return getIDsWithSearchCriteria(new Hashtable<>(), loginDTO, objects);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects)
            throws Exception {
        return applicationService.getIDsWithSearchCriteria(searchCriteria, loginDTO, ModuleConstants.Module_ID_LLI);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
//        List<Application> list = (List<Application>) applicationService.getDTOs(recordIDs);
        LoginDTO loginDTO = (LoginDTO) objects[0];

        int elementsToSkip = (int) objects[1];
        int elementsToConsider = (int) objects[2];

        int roleId = LoginService.getRoleIdForApplicationsConsideringClient(loginDTO);
        List< ASNApplication > asnApplications = applicationService
                .getAllApplicationsByAppIds((List<Long>)recordIDs, ASNApplication.class);

        Map<Long, List<ASN> > mapOfASNsToASNApplicationId = globalService.getAllObjectListByCondition(
                ASN.class, new ASNConditionBuilder()
                        .Where()
                        .applicationIdIn(
                                asnApplications.stream()
                                .map(ASNApplication::getAsnAppId)
                                .collect(Collectors.toList())
                        )
                        .getCondition()
        )
                .stream()
                .collect(Collectors.groupingBy(ASN::getApplicationId));

        return asnApplications.stream()
                .peek(t -> {
                        String concatenatedState = "";
                        boolean hasPermission = false;
                        try {
//                            ASNApplication asnApplication = (ASNApplication) applicationService.getApplicationByApplicationId(t.getApplicationId());


                            List<ASN> asns = mapOfASNsToASNApplicationId
                                    .getOrDefault(t.getAsnAppId(), Collections.emptyList());

                            for (int i = 0; i < asns.size(); i++) {
                                FlowState flowState =  FlowRepository.getInstance().getFlowStateByFlowStateId(asns.get(i).getState());

                                        //new FlowService().getStateById(asns.get(i).getState());
                                List<FlowState> nextStates = FlowRepository.getInstance().getNextStatesByCurrentStateAndRoleId(flowState.getId(), roleId);

                                        //new FlowService().getNextStatesFromStateRole(asns.get(i).getState(), roleId);
                                if (nextStates.size() > 0) {
                                    hasPermission = true;
                                }
                                concatenatedState += "<p style= color:" + flowState.getColor() + ">" + flowState.getViewDescription() + "</p> ";
                            }
                        } catch (Exception e) {
                            log.fatal(e.getMessage());
                        }
                        t.setHasPermission(hasPermission);
                        t.setStateDescription(concatenatedState);
                })
                .skip(elementsToSkip)
                .limit(elementsToConsider)
                .collect(Collectors.toList());
    }
    public List<ASNApplication> getLLIApplicationListByIDList(List<Long> applicationIDList) throws Exception {
        return getAllObjectList(ASNApplication.class, new ASNApplicationConditionBuilder()
                .Where()
                .asnAppIdIn(applicationIDList)
//                .orderByapplicationIDDesc()
                .getCondition());
    }

    private List<ASN> getAsnsByAppId(long asnAppId) throws Exception{
       return globalService.getAllObjectListByCondition(ASN.class,new ASNConditionBuilder()
               .Where()
               .applicationIdEquals(asnAppId)
               .getCondition());
    }

    public FlowState getCurrentState(int state) {
        FlowState flowState = flowService.getStateById(state);
        flowState.setUrl(ASNConstant.stateMap.get(state).getUrl());
        flowState.setModal(ASNConstant.stateMap.get(state).getModal());
        flowState.setView(ASNConstant.stateMap.get(state).getView());
        flowState.setRedirect(ASNConstant.stateMap.get(state).getRedirect());
        flowState.setRedirect(ASNConstant.stateMap.get(state).getParam());
        flowState.setName(ApplicationState.getApplicationStateByStateId(flowState.getId()).name());
        return flowState;
    }

    public List<FlowState> getActionList(int state, int roleId) {
        List<FlowState> flowStates = new ArrayList<>();
        flowStates = flowService.getNextStatesFromStateRole(state, roleId);
        for (FlowState flowState : flowStates) {
            flowState.setUrl(ASNConstant.stateMap.get(flowState.getId()).getUrl());
            flowState.setModal(ASNConstant.stateMap.get(flowState.getId()).getModal());
            flowState.setView(ASNConstant.stateMap.get(flowState.getId()).getView());
            flowState.setRedirect(ASNConstant.stateMap.get(flowState.getId()).getRedirect());
            flowState.setParam(ASNConstant.stateMap.get(flowState.getId()).getParam());
            flowState.setName(ApplicationState.getApplicationStateByStateId(flowState.getId()).name());
        }
        return flowStates;
    }

    @Transactional
    public List<ASN> getASNSByAppId(long applicationId) throws Exception{
        List<ASN> asnList =globalService.getAllObjectListByCondition(ASN.class,
                new ASNConditionBuilder()
                        .Where()
                        .applicationIdEquals(applicationId)
                        .getCondition()
        );
        asnList.stream().forEach(s->{
            try {
                List<ASNmapToIP> asNmapToIPS = globalService.getAllObjectListByCondition(ASNmapToIP.class,
                        new ASNmapToIPConditionBuilder()
                                .Where()
                                .asnIdEquals(s.getId())
                                //.isDeletedEquals(0)
                                .applicationIdEquals(applicationId)
                                .getCondition());
                s.setAsNmapToIPS(asNmapToIPS);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        return asnList;
    }

    @Transactional
    public void acceptASNRequest(ASNApplication application,LoginDTO loginDTO)throws Exception {
        ASNApplication asnApplication =(ASNApplication) ServiceDAOFactory.getService(ApplicationService.class).getApplicationByApplicationId(application.getApplicationId());
        List<ASN> asnList = getASNSByAppId(asnApplication.getAsnAppId());
        asnList.forEach(s->{
            s.setState(ApplicationState.REQUEST_ACCEPT.getState());
            s.setStatus(ASNConstant.IS_ACTIVE);
            s.setLastModifyTime(System.currentTimeMillis());
            s.setCreated_by(loginDTO.getUserID()>0?loginDTO.getUserID():loginDTO.getAccountID());
            ServiceDAOFactory.getService(GlobalService.class).update(s);
            s.getAsNmapToIPS().forEach(t->{
                        t.setLastModifyTime(System.currentTimeMillis());
                        globalService.update(t);
                    }
            );
        });
    }

    @Transactional
    public void editBatchOperation(String application, LoginDTO loginDTO)throws Exception {
        JsonElement jsonElement = new JsonParser().parse(application);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long asnId = jsonObject.get("asnId").getAsLong();
        /*create asn Application*/
        List<String>ipList2 =new ArrayList<>();
        List<String>ipList3 =new ArrayList<>();
        JsonArray deletableList = jsonObject.get("deletableList").getAsJsonArray();
        if (deletableList != null) {
            for (JsonElement j : deletableList) {
                long id = j.getAsJsonObject().get("id").getAsLong();
                deleteBatchOperation(id);
            }
        }
        JsonArray jsonArray = jsonObject.get("ipV4List").getAsJsonArray();
        JsonArray jsonArray1 = jsonObject.get("ipV6List").getAsJsonArray();
        if (jsonArray != null) {
            for (JsonElement j : jsonArray) {
                ipList2.add(j.getAsString());
            }
        }
        if (jsonArray1 != null) {
            for (JsonElement j : jsonArray1) {
                ipList3.add(j.getAsString());
            }
        }

        ipList2.forEach(s->{
            ASNmapToIP asNmapToIP = new ASNmapToIP();
            asNmapToIP.setIp(s);
            asNmapToIP.setAsnId(asnId);
            asNmapToIP.setIpVersion(1);
            asNmapToIP.setLastModifyTime(System.currentTimeMillis());
            try{
                globalService.save(asNmapToIP);
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        ipList3.forEach(s->{
            ASNmapToIP asNmapToIP = new ASNmapToIP();
            asNmapToIP.setIp(s);
            asNmapToIP.setAsnId(asnId);
            asNmapToIP.setIpVersion(2);
            asNmapToIP.setLastModifyTime(System.currentTimeMillis());
            try{
                globalService.save(asNmapToIP);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    @Transactional
    public void deleteBatchOperation(long ipId)throws Exception {
        ASNmapToIP asNmapToIP = globalService.findByPK(ASNmapToIP.class,ipId);
        asNmapToIP.setLastModifyTime(System.currentTimeMillis());
        asNmapToIP.setIsDeleted(1);
        globalService.update(asNmapToIP);
    }
}
