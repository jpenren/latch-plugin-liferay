package com.elevenpaths.latch.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.elevenpaths.latch.LatchKeys;
import com.liferay.portal.kernel.struts.BaseStrutsAction;
import com.liferay.portal.util.PortalUtil;

/**
 * Manage Second Authentication Factor page
 * @author jpenren
 */
public class LatchSecuredAreaAction extends BaseStrutsAction {
	private static final String LOGOUT_URL = PortalUtil.getPathMain()+"/portal/logout";
	
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		final HttpSession session = request.getSession();
		String token = (String) session.getAttribute(LatchKeys.SESSION_ACCOUNT_2_FACTOR);
		final String input = request.getParameter("--token-value");
		if(input!=null && input.equals(token)){
			session.removeAttribute(LatchKeys.SESSION_ACCOUNT_2_FACTOR);
			token=null;
		}
		
		if(token==null){
			//TODO configurable at portlet
			response.sendRedirect("/"+PortalUtil.getPathContext());
		}
		
		request.setAttribute("logoutUrl", LOGOUT_URL);
		
		return "/latch/twoFactor.jsp";
	}
}
