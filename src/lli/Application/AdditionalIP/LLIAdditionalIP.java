package lli.Application.AdditionalIP;

import annotation.ColumnName;
import annotation.ForeignKeyName;
import annotation.PrimaryKey;
import annotation.TableName;
import lli.Application.LLIApplication;
import lombok.Data;

@Data
@TableName("at_lli_additional_ip")
public class LLIAdditionalIP{
    @PrimaryKey
    @ColumnName("id")
    long id;
    @ColumnName("applicationID")
    long applicationId;
    @ColumnName("connectionID")
    long connectionID;
    @ColumnName("ipCount")
    int ipCount;
    @ColumnName("suggestedDate")
    long suggestedDate;

    public LLIAdditionalIP deserialize(LLIApplication lliApplication) {
        LLIAdditionalIP lliAdditionalIP = new LLIAdditionalIP();
        lliAdditionalIP.setConnectionID(lliAdditionalIP.getConnectionID());
        lliAdditionalIP.setApplicationId(lliApplication.getApplicationID());
        lliAdditionalIP.setIpCount(lliApplication.getIp());
        lliApplication.setSuggestedDate(lliApplication.getSuggestedDate());
        return lliAdditionalIP;
    }
}
