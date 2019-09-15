
package costConfig.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import common.ObjectPair;
import common.RequestFailureException;
import costConfig.CostConfigService;
import costConfig.TableDTO;
import costConfig.form.CostConfigForm;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import util.ColumnDTO;


@ActionRequestMapping("CostChart")
public class CostConfigAction extends AnnotatedRequestMappingAction {
	
	@Service
	CostConfigService costConfigService;
	
	@RequestMapping(mapping="/deleteTable",requestMethod=RequestMethod.POST)
	public ObjectPair<String, Boolean> deleteCostChartByTableID(@RequestParameter("tableID") long tableID) throws Exception{
		costConfigService.deleteCostChartByTableID(tableID);
//		throw new RequestFailureException("This is a failure");
		return new ObjectPair<String, Boolean>("success", true);
	}
	
	@RequestMapping(mapping="/edit", requestMethod=RequestMethod.POST) 
	public ActionForward updateCostChart(HttpServletRequest request, HttpServletResponse response, ActionForm form, ActionMapping mapping) throws Exception {
		int moduleID = Integer.parseInt(request.getParameter("moduleID"));
		int categoryID = Integer.parseInt(request.getParameter("categoryID"));
		long prevTableID = Long.parseLong(request.getParameter("tableID"));
		TableDTO tableDTO = costConfigService.createTableDTO((CostConfigForm)(form), moduleID);
		costConfigService.updateCostConfigChartForLLLI(tableDTO, categoryID, prevTableID);
		ActionForward actionForward = new ActionForward();
		actionForward.setPath("/GetCostConfig.do?moduleID="+moduleID+"&categoryID="+categoryID);
		actionForward.setRedirect(true);
		return actionForward;
	} 
}
