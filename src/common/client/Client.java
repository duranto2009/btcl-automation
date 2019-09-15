package common.client;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author maruf
 */
@NoArgsConstructor
@Setter
@Getter
@ToString
@TableName("at_client")
public class Client {

    @PrimaryKey
    @ColumnName("clID")
    long id;
    @ColumnName("clLoginName")
    String loginName;
    @ColumnName("clLoginPassword")
    String loginPassword;
    @ColumnName("clBalance")
    double balance;
    @ColumnName("clIsDeleted")
    boolean isDeleted;
    @ColumnName("clActivationDate")
    long activationTimestamp;
    @ColumnName("clProfilePicturePath")
    String profilePicPath;
    @ColumnName("clCurrentStatus")
    int currentStatus;
}
