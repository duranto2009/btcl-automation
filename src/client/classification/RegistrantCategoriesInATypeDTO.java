/**
 * 
 */
package client.classification;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.*;

/**
 * @author Touhid
 *
 */
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("at_reg_category_vs_type")
public class RegistrantCategoriesInATypeDTO {

	@PrimaryKey
	@ColumnName("id")
	long id;

	@ColumnName("regTypeInAModuleId")
	long registrantTypeInAModuleId;

	@ColumnName("registrantCategoryId")
	long registrantCategoryId;

	@ColumnName("tariffCatId")
	int tariffCatId;

	@ColumnName("isDeleted")
	boolean isDeleted;

	@ColumnName("lastModificationTime")
	long lastModificationTime;

}
