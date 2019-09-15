package lli.Application.ownership;

import java.util.HashMap;
import java.util.Map;

public class LLIOwnerChangeConstant {

    public final static String APPLICATION_DEMAND_NOTE = "application-demand-note";
    public final static String OWNER_CHANGE_SESSION_URL = "url";
    public static final String APPLICATION_SEARCH = "application-search";
    public static final int ENTITY_TYPE = 702;
    public static final int APPLICATION_COMPLETED = 1;

//    public static final String APPLICATION_DETAILS ="application-details" ;

    //state constants
    public enum STATE {
        SUBMITTED(500),
        REJECTED(501),
        APPLICATION_REOPEN(502),
        CLIENT_CORRECTION(503),
        SUBMIT_CORRECTION(504),
        REQUEST_FOR_CC(505),
        ACCOUNT_CC_POSITIVE(506),
        ACCOUNT_CC_NEGETIVE(507),
        FORWARD_TO_BUYER(508),
        RESPONSE_POSITIVE(509),
        RESPONSE_NEGETIVE(515),
        DEMAND_NOTE_GENERATED(510),
        PAYMENT_DONE(511),
        PAYMENT_VERIFIED(512),
        ADVICE_NOTE_PUBLISH(513),
        COMPLETE_CONNECTION(514);
        private final int id;
        STATE(int id) {
            this.id = id;
        }
        public int getValue() {
            return id;
        }
    }

    public static Map<Integer, Map<String, String>> stateMap = new HashMap<Integer, Map<String, String>>() {
        {

            put(STATE.SUBMITTED.getValue(), new HashMap<String, String>() {{
                put("url", "change-state");
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
            put(STATE.DEMAND_NOTE_GENERATED.getValue(), new HashMap<String, String>() {{
                put("url", "demand-note-generate");
                put("modal", "");
                put("view", "DEMAND_NOTE");
                put("redirect", "lli/dn/change-owner-dn-page.do");
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
            put(STATE.COMPLETE_CONNECTION.getValue(), new HashMap<String, String>() {{
                put("url", "complete-owner-change");
                put("modal", "");
                put("view", "");
                put("redirect", "");
                put("param", "");
            }});

            put(STATE.CLIENT_CORRECTION.getValue(), new HashMap<String, String>() {{
                put("url", "client-correction");
                put("modal", "");
                put("view", "");
                put("redirect", "");
                put("param", "");
            }});
            put(STATE.SUBMIT_CORRECTION.getValue(), new HashMap<String, String>() {{
                put("url", "change-state");
                put("modal", "");
                put("view", "");
                put("redirect", "");
                put("param", "");
            }});
            put(STATE.ACCOUNT_CC_NEGETIVE.getValue(), new HashMap<String, String>() {{
                put("url", "change-state");
                put("modal", "");
                put("view", "");
                put("redirect", "");
                put("param", "");
            }});
            put(STATE.ACCOUNT_CC_POSITIVE.getValue(), new HashMap<String, String>() {{
                put("url", "change-state");
                put("modal", "");
                put("view", "");
                put("redirect", "");
                put("param", "");
            }});
            put(STATE.REQUEST_FOR_CC.getValue(), new HashMap<String, String>() {{
                put("url", "change-state");
                put("modal", "");
                put("view", "");
                put("redirect", "");
                put("param", "");
            }});
            put(STATE.FORWARD_TO_BUYER.getValue(), new HashMap<String, String>() {{
                put("url", "change-state");
                put("modal", "");
                put("view", "");
                put("redirect", "");
                put("param", "");
            }});
            put(STATE.RESPONSE_POSITIVE.getValue(), new HashMap<String, String>() {{
                put("url", "change-state");
                put("modal", "");
                put("view", "");
                put("redirect", "");
                put("param", "");
            }});
            put(STATE.RESPONSE_NEGETIVE.getValue(), new HashMap<String, String>() {{
                put("url", "change-state");
                put("modal", "");
                put("view", "");
                put("redirect", "");
                put("param", "");
            }});
        }
    };
}
