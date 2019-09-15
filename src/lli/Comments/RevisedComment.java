package lli.Comments;


import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@TableName("at_lli_connection_revise_client_comments")
@Getter
@Setter
@NoArgsConstructor
public class RevisedComment {
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
