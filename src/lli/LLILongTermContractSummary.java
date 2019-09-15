package lli;

public class LLILongTermContractSummary {
	public long fromDate;
	public long toDate;
	public double totalBandwidthContract;
	public LLILongTermContractSummary(long fromDate,long toDate,double totalBandwidth){
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.totalBandwidthContract = totalBandwidth;
	}
}
