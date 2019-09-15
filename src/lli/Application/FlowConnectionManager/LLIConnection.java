package lli.Application.FlowConnectionManager;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lli.Application.Office.Office;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import report.Display;
import report.LLIConnectionStatusConverter;

import java.util.List;
@Data
@Getter
@Setter
@NoArgsConstructor
@TableName("at_lli_connection")
public class LLIConnection {

    @PrimaryKey
    @ColumnName("historyID")
    long historyID;
    @ColumnName("ID")
    long ID;
    @ColumnName("clientID")
    long clientID;

    @ColumnName("name")
    String name = "";

    @ColumnName("connectionType")
    int connectionType;
    @ColumnName("costChartID")
    long costChartID;
    @ColumnName("activeFrom")
    long activeFrom;
    @ColumnName("activeTo")
    long activeTo;
    @ColumnName("validFrom")
    long validFrom;
    @ColumnName("validTo")
    long validTo;
    @ColumnName("bandwidth")
    double bandwidth;
    @Display(LLIConnectionStatusConverter.class)
    @ColumnName("status")
    int status;
    @ColumnName("startDate")
    long startDate;
    @ColumnName("incident")
    int incident;

    @ColumnName("zoneID")
    int zoneID;

    @ColumnName("discountRate")
    double discountRate = 0.0;	//new

    List<Office> offices ;

    public List<Office> getOffices() {
        return offices;
    }

    public void setOffices(List<Office> offices) {
        this.offices = offices;
    }
}
