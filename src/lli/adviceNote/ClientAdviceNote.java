package lli.adviceNote;

import annotation.TableName;
import api.FileAPI;
import common.ModuleConstants;
import common.bill.BillConstants;
import common.pdf.PdfMaterial;
import common.pdf.PdfParameter;
import file.FileTypeConstants;
import ip.IPConstants;
import lli.Application.ReviseClient.ReviseDTO;
import lli.Application.ReviseClient.ReviseService;
import lli.connection.LLIConnectionConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import officialLetter.OfficialLetter;
import officialLetter.OfficialLetterType;
import util.ServiceDAOFactory;

import java.util.HashMap;
import java.util.Map;

@Data@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("official_letter")
public class ClientAdviceNote extends OfficialLetter implements PdfMaterial {
    public ClientAdviceNote(long appId, long clientId) {
        setApplicationId(appId);
        setClientId(clientId);

        setCreationTime(System.currentTimeMillis());
        setClassName(ClientAdviceNote.class.getCanonicalName());
        setDeleted(false);
        setLastModificationTime(System.currentTimeMillis());
        setOfficialLetterType(OfficialLetterType.ADVICE_NOTE);
        setModuleId(ModuleConstants.Module_ID_LLI);

    }

    public ClientAdviceNote(OfficialLetter ol){

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

    @Override
    public Map<String, Object> getPdfParameters() throws Exception {
        ReviseDTO reviseApplication = ServiceDAOFactory.getService(ReviseService.class).getappById(this.getApplicationId());
        Map<String, Object>map = new HashMap<>();
        PdfParameter.populateOfficialLetterInfo(map, this);
        PdfParameter.populateClientInfoForAdviceNote(map, this, ModuleConstants.Module_ID_LLI);
        PdfParameter.populateAdviceNoteTopInfo(map, this, LLIConnectionConstants.applicationTypeNameMap.get(reviseApplication.getApplicationType()), reviseApplication.isCompleted());
//        PdfParameter.populateLLIAdviceNoteTopInfo(map);
        PdfParameter.populateBillInfoForAdviceNote(map, reviseApplication.getDemandNoteID());
        PdfParameter.populateIPInfoForLLIAdviceNote(map, 0L, IPConstants.Purpose.LLI_CONNECTION);
        PdfParameter.populateWorkOrderInfoForLLIAdviceNote(map, reviseApplication.getId());

        return map;
    }



    @Override
    public String getResourceFile() {

        return BillConstants.LLI_CONNECTION_ADVICE_NOTE;
    }

    @Override
    public JRDataSource getJasperDataSource() {
        return new JREmptyDataSource();
    }

    @Override
    public String getOutputFilePath() throws Exception {
        return FileAPI.getInstance().getFilePath(FileTypeConstants.LLI_BILL_DIRECTORY, "client-advice-note-" + this.applicationId +".pdf", this.getCreationTime());
    }
}