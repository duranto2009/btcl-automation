package filter;

import com.google.gson.Gson;
import common.Context;
import login.LoginDTO;
import org.apache.log4j.Logger;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.ApiResponse;
import sessionmanager.SessionConstants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static util.HttpRequestUtils.*;

class DefaultFailureJasonDTO {
	public String failureMsg;
}

public class LoginFilter implements Filter {

	
	String loginURI = "";
	Logger logger = Logger.getLogger(getClass());

	@Override
	public void destroy() {
	}

	boolean isServerPageRequest(String requestedURI) {
		if (!requestedURI.endsWith("Login.do") && (requestedURI.endsWith("do") || requestedURI.endsWith("jsp")
				|| requestedURI.endsWith("JqueryFileUpload"))) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		httpServletRequest.getSession(true).setAttribute("root",
				httpServletRequest.getSession().getServletContext().getContextPath());
		
		String methodName = httpServletRequest.getMethod();
		String[] loginCheckIgnoreList = new String[] { ".*/UsernameAvailability.do",".*/userguide.do", ".*/paymentstep.do", ".*/api/.*.jsp", ".*/payment/api/.*",
				".*billViewPrintable.jsp?.*", ".*addNewClient.jsp", ".*email-phone-verification-form.jsp" ,
				".*Client/Registration.do", ".*Client/RegistrationForm.do", 
				".*Client/temporaryAdd.do?.*",
				".*Client/get-verification-page.do",
				".*Client/get-reg-form.do?.*",
				".*Client/check-availability.do?.*",
				".*Client/send-verification-mail.do?.*",
				".*Client/send-verification-sms.do?.*",
				".*ClientType/GetRegistrantCategory.do", ".*ClientType/GetCompanyIdentityList.do", ".*ClientType/GetIndividualIdentityList.do",
				".*ClientType/getAllModules.do",
				".*ClientType/GetRegistrantCategory.do?.*",
				".*ClientType/GetRegistrantTypesInAModule.do?.*",
				".*Client/get-temporary-client?.*",
				".*Client/verify-sms.*",
				".*Client/new-client-registration-by-client.*",
				".*Client/new-client-registration-by-admin.*",
				".*Client/get-all-countries.do.*",
				".*view-all-connections.do",
				".*ClientType/getSubCategoriesUnderACategory.do?.*",
				".*/assets/static/.*", ".*/flow/test.*", ".*/flow/admin.*",
				".*Client/Add.do", ".*searchDomain.jsp",
				".*DomainChecker.do", ".*forgetPassword.jsp", ".*ForgetPassword.do", ".*/api/client.do",
				".*guideLines.jsp", ".*500.jsp", ".*/api/.*"  };

		String requestedURI = httpServletRequest.getRequestURI();

		boolean isLoginCheckNeeded = true;
		for (String ignorePath : loginCheckIgnoreList) {
			if (requestedURI.matches(ignorePath)) {
				isLoginCheckNeeded = false;
				break;
			}
		}

		loginURI = httpServletRequest.getSession(true).getAttribute("root") + "/";
		Context.BASE_PATH = "../../.."+httpServletRequest.getContextPath()+"/";
		if (!isServerPageRequest(requestedURI)) {
			if (isLoggedIn(httpServletRequest)) {
				handleLoggedInNonServerPageRequest(methodName, requestedURI, httpServletRequest, httpServletResponse,
						chain);
			} else {
				// send to login page
				chain.doFilter(request, response);
			}
		} else {
			handleServerPageRequest(httpServletRequest, httpServletResponse, chain, isLoginCheckNeeded);
		}
	}

