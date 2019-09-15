package report;

import common.ClientDTO;
import common.bill.BillDTO;
import common.payment.PaymentDTO;
import crm.CrmCommonPoolDTO;
import crm.CrmComplainDTO;
import crm.CrmEmployeeDTO;
import crm.inventory.CRMInventoryItem;
import lli.LLIConnectionInstance;
import permission.ActionStateDTO;
import request.CommonRequestDTO;
import vpn.client.ClientDetailsDTO;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import java.util.HashMap;
import java.util.Map;

public class ReportHelper {
	public Map<String, Class> classMap;
	public String tableJoinSql;

	
	public static ReportHelper getDomainReportHelper(){
		ReportHelper instance = new ReportHelper();
		instance.classMap = new HashMap<String, Class>() {
			{
				

				put("ClientDTO", ClientDTO.class);
				put("ClientDetailsDTO", ClientDetailsDTO.class);
				put("ClientContactDetailsDTO", ClientContactDetailsDTO.class);
			}
		};
		
		instance.tableJoinSql = " at_domain A INNER JOIN at_client B ON "
				+ "( A.domDomainClientID = B.clID AND A.domIsDeleted = 0 AND B.clIsDeleted = 0 ) INNER JOIN at_client_details C ON "
				+ "( B.clID = C.vclClientID AND C.vclIsDeleted = 0 AND C.vclModuleID = 1 ) INNER JOIN  at_client_contact_details D ON "
				+ "( C.vclID = D.vclcVpnClientID AND D.vclcIsDeleted = 0 AND D.vclcDetailsType = 0 )";
		
		return instance;
	}
	
	public static ReportHelper getColocationReportHelper() {
		ReportHelper instance = new ReportHelper();
		instance.classMap = new HashMap<String, Class>() {
			{
				
//				put("ColocationDTO", ColocationDTO.class);
				put("ClientDTO", ClientDTO.class);
				put("ClientDetailsDTO", ClientDetailsDTO.class);
				put("ClientContactDetailsDTO", ClientContactDetailsDTO.class);
			}
		};
		
		instance.tableJoinSql = " at_colocation A INNER JOIN at_client B ON "
				+ "( A.colocClientID = B.clID AND A.colocIsDeleted = 0 AND B.clIsDeleted = 0 ) INNER JOIN at_client_details C ON "
				+ "( B.clID = C.vclClientID AND C.vclIsDeleted = 0 AND C.vclModuleID = 1 ) INNER JOIN  at_client_contact_details D ON "
				+ "( C.vclID = D.vclcVpnClientID AND D.vclcIsDeleted = 0 AND D.vclcDetailsType = 0 )";
		
		return instance;
	}
	
	public static ReportHelper getCRMReportHelper(){
		ReportHelper instance = new ReportHelper();
		instance.classMap = new HashMap<String, Class>() {
			{
				
				put("CrmComplainDTO", CrmComplainDTO.class);
				put("CrmCommonPoolDTO", CrmCommonPoolDTO.class);
				put("CrmEmployeeDTO", CrmEmployeeDTO.class);
				put("CRMInventoryItem", CRMInventoryItem.class);
				put("ClientDTO", ClientDTO.class);
				put("ClientDetailsDTO", ClientDetailsDTO.class);
				put("ClientContactDetailsDTO", ClientContactDetailsDTO.class);
			}
		};
		
		instance.tableJoinSql = " at_crm_common_pool A JOIN at_client B ON "
				+ "( A.clientID = B.clID AND A.isDeleted = 0 AND B.clIsDeleted = 0 ) JOIN at_client_details C ON "
				+ "( B.clID = C.vclClientID AND C.vclIsDeleted = 0 AND FLOOR(A.entityTypeID/100) = C.vclModuleID) JOIN  at_client_contact_details D ON "
				+ "( C.vclID = D.vclcVpnClientID AND D.vclcIsDeleted = 0 AND D.vclcDetailsType = 0 ) JOIN at_crm_employee E ON"
				+ "( A.nocEmployeeID = E.crmEmployeeID AND E.isDeleted = 0 ) JOIN at_crm_inventory_item F ON"
				+ "( E.inventoryItemID = F.invitID AND F.invitIsDeleted = 0)";
		
		return instance;
	}
	
	public static ReportHelper getVPNReportHelper(){
		ReportHelper instance = new ReportHelper();
		instance.classMap = new HashMap<String, Class>() {
			{
				
//				put("VpnLinkDTO", VpnLinkDTO.class);
				put("ClientDTO", ClientDTO.class);
				put("ClientDetailsDTO", ClientDetailsDTO.class);
				put("ClientContactDetailsDTO", ClientContactDetailsDTO.class);
			}
		};
		
		instance.tableJoinSql = " at_vpn_link A JOIN at_client B ON "
				+ "( A.vlkClientID = B.clID AND A.vlkIsDeleted = 0 AND B.clIsDeleted = 0 ) JOIN at_client_details C ON "
				+ "( B.clID = C.vclClientID AND C.vclIsDeleted = 0 AND C.vclModuleID = 6) JOIN  at_client_contact_details D ON "
				+ "( C.vclID = D.vclcVpnClientID AND D.vclcIsDeleted = 0 AND D.vclcDetailsType = 0 )";
		
		return instance;
	}
	
