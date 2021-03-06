package com.elevenpaths.latch.web.portlet;

import static com.elevenpaths.latch.LatchKeys.CSRF_TOKEN;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.elevenpaths.latch.auth.LatchService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class LatchPairingPortlet
 */
public class PairingPortlet extends MVCPortlet {
	private static Log log = LogFactoryUtil.getLog(PairingPortlet.class);
	private String pairTemplate;
	private String unPairTemplate;
	private String errorTemplate;
	private String guestTemplate;
	
	public void init() throws PortletException {
		super.init();
		pairTemplate = getInitParameter("pair-template");
		unPairTemplate = getInitParameter("unpair-template");
		errorTemplate = getInitParameter("error-template");
		guestTemplate = getInitParameter("guest-template");
	}
	
	public void doView(RenderRequest request, RenderResponse response) throws IOException, PortletException {
		String view = guestTemplate;
		try {
			User user = PortalUtil.getUser(request);
			if(user!=null){
				boolean hasLatch = LatchService.hasLatch(user.getUserId());
				view = (hasLatch ? unPairTemplate : pairTemplate);
				
				request.getPortletSession().setAttribute(CSRF_TOKEN, String.valueOf(System.currentTimeMillis()));
			}
		} catch (Exception e) {
			log.error("Error loading Latch Portlet view: ", e);
			view = errorTemplate;
		}
		
		include(view, request, response);
	}
	
	public void doPair(ActionRequest request, ActionResponse response) {
		String token = ParamUtil.getString(request, "token-value");
		try {
			//User pairing to
			long userId = PortalUtil.getUser(request).getUserId();
			//Calling to Latch and retrieving response
			LatchService.doPair(userId, token);
			SessionMessages.add(request, "success");
		} catch (Exception e) {
			SessionErrors.add(request, "error");
		} 
	}
	
	public void doUnPair(ActionRequest request, ActionResponse response) throws PortletException{
		//Check csrf values from session and request match
		String sessionCsrf = request.getPortletSession().getAttribute(CSRF_TOKEN).toString();
		String requestCsrf = request.getParameter(CSRF_TOKEN);
		if( !sessionCsrf.equals(requestCsrf) ){
			throw new PortletException("CSRF values not match");
		}
		
		try {
			long userId = PortalUtil.getUser(request).getUserId();
			LatchService.doUnPair(userId);
			SessionMessages.add(request, "success");
		} catch (Exception e) {
			SessionMessages.add(request, "error");
		} 
	}

}
