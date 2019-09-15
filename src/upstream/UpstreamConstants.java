package upstream;

import application.ApplicationState;
import entity.utility.FrontEndHelperBean;
import entity.utility.URLHelperBean;
import exception.NoDataFoundException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class UpstreamConstants {
    public final static String UPSTREAM_BASE_URL = "upstream";
    public final static String UPSTREAM_SESSION_URL = ".url";

    public final static int VENDOR_ROLE = 16020;

    public static final String UPSTREAM_CLOSE_APPLICATION = "close/application";


    //    link
    final static String NEW_REQUEST = "/new-request";
    final static String CONTRACT_EXTENSION = "/upstream-contract-extension-application";
    final static String CONTRACT_CLOSE= "/contract-close";
    final static String CONTRACT_BANDWIDTH_CHANGE = "/bandwidth-change";
    final static String REQUEST_DETAILS = "/request-details";
    final static String REQUEST_SEARCH = "/request-search";
    final static String CONTRACT_SEARCH = "/contract-search";
    final static String INVOICE_SEARCH = "/invoice-search";
    final static String CONTRACT_DETAILS = "/contract-details";
    final static String LINK_SEARCH = "link/search";

    final static String LINK_DETAILS = "link/details";

    final static String NETWORK_DETAILS = "network/details";
    final static String NETWORK_SEARCH = "network/search";


    public static String URL = "url";
    public static String subMenu1 = "subMenu1";
    public static String subMenu2 = "subMenu2";


    //url map
    public static Map<String, URLHelperBean> urlMap = new HashMap<>();

    static {
        urlMap.put(NEW_REQUEST, URLHelperBean.builder()
                        .url("../upstream/request/upstream-request-new.jsp")
//                        .subMenu1("upstream")
                        .subMenu2("newUpstreamRequest").build()
//            //.urlHelperMap.urlMap.put("subMenu3","addVpnLink")

        );

        urlMap.put(REQUEST_DETAILS, URLHelperBean.builder()
                        .url("../upstream/request/upstream-request-details.jsp")
//                        .subMenu1("upstream")
                        .subMenu2("searchUpstreamRequest").build()
//            //.urlHelperMap.urlMap.put("subMenu3","addVpnLink")

        );

        urlMap.put(REQUEST_SEARCH, URLHelperBean.builder()
                        .url("../upstream/request/upstream-request-search.jsp")
//                        .subMenu1("upstream")
                        .subMenu2("searchUpstreamRequest").build()
//            //.urlHelperMap.urlMap.put("subMenu3","addVpnLink")

        );
        urlMap.put(CONTRACT_SEARCH, URLHelperBean.builder()
                        .url("../upstream/contract/upstream-contract-search.jsp")
//                        .subMenu1("upstream")
                        .subMenu2("searchUpstreamContract").build()
//            //.urlHelperMap.urlMap.put("subMenu3","addVpnLink")

        );

        urlMap.put(INVOICE_SEARCH, URLHelperBean.builder()
                        .url("../upstream/invoice/upstream-invoice-search.jsp")
//                        .subMenu1("upstream")
                        .subMenu2("searchUpstreamInvoice").build()
//            //.urlHelperMap.urlMap.put("subMenu3","addVpnLink")

        );

        urlMap.put(CONTRACT_DETAILS, URLHelperBean.builder()
                        .url("../upstream/contract/upstream-contract-details.jsp")
//                        .subMenu1("upstream")
                        .subMenu2("searchUpstreamContract")
                        .build()
//            //.urlHelperMap.urlMap.put("subMenu3","addVpnLink")

        );

//        urlMap.put(LINK_SEARCH, URLHelperBean.builder()
//
//                        .url("../vpn/link/search.jsp")
//                        .subMenu1("vpnLink")
//                        .subMenu2("searchVpnLink").build()
////            //.urlHelperMap.urlMap.put("subMenu3","addVpnLink")
//
//        );

        //contract extension
        urlMap.put(CONTRACT_EXTENSION, URLHelperBean.builder()
                        .url("../upstream/extension/upstream-contract-extension-application.jsp")
//                        .subMenu1("upstream")
                        .subMenu2("contractExtension").build()
//            //.urlHelperMap.urlMap.put("subMenu3","addVpnLink")

        );
        //contract close
        urlMap.put(CONTRACT_CLOSE, URLHelperBean.builder()
                        .url("../upstream/close/upstream-contract-close-application.jsp")
//                        .subMenu1("upstream")
                        .subMenu2("contractClose").build()
//            //.urlHelperMap.urlMap.put("subMenu3","addVpnLink")

        );

        //contract bw change
        urlMap.put(CONTRACT_BANDWIDTH_CHANGE, URLHelperBean.builder()
                        .url("../upstream/bandwidth_change/upstream-contract-bandwidth-change-application.jsp")
//                        .subMenu1("upstream")
                        .subMenu2("contractBandwidthChange").build()
//            //.urlHelperMap.urlMap.put("subMenu3","addVpnLink")

        );

    }

    private static FrontEndHelperBean applicationUpdate = FrontEndHelperBean.builder()
            .url("application-update")
            .build();
    //state map
    public static Map<Integer, FrontEndHelperBean> stateMap = new HashMap<>();

    static {

        //region UPSTREAMApplication
        stateMap.put(ApplicationState.UPSTREAM_FORWARDED_TO_GM_DATA_AND_INTERNET.getState(), FrontEndHelperBean.builder().url("application-update").build());
        stateMap.put(ApplicationState.UPSTREAM_APPROVED_AND_FORWARDED_TO_CGM_OVERSEAS.getState(),
                FrontEndHelperBean.builder()
                        .url("application-update")
                        .build());
        stateMap.put(ApplicationState.UPSTREAM_APPROVED_AND_FORWARDED_TO_GM_INTERNATIONAL.getState(),
                FrontEndHelperBean.builder()
//                        .url("work-order-generate")

                        .view("upload-document-for-gm-international")
                        .url("application-update")
                        .build()
        );
        stateMap.put(ApplicationState.UPSTREAM_APPROVED_VENDOR_AND_FORWARDED_TO_DGM_CORE_UPSTREAM.getState(),
                FrontEndHelperBean.builder()
//                        .url("work-order-generate")
                        .view("circuit-info")
                        //.modal("#gm-international-request-modal")
                        .url("application-update")
                        .build()
        );
        stateMap.put(ApplicationState.UPSTREAM_CONNECTION_COMPLETE.getState(),
                FrontEndHelperBean.builder()
//                        .url("work-order-generate")
//                        .view("WORK_ORDER")
                        .url("connection-complete")
                        .build()
        );
        stateMap.put(ApplicationState.UPSTREAM_REQUESTED_DGM_CORE_UPSTREAM_FOR_CORRECTION.getState(),
                FrontEndHelperBean.builder()
                        .url("application-update")
                        .view("update-form-view")
                        .build());
        stateMap.put(ApplicationState.UPSTREAM_PRECHECK_DONE.getState(),
                FrontEndHelperBean.builder()
//                        .url("work-order-generate")
//                        .view("WORK_ORDER")
                        .url("application-update")
                        .build()
        );
        stateMap.put(ApplicationState.UPSTREAM_REJECTED.getState(),
                FrontEndHelperBean.builder()
                        .url("application-update")
                        .build());
        stateMap.put(ApplicationState.UPSTREAM_APPLICATION_REOPENED.getState(),
                FrontEndHelperBean.builder()
//                        .url("work-order-generate")
//                        .view("WORK_ORDER")
                        .url("application-update")
                        .build()
        );


        //endregion
    }


    public static final int ENTITY_TYPE = 602;

    public enum Status {
        UPSTREAM_ACTIVE("ACTIVE"),
        UPSTREAM_CLOSE("CLOSED"),
        UPSTREAM_RECONNECT("RECONNECTED");

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

    public enum APPLICATION_TYPE {
        NEW("NEW"),
        UPGRADE("UPGRADE"),
        DOWNGRADE("DOWNGRADE"),
        CLOSE("CLOSE"),
        CONTRACT_EXTENSION("CONTRACT_EXTENSION");

        private String incidentName;

        APPLICATION_TYPE(String name) {
            incidentName = name;
        }

        public String getIncidentName() {
            return this.incidentName;
        }
    }


    public enum INVENTORY_ITEM_TYPE {
        TYPE_OF_BW("TYPE_OF_BW"),
        MEDIA("MEDIA"),
        BTCL_SERVICE_LOCATION("BTCL_SERVICE_LOCATION"),
        PROVIDER_LOCATION("PROVIDER_LOCATION"),
        PROVIDER("PROVIDER");

        private String typeName;

        INVENTORY_ITEM_TYPE(String type) {
            typeName = type;
        }

        public String getInventoryItemName() {
            return this.typeName;
        }
    }


    public enum CIRCUIT_INFO_TYPE {
        LINK_INFO_OF_BSCCL("LINK_INFO_OF_BSCCL"),
        BTCLS_WORK_ORDER_TO_PROVIDER("BTCLS_WORK_ORDER_TO_PROVIDER"),
        LETTERS_OF_BTCL_BACKHAUL("LETTERS_OF_BTCL_BACKHAUL");

        private String typeName;

        CIRCUIT_INFO_TYPE(String type) {
            typeName = type;
        }

        public String getCircuitInfoType() {
            return this.typeName;
        }
    }

}
