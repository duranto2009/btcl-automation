package lli.adviceNote;

import annotation.TableName;
import api.ClientAPI;
import api.FileAPI;
import common.ModuleConstants;
import common.RequestFailureException;
import common.bill.BillConstants;
import common.bill.BillDTO;
import common.bill.BillService;
import common.payment.PaymentDTO;
import common.payment.PaymentService;
import common.payment.constants.PaymentConstants;
import common.pdf.PdfMaterial;
import common.pdf.PdfParameter;
import file.FileTypeConstants;
import global.GlobalService;
import ip.IPBlockLLI;
import ip.IPConstants;
import ip.IPService;
import ip.ipUsage.IPBlockUsage;
import lli.*;
import lli.Application.EFR.EFR;
import lli.Application.EFR.EFRBean;
import lli.Application.EFR.EFRService;
import lli.Application.LLIApplication;
import lli.Application.LLIApplicationService;
import lli.Application.LocalLoop.LocalLoop;
import lli.Application.LocalLoop.LocalLoopConditionBuilder;
import lli.Application.LocalLoop.LocalLoopService;
import lli.Application.LocalLoop.LocalLoopStringProjection;
import lli.Application.Office.Office;
import lli.Application.Office.OfficeService;
import lli.connection.LLIConnectionConstants;
import lli.demandNote.LLIDemandNoteService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import officialLetter.*;
import user.UserDTO;
import user.UserRepository;
import util.KeyValuePair;
import util.ServiceDAOFactory;
import util.TimeConverter;

import vpn.client.ClientDetailsDTO;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static util.ServiceDAOFactory.*;


@Data
@EqualsAndHashCode(callSuper = false)
@TableName("official_letter")
@NoArgsConstructor
@Log4j
public class ConnectionAdviceNote extends OfficialLetter implements PdfMaterial {


    public ConnectionAdviceNote(long appId, long clientId) {
        setApplicationId(appId);
        setClientId(clientId);

        setCreationTime(System.currentTimeMillis());
        setClassName(ConnectionAdviceNote.class.getCanonicalName());
        setDeleted(false);
        setLastModificationTime(System.currentTimeMillis());
        setOfficialLetterType(OfficialLetterType.ADVICE_NOTE);
        setModuleId(ModuleConstants.Module_ID_LLI);

    }

    public ConnectionAdviceNote(OfficialLetter ol) {

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
        populate_bandwidth_information(map);
        populate_advice_note_info(map);

        return map;
    }

    private void populate_advice_note_info(Map<String, Object> map) throws Exception {
        LLIApplication lliApplication = getService(LLIApplicationService.class).
                getLLIApplicationByApplicationID(this.getApplicationId());
        PdfParameter.populateAdviceNoteTopInfo(map, this, LLIConnectionConstants.
                        applicationTypeNameMap.get(lliApplication.getApplicationType()),
                lliApplication.isServiceStarted());
        PdfParameter.populateBillInfoForAdviceNote(map, lliApplication.getDemandNoteID());

        map.put("IS_LLI_CLOSE", false);
        if (this.getModuleId() == ModuleConstants.Module_ID_LLI &&
                lliApplication.getApplicationType()==LLIConnectionConstants.CLOSE_CONNECTION){
            map.put("IS_LLI_CLOSE", true);
        }

        LLIConnectionInstance connectionInstance = null;
        List<LocalLoop> listOfLocalLoop = null;
        List<LocalLoop> listOfLocalLoop2 = null;
        if (lliApplication.getConnectionId() == 0L) {
            Office office = getService(OfficeService.class).getOffice(lliApplication.getApplicationID())
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new RequestFailureException("No Office Found for Application " + this.applicationId));
            map.put("connection_1_name", "N/A");
            map.put("connection_1_address", office.getOfficeAddress());
            map.put("connection_1_activation_date", "N/A");


            listOfLocalLoop = getService(LocalLoopService.class).getLocalLoop(this.getApplicationId());
        } else {
            if(lliApplication.getApplicationType()==LLIConnectionConstants.CLOSE_CONNECTION){
                connectionInstance = getService(GlobalService.class).getAllObjectListByCondition(
                        LLIConnectionInstance.class, new LLIConnectionInstanceConditionBuilder()
                        .Where()
                        .IDEquals((long)lliApplication.getConnectionId())
                        .getCondition()
                ).stream()
                        .findFirst()
                        .orElse(null);
                if(connectionInstance != null ){
                    getService(LLIConnectionService.class)
                            .populateLLIConnectionInstanceWithOfficeAndLocalLoop(connectionInstance);
                    List<Long> officeIds = connectionInstance.getLliOffices().stream().map(LLIOffice::getID).collect(Collectors.toList());
                    if(officeIds.isEmpty()) {
                        listOfLocalLoop = Collections.emptyList();
                    } else {
                        listOfLocalLoop = getService(GlobalService.class).getAllObjectListByCondition(
                                LocalLoop.class, new LocalLoopConditionBuilder()
                                        .Where()
                                        .officeIDIn(officeIds)
                                        .isDeleted(false)
                                        .getCondition()
                        );
                    }

                }
            }else {
                connectionInstance = getService(LLIConnectionService.class)
                    .getLLIConnectionByConnectionID(lliApplication.getConnectionId());
                if (connectionInstance == null) {
                    throw new RequestFailureException("No Connection Instance Found for connection Id " + lliApplication.getConnectionId());
                }
                listOfLocalLoop = getService(LocalLoopService.class).getLocalLoopByCon(connectionInstance.getID());
            }

            if (connectionInstance == null) {
                throw new RequestFailureException("No Connection Instance Found for connection Id " + lliApplication.getConnectionId());
            }

            map.put("connection_1_name", connectionInstance.getName());
            map.put("connection_1_address", connectionInstance.getLliOffices().get(0).getAddress());
            map.put("connection_1_activation_date", TimeConverter.getDateTimeStringByMillisecAndDateFormat(connectionInstance.getActiveFrom(), "dd/MM/yyyy"));


        }

