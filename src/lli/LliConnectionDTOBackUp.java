package lli;

import java.util.Date;

import org.apache.struts.action.ActionForm;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import common.EntityDTO;

@SuppressWarnings("serial")
@TableName("at_lli_con")
public class LliConnectionDTOBackUp extends ActionForm implements EntityDTO{
	@PrimaryKey
	@ColumnName("ID")
	long ID;
	@ColumnName("conName")
	String cnName;
	@ColumnName("serviceClientID")
	long serviceClientID;
	@ColumnName("conDescription")
	String cnDescription;
	@ColumnName("applicationDate")
	long applicationDate;
	@ColumnName("expireDate")
	long expireDate;
	@ColumnName("lastModificationTime")
	long lastModificationTime;
	@ColumnName("isDeleted")
	boolean isDeleted;
	@ColumnName("activationDate")
	long activationDate;
	@ColumnName("clientID")
	long clientID;

	double shiftingCharge;
	double upgradationCharge;
	double chargePerPoint;
	double extraChargeForL3ToL2INPercentage;
	double discountForIIGAndClientInPercentage;
	int minimumAdvancedPaymentInMonth;
	int minimumAdvancedPaymentForSpecialPaymentInDays;
	int minimumNumberOfConnectionForSpecialPayment;
	int minimumNoOfConnectionForFreePort;
	double minimumBandwidthForFreePort;
	double compensationForLateBillInPercentage;
	int timeForNewBIllForLatePaymentInMonth;
	double compensationForNewBillForLatePayment;
	int tImeForOldLatePaymentInMonth;
	double connectionChargeForReconnectionOfACutDownConnection;
	Date activationdate;

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

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public String getCnDescription() {
		return cnDescription;
	}

	public void setCnDescription(String conDescription) {
		this.cnDescription = conDescription;
	}

	public String getConnDescription() {
		return cnDescription;
	}

	public void setConnDescription(String connDescription) {
		this.cnDescription = connDescription;
	}

	public long getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(long applicationtDate) {
		this.applicationDate = applicationtDate;
	}

	public double getShiftingCharge() {
		return shiftingCharge;
	}

	public void setShiftingCharge(double shiftingCharge) {
		this.shiftingCharge = shiftingCharge;
	}

	public double getUpgradationCharge() {
		return upgradationCharge;
	}

	public void setUpgradationCharge(double upgradationCharge) {
		this.upgradationCharge = upgradationCharge;
	}

	public double getChargePerPoint() {
		return chargePerPoint;
	}

	public void setChargePerPoint(double chargePerPoint) {
		this.chargePerPoint = chargePerPoint;
	}

	public double getExtraChargeForL3ToL2INPercentage() {
		return extraChargeForL3ToL2INPercentage;
	}

	public void setExtraChargeForL3ToL2INPercentage(double extraChargeForL3ToL2INPercentage) {
		this.extraChargeForL3ToL2INPercentage = extraChargeForL3ToL2INPercentage;
	}

	public double getDiscountForIIGAndClientInPercentage() {
		return discountForIIGAndClientInPercentage;
	}

	public void setDiscountForIIGAndClientInPercentage(double discountForIIGAndClientInPercentage) {
		this.discountForIIGAndClientInPercentage = discountForIIGAndClientInPercentage;
	}

	public int getMinimumAdvancedPaymentInMonth() {
		return minimumAdvancedPaymentInMonth;
	}

	public void setMinimumAdvancedPaymentInMonth(int minimumAdvancedPaymentInMonth) {
		this.minimumAdvancedPaymentInMonth = minimumAdvancedPaymentInMonth;
	}

	public int getMinimumAdvancedPaymentForSpecialPaymentInDays() {
		return minimumAdvancedPaymentForSpecialPaymentInDays;
	}

	public void setMinimumAdvancedPaymentForSpecialPaymentInDays(int minimumAdvancedPaymentForSpecialPaymentInDays) {
		this.minimumAdvancedPaymentForSpecialPaymentInDays = minimumAdvancedPaymentForSpecialPaymentInDays;
	}

	public int getMinimumNumberOfConnectionForSpecialPayment() {
		return minimumNumberOfConnectionForSpecialPayment;
	}

	public void setMinimumNumberOfConnectionForSpecialPayment(int minimumNumberOfConnectionForSpecialPayment) {
		this.minimumNumberOfConnectionForSpecialPayment = minimumNumberOfConnectionForSpecialPayment;
	}

	public int getMinimumNoOfConnectionForFreePort() {
		return minimumNoOfConnectionForFreePort;
	}

	public void setMinimumNoOfConnectionForFreePort(int minimumNoOfConnectionForFreePort) {
		this.minimumNoOfConnectionForFreePort = minimumNoOfConnectionForFreePort;
	}

	public double getMinimumBandwidthForFreePort() {
		return minimumBandwidthForFreePort;
	}

	public void setMinimumBandwidthForFreePort(double minimumBandwidthForFreePort) {
		this.minimumBandwidthForFreePort = minimumBandwidthForFreePort;
	}

