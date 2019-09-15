package vpnCommonChargeConfig.form;

import org.apache.struts.action.ActionForm;

public class vpnCommonChargeConfigForm extends ActionForm {

	/**
	 * 
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = -6024390595785521112L;
	long id;
	long categoryID = 1;
	double shiftingCharge;
	double upgradationCharge;
	double chargePerPoint;
	double ratioL3L2;
	double minAdvancePaymentForMonth;
	double minAdvancePaymentForSpecialPayment;
	int minNumOfConnectionForFreePort;
	double minimumBWForFreePort;
	double compensationForLateBillInPercentage;
	double timeForNewBIllForLatePaymentInMonth;
	double compensationForNewBillForLatePayment;
	double timeForOldLatePaymentInMonth;
	double reConChargeForCutDown;
	double opticalFiberInstallationCharge;
	double OFChargePerMeter;
	String activationDate;
	long lastModificationTime;
	boolean isDeleted;
	
	
	public double getOpticalFiberInstallationCharge() {
		return opticalFiberInstallationCharge;
	}
	
	public void setOpticalFiberInstallationCharge(double opticalFiberInstallationCharge) {
		this.opticalFiberInstallationCharge = opticalFiberInstallationCharge;
	}
	
	public double getOFChargePerMeter() {
		return OFChargePerMeter;
	}
	
	public void setOFChargePerMeter(double oFChargePerMeter) {
		OFChargePerMeter = oFChargePerMeter;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getCategoryID() {
		return categoryID;
	}
	public void setCategoryID(long categoryID) {
		this.categoryID = categoryID;
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
	public double getRatioL3L2() {
		return ratioL3L2;
	}
	public void setRatioL3L2(double ratioL3L2) {
		this.ratioL3L2 = ratioL3L2;
	}
	public double getMinAdvancePaymentForMonth() {
		return minAdvancePaymentForMonth;
	}
	public void setMinAdvancePaymentForMonth(double minAdvancePaymentForMonth) {
		this.minAdvancePaymentForMonth = minAdvancePaymentForMonth;
	}
	public double getMinAdvancePaymentForSpecialPayment() {
		return minAdvancePaymentForSpecialPayment;
	}
	public void setMinAdvancePaymentForSpecialPayment(double minAdvancePaymentForSpecialPayment) {
		this.minAdvancePaymentForSpecialPayment = minAdvancePaymentForSpecialPayment;
	}
	public int getMinNumOfConnectionForFreePort() {
		return minNumOfConnectionForFreePort;
	}
	public void setMinNumOfConnectionForFreePort(int minNumOfConnectionForFreePort) {
		this.minNumOfConnectionForFreePort = minNumOfConnectionForFreePort;
	}
	public double getMinimumBWForFreePort() {
		return minimumBWForFreePort;
	}
	public void setMinimumBWForFreePort(double minimumBWForFreePort) {
		this.minimumBWForFreePort = minimumBWForFreePort;
	}
	public double getCompensationForLateBillInPercentage() {
		return compensationForLateBillInPercentage;
	}
	public void setCompensationForLateBillInPercentage(double compensationForLateBillInPercentage) {
		this.compensationForLateBillInPercentage = compensationForLateBillInPercentage;
	}
	public double getTimeForNewBIllForLatePaymentInMonth() {
		return timeForNewBIllForLatePaymentInMonth;
	}
	public void setTimeForNewBIllForLatePaymentInMonth(double timeForNewBIllForLatePaymentInMonth) {
		this.timeForNewBIllForLatePaymentInMonth = timeForNewBIllForLatePaymentInMonth;
	}
	public double getCompensationForNewBillForLatePayment() {
		return compensationForNewBillForLatePayment;
	}
	public void setCompensationForNewBillForLatePayment(double compensationForNewBillForLatePayment) {
		this.compensationForNewBillForLatePayment = compensationForNewBillForLatePayment;
	}
	public double getTimeForOldLatePaymentInMonth() {
		return timeForOldLatePaymentInMonth;
	}
	public void setTimeForOldLatePaymentInMonth(double timeForOldLatePaymentInMonth) {
		this.timeForOldLatePaymentInMonth = timeForOldLatePaymentInMonth;
	}
	public double getReConChargeForCutDown() {
		return reConChargeForCutDown;
	}
	public void setReConChargeForCutDown(double reConChargeForCutDown) {
		this.reConChargeForCutDown = reConChargeForCutDown;
	}
	
	public long getLastModificationTime() {
		return lastModificationTime;
	}
	public void setLastModificationTime(long lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}
	public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getActivationDate() {
		return activationDate;
	}
	public void setActivationDate(String activationDate) {
		this.activationDate = activationDate;
	}
	@Override
	public String toString() {
		return "vpnCommonChargeConfigForm [id=" + id + ", categoryID=" + categoryID + ", shiftingCharge="
				+ shiftingCharge + ", upgradationCharge=" + upgradationCharge + ", chargePerPoint=" + chargePerPoint
				+ ", ratioL3L2=" + ratioL3L2 + ", minAdvancePaymentForMonth=" + minAdvancePaymentForMonth
				+ ", minAdvancePaymentForSpecialPayment=" + minAdvancePaymentForSpecialPayment
				+ ", minNumOfConnectionForFreePort=" + minNumOfConnectionForFreePort + ", minimumBWForFreePort="
				+ minimumBWForFreePort + ", compensationForLateBillInPercentage=" + compensationForLateBillInPercentage
				+ ", timeForNewBIllForLatePaymentInMonth=" + timeForNewBIllForLatePaymentInMonth
				+ ", compensationForNewBillForLatePayment=" + compensationForNewBillForLatePayment
				+ ", timeForOldLatePaymentInMonth=" + timeForOldLatePaymentInMonth + ", reConChargeForCutDown="
				+ reConChargeForCutDown + ", activationDate=" + activationDate + ", lastModificationTime="
				+ lastModificationTime + ", isDeleted=" + isDeleted + "]";
	}
	
	
	
}
