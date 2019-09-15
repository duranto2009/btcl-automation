package coLocation;

import java.util.HashMap;
import java.util.Map;


public class CoLocationConstants {

    public final static String COLOCATION_DETAILS_PAGE_URL = "/co-location/new-connection-application-details.do?id=";

    public static final String CONNECTION_DETAILS = "connection-details";
    public final static String CO_LOCATION_BASE_URL = "co-location";
    public final static String CO_LOCATION_SESSION_URL = "url";


    public final static String APPLICATION_SEARCH = "new-connection-application-search";

    public final static String CONNECTION_SEARCH = "connection-search";
    //inventory
    public final static String INVENTORY_ADD = "inventory-add";
    public final static String INVENTORY_SEARCH = "co-location-inventory-search";

    public final static String INVENTORY_COST_CONFIG_ADD = "inventory-cost-config";
    public final static String INVENTORY_COST_CONFIG_SEARCH = "co-location-inventory-cost-config-search";



    //new connection
    public final static String NEW_CONNECTION_APPLICATION = "new-connection-application";
    public final static String UPGRADE_APPLICATION = "co-location-application-upgrade-application";
    public final static String DOWNGRADE_APPLICATION = "co-location-application-downgrade-application";
    public final static String REVISE_APPLICATION = "co-location-application-revise-application";
    public final static String NEW_CONNECTION_VIEW_DETAILS = "new-connection-application-details";
    public final static String NEW_CONNECTION_VIEW_DETAILS_FULL = "new-connection-application-details-full";

    public final static String NEW_CONNECTION_APPLICATION_DEMAND_NOTE = "new-connection-application-demand-note";




    //td
    public final static String RECONNECT = "co-location-reconnect";
    public final static String CLOSE_CONNECTION = "co-location-close-connection";
    public final static String PROBABLE_TD = "co-location-probable-td";

    public static final int ENTITY_TYPE = 402;
    public static final int MAXIMUM_VAT_PERCENTAGE_TYPE = 2;


    public static final int INVENTORY_RACK = 6;
    public static final int INVENTORY_POWER= 7;
    public static final int INVENTORY_FIBER = 8;
//    public static final int INVENTORY_FLOOR_SPACE = 10;

    public static final int SPACE_OFC_ROLE=20002;
    public  static final int POWER_ROLE=20005;
    public  static final int DGM_BROADBAND=20004;


