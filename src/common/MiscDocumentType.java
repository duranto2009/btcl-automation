package common;

import lombok.Getter;

import java.util.Arrays;

public enum MiscDocumentType {

    REQUEST_LETTER(1),
    SUBSCRIBER_LEDGER_REPORT(100),
    CLEARANCE_CERTIFICATE_NO_DUE(2),
    CLEARANCE_CERTIFICATE_WITH_DUE(3),
    FINAL_BILL(4),
    MULTIPLE_BILL(5);

    @Getter
    private int type;

    MiscDocumentType(int type) {
        this.type = type;
    }

    public static MiscDocumentType getMiscDocumentTypeByTypeId(int type) {
        return Arrays.stream(MiscDocumentType.values())
                .filter(t -> t.getType() == type).findFirst()
                .orElseThrow(() -> new RequestFailureException("No document type found with type: " + type));
    }


}
