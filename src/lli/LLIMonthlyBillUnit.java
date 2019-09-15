package lli;

import util.DateUtils;
import util.TimeConverter;

public class LLIMonthlyBillUnit {
	long fromDate;
	long toDate;
	long connectionID;
	double bandwidth;
	double bandwidthMRC;
	double bandwidthCost;
	double totalCoreDistance;
	int numberOfCores;
	double coreMRC;
	double coreCost;
	public long getFromDate() {
		return fromDate;
	}
	public void setFromDate(long fromDate) {
		this.fromDate = fromDate;
	}
	public long getToDate() {
		return toDate;
	}
	public void setToDate(long toDate) {
		this.toDate = toDate;
	}
	public long getConnectionID() {
		return connectionID;
	}
	public void setConnectionID(long connectionID) {
		this.connectionID = connectionID;
	}
	public double getBandwidth() {
		return bandwidth;
	}
	public void setBandwidth(double bandwidth) {
		this.bandwidth = bandwidth;
	}
	public double getBandwidthMRC() {
		return bandwidthMRC;
	}
	public void setBandwidthMRC(double bandwidthMRC) {
		this.bandwidthMRC = bandwidthMRC;
	}
	public double getBandwidthCost() {
		return DateUtils.getMultiplierConstantByFromToDate(fromDate, toDate) * bandwidthMRC;
	}
	
	public double getTotalCoreDistance() {
		return totalCoreDistance;
	}
	public void setTotalCoreDistance(double totalCoreDistance) {
		this.totalCoreDistance = totalCoreDistance;
	}
	public int getNumberOfCores() {
		return numberOfCores;
	}
	public void setNumberOfCores(int numberOfCores) {
		this.numberOfCores = numberOfCores;
	}
	public double getCoreMRC() {
		return this.coreMRC;
	}
	public void setCoreMRC(double coreMRC) {
		this.coreMRC = coreMRC;
	}
	public double getCoreCost() {
		return DateUtils.getMultiplierConstantByFromToDate(fromDate, toDate) * coreMRC;
	}
	@Override
	public String toString() {
		return "LLIMonthlyBillUnit [fromDate=" + TimeConverter.getDateTimeStringByMillisecAndDateFormat(fromDate, "dd/MM/yyyy") + ", toDate=" + 
				TimeConverter.getDateTimeStringByMillisecAndDateFormat(toDate, "dd/MM/yyyy")+ ", connectionID=" + connectionID
				+ ", bandwidth=" + bandwidth + ", bandwidthMRC=" + bandwidthMRC + ", bandwidthCost=" + bandwidthCost
				+ ", totalCoreDistance=" + totalCoreDistance + ", numberOfCores=" + numberOfCores + ", coreMRC="
				+ coreMRC + ", coreCost=" + coreCost + "]";
	}
	
	
	
}
