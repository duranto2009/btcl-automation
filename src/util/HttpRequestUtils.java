package util;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import login.LoginDTO;

public class HttpRequestUtils {
	public static final String[] loginIgnoreExtList = { "css", "js", "jpg", "png", "ico", "ttf", "woff", "woff2", "gif" };
	static public boolean isAjaxRequest(ServletRequest request) {
		return "XMLHttpRequest".equals(((HttpServletRequest)request).getHeader("X-Requested-With"));
	}
	static public LoginDTO getLoginDTO(ServletRequest request){
		return (LoginDTO)((HttpServletRequest)request).getSession(true).getAttribute("user_login");
	}
	static public boolean isGetRequest(ServletRequest request) {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		return httpServletRequest.getMethod().equalsIgnoreCase("GET");
	}
}
