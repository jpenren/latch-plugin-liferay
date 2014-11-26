<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<portlet:defineObjects />
<portlet:actionURL var="doPairUrl" name="doPair"></portlet:actionURL>

<aui:form action="<%= doPairUrl %>" name="<portlet:namespace/>fm" cssClass="form-inline pairing-form">
	<div class="pairing-form__content">
		<input class="field pairing-form__content__token" name="<portlet:namespace/>token-value" maxlength="6" />
	    <aui:button type="submit" value="latch.button.pair"></aui:button>
	</div>
</aui:form>