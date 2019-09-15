package filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import static util.HttpRequestUtils.*;
import common.LoggedInUserService;
import common.UserActivityLog;
import config.GlobalConfigConstants;
import login.LoginDTO;
import lombok.extern.log4j.Log4j;
import util.ServiceDAOFactory;
@Log4j
public class LoggedInUserActivityFilter implements Filter{

	private LoggedInUserService loggedInUserService = ServiceDAOFactory.getService(LoggedInUserService.class);

	@Override
	public void destroy() {
		log.info("*******Killing Thread*******");
		log.info("*******LoggedInUserService Thread*******");
		loggedInUserService.kill();
		log.info("******Done******");
	}
	
	private boolean isLoggable(ServletRequest request){

		String requestedURI = ((HttpServletRequest)request).getRequestURI();
		
		
		for(String loginIgnoreExt:  loginIgnoreExtList){
			if(requestedURI.endsWith(loginIgnoreExt)){
				return false;
			}
			
			if(isAjaxRequest(request) && isGetRequest(request)){
				return false;
			}
		}
		
		if(requestedURI.endsWith("ClientProfileInfo.do")){
			return false;
		}
		
		return true;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		UserActivityLog userActivityLog = createUserActivityLogByRequest((HttpServletRequest) request);

		
		boolean isLoggable = isLoggable(request);
		if(isLoggable){
			loggedInUserService.insert(userActivityLog);
		}
		GlobalConfigConstants.contextMap.putIfAbsent("context", formContextPath((HttpServletRequest) request));
		filterChain.doFilter(request, response);
		
	}

	private UserActivityLog createUserActivityLogByRequest(HttpServletRequest request) {
		UserActivityLog userActivityLog = new UserActivityLog();
		
		userActivityLog.setIPAddress(request.getRemoteAddr());
		userActivityLog.setRequestMethod(request.getMethod());
		userActivityLog.setURI(request.getRequestURL().toString());
		LoginDTO loginDTO = getLoginDTO(request);
		userActivityLog.setUserID( loginDTO==null?0:(loginDTO.getIsAdmin()?-loginDTO.getUserID():loginDTO.getAccountID()));
		userActivityLog.setHeaders(getHeaders(request));
		
		return userActivityLog;
	}
	private String getCookies(HttpServletRequest request){
		StringBuilder stringBuilder = new StringBuilder();
		for( Cookie cookie: request.getCookies()){
			stringBuilder.append(cookie.getName())
						.append("=")
						.append(cookie.getValue())
						.append("\n");
		}
		return stringBuilder.toString();
	}
	private String getHeaders(HttpServletRequest request){
		StringBuilder stringBuilder = new StringBuilder();
		Enumeration<String> headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements()){
			String headerName = headerNames.nextElement(); 
			stringBuilder.append(headerName)
						.append("=")
						.append(request.getHeader(headerName))
						.append("\n");
		}
		return stringBuilder.toString();
	}

	@Override
	public void init(FilterConfig arg0) {
	}

	private String formContextPath(HttpServletRequest request){
		StringBuffer url = request.getRequestURL();
		String uri = request.getRequestURI();
		String context = url.substring(0, url.indexOf(uri));
		context = context + request.getContextPath() + "/";
		return  context;
	}
}
