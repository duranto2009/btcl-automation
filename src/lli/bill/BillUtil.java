/**
 * 
 */
package lli.bill;

import annotation.AllowPdfGeneration;
import common.StringUtils;
import common.bill.BillConstants;
import common.bill.BillConstants.BillConfiguration;
import common.bill.BillDTO;
import vpn.bill.BillConfigurationDTO;

import java.util.List;
import java.util.Map;

public class BillUtil {
	/*
	public static int getFarEndLoopDistance( LliLinkDTO lliLinkDTO, DatabaseConnection databaseConnection ) throws Exception{
		
		LliFarEndDTO farEndDTO = new LliLinkService().getFarEndByFarEndID( lliLinkDTO.getFarEndID() );
		
		return getFarEndLoopDistance( farEndDTO, databaseConnection );
	}
	
	public static int getFarEndLoopDistance( LliFarEndDTO farEndDTO, DatabaseConnection databaseConnection ) throws Exception{
		
		int farEndLoopDistance = 0;
		
		LliFRResponseExternalDTO farEndResponse = new LliDAO().getLatestExternalFRForFarEnd( farEndDTO, databaseConnection );
		
		if( farEndResponse == null )
			return 0;
		
		if( farEndDTO.getParentEndPointID() > 0 ) 
			farEndLoopDistance = 0;
		else
			farEndLoopDistance = farEndResponse.getDistanceTotal();
		
		return farEndLoopDistance;
	}
	
	public static double getFarEndLoopCharge( LliLinkDTO lliLinkDTO, DatabaseConnection databaseConnection ) throws Exception{
		
		LLIFixedCostConfigurationDTO commonCharge = new LLICostConfigurationService().getCurrentActiveLLI_FixedCostConfigurationDTO();
		
		LliFarEndDTO farEndDTO = new LliLinkService().getFarEndByFarEndID( lliLinkDTO.getFarEndID() );
		
		int farEndLoopDistance = getFarEndLoopDistance( farEndDTO, databaseConnection);
		
		BigDecimal OFCoreCharge = BigDecimal.valueOf( 1000 );
		
		if( farEndLoopDistance > 500 ) {
//			OFCoreCharge = ( new BigDecimal(farEndLoopDistance-500) ).multiply( new BigDecimal(commonCharge.getOFChargePerMeter( farEndDTO.getDistrictID() ) ) );
			OFCoreCharge = OFCoreCharge.add( BigDecimal.valueOf( 1000 ) );
		}
		
		return OFCoreCharge.setScale( 2 ).doubleValue();
	}
	
//	@Deprecated
//	public static double getTotalOfCoreCharge( int nearEndLoopDistance, int farEndLoopDistance ) throws Exception{
//		
//		LLIFixedCostConfigurationDTO commonCharge = new LLICostConfigurationService().getCurrentActiveLLI_FixedCostConfigurationDTO();
//		
////		BigDecimal OFCoreCharge = ( new BigDecimal(nearEndLoopDistance).add( new BigDecimal( farEndLoopDistance) ) ).multiply( new BigDecimal(commonCharge.getOFChargePerMeter() ) );
//		
////		double totalOFCoreCharge = OFCoreCharge.setScale( 2 ).doubleValue();
//		
////		return totalOFCoreCharge;
//	}

	public static double getBandwidthCharge( LliLinkDTO lliLinkDTO ) throws Exception{
		
		double bandwidth = lliLinkDTO.getLliBandwidth();
		int bandwidthType = lliLinkDTO.getLliBandwidthType();
		
		if( bandwidthType == EntityTypeConstant.BANDWIDTH_TYPE_GB )
			bandwidth *= 1024; //Converting to MB
		
		int year = EndPointConstants.yearConnectionTypeMapping.get( lliLinkDTO.getConnectionType() );
		
		double bandwidthCharge = new CostConfigService().getCostLLI( bandwidth, year, lliLinkDTO.getClientID() );

		return bandwidthCharge;
	}
	*/
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

	public static double getCostForIpAddress( int noOfAdditionalIp ) {
		
		int remaining = noOfAdditionalIp;
		double cost = 0;
		
		for( Integer[] costMap : BillConstants.IpAddressCostMapping ) {
			
			if( remaining > 0 ) {
				
				if( remaining > costMap[0] ) {
					
					cost += costMap[0] * costMap[1];
				}else {
					
					cost += remaining * costMap[1];
				}
				
				remaining -= costMap[0];
			}
		}
		
		if( remaining > 0 ) {
			
			cost += BillConstants.DEFAULT_IP_ADDRESS_COST * remaining;
		}
		
		return cost;
	}
}
