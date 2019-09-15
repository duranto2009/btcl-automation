<%@page import="common.EntityTypeConstant"%>
<%@page import="common.bill.BillDTO"%>
<%@ page language="java"%><%@ taglib uri="../../WEB-INF/struts-bean.tld"
	prefix="bean"%><%@ taglib uri="../../WEB-INF/struts-html.tld"
	prefix="html"%><%@ taglib uri="../../WEB-INF/struts-logic.tld"
	prefix="logic"%>
<%@ page
	import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*"%>
<%
	String requested = (String) session.getAttribute("domainAddress");
	/* if(orderTime == null)orderTime=format.format(System.currentTimeMillis()); */
	if (requested == null) {
		requested = "";
	}
%>
<style>
.btn:not(.btn-sm):not(.btn-lg) {
    line-height: 1.30;
}
</style>
<div class="form-group ">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Domain Name</label>
	<div class="col-md-8">
		<div class="input-group">
			<input name="domainAddress" id="domainName" class="form-control"
				autocomplete="off" type="text" value="<%=requested%>"
				
				title="Please use bangla keyboard and write a domain name without www.">
			<span class="input-group-btn">
				<button class="btn blue" data-toggle="popover"data-placement="bottom"  data-container="body"
				data-html="true"  type="button"><i class="fa fa-keyboard-o"></i></button>
			</span>
		</div>
	</div>
</div>
<div id="popover-content" class="hide" class="col-md-12 bangla-keyboard">
	<%@ include file="../../keyboard/amarBangla.jsp"%>
</div>
<script type="text/javascript">
$(document).ready(function() {
	 $("[data-toggle='popover']").popover({
		    html: true, 
		    trigger: 'click',
			content: function() {
		          return $('#popover-content').html();
		    }
		});
});
</script>