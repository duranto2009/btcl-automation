package vpn.connection;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;
import entity.office.Office;

import java.util.List;

@Data
@TableName("nix_connection")
public class Connection {
    @PrimaryKey
    @ColumnName("id")
    long id;
    @ColumnName("application_id")
    long application;
    @ColumnName("status")
    int status;
    @ColumnName("client")
    long client;
    @ColumnName("name")
    String name;
    @ColumnName("connection")
    long connectionId;
    @ColumnName("active_from")
    long activeFrom;
    @ColumnName("active_to")
    long activeTo;
    @ColumnName("valid_from")
    long validFrom;
    @ColumnName("valid_to")
    long validTo;
    @ColumnName("incident")
    int incidentId;
    @ColumnName("start_date")
    long startDate;

    @ColumnName("zone")
    int zone;
    List<Office> nixOffices ;

}
