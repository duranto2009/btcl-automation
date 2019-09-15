package common;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CommonActionStatusDTO {
	int statusCode;
	String statusName;
	String title;
	String message;
	
	public static final int IGNORE_STATUS_CODE = 100; //ignore
	public static final int SUCCESS_STATUS_CODE = 200; //success
	public static final int WARNING_STATUS_CODE = 300; //warning
	public static final int ERROR_STATUS_CODE = 400;  //error
	
	public CommonActionStatusDTO() {
		this.statusCode = SUCCESS_STATUS_CODE;
	}

	public CommonActionStatusDTO(String message) {
		this.message = message;
		this.statusCode = SUCCESS_STATUS_CODE;
	}

	public CommonActionStatusDTO(int statusCode, String message) {
		this.statusCode = statusCode;
		this.message = message;
	}

	public CommonActionStatusDTO(int statusCode, String statusName, String message) {
		this.statusCode = statusCode;
		this.statusName = statusName;
		this.message = message;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * This method store a message in session. Try to use storeInAttribute method to avoid possible memory leak.
	 * Don't use this if you don't need this message to be accessed from several places - Alam
	 * @param request Http Servlet request sent to action by tomcat
	 * @param response Http Servlet response sent to action by tomcat
	 */
	public void storeInSession(HttpServletRequest request, HttpServletResponse response) {
		request.getSession(true).setAttribute("actionStatusDTO", this);
	}
	
	/**
	 * This method is similar to another store in session, just without the response parameter.
	 * @author Alam
	 * @param request Httpservlet Request object
	 */
	public void storeInSession( HttpServletRequest request ) {
		request.getSession(true).setAttribute("actionStatusDTO", this);
	}

	/**
	 * This method store a message in request object. This message will be accessible as long as the request object exists.
	 * Passing message using this will avoid possible memory leak - Alam
	 * @param request Http Servlet request sent to action by tomcat
	 * @param response Http Servlet response sent to action by tomcat
	 */
	public void storeInAttribute(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("actionStatusDTO", this);

	}
	
	/**
	 * This method is similar to another store in request, just without the response parameter.
	 * @author Alam
	 * @param request Httpservlet Request object
	 */
	public void storeInAttribute( HttpServletRequest request ) {
		request.setAttribute("actionStatusDTO", this);

	}

	public static CommonActionStatusDTO getCommonActionDTO(HttpServletRequest request, HttpServletResponse response) {
		CommonActionStatusDTO dto  = (CommonActionStatusDTO) request.getAttribute("actionStatusDTO");
		if (dto == null) {
			dto = (CommonActionStatusDTO) request.getSession(true).getAttribute("actionStatusDTO");
		}
		request.getSession(true).removeAttribute("actionStatusDTO");
		return dto;
	}
	public static CommonActionStatusDTO getWarningActionDTO(HttpServletRequest request, HttpServletResponse response) {
		CommonActionStatusDTO dto  = (CommonActionStatusDTO) request.getAttribute("warningActionStatusDTO");
		if (dto == null) {
			dto = (CommonActionStatusDTO) request.getSession(true).getAttribute("warningActionStatusDTO");
		}
		request.getSession(true).removeAttribute("warningActionStatusDTO");
		return dto;
	}

	@Override
	public String toString() {
		return "CommonActionStatusDTO [statusCode=" + statusCode + ", statusName=" + statusName + ", title=" + title
				+ ", message=" + message + "]";
	}

	/**
	 * This method set error message and status code to current object. Try to save it in request
	 * object to avoid memory leak.
	 * @author Alam 
	 * @param message Error message to set
	 * @param storeInRequest Will store it request if set true, otherwise will store in session.
	 * @param request HttpServletRequest type object
	 */
	public void setErrorMessage( String message, boolean storeInRequest, HttpServletRequest request ){
		
		this.setMessage( message );
		this.setStatusCode( CommonActionStatusDTO.ERROR_STATUS_CODE );
		
		if( storeInRequest )
			this.storeInAttribute( request );
		else
			this.storeInSession(request);
	}
	
	/**
	 * This method set success message and status code to current object. Try to save it in request
	 * object to avoid memory leak.
	 * @author Alam 
	 * @param message Error message to set
	 * @param storeInRequest Will store it request if set true, otherwise will store in session.
	 * @param request HttpServletRequest type object
	 */
	public void setSuccessMessage( String message, boolean storeInRequest, HttpServletRequest request ){
		
		this.setMessage( message );
		this.setStatusCode( CommonActionStatusDTO.SUCCESS_STATUS_CODE );
		
		if( storeInRequest )
			this.storeInAttribute( request );
		else
			this.storeInSession( request );
	}
	
	public void setWarningMessage( String message, boolean storeInRequest, HttpServletRequest request ){
		
		this.setMessage( message );
		this.setStatusCode( CommonActionStatusDTO.WARNING_STATUS_CODE );
		if(storeInRequest){
			request.setAttribute("warningActionStatusDTO", this);
		}else{
			request.getSession(true).setAttribute("warningActionStatusDTO", this);
		}
	}

}
