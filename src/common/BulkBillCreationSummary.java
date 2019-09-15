package common;

import java.util.*;

public class BulkBillCreationSummary {
	public int numberOfExpectedBill;
	public int numberOfCreatedBill;
	public int numberOfFailedAttemp;
	public List<Long> listOfEntityIDForFailedAttemp = new ArrayList<>();
	public String stackTrace;
	@Override
	public String toString() {
		return "BulkBillCreationSummary [numberOfExpectedBill=" + numberOfExpectedBill + ", numberOfCreatedBill="
				+ numberOfCreatedBill + ", numberOfFailedAttemp=" + numberOfFailedAttemp
				+ ", listOfEntityIDForFailedAttemp=" + listOfEntityIDForFailedAttemp + ", stackTrace=" + stackTrace
				+ "]";
	}
}
