<%@page import="javax.portlet.PortletRequest"%>
<%@page import="com.elevenpaths.latch.LatchKeys"%>
<%@page import="com.liferay.portal.kernel.util.GetterUtil"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<portlet:defineObjects />
<portlet:actionURL var="doUnPairUrl" name="doUnPair"></portlet:actionURL>


<%  
	String token = GetterUtil.getString(renderRequest.getPortletSession().getAttribute(LatchKeys.CSRF_TOKEN));
%>

<aui:form action="<%= doUnPairUrl %>" name="<portlet:namespace />fm">
	<p><liferay-ui:message key="latch.unpair.info" /></p>
    <aui:button-row>
        <aui:button type="submit" value="latch.button.unpair"></aui:button>
    </aui:button-row>
    
    <input type="hidden" name="<portlet:namespace /><%= LatchKeys.CSRF_TOKEN %>" value="<%= token %>">
</aui:form>