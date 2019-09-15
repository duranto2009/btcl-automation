package coLocation.adviceNote;

import annotation.TableName;
import api.FileAPI;
import coLocation.CoLocationConstants;
import coLocation.application.CoLocationApplicationDTO;
import coLocation.application.CoLocationApplicationService;
import coLocation.connection.CoLocationConnectionDTO;
import coLocation.connection.CoLocationConnectionService;
import coLocation.inventory.CoLocationInventoryTemplateDTO;
import coLocation.inventory.CoLocationInventoryTemplateService;
import common.ModuleConstants;
import common.bill.BillConstants;
import common.pdf.PdfMaterial;
import common.pdf.PdfParameter;
import file.FileTypeConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import officialLetter.OfficialLetter;
import officialLetter.OfficialLetterType;
import util.ServiceDAOFactory;
import util.TimeConverter;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = false)
@Data
@TableName("official_letter")
@NoArgsConstructor
@Log4j
public class ColocationAdviceNote extends OfficialLetter implements PdfMaterial {

    public ColocationAdviceNote (long appId, long clientId) {
        setClientId(clientId);
        setApplicationId(appId);
        setModuleId(ModuleConstants.Module_ID_COLOCATION);
        setClassName(this.getClass().getCanonicalName());
        setLastModificationTime(System.currentTimeMillis());
        setOfficialLetterType(OfficialLetterType.ADVICE_NOTE);
        setCreationTime(System.currentTimeMillis());
        setDeleted(false);
    }

    public ColocationAdviceNote(OfficialLetter ol) {

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
        Map<String, Object> map = new HashMap<>();

        populatePdfParameters(map);
        return map;
    }

    private void populatePdfParameters(Map<String, Object> map) throws Exception {
        CoLocationApplicationDTO applicationDTO = ServiceDAOFactory.getService(CoLocationApplicationService.class).getColocationApplication(this.getApplicationId());
        PdfParameter.populateAdviceNoteTopInfo(map, this, CoLocationConstants.applicationTypeNameMap.get(applicationDTO.getApplicationType()), applicationDTO.isServiceStarted());
        PdfParameter.populateCoLocationAdviceTopInfo(map);
        PdfParameter.populateOfficialLetterInfo(map, this);
        PdfParameter.populateClientInfoForAdviceNote(map, this, ModuleConstants.Module_ID_COLOCATION);
        PdfParameter.populateBillInfoForAdviceNote(map, applicationDTO.getDemandNoteID());
        Map<Long, CoLocationInventoryTemplateDTO> inventoryTemplateMap = ServiceDAOFactory.getService(CoLocationInventoryTemplateService.class).getCoLocationInventoryTemplateMap();




        if(applicationDTO.isRackNeeded()) {
            CoLocationInventoryTemplateDTO rackSize = inventoryTemplateMap.getOrDefault((long)applicationDTO.getRackTypeID(), null);
            CoLocationInventoryTemplateDTO rackSpace = inventoryTemplateMap.getOrDefault((long)applicationDTO.getRackSpace(), null);

            map.put("rack_size", String.valueOf(rackSize != null ? rackSize.getValue() : "N/A"));
            map.put("rack_space", String.valueOf(rackSpace != null ? rackSpace.getValue() : "N/A"));
        }else {
            map.put("rack_size","N/A");
            map.put("rack_space", "N/A");
        }

        if(applicationDTO.isPowerNeeded()) {
            CoLocationInventoryTemplateDTO powerType = inventoryTemplateMap.getOrDefault((long)applicationDTO.getPowerType(), null);
            map.put("power_type", String.valueOf(powerType != null ? powerType.getValue() : "N/A"));
        }else {
            map.put("power_type", "N/A");
        }

        if(applicationDTO.isFloorSpaceNeeded()) {
            CoLocationInventoryTemplateDTO floorSpaceType = inventoryTemplateMap.getOrDefault((long)applicationDTO.getFloorSpaceType(), null);
            map.put("floor_space_type", String.valueOf(floorSpaceType != null ? floorSpaceType.getValue() : "N/A"));
        }else {
            map.put("floor_space_type", "N/A");
        }

        if(applicationDTO.isFiberNeeded()) {
            CoLocationInventoryTemplateDTO coreType = inventoryTemplateMap.getOrDefault((long)applicationDTO.getFiberType(), null);;
            map.put("core_type", String.valueOf(coreType != null ? coreType.getValue() : "N/A"));
        }else {
            map.put("core_type", "N/A");
        }



        map.put("floor_space_unit", String.valueOf(applicationDTO.getFloorSpaceAmount()));
        map.put("total_number_of_cores", String.valueOf(applicationDTO.getFiberCore() == null ? "0.0" : applicationDTO.getFiberCore()));
        map.put("power_unit", String.valueOf(applicationDTO.getPowerAmount()));
        if(applicationDTO.getConnectionId() == 0){
            map.put("connection_name", "N/A");
            map.put("connection_activation_date", "N/A");


        }else {
            CoLocationConnectionDTO connectionDTO = ServiceDAOFactory.getService(CoLocationConnectionService.class).getColocationConnection(applicationDTO.getConnectionId());

            map.put("connection_name", connectionDTO.getName());
            map.put("connection_activation_date", TimeConverter.getDateTimeStringByMillisecAndDateFormat(connectionDTO.getActiveFrom(), "dd/MM/yyyy"));
        }
    }


    @Override
    public String getResourceFile() {
        return BillConstants.COLOCATION_ADVICE_NOTE;
    }

    @Override
    public JRDataSource getJasperDataSource() {
        return new JREmptyDataSource();
    }

    @Override
    public String getOutputFilePath() throws Exception {
        return FileAPI.getInstance().getFilePath(FileTypeConstants.COLOCATION_BILL_DIRECTORY, "colocation-advice-note-" + this.applicationId +".pdf", this.getCreationTime());
    }
}
