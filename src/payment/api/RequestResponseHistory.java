package payment.api;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import annotation.*;
import util.CurrentTimeFactory;

@TableName("at_Request_Response_History")
public class RequestResponseHistory {
	@PrimaryKey
	@ColumnName("rqrsID")
	long ID;
	@ColumnName("rqrsIpAddress")
	String IPAddressOfTheRequest;
	@ColumnName("rqrsURI")
	String requestedURI;
	@ColumnName("rqrsHeaders")
	String requestHeaders;
	@ColumnName("rqrsParameters")
	String requestParameters;
	@ColumnName("rqrsRequestMethod")
	String requestMethod;
	@ColumnName("rqrsPathInfo")
	String requestPathInfo;
	@ColumnName("rqrsRequestTime")
	long requestTime;
	@ColumnName("rqrsResponseBody")
	String responseBody;
	@ColumnName("rqrsResponseTime")
	Long responseTime;
	@ColumnName("rqrsBankCode")
	String bankCode;
	
	public String getBankCode() {
		return bankCode;
	}


	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}


	public static RequestResponseHistory createRequestResponseHistory(HttpServletRequest request,Object response,long responseTime){
		RequestResponseHistory requestResponseHistory = new RequestResponseHistory();
		
		requestResponseHistory.setIPAddressOfTheRequest(request.getRemoteAddr());
		requestResponseHistory.setRequestedURI(request.getRequestURI());
		
		Enumeration<String> headerNames = request.getHeaderNames();
		StringBuilder headers = new StringBuilder(); 
		while(headerNames.hasMoreElements()){
			String headerName = headerNames.nextElement(); 
			headers.append("name:")
					.append(headerName)
					.append(":")
					.append("values:[");
			
			Enumeration<String> headerValues = request.getHeaders(headerName);
			
			while(headerValues.hasMoreElements()){
				headers.append(headerValues.nextElement());
			}
			headers.append("]\n");
		}
		requestResponseHistory.setRequestHeaders(headers.toString());
		requestResponseHistory.setRequestParameters(request.getParameterMap().toString());
		requestResponseHistory.setRequestMethod(request.getMethod());
		requestResponseHistory.setRequestPathInfo(request.getPathInfo());
		requestResponseHistory.setRequestTime(CurrentTimeFactory.getCurrentTime());
		requestResponseHistory.setResponseBody(response.toString());
		requestResponseHistory.setResponseTime(responseTime);
		requestResponseHistory.setBankCode(request.getParameter("bankCode"));
		return requestResponseHistory;
	}
	
	
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public String getIPAddressOfTheRequest() {
		return IPAddressOfTheRequest;
	}
	public void setIPAddressOfTheRequest(String iPAddressOfTheRequest) {
		IPAddressOfTheRequest = iPAddressOfTheRequest;
	}
	public String getRequestedURI() {
		return requestedURI;
	}
	public void setRequestedURI(String requestedURI) {
		this.requestedURI = requestedURI;
	}
	public String getRequestHeaders() {
		return requestHeaders;
	}
	public void setRequestHeaders(String requestHeaders) {
		this.requestHeaders = requestHeaders;
	}
	public String getRequestParameters() {
		return requestParameters;
	}
	public void setRequestParameters(String requestParameters) {
		this.requestParameters = requestParameters;
	}
	public String getRequestMethod() {
		return requestMethod;
	}
	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}
	public long getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(long requestTime) {
		this.requestTime = requestTime;
	}
	public String getResponseBody() {
		return responseBody;
	}
	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}
	public Long getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(Long responseTime) {
		this.responseTime = responseTime;
	}
	public String getRequestPathInfo() {
		return requestPathInfo;
	}
	public void setRequestPathInfo(String requestPathInfo) {
		this.requestPathInfo = requestPathInfo;
	}
	@Override
	public String toString() {
		return "RequestResponseHistory [ID=" + ID + ", IPAddressOfTheRequest=" + IPAddressOfTheRequest
				+ ", requestedURI=" + requestedURI + ", requestHeaders=" + requestHeaders + ", requestParameters="
				+ requestParameters + ", requestMethod=" + requestMethod + ", requestPathInfo=" + requestPathInfo
				+ ", requestTime=" + requestTime + ", responseBody=" + responseBody + ", responseTime=" + responseTime
				+ "]";
	}
	
}
