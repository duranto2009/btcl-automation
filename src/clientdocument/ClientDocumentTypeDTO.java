package clientdocument;

/**
 * @author Touhid
 *
 */
import annotation.*;
import lombok.Data;

@Data
@TableName("at_client_document_type")
public class ClientDocumentTypeDTO {

	@PrimaryKey
	@ColumnName("id")
	long Id;

	@ColumnName("docTypeId")
	long docTypeId;

	@ColumnName("name")
	String name;

	@ColumnName("isGlobal")
	boolean isGlobal;

	@ColumnName("isDeleted")
	boolean isDeleted;
	
	@ColumnName("lastModificationTime")
	long lastModificationTime;
}
