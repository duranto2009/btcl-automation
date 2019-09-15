package nix.constants;

import java.util.HashMap;
import java.util.Map;

public class NIXConstants {


    public final static String NIX_DETAILS_PAGE_URL = "/nix/application/newview.do?id=";
    public final static String NIX_REVISE_DETAILS_PAGE_URL ="/nix/revise/newview.do?id=";

    public static final int NIX_NEW_CONNECTION_STATE = 5001;
    public static final int NIX_DOWNGRADE_STATE = 5100;
    public static final int NIX_CLOSE_STATE = 5200;
    public static final int NIX_CLOSE_STATE_WITHOUT_WO = 5250;



    public static final int NEW_CONNECTION_APPLICATION = 1;
    public static final int NIX_UPGRADE_APPLICATION = 2;
    public static final int NIX_DOWNGRADE_APPLICATION = 3;
    public static final int NIX_CLOSE_APPLICATION = 4;
    public static final int NIX_RECONNECT = 5;
    public static final int NIX_TD = 6;
    public static final int NIX_CLOSE_PORT = 7;


    public static final int WITHOUT_LOOP_IFR_RESPONDED_STATE = 5024;

   // public static final int NIX_NEW_CONNECTION = 1;
    public static final int IFR_IGNORED =1 ;
    public static final long EFR_WORK_DONE =1 ;
    public static final int LOOP_PROVIDER_CLIENT = 2;
    public static final int ENTITY_TYPE = 902;
    public static final int STATUS_APPLIED = 1 ;

    public static final int STATUS_ACTIVE = 1;
    public static final int STATUS_TD = 2 ;
    public static final int STATUS_CLOSED = 3;

    public static final int CONNECTION_TYPE_REGULAR = 1;

    public static final long TD_DONE =1 ;
    public static final long TD_AN = 2;
    public static final long RECONNECT_AN = 3;
    public static final long RECONNECT_DONE = 4;

    public static final int RECONNECT_DEMAND_NOTE = 86;
    public static final long RECONNECT_SUBMIT_STATE = 85;
    public static final long TD_SUBMIT_STATE = 82;
    public static final int TD_AN_GENERATED = 83;
    public static final int TD_COMPLETED = 84;
    public static final int RECONNECT_AN_GENERATED = 89;
    public static final int COMPLETE_RECONNECTION = 90;

    //common charge config
    public static final int RECONNECT_CONFIG = 1;
    public static final int VAT_CONFIG = 2;
    public static final Integer EFR_QUOTATION_PENDING = 0;
    public static final Integer EFR_QUOTATION_GIVEN = 1;
    public static final Integer EFR_QUOTATION_EXPIRED = 2;
    public static final Integer EFR_QUOTATION_IGNORED = 3;

    public static Map<Integer, String> nixapplicationTypeNameMap = new HashMap() {{
        put(NEW_CONNECTION_APPLICATION, "NIX New Connection Application");
        put(NIX_UPGRADE_APPLICATION, "NIX Upgrade Application");
        put(NIX_DOWNGRADE_APPLICATION, "NIX Downgrade Application");
        put(NIX_CLOSE_APPLICATION, "NIX close Application");
        put(NIX_RECONNECT, "NIX reconnect Application");
        put(NIX_TD, "NIX TD Application");

    }};
    public static Map<Integer, String> nixapplicationTypeHyphenSeperatedMap = new HashMap() {{
        put(NEW_CONNECTION_APPLICATION, "new-connection");
        put(NIX_UPGRADE_APPLICATION, "upgrade-port"); // REVISE
        put(NIX_DOWNGRADE_APPLICATION, "downgrade-port"); // REVISE
        put(NIX_CLOSE_APPLICATION, "close-port"); // REVISE
        put(NIX_RECONNECT, "reconnect"); // REVISE
        put(NIX_TD, "td"); // REVISE

    }};


    public enum IPUsageType {MANDATORY, ADDITIONAL};
    @SuppressWarnings({ "rawtypes", "unchecked", "serial" })
    public static Map<Integer, String> applicationTypeHyphenSeperatedMap = new HashMap() {{
        put(NEW_CONNECTION_APPLICATION, "new-connection");
        put(NIX_DOWNGRADE_APPLICATION, "upgrade-port"); // REVISE
        put(NIX_DOWNGRADE_APPLICATION, "downgrade-port"); // REVISE
        put(NIX_CLOSE_APPLICATION, "nix-close"); // REVISE
    }};

    public static Map<Integer, String> nixConnectionStatus = new HashMap() {{
        put(STATUS_ACTIVE, "Active");
        put(STATUS_TD, "TD"); // REVISE
        put(STATUS_CLOSED, "Close"); // REVISE
    }};
}
