package client.temporaryClient;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@TableName("temporary_client")
public class TemporaryClient {

    @ColumnName("id")
    @PrimaryKey
    long id;

    @ColumnName("emailId")
    String emailId;

    @ColumnName("mobileNumber")
    String mobileNumber;

    @ColumnName("countryCode")
    String countryCode;

    @ColumnName("moduleId")
    int moduleId;

    @ColumnName("clientType")
    int clientType;

    @ColumnName("isEmailVerified")
    boolean isEmailVerified;

    @ColumnName("isMobileNumberVerified")
    boolean isMobileNumberVerified;

    @ColumnName("activationTime")
    long activationTime = System.currentTimeMillis();
}