    public static Map<Integer, Map<String, String>> stateMap = new HashMap<Integer, Map<String, String>>() {
        {
            put(STATE.SUBMITTED.getValue(), new HashMap<String, String>() {{
                put("url", "");
                put("modal", "");
                put("view", "");
                put("redirect", "");
                put("param", "");
            }});
            put(STATE.PRECHECK_DONE.getValue(), new HashMap<String, String>() {{
                put("url", "client-correction");
                put("modal", "");
                put("view", "");
                put("redirect", "");
                put("param", "");
            }});
            put(STATE.REJECTED.getValue(), new HashMap<String, String>() {{
                put("url", "change-state");
                put("modal", "");
                put("view", "");
                put("redirect", "");
                put("param", "");
            }});
            put(STATE.APPLICATION_REOPEN.getValue(), new HashMap<String, String>() {{
                put("url", "change-state");
                put("modal", "");
                put("view", "");
                put("redirect", "");
                put("param", "");
            }});
            put(STATE.IFR_SUBMITTED.getValue(), new HashMap<String, String>() {{
                put("url", "ifr-request");
                put("modal", "");
                put("view", "IFR_REQUEST_VIEW");
                put("redirect", "");
                put("param", "");
            }});
            put(STATE.IFR_RESPONDED.getValue(), new HashMap<String, String>() {{
                put("url", "ifr-response");
                put("modal", "");
                put("view", "IFR_DETAILS_VIEW");
                put("redirect", "");
                put("param", "");
            }});
            put(STATE.FORWARD_IFR_TO_ADMIN.getValue(), new HashMap<String, String>() {{
                put("url", "change-state");
                put("modal", "");
                put("view", "IFR_DETAILS_VIEW");
                put("redirect", "");
                put("param", "");
            }});
            put(STATE.APPROVE_IFR_ADMIN.getValue(), new HashMap<String, String>() {{
                put("url", "change-state");
                put("modal", "");
                put("view", "IFR_DETAILS_VIEW");
                put("redirect", "");
                put("param", "");
            }});
            put(STATE.DEMAND_NOTE_GENERATED.getValue(), new HashMap<String, String>() {{
                put("url", "demand-note-generate");
                put("modal", "");
                put("view", "DEMAND_NOTE");
                put("redirect", "co-location/dn/new.do");
                put("param", "applicationID_nextState");
            }});
            put(STATE.DEMAND_NOTE_GENERATED_SKIP.getValue(), new HashMap<String, String>() {{
                put("url", "demand-note-generate-skip");
                put("modal", "");
                put("view", "DEMAND_NOTE");
                put("redirect", "co-location/dn/new.do");
                put("param", "applicationID_nextState");
            }});
            put(STATE.PAYMENT_DONE.getValue(), new HashMap<String, String>() {{
                put("url", "no-state-change");
                put("modal", "");
                put("view", "");
                put("redirect", "MultipleBillPaymentView.do");
                put("param", "billIDs");
            }});
            put(STATE.PAYMENT_VERIFIED.getValue(), new HashMap<String, String>() {{
                put("url", "no-state-change");
                put("modal", "");
                put("view", "");
                put("redirect", "common/payment/linkPayment.jsp");
                put("param", "paymentID_moduleID");
            }});
            put(STATE.ADVICE_NOTE_PUBLISH.getValue(), new HashMap<String, String>() {{
                put("url", "advice-note-generate");
                put("modal", "#forwardAdviceNote");
                put("view", "ADVICE_NOTE");
                put("redirect","");
                put("param", "");
            }});
            put(STATE.ADVICE_NOTE_FORWARD.getValue(), new HashMap<String, String>() {{
                put("url", "change-state");
                put("modal", "");
                put("view", "ADVICE_NOTE");
                put("redirect","");
                put("param", "");
            }});
            put(STATE.COMPLETE_SETUP.getValue(), new HashMap<String, String>() {{
                put("url", "complete-setup");
                put("modal", "");
                put("view", "");
                put("redirect", "");
                put("param", "");
            }});
            put(STATE.COMPLETE_CONNECTION.getValue(), new HashMap<String, String>() {{
                put("url", "complete-connection");
//                put("modal", "COMPLETE_CONNECTION");
                put("modal", "#connectionCompletionModal");
                put("view", "");
                put("redirect", "");
                put("param", "");
            }});

            put(STATE.CLIENT_CORRECTION.getValue(), new HashMap<String, String>() {{
                put("url", "change-state");
                put("modal", "");
                put("view", "");
                put("redirect", "");
                put("param", "");
            }});

            put(STATE.REJECT_CLOSE.getValue(), new HashMap<String, String>() {{
                put("url", "change-state");
                put("modal", "");
                put("view", "");
                put("redirect", "");
                put("param", "");
            }});
            put(STATE.ADVICE_NOTE_FORWARD_SPACE.getValue(), new HashMap<String, String>() {{
                put("url", "change-state");
                put("modal", "");
                put("view", "ADVICE_NOTE");
                put("redirect","");
                put("param", "");
            }});
            put(STATE.ADVICE_NOTE_FORWARD_DGMB.getValue(), new HashMap<String, String>() {{
                put("url", "change-state");
                put("modal", "");
                put("view", "ADVICE_NOTE");
                put("redirect","");
                put("param", "");
            }});
            put(STATE.ADVICE_NOTE_FORWARD_AMD.getValue(), new HashMap<String, String>() {{
                put("url", "change-state");
                put("modal", "");
                put("view", "ADVICE_NOTE");
                put("redirect","");
                put("param", "");
            }});
            put(STATE.APPROVE_BY_ADMIN.getValue(), new HashMap<String, String>() {{
                put("url", "change-state");
                put("modal", "");
                put("view", "");
                put("redirect","");
                put("param", "");
            }});

            put(STATE.DOWNGRADE_APPLICATION_SUBMITTED.getValue(), new HashMap<String, String>() {{
                put("url", "");
                put("modal", "");
                put("view", "");
                put("redirect", "");
                put("param", "");
            }});
            put(STATE.DOWNGRADE_APPLICATION_REJECTED.getValue(), new HashMap<String, String>() {{
                put("url", "change-state");
                put("modal", "");
                put("view", "");
                put("redirect", "");
                put("param", "");
            }});
            put(STATE.DOWNGRADE_APPLICATION_ADVICE_NOTE_PUBLISH.getValue(), new HashMap<String, String>() {{
                put("url", "advice-note-generate");
                put("modal", "#forwardAdviceNote");
                put("view", "ADVICE_NOTE");
                put("redirect","");
                put("param", "");
            }});

            put(STATE.DOWNGRADE_APPLICATION_CLIENT_CORRECTION.getValue(), new HashMap<String, String>() {{
                put("url", "change-state");
                put("modal", "");
                put("view", "");
                put("redirect", "");
                put("param", "");
            }});
            put(STATE.DOWNGRADE_APPLICATION_REOPEN.getValue(), new HashMap<String, String>() {{
                put("url", "change-state");
                put("modal", "");
                put("view", "");
                put("redirect", "");
                put("param", "");
            }});
            put(STATE.DOWNGRADE_APPLICATION_ADVICE_NOTE_FORWARD.getValue(), new HashMap<String, String>() {{
                put("url", "change-state");
                put("modal", "");
                put("view", "ADVICE_NOTE");
                put("redirect","");
                put("param", "");
            }});
            put(STATE.DOWNGRADE_APPLICATION_ADVICE_NOTE_FORWARD_SPACE.getValue(), new HashMap<String, String>() {{
                put("url", "change-state");
                put("modal", "");
                put("view", "ADVICE_NOTE");
                put("redirect","");
                put("param", "");
            }});
            put(STATE.DOWNGRADE_APPLICATION_ADVICE_NOTE_FORWARD_DGMB.getValue(), new HashMap<String, String>() {{
                put("url", "change-state");
                put("modal", "");
                put("view", "ADVICE_NOTE");
                put("redirect","");
                put("param", "");
            }});
            put(STATE.DOWNGRADE_APPLICATION_ADVICE_NOTE_FORWARD_AMD.getValue(), new HashMap<String, String>() {{
                put("url", "change-state");
                put("modal", "");
                put("view", "ADVICE_NOTE");
                put("redirect","");
                put("param", "");
            }});
            put(STATE.DOWNGRADE_APPLICATION_SETUP_COMPLETE.getValue(), new HashMap<String, String>() {{
                put("url", "complete-setup");
                put("modal", "");
                put("view", "");
                put("redirect", "");
                put("param", "");
            }});
            put(STATE.DOWNGRADE_APPLICATION_COMPLETE_CONNECTION.getValue(), new HashMap<String, String>() {{
                put("url", "complete-connection");
//                put("modal", "COMPLETE_CONNECTION");
                put("modal", "#connectionCompletionModal");
                put("view", "");
                put("redirect", "");
                put("param", "");
            }});
            put(STATE.DOWNGRADE_APPLICATION_APPROVE_BY_ADMIN.getValue(), new HashMap<String, String>() {{
                put("url", "change-state");
                put("modal", "");
                put("view", "");
                put("redirect","");
                put("param", "");
            }});


            //TD
            put(STATE.TD_APPLICATION_SUBMITTED.getValue(), new HashMap<String, String>() {{
                put("url", "change-state");
                put("modal", "");
                put("view", "");
                put("redirect","");
                put("param", "");
            }});

            put(STATE.TD_APPLICATION_REJECTED.getValue(), new HashMap<String, String>() {{
                put("url", "reject-td");
                put("modal", "");
                put("view", "");
                put("redirect","");
                put("param", "");
            }});


            put(STATE.TD_APPLICATION_REJECTED_CLOSE.getValue(), new HashMap<String, String>() {{
                put("url", "change-state");
                put("modal", "");
                put("view", "");
                put("redirect","");
                put("param", "");
            }});



            put(STATE.TD_APPLICATION_ADVICE_NOTE_PUBLISH.getValue(), new HashMap<String, String>() {{
                put("url", "advice-note-generate");
                put("modal", "#forwardAdviceNote");
                put("view", "ADVICE_NOTE");
                put("redirect","");
                put("param", "");
            }});



            put(STATE.TD_APPLICATION_APPROVE_BY_ADMIN.getValue(), new HashMap<String, String>() {{
                put("url", "change-state");
                put("modal", "");
                put("view", "");
                put("redirect","");
                put("param", "");
            }});




            put(STATE.TD_APPLICATION_CONNECTION_COMPLETE.getValue(), new HashMap<String, String>() {{
                put("url", "complete-connection");
                put("modal", "");
                put("view", "");
                put("redirect","");
                put("param", "");
            }});


            // RECONNECT
            put(STATE.RECONNECT_APPLICATION_SUBMITTED.getValue(), new HashMap<String, String>() {{
                put("url", "change-state");
                put("modal", "");
                put("view", "");
                put("redirect", "");
                put("param", "");
            }});
            put(STATE.RECONNECT_APPLICATION_REJECTED.getValue(), new HashMap<String, String>() {{
                put("url", "change-state");
                put("modal", "");
                put("view", "");
                put("redirect", "");
                put("param", "");
            }});
            put(STATE.RECONNECT_APPLICATION_DEMAND_NOTE_GENERATED.getValue(), new HashMap<String, String>() {{
                put("url", "demand-note-generate");
                put("modal", "");
                put("view", "DEMAND_NOTE");
                put("redirect", "co-location/dn/new.do");
                put("param", "applicationID_nextState");
            }});
            put(STATE.DEMAND_NOTE_GENERATED_SKIP.getValue(), new HashMap<String, String>() {{
                put("url", "demand-note-generate-skip");
                put("modal", "");
                put("view", "DEMAND_NOTE");
                put("redirect", "co-location/dn/new.do");
                put("param", "applicationID_nextState");
            }});
            put(STATE.RECONNECT_APPLICATION_PAYMENT_DONE.getValue(), new HashMap<String, String>() {{
                put("url", "no-state-change");
                put("modal", "");
                put("view", "");
                put("redirect", "MultipleBillPaymentView.do");
                put("param", "billIDs");
            }});
            put(STATE.RECONNECT_APPLICATION_PAYMENT_VERIFIED.getValue(), new HashMap<String, String>() {{
                put("url", "no-state-change");
                put("modal", "");
                put("view", "");
                put("redirect", "common/payment/linkPayment.jsp");
                put("param", "paymentID_moduleID");
            }});
            put(STATE.RECONNECT_APPLICATION_ADVICE_NOTE_PUBLISH.getValue(), new HashMap<String, String>() {{
                put("url", "advice-note-generate");
                put("modal", "#forwardAdviceNote");
                put("view", "ADVICE_NOTE");
                put("redirect","");
                put("param", "");
            }});
//            put(STATE.ADVICE_NOTE_FORWARD.getValue(), new HashMap<String, String>() {{
//                put("url", "change-state");
//                put("modal", "");
//                put("view", "ADVICE_NOTE");
//                put("redirect","");
//                put("param", "");
//            }});
            put(STATE.RECONNECT_APPLICATION_CONNECTION_COMPLETE.getValue(), new HashMap<String, String>() {{
                put("url", "complete-connection");
                put("modal", "");
                put("view", "");
                put("redirect", "");
                put("param", "");
            }});
//            put(STATE.COMPLETE_CONNECTION.getValue(), new HashMap<String, String>() {{
//                put("url", "complete-connection");
////                put("modal", "COMPLETE_CONNECTION");
//                put("modal", "#connectionCompletionModal");
//                put("view", "");
//                put("redirect", "");
//                put("param", "");
//            }});
            put(STATE.RECONNECT_APPLICATION_APPROVE_BY_ADMIN.getValue(), new HashMap<String, String>() {{
                put("url", "change-state");
                put("modal", "");
                put("view", "");
                put("redirect","");
                put("param", "");
            }});






        }
    };
    public static final int NEW_CONNECTION = 1;
    public static final int UPGRADE_CONNECTION = 2;
    public static final int DOWNGRADE_CONNECTION = 3;
    public static final int TD = 4;
    public static final int RECONNECT_CONNECTION = 5;
    public static final int CLOSE= 6;

public static Map<Integer, String> applicationTypeNameMap = new HashMap<>();
static {
    applicationTypeNameMap.put(NEW_CONNECTION, "New Connection");
    applicationTypeNameMap.put(UPGRADE_CONNECTION, "Upgrade Connection");
    applicationTypeNameMap.put(DOWNGRADE_CONNECTION, "Downgrade Connection");
    applicationTypeNameMap.put(TD, "Temporary Disconnect");
    applicationTypeNameMap.put(RECONNECT_CONNECTION, "Reconnect Connection");
    applicationTypeNameMap.put(CLOSE, "Close Connection");
//    put(DOWNGRADE_BANDWIDTH, "Downgrade Bandwidth");
//    put(TEMPORARY_UPGRADE_BANDWIDTH, "Temporary Upgrade Bandwidth");
//    put(ADDITIONAL_PORT, "Additional Port");

}


