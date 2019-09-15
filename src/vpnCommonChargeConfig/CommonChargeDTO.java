package vpnCommonChargeConfig;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import util.ServiceDAOFactory;
import vpn.ofcinstallation.DistrictOfcInstallationDTO;
import vpn.ofcinstallation.DistrictOfcInstallationService;

@TableName("at_vpn_common_charge_config")
public class CommonChargeDTO {
	
	@ColumnName("vcccID")
	@PrimaryKey
	long id;
	
	@ColumnName("vcccCategoryID")
	long categoryID = 1;
	@ColumnName("vcccShiftingCharge")
	double shiftingCharge;
	@ColumnName("vcccUpgradationCharge")
	double upgradationCharge;
	@ColumnName("vcccchargePerPoint")
	double chargePerPoint;
	@ColumnName("vcccextraChargeForL3toL2InPercentage")
	double ratioL3L2;
	@ColumnName("vcccMinAdvPaymentInMonth")
	double minAdvancePaymentForMonth;
	@ColumnName("vcccMinAdvPaymentForSplPayment")
	double minAdvancePaymentForSpecialPayment;
	@ColumnName("vcccMinNumOfConForFreePort")
	int minNumOfConnectionForFreePort;
	@ColumnName("vcccMinBWForFreePort")
	double minimumBWForFreePort;
	@ColumnName("vcccCompensationForLateBillInPercentage")
	double compensationForLateBillInPercentage;
	@ColumnName("vcccTimeForNewBIllForLatePaymentInMonth")
	double timeForNewBIllForLatePaymentInMonth;
	@ColumnName("vcccCompensationForNewBillForLatePayment")
	double compensationForNewBillForLatePayment;
	@ColumnName("vccccTimeForOldLatePaymentInMonth")
	double timeForOldLatePaymentInMonth;
	@ColumnName("vccccConChargeForReConForCutDownCon")
	double reConChargeForCutDown;
	@ColumnName("vcccActivationDate")
	long activationDate;
	@ColumnName("vcccOpticalFiberInstallation")
	double opticalFiberInstallationCharge;
	@ColumnName("vcccOFCharge")
	double OFChargePerMeter;
	
	@ColumnName("vcccLLCRentCost")
	double LLCRentCost;
	@ColumnName("vcccLLCSellCost")
	double LLCSellCost;
	@ColumnName("vcccDarkRentCost")
	double darkRentCost;
	@ColumnName("vcccDarkSellCost")
	double darkSellCost;
	@ColumnName("vcccFibreMaintenanceCost")
	double fibreMaintenanceCost;
	@ColumnName("vcccFibreLayingCost")
	double fibreLayingCost;
	@ColumnName("vcccDaysForMonthlyPayment")
	int maxNumberOfDaysForMonthlyBillPayment;
	@ColumnName("vcccLastModificationTime")
	long lastModificationTime;
	@ColumnName("vcccIsDeleted")
	boolean isDeleted;
	
	
	
	
	public int getMaxNumberOfDaysForMonthlyBillPayment() {
		return maxNumberOfDaysForMonthlyBillPayment;
	}
	public void setMaxNumberOfDaysForMonthlyBillPayment(int maxNumberOfDaysForMonthlyBillPayment) {
		this.maxNumberOfDaysForMonthlyBillPayment = maxNumberOfDaysForMonthlyBillPayment;
	}
	/**
	 * @return the opticalFiberInstallationCharge
	 */
	public double getOpticalFiberInstallationCharge() {
		return opticalFiberInstallationCharge;
	}
	/**
	 * @param opticalFiberInstallationCharge the opticalFiberInstallationCharge to set
	 */
	public void setOpticalFiberInstallationCharge(double opticalFiberInstallationCharge) {
		this.opticalFiberInstallationCharge = opticalFiberInstallationCharge;
	}
	/**
	 * @return the oFChargePerMeter
	 * @throws Exception 
	 */
	public double getOFChargePerMeter() throws Exception {
		
		return OFChargePerMeter;
	}
	
	public double getOFChargePerMeter( long districtID ) throws Exception {
		
		DistrictOfcInstallationService districtOfcInstallationService = (DistrictOfcInstallationService)ServiceDAOFactory.getService(DistrictOfcInstallationService.class);
		return districtOfcInstallationService.getOfcInstallationCostByDistrictID( districtID );
	}
	
	/**
	 * @param oFChargePerMeter the oFChargePerMeter to set
	 */
	public void setOFChargePerMeter(double oFChargePerMeter) {
		OFChargePerMeter = oFChargePerMeter;
	}
	public double getLLCRentCost() {
		return LLCRentCost;
	}
	public void setLLCRentCost(double lLCRentCost) {
		LLCRentCost = lLCRentCost;
	}
	public double getLLCSellCost() {
		return LLCSellCost;
	}
	public void setLLCSellCost(double lLCSellCost) {
		LLCSellCost = lLCSellCost;
	}
	public double getDarkRentCost() {
		return darkRentCost;
	}
	public void setDarkRentCost(double darkRentCost) {
		this.darkRentCost = darkRentCost;
	}
	public double getDarkSellCost() {
		return darkSellCost;
	}
	public void setDarkSellCost(double darkSellCost) {
		this.darkSellCost = darkSellCost;
	}
	public double getFibreMaintenanceCost() {
		return fibreMaintenanceCost;
	}
	public void setFibreMaintenanceCost(double fibreMaintenanceCost) {
		this.fibreMaintenanceCost = fibreMaintenanceCost;
	}
	public double getFibreLayingCost() {
		return fibreLayingCost;
	}
	public void setFibreLayingCost(double fibreLayingCost) {
		this.fibreLayingCost = fibreLayingCost;
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
	public long getActivationDate() {
		return activationDate;
	}
	public String getActivationDateStr() {

		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy");
		
		return sdf.format( new Date( activationDate ) );
	}
	public void setActivationDate(long activationDate) {
		this.activationDate = activationDate;
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
	@Override
	public String toString() {
		return "CommonChargeDTO [id=" + id + ", categoryID=" + categoryID + ", shiftingCharge=" + shiftingCharge
				+ ", upgradationCharge=" + upgradationCharge + ", chargePerPoint=" + chargePerPoint + ", ratioL3L2="
				+ ratioL3L2 + ", minAdvancePaymentForMonth=" + minAdvancePaymentForMonth
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
