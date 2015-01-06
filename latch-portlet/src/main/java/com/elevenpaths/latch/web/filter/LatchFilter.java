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

import com.elevenpaths.latch.LatchConfig;
import com.elevenpaths.latch.LatchKeys;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.util.PortalUtil;

/**
 * Check latch session values to restrict access to portal  
 * @author jpenren
 */
public class LatchFilter implements Filter {
	private static Log log = LogFactoryUtil.getLog(LatchFilter.class);
	private String secondFactorUrl;
	private String accountLockedUrl;
	private final Set<String> internalUrls = new HashSet<String>();

	public void init(FilterConfig config) throws ServletException {
		secondFactorUrl = config.getInitParameter("latch-second-factor-url");
		accountLockedUrl = config.getInitParameter("latch-account-locked-url");
		internalUrls.add( get2FactorPath() );
		internalUrls.add( getAccountLockedPath() );
		internalUrls.add( PortalUtil.getPathMain()+"/portal/logout" );
	}
	
	public void doFilter(ServletRequest sRequest, ServletResponse sResponse, FilterChain chain) throws IOException, ServletException {
		if( sRequest instanceof HttpServletRequest ){
			try {
				HttpServletRequest request = (HttpServletRequest) sRequest;
				HttpServletResponse response = (HttpServletResponse) sResponse;
				HttpSession session = request.getSession(false);
				String uri = request.getRequestURI();
				
				if( session!=null && LatchConfig.isConfigured() && !isInternalUrl(uri) ){
					//Checks if account is locked
					boolean accountLocked = session.getAttribute(LatchKeys.SESSION_ACCOUNT_LOCKED)!=null;
					if( accountLocked ){
						response.sendRedirect( getAccountLockedPath() );
					}
					
					//Checks if has 2 factor
					boolean hasToken = session.getAttribute(LatchKeys.SESSION_ACCOUNT_2_FACTOR)!=null;
					if( hasToken ){
						response.sendRedirect( get2FactorPath() );
					}
				}
			} catch (Exception e) {
				log.error("Error executing Latch filter: ", e);
			}
		}
		
		chain.doFilter(sRequest, sResponse);
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
			if( path.equals(uri) ){
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
