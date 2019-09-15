package login;

import common.CommonActionStatusDTO;
import common.repository.AllClientRepository;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.struts.action.*;
import sessionmanager.SessionManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginAction extends Action
{
	static Logger logger= Logger.getLogger(LoginAction.class);;
    private static boolean initializeStatus = false;

    public LoginAction()
    {
    }

    public synchronized void initializeiTelBilling(HttpServletRequest p_request)
    {
        if(!initializeStatus)
        {
        	logger.info("Directory: " + System.getProperty("user.dir"));
        	String realContextPath = p_request.getSession().getServletContext().getRealPath("/");
        	String loggerFilePath=this.getServlet().getInitParameter("log4j-properties-location");
            PropertyConfigurator.configure(realContextPath+loggerFilePath);
            
            /*for future use: turning off of logger*/
            /*List<Logger> loggers = Collections.<Logger>list(LogManager.getCurrentLoggers());
            loggers.add(LogManager.getRootLogger());
            for ( Logger logger : loggers ) {
            	logger.debug(logger.getLevel());
            	logger.setLevel(Level.INFO);
            	if("DEBUG".equals(logger.getName())){
            		logger.setLevel(Level.OFF);
            	}
            }*/
            
            //File realContextPathFile = new File(realContextPath);
            //AutoSignUp.getInstance(realContextPath);
            initializeStatus = true;
        }
    }

    public ActionForward execute(ActionMapping p_mapping, ActionForm p_form, HttpServletRequest p_request, HttpServletResponse p_response) throws ServletException, IOException
    {
        if(!initializeStatus)
            initializeiTelBilling(p_request);
        String target = "home";        
        String mailPass = p_request.getParameter("mailPassword");
        
        if(mailPass != null && mailPass.equals("1"))
        {
            logger.debug("Value of mailPassword:" + mailPass);
            target = "mailPassword";
            return p_mapping.findForward(target);
        }
        LoginForm form = (LoginForm)p_form;
        LoginService service = new LoginService();
        LoginDTO dto = new LoginDTO();
        
        dto.setLoginSourceIP(p_request.getRemoteAddr());
        //System.out.println("ip:"+dto.getLoginSourceIP());
        dto.setUsername(form.getUsername());
        dto.setPassword(form.getPassword());
        dto.setUserAgent(p_request.getHeader("User-Agent"));
        
        String languageID = p_request.getParameter("language");
        if(languageID == null)
            languageID = "1";
        dto.setLanguageID(Integer.parseInt(languageID));
        try
        {
            dto = service.validateUser(dto);

            if(dto == null )
            {
                logger.info("Invalid Login");
                //System.out.println("Invalid Login");
                target = "actionError";
                ActionErrors errors = new ActionErrors();
                errors.add("loginFailure", new ActionMessage("error.loginfailure"));
                saveErrors(p_request, errors);
                new CommonActionStatusDTO().setErrorMessage("Invalid Username or Password", false, p_request);
            }else
            {
                dto.getIsAdmin();
            	logger.debug("Login Accepted");
                p_request.getSession(true).setAttribute("user_login", dto);
                logger.debug("Remote Address :" + p_request.getRemoteAddr());
                AllClientRepository.getInstance().reloadClientRepository(false);
            }
        }
        catch(Exception e)
        {
            logger.fatal("Exception during login", e);
            target = "failure";
            SessionManager.setFailureMessage(p_request, e.toString());
        }
        logger.debug("Target :" + target);
        
        Object lastRequestedUrlObj=p_request.getSession(true).getAttribute("lastRequestedURL");
        String lastRequestedURL =null;
	    if(lastRequestedUrlObj!=null){
	    	lastRequestedURL=lastRequestedUrlObj.toString();
	    	if(blockLastRequestedURL(lastRequestedURL)) {
	    	    lastRequestedURL = null;
            }

	    }
        logger.debug("forward after login: "+ lastRequestedURL);
        
        if(lastRequestedURL!=null){
        	p_response.sendRedirect(lastRequestedURL);
        	return null;
        }else{
             return p_mapping.findForward(target);
        }
    }

    private boolean blockLastRequestedURL(String lastRequestedURL) {
        return
                lastRequestedURL.contains("AddClient.do")
                        || lastRequestedURL.contains("ClientProfileInfo")
                        || lastRequestedURL.contains("addNewClient")
                        || lastRequestedURL.contains("ForgetPassword")
                        || lastRequestedURL.contains("terms-and-conditions")
                        || lastRequestedURL.contains("ClientType/getAllModules")
                        || lastRequestedURL.contains("dashboard/get-data")
                        || lastRequestedURL.contains("ClientType/getSubCategoriesUnderACategory")
                        || lastRequestedURL.contains("Client/check-availability")
                        || lastRequestedURL.contains("Client/UsernameAvailability")
                        || lastRequestedURL.contains("Client/send-verification-mail")
                        || lastRequestedURL.contains("Client/send-verification-sms")
                        || lastRequestedURL.contains("Client/get-temporary-client")
                        || lastRequestedURL.contains("Client/Registration")
                        || lastRequestedURL.contains("Client/verify-sms")
                        || lastRequestedURL.contains("Client/get-all-countries")
                        || lastRequestedURL.contains("Client/new-client-registration-by-admin")
                        || lastRequestedURL.contains("Client/new-client-registration-by-client")


                ;
    }
}