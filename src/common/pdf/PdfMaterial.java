package common.pdf;

import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

public interface PdfMaterial {
	Map<String, Object> getPdfParameters() throws Exception;
	String getResourceFile() throws Exception;
	JRDataSource getJasperDataSource();
	String getOutputFilePath() throws Exception;
}
