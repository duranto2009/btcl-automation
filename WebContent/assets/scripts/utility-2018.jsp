<%@page import="lli.connection.LLIConnectionConstants"%>
<script>
function goHome() {
	window.location.href = context + "Welcome.do"
}

var utilityConstants = {
		lli : {
			applicationType: {
				NEW_CONNECTION : parseInt('<%=LLIConnectionConstants.NEW_CONNECTION%>'),
				UPGRADE_BANDWIDTH : parseInt('<%=LLIConnectionConstants.UPGRADE_BANDWIDTH%>'),
				DOWNGRADE_BANDWIDTH : parseInt('<%=LLIConnectionConstants.DOWNGRADE_BANDWIDTH%>'),
				TEMPORARY_UPGRADE_BANDWIDTH : parseInt('<%=LLIConnectionConstants.TEMPORARY_UPGRADE_BANDWIDTH%>'),
				ADDITIONAL_PORT : parseInt('<%=LLIConnectionConstants.ADDITIONAL_PORT%>'),
				RELEASE_PORT : parseInt('<%=LLIConnectionConstants.RELEASE_PORT%>'),
				ADDITIONAL_LOCAL_LOOP : parseInt('<%=LLIConnectionConstants.ADDITIONAL_LOCAL_LOOP%>'),
				RELEASE_LOCAL_LOOP : parseInt('<%=LLIConnectionConstants.RELEASE_LOCAL_LOOP%>'),
				ADDITIONAL_IP : parseInt('<%=LLIConnectionConstants.ADDITIONAL_IP%>'),
				RELEASE_IP : parseInt('<%=LLIConnectionConstants.RELEASE_IP%>'),
				ADDITIONAL_CONNECTION_ADDRESS : parseInt('<%=LLIConnectionConstants.ADDITIONAL_CONNECTION_ADDRESS%>'),
				SHIFT_CONNECTION_ADDRESS : parseInt('<%=LLIConnectionConstants.SHIFT_CONNECTION_ADDRESS%>'),
				RELEASE_CONNECTION_ADDRESS : parseInt('<%=LLIConnectionConstants.RELEASE_CONNECTION_ADDRESS%>'),
				SHIFT_POP : parseInt('<%=LLIConnectionConstants.SHIFT_POP%>'),
				NEW_LONG_TERM : parseInt('<%=LLIConnectionConstants.NEW_LONG_TERM%>'),
				BREAK_LONG_TERM : parseInt('<%=LLIConnectionConstants.BREAK_LONG_TERM%>'),
				SHIFT_BANDWIDTH : parseInt('<%=LLIConnectionConstants.SHIFT_BANDWIDTH%>'),
				CHANGE_OWNERSHIP : parseInt('<%=LLIConnectionConstants.CHANGE_OWNERSHIP%>'),
				RECONNECT : parseInt('<%=LLIConnectionConstants.RECONNECT%>'),
				CHANGE_BILLING_ADDRESS : parseInt('<%=LLIConnectionConstants.CHANGE_BILLING_ADDRESS%>'),
				CLOSE_CONNECTION : parseInt('<%=LLIConnectionConstants.CLOSE_CONNECTION%>')
			},
			status: {
				STATUS_APPLIED : parseInt('<%=LLIConnectionConstants.STATUS_APPLIED%>'),
				STATUS_VERIFIED : parseInt('<%=LLIConnectionConstants.STATUS_VERIFIED%>'),
				STATUS_PROCESSED : parseInt('<%=LLIConnectionConstants.STATUS_PROCESSED%>'),
				STATUS_FINALIZED : parseInt('<%=LLIConnectionConstants.STATUS_FINALIZED%>'),
				STATUS_DEMAND_NOTE_GENERATED : parseInt('<%=LLIConnectionConstants.STATUS_DEMAND_NOTE_GENERATED%>'),
				STATUS_PAYMENT_CLEARED : parseInt('<%=LLIConnectionConstants.STATUS_PAYMENT_CLEARED%>'),
				STATUS_COMPLETED : parseInt('<%=LLIConnectionConstants.STATUS_COMPLETED%>'),
				STATUS_REQUESTED_FOR_CORRECTION : parseInt('<%=LLIConnectionConstants.STATUS_REQUESTED_FOR_CORRECTION%>'),
				STATUS_REJECTED : parseInt('<%=LLIConnectionConstants.STATUS_REJECTED%>')
			}
		}
}
</script>
<%@ include file="/common/globalConfigConstants.jsp"%>
<%--style for radio button --- added by forhad!!--%>
<style>.form-horizontal .checkbox, .form-horizontal .checkbox-inline, .form-horizontal .radio, .form-horizontal .radio-inline{padding-top:2px!important;}div.checker, div.radio {margin-right: 0;margin-left: 3px;margin-right: 5px!important;}</style>
