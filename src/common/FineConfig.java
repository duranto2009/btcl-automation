package common;
import java.util.*;

import util.TimeConverter;

import annotation.*;
@TableName("at_bill_fine_config")
public class FineConfig {
	
	public static final int CONSTANT_FINE = 1;
	public static final int  FINE_IN_PERCENTAGE = 2;
	
	@PrimaryKey
	@ColumnName("blfcID")
	long ID;
	@ColumnName("blfcActivationDate")
	long activationDate;
	@ColumnName("blfcEntityTypeID")
	int entityTypeID;
	@ColumnName("blfcFine")
	double fine;
	@ColumnName("blfcFineType")
	int fineType;
	@ColumnName("blfcDays")
	int days;
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public long getActivationDate() {
		return activationDate;
	}
	public void setActivationDate(long activationDate) {
		this.activationDate = activationDate;
	}
	public int getEntityTypeID() {
		return entityTypeID;
	}
	public void setEntityTypeID(int entityTypeID) {
		this.entityTypeID = entityTypeID;
	}
	public double getFine() {
		return fine;
	}
	public void setFine(double fine) {
		this.fine = fine;
	}
	public int getFineType() {
		return fineType;
	}
	public void setFineType(int fineType) {
		this.fineType = fineType;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	
	
	public static double getFineOfBill(double paymentWithoutFine,long expirydate,List<FineConfig> billFineConfigs) {
		double fine = 0;
		
		if(expirydate<System.currentTimeMillis()){
			return fine;
		}
		int daysAfterExpiration = (int)((System.currentTimeMillis()-expirydate)/TimeConverter.MILLS_IN_A_DAY);
		for(FineConfig billFineConfig: billFineConfigs){
			 if(billFineConfig.getDays() >= daysAfterExpiration){
				 if(billFineConfig.getFineType() == FineConfig.CONSTANT_FINE)
				 {
					 fine = billFineConfig.getFine();
				 }else if(billFineConfig.getFineType() == FineConfig.FINE_IN_PERCENTAGE){
					 fine = paymentWithoutFine* billFineConfig.getFine() / 100;
				 }
				 return fine;
			 }
		
		}
		
		throw new RequestFailureException("Bill has totally expired no");
	}
}
