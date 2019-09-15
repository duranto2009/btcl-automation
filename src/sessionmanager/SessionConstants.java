/*
 * Created on Oct 27, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package sessionmanager;

import coLocation.CoLocationConstants;
import common.EntityTypeConstant;
import common.ModuleConstants;
import global.GlobalService;
import ip.IPConstants;
import ip.MethodReferences;
import lli.LLILongTermContract;
import lli.connection.LLIConnectionConstants;
import officialLetter.OfficialLetterType;
import upstream.UpstreamConstants;
import upstream.inventory.UpstreamInventoryItem;
import upstream.inventory.UpstreamInventoryService;
import util.KeyValuePair;
import util.ServiceDAOFactory;
import vpn.client.ClientForm;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SessionConstants {




    /**
     * **********Other Charge Search********
     */
    public final static String NAV_CHARGE = "navcharge";
    public final static String VIEW_CHARGE = "viewcharge";
    public final static String[][] SEARCH_CHARGE =
            {
                    {"ClientID", "clUserName"},
                    {".//clients//AreaCode.jsp", "clAreaCode"},
                    {"ADSL Phone No", "clPhoneNo"},
                    {".//othcharge//Status.jsp", "othStatus"}
            };
    /**
     * *********Bill Search***********
     */


    public final static String EMAIL = "email";

    public final static String NAV_EMAIL = "navemail";
    public final static String VIEW_EMAIL = "VIEW_EMAIL";
    public final static String HELP = "help";
    public final static String REPLY = "reply";
    public final static String REGION = "Region";
    public final static String COMPLAIN = "Complain";
    public final static String[][] SEARCH_REGION =
            {
                    {"Region Name", "rRegionName"},
                    {"Region Code", "rRegionDesc"},
                    {".//region//RegionStatus.jsp", "rStatus"}
            };
    public static String[][] SEARCH_INVENTORY_ITEM =
            {
                    {"Name", "45010"},
                    {"Brand", "45011"},
                    {"Model", "45012"}
            };

    public final static String[][] SEARCH_REPLY = {
            {"Token Code", "hrTokenCode"},
            {"Subject", "hrHelpSub"},
            {"User ID", "clUserName"},
            {".//clients//AreaCode.jsp", "clAreaCode"},
            {"Telephone No", "clPhoneNo"},
            {".//help//HelpStatus.jsp", "hrStatus"},
            {".//help//HelpCategory.jsp", "hrCategoryID"}
    };
    public final static String[][] SEARCH_HELP = {
            {
                    "Token Code", "hrTokenCode"},
            {"Subject", "hrHelpSub"},
            {".//help//HelpStatus.jsp", "hrStatus"},
            {".//help//HelpCategory.jsp", "hrCategoryID"}
    };


    public static final String NAV_SERVICE = "navservice";
    public static final String VIEW_SERVICE = "viewservice";
    public final static String NAV_HELP = "navhelp";
    public final static String NAV_REPLY = "navreply";
    public final static String NAV_REGION = "navregion";
    public final static String VIEW_HELP = "VIEW_HELP";
    public final static String VIEW_REPLY = "VIEW_REPLY";
    public final static String VIEW_REGION = "VIEW_REGION";
    public final static String USER_LOGIN = "user_login";

    public final static String CLIENT_DEST_RATE = "client_dest_rate";
    public final static String SLOT_INFO = "dest_prefix";
    public final static String RATE_INFO = "rate_info";
    public final static String DEST_PREFIX = "dest_prefix";
    public final static String DEST_RATE_INFO = "dest_rate_info";
    public final static String CALLING_ID = "calling_id";
    public final static String CALLER_ID = "caller_id";
    public final static String GATEWAYS = "gateways";
    //Error Message
    public final static String FAILURE_MESSAGE = "failuremessage";
    //Search Pages DTO
    public final static String VIEW_CHARGE_DEF = "viewchargedef";
    public final static String VIEW_RATE = "viewrate";
    public final static String VIEW_CALL_DEST = "viewcalldest";
    public final static String VIEW_CLIENT = "viewclient";
    public final static String APPROVE_CLIENT = "approveclients";
    public final static String VERIFY_CLIENT = "verifyclients";
    public final static String VIEW_RECHARGE_CLIENT = "viewrechargeclient";
    public final static String VIEW_CREDIT_CLIENT = "viewcreditclient";
    public final static String VIEW_BONUS_CLIENT = "viewbonusclient";
    public final static String VIEW_STATUS_CLIENT = "viewstatusclient";
    public final static String VIEW_PLAN = "viewplan";
    public final static String VIEW_PIN_BATCH = "viewpinbatch";
    public final static String VIEW_PIN_BATCH_DETAILS = "viewpinbatchdetails";
    public final static String VIEW_RATE_PLAN = "viewrateplan";
    public final static String VIEW_RATE_PLAN_DETAILS = "viewrateplandetails";
    public final static String VIEW_MULTIPLE_BILL = "viewmultiplebill";
    public final static String VIEW_CALL_SHOP = "viewcallshop";
    public final static String VIEW_CALL_SHOP_DETAILS = "viewcallshopdetails";
    public final static String VIEW_ROLE = "viewrole";
    public final static String VIEW_GATEWAY = "viewgateway";
    public final static String VIEW_GATEWAY_SERIES = "viewgatewayseries";
    public final static String VIEW_CLIENT_DEST_RATE = "viewclientdestrate";
    public final static String VIEW_SHAREDSECRET = "viewsharedsecret";
    public final static String VIEW_LANGUAGE = "viewlanguage";


    public final static String[][] SEARCH_CLIENT = {
            {".//clients//AreaCode.jsp", "clAreaCode"},
            {"ADSL Phone No", "clPhoneNo"},
            {"Name", "clCustomerName"},
            {".//clients//AddedBy.jsp", "clAddedBy"},
            {".//clients//VerifiedBy.jsp", "clVerifiedBy"},
            {".//clients//ApprovedBy.jsp", "clApprovedBy"},
            {"ClientID", "clUserName"},
            {"Port Number", "clPortNo"},
            {"Reference No/Invoice", "clRefNo"},
            {".//clients//dslm.jsp", "clDslmNo"},
            {".//clients//TypeOfConnection.jsp", "clTypeOfConnection"},
            {".//clients//PaymentType.jsp", "clPaymentType"},
            {".//clients//ApplicationStatus.jsp", "clApplicationStatus"},
            {".//clients//AccountStatus.jsp", "clAccountStatus"},
            {".//clients//Rates.jsp", "clRateID"}
    };
    public final static String[][] SEARCH_TOBEAPPROVED_CLIENT = {
            {"ADSL Phone No", "clPhoneNo"},
            {"Name", "clCustomerName"},
            {"Added By", "clAddedBy"},
            {"Verified By", "clVerifiedBy"},
            {"Approved By", "clApprovedBy"},
            {"Customer ID/User Name", "clUserName"},
            {"Port Number", "clPortNo"},
            {".//clients//AreaCode.jsp", "clAreaCode"},
            {"DSLAM Name", "clDslmNo"},
            {"Reference No/Invoice", "clRefNo"},
            {".//clients//TypeOfConnection.jsp", "clTypeOfConnection"},
            {".//clients//PaymentType.jsp", "clPaymentType"},
            {".//clients//Operators.jsp", "clOperatorID"},
            {".//clients//Verified.jsp", "clApplicationStatus"},
            {".//clients//AccountStatus.jsp", "clAccountStatus"},
            {".//clients//Rates.jsp", "clRateID"}
    };
    public final static String[][] SEARCH_TOBEVERIFIED_CLIENT = {
            {"ADSL Phone No", "clPhoneNo"},
            {"Name", "clCustomerName"},
            {"Added By", "clAddedBy"},
            {"Verified By", "clVerifiedBy"},
            {"Approved By", "clApprovedBy"},
            {"Customer ID/User Name", "clUserName"},
            {"Port Number", "clPortNo"},
            {".//clients//AreaCode.jsp", "clAreaCode"},
            {"DSLM Name", "clDslmNo"},
            {"Reference No/Invoice", "clRefNo"},
            {".//clients//TypeOfConnection.jsp", "clTypeOfConnection"},
            {".//clients//PaymentType.jsp", "clPaymentType"},
            {".//clients//Operators.jsp", "clOperatorID"},
            {".//clients//Submitted.jsp", "clApplicationStatus"},
            {".//clients//AccountStatus.jsp", "clAccountStatus"},
            {".//clients//Rates.jsp", "clRateID"}
    };


    public final static String[][] SEARCH_STATUS_CLIENT = {
            {"ClientID", "clUserName"},

            {".//clients//AreaCode.jsp", "clAreaCode"},
            {"ADSL Phone No", "clPhoneNo"},
            {
                    ".//clients//AccountStatus.jsp", "clAccountStatus"},
            {
                    ".//clients//PaymentType.jsp", "clPaymentType"},};

    public final static String[][] SEARCH_ROLE = {
            {
                    "Role Name", "rlRoleName"},
            {
                    "Role Description", "rlRoleDesc"}
    };


    public final static String SUCCESS = "success";
    public final static String NAV_CHARGE_DEF = "navchargedef";
    public final static String NAV_RATE = "navrate";
    public final static String NAV_CALL_DEST = "navcalldest";
    public final static String NAV_CLIENT = "navclient";
    public final static String NAV_RECHARGE_CLIENT = "navrechargeclient";
    public final static String NAV_CREDIT_CLIENT = "navcreditclient";
    public final static String NAV_BONUS_CLIENT = "navbonusclient";
    public final static String NAV_STATUS_CLIENT = "navstatusclient";
    public final static String NAV_MULTIPLE_BILL = "navmultiplebill";
    public final static String NAV_PLAN = "navplan";
    public final static String NAV_ROLE = "navrole";
    public final static String NAV_GATEWAY = "navgateway";
    public final static String NAV_GATEWAY_SERIES = "navgatewayseries";
    public final static String NAV_CLIENT_DEST_RATE = "navclientdestrate";
    public final static String NAV_SHAREDSECRET = "navsharedsecret";
    public final static String NAV_PIN_BATCH = "navpinbatch";
    public final static String NAV_PIN_BATCH_DETAILS = "navpinbatchdetails";
    public final static String NAV_RATE_PLAN = "navrateplan";
    public final static String NAV_RATE_PLAN_DETAILS = "navrateplandetails";
    public final static String NAV_LANGUAGE = "navlanguage";
    public final static String NAV_CALL_SHOP = "navcallshop";
    public final static String NAV_CALL_SHOP_DETAILS = "navcallshopdetails";
    //navigation bar related
    public final static String NAVIGATION_BAR_FIRST = "first";
    public final static String NAVIGATION_BAR_NEXT = "next";
    public final static String NAVIGATION_BAR_PREVIOUS = "previous";
    public final static String NAVIGATION_BAR_LAST = "last";
    public final static String NAVIGATION_BAR_CURRENT = "current";
    //form check related
    public final static String NAVIGATION_LINK = "id";
    public final static String GO_CHECK_FIELD = "go";
    public final static String SEARCH_CHECK_FIELD = "search";
    public final static String HTML_SEARCH_CHECK_FIELD = "htmlsearch";
    //search quiry related
    public final static String ADVANCED_SEARCH = "AdvancedSearch";
    //record navigation initial parameter
    public final static int CRRENT_PAGE_NO = 1;
    public final static int TOTAL_PAGES = 10;
    public final static int PAGE_SIZE = 50000;
    public final static int NUMBER_OF_RECORDS = 100;
    public final static int FIRST_PAGE = 1;
    public static final String RECORDS_PER_PAGE = "RECORDS_PER_PAGE";
    public static final String Accountnumber_emailID = "Accountnumber_emailID";
    public static final String PAYMENT_INFO = "payment_info";
    public static final String BONUS_ID = "BONUS_ID";
    public final static String NAV_BONUS = "navbonus";
    public final static String VIEW_BONUS = "viewbonus";
    public final static String[][] SEARCH_BONUS = {
            {"Name", "bonusName"},
            {".//bonus//BonusCategory.jsp", "bonusCategoryID"},
            {".//bonus//Status.jsp", "bonusStatus"}
    };
    public final static String NAV_RECORD = "navrecord";
    public final static String VIEW_RECORD = "viewrecord";
    public final static String[][] SEARCH_RECORD = {
            {"Login Name", "loginName"},
            {"Login IP", "sourceIP"},
            {".//logs//From.jsp", "frDate"},
            {".//logs//To.jsp", "toDate"},};
    public static final String DISCOUNT_ID = "DISCOUNT_ID";
    public final static String NAV_DISCOUNT = "navdiscount";
    public final static String VIEW_DISCOUNT = "viewdiscount";
    public final static String[][] SEARCH_DISCOUNT = {
            {"Name", "discountName"},
            {".//discount//DiscountCategory.jsp", "discountCategoryID"},
            {".//discount//Status.jsp", "discountStatus"}
    };

    public final static String NAV_EXCHANGE = "navexchange";
    public final static String VIEW_EXCHANGE = "viewexchange";
    public final static String[][] SEARCH_EXCHANGE = {
            {"Code", "exCode"},
            {"Name", "exName"},
            {".//exchange//Area.jsp", "exNWD"},
            {".//exchange//originalarea.jsp", "exOriginalNWD"},
            {".//exchange//lot.jsp", "exLot"},
            {".//exchange//status.jsp", "exStatus"}

    };
    public final static String[][] SEARCH_PACKAGE = {
            {"Package Name", "pPackageName"},
            {".//package//PackageType.jsp", "pPackageType"},
            {".//package//TariffType.jsp", "pTariffType"},
            {".//package//UnitType.jsp", "pTariffType"}
    };
    public final static String NAV_PACKAGE = "navpackage";
    public final static String VIEW_PACKAGE = "viewpackage";
    public final static String[][] SEARCH_MSG_FORMAT = {
            {".//messageformat//Category.jsp", "msgFormatCategory"},
            {".//messageformat//Type.jsp", "msgFormatType"}
    };
    public final static String NAV_MSG_FORMAT = "navmsgformat";
    public final static String VIEW_MSG_FORMAT = "viewmsgformat";
    public final static String[][] SEARCH_MSG = {
            {".//message//Type.jsp", "msgType"},
            {"Receiver", "msgTo"},
            {"Sender", "msgFrom"},
            {".//message//From.jsp", "frDate"},
            {".//message//To.jsp", "toDate"},
            {".//message//Status.jsp", "msgStatus"}
    };
    public final static String NAV_MSG = "navmsg";
    public final static String VIEW_MSG = "viewmsg";
    public final static String[][] SEARCH_DSLM = {
            {"DSLM Name", "dslmName"},
            {".//dslm//Exchange.jsp", "dslmExchangeNo"},


    };
    public final static String NAV_DSLM = "navdslm";
    public final static String VIEW_DSLM = "viewdslm";
    public static final String[][] SEARCH_OPERATOR = {
            {"Opeator Name", "btOperatorName"},
            {".//operator//OperatorStatus.jsp", "btOperatorStatus"}
    };
    public final static String NAV_OPERATOR = "navoperator";
    public final static String VIEW_OPERATOR = "viewoperator";
    public static final String[][] SEARCH_RECHARGEOPERATOR = {
            {".//rechargeoperator//Operators.jsp", "rechargeOperator"}
    };
    public final static String NAV_RECHARGEOPERATOR = "navrechargeoperator";
    public final static String VIEW_RECHARGEOPERATOR = "viewrechargeoperator";
    public static final String[][] SEARCH_INVOICE = {
            {"Invoice Number", "invoiceName"},
            {".//invoice//PackageType.jsp", "invoiceType"},
            {".//invoice//Operators.jsp", "invoiceOperatorID"},
            {".//invoice//Status.jsp", "invoiceStatus"}
    };
    public final static String NAV_INVOICE = "navinvoice";
    public final static String VIEW_INVOICE = "viewinvoice";
    public static final String[][] SEARCH_INVOICE_ORDER = {
            {".//invoiceorder//PackageType.jsp", "invoiceOrderType"},
            {".//invoiceorder//Operators.jsp", "invoiceOrderOperator"}
    };
    public final static String NAV_INVOICE_ORDER = "navinvoiceorder";
    public final static String VIEW_INVOICE_ORDER = "viewinvoiceorder";
    public final static String[][] SEARCH_MDF = {
            {"ADSL Phone No", "clPhoneNo"},
            {".//mdf//dslm.jsp", "clDslmNo"}
    };
    public final static String NAV_MDF = "navmdf";
    public final static String VIEW_MDF = "viewmdf";
    public final static String[][] SEARCH_MIGRATION = {
            {"ClientID", "clUserName"},
            {".//clients//AreaCode.jsp", "clAreaCode"},
            {"ADSL Phone No", "clPhoneNo"},
            {".//migration//migratedPackage.jsp", "migratedPackage"},
            {".//migration//previousPackage.jsp", "previousPackage"},
            {".//migration//From.jsp", "frDate"},
            {".//migration//To.jsp", "toDate"},};
    public final static String NAV_MIGRATION = "navmigration";
    public final static String VIEW_MIGRATION = "viewmigration";


    public final static String[][] SEARCH_BATCHMIGRATION = {


            {".//batchmigration//migratedPackage.jsp", "migratedPackage"},
            {".//batchmigration//previousPackage.jsp", "previousPackage"},
            {".//batchmigration//From.jsp", "frDate"},
            {".//batchmigration//To.jsp", "toDate"},};
    public final static String NAV_BATCHMIGRATION = "navbatchmigration";
    public final static String VIEW_BATCHMIGRATION = "viewbatchmigration";


    public final static String NAV_CHANGESTATUS_CLIENT = "navchangestatusclient";
    public final static String VIEW_CHANGESTATUS_CLIENT = "viewchangestatusclient";
    public final static String[][] SEARCH_CHANGESTATUS_CLIENT = {
            {"ClientID", "clUserName"},
            {".//clients//AreaCode.jsp", "clAreaCode"},
            {"ADSL Phone Number", "clPhoneNo"},
            {
                    ".//clientstatus//AccountStatus.jsp", "csPrevStatus"},
            {
                    ".//clientstatus//AccountStatusCur.jsp", "csCurStatus"},};
    public final static String[][] SEARCH_SHOW_RECHARGE_INFO = {
            {".//recharge//From.jsp", "frDate"},
            {".//recharge//To.jsp", "toDate"},};
    public final static String[][] SEARCH_RECHARGE_INFO = {
            {"ClientID", "clUserName"},
            {".//clients//AreaCode.jsp", "clAreaCode"},
            {"ADSL Phone No", "clPhoneNo"},
            {".//recharge//From.jsp", "frDate"},
            {".//recharge//To.jsp", "toDate"}};
    public final static String NAV_RECHARGE_INFO = "navrechargeinfo";
    public final static String VIEW_RECHARGE_INFO = "viewrechargeinfo";


    public final static String[][] SEARCH_PREPAID_PAYMENT = {
            {"ClientID", "clUserName"},
            {"ADSL Phone No", "clPhoneNo"},
            {".//prepaidpayment//From.jsp", "frDate"},
            {".//prepaidpayment//To.jsp", "toDate"}};
    public final static String NAV_PREPAID_PAYMENT = "navprepaidpayment";
    public final static String VIEW_PREPAID_PAYMENT = "viewprepaidpayment";


    public final static String[][] SEARCH_BILL_INFO = {
            {"Client ID", "clUserName"},
            {".//clients//AreaCode.jsp", "clAreaCode"},
            {"ADSL Phone No", "clPhoneNo"},
            {"Bill No.", "cbBillNo"},
            {".//billing//From.jsp", "frDate"},
            {".//billing//To.jsp", "toDate"}

    };
    public final static String[][] SEARCH_CUSTOMER_FOR_BILL = {
            {".//clients//AreaCode.jsp", "clAreaCode"},
            {"ADSL Phone No.", "clPhoneNo"},
            {"ClientID", "clUserName"}};
    public final static String NAV_BILL_INFO = "navbillinfo";
    public final static String VIEW_BILL_INFO = "viewbillinfo";
    public final static String[][] SEARCH_REMOVE_INFO = {
            {"ClientID", "clUserName"},
            {".//clients//AreaCode.jsp", "clAreaCode"},
            {"Phone No.", "clPhoneNo"},
            {".//remove//From.jsp", "frDate"},
            {".//remove//To.jsp", "toDate"}
    };
    public final static String NAV_REMOVE_INFO = "navremoveinfo";
    public final static String VIEW_REMOVE_INFO = "viewremoveinfo";
    public final static String[][] SEARCH_SHIFT_INFO = {
            {"ClientID", "clUserName"},
            {"Old Phone No.", "csOldPhone"},
            {"New Phone No.", "csNewPhone"},
            {".//shift//From.jsp", "frDate"},
            {".//shift//To.jsp", "toDate"}
    };
    public final static String NAV_SHIFT_INFO = "navshiftinfo";
    public final static String VIEW_SHIFT_INFO = "viewshiftinfo";
    //Tausun
    public final static String REPORT_CONFIGURATION_OPTIONS = "reportConfigurationOptions";
    public final static String NAV_REPORT = "navreport";
    public final static String VIEW_REPORT = "viewreport";
    public final static String[][] SEARCH_REPORT = {
            {"Report Configuration Title", "rcConfigurationTitle"},
            {"Report Configuration Type", "rcType"}
    };
    public final static String REPORT_CONFIGURATION_SAVED = "reportConfigurationSaved";
    public final static String REPORT_CONFIGURATION_DELETED = "reportConfigurationDeleted";
    public final static String NAV_USAGE = "navusage";
    public final static String VIEW_USAGE = "viewusage";
    public final static String[][] SEARCH_USAGE =
            {
                    {"Client Username", "clUserName"},
                    {"MAC Address", "cdrMacAddress"},
                    {"IP Address", "cdrIpAddress"},
                    {"ADSL Phone", "clPhoneNo"},
                    {".//usage//ConnectTime.jsp", "frDate"},
                    {".//usage//DisconnectTime.jsp", "toDate"}
            };

    public final static String[][] SEARCH_PENDING_COMMANDS = {
            {".//clients//AreaCode.jsp", "clAreaCode"},
            {"ADSL Phone No", "clPhoneNo"},
            {"ClientID", "clUserName"},

    };
    public final static String NAV_PENDING_COMMANDS = "navpendingcommands";
    public final static String VIEW_PENDING_COMMANDS = "viewpendingcommands";

    public final static String NAV_MML_LOG = "navmmllog";
    public final static String VIEW_MML_LOG = "viewmmllog";
    public final static String[][] SEARCH_MML_LOG = {
            {"ClientID", "chCommand"},
            {".//logs//Status.jsp", "chStatus"},
            {".//logs//From.jsp", "frDate"},
            {".//logs//To.jsp", "toDate"},};

    /*********** Vpn block*************/
    public final static String[][] SEARCH_VPNCLIENT =
            {
                    {"Client Name", "vpnClientName"},
                    {"Phone No", "vpPhoneNo"},
                    {".//clients//TypeOfConnection.jsp", "clTypeOfConnection"},
                    {".//clients//dslm.jsp", "clDslmNo"},
                    {".//vpnclients//destdslm.jsp", "clDestDslmNo"},
                    {".//clients//AccountStatus.jsp", "clAccountStatus"},
                    {".//vpnclients//Rates.jsp", "clRateID"}
            };
    public final static String NAV_VPNCLIENT = "navvpnclient";
    public final static String VIEW_VPNCLIENT = "viewvpnclient";
    /*********** Vpn block*************/

    /**********LLI BLOCK*********/
    public final static String[][] SEARCH_LLICLIENT =
            {
                    {"Client Name", "lliClientName"},
                    {"Phone No", "lliPhoneNo"},
                    {".//clients//TypeOfConnection.jsp", "clTypeOfConnection"},
                    {".//clients//dslm.jsp", "clDslmNo"},
                    {".//lliclients//destdslm.jsp", "clDestDslmNo"},
                    {".//clients//AccountStatus.jsp", "clAccountStatus"},
                    {".//lliclients//Rates.jsp", "clRateID"}
            };
    public final static String NAV_LLICLIENT = "navlliclient";
    public final static String VIEW_LLICLIENT = "viewlliclient";
    /**********LLI BLOCK END*********/

    public final static String NAV_HOSTINGPACKAGE = "navHOSTINGPACKAGE";
    public final static String VIEW_HOSTINGPACKAGE = "viewHOSTINGPACKAGE";
    public static final String[][] SEARCH_HOSTINGPACKAGE = {{"Package Name", "HPNAME"},/*{"Cost","HPCOST"},{".//hostingpackage//CostType.jsp","HPCOSTTYPE"},*/{".//hostingpackage//Space.jsp", "HPSPACE"}, {".//hostingpackage//OS.jsp", "HPOS"}, {"Bandwidth", "HPBANDWIDTH"}, {".//hostingpackage//orderDate.jsp", "ORTIME"}, {".//hostingpackage//orderDateEnd.jsp", "ORTIMEEND"},/*{"Type","ORTYPE"},*/ {".//hostingpackage//Status.jsp", "ORSTATUS"}};

    public final static String NAV_HOSTINGPACKAGE_REPORT = "navHOSTINGPACKAGE";
    public final static String VIEW_HOSTINGPACKAGE_REPORT = "viewHOSTINGPACKAGE";
    public static final String[][] SEARCH_HOSTINGPACKAGE_REPORT = {{"Package Name", "HPNAME"},/*{"Cost","HPCOST"},{".//hostingpackage//CostType.jsp","HPCOSTTYPE"},*/{".//hostingpackage//Space.jsp", "HPSPACE"}, {".//hostingpackage//OS.jsp", "HPOS"}, {"Bandwidth", "HPBANDWIDTH"}, {".//hostingpackage//orderDate.jsp", "ORTIME"}, {".//hostingpackage//orderDateEnd.jsp", "ORTIMEEND"},/*{"Type","ORTYPE"}, */{".//hostingpackage//Status.jsp", "ORSTATUS"}};

    public final static String NAV_HOSTINGPAYMENT = "navHostingPayment";
    public final static String VIEW_HOSTINGPAYMENT = "viewHostingPayment";
    public static final String[][] SEARCH_HOSTINGPAYMENT = {{"Bank Name", "PYBANKNAME"}, {"Account Name", "PYACCOUNTNAME"}, {"Account No", "PYACCOUNTNO"}, {"Reference", "PYREFERENCE"}, {".//hostingpayment//Status.jsp", "ORSTATUS"}, {".//hostingpayment//Service.jsp", "ORTYPE"}, {".//hostingpayment//paymentDate.jsp", "PYPAYMENTDATE"}, {".//hostingpayment//paymentDateEnd.jsp", "PYPAYMENTDATE_END"}};

    public final static String NAV_HOSTINGDOMAIN = "navHostingdomain";
    public final static String VIEW_HOSTINGDOMAIN = "viewHostingdomain";
    public static final String[][] SEARCH_HOSTINGDOMAIN = {{"Domain", "hdNAME"}, {".//hostingdomain//TLD.jsp", "TLD"},/*{"Cost","hdCOST"}, {"Cost Type","hdCOSTTYPE"},*/{".//hostingpackage//Status.jsp", "ORSTATUS"}, {".//hostingdomain//orderDate.jsp", "ORTIME"}, {".//hostingdomain//orderDateEnd.jsp", "ORTIMEEND"}};

    public final static String NAV_HOSTINGDOMAIN_REPORT = "navHostingdomain";
    public final static String VIEW_HOSTINGDOMAIN_REPORT = "viewHostingdomain";
    public static final String[][] SEARCH_HOSTINGDOMAIN_REPORT = {{"Domain", "hdNAME"}, {".//hostingdomain//TLD.jsp", "TLD"},/* {"Cost","hdCOST"}, {"Cost Type","hdCOSTTYPE"},*/{".//hostingdomain//orderDate.jsp", "ORTIME"}, {".//hostingdomain//orderDateEnd.jsp", "ORTIMEEND"}, {".//hostingpackage//Status.jsp", "ORSTATUS"}};

    public final static String NAV_IIG = "navIig";
    public final static String VIEW_IIG = "viewIig";
    public static final String[][] SEARCH_IIG = {{".//iig//RegCat.jsp", "IGREGCAT"}, {".//iig//ConnType.jsp", "IGCONNTYPE"}, {".//iig//LoopProvider.jsp", "IGLOOPPROVIDER"}, /*{"Initial Commitment Period","IGINITPERIOD"},*/ {".//iig//Status.jsp", "IGSTATUS"}, {".//iig//dateFrom.jsp", "IGSERVICESTARTTIME"}, {".//iig//dateTo.jsp", "IGSERVICESTARTTIMEEND"}};

    public final static String NAV_HOSTINGPACKAGE2 = "navHostingpackage2";
    public final static String VIEW_HOSTINGPACKAGE2 = "viewHostingpackage2";
    public static final String[][] SEARCH_HOSTINGPACKAGE2 = {{"Name", "hpNAME"}, {"Cost", "hpCOST"}, {".//hostingpackage//CostType.jsp", "HPCOSTTYPE"}, {".//hostingpackage//Space.jsp", "HPSPACE"}, {".//hostingpackage//OS.jsp", "HPOS"}, {"BandWidth", "hpBANDWIDTH"}};

    public final static String NAV_ORDER = "navOrder";
    public final static String VIEW_ORDER = "viewOrder";
    public static final String[][] SEARCH_ORDER = {{"Invoice No", "ORID"}, {".//order//Service.jsp", "ORTYPE"}, {".//order//orderDate.jsp", "ORTIME"}, {".//order//orderDateEnd.jsp", "ORTIMEEND"}, {".//order//Status.jsp", "ORSTATUS"}/*, {"ORDURATION","ORDURATION"}*/};

    //Insert the following variables in sessionmanager.SessionConstants file
    public final static String NAV_VPN = "navVpn";
    public final static String VIEW_VPN = "viewVpn";
    public static final String[][] SEARCH_VPN = {{".//vpn//BandwidthType.jsp", "VPBANDWIDTHTYPE"}, {".//vpn//Bandwidth.jsp", "VPBANDWIDTH"}, {".//vpn//PortType.jsp", "VPPORTTYPE"}, {".//vpn//CableType.jsp", "VPCABLETYPE"}, {".//vpn//orderDate.jsp", "ORTIME"}, {".//vpn//orderDateEnd.jsp", "ORTIMEEND"}, {".//vpn//Status.jsp", "ORSTATUS"},/*{"VPDISTRICTFROM","VPDISTRICTFROM"}, {"VPTHANAFROM","VPTHANAFROM"}, {"VPLOCATIONFROM","VPLOCATIONFROM"}, {"VPDISTRICTTO","VPDISTRICTTO"}, {"VPTHANATO","VPTHANATO"}, {"VPLOCATIONTO","VPLOCATIONTO"},*/ {".//vpn//Distance.jsp", "VPDISTANCE"}};

    public final static String NAV_LLI = "navLli";
    public final static String VIEW_LLI = "viewLli";
    public static final String[][] SEARCH_LLI = {
            {".//lli//Bandwidth.jsp", "LLBANDWIDTH"},
            {".//lli//LoopProvider.jsp", "LLLOOPPROVIDER"},
            {"Equipment", "LLEQUIPMENTNAME"},
            {"IP Route", "LLIPROUTE"},
            {".//lli//orderDate.jsp", "ORTIME"},
            {".//lli//orderDateEnd.jsp", "ORTIMEEND"},
            {".//lli//Status.jsp", "ORSTATUS"}
    };
    /*
     public final static String NAV_COMPLAINT = "navComplaint";
     public final static String VIEW_COMPLAINT = "viewComplaint";
     public static final String[][] SEARCH_COMPLAINT = {{"Department","cmDepartment"}, {"Priority","cmPriority"}, {"Subject","cmSubject"},{"Summary","cmSummary"},{"Message","cmMessage"},{"Document","cmDocument"}};
     */
    public final static String NAV_USER = "navUser";
    public final static String VIEW_USER = "viewUser";
    public static final String[][] SEARCH_USER = {
            {"Username", "usUserName"},
            {".//user//status.jsp", "usStatus"},
            {"Name", "usFullName"},
            {"Phone", "usPhoneNo"},
            {"Designation", "usDesignation"},
            {"Email", "usMailAddr"},
            {".//user//Roles.jsp", "usRoleID"}
    };


    public final static String NAV_TEST = "navTEST";
    public final static String VIEW_TEST = "viewTEST";
    public static final String[][] SEARCH_TEST = {{"Roll", "roll"}, {"Name", "name"}};


    public final static String NAV_ABC = "navABC";
    public final static String VIEW_ABC = "viewABC";
    public static final String[][] SEARCH_ABC = {{"Profession", "profession"}, {"Income", "income"}};


    //Insert the following variables in sessionmanager.SessionConstants file
    public final static String NAV_VPN_CLIENT = "navVpnClient";
    public final static String VIEW_VPN_CLIENT = "viewVpnClient";
    public static final String[][] SEARCH_VPN_CLIENT = {
            {"Name", "vclcName"},
//            {".//clients//clientType.jsp", "vclClientType"},
            {"keyValuePairDropdown", "Client Type_" + NAV_VPN_CLIENT},
            {"Username", "clLoginName"},
            {".//clients//registrationType.jsp", "vclRegType"},
//            {"keyValuePairDropdown", "Registration Type_" + NAV_VPN_CLIENT},


            /*{"ID No ","vclIdentity"},*/
//            {".//clients//clientStatus.jsp", "vclCurrentStatus"},
            {"keyValuePairDropdown", "Client Status_" + NAV_VPN_CLIENT},
//            {".//clients//clientStatus.jsp", "vclCurrentStatus"},
            {"Mobile Number", "vclcPhoneNumber"},
            {"E-mail", "vclcEmail"},
//			{".//common//showDeleted.jsp", "showDeleted"}

    };

    //Insert the following variables in sessionmanager.SessionConstants file
    public final static String NAV_LLI_CLIENT = "navLliClient";
    public final static String VIEW_LLI_CLIENT = "viewLliClient";
    public static final String[][] SEARCH_LLI_CLIENT = {
            {"First Name", "vclcName"},
            {".//clients//clientType.jsp", "vclClientType"},
            {"Username", "clLoginName"},
            {".//clients//registrationType.jsp", "vclRegType"},
            {"ID No ", "vclIdentityNo"},
            {".//clients//clientStatus.jsp", "vclCurrentStatus"},
            {"Mobile Number", "vclcPhoneNumber"},
            {"E-mail", "vclcEmail"},
    };

    public final static String NAV_DOMAIN = "navDomain";
    public final static String VIEW_DOMAIN = "viewDomain";
    public static final String[][] SEARCH_DOMAIN = {
            {".//common//banglaKeyboard.jsp", "domainAddress"},
            {"Client", "client"},
            {".//domain//expireDateFrom.jsp", "expireDateFrom"},
            {".//domain//expireDateTo.jsp", "expireDateTo"},
            {".//domain//status.jsp", "status"},
            {".//domain//statusSpecific.jsp", "statusSpecific"},
            {"Email", "email"},
            {"Mobile No", "mobileNo"},
            {".//common//showDeleted.jsp", "showDeleted"}
    };

    public final static String NAV_DOMAIN_SEARCH_LOG = "navDomainSearchLog";
    public final static String VIEW_DOMAIN_SEARCH_LOG = "viewDomainSearchLog";
    public static final String[][] SEARCH_DOMAIN_SEARCH_LOG = {
            {"Domain", "domainName"},
            {"IP", "ip"},
            {".//domain//dateFrom.jsp", "dateFrom"},
            {".//domain//dateTo.jsp", "dateTo"},
            {".//domain//availableStatus.jsp", "status"}
    };
  /*
  //dhrubo
  public final static String NAV_DNS_DOMAIN = "navDnsDomain";
  public final static String VIEW_DNS_DOMAIN = "viewDnsDomain";
  public static final String[][] SEARCH_DNS_DOMAIN= {
			  {"Domain Address","dnsDomainAddress"} , //label, key
			  {"Client Name","dnsDomainClientName"},
			  //{".//dnsdomain//status.jsp","dnsDomainCurrentStatus"},
			  {".//dnsdomain//expireDateFrom.jsp","dnsDomainExpiryDateFrom"},
			  {".//dnsdomain//expireDateTo.jsp","dnsDomainExpiryDateTo"},
			  {"Primary DNS", "dnsDomainPrimaryDNS"},
			  {"Primary DNS IP", "dnsDomainPrimaryDnsIP"},
			  {"Secondary DNS", "dnsDomainSecondaryDNS"},
			  {"Secondary DNS IP", "dnsDomainSecondaryDnsIP"},
			  {"Tertiary DNS", "dnsDomainTertiaryDNS"},
			  {"Tertiary DNS IP", "dnsDomainTertiaryDnsIP"}
  };
  //dhrubo
  */

    /*raihan*/
    public final static String NAV_DNS_DOMAIN = "navDnsDomain";
    public final static String VIEW_DNS_DOMAIN = "viewDnsDomain";
    public static final String[][] SEARCH_DNS_DOMAIN = {
            {"Domain Name", "dnsDomainName"}, //label, key
            {"Client Name", "dnsDomainClientName"},
    };

    /*raihan*/
    public final static String NAV_DOMAIN_NAME = "navDomainName";
    public final static String VIEW_DOMAIN_NAME = "viewDomainName";
    public static final String[][] SEARCH_DOMAIN_NAME = {{"Domain Name", "domainName"}, {".//common//domainPackageType.jsp", "packageTypeID"}};

    public final static String NAV_FORBIDDEN_DOMAIN_NAME = "navForbiddenWord";
    public final static String VIEW_FORBIDDEN_DOMAIN_NAME = "viewForbiddenWord";
    public static final String[][] SEARCH_FORBIDDEN_DOMAIN_NAME = {{"Forbidden Word", "forbiddenWord"}};

    public final static String NAV_DOMAIN_PACKAGE_TYPE = "navDomainPackageType";
    public final static String VIEW_DOMAIN_PACKAGE_TYPE = "viewDomainPackageType";
    public static final String[][] SEARCH_DOMAIN_PACKAGE_TYPE = {};


    public final static String NAV_DOMAIN_PACKAGE = "navDomainPackage";
    public final static String VIEW_DOMAIN_PACKAGE = "viewDomainPackage";
    public static final String[][] SEARCH_DOMAIN_PACKAGE = {{".//billing//generationTimeFrom.jsp", "generationTimeFrom"}, {".//billing//generationTimeTo.jsp", "generationTimeTo"}, {"Domain Name", "domainName"}};

    public final static String NAV_BILL = "navBill";
    public final static String VIEW_BILL = "viewBill";
    public static final String[][] SEARCH_BILL = {
            {"Bill ID", "billID"}, {"Client Name", "clientName"},

            {".//billing//generationTimeFrom.jsp", "generationTimeFrom"},
            {".//billing//generationTimeTo.jsp", "generationTimeTo"},
            {".//billing//LastPaymentFrom.jsp", "lastPaymentDateFrom"},
            {".//billing//LastPaymentTo.jsp", "lastPayementDateTo"},
//		  {".//billing//PaymentFrom.jsp","paymentDateFrom"},
//		  {".//billing//PaymentTo.jsp","paymentDateTo"},
            {".//common//paymentStatus.jsp", "paymentStatus"},
            {".//billing//billActionType.jsp", "billActionType"}

    };

    public final static String NAV_PAYMENT = "navPayment";
    public final static String VIEW_PAYMENT = "viewPayment";
    public static final String[][] SEARCH_PAYMENT = {
            {"Client Name", "clientName"},
            {".//common//description.jsp", "description"},
            {"Bill Amount From", "billAmountFrom"},
            {"Bill Amount To", "billAmountTo"},
            {".//billing//PaymentFrom.jsp", "paymentDateFrom"},
            {".//billing//PaymentTo.jsp", "paymentDateTo"},
            {".//common//paymentGatewayType.jsp", "paymentGatewayType"},
            {".//common//paymentStatusForPaymentSearch.jsp", "paymentStatus"}
    };


    public final static String NAV_COMPLAIN = "navcomplain";
    public final static String VIEW_COMPLAIN = "VIEW_COMPLAIN";
    public final static String[][] SEARCH_COMPLAIN = {
            {"Client Name", "clientName"},
            {".//complain//statusType.jsp", "chStatus"},
            {".//complain//creationTimeFrom.jsp", "creationTimeFrom"},
            {".//complain//creationTimeTo.jsp", "creationTimeTo"},
            {"Token", "cmID"},
            {".//complain//priorityType.jsp", "cmPriority"},

    };

    public final static String NAV_TEST_ = "navtest_";
    public final static String VIEW_TEST_ = "VIEW_TEST_";
    public final static String[][] SEARCH_TEST_ = {
            {"Name", "name"}
    };

    /*Crm*/
    public final static String NAV_CRM_COMPLAIN = "navcrmcomplain";
    public final static String VIEW_CRM_COMPLAIN = "VIEW_CRM_COMPLAIN";
    public final static String[][] SEARCH_CRM_COMPLAIN = {
            {"Token ID", "tokenID"},
            {"Sub Token ID", "subTokenID"},
            {".//crm//SubmissionFrom.jsp", "submissionFrom"},
            {".//crm//SubmissionTo.jsp", "submissionTo"},
            {".//crm//complainStatus.jsp", "status"},
            {"Employee Name", "employeeName"},
            {".//crm//complainPriority.jsp", "priority"},


    };

    public static final String NAV_CRM_CLIENT_COMPLAIN = "navCrmClientComplain";
    public static final String VIEW_CRM_CLIENT_COMPLAIN = "viewCrmClientComplain";
    public static final String[][] SEARCH_CRM_CLIENT_COMPLAIN = {
            {"Client Name", "cleintName"},
            {"Employee Name", "employeeName"},
            {"Token ID", "ID"},
            {".//crm//complainEntityType.jsp", "entityTypeID"},
            {".//crm//complainStatus.jsp", "status"},
            {".//crm//complainPriority.jsp", "priority"},
            {".//crm//SubmissionFrom.jsp", "submissionFrom"},
            {".//crm//SubmissionTo.jsp", "submissionTo"}
    };

    public final static String NAV_CRM_REPORT = "navcrmreport";
    public final static String VIEW_CRM_REPORT = "VIEW_CRM_REPORT";
    public final static String[][] SEARCH_CRM_REPORT = {
            {"Designation", "designation"},
            {"Day", "day"}
    };
    /*./Crm*/


    public final static String NAV_LLI_LINK = "navLliLink";
    public final static String VIEW_LLI_LINK = "viewLliLink";
    public static final String[][] SEARCH_LLI_LINK = {
            {"Connection Name", "name"},
            {".//lli//clientSearchField.jsp", ""},
            {"BandWidth From(Mb)", "bwFrom"},
            {"Bandwidth To(Mb)", "bwTo"},
            {".//link//activationDateFrom.jsp", "activationDateFrom"},
            {".//link//activationDateTo.jsp", "activationDateTo"},
            {".//lli//status.jsp", "status"},
            {".//common//showDeleted.jsp", ""}
    };


    public final static String NAV_VPN_LINK = "nav-vpn-link";
    public final static String VIEW_VPN_LINK = "view-vpn-link";
    public static final String[][] SEARCH_VPN_LINK = {
//		  {"Link Name","vlkName"},
//		  {".//clients//clientSearchField.jsp",""},
//		  {"Near End Address", "nearEnd"},
//		  {"Far End Address","farEnd"},
//		  {".//vpn//vpnLinkStatus.jsp",""},
//		  {".//common//showDeleted.jsp", ""},
//		  {"BandWidth From(Mb)","bwFrom"},
//		  {"Bandwidth To(Mb)","bwTo"},
//		  {".//link//activationDateFrom.jsp",""},
//		  {".//link//activationDateTo.jsp",""} ,
            {"Application ID", "applicationId"},
            {"Client", "clientName"},
//            {"keyValuePairDropdown","Type_"+NAV_VPN_LINK},
//            {" ", "Status_"+NAV_VPN_LINK},
//            {".//common//genericFromDate.jsp", "application-date-from<Application>"},
//            {".//common//genericToDate.jsp", "application-date-to<Application>"}

    };

    public final static String NAV_ASN_APP= "nav-asn-app";
    public final static String VIEW_ASN_APP = "view-asn-app";
    // TODO: 3/26/2019  need to add more filters for search 
    public static final String[][] SEARCH_ASN_APP = {
            {"Application ID","applicationId"},
            {"Client", "clientName"},
    };

    public final static String NAV_ASN= "nav-asn";
    public final static String VIEW_ASN = "view-asn";
    // TODO: 3/26/2019 need to add more filters for search
    public static final String[][] SEARCH_ASN = {
            {"Asn Id","applicationId"},
            {"Client", "clientName"},
    };


    public final static String NAV_UPSTREAM_REQUEST = "nav-upstream-request";
    public final static String VIEW_UPSTREAM_REQUEST = "view-upstream-request";
    public static final String[][] SEARCH_UPSTREAM_REQUEST = {
            {"Application ID", "applicationId"},
            {".//common//genericFromDate.jsp", "srfDateFrom<SRF Date From>"},
            {".//common//genericToDate.jsp", "srfDateTo<SRF Date TO>"},
            {"keyValuePairDropdown", "Type Of Bandwidth_" + NAV_UPSTREAM_REQUEST},
            {"keyValuePairDropdown", "BTCL Service Location_" + NAV_UPSTREAM_REQUEST},
            {"keyValuePairDropdown", "Selected Provider_" + NAV_UPSTREAM_REQUEST},
            {"keyValuePairDropdown", "Media_" + NAV_UPSTREAM_REQUEST},
            {"keyValuePairDropdown", "Provider Location_" +NAV_UPSTREAM_REQUEST },
//            {"Status", "applicationStatus"},
    };
    public final static String NAV_UPSTREAM_CONTRACT = "nav-upstream-contract";
    public final static String VIEW_UPSTREAM_CONTRACT = "view-upstream-contract";
    public static final String[][] SEARCH_UPSTREAM_CONTRACT = {
            {"Contract ID", "contractId"},
            {".//common//genericFromDate.jsp", "srfDateFrom<SRF Date From>"},
            {".//common//genericFromDate.jsp", "srfDateTo<SRF Date TO>"},
            {"keyValuePairDropdown", "Type Of Bandwidth_" + NAV_UPSTREAM_REQUEST},
            {"keyValuePairDropdown", "BTCL Service Location_" + NAV_UPSTREAM_REQUEST},
            {"keyValuePairDropdown", "Selected Provider_" + NAV_UPSTREAM_REQUEST},
            {"keyValuePairDropdown", "Media_" + NAV_UPSTREAM_REQUEST},
            {"keyValuePairDropdown", "Provider Location_" +NAV_UPSTREAM_REQUEST },
    };




    public final static String NAV_VPN_NETWORK_LINK = "nav-vpn-network-link";
    public final static String VIEW_VPN_NETWORK_LINK = "view-vpn-network-link";
    public static final String[][] SEARCH_NETWORK_VPN_LINK = {
            {"Link ID", "linkId"},
            {"Client", "clientName"},
    };


    public final static String NAV_VPN_PROBABLE_TD_CLIENT = "nav_vpn_probable_td_client";
    public final static String VIEW_VPN_PROBABLE_TD_CLIENT = "view_vpn_probable_td_client";
    public final static String[][] SEARCH_VPN_PROBABLE_TD_CLIENT = {
            {".//common//genericFromDate.jsp", "fromDate"},
            {".//common//genericToDate.jsp", "toDate"},
            {"Client Name", "clientName"},
    };


    public final static String NAV_REQUEST = "navRequest";
    public final static String VIEW_REQUEST = "viewRequest";
    public static final String[][] SEARCH_REQUEST = {
            {".//request//requestType.jsp", "arRequestTypeID"},
            {".//request//requestStatus.jsp", "arCompletionStatus"},
            {".//request//requestDateFrom.jsp", "arReqTimeFrom"},
            {".//request//requestDateTo.jsp", "arReqTimeTo"},
            {".//user//autoUserListByName.jsp", "arRequestedByAccountID"},
            {".//user//autoUserListByName.jsp", "arRequestedToAccountID"},
            {"Client", "arClientId"}
    };


    public final static String NAV_NOTE = "navNote";
    public final static String VIEW_NOTE = "viewNote";
    public static final String[][] SEARCH_NOTE = {
            {".//request//noteType.jsp", "arRequestTypeID"},
            {"Client", "arClientId"},
            {".//request//requestDateFrom.jsp", "arReqTimeFrom"},
            {".//request//requestDateTo.jsp", "arReqTimeTo"}
    };

    public final static String NAV_COMMON_REQUEST = "navCommonRequest";
    public final static String VIEW_COMMON_REQUEST = "viewCommonRequest";
    public static final String[][] SEARCH_COMMON_REQUEST = {
            {"Name", "arClientId"},
            {"Description", "arDescription"},
//            {".//request//requestType.jsp", "arRequestTypeID"},
            {".//request//requestDateFrom.jsp", "arReqTimeFrom"},
            {".//request//requestDateTo.jsp", "arReqTimeTo"},
    };

    public final static String NAV_COMMENT = "navComment";
    public final static String VIEW_COMMENT = "viewComment";
    public static final String[][] SEARCH_COMMENT = {
            {"Name", "arClientId"},
            {"Description", "arDescription"},
//            {".//request//requestType.jsp", "arRequestTypeID"},
            {".//request//requestDateFrom.jsp", "arReqTimeFrom"},
            {".//request//requestDateTo.jsp", "arReqTimeTo"},
    };


    public final static String NAV_VPN_INVENTORY = "navVpnInventory";
    public final static String VIEW_VPN_INVENTORY = "viewVpnInventory";

    public final static String NAV_WEB_SECURITY_LOG = "navWebSecurityLog";
    public final static String VIEW_WEB_SECURITY_LOG = "viewWebSecurityLog";
    public static final String[][] SEARCH_WEB_SECURITY_LOG = {{"Username", "awslUsername"}, {".//webSecurityLog//attemptType.jsp", "awslAttemptType"}, {"IP Address", "awslIpAddress"}, {"Port", "awslPort"}, {".//webSecurityLog//requestDateFrom.jsp", "awslTimeFrom"}, {".//webSecurityLog//requestDateTo.jsp", "awslTimeTo"}};

    public final static String NAV_CLC_REGION = "navClcRegion";
    public final static String VIEW_CLC_REGION = "viewClcRegion";
    public final static String[][] SEARCH_CLC_REGION = {
            {"Region Name", "regionName"}
    };

    public final static String NAV_CLC_SPACE = "navClcSpace";
    public final static String VIEW_CLC_SPACE = "viewClcSpace";
    public final static String[][] SEARCH_CLC_SPACE = {
            {"Region Name", "regionName"},
            {"Space Name", "spaceName"},
            {"Length", "spaceLength"},
            {"Width", "spaceWidth"}
    };

    public final static String NAV_CLC_RACK = "navClcRack";
    public final static String VIEW_CLC_RACK = "viewClcRack";
    public final static String[][] SEARCH_CLC_RACK = {
            {"Region Name", "regionName"},
            {"Space Name", "spaceName"},
            {"Rack Name", "rackName"},
            {"From Depth", "fromDepth"},
            {"To Depth", "toDepth"},
            {"From Width", "fromWidth"},
            {"To Width", "toWidth"},
            {"From Height", "fromHeight"},
            {"To Height", "toHeight"},
            {"..//coLocation//rack//rackType.jsp", "rackType"},
            {"Rack Position", "rackPosition"}


    };

    public static final String NAV_CLC_RACK_SLICE = "navClcRackSlice";
    public static final String VIEW_CLC_RACK_SLICE = "viewClcRackSlice";
    public static final String[][] SEARCH_CLC_RACK_SLICE = {
            {"Region Name", "regionName"},
            {"Space Name", "spaceName"},
            {"Rack Name", "rackName"},
            {"..//coLocation//rackSlice//powerType.jsp", "powerType"},
            {"From Offset", "fromOffset"},
            {"To Offset", "toOffset"},
            {"From Length", "fromLength"},
            {"To Length", "toLength"},
            {"From OFC Length", "fromOFCLength"},
            {"To OFC Length", "toOFCLength"},
            {"..//coLocation//rackSlice//occupyType.jsp", "occupyType"}


    };

    public final static String NAV_CRM_DEPT = "navCrmDept";
    public final static String VIEW_CRM_DEPT = "viewCrmDept";
    public final static String[][] SEARCH_CRM_DEPT = {
            {"Department", "departmentName"},
            {"District", "districtName"},
            {"Upazila", "upazilaName"},
            {"Union", "unionName"}

    };

    public final static String NAV_BANK = "navbank";
    public final static String VIEW_BANK = "VIEW_BANK";
    public final static String[][] SEARCH_BANK = {
            {"Bank Name", "bankName"}
    };

    /*Crm Employee Search */
    public final static String NAV_CRM_EMPLOYEE = "nav_crm_employee";
    public final static String VIEW_CRM_EMPLOYEE = "view_crm_employee";
    public final static String[][] SEARCH_CRM_EMPLOYEE = {
            {"Name", "crmEmployeeName"},
            {"District", "districtName"},
            {"Upazila", "upazilaName"},
            {"Union", "unionName"},
            {"Department", "departmentName"}


    };

    /* Colocation Colocation Search - Dhrubo*/
    public final static String NAV_COLOCATION_COLOCATION = "nav_colocation_colocation";
    public final static String VIEW_COLOCATION_COLOCATION = "view_colocation_colocation";
    public final static String[][] SEARCH_COLOCATION_COLOCATION = {
            {".//colocation//colocation//clientName.jsp", "colocationClientID"},
            {"Description", "colocationDescription"},
            {"AC Power From", "colocationPowerAmountACFrom"},
            {"AC Power To", "colocationPowerAmountACTo"},
            {"DC Power From", "colocationPowerAmountDCFrom"},
            {"DC Power To", "colocationPowerAmountDCTo"},
            {"OFC Length From", "colocationOFCLengthFrom"},
            {"OFC Length To", "colocationOFCLengthTo"},
            {"OFC Core From", "colocationOFCCoreFrom"},
            {"OFC Core To", "colocationOFCCoreTo"},

    };


    /* Colocation Colocation Inventory- Forhad*/
    public final static String NAV_COLOCATION_INVENTORY = "nav_colocation_colocation";
    public final static String VIEW_COLOCATION_INVENOTRY = "view_colocation_colocation";
    public final static String[][] SEARCH_COLOCATION_INVENTORY = {
//            {".//lli//fromDate.jsp", ""},
//            {".//lli//toDate.jsp", ""},
            {"keyValuePairDropdown", "Category Name_"+NAV_COLOCATION_INVENTORY},

    };

    /* Co Location Probable TD - Forhad*/
    public final static String NAV_COLOCATION_PROBABLE_TD = "nav_colocation_probable_td";
    public final static String VIEW_COLOCATION_PROBABLE_TD = "view_colocation_probable_td";
    public final static String[][] SEARCH_COLOCATION_PROBABLE_TD = {
            {".//common//genericFromDate.jsp", "fromDate"},
            {".//common//genericToDate.jsp", "toDate"},
            {"Client Name", "clientName"},
    };

    public final static String NAV_PAY_ORDER = "navpayorder";
    public final static String VIEW_PAY_ORDER = "VIEW_PAY_ORDER";
    public final static String[][] SEARCH_PAY_ORDER = {
            {".//payOrder//bankAutoComplete.jsp", "payDraftBankID"},
            {".//payOrder//clientAutoComplete.jsp", "payDraftClientID"},
            {"Draft Number", "payDraftNumber"}
    };

    /*
     * Crm Activity Search
     */
    public final static String NAV_CRM_ACTIVITY = "nav_crm_activity";
    public final static String VIEW_CRM_ACTIVITY = "view_crm_activity";
    public final static String[][] SEARCH_CRM_ACTIVITY = {
            {"Complain ID", "complainID"},
            {"Client Name", "clientName"},
            {".//crm//previousEmployee.jsp", "previousEmployee"},
            {".//crm//currentEmployee.jsp", "currentEmployee"},
            {".//crm//statusType.jsp", "status"},
            {".//crm//actionType.jsp", "actionType"}

    };


    /*LLI Module Begins*/
    /*Please write all things LLI, here*/
    /*May 29, 2018*/
    /*LLI Connection*/
    public final static String NAV_LLI_CONNECTION = "nav_lli_connection";
    public final static String VIEW_LLI_CONNECTION = "view_lli_connection";
    public final static String[][] SEARCH_LLI_CONNECTION = {
            {"Connection ID", "connectionId"},
            {"Connection Name", "connectionName"},
            {"Client Name", "clientName"},
            {"keyValuePairDropdown", "Status_" + NAV_LLI_CONNECTION},
    };
    /*June 5, 2018*/
    /*LLI Application*/
    public final static String NAV_LLI_APPLICATION = "nav_lli_application";
    public final static String VIEW_LLI_APPLICATION = "view_lli_application";
    public final static String[][] SEARCH_LLI_APPLICATION = {
            {"Application ID", "applicationID"},
            {"Client", "clientName"},
            {"keyValuePairDropdown", "Type_" + NAV_LLI_APPLICATION},
//            {"keyValuePairDropdown", "Status_" + NAV_LLI_APPLICATION},
            {".//common//genericFromDate.jsp", "application-date-from<Application>"},
            {".//common//genericToDate.jsp", "application-date-to<Application>"}
    };


    public final static String NAV_LONG_TERM = "nav_lli_long_term";
    public final static String VIEW_LONG_TERM = "view_lli_long_term";
    public final static String[][] SEARCH_LONG_TERM = {
            {"Contract ID", "ID"},
            {"Client Name", "clientName"},
            {".//common//genericFromDate.jsp", "contractStartDateFrom<Contract Start>"},
            {".//common//genericToDate.jsp", "contractStartDateTo<Contract Start>"},
            {".//common//genericFromDate.jsp", "contractEndDateFrom<Contract End>"},
            {".//common//genericToDate.jsp", "contractEndDateTo<Contract End>"},
            {"keyValuePairDropdown", "Status_" + NAV_LONG_TERM}
    };


    /*LLI Module Ends*/
    /* Accounting Incidents */
    public final static String NAV_ACC_INCIDENT = "nav_acc_incident";
    public final static String VIEW_ACC_INCIDENT = "view_acc_incident";
    public final static String[][] SEARCH_ACC_INCIDENT = {


            {".//common//genericFromDate.jsp", "fromDate"},
            {".//common//genericToDate.jsp", "toDate"},
            {"Client Name", "clientName"}
    };
    /* Accounting Incidents Ends */
    /* LLI Probable TD Client Search begins*/
    public final static String NAV_LLI_PROBABLE_TD_CLIENT = "nav_lli_probable_td_client";

    public final static String NAV_LLI_CLIENT_REVISE = "nav_lli_client_revise";
    public final static String NAV_COLOCATION_APPLICATION = "nav_colocation_application";
    public final static String NAV_COLOCATION_CONNECTION = "nav_colocation_connection";


    public final static String VIEW_LLI_PROBABLE_TD_CLIENT = "view_lli_probable_td_client";

    public final static String VIEW_LLI_CLIENT_REVISE = "view_lli_client_revise";
    public final static String VIEW_COLOCATION_APPLICATION = "view_colocation_application";
    public final static String VIEW_COLOCATION_CONENCTION = "view_colocation_connection";

    public final static String[][] SEARCH_LLI_PROBABLE_TD_CLIENT = {
            {".//common//genericFromDate.jsp", "fromDate"},
            {".//common//genericToDate.jsp", "toDate"},
            {"Client Name", "clientName"},
    };

    public final static String[][] SEARCH_LLI_CLIENT_REVISE = {
            {".//lli//fromDate.jsp", ""},
            {".//lli//toDate.jsp", ""},
            {"Client Name", "clientName"},
    };

    public final static String[][] SEARCH_COLOCATION_APPLICATION = {
//            {".//lli//fromDate.jsp", ""},
//            {".//lli//toDate.jsp", ""},
            {"Client Name", "clientName"},
    };

    public final static String[][] SEARCH_COLOCATION_CONNECTION = {
//            {".//lli//fromDate.jsp", ""},
//            {".//lli//toDate.jsp", ""},
            {"Client Name", "clientName"},
//            {"Connection Name","connectionName"},
    };

    /* Client_TD_Status Search Ends*/
    public final static String NAV_NIX_PROBABLE_TD_CLIENT = "nav_nix_probable_td_client";
    public final static String VIEW_NIX_PROBABLE_TD_CLIENT = "view_nix_probable_td_client";
    public final static String[][] SEARCH_NIX_PROBABLE_TD_CLIENT = {
            {".//common//genericFromDate.jsp", "fromDate"},
            {".//common//genericToDate.jsp", "toDate"},
            {"Client Name", "clientName"},
    };

    //lli, nix, vpn monthly bill is generated check

    public final static String NAV_LLI_BILL_GENERATION_CHECK = "nav_lli_bill_generation_check";
    public final static String NAV_NIX_BILL_GENERATION_CHECK = "nav_nix_bill_generation_check";
    public final static String NAV_VPN_BILL_GENERATION_CHECK = "nav_vpn_bill_generation_check";

    public final static String VIEW_LLI_BILL_GENERATION_CHECK = "view_lli_bill_generation_check";
    public final static String VIEW_NIX_BILL_GENERATION_CHECK = "view_nix_bill_generation_check";
    public final static String VIEW_VPN_BILL_GENERATION_CHECK = "view_vpn_bill_generation_check";


    public final static String[][] SEARCH_LLI_BILL_GENERATION_CHECK = {

            {".//lli//clientSearchField.jsp", "clientName"},
/*		  {".//lli//monthlyBillGenerateStatus.jsp", "Status"},
*/          {".//lli//monthpicker.jsp", "Month"}


    };
    public final static String[][] SEARCH_NIX_BILL_GENERATION_CHECK = {

            {".//nix//clientSearchField.jsp", "clientName"},
/*		  {".//lli//monthlyBillGenerateStatus.jsp", "Status"},
*/          {".//lli//monthpicker.jsp", "Month"}


    };


    public final static String[][] SEARCH_VPN_BILL_GENERATION_CHECK = {

            {".//vpn//clientSearchField.jsp", "clientName"},
/*		  {".//lli//monthlyBillGenerateStatus.jsp", "Status"},
*/          {".//lli//monthpicker.jsp", "Month"}


    };


    public final static String NAV_NIX_CLIENT_REVISE = "nav_nix_client_revise";
    public final static String VIEW_NIX_CLIENT_REVISE = "view_nix_client_revise";


    public final static String[][] SEARCH_NIX_CLIENT_REVISE = {
            {".//lli//fromDate.jsp", ""},
            {".//lli//toDate.jsp", ""},
            {"Client Name", "clientName"},
    };
    //end monthly bill is generated check
    /***
     * IP Usage Search
     */
    public final static String NAV_IP_USAGE = "nav_ip_usage";
    public final static String VIEW_IP_USAGE = "view_ip_usage";
    public final static String[][] SEARCH_IP_USAGE = {
            {"keyValuePairDropdown", "Version_" + NAV_IP_USAGE},
            {".//ip//region-dropdown.jsp", "Region"},
            {"keyValuePairDropdown", "Purpose_" + NAV_IP_USAGE},
            {"keyValuePairDropdown", "Status_" + NAV_IP_USAGE},
            {"From IP ", "fromIP"},
            {"To IP", "toIP"},
            {".//common//genericFromDate.jsp", "fromDate"},
            {".//common//genericToDate.jsp", "toDate"},

    };

    public final static String NAV_IP_INVENTORY = "nav_ip_inventory";
    public final static String VIEW_IP_INVENTORY = "view_ip_inventory";
    public final static String SEARCH_IP_INVENTORY[][] = {
            {"keyValuePairDropdown", "Version_" + NAV_IP_USAGE},
            {"keyValuePairDropdown", "Type_" + NAV_IP_INVENTORY},
            {".//ip//region-dropdown.jsp", "Region"},
            {"IP", "ip"},
    };

    public final static String NAV_EE_OFFICIAL_LETTER = "nav_ee_official_letter";
    public final static String VIEW_EE_OFFICIAL_LETTER = "view_ee_official_letter";
    public final static String SEARCH_EE_OFFICIAL_LETTER[][] = {
            {"keyValuePairDropdown", "Module_" + NAV_EE_OFFICIAL_LETTER},
            {"keyValuePairDropdown", "Official letter type_" + NAV_EE_OFFICIAL_LETTER},
            {".//common//genericFromDate.jsp", "fromDate"},
            {".//common//genericToDate.jsp", "toDate"},
            {"Client Name", "clientId"},

    };


    /*nix search page design*/
    public final static String NAV_NIX_APPLICATION = "nav_nix_application";
    public final static String VIEW_NIX_APPLICATION = "view_nix_application";
    public final static String[][] SEARCH_NIX_APPLICATION = {
            {"Application ID", "applicationID"},
            {"Client", "clientName"},/*{"keyValuePairDropdown","Type_"+NAV_NIX_APPLICATION},
            {"keyValuePairDropdown", "Status_"+NAV_NIX_APPLICATION},*/

            {".//common//genericFromDate.jsp", "application-date-from<Application>"},
            {".//common//genericToDate.jsp", "application-date-to<Application>"}
    };

    /*NIX Connection*/
    public final static String NAV_NIX_CONNECTION = "nav_nix_connection";
    public final static String VIEW_NIX_CONNECTION = "view_nix_connection";
    public final static String[][] SEARCH_NIX_CONNECTION = {
            {"Connection ID", "connectionID"},
            {"Connection Name", "connectionName"},
            {"Client Name", "clientName"},
//            {"keyValuePairDropdown", "Status_"+NAV_NIX_CONNECTION},
    };


    /*lli owner change search */
    public final static String NAV_LLI_OWNER_CHANGE = "nav_lli_owner_change";
    public final static String VIEW_LLI_OWNER_CHANGE = "view_lli_owner_change";
    public final static String[][] SEARCH_LLI_OWNER_CHANGE = {
            {"Application ID", "connectionID"},
           /* {"Connection Name", "connectionName"},*/
            {"Client Name", "clientName"},
    };


