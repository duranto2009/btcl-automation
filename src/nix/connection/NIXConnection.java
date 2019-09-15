package nix.connection;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;
import nix.office.NIXOffice;

import java.util.List;

@Data
@TableName("nix_connection")
public class NIXConnection {
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
    List<NIXOffice> nixOffices ;

}
