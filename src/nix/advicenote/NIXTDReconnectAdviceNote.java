package nix.advicenote;

import annotation.TableName;
import api.FileAPI;
import common.ModuleConstants;
import common.bill.BillConstants;
import common.bill.BillDTO;
import common.payment.PaymentDTO;
import common.payment.constants.PaymentConstants;
import common.pdf.PdfMaterial;
import common.pdf.PdfParameter;
import file.FileTypeConstants;
import lli.connection.LLIConnectionConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import nix.application.NIXApplication;
import nix.application.NIXApplicationService;
import nix.revise.NIXReviseDTO;
import nix.revise.NIXReviseService;
import officialLetter.OfficialLetter;
import officialLetter.OfficialLetterType;
import util.ServiceDAOFactory;
import util.TimeConverter;

import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("official_letter")
@NoArgsConstructor
@Log4j
public class NIXTDReconnectAdviceNote extends OfficialLetter implements PdfMaterial {

    public NIXTDReconnectAdviceNote(long appId, long clientId) {
        setApplicationId(appId);
        setClientId(clientId);

        setCreationTime(System.currentTimeMillis());
        setClassName(NIXTDReconnectAdviceNote.class.getCanonicalName());
        setDeleted(false);
        setLastModificationTime(System.currentTimeMillis());
        setOfficialLetterType(OfficialLetterType.ADVICE_NOTE);
        setModuleId(ModuleConstants.Module_ID_NIX);
    }

    public NIXTDReconnectAdviceNote(OfficialLetter ol) {

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
        populate_letter_info(map);
        populate_client_info(map);
        populate_connection_information(map);

        NIXReviseDTO nixReviseDTO = ServiceDAOFactory.getService(NIXReviseService.class).getappById(this.getApplicationId());

        PdfParameter.populateBillInfoForAdviceNote(map, nixReviseDTO.getDemandNoteID());

        return map;
    }
    private void populate_connection_information(Map<String, Object> map) {
        //dummy values
        map.put("number_of_connections", "6");
        String connectionDetalsLink = "www.google.com";
        map.put("connection_detail_link", connectionDetalsLink);
    }


    private void populate_client_info(Map<String, Object> map) throws Exception {
        PdfParameter.populateClientInfoForAdviceNote(map, this, ModuleConstants.Module_ID_NIX);
    }


    private void populate_letter_info(Map<String, Object> map) throws Exception {
        PdfParameter.populateOfficialLetterInfo(map, this);

        NIXReviseDTO nixReviseDTO = ServiceDAOFactory.getService(NIXReviseService.class).getappById(this.getApplicationId());
        String appType = LLIConnectionConstants.applicationTypeNameMap.get(nixReviseDTO.getApplicationType());

        boolean isServiceStarted = false;
        PdfParameter.populateAdviceNoteTopInfo(map, this, appType, isServiceStarted);
        map.put("an_title", "National Internet Exchange (NIX) Advice Note");
        PdfParameter.populateNIXAdviceNoteTopInfo(map);
    }

    @Override
    public String getResourceFile() {
        return BillConstants.NIX_CLOSE_TD_RECONNECT_ADVICE_NOTE;
    }

    @Override
    public JRDataSource getJasperDataSource() {
        return new JREmptyDataSource();
    }

    @Override
    public String getOutputFilePath() throws Exception {
        return FileAPI.getInstance().getFilePath(FileTypeConstants.NIX_BILL_DIRECTORY, "td_reconnect-advice-note-" + this.applicationId + ".pdf", this.getCreationTime());
    }
}
