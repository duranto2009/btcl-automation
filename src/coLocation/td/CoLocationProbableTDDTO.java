package coLocation.td;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("colocation_probable_td")
public class CoLocationProbableTDDTO {

    @PrimaryKey
    @ColumnName("id")
    long ID;
    @ColumnName("connection_id")
    long connectionID;
    @ColumnName("client_id")
    long clientID;
    @ColumnName("day_left")
    long dayLeft;
    @ColumnName("is_td_requested")
    boolean isTDRequested;
    @ColumnName("history_id")
    long historyID;

    String connectionName;
    String clientName;
}
