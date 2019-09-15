package flow;


import annotation.DAO;
import annotation.Transactional;
import application.ApplicationPhase;
import client.module.ClientModuleDTO;
import client.module.ClientModuleService;
import flow.dao.*;
import flow.entity.*;
import flow.repository.FlowRepository;
import global.GlobalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ServiceDAOFactory;
import util.TransactionType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author maruf
 */
public class FlowService {

    private static final Logger LOG = LoggerFactory.getLogger(FlowService.class);

    @DAO
    FlowDAO flowDAO;
    @DAO
    FlowStateDAO flowStateDAO;
    @DAO
    FlowStateTransitionDAO flowStateTransitionDAO;
    @DAO
    FlowStateTransitionRoleDAO flowStateTransitionRoleDAO;
    @DAO
    RoleDAO roleDAO;

    GlobalService globalService = ServiceDAOFactory.getService(GlobalService.class);

    ClientModuleService clientModuleService = ServiceDAOFactory.getService(ClientModuleService.class);

    @Transactional(transactionType = TransactionType.READONLY)
    public Object test(HttpServletRequest request, HttpServletResponse response) {
        return getTerminalStates();
    }

    // Mirror : FlowRepository.getInstance().getFlowStateByFlowStateId()
    public FlowState getStateById(int id) {
        return new ClassicDAO().getStateById(id);
    }

    // Mirror : FlowRepository.getInstance().getAllFlowStates()
    public List<FlowState> getStates() {
        return new ClassicDAO().getStates();
    }

    // Mirror : FlowRepository.getInstance().getActionableFlowStatesByRoleId()
    public List<FlowState> getStatesByRole(int roleId) {
        return new ClassicDAO().getStatesByRoleId(roleId);
    }

    public List<FlowState> getStatesByRole(String roleName) {
        return new ClassicDAO().getStatesByRoleName(roleName);
    }

    // Mirror : FlowRepository.getInstance().getNextStatesByCurrentState()
    public List<FlowState> getNextStatesFromState(int stateId) {
        return new ClassicDAO().getNextStatesFromCurentStateId(stateId);
    }

    // Mirror : FLowRepository.getInstance().getNextStatesByCurrentStateAndRoleId()
    public List<FlowState> getNextStatesFromStateRole(int stateId, int roleId) {
        return new ClassicDAO().getNextStatesFromCurentStateIdAndRoleId(stateId, roleId);
    }

    public List<FlowState> getNextStatesFromState(String stateName) {
        return new ClassicDAO().getNextStatesFromCurentStateName(stateName);
    }

    public List<Role> getRolesByNextState(int nextState) {
        return new ClassicDAO().getRolesByNextState(nextState);
    }

    public List<FlowState> getTerminalStates() {
        return new ClassicDAO().getTerminalStates();
    }

    public Map<Long, String> getModuleIdNameMap() throws Exception {
        Map<Long, String> idNameMap = null;
        try {
            List<ClientModuleDTO> clientModuleDTOs = clientModuleService.getAllModules();
            if (clientModuleDTOs != null) {
                if (!clientModuleDTOs.isEmpty()) {
                    idNameMap = new HashMap<>();
                    for (ClientModuleDTO clientModuleDTO : clientModuleDTOs) {
                        idNameMap.put(clientModuleDTO.getModuleId(), clientModuleDTO.getName());
                    }
                }
            }
        } catch (Exception ex) {
            LOG.error(ex.toString(), ex);
        }
        return idNameMap;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public Map<Long, String> getRoleIdNameMap() throws Exception {
        Map<Long, String> idNameMap = null;
        try {
            List<Role> roles = roleDAO.getAll();
            if (roles != null) {
                if (!roles.isEmpty()) {
                    idNameMap = new HashMap<>();
                    for (Role role : roles) {
                        idNameMap.put(role.getId(), role.getName());
                    }
                }
            }
        } catch (Exception ex) {
            LOG.error(ex.toString(), ex);
        }
        return idNameMap;
    }

//    @Transactional(transactionType = TransactionType.READONLY)
//    public List<Flow> getFlowsByModule(int id) throws Exception {
//        Map<String,List<FlowState>> mappedstate=flowStateGroup(id);
//        return flowDAO.getByModule(id);
//    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<FlowState> getAllStates() throws Exception {
        return flowStateDAO.getAll();
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<FlowState> getStatesByFlow(int id) throws Exception {
        return flowStateDAO.getByFlow(id);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<FlowStateTransition> getTransitionsFrom(int id) throws Exception {
        return flowStateTransitionDAO.getBySource(id);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<FlowStateTransition> getAllTransitions() throws Exception {
        return flowStateTransitionDAO.getAll();
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<FlowStateTransitionRole> getRolesByTransition(int id) throws Exception {
        return flowStateTransitionRoleDAO.getByStateTransition(id);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<FlowStateTransitionRole> getAllTransitionRoles() throws Exception {
        return flowStateTransitionRoleDAO.getAll();
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<FlowState> getStatesByIdList(List<Integer> stateIds) throws Exception {
        return flowStateDAO.getStatesByIdList(stateIds);
    }

    private List<Integer>getFlowIDs(int module) throws Exception {
        List<Integer> flowIds = new ArrayList<>();
        List<Component> components = globalService.getAllObjectListByCondition(Component.class);
        if (module > 0) {
            components = components
                    .stream()
                    .filter(t -> t.getModule() == module)
                    .collect(Collectors.toList());
        }

        List<Integer> componentIds = new ArrayList<>();
        components.forEach(t -> componentIds.add(t.getId()));

        List<Flow> flows = globalService.getAllObjectListByCondition(Flow.class
                , new FlowConditionBuilder()
                        .Where()
                        .componentIn(componentIds)
                        .getCondition()
        );
        flows.forEach(t -> flowIds.add(t.getId()));
        return flowIds;

    }

    @Transactional(transactionType = TransactionType.READONLY)
    public Map<String,List<FlowState>> flowStateGroup(int module) throws Exception {

        List<Integer> flowIds=getFlowIDs(module);

        List<FlowState> flowStates=FlowRepository.getInstance().getFlowStatesByFlowIds(flowIds);
        return flowStates
                .stream()
                .collect(Collectors.groupingBy(FlowState::getViewDescription));
    }


    @Transactional(transactionType = TransactionType.READONLY)
    public Map<String,List<FlowState>> flowStateGroupByPhase(int module) throws Exception {

        List<Integer> flowIds=getFlowIDs(module);
        List<FlowState> flowStates=FlowRepository.getInstance().getFlowStatesByFlowIds(flowIds);
        Map<String, List<FlowState>> mappedFlowSataes = flowStates
                .stream()
                .collect(Collectors.groupingBy(FlowState::getPhase));
        return mappedFlowSataes;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public Map<String,List<FlowState>> flowStateGroupBySpecificPhase(int module, ApplicationPhase applicationPhase) throws Exception {

        List<Integer> flowIds=getFlowIDs(module);
        List<FlowState> flowStates=FlowRepository.getInstance().getFlowStatesByFlowIds(flowIds);
        Map<String, List<FlowState>> mappedFlowSataes = flowStates
                .stream()
                .filter(t->t.getPhase().equalsIgnoreCase(applicationPhase.getPhase()))
                .collect(Collectors.groupingBy(FlowState::getPhase));
        return mappedFlowSataes;
    }



    public static void main(String[] args) throws Exception {
        ServiceDAOFactory.getService(FlowService.class).flowStateGroup(6);
    }

}
