package com.elevenpaths.latch.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.elevenpaths.latch.LatchKeys;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;

public class LatchFilter implements Filter {
	private static Log log = LogFactoryUtil.getLog(LatchFilter.class);
	private String secondFactorUrl;

	public void init(FilterConfig config) throws ServletException {
		secondFactorUrl = config.getInitParameter("latch-second-factor-url");
	}
	
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain) throws IOException, ServletException {
		
		boolean doChain = true;
		if(servletRequest instanceof HttpServletRequest){
			try {
				final HttpServletRequest request = (HttpServletRequest) servletRequest;
				final HttpServletResponse response = (HttpServletResponse) servletResponse;
				final User user = PortalUtil.getUser(request);
				if(user!=null){
					final HttpSession session = request.getSession();
					final boolean hasToken = session.getAttribute(LatchKeys.SESSION_ACCOUNT_2_FACTOR)!=null;
					if( hasToken ){
						response.sendRedirect( getPath() );
						doChain=false;
					}
				}
			} catch (Exception e) {
				log.error("Error executing Latch filter: ", e);
			}
		}
		
		if(doChain){
			filterChain.doFilter(servletRequest, servletResponse);
		}
	}
	
	public void destroy() {
		log.info("Latch Filter is down");
	}
	
	/**
	 * Gets second factor authentication path
	 * @return
	 */
	private String getPath(){
		return new StringBuilder(PortalUtil.getPathMain()).append(secondFactorUrl).toString();
	}

}
