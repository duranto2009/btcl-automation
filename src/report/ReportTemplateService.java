package report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import connection.DatabaseConnection;

public class ReportTemplateService {
	Logger logger = Logger.getLogger(getClass());
	ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();

	public void saveReportTemplate(String reportTemplateName, List<String> criteriaKeys, List<String> displayKeys, List<String> orderValues) {
		
		ReportTemplateDTO reportTemplateDTO = new ReportTemplateDTO();
		reportTemplateDTO.setReportName(reportTemplateName);
		reportTemplateDTO.setReportCriteria(String.join(",", criteriaKeys));
		reportTemplateDTO.setReportDisplay(String.join(",", displayKeys));
		reportTemplateDTO.setReportOrder(String.join(",", orderValues));
		reportTemplateDTO.setLastModificationTime(System.currentTimeMillis());
		
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			reportTemplateDAO.saveReportTemplate(reportTemplateDTO, databaseConnection);
			databaseConnection.dbTransationEnd();
		} catch (Exception ex) {
			logger.debug(ex);
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
				logger.debug("Exception Saving Report Template :" ,ex2);
			}
		} finally {
			databaseConnection.dbClose();
		}
		
	}

	public List<HashMap<String, String>> getAllReportTemplateIdName() {
		List<HashMap<String, String>> allReportTemplateIdName = new ArrayList<HashMap<String, String>>();
		
		List<ReportTemplateDTO> allReportTemplateDTO = new ArrayList<ReportTemplateDTO>();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			allReportTemplateDTO = reportTemplateDAO.getAllReportTemplateDTO(databaseConnection);
			databaseConnection.dbTransationEnd();
		} catch (Exception ex) {
			logger.debug(ex);
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
				logger.debug("Exception Loading Report Template List:" ,ex2);
			}
		} finally {
			databaseConnection.dbClose();
		}
		
		for(int i=0;i<allReportTemplateDTO.size();i++){
			String reportTemplateID = String.valueOf(allReportTemplateDTO.get(i).getReportID());
			String reportTemplateName = allReportTemplateDTO.get(i).getReportName();
			
			HashMap<String, String> mapForOneReportTemplate = new HashMap<String, String>();
			mapForOneReportTemplate.put("id", reportTemplateID);
			mapForOneReportTemplate.put("name", reportTemplateName);
			
			allReportTemplateIdName.add(mapForOneReportTemplate);
		}
		return allReportTemplateIdName;
	}

	public ReportTemplateDTO getReportTemplateDTOByReportTemplateID(String reportTemplateID) {
		ReportTemplateDTO reportTemplateDTO = new ReportTemplateDTO();
		
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			reportTemplateDTO = reportTemplateDAO.getReportTemplateDTOByReportTemplateID(Long.parseLong(reportTemplateID), databaseConnection);
			databaseConnection.dbTransationEnd();
		} catch (Exception ex) {
			logger.debug(ex);
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
				logger.debug("Exception Loading Report Template List:" ,ex2);
			}
		} finally {
			databaseConnection.dbClose();
		}
		
		return reportTemplateDTO;
	}

}
