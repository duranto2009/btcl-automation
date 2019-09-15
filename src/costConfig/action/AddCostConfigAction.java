package costConfig.action;

import costConfig.CostConfigService;
import costConfig.form.*;

import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;


import login.LoginDTO;

import sessionmanager.SessionConstants;
import sessionmanager.SessionManager;
import util.*;

public class AddCostConfigAction extends Action {
	Logger logger = Logger.getLogger(getClass());
	public ActionForward execute(ActionMapping p_mapping, ActionForm p_form, HttpServletRequest p_request, HttpServletResponse p_response)
	{
		logger.debug("UpdateCostConfigAction()");
		String target = "success";
		CostConfigForm form = (CostConfigForm)p_form;
		
		
		form.setRowList(new ArrayList<RowDTO>());
		RowDTO rowDTO = new RowDTO();
		rowDTO.setIndex(0);
		rowDTO.setLowerRange(100);
		rowDTO.setUpperRange(120);
		form.getRowList().add(rowDTO);

		rowDTO.setUpperRange(123);
		form.getRowList().add(rowDTO);
		p_request.setAttribute(p_mapping.getAttribute(), form);
		CostConfigService service  = new CostConfigService();
		  DAOResult daoResult;
	        try 
	        {
	            daoResult = service.add(form);
	            if (!daoResult.isSuccessful())
	            {
	                switch (daoResult.getType())
	                {
	                  case DAOResult.DB_EXCEPTION:
	                    target = "failure";
	                    SessionManager.setFailureMessage(p_request, daoResult.getMessage());
	                    break;

	                  case DAOResult.VALIDATION_ERROR:
	                    target = "actionError";
	                    ActionErrors errors = new ActionErrors();

	                   if(daoResult.getMessage().equals("error.test."))
	                                    errors.add("", new ActionMessage("error.testType.DuplicateTestType"));
	                    saveErrors(p_request, errors);
	                    break;
	                }
	            }
	            else
	            {
	                p_request.getSession(true).setAttribute(util.ConformationMessage.TEST_EDIT, "Edited Successfully");
	            }
	        }
	        catch (Exception e) {
	            target = "failure";
	           logger.fatal(e);
	        }
		return p_mapping.findForward(target);
	}

	
}