//    public final static Map<String, List<KeyValuePair>> keyValuePairDropdown = new HashMap<>();

    private static Map<String, List<KeyValuePair>> keyValuePairDropdown = new ConcurrentHashMap<>();

    public static List<KeyValuePair> getDropdownListByKey(String key) {
        return keyValuePairDropdown.getOrDefault(key, Collections.emptyList());
    }

    public static void mutateDropdownMap(String key, List<KeyValuePair> updatedContent) {
        keyValuePairDropdown.put(key, updatedContent);
    }

    static {

        //change
        UpstreamInventoryService inventoryService = ServiceDAOFactory.getService(UpstreamInventoryService.class);
        GlobalService globalService = ServiceDAOFactory.getService(GlobalService.class);
        Map<UpstreamConstants.INVENTORY_ITEM_TYPE, List<UpstreamInventoryItem>> upstreamInventoryItemsMap = null;
        try {
            upstreamInventoryItemsMap = inventoryService.getAllUpstreamItems();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //purpose of using nav filter is to make the key unique nothing else.
        keyValuePairDropdown.put(NAV_EE_OFFICIAL_LETTER + "_module",
                ModuleConstants.ActiveModuleMap
                        .entrySet()
                        .stream()
                        .map(t -> MethodReferences.newKeyValue_Integer_String.apply(t.getKey(), t.getValue()))
                        .collect(Collectors.toList())

        );

        keyValuePairDropdown.put(NAV_EE_OFFICIAL_LETTER + "_official letter type",
                Arrays.stream(OfficialLetterType.values())
                        .map(t -> MethodReferences.newKeyValueString_String.apply(t.name(), OfficialLetterType.getName(t)))
                        .collect(Collectors.toList())

        );

        keyValuePairDropdown.put(NAV_IP_INVENTORY + "_type",
                Arrays.stream(IPConstants.Type.values())
                        .map(t -> MethodReferences.newKeyValueString_String.apply(t.name(), t.name()))
                        .collect(Collectors.toList())
        );

        keyValuePairDropdown.put(NAV_LLI_CONNECTION + "_status",
                LLIConnectionConstants.connectionStatusMap
                        .entrySet()
                        .stream()
                        .map(t -> MethodReferences.newKeyValue_Integer_String.apply(t.getKey(), t.getValue()))
                        .collect(Collectors.toList())
        );

        keyValuePairDropdown.put(NAV_LLI_APPLICATION + "_type",
                LLIConnectionConstants.applicationTypeNameMap
                        .entrySet()
                        .stream()
                        .map(t -> MethodReferences.newKeyValue_Integer_String.apply(t.getKey(), t.getValue()))
                        .collect(Collectors.toList())

        );

//        keyValuePairDropdown.put(NAV_LLI_APPLICATION + "_status",
//                LLIConnectionConstants.applicationStatusMap
//                        .entrySet()
//                        .stream()
//                        .map(t -> MethodReferences.newKeyValue_Integer_String.apply(t.getKey(), t.getValue()))
//                        .collect(Collectors.toList())
//        );

        keyValuePairDropdown.put(NAV_IP_USAGE + "_version",
                Arrays.stream(IPConstants.Version.values())
                        .map(t -> MethodReferences.newKeyValueString_String.apply(t.name(), t.name()))
                        .collect(Collectors.toList())

        );

        keyValuePairDropdown.put(NAV_IP_USAGE + "_purpose", Arrays.stream(IPConstants.Purpose.values())
                .map(t -> MethodReferences.newKeyValueString_String.apply(t.name(), t.name()))
                .collect(Collectors.toList())
        );

        keyValuePairDropdown.put(NAV_IP_USAGE + "_status", Arrays.stream(IPConstants.Status.values())
                .map(t -> MethodReferences.newKeyValueString_String.apply(t.name(), t.name()))
                .collect(Collectors.toList())
        );

        // currently ok.
        keyValuePairDropdown.put(NAV_COLOCATION_INVENTORY + "_category name", CoLocationConstants.categoryNameMap.entrySet()
                .stream()
                .map(t->MethodReferences.newKeyValue_Integer_String.apply(t.getKey(), t.getValue()))
                .collect(Collectors.toList())
        );

        keyValuePairDropdown.put(NAV_VPN_CLIENT + "_client type", ClientForm.CLIENT_TYPE_STR.entrySet()
            .stream()
                .map(t->MethodReferences.newKeyValue_Integer_String.apply(t.getKey(), t.getValue()))
                .collect(Collectors.toList())

        );

        keyValuePairDropdown.put(NAV_VPN_CLIENT + "_client status", EntityTypeConstant.statusStrMap.entrySet()
            .stream()
                .map(t->MethodReferences.newKeyValue_Integer_String.apply(t.getKey(), t.getValue()))
                .collect(Collectors.toList())
        );

        // change
//        keyValuePairDropdown.put(NAV_VPN_CLIENT + "registration type", RegistrantTypeConstants.RegistrantTypeName().
//            .stream()
//                .map(t->MethodReferences.newKeyValue_Integer_String.apply(t.getKey(), t.getValue()))
//                .collect(Collectors.toList())
//        );

        keyValuePairDropdown.put(NAV_LONG_TERM + "_status", LLILongTermContract.statusMap
                .entrySet()
                .stream()
                .map(t -> MethodReferences.newKeyValue_Integer_String.apply(t.getKey(), t.getValue()))
                .collect(Collectors.toList())
        );

        //change
        try {
            keyValuePairDropdown.put(NAV_UPSTREAM_REQUEST + "_type of bandwidth",
                    upstreamInventoryItemsMap.get(UpstreamConstants.INVENTORY_ITEM_TYPE.TYPE_OF_BW)
                    .stream()
                    .map(x -> new KeyValuePair<>(x.getId(), x.getItemName()))
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            e.printStackTrace();
        }


        //change
        try {
            keyValuePairDropdown.put(NAV_UPSTREAM_REQUEST + "_media", upstreamInventoryItemsMap.get(UpstreamConstants.INVENTORY_ITEM_TYPE.MEDIA)
                    .stream()
                    .map(x -> new KeyValuePair(x.getId(), x.getItemName()))
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //change
        try {
            keyValuePairDropdown.put(NAV_UPSTREAM_REQUEST + "_btcl service location", upstreamInventoryItemsMap.get(UpstreamConstants.INVENTORY_ITEM_TYPE.BTCL_SERVICE_LOCATION)
                    .stream()
                    .map(x -> new KeyValuePair(x.getId(), x.getItemName()))
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //change
        try {
            keyValuePairDropdown.put(NAV_UPSTREAM_REQUEST + "_provider location", upstreamInventoryItemsMap.get(UpstreamConstants.INVENTORY_ITEM_TYPE.PROVIDER_LOCATION)
                    .stream()
                    .map(x -> new KeyValuePair(x.getId(), x.getItemName()))
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            e.printStackTrace();
        }


        //TODO - do it for selected provider
//        try {
//            keyValuePairDropdown.put(NAV_UPSTREAM_REQUEST + "_selected provider", globalService.
//                    getAllObjectListByCondition(UpstreamVendor.class)
//                    .stream()
//                    .map(x ->{
//                        return  new KeyValuePair(x.getId(), x.getVendorName());
//                    })
//                    .collect(Collectors.toList()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        //change
        try {
            keyValuePairDropdown.put(NAV_UPSTREAM_REQUEST + "_selected provider",upstreamInventoryItemsMap.get(UpstreamConstants.INVENTORY_ITEM_TYPE.PROVIDER)
                    .stream()
                    .map(x -> new KeyValuePair(x.getId(), x.getItemName()))
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
