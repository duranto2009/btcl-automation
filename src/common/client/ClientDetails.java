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
@TableName("at_client_details")
public class ClientDetails {

    @PrimaryKey
    @ColumnName("vclID")
    long id;
    @ColumnName("vclClientID")
    long clientId;
    @ColumnName("vclModuleID")
    int moduleId;
    @ColumnName("vclIdentity")
    String identity;
    @ColumnName("vclCurrentStatus")
    int currentStatus;
    @ColumnName("vclRegType")
    int regType;
    @ColumnName("vclConEligibleForDiscount")
    boolean isEligibleForDiscount;
    @ColumnName("vclRegistrationCategory")
    long regCategory;
    @ColumnName("vclIsDeleted")
    boolean isDeleted;
    @ColumnName("vclActivationDate")
    long activationDateTimestamp;
    @ColumnName("vclClientType")
    int clientType;
}
