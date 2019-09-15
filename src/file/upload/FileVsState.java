package file.upload;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("at_uploaded_file_vs_state")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileVsState {

    @PrimaryKey
    @ColumnName("id")
    long id;

    @ColumnName("file_id")
    long fileId;

    @ColumnName("state_id")
    long stateId;

    @ColumnName("is_deleted")
    boolean isDeleted = false;

    @ColumnName("last_modification_time")
    @CurrentTime
    long lastModificationTime;
}
