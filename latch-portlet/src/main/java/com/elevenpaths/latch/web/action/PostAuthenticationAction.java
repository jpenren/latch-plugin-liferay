package com.elevenpaths.latch.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elevenpaths.latch.LatchConfig;
import com.elevenpaths.latch.LatchKeys;
import com.elevenpaths.latch.auth.LatchAuthenticator;
import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;

public class PostAuthenticationAction extends Action{
	private final Logger log = LoggerFactory.getLogger(PostAuthenticationAction.class);
	
	@Override
	public void run(HttpServletRequest request, HttpServletResponse response) throws ActionException {
		if(LatchConfig.isConfigured()){
			try {
				//Checks if user has second factor auth
				final User user = PortalUtil.getUser(request);
				final long userId = user.getUserId();
				final boolean has2Factor = LatchAuthenticator.has2Factor(userId);
				if(has2Factor){
					final HttpSession session = request.getSession();
					final String token = LatchAuthenticator.get2Factor(userId);
					session.setAttribute(LatchKeys.SESSION_ACCOUNT_2_FACTOR, token);
				}
			} catch (Exception e) {
				log.error("Error authenticating with Latch: ", e);
			} 
		}
	}

}
