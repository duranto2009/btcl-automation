package scheduler.entity;

public class BillingTasks {
    public void generateYearlyIPAddressBill() throws Exception {
        new IPTasks().generateYearlyBill();
    }

    public void generateYearlyCoLocationBill() throws Exception {
        new CoLocationTasks().calculateCoLocationYearlyBill();
    }
}
