<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<portlet:defineObjects />
<portlet:actionURL var="doPairUrl" name="doPair"></portlet:actionURL>

<aui:form action="<%= doPairUrl %>" name="<portlet:namespace/>fm" cssClass="form-inline pairing-form">
	<p><liferay-ui:message key="latch.pairing.info" /></p>
	<div class="row pairing-form__content">
		<div class="span6 text-center">
			<img src="<%= request.getContextPath() %>/img/LatchCode.png" align="middle" />
		</div>
		<div class="span6 pairing-form__content_fields">
			<div>
				<input class="field pairing-form__content__fields__token" name="<portlet:namespace/>token-value" maxlength="6" placeholder="<liferay-ui:message key="latch.pairing.placeholder" />" />
	    		<aui:button type="submit" cssClass="pairing-form__content__fields__send" value="latch.button.pair"></aui:button>
			</div>
	    </div>
	</div>
</aui:form>