    //state constants
    public enum STATE {











        SUBMITTED(6001),
        PRECHECK_DONE(6002),
        REJECTED(6003),
        APPLICATION_REOPEN(6004),
        IFR_SUBMITTED(6005),
        IFR_RESPONDED(6006),
        DEMAND_NOTE_GENERATED(6007),
        DEMAND_NOTE_GENERATED_SKIP(6019),
        PAYMENT_DONE(6008),
        PAYMENT_VERIFIED(6009),
        ADVICE_NOTE_PUBLISH(6010),
        ADVICE_NOTE_FORWARD(6011),
        ADVICE_NOTE_FORWARD_SPACE(6015),
        ADVICE_NOTE_FORWARD_DGMB(6017),
        ADVICE_NOTE_FORWARD_AMD(6018),
        APPROVE_BY_ADMIN(6020),
        FORWARD_IFR_TO_ADMIN(6021),
        APPROVE_IFR_ADMIN(6022),
        COMPLETE_SETUP(6012),
        COMPLETE_CONNECTION(6016),
        CLIENT_CORRECTION(6013),
        REJECT_CLOSE(6014),

        DOWNGRADE_APPLICATION_SUBMITTED(6101),
//        DOWNGRADE_APPLICATION_SUBMITTED(6101),
        DOWNGRADE_APPLICATION_REJECTED(6103),
        DOWNGRADE_APPLICATION_CLIENT_CORRECTION(6108),
        DOWNGRADE_APPLICATION_ADVICE_NOTE_PUBLISH(6105),
        DOWNGRADE_APPLICATION_ADVICE_NOTE_FORWARD(6106),
        DOWNGRADE_APPLICATION_SETUP_COMPLETE(6107),
        DOWNGRADE_APPLICATION_ADVICE_NOTE_FORWARD_SPACE(6110),
        DOWNGRADE_APPLICATION_ADVICE_NOTE_FORWARD_AMD(6113),
        DOWNGRADE_APPLICATION_ADVICE_NOTE_FORWARD_DGMB(6114),
        DOWNGRADE_APPLICATION_REOPEN(6104),
        DOWNGRADE_APPLICATION_COMPLETE_CONNECTION(6112),
        DOWNGRADE_APPLICATION_APPROVE_BY_ADMIN(6115),

