package lli.configuration.ofc.cost;


import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName(value="lli_local_loop_base_charge")
@EqualsAndHashCode
public class OfcInstallationCostDTO {
	@PrimaryKey
	@ColumnName(value="id")
	Long id;
	
	@ColumnName(value="fiberLength")
	Integer fiberLength;
	
	@ColumnName(value="oneTimeCost")
	Integer oneTimeCost;
	
	@ColumnName(value="fiberCost")
	Integer fiberCost;
	
	@ColumnName(value="createdDate")
	Long createdDate;
	
	@ColumnName(value="applicableFrom")
	Long applicableFrom;
}
