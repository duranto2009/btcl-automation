package distance.form;


import org.apache.struts.action.ActionForm;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.TableName;

@TableName("at_district_distance")
public class DistrictDistanceDTO extends ActionForm {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7925534850671182765L;

	private String name;
	
	@PrimaryKey
	@ColumnName("id")
	private int id;
	
	@ColumnName("rowId")
	private int sourceDistrictId;
	
	@ColumnName("colId")
	private int destinationDistrictId;
	
	@ColumnName("distance")
	private double distance;
	
	@CurrentTime
	@ColumnName("lastModificationTime")
	private long lastModificationTime;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSourceDistrictId() {
		return sourceDistrictId;
	}

	public void setSourceDistrictId(int sourceDistrictId) {
		this.sourceDistrictId = sourceDistrictId;
	}

	public int getDestinationDistrictId() {
		return destinationDistrictId;
	}

	public void setDestinationDistrictId(int destinationDistrictId) {
		this.destinationDistrictId = destinationDistrictId;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public long getLastModificationTime() {
		return lastModificationTime;
	}

	public void setLastModificationTime(long lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}

	@Override
	public String toString() {
		return "DistrictDistanceDTO [name=" + name + ", id=" + id + ", sourceDistrictId=" + sourceDistrictId
				+ ", destinationDistrictId=" + destinationDistrictId + ", distance=" + distance
				+ ", lastModificationTime=" + lastModificationTime + "]";
	}


	

}
