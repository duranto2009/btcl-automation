package lli.instruction;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;

@ActionRequestMapping("LLI/Instruction/")
public class LliInstructionAction extends AnnotatedRequestMappingAction{

	@RequestMapping(mapping="Payment", requestMethod=RequestMethod.All)
	public ActionForward getPayment(ActionMapping mapping) throws Exception{
		return mapping.findForward("payment");
	}
	
}
