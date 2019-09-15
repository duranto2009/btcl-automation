package payment.api;

import common.ClientDTO;
import common.EntityTypeConstant;
import common.ModuleConstants;
import common.bill.BillDTO;
import common.repository.AllClientRepository;

public class BillDTOForBankPayment extends BillDTO{
	String bankCode;
	String branchCode;
	long paymentTime;
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public long getPaymentTime() {
		return paymentTime;
	}
	public void setPaymentTime(long paymentTime) {
		this.paymentTime = paymentTime;
	}
	
	public String getServiceType(){
		int moduleID = EntityTypeConstant.entityModuleIDMap.get(getEntityTypeID());
		return ModuleConstants.ModuleMap.get(moduleID);
	}
	
	public String customerUserName(){
		ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(getClientID()); 
		return (clientDTO==null||clientDTO.isDeleted())?"No valid customer found":clientDTO.getLoginName();
	}
	
}
