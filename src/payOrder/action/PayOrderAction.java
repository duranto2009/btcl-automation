package payOrder.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import bank.BankDTO;
import bank.service.BankService;
import common.ClientDTO;
import common.repository.AllClientRepository;
import payOrder.PayOrderDTO;
import payOrder.service.PayOrderService;
import login.LoginDTO;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import util.TimeConverter;

@ActionRequestMapping("PayOrder")
public class PayOrderAction extends AnnotatedRequestMappingAction {

	@Service
	PayOrderService payOrderService;
	@Service
	BankService bankService;
	@RequestMapping(mapping = "/AddNewPayOrder", requestMethod = RequestMethod.GET)
	public ActionForward getAddPayOrderPage(ActionMapping mapping) throws Exception {
		return mapping.findForward("GetAddPayOrderView");
	}
	
	@RequestMapping(mapping = "/AddNewPayOrder", requestMethod = RequestMethod.POST)
	public ActionForward addNewPayOrder(ActionMapping mapping, HttpServletRequest request, PayOrderDTO payOrderDTO,@RequestParameter("paymentDateStr")String paymentDateStr, LoginDTO loginDTO) throws Exception {
		payOrderDTO.setPayDraftPaymentDate(TimeConverter.getTimeFromString(paymentDateStr));
		payOrderService.insertPayOrder(payOrderDTO,loginDTO);
		request.setAttribute("PayOrderDTO", payOrderDTO);
		request.setAttribute("msg", "Add Successfully");
		BankDTO bankDTO = bankService.getBankDTOByID(payOrderDTO.getPayDraftBankID());
		ClientDTO clientDTO =  AllClientRepository.getInstance().getClientByClientID(payOrderDTO.getPayDraftClientID());

		request.setAttribute("BankName", bankDTO.getBankName()); 
		request.setAttribute("ClientName", clientDTO.getLoginName()); 
		return mapping.findForward("GetPayOrderView");
	}
	
	@RequestMapping(mapping="/UpdatePayOrder",requestMethod= RequestMethod.POST)
	public ActionForward updatePayOrder(ActionMapping mapping, HttpServletRequest request,PayOrderDTO payOrderDTO, LoginDTO loginDTO) throws Exception {
		request.setAttribute("PayOrderDTO", payOrderService.editPayOrder(payOrderDTO,loginDTO)); 
		request.setAttribute("msg", "Update Successfully");
		BankDTO bankDTO = bankService.getBankDTOByID(payOrderDTO.getPayDraftBankID());
		ClientDTO clientDTO =  AllClientRepository.getInstance().getClientByClientID(payOrderDTO.getPayDraftClientID());

		request.setAttribute("BankName", bankDTO.getBankName()); 
		request.setAttribute("ClientName", clientDTO.getLoginName()); 
		return mapping.findForward("GetPayOrderView");
		
	}
	
	@RequestMapping(mapping="/GetPayOrder",requestMethod= RequestMethod.GET)
	public ActionForward getPayOrder(ActionMapping mapping, HttpServletRequest request,@RequestParameter("payOrderID")long payOrderID, LoginDTO loginDTO) throws Exception {
		PayOrderDTO payOrderDTO = payOrderService.getPayOrderDTOByID(payOrderID);
		
		BankDTO bankDTO = bankService.getBankDTOByID(payOrderDTO.getPayDraftBankID());
		ClientDTO clientDTO =  AllClientRepository.getInstance().getClientByClientID(payOrderDTO.getPayDraftClientID());
		
		request.setAttribute("PayOrderDTO", payOrderDTO); 
		request.setAttribute("BankName", bankDTO.getBankName()); 
		request.setAttribute("ClientName", clientDTO.getLoginName()); 
		return mapping.findForward("GetPayOrderView");
		
	}
	
	@RequestMapping(mapping="/DeletePayOrder",requestMethod= RequestMethod.GET)
	public ActionForward deletePayOrder(ActionMapping mapping, HttpServletRequest request,@RequestParameter("payOrderID")long payOrderID, LoginDTO loginDTO) throws Exception {
		payOrderService.deletePayOrder(payOrderID, loginDTO);
		request.setAttribute("msg","Deleted successfully");
		return mapping.findForward("PayOrderSearchPage");
		
	}
	
	//GetPayOrderListByPayorderTypeAndPartialMatch
	@RequestMapping(mapping="/GetPayOrderListByPayorderTypeAndPartialMatch",requestMethod= RequestMethod.All)
	public List<PayOrderDTO> getPayOrderListByPayorderTypeAndPartialMatch(@RequestParameter("payDraftType")int payDraftType,@RequestParameter("payDraftNumber")String partialPayDraftNumber) throws Exception{
		return payOrderService.getPayOrderListByPayorderTypeAndPartialMatch(payDraftType,partialPayDraftNumber);
		//return null;
	}
	
}
