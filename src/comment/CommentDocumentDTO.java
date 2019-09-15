package comment;

import com.mysql.jdbc.Blob;

import annotation.ColumnName;
import annotation.TableName;


@TableName("")
public class CommentDocumentDTO {
	@ColumnName("")
	long ID;
	@ColumnName("")
	Blob document;
	@ColumnName("")
	long commentID;
}
