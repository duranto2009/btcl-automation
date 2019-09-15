/**
 * 
 */
package vpn.bill;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import annotation.AllowPdfGeneration;
import annotation.ColumnName;
import annotation.ForeignKeyName;
import annotation.PrimaryKey;
import annotation.TableName;
import common.ModuleConstants;
import common.RegistrantTypeConstants;
import common.bill.BillConstants;
import common.bill.BillConstants.BillConfiguration;
import common.bill.BillDAO;
import common.bill.BillDTO;
import common.bill.BillService;
import common.repository.AllClientRepository;
import connection.DatabaseConnection;
import vpn.client.ClientDetailsDTO;
import vpn.client.ClientService;
import vpn.clientContactDetails.ClientContactDetailsDTO;

/**
 * @author Alam
 */
@AllowPdfGeneration(billLocator = VpnBillTemplateLocator.class)
@TableName("at_bill_vpn_shift")
@ForeignKeyName("billvpnBillID")
public class VpnBillShiftDTO extends BillDTO{
	
	@PrimaryKey
	@ColumnName("billvpnID")
	Long vpnBillID;
	
	String linkName = "Dummy Link name";
	String customerName = "Dummy Customer";
	String customerAddress = "Dummy Address";
	
	long invoiceID = 1234L;
	@ColumnName("billvpnBWConnectionCharge")
	Double bwConnectionCharge = 0.0;
	@ColumnName("billvpnOFCInstallationCharge")
	Double ofcInstallationCharge = 0.0;
	@ColumnName("billvpnOFCLayingCost")	
	Double ofcLayingCost = 0.0;
	@ColumnName("billvpnEstablishmentCost")		
	Double establishmentCost = 0.0;
	@ColumnName("billvpnMediaConverterPrice")
	Double mediaConverterPrice = 0.0;
	@ColumnName("billvpnSFPModuleCost")
	Double sfpModuleCost = 0.0;
	@ColumnName("billvpnOthersCost")
	Double others = 0.0;
	@ColumnName("billvpnSecurityCharge")
	Double securityCharge = 0.0;
	@ColumnName("billvpnOFCCost")
	Double ofcCharge = 0.0;
	
	@ColumnName("billvpnOFCChargeNearEndMonthly")
	Double localEndOFC = 0.0;
	@ColumnName("billvpnOFCChargeFarEndMonthly")
	Double remoteEndOFC = 0.0;
	
	@ColumnName("billvpnBWChargeMonthly")
	Double bwCharge = 0.0;
	
	@ColumnName("billvpnUpgradationCharge")
	Double upgradationCharge = 0.0;
	
	@ColumnName("billvpnExistingSecurityMoney")
	Double existingSecurityMoney = 0.0;


	public Double getExistingSecurityMoney() {
		return existingSecurityMoney;
	}

	public void setExistingSecurityMoney(Double existingSecurityMoney) {
		setGrandTotal( getGrandTotal() - existingSecurityMoney );
		this.existingSecurityMoney = existingSecurityMoney;
	}

	public Double getUpgradationCharge() {
		return upgradationCharge;
	}

