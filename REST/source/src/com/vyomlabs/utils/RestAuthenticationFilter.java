package com.vyomlabs.utils;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

public class RestAuthenticationFilter implements javax.servlet.Filter {

	public static final String AUTHENTICATION_HEADER = "Authorization";
	private final static Logger log = Logger.getLogger(RestAuthenticationFilter.class.getName());

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter) 
			throws IOException, ServletException {
		log.info("START Authentication");		
		 
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			
			String contentType = httpServletRequest.getHeader("content-type");
			log.info("Incoming ContentType : "+contentType);
			
			String authCredentials = httpServletRequest.getHeader(AUTHENTICATION_HEADER);
			log.info("AuthCredentials : "+authCredentials);
			
			AuthenticationService authenticationService = new AuthenticationService();
			boolean authenticationStatus = authenticationService.authenticate(authCredentials);
			
			if (authenticationStatus) {
				log.info("Authorized Access Found.");
				filter.doFilter(request, response);
//				filter.doFilter(new ValidatingHttpRequest(httpServletRequest), response);
				log.info("Do filter finished.");
			} else {
				log.info("Unauthorized Access.");
				if (response instanceof HttpServletResponse) {
					HttpServletResponse httpServletResponse = (HttpServletResponse) response;
					httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized Access.");
				}
			}
		}
		log.info("END Authentication");
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
}
