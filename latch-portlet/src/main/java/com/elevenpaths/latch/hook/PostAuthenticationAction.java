package com.elevenpaths.latch.hook;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elevenpaths.latch.LatchConfig;
import com.elevenpaths.latch.LatchKeys;
import com.elevenpaths.latch.api.exception.CommunicationException;
import com.elevenpaths.latch.api.response.StatusResponse;
import com.elevenpaths.latch.auth.LatchService;
import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.util.PortalUtil;

/**
 * Implements Latch account validation after user login
 * @author jpenren
 */
public class PostAuthenticationAction extends Action{
	private final Logger log = LoggerFactory.getLogger(PostAuthenticationAction.class);
	private static final String REDIRECT_URL = PortalUtil.getPathMain() +"/latch/security/";
	
	public void run(HttpServletRequest request, HttpServletResponse response) throws ActionException {
		if( LatchConfig.isConfigured() ){
			//Checks if user has second factor auth
			final HttpSession session = request.getSession();
			final long userId = PortalUtil.getUserId(request);
			try {
				if( LatchService.hasLatch(userId) ){
					//Get user status from Latch
					final StatusResponse status = LatchService.authenticate(userId);
					if( status.isOn() ){
						if( status.hasSecondFactor() ){
							session.setAttribute(LatchKeys.SESSION_ACCOUNT_2_FACTOR, status.getToken());
							response.sendRedirect(REDIRECT_URL);
						}
					}else{
						//Account locked
						denyAccess( session, response );
					}
				}
			} catch (CommunicationException e) {
				log.error("Communication error:", e);
				if( LatchConfig.isForceLoginFail() ){
					log.debug("Force loging fail active: denying access");
					try {
						denyAccess( session, response );
					} catch (IOException e1) {
						log.error("Error try to deny access:", e);
					}
				}
			} catch (Exception e) {
				log.error("Error in Latch plugin:", e);
			}
		}
	}
	
	/**
	 * Store session variable to lock account and redirect user 
	 * @param session
	 * @param response
	 * @throws IOException
	 */
	private void denyAccess(HttpSession session, HttpServletResponse response) throws IOException{
		session.setAttribute(LatchKeys.SESSION_ACCOUNT_LOCKED, System.currentTimeMillis());
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.sendRedirect(REDIRECT_URL);
	}

}
