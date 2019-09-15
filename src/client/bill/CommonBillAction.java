package client.bill;


import accounting.AccountType;
import accounting.AccountingEntryService;
import annotation.ForwardedAction;
import annotation.JsonPost;
import com.google.gson.JsonObject;
import common.bill.BillDTO;
import lli.monthlyBillSummary.LLIMonthlyBillSummaryService;
import org.apache.log4j.Logger;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import sessionmanager.SessionConstants;
import util.KeyValuePair;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ActionRequestMapping("common/bill")
public class CommonBillAction extends AnnotatedRequestMappingAction {

    static Logger logger = Logger.getLogger(CommonBillAction.class);

    @Service
    LLIMonthlyBillSummaryService lliMonthlyBillSummaryService;
    @Service
    CommonBillService commonBillService;

    @Service
    AccountingEntryService accountingEntryService ;




    @RequestMapping(mapping = "/search-final", requestMethod = RequestMethod.All)
    public KeyValuePair<Long,List<BillDTO>> getFinalMonthlyBillView(
            HttpServletRequest request,
            @RequestParameter("id") long clientId,
            @RequestParameter("module") int moduleId,
            @RequestParameter("lastPayDate") long lastDate
    ) throws Exception {

        login.LoginDTO loginDTO = (login.LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);

        if (clientId == -1) {
            logger.debug("clientId is before : " + clientId);
            clientId = loginDTO.getAccountID();
            logger.debug("clientId is after: " + clientId);
        }

        return commonBillService.getFinalMonthlyBill(clientId,moduleId,lastDate);

    }


    @RequestMapping(mapping = "/get-info", requestMethod = RequestMethod.All)
    public KeyValuePair<Boolean,JsonObject> getInfoForFinalMonthlyBillView(
            HttpServletRequest request,
            @RequestParameter("id") long clientId,
            @RequestParameter("module") int moduleId
    ) throws Exception {

        login.LoginDTO loginDTO = (login.LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);

        if (clientId == -1) {
            logger.debug("clientId is before : " + clientId);
            clientId = loginDTO.getAccountID();
            logger.debug("clientId is after : " + clientId);
        }

        return commonBillService.getExistingInfo(clientId,moduleId);

    }


    @RequestMapping(mapping = "/search-multiple", requestMethod = RequestMethod.All)
    // there may be more parameter (like month of the year etc)
    public KeyValuePair<List<BillDTO>, String> getMultipleMonthlyBillView(
            HttpServletRequest request,
            @RequestParameter("id") long clientId,
            @RequestParameter("module") int module,
            @RequestParameter("from") long fromDate,
            @RequestParameter("to") long toDate
    ) throws Exception {

        login.LoginDTO loginDTO = (login.LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);

        if (clientId == -1) {
            logger.debug("clientId is before : " + clientId);
            clientId = loginDTO.getAccountID();
            logger.debug("clientId is after: " + clientId);
        }
        return commonBillService.getMultipleMonthlyBill(clientId,module,fromDate,toDate);

    }

    @JsonPost
    @RequestMapping(mapping = "/generate-bill",requestMethod = RequestMethod.POST)
    public KeyValuePair<Long,List<BillDTO>> generateBill(@RequestParameter(isJsonBody = true, value = "data")List<BillDTO> billDTOS,
                                                         @RequestParameter(isJsonBody =true,value = "lastPayDate") long lastPayDate,
                                                         @RequestParameter(isJsonBody = true, value = "from") long fromDate,
                                                         @RequestParameter(isJsonBody = true, value = "to") long toDate
    ) throws Exception {

        KeyValuePair<Long,List<BillDTO>> billDTOS1=commonBillService.generateMultipleBill(billDTOS,lastPayDate,fromDate,toDate);
        return billDTOS1;

    }

//    @JsonPost
//    @RequestMapping(mapping = "/generate-final-bill",requestMethod = RequestMethod.POST)
//    public void generateFinalBill(@RequestParameter(isJsonBody = true, value = "data")List<BillDTO> billDTOS,
//                             @RequestParameter(isJsonBody =true,value = "lastPayDate") long lastPayDate
//    ) throws Exception {
//
//        commonBillService.generateFinalBill(billDTOS,lastPayDate);
//
//    }






    @RequestMapping(mapping = "/convert-total", requestMethod = RequestMethod.All)
    // there may be more parameter (like month of the year etc)
    public String getMultipleMonthlyBillView(
            HttpServletRequest request,
            @RequestParameter("amount") double grandTotal,
            @RequestParameter("final") int finalBill,
            @RequestParameter("client") long clientId

    ) throws Exception {
        double convertAmount;
        double securityMoney=0;
        if(finalBill==1){
             securityMoney = (accountingEntryService
                    .getBalanceByClientIDAndAccountID(clientId
                            , AccountType.SECURITY.getID()));

        }

        String NumToWords = commonBillService.convertTotalToWords(grandTotal-securityMoney);
        return NumToWords;
    }


    @RequestMapping(mapping = "/get-security-amount", requestMethod = RequestMethod.All)
    // there may be more parameter (like month of the year etc)
    public double getMultipleMonthlyBillView(
            HttpServletRequest request,
            @RequestParameter("clientId") long clientId

    ) throws Exception {

        double securityMoney = (accountingEntryService
                .getBalanceByClientIDAndAccountID(clientId
                        , AccountType.SECURITY.getID()));

        return securityMoney;


    }



    @ForwardedAction
    @RequestMapping(mapping = "/searchMultiplePage", requestMethod = RequestMethod.GET)
    public String getMultipleMonthlyBillSummarySearch() throws Exception {
        return "multiple-monthly-bill-summary";
    }

    @ForwardedAction
    @RequestMapping(mapping = "/searchFinalPage", requestMethod = RequestMethod.GET)
    public String getFinalMonthlyBillSummarySearch() throws Exception {
        return "final-monthly-bill-summary";
    }


}
