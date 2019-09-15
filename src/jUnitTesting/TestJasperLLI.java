package jUnitTesting;

import java.util.HashMap;
import java.util.Map;

public class TestJasperLLI {
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
	public Map<String, Object> getPdfParamMap(){
		Map<String, Object> params = new HashMap<>();
		params.put( "logo_btcl", "D:\\\\logo_btcl.jpg" );
		params.put( "logo_bd", "D:\\\\logo_bd.png");
		params.put( "heading", "Bangladesh Telecommunications Company Limited");
		params.put( "client_name", "ABCD EFGH");
		params.put( "client_address", "HIJK LMNOP");
		params.put( "client_type", "Govt.");
		return params;
	}
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
		return bandwidthCost;
	}
	public void setBandwidthCost(double bandwidthCost) {
		this.bandwidthCost = bandwidthCost;
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
		return coreMRC;
	}
	public void setCoreMRC(double coreMRC) {
		this.coreMRC = coreMRC;
	}
	public double getCoreCost() {
		return coreCost;
	}
	public void setCoreCost(double coreCost) {
		this.coreCost = coreCost;
	}
	

	
}
