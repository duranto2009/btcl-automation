package costConfig;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;

@Data
@TableName("at_cost_chart_category")
public class CategoryDTO {
	
	
	@ColumnName("id")
	@PrimaryKey
	long id;
	
	@ColumnName("moduleID")
	int moduleID;
	
	@ColumnName("categoryName")
	String categoryName;
	
	@ColumnName("isDeleted")
	boolean isDeleted;
	
	@CurrentTime
	@ColumnName("lastModificationTime")
	long lastModificationTime;
}
