package lli;

import java.util.HashMap;
import java.util.Map;

import annotation.*;
import dataConverter.DateTime.DateToEndOfTheDayConverter;
import dataConverter.DateTime.DateToStartOfTheDayConverter;

@TableName("at_lli_longterm_contract")
public class LLILongTermContract {

    public static final int STATUS_ACTIVE = 1;
    public static final int STATUS_BROKEN = 2;
    public static final int STATUS_OWNERSHIP_CHANGED = 3;

    @PrimaryKey
    @ColumnName("historyID")
    long historyID;
    @ColumnName("ID")
    long ID;
    @ColumnName("clientID")
    long clientID;

    @ColumnName("contractStartDate")
    @ManipulateData(DateToStartOfTheDayConverter.class)
    long contractStartDate;

    @ColumnName("contractEndDate")
    @ManipulateData(DateToEndOfTheDayConverter.class)
    long contractEndDate;

    @ColumnName("activeFrom")
    @ManipulateData(DateToStartOfTheDayConverter.class)
    long activeFrom;

    @ColumnName("activeTo")
    @ManipulateData(DateToEndOfTheDayConverter.class)
    long activeTo;

    @ColumnName("bandwidth")
    double bandwidth;

    @ColumnName("validFrom")
    @ManipulateData(DateToStartOfTheDayConverter.class)
    long validFrom;

    @ColumnName("validTo")
    @ManipulateData(DateToEndOfTheDayConverter.class)
    long validTo;

    @ColumnName("status")
    int status;
    
    public long getHistoryID() {
        return historyID;
    }

    public void setHistoryID(long historyID) {
        this.historyID = historyID;
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

    public long getActiveFrom() {
        return activeFrom;
    }

    public void setActiveFrom(long activeFrom) {
        this.activeFrom = activeFrom;
    }

    public long getActiveTo() {
        return activeTo;
    }

    public void setActiveTo(long activeTo) {
        this.activeTo = activeTo;
    }

    public double getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(double bandwidth) {
        this.bandwidth = bandwidth;
    }

    public long getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(long validFrom) {
        this.validFrom = validFrom;
    }

    public long getContractStartDate() {
        return contractStartDate;
    }

    public void setContractStartDate(long contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public long getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(long contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public long getValidTo() {
        return validTo;
    }

    public void setValidTo(long validTo) {
        this.validTo = validTo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static Map<Integer, String> statusMap = new HashMap() {{
        put(STATUS_ACTIVE, "Active");
        put(STATUS_BROKEN, "Broken");
        put(STATUS_OWNERSHIP_CHANGED, "Ownership Changed");
    }};

    @Override
    public String toString() {
        return "LLILongTermContract [historyID=" + historyID + ", ID=" + ID + ", clientID=" + clientID
                + ", contractStartDate=" + contractStartDate + ", contractEndDate=" + contractEndDate + ", activeFrom="
                + activeFrom + ", activeTo=" + activeTo + ", bandwidth=" + bandwidth + ", validFrom=" + validFrom
                + ", validTo=" + validTo + ", status=" + status + "]";
    }
}
