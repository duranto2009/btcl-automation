
package common.bill;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.SearchFieldFromReferenceTable;
import annotation.TableName;
import common.ClientDTO;
import common.ModuleConstants;
import connection.DatabaseConnection;
import org.apache.log4j.Logger;
import report.*;
import util.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@TableName("at_bill")
public class BillDTO {
	
	static Logger logger = Logger.getLogger( BillDTO.class );
	
	public static SimpleDateFormat dayMonthYearDateFormat = new SimpleDateFormat( "dd/MM/yyyy" );
	
	public static final int PREPAID_BILL = 0;/*demand note*/
	public static final int POSTPAID_BILL = 1;/*monthly bill*/
	public static final int PREPAID_AND_POSTPAID_BILL = 2;
	
	public static final int UNPAID = 0;
	public static final int PAID_UNVERIFIED = 1;
	public static final int PAID_UNVERIFIED_FROM_SKIPPED = 3;
	public static final int PAID_VERIFIED = 2;
	public static final int SKIPPED = 4;
	
	public static final int PAYMENT_GATEWAY_TELETALK = 1;
	public static final int PAYMENT_GATEWAY_BANK = 2 ;
	public static final int PAYMENT_GATEWAY_BTCL_ADMIN = 3;
	
	@SuppressWarnings("serial")
	public static final Map<Integer,String> paymentGatewayMap = new HashMap<Integer,String>(){{
		put(PAYMENT_GATEWAY_BANK, "BANK");
		put(PAYMENT_GATEWAY_BTCL_ADMIN,"BTCL ADMIN");
		put(PAYMENT_GATEWAY_TELETALK,"TELETALK");
		put(null,"Not paid yet Paid");
	}};

	public static final Map<Integer, String> demandNoteStatusMap = new HashMap<>();
	static {
		demandNoteStatusMap.put(PAID_UNVERIFIED, "Paid Unverified");
		demandNoteStatusMap.put(PAID_VERIFIED, "Paid Verified");
		demandNoteStatusMap.put(PAID_UNVERIFIED_FROM_SKIPPED, "Paid Unverified from Skipped");
		demandNoteStatusMap.put(UNPAID, "Unpaid");
		demandNoteStatusMap.put(SKIPPED, "Skipped");
	}
	public String entityName = "";
	@ColumnName("blVatPercentage")
	double VatPercentage = 0;
	@ColumnName("blDiscountPercentage")
	double discountPercentage = 0;
	@PrimaryKey
	@ColumnName("blID")
	long ID;
	@SearchFieldFromReferenceTable(entityClass=ClientDTO.class,entityColumnName="loginName",operator="like")
	@ColumnName(value = "blClientID", editable = false)
	long clientID = -1;
	@ColumnName("blGrandTotal")
	double grandTotal = 0.0;
	@ColumnName("blTotalPayable")
	double totalPayable = 0;
	@ColumnName("blVAT")
	double VAT = 0.0;
	@ColumnName(value = "blDescription", editable = false)
	String description = "";
	
	@ColumnName(value = "blMonth",editable = false)
			// 0 ->Jan, 11->Dec
	int month = LocalDate.now(ZoneId.systemDefault()).getMonthValue()-1;
	@ColumnName("blYear")
	int year = LocalDate.now(ZoneId.systemDefault()).getYear();
	@ReportCriteria(value = SubqueryBuilderForDate.class,moduleID = 0)
	@ColumnName("blLastPaymentDate")
	long lastPaymentDate = System.currentTimeMillis();
	
	
	@Display(BillTypeConverter.class)
	@ColumnName(value = "blType",editable = false)
	int billType; //0 = prepaid or  1 = postpaid
	@Display(DateConvertor.class)
	@ReportCriteria(value = SubqueryBuilderForDate.class, moduleID = 0)
	@ColumnName(value = "blGenerationTime", editable = false)
	long generationTime = System.currentTimeMillis();
	@ColumnName("blIsDeleted")
	boolean isDeleted;
	@ColumnName("blLastModificationTime")
	long lastModificationTime = System.currentTimeMillis();
	@ColumnName(value = "blEntityID",editable = false)
	long entityID = -1;
	@ColumnName(value = "blEntityTypeID",editable = false)
	int entityTypeID;
	@ColumnName("blNetPayable")
	double netPayable = 0.0;
	@ColumnName("blReqID")
	long reqID = 0;
	@ColumnName(value = "blClassName",editable = false)
	String className = this.getClass().getCanonicalName();
	@ColumnName("blPaymentID")
	protected Long paymentID = 0L;
	@ColumnName("blDiscount")
	double discount = 0.0;
	
