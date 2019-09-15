package flow.repository;

import connection.DatabaseConnection;
import exception.NoDataFoundException;
import flow.entity.FlowState;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import repository.Repository;
import repository.RepositoryManager;
import util.ModifiedSqlGenerator;
import util.SqlGenerator;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Log4j
public class FlowStateRepository implements Repository {
    private static FlowStateRepository ourInstance = new FlowStateRepository();
    private Map<Integer, FlowState> flowStateMap;

    public static FlowStateRepository getInstance() {
        return ourInstance;
    }

    public Map<Integer, FlowState> getUnmodifieableFlowStateMap(){
        return Collections.unmodifiableMap(flowStateMap);
    }

    public List<FlowState> getFlowStatesByFlowStateIds ( List<Integer> flowStateIds) {
        //        if(flowStates.isEmpty()){
//            log.info("Collection Empty");
//        }
//        flowStates.forEach(log::info);
        return flowStateIds
                .stream()
                .map(t->flowStateMap.getOrDefault(t, null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    public FlowState getFlowStateByFlowStateId(int flowStateId) {
        FlowState flowState = flowStateMap.getOrDefault(flowStateId, null);
        if(flowState == null){
            throw new NoDataFoundException("No Flow State Found with Flow State Id " + flowStateId);
        }
//        log.info(flowState);
        return flowState;
    }

    private FlowStateRepository() {
        flowStateMap = new ConcurrentHashMap<>();
        RepositoryManager.getInstance().addRepository(this, true);

    }


    @Override
    public void reload(boolean reloadAll) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try{
            databaseConnection.dbOpen();
            ResultSet rs = SqlGenerator.getAllResultSetForRepository(FlowState.class, databaseConnection, reloadAll);
            while(rs.next()){
                FlowState flowState = new FlowState();
                ModifiedSqlGenerator.populateObjectFromDB(flowState, rs);

                flowStateMap.putIfAbsent(flowState.getId(), flowState);
            }

        }catch (Exception e){
            log.fatal(e.getStackTrace());
        }finally {
            databaseConnection.dbClose();
        }

    }

    @Override
    public String getTableName() {
        try {
            return ModifiedSqlGenerator.getTableName(FlowState.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        FlowStateRepository.getInstance().getFlowStateByFlowStateId(15);
        FlowStateRepository.getInstance();
    }
}
