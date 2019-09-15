package crm;

import java.util.ArrayList;
import java.util.List;

public class CrmClientComplainToCrmComplainMapper {
	public Long getCrmEmployeeID() {
		return crmEmployeeID;
	}
	public void setCrmEmployeeID(Long crmEmployeeID) {
		this.crmEmployeeID = crmEmployeeID;
	}
	long clientComplainID;
	Long crmEmployeeID;
    List<CrmComplainDTO> crmComplainDTOs = new ArrayList<>();
	public long getClientComplainID() {
		return clientComplainID;
	}
	public void setClientComplainID(long clientComplainID) {
		this.clientComplainID = clientComplainID;
	}
	public List<CrmComplainDTO> getCrmComplainDTOs() {
		return crmComplainDTOs;
	}
	public void setCrmComplainDTOs(List<CrmComplainDTO> crmComplainDTOs) {
		this.crmComplainDTOs = crmComplainDTOs;
	}
}
