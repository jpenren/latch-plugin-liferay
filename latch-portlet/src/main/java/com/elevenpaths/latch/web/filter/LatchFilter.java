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

	public void init(FilterConfig config) throws ServletException {
		secondFactorUrl = config.getInitParameter("latch-second-factor-url");
		accountLockedUrl = config.getInitParameter("latch-account-locked-url");
	}
	
	public void doFilter(ServletRequest sRequest, ServletResponse sResponse, FilterChain chain) throws IOException, ServletException {
		boolean doChain = true;
		try {
			HttpServletRequest request = (HttpServletRequest) sRequest;
			HttpServletResponse response = (HttpServletResponse) sResponse;
			HttpSession session = request.getSession(false);
			
			if( session!=null && LatchConfig.isConfigured() ){
				//Checks if account is locked
				boolean accountLocked = session.getAttribute(LatchKeys.SESSION_ACCOUNT_LOCKED)!=null;
				if( accountLocked ){
					response.sendRedirect( getAccountLockedPath() );
					doChain = false;
				}
				
				//Checks if has 2 factor
				boolean hasToken = session.getAttribute(LatchKeys.SESSION_ACCOUNT_2_FACTOR)!=null;
				if( hasToken ){
					response.sendRedirect( get2FactorPath() );
					doChain = false;
				}
			}
		} catch (Exception e) {
			log.error("Error executing Latch filter: ", e);
		}
		
		if(doChain){
			chain.doFilter(sRequest, sResponse);
		}
	}
	
	public void destroy() {
		log.info("Latch Filter is down");
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
