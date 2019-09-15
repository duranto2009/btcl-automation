package jUnitTesting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class JasperTestingDomain {
	public static void main(String[] args) throws Exception {
		
		InputStream resourceContent = JasperTestingDomain.class.getResourceAsStream("/domain/bill/domain_invoice.jasper");
		OutputStream output = new FileOutputStream(new File("C:\\Users\\Acer\\Desktop\\domain_invoice.pdf"));
		writePdfToStream( resourceContent, output, new TestJasperDomain[] {new TestJasperDomain()} );
		
	}
	private static void writePdfToStream( InputStream resourceContent, OutputStream outputStream, TestJasperDomain[] test ) throws Exception{
		
		
		JasperReport jasperReport = (JasperReport)JRLoader.loadObject( resourceContent );
		List<TestJasperDomain> bills = Arrays.asList( test );
		Map <String, Object> params = new TestJasperDomain().getPdfParamMap();
		//If second param is passed as false, then the attributes name in DTO have to exactly same as
		//Field name in jasper template. if passed as true, we have to set custom jasper field and dto attribute 
		//mapping in jasper template. Currenlt we assume that no mapping is required.
		JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource( bills, false );
		
		
		JasperPrint jasperPrint = JasperFillManager.fillReport( jasperReport, params, itemsJRBean );
		
		JasperExportManager.exportReportToPdfStream( jasperPrint, outputStream );
	}
	
}
