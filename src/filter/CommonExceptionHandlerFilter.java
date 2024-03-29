package filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.util.ajax.JSON;

import org.apache.log4j.Logger;
import common.RequestFailureException;
import common.RequestFailureExceptionForwardToSamePage;
import common.StringUtils;

public class CommonExceptionHandlerFilter implements Filter {

	Logger logger = Logger.getLogger(getClass());
	final String errorPageURI = "/common/failure.jsp";

	@Override
	public void init(FilterConfig paramFilterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {

			if (isGetRequest(request)) {
				String requestedURLWithQueryParameter = getRequestedURLWithQueryParameter(request);
				request.setAttribute("requestedURL", URLEncoder.encode(requestedURLWithQueryParameter, "UTF-8"));
			}

			chain.doFilter(request, response);
		} catch (Exception ex) {
			ex.printStackTrace();
			
			
			
			
			String referer=(String)((HttpServletRequest)request).getHeader("referer");

			String applicationContex = ((HttpServletRequest) request).getContextPath();
			int indexOfRelativePathOfRefferer = referer.indexOf(applicationContex)+applicationContex.length();
			//referer = referer.substring(indexOfRelativePathOfRefferer);
			
			
			while(ex.getCause()!=null && ex.getCause() instanceof Exception
					&& !(ex instanceof RequestFailureException)){
				ex = (Exception)ex.getCause();
			}
			

			String failureMsg = (ex instanceof RequestFailureException)?ex.getMessage():"Server Error";
			String forwardingLink = (!StringUtils.isBlank(referer))?referer:errorPageURI;
			
			handleExceptionForwardToErrorPage(request, response, failureMsg, forwardingLink);
			
			
			/*
			if (ex.getCause() != null && ex.getCause() instanceof RequestFailureException) {

				handleExceptionForwardToErrorPage(request, response, ex.getCause().getMessage(), errorPageURI);
			} else if (ex.getCause() != null && ex.getCause() instanceof RequestFailureExceptionForwardToSamePage) {
				handleExceptionForwardToSamePage(request, response, ex.getCause().getMessage());
			} else {
				logger.debug("Fatal", ex);
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				ex.printStackTrace(pw);
				String failureMsg = sw.toString(); // stack trace as a
													// string
				handleExceptionForwardToErrorPage(request, response, "server error", errorPageURI);
			}*/

		}

	}

	private void handleExceptionForwardToSamePage(ServletRequest request, ServletResponse response, String failureMsg)
			throws ServletException, IOException {
		String requestedURL = request.getParameter("requestedURL");
		// parseQueryAndInsertIntoRequest(requestedURL, request);
		String requestedURI = requestedURL;
		logger.debug("requestedURI " + requestedURI);
		request.setAttribute("failureMsg", failureMsg);
		String applicationContex = ((HttpServletRequest) request).getContextPath();
		requestedURI = requestedURI.replace(applicationContex, "");
		request.getRequestDispatcher(requestedURI).forward(request, response);

	}

	private void handleExceptionForwardToErrorPage(ServletRequest request, ServletResponse response, String failureMsg,
			String errorPageURI) throws IOException, ServletException {
		if (isAjaxRequest((HttpServletRequest) request)) {
			// send failure jason
			DefaultFailureJasonDTO defaultFailureJasonDTO = new DefaultFailureJasonDTO();
			defaultFailureJasonDTO.failureMsg = failureMsg;
			response.setContentType("application/json");
			response.getWriter().write(new JSON().toJSON(defaultFailureJasonDTO));

		} else {
			((HttpServletRequest)request).getSession().setAttribute("failureMsg", failureMsg);
			((HttpServletResponse)response).sendRedirect(errorPageURI);
			
		}
	}

	private String getRequestedURLWithQueryParameter(ServletRequest request) {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String requestedURLWithQueryParameter = httpServletRequest.getRequestURI();

		boolean isParameterAdded = false;
		Enumeration<String> names = request.getParameterNames();
		while (names.hasMoreElements()) {
			if (isParameterAdded) {
				requestedURLWithQueryParameter += "&";
			} else {
				requestedURLWithQueryParameter += "?";
			}
			String name = names.nextElement();
			String value = request.getParameter(name);
			if (!StringUtils.isBlank(value)) {
				requestedURLWithQueryParameter += (name + "=" + value);
			}
			isParameterAdded = true;

		}

		return requestedURLWithQueryParameter;

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	private void parseQueryAndInsertIntoRequest(String url, ServletRequest request) {
		if (url.contains("?")) {
			String queryString = url.substring(url.indexOf('?') + 1);
			String[] requestParams = queryString.split("&");
			for (String requestParam : requestParams) {
				String[] keyValue = requestParam.split("=");
				request.setAttribute(keyValue[0], keyValue[1]);
			}
		}
	}

	private boolean isGetRequest(ServletRequest request) {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		return httpServletRequest.getMethod().equalsIgnoreCase("GET");
	}

	private boolean isAjaxRequest(HttpServletRequest request) {
		return "X-Requested-With".equals(request.getHeader("XMLHttpRequest"));
	}

}
