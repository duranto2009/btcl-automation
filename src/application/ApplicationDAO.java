package application;

import common.ClientDTO;
import common.ClientDTOConditionBuilder;
import common.StringUtils;
import common.repository.AllClientRepository;
import login.LoginDTO;
import util.ModifiedSqlGenerator;
import util.TimeConverter;

import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.List;

import static util.SqlGenerator.getForeignKeyColumnName;

public class ApplicationDAO {
    public void updateApplicatonState(long applicatonID, int applicationState) {
        try {
            Application application = getApplicationById(applicatonID);
            application.setApplicationState(ApplicationState.getApplicationStateByStateId(applicationState));
            ModifiedSqlGenerator.updateEntity(application);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void saveApplication(Application application) throws Exception {

        ModifiedSqlGenerator.insert(application);
    }

    Application getApplicationById(long appId) throws Exception {
        return ModifiedSqlGenerator.getObjectByID(Application.class, appId);
    }

    List<? extends Application> getChildApplicationByParentApplicationId(long appId, Class<? extends Application> classObject) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(classObject, " WHERE "
                + getForeignKeyColumnName(classObject) + " = " + appId
        );
    }

    List<? extends Application> getAllApplicationsByApplicationIdsAndClassObject(List<Long> appIds, Class<? extends Application> classObject) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(
                classObject, " WHERE "
                        + getForeignKeyColumnName(classObject) + " IN "
                        + StringUtils.getCommaSeparatedString(appIds)
        );
    }

    List<Application> getBaseApplicationsApplicationIds(List<Long> appIds) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(Application.class, " WHERE "
                + ModifiedSqlGenerator.getColumnName(Application.class, "applicationId")
                + " IN " + StringUtils.getCommaSeparatedString(appIds)

        );
    }

    public List<Application> getDTOs(List<Long> recordIDs) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(Application.class,
                new ApplicationConditionBuilder()
                        .Where()
                        .applicationIdIn(recordIDs)
                        .getCondition()

        );
    }

    public List<Long> getIDsWithSearchCriteria(Hashtable<String, String> searchCriteria, LoginDTO loginDTO) throws Exception {

        ClientDTO clientDTO =null;

        if (loginDTO.getUserID()< 0) {
             clientDTO = AllClientRepository.getInstance()
                    .getModuleClientByClientIDAndModuleID(loginDTO.getAccountID(), Integer.parseInt(searchCriteria.get("moduleId")));
            if (clientDTO.getClientID() >= 0) {
                searchCriteria.put("clientName", clientDTO.getLoginName());
            }
        }
        ResultSet resultSet = ModifiedSqlGenerator.getResultSetBySqlPair(
                new ApplicationConditionBuilder()
                        .selectApplicationId()
                        .fromApplication()
                        .Where()
                        .moduleIdEqualsString(String.valueOf(searchCriteria.get("moduleId")))
                        .applicationIdEqualsString(searchCriteria.get("applicationId"))
//                        .applicationStateLike(searchCriteria.get("applicationState"))
//                        .applicationTypeLike(searchCriteria.get("applicationType"))
                        .submissionDateGreaterThanEquals(TimeConverter.getDateFromString(searchCriteria.get("fromDate")))
                        .submissionDateLessThanEquals(TimeConverter.getDateFromString(searchCriteria.get("toDate")))
                        .clientIdInSqlPair(
                                new ClientDTOConditionBuilder()
                                        .selectClientID()
                                        .fromClientDTO()
                                        .Where()
                                        .loginNameBothLike(searchCriteria.get("clientName"))
                                        .getNullableSqlPair()
                        ) .Or(new ApplicationConditionBuilder()
                        .secondClientInSqlPair(new ClientDTOConditionBuilder()
                                .selectClientID()
                                .fromClientDTO()
                                .Where()
                                .loginNameBothLike(searchCriteria.get("clientName"))
                                .getNullableSqlPair())
                        .getNullableSqlPair())
                        .clientIdEqualsString(searchCriteria.get("clientID"))
                        .orderByapplicationIdDesc()
                        .getSqlPair()
        );



        return ModifiedSqlGenerator.getSingleColumnListByResultSet(resultSet, Long.class);
    }
}
