package nix.ifr;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;

@Data
@TableName("nix_ifr")
public class NIXIFR {
    @PrimaryKey
    @ColumnName("id")
    long id;
    @ColumnName("application")
    long application;
    @ColumnName("office")
    long office;
    @ColumnName("pop")
    long pop;
    @ColumnName("replied")
    long replied;
    @ColumnName("selected")
    int selected;
    @ColumnName("submission_date")
    long submissionDate;
    @ColumnName("created")
    long created;
    @ColumnName("modified")
    long lastModificationTime;
    @ColumnName("is_forwarded")
    int isForwarded;
    @ColumnName("is_Ignored")
    int isIgnored;
}
