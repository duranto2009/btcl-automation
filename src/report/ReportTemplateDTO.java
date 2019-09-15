package report;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;

@TableName("at_report_template")
public class ReportTemplateDTO {
	@PrimaryKey
	@ColumnName("rtID")
	long reportID;
	
	@ColumnName("rtName")
	String reportName;
	
	@ColumnName("rtCriteria")
	String reportCriteria;
	
	@ColumnName("rtDisplay")
	String reportDisplay;
	
	@ColumnName("rtOrder")
	String reportOrder;
	
	@ColumnName("rtLastModificationTime")
	long lastModificationTime;

	
//	Getters and Setters

	public long getReportID() {
		return reportID;
	}

	public void setReportID(long reportID) {
		this.reportID = reportID;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getReportCriteria() {
		return reportCriteria;
	}

	public void setReportCriteria(String reportCriteria) {
		this.reportCriteria = reportCriteria;
	}

	public String getReportDisplay() {
		return reportDisplay;
	}

	public void setReportDisplay(String reportDisplay) {
		this.reportDisplay = reportDisplay;
	}

	public String getReportOrder() {
		return reportOrder;
	}

	public void setReportOrder(String reportOrder) {
		this.reportOrder = reportOrder;
	}

	public long getLastModificationTime() {
		return lastModificationTime;
	}

	public void setLastModificationTime(long lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}
	 
	
}
