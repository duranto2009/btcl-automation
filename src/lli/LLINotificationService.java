package lli;

import common.ModuleConstants;
import common.RequestFailureException;
import common.UniversalDTOService;
import common.bill.BillDTO;
import common.repository.AllClientRepository;
import mail.MailDTO;
import mail.MailSend;
import mail.MailServerInformationDTO;
import requestMapping.Service;
import util.ServiceDAOFactory;
import vpn.client.ClientDetailsDTO;
import vpn.client.ClientService;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class LLINotificationService {
	@Service
	ClientService clientService;
	
    public static void main (String []args) throws Exception {
	    MailDTO mailDTO = new MailDTO();
	    mailDTO.attachmentPath="C:\\Users\\Acer\\Desktop\\Screenshot_1.png";
	    mailDTO.ccList = null;
	    mailDTO.isHtmlMail = false;
	    mailDTO.mailSubject = "User guide";
	    mailDTO.msgText = "Please Find the Attachment";
	    mailDTO.toList = "mraihan29@gmail.com";
	    MailSend.getInstance().sendMailToParticipients(mailDTO);
//    	updateMailServerInformation();

    	
    }
    private static void updateMailServerInformation() throws Exception {
    	UniversalDTOService universalDTOService = ServiceDAOFactory.getService(UniversalDTOService.class);
    	
    	MailServerInformationDTO mailConfig = getMailServerConfig();
    	
    	universalDTOService.update(mailConfig);
    }
    private static MailServerInformationDTO getMailServerConfig() {
    	MailServerInformationDTO mailConfig = new MailServerInformationDTO();
//    	configuration 1
//    	mailConfig.additionToAddresstxt = "validation@revesoft.com";
//    	mailConfig.authEmailAddesstxt = "validation@revesoft.com";
//    	mailConfig.fromAddresstxt = "validation@revesoft.com";
//    	mailConfig.authEmailPasstxt = "validation$";
//    	mailConfig.mailServerPorttxt = "2525";
//    	mailConfig.mailServertxt = "mail.revesoft.com";
//    	mailConfig.isActive = true;
//    	mailConfig.tlsRequired = false;
//    	mailConfig.authFromServerChk = true;
//		 configuraion 2
    	mailConfig.additionToAddresstxt = "test@revesoft.com";
    	mailConfig.authEmailAddesstxt = "noreply@btcl.com.bd";
    	mailConfig.fromAddresstxt = "noreply@btcl.com.bd";
    	mailConfig.authEmailPasstxt = "%0Nr]@rw?ttI";
    	mailConfig.mailServerPorttxt = "25";
    	mailConfig.mailServertxt = "mail.btcl.com.bd";
    	mailConfig.isActive = true;
    	mailConfig.tlsRequired = false;
    	mailConfig.authFromServerChk = true;
		
    	
    	
    	return mailConfig;
	}
	public void sendMail(BillDTO billDTO, String attachmentFileName) throws Exception {
    	MailDTO mailDTO = createMail(billDTO, attachmentFileName);
    	MailSend.getInstance().sendMailToParticipients(mailDTO);
    }
	private MailDTO createMail(BillDTO billDTO, String attachmentFileName) throws Exception {
		MailDTO mailDTO = new MailDTO();
    	mailDTO.toList = getEmailAddress(billDTO);
    	mailDTO.ccList = null;
    	mailDTO.isHtmlMail = false;
    	mailDTO.mailSubject = "Invoice Generated";
    	mailDTO.msgText = "An Invoice has been generated. Please Find the following attachment for more information.";
    	mailDTO.attachmentPath = getBillFilePath(attachmentFileName);
    	return mailDTO;
	}
	private String getBillFilePath(String attachmentFileName) throws Exception {
		if(attachmentFileName == null || attachmentFileName.isEmpty()) {
			throw new RequestFailureException("Invalid Invoice Document Path");
		}
		File file = new File(attachmentFileName);
		if(!file.exists()) {
			throw new RequestFailureException("Document Does not Exists");
		}
		if( !file.isFile()) {
			throw new RequestFailureException("Invalid Document");
		}
		return attachmentFileName;
	}
	private String getEmailAddress(BillDTO billDTO) throws Exception {
		ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().
				getVpnClientByClientID( billDTO.getClientID(), ModuleConstants.Module_ID_LLI );
		if(clientDetailsDTO == null) {
			throw new RequestFailureException("No Client Details Found with client id "+ billDTO.getClientID());
		}
    	List<ClientContactDetailsDTO> clientContactDetailsDTOs =  clientService.getVpnContactDetailsListByClientID( clientDetailsDTO.getId() );
		
		if(clientContactDetailsDTOs == null || clientContactDetailsDTOs.isEmpty()) {
			throw new RequestFailureException("No Client Contact Details Found with client id "+ billDTO.getClientID());
		}	
		Optional<ClientContactDetailsDTO> optional = clientContactDetailsDTOs.stream().filter(a->a.getDetailsType()==ClientContactDetailsDTO.BILLING_CONTACT).findFirst();
		
		if(!optional.isPresent()) {
			throw new RequestFailureException("No Billing Address Found");
		}
		ClientContactDetailsDTO contactDetailsDTO = optional.get();
		if(contactDetailsDTO.getEmail() == null || contactDetailsDTO.getEmail().isEmpty()) {
			throw new RequestFailureException ("No Email Address found for client id " + billDTO.getClientID());
		}
		return contactDetailsDTO.getEmail();
		//for testing purpose 
		//return "mostafa.khalid@revesoft.com";
//		return "sujan.bhowmik@revesoft.com";
	}
}
