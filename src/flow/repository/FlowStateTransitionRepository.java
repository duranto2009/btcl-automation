package flow.repository;

import connection.DatabaseConnection;
import exception.NoDataFoundException;
import flow.entity.FlowStateTransition;
import lombok.Value;
import lombok.extern.log4j.Log4j;
import repository.Repository;
import repository.RepositoryManager;
import util.SqlGenerator;

import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Log4j
public class FlowStateTransitionRepository implements Repository {
    private static FlowStateTransitionRepository ourInstance = new FlowStateTransitionRepository();

    public static FlowStateTransitionRepository getInstance() {
        return ourInstance;
    }

    private Map<Integer, FlowStateTransition> flowStateTransitionMap;


    public Map<Integer, FlowStateTransition> getUnmodifieableFlowStateTransitionMap() {
        return Collections.unmodifiableMap(flowStateTransitionMap);
    }

    public FlowStateTransition getFlowStateTransitionByFlowStateTransitionId( int flowStateTransitionId ) {
        FlowStateTransition flowStateTransition =  flowStateTransitionMap.getOrDefault(flowStateTransitionId, null);
        if(flowStateTransition == null ) {
            throw new NoDataFoundException("No Flow State Transition Found with flow state transition id " + flowStateTransitionId);
        }
        log.info(flowStateTransition);
        return  flowStateTransition;
    }
    public List<FlowStateTransition> getFlowStateTransitionsByStateId(int stateId, boolean isSourceState) {
        return flowStateTransitionMap.values()
                .stream()
                .filter(t->isSourceState ? t.getSource() == stateId : t.getDestination() == stateId)
                .collect(Collectors.toList());
    }


    public List<FlowStateTransition> getFlowStateTransitionsByTransitionIds(List<Integer> transitionIds) {
        //        if(flowStateTransitions.isEmpty()) {
//            log.info("Empty Collections");
//        }
//        flowStateTransitions.forEach(log::info);
        return transitionIds
                .stream()
                .map(t->flowStateTransitionMap.getOrDefault(t, null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    private FlowStateTransitionRepository() {
        flowStateTransitionMap = new ConcurrentHashMap<>();
        RepositoryManager.getInstance().addRepository(this);
    }

    @Override
    public void reload(boolean reloadAll) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try{
            databaseConnection.dbOpen();
            ResultSet rs = SqlGenerator.getAllResultSetForRepository(FlowStateTransition.class, databaseConnection, reloadAll);
            while(rs.next()){
                FlowStateTransition flowStateTransition = new FlowStateTransition();
                SqlGenerator.populateObjectFromDB(flowStateTransition, rs);

                flowStateTransitionMap.putIfAbsent(flowStateTransition.getId(), flowStateTransition);
            }
        }catch(Exception e){
            log.fatal(e.getStackTrace());
        }finally {
            databaseConnection.dbClose();
        }

    }

    @Override
    public String getTableName() {
        try {
            return SqlGenerator.getTableName(FlowStateTransition.class);
        } catch (Exception e) {
            log.fatal(e.getStackTrace());
            return null;
        }
    }

    public static void main(String[] args) {
        FlowStateTransitionRepository.getInstance().getFlowStateTransitionsByTransitionIds(Arrays.asList(4,5,6));
    }
}
