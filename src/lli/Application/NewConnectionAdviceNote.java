package lli.Application;

import java.io.File;
import java.util.*;

import api.FileAPI;
import common.client.ClientDTO;
import file.FileTypeConstants;
import common.pdf.PdfMaterial;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import role.RoleDTO;
import util.TimeConverter;

public class NewConnectionAdviceNote implements PdfMaterial {

    private static final Logger logger = LoggerFactory.getLogger(NewConnectionAdviceNote.class);
    private static final String HASH_ALGO_MD5 = "MD5";

    private static final String NULL_STRING = "............................................................";
    private static final String BTCL_LOGO_HEADING = "../../images/common/btcl_logo_heading.png";
    private static final String JASPER_TEMPLATE_FILE = "lli/bill/lli_advice_note.jasper";
    private static final String LLI_NEW_CONNECTION_ADVICE_NOTE_DIR = "lli/new-connection/advice-note/";

    String recipientName;
    List<String> listOfCC;
    String clientName;
    String clientAddress;

    double distance;
    double measurementOfLoopWearing;
    String expandType;
    String typeOfTransfer;
    double transferFee;
    String typeOfPayment;
    long demandNoteNumber;
    String demandNoteDate;

    double lengthOfUsedCable;
    double postLineMeasurement;
    double lengthOfNotUsedCable;
    double lengthOfPostLine;
    double lengthOfCable;
    long numberOfUsedPostHead;

    String outputFilePath;

    public NewConnectionAdviceNote(long applicationId) {
        this.outputFilePath = NewConnectionAdviceNote.generateFilePath(applicationId);
    }

    public double getLengthOfUsedCable() {
        return lengthOfUsedCable;
    }

    public void setLengthOfUsedCable(double lengthOfUsedCable) {
        this.lengthOfUsedCable = lengthOfUsedCable;
    }

    public double getPostLineMeasurement() {
        return postLineMeasurement;
    }

    public void setPostLineMeasurement(double postLineMeasurement) {
        this.postLineMeasurement = postLineMeasurement;
    }

    public double getLengthOfNotUsedCable() {
        return lengthOfNotUsedCable;
    }

    public void setLengthOfNotUsedCable(double lengthOfNotUsedCable) {
        this.lengthOfNotUsedCable = lengthOfNotUsedCable;
    }

    public double getLengthOfPostLine() {
        return lengthOfPostLine;
    }

    public void setLengthOfPostLine(double lengthOfPostLine) {
        this.lengthOfPostLine = lengthOfPostLine;
    }

    public double getLengthOfCable() {
        return lengthOfCable;
    }

    public void setLengthOfCable(double lengthOfCable) {
        this.lengthOfCable = lengthOfCable;
    }

    public long getNumberOfUsedPostHead() {
        return numberOfUsedPostHead;
    }