	public void setUpgradationCharge(Double upgradationCharge) {
		setGrandTotal( getGrandTotal() + upgradationCharge );
		this.upgradationCharge = upgradationCharge;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public Double getOfcInstallationCharge() {
		return ofcInstallationCharge;
	}

	public void setOfcInstallationCharge(Double ofcInstallationCharge) {
		this.ofcInstallationCharge = ofcInstallationCharge;
		setGrandTotal( getGrandTotal() + ofcInstallationCharge );
	}

	public Double getOfcLayingCost() {
		return ofcLayingCost;
	}

	public void setOfcLayingCost(Double ofcLayingCost) {
		this.ofcLayingCost = ofcLayingCost;
		setGrandTotal( getGrandTotal() + ofcLayingCost );
	}

	public Double getEstablishmentCost() {
		return establishmentCost;
	}

	public void setEstablishmentCost(Double establishmentCost) {
		this.establishmentCost = establishmentCost;
		setGrandTotal( getGrandTotal() + establishmentCost );
	}

	public Double getMediaConverterPrice() {
		return mediaConverterPrice;
	}
	
	public void setMediaConverterPrice(Double mediaConverterPrice) {
		this.mediaConverterPrice = mediaConverterPrice;
		setGrandTotal( getGrandTotal() + mediaConverterPrice );
	}

	public Double getSfpModuleCost() {
		return sfpModuleCost;
	}

	public void setSfpModuleCost(Double sfpModuleCost) {
		this.sfpModuleCost = sfpModuleCost;
		setGrandTotal( getGrandTotal() + sfpModuleCost );
	}

	public Double getOthers() {
		return others;
	}

	public void setOthers(Double others) {
		this.others = others;
		setGrandTotal( getGrandTotal() + others );
	}

	public Double getSecurityCharge() {
		return securityCharge;
	}

	public void setSecurityCharge(Double securityCharge) {
		
		this.securityCharge = securityCharge;
		setGrandTotal( getGrandTotal() + securityCharge );
	}

	public Double getOfcCharge() {
		return ofcCharge;
	}

	public void setOfcCharge(Double ofcCharge) {
		this.ofcCharge = ofcCharge;
		setGrandTotal( getGrandTotal() + ofcCharge );
	}

	public Double getLocalEndOFC() {
		return localEndOFC;
	}

	public void setLocalEndOFC(Double localEndOFC) {
		this.localEndOFC = localEndOFC;
		setGrandTotal( getGrandTotal() + localEndOFC );
	}

	public Double getRemoteEndOFC() {
		return remoteEndOFC;
	}

	public void setRemoteEndOFC(Double remoteEndOFC) {
		this.remoteEndOFC = remoteEndOFC;
		setGrandTotal( getGrandTotal() + remoteEndOFC );
	}

	public Double getBwCharge() {
		return bwCharge;
	}

	public void setBwCharge(Double bwCharge) {
		this.bwCharge = bwCharge;
		setGrandTotal( getGrandTotal() + bwCharge );
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public Long getVpnBillID() {
		return vpnBillID;
	}

	public void setVpnBillID(Long vpnBillID) {
		this.vpnBillID = vpnBillID;
	}

	public Double getBwConnectionCharge() {
		return bwConnectionCharge;
	}

	public void setBwConnectionCharge(Double bwConnectionCharge) {
		this.bwConnectionCharge = bwConnectionCharge;
		setGrandTotal( getGrandTotal() + bwConnectionCharge );
	}

	public long getInvoiceID() {
		return super.getID();
	}

	public void setInvoiceID(long invoiceID) {
		this.invoiceID = invoiceID;
	}

	@Override
	public String toString() {
		return "VpnBillDTO [bwConnectionCharge=" + bwConnectionCharge + ", ofcInstallationCharge="
				+ ofcInstallationCharge + ", ofcLayingCost=" + ofcLayingCost + ", establishmentCost="
				+ establishmentCost + ", mediaConverterPrice=" + mediaConverterPrice + ", sfpModuleCost="
				+ sfpModuleCost + ", others=" + others + ", securityCharge=" + securityCharge + ", ofcCharge="
				+ ofcCharge + ", localEndOFC=" + localEndOFC + ", remoteEndOFC=" + remoteEndOFC + ", bwCharge="
				+ bwCharge + "]";
	}

	@Override
	public void payBill(DatabaseConnection databaseConnection, Object...objects ) throws Exception {
		//TODO Need to be implemented later
	}
	

	public Map<String, Object> getPdfParamMap(DatabaseConnection databaseConnection) throws Exception{
		
		Map<String, Object> params = new HashMap<>();
		
		params.put( "logo", "../../images/common/logo.png" );
		
		ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getVpnClientByClientID( getClientID(), ModuleConstants.Module_ID_VPN );
		
		if( clientDetailsDTO != null ){
			
			List<ClientContactDetailsDTO> clientContactDetailsDTO = (List<ClientContactDetailsDTO>) new ClientService().getVpnContactDetailsListByClientID( clientDetailsDTO.getId() );
			
			if( clientContactDetailsDTO != null && clientContactDetailsDTO.size() > 0 ){
				
				ClientContactDetailsDTO contactDetailsDTO = clientContactDetailsDTO.get(0);
				
				params.put( "customerName", contactDetailsDTO.getRegistrantsName() + " " + contactDetailsDTO.getRegistrantsLastName() );
				params.put( "customerAddress", contactDetailsDTO.getAddress() );
				params.put( "customerType", RegistrantTypeConstants.VPN_REGISTRANT_TYPE.get( clientDetailsDTO.getRegistrantType() ) );
			}	
		}
		else {
			
			params.put( "customerName", this.getCustomerName() );
			params.put( "customerAddress", this.getCustomerAddress() );
			params.put( "customerType", "Demo Customer Type" );
		}
		
		BillUtil.populateMapWithBillConfigurations( ModuleConstants.Module_ID_VPN, this.getBillType(), params);
		
		return params;
	}

	public static VpnBillShiftDTO getDummyBill( int docType ) {
		
		VpnBillShiftDTO vpnBillDTO = new VpnBillShiftDTO();
		vpnBillDTO.setBillType( docType );
		return vpnBillDTO;
	}
}