        TD_APPLICATION_SUBMITTED(6201),
        TD_APPLICATION_REJECTED(6202),
        TD_APPLICATION_REJECTED_CLOSE(6203),
        TD_APPLICATION_ADVICE_NOTE_PUBLISH(6204),
        TD_APPLICATION_CONNECTION_COMPLETE(6205),
        TD_APPLICATION_APPROVE_BY_ADMIN(6206),


        RECONNECT_APPLICATION_SUBMITTED(6501),
        RECONNECT_APPLICATION_REJECTED(6502),
        RECONNECT_APPLICATION_DEMAND_NOTE_GENERATED(6503),
        RECONNECT_APPLICATION_PAYMENT_DONE(6504),
        RECONNECT_APPLICATION_PAYMENT_VERIFIED(6505),
        RECONNECT_APPLICATION_ADVICE_NOTE_PUBLISH(6506),
        RECONNECT_APPLICATION_CONNECTION_COMPLETE(6507),
        RECONNECT_APPLICATION_APPROVE_BY_ADMIN(6508) ;

        private final int id;
        STATE(int id) {
            this.id = id;
        }
        public int getValue() {
            return id;
        }
    }

//    public final static int ADVICE_NOTE_FORWARD = 6001;


    public static final int CONNECTION_TYPE_REGULAR = 1;
    public static final int CONNECTION_TYPE_SPECIAL = 2;
//    public static final int CONNECTION_TYPE_REGULAR = 1;

