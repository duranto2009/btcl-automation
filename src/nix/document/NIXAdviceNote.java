package nix.document;

import annotation.TableName;
import api.FileAPI;
import common.ModuleConstants;
import common.RequestFailureException;
import common.bill.BillConstants;
import common.pdf.PdfMaterial;
import common.pdf.PdfParameter;
import file.FileTypeConstants;
import global.GlobalService;
import ip.IPConstants;
import lli.Application.EFR.EFR;
import lli.Application.EFR.EFRBean;
import lli.Application.LocalLoop.LocalLoopStringProjection;
import lli.connection.LLIConnectionConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import nix.application.NIXApplication;
import nix.application.NIXApplicationService;
import nix.application.localloop.NIXApplicationLocalLoop;
import nix.application.localloop.NIXApplicationLocalLoopService;
import nix.application.office.NIXApplicationOffice;
import nix.application.office.NIXApplicationOfficeService;
import nix.connection.NIXConnection;
import nix.connection.NIXConnectionConditionBuilder;
import nix.connection.NIXConnectionService;
import nix.constants.NIXConstants;
import nix.efr.NIXEFRService;
import nix.localloop.NIXLocalLoopService;
import nix.office.NIXOffice;
import nix.office.NIXOfficeService;
import officialLetter.OfficialLetter;
import officialLetter.OfficialLetterType;
import util.ServiceDAOFactory;
import util.TimeConverter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = false)
@Data
@TableName("official_letter")
@NoArgsConstructor
@Log4j
public class NIXAdviceNote extends OfficialLetter implements PdfMaterial {

    public NIXAdviceNote (long appId, long clientId) {
        setClientId(clientId);
        setApplicationId(appId);
        setModuleId(ModuleConstants.Module_ID_NIX);
        setClassName(this.getClass().getCanonicalName());
        setLastModificationTime(System.currentTimeMillis());
        setOfficialLetterType(OfficialLetterType.ADVICE_NOTE);
        setCreationTime(System.currentTimeMillis());
        setDeleted(false);
    }

    public NIXAdviceNote(OfficialLetter ol) {

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
        NIXApplication nixApplication = ServiceDAOFactory.getService(NIXApplicationService.class).getApplicationById(this.getApplicationId());
        PdfParameter.populateAdviceNoteTopInfo(map, this, NIXConstants.nixapplicationTypeNameMap.get(nixApplication.getType()),
                nixApplication.getIsServiceStarted() == 1);
        PdfParameter.populateNIXAdviceNoteTopInfo(map);
        PdfParameter.populateOfficialLetterInfo(map, this);
        PdfParameter.populateClientInfoForAdviceNote(map, this, ModuleConstants.Module_ID_NIX);
        PdfParameter.populateBillInfoForAdviceNote(map, nixApplication.getDemandNote());

        NIXConnection nixConnection = null;
        List<NIXApplicationLocalLoop> listOfLocalLoop = null;
        List<NIXApplicationLocalLoop> listOfLocalLoop2 = null;

        if(nixApplication.getConnection() == 0L) {
            NIXApplicationOffice office = ServiceDAOFactory.getService(NIXApplicationOfficeService.class)
                    .getOfficesByApplicationId(nixApplication.getId())
                    .stream()
                    .findFirst()
                    .orElseThrow(()->new RequestFailureException("No Office Found for Application " + this.applicationId));
            map.put("connection_1_name", "N/A");
            map.put("connection_1_address", office.getAddress());
            map.put("connection_1_activation_date", "N/A");

            listOfLocalLoop = ServiceDAOFactory.getService(NIXApplicationLocalLoopService.class).getLocalLoopsAdjustedWithActualDistanceByApplicationId(this.getApplicationId());
        }else {
            nixConnection = ServiceDAOFactory.getService(GlobalService.class).getAllObjectListByCondition(
                    NIXConnection.class, new NIXConnectionConditionBuilder().
                            Where()
                            .applicationEquals(applicationId)
                            .getCondition()
            )     .stream()
                    .findFirst()
                    .orElseThrow(()->new RequestFailureException("No Connection Instance Found for Client " + this.getClientId() + " appId " + this.getApplicationId()));
            long connectionId = nixConnection.getConnectionId();
            NIXOffice office = ServiceDAOFactory.getService(NIXOfficeService.class)
                    .getOfficesByConnectionID(nixConnection.getConnectionId())
                    .stream()
                    .findFirst()
                    .orElseThrow(()->new RequestFailureException("No Office Found for Connection " + connectionId));
            map.put("connection_1_name", nixConnection.getName());
            map.put("connection_1_address", office.getName());
            map.put("connection_1_activation_date", TimeConverter.getDateTimeStringByMillisecAndDateFormat(nixConnection.getActiveFrom(), "dd/MM/yyyy"));

            listOfLocalLoop = ServiceDAOFactory.getService(NIXLocalLoopService.class).getNIXApplicationLocalLoopsByConID(nixConnection.getConnectionId());

        }

        map.put("connection_1_connection_type", "N/A");

        listOfLocalLoop.forEach(log::info);
        List<LocalLoopStringProjection> listOfLocalLoopStringProjection = listOfLocalLoop.stream()
                .map(NIXLocalLoopService::getLocalLoopProjection)
                .collect(Collectors.toList());
        map.put("connection_1_local_loops", listOfLocalLoopStringProjection);

        map.put("print_sub2_connection_2", false);

        //to do
        PdfParameter.populateIPInfoForLLIAdviceNote(map, nixConnection==null ? 0L: nixConnection.getId(), IPConstants.Purpose.NIX_CONNECTION);

        List<EFRBean> list =
                ServiceDAOFactory.getService(NIXEFRService.class).getSelected(this.getApplicationId())
                        .stream()
                        .map(t->new EFRBean(
                                t.getSource(),
                                EFR.TERMINAL.get((long)t.getSourceType()),
                                t.getDestination(),
                                EFR.TERMINAL.get((long)t.getDestinationType()),
                                t.getProposedDistance(),
                                t.getActualDistance())
                        )
                        .collect(Collectors.toList());

        map.put("work_order_list", list);

        return map;
    }

    @Override
    public String getResourceFile() {
        return BillConstants.NIX_ADVICE_NOTE;
    }

    @Override
    public JRDataSource getJasperDataSource() {
        return new JREmptyDataSource();
    }

    @Override
    public String getOutputFilePath() throws Exception {
        return FileAPI.getInstance().getFilePath(FileTypeConstants.NIX_BILL_DIRECTORY, "nix-advice-note-" + this.applicationId +".pdf", this.getCreationTime());
    }
}
