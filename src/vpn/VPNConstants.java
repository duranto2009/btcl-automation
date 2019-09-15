package vpn;

import application.ApplicationState;
import exception.NoDataFoundException;
import entity.utility.FrontEndHelperBean;
import entity.utility.URLHelperBean;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class VPNConstants {
    public final static String VPN_BASE_URL = "vpn";
    public final static String VPN_DETAILS_PAGE_URL = "/vpn/link/details.do?id=";
    public final static String VPN_SESSION_URL = ".url";

    public final static int LOOP_BTCL = 1;
    public final static int LOOP_CLIENT = 2;
    public final static int VENDOR_ROLE = 16020;
    public static final String VPN_CLOSE_APPLICATION = "close/application";


    //    link
    final static String LINK_ADD = "link/add";
    final static String LINK_SEARCH = "link/search";
    final static String LINK_DETAILS = "link/details";
    final static String NETWORK_DETAILS = "network/details";

    final static String RECONNECT_APPLICATION = "reconnect/application";
    final static String OWNER_CHANGE_APPLICATION ="ownerchange/application" ;


    final static String BANDWIDTH_CHANGE = "network/bandwidth-change";
    final static String SHIFT = "link/shift";


    final static String NETWORK_SEARCH = "network/search";
    final static String TD_SEARCH = "td/search";

    //region td state
    public static final int TD_STATE = 0 ;
    public static final int TD_SUBMITTED = 2 ;
    //endregion

//    public final static String NETWORK_SEARCH = "link/search";



    public static String URL = "url";
    public static String subMenu1= "subMenu1";
    public static String subMenu2 = "subMenu2";


    //url map
    public static Map<String, URLHelperBean> urlMap = new HashMap<>();
    static {
        urlMap.put(LINK_ADD, URLHelperBean.builder()
                        .url("../vpn/link/add.jsp")
                        .subMenu1("vpnLink")
                        .subMenu2("addVpnLink").build()
//            //.urlHelperMap.urlMap.put("subMenu3","addVpnLink")

        );

        urlMap.put(LINK_SEARCH, URLHelperBean.builder()

                        .url("../vpn/link/search.jsp")
                        .subMenu1("vpnLink")
                        .subMenu2("searchVpnLink").build()
//            //.urlHelperMap.urlMap.put("subMenu3","addVpnLink")

        );

        urlMap.put(LINK_DETAILS, URLHelperBean.builder()

                        .url("../vpn/link/details.jsp")
                        .subMenu1("vpnLink").build()
//            urlMap.put(subMenu2,"searchVpnLink");.build()

        );

        urlMap.put(NETWORK_SEARCH, URLHelperBean.builder()

                        .url("../vpn/network/search.jsp")
                        .subMenu1("vpnLink")
                        .subMenu2("searchVpnNetworkLink").build()
//            //.urlHelperMap.urlMap.put("subMenu3","addVpnLink")

        );

        urlMap.put(NETWORK_DETAILS, URLHelperBean.builder()

                        .url("../vpn/network/details.jsp")
                        .subMenu1("vpnLink").build()
//            urlMap.put(subMenu2,"searchVpnLink");.build()
//            //.urlHelperMap.urlMap.put("subMenu3","addVpnLink")

        );

        urlMap.put(TD_SEARCH, URLHelperBean.builder()

                        .url("../vpn/td/search.jsp")
                        .subMenu1("vpnLink")
                        .subMenu2("searchVpnLink").build()
//            //.urlHelperMap.urlMap.put("subMenu3","addVpnLink")

        );

        urlMap.put(RECONNECT_APPLICATION, URLHelperBean.builder()

                        .url("../vpn/reconnect/vpn-reconnect.jsp")
                        .subMenu1("vpnLink")
                        .subMenu2("addVpnLink").build()
//            //.urlHelperMap.urlMap.put("subMenu3","addVpnLink")

        );
        urlMap.put(BANDWIDTH_CHANGE, URLHelperBean.builder()

                        .url("../vpn/network/vpn-bandwidth-change.jsp")
                        .subMenu1("vpnLink")
                        .subMenu2("updateBandwidth").build()
                //.urlHelperMap.urlMap.put("subMenu3","addVpnLink")

        );
        urlMap.put(SHIFT, URLHelperBean.builder()

                        .url("../vpn/link/vpn-shift.jsp")
                        .subMenu1("vpnLink")
                        .subMenu2("shiftVpnLink").build()
                //.urlHelperMap.urlMap.put("subMenu3","addVpnLink")

        );

        urlMap.put(OWNER_CHANGE_APPLICATION, URLHelperBean.builder()

                        .url("../vpn/ownerchange/vpn-ownerchange.jsp")
                        .subMenu1("vpnLink")
                        .subMenu2("VpnLinkOwnerChange").build()
//            .urlHelperMap.urlMap.put("subMenu3","addVpnLink")

        );

        urlMap.put(VPN_CLOSE_APPLICATION, URLHelperBean.builder()

                        .url("../vpn/close/vpn-close.jsp")
                        .subMenu1("vpnLink")
                        .subMenu2("VpnLinkClose").build()
//            .urlHelperMap.urlMap.put("subMenu3","addVpnLink")

        );



    }

    private static FrontEndHelperBean applicationUpdate = FrontEndHelperBean.builder()
            .url("application-update")
            .build();
    private static FrontEndHelperBean ifrResponse = FrontEndHelperBean.builder()
            .url("application-update")
            .modal("#modal-pop-selection")
            .view("IFR_DETAILS_VIEW")
            .param("feasibility")
            .build();


    private static FrontEndHelperBean withoutLoopIFRResponse = FrontEndHelperBean.builder()
            .url("application-update")
            .modal("#modal-pop-selection")
            .view("IFR_DETAILS_VIEW")
            .param("feasibility")
            .build();
    private static FrontEndHelperBean adviceNoteGenerate = FrontEndHelperBean.builder()
            .url("advice-note-generate")
            .modal("#forwardAdviceNote")
            .view("ADVICE_NOTE")
            .build();


    private static FrontEndHelperBean loopDistanceApproval = FrontEndHelperBean.builder()
            .url("loop-distance-approval")
            .build();
    //state map
    public static Map<Integer, FrontEndHelperBean> stateMap = new HashMap<>();

    static {

        //region VPNApplication
        stateMap.put(ApplicationState.VPN_SUBMITTED.getState(), FrontEndHelperBean.builder().build());

        stateMap.put(ApplicationState.VPN_WITHOUT_LOOP_SUBMITTED.getState(), FrontEndHelperBean.builder().build());

        stateMap.put(ApplicationState.VPN_PRECHECK_DONE.getState(), applicationUpdate);

        stateMap.put(ApplicationState.VPN_WITHOUT_LOOP_CLIENT_CORRECTION_SUBMITTED.getState(), applicationUpdate);

        stateMap.put(ApplicationState.VPN_REJECTED.getState(), applicationUpdate);

        stateMap.put(ApplicationState.VPN_WITHOUT_LOOP_REJECTED.getState(), applicationUpdate);

        stateMap.put(ApplicationState.VPN_APPLICATION_REOPEN.getState(), applicationUpdate);

        stateMap.put(ApplicationState.VPN_WITHOUT_LOOP_APPLICATION_REOPEN.getState(), applicationUpdate);

        stateMap.put(ApplicationState.VPN_IFR_RESPONDED.getState(), ifrResponse);

        stateMap.put(ApplicationState.VPN_WITHOUT_LOOP_IFR_RESPONSE.getState(), ifrResponse);

        stateMap.put(ApplicationState.VPN_REQUESTED_TO_CENTRAL.getState(), applicationUpdate);

        stateMap.put(ApplicationState.VPN_SELECT_EXTERNAL_FR.getState(),
                FrontEndHelperBean.builder()
                        .url("efr-select")
                        .build()
        );

        stateMap.put(ApplicationState.VPN_GIVE_WORK_ORDER.getState(), FrontEndHelperBean.builder()
                .url("work-order-generate")
                .view("WORK_ORDER")
                .build()
        );

        stateMap.put(ApplicationState.VPN_JOB_DONE.getState(), FrontEndHelperBean.builder()

                .url("work-done")
                .modal("#modal-vendor-response")
                .view("efr-details-info")
                .param("complete-work-order")
                .build()
        );

        stateMap.put(ApplicationState.VPN_FORWARD_FOR_WORK_ORDER.getState(),
                FrontEndHelperBean.builder()
                        .url("application-wo-forward")
                        .modal("#efrRequestToLocalDGMModal")
                        .build()
        );

        stateMap.put(ApplicationState.VPN_EFR_WIP.getState(),
                FrontEndHelperBean.builder()
                        .url("efr-response")
                        .modal("#modal-vendor-response")
                        .view("view-vendor-selection")
                        .param("efr-response")
                        .build()
        );

        stateMap.put(ApplicationState.VPN_EFR_DONE.getState(), FrontEndHelperBean.builder()
                .url("efr-request")
                .modal("#modal-vendor-selection")
                .build()
        );
        stateMap.put(ApplicationState.VPN_APPROVE_AND_FORWARD.getState(), loopDistanceApproval);

        stateMap.put(ApplicationState.VPN_WORK_ORDER_APPROVE.getState(), loopDistanceApproval);

        stateMap.put(ApplicationState.VPN_WORK_DONE.getState(),
                FrontEndHelperBean.builder()
                        .url("work-done")
                        .modal("#modal-vendor-response")
                        .view("efr-details-info")
                        .param("complete-work-order")
                        .build()
        );

        stateMap.put(ApplicationState.VPN_APPROVE_SR_COLLABORATION.getState(),
                FrontEndHelperBean.builder()
                        .url("vendor-work-approve")
                        .view("view-loop-distance-approve")
                        .build()
        );

        stateMap.put(ApplicationState.VPN_APPROVE_FORWARDED_SR_COLLABORATION.getState(),
                FrontEndHelperBean.builder()
                        .url("vendor-work-approve")
                        .view("view-loop-distance-approve")
                        .build()
        );

        stateMap.put(ApplicationState.VPN_REJECT_SR_COLLABORATION.getState(),
                FrontEndHelperBean.builder()
                        .url("vendor-work-reject")
                        .build()
        );

        stateMap.put(ApplicationState.VPN_REJECT_FORWARDED_SR_COLLABORATION.getState(),
                FrontEndHelperBean.builder()
                        .url("vendor-work-reject")
                        .build()
        );

        stateMap.put(ApplicationState.VPN_REJECT_LOOP_DISTANCE.getState(),
                FrontEndHelperBean.builder()
                        .url("vendor-loop-reject")
                        .build()
        );

        stateMap.put(ApplicationState.VPN_APPLY_LOOP_DISTANCE_APPROVAL.getState(),
                FrontEndHelperBean.builder()
                        .url("work-done")
                        .modal("#modal-vendor-response")
                        .view("view-loop-distance-approve")
                        .param("complete-work-order")
                        .build()
        );

        stateMap.put(ApplicationState.VPN_LDGM_REJECT_LOOP_DISTANCE.getState(),
                FrontEndHelperBean.builder()
                        .url("vendor-loop-reject")
                        .build()
        );

        stateMap.put(ApplicationState.VPN_FORWARD_APPLY_LOOP_DISTANCE_APPROVAL.getState(),
                FrontEndHelperBean.builder()
                        .url("work-done")
                        .modal("#modal-vendor-response")
                        .view("view-loop-distance-approve")
                        .param("complete-work-order")
                        .build()
        );


        stateMap.put(ApplicationState.VPN_IFR_SUBMITTED.getState(), FrontEndHelperBean.builder()
                .url("application-update")
                .modal("#modal-pop-selection")
                .view("ifr-request-show")
                .build()
        );

        stateMap.put(ApplicationState.VPN_WITHOUT_LOOP_IFR_REQUEST.getState(), FrontEndHelperBean.builder()
                .url("application-update")
                .modal("#modal-pop-selection")
                .view("ifr-request-show")
                .build()
        );

        stateMap.put(ApplicationState.VPN_EFR_REQUESTED.getState(), FrontEndHelperBean.builder()
                .url("efr-request")
                .modal("#modal-vendor-selection")
                .build()
        );

        stateMap.put(ApplicationState.VPN_REQUEST_FOR_ACCOUNT_CC.getState(), applicationUpdate);

        stateMap.put(ApplicationState.VPN_WITHOUT_LOOP_REQUEST_FOR_ACCOUNT_CC.getState(), applicationUpdate);

        stateMap.put(ApplicationState.VPN_REQUESTED_CLIENT_FOR_CORRECTION.getState(), applicationUpdate);

        stateMap.put(ApplicationState.VPN_WITHOUT_LOOP_REQUESTED_CLIENT_FOR_CORRECTION.getState(), applicationUpdate);

        stateMap.put(ApplicationState.VPN_ACCOUNT_CC_POSITIVE.getState(), applicationUpdate);
        stateMap.put(ApplicationState.VPN_WITHOUT_LOOP_ACCOUNT_CC_POSITIVE.getState(), applicationUpdate);

        stateMap.put(ApplicationState.VPN_ACCOUNT_CC_NEGETIVE.getState(), applicationUpdate);
        stateMap.put(ApplicationState.VPN_WITHOUT_LOOP_ACCOUNT_CC_NEGETIVE.getState(), applicationUpdate);

        stateMap.put(ApplicationState.VPN_GENERATE_WORK_ORDER.getState(), FrontEndHelperBean.builder()
                .url("work-order-generate")
                .build()
        );

        stateMap.put(ApplicationState.VPN_TESTING_DONE.getState(), FrontEndHelperBean.builder()
                .url("application-update")
                .modal("#serverRoomTestingComplete")
                .param("complete-testing")
                .build()
        );

        stateMap.put(ApplicationState.VPN_WITHOUT_LOOP_TESTING_COMPLETE.getState(), FrontEndHelperBean.builder()
                .url("application-update")
                .modal("#serverRoomTestingComplete")
                .param("complete-testing")
                .build()
        );

        stateMap.put(ApplicationState.VPN_CONNECTION_COMPLETE.getState(), FrontEndHelperBean.builder()
                .url("connection-complete")
                .build()

        );
        stateMap.put(ApplicationState.VPN_WITHOUT_LOOP_CONNECTION_COMPLETE.getState(), FrontEndHelperBean.builder()
                .url("connection-complete")
                .build()
        );

        stateMap.put(ApplicationState.VPN_APPLICATION_REOPEN.getState(), applicationUpdate);

        stateMap.put(ApplicationState.VPN_FORWARD_TO_MUX_TEAM.getState(), applicationUpdate);

        stateMap.put(ApplicationState.VPN_MUX_CONFIGURE_DONE.getState(), applicationUpdate);

        stateMap.put(ApplicationState.VPN_DEMAND_NOTE_GENERATED.getState(), FrontEndHelperBean.builder()
                .url("check-if-skip")
                .view("DEMAND_NOTE")
                .redirect("vpn/dn/preview.do")
                .param("id_nextstate_appGroup_global") //set fron vpn-link-detail.js
                .build()
        );

        stateMap.put(ApplicationState.VPN_WITHOUT_LOOP_DEMAND_NOTE_GENERATED.getState(), FrontEndHelperBean.builder()
                .url("check-if-skip")
                .view("DEMAND_NOTE")
                .redirect("vpn/dn/preview.do")
                .param("id_nextstate_appGroup_global")
                .build()
        );

        stateMap.put(ApplicationState.VPN_DEMAND_NOTE_SKIP.getState(), FrontEndHelperBean.builder()
                .url("check-if-skip")
                .view("DEMAND_NOTE")
                .redirect("vpn/dn/preview.do")
                .param("id_nextstate_appGroup_global")
                .build()
        );

        stateMap.put(ApplicationState.VPN_WITHOUT_LOOP_DEMAND_NOTE_SKIP.getState(), FrontEndHelperBean.builder()
                .url("check-if-skip")
                .modal("")
                .view("DEMAND_NOTE")
                .redirect("vpn/dn/preview.do")
                .param("id_nextstate_appGroup_global").build()
        );

        stateMap.put(ApplicationState.VPN_PAYMENT_DONE.getState(), FrontEndHelperBean.builder()
                .url("no-state-change")
                .modal("")
                .view("")
                .redirect("MultipleBillPaymentView.do")
                .param("billIDs").build()
        );

        stateMap.put(ApplicationState.VPN_WITHOUT_LOOP_PAYMENT_DONE.getState(), FrontEndHelperBean.builder()
                .url("no-state-change")
                .modal("")
                .view("")
                .redirect("MultipleBillPaymentView.do")
                .param("billIDs").build()
        );

        stateMap.put(ApplicationState.VPN_PAYMENT_VERIFIED.getState(), FrontEndHelperBean.builder()
                .url("no-state-change")
                .modal("")
                .view("")
                .redirect("common/payment/linkPayment.jsp")
                .param("paymentID_moduleID").build()
        );

        stateMap.put(ApplicationState.VPN_WITHOUT_LOOP_PAYMENT_VERIFIED.getState(), FrontEndHelperBean.builder()
                .url("no-state-change")
                .modal("")
                .view("")
                .redirect("common/payment/linkPayment.jsp")
                .param("paymentID_moduleID").build()
        );

        stateMap.put(ApplicationState.VPN_ADVICE_NOTE_PUBLISH.getState(), adviceNoteGenerate);

        stateMap.put(ApplicationState.VPN_WITHOUT_LOOP_ADVICE_NOTE_PUBLISH.getState(), adviceNoteGenerate);

        stateMap.put(ApplicationState.VPN_FORWARD_LDGM_FOR_LOOP.getState(), FrontEndHelperBean.builder()
                .url("application-forward")
                .modal("#efrRequestToLocalDGMModalSelectZone")
                .view("")
                .redirect("")
                .param("").build()
        );

        stateMap.put(ApplicationState.VPN_FORWARD_LDGM_EFR_REQUEST_FOR_LOOP.getState(), FrontEndHelperBean.builder()
                .url("efr-request")
                .modal("#modal-vendor-selection")
                .view("")
                .redirect("")
                .param("").build()
        );

        stateMap.put(ApplicationState.VPN_FORWARD_LDGM_RESPONSE_EXTERNAL_FR.getState(), FrontEndHelperBean.builder()
                .url("efr-response")
                .modal("#modal-vendor-response")
                .view("view-vendor-selection")
                .redirect("")
                .param("efr-response").build()
        );

        stateMap.put(ApplicationState.VPN_FORWARD_LDGM_SELECT_EXTERNAL_FR.getState(), FrontEndHelperBean.builder()
                .url("efr-select")
                .modal("")
                .view("")
                .redirect("")
                .param("").build()
        );
//endregion

        //region td
        stateMap.put(ApplicationState.VPN_TD_APPLICATION_SUBMITTED.getState(), FrontEndHelperBean.builder()
                .url("")
                .modal("")
                .view("")
                .redirect("")
                .param("").build()
        );

        stateMap.put(ApplicationState.VPN_TD_APPLICATION_REJECTED.getState(), applicationUpdate);

        stateMap.put(ApplicationState.VPN_TD_APPLICATION_ADVICE_NOTE_PUBLISH.getState(), adviceNoteGenerate);

        stateMap.put(ApplicationState.VPN_TD_APPLICATION_CONNECTION_COMPLETE.getState(), FrontEndHelperBean.builder()
                .url("td-complete")
                .modal("")
                .view("")
                .redirect("")
                .param("").build()
        );
        //endregion

        //region reconnect
        stateMap.put(ApplicationState.VPN_RECONNECT_APPLICATION_SUBMITTED.getState(), FrontEndHelperBean.builder().build());

        stateMap.put(ApplicationState.VPN_RECONNECT_APPLICATION_REJECTED.getState(), applicationUpdate);

        stateMap.put(ApplicationState.VPN_RECONNECT_APPLICATION_DEMAND_NOTE_GENERATED.getState(), FrontEndHelperBean.builder()
                .url("no-state-change")
                .modal("")
                .view("DEMAND_NOTE")
                .redirect("vpn/dn/preview.do")
                .param("id_nextstate_appGroup_global").build()
        );

        stateMap.put(ApplicationState.VPN_RECONNECT_APPLICATION_PAYMENT_DONE.getState(), FrontEndHelperBean.builder()
                .url("no-state-change")
                .modal("")
                .view("")
                .redirect("MultipleBillPaymentView.do")
                .param("billIDs").build()
        );
        stateMap.put(ApplicationState.VPN_RECONNECT_APPLICATION_PAYMENT_VERIFIED.getState(), FrontEndHelperBean.builder()
                .url("no-state-change")
                .modal("")
                .view("")
                .redirect("common/payment/linkPayment.jsp")
                .param("paymentID_moduleID").build()
        );

        stateMap.put(ApplicationState.VPN_RECONNECT_APPLICATION_ADVICE_NOTE_PUBLISH.getState(), adviceNoteGenerate);

        stateMap.put(ApplicationState.VPN_RECONNECT_APPLICATION_CONNECTION_COMPLETE.getState(), FrontEndHelperBean.builder()
                .url("reconnect-complete")
                .modal("")
                .view("")
                .redirect("")
                .param("").build()
        );

        //endregion

        //region Owner change
        stateMap.put(ApplicationState.VPN_OC_APPLICATION_SUBMITTED.getState(), FrontEndHelperBean.builder().build());

        stateMap.put(ApplicationState.VPN_OC_APPLICATION_REJECTED.getState(), applicationUpdate);

        stateMap.put(ApplicationState.VPN_OC_APPLICATION_REOPENED.getState(), applicationUpdate);

        stateMap.put(ApplicationState.VPN_OC_REQUEST_FOR_CC.getState(), applicationUpdate);
        stateMap.put(ApplicationState.VPN_OC_AC_CC_POSITIVE.getState(), applicationUpdate);
        stateMap.put(ApplicationState.VPN_OC_AC_CC_NEGETIVE.getState(), applicationUpdate);

        stateMap.put(ApplicationState.VPN_OC_CORRECTION.getState(), applicationUpdate);

        stateMap.put(ApplicationState.VPN_OC_CORRECTION_DONE.getState(), applicationUpdate);
        stateMap.put(ApplicationState.VPN_OC_FORWARD_TO_DST.getState(), applicationUpdate);
        stateMap.put(ApplicationState.VPN_OC_DST_POSITIVE.getState(), applicationUpdate);
        stateMap.put(ApplicationState.VPN_OC_DST_NEGETIVE.getState(), applicationUpdate);

        stateMap.put(ApplicationState.VPN_OC_FORWARD_TO_SRC.getState(), applicationUpdate);

        stateMap.put(ApplicationState.VPN_OC_APP_RECHECK.getState(), applicationUpdate);

        stateMap.put(ApplicationState.VPN_OC_DN_GENERATED.getState(), FrontEndHelperBean.builder()
                .url("no-state-change")
                .modal("")
                .view("DEMAND_NOTE")
                .redirect("vpn/dn/preview.do")
                .param("id_nextstate_appGroup_global").build()
        );

        stateMap.put(ApplicationState.VPN_OC_PAYMENT_DONE.getState(), FrontEndHelperBean.builder()
                .url("no-state-change")
                .modal("")
                .view("")
                .redirect("MultipleBillPaymentView.do")
                .param("billIDs").build()
        );
        stateMap.put(ApplicationState.VPN_OC_PAYMENT_VERIFIED.getState(), FrontEndHelperBean.builder()
                .url("no-state-change")
                .modal("")
                .view("")
                .redirect("common/payment/linkPayment.jsp")
                .param("paymentID_moduleID").build()
        );

        stateMap.put(ApplicationState.VPN_OC_ADVICE_NOTE_PUBLISH.getState(), adviceNoteGenerate);

        stateMap.put(ApplicationState.VPN_OC_COMPLETE.getState(), FrontEndHelperBean.builder()
                .url("ownerchange-complete")
                .build()
        );
        //endregion

        //region bw modify
        stateMap.put(ApplicationState.VPN_DOWNGRADE_APPLICATION_SUBMITTED.getState(), applicationUpdate);

        stateMap.put(ApplicationState.VPN_DOWNGRADE_APPLICATION_REQUEST_CLIENT_FOR_CORRECTION.getState(), applicationUpdate);

            stateMap.put(ApplicationState.VPN_DOWNGRADE_APPLICATION_REQUEST_ACCOUNT_FOR_CC.getState(), applicationUpdate);
            stateMap.put(ApplicationState.VPN_DOWNGRADE_APPLICATION_REJECTED.getState(), applicationUpdate);
            stateMap.put(ApplicationState.VPN_DOWNGRADE_APPLICATION_ADVICE_NOTE_PUBLISH.getState(), adviceNoteGenerate);
//            stateMap.put(ApplicationState.VPN_DOWNGRADE_APPLICATION_CLIENT_CORRECTION_SUBMITTED.getState(), applicationUpdate);
            stateMap.put(ApplicationState.VPN_DOWNGRADE_APPLICATION_CLIENT_CORRECTION_SUBMITTED.getState(), FrontEndHelperBean.builder()
                    .url("bandwidth-change")
                    .modal("#modal-bandwidth-change")
                    .build());
            stateMap.put(ApplicationState.VPN_DOWNGRADE_APPLICATION_REJECTED.getState(), applicationUpdate);
            stateMap.put(ApplicationState.VPN_DOWNGRADE_APPLICATION_ACCOUNT_CC_POSITIVE.getState(), applicationUpdate);
            stateMap.put(ApplicationState.VPN_DOWNGRADE_APPLICATION_ACCOUNT_CC_NEGATIVE.getState(), applicationUpdate);
            stateMap.put(ApplicationState.VPN_DOWNGRADE_APPLICATION_APPLICATION_REOPEN.getState(), applicationUpdate);
            stateMap.put(ApplicationState.VPN_DOWNGRADE_APPLICATION_TESTING_DONE.getState(), FrontEndHelperBean.builder()
                    .url("connection-complete")
                    .build());

        //endregion

        //region close Application flow1
        stateMap.put(ApplicationState.VPN_CLOSE_APPLICATION_SUBMITTED.getState(), applicationUpdate);

        stateMap.put(ApplicationState.CLOSE_APPLICATION_REJCTED.getState(), applicationUpdate);
        stateMap.put(ApplicationState.CLOSE_APPLICATION_REOPENED.getState(), applicationUpdate);
        stateMap.put(ApplicationState.CLOSE_CHECK_CC.getState(), applicationUpdate);
        stateMap.put(ApplicationState.CLOSE_CC_POSITIVE.getState(), applicationUpdate);
        stateMap.put(ApplicationState.CLOSE_CC_NEGETIVE.getState(), applicationUpdate);
        stateMap.put(ApplicationState.CLOSE_RQST_CORRECTION.getState(),applicationUpdate);

        stateMap.put(ApplicationState.CLOSE_RQST_CORRECTION.getState(), applicationUpdate);
        stateMap.put(ApplicationState.CLOSE_CORRECTION_DONE.getState(), applicationUpdate);
        stateMap.put(ApplicationState.CLOSE_ADVICE_NOTE.getState(),  FrontEndHelperBean.builder()
                .url("advice-note-generate")
                .modal("#forwardAdviceNote")
                .view("ADVICE_NOTE")
                .build());
        stateMap.put(ApplicationState.CLOSE_COMPLETE.getState(), FrontEndHelperBean.builder()
                .url("close-complete")
                .build());

        //flow2
        stateMap.put(ApplicationState.CLOSE_WITH_LOOP_SUBMITTED.getState(), applicationUpdate);

        stateMap.put(ApplicationState.CLOSE_WITH_LOOP_REJCTED.getState(), applicationUpdate);
        stateMap.put(ApplicationState.CLOSE_WITH_LOOP_REOPENED.getState(), applicationUpdate);
        stateMap.put(ApplicationState.CLOSE_WITH_LOOP_CHECK_CC.getState(), applicationUpdate);
        stateMap.put(ApplicationState.CLOSE_WITH_LOOP_CC_POSITIVE.getState(), applicationUpdate);
        stateMap.put(ApplicationState.CLOSE_WITH_LOOP_CC_NEGETIVE.getState(), applicationUpdate);
        stateMap.put(ApplicationState.CLOSE_WITH_LOOP_RQST_CORRECTION.getState(),applicationUpdate);

        stateMap.put(ApplicationState.CLOSE_WITH_LOOP_RQST_CORRECTION.getState(), applicationUpdate);
        stateMap.put(ApplicationState.CLOSE_WITH_LOOP_CORRECTION_DONE.getState(), applicationUpdate);
        stateMap.put(ApplicationState.CLOSE_FORWARD_TO_LDGM.getState(),  FrontEndHelperBean.builder()
                .url("close-wo-forward")
                .modal("#efrRequestToLocalDGMModal")
                .build());
        // TODO: 3/11/2019  For Work order need to show the loop details
        stateMap.put(ApplicationState.CLOSE_WO.getState(), FrontEndHelperBean.builder()
                .url("work-order-generate")
                .view("WORK_ORDER")
                .build());
        stateMap.put(ApplicationState.CLOSE_WO_DONE.getState(), FrontEndHelperBean.builder()

                .url("work-done")
//                .modal("#modal-vendor-response")
                .modal("")
                .view("")
                .param("complete-work-order")
                .build());
        stateMap.put(ApplicationState.CLOSE_WO_BY_LDGM.getState(), FrontEndHelperBean.builder()
                .url("work-order-generate")
                .view("WORK_ORDER")
                .build());
        stateMap.put(ApplicationState.CLOSE_WO_RES_TO_LDGM.getState(), FrontEndHelperBean.builder()

                .url("work-done")
                .modal("")
                .view("")
                .param("complete-work-order")
                .build());
        //todo end

        stateMap.put(ApplicationState.CLOSE_FORWARD_TO_CDGM.getState(), FrontEndHelperBean.builder()
                .url("close-wo-approval")
                .modal("")
                .view("")
                //.param("complete-work-order")
                .build());
        stateMap.put(ApplicationState.SERVER_ROOM_ACCEPT.getState(),FrontEndHelperBean.builder()
                .url("vendor-work-approve")
                .view("")
                .build());
        stateMap.put(ApplicationState.SERVER_ROOM_REJECT.getState(), FrontEndHelperBean.builder()
                .url("vendor-work-reject")
                .build());
        stateMap.put(ApplicationState.SERVER_ROOM_REJECT_FLOW.getState(), FrontEndHelperBean.builder()
                .url("vendor-work-reject")
                .build());
        stateMap.put(ApplicationState.SERVER_ROOM_ACCEPT_FLOW.getState(), FrontEndHelperBean.builder()
                .url("vendor-work-approve")
                .view("view-loop-distance-approve")
                .build());

        //endregion
    }


    public static final int ENTITY_TYPE = 602;

    public enum Status {
        VPN_ACTIVE("ACTIVE"),
        VPN_TD("TD"),
        VPN_CLOSE("CLOSED"),
        VPN_RECONNECT("RECONNECTED");

        private String statusName;

        Status(String name) {
            statusName = name;
        }

        public String getStatus() {
            return this.statusName;
        }

        public static Status getStatusByName(String name) {
            return Arrays.stream(Status.values())
                    .filter(t -> t.getStatus().equals(name))
                    .findFirst()
                    .orElseThrow(() -> new NoDataFoundException("No Status Found"));

        }
    }

    public enum INCIDENT {
        NEW("New Connection"),
        UPGRADE("Upgrade Link"),
        DOWNGRADE("Downgrade Link"),
        CLOSE("Link Closed"),
        TD("Temporary Disconnected"),
        OWNER_CHANGE("Owner Changed"),
        RECONNECT("Reconnected"),
        SHIFT("Link Shifted");

        private String incidentName;

        INCIDENT(String name) {
            incidentName = name;
        }

        public String getIncidentName() {
            return this.incidentName;
        }
    }

    public enum LINK_TYPE{
        LAYER_2("LAYER_2"),
        LAYER_3("LAYER_3");

        private String type_name;

        LINK_TYPE(String name){
            type_name = name;
        }

        public String getLinkTypeName(){
            return this.type_name;
        }

    }

    public enum END_TYPE {
        LOCAL_END,
        REMOTE_END,
    }



}
