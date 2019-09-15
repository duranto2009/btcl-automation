package flow.dao;

import connection.DatabaseConnection;
import databasemanager.DatabaseManager;
import flow.entity.FlowState;
import flow.entity.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClassicDAO {

    protected static final Logger logger = LoggerFactory.getLogger(ClassicDAO.class);

    private static final String SQL_GET_STATE_BY_ROLE_ID = "SELECT fs.id AS id, fs.name AS name, fs.description AS description, fs.flow AS flow, " +
            "fs.color AS color, fs.view_description AS view_description " +
            "FROM flow_state fs " +
            "INNER JOIN flow_state_transition fst ON fst.source = fs.id " +
            "INNER JOIN flow_state_transition_role fstr ON fstr.flow_state_transition = fst.id " +
            "WHERE fstr.role = ?";

    private static final String SQL_GET_STATE_BY_ROLE_NAME = "SELECT fs.id AS id, fs.name AS name, fs.description AS description, fs.flow AS flow, " +
            "fs.color AS color, fs.view_description AS view_description " +
            "FROM flow_state fs " +
            "INNER JOIN flow_state_transition fst ON fst.source = fs.id " +
            "INNER JOIN flow_state_transition_role fstr ON fstr.flow_state_transition = fst.id " +
            "INNER JOIN adrole ar ON ar.rlRoleID = fstr.role " +
            "WHERE ar.rlRoleName = ?";

    private static final String SQL_GET_NEXT_STATES_BY_STATE_ID = "SELECT fs.id as id, fs.name as name, fs.description, fs.flow as flow, " +
            "fs.color AS color, fs.view_description AS view_description " +
            "FROM flow_state fs " +
            "JOIN flow_state_transition fst ON fst.destination = fs.id " +
            "WHERE fst.source = ?";

    private static final String SQL_GET_NEXT_STATES_BY_STATE_ID_AND_ROLE_ID = "SELECT fs.id as id, fs.name as name, fs.description, fs.flow as flow, " +
            "fs.color AS color, fs.view_description AS view_description " +
            "FROM flow_state fs " +
            "JOIN flow_state_transition fst ON fst.destination = fs.id " +
            "JOIN flow_state_transition_role fstr ON fstr.flow_state_transition = fst.id " +
            "WHERE fst.source = ? AND fstr.role = ?";

    private static final String SQL_GET_NEXT_STATES_BY_STATE_NAME = "SELECT fs.id as id, fs.name as name, fs.description, fs.flow as flow, " +
            "fs.color AS color, fs.view_description AS view_description " +
            "FROM flow_state fs " +
            "JOIN flow_state_transition fst ON fst.destination = fs.id " +
            "JOIN flow_state fs2 ON fs2.id = fst.source " +
            "WHERE fs2.name = ?";

    private static final String SQL_GET_ROLES_BY_NEXT_STATE = "SELECT * FROM adrole WHERE rlRoleID in (SELECT role FROM flow_state_transition_role WHERE flow_state_transition in (SELECT id FROM flow_state_transition WHERE destination = ?))";

    private static final String SQL_GET_STATE = "SELECT * FROM flow_state WHERE id = ?";
    private static final String SQL_GET_ALL_STATE = "SELECT * FROM flow_state";

    private static final String SQL_GET_TERMINAL_STATES = "SELECT * FROM flow_state WHERE id NOT IN (SELECT source FROM flow_state_transition)";

    public void test() {
        Connection conn = null;
        try {
            conn = DatabaseManager.getInstance().getConnection();
            conn.setAutoCommit(false);
            // Do stuff
            conn.commit();
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Exception ex2) {
                    logger.error(ex.toString(), ex);
                }
            }
        } finally {
            if (conn != null) {
                try {
                    DatabaseManager.getInstance().freeConnection(conn);
                } catch (Exception ex3) {
                    logger.error(ex3.toString(), ex3);
                }
            }
        }
    }

    public List<FlowState> getStatesByRoleId(int id) {
        List<FlowState> flowStates = new ArrayList<>();
        DatabaseConnection conn = new DatabaseConnection();
        try {
            conn.dbOpen();
            PreparedStatement preparedStatement = conn.getNewPrepareStatement(SQL_GET_STATE_BY_ROLE_ID);
            preparedStatement.setInt(1, id);
//            logger.info(preparedStatement.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                flowStates.add(new FlowState(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("flow"),
                        resultSet.getString("color"),
                        resultSet.getString("view_description")
                ));
            }
            conn.closeStatements();
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
        } finally {
            conn.dbClose();
        }
        return flowStates;
    }

    public List<FlowState> getStatesByRoleName(String name) {
        List<FlowState> flowStates = new ArrayList<>();
        DatabaseConnection conn = new DatabaseConnection();
        try {
            conn.dbOpen();
            PreparedStatement preparedStatement = conn.getNewPrepareStatement(SQL_GET_STATE_BY_ROLE_NAME);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                flowStates.add(new FlowState(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("flow"),
                        resultSet.getString("color"),
                        resultSet.getString("view_description")
                ));
            }
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
        } finally {
            conn.dbClose();
        }
        return flowStates;
    }

    public List<FlowState> getNextStatesFromCurentStateId(int stateId) {
        List<FlowState> flowStates = new ArrayList<>();
        DatabaseConnection conn = new DatabaseConnection();
        try {
            conn.dbOpen();
            PreparedStatement preparedStatement = conn.getNewPrepareStatement(SQL_GET_NEXT_STATES_BY_STATE_ID);
            preparedStatement.setInt(1, stateId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                flowStates.add(new FlowState(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("flow"),
                        resultSet.getString("color"),
                        resultSet.getString("view_description")
                ));
            }
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
        } finally {
            conn.dbClose();
        }
        return flowStates;
    }

    public List<FlowState> getNextStatesFromCurentStateIdAndRoleId(int stateId, int roleId) {
        List<FlowState> flowStates = new ArrayList<>();
        DatabaseConnection conn = new DatabaseConnection();
        try {
            conn.dbOpen();
            PreparedStatement preparedStatement = conn.getNewPrepareStatement(SQL_GET_NEXT_STATES_BY_STATE_ID_AND_ROLE_ID);
            preparedStatement.setInt(1, stateId);
            preparedStatement.setInt(2, roleId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                flowStates.add(new FlowState(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("flow"),
                        resultSet.getString("color"),
                        resultSet.getString("view_description")
                ));
            }
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
        } finally {
            conn.dbClose();
        }
        return flowStates;
    }

    public List<FlowState> getNextStatesFromCurentStateName(String stateName) {
        List<FlowState> flowStates = new ArrayList<>();
        DatabaseConnection conn = new DatabaseConnection();
        try {
            conn.dbOpen();
            PreparedStatement preparedStatement = conn.getNewPrepareStatement(SQL_GET_NEXT_STATES_BY_STATE_NAME);
            preparedStatement.setString(1, stateName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                flowStates.add(new FlowState(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("flow"),
                        resultSet.getString("color"),
                        resultSet.getString("view_description")
                ));
            }
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
        } finally {
            conn.dbClose();
        }
        return flowStates;
    }

    public FlowState getStateById(int id) {
        FlowState flowState = null;
        DatabaseConnection conn = new DatabaseConnection();
        try {
            conn.dbOpen();
            PreparedStatement preparedStatement = conn.getNewPrepareStatement(SQL_GET_STATE);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.first()) {
                flowState = new FlowState(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("flow"),
                        resultSet.getString("color"),
                        resultSet.getString("view_description")
                );
            }
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
        } finally {
            conn.dbClose();
        }
        return flowState;
    }

    public List<FlowState> getStates() {
        List<FlowState> flowStates = new ArrayList<>();
        DatabaseConnection conn = new DatabaseConnection();
        try {
            conn.dbOpen();
            PreparedStatement preparedStatement = conn.getNewPrepareStatement(SQL_GET_ALL_STATE);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                FlowState flowState = new FlowState(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("flow"),
                        resultSet.getString("color"),
                        resultSet.getString("view_description")
                );
                flowStates.add(flowState);
            }
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
        } finally {
            conn.dbClose();
        }
        return flowStates;
    }

    public List<Role> getRolesByNextState(int nextState) {
        List<Role> roles = new ArrayList<>();
        DatabaseConnection conn = new DatabaseConnection();
        try {
            conn.dbOpen();
            PreparedStatement preparedStatement = conn.getNewPrepareStatement(SQL_GET_ROLES_BY_NEXT_STATE);
            preparedStatement.setInt(1, nextState);
            ResultSet resultSet = preparedStatement.executeQuery();
            Role role;
            while (resultSet.next()) {
                role = new Role(
                        resultSet.getLong("rlRoleID"),
                        resultSet.getInt("rlLevel"),
                        resultSet.getString("rlRoleName"),
                        resultSet.getString("rlRoleDesc"),
                        resultSet.getLong("rlMaxClientPerDay"),
                        resultSet.getBoolean("rlRestrictedToOwn"),
                        resultSet.getLong("rlLastModificationTime"),
                        resultSet.getBoolean("rlIsDeleted"),
                        resultSet.getLong("rlParentRoleID")
                );
                roles.add(role);
            }
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
        } finally {
            conn.dbClose();
        }
        return roles;
    }

    public List<FlowState> getTerminalStates() {
        List<FlowState> flowStates = new ArrayList<>();
        DatabaseConnection conn = new DatabaseConnection();
        try {
            conn.dbOpen();
            PreparedStatement preparedStatement = conn.getNewPrepareStatement(SQL_GET_TERMINAL_STATES);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                flowStates.add(new FlowState(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("flow"),
                        resultSet.getString("color"),
                        resultSet.getString("view_description")
                ));
            }
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
        } finally {
            conn.dbClose();
        }
        return flowStates;
    }
}
