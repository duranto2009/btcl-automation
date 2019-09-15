package nix.application.office;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import nix.application.localloop.NIXApplicationLocalLoop;

import java.util.List;

@Getter
@Setter
@TableName("nix_application_office")
public class NIXApplicationOffice {

    @PrimaryKey
    @ColumnName("id")
    long id;
    @ColumnName("name")
    String name;
    @ColumnName("address")
    String address;
    @ColumnName("application")
    long application;
    @ColumnName("history")
    long history;
    @ColumnName("created")
    long created;
    @ColumnName("modified")
    long lastModificationTime;

    List<NIXApplicationLocalLoop> loops;
}
