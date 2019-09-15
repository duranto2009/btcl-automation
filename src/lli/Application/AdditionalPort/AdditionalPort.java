package lli.Application.AdditionalPort;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lli.Application.LLIApplication;
import lombok.Data;


@Data
@TableName("at_lli_additional_port")
public class AdditionalPort {
    @PrimaryKey
    @ColumnName("id")
    long id;
    @ColumnName("applicationID")
    long applicationId;
    @ColumnName("connectionID")
    long connectionID;
    @ColumnName("portCount")
    int portCount;
    @ColumnName("officeType")
    int officeType;
    @ColumnName("loopType")
    int loopType;
    @ColumnName("suggestedDate")
    long suggestedDate;

    public AdditionalPort deserialize(LLIApplication lliApplication, int isReuse, int selectOld) {

        AdditionalPort additionalPort =new AdditionalPort();

        additionalPort.setApplicationId(lliApplication.getApplicationID());
        additionalPort.setConnectionID(lliApplication.getConnectionId());
        additionalPort.setPortCount(lliApplication.getPort());
        additionalPort.setSuggestedDate(lliApplication.getSuggestedDate());
        additionalPort.setLoopType(isReuse);
        additionalPort.setOfficeType(selectOld);
        return additionalPort;
    }
}
