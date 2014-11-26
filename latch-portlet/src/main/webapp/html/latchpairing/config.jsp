<%@page import="com.elevenpaths.latch.LatchKeys"%>
<%@page import="com.liferay.portal.kernel.util.StringPool"%>
<%@page import="com.liferay.portal.kernel.util.GetterUtil"%>
<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<portlet:defineObjects />
<liferay-portlet:actionURL portletConfiguration="true" var="configurationURL" />

<%  
	String appId = GetterUtil.getString(request.getAttribute(LatchKeys.APP_ID));
	String secretKey = GetterUtil.getString(request.getAttribute(LatchKeys.SECRET_KEY));
	boolean forceLoginFail = GetterUtil.getBoolean(request.getAttribute(LatchKeys.FORCE_LOGIN_FAIL));
%>

<p><liferay-ui:message key="latch.config.info" /></p>

<aui:form action="<%= configurationURL %>" method="post" name="fm">
	<aui:input name="preferences--appId--" cssClass="span5" label="latch.app.id" type="text" value="<%= appId %>" />
	<aui:input name="preferences--secretKey--" cssClass="span5" label="latch.app.secret" type="text" value="<%= secretKey %>" />
	<aui:input name="preferences--forceLoginFail--" type="checkbox" label="latch.app.force.login.fail" value="<%= forceLoginFail %>" />
	<small><liferay-ui:message key="latch.app.force.login.fail.info" /></small>
	
    <aui:button-row>
        <aui:button type="submit" />
    </aui:button-row>
</aui:form>
