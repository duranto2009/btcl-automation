package jUnitTesting;

import java.util.HashMap;
import java.util.Map;

import util.TimeConverter;

public class TestJasperDomain {
	long ID = 1;
	long generationTime = 1234565789998L;
	long lastPaymentDate = 123456789998L;
	double discount = 2;
	double grandTotal = 1600.00;
	double lateFee = 120.00;
	double netPayable = 1603;
	double VAT = 5;
	double totalPayable = 1598.0;
	double teletalkCharge = java.lang.Math.ceil( netPayable / ( 1.0 - common.payment.constants.PaymentConstants.TELETALK.TELETALK_CHARGE ) ) - netPayable;
	double payable_using_teletalk = netPayable + teletalkCharge;
	public Map<String, Object> getPdfParamMap(){
		Map<String, Object> params = new HashMap<>();
		params.put( "registrant name", "Dummy Namasdfasdfhakdsjfhaksdjfhkadjsfhkajdfhkjadfhe");
		params.put( "registrant address", null );
		params.put( "registrant contact", "Dummy contact");
		params.put( "registrant email", "Dummy mail" );
		
		params.put( "username", "dummyusername" );
		params.put( "domainName", "D:\\Text.png" );
		params.put( "year", 5 );
		params.put( "applicationType", "Domain Renew" );
		params.put( "expirationDate", System.currentTimeMillis() );
		params.put( "paid", "D:\\paid2.png" );
		params.put( "logo_btcl", "D:\\logo_btcl.jpg" );
		params.put( "logo_bd", "D:\\logo_bd.png");
		params.put( "top_bangla_heading", "D:\\top_bangla_heading.png");
		params.put( "serviceStart",  TimeConverter.getDateTimeStringByMillisecAndDateFormat(2234567892234L + TimeConverter.MILLISECONDS_IN_YEAR* 5, "MMMM dd, yyyy"));
		params.put( "serviceEnd", 5 + "year(s) from payment receiving date");
		params.put( "packageNotFound", false);
		params.put( "package per year cost", 800.0);
		
		params.put( "teletalkCharge", teletalkCharge);
		params.put( "payable_using_teletalk", netPayable + teletalkCharge);
		return params;
	}

	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}

	public long getGenerationTime() {
		return generationTime;
	}

	public void setGenerationTime(long generationTime) {
		this.generationTime = generationTime;
	}

	public long getLastPaymentDate() {
		return lastPaymentDate;
	}

	public void setLastPaymentDate(long lastPaymentDate) {
		this.lastPaymentDate = lastPaymentDate;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public double getLateFee() {
		return lateFee;
	}

	public void setLateFee(double lateFee) {
		this.lateFee = lateFee;
	}

	public double getNetPayable() {
		return netPayable;
	}

	public void setNetPayable(double netPayable) {
		this.netPayable = netPayable;
	}

	public double getVAT() {
		return VAT;
	}

	public void setVAT(double vAT) {
		VAT = vAT;
	}

	public double getTotalPayable() {
		return totalPayable;
	}

	public void setTotalPayable(double totalPayable) {
		this.totalPayable = totalPayable;
	}
}
