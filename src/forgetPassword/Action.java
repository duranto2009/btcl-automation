package forgetPassword;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.mysql.fabric.xmlrpc.Client;

import common.ClientDTO;
import common.CommonActionStatusDTO;
import common.repository.AllClientRepository;
import util.PasswordService;

/**
 * @author Alam
 */
public class Action extends DispatchAction {
	
	private static Logger logger = Logger.getLogger( Action.class );
	
	/**
	 * This method will set a given error message to request object.
	 * @author Alam
	 * @param commonActionStatusDTO an object of CommonActionStatusDto which need to be stored 
	 * @param request request object
	 * @param response response object
	 */
	private void setErrorMessage( CommonActionStatusDTO commonActionStatusDTO, String message, HttpServletRequest request, HttpServletResponse response ){
		
		commonActionStatusDTO.setMessage( message );
		commonActionStatusDTO.setStatusCode( CommonActionStatusDTO.ERROR_STATUS_CODE );
		commonActionStatusDTO.storeInAttribute( request, response );
	}
	
	/**
	 * This method wil set a given success message in attribute
	 * @author Alam
	 * @param commonActionStatusDTO
	 * @param message
	 * @param request
	 * @param response
	 * @param setInSession if set true, object will be stored in session. otherwise in Request object
	 */
	private void setSuccessMessage( CommonActionStatusDTO commonActionStatusDTO, String message, HttpServletRequest request, HttpServletResponse response, boolean setInSession ){
		
		commonActionStatusDTO.setMessage( message );
		commonActionStatusDTO.setStatusCode( CommonActionStatusDTO.SUCCESS_STATUS_CODE );
		
		if( setInSession )
			commonActionStatusDTO.storeInSession( request, response );
		else
			commonActionStatusDTO.storeInAttribute( request, response );
	}
	
	
	/**
	 * This action just forwards user to Forget password page
	 * @author Alam
	 */
	public ActionForward forgetPassword( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		
		//Just show the forget password change form
		String forgetPasswordPage = "forgetPasswordPage";
		
		return mapping.findForward( forgetPasswordPage );
	}

	
	/**
	 * This method receives an username or email as parameter from request and send an OTP to that email
	 * or the email found with the given username. If no email is found or invalid username is given
	 * an error message should be sent with proper error message.
	 * @author Alam
	 */
	public ActionForward sendOTP( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		
		String identifierGiven = request.getParameter("identifierGiven");
		String usernameOrEmail = request.getParameter("username");
		String OTPSentForwardPath = "OTPSent";
		List<String> to = null;
		CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
		
		try{
			
			String token = ForgetPasswordUtility.getPasswordResetToken( 32 );
			
			if( identifierGiven.equals( "username" ) ){
				
				to = ForgetPasswordUtility.getClientEmailFromUsername( usernameOrEmail );
			}
			else{
				
				try{
					
					new InternetAddress( usernameOrEmail, true );
				}
				catch( Exception e ){
					
					throw new Exception( "Give a valid email address" );
				}
				
				ClientDTO clientDTO = ForgetPasswordUtility.getClientDTOFromEmail( usernameOrEmail );
				
				if( clientDTO == null ){
					
					throw new Exception( "Email Doesn't exists in the system" );
				}
				
				to = new ArrayList<String>();
				to.add( usernameOrEmail );
			}
			
			if( ( to == null || to.size() == 0 ) && identifierGiven.equals( "username" ) )
				throw new Exception( "No email is found with this username" );
			
			else if( to == null || to.size() == 0 )
				throw new Exception( "Email is not found in system" );
			
			ForgetPasswordUtility.sendMail( to, token, usernameOrEmail, identifierGiven, request );
			
			ForgetPasswordUtility.insertTokenAndUsername( usernameOrEmail, token );
			
		}
		catch( MessagingException e){
			
			setErrorMessage(commonActionStatusDTO, e.toString(), request, response);
			return mapping.findForward( OTPSentForwardPath );
			
		}
		catch( Exception e){
			
			setErrorMessage(commonActionStatusDTO, e.getMessage(), request, response);
			return mapping.findForward( OTPSentForwardPath );
		}
		
		String mail = to.get(0);
		int index = mail.indexOf( '@' );
		mail = mail.substring( index-2, mail.length() );
		mail = "****" + mail;
		
		setSuccessMessage(commonActionStatusDTO, "An email is sent to your mail address '<b>"+mail+"</b>' containing instruction to change password<br/>This link will be active only for 5 minutes", request, response, true);
		
		return mapping.findForward( OTPSentForwardPath );
	}
	
	
	/**
	 * This action recieves a token and username, verify them and if valid then passed to password reset page.
	 * It also recieves the password change request and update password. It uses form submitted parameter 
	 * to check which request has been sent. 
	 * @author Alam
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward resetPassword( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		
		//Check if password reset request is submitted. 
		String formSubmitted = request.getParameter( "formSubmitted" );
		
		String forgetPasswordPage = "forgetPasswordPage";
		String resetPasswordPage = "resetPassword";
		String login = "login";
		boolean isValidToken = false;
		
		CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
		
		//This means that user has not submitted new password yet
		//Show him the password reset page if token is valid
		if( formSubmitted == null ){
			
			String token = request.getParameter( "token" );
			String username = request.getParameter( "username" );
			
			//If token or username empty send error
			if( StringUtils.isEmpty( token ) || StringUtils.isEmpty( username ) ){
				
				setErrorMessage(commonActionStatusDTO, "Invalid password reset token or username. Please try again", request, response);
				return mapping.findForward( forgetPasswordPage );
			}
			
			try{
				
				isValidToken = ForgetPasswordUtility.verifyUsernameAndToken( username, token );
			}
			catch( Exception e ){
				
				setErrorMessage(commonActionStatusDTO, "Invalid password reset token or username. Please try again", request, response);
				return mapping.findForward( forgetPasswordPage );
			}
				
			
			//If valid token send to reset page
			if( isValidToken ){
				
				request.setAttribute( "validTokenAndUsername", true );
				return mapping.findForward( resetPasswordPage );
			}
			else{
				
				setErrorMessage(commonActionStatusDTO, "Invalid password reset token or username. Please try again", request, response);;
				return mapping.findForward( forgetPasswordPage );
			}
		}
		else{
			
			String password=StringUtils.trimToEmpty(request.getParameter("password"));
			String rePassword=StringUtils.trimToEmpty(request.getParameter("rePassword"));
			String username = StringUtils.trimToEmpty( request.getParameter( "username" ));
			String identifierGiven = StringUtils.trimToEmpty( request.getParameter( "identifierGiven" ));
			String token = StringUtils.trimToEmpty( request.getParameter( "token" ));
			
			try{
				
				isValidToken = ForgetPasswordUtility.verifyUsernameAndToken( username, token );
			}
			catch( Exception e ){
				
				setErrorMessage(commonActionStatusDTO, "Invalid password reset token or username. Please try again", request, response);
				return mapping.findForward( forgetPasswordPage );
			}

			if( StringUtils.isEmpty( username ) ){
				
				setErrorMessage(commonActionStatusDTO, "No username is given", request, response);
				return mapping.findForward( resetPasswordPage );
			}
			
			String validationMessage="";
			
			ClientDTO clientDTO = null; 
			
			if( identifierGiven.equals( "username" ) ){
				
				clientDTO = AllClientRepository.getInstance().getClientByClientLoginName( username );
			}
			else if( identifierGiven.equals( "email" ) ){
				
				clientDTO = ForgetPasswordUtility.getClientDTOFromEmail( username );
			}
			
			try{
		
				if(password.isEmpty() || password.length()>50 || password.length()<4){
					validationMessage += "Password size should be at least 4 or at most 50<br>";
				}
				if( StringUtils.isEmpty( rePassword ) || !password.equals(rePassword)){
					validationMessage += "New Password and  confirm new password are not same<br>";
				}
				
				if(validationMessage.length()>0 ){
				
					setErrorMessage(commonActionStatusDTO, validationMessage, request, response);
					return mapping.findForward( resetPasswordPage );
				}
				
				String encryptedPassword = PasswordService.getInstance().encrypt(password);
				
				if(!clientDTO.getLoginPassword().equals(encryptedPassword))
				{
					clientDTO.setLoginPassword( encryptedPassword );
				}
				clientDTO.setLastModificationTime( System.currentTimeMillis() );
				//Update password of user and remove entry form Token table
				ForgetPasswordUtility.updatePassword( clientDTO, username );
				
				setSuccessMessage(commonActionStatusDTO, "Password is changed successfully", request, response, true);
					
				return mapping.findForward( login );
			}
			catch( Exception e ){
				
				logger.debug( e.toString() );
				setErrorMessage(commonActionStatusDTO, "An error occured while changing the password. Try again", request, response);
				return mapping.findForward( resetPasswordPage );
			}
		}
	}
}
