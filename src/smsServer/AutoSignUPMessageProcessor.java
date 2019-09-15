package smsServer;


import common.ClientConstants;
import common.ClientDTO;
import common.ClientStatusConstants;
import common.EntityTypeConstant;
import common.repository.AllClientRepository;
import complain.ComplainConstants;
import complain.ComplainDTO;
import complain.ComplainHistoryDTO;
import complain.ComplainService;
import connection.DatabaseConnection;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.steadystate.css.parser.ParseException;

import databasemanager.DatabaseManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import login.LoginDTO;
import smsServer.constants.Constants;
import smsServer.constants.SignUPConstants;
import util.DAOResult;
import util.MessageCreator;
import vpn.client.ClientUtility;

public class AutoSignUPMessageProcessor extends Thread {

    static Logger logger = Logger.getLogger(AutoSignUPMessageProcessor.class);
    byte[] sendBuffer;
    private DatagramPacket sendPacket;
    protected boolean running = false;
    public static DatagramSocket socket;
    static AutoSignUPMessageProcessor autoSignUpProcessor;
    static HashMap<String, Long> smsRequestStatusByPhoneNumber;
    public static HashMap<String, Long> callRequestTimeByPhoneNumber;
    static HashMap<InetAddress, PacketDTO> smsAppServerMap;
    
    int SMS_DELIVERY_TIME = 2 * 60 * 1000;
    int REGISTRATION_EXPIRE_DURATION = 1 * 60 * 1000;
    int signUpRequest = 0;

