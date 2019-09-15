package crm.inventory.action;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import common.CategoryConstants;
import crm.inventory.CRMInventoryAttributeName;
import crm.inventory.CRMInventoryCatagoryType;
import crm.inventory.CRMInventoryItemSearchService;
import crm.inventory.repository.CRMInventoryRepository;
import login.LoginDTO;
import sessionmanager.SessionConstants;
import sessionmanager.SessionManager;
import util.RecordNavigationManager;
import vpn.client.ClientService;

public class SearchInventoryItemAllAction extends Action{
	Logger logger = Logger.getLogger(SearchInventoryItemAllAction.class);
	LoginDTO loginDTO = null;
	  public ActionForward execute(ActionMapping p_mapping,ActionForm p_form,HttpServletRequest p_request,HttpServletResponse p_response)
	  {
	    String target = "success";
		loginDTO = (login.LoginDTO) p_request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
        CRMInventoryItemSearchService service = new CRMInventoryItemSearchService();
        String categoryIDString = p_request.getParameter("categoryID");
        if(StringUtils.isBlank(categoryIDString)){
        	categoryIDString = CategoryConstants.CATEGORY_ID_DISTRICT+"";
        }
        int categoryID = Integer.parseInt(categoryIDString);
        SessionConstants.SEARCH_INVENTORY_ITEM=getLabelNamePair(categoryID);
        service.setCategoryID(categoryID);
        p_request.getSession(true).setAttribute("categoryID", categoryIDString);
        
        logger.debug(categoryIDString);
        logger.debug("category id"+categoryID);
        logger.info(Arrays.toString(SessionConstants.SEARCH_INVENTORY_ITEM));
        
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

        if( p_request.getParameter( "moduleID" ) != null ) {
        	
        	p_request.setAttribute( "moduleID", p_request.getParameter( "moduleID" ) );
        }
        
        return (p_mapping.findForward(target));
	   
	  }
	  private String[][] getLabelNamePair(int categoryID){
		  
		  List<CRMInventoryCatagoryType> pathList = CRMInventoryRepository.getInstance().getCRMInventoryCatagoryTypePathFromRoot(categoryID);
		  List<CRMInventoryAttributeName> inventoryAttributeNames =CRMInventoryRepository.getInstance().getCRMInventoryAttributeNameListByCatagoryID(categoryID);
		  String [][] labelNamePairs= new String[inventoryAttributeNames.size()/*+pathList.size()*/][2];
		  System.out.println("inventoryAttributeNames with categoryID = "+""+categoryID+" attr size: "+inventoryAttributeNames.size()+" toppath size:"+" "+pathList.size());
		  int j=0;
		  for(CRMInventoryCatagoryType pathCatType : pathList){ 
			  labelNamePairs[j][0]=pathCatType.getName();
			  labelNamePairs[j][1]=""+pathCatType.getID();
			  j++;
		  }
		  return labelNamePairs;
	  }
}