	private void handleLoggedInNonServerPageRequest(String methodName, String requestedURI,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain chain)
			throws IOException, ServletException {

		String homePageURI = httpServletRequest.getSession(true).getAttribute("root") + "/dashboard.do";
		if (methodName.equalsIgnoreCase("get")) {
			boolean isPassable = false;
			for (String loginIgnoreExtension : loginIgnoreExtList) {
				if (requestedURI.endsWith(loginIgnoreExtension)) {
					isPassable = true;
				}
			}
			if (isPassable) {
				chain.doFilter(httpServletRequest, httpServletResponse);
			} else {
				httpServletResponse.sendRedirect(homePageURI);
			}

		} else {
			chain.doFilter(httpServletRequest, httpServletResponse);
		}
	}

	private void handleServerPageRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			FilterChain chain, boolean isLoginCheckNeeded) throws IOException, ServletException {

		if(!isAjaxRequest(httpServletRequest)  && isGetRequest(httpServletRequest)) {
			setLastRequestedURI(httpServletRequest);
		}
		
		/**
		 * @author Dhrubo
		 */
		if (isLoggedIn(httpServletRequest)) {
			if(isCurrentRequestIPSameAsLoginSourceIP(httpServletRequest)) {
				chain.doFilter(httpServletRequest, httpServletResponse);
			} else {
				httpServletRequest.getSession(false).invalidate();
				httpServletResponse.sendRedirect(loginURI);
			}
		} else {
			handleNotLoggedInServerPageRequest(httpServletRequest, httpServletResponse, chain, isLoginCheckNeeded);
		}
	}
	
	/**
	 * @author Dhrubo
	 */
	private boolean isCurrentRequestIPSameAsLoginSourceIP(HttpServletRequest request) {
		LoginDTO loginDTO = (LoginDTO) request.getSession(false).getAttribute(SessionConstants.USER_LOGIN);
		if(loginDTO != null) {
			if(getClientIpAddress(request).equals(loginDTO.getLoginSourceIP())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @author Dhrubo
	 */
	private String getClientIpAddress(HttpServletRequest request) {
	    String remoteAddr = "";
	    if (request != null) {
	        remoteAddr = request.getHeader("X-FORWARDED-FOR");
	        if (remoteAddr == null || "".equals(remoteAddr)) {
	            remoteAddr = request.getRemoteAddr();
	        }
	    }
	    return remoteAddr;
	}

	private void handleNotLoggedInServerPageRequest(HttpServletRequest request, HttpServletResponse response,
			FilterChain chain, boolean isLoginCheckNeeded) throws IOException, ServletException {
		if (!isLoginCheckNeeded) {
			chain.doFilter(request, response);
		} else {
			if (isAjaxRequest((HttpServletRequest) request)) {

				/*
				 * This method need to be worked. So that a response is sent as
				 * json to ajax requests
				 */
				ApiResponse apiResponse = new ApiResponse(AnnotatedRequestMappingAction.API_RESPONSE_NOT_LOGGED_IN,
						null, "User not logged in.Please Login to continue");
				response.getWriter().println(new Gson().toJson(apiResponse));
			} else {
				response.sendRedirect(loginURI);
			}
		}
	}

	private boolean isLoggedIn(HttpServletRequest httpServletRequest) {
		LoginDTO loginDTO = (login.LoginDTO) httpServletRequest.getSession(true)
				.getAttribute(SessionConstants.USER_LOGIN);
		return loginDTO != null;
	}

	private void setLastRequestedURI(HttpServletRequest httpServletRequest) {
		String requestedURI = httpServletRequest.getRequestURI();
		if (requestedURI != null && !requestedURI.endsWith("Login.do") && !requestedURI.endsWith("login.jsp") 
				&& !requestedURI.endsWith("Client/Registration.do") && !requestedURI.contains("assets")
				&& !requestedURI.endsWith("/") && (requestedURI.contains(".do")
						|| requestedURI.contains(".jsp") && "get".equalsIgnoreCase(httpServletRequest.getMethod()))) {

			String lastRequestedURL = requestedURI;
			if (httpServletRequest.getQueryString() != null) {
				lastRequestedURL += "?" + httpServletRequest.getQueryString();
			}
			httpServletRequest.getSession(true).setAttribute("lastRequestedURL", lastRequestedURL);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
