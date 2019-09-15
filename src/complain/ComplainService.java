package complain;

import common.ClientDTO;
import common.EntityTypeConstant;
import common.RequestFailureException;
import common.repository.AllClientRepository;
import connection.DatabaseConnection;
import file.FileDTO;
import file.FileService;
import login.LoginDTO;
import org.apache.log4j.Logger;
import util.NavigationService;
import vpn.client.ClientDetailsDTO;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

public class ComplainService implements NavigationService {
	
	Logger logger = Logger.getLogger(getClass());
	int moduleID;
	LoginDTO loginDTO ;
	ComplainDAO complainDAO = new ComplainDAO();
	FileService fileDAO = new FileService();
	long currentTime = 0;
	public void setModuleID(int moduleID){
		this.moduleID = moduleID;
	}

	public void setLoginDTO(LoginDTO loginDTO) {
		this.loginDTO = loginDTO;
	}


	public ComplainService() {
	}

	public String addComplain(ComplainDTO complainDTO, LoginDTO loginDTO) throws Exception {

		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			currentTime = System.currentTimeMillis();

			complainDAO.addComplain(complainDTO, databaseConnection);

			ComplainHistoryDTO complainHistoryDTO = complainDTO.getComplainHistory();
			complainHistoryDTO = createComplainHistoryDTO(complainHistoryDTO, complainDTO, loginDTO);
			if(!validateCompaintHistoryDTO(complainHistoryDTO,loginDTO)){
				throw new RequestFailureException("Imvalid complain! Please fill up all required fields.");
			}
			complainDAO.addComplainHistory(complainHistoryDTO, databaseConnection);

			if (complainDTO.getComplainHistory().getDocument() != null && complainDTO.getComplainHistory().getDocument().getFileName() != null
					&& !complainDTO.getComplainHistory().getDocument().getFileName().isEmpty()) {
				FileDTO fileDTO = new FileDTO();
				fileDTO = createFileDTO(complainHistoryDTO, loginDTO);
				fileDAO.insert(fileDTO, databaseConnection);
			}
			
			databaseConnection.dbTransationEnd();
			createAutoReplyComplainHistoryDTO(complainHistoryDTO, complainDTO);
			addComplainHistory(complainHistoryDTO, loginDTO);
		} catch (Exception ex) {
			try {
				logger.fatal("Fatal: ", ex);
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
			}
			if(ex instanceof RequestFailureException){
				throw new RequestFailureException("Imvalid complain! Please fill up all required fields.");
			}
		} finally {
			databaseConnection.dbClose();
		}

