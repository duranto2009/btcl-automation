/**
 * 
 */
package client.classification;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Touhid
 *
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("at_client_tariff_category")
public class ClientTariffCategoryDTO {
	@PrimaryKey
	@ColumnName("id")
	long Id;

	@ColumnName("tariffCatId")
	long tariffCatId;
	
	@ColumnName("categoryName")
    String name;
	
	@ColumnName("isDeleted")
	boolean isDeleted;
	
	@ColumnName("lastModificationTime")
	long lastModificationTime;


	@Override
	public String toString() {
		return "ClientRegistrantCategoryDTO [Id=" + Id + ", name=" + name + ", isDeleted=" + isDeleted
				+ ", lastModificationTime=" + lastModificationTime + "]";
	}

}
