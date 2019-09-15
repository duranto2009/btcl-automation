package bank.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import bank.BankDTO;
import bank.service.BankService;
import crm.CrmEmployeeDTO;
import login.LoginDTO;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;

@ActionRequestMapping("Bank")
public class BankAction extends AnnotatedRequestMappingAction {

	@Service
	BankService bankService;
	
	@RequestMapping(mapping = "/AddNewBank", requestMethod = RequestMethod.GET)
	public ActionForward getAddBankPage(ActionMapping mapping) throws Exception {
		return mapping.findForward("GetAddBankView");
	}
	
	@RequestMapping(mapping = "/AddNewBank", requestMethod = RequestMethod.POST)
	public ActionForward addNewBank(ActionMapping mapping, HttpServletRequest request, BankDTO bankDTO, LoginDTO loginDTO) throws Exception {
		bankService.insertBank(bankDTO,loginDTO);
		request.setAttribute("BankDTO", bankDTO);
		request.setAttribute("msg", "Add Successfully");
		return mapping.findForward("GetBankView");
	}
	
	@RequestMapping(mapping="/UpdateBank",requestMethod= RequestMethod.POST)
	public ActionForward updateBank(ActionMapping mapping, HttpServletRequest request,BankDTO bankDTO, LoginDTO loginDTO) throws Exception {
		request.setAttribute("BankDTO", bankService.editBank(bankDTO,loginDTO)); 
		request.setAttribute("msg", "Update Successfully");
		return mapping.findForward("GetBankView");
		
	}
	
	@RequestMapping(mapping="/GetBank",requestMethod= RequestMethod.GET)
	public ActionForward getBank(ActionMapping mapping, HttpServletRequest request,@RequestParameter("bankID")long bankID, LoginDTO loginDTO) throws Exception {
		request.setAttribute("BankDTO", bankService.getBankDTOByID(bankID)); 
		return mapping.findForward("GetBankView");
		
	}
	
	@RequestMapping(mapping="/GetBankByID",requestMethod= RequestMethod.GET)
	public BankDTO getBank(@RequestParameter("bankID")long bankID, LoginDTO loginDTO) throws Exception {
		return bankService.getBankDTOByID(bankID); 
	}
	
	@RequestMapping(mapping="/DeleteBank",requestMethod= RequestMethod.GET)
	public ActionForward deleteBank(ActionMapping mapping, HttpServletRequest request,@RequestParameter("bankID")long bankID, LoginDTO loginDTO) throws Exception {
		bankService.deleteBank(bankID, loginDTO);
		request.setAttribute("msg","Deleted successfully");
		return mapping.findForward("BankSearchPage");
		
	}
	
	@RequestMapping(mapping="/Banks",requestMethod= RequestMethod.All)
	public List<BankDTO> getBank(@RequestParameter("payDraftBankName")String partialPayDraftBankName) throws Exception {		
		return bankService.getBankDTOListByBankPartialName(partialPayDraftBankName);	
	}
	
}