	@ColumnName("blActivationTimeFrom")
	Long activationTimeFrom = System.currentTimeMillis();
	
	@ColumnName("blActivationTimeTo")
	Long activationTimeTo = System.currentTimeMillis();
	
	@ColumnName("blBillFilePath")
	String billFilePath = "";
	
	@ColumnName(value = "blRequestType",editable = false)
	int billReqType;
	@ColumnName("blLateFee")
	double lateFee = 0.0;
	@Display(BillPaymentStatusConverter.class)
	@ColumnName("blPaymentStatus")
	int paymentStatus;
	
	@ColumnName("blPaymentGatewayType")
	Integer paymentGateway;

	@ColumnName("blAdjustmentAmount")
	double adjustmentAmount;



	@ColumnName("blIsMultiple")
	int isMultiple;//1=child 2=parent

	public int getIsMultiple() {
		return isMultiple;
	}

	public void setIsMultiple(int isMultiple) {
		this.isMultiple = isMultiple;
	}
	
	public double getLateFee() {
		return lateFee;
	}

	public void setLateFee(double lateFee) {
		this.lateFee = lateFee;
	}
	
	
	public Integer getPaymentPortal() {
		return paymentGateway;
	}

	public void setPaymentPortal(Integer paymentPortal) {
		this.paymentGateway = paymentPortal;
	}

