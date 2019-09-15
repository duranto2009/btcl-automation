package application;

import java.util.*;

public enum ApplicationPhaseClient {
//    INITIALISED,
    PROCESSING("Processing"),
    REJECTED("Rejected"),
    DEMAND_NOTE_GENERATED("Demand Note Generated"),
    PAYMENT_DONE("Payment Done"),
    COMPLETED("Completed");
    private String title;
    ApplicationPhaseClient(String title){
        this.title = title;
    }
    public String getTitle() {
        return this.title;
    }

    public static List<ApplicationPhase> getApplicationPhases(ApplicationPhaseClient applicationPhaseClient) {
        return map.getOrDefault(applicationPhaseClient, null);
    }

    private static Map<ApplicationPhaseClient, List<ApplicationPhase>> map = new HashMap<>();
    static {

        map.put(PROCESSING,
                Arrays.asList(
                        ApplicationPhase.ACCOUNT_CC,
                        ApplicationPhase.INTERNAL_FR,
                        ApplicationPhase.EXTERNAL_FR,
                        ApplicationPhase.PAYMENT_VERIFIED,
                        ApplicationPhase.PAYMENT_SKIP,
                        ApplicationPhase.WORK_ORDER,
                        ApplicationPhase.ADVICE_NOTE,
                        ApplicationPhase.TESTING,
                        ApplicationPhase.APPROVAL,


                        ApplicationPhase.SUBMIT,
                        ApplicationPhase.CORRECTION,
                        ApplicationPhase.REOPEN
                )
        );

        map.put(REJECTED,
                Collections.singletonList(
                        ApplicationPhase.REJECT
                )
        );

        map.put(DEMAND_NOTE_GENERATED,
                Collections.singletonList(
                        ApplicationPhase.DEMAND_NOTE
                )
        );

        map.put(PAYMENT_DONE,
                Collections.singletonList(
                         ApplicationPhase.PAYMENT_GIVEN
                )
        );


        map.put(COMPLETED,
                Collections.singletonList(
                        ApplicationPhase.COMPLETE
                )
        );
    }
}
