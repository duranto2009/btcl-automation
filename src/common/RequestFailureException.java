package common;

public class RequestFailureException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RequestFailureException(String msg){
		super(msg);
	}
}