    public AutoSignUPMessageProcessor() {
    	
        sendBuffer = new byte[500];
        sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length);
        socket = AutoSignUp.socket;
        smsRequestStatusByPhoneNumber = new HashMap<String, Long>();
        callRequestTimeByPhoneNumber = new HashMap<String, Long>();
        smsAppServerMap = new HashMap<InetAddress, PacketDTO>();
    }

    public static AutoSignUPMessageProcessor getInstance() {
        if (autoSignUpProcessor == null) {
            createInstance();
        }
        return autoSignUpProcessor;
    }

    protected static synchronized void createInstance() {
        if (autoSignUpProcessor == null) {
            autoSignUpProcessor = new AutoSignUPMessageProcessor();
        }
    }

    public void run() {
        running = true;
        byte data[] = new byte[1024];
        DatagramPacket packet = new DatagramPacket(data, data.length);

        while (running) {
            try {
                socket.receive(packet);
                processReceivedPacket(data, packet.getLength(), packet.getAddress(), packet.getPort());
                
            } catch (Exception ex) {

                if (socket == null || socket.isClosed()) {
                    running = false;

                } else {
                    logger.fatal("Exception in Auto SignUP processor  client while Recieving Data", ex);
                }
            }
        }
    }

    public void shutdown() {
        running = false;
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (Exception ex) {
        }
        socket = null;
    }
    
    private String createEncryptedPassword(String nonce,String username,String password) {
    	String md5String = nonce + username + password;
    	MessageDigest md = null;
    	try {
    		md = MessageDigest.getInstance("MD5");
    		md.update(md5String.getBytes("UTF-8"));
    	} catch (Exception e) {

    		e.printStackTrace();
    	}
    	byte[] mdbytes = md.digest();
    	// convert the byte to hex format method 1
    	StringBuffer sb = new StringBuffer();
    	for (int i = 0; i < mdbytes.length; i++) {
    		sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16)
    				.substring(1));
    	}
    	return sb.toString();
    }    
    
    /**
     * This method sends a sms sending request to smsSender app.
     * @author Alam
     * @param message text to be sent
     * @param destination phn no of sms destination
     * @param srcAddress sms sender app ip 
     * @param srcPort sms sender app port
     * @throws Exception 
     */
    private void sendSMS(String message, String destination,InetAddress srcAddress, int srcPort) throws Exception
    {
    	logger.debug("sending sms " + message);
    	
    	int sendingLength = MessageCreator.prepareMessage(sendBuffer, Constants.MESSAGE_TYPE_SMS_REQUEST);
    	sendingLength = MessageCreator.addAttribute(Constants.SMS_ATT_DESTINATION_ID, destination, sendBuffer);
    	sendingLength = MessageCreator.addAttribute(Constants.SMS_ATT_SMS_CONTENT, message, sendBuffer);
    	
    	sendMessage(srcAddress, srcPort, sendingLength);
    	
    	SMSSender sender = SMSSender.getInstance();
    	sender.sendSMS( message, destination );
    }
    
    public int sendSMS(String message, String destination) throws Exception
    {
    	boolean smsAppServerNotFound = false;
    	if(smsAppServerMap.size() == 0)
    	{
    		smsAppServerNotFound = true;
    	}
    	long currentTime = System.currentTimeMillis();
    	PacketDTO selectedPakcetDTO = null;
    	for(PacketDTO packetDTO: smsAppServerMap.values())
    	{
    		if(currentTime - packetDTO.lastRegistrationTime > REGISTRATION_EXPIRE_DURATION)
    		{
    			continue;
    		}    		
    		selectedPakcetDTO = packetDTO;
    	}
    	if(selectedPakcetDTO == null)
    	{
    		smsAppServerNotFound = true;
    	}
    	if(!smsAppServerNotFound)
    	{
    		sendSMS(message, destination, selectedPakcetDTO.srcAddress, selectedPakcetDTO.srcPort);
    		logger.debug("Trying to send SMS to " + destination + " message: " + message);
    		return 1;
    	}
    	else
    	{
    		logger.debug("SMSAppServer is probably down");
    		return 0;
    	}
    }
    
    public int processMessageTypeLogin(byte[] data, int receivedLength, InetAddress srcAddress, int srcPort, int offset) throws Exception
    {    	
    	logger.debug("processMessageTypeLogin");
        int sendingLength = MessageCreator.prepareMessage(sendBuffer, Constants.MESSAGE_TYPE_ACK_LOGIN_REQUEST);
/*		PacketDTO pdto = new PacketDTO();
		pdto.srcAddress = srcAddress;
		pdto.srcPort = srcPort;
		pdto.length = sendingLength;
    	sendMessage(pdto);*/
    	sendMessage(srcAddress, srcPort, sendingLength);
		
		
        String userName = null;
        String receivedEncryptedPassword = null;
        String nonce = null;
        while (offset < receivedLength) {
            int attributeType = MessageCreator.twoByteToInt(data, offset);
            offset += 2;
            int attributeLength = MessageCreator.twoByteToInt(data, offset);
            offset += 2;
            logger.debug("attributeType " + attributeType);
            switch (attributeType) {
                case Constants.SMS_ATT_USER_NAME_ID:
                case Constants.ATT_TYPE_PHONE_NUMBER:
                    userName = new String(data, offset, attributeLength);
                    logger.debug("PHone number:" + userName);
                    break;
                case Constants.ATT_TYPE_ENCRYPTED_PASSWORD:
                	receivedEncryptedPassword = new String(data, offset, attributeLength);
                    logger.debug("receivedEncryptedPassword:" + receivedEncryptedPassword);
                    break;
                case Constants.ATT_TYPE_PASSWORD_NONCE:
                    nonce = new String(data, offset, attributeLength);
                    logger.debug("nonce:" + nonce);
                    break;                                
            }
            offset = offset + attributeLength;
        }
        if (userName != null) {
        	if(receivedEncryptedPassword != null   && nonce != null )
        	{
        		
        		String dbPassword = getPasswordByUserName(userName);
        		logger.debug("dbPlain password " + dbPassword);
        		String dbEncryptedPassword = createEncryptedPassword(nonce, userName, dbPassword);
        		logger.debug("dbEncryptedPassword " + dbEncryptedPassword);
        		if(receivedEncryptedPassword.equals(dbEncryptedPassword))
        		{
        			//send status
        			//sendStatus(data, length, srcAddress, srcPort, offset, 1);
        			MessageCreator.prepareMessage(sendBuffer, Constants.MESSAGE_TYPE_LOGIN_REQUEST_STATUS);
        			sendingLength = MessageCreator.addAttribute(SignUPConstants.STATUS, 1, sendBuffer);
/*        			pdto.length = sendingLength;
        			sendMessage(pdto);*/
        			sendMessage(srcAddress, srcPort, sendingLength);
        			PacketDTO packetDTO = new PacketDTO();
        			packetDTO.srcAddress = srcAddress;
        			packetDTO.srcPort = srcPort;
        			packetDTO.lastRegistrationTime = System.currentTimeMillis();
        			smsAppServerMap.put(packetDTO.srcAddress, packetDTO);
        			//sendSMS("hello test", "01911188120", srcAddress, srcPort, offset);
        		}
        		else
        		{
        			MessageCreator.prepareMessage(sendBuffer, Constants.MESSAGE_TYPE_LOGIN_REQUEST_STATUS);
        			sendingLength = MessageCreator.addAttribute(SignUPConstants.STATUS, 0, sendBuffer);
//        			pdto.length = sendingLength;
//        			sendMessage(pdto);
        			sendMessage(srcAddress, srcPort, sendingLength);
        		}
        	}
        	else
        	{
        		logger.debug("sending nonce");
    			MessageCreator.prepareMessage(sendBuffer, Constants.MESSAGE_TYPE_LOGIN_REQUEST_STATUS);
    			sendingLength = MessageCreator.addAttribute(Constants.ATT_TYPE_PASSWORD_NONCE, MessageCreator.getNonce(), sendBuffer);
    			sendingLength = MessageCreator.addAttribute(SignUPConstants.STATUS, 1, sendBuffer);
//    			pdto.length = sendingLength;
//    			sendMessage(pdto);  	
    			sendMessage(srcAddress, srcPort, sendingLength);
        	}
        }
        else
        {
			MessageCreator.prepareMessage(sendBuffer, Constants.MESSAGE_TYPE_LOGIN_REQUEST_STATUS);
			sendingLength = MessageCreator.addAttribute(SignUPConstants.STATUS, 0, sendBuffer);
//			pdto.length = sendingLength;
//			sendMessage(pdto);
			sendMessage(srcAddress, srcPort, sendingLength);
        }
        
        
        return offset;
    }
    
    public String getPasswordByUserName(String username) throws Exception
    {
    	String password = null;
    	DatabaseConnection databaseConnection = new DatabaseConnection();
    	try
    	{
    		databaseConnection.dbOpen();    		
    		String sql = "select * from at_credentials where acUsername = '" + username + "' and acType = " + Constants.CREDENTIAL_TYPE_COMPLAIN_APP;
    		logger.debug("sql " + sql);
    		Statement stmt = databaseConnection.getNewStatement();
    		ResultSet rs = stmt.executeQuery(sql);

    		while(rs.next())
    		{
    			password = rs.getString("acPassword");
    		}
    	}
    	catch(Exception ex)
    	{
    		logger.debug("ex " ,ex);
    		throw new Exception("Exception reading password");
    	}
    	finally
    	{
    		databaseConnection.dbClose();
    	}
    	return password;
    }
    
    public void sendStatusZero(InetAddress srcAddress, int srcPort, int offset, int sendingLength)
    {
		MessageCreator.prepareMessage(sendBuffer, Constants.LOG_MESSAGE_TYPE_SIGNUP_REQUEST_FROM_AGENT_STATUS);
		sendingLength = MessageCreator.addAttribute(SignUPConstants.STATUS, 0, sendBuffer);
		sendMessage(srcAddress, srcPort, sendingLength);
    }
    
    public void sendMessage(InetAddress srcAddress, int srcPort, int sendingLength)
    {
        sendPacket.setLength(sendingLength);
        sendPacket.setPort(srcPort); 
        sendPacket.setAddress(srcAddress);
        
        logger.debug("sending message to " + sendPacket.getAddress() + ":" + sendPacket.getPort() + " packetlength " + sendPacket.getLength() );        
        
        sendDatagramPacket(sendPacket);
    }
    
    
    /**
     * This method process a complain sms. It parses the sms and store it to complain table.
     * @author Alam
     * @param data sms text int byte array. It is in TLD format. First 8 bit are used for this purpose.
     * @param receivedLength length of the data recieved
     * @param srcAddress src ip address ( That is sms sender app ip address )
     * @param srcPort src port ( That is sms sender app port )
     * @param offset from whice byte to start read the data.
     * @return
     * @throws Exception
     */
    public int processComplainSMS(byte[] data, int receivedLength, InetAddress srcAddress, int srcPort, int offset) throws Exception
    {
    	byte[] tempData = new byte[1000];
    	int tempLength = -1;
    	
    	logger.debug("Processing message type add complain");
    	
        int sendingLength = MessageCreator.prepareMessage( sendBuffer, Constants.COMPLAIN_MESSAGE_FROM_CLIENT );

        sendMessage(srcAddress, srcPort, sendingLength);
    	
        String phoneNumber = null;
        String operatorCode = null;
        String smsText = null;
        
        while (offset < receivedLength) {
        	
            int attributeType = MessageCreator.twoByteToInt(data, offset);
            offset += 2;
            
            int attributeLength = MessageCreator.twoByteToInt(data, offset);
            offset += 2;
            
            logger.debug("attributeType " + attributeType);
            
            switch (attributeType) {
            
                case SignUPConstants.PHONE_NUMBER:
                    phoneNumber = new String(data, offset, attributeLength);
                    logger.debug("Phone number:" + phoneNumber);
                    break;
                    
                case SignUPConstants.OPERATOR_CODE:
                    operatorCode = new String(data, offset, attributeLength);
                    logger.debug("Operator Code:" + operatorCode);
                    break;
                    
                case Constants.ATT_TYPE_IMEI:
                	smsText = new String(data, offset, attributeLength);
                	for( int i=0;i<offset+attributeLength;i++)
                		tempData[i] = data[i];
                	tempLength = offset+attributeLength;
                	
                    logger.debug( "SMS Text:" + smsText );
                    break; 
                    
            }
            
            offset = offset + attributeLength;
        }
        
        if( phoneNumber != null && smsText != null)
        {
        	System.out.println( "Phone number " + phoneNumber );
        	
        	List<ClientDTO> cdtos = ClientUtility.getByPhoneNumber( phoneNumber );
        	
        	System.out.println( "Client DTOs got by phone number " + cdtos );
        	
        	if( cdtos.size() <= 0 ){
        		
        		sendSMS("No client is registered with this phone number", phoneNumber, srcAddress, srcPort);
        		SendMessageProcessedSignal( tempData,tempLength,srcAddress,srcPort );
        		return offset;
        	}
        		
        	
        	ClientDTO cdto = cdtos.get(0);
        	
        	if(cdto == null)
        	{
        		sendSMS("No client is registered with this phone number", phoneNumber, srcAddress, srcPort);
        		SendMessageProcessedSignal( tempData,tempLength,srcAddress,srcPort );
        		return offset;
        	}
        	else if(cdto.getCurrentStatus() != ClientStatusConstants.STATUS_ACTIVE)
        	{
        		
        		//TODO This will be decided later, wheater or not to allow an inactive user to send sms
        		
        		sendSMS("Client is not currently active", phoneNumber, srcAddress, srcPort);
        		SendMessageProcessedSignal( tempData,tempLength,srcAddress,srcPort );
    			return offset;
        	}
        	
        	String moduleName = null;
        	String msg = null;
        	
    		int index = -1;		
    		
    		if( StringUtils.isEmpty( smsText ) ){
    			
    			sendSMS("Invalid Request. Try this format <cmp> <ModuleName> <EntityID> <Your Query>", phoneNumber, srcAddress, srcPort);
    			SendMessageProcessedSignal( tempData,tempLength,srcAddress,srcPort );
    			return offset;
    		}
    		
    		index = smsText.indexOf(" ");
    		
    		moduleName = smsText.substring(0, index);
    		
    		if( StringUtils.isEmpty( moduleName) ){
    			
    			sendSMS("Invalid Domain name. Try this format <cmp> <ModuleName> <EntityID> <Your Query>", phoneNumber, srcAddress, srcPort);
    			SendMessageProcessedSignal( tempData,tempLength,srcAddress,srcPort );
    			return offset;
    		}
    		
    		moduleName = moduleName.toLowerCase();
    		
    		String msgWithEntityId = smsText.substring(index + 1);
    		
    		if( StringUtils.isEmpty( msgWithEntityId) ){
    			
    			sendSMS("Invalid Request name. Try this format <cmp> <ModuleName> <EntityID> <Your Query>", phoneNumber, srcAddress, srcPort);
    			SendMessageProcessedSignal( tempData,tempLength,srcAddress,srcPort );
    			return offset;
    		}
    		
    		index = msgWithEntityId.indexOf( " " );
    		
    		String entityIdStr = msgWithEntityId.substring( 0, index );
    		Long entityId = null;
    		
    		if( StringUtils.isEmpty( entityIdStr ) ){
    			
    			sendSMS("Invalid Entity ID. Try this format <cmp> <ModuleName> <EntityID> <Your Query>", phoneNumber, srcAddress, srcPort);
    			SendMessageProcessedSignal( tempData,tempLength,srcAddress,srcPort );
    			return offset;
    		}
    		
    		try{
    			
    			entityId = Long.parseLong( entityIdStr );
    		}
    		catch( NumberFormatException exception ){
    			
    			sendSMS("Invalid Entity ID.\n Try this format <cmp> <ModuleName> <EntityID> <Your Query>", phoneNumber, srcAddress, srcPort);
    			SendMessageProcessedSignal( tempData,tempLength,srcAddress,srcPort );
    			return offset;
    		}
    		
    		msg = msgWithEntityId.substring( index + 1 );
    		
    		if( msg == null ) msg = "";
        		
        	
        	logger.debug("moduleName: " + moduleName);
        	logger.debug("msg: " + msg);
        	
        	if( moduleName != null )
        	{
        		Integer moduleID = EntityTypeConstant.moduleNameModuleIDMap.get(moduleName);
        		
        		if( moduleID == null )
        		{
            		sendSMS("Invalid Module Name.\n Try this format <cmp> <ModuleName> <EntityID> <Your Query>", phoneNumber, srcAddress, srcPort);
            		SendMessageProcessedSignal( tempData,tempLength,srcAddress,srcPort );
            		return offset;
        		}
        		
        		//Make a complain object from sms
        		ComplainDTO dto = ComplainDTO.makeComplainFromSMS( cdto.getClientID(), msg, moduleID );
        		
        		dto.insert( cdto, msg );
        		
        	}
        	else
        	{
        		sendSMS("Invalid Request.\n Try this format <cmp> <ModuleName> <EntityID> <Your Query>", phoneNumber, srcAddress, srcPort);
        		SendMessageProcessedSignal( tempData,tempLength,srcAddress,srcPort );
        	}
        	
        	SendMessageProcessedSignal( tempData,tempLength,srcAddress,srcPort );
        	
        	
        }
        else
        {
        	sendStatusZero(srcAddress, srcPort, offset, sendingLength);
        }
        
        return offset;
    }

    /**
     * This method signal sms sender app that the sms has been processed. So that app doesnt send same sms again.
	 * @author Alam 
	 * @param tempData Byte array of data to be sent
	 * @param tempLength length of the data
	 * @param srcAddress ip address of destination
	 * @param srcPort port of destination
	 */
	private void SendMessageProcessedSignal(byte[] tempData, int tempLength, InetAddress srcAddress, int srcPort) {
		
		//Temporary save sendPacket. 
    	DatagramPacket temp = sendPacket;
    	
    	//Initialize sendPacket by tempData, which contains Message type bytes set and sms text
    	//Sms sender app uses sms text as key to remove it from it's message queue. Otherwise 
    	//it will keep sending same message again and again.
    	
    	sendPacket = new DatagramPacket( tempData, tempData.length);
    	
    	//Sms sender app remove sms from it's queue if the message type is as specified below.
    	MessageCreator.prepareMessage( tempData, Constants.MESSAGE_TYPE_SIGNUP_STATUS_FOR_AGENT_REQUEST );

    	sendMessage( srcAddress, srcPort, tempLength );
    	
    	System.out.println( "Payload : " + new String( tempData ) );
    	
    	sendPacket = temp;
		
	}

	public void processReceivedPacket(byte[] data, int length, InetAddress srcAddress, int srcPort) {
        
    	logger.debug("processing received Packet from SMS sender app :" + new String(data, 0, length));
        
    	int offset = 0;
        int MessageType = MessageCreator.twoByteToInt(data, offset);
        
        offset += 2;
        int Length = MessageCreator.twoByteToInt(data, offset);
        offset += 2;
        
        if (Length <= MessageCreator.MINIMUM_PACKET_LEN) {
            return;
        }
        
        logger.debug("MessageType " + MessageType);
        
        try {
        	
            switch (MessageType) 
            {
            	case Constants.MESSAGE_TYPE_LOGIN_REQUEST:
            	{
            		offset = processMessageTypeLogin(data, Length, srcAddress, srcPort, offset);
            		break;
            	}
                case Constants.COMPLAIN_MESSAGE_FROM_CLIENT: {
                    offset = processComplainSMS(data, Length, srcAddress, srcPort, offset);
                    break;
                }
                default:
                {
                	//send status 0
                }
            }
        } catch (Exception ex) {
        	
            logger.fatal("got exception during received data processing", ex);            
        }
    }
    //by kayesh
    private void sendAck(int ackName, InetAddress srcAddress, int srcPort) {
        int length = MessageCreator.prepareMessage(sendBuffer, ackName);
        sendPacket.setLength(length);
        sendPacket.setAddress(srcAddress);
        sendPacket.setPort(srcPort);
        sendDatagramPacket(sendPacket);
    }
    
    private void cleanupSMSRequestMap() {
        try {
            Iterator<String> it = smsRequestStatusByPhoneNumber.keySet().iterator();
            long currentTime = System.currentTimeMillis();
            ArrayList<String> removableKey = new ArrayList<String>();
            while (it.hasNext()) {
                String key = it.next();
                long lastSMSSentTime = smsRequestStatusByPhoneNumber.get(key);

                if (currentTime > (lastSMSSentTime + SMS_DELIVERY_TIME)) {
                    removableKey.add(key);
                }
            }

            for (int i = 0; i < removableKey.size(); i++) {
                smsRequestStatusByPhoneNumber.remove(removableKey.get(i));
            }
        } catch (Exception ex) {
            logger.fatal("Exception in clearing orgMap", ex);
        }

    }

    private void cleanupCallRequestMap() {
        try {
            Iterator<String> it = callRequestTimeByPhoneNumber.keySet().iterator();
            long currentTime = System.currentTimeMillis();
            ArrayList<String> removableKey = new ArrayList<String>();
            while (it.hasNext()) {
                String key = it.next();
                long lastSMSSentTime = callRequestTimeByPhoneNumber.get(key);

                if (currentTime > (lastSMSSentTime + SMS_DELIVERY_TIME)) {
                    removableKey.add(key);

                }
            }

            for (int i = 0; i < removableKey.size(); i++) {
                callRequestTimeByPhoneNumber.remove(removableKey.get(i));
            }
        } catch (Exception ex) {
            logger.fatal("Exception in clearing orgMap", ex);
        }

    }




    private String getVoipSwitchPassword(String phoneNumber) {
        //throw new UnsupportedOperationException("Not yet implemented");
        String password = null;
        Statement stmt = null;
        Connection cn = null;

        try {
            cn = DatabaseManager.getInstance().getConnection();
            stmt = cn.createStatement();
            String sql = null;

            sql = "select login, password from clientse164 where login ='" + phoneNumber + "'" + " limit 1";
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                System.out.println("client already present");
                logger.debug("login already present");
                return password;
            }
            rs.close();

        } catch (Exception ex) {
            logger.fatal("Exception at Getting password", ex);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception ex) {
                }
            }
            if (cn != null) {
                try {
                    DatabaseManager.getInstance().freeConnection(cn);
                } catch (Exception ee) {
                }
            }
        }
        return password;
    }

    private String getPasswordFromDB(String phoneNumber) {

        String password = null;
        Connection cn = null;
        Statement stmt = null;

        try {
            cn = DatabaseManager.getInstance().getConnection();
            stmt = cn.createStatement();
            String sql = "select clCustomerID,clAccountID,clBillingPassword from vbClient where clCustomerID ='" + phoneNumber + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String customerID = rs.getString("clCustomerID");
                if (customerID.equals(phoneNumber)) {
                    password = rs.getString("clBillingPassword");;
                }

            }
            rs.close();
        } catch (Exception ex) {
            logger.fatal("Exception at validating PIN and Password.", ex);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception ex) {
                }
            }
            if (cn != null) {
                try {
                    DatabaseManager.getInstance().freeConnection(cn);
                } catch (Exception ee) {
                }
            }
        }

        return password;
    }

    private void sendPasswordBySMS(String password, String phoneNumber, String operatorCode) {
        logger.debug("Before Sending SMS");
        smsRequestStatusByPhoneNumber.put(phoneNumber, System.currentTimeMillis());
        MessageCreator.prepareMessage(sendBuffer, SignUPConstants.SMS_REQUEST);
        MessageCreator.addAttribute(SignUPConstants.OPERATOR_CODE_ID, operatorCode, sendBuffer);
        MessageCreator.addAttribute(SignUPConstants.USER_NAME_ID, AutoSignUp.smsSenderUserName, 0, AutoSignUp.smsSenderUserName.length, sendBuffer);
        MessageCreator.addAttribute(SignUPConstants.CALLER_ID, AutoSignUp.smsCallerID, 0, AutoSignUp.smsCallerID.length, sendBuffer);
        MessageCreator.addAttribute(SignUPConstants.DESTINATION_ID, phoneNumber, sendBuffer);
        int length = MessageCreator.addAttribute(SignUPConstants.SMS_ID, password, sendBuffer);
        try {
            sendPacket.setLength(length);
            sendPacket.setAddress(AutoSignUp.SMSServerIP);
            sendPacket.setPort(AutoSignUp.SMSServerPort);
            sendDatagramPacket(sendPacket);
        } catch (Exception e) {
            logger.fatal("Exception at Sending SMS request packet.", e);

        }
        logger.debug("Message sent");


    }



    public static void sendDatagramPacket(DatagramPacket pkt) {
        try {
            socket.send(pkt);
        } catch (Exception e) {

            logger.fatal("Exception at Sending packet:", e);
        }
    }


    private boolean StringEquals(String str1, String str2) {
        if (str1.toLowerCase().equals(str2.toLowerCase())) {
            return true;
        } else {
            return false;
        }
    }
    //by kayesh
    private LoginDTO createLoginDTOifDoesNotExist() {

        LoginDTO ld = new LoginDTO();
        ld.setUsername("AutoSignupServer");

        ld.setLoginSourceIP(AutoSignUp.autoSignUpListenIP.getHostAddress());

        return ld;
    }



}