	public int getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(int paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public int getBillReqType() {
		return billReqType;
	}

	public void setBillReqType(int billReqType) {
		this.billReqType = billReqType;
	}
	
	public boolean isPayable(){
		return paymentStatus == UNPAID;
	}
	
	private String row = "";
	
	public void payBill(DatabaseConnection databaseConnection, Object...objects ) throws Exception{
	
		//throw new Exception("Method not implemeted yet");
		logger.debug("default payBill called");
	}
	
	
	public void cancelBill() throws Exception{
		logger.debug("default cancel bill is called");
	}
	
	public void verifyPayment() throws Exception{
		logger.debug("verify payment");
	}
	
	
	
	public String getModule() {
		return ModuleConstants.ActiveModuleMap.getOrDefault(this.getEntityTypeID()/100, "N/A");
	}
	public static String getModule(int entityTypeID) {
		return ModuleConstants.ActiveModuleMap.getOrDefault(entityTypeID/100, "N/A");
	}
	public static String getStatus(int status) {
		return demandNoteStatusMap.getOrDefault(status, "N/A");
	}

	public String getBillTypeStr() {
		switch (this.getBillType()) {
			case BillConstants.DEMAND_NOTE:
				return "Demand Note";
			case BillConstants.MONTHLY_BILL:
				return "Monthly Bill";
			case BillConstants.YEARLY_BILL:
				return "Yearly Bill";
			case BillConstants.FINAL_BILL:
				return "Final Bill";
			case BillConstants.MULTIPLE_MONTH_BILL:
				return "Multiple Monthly Bill";
		}
		return "Invalid";
	}

	public static String getBillTypeStr(int billType) {
		switch (billType) {
			case BillConstants.DEMAND_NOTE:
				return "Demand Note";
			case BillConstants.MONTHLY_BILL:
				return "Monthly Bill";
			case BillConstants.YEARLY_BILL:
				return "Yearly Bill";
			case BillConstants.FINAL_BILL:
				return "Final Bill";
			case BillConstants.MULTIPLE_MONTH_BILL:
				return "Multiple Monthly Bill";

		}
		return "Invalid";
	}

	public void startDescription( String[] columns ){
		
		description += "<table class='items'><thead><tr class='title textcenter'>";
		boolean firstColumn = true;
		int columnWidth = 100/(columns.length);
		
		for (String columnName : columns) {
			
			description += "<td ";
			if( firstColumn ){
				
				description += " width='" + columnWidth + "%'>" + columnName + "</td>";
				firstColumn = false;
			}
			else{
				
				description += " width='" + columnWidth + "%'>" + columnName + "</td>";
			}
		}
		
		description += "</tr></thead><tbody>";
	}
	
	public void startDescription( String column1, String column2 ){
		
		description += "<table class='items'><thead><tr class='title textcenter'><td width='40%'>" + column1 + "</td><td width='60%'> " + column2 + " </td></tr></thead><tbody>";
	}
	
	public void startDescription(){
		
		description += "<table class='items'><thead><tr class='title textcenter'><td width='70%'>Description</td><td width='30%'>Amount(BDT)</td></tr></thead><tbody>";
	}
	
	public void startRow(){
		
		description += "<tr>";
		row += "<tr>";
	}
	
	public void endRow(){
		
		description += "</tr>";
		row += "</tr>";
	}
	
	public void addCell( double value, Object ... param ){
		
		description += "<td>" + value + "</td>";
		row += "<td>" + value + "</td>";
		
		if( !(param.length == 1 && param[0] instanceof Boolean && (Boolean)param[0] == false ) )
			totalPayable += this.setDecimalPlaces( value, 2 );
	}
	
	public void addHeader( boolean containsHeader ){
		
		description += "<header>" + containsHeader + "</header>";
	}
	
	public void addCell( String value ){
		
		description += "<td>" + value + "</td>";
	}
	
	public void addRow( String name, String value ){
	
		description += "<tr><td>"+name+"</td><td class='textcenter'>"+value+"</td></tr>\n";
	}
	
	public void addRow( Object ... tableCells ){
		
		description += "<tr>";
		boolean firstColumn = true;
		
		for (Object cell : tableCells) {
			
			description += "<td";
			if( firstColumn ) firstColumn = false;
			else description += " class='textcenter'";
			
			description += ">" + cell + "</td>";
		}
		description += "</tr>";
	}
	
	public void addRow(String row,double cost){
		description += "<tr><td>"+row+"</td><td class='textcenter'>"+cost+"</td></tr>\n";
//		totalPayable += cost;
	}
	
	public String getRow(){
		
		return row;
	}
	
	public void endDescription( boolean includeFooter ){
		
		if( includeFooter )
			this.endDescrption();
		else
			description += "</tbody></table> <br/>";
	}
	
	public void endDescrption(){
		VAT = totalPayable*VatPercentage;
		DecimalFormat df = new DecimalFormat("#.##");      
		VAT = Double.valueOf(df.format(VAT));
		netPayable = VAT + totalPayable;
		description+="<tr class='title'><td class='textright'>VAT</td><td class='textcenter'>"+VAT+"</td></tr>";
		description+="<tr class='title'><td class='textright'>Total</td><td class='textcenter'>"+(totalPayable+VAT)+"</td></tr></tbody></table> <br/>";
	}
	
	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getNetPayable() {
		return netPayable;
	}

	public void setNetPayable(double netPayable) {
		this.netPayable = netPayable;
	}

	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public long getClientID() {
		return clientID;
	}
	public void setClientID(long clientID) {
		this.clientID = clientID;
	}

	public double getTotalPayable() {
		return totalPayable;
	}


	public void setTotalPayable(double totalPayable) {
		
		if( totalPayable < 0 ) 
			totalPayable = 0.0;
		
		this.totalPayable = totalPayable;
	}


	public double getVAT() {
		return VAT;
	}
	public void setVAT(double vAT) {
		VAT = vAT;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public long getLastPaymentDate() {
		return lastPaymentDate;
	}
	
	public void setLastPaymentDate(long lastPaymentDate) {
		this.lastPaymentDate = lastPaymentDate;
	}
	
	public void setLastPaymentDateStr( String dateStr ) throws ParseException {
		
		this.lastPaymentDate = dayMonthYearDateFormat.parse( dateStr ).getTime();
	}
	
	public int getBillType() {
		return billType;
	}
	public void setBillType(int billType) {
		this.billType = billType;
	}
	public long getGenerationTime() {
		return generationTime;
	}
	public void setGenerationTime(long generationTime) {
		this.generationTime = generationTime;
	}
	/*public Long getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Long paymentDate) {
		this.paymentDate = paymentDate;
	}*/
	public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public long getLastModificationTime() {
		return lastModificationTime;
	}
	public void setLastModificationTime(long lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}
	public long getEntityID() {
		return entityID;
	}
	public void setEntityID(long entityID) {
		this.entityID = entityID;
	}
	public int getEntityTypeID() {
		return entityTypeID;
	}
	public void setEntityTypeID(int entityTypeID) {
		this.entityTypeID = entityTypeID;
	}
	
	public boolean isPaid(){
//		return (paymentID!=null && paymentID != 0);
		//logic changed by kayesh
		return paymentStatus != 0;
	}
	
	
	public long getReqID() {
		return reqID;
	}

	public void setReqID(long reqID) {
		this.reqID = reqID;
	}

	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public long getNetPayableCeiled(){
		return (long)(Math.ceil(getNetPayable())+.00001);
	}

	public Long getPaymentID() {
		return paymentID;
	}
	public void setPaymentID(Long paymentID) {
		this.paymentID = paymentID;
	}


	public double getGrandTotal() {
		return grandTotal;
	}


	public void setGrandTotal(double grandTotal) {
		this.grandTotal = NumberUtils.formattedValue(grandTotal);//Math.round(grandTotal);
	}

	public String getActivationTimeFromStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("MMM, yyyy");
		
		if( activationTimeFrom != null )
			return sdf.format( new Date( activationTimeFrom ) );
		else
			return "";
	}
	
	public Long getActivationTimeFrom() {
		return	activationTimeFrom;
	}


	public void setActivationTimeFrom(Long activationTimeFrom) {
		this.activationTimeFrom = activationTimeFrom;
	}

	public void setActivationTimeFromStr( String dateStr ) throws ParseException{
		
		this.activationTimeFrom = dayMonthYearDateFormat.parse( dateStr ).getTime();
	}

	public String getActivationTimeToStr() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("MMM, yyyy");
		
		if( activationTimeTo != null )
			return sdf.format( new Date( activationTimeTo ) );
		else
			return "";
	}
	public Long getActivationTimeTo() {
		return activationTimeTo;		
	}


