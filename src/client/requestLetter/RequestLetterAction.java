package client.requestLetter;

import annotation.ForwardedAction;
import common.bill.BillCheckService;
import global.GlobalService;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import util.EnglishNumberToWords;
import util.KeyValuePair;
import util.UtilService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@ActionRequestMapping("request-letter")
public class RequestLetterAction extends AnnotatedRequestMappingAction {

    @Service
    private GlobalService globalService;

    @Service
    private BillCheckService billCheckService;


    @ForwardedAction
    @RequestMapping(mapping = "/get-search-page", requestMethod = RequestMethod.GET)
    // It will load the search page only
    public String getRequestLetterSearchPage() {
        return "request-letter-search";
    }

    @RequestMapping(mapping = "/search", requestMethod = RequestMethod.All)
    public List<KeyValuePair<Integer, KeyValuePair<Integer, Double>>>
    getAllDueBillsOfClient(HttpServletRequest request,
                           HttpServletResponse response,
                           @RequestParameter("clientId") long clientId,
                           @RequestParameter("fromDate") long fromDate,
                           @RequestParameter("toDate") long toDate,
                           @RequestParameter("moduleId") int moduleId) throws Exception {

        System.out.println(clientId + " " + fromDate + " " + toDate + " " + moduleId);
        return billCheckService.getMonthWiseDues(clientId, fromDate, toDate, moduleId);

    }

    @RequestMapping(mapping = "/get-amount-in-words", requestMethod = RequestMethod.GET)
    public String getAmountInWords(
            @RequestParameter("amount")double amount){

        String numberStr = Double.toString(UtilService.round(amount, 2));
        String fractionalStr = numberStr.substring(numberStr.indexOf('.')+1);
        long fractional = Long.valueOf(fractionalStr);
        String numToWords= EnglishNumberToWords.convert((long) amount)+ " Taka " +
                EnglishNumberToWords.convert(fractional) + " Paisa Only ";

        return numToWords;

    }
}
