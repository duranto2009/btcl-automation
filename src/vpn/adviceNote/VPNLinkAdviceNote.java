package vpn.adviceNote;

import api.ClientAPI;
import api.FileAPI;
import application.ApplicationType;
import client.classification.ClientClassificationService;
import com.google.gson.JsonArray;
import common.ModuleConstants;
import common.bill.BillConstants;
import common.pdf.PdfMaterial;
import common.pdf.PdfParameter;
import exception.NoDataFoundException;
import file.FileTypeConstants;
import lombok.Builder;
import lombok.Setter;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import officialLetter.OfficialLetter;
import officialLetter.OfficialLetterType;
import officialLetter.RecipientElement;
import util.KeyValuePair;
import util.ServiceDAOFactory;
import vpn.application.VPNApplication;
import vpn.client.ClientDetailsDTO;
import vpn.clientContactDetails.ClientContactDetailsDTO;
import vpn.network.VPNNetworkLink;
import vpn.network.VPNNetworkLinkService;

import java.util.*;

public class VPNLinkAdviceNote extends OfficialLetter implements PdfMaterial {

    private VPNApplication vpnApplication;
    private List<RecipientElement> allRecipient; // TODO redundant

    public VPNLinkAdviceNote(VPNApplication vpnApplication) {
        // FOR View Purpose
        this.vpnApplication = vpnApplication;
        setClientId(vpnApplication.getClientId());
        setClassName(VPNLinkAdviceNote.class.getCanonicalName());
        setDeleted(false);
        setLastModificationTime(System.currentTimeMillis());
        setCreationTime(System.currentTimeMillis());
        setOfficialLetterType(OfficialLetterType.ADVICE_NOTE);
        setModuleId(ModuleConstants.Module_ID_VPN);
        setApplicationId(vpnApplication.getApplicationId());
    }

    @Override
    public Map<String, Object> getPdfParameters() throws Exception {
        Map<String, Object> params = new HashMap<>();
        // Common Part
        PdfParameter.populateAdviceNoteTopInfo(params, this, vpnApplication.getApplicationType().getApplicationTypeName(),false);
        PdfParameter.populateClientInfoForAdviceNote(params, this, ModuleConstants.Module_ID_VPN);
        PdfParameter.populateOfficialLetterInfo(params, this);
        PdfParameter.populateVPNAdviceNoteTopInfo(params);

        // Specific Part
        PdfParameter.populateVPNSpecificAdviceNote(params, vpnApplication.getVpnApplicationLinks());
        // owner change
        
        params.put("ownerChange", false);
        if(vpnApplication.getApplicationType() == ApplicationType.VPN_OWNER_CHANGE) {
            params.put("ownerChange", true);

            KeyValuePair<ClientDetailsDTO, ClientContactDetailsDTO> pair =
                    ClientAPI.getInstance().getPairOfClientDetailsAndClientContactDetails(
                            vpnApplication.getSecondClient(), moduleId, ClientContactDetailsDTO.BILLING_CONTACT);
            ClientDetailsDTO clientDetailsDTO = pair.key;
            ClientContactDetailsDTO contactDetailsDTO = pair.value;
            params.put("client_full_name_dest", contactDetailsDTO.getRegistrantsName() + contactDetailsDTO.getRegistrantsLastName());
            params.put("client_user_name_dest", clientDetailsDTO.getLoginName());
            params.put("client_mobile_dest", contactDetailsDTO.getPhoneNumber());
            params.put("client_billing_address_dest", contactDetailsDTO.getAddress());
            //TODO
            ClientClassificationService clientClassificationService = ServiceDAOFactory.getService(ClientClassificationService.class);

            params.put("client_type_dest", clientClassificationService.getClientTypeById((long) clientDetailsDTO.getRegistrantType()));
            params.put("client_category_dest", clientClassificationService.getClientCategoryById(clientDetailsDTO.getRegistrantCategory()));
            params.put("client_isp_license_type_dest", clientClassificationService.getClientSubCategoryById(clientDetailsDTO.getRegSubCategory()));
            
        }



        if(vpnApplication.getApplicationType() == ApplicationType.VPN_OWNER_CHANGE
            || vpnApplication.getApplicationType() == ApplicationType.VPN_TD
                || vpnApplication.getApplicationType() == ApplicationType.VPN_RECONNECT
        ){
            params.put("previousBWFound", false);
        }else {
            long linkId = vpnApplication.getVpnApplicationLinks().get(0).getNetworkLinkId();
            if(linkId != 0) {
                VPNNetworkLink networkLink = ServiceDAOFactory.getService(VPNNetworkLinkService.class)
                        .getHistoryOfLinkByLinkId(linkId)
                        .stream()
                        .max(Comparator.comparingLong(VPNNetworkLink::getActiveTo))
                        .orElseThrow(()->new NoDataFoundException("No Latest History Found for Link Id " + linkId))
                        ;
                params.put("previousBW", networkLink.getLinkBandwidth() + " Mbps");
                params.put("previousBWFound", true);
            }else {
                params.put("previousBWFound", false);
            }
        }
        return params;
    }


    @Override
    public String getResourceFile()  {
        return BillConstants.VPN_LINK_ADVICE_NOTE;
    }

    @Override
    public JRDataSource getJasperDataSource() {
        return new JREmptyDataSource();
    }

    @Override
    public String getOutputFilePath() throws Exception {
        String proposedName = this.vpnApplication.getVpnApplicationLinks().size() == 1 ? "VPN-advice-note-link-id-" + this.vpnApplication.getVpnApplicationLinks().get(0).getId()
                : "VPN-advice-note-app-id-" + this.vpnApplication.getApplicationId();
        return FileAPI.getInstance().
                getFilePath(FileTypeConstants.VPN_BILL_DIRECTORY, proposedName +".pdf", this.getCreationTime());
    }
}
