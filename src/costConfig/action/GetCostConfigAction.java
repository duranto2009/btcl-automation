package costConfig.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import common.ModuleConstants;
import costConfig.CategoryDTO;
import costConfig.CategoryService;
import costConfig.CostConfigService;
import costConfig.TableDTO;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.Service;

public class GetCostConfigAction extends AnnotatedRequestMappingAction {
	Logger logger = Logger.getLogger(getClass());

	@Service
	CostConfigService costConfigService;
	@Service
	CategoryService categoryService;
	
	public ActionForward execute(ActionMapping p_mapping, ActionForm p_form, HttpServletRequest p_request,
			HttpServletResponse p_response) throws Exception {
		logger.debug("GetCostConfigAction()");
		String target = "success";
		int moduleID = Integer.parseInt(p_request.getParameter("moduleID"));
		int categoryID=-1;
		if(moduleID==ModuleConstants.Module_ID_LLI){
			categoryID = Integer.parseInt(p_request.getParameter("categoryID"));
		}
		switch(moduleID){
			case ModuleConstants.Module_ID_VPN:
				target = getCostConfigForVPN(moduleID, costConfigService, p_request);
				break;
			case ModuleConstants.Module_ID_LLI:
				logger.warn("In lli action");
				target = getCostConfigForLLI(moduleID,costConfigService, categoryService, p_request, categoryID);
				break;
			default:
				target = "failure";
		}
		return p_mapping.findForward(target);
		
	}

/*	private String getCostConfigForLLI(int moduleID, CostConfigService costConfigService,
			HttpServletRequest p_request, int categoryID) {
		try{
			TableDTO tableDTO = null;
			try {
				tableDTO = costConfigService.getLatestTableWithCategoryID(moduleID, categoryID);
			}catch(Exception ex) {
				logger.debug("FATAL", ex);
			}
			int numberofTotalCategories = costConfigService.getTotalCategory(moduleID);
			p_request.setAttribute("totalCategory", numberofTotalCategories);
				
			p_request.setAttribute("table", tableDTO);
			return "successLLI";
		}catch(Exception e){
			logger.debug("fatal", e);
			return "failure";
		}
	}*/

	private String getCostConfigForLLI(
			int moduleID, 
			CostConfigService costConfigService,
			CategoryService categoryService,
			HttpServletRequest p_request, 
			int categoryID) 
	{
		
		try{
			TableDTO tableDTO = null;
			List<CategoryDTO> categoryList = categoryService.getAllCategories();
			p_request.setAttribute("totalCategory", categoryList.size());
			p_request.setAttribute("categoryList",categoryList );
			if(categoryList.size()==0) return "failure";
			try {
				//tableDTO = costConfigService.getLatestTableWithCategoryID(moduleID, categoryList.get(0).getId());
				tableDTO = costConfigService.getLatestTableWithCategoryID(moduleID, categoryID);
			}catch(Exception ex) {
				logger.debug("FATAL", ex);
			}
			
				
			p_request.setAttribute("table", tableDTO);
			return "successLLI";
		}catch(Exception e){
			logger.debug("fatal", e);
			return "failure";
		}
	}

	private String getCostConfigForVPN(
			int moduleID, 
			CostConfigService costConfigService, 
			HttpServletRequest p_request) throws Exception {
		// TODO Auto-generated method stub
		String target = "failure";
		try {
			TableDTO tableDTO = null;
			tableDTO = costConfigService.getLatestTableWithCategoryID(moduleID, 1);
			logger.debug("tableDTO " + tableDTO);
			p_request.setAttribute("table", tableDTO);
			target = "success";

		} catch (Exception e) {
			
	 		p_request.setAttribute("NoSuchCostChartFound", "No cost chart is found");
	 		target = "success";

			
		}
		return target;
	}
}
