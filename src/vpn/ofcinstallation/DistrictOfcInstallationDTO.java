package vpn.ofcinstallation;


import org.apache.struts.action.ActionForm;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.TableName;

@TableName("at_ofc_install_cost")
public class DistrictOfcInstallationDTO extends ActionForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6638616051340204144L;

	@PrimaryKey
	@ColumnName("id")
	private int id;
	
	@ColumnName("distID")
	private long districtID;
	
	@ColumnName("installCost")
	private double installationCost;
	
	@CurrentTime
	@ColumnName("lastModificationTime")
	private long lastModificationTime;
	
	private String districtName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getDistrictID() {
		return districtID;
	}

	public void setDistrictID(long districtID) {
		this.districtID = districtID;
	}

	public double getInstallationCost() {
		return installationCost;
	}

	public void setInstallationCost(double installationCost) {
		this.installationCost = installationCost;
	}

	public long getLastModificationTime() {
		return lastModificationTime;
	}

	public void setLastModificationTime(long lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	@Override
	public String toString() {
		return "DistrictOfcInstallationDTO [id=" + id + ", districtID=" + districtID + ", installationCost="
				+ installationCost + ", lastModificationTime=" + lastModificationTime + ", districtName=" + districtName
				+ "]";
	}

	
	
}
