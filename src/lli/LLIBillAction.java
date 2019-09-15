package lli;

import annotation.ForwardedAction;
import annotation.JsonPost;
import common.RequestFailureException;
import common.bill.BillDTO;
import common.bill.BillService;
import lli.monthlyBill.LLIManualBill;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
@ActionRequestMapping("lli/bill/")
public class LLIBillAction extends AnnotatedRequestMappingAction {
	@Service
	BillService billService;
	
	@RequestMapping(mapping="manual", requestMethod=RequestMethod.GET)
    @ForwardedAction
    public String getManualBillPage() {
        return "lli-manual-bill";
    }
	
	@JsonPost
	@RequestMapping(mapping="manual", requestMethod=RequestMethod.POST)
	public void generateManualBill (@RequestParameter(isJsonBody=true, value="bill") LLIManualBill bill) throws Exception {
		billService.generateManualBill(bill);
	}
	
	@RequestMapping(mapping="manual/view", requestMethod=RequestMethod.GET)
    @ForwardedAction
    public String getManualBillViewPage() {
        return "lli-manual-bill-view";
    }
	
	@RequestMapping(mapping="get-bill", requestMethod=RequestMethod.GET)
    public BillDTO getBill(@RequestParameter("id")long billId) throws Exception {
        return billService.getBillDTOVerified(billId);
    }
	@ForwardedAction
	@RequestMapping(mapping="monthly/view", requestMethod=RequestMethod.GET)
	public String getMonthlyBillViewPage() {
		return "lli-monthly-bill-view";
	}

	@RequestMapping(mapping="oc", requestMethod=RequestMethod.GET)
	@ForwardedAction
	public String getOCBillPage() {
		return "lli-oc-bill";
	}
	
}
