package com.elevenpaths.latch.web.portlet;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.elevenpaths.latch.LatchConfig;
import com.elevenpaths.latch.LatchKeys;
import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.util.ParamUtil;

public class PairingPortletConfigurationAction extends DefaultConfigurationAction {
	
	public String render(PortletConfig portletConfig, RenderRequest request, RenderResponse response) throws Exception {
		request.setAttribute(LatchKeys.APP_ID, LatchConfig.getAppId());
		request.setAttribute(LatchKeys.SECRET_KEY, LatchConfig.getSecretKey());
		request.setAttribute(LatchKeys.FORCE_LOGIN_FAIL, LatchConfig.isForceLoginFail());
		
		return super.render(portletConfig, request, response);
	}
	
	public void processAction(PortletConfig portletConfig, ActionRequest request, ActionResponse response) throws Exception {
		// Read configuration form params
		String appId = ParamUtil.getString(request, "preferences--appId--");
		String secretKey = ParamUtil.getString(request,	"preferences--secretKey--");
		boolean forceLoginFail  = ParamUtil.getBoolean(request, "preferences--forceLoginFail--");

		// Configuration file based, clean access from authentication hook
		LatchConfig.setAppId(appId);
		LatchConfig.setSecretKey(secretKey);
		LatchConfig.setForceLoginFail(forceLoginFail);
		LatchConfig.store();
	}

}
