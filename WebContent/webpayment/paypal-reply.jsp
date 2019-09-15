<%@page import="client.ClientRepository"%>
<%@page import="client.ClientDTO"%>

<%@ page import="java.util.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.io.*,javax.net.ssl.*,java.security.cert.X509Certificate" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="org.apache.log4j.Logger"%>
<%@ page import="databasemanager.DatabaseManager"%>
<%@ page import="sessionmanager.SessionConstants"%>
<%@ page import="login.LoginDTO"%>
 
<%@ page import="util.ConformationMessage"%> 
<%@ page import="config.OnlinePaymentConfigDTO"%>
<%@page import="creditcardpayment.client.*"%>
<%
 	Logger logger = Logger.getLogger("Recharge Account jsp");
 Connection cn = null;
 PreparedStatement ps = null;

 try{
	 

 Enumeration en = request.getParameterNames();
 String str = "cmd=_notify-validate";
 while(en.hasMoreElements()){
 String paramName = (String)en.nextElement();
 String paramValue = request.getParameter(paramName);
 str = str + "&" + paramName + "=" + URLEncoder.encode(paramValue);
 }
 logger.debug("STR new: "+str);

    TrustManager[] trustAllCerts = new TrustManager[] {
       new X509TrustManager() {
          public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
          }

          public void checkClientTrusted(X509Certificate[] certs, String authType) {  }

          public void checkServerTrusted(X509Certificate[] certs, String authType) {  }

       }
    };

    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new java.security.SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

    // Create all-trusting host name verifier
    HostnameVerifier allHostsValid = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
          return true;
        }
    };
    // Install the all-trusting host verifier
    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    /*
     * end of the fix
     */
 
 URL u = new URL("https://www.sandbox.paypal.com/cgi-bin/webscr");
 URLConnection uc = u.openConnection();
 uc.setDoOutput(true);
 uc.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
 PrintWriter pw = new PrintWriter(uc.getOutputStream());
 pw.println(str);
 pw.close();

 BufferedReader in = new BufferedReader(
 new InputStreamReader(uc.getInputStream()));
 String res = in.readLine();
 in.close();

 // assign posted variables to local variables
 String itemName = request.getParameter("item_name");
 String itemNumber = request.getParameter("item_number");
 String paymentStatus = request.getParameter("payment_status");
 String paymentAmount = request.getParameter("mc_gross");
 String paymentCurrency = request.getParameter("mc_currency");
 String txnId = request.getParameter("txn_id");
 String receiverEmail = request.getParameter("receiver_email");
 String payerEmail = request.getParameter("payer_email");
 String custom = request.getParameter("custom");
 String paymentDate = request.getParameter("payment_date");
 String quantity=request.getParameter("quantity");

 logger.debug("itemName : "+itemName);
 logger.debug("itemNumber : "+itemNumber);
 logger.debug("paymentStatus : "+paymentStatus);
 logger.debug("paymentAmount : "+paymentAmount);
 logger.debug("paymentCurrency : "+paymentCurrency);
 logger.debug("txnId : "+txnId);
 logger.debug("receiverEmail : "+receiverEmail);
 logger.debug("payerEmail : "+payerEmail);
 logger.debug("custom : "+custom);
 logger.debug("paymentDate : "+paymentDate);

 //check notification validation
 if(res.equals("VERIFIED")) {
 logger.debug("VERIFIED");
 
  String login_info[]=custom.split(",");
  String PIN=login_info[0];
  long accountID=Long.parseLong(PIN);
  ClientDTO clientDTO=ClientRepository.getInstance().getClient(accountID);
 
config.PaymentGatewayCredentialDAO paymentcredentialdao = new config.PaymentGatewayCredentialDAO();
OnlinePaymentConfigDTO paymentConfigDTO=paymentcredentialdao.getPaymentGatewayCredential(clientDTO, OnlinePaymentConfigDTO.paypalgateway);
 String payPalID= paymentConfigDTO.getpaypalID();// RadiusConfiguration.getInstance().getConfigurationValue(RadiusConfigurationConstants.PAYPAL_ID);
 double tax=paymentConfigDTO.getTaxAmount();
 if(paymentStatus.equals("Completed") && receiverEmail.equals(payPalID)){

 LoginDTO login_dto_for_paypal=new LoginDTO();
 login_dto_for_paypal.setAccountID(accountID);
 login_dto_for_paypal.setUsername(login_info[1]);
 login_dto_for_paypal.setLoginSourceIP(login_info[2]);

 logger.debug("login dto user name: "+login_dto_for_paypal.getUsername());
 logger.debug("login dto account id: "+login_dto_for_paypal.getAccountID());
 logger.debug("login dto source ip: "+login_dto_for_paypal.getLoginSourceIP());
 String desc="Account Recharged by Paypal";

 double amount=Double.parseDouble(paymentAmount);
 if(tax>0)
 {
	 amount-=(amount*tax)/100.0;
	 desc+="after deducting tax "+tax+"%";
 }
 WebPaymentClient wbPaymentClient = new WebPaymentClient(""
			+ clientDTO.getUniqueID(), "", "USD",
			" ", "",
			amount+"");
	
	logger.debug("AccumulatedAmount : " + amount );
	wbPaymentClient.makePayment();	
 
 //dao.rechargeAccountByOnlinePaymentGateway(login_dto_for_paypal,amount ,txnId,desc);
 session.setAttribute("CARD_RECHARGE","Account Recharged Successfully.");

 	long ppID = DatabaseManager.getInstance().getNextSequenceId("vbPaypalPayment");
 	cn = DatabaseManager.getInstance().getConnection();
 	String sql="insert into vbPaypalPayment(ppId,ppPIN,ppClientID,ppPaymentDate,ppTransactionId,ppItemName,";
 	sql+="ppPaymentAmount,ppCurrency,ppPayerEmail,ppReceiverEmail,ppPaymentStatus,ppLocalServerTime) ";
 	sql+="values(?,?,?,?,?,?,?,?,?,?,?,?)";
 	ps = cn.prepareStatement(sql);
 	ps.setLong(1, ppID);
 	ps.setString(2, PIN);
 	ps.setLong(3, accountID);
 	ps.setString(4, paymentDate);
 	ps.setString(5, txnId);
 	ps.setString(6, itemName);
 	ps.setFloat(7, Float.parseFloat(paymentAmount));
 	ps.setString(8, paymentCurrency);
 	ps.setString(9, payerEmail);
 	ps.setString(10, receiverEmail);
 	ps.setString(11, paymentStatus);
 	ps.setLong(12, System.currentTimeMillis());
 	ps.executeUpdate();
 	}
 }
 else if(res.equals("INVALID")) {
 logger.debug("INVALID");

 }
 else {
 logger.debug("Error in IPN Reply.");
 }

 }catch(Exception ex){
 logger.debug("Error in Paypal transaction: "+ex);
 }

 finally {
   try {
      if (ps != null) {
        ps.close();
        }
      if (cn != null) {
        DatabaseManager.getInstance().freeConnection(cn);
        }
      }
      catch (Exception e) {
 	 logger.debug("Exception closing connection: "+e);
      }
     }
 %>
