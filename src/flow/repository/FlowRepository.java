package flow.repository;

import flow.entity.FlowState;
import flow.entity.FlowStateTransition;
import flow.entity.FlowStateTransitionRole;
import lombok.extern.log4j.Log4j;

import java.util.*;
import java.util.stream.Collectors;

@Log4j
public class FlowRepository {
    private static FlowRepository ourInstance = new FlowRepository();

    public static FlowRepository getInstance() {
        return ourInstance;
    }

    Map<Integer, FlowState> flowStateMap;
    Map<Integer, FlowStateTransition> flowStateTransitionMap;
    Map<Integer, FlowStateTransitionRole> flowStateTransitionRoleMap;

    private FlowRepository() {
        flowStateMap = FlowStateRepository.getInstance().getUnmodifieableFlowStateMap();
        flowStateTransitionMap = FlowStateTransitionRepository.getInstance().getUnmodifieableFlowStateTransitionMap();
        flowStateTransitionRoleMap = FlowStateTransitionRoleRepository.getInstance().getUnmodifieableFlowStateTransitionRoleMap();
    }


    //API
    // Mirror : FlowService.getStateById
    public FlowState getFlowStateByFlowStateId(int flowStateId) {
        return FlowStateRepository.getInstance().getFlowStateByFlowStateId(flowStateId);
    }

    // Mirror : FlowService.getStates
    public List<FlowState> getAllFlowStates() {
        return new ArrayList<>(flowStateMap.values());
    }

    // Mirror : FlowService.getStatesByRole
    public List<FlowState> getActionableFlowStatesByRoleId(long roleId) {
        List<Integer> flowStateTransitionIds = getFlowStateTransitionIdsByRoleId(roleId);
        List<Integer> sourceFlowStateIds = FlowStateTransitionRepository.getInstance().getFlowStateTransitionsByTransitionIds(
                flowStateTransitionIds
        )
                .stream()
                .map(FlowStateTransition::getSource)
                .collect(Collectors.toList());

        return FlowStateRepository.getInstance()
                .getFlowStatesByFlowStateIds(
                        sourceFlowStateIds
                );

    }

    // Mirror : FlowService.getNextStatesFromState()
    public List<FlowState> getNextStatesByCurrentState(int currentStateId) {
        List<Integer> nextStateIds = getNextStateIdsByCurrentStateId(currentStateId);
        return FlowStateRepository.getInstance()
                .getFlowStatesByFlowStateIds(
                        nextStateIds
                );
    }

    // Mirror : FlowService.getNextStatesFromStateRole
    public List<FlowState> getNextStatesByCurrentStateAndRoleId(int currentStateId, long roleId) {
        List<Integer> flowStateTransitionIds = getFlowStateTransitionIdsByRoleId(roleId);
        List<Integer> destinationFlowStateIds = FlowStateTransitionRepository.getInstance().getFlowStateTransitionsByTransitionIds(
                flowStateTransitionIds
        )
                .stream()
                .filter(t -> t.getSource() == currentStateId)
                .map(FlowStateTransition::getDestination)
                .collect(Collectors.toList());

        return FlowStateRepository.getInstance()
                .getFlowStatesByFlowStateIds(
                        destinationFlowStateIds
                );

    }


    private List<Integer> getNextStateIdsByCurrentStateId(int currentStateId) {
        return FlowStateTransitionRepository.getInstance()
                .getFlowStateTransitionsByStateId(currentStateId, true)
                .stream()
                .map(FlowStateTransition::getDestination)
                .collect(Collectors.toList())
                ;
    }

    private void print(Collection collection) {
        if (collection.isEmpty()) {
            log.info("Collection Empty");

        }
        collection.forEach(log::info);
    }


    private List<Integer> getFlowStateTransitionIdsByRoleId(long roleId) {
        return FlowStateTransitionRoleRepository.getInstance().getAllFlowStateTransitionRolesByRoleId(roleId)
                .stream()
                .map(FlowStateTransitionRole::getFlowStateTransition)
                .collect(Collectors.toList());
    }

    public List<FlowState> getFlowStatesByFlowIds(List<Integer> flowIds) {
        Map<Integer, List<FlowState>> mappedFlowStates = getAllFlowStates().stream()
                .collect(Collectors.groupingBy(FlowState::getFlow));
        List<FlowState> flowStates = new ArrayList<>();
        flowIds
                .forEach(
                        t -> {
                            List<FlowState> flowStates1 = mappedFlowStates.getOrDefault(t, null);
                            if (flowStates1 != null) {
                                flowStates.addAll(flowStates1);
                            }
                        }
                );
        return flowStates;
    }

    public static void main(String[] args) {
//        List<FlowState> list = FlowRepository.getInstance().getActionableFlowStatesByRoleId(22020).stream().sorted(Comparator.comparing(FlowState::getId)).collect(Collectors.toList());
//        log.info("-----------------------------------");
//        log.info(list.size());
//        list.forEach(log::info);
//        log.info("-----------------------------------");


//        List<FlowState> list = FlowRepository.getInstance()
//                .getNextStatesByCurrentState(22);
//        log.info("-----------------------------------");
//        log.info(list.size());
//        list.forEach(log::info);
//        log.info("-----------------------------------");


//        List<FlowState> list = FlowRepository.getInstance()
//                .getNextStatesByCurrentStateAndRoleId(31, 22020L);
//        log.info("-----------------------------------");
//        log.info(list.size());
//        list.forEach(log::info);
//        log.info("-----------------------------------");


    }


}
