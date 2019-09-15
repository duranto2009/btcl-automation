package websecuritylog;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import annotation.Transactional;
import common.Logger;
import connection.DatabaseConnection;
import login.LoginDTO;
import util.NavigationService;
import util.TimeConverter;

public class WebSecurityLogService implements NavigationService {
	WebSecurityLogDAO webSecurityDAO = new WebSecurityLogDAO();
	Logger logger = Logger.getLogger(WebSecurityLogService.class);

	public WebSecurityLogDTO getDomainByID(long id) {
		WebSecurityLogDTO webSecurityDTO = new WebSecurityLogDTO();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();

			webSecurityDTO = webSecurityDAO.getWebSecurityLogById(id, databaseConnection);

			databaseConnection.dbTransationEnd();
		} catch (Exception ex) {
			logger.fatal("Fatal", ex);
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
			}
		} finally {
			databaseConnection.dbClose();
		}
		return webSecurityDTO;
	}

	public WebSecurityLogDTO addNewLog(WebSecurityLogDTO webSecurityDTO) throws Exception {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();

			//escaping characters before insert
			webSecurityDTO.setUsername(escapeHtml4(webSecurityDTO.getUsername()));
			webSecurityDTO.setPassword(escapeHtml4(webSecurityDTO.getPassword()));
			webSecurityDTO.setUrl(escapeHtml4(webSecurityDTO.getUrl()));
			webSecurityDTO.setDescription(escapeHtml4(webSecurityDTO.getDescription()));
			
			webSecurityDAO.insertWebSecurityLog(webSecurityDTO, databaseConnection);
			databaseConnection.dbTransationEnd();
		} catch (Exception ex) {
			logger.fatal("Fatal", ex);
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
				logger.fatal("Fatal", ex2);
			}
			throw ex;
		} finally {
			databaseConnection.dbClose();
		}
		return webSecurityDTO;
	}

	@Override
	public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
		List<Long> logList = new ArrayList<Long>();

		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();

			logList = webSecurityDAO.getIDs(databaseConnection);

			databaseConnection.dbTransationEnd();
		} catch (Exception ex) {
			logger.fatal("Fatal", ex);
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
			}
		} finally {
			databaseConnection.dbClose();
		}

		return logList;
	}

	@Override
	public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects)
			throws Exception {
		// TODO Auto-generated method stub
		List<Long> logList = new ArrayList<Long>();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			logList = /* (List<Long>) getIDs(loginDTO, objects); **/ webSecurityDAO
					.getLogListBySearchCriteri(searchCriteria, databaseConnection);
			databaseConnection.dbTransationEnd();
		} catch (Exception ex) {
			logger.fatal("Fatal", ex);
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
			}
		} finally {
			databaseConnection.dbClose();
		}
		return logList;
	}

	@Override
	public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
		List<WebSecurityLogDTO> webSecurityLogDTOs = new ArrayList<WebSecurityLogDTO>();

		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			webSecurityLogDTOs = webSecurityDAO.getLogDTOListByIDList((List<Long>) recordIDs, databaseConnection);

			databaseConnection.dbTransationEnd();
		} catch (Exception ex) {
			logger.fatal("Fatal", ex);
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
			}
		} finally {
			databaseConnection.dbClose();
		}

		return webSecurityLogDTOs;
	}
	
	
	//charts begin
	public List<HashMap<String, String>> getAllUserAttemptsByAttemptCategoryAndDuration(int attemptCategory, String start, String end) throws Exception{
		List<HashMap<String, String>> allAttemptsByAttemptCategoryAndDuration = new ArrayList<HashMap<String, String>>();
		
		if(end.equals("-1")){
			DateTime tempDate = new DateTime(TimeConverter.getStartTimeOfTheDay(System.currentTimeMillis()));
			DateTime startDate = tempDate.minusDays(Integer.parseInt(start));
			
			DateTime endDate = new DateTime(System.currentTimeMillis());
			allAttemptsByAttemptCategoryAndDuration = webSecurityDAO.getAllUserAttemptsByAttemptCategoryAndDuration(attemptCategory, startDate, endDate);
		}else{
			long tempDate = TimeConverter.getStartTimeOfTheDay(DateTime.parse(start, DateTimeFormat.forPattern("dd/MM/yyyy")).getMillis());
			DateTime startDate = new DateTime(tempDate);
			
			tempDate = TimeConverter.getStartTimeOfTheDay(DateTime.parse(end, DateTimeFormat.forPattern("dd/MM/yyyy")).getMillis());
			DateTime endDate = new DateTime(tempDate).plusDays(1);
			
			allAttemptsByAttemptCategoryAndDuration = webSecurityDAO.getAllUserAttemptsByAttemptCategoryAndDuration(attemptCategory, startDate, endDate);
		}
		return allAttemptsByAttemptCategoryAndDuration;
	}
	public List<HashMap<String, String>> getAllIpAttemptsByAttemptCategoryAndDuration(int attemptCategory, String start, String end) throws Exception{
		List<HashMap<String, String>> allAttemptsByAttemptCategoryAndDuration = new ArrayList<HashMap<String, String>>();
		
		if(end.equals("-1")){
			DateTime tempDate = new DateTime(TimeConverter.getStartTimeOfTheDay(System.currentTimeMillis()));
			DateTime startDate = tempDate.minusDays(Integer.parseInt(start));
			
			DateTime endDate = new DateTime(System.currentTimeMillis());
			allAttemptsByAttemptCategoryAndDuration = webSecurityDAO.getAllIpAttemptsByAttemptCategoryAndDuration(attemptCategory, startDate, endDate);
		}else{
			long tempDate = TimeConverter.getStartTimeOfTheDay(DateTime.parse(start, DateTimeFormat.forPattern("dd/MM/yyyy")).getMillis());
			DateTime startDate = new DateTime(tempDate);
			
			tempDate = TimeConverter.getStartTimeOfTheDay(DateTime.parse(end, DateTimeFormat.forPattern("dd/MM/yyyy")).getMillis());
			DateTime endDate = new DateTime(tempDate).plusDays(1);
			
			allAttemptsByAttemptCategoryAndDuration = webSecurityDAO.getAllIpAttemptsByAttemptCategoryAndDuration(attemptCategory, startDate, endDate);
		}
		return allAttemptsByAttemptCategoryAndDuration;
	}
}
