package entity.comment;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("comment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @PrimaryKey
    @ColumnName("id")
    long id;

    @ColumnName("moduleId")
    int moduleId;

    @ColumnName("userId")
    long userId;

    String commentProviderName;

    @ColumnName("stateId")
    int stateId;
//
//    @ColumnName("applicationId")
//    long applicationId;

    @ColumnName("entityId")
    long entityId;

    @ColumnName("sequenceId")
    long sequenceId;

    @ColumnName("comment")
    String comment;

    @ColumnName("submissionDate")
    long submissionDate;


}