		return "success";
	}
	
	private boolean validateCompaintHistoryDTO(ComplainHistoryDTO complainHistoryDTO, LoginDTO loginDTO) {
		if(complainHistoryDTO == null)
			return false;
		if(complainHistoryDTO.getComplainID() <= 0){
			return false;
		}
		if(complainHistoryDTO.getMessage().trim().equals("")){
			return false;
		}
		if(loginDTO.getIsAdmin()){
			if(complainHistoryDTO.getStatus() < 0){
				return false;
			}
			if(complainHistoryDTO.getNote().trim().equals("")){
				return false;
			}
		}
		return true;
		
	}
	
	private void createAutoReplyComplainHistoryDTO(ComplainHistoryDTO complainHistoryDTO, ComplainDTO complainDTO) {
		
		complainHistoryDTO.setComplainID( complainDTO.getID() );
		complainHistoryDTO.setAccountID( -1 );
		complainHistoryDTO.setStatus( ComplainConstants.STATUS_MAP_GET_VAL.get( "Received" ));
		complainHistoryDTO.setMessage( "Your request is processing. Please mail to domain@btcl.com.bd for any Domain related problems." );
		complainHistoryDTO.setNote( "Auto Reply");
		complainHistoryDTO.setLastModificationTime( System.currentTimeMillis() );	
	}

	public String addComplain(ComplainDTO complainDTO, ClientDTO clientDTO, String message) throws Exception {

		DatabaseConnection databaseConnection = new DatabaseConnection();
		
		try {
			
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			
			currentTime = System.currentTimeMillis();

			complainDAO.addComplain(complainDTO, databaseConnection);

			ComplainHistoryDTO complainHistoryDTO = complainDTO.getComplainHistory();
			
			complainHistoryDTO = createComplainHistoryDTO( complainHistoryDTO, complainDTO, clientDTO, message );
			
			complainDAO.addComplainHistory(complainHistoryDTO, databaseConnection);
			
			databaseConnection.dbTransationEnd();
			
		} catch (Exception ex) {
			
			try {
				logger.fatal("Fatal: ", ex);
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
			}
			if(ex instanceof RequestFailureException){
				throw (RequestFailureException)ex;
			}
			
		} finally {
			
			databaseConnection.dbClose();
		}
		return "Compalain is taken. Thread ID : "+complainDTO.getID();
	}

	private FileDTO createFileDTO(ComplainHistoryDTO complainHistoryDTO, LoginDTO loginDTO) throws Exception {

		FileDTO fileDTO = new FileDTO();
		fileDTO.setDocument(complainHistoryDTO.getDocument());
		if (loginDTO.getAccountID() > 0)
			fileDTO.setDocOwner(loginDTO.getAccountID());
		else
			fileDTO.setDocOwner(-loginDTO.getUserID());
		fileDTO.setDocEntityTypeID(EntityTypeConstant.COMPLAIN);

		fileDTO.setDocEntityID(complainHistoryDTO.getID());
		fileDTO.setLastModificationTime(System.currentTimeMillis());

		//java.io.File file = fileDTO.createLocalFile();
		return fileDTO;
	}

	private ComplainHistoryDTO createComplainHistoryDTO(ComplainHistoryDTO complainHistoryDTO, ComplainDTO complainDTO, LoginDTO loginDTO) {
		if (loginDTO.getAccountID() > 0)
			complainHistoryDTO.setAccountID(loginDTO.getAccountID());
		else
			complainHistoryDTO.setAccountID(-loginDTO.getUserID());
		complainHistoryDTO.setLastModificationTime(currentTime);
		complainHistoryDTO.setComplainID(complainDTO.getID());
		return complainHistoryDTO;
	}
	
	private ComplainHistoryDTO createComplainHistoryDTO(ComplainHistoryDTO complainHistoryDTO, ComplainDTO complainDTO, ClientDTO clientDTO, String message) {
		
		complainHistoryDTO.setComplainID( complainDTO.getID() );
		complainHistoryDTO.setAccountID( clientDTO.getClientID() );
		complainHistoryDTO.setStatus( ComplainConstants.STATUS_MAP_GET_VAL.get( "Submitted" ));
		complainHistoryDTO.setMessage( message );
		complainHistoryDTO.setLastModificationTime( currentTime );
		complainHistoryDTO.setNote( ComplainConstants.SUBJECT_FOR_COMPLAIN_SMS );
		complainHistoryDTO.setLastModificationTime( System.currentTimeMillis() );
		
		return complainHistoryDTO;
	}
	
	

	@Override
	public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		Collection<Long> complainIDList = new ArrayList<Long>();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();

			complainIDList = complainDAO.getComplainIDs(loginDTO, moduleID, databaseConnection);

			databaseConnection.dbTransationEnd();
		} catch (Exception ex) {
	
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
			}
			if(ex instanceof RequestFailureException){
				throw (RequestFailureException)ex;
			}
		} finally {
			databaseConnection.dbClose();
		}
		return complainIDList;
	}

	@Override
	public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects) throws Exception {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		Collection<Long> complainIDs = new ArrayList<Long>();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();

			complainIDs = complainDAO.getComplainIDsFromSearchCriteria(loginDTO,searchCriteria,moduleID, databaseConnection);

			databaseConnection.dbTransationEnd();
		} catch (Exception ex) {
			logger.debug("inside getIDs from search criteria method of Complain Service", ex);
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
				logger.debug("inside  getIDs from search criteria  method of Complain Service", ex2);
			}
			if(ex instanceof RequestFailureException){
				throw (RequestFailureException)ex;
			}
		} finally {
			databaseConnection.dbClose();
		}
		return complainIDs;
	}

	@Override
	public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		Collection<ComplainDTO> complainDTOList = new ArrayList<ComplainDTO>();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();

			complainDTOList = (Collection<ComplainDTO>) complainDAO.getComplainDTOsByIDList(recordIDs,moduleID, databaseConnection);

			databaseConnection.dbTransationEnd();
		} catch (Exception ex) {
			try {
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {
			}
			if(ex instanceof RequestFailureException){
				throw (RequestFailureException)ex;
			}
		} finally {
			databaseConnection.dbClose();
		}
		logger.debug(complainDTOList);
		return complainDTOList;
	}

	public String addComplainHistory(ComplainHistoryDTO complainHistoryDTO, Object ...loginDTO) {
		if(loginDTO.length>1){
			return "Invalid operation";
		}
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try {
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			currentTime = System.currentTimeMillis();
			complainHistoryDTO.setLastModificationTime(currentTime);
			complainDAO.addComplainHistory(complainHistoryDTO, databaseConnection);

			if (complainHistoryDTO.getDocument() != null && complainHistoryDTO.getDocument().getFileName() != null && !complainHistoryDTO.getDocument().getFileName().isEmpty()) {
				FileDTO fileDTO = new FileDTO();
				fileDTO = createFileDTO(complainHistoryDTO,(LoginDTO)loginDTO[0]);
				fileDAO.insert(fileDTO, databaseConnection);
			}
			
			String sql = "UPDATE at_complaint SET cmStatus = ? WHERE cmID = ?";
			PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);
			ps.setObject(1, complainHistoryDTO.getStatus());
			ps.setObject(2, complainHistoryDTO.getComplainID());
			ps.executeUpdate();

			databaseConnection.dbTransationEnd();
		} catch (Exception ex) {
			try {
				logger.fatal("Fatal: ", ex);
				databaseConnection.dbTransationRollBack();
			} catch (Exception ex2) {

			}
			if(ex instanceof RequestFailureException){
				throw (RequestFailureException)ex;
			}
		} finally {
			databaseConnection.dbClose();
		}

		return "Done";
	}
	
	public void insertComplainUsingSms( ComplainDTO complainDTO, ClientDTO clientDTO ,String message ) throws Exception{
		
		addComplain( complainDTO, clientDTO, message );
	}
	
	public String insertComplainHistoryUsingSms(int p_moduleID,long p_complainID, String p_message, String p_mobileNumber) throws Exception{
		ClientDTO clientDTO = getClientDTOByMobile(p_moduleID,p_mobileNumber);
		if(clientDTO != null){
			if(clientDTO.getClientID() > 0){
					ComplainHistoryDTO complainHistoryDTO = new ComplainHistoryDTO();
					currentTime = System.currentTimeMillis();
					complainHistoryDTO.setAccountID(clientDTO.getClientID());
					complainHistoryDTO.setMessage(p_message);
					complainHistoryDTO.setComplainID(p_complainID);
					complainHistoryDTO.setLastModificationTime(currentTime);
					return addComplainHistory(complainHistoryDTO);
						
			}
		}else{
			return "Mobile number is not registered!";
		}
		return null;
	}

	private ClientDTO getClientDTOByMobile(int p_moduleID, String p_mobileNumber) {
		ClientDTO clientDTO = new ClientDTO();
		clientDTO.setClientID(-1);
		
		//@ client search by mobileNumber and module id here
		ArrayList<ClientDetailsDTO>  clientDetailsDTOs = AllClientRepository.getInstance().getAllVpnCleint();
		for(ClientDetailsDTO clientDetailsDTO : clientDetailsDTOs){
			List<ClientContactDetailsDTO> clientContactDetailsDTOs =  clientDetailsDTO.getVpnContactDetails();
			for(ClientContactDetailsDTO clientContactDetailsDTO : clientContactDetailsDTOs){
				if(clientDetailsDTO.getModuleID() == p_moduleID &&  clientContactDetailsDTO.getPhoneNumber() == p_mobileNumber){
					return clientDetailsDTO;
				}
			}
		}
		//
		return clientDTO;
	}
}