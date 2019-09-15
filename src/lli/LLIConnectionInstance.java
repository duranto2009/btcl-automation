package lli;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import annotation.*;
import dataConverter.DateTime.DateToEndOfTheDayConverter;
import dataConverter.DateTime.DateToStartOfTheDayConverter;
import lombok.Data;
import report.Display;
import report.LLIConnectionStatusConverter;
import util.TimeConverter;

@Data
@TableName("at_lli_connection")
public class LLIConnectionInstance{
	
	
	@PrimaryKey
	@ColumnName("historyID")
	long historyID;
	@ColumnName("ID")
	long ID;
	@ColumnName("clientID")
	long clientID;
	
	@ColumnName("name")
	String name = "";
	
	@ColumnName("connectionType")
	int connectionType;
	@ColumnName("costChartID")
	long costChartID;

	@ColumnName("activeFrom")
	@ManipulateData(DateToStartOfTheDayConverter.class)
	long activeFrom;

	@ColumnName("activeTo")
    @ManipulateData(DateToEndOfTheDayConverter.class)
	long activeTo;

	@ColumnName("validFrom")
	@ManipulateData(DateToStartOfTheDayConverter.class)
	long validFrom;

	@ColumnName("validTo")
	@ManipulateData(DateToEndOfTheDayConverter.class)
	long validTo;

	@ColumnName("bandwidth")
	double bandwidth;
	@Display(LLIConnectionStatusConverter.class)
	@ColumnName("status")
	int status;

	@ColumnName("startDate")
    @ManipulateData(DateToStartOfTheDayConverter.class)
	long startDate;

	@ColumnName("incident")
	int incident;

	@ColumnName("zoneID")
	int zoneID;
	
	@ColumnName("discountRate")
	double discountRate = 0.0;	//new
	
	

	List<LLIOffice> lliOffices ;
	public List<LLIOffice> getLliOffices() {
		return lliOffices;
	}
	public void setLliOffices(List<LLIOffice> lliOffices) {
		this.lliOffices = lliOffices;
	}
	
	
	@Override
	public String toString() {
		return "LLIConnectionInstance [historyID=" + historyID + ", ID=" + ID + ", name=" + name + ", connectionType="
				+ connectionType + ", costChartID=" + costChartID + ", activeFrom=" + TimeConverter.getDateTimeStringByMillisecAndDateFormat(activeFrom, "dd/MM/yyyy hh:mm a") + ", activeTo="
				+ TimeConverter.getDateTimeStringByMillisecAndDateFormat(activeTo, "dd/MM/yyyy hh:mm a") + ", validFrom=" + validFrom + ", validTo=" + validTo + ", lliOffices=" + lliOffices + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (ID ^ (ID >>> 32));
		long temp;
		temp = Double.doubleToLongBits(bandwidth);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((lliOffices == null) ? 0 : lliOffices.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LLIConnectionInstance other = (LLIConnectionInstance) obj;
		if (ID != other.ID)
			return false;
		if (Double.doubleToLongBits(bandwidth) != Double.doubleToLongBits(other.bandwidth))
			return false;
		if (lliOffices == null) {
			if (other.lliOffices != null)
				return false;
		} else if (!new HashSet<>(lliOffices).equals(new HashSet<>(other.getLliOffices())))
			return false;
		return true;
	}
	
	
	public static final int CONNECTION_TYPE_REGULAR = 1;
	public static final int CONNECTION_TYPE_CACHE = 2;
	
	public static final int STATUS_ACTIVE = 1;
	public static final int STATUS_TD = 2;
	public static final int STATUS_CLOSED = 3;
	public static final int OWNERSHIP_CHANGED = 5;
	
	public static final int INCIDENT_NEW_CONNECTION = 1;
	public static final int INCIDENT_BANDWIDTH_UPGRADE = 2;
	public static final int INCIDENT_BANDWIDTH_DOWNGRADE = 3;
	public static final int INCIDENT_PORT_CHANGE = 4;
	public static final int INCIDENT_SWITCH_CHANGE = 5;
	public static final int INCIDENT_POP_SHIFT = 6;
	public static final int INCIDENT_OFFICE_SHIFT = 7;
	public static final int INCIDENT_NEW_LOCAL_LOOP = 8;
	public static final int INCIDENT_NEW_PORT = 9;
	
	@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
	public static Map<Integer, String> incidentMap = new HashMap() {{
		put(INCIDENT_NEW_CONNECTION, "New Connection");
		put(INCIDENT_BANDWIDTH_UPGRADE, "Upgrade Bandwidth");
		put(INCIDENT_BANDWIDTH_DOWNGRADE, "Downgrade Bandwidth");
		put(INCIDENT_PORT_CHANGE, "Change Port");
		put(INCIDENT_SWITCH_CHANGE, "Change Switch/Router");
		put(INCIDENT_POP_SHIFT, "Shift PoP");
		put(INCIDENT_OFFICE_SHIFT, "Shift Office");
		put(INCIDENT_NEW_LOCAL_LOOP, "New Local Loop");
		put(INCIDENT_NEW_PORT, "New Port");
	}};
	
	@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
	public static Map<Integer, String> statusMap = new HashMap() {{
		put(STATUS_ACTIVE, "Active");
		put(STATUS_TD, "Temporarily Disconnected");
		put(STATUS_CLOSED, "Closed");
		put(OWNERSHIP_CHANGED, "Ownership Changed");
	}};
	

	
	
}