    public void setNumberOfUsedPostHead(long numberOfUsedPostHead) {
        this.numberOfUsedPostHead = numberOfUsedPostHead;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getMeasurementOfLoopWearing() {
        return measurementOfLoopWearing;
    }

    public void setMeasurementOfLoopWearing(double measurementOfLoopWearing) {
        this.measurementOfLoopWearing = measurementOfLoopWearing;
    }

    public String getExpandType() {
        return expandType;
    }

    public void setExpandType(String expandType) {
        this.expandType = expandType;
    }

    public String getTypeOfTransfer() {
        return typeOfTransfer;
    }

    public void setTypeOfTransfer(String typeOfTransfer) {
        this.typeOfTransfer = typeOfTransfer;
    }

    public double getTransferFee() {
        return transferFee;
    }

    public void setTransferFee(double transferFee) {
        this.transferFee = transferFee;
    }

    public String getTypeOfPayment() {
        return typeOfPayment;
    }

    public void setTypeOfPayment(String typeOfPayment) {
        this.typeOfPayment = typeOfPayment;
    }

    public long getDemandNoteNumber() {
        return demandNoteNumber;
    }

    public void setDemandNoteNumber(long demandNoteNumber) {
        this.demandNoteNumber = demandNoteNumber;
    }

    public String getDemandNoteDate() {
        return demandNoteDate;
    }

    public void setDemandNoteDate(String demandNoteDate) {
        this.demandNoteDate = demandNoteDate;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public List<String> getListOfCC() {
        return listOfCC;
    }

    public void setListOfCC(List<String> listOfCC) {
        this.listOfCC = listOfCC;
    }

    @Override
    public Map<String, Object> getPdfParameters() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("btcl_logo_heading", this.BTCL_LOGO_HEADING);
        params.put("recipient_name", nullOrNotNull(this.getRecipientName()));

        putParameterValue(params, "CC", this.getListOfCC());

        params.put("client_name", nullOrNotNull(this.getClientName()));
        params.put("client_address", nullOrNotNull(this.getClientAddress()));
        params.put("exchange_name_and_phone_number", NULL_STRING);
        params.put("requirement_number_and_date", NULL_STRING);
        params.put("date_of_agreement", NULL_STRING);
        params.put("date_of_establishment_transfer_withdrawal", NULL_STRING);
        params.put("date_of_removal_from_old_address", NULL_STRING);
        params.put("date_of_placement_at_new_address", NULL_STRING);

        putParameterValue(params, "distance", this.getDistance());
        putParameterValue(params, "measurement_of_loop_wearing", this.getMeasurementOfLoopWearing());
        params.put("expand_type", nullOrNotNull(this.getExpandType()));
        params.put("type_of_transfer", nullOrNotNull(this.getTypeOfTransfer()));
        putParameterValue(params, "transfer_fee", this.getTransferFee());
        params.put("type_of_payment", nullOrNotNull(this.getTypeOfPayment()));
        putParameterValue(params, "demand_note_number", this.getDemandNoteNumber());
        params.put("demand_note_date", nullOrNotNull(this.getDemandNoteDate()));

        putParameterValue(params, "length_of_used_cable", this.getLengthOfUsedCable());
        putParameterValue(params, "post_line_measurement", this.getPostLineMeasurement());
        putParameterValue(params, "length_of_not_used_cable", this.getLengthOfNotUsedCable());
        putParameterValue(params, "length_of_post_line", this.getLengthOfPostLine());
        putParameterValue(params, "length_of_cable", this.getLengthOfCable());
        putParameterValue(params, "number_of_used_post_head", this.getNumberOfUsedPostHead());

        params.put("null_string", NULL_STRING);
        return params;
    }

    @Override
    public String getResourceFile() {
        return JASPER_TEMPLATE_FILE;
    }

    @Override
    public JRDataSource getJasperDataSource() {
        return new JREmptyDataSource();
    }

    @Override
    public String getOutputFilePath() throws Exception {
        return this.outputFilePath;
    }

    public void populateData(RoleDTO roleDTO, ArrayList<String> membersOfCC, ClientDTO clientDTO, long demandNoteId, long distance) {
        this.setRecipientName(roleDTO.getRoleName());
        this.setListOfCC(membersOfCC);
        this.setClientName(clientDTO.getName() + " " + clientDTO.getLastName());
        this.setClientAddress(clientDTO.getAddress());
        this.setDistance(distance);
        this.setDemandNoteNumber(demandNoteId);
    }

    public String nullOrNotNull(String inputString) {
        if (inputString == null)
            return NULL_STRING;
        else
            return inputString;
    }

    void putParameterValue(Map<String, Object> params, String parameterName, double parameterValue) {
        if (parameterValue == 0.0)
            params.put(parameterName, NULL_STRING);
        else
            params.put(parameterName, Double.toString(parameterValue));
    }

    void putParameterValue(Map<String, Object> params, String parameterName, long parameterValue) {
        if (parameterValue == 0)
            params.put(parameterName, NULL_STRING);
        else
            params.put(parameterName, Long.toString(parameterValue));
    }

    void putParameterValue(Map<String, Object> params, String parameterName, List<String> parameterValue) {
        if (parameterValue == null)
            params.put(parameterName, Arrays.asList(NULL_STRING, NULL_STRING, NULL_STRING, NULL_STRING, NULL_STRING));
        else
            params.put(parameterName, parameterValue);
    }

    public static String generateFilePath(long applicationId) {
        String proposedFileName = applicationId + ".pdf";
        StringBuilder sb = new StringBuilder()
                .append(FileTypeConstants.BASE_PATH)
                .append(LLI_NEW_CONNECTION_ADVICE_NOTE_DIR)
                // TODO:maruf: change system time: old documents will not be found
                .append(TimeConverter.getYear(System.currentTimeMillis()))
                .append(File.separatorChar)
                // TODO:maruf: change system time: old documents will not be found
                .append(TimeConverter.getMonth(System.currentTimeMillis()))
                .append(File.separatorChar);

        try {
            return FileAPI.getInstance().createDirectory(sb.toString()).getPath() + File.separatorChar + proposedFileName;
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
            return sb.toString() + File.separatorChar + proposedFileName;
        }
    }
}