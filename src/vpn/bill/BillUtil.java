/**
 * 
 */
package vpn.bill;

import annotation.AllowPdfGeneration;
import common.StringUtils;
import common.bill.BillConstants.BillConfiguration;
import common.bill.BillDTO;
import common.bill.BillService;
import connection.DatabaseConnection;
import util.SqlGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BillUtil {
	
//	public static int getNearEndLoopDistance( VpnLinkDTO vpnLinkDTO, DatabaseConnection databaseConnection ) throws Exception{
//
//		VpnNearEndDTO nearEndDTO = new VpnLinkService().getNearEndByNearEndID( vpnLinkDTO.getNearEndID() );
//
//		return getNearEndLoopDistance( nearEndDTO, databaseConnection );
//	}
//
//	public static int getNearEndLoopDistance( VpnNearEndDTO nearEndDTO, DatabaseConnection databaseConnection ) throws Exception {
//
//		int nearEndLoopDistance = 0;
//		VpnFRResponseExternalDTO nearEndResponse = new VpnDAO().getLatestExternalFRForNearEnd( nearEndDTO, databaseConnection );
//		if(nearEndResponse == null) return 0;
//		if( nearEndDTO.getParentEndPointID() > 0 )
//			nearEndLoopDistance = 0;
//		else
//			nearEndLoopDistance = nearEndResponse.getDistanceTotal();
//
//		return nearEndLoopDistance;
//	}
//
//	public static int getFarEndLoopDistance( VpnLinkDTO vpnLinkDTO, DatabaseConnection databaseConnection ) throws Exception{
//
//		VpnFarEndDTO farEndDTO = SqlGenerator.getObjectByID( VpnFarEndDTO.class, vpnLinkDTO.getFarEndID(), databaseConnection );
//
//		return getFarEndLoopDistance( farEndDTO, databaseConnection );
//	}
//
//	public static int getFarEndLoopDistance( VpnFarEndDTO farEndDTO, DatabaseConnection databaseConnection ) throws Exception {
//
//		int farEndLoopDistance = 0;
//
//		VpnFRResponseExternalDTO farEndResponse = new VpnDAO().getLatestExternalFRForFarEnd( farEndDTO, databaseConnection );
//		if(farEndResponse == null)
//		{
//			return 0;
//		}
//		if( farEndDTO.getParentEndPointID() > 0 )
//			farEndLoopDistance = 0;
//		else
//			farEndLoopDistance = farEndResponse.getDistanceTotal();
//
//		return farEndLoopDistance;
//	}
//
//	public static double getNearEndLoopCharge( VpnLinkDTO vpnLinkDTO, DatabaseConnection databaseConnection ) throws Exception{
//
//		CommonChargeDTO commonCharge = new VpnCommonChargeConfigService().getCurrentActiveCommonChargeDTO();
//
//		VpnNearEndDTO nearEndDTO = new VpnLinkService().getNearEndByNearEndID( vpnLinkDTO.getNearEndID() );
//
//		int nearEndLoopDistance = getNearEndLoopDistance( nearEndDTO, databaseConnection);
//
//		BigDecimal OFCoreCharge = BigDecimal.valueOf( 1000 );;
//
//		if( nearEndLoopDistance > 500 ) {
//			OFCoreCharge = ( new BigDecimal(nearEndLoopDistance-500) ).multiply( new BigDecimal(commonCharge.getOFChargePerMeter( nearEndDTO.getDistrictID() ) ) );
//			OFCoreCharge = OFCoreCharge.add( BigDecimal.valueOf( 1000 ) );
//		}
//
//		return OFCoreCharge.setScale( 2 ).doubleValue();
//	}
//
//	public static double getFarEndLoopCharge( VpnLinkDTO vpnLinkDTO, DatabaseConnection databaseConnection ) throws Exception{
//
//		CommonChargeDTO commonCharge = new VpnCommonChargeConfigService().getCurrentActiveCommonChargeDTO();
//
//		VpnFarEndDTO farEndDTO = SqlGenerator.getObjectByID( VpnFarEndDTO.class, vpnLinkDTO.getFarEndID(), databaseConnection );
//
//		int farEndLoopDistance = getFarEndLoopDistance( farEndDTO, databaseConnection);
//
//		BigDecimal OFCoreCharge = BigDecimal.valueOf( 1000 );;
//
//		if( farEndLoopDistance > 500 ) {
//			OFCoreCharge = ( new BigDecimal(farEndLoopDistance-500) ).multiply( new BigDecimal(commonCharge.getOFChargePerMeter( farEndDTO.getDistrictID() ) ) );
//			OFCoreCharge = OFCoreCharge.add( BigDecimal.valueOf( 1000 ) );
//		}
//
//		return OFCoreCharge.setScale( 2 ).doubleValue();
//	}
//
//	@Deprecated
//	public static double getTotalOfCoreCharge( int nearEndLoopDistance, int farEndLoopDistance ) throws Exception{
//
//		CommonChargeDTO commonCharge = new VpnCommonChargeConfigService().getCurrentActiveCommonChargeDTO();
//
//		BigDecimal OFCoreCharge = ( new BigDecimal(nearEndLoopDistance).add( new BigDecimal( farEndLoopDistance) ) ).multiply( new BigDecimal(commonCharge.getOFChargePerMeter() ) );
//
//		double totalOFCoreCharge = OFCoreCharge.setScale( 2 ).doubleValue();
//
//		return totalOFCoreCharge;
//	}
//
//	public static double getBandwidthCharge( VpnLinkDTO vpnLinkDTO ) throws Exception{
//
//		double distanceBetweenDistricts = ((APIUtil)ServiceDAOFactory.getService(APIUtil.class)).getDistanceByLinkID( vpnLinkDTO.getID() ).getDistance();
//
//		double bandwidth = vpnLinkDTO.getVpnBandwidth();
//		int bandwidthType = vpnLinkDTO.getVpnBandwidthType();
//
//		if( bandwidthType == EntityTypeConstant.BANDWIDTH_TYPE_GB )
//			bandwidth *= 1024; //Converting to MB
//
//		double bandwidthCharge = ServiceDAOFactory.getService(CostConfigService.class).getCostTableDTOForSpecificTimeByModuleIDAndCategoryID(System.currentTimeMillis(), ModuleConstants.Module_ID_VPN, 1)
//
//				.getCostByRowValueAndColumnValue(bandwidth, distanceBetweenDistricts);
//
//		return bandwidthCharge;
//	}
	
	public static List<BillDTO> getAdvanceMonthlyBillList( long rootReqID, DatabaseConnection databaseConnection ) throws Exception{
		
		ArrayList<BillDTO> billList = (ArrayList<BillDTO>)SqlGenerator.getAllObjectList(BillDTO.class, databaseConnection,
				" where " + SqlGenerator.getColumnName( BillDTO.class, "reqID" ) + " = " + rootReqID
				+ " and " + SqlGenerator.getColumnName( BillDTO.class, "isDeleted" ) + " = 0 " );
		
		return billList;
	}
	
	public static void populateMapWithBillConfigurations( int moduleID, int docType, Map<String, Object> params ) throws Exception {
		
		List<BillConfigurationDTO> billConfigurationDTOs = BillService.getBillConfiguration( moduleID, docType );
		
		populateMapWithBillConfigurations( billConfigurationDTOs, params );
	}
	
	public static void populateMapWithBillConfigurations( List<BillConfigurationDTO> billConfigurationDTOs, Map<String, Object> params ) {
		
		for( BillConfigurationDTO billConfigurationDTO: billConfigurationDTOs ){
			
			switch ( billConfigurationDTO.getHeaderFooterID() ) {
			case BillConfiguration.FOOTER_TEXT_1:
				params.put( "footer_text_1", billConfigurationDTO.getText() );
				break;
			case BillConfiguration.FOOTER_TEXT_2:
				params.put( "footer_text_2", billConfigurationDTO.getText() );
				break;
			case BillConfiguration.FOOTER_TEXT_3:
				params.put( "footer_text_3", billConfigurationDTO.getText() );
				break;
			case BillConfiguration.HEADER_TEXT_1:
				params.put( "header_text_1", billConfigurationDTO.getText() );
				break;
			case BillConfiguration.HEADER_TEXT_2:
				params.put( "header_text_2", billConfigurationDTO.getText() );
				break;
			}
		}
	}

	public static String getBIllTemplateLocation( BillDTO billDTO) throws Exception {
		
		String className = billDTO.getClassName(); 
		
		Class<?> billClassObject = Class.forName(className);
		
		AllowPdfGeneration allowPdfGeneration = (AllowPdfGeneration)billClassObject.getAnnotation(AllowPdfGeneration.class);
		
		if( allowPdfGeneration == null ){
			
			throw new Exception("Bill template annotation is not found");
		}
		
		String billTemplateLocation = allowPdfGeneration.billLocator().newInstance().getTemplateFilename( billDTO );
		
		if( StringUtils.isBlank( billTemplateLocation ) ){
			throw new Exception("Invalid bill template location");
		}
		return billTemplateLocation;
	}
}
