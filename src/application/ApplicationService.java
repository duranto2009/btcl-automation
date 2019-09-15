package application;

import annotation.DAO;
import annotation.Transactional;
import common.RequestFailureException;
import common.StringUtils;
import global.GlobalService;
import login.LoginDTO;
import lombok.extern.log4j.Log4j;
import requestMapping.Service;
import util.*;
import vpn.application.VPNApplication;

import java.util.*;

@Log4j
public class ApplicationService implements NavigationService {

    @Service
    private GlobalService globalService;
    @DAO
    private ApplicationDAO applicationDAO;


    @Transactional
    public void saveApplication(Application application) throws Exception {
        applicationDAO.saveApplication(application);
    }


    @Transactional(transactionType = TransactionType.READONLY)
    public <T extends Application> List<T> getAllApplicationsByAppIds(List<Long> ids, Class<T> classObject) throws Exception {

        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        return ModifiedSqlGenerator.getAllObjectListFullyPopulated(classObject,
                " WHERE "
                        + SqlGenerator.getForeignKeyColumnName(classObject)
                        + " IN " + StringUtils.getCommaSeparatedString(ids)
        );


    }

    public static void main(String[] args) throws Exception {


        ServiceDAOFactory.getService(ApplicationService.class)
                .getAllApplicationsByAppIds(Arrays.asList(1L), VPNApplication.class)
                .forEach(log::info);

        ;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public Application getApplicationByApplicationId(long appId) throws Exception {
        Application baseApplication = applicationDAO.getApplicationById(appId);
        if (baseApplication == null) {
            throw new RequestFailureException("No base application found with app id " + appId);
        }

        Class<? extends Application> classObject = (Class<? extends Application>) Class.forName(baseApplication.getClassName());

        Application childApplication = applicationDAO.getChildApplicationByParentApplicationId(appId, classObject)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RequestFailureException("No child application found with parent application id " + appId));

        ModifiedSqlGenerator.populateObjectFromOtherObject(baseApplication, childApplication, Application.class);
        return childApplication;

    }


    @Transactional(transactionType = TransactionType.READONLY)
    public List<Application> getApplicationByClientId(long clientId) throws Exception {

        List<Application> applications = globalService.getAllObjectListByCondition(Application.class,
                new ApplicationConditionBuilder()
                        .Where()
                        .clientIdEquals(clientId)
                        .getCondition());

        for (Application baseApplication : applications) {

            if (baseApplication == null) {
                throw new RequestFailureException("No base application found with app id " + baseApplication.getApplicationId());
            }

            Class<? extends Application> classObject = (Class<? extends Application>) Class.forName(baseApplication.getClassName());

            Application childApplication = applicationDAO.getChildApplicationByParentApplicationId(baseApplication.getApplicationId(), classObject)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new RequestFailureException("No child application found with parent application id " + baseApplication.getApplicationId()));

            ModifiedSqlGenerator.populateObjectFromOtherObject(baseApplication, childApplication, Application.class);
        }
        return applications;

    }

    @Override
    public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
        return getIDsWithSearchCriteria(new Hashtable(), loginDTO);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects) throws Exception {

        searchCriteria.put("moduleId", Integer.toString((int) objects[0]));
        return applicationDAO.getIDsWithSearchCriteria(searchCriteria, loginDTO);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
        return applicationDAO.getDTOs((List<Long>) recordIDs);
    }

    @Transactional
    public void updateApplicationState(long applicationID, int applicationState) throws Exception {
        applicationDAO.updateApplicatonState(applicationID, applicationState);
    }

    @Transactional
    public void updateApplicationState(Application application, int applicationState) {
        application.setApplicationState(ApplicationState.getApplicationStateByStateId(applicationState));
        ServiceDAOFactory.getService(GlobalService.class).update(application);
    }
}
