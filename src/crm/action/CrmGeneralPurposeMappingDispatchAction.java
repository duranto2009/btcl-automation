package crm.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.MappingDispatchAction;

import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;

@ActionRequestMapping("CrmAction")
public class CrmGeneralPurposeMappingDispatchAction extends AnnotatedRequestMappingAction {

	@RequestMapping(mapping="/addComplain.do",requestMethod= RequestMethod.GET)
	public ActionForward getComplainForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		return mapping.findForward("view");
	}
	@RequestMapping(mapping="/addComplain.do",requestMethod= RequestMethod.POST)
	public ActionForward submitComplain(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		return mapping.findForward("success");
	}

	public ActionForward editComplain(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		return mapping.findForward("success");
	}

	public ActionForward deleteComplain(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		return mapping.findForward("success");
	}
	
	public ActionForward searchComplain(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		return mapping.findForward("success");
	}
	
	

}
