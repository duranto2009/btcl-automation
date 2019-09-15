package application;

import common.RequestFailureException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum ApplicationPhase {
    //    DUMMY_STATE(0);
    SUBMIT("Submit"),
    CORRECTION("Correction"),
    REJECT("Reject"),
    REOPEN("Reopen"),
    ACCOUNT_CC("Account Check"),
    INTERNAL_FR("Internal FR"),
    EXTERNAL_FR("External FR"),
    DEMAND_NOTE("Demand Note"),
    PAYMENT_GIVEN("Payment Given"),
    PAYMENT_VERIFIED("Payment Verified"),
    PAYMENT_SKIP("Payment Skipped"),
    WORK_ORDER("Work Order"),
    ADVICE_NOTE("Advice Note"),
    TESTING("Testing"),
    COMPLETE("Complete"),
    APPROVAL("Approval"),
    UNDEFINED("Undefined")

    ;


    //    VPN_TESTING_PENDING	(7015),

    //endregion

    //endregion
    private static Map<ApplicationPhase, ApplicationPhaseClient> mapOfAppPhaseClientToAppPhase = new HashMap<>();
    static {

        mapOfAppPhaseClientToAppPhase.put(SUBMIT, ApplicationPhaseClient.PROCESSING);
        mapOfAppPhaseClientToAppPhase.put(CORRECTION, ApplicationPhaseClient.PROCESSING);
        mapOfAppPhaseClientToAppPhase.put(REJECT, ApplicationPhaseClient.REJECTED);
        mapOfAppPhaseClientToAppPhase.put(REOPEN, ApplicationPhaseClient.PROCESSING);
        mapOfAppPhaseClientToAppPhase.put(ACCOUNT_CC, ApplicationPhaseClient.PROCESSING);
        mapOfAppPhaseClientToAppPhase.put(INTERNAL_FR, ApplicationPhaseClient.PROCESSING);
        mapOfAppPhaseClientToAppPhase.put(EXTERNAL_FR, ApplicationPhaseClient.PROCESSING);
        mapOfAppPhaseClientToAppPhase.put(DEMAND_NOTE, ApplicationPhaseClient.DEMAND_NOTE_GENERATED);
        mapOfAppPhaseClientToAppPhase.put(PAYMENT_GIVEN, ApplicationPhaseClient.PAYMENT_DONE);
        mapOfAppPhaseClientToAppPhase.put(PAYMENT_VERIFIED, ApplicationPhaseClient.PAYMENT_DONE);
        mapOfAppPhaseClientToAppPhase.put(PAYMENT_SKIP, ApplicationPhaseClient.DEMAND_NOTE_GENERATED);
        mapOfAppPhaseClientToAppPhase.put(WORK_ORDER, ApplicationPhaseClient.PROCESSING);
        mapOfAppPhaseClientToAppPhase.put(ADVICE_NOTE, ApplicationPhaseClient.PROCESSING);
        mapOfAppPhaseClientToAppPhase.put(TESTING, ApplicationPhaseClient.PROCESSING);
        mapOfAppPhaseClientToAppPhase.put(COMPLETE, ApplicationPhaseClient.COMPLETED);
        mapOfAppPhaseClientToAppPhase.put(APPROVAL, ApplicationPhaseClient.PROCESSING);
    }

    public static ApplicationPhaseClient getApplicationPhaseClientByApplicationPhase(ApplicationPhase applicationPhase) {
        return mapOfAppPhaseClientToAppPhase.getOrDefault(applicationPhase, null);
    }

    private String phase;

    ApplicationPhase(String phase) {
        this.phase = phase;
    }


    public static ApplicationPhase getApplicationPhase(String phase) {
        List<ApplicationPhase> list = Arrays.stream(ApplicationPhase.values())
                .filter(t -> t.name().equals(phase))
                .collect(Collectors.toList());

        if (list.isEmpty()) {
            throw new RequestFailureException("No Application Phase Found with Phase Name : " + phase);
        } else if (list.size() > 1) {
            throw new RequestFailureException("Multiple Application Phase Found with Phase Name : " + phase);
        }
        return list.get(0);
    }

    public String getPhase() {
        return this.phase;
    }

}
