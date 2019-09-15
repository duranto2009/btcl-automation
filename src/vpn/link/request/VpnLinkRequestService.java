package vpn.link.request;

import inventory.InventoryService;
import request.RequestUtilService;

public interface VpnLinkRequestService {
	RequestUtilService requestUtilService = new RequestUtilService();
//	VpnLinkService vpnLinkService = new VpnLinkService();
	InventoryService inventoryService = new InventoryService();
	
	public String getRequestPreviewActionURL(Long rootRequestID) throws Exception;
	
	public Object getRequestDTOByRootRequestID(Long requestID) throws Exception;

	Object getRequestDTOByPrimaryKey(Long requestDTOID) throws Exception;
	
	Object setCommonRequest(Object vpnRequestDTO);
}
