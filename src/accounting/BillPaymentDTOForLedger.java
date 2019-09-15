package accounting;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter@Setter
public class BillPaymentDTOForLedger {
    String generationOrPaymentDate;
    String invoiceId;
    String billTypeMonthYear;

    String btclAmountDr;
    String vatDr;
    String totalAmountDr;

    String btclAmountCr;
    String vatCr;
    String totalAmountCr;

    String btclAmountBl;
    String vatBl;
    String totalAmountBl;

    String drOrCr;

    String bankName;
    String branchName;

}
