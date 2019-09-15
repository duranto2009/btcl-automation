package common.payment;

import java.util.Arrays;
import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import report.DateConvertor;
import report.Display;
import report.PaymentGatewayConverter;
import report.ReportCriteria;
import report.SubqueryBuilderForDate;
import util.TimeConverter;

@SuppressWarnings({ "serial" })
@TableName("at_payment")
public class PaymentDTO  extends ActionForm{
	@PrimaryKey
	@ColumnName("pID")
	long ID;
	//@ColumnName("pBillID")
//	long billID;
	@ColumnName("pAmount")
	double btclAmount;
	/*vat*/
	@ColumnName("pVatAmount")
	double vatAmount;
	@ColumnName("vatChalan")
	String vatChalan;
	@ColumnName("pIsVatIncluded")
	int vatIncluded;
	/*vat*/
	
	/*it deduction*/
	@ColumnName("itDeductionChalan")
	String itDeductionChalan;
	@ColumnName("itDeductionAmount")
	double itDeductionAmount;
	@ColumnName("isItDeductionIncluded")
	boolean itDeductionIncluded;
	/*it deduction*/
	
	
	@ColumnName("pPayableAmount")
	double payableAmount;
	
	@ColumnName("pPaidAmount")
	double paidAmount;
	@Display(PaymentGatewayConverter.class)
	@ColumnName("pPaymentGatewayType")
	int paymentGatewayType;
	@ColumnName("pPaymentType")
	int paymentType;
	@ColumnName("pBankName")
	String bankName;
	@ColumnName("pBankBranchName")
	String bankBranchName;
	@ColumnName("pPaymentStatus")
	int paymentStatus;
	@ColumnName("pOrderNo")
	String orderNo;
	@Display(DateConvertor.class)
	@ReportCriteria(value = SubqueryBuilderForDate.class, moduleID = 0)
	@ColumnName("pPaymentTime")
	long paymentTime;
	@ColumnName("pVerifiedBy")
	long verifiedBy;
	@ColumnName("pVerificationTime")
	long verificationTime;
	@ColumnName("pVerificationMessage")
	String verificationMessage = "";
	
	@ColumnName("pApprovedBy")
	long approvedBy;
	@ColumnName("pLastModificationTime")	
	long lastModificationTime;
	@ColumnName("pDescription")
	String description;
	
	@ColumnName("pIsDeleted")
	boolean isDeleted;
	
	String paymentDateStr;
	long clientID;
	FormFile document;
	FormFile documentIT;
	long []billIDs;
	
	@ColumnName("pModuleID")
	int moduleID;
	
	public double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}

	/*calculate payment*/
	public long getPaymentAmount(){
		return (long)(Math.ceil(getPayableAmount())+.00001);
	}

	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}

	public double getBtclAmount() {
		return btclAmount;
	}

	public void setBtclAmount(double btclAmount) {
		this.btclAmount = btclAmount;
	}

	public double getVatAmount() {
		return vatAmount;
	}

	public void setVatAmount(double vatAmount) {
		this.vatAmount = vatAmount;
	}

	public int getVatIncluded() {
		return vatIncluded;
	}

	public void setVatIncluded(int vatIncluded) {
		this.vatIncluded = vatIncluded;
	}

	public String getVatChalan() {
		return vatChalan;
	}

	public void setVatChalan(String vatChalan) {
		this.vatChalan = vatChalan;
	}

	public boolean isItDeductionIncluded() {
		return itDeductionIncluded;
	}

	public void setItDeductionIncluded(boolean itDeductionIncluded) {
		this.itDeductionIncluded = itDeductionIncluded;
	}

	public double getPayableAmount() {
		return payableAmount;
	}

	public void setPayableAmount(double payableAmount) {
		this.payableAmount = payableAmount;
	}

	public int getPaymentGatewayType() {
		return paymentGatewayType;
	}

	public void setPaymentGatewayType(int paymentGatewayType) {
		this.paymentGatewayType = paymentGatewayType;
	}

	public int getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(int paymentType) {
		this.paymentType = paymentType;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankBranchName() {
		return bankBranchName;
	}

	public void setBankBranchName(String bankBranchName) {
		this.bankBranchName = bankBranchName;
	}


	public int getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(int paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public long getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(long paymentTime) {
		this.paymentTime = paymentTime;
	}

	public long getVerifiedBy() {
		return verifiedBy;
	}

	public void setVerifiedBy(long verifiedBy) {
		this.verifiedBy = verifiedBy;
	}

	public long getVerificationTime() {
		return verificationTime;
	}

	public void setVerificationTime(long verificationTime) {
		this.verificationTime = verificationTime;
	}

	public String getVerificationMessage() {
		return verificationMessage;
	}

	public void setVerificationMessage(String verificationMessage) {
		this.verificationMessage = verificationMessage;
	}

	public long getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(long approvedBy) {
		this.approvedBy = approvedBy;
	}

	public long getLastModificationTime() {
		return lastModificationTime;
	}

	public void setLastModificationTime(long lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getPaymentDateStr() {
		return paymentDateStr;
	}

	public void setPaymentDateStr(String paymentDateStr) {
		this.paymentDateStr = paymentDateStr;
		paymentTime=TimeConverter.getTimeFromString(paymentDateStr);
	}

	public long getClientID() {
		return clientID;
	}

	public void setClientID(long clientID) {
		this.clientID = clientID;
	}

	public FormFile getDocument() {
		return document;
	}

	public void setDocument(FormFile document) {
		this.document = document;
	}

	public FormFile getDocumentIT() {
		return documentIT;
	}

	public void setDocumentIT(FormFile documentIT) {
		this.documentIT = documentIT;
	}

	public long[] getBillIDs() {
		return billIDs;
	}

	public void setBillIDs(long[] billIDs) {
		this.billIDs = billIDs;
	}

	public String getItDeductionChalan() {
		return itDeductionChalan;
	}

	public void setItDeductionChalan(String itDeductionChalan) {
		this.itDeductionChalan = itDeductionChalan;
	}

	public double getItDeductionAmount() {
		return itDeductionAmount;
	}

	public void setItDeductionAmount(double itDeductionAmount) {
		this.itDeductionAmount = itDeductionAmount;
	}

	
	
	public int getModuleID() {
		return moduleID;
	}

	public void setModuleID(int moduleID) {
		this.moduleID = moduleID;
	}

	@Override
	public String toString() {
		return "PaymentDTO [ID=" + ID + ", btclAmount=" + btclAmount + ", vatAmount=" + vatAmount + ", vatChalan="
				+ vatChalan + ", vatIncluded=" + vatIncluded + ", itDeductionChalan=" + itDeductionChalan
				+ ", itDeductionAmount=" + itDeductionAmount + ", itDeductionIncluded=" + itDeductionIncluded
				+ ", payableAmount=" + payableAmount + ", paymentGatewayType=" + paymentGatewayType + ", paymentType="
				+ paymentType + ", bankName=" + bankName + ", bankBranchName=" + bankBranchName +", paymentStatus=" + paymentStatus + ", orderNo=" + orderNo + ", paymentTime="
				+ paymentTime + ", verifiedBy=" + verifiedBy + ", verificationTime=" + verificationTime
				+ ", verificationMessage=" + verificationMessage + ", approvedBy=" + approvedBy
				+ ", lastModificationTime=" + lastModificationTime + ", description=" + description + ", isDeleted="
				+ isDeleted + ", paymentDateStr=" + paymentDateStr + ", clientID=" + clientID + ", document=" + document
				+ ", billIDs=" + Arrays.toString(billIDs) + "]";
	}

}
