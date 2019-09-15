package distance.form;

import org.apache.struts.action.ActionForm;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.TableName;


@TableName("at_union_distance")
public class UnionDistanceDTO extends ActionForm{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6902525331722592598L;

	@PrimaryKey
	@ColumnName("unionID")
	long id;
	
	@ColumnName("upUpazilaID")
	long parentUpazilaID;
	
	@ColumnName("unionUnionID")
	long unionID;
	
	@ColumnName("unionDistance")
	int distance;
	
	@CurrentTime
	@ColumnName("unionLastModificationTime")
	long lastModificationTime;

	String unionName;
	
	
	
	public String getUnionName() {
		return unionName;
	}

	public void setUnionName(String unionName) {
		this.unionName = unionName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getParentUpazilaID() {
		return parentUpazilaID;
	}

	public void setParentUpazilaID(long parentUpazilaID) {
		this.parentUpazilaID = parentUpazilaID;
	}

	public long getUnionID() {
		return unionID;
	}

	public void setUnionID(long unionID) {
		this.unionID = unionID;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
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
		return "UnionDistanceDTO [id=" + id + ", parentUpazilaID=" + parentUpazilaID + ", unionID=" + unionID
				+ ", distance=" + distance + ", lastModificationTime=" + lastModificationTime + "]";
	}
	
	
	
	
}
