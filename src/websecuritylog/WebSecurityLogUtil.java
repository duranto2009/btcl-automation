package websecuritylog;

import java.util.Enumeration;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import login.LoginDTO;
import sessionmanager.SessionConstants;

public class WebSecurityLogUtil {
	static WebSecurityLogService logService = new WebSecurityLogService();
	private static Logger logger = Logger.getLogger(WebSecurityLogService.class);

	private static void commonWebSecurityLogInsertion(HttpServletRequest httpReq, WebSecurityLogDTO webSecurityDTO,
			LoginDTO loginDTO) throws Exception {
		long currentTime = System.currentTimeMillis();

		if (StringUtils.isBlank(webSecurityDTO.getUrl())) {
			webSecurityDTO.setUrl(httpReq.getRemoteHost());
		}
		if (StringUtils.isBlank(webSecurityDTO.getPort())) {
			webSecurityDTO.setPort(httpReq.getRemotePort() + "");
		}
		if (loginDTO != null) {
			webSecurityDTO.setUsername(loginDTO.getUsername());
		}
		if (StringUtils.isBlank(webSecurityDTO.getUsername())) {
			webSecurityDTO.setUsername((httpReq.getRemoteUser() != null) ? httpReq.getRemoteUser() : "N/A");
		}
		webSecurityDTO.setAttemptTime(System.currentTimeMillis());
		webSecurityDTO.setActionTaken(0);
		webSecurityDTO.setLastModificationTime(currentTime);

		logService.addNewLog(webSecurityDTO);
	}

	public static void invalidLoginRequest(LoginDTO loginDTO) {
		WebSecurityLogDTO webSecurityDTO = new WebSecurityLogDTO();

		webSecurityDTO.setUrl(loginDTO.getLoginSourceIP());
		webSecurityDTO.setPort("N/A");
		webSecurityDTO.setPassword(loginDTO.getPassword());
		webSecurityDTO.setDescription("Invalid username or password. User-Agent:  " + loginDTO.getUserAgent());
		webSecurityDTO.setAttemptType(WebSecurityConstant.ATTEMPT_TYPE_INVALID_LOGIN);

		try {
			commonWebSecurityLogInsertion(null, webSecurityDTO, loginDTO);
		} catch (Exception ex) {
			logger.fatal("Error in log insertion", ex);
		}
	}
	
	public static void userFromInvalidIP(LoginDTO loginDTO) {
		WebSecurityLogDTO webSecurityDTO = new WebSecurityLogDTO();

		webSecurityDTO.setUrl(loginDTO.getLoginSourceIP());
		webSecurityDTO.setPort("N/A");
		webSecurityDTO.setDescription("Invalid IP Address. User-Agent:  " + loginDTO.getUserAgent());
		webSecurityDTO.setAttemptType(WebSecurityConstant.ATTEMPT_TYPE_USER_INVALID_IP);

		try {
			commonWebSecurityLogInsertion(null, webSecurityDTO, loginDTO);
		} catch (Exception ex) {
			logger.fatal("Error in log insertion", ex);
		}
	}
	
	

	public static void csrfTokenViolation(HttpServletRequest httpReq) {
		WebSecurityLogDTO webSecurityDTO = new WebSecurityLogDTO();
		LoginDTO loginDTO = (login.LoginDTO) httpReq.getSession(true).getAttribute(SessionConstants.USER_LOGIN);

		webSecurityDTO.setDescription("Potential CSRF detected. Request URL (with param): "
				+ getRequestedURLWithQueryParameter(httpReq) + ", User-Agent: " + httpReq.getHeader("User-Agent"));
		webSecurityDTO.setAttemptType(WebSecurityConstant.ATTEMPT_TYPE_CSRF);

		try {
			commonWebSecurityLogInsertion(httpReq, webSecurityDTO, loginDTO);
		} catch (Exception ex) {
			logger.fatal("Error in log insertion", ex);
		}
	}

	public static void refererMismatch(HttpServletRequest httpReq) {
		WebSecurityLogDTO webSecurityDTO = new WebSecurityLogDTO();
		LoginDTO loginDTO = (login.LoginDTO) httpReq.getSession(true).getAttribute(SessionConstants.USER_LOGIN);

		webSecurityDTO.setAttemptType(WebSecurityConstant.ATTEMPT_TYPE_REFERER);
		webSecurityDTO.setDescription("Potential referer mismatch. Request URL (with param): "
				+ getRequestedURLWithQueryParameter(httpReq) + ", User-Agent: " + httpReq.getHeader("User-Agent"));

		try {
			commonWebSecurityLogInsertion(httpReq, webSecurityDTO, loginDTO);
		} catch (Exception ex) {
			logger.fatal("Error in log insertion", ex);
		}

	}

	private static String getRequestedURLWithQueryParameter(ServletRequest request) {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String requestedURLWithQueryParameter = httpServletRequest.getRequestURI();

		boolean isParameterAdded = false;
		Enumeration<String> names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			String value = request.getParameter(name);
			if ("password".equals(name)) {
				continue;
			}
			if (isParameterAdded) {
				requestedURLWithQueryParameter += "&";
			} else {
				requestedURLWithQueryParameter += "?";
			}

			if (!StringUtils.isBlank(value)) {
				requestedURLWithQueryParameter += (name + "=" + value);
			}
			isParameterAdded = true;

		}

		return requestedURLWithQueryParameter;

	}

}
