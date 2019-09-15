package report;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import connection.DatabaseConnection;
import util.SqlGenerator;

public class ReportTemplateDAO {
	Logger logger = Logger.getLogger(getClass());

	public void saveReportTemplate(ReportTemplateDTO reportTemplateDTO, DatabaseConnection databaseConnection){
		try {
			SqlGenerator.insert(reportTemplateDTO, ReportTemplateDTO.class, databaseConnection, false);
		} catch (Exception e) {
			logger.debug("Exception Saving Report Template in DAO:" ,e);
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public List<ReportTemplateDTO> getAllReportTemplateDTO(DatabaseConnection databaseConnection) {
		List<ReportTemplateDTO> allReportTemplateDTO = new ArrayList<ReportTemplateDTO>();
		try {
			allReportTemplateDTO = (List<ReportTemplateDTO>) SqlGenerator.getAllObjectList(ReportTemplateDTO.class, databaseConnection);
		} catch (Exception e) {
			logger.debug("Exception Loading Report Template List in DAO:" ,e);
			e.printStackTrace();
		}
		return allReportTemplateDTO;
	}

	public ReportTemplateDTO getReportTemplateDTOByReportTemplateID(long reportTemplateID, DatabaseConnection databaseConnection) {
		ReportTemplateDTO reportTemplateDTO = new ReportTemplateDTO();
		
		try {
			reportTemplateDTO = (ReportTemplateDTO) SqlGenerator.getObjectByID(ReportTemplateDTO.class, reportTemplateID, databaseConnection);
		} catch (Exception e) {
			logger.debug("Exception Loading Report Template in DAO:" ,e);
			e.printStackTrace();
		}
		return reportTemplateDTO;
	}

}
