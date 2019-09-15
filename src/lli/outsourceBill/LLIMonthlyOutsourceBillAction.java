package lli.outsourceBill;

import annotation.ForwardedAction;
import common.ClientDTO;
import common.repository.AllClientRepository;
import lli.Application.FlowConnectionManager.LLIConnection;
import lli.Application.FlowConnectionManager.LLIFlowConnectionService;
import lli.LLIConnectionService;
import officialLetter.OfficialLetterService;
import officialLetter.OfficialLetterType;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;

import java.util.List;


@ActionRequestMapping("lli/monthly-outsource-bill")
public class LLIMonthlyOutsourceBillAction  extends AnnotatedRequestMappingAction {

    @Service
    LLIFlowConnectionService lliConnectionService;

    @Service
    OfficialLetterService officialLetterService;


    @Service
    LLIMonthlyOutsourceBillByConnectionService lliMonthlyOutsourceBillByConnectionService;
    @Service
    LLIMonthlyOutsourceBillService lliMonthlyOutsourceBillService;

    @ForwardedAction
    @RequestMapping(mapping="/search", requestMethod= RequestMethod.All)
    public String getMonthlyOutSourceBillSummaryPage()
    {
        return  "lli-monthly-outsourcing-bill-page";
    }

    @RequestMapping(mapping="/summary", requestMethod= RequestMethod.GET)
    public LLIMonthlyOutsourceBill getMonthlyOutSourceBillSummary(
            @RequestParameter("vendor")long vendor,
            @RequestParameter("month")int month,
            @RequestParameter("year")int year)throws Exception{

        // TODO: 12/20/2018  we need to take the month and year
        return  lliMonthlyOutsourceBillService.getByVendorIdByMonthByYear(vendor,month,year);
    }

    @RequestMapping(mapping="/details", requestMethod= RequestMethod.GET)
    public List<LLIMonthlyOutsourceBillByConnection> getMonthlyOutSourceBillDetails(
            @RequestParameter("vendor")long vendor,
            @RequestParameter("month")int month,
            @RequestParameter("year")int year)throws Exception{

        LLIMonthlyOutsourceBill lliMonthlyOutsourceBill = lliMonthlyOutsourceBillService.getByVendorIdByMonthByYear(vendor, month, year);
        long outSourceBillId = lliMonthlyOutsourceBill.getId();
        List<LLIMonthlyOutsourceBillByConnection> lliMonthlyOutsourceBillByConnections =
                lliMonthlyOutsourceBillByConnectionService.getByOutsourceBillId(outSourceBillId);


        for(LLIMonthlyOutsourceBillByConnection lliMonthlyOutsourceBillByConnection:lliMonthlyOutsourceBillByConnections){
            long conId = lliMonthlyOutsourceBillByConnection.getConnectionId();
            LLIConnection lliConnection = lliConnectionService.getConnectionByID(conId);

            //get AN & DN
//            lliMonthlyOutsourceBillByConnection.setAdviceNoteNo(officialLetterService.getOfficialLetterByApplicationIdAndLetterType(lliConnection.getID(), OfficialLetterType.ADVICE_NOTE).getId());
//            lliMonthlyOutsourceBillByConnection.setDemandNoteNo(officialLetterService.getOfficialLetterByApplicationIdAndLetterType(lliConnection.getID(), OfficialLetterType.DEMAND_NOTE).getId());
//            lliMonthlyOutsourceBillByConnection.setActiveFrom(lliConnection.getActiveFrom());



            long clientId = lliConnection.getClientID();
            ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(clientId);
            // TODO: 12/20/2018 if any client information needed we will add it from client DTO might need to add fields in the LLIMonthlyOutsourceBillByConnection
            lliMonthlyOutsourceBillByConnection.setClientName(clientDTO.getName());
            lliMonthlyOutsourceBillByConnection.setActiveFrom(lliConnection.getActiveFrom());
        }
        return  lliMonthlyOutsourceBillByConnections;
    }
}
