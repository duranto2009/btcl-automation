package lli.configuration;


import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.TableName;

@TableName("at_lli_fixed_cost_config")
public class LLIFixedCostConfigurationDTO {
	@ColumnName("ID")
	@PrimaryKey
	public long id;
	
	@ColumnName("category_ID")
	public int categoryID = 1;
	
	@ColumnName("registration_charge")
	public double registrationCharge;
	
	@ColumnName("downgrade_charge")
	public double downgradeCharge;

	@ColumnName("port_charge")
	public double portCharge;
	
	@ColumnName("shifting_charge")
	public double shiftingCharge;
	
	@ColumnName("fiber_charge")
	public double fiberCharge;
	
	@ColumnName("cache_service_flat_rate")
	public double cacheServiceFlatRate;
	
	@ColumnName("instant_closing_charge")
	public double instantClosingCharge;
	
	@ColumnName("max_vat_percentage")
	public double maximumVatPercentage;
	
	@ColumnName("max_discount_percentage")
	public double maximumDiscountPercentage;
	
	@ColumnName("reconnection_charge")
	public double reconnectionCharge;
	
	@ColumnName("activationDate")
	public long activationDate;
	
	@CurrentTime
	@ColumnName("lastModificationTime")
	public long lastModificationTime;
	
	@ColumnName("isDeleted")
	public boolean isDeleted;

	@ColumnName("fixed_ip_count")
	public int fixedIpCount;
	@ColumnName("fixed_ip_charge")
	public double fixedIpCharge;
	@ColumnName("variable_ip_charge")
	public double variableIpCharge;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}

	public double getRegistrationCharge() {
		return registrationCharge;
	}

	public void setRegistrationCharge(double registrationCharge) {
		this.registrationCharge = registrationCharge;
	}

	public double getDowngradeCharge() {
		return downgradeCharge;
	}

	public void setDowngradeCharge(double downgradeCharge) {
		this.downgradeCharge = downgradeCharge;
	}

	public double getPortCharge() {
		return portCharge;
	}

	public void setPortCharge(double portCharge) {
		this.portCharge = portCharge;
	}

	public double getShiftingCharge() {
		return shiftingCharge;
	}

	public void setShiftingCharge(double shiftingCharge) {
		this.shiftingCharge = shiftingCharge;
	}

	public double getFiberCharge() {
		return fiberCharge;
	}

	public void setFiberCharge(double fiberCharge) {
		this.fiberCharge = fiberCharge;
	}

	public double getInstantClosingCharge() {
		return instantClosingCharge;
	}

	public void setInstantClosingCharge(double instantClosingCharge) {
		this.instantClosingCharge = instantClosingCharge;
	}

	public double getMaximumVatPercentage() {
		return maximumVatPercentage;
	}

	public void setMaximumVatPercentage(double maximumVatPercentage) {
		this.maximumVatPercentage = maximumVatPercentage;
	}

	public double getMaximumDiscountPercentage() {
		return maximumDiscountPercentage;
	}

	public void setMaximumDiscountPercentage(double maximumDiscountPercentage) {
		this.maximumDiscountPercentage = maximumDiscountPercentage;
	}

	public long getActivationDate() {
		return activationDate;
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

	public double getReconnectionCharge() {
		return reconnectionCharge;
	}

	public void setReconnectionCharge(double reconnectionCharge) {
		this.reconnectionCharge = reconnectionCharge;
	}

	public double getCacheServiceFlatRate() {
		return cacheServiceFlatRate;
	}

	public void setCacheServiceFlatRate(double cacheServiceFlatRate) {
		this.cacheServiceFlatRate = cacheServiceFlatRate;
	}

	public int getFixedIpCount() {
		return fixedIpCount;
	}

	public void setFixedIpCount(int fixedIpCount) {
		this.fixedIpCount = fixedIpCount;
	}

	public double getFixedIpCharge() {
		return fixedIpCharge;
	}

	public void setFixedIpCharge(double fixedIpCharge) {
		this.fixedIpCharge = fixedIpCharge;
	}

	public double getVariableIpCharge() {
		return variableIpCharge;
	}

	public void setVariableIpCharge(double variableIpCharge) {
		this.variableIpCharge = variableIpCharge;
	}
}
