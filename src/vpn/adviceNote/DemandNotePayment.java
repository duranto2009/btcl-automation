package vpn.adviceNote;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DemandNotePayment {
    @Builder.Default String id = "";
    @Builder.Default String amount = "";
    @Builder.Default String generationdate = "";
    @Builder.Default String status = "";
    @Builder.Default String paymentdate = "";
    @Builder.Default String medium = "";
    @Builder.Default String bank = "";
    @Builder.Default String branch = "";
}