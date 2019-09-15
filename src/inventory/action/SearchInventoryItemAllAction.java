package inventory.action;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import common.CategoryConstants;
import inventory.InventoryAttributeName;
import inventory.InventoryCatagoryType;
import inventory.InventoryItemSearchService;
import inventory.repository.InventoryRepository;
import login.LoginDTO;
import sessionmanager.SessionConstants;
import sessionmanager.SessionManager;
import util.RecordNavigationManager;

public class SearchInventoryItemAllAction extends Action{
	Logger logger = Logger.getLogger(SearchInventoryItemAllAction.class);
	LoginDTO loginDTO = null;
	  public ActionForward execute(ActionMapping p_mapping,ActionForm p_form,HttpServletRequest p_request,HttpServletResponse p_response)
	  {
	    String target = "success";
		loginDTO = (login.LoginDTO) p_request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
        InventoryItemSearchService service = new InventoryItemSearchService();
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
        
        p_request.getSession().setAttribute("isUsed", p_request.getParameter("isUsed"));
        
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
		  
		  List<InventoryCatagoryType> pathList = InventoryRepository.getInstance().getInventoryCatagoryTypePathFromRoot(categoryID);
		  List<InventoryAttributeName> inventoryAttributeNames =InventoryRepository.getInstance().getInventoryAttributeNameListByCatagoryID(categoryID);
		  String [][] labelNamePairs= new String[/*inventoryAttributeNames.size()+*/pathList.size()][2];
		  System.out.println("inventoryAttributeNames with categoryID = "+""+categoryID+" attr size: "+inventoryAttributeNames.size()+" toppath size:"+" "+pathList.size());
		  int j=0;
		  for(InventoryCatagoryType pathCatType : pathList){ 
			  labelNamePairs[j][0]=pathCatType.getName();
			  labelNamePairs[j][1]=""+pathCatType.getID();
			  j++;
		  }
		  /*
		  for(int i=0;i<inventoryAttributeNames.size();i++){
			  labelNamePairs[j][0]=inventoryAttributeNames.get(i).getName();
			  labelNamePairs[j][1]=""+inventoryAttributeNames.get(i).getID();
			  j++;
			  
		  }
		  */
		  return labelNamePairs;
	  }
}
