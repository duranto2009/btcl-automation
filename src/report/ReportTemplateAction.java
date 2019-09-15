package report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.google.gson.Gson;

public class ReportTemplateAction extends Action {
	Logger logger = Logger.getLogger(getClass());
	ReportTemplateService reportTemplateService = new ReportTemplateService();

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("ReportTemplateAction");
		String mode=request.getParameter("mode");
		
		if("saveTemplate".equals(mode)){
			saveTemplate(mapping, form, request, response);
		} else if("loadTemplate".equals(mode)){
			loadTemplate(mapping, form, request, response);
		} else if("loadTemplateList".equals(mode)){
			loadTemplateList(mapping, form, request, response);
		}
		
		return null;
	}

	private void loadTemplateList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		List<HashMap<String, String>> allReportTemplateIdName = reportTemplateService.getAllReportTemplateIdName();
		
		String listOfMap = new Gson().toJson(allReportTemplateIdName);		
		try {
			response.getWriter().write(listOfMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadTemplate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String reportTemplateID = request.getParameter("reportTemplateID");
		ReportTemplateDTO reportTemplateDTO = reportTemplateService.getReportTemplateDTOByReportTemplateID(reportTemplateID);
		
		String mapToString = new Gson().toJson(reportTemplateDTO);		
		try {
			response.getWriter().write(mapToString);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void saveTemplate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){

		String reportTemplateName = request.getParameter("reportTemplateName");
		
		List<String> parameterNames = new ArrayList<String>(request.getParameterMap().keySet());
		
		List<String> criteriaKeys = new ArrayList<String>();
		List<String> displayKeyValuePairs = new ArrayList<String>();
		List<String> orderValues = new ArrayList<String>();
		boolean isOrderValuesAdded = false;
		
		for(int i=0;i<parameterNames.size();i++){
			if(parameterNames.get(i).contains("criteria")){
				criteriaKeys.add(parameterNames.get(i));
			}else if(parameterNames.get(i).contains("display")){
				displayKeyValuePairs.add(parameterNames.get(i)+"="+request.getParameter(parameterNames.get(i)));
			}else if(parameterNames.get(i).contains("orderByColumns")){
				if(!isOrderValuesAdded){
					orderValues = Arrays.asList(request.getParameterValues("orderByColumns"));
					isOrderValuesAdded = true;
				}
			}
		}
		
		reportTemplateService.saveReportTemplate(reportTemplateName, criteriaKeys, displayKeyValuePairs, orderValues);
		
		Map<String, String> responseMap = new HashMap<String, String>(); 
		responseMap.put("success", "true");
		
		String mapToString = new Gson().toJson(responseMap);		
		try {
			response.getWriter().write(mapToString);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
