/**
 * 
 */
package requestMapping;

public class ApiResponse {
	
	public static final int API_RESPONSE_SUCCESS = 1;
	public static final int API_RESPONSE_ERROR = 2;
	
	int responseCode;
	Object payload;
	String msg;
	public ApiResponse(){}
	public ApiResponse(int responseCode,Object payload,String msg){
		this.responseCode = responseCode;
		this.payload = payload;
		this.msg = msg;
	}
	public static ApiResponse makeErrorResponse(String msg) {
		
		return new ApiResponse( API_RESPONSE_ERROR, null, msg );
	}
	public static ApiResponse makeSuccessResponse(String msg) {
		
		return new ApiResponse( API_RESPONSE_SUCCESS, null, msg );
	}
}