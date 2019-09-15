package lli.adviceNote;

import annotation.TableName;
import api.FileAPI;
import common.ModuleConstants;
import common.RequestFailureException;
import common.bill.BillConstants;
import common.pdf.PdfMaterial;
import common.pdf.PdfParameter;
import config.GlobalConfigConstants;
import file.FileTypeConstants;
import lli.Application.ReviseClient.ReviseDTO;
import lli.Application.ReviseClient.ReviseService;
import lli.LLIConnectionService;
import lli.LLIDropdownPair;
import lli.connection.LLIConnectionConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import officialLetter.OfficialLetter;
import officialLetter.OfficialLetterType;
import util.ServiceDAOFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("official_letter")
@NoArgsConstructor
@Log4j
public class LLITDReconnectAdviceNote extends ClientAdviceNote implements PdfMaterial {

    public LLITDReconnectAdviceNote(long appId, long clientId) {
        setApplicationId(appId);
        setClientId(clientId);

        setCreationTime(System.currentTimeMillis());
        setClassName(LLITDReconnectAdviceNote.class.getCanonicalName());
        setDeleted(false);
        setLastModificationTime(System.currentTimeMillis());
        setOfficialLetterType(OfficialLetterType.ADVICE_NOTE);
        setModuleId(ModuleConstants.Module_ID_LLI);
    }

    public LLITDReconnectAdviceNote(OfficialLetter ol) {

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
        populate_letter_info(map);
        populate_client_info(map);
        populate_connection_information(map, context);

        populate_bandwidth_information(map);

        ReviseDTO reviseDTO = ServiceDAOFactory.getService(ReviseService.class).getappById(this.getApplicationId());

        PdfParameter.populateBillInfoForAdviceNote(map, reviseDTO.getDemandNoteID());

        return map;
    }

    private void populate_bandwidth_information(Map<String, Object> map) throws Exception {
        PdfParameter.populate_bandwidth_information(map, this);
    }

    private void populate_connection_information(Map<String, Object> map, String context) throws Exception {

        LLIConnectionService lliConnectionService = ServiceDAOFactory.getService(LLIConnectionService.class);

        List<LLIDropdownPair> list = lliConnectionService.getLLIConnectionNameIDPairListByClient(this.getClientId());

        int active_count = 0;
        int td_count = 0;
        for (LLIDropdownPair l : list) {
            if (ServiceDAOFactory.getService(LLIConnectionService.class)
                    .getLLIConnectionByConnectionID(l.getID()).getStatus() == LLIConnectionConstants.STATUS_ACTIVE)
                active_count++;
            if (ServiceDAOFactory.getService(LLIConnectionService.class)
                    .getLLIConnectionByConnectionID(l.getID()).getStatus() == LLIConnectionConstants.STATUS_TD)
                td_count++;
        }

        ReviseDTO reviseDTO = ServiceDAOFactory.getService(ReviseService.class).getappById(this.getApplicationId());

        if (reviseDTO.getApplicationType() == LLIConnectionConstants.RECONNECT) {
            //reconnect
            map.put("number_of_connections", Integer.toString(td_count));
        } else {
            // TD, LT, BLT
            map.put("number_of_connections", Integer.toString(active_count));
        }
        //context????
        String connectionDetalsLink = context + "lli/connection/view-all-connections.do?clientId=" + this.getClientId();
        System.out.println(context);
        map.put("connection_detail_link", connectionDetalsLink);
    }


    private void populate_client_info(Map<String, Object> map) throws Exception {
        PdfParameter.populateClientInfoForAdviceNote(map, this, ModuleConstants.Module_ID_LLI);
    }


    private void populate_letter_info(Map<String, Object> map) throws Exception {
        PdfParameter.populateOfficialLetterInfo(map, this);

        ReviseDTO reviseDTO = ServiceDAOFactory.getService(ReviseService.class).getappById(this.getApplicationId());
        String appType = LLIConnectionConstants.applicationTypeNameMap.get(reviseDTO.getApplicationType());

        boolean isServiceStarted = false;
        PdfParameter.populateAdviceNoteTopInfo(map, this, appType, isServiceStarted);
        map.put("an_title", "Lease Line Internet (LLI) Advice Note");
        PdfParameter.populateLLITDReconnectAdviceNoteTopInfo(map);
    }

    @Override
    public String getResourceFile() {
        return BillConstants.LLI_CLOSE_TD_RECONNECT_ADVICE_NOTE;
    }

    @Override
    public JRDataSource getJasperDataSource() {
        return new JREmptyDataSource();
    }

    @Override
    public String getOutputFilePath() throws Exception {
        return FileAPI.getInstance().getFilePath(FileTypeConstants.LLI_BILL_DIRECTORY, "td_reconnect-advice-note-" + this.applicationId + ".pdf", this.getCreationTime());
    }
}
