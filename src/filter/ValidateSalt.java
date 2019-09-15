package filter;

import com.google.common.cache.Cache;

import common.CommonActionStatusDTO;
import common.StringUtils;
import websecuritylog.WebSecurityLogUtil;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class ValidateSalt implements Filter {
	String[] csrfEnabledList = new String[] { ".*Login.do", ".*DomainAction.do", ".*DomainChecker.do",".*AddClient.do", ".*EditClient.do" };
	Logger logger = Logger.getLogger(this.getClass());
	final String errorPageURI = "/common/500.jsp";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		// Assume its HTTP
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpServletResponse httpRes = (HttpServletResponse) response;

		// Get the salt sent with the request
		String salt = (String) httpReq.getParameter("csrfPreventionSalt");

		// Validate that the salt is in the cache
		Cache<String, Boolean> csrfPreventionSaltCache = (Cache<String, Boolean>) httpReq.getSession()
				.getAttribute("csrfPreventionSaltCache");

		String clientOrigin = httpReq.getScheme() + "://" + httpReq.getServerName() ;
		//logger.debug("clientOrigin: " + clientOrigin);

		if (isGetRequest(request)) {
			httpRes.setHeader("origin", clientOrigin);
			chain.doFilter(request, response);
		} else if (isPostRequest(request)) {
			//logger.debug("referer: "+httpReq.getHeader("referer") + ", clientOrigin: " + clientOrigin);
			if (httpReq.getHeader("referer")==null ||  !httpReq.getHeader("referer").startsWith(clientOrigin)) {
				logger.fatal("Potential referer mismatch detected!! Inform a scary sysadmin ASAP. url: " + httpReq.getRequestURI());
				WebSecurityLogUtil.refererMismatch(httpReq);
				httpRes.sendError(400, "Bad Request" );
				//request.getRequestDispatcher(errorPageURI).forward(request, response);
				//throw new ServletException("Potential referer mismatch detected!! Inform a scary sysadmin ASAP.");
				return;
			}
			if(crsfValidationRequired(request)) {
				logger.info("salt cache: " + (csrfPreventionSaltCache == null ? "null" : csrfPreventionSaltCache) + " salt: " + salt);

				if (csrfPreventionSaltCache != null && salt != null && csrfPreventionSaltCache.getIfPresent(salt) != null) {
					// If the salt is in the cache, we move on
					//logger.debug("CSRF checking passed for " + httpReq.getRequestURI());
					chain.doFilter(request, response);
				}
				else {
					// Otherwise we throw an exception aborting the request flow
					logger.info("httpReq.getRemoteAddr()" + httpReq.getRemoteAddr() + " httpReq.getRemoteHost() " + httpReq.getRemoteHost() +" "+ httpReq.getRemoteUser());
					logger.fatal("Potential CSRF detected!! Inform a scary sysadmin ASAP. url: " + httpReq.getRequestURI());
					WebSecurityLogUtil.csrfTokenViolation(httpReq);
					if(httpReq.getRequestURI()!=null && httpReq.getRequestURI().contains("Login.do")){
						new CommonActionStatusDTO().setErrorMessage("Session time out. Please try again ", false, httpReq);
						String root=httpReq.getSession().getServletContext().getContextPath();
						httpRes.sendRedirect(root+"/");
						logger.warn("Forwarding to login page");
					}else{
						httpRes.sendError(400, "Bad Request" );
						logger.warn("Forwarding to 400.jsp");
					}
					
					//request.getRequestDispatcher(errorPageURI).forward(request, response);
					return;
					//throw new ServletException("Potential CSRF detected!! Inform a scary sysadmin ASAP.");
				}
			}else{
				//logger.info("Ignoring CSRF Token and referer " + httpReq.getRequestURI());
				chain.doFilter(request, response);
			}
		} else {
			//logger.info("Ignoring CSRF Token and referer " + httpReq.getRequestURI());
			chain.doFilter(request, response);
		}

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	private boolean crsfValidationRequired(ServletRequest request) {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String requestedURI = httpServletRequest.getRequestURI();
		//logger.debug("requested URI is " + requestedURI);
		for (String enabledPath : csrfEnabledList) {

			if (requestedURI.contains("Login.do")) {
				return true;
			}

			if (requestedURI.contains("AddClient.do")) {
				return false;
			}
			
			if (requestedURI.contains("EditClient.do")) {
				return false;
			}
			
			if (requestedURI.contains("DomainAction.do")) {
				Enumeration<String> names = request.getParameterNames();
				boolean isMatched = false;
				while (names.hasMoreElements()) {
					String name = names.nextElement();
					String value = request.getParameter(name);
					if (name.equals("mode") && (value.equals("editDomain") || value.equals("checkDomain"))) {
						isMatched = true;
						//logger.info(name + " " + value);
						return isMatched;
					}
				}

			}
			if (requestedURI.contains("DomainChecker.do")) {
				Enumeration<String> names = request.getParameterNames();
				boolean isMatched = false;
				while (names.hasMoreElements()) {
					String name = names.nextElement();
					String value = request.getParameter(name);
					if (name.equals("mode") && value.equals("checkDomain")) {
						isMatched = true;
						//logger.info(name + " " + value);
						return isMatched;
					}
				}

			}
		}
		return false;

	}

	private boolean isGetRequest(ServletRequest request) {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		return httpServletRequest.getMethod().equalsIgnoreCase("GET");
	}

	private boolean isPostRequest(ServletRequest request) {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		return httpServletRequest.getMethod().equalsIgnoreCase("POST");
	}
}