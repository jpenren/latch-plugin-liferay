package com.elevenpaths.latch.web.portlet;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elevenpaths.latch.api.exception.CommunicationException;
import com.elevenpaths.latch.auth.LatchService;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class LatchPairingPortlet
 */
public class PairingPortlet extends MVCPortlet {
	private final Logger log = LoggerFactory.getLogger(PairingPortlet.class);
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
		} catch (CommunicationException e) {
			//TODO add info about error
			SessionMessages.add(request, "error");
		} catch (Exception e) {
			SessionMessages.add(request, "error");
		} 
	}
	
	public void doUnPair(ActionRequest request, ActionResponse response){
		try {
			long userId = PortalUtil.getUser(request).getUserId();
			LatchService.doUnPair(userId);
		} catch (Exception e) {
			SessionMessages.add(request, "error");
		} 
	}

}
