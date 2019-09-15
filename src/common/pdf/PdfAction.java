package common.pdf;

import accounting.BillPaymentDTOForLedger;
import annotation.JsonPost;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import common.MiscDocumentType;
import common.ModuleConstants;
import common.Month;
import common.RequestFailureException;
import common.bill.BillDTO;
import common.bill.BillService;
import common.bill.MultipleBillMappingDTO;
import common.bill.MultipleBillMappingDTOConditionBuilder;
import global.GlobalService;
import lli.Application.LLIApplicationService;
import lli.monthlyBill.LLIMonthlyBillPdf;
import lli.monthlyBillSummary.LLIMonthlyBillSummaryByClient;
import lli.monthlyBillSummary.LLIMonthlyBillSummaryByItem;
import lli.monthlyBillSummary.LLIMonthlyBillSummaryByItemService;
import lombok.extern.log4j.Log4j;
import nix.monthlybill.NIXMonthlyBillByClient;
import nix.monthlybill.NIXMonthlyBillByClientService;
import nix.monthlybill.NIXMonthlyBillPdf;
import nix.monthlybillsummary.NIXMonthlyBillSummaryByClient;
import nix.monthlybillsummary.NIXMonthlyBillSummaryByItem;
import nix.monthlybillsummary.NIXMonthlyBillSummaryByItemService;
import nix.monthlybillsummary.NIXMonthlyBillSummaryService;
import org.apache.commons.io.IOUtils;
import org.apache.struts.action.ActionForward;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import util.ServiceDAOFactory;
import vpn.monthlyBill.VPNMonthlyBillByClientService;
import vpn.monthlyBill.VPNMonthlyBillPdf;
import vpn.monthlyBillSummary.VPNMonthlyBillSummaryByClient;
import vpn.monthlyBillSummary.VPNMonthlyBillSummaryByItem;
import vpn.monthlyBillSummary.VPNMonthlyBillSummaryByItemService;
import vpn.monthlyBillSummary.VPNMonthlyBillSummaryService;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ActionRequestMapping("pdf/")
@Log4j
public class PdfAction extends AnnotatedRequestMappingAction {

    @Service private PdfService pdfService;
    @Service private LLIApplicationService lliApplicationService;
    @Service private NIXMonthlyBillByClientService nixMonthlyBillByClientService;
    @Service private BillService billService;
    @Service private NIXMonthlyBillSummaryService nixMonthlyBillSummaryService;
    @Service private VPNMonthlyBillByClientService vpnMonthlyBillByClientService;
    @Service private VPNMonthlyBillSummaryService vpnMonthlyBillSummaryService;

    @Service
    GlobalService globalService;

    @RequestMapping(mapping = "view/link/advice-note", requestMethod = RequestMethod.GET)
    public ActionForward viewAdviceNoteVPN(@RequestParameter("linkId")long linkId,
                                           HttpServletResponse response)
            throws  Exception {
        String proposedName = "VPN-advice-note-link-id-" + linkId;

        setPdfSpecificResponseEnvironment(response, proposedName);
        OutputStream outputStream = response.getOutputStream();
        pdfService.viewPDFVpnLinkAN(linkId, outputStream);
        IOUtils.closeQuietly(outputStream);
        return null;

    }

    @RequestMapping(mapping = "view/advice-note", requestMethod = RequestMethod.GET)
    public ActionForward viewAdviceNote(@RequestParameter("appId") long appId,
                                        @RequestParameter("module") int moduleId,
                                        HttpServletResponse response
                                        ) throws Exception {
        String proposedName = ModuleConstants.ActiveModuleMap.get(moduleId) + "-advice-note-app-id-" + appId;
        setPdfSpecificResponseEnvironment(response, proposedName);
        OutputStream outputStream = response.getOutputStream();
        pdfService.viewPDFAN(appId, outputStream, moduleId);
        IOUtils.closeQuietly(outputStream);
        return null;
    }

    @RequestMapping(mapping = "view/demand-note", requestMethod = RequestMethod.GET)
    public ActionForward viewDemandNote(@RequestParameter("billId") long demandNoteId,
                                        HttpServletResponse response) throws Exception {
        setPdfSpecificResponseEnvironment(response, "demand-note-" + demandNoteId);
        OutputStream outputStream = response.getOutputStream();
        pdfService.viewPDFDN(demandNoteId, outputStream);
        IOUtils.closeQuietly(outputStream);
        return null;
    }

