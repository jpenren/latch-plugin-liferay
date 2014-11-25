<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui" %>

<portlet:defineObjects />
<portlet:actionURL var="doUnPairUrl" name="doUnPair"></portlet:actionURL>

<aui:form action="<%= doUnPairUrl %>" name="<portlet:namespace />fm">
Unpair your account
        <aui:button-row>
            <aui:button type="submit" value="UnPair account"></aui:button>
        </aui:button-row>
</aui:form>