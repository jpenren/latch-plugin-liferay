<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui" %>

<portlet:defineObjects />
<portlet:actionURL var="doPairUrl" name="doPair"></portlet:actionURL>

<aui:form action="<%= doPairUrl %>" name="<portlet:namespace />fm">
        <aui:fieldset>
            <aui:input name="token-value"></aui:input>
        </aui:fieldset>

        <aui:button-row>
            <aui:button type="submit" value="Pair my account"></aui:button>
        </aui:button-row>
</aui:form>