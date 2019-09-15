package nix.office;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;
import nix.application.office.NIXApplicationOffice;
import nix.localloop.NIXLocalLoop;

import java.util.List;

@Data
@TableName("nix_office")
public class NIXOffice {
    @PrimaryKey
    @ColumnName("id")
    long id;
    @ColumnName("application")
    long appication;
    @ColumnName("connection")
    long connection;
    @ColumnName("name")
    String name;
    @ColumnName("application_office")
    long application_offfice;

    NIXApplicationOffice nixApplicationOffice;

    List<NIXLocalLoop> localLoops;
}
