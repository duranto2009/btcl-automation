package crm.inventory.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import crm.inventory.CRMInventoryAttributeName;
import crm.inventory.CRMInventoryItemSearchService;
import crm.inventory.repository.CRMInventoryRepository;
import login.LoginDTO;
import sessionmanager.SessionConstants;
import sessionmanager.SessionManager;
import util.RecordNavigationManager;
import vpn.client.ClientService;

public class SearchCRMInventoryItemAction extends Action{
	Logger logger = Logger.getLogger(getClass());
	LoginDTO loginDTO = null;
	  public ActionForward execute(ActionMapping p_mapping,ActionForm p_form,HttpServletRequest p_request,HttpServletResponse p_response)
	  {
	    String target = "success";
		loginDTO = (login.LoginDTO) p_request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
        CRMInventoryItemSearchService service = new CRMInventoryItemSearchService();
        String categoryIDString = p_request.getParameter("categoryID");
        if(categoryIDString==null){
        	categoryIDString = "3";
        }
        int categoryID = Integer.parseInt(categoryIDString);
        SessionConstants.SEARCH_INVENTORY_ITEM=getLabelNamePair(categoryID);
        
        System.out.println(SessionConstants.SEARCH_INVENTORY_ITEM.length);
        
        RecordNavigationManager rnManager = new RecordNavigationManager(
        SessionConstants.NAV_VPN_INVENTORY, p_request, service,
        SessionConstants.VIEW_VPN_INVENTORY,
        SessionConstants.SEARCH_INVENTORY_ITEM);
        
        try
        {
            rnManager.doJob(loginDTO);
        }
        catch (Exception e)
        {
        	logger.debug("Exception ", e);
            target = "failure";
            SessionManager.setFailureMessage(p_request, e.toString());
        }

        return (p_mapping.findForward(target));
	   
	  }
	  private String[][] getLabelNamePair(int categoryID){
		  List<CRMInventoryAttributeName> inventoryAttributeNames =CRMInventoryRepository.getInstance().getCRMInventoryAttributeNameListByCatagoryID(categoryID);
		  System.out.println("inventoryAttributeNames with categoryID = "+categoryID+":"+inventoryAttributeNames);
		  String [][] labelNamePairs= new String[inventoryAttributeNames.size()][2];
		  for(int i=0;i<inventoryAttributeNames.size();i++){
			  labelNamePairs[i][0]=inventoryAttributeNames.get(i).getName();
			  labelNamePairs[i][1]=""+inventoryAttributeNames.get(i).getID();
			  
		  }
		  return labelNamePairs;
	  }
}
