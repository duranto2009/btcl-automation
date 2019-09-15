/**
 * 
 */
package vpn.bill;

import common.bill.BillConstants;
import common.bill.BillDTO;
import util.BillTemplateLocator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alam
 */
public class VpnBillTemplateLocator implements BillTemplateLocator{

	public static Map<Integer, String> templatePath = new HashMap<>();
	
//	static {
//
//		templatePath.put( VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_DEMAND_NOTE , BillConstants.VPN_BILL_TEMPLATE_LOCATION );
//		templatePath.put( VpnRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_GENERATE_DEMAND_NOTE , BillConstants.VPN_BW_CHANGE_DEMAND_NOTE );
//		templatePath.put( VpnRequestTypeConstants.REQUEST_POP_CHANGE.SYSTEM_GENERATE_DEMAND_NOTE , BillConstants.VPN_SHIFTING_DEMAND_NOTE );
//	}
	
	@Override
	public String getTemplateFilename(BillDTO billDTO) {
		
		VpnBillDTO vpnBillDTO = (VpnBillDTO)billDTO;
		
		if( vpnBillDTO.getBillType() == BillConstants.DEMAND_NOTE && templatePath.containsKey( vpnBillDTO.getBillReqType() ) )
			
			return templatePath.get( vpnBillDTO.getBillReqType() );
		else
			
			return BillConstants.VPN_BILL_MONTHLY_TEMPLATE_LOCATION;
	}
}
