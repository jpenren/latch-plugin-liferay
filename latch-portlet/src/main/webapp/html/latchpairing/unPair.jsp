<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<portlet:defineObjects />
<portlet:actionURL var="doUnPairUrl" name="doUnPair"></portlet:actionURL>

<aui:form action="<%= doUnPairUrl %>" name="<portlet:namespace />fm">
	<p><liferay-ui:message key="latch.unpair.info" /></p>
    <aui:button-row>
        <aui:button type="submit" value="latch.button.unpair"></aui:button>
    </aui:button-row>
</aui:form>