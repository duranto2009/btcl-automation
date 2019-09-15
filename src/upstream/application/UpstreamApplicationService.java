package upstream.application;

import annotation.DAO;
import annotation.Transactional;
import application.ApplicationState;
import application.ApplicationType;
import common.ModuleConstants;
import common.RequestFailureException;
import entity.comment.CommentService;
import flow.FlowService;
import flow.entity.FlowState;
import global.GlobalService;
import login.LoginDTO;
import login.LoginService;
import requestMapping.Service;
import upstream.UpstreamConstants;
import upstream.circuitInfo.CircuitInformationService;
import upstream.contract.UpstreamContract;
import upstream.contract.UpstreamContractService;
import upstream.inventory.UpstreamInventoryItem;
import upstream.inventory.UpstreamInventoryService;
import util.DatabaseConnectionFactory;
import util.ModifiedSqlGenerator;
import util.NavigationService;
import util.TransactionType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

public class UpstreamApplicationService implements NavigationService {

    @Service
    GlobalService globalService;

    @DAO
    UpstreamApplicationDAO applicationDAO;

    @Service
    FlowService flowService;

    @Service
    CircuitInformationService circuitInformationService;

    @Service UpstreamInventoryService upstreamInventoryService;
    @Service
    UpstreamContractService upstreamContractService;

    @Service
    CommentService commentService;

    @Transactional
    public void saveApplication(UpstreamApplication application, LoginDTO loginDTO, ApplicationType applicationType) throws Exception {

//        if (applicationType == ApplicationType.UPSTREAM_NEW_REQUEST) {
//            application.setState(ApplicationState.UPSTREAM_FORWARDED_TO_GM_DATA_AND_INTERNET.getState());
//            application.setApplicationStatus(ApplicationState.UPSTREAM_FORWARDED_TO_GM_DATA_AND_INTERNET.name());
//            application.setApplicationType(UpstreamConstants.APPLICATION_TYPE.NEW);
//        }
        application.setState(ApplicationState.UPSTREAM_FORWARDED_TO_GM_DATA_AND_INTERNET.getState());
        application.setApplicationStatus(ApplicationState.UPSTREAM_FORWARDED_TO_GM_DATA_AND_INTERNET.name());
        application.setApplicationType(applicationType);
        saveApplication(application);

        //insert the comments
        if(application.getComment()!=null && !application.getComment().isEmpty())commentService.insertComment(application.getComment(), application.getApplicationId(), loginDTO.getUserID(), ModuleConstants.Module_ID_UPSTREAM, (int)application.getState());

    }

    @Transactional
    public void saveApplication(UpstreamApplication application) throws Exception {
        globalService.save(application);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<UpstreamApplication> getApplicationsByBandwidthType(long bandwidthTypeId) throws Exception {

        List<UpstreamApplication> applications = globalService.getAllObjectListByCondition(UpstreamApplication.class,
                new UpstreamApplicationConditionBuilder()
                        .Where()
                        .typeOfBandwidthIdEquals(bandwidthTypeId)
                        .getCondition());

        if (applications.isEmpty()) {
            throw new RequestFailureException("No application found with bandwidth type id " + bandwidthTypeId);
        }
        return applications;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<UpstreamApplication> getApplicationsByBTCLServiceLocation(long serviceLocationId) throws Exception {

        List<UpstreamApplication> applications = globalService.getAllObjectListByCondition(UpstreamApplication.class,
                new UpstreamApplicationConditionBuilder()
                        .Where()
                        .btclServiceLocationIdEquals(serviceLocationId)
                        .getCondition());

        if (applications.isEmpty()) {
            throw new RequestFailureException("No application found with BTCL Service Location id " + serviceLocationId);
        }
        return applications;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<UpstreamApplication> getApplicationsByProviderId(long providerId) throws Exception {

        List<UpstreamApplication> applications = globalService.getAllObjectListByCondition(UpstreamApplication.class,
                new UpstreamApplicationConditionBuilder()
                        .Where()
                        .selectedProviderIdEquals(providerId)
                        .getCondition());

        if (applications.isEmpty()) {
            throw new RequestFailureException("No application found with provider id " + providerId);
        }
        return applications;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<UpstreamApplication> getApplicationsByMediaType(long mediaTypeId) throws Exception {

        List<UpstreamApplication> applications = globalService.getAllObjectListByCondition(UpstreamApplication.class,
                new UpstreamApplicationConditionBuilder()
                        .Where()
                        .mediaIdEquals(mediaTypeId)
                        .getCondition());

        if (applications.isEmpty()) {
            throw new RequestFailureException("No application found with media id " + mediaTypeId);
        }
        return applications;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<UpstreamApplication> getApplicationsByProviderLocation(long providerLocationId) throws Exception {

        List<UpstreamApplication> applications = globalService.getAllObjectListByCondition(UpstreamApplication.class,
                new UpstreamApplicationConditionBuilder()
                        .Where()
                        .providerLocationIdEquals(providerLocationId)
                        .getCondition());

        if (applications.isEmpty()) {
            throw new RequestFailureException("No application found with provider location id " + providerLocationId);
        }
        return applications;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<UpstreamApplication> getApplicationsByStatus(String status) throws Exception {

        List<UpstreamApplication> applications = globalService.getAllObjectListByCondition(UpstreamApplication.class,
                new UpstreamApplicationConditionBuilder()
                        .Where()
                        .stateBothLike(status)
                        .getCondition());

        if (applications.isEmpty()) {
            throw new RequestFailureException("No application found with status " + status);
        }
        return applications;
    }

    @Transactional(transactionType = TransactionType.READONLY)

    @Override
    public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
        return getIDsWithSearchCriteria(new Hashtable(), loginDTO);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects) throws Exception {
        searchCriteria.put("moduleId", Integer.toString(ModuleConstants.Module_ID_UPSTREAM));
        return applicationDAO.getIDsWithSearchCriteria(searchCriteria, loginDTO);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
        List<UpstreamApplication> applications = applicationDAO.getDTOs((List<Long>) recordIDs);

        LoginDTO loginDTO = (LoginDTO) objects[0];
        int roleId = LoginService.getRoleIdForApplicationsConsideringClient(loginDTO);

        List<UpstreamApplication> permittedApplications = new ArrayList<>();

        for (UpstreamApplication application : applications) {
            FlowState flowState = flowService.getStateById((int) application.getState());
            List<FlowState> nextStates = flowService.getNextStatesFromStateRole((int) application.getState(), roleId);
            if (nextStates.size() > 0)
                application.setHasPermission(true);
            else
                application.setHasPermission(false);

            permittedApplications.add(application);

            if (flowState != null)
                application.setApplicationStatus(flowState.getViewDescription());
        }
        return permittedApplications;
    }

    public void changeApplication(UpstreamApplication application, LoginDTO loginDTO) {
        //TODO - check bw
    }

    public UpstreamApplication getApplicationByApplicationId(long applicationID) throws Exception {
        UpstreamApplication application = globalService.findByPK(UpstreamApplication.class, applicationID);
//        if(application.getContractId()> 0){
//            application.setCircuitInformationDTOs(circuitInformationService.getAllCircuitsByContractId(application.getContractId()));
//        }

        if (application == null) {
            throw new RequestFailureException("No application found with application id " + applicationID);
        }
        return application;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public FlowState getCurrentState(Integer state) {
        FlowState flowState = flowService.getStateById(state);
        flowState.setUrl(UpstreamConstants.stateMap.get(state).getUrl());
        flowState.setModal(UpstreamConstants.stateMap.get(state).getModal());
        flowState.setView(UpstreamConstants.stateMap.get(state).getView());
        flowState.setRedirect(UpstreamConstants.stateMap.get(state).getRedirect());
        flowState.setRedirect(UpstreamConstants.stateMap.get(state).getParam());
        flowState.setName(ApplicationState.getApplicationStateByStateId(flowState.getId()).name());

        return flowState;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<FlowState> getActionList(Integer state, Integer roleId) {
        List<FlowState> flowStates = new ArrayList<>();

        flowStates = flowService.getNextStatesFromStateRole(state, roleId);

        for (FlowState flowState : flowStates) {
            flowState.setUrl(UpstreamConstants.stateMap.get(flowState.getId()).getUrl());
            flowState.setModal(UpstreamConstants.stateMap.get(flowState.getId()).getModal());
            flowState.setView(UpstreamConstants.stateMap.get(flowState.getId()).getView());
            flowState.setRedirect(UpstreamConstants.stateMap.get(flowState.getId()).getRedirect());
            flowState.setParam(UpstreamConstants.stateMap.get(flowState.getId()).getParam());
            flowState.setName(ApplicationState.getApplicationStateByStateId(flowState.getId()).name());
        }
        return flowStates;
    }

    @Transactional
    public void update(UpstreamApplication application, LoginDTO loginDTO) throws Exception {

//        if(application.applicationType == ApplicationType.UPSTREAM_NEW_REQUEST)
//        {
//            addCircuitInformation(application, loginDTO);
//        }

        globalService.update(application);

        //for contract-extension direct jump from gm_international to finalize connection
        if (
                application.applicationType == ApplicationType.UPSTREAM_CONTRACT_EXTENSION_REQUEST
                        && application.getState() == ApplicationState.UPSTREAM_APPROVED_VENDOR_AND_FORWARDED_TO_DGM_CORE_UPSTREAM.getState()
        ) {
            UpstreamContract previousContract = upstreamContractService.getContractByContractId(application.getContractId());
            application.setContractDuration(application.getContractDuration()+previousContract.getContractDuration()); // increase from the previous duration of contract!!!
            completeConnectionFromGMInternational(application, loginDTO);
        }

        //for contract-close
        if (
                application.applicationType == ApplicationType.UPSTREAM_CONTRACT_CLOSE_REQUEST
                        && application.getState() == ApplicationState.UPSTREAM_APPROVED_VENDOR_AND_FORWARDED_TO_DGM_CORE_UPSTREAM.getState()
        ) {

            completeConnectionFromGMInternational(application, loginDTO);
        }

        if(!application.getComment().isEmpty())commentService.insertComment(application.getComment(), application.getApplicationId(), loginDTO.getUserID(), ModuleConstants.Module_ID_UPSTREAM, (int)application.getState());

    }

    @Transactional
    public void connectionCreatorOrUpdaterManager(UpstreamApplication application, LoginDTO loginDTO) throws Exception {


        //update application state with srf date or related data
        globalService.update(application);

        //save the contract
        UpstreamContract newContract = createContractFromApplicationAndUpdateNecessaryInfos(application);

        globalService.save(newContract);

        //insert circuit info's
        //TODO pass Contract Id and add new column contract in circuit dto
        addCircuitInformation(application, newContract, loginDTO);


        if(!application.getComment().isEmpty())commentService.insertComment(application.getComment(), application.getApplicationId(), loginDTO.getUserID(), ModuleConstants.Module_ID_UPSTREAM, (int)application.getState());

    }
    @Transactional
    public void invalidateContract(UpstreamContract contract){
        contract.setActiveTo(System.currentTimeMillis());
        contract.setValidTo(System.currentTimeMillis());

        globalService.update(contract);
    }

    public UpstreamContract createContractFromApplicationAndUpdateNecessaryInfos(UpstreamApplication application) throws Exception {
        long contactId = DatabaseConnectionFactory.getCurrentDatabaseConnection()
                .getNextIDWithoutIncrementing(
                        ModifiedSqlGenerator.getTableName(UpstreamContract.class));

        long historyId = 0;
        if(application.getContractId()>0){
            UpstreamContract previousContract = upstreamContractService.getContractByContractId(application.getContractId());
            historyId = previousContract.getContractHistoryId();

            invalidateContract(previousContract);
            //TODO invalidate previous contract for other than new connection

        }else{
            historyId = contactId;
        }

        UpstreamContract upstreamContract = UpstreamContract.builder()
                .contractId(contactId)
                .applicationId(application.getApplicationId())
                .contractHistoryId(historyId) // TODO code idea taken from vpn/FlowConnectionManagerService.java -> newLinkCreator()
                .applicationType(application.getApplicationType())
                .typeOfBandwidthId(application.getTypeOfBandwidthId())
                .bandwidthCapacity(application.getBandwidthCapacity())
                .mediaId(application.getMediaId())
                .btclServiceLocationId(application.getBtclServiceLocationId())
                .providerLocationId(application.getProviderLocationId())
                .selectedProviderId(application.getSelectedProviderId())
                .bandwidthPrice(application.getBandwidthPrice())
                .otc(application.getOtc())
                .mrc(application.getMrc())
                .contractDuration(application.getContractDuration())
                .srfDate(application.getSrfDate())
                .circuitInfoLink(application.getCircuitInfoLink())
//                .contractName()
                .activeFrom(System.currentTimeMillis())
                .activeTo(Long.MAX_VALUE)
                .validFrom(System.currentTimeMillis())
                .validTo(Long.MAX_VALUE)

                .build();

        String contractName = "";

        UpstreamInventoryItem selectedProviderItem = upstreamInventoryService.getUpstreamInventoryItemsByIdAndItemType(application.getSelectedProviderId(), UpstreamConstants.INVENTORY_ITEM_TYPE.BTCL_SERVICE_LOCATION.getInventoryItemName());
        String selectedProvider = selectedProviderItem != null ? selectedProviderItem.getItemName() : "";

        UpstreamInventoryItem serviceLocationItem = upstreamInventoryService.getUpstreamInventoryItemsByIdAndItemType(application.getBtclServiceLocationId(), UpstreamConstants.INVENTORY_ITEM_TYPE.BTCL_SERVICE_LOCATION.getInventoryItemName());
        String serviceLocation = serviceLocationItem != null ? serviceLocationItem.getItemName() : "";

        UpstreamInventoryItem providerLocationItem = upstreamInventoryService.getUpstreamInventoryItemsByIdAndItemType(application.getProviderLocationId(), UpstreamConstants.INVENTORY_ITEM_TYPE.PROVIDER_LOCATION.getInventoryItemName());
        String providerLocation = providerLocationItem != null ? providerLocationItem.getItemName() : "";

        String bandwidth = String.valueOf(application.getBandwidthCapacity());

        contractName = contactId + " : " +  selectedProvider + " : " + providerLocation + " -> " + serviceLocation + ": " + bandwidth + " GB";
        upstreamContract.setContractName(contractName);
//        in
        return upstreamContract;

    }

    @Transactional
    public void updateCircuitInformation(UpstreamApplication application, LoginDTO loginDTO) {
        if (application.getCircuitInformationDTOs().size() > 0) {
            application.getCircuitInformationDTOs().forEach(x -> {
                try {
                    if (x.getId() > 0) circuitInformationService.updateCircuitInformation(x);
                    else circuitInformationService.addNewCircuitInformation(x);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Transactional
    public void addCircuitInformation(UpstreamApplication application, UpstreamContract contract, LoginDTO loginDTO) {
        if (application.getCircuitInformationDTOs().size() > 0) {
            application.getCircuitInformationDTOs().forEach(x -> {
                try {
                    //to insert again clear the primary key
//                    x.setId(0L);

                    //set current app id as new app id
//                    x.setApplicationId(application.getApplicationId());
                    x.setContractId(contract.getContractId());

                    circuitInformationService.addNewCircuitInformation(x);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Transactional
    public void completeConnectionFromGMInternational(UpstreamApplication application, LoginDTO loginDTO) throws Exception {
        application.setApplicationStatus(ApplicationState.UPSTREAM_CONNECTION_COMPLETE.name());
        application.setState(ApplicationState.UPSTREAM_CONNECTION_COMPLETE.getState());
        // for contract extension need to update contract
        if(application.applicationType != ApplicationType.UPSTREAM_CONTRACT_CLOSE_REQUEST){
            connectionCreatorOrUpdaterManager(application, loginDTO);
        }
        //for close contract need to update the application and invalidate the contract
        else{
            globalService.update(application);
            UpstreamContract previousContract = upstreamContractService.getContractByContractId(application.getContractId());
            invalidateContract(previousContract);
        }
    }

    @Transactional
    public void bandwidthChangeApplicationSubmit(UpstreamApplication application, LoginDTO loginDTO) throws Exception {
        UpstreamContract previousContract = upstreamContractService.getContractByContractId(application.getContractId());
        application.setApplicationId(0L);
        //update application
        if(application.getBandwidthCapacity() > previousContract.getBandwidthCapacity()){
            saveApplication(application, loginDTO, ApplicationType.UPSTREAM_UPGRADE);
        }
        // downgrade application
        else if(application.getBandwidthCapacity() < previousContract.getBandwidthCapacity()) {
            saveApplication(application, loginDTO, ApplicationType.UPSTREAM_DOWNGRADE);
        }
        else{
            // no change app!! throw exception
        }
    }
}