    public static Map<Integer, String> connectionTypeNameMap = new HashMap<>();
    static{
        connectionTypeNameMap.put(CONNECTION_TYPE_REGULAR, "Regular");
        connectionTypeNameMap.put(CONNECTION_TYPE_SPECIAL, "Special Connection");

    }

    public static final int INVENTORY_CATEGORY_RACK_SIZE = 1;
    public static final int INVENTORY_CATEGORY_POWER_TYPE = 2;
    public static final int INVENTORY_CATEGORY_FIBER_TYPE = 3;
    public static final int INVENTORY_CATEGORY_RACK_SPACE = 4;
    public static final int INVENTORY_CATEGORY_POWER_AMOUNT = 5;

    public static final int INVENTORY_CATEGORY_FLOOR_SPACE = 9;




    public static final int STATUS_INACTIVE = 0;
    public static final int STATUS_ACTIVE = 1;
    public static final int STATUS_TD = 2;
    public static final int STATUS_CLOSED = 3;
    public static Map<Integer, String> connectionStatusNameMap = new HashMap<>();
    static {
        connectionStatusNameMap.put(STATUS_INACTIVE, "Inactive");
        connectionStatusNameMap.put(STATUS_ACTIVE, "Active");
        connectionStatusNameMap.put(STATUS_TD, "Temporary Disconnect");
        connectionStatusNameMap.put(STATUS_CLOSED, "Closed");
//    put(DOWNGRADE_BANDWIDTH, "Downgrade Bandwidth");
//    put(TEMPORARY_UPGRADE_BANDWIDTH, "Temporary Upgrade Bandwidth");
//    put(ADDITIONAL_PORT, "Additional Port");

    }

    public static Map<Integer, String> categoryNameMap= new HashMap<>();
    static{
        categoryNameMap.put(INVENTORY_POWER, "Power");
        categoryNameMap.put(INVENTORY_RACK, "Rack");
        categoryNameMap.put(INVENTORY_FIBER,"Fiber");
        categoryNameMap.put(INVENTORY_CATEGORY_FLOOR_SPACE,"Floor Space");
    }

    public static final int POWER_TYPE_AC = 4;
    public static final int POWER_TYPE_DC = 9;



}
