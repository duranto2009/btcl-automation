package smsServer;

import java.io.File;
import java.io.FileInputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import vpn.client.ClientUtility;

/*import shutdown.ShutDownListener;
import shutdown.ShutDownService;*/

public class AutoSignUp{

    static AutoSignUp autoSignUp;
    public static String APPLICATION_NAME = "BTCL_SMS_Server";
    public static String APPLICATION_VERSION = "1.0";
    public static InetAddress switchIp;
    public static byte[] switchIpByte;
    public static String switchIpStr;
    public static int switchPort;
    public static InetAddress autoSignUpListenIP;
    public static int autoSignUpListenPort;
    static Logger logger = Logger.getLogger(AutoSignUp.class);
    public static DatagramSocket socket;
    public static InetAddress SMSServerIP;
    public static int SMSServerPort;
    public static String autoSignUPListenIpStr;
    public static byte[] autoSingUpListenIP;
    public static byte[] smsSenderUserName;    
    public static byte[] smsCallerID;
    public static byte[] callSenderPassword;
    public static int NO_OF_RETRY = 3;
	private static AutoSignUp instance = null;
	
    private AutoSignUp(String realContextPath) {
        try {

            readConfiguration(realContextPath);
            
//            System.out.println("listen ip:")
            logger.debug("autoSignUpListenIP " + autoSignUpListenIP + " autoSignUpListenPort " + autoSignUpListenPort);
            socket = new DatagramSocket(autoSignUpListenPort, autoSignUpListenIP);
            
            AutoSignUPMessageProcessor.getInstance().start();
            logger.debug(APPLICATION_NAME + " started Successfully ");
            
        } catch (Exception e) {
        	
            logger.debug("Exception:", e);
            //System.exit(0);
        }
    }

	public static AutoSignUp getInstance(String realContextPath)
	{
		if(instance == null)
		{
			createInstance(realContextPath);
		}
		return instance;
	}
	private synchronized static void createInstance(String realContextPath)
	{
		if(instance == null)
		{
			instance = new AutoSignUp(realContextPath);
		}
		
	}
    public void readConfiguration(String realContextPath) {
        FileInputStream fin = null;
        try {
        	logger.debug("Directory: " + System.getProperty("user.dir"));
        	
            String fileName = realContextPath + File.separator + "config.txt";
//        	String fileName = "config.txt";
        	logger.debug("new File(fileName).exists() " + new File(fileName).exists());
            logger.debug("Loading from configuration file: " + fileName);

            Properties prop = new Properties();
            File posFile = new File(fileName);
            if (posFile.exists()) {
                fin = new FileInputStream(fileName);
                prop.load(fin);
                if (prop.containsKey("localBindIP")) {
                    autoSignUPListenIpStr = (String) (prop.getProperty("localBindIP")).trim();
                    autoSignUpListenIP = InetAddress.getByName(autoSignUPListenIpStr);
                    autoSingUpListenIP = autoSignUPListenIpStr.getBytes();
                } else {
                    logger.fatal("No  Bind IP  in the configuration file");
                    System.exit(0);
                }

                if (prop.containsKey("localBindPort")) {
                    autoSignUpListenPort = Integer.parseInt((prop.getProperty("localBindPort")));
                } else {
                    logger.fatal("No  bind Port  in the configuration file");
                }

                if (prop.containsKey("smsServerIp")) {
                    String smsServerIp = (String) prop.getProperty("smsServerIp");
                    SMSServerIP = InetAddress.getByName(smsServerIp);

                } else {
                    logger.fatal("No  smsServerIp in the configuration file");
                }

                if (prop.containsKey("smsServerPort")) {
                    SMSServerPort = Integer.parseInt((prop.getProperty("smsServerPort")));
                } else {
                    logger.fatal("No  smsServerPort in the configuration file");
                }

                if (prop.containsKey("smsSenderUsername")) {
                    String smsUsername = (String) (prop.getProperty("smsSenderUsername"));
                    smsSenderUserName = smsUsername.getBytes();
                } else {
                    logger.fatal("No  SMS Sender User name  in the configuration file");
                }

                if (prop.containsKey("smsCallerID")) {
                    String smsCallerID = (String) (prop.getProperty("smsCallerID"));
                    AutoSignUp.smsCallerID = smsCallerID.getBytes();
                } else {
                    logger.fatal("No  sms Caller ID   in the configuration file");
                }              

            } else {
                logger.debug((new StringBuilder("Configuration file '")).append(fileName).append("' does not found.").toString());
                shutDown();
            }

        } catch (Exception ex) {
            logger.fatal("Exception in reading configuration file :", ex);
        }
        if (fin != null) {
            try {
                fin.close();
            } catch (Exception p) {
                logger.fatal((new StringBuilder("Error while closeing configuration file:")).append(p.getMessage()).toString(), p);
            } finally {
                if (fin != null) {
                    try {
                        fin.close();
                    } catch (Exception p) {
                        logger.fatal((new StringBuilder("Error while closing configuration file:")).append(p.getMessage()).toString(), p);
                    }
                }
            }
        }
    }
    /*public int sendSMS(String message, String destination)
    {
    	return AutoSignUPMessageProcessor.getInstance().sendSMS(message, destination);
    }*/

    public static void main(String args[]) {

    	//ClientUtility.getByPhoneNumber( "8801844015754" );
        PropertyConfigurator.configure("src/log4j.properties");
        
        logger.debug("Starting " + APPLICATION_NAME + " " + APPLICATION_VERSION + ".......  ");
        System.out.println("Starting " + APPLICATION_NAME + "  " + APPLICATION_VERSION + ".......  ");
        autoSignUp = new AutoSignUp( "src" );    
        //System.out.println("socket.getInetAddress() " + socket.getInetAddress());
        AutoSignUPMessageProcessor.getInstance().start();
        //SIPProcessor.getInstance().start();
        //CallSender.getInstance().start();
        //  CallRequest callRequest=new CallRequest(callSenderUserName, "8801814655957","3311");
        //  CallSender.m_Requests.offer(callRequest);
        //ShutDownService.getInstance().addShutDownListener(autoSignUp);

        logger.debug(APPLICATION_NAME + " started Successfully ");
        System.out.println(APPLICATION_NAME + " started Successfully ");

    }

    public void shutDown() {
        logger.debug("Going to shutdown " + APPLICATION_NAME + "...");
        AutoSignUPMessageProcessor.getInstance().shutdown();
        //SIPProcessor.getInstance().shutdown();
        //CallSender.getInstance().shutdown();
        logger.debug(APPLICATION_NAME + " shutdown successfully");
        System.out.println(APPLICATION_NAME + " shutdown successfully");
        //System.exit(0);
    }
}
