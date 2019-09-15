package lli.adviceNote;

import annotation.TableName;
import api.ClientAPI;
import api.FileAPI;
import client.classification.ClientClassificationService;
import common.ModuleConstants;
import common.RequestFailureException;
import common.bill.BillConstants;
import common.bill.BillDTO;
import common.bill.BillService;
import common.pdf.PdfMaterial;
import common.pdf.PdfParameter;
import config.GlobalConfigConstants;
import file.FileTypeConstants;
import ip.IPConstants;
import lli.Application.FlowConnectionManager.LLIConnection;
import lli.Application.LLIApplication;
import lli.Application.LLIApplicationService;
import lli.Application.LocalLoop.LocalLoop;
import lli.Application.LocalLoop.LocalLoopService;
import lli.Application.LocalLoop.LocalLoopStringProjection;
import lli.Application.Office.Office;
import lli.Application.Office.OfficeService;
import lli.Application.ownership.LLIOnProcessConnectionService;
import lli.Application.ownership.LLIOwnerChangeService;
import lli.Application.ownership.LLIOwnerShipChangeApplication;
import lli.LLIConnectionInstance;
import lli.LLIConnectionService;
import lli.connection.LLIConnectionConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import officialLetter.OfficialLetter;
import officialLetter.OfficialLetterType;
import util.KeyValuePair;
import util.ServiceDAOFactory;
import util.TimeConverter;
import vpn.client.ClientDetailsDTO;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Data
@EqualsAndHashCode(callSuper = false)
@TableName("official_letter")
@NoArgsConstructor
@Log4j
public class OwnershipChangeAdviceNote extends OfficialLetter implements PdfMaterial {

    public OwnershipChangeAdviceNote(long appId, long clientId) {
        setApplicationId(appId);
        setClientId(clientId);

        setCreationTime(System.currentTimeMillis());
        setClassName(OwnershipChangeAdviceNote.class.getCanonicalName());
        setDeleted(false);
        setLastModificationTime(System.currentTimeMillis());
        setOfficialLetterType(OfficialLetterType.ADVICE_NOTE);
        setModuleId(ModuleConstants.Module_ID_LLI);
    }

    public OwnershipChangeAdviceNote(OfficialLetter ol) {
        setId(ol.getId());
        setModuleId(ol.getModuleId());
        setClientId(ol.getClientId());
        setApplicationId(ol.getApplicationId());
        setOfficialLetterType(ol.getOfficialLetterType());
        setClassName(ol.getClassName());
        setCreationTime(ol.getCreationTime());
        setLastModificationTime(ol.getLastModificationTime());
        setDeleted(ol.isDeleted());
    }

    public Map<String, Object> getPdfParameters() throws Exception {
        Map<String, Object> map = new HashMap<>();

        String context = GlobalConfigConstants.contextMap.get("context");
        if (context == null) {
            throw new RequestFailureException("Context Path Not Set");
        }

        LLIOwnerShipChangeApplication lliApplication = ServiceDAOFactory.getService(LLIOwnerChangeService.class).
                getApplicationById(this.getApplicationId());

        populateClientInfo(map, lliApplication.getSrcClient(), lliApplication.getDstClient(),
                ModuleConstants.Module_ID_LLI);

        PdfParameter.populateOfficialLetterInfo(map, this);
        PdfParameter.populateLLIOwnershipChangeAdviceNoteTopInfo(map);
        boolean is_service_started = false;
        PdfParameter.populateAdviceNoteTopInfo(map, this, LLIConnectionConstants.applicationTypeNameMap.get(lliApplication.getType()), is_service_started);

        populateBillInfoForAdviceNote(map, lliApplication.getDemandNote());
        populateConnectionInformation(map, context, lliApplication);


        return map;
    }

    public void populateConnectionInformation(Map<String, Object> map, String context, LLIOwnerShipChangeApplication lliApplication) throws Exception {

        LLIOnProcessConnectionService service = ServiceDAOFactory.getService(LLIOnProcessConnectionService.class);
        List<LLIConnection> connections = service.getOnProcessConnectionsByAppId(this.getApplicationId());

        if (connections.size() != 0)
            map.put("number_of_connections", Integer.toString(connections.size()));
        else
            map.put("number_of_connections", "N/A");

        String connectionDetalsLink = context + "lli/ownershipChange/view-all-connections-of-prev-owner.do?applicationId=" + this.getApplicationId()
                + "&" + "clientId=" + lliApplication.getSrcClient();

        map.put("connection_detail_link", connectionDetalsLink);
    }

