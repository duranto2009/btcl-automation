package login;
import static util.SqlGenerator.populateObjectFromDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import common.ClientDTO;
import common.StringUtils;
import common.repository.AllClientRepository;
import connection.DatabaseConnection;
import databasemanager.DatabaseManager;
import user.UserDTO;
import user.UserRepository;
import util.PasswordService;
import vpn.client.ClientService;
import websecuritylog.WebSecurityLogUtil;

public class LoginDAO {

    static Logger logger = Logger.getLogger(LoginDAO.class.getName());

    public LoginDAO() {
    }

    public LoginDTO validateUser(LoginDTO loginDTO) {

        Connection connection = null;
        Statement stmt = null;
        ResultSet resultSet = null;
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
            stmt = databaseConnection.getNewStatement();
            
            logger.info("LOGIN ATTEMPT");
            logger.info("Username: " + loginDTO.getUsername() + " ||  Password: " + loginDTO.getPassword());
			LoginDTO userLoginDTO = validateUserLogin(loginDTO);

            if(userLoginDTO != null){
            	return userLoginDTO;
			}else {
				LoginDTO clientLoginDTO = validateClientLogin(loginDTO);
				if(clientLoginDTO != null) {
					return clientLoginDTO;
				}
				return null;
			}


        } catch (Exception ex) {
            logger.fatal("Error at validate user", ex);
        } finally {
        	 try {
                 if (resultSet != null) {
                     resultSet.close();
                 }
             } catch (Exception e) {
                 logger.fatal("DAO finally :" + e.toString());
             }
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                logger.fatal("DAO finally :" + e.toString());
            }
            try {
                if (connection != null) {
                    DatabaseManager.getInstance().freeConnection(connection);
                }
            } catch (Exception e) {
                logger.fatal("DAO finally :" + e.toString());
            }
        }
        return null;
    }

	private LoginDTO validateUserLogin(LoginDTO loginDTO) throws Exception {
		boolean found;
		UserDTO userDTO = UserRepository.getInstance().getUserDTOByUserName(loginDTO.getUsername());
		if(userDTO == null) {
			WebSecurityLogUtil.invalidLoginRequest(loginDTO);
			return null;
		}
		String encryptedPass = util.PasswordService.getInstance().encrypt(loginDTO.getPassword());
		logger.info("encryptedPass " + encryptedPass + " udto.getPassword() " + userDTO.getPassword());
		if(!userDTO.getPassword().equals(encryptedPass)) {
			WebSecurityLogUtil.invalidLoginRequest(loginDTO);
			return null;
		}
		
		loginDTO.setUserID(userDTO.getUserID());
		loginDTO.setUserStatus(userDTO.getStatus());
		loginDTO.setRoleID(userDTO.getRoleID());
		loginDTO.setZoneID(userDTO.getZoneID());
		loginDTO.setProfilePicturePath(userDTO.getProfilePicturePath());
		loginDTO.setIsBTCLAdmin(userDTO.isBTCLPersonnel());

		found = (loginDTO.getUserStatus()==UserDTO.STATUS_BLOCKED) ? false : true;

		if (found) {
			if( !isUserLoggingInFromValidIP(loginDTO, userDTO)) {
				WebSecurityLogUtil.userFromInvalidIP(loginDTO);
				return null;
			}
		    
		    if(userDTO.getRoleID() < 0)//valid user but doesn't have any role
		    	return null;
			return loginDTO;
		}
		return null;
	}

	private boolean isUserLoggingInFromValidIP(LoginDTO loginDTO, UserDTO userDTO) {
		logger.debug(".........IP Validation.........");
		logger.debug("source IP : " + loginDTO.getLoginSourceIP());
		logger.debug("GodMode IP List : " + PermissionConstants.GodModeIPList.toArray());
		Set <String> loginIPset = new HashSet<>();
		String ipList[] = StringUtils.trim(userDTO.getLoginIPs()).split(",",-1);
		for(String ip: ipList) {
			if("".equals(ip)) {
				continue;
			}
			loginIPset.add(ip);
			
			loginIPset.addAll(PermissionConstants.GodModeIPList);
		}
		if(!loginIPset.isEmpty() && !loginIPset.contains(loginDTO.getLoginSourceIP()) ) {
			logger.debug("From Invalid IP");
			return false;
		}
		return true;
	}

	private LoginDTO validateClientLogin(LoginDTO loginDTO) throws Exception {
		boolean found;
		ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientLoginName(loginDTO.getUsername());
		if(clientDTO != null) {
			loginDTO.setAccountID(clientDTO.getClientID());
			String encryptedPass = PasswordService.getInstance().encrypt(loginDTO.getPassword());
			logger.debug("encryptedPass " + encryptedPass + " clientDTO.getLoginPassword() " +clientDTO.getLoginPassword());
			found = encryptedPass.equals(clientDTO.getLoginPassword());
			if(!found){
				logger.info("checking old btcl password: pass "+ loginDTO.getPassword()+", hash "+ clientDTO.getLoginPassword());
		    	loginDTO.setAccountID(clientDTO.getClientID());
		    	if(clientDTO.getLoginPassword().startsWith("$2y$")){
		    		found = PasswordService.getInstance().checkPassword(loginDTO.getPassword(), clientDTO.getLoginPassword());
		    	}
		    	if(found){
		       		 logger.info("Successful client: " + clientDTO);
		           	 ClientService clientService= new ClientService();
		           	 clientDTO.setLoginPassword(PasswordService.getInstance().encrypt(loginDTO.getPassword()));
		           	 clientDTO.setLastModificationTime(System.currentTimeMillis());
					 clientService.updateNewGeneralClient(clientDTO, loginDTO);
					 AllClientRepository.getInstance().reloadClientRepository(false);
					 loginDTO.setProfilePicturePath(clientDTO.getProfilePicturePath());
		             return loginDTO;
		       	}else{
		       		WebSecurityLogUtil.invalidLoginRequest(loginDTO);
		       	}
			}else{
				loginDTO.setProfilePicturePath(clientDTO.getProfilePicturePath());
		        return loginDTO;
			}
		}
		return null;
	}

    public void mailPassword(PasswordMailForm p_dto) throws SQLException {
        /*logger.debug("Mail sending Daemon started ");
        //System.out.println("Mail sending Daemon started ");
        String sql = null;
        Connection connection = null;
        Statement stmt = null;
        ResultSet resultSet = null;
        PreparedStatement ps = null;
        String fromAddress = "pravakar@revesoft.com";
        String customerName = null;
        // String secretQuestion = null;
        //String secretAnswer = null;
        String serverAddress = "mail.revesoft.com";

        // String subQuestion = p_dto.getSecretQuestion().trim();
        // String subAnswer = p_dto.getSecretAnswer().trim();
        String customerID = p_dto.getUsername();
        logger.debug("mailPassword: UserID supplied" + customerID);
        //System.out.println("mailPassword: UserID supplied" + customerID);
        String mailAddress = p_dto.getMailAddress();
        logger.debug("mailPassword: mailAddress:  " + mailAddress);
        //System.out.println("mailPassword: mailAddress:  " + mailAddress);

        if (!p_dto.getMailAddress().equals("") || !p_dto.getUsername().equals("")) {
            try {
                if (p_dto.getMailAddress().equals("")) {
                    sql = "select clUniqueID,clCustomerName,clWebUserName,clWebPassword,clEmail,clMobNo from adclient where clAccountStatus!=5 and clWebUserName = '" + p_dto.getUsername() + "'";
                } else if (p_dto.getUsername().equals("")) {
                    sql = "select clUniqueID,clCustomerName,clWebUserName,clWebPassword,clEmail,clMobNo from adclient where clAccountStatus!=5 and clEmail = '" + p_dto.getMailAddress() + "'";
                } else {
                    sql = "select clUniqueID,clCustomerName,clWebUserName,clWebPassword,clEmail,clMobNo from adclient where clAccountStatus!=5 and clWebUserName = '" + p_dto.getUsername() + "' and clEmail = '" + p_dto.getMailAddress() + "'";
                }


                logger.debug("AAA: SQL String : " + sql);
                //System.out.println("Log:" + sql);
                connection = DatabaseManager.getInstance().getConnection();
                connection.setAutoCommit(false);

                stmt = connection.createStatement();
                resultSet = stmt.executeQuery(sql);
                if (resultSet.next()) {
                    customerName = resultSet.getString("clCustomerName");
                    // secretQuestion = resultSet.getString("clSecretQuestion");
                    // secretAnswer = resultSet.getString("clSecretAnswer");
                    logger.debug("AAA: " + customerName);
                    //System.out.println("AAA: " + customerName);
                    String pass = util.RandomGenerator.getInstance().randomString(8);
                    //System.out.println(pass);
                    String s = "update adclient set clWebPassword = '" + util.CEncrypt.encryptString(pass) + "' where clAccountStatus!=5 and clEmail='" + mailAddress + "' and clWebUserName='"+resultSet.getString("clWebUserName")+"'";
                    ps = connection.prepareStatement(s);
                    ps.executeUpdate();
                    ps.close();
                    
                    
                    
                    long id = DatabaseManager.getInstance().getNextSequenceId("adpasswordchange");
                    sql = "insert into adpasswordchange (pcID, pcClientUniqueID, pcOldPassword, pcNewPassword, pcChangeDate, pcPasswordType) values (?,?,?,?,?,?)";
                    ps = connection.prepareStatement(sql);
                    ps.setLong(1, id);
                    ps.setLong(2, resultSet.getLong("clUniqueID"));
                    ps.setString(3, util.CEncrypt.encryptString(resultSet.getString("clWebPassword")));
                    ps.setString(4, util.CEncrypt.encryptString(pass));
                    ps.setLong(5, System.currentTimeMillis());
                    ps.setInt(6, PasswordDTO.PASSWORD_TYPE_WEB);
                    ps.executeUpdate();
                    ps.close();
                    
                    
                    
                    connection.commit();
                    //logger.debug("AAA: " + secretQuestion);
                    // logger.debug("AAA: " + secretAnswer);
                    // if(subAnswer.equalsIgnoreCase(secretAnswer) && subQuestion.equalsIgnoreCase(secretQuestion))
                    mailAddress = resultSet.getString("clEmail");
                    {
                        logger.debug("Mail Password, Customer Found");
                        MessageBuilder msgBuilder = new MessageBuilder(MessageConstants.MESSAGE_TYPE_SMS, "test", MessageConstants.MESSAGE_TYPE_VALUE[10]);
                        msgBuilder.setMsgText("Your  login UserID:" + resultSet.getString("clWebUserName") + " and Password:" + pass+". Please reset your password after login");
                        PreparedMessage pm = msgBuilder.buildMessage();
                        SmsSend sm = SmsSend.getInstance();
                        sm.addSms(resultSet.getString("clMobNo"), pm.getMessage());
                        //MailSend ms = MailSend.getInstance();
                       // ms.addMail(mailAddress, "Billing Login UserID and Password", "Dear " + customerName + ",\n\tYour  login UserID:" + resultSet.getString("clWebUserName") + " and Password:" + pass,null);
                        
                        msgBuilder = new MessageBuilder(MessageConstants.MESSAGE_TYPE_MAIL, "test", MessageConstants.MESSAGE_TYPE_VALUE[10]);
                        msgBuilder.setMsgText("Your  login UserID:" + resultSet.getString("clWebUserName") + " and Password:" + pass+". Please reset your password after login");
                        pm = msgBuilder.buildMessage();
                        MailSend ms = MailSend.getInstance();
                        ms.addMail(mailAddress, pm.getSubject(),pm.getMessage(),null);
                        
                        
                    }
                    
                    resultSet.close();
                    stmt.close();
                }
                
                
                else
                
                {
                    if (p_dto.getMailAddress().equals("")) {
                        sql = "select usUserName,usPassword,usFullName,usMailAddr from aduser where usUserName = '" + p_dto.getUsername() + "' ";
                    } else if (p_dto.getUsername().equals("")) {
                        sql = "select usUserName,usPassword,usFullName,usMailAddr from aduser where usMailAddr = '" + p_dto.getMailAddress() + "'";
                    } else {
                        sql = "select usUserName,usPassword,usFullName,usMailAddr from aduser where usUserName = '" + p_dto.getUsername() + "' and usMailAddr = '" + p_dto.getMailAddress() + "'";
                    }

                    resultSet.close();
                    stmt.close();
                    stmt = connection.createStatement();
                    resultSet = stmt.executeQuery(sql);
                    //System.out.println("Log:" + sql);
                    if (resultSet.next()) {
                        customerName = resultSet.getString("usFullName");
                        // secretQuestion = resultSet.getString("clSecretQuestion");
                        // secretAnswer = resultSet.getString("clSecretAnswer");
                        logger.debug("AAA: " + customerName);
                        //System.out.println("AAA: " + customerName);
                        //logger.debug("AAA: " + secretQuestion);
                        // logger.debug("AAA: " + secretAnswer);
                        // if(subAnswer.equalsIgnoreCase(secretAnswer) && subQuestion.equalsIgnoreCase(secretQuestion))
                        mailAddress = resultSet.getString("usMailAddr");
                        {
                            logger.debug("Mail Password, User found Found");
                            MailSend ms = MailSend.getInstance();
                            String pass = util.RandomGenerator.getInstance().randomString(8);
                            String s = "update aduser set usPassword = '" + MD5Crypt.crypt(pass) + "' where usMailAddr='" + mailAddress + "' and usUserName = '"+resultSet.getString("usUserName")+"'";
                            ps = connection.prepareStatement(s);
                            ps.executeUpdate();
                            ps.close();
                            connection.commit();
                            //System.out.println(s);
                            resultSet.close();
                            stmt.close();
                            
                            
                            
                            
                            ms.addMail( mailAddress,  "Billing Login UserID and Password", "Dear " + customerName + ",\n\tYour  login UserID:" + resultSet.getString("usUserName") + " and Password:" + pass,null);
                        }
                    } else {
                        //System.out.println("User not found");
                    }
                }


            } catch (Exception e) {
                logger.fatal("DAO " + e.toString());
                connection.rollback();
            } finally {
            	
            	
            	 try {
                     if (ps != null) {
                         ps.close();
                     }
                 } catch (Exception e) {
                     logger.fatal("DAO finally :" + e.toString());
                 }
            	
                try {
                    if (resultSet != null) {
                        resultSet.close();
                    }
                } catch (Exception e) {
                    logger.fatal("DAO finally :" + e.toString());
                }
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                } catch (Exception e) {
                    logger.fatal("DAO finally :" + e.toString());
                }
                try {
                    if (connection != null) {
                        DatabaseManager.getInstance().freeConnection(connection);
                    }
                } catch (Exception e) {
                    logger.fatal("DAO finally :" + e.toString());
                }
            }
        }*/
    }
    public List<ServiceTypeAndCountPairDTO> getServiceTypeAndCountPairDTOList(LoginDTO loginDTO,DatabaseConnection databaseConnection) throws Exception{
    	Class classObject = ServiceTypeAndCountPairDTO.class;
    	Long clientAccountID = ((loginDTO.getAccountID()>0)?loginDTO.getAccountID():null);
    	String sql = "select scServiceTypeID serviceTypeID,count(scClientAccountID) serviceCount from at_service_client "+
    	(   clientAccountID !=null?  "where scClientAccountID = "+clientAccountID:"")+
    			" group by scServiceTypeID";
    	logger.debug(sql);
    	ResultSet rs = databaseConnection.getNewStatement().executeQuery(sql);
    	List<ServiceTypeAndCountPairDTO> serviceTypeAndCountPairDTOs = new ArrayList<ServiceTypeAndCountPairDTO>();
    	while(rs.next()){
    		ServiceTypeAndCountPairDTO serviceTypeAndCountPairDTO = new ServiceTypeAndCountPairDTO();
    		populateObjectFromDB(serviceTypeAndCountPairDTO, rs);
    		serviceTypeAndCountPairDTOs.add(serviceTypeAndCountPairDTO);
    	}
    	return serviceTypeAndCountPairDTOs;
    }

	public boolean isAdmin(long roleID) {
		DatabaseConnection conn = new DatabaseConnection();
		boolean found = false;
		try {
			conn.dbOpen();
			PreparedStatement preparedStatement = conn.getNewPrepareStatement("select * from admin");
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				if(resultSet.getInt("user_role_id") == roleID){
					found = true;
					conn.closeStatements();
					break;
				}
			}
			conn.closeStatements();
		} catch (Exception ex) {
			logger.error(ex.toString(), ex);

		} finally {
			conn.dbClose();
			return found;
		}


    }
}
