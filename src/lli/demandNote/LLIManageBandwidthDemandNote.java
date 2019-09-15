package lli.demandNote;
import java.util.*;

import annotation.AccountingLogic;
import annotation.ColumnName;
import annotation.ForeignKeyName;
import annotation.PrimaryKey;
import annotation.TableName;
import common.bill.BillDTO;

@AccountingLogic(LLIManageBandwidthBusinessLogic.class)
@TableName("")
@ForeignKeyName("")
public class LLIManageBandwidthDemandNote extends BillDTO{
	@PrimaryKey
	@ColumnName("")
	long manageBandwidthDemanNoteID;
	@ColumnName("")
	double bandwidthShiftCharge;
	@ColumnName("")
	double others;
	public long getManageBandwidthDemanNoteID() {
		return manageBandwidthDemanNoteID;
	}
	public void setManageBandwidthDemanNoteID(long manageBandwidthDemanNoteID) {
		this.manageBandwidthDemanNoteID = manageBandwidthDemanNoteID;
	}
	public double getBandwidthShiftCharge() {
		return bandwidthShiftCharge;
	}
	public void setBandwidthShiftCharge(double bandwidthShiftCharge) {
		this.bandwidthShiftCharge = bandwidthShiftCharge;
	}
	public double getOthers() {
		return others;
	}
	public void setOthers(double others) {
		this.others = others;
	}
	
	
}
