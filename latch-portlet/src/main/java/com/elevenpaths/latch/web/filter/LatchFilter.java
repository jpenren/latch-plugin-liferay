package com.elevenpaths.latch.web.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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
import com.liferay.portal.util.PortalUtil;

public class LatchFilter implements Filter {
	private static Log log = LogFactoryUtil.getLog(LatchFilter.class);
	private String secondFactorUrl;
	private String accountLockedUrl;
	private Set<String> internalUrls = new HashSet<String>();

	public void init(FilterConfig config) throws ServletException {
		secondFactorUrl = config.getInitParameter("latch-second-factor-url");
		accountLockedUrl = config.getInitParameter("latch-account-locked-url");
		internalUrls.add( get2FactorPath() );
		internalUrls.add( getAccountLockedPath() );
		internalUrls.add( PortalUtil.getPathMain()+"/portal/logout" );
	}
	
	public void doFilter(ServletRequest sRequest, ServletResponse sResponse, FilterChain chain) throws IOException, ServletException {
		boolean doChain = true;
		if( sRequest instanceof HttpServletRequest ){
			try {
				final HttpServletRequest request = (HttpServletRequest) sRequest;
				final HttpServletResponse response = (HttpServletResponse) sResponse;
				final HttpSession session = request.getSession();
				final String uri = request.getRequestURI();
				
				if( !isInternalUrl(uri) ){
					//Checks if account is locked
					final boolean accountLocked = session.getAttribute(LatchKeys.SESSION_ACCOUNT_LOCKED)!=null;
					if( accountLocked ){
						response.sendRedirect( getAccountLockedPath() );
						doChain=false;
					}
					
					//Check if has 2 factor
					final boolean hasToken = session.getAttribute(LatchKeys.SESSION_ACCOUNT_2_FACTOR)!=null;
					if( hasToken ){
						response.sendRedirect( get2FactorPath() );
						doChain=false;
					}
				}
			} catch (Exception e) {
				log.error("Error executing Latch filter: ", e);
			}
		}
		
		if( doChain ){
			chain.doFilter(sRequest, sResponse);
		}
	}
	
	public void destroy() {
		log.info("Latch Filter is down");
	}
	
	/**
	 * Check if requested URL is any of the URLs used by Latch Plugin 
	 * @param uri
	 * @return true if is internal URL
	 */
	private boolean isInternalUrl(String uri){
		for(String path : internalUrls){
			if( uri.equals(path) ){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Gets account locked path
	 * @return
	 */
	private String getAccountLockedPath(){
		return new StringBuilder(PortalUtil.getPathMain()).append(accountLockedUrl).toString();
	}
	
	/**
	 * Gets second factor authentication path
	 * @return
	 */
	private String get2FactorPath(){
		return new StringBuilder(PortalUtil.getPathMain()).append(secondFactorUrl).toString();
	}

}
