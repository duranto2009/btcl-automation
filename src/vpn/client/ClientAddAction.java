package vpn.client;


import org.apache.log4j.Logger;

import client.ClientService;
import login.LoginDTO;
import request.RequestDAO;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.annotation.ActionRequestMapping;
import util.ServiceDAOFactory;

@ActionRequestMapping("Client")
public class ClientAddAction extends AnnotatedRequestMappingAction {
	LoginDTO loginDTO = null;
	Logger logger = Logger.getLogger(ClientAddAction.class);
	ClientAddService clientAddService = new ClientAddService();
	ClientService clientService = ServiceDAOFactory.getService(ClientService.class);
	
	RequestDAO requestDAO = new RequestDAO();

	

}
