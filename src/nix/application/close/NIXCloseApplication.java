package nix.application.close;


import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;

@Data
@TableName("nix_close_application")
public class NIXCloseApplication {
    @PrimaryKey
    @ColumnName("id")
    long id;
    @ColumnName("parent_application")
    long parent;
    @ColumnName("port_id")
    long portId;
    @ColumnName("port_type")
    int portType;
}
