package application;

import annotation.*;
import common.ClientDTO;
import lombok.Data;


@TableName("application") @Data
public class Application {
    @PrimaryKey @ColumnName("application_id")
    long applicationId;

    @ColumnName("client_id") @SearchFieldFromReferenceTable(operator = "LIKE", entityClass = ClientDTO.class, entityColumnName = "loginName")
    long clientId;

    @ColumnName("application_type")
    ApplicationType applicationType;

    @ColumnName("application_state")
    ApplicationState applicationState;

    @ColumnName("user_id")
    Long userId;

    @ColumnName("module_id")
    int moduleId;

    @ColumnName("submission_date")
    long submissionDate = System.currentTimeMillis();

    @ColumnName("suggested_date")
    long suggestedDate;

    @ColumnName("created_date")
    long createdDate = System.currentTimeMillis();

    @CurrentTime @ColumnName("last_modification_time")
    long lastModificationTime;

    @ColumnName("is_service_started")
    int isServiceStarted;

    @ColumnName("class_name")
    String className;

//  2nd client add in application table  3/5/19 @jami
    @ColumnName("second_client")
    long secondClient;

    String stateDescription;
    String color;
    boolean hasPermission = false;

    String comment;
    String description;

}
