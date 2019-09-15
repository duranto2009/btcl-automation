package distance.form;

import org.apache.struts.action.ActionForm;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.TableName;

@TableName("at_upazila_distance")
public class UpazilaDistanceDTO extends ActionForm{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1586113634764433087L;
	
	@PrimaryKey
	@ColumnName("upID")
	long id;
	
	@ColumnName("upDistrictID")
	long parentDistrictId;
	
	@ColumnName("upUpazilaID")
	long upazilaId;
	
	String upazilaName;

	@ColumnName("upDistance")
	int distance;
	@CurrentTime
	@ColumnName("upLastModificationTime")
	long lastModificationTime;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getParentDistrictId() {
		return parentDistrictId;
	}
	public void setParentDistrictId(long parentDistrictId) {
		this.parentDistrictId = parentDistrictId;
	}

	public double getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public long getUpazilaId() {
		return upazilaId;
	}
	public void setUpazilaId(long upazilaId) {
		this.upazilaId = upazilaId;
	}
	public String getUpazilaName() {
		return upazilaName;
	}
	public void setUpazilaName(String upazilaName) {
		this.upazilaName = upazilaName;
	}
	
	public long getLastModificationTime() {
		return lastModificationTime;
	}
	public void setLastModificationTime(long lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}
	@Override
	public String toString() {
		return "UpazilaDTO [id=" + id + ", parentDistrictId=" + parentDistrictId + ", upazilaId=" + upazilaId
				+ ", upazilaName=" + upazilaName + ", distance=" + distance + "]";
	}
	
	
	
}
