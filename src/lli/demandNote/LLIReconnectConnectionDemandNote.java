package lli.demandNote;

import annotation.*;
import api.ClientAPI;
import api.FileAPI;
import common.ModuleConstants;
import common.bill.BillConstants;
import common.bill.BillDTO;
import common.pdf.PdfMaterial;
import common.repository.AllClientRepository;
import file.FileTypeConstants;
import lli.Application.ReviseClient.ReviseDTO;
import lli.Application.ReviseClient.ReviseService;
import lli.connection.LLIConnectionConstants;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import util.EnglishNumberToWords;
import util.KeyValuePair;
import util.ServiceDAOFactory;
import util.TimeConverter;
import validator.annotation.NonNegative;
import vpn.client.ClientDetailsDTO;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@AccountingLogic(LLIReconnectConnectionBusinessLogic.class)
@TableName("at_lli_reconnect_connection_dn")
@ForeignKeyName("lli_rc_dn_parent_bill_id")
public class LLIReconnectConnectionDemandNote extends BillDTO implements PdfMaterial {
    public LLIReconnectConnectionDemandNote() {
        setClassName(this.getClass().getCanonicalName());
        setBillType(BillConstants.DEMAND_NOTE);
        setEntityTypeID(LLIConnectionConstants.ENTITY_TYPE);
    }

    @PrimaryKey
    @ColumnName("lli_rc_dn_id")
    long reconnectConnectionDemandNoteID;

    @ColumnName("lli_rc_dn_reconnection_charge")
    @NonNegative("Reconnection charge")
    double reconnectionCharge;

    @ColumnName("lli_rc_dn_other_cost")
    @NonNegative("Other Cost")
    double otherCost;

    ReviseDTO reviseDTO;

    public double getReconnectionCharge() {
        return reconnectionCharge;
    }

    public void setReconnectionCharge(double reconnectionCharge) {
        this.reconnectionCharge = reconnectionCharge;
    }

    public double getOtherCost() {
        return otherCost;
    }

    public void setOtherCost(double otherCosts) {
        this.otherCost = otherCosts;
    }

    public long getReconnectConnectionDemandNoteID() {
        return reconnectConnectionDemandNoteID;
    }

    public void setReconnectConnectionDemandNoteID(long id) {
        this.reconnectConnectionDemandNoteID = id;
    }

    @Override
    public Map<String, Object> getPdfParameters() throws Exception {
        Map<String, Object> params = new HashMap<>();

        KeyValuePair<ClientDetailsDTO, ClientContactDetailsDTO> pair =
                ClientAPI.getInstance().getPairOfClientDetailsAndClientContactDetails(
                        super.getClientID(), ModuleConstants.Module_ID_LLI, ClientContactDetailsDTO.BILLING_CONTACT);
        ClientDetailsDTO clientDetailsDTO = pair.key;
        ClientContactDetailsDTO contactDetailsDTO = pair.value;

        params.put("logo", "../../images/common/btcl_logo_heading.png");
        params.put("footerLeft", "Powered By Reve Systems");
        params.put("footerRight", "Bangladesh Telecommunications Company Limited");
        params.put("NB", "NB: ( Client will arrange and maintain local loops from BTCL switch to his offices on his own ).");

        params.put("clientFullName", contactDetailsDTO.getRegistrantsName() + " " + contactDetailsDTO.getRegistrantsLastName());
        params.put("clientAddress", contactDetailsDTO.getAddress());
        params.put("clientEmail", contactDetailsDTO.getEmail() != null ? contactDetailsDTO.getEmail() : "N/A");
        params.put("clientLoginName", clientDetailsDTO.getLoginName());

        params.put("billGenerationDate", TimeConverter.getDateTimeStringByMillisecAndDateFormat(super.getGenerationTime(), "dd/MM/yyyy"));
        params.put("billLastPaymentDate", TimeConverter.getDateTimeStringByMillisecAndDateFormat(super.getLastPaymentDate(), "dd/MM/yyyy"));
        params.put("invoiceID", super.getID() + "");
        params.put("discount", String.format("%.2f", super.getDiscount()));
        params.put("VAT", String.format("%.2f", super.getVAT()));
        params.put("total", String.format("%.2f", super.getNetPayable()));
        params.put("demandedAmount", EnglishNumberToWords.convert((long) Math.ceil(super.getNetPayable())));

        params.put("reconnectionCharge", String.format("%.2f", this.getReconnectionCharge()));
        params.put("otherCost", String.format("%.2f", this.getOtherCost()));

        params.put("connectionName", "");

        if (reviseDTO == null) {
            reviseDTO = ServiceDAOFactory.getService(ReviseService.class).getApplicationByDemandNoteId(super.getID());
        }

        String clientSuggestedDate = TimeConverter.getDateTimeStringByMillisecAndDateFormat(reviseDTO.getSuggestedDate(), "dd/MM/yyyy");

        String demandNoteCause = "In the context of your application in <b>"
                + clientSuggestedDate + "</b> demand note of reconnect connection for <b>"
                + AllClientRepository.getInstance().getClientByClientID(reviseDTO.getClientID()).getLoginName()
                + "</b> has been issued.";

        params.put("demandNoteCause", demandNoteCause);
        params.put("connectionAddress", "N/A");
        return params;
    }

    @Override
    public String getResourceFile() {
        return BillConstants.LLI_DEMAND_NOTE_RECONNECT_TEMPLATE;
    }

    @Override
    public JRDataSource getJasperDataSource() {
        return new JRBeanCollectionDataSource(Arrays.asList(this), false);
    }

    @Override
    public String getOutputFilePath() throws Exception {
        String proposedFileName = "demand-note-reconnect-" + super.getID() + ".pdf";

        StringBuilder sb = new StringBuilder();
        sb.append(FileTypeConstants.BASE_PATH);
        sb.append(FileTypeConstants.LLI_BILL_DIRECTORY);
        sb.append(TimeConverter.getYear(super.getGenerationTime()));
        sb.append(File.separatorChar);
        sb.append(TimeConverter.getMonth(super.getGenerationTime()));
        sb.append(File.separatorChar);

        File file = FileAPI.getInstance().createDirectory(sb.toString());
        return file.getPath() + File.separatorChar + proposedFileName;
    }

    public ReviseDTO getReviseDTO() {
        return reviseDTO;
    }

    public void setReviseDTO(ReviseDTO reviseDTO) {
        this.reviseDTO = reviseDTO;
    }
}
