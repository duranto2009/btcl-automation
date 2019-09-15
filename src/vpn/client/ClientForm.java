package vpn.client;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import java.util.Arrays;
import java.util.HashMap;

public class ClientForm extends ActionForm {
	private static final long serialVersionUID = -7092303541039846714L;
	public static final int CLIENT_TYPE_INDIVIDUAL=1;
	public static final int CLIENT_TYPE_COMPANY=2;
	public static final HashMap<Integer, String> CLIENT_TYPE_STR= new HashMap<>();
	static{
		CLIENT_TYPE_STR.put(CLIENT_TYPE_INDIVIDUAL, "Individual");
		CLIENT_TYPE_STR.put(CLIENT_TYPE_COMPANY, "Org/Institution/Company");
	}
	
	ClientDetailsDTO clientDetailsDTO = new ClientDetailsDTO();
	ClientContactDetailsDTO registrantContactDetails = new ClientContactDetailsDTO();
	ClientContactDetailsDTO billingContactDetails = new ClientContactDetailsDTO();
	ClientContactDetailsDTO adminContactDetails = new ClientContactDetailsDTO();
	ClientContactDetailsDTO technicalContactDetails = new ClientContactDetailsDTO();
	String[] documents;
	FormFile document;

	public FormFile getDocument() {
		return document;
	}

	public void setDocument(FormFile document) {
		this.document = document;
	}

	public String[] getDocuments() {
		return documents;
	}
	
	public void setDocuments(String[] documents) {
		this.documents = documents;
	}

	String[] dates = new String[3];

	public String[] getDates() {
		return dates;
	}

	public void setDates(String[] dates) {
		this.dates = dates;
	}

	public ClientDetailsDTO getClientDetailsDTO() {
		return clientDetailsDTO;
	}

	public void setClientDetailsDTO(ClientDetailsDTO clientDetailsDTO) {
		this.clientDetailsDTO = clientDetailsDTO;
	}

	public ClientContactDetailsDTO getRegistrantContactDetails() {
		return registrantContactDetails;
	}

	public void setRegistrantContactDetails(ClientContactDetailsDTO registrantContactDetails) {
		this.registrantContactDetails = registrantContactDetails;
	}

	public ClientContactDetailsDTO getBillingContactDetails() {
		return billingContactDetails;
	}

	public void setBillingContactDetails(ClientContactDetailsDTO billingContactDetails) {
		this.billingContactDetails = billingContactDetails;
	}

	public ClientContactDetailsDTO getAdminContactDetails() {
		return adminContactDetails;
	}

	public void setAdminContactDetails(ClientContactDetailsDTO adminContactDetails) {
		this.adminContactDetails = adminContactDetails;
	}

	public ClientContactDetailsDTO getTechnicalContactDetails() {
		return technicalContactDetails;
	}

	public void setTechnicalContactDetails(ClientContactDetailsDTO technicalContactDetails) {
		this.technicalContactDetails = technicalContactDetails;
	}

	@Override
	public String toString() {
		return "ClientForm [clientDetailsDTO=" + clientDetailsDTO + ", registrantContactDetails=" + registrantContactDetails
				+ ", billingContactDetails=" + billingContactDetails + ", adminContactDetails=" + adminContactDetails
				+ ", technicalContactDetails=" + technicalContactDetails + ", documents=" + Arrays.toString(documents)
				+ ", dates=" + Arrays.toString(dates) + "]";
	}
	
}