    public static void populateBillInfoForAdviceNote(Map<String, Object> map, Long demandNoteId) throws Exception {
        BillDTO billDTO = null;
        if (demandNoteId != null) {
            billDTO = ServiceDAOFactory.getService(BillService.class).getBillByBillID(demandNoteId);
        }
        PdfParameter.populateDemandNoteInfoForAdviceNote(map, billDTO);
    }


    private static void populateClientInfo(
            Map<String, Object> params,
            long prevClientId,
            long newClientId,
            int moduleId)
            throws Exception {

        populate_previous_client_information(params, prevClientId, moduleId);
        populate_new_client_information(params, newClientId, moduleId);

    }

    private static void populate_new_client_information(
            Map<String, Object> params,
            long newClientId,
            int moduleId)
            throws Exception {

        KeyValuePair<ClientDetailsDTO, ClientContactDetailsDTO> newPair = ClientAPI.getInstance().
                getPairOfClientDetailsAndClientContactDetails(
                        newClientId, moduleId, ClientContactDetailsDTO.BILLING_CONTACT);

        params.put("current_client_full_name", newPair.getValue().getRegistrantsName() + " " + newPair.getValue().getRegistrantsLastName());
        params.put("current_client_user_name", newPair.getKey().getLoginName());

        params.put("current_client_mobile", newPair.getValue().getPhoneNumber());
        params.put("current_client_billing_address", newPair.getValue().getAddress());

        ClientClassificationService clientClassificationService = ServiceDAOFactory.getService(ClientClassificationService.class);

        params.put("current_client_type", clientClassificationService.getClientTypeById((long) newPair.getKey().getRegistrantType()));
        params.put("current_client_category", clientClassificationService.getClientCategoryById(newPair.getKey().getRegistrantCategory()));
        params.put("current_client_isp_license_type", clientClassificationService.getClientSubCategoryById(newPair.getKey().getRegSubCategory()));
    }

    private static void populate_previous_client_information(
            Map<String, Object> params,
            long prevClientId,
            int moduleId)
            throws Exception {

        KeyValuePair<ClientDetailsDTO, ClientContactDetailsDTO> prevPair = ClientAPI.getInstance().
                getPairOfClientDetailsAndClientContactDetails(
                        prevClientId, moduleId, ClientContactDetailsDTO.BILLING_CONTACT);

        params.put("prev_client_full_name", prevPair.getValue().getRegistrantsName() + " " + prevPair.getValue().getRegistrantsLastName());
        params.put("prev_client_user_name", prevPair.getKey().getLoginName());

        params.put("prev_client_mobile", prevPair.getValue().getPhoneNumber());
        params.put("prev_client_billing_address", prevPair.getValue().getAddress());
        //TODO
        ClientClassificationService clientClassificationService = ServiceDAOFactory.getService(ClientClassificationService.class);

        params.put("prev_client_type", clientClassificationService.getClientTypeById((long) prevPair.getKey().getRegistrantType()));
        params.put("prev_client_category", clientClassificationService.getClientCategoryById(prevPair.getKey().getRegistrantCategory()));
        params.put("prev_client_isp_license_type", clientClassificationService.getClientSubCategoryById(prevPair.getKey().getRegSubCategory()));
    }

    @Override
    public String getResourceFile() {
        return BillConstants.LLI_OWNERSHIP_CHANGE_ADVICE_NOTE;
    }

    @Override
    public JRDataSource getJasperDataSource() {
        return new JREmptyDataSource();
    }

    @Override
    public String getOutputFilePath() throws Exception {
        return FileAPI.getInstance().getFilePath(FileTypeConstants.LLI_BILL_DIRECTORY,
                "ownership-change-advice-note-" + this.applicationId + ".pdf",
                this.getCreationTime());
    }
}

