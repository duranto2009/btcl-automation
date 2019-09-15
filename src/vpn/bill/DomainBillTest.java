/**
 * 
 */
package vpn.bill;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.bill.BillDTO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class DomainBillTest {

	public static void main(String[] args) throws FileNotFoundException, JRException{
		
		InputStream is = new FileInputStream("C:\\Users\\reve\\workspace_old\\BTCL_NEW\\resources\\domain\\bill\\DomainBill.jasper");
		
		JasperReport jasperReport = (JasperReport)JRLoader.loadObject( is );
		//JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile("/WEB-INF/VpnBillTemplate.jasper");
		
		Map<String, Object> params = new HashMap<>();
		
		params.put( "name", "Kayes bro" );
		params.put( "username", "Kayes bro" );
		params.put( "address", "Kayes bro" );
		params.put( "domainName", "www.bolod.com" );
		params.put( "applicationType", "Regular" );
		
		BillDTO billDTO = new BillDTO();
		
		billDTO.setID( 1000L );
		billDTO.setGenerationTime( 1494485995948L );
		billDTO.setLastPaymentDate( 1494485995948L );
		billDTO.setYear( 2017 );
		billDTO.setDiscount( 100 );
		
		List<BillDTO> bills = new ArrayList<>();
		bills.add( billDTO );
		
		JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource( bills , false );
		
		JasperPrint jasperPrint = JasperFillManager.fillReport( jasperReport, params, itemsJRBean );
		
		JasperExportManager.exportReportToPdfFile( jasperPrint, "E:\\Domain.pdf" );
	}
}