        map.put("connection_1_connection_type", LLIConnectionConstants.connectionTypeMap.getOrDefault(lliApplication.getConnectionType(), "N/A"));
//        List<LocalLoop> listOfLocalLoop = ServiceDAOFactory.getService(LocalLoopService.class).getLocalLoop(this.getApplicationId());
        listOfLocalLoop.forEach(log::info);
        List<LocalLoopStringProjection> listOfLocalLoopStringProjection = listOfLocalLoop.stream().map(LocalLoopService::getLocalLoopProjection).collect(Collectors.toList());
        map.put("connection_1_local_loops", listOfLocalLoopStringProjection);

        // TODO decide
        LLIConnectionInstance sourceConnection = null;
        if (lliApplication.getApplicationType() == LLIConnectionConstants.SHIFT_BANDWIDTH ||
                lliApplication.getApplicationType() == LLIConnectionConstants.SHIFT_BANDWIDTH_NEW_CONNECTION) {
            map.put("print_sub2_connection_2", true);

            sourceConnection = getService(LLIConnectionService.class)
                    .getLLIConnectionByConnectionID(lliApplication.getSourceConnectionID());
            if (sourceConnection == null) {
                throw new RequestFailureException("No Source Connection Found for source connection Id " + lliApplication.getSourceConnectionID());
            }
            map.put("connection_2_name", sourceConnection.getName());
            map.put("connection_2_address", sourceConnection.getLliOffices().get(0).getAddress());
            map.put("connection_2_activation_date", TimeConverter.getDateTimeStringByMillisecAndDateFormat(sourceConnection.getActiveFrom(), "dd/MM/yyyy"));
            map.put("connection_2_connection_type", LLIConnectionConstants.connectionTypeMap.getOrDefault(lliApplication.getConnectionType(), "N/A"));
            listOfLocalLoop2 = getService(LocalLoopService.class).getLocalLoopByCon(sourceConnection.getID());
            listOfLocalLoop2.forEach(log::info);
            List<LocalLoopStringProjection> listOfLocalLoopStringProjection2 = listOfLocalLoop2.stream().map(LocalLoopService::getLocalLoopProjection).collect(Collectors.toList());
            map.put("connection_2_local_loops", listOfLocalLoopStringProjection2);

        } else {
            map.put("print_sub2_connection_2", false);
        }

        PdfParameter.populateLLIAdviceNoteTopInfo(map, lliApplication, connectionInstance, sourceConnection);
        PdfParameter.populateIPInfoForLLIAdviceNote(map, connectionInstance == null ? 0L : connectionInstance.getID(), IPConstants.Purpose.LLI_CONNECTION);
        PdfParameter.populateWorkOrderInfoForLLIAdviceNote(map, lliApplication.getApplicationID());
    }

    private void populate_client_info(Map<String, Object> map) throws Exception {
        PdfParameter.populateClientInfoForAdviceNote(map, this, ModuleConstants.Module_ID_LLI);
    }


    private void populate_letter_info(Map<String, Object> map) throws Exception {
        PdfParameter.populateOfficialLetterInfo(map, this);
    }

    private void populate_bandwidth_information(Map<String, Object> map) throws Exception {
        PdfParameter.populate_bandwidth_information(map, this);
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
        return FileAPI.getInstance().getFilePath(FileTypeConstants.LLI_BILL_DIRECTORY, "connection-advice-note-" + this.applicationId + ".pdf", this.getCreationTime());
    }
}

