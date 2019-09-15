package vpn.client;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.google.gson.Gson;

public class GetClientFormData extends Action{

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		ClientService clientService = new ClientService();
		int moduleID = Integer.parseInt(request.getParameter("moduleID"));
		long clientID = Long.parseLong(request.getParameter("clientID"));
		HashMap<String, String> clientFormData =  clientService.getFormDataByClientIDandModuleID(moduleID, clientID);
		
		String result = new Gson().toJson(clientFormData);
		response.getWriter().write(result);

		return null;
	}

}