	public double getCompensationForLateBillInPercentage() {
		return compensationForLateBillInPercentage;
	}

	public void setCompensationForLateBillInPercentage(double compensationForLateBillInPercentage) {
		this.compensationForLateBillInPercentage = compensationForLateBillInPercentage;
	}

	public int getTimeForNewBIllForLatePaymentInMonth() {
		return timeForNewBIllForLatePaymentInMonth;
	}

	public void setTimeForNewBIllForLatePaymentInMonth(int timeForNewBIllForLatePaymentInMonth) {
		this.timeForNewBIllForLatePaymentInMonth = timeForNewBIllForLatePaymentInMonth;
	}

	public double getCompensationForNewBillForLatePayment() {
		return compensationForNewBillForLatePayment;
	}

	public void setCompensationForNewBillForLatePayment(double compensationForNewBillForLatePayment) {
		this.compensationForNewBillForLatePayment = compensationForNewBillForLatePayment;
	}

	public int gettImeForOldLatePaymentInMonth() {
		return tImeForOldLatePaymentInMonth;
	}

	public void settImeForOldLatePaymentInMonth(int tImeForOldLatePaymentInMonth) {
		this.tImeForOldLatePaymentInMonth = tImeForOldLatePaymentInMonth;
	}

	public double getConnectionChargeForReconnectionOfACutDownConnection() {
		return connectionChargeForReconnectionOfACutDownConnection;
	}

	public void setConnectionChargeForReconnectionOfACutDownConnection(double connectionChargeForReconnectionOfACutDownConnection) {
		this.connectionChargeForReconnectionOfACutDownConnection = connectionChargeForReconnectionOfACutDownConnection;
	}

	public Date getActivationdate() {
		return activationdate;
	}

	public void setActivationdate(Date activationdate) {
		this.activationdate = activationdate;
	}
	
	public long getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(long expireDate) {
		this.expireDate = expireDate;
	}
	
	public long getActivationDate() {
		return activationDate;
	}
	public void setActivationDate(long activationDate) {
		this.activationDate = activationDate;
	}

	public long getClientID() {
		return clientID;
	}

	public void setClientID(long clientID) {
		this.clientID = clientID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (ID ^ (ID >>> 32));
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
		//LliConnectionDTO other = (LliConnectionDTO) obj;
		//if (ID != other.ID)
		//	return false;
		return true;
	}

	

	@Override
	public String toString() {
		return "VpnConnection [ID=" + ID + ", cnName=" + cnName + ", serviceClientID=" + serviceClientID
				+ ", cnDescription=" + cnDescription + ", applicationtDate=" + applicationDate + ", expireDate="
				+ expireDate + ", applicationLastModificationTime=" + lastModificationTime + ", isDeleted="
				+ isDeleted + ", shiftingCharge=" + shiftingCharge + ", upgradationCharge=" + upgradationCharge
				+ ", chargePerPoint=" + chargePerPoint + ", extraChargeForL3ToL2INPercentage="
				+ extraChargeForL3ToL2INPercentage + ", discountForIIGAndClientInPercentage="
				+ discountForIIGAndClientInPercentage + ", minimumAdvancedPaymentInMonth="
				+ minimumAdvancedPaymentInMonth + ", minimumAdvancedPaymentForSpecialPaymentInDays="
				+ minimumAdvancedPaymentForSpecialPaymentInDays + ", minimumNumberOfConnectionForSpecialPayment="
				+ minimumNumberOfConnectionForSpecialPayment + ", minimumNoOfConnectionForFreePort="
				+ minimumNoOfConnectionForFreePort + ", minimumBandwidthForFreePort=" + minimumBandwidthForFreePort
				+ ", compensationForLateBillInPercentage=" + compensationForLateBillInPercentage
				+ ", timeForNewBIllForLatePaymentInMonth=" + timeForNewBIllForLatePaymentInMonth
				+ ", compensationForNewBillForLatePayment=" + compensationForNewBillForLatePayment
				+ ", tImeForOldLatePaymentInMonth=" + tImeForOldLatePaymentInMonth
				+ ", connectionChargeForReconnectionOfACutDownConnection="
				+ connectionChargeForReconnectionOfACutDownConnection + ", activationdate=" + activationdate + "]";
	}

	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}

	public long getServiceClientID() {
		return serviceClientID;
	}

	public void setServiceClientID(long serviceClientID) {
		this.serviceClientID = serviceClientID;
	}
	
	@Override
	public boolean isActivated() {
		return activationDate !=0;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public int getCurrentStatus() {
		return 0;
	}

	@Override
	public int getLatestStatus() {
		return 0;
	}

	@Override
	public void setCurrentStatus(int currentStatus) {
	}

	@Override
	public void setLatestStatus(int latestStatus) {
	}
	
	@Override
	public long getEntityID() {
		return ID;
	}

	@Override
	public double getBalance() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setBalance(double balance) {
		// TODO Auto-generated method stub
		
	}

}
