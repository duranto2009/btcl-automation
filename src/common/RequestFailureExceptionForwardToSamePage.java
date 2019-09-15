package common;

public class RequestFailureExceptionForwardToSamePage extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public RequestFailureExceptionForwardToSamePage(String msg){
		super(msg);
	}
}