	public void setActivationTimeTo(Long activationTimeTo) {
		this.activationTimeTo = activationTimeTo;
	}

	public void setActivationTimeToStr(String dateStr) throws ParseException {
		
		this.activationTimeTo = dayMonthYearDateFormat.parse( dateStr ).getTime();
	}
	
	public String getBillFilePath() {
		return billFilePath;
	}

	public void setBillFilePath(String billFilePath) {
		this.billFilePath = billFilePath;
	}

	
	public double getAdjustmentAmount() {
		return adjustmentAmount;
	}

	public void setAdjustmentAmount(double adjustmentAmount) {
		this.adjustmentAmount = adjustmentAmount;
		setGrandTotal( getGrandTotal() + adjustmentAmount );
	}

	@Override
	public String toString() {
		return "BillDTO [VatPercentage=" + VatPercentage + ", ID=" + ID + ", clientID=" + clientID + ", btclAmount="
				+ totalPayable + ", VAT=" + VAT + ", description=" + description + ", month=" + month + ", year=" + year
				+ ", lastPaymentDate=" + lastPaymentDate + ", billType=" + billType + ", generationTime="
				+ generationTime + ", isDeleted=" + isDeleted + ", lastModificationTime=" + lastModificationTime
				+ ", entityID=" + entityID + ", entityTypeID=" + entityTypeID + ", total=" + netPayable + ", reqID=" + reqID
				+ ", className=" + className + ", paymentID=" + paymentID + ", discount=" + discount + ", row=" + row
				+ "]";
	}
	


	public static double setDecimalPlaces( double num, int decimalPlace ){
		
		return new BigDecimal( num ).setScale( decimalPlace, RoundingMode.HALF_UP ).doubleValue();
	}

	public double getVatPercentage() {
		return VatPercentage;
	}

	public void setVatPercentage(double vatPercentage) {
		VatPercentage = vatPercentage;
	}

	public double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

}
