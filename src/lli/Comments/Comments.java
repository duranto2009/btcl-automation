package lli.Comments;


import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@TableName("at_lli_application_comments")
@Getter
@Setter
@NoArgsConstructor
public class Comments {
    @PrimaryKey
    @ColumnName("id")
    long id;
    @ColumnName("userID")
    long userID;
    @ColumnName("stateID")
    long stateID;
    @ColumnName("applicationID")
    long applicationID;
    @ColumnName("sequenceID")
    long sequenceID;
    @ColumnName("comments")
    String comments;
    @ColumnName("submissionDate")
    long submissionDate;


}
