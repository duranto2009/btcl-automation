package client.multipleMonthBill;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MultipleMonthBillRow {
    String serial;
    String billId;
    String billIssueDate;
    String month;
    String year;
    String btclAmount;
    String vat;
    String netCharge;
}