    @RequestMapping(mapping = "view/work-order", requestMethod = RequestMethod.GET)
    public ActionForward viewWorkOrder(@RequestParameter("appId") long appId,
                                       @RequestParameter("module") int moduleId,
                                       @RequestParameter("vendorId") long vendorId,
                                       HttpServletResponse response) throws Exception {
        String proposedName = ( moduleId == ModuleConstants.Module_ID_VPN ? "vpn-" :
                (moduleId == ModuleConstants.Module_ID_NIX ? "nix-" :
                        (moduleId == ModuleConstants.Module_ID_LLI ? "lli-": "")
                )
        );
        proposedName+="work-order-" + appId + "-" + vendorId;
        setPdfSpecificResponseEnvironment(response, proposedName);
        OutputStream outputStream = response.getOutputStream();
        pdfService.viewPDFWO(appId, moduleId, vendorId, outputStream);
        IOUtils.closeQuietly(outputStream);
        return null;
    }

    private void setPdfSpecificResponseEnvironment(HttpServletResponse response, String filePrefix) {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=" + filePrefix + ".pdf");
        response.setCharacterEncoding("UTF-8");

    }


    //TODO raihan
    @RequestMapping(mapping = "view/monthly-bill", requestMethod = RequestMethod.GET)
    public ActionForward viewMonthlyBill(@RequestParameter("billId") long billId,
                                         @RequestParameter("module") int module,
                                         HttpServletResponse response) throws Exception {
        BillDTO billDTO = billService.getBillDTOVerified(billId);
        String filePrefix = "";
        PdfMaterial bill = null;
        if(module==ModuleConstants.Module_ID_LLI) {

            LLIMonthlyBillSummaryByClient clientSummary = (LLIMonthlyBillSummaryByClient) billDTO;
            List<LLIMonthlyBillSummaryByItem> list = ServiceDAOFactory.getService(LLIMonthlyBillSummaryByItemService.class)
                    .getListByMonthlyBillSummaryByClientId(clientSummary.getId());
            clientSummary.getLliMonthlyBillSummaryByItems().addAll(list);
            filePrefix = "lli-monthly-bill-" +clientSummary.getClientId() + "-" + Month.getMonthNameById(clientSummary.getMonth());
            bill = new LLIMonthlyBillPdf(clientSummary);
        }else if(module== ModuleConstants.Module_ID_VPN) {
            VPNMonthlyBillSummaryByClient clientSummary = (VPNMonthlyBillSummaryByClient)billDTO;
            List<VPNMonthlyBillSummaryByItem> list = ServiceDAOFactory.getService(VPNMonthlyBillSummaryByItemService.class)
                    .getListByMonthlyBillSummaryByClientId(clientSummary.getId());
            clientSummary.getVpnMonthlyBillSummaryByItems().addAll(list);
            filePrefix = "vpn-monthly-bill-" +clientSummary.getClientId() + "-" + Month.getMonthNameById(clientSummary.getMonth());
            bill = new VPNMonthlyBillPdf(clientSummary);

        }else if(module == ModuleConstants.Module_ID_NIX) {
            NIXMonthlyBillSummaryByClient clientSummary = (NIXMonthlyBillSummaryByClient)billDTO;
            List<NIXMonthlyBillSummaryByItem> list = ServiceDAOFactory.getService(NIXMonthlyBillSummaryByItemService.class)
                    .getListByMonthlyBillSummaryByClientId(clientSummary.getId());
            clientSummary.getNixMonthlyBillSummaryByItems().addAll(list);
            NIXMonthlyBillByClient nixMonthlyBillByClient = nixMonthlyBillByClientService.getByClientIdAndDateRange(clientSummary.getClientId(), clientSummary.getMonth(), clientSummary.getYear());
            filePrefix = "nix-monthly-bill-" +clientSummary.getClientId() + "-" + Month.getMonthNameById(clientSummary.getMonth());
            bill = new NIXMonthlyBillPdf(nixMonthlyBillByClient, clientSummary);

        }
        setPdfSpecificResponseEnvironment(response,  filePrefix);
        OutputStream outputStream = response.getOutputStream();
        if(bill != null) {
            pdfService.viewPDFMonthlyBill(bill, outputStream);
        }else throw new RequestFailureException("Bill Not Formed");

        IOUtils.closeQuietly(outputStream);
        return null;
    }


