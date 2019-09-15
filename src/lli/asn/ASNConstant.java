package lli.asn;

import application.ApplicationState;
import application.ApplicationType;
import entity.utility.FrontEndHelperBean;
import entity.utility.URLHelperBean;

import java.util.HashMap;
import java.util.Map;

public class ASNConstant {

    public final static String ASN_BASE_URL = "asn";
    public final static String ASN_SESSION_URL = ".url";
    public final static String ASN_APP_SEARCH = "asn/search";
    public final static String ASN_APP_DETAILS = "asn/details";
    public static final String ASN_APP_DETAILS_VIEW = "asn/details-view";

    public final static String ASN_SEARCH = "asn/search-asn";
    public static final String ASN_DETAILS = "asn/details-asn";
    public static final String ASN_EDIT = "asn/edit";

    public final static  int ON_PROCESS = 1;
    public final static  int IS_ACTIVE = 2 ;

    public final static String ASN_ADD = "add";

    public static String URL = "url";
    public static String subMenu1= "subMenu1";
    public static String subMenu2 = "subMenu2";

    public static Map<String, URLHelperBean> urlMap = new HashMap<>();
    static {
        urlMap.put(ASN_ADD, URLHelperBean.builder()
                        .url("../lli/asn/add.jsp")
                        .subMenu1("lli-asn")
                        .subMenu2("lli-asn-add").build()
        );

        urlMap.put(ASN_EDIT, URLHelperBean.builder()
                .url("../lli/asn/asn-edit.jsp")
                .subMenu1("lli-asn")
                .subMenu2("lli-asn-add").build()
        );
        urlMap.put(ASN_APP_SEARCH, URLHelperBean.builder()
                        .url("../lli/asn/search.jsp")
                        .subMenu1("lli-asn")
                        .subMenu2("lli-asn-app-search").build()
        );
        urlMap.put(ASN_APP_DETAILS, URLHelperBean.builder()
                        .url("../lli/asn/details.jsp")
                        .subMenu2("lli-asn-app-search").build()
        );
        urlMap.put(ASN_APP_DETAILS_VIEW, URLHelperBean.builder()
                .url("../lli/asn/details-view.jsp")
                .subMenu2("lli-asn-app-search").build()
        );
        urlMap.put(ASN_SEARCH, URLHelperBean.builder()
                .url("../lli/asn/search-asn.jsp")
                .subMenu1("lli-asn")
                .subMenu2("lli-asn-search").build()
        );
        urlMap.put(ASN_DETAILS, URLHelperBean.builder()
                .url("../lli/asn/details-asn.jsp")
                .subMenu2("details-asn").build()
        );
    }

    public static Map<Integer, FrontEndHelperBean> stateMap = new HashMap<>();
    static {
        //region ASNApplication
        stateMap.put(ApplicationState.ASN_SUBMITTED.getState(), FrontEndHelperBean.builder().build());
        stateMap.put(ApplicationState.REQUEST_REJECT.getState(), FrontEndHelperBean.builder()
                .url("request-reject")
                .build());
        stateMap.put(ApplicationState.REQUEST_ACCEPT.getState(), FrontEndHelperBean.builder()
                .url("request-accept")
                .build());
        //endregion
    }
}
