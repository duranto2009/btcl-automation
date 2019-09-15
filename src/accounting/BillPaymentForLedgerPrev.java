package accounting;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter@Setter
public class BillPaymentForLedgerPrev {
    String serial;
    String invoiceId;
    String monthYear;
    String btclAmount;
    String vat;
    String totalAmount;
    String bankName;
    String branchName;
    String paymentDate;
    String paidAmount;
}