	public static ReportHelper getLLIReportHelper(){
		ReportHelper instance = new ReportHelper();
		instance.classMap = new HashMap<String, Class>() {
			{
				
				put("LLIConnectionInstance", LLIConnectionInstance.class);
				put("ClientDTO", ClientDTO.class);
				put("ClientDetailsDTO", ClientDetailsDTO.class);
				put("ClientContactDetailsDTO", ClientContactDetailsDTO.class);
			}
		};
		
		/*instance.tableJoinSql = " at_lli_link A JOIN at_client B ON "
				+ "( A.clientID = B.clID AND A.isDeleted = 0 AND B.clIsDeleted = 0 ) JOIN at_client_details C ON "
				+ "( B.clID = C.vclClientID AND C.vclIsDeleted = 0 AND C.vclModuleID = 7) JOIN  at_client_contact_details D ON "
				+ "( C.vclID = D.vclcVpnClientID AND D.vclcIsDeleted = 0 AND D.vclcDetailsType = 0 )";*/
		
		instance.tableJoinSql = " at_lli_connection A JOIN at_client B ON "
				+ "( A.clientID = B.clID AND A.activeTo = " + Long.MAX_VALUE +" AND A.validTo = " + Long.MAX_VALUE+ " AND B.clIsDeleted = 0 ) JOIN at_client_details C ON "
				+ "( B.clID = C.vclClientID AND C.vclIsDeleted = 0 AND C.vclModuleID = 7) JOIN  at_client_contact_details D ON "
				+ "( C.vclID = D.vclcVpnClientID AND D.vclcIsDeleted = 0 AND D.vclcDetailsType = 0 )";
		
		return instance;
	}

	 public static ReportHelper getLogHelper() {
		ReportHelper instance = new ReportHelper();
		instance.classMap = new HashMap<String, Class>() {
			{	
				put("CommonRequestDTO", CommonRequestDTO.class);
				put("ActionStateDTO", ActionStateDTO.class);
				put("ClientDTO", ClientDTO.class);

			}
		};
		
		instance.tableJoinSql = " at_req A JOIN at_action_state B ON"
				+ "(A.arRequestTypeID = B.asActionTypeID ) JOIN at_client C ON"
				+ "(A.arClientID = C.clID)";
		
		return instance;
	}

	public static ReportHelper getPaymentHelper() {
		ReportHelper instance = new ReportHelper();
		instance.classMap = new HashMap<String, Class>() {
			{	
				put("PaymentDTO", PaymentDTO.class);

			}
		};
		
		instance.tableJoinSql = " at_payment ";
		
		return instance;
	}

	public static ReportHelper getClientReportHelperByModuleID(Integer moduleID) {
		ReportHelper instance = new ReportHelper();
		instance.classMap = new HashMap<String, Class>() {
			{
				put("ClientDTO", ClientDTO.class);
				put("ClientDetailsDTO", ClientDetailsDTO.class);
				put("ClientContactDetailsDTO", ClientContactDetailsDTO.class);
			}
		};
		
		instance.tableJoinSql = " at_client B INNER JOIN at_client_details C ON "
				+ "( B.clID = C.vclClientID AND C.vclIsDeleted = 0 AND C.vclModuleID = "+moduleID+" ) INNER JOIN  at_client_contact_details D ON "
				+ "( C.vclID = D.vclcVpnClientID AND D.vclcIsDeleted = 0 AND D.vclcDetailsType = 0 )";
		
		return instance;
	}

	public static ReportHelper getBillReportHelperByModuleID(Integer moduleID) {
		ReportHelper instance = new ReportHelper();
		instance.classMap = new HashMap<String, Class>() {
			{
				
				put("BillDTO", BillDTO.class);
				put("ClientDTO", ClientDTO.class);
				put("ClientDetailsDTO", ClientDetailsDTO.class);
				put("ClientContactDetailsDTO", ClientContactDetailsDTO.class);
			}
		};
		
		instance.tableJoinSql = " at_bill A JOIN at_client B ON "
				+ "( A.blClientID = B.clID AND FLOOR(A.blEntityTypeID/100) = "+moduleID+" AND A.blIsDeleted = 0 AND B.clIsDeleted = 0 ) JOIN at_client_details C ON "
				+ "( B.clID = C.vclClientID AND C.vclIsDeleted = 0 AND C.vclModuleID = "+moduleID+") JOIN  at_client_contact_details D ON "
				+ "( C.vclID = D.vclcVpnClientID AND D.vclcIsDeleted = 0 AND D.vclcDetailsType = 0 )";
		
		return instance;
	}

}
