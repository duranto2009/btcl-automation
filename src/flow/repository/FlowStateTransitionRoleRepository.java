package flow.repository;

import connection.DatabaseConnection;
import exception.NoDataFoundException;
import flow.entity.FlowStateTransitionRole;
import lombok.extern.log4j.Log4j;
import repository.Repository;
import repository.RepositoryManager;
import util.ModifiedSqlGenerator;
import util.SqlGenerator;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Log4j
public class FlowStateTransitionRoleRepository implements Repository {
    private static FlowStateTransitionRoleRepository ourInstance = new FlowStateTransitionRoleRepository();

    public static FlowStateTransitionRoleRepository getInstance() {
        return ourInstance;
    }

    private Map<Integer, FlowStateTransitionRole> flowStateTransitionRoleMap;

    public Map<Integer, FlowStateTransitionRole> getUnmodifieableFlowStateTransitionRoleMap() {
        return Collections.unmodifiableMap(flowStateTransitionRoleMap);
    }

    private FlowStateTransitionRoleRepository() {
        flowStateTransitionRoleMap = new ConcurrentHashMap<>();
        RepositoryManager.getInstance().addRepository(this);
    }

    public FlowStateTransitionRole getFlowStateTransitionRoleById(int id) {
        FlowStateTransitionRole flowStateTransitionRole = flowStateTransitionRoleMap.getOrDefault( id, null);
        if(flowStateTransitionRole == null) {
            throw new NoDataFoundException(" No Flow State Transition Role Entry Found with PK " + id);
        }
        log.info(flowStateTransitionRole);
        return flowStateTransitionRole;
    }

    public List<FlowStateTransitionRole> getAllFlowStateTransitionRolesByRoleId(long roleId) {
        //        if(flowStateTransitionRoles.isEmpty()) {
//            log.info("Empty Collections");
//        }
//        flowStateTransitionRoles.forEach(log::info);
        return flowStateTransitionRoleMap.values()
                .stream()
                .filter(t->t.getRole() == roleId)
                .collect(Collectors.toList());
    }

    @Override
    public void reload(boolean reloadAll) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try{
            databaseConnection.dbOpen();
            ResultSet rs = SqlGenerator.getAllResultSetForRepository(FlowStateTransitionRole.class, databaseConnection, reloadAll);
            while(rs.next()){
                FlowStateTransitionRole flowStateTransitionRole = new FlowStateTransitionRole();
                ModifiedSqlGenerator.populateObjectFromDB(flowStateTransitionRole, rs);

                flowStateTransitionRoleMap.putIfAbsent(flowStateTransitionRole.getId(), flowStateTransitionRole);
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
            return SqlGenerator.getTableName(FlowStateTransitionRole.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        FlowStateTransitionRoleRepository.getInstance().getAllFlowStateTransitionRolesByRoleId(-1);
    }
}