    public void viewPDF(HttpServletResponse response, String filePath, String fileName) {
        try {
            OutputStream outputStream = response.getOutputStream();
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=" + fileName);
            response.setCharacterEncoding("UTF-8");

//            JasperAPI.getInstance().renderPDFToBrowser(new NIXWorkOrderDocument());

            outputStream.write(Files.readAllBytes(Paths.get(filePath)));
            IOUtils.closeQuietly(outputStream);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @RequestMapping(mapping="view/manual-bill", requestMethod=RequestMethod.GET)
    public ActionForward viewManualBillPDF(@RequestParameter("id") long billId,
                                           HttpServletResponse response) throws Exception {
        OutputStream outputStream = response.getOutputStream();
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=manual-bill-"+billId+".pdf");
        response.setCharacterEncoding("UTF8");
        pdfService.viewPDFDN(billId, outputStream);
        IOUtils.closeQuietly(outputStream);
        return null;
    }

    @RequestMapping(mapping = "view/multiple-bill", requestMethod = RequestMethod.GET)
    public ActionForward viewMultipleBill(@RequestParameter("billId") long billId,
                                         @RequestParameter("module") int module,
                                         @RequestParameter("type") int type,
                                          HttpServletResponse response
                                          ) throws Exception {

        OutputStream outputStream = response.getOutputStream();


        BillDTO billDTO=globalService.findByPK(BillDTO.class,billId);
        List<MultipleBillMappingDTO> multipleBillMappingDTOS=globalService.getAllObjectListByCondition(MultipleBillMappingDTO.class,
                new MultipleBillMappingDTOConditionBuilder()
                .Where()
                .commonBillIdEquals(billDTO.getID())
                .getCondition()

                );

        List<BillDTO> billDTOList=new ArrayList<>();
        for(MultipleBillMappingDTO multipleBillMappingDTO:multipleBillMappingDTOS){
            BillDTO billDTO1=globalService.findByPK(BillDTO.class,multipleBillMappingDTO.getIndividualBillId());
            billDTOList.add(billDTO1);
        }


        MiscDocumentType documentType = MiscDocumentType.getMiscDocumentTypeByTypeId(type);
        JsonObject params=new JsonObject();

        params.add("dueBills", new Gson().toJsonTree( billDTOList));
        params.addProperty("netPayable",billDTO.getNetPayable());
        params.addProperty("amountInWords","");
        params.addProperty("clientId",billDTO.getClientID());
        params.addProperty("lastPaymentDate",new SimpleDateFormat("dd/MM/yyyy").format(new Date(billDTO.getLastPaymentDate())));
        params.addProperty("type",type);
        params.addProperty("moduleId",module);
        params.addProperty("invoiceId",billId);
        params.addProperty("fromDate",billDTO.getActivationTimeFrom());
        params.addProperty("toDate",billDTO.getActivationTimeTo());


        String proposedName = "";
        switch (documentType) {
            case FINAL_BILL:
                proposedName = "final-bill";
                setPdfSpecificResponseEnvironment(response, proposedName);
                pdfService.viewPDFFinalBill(params, outputStream);
                break;

            case MULTIPLE_BILL:

                proposedName = "Multiple-bill";
                setPdfSpecificResponseEnvironment(response, proposedName);
                pdfService.viewPDFMultipleBill(params, outputStream);

        }

        IOUtils.closeQuietly(outputStream);
        return  null;



    }


    @JsonPost
    @RequestMapping(mapping = "view/misc-document", requestMethod = RequestMethod.POST)
    public ActionForward viewMiscDocument(
            @RequestParameter(isJsonBody = true, value = "type")int type,
            @RequestParameter(isJsonBody = true, value = "params")JsonObject params,
            HttpServletResponse response
            ) throws Exception {
        MiscDocumentType documentType = MiscDocumentType.getMiscDocumentTypeByTypeId(type);
        OutputStream outputStream = response.getOutputStream();
        String proposedName = "";
        switch (documentType){
            case REQUEST_LETTER:
                proposedName = "Request-Letter";
                setPdfSpecificResponseEnvironment(response, proposedName);
                pdfService.viewPDFRequestLetter(params, outputStream);
                break;

            case SUBSCRIBER_LEDGER_REPORT:
                long clientId = params.get("clientId").getAsLong();
                List<BillPaymentDTOForLedger> billPaymentDTOs = new Gson().fromJson(
                        params.get("list"),
                        new TypeToken<List<BillPaymentDTOForLedger>>(){}.getType()
                );
                proposedName = "subscriber-ledger-report-client-id"+clientId + ".pdf";
                setPdfSpecificResponseEnvironment(response, proposedName);
                pdfService.viewPDFSubscriberLedgerReport(clientId, billPaymentDTOs, outputStream);
                break;

            case CLEARANCE_CERTIFICATE_WITH_DUE:
                proposedName = "Clearance-certificate-with-due";
                setPdfSpecificResponseEnvironment(response, proposedName);
                pdfService.viewPDFClearanceCertificateWithDues(params, outputStream);
                break;

            case CLEARANCE_CERTIFICATE_NO_DUE:
                proposedName = "Clearance-certificate-no-due";
                setPdfSpecificResponseEnvironment(response, proposedName);
                pdfService.viewPDFClearanceCertificateWithoutDues(params, outputStream);
                break;
            case MULTIPLE_BILL:
                proposedName = "Multiple-bill";
                setPdfSpecificResponseEnvironment(response, proposedName);
                pdfService.viewPDFMultipleBill(params, outputStream);
                break;
            case FINAL_BILL:
                proposedName = "final-bill";
                setPdfSpecificResponseEnvironment(response, proposedName);
                pdfService.viewPDFFinalBill(params, outputStream);
                break;
        }

        IOUtils.closeQuietly(outputStream);
        return  null;
    }



}
