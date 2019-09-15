package vpn.bill;

public class Installment {

	int month;
	int year;
	double installment;
	
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
	public double getInstallment() {
		return installment;
	}
	public void setInstallment(double installment) {
		this.installment = installment;
	}
	
	public double getVat() {
		
		return ( installment / 1.15 ) * 0.15;
	}
	
	public double getBtclAmount() {
		
		return installment / 1.15;
	}
}
