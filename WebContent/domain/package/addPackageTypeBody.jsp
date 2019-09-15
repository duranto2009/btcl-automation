<%@page import="common.ModuleConstants"%>
<%@page import="domain.constants.DomainRegistrantsConstants"%>
<%@page import="domain.DomainContants"%>
<%@page import="vpn.client.ClientForm"%>
<%@page import="vpn.constants.VpnRegistrantsConstants"%>
<%@page import="common.RegistrantTypeConstants"%>
<%@page import="java.util.HashMap"%>
<%@page import="domain.DomainPackageTypeDTO"%>
<%@page import="java.util.List"%>
<%@page import="domain.DomainPackageDetails"%>
<%@page import="domain.DomainService"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	String actionName = "/DomainPackageType";
%>
<!-- BEGIN PAGE LEVEL PLUGINS -->
<!-- END PAGE LEVEL PLUGINS -->
<div class="portlet box portlet-btcl ">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-info"></i> Add Domain Package Type Info
        </div>
    </div>
    <div class="portlet-body form">
    <html:form  styleId="fileupload" styleClass="form-horizontal" action="<%=actionName%>" enctype="multipart/form-data" method="POST">
            <div class="form-body">
               	<input type="hidden" name="mode" value="addPackageType"/>
                <div class="form-group">
                    <label class="col-md-3 control-label">Package Type Name</label>
                    <div class="col-md-6">
                        <html:text property="packageName" styleClass="form-control"></html:text>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-3 control-label">TLD</label>
                    <div class="col-md-6">
                         <html:select styleClass="form-control" property="tld">
                           		<html:option value="1">.bd</html:option>
                           		<html:option value="2">.বাংলা</html:option>
                          </html:select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-3 control-label">Document Required</label>
                    <div class="col-md-6">
                       <input type="checkbox" name="documentRequired" value="true" /> <i>if yes, check this</i>
                    </div>
                </div>
                
                <div class="form-group">
					<label class="control-label col-md-3 text-center">Client Type</label>
					<div class="col-md-6">
						<%
                      	HashMap<Integer, String> clientTypeMap =ClientForm.CLIENT_TYPE_STR;
						for (Integer clientType : clientTypeMap.keySet()) {
								String clientTypeStr=clientType+"";
							%>
							<div class="col-md-5 col-sm-12 col-xs-12 lr-no-padding">
								<html:checkbox property="clientTypes"  value="<%=clientTypeStr%>" />
								<label class=""><%=clientTypeMap.get(clientType)%></label>
							</div>
						<% } %>
					</div>
				</div>
                <div class="form-group">
                    <label class="col-md-3 control-label">Company Type</label>
                   	<div class="col-md-6">
                		<%
							HashMap<Integer, String> companyMap = RegistrantTypeConstants.REGISTRANT_TYPE.get(ModuleConstants.Module_ID_DOMAIN);
							for (Integer key : companyMap.keySet()) {
								String keyStr=key+"";
						%>
							<div class="col-md-5 col-sm-12 col-xs-12 lr-no-padding">
								<html:checkbox property="regTypes" value="<%=keyStr%>" />
								<label class=""><%=companyMap.get(key) %></label>
							</div>
						<% } %>
                     </div>
                </div>
               <div class="form-group">
               		<label class="control-label col-md-3">Registrant Category</label>
                       	<div class="col-md-6">
                          <%
							HashMap<Integer, String> regMap =DomainRegistrantsConstants.domainRegTypeMap;
							for (Integer reg : regMap.keySet()) {
								String regStr=reg+"";
							%>
							<div class="col-md-5 col-sm-12 col-xs-12 lr-no-padding">
								<html:checkbox property="regiCategories" value="<%=regStr%>" />
								<label class=""><%=regMap.get(reg)%></label>
							</div>
						<% } %>
						<br>
                   	</div>
                </div>
                 <div class="form-group">
               		<label class="control-label col-md-3">Second Level Domains</label>
                       	<div class="col-md-6">
                          <%
							HashMap<Integer, String> secondLevelMap=DomainContants.secondLevelDomainsMap;
							for (Integer key : secondLevelMap.keySet()) {
								String idStr=key+"";
							%>
							<div class="col-md-5 col-sm-12 col-xs-12 lr-no-padding">
								<html:checkbox property="secondLevelDomains" value="<%=idStr%>" title="only for .bd domain"/>
								<label class="">.<%=secondLevelMap.get(key)%></label>
							</div>
						<% } %>
						<br>
                   	</div>
                </div>
            </div>
            <div class="form-actions center">
                <div class="row">
                     <div class="col-md-offset-4 col-md-8">
                      		<a class="btn btn-cancel-btcl" type="button" href="<%=request.getContextPath()%>/SearchPackageType.do">Cancel</a>
                     	 	<button type="reset" class="btn btn-reset-btcl">Reset</button>
         					<button type="submit" class="btn btn-submit-btcl">Submit</button>
                     </div>
                 </div>
            </div>
        </html:form>
    </div>
</div>
<!-- END SAMPLE FORM PORTLET-->
<script type="text/javascript">
$(document).ready(function(){
	var domain_bd=1;
	var domain_bangla=2;
	$('select[name=tld]').change(function(){
		var val=$(this).val();
		if(val==domain_bangla){
			$('input[name=secondLevelDomains]').prop('disabled',true);
			resetCheckbox();
		}else{
			$('input[name=secondLevelDomains]').prop('disabled',false);
			checkCheckbox();
		}
		console.info(val);		
	});
	function resetCheckbox(){
		$('input[name=secondLevelDomains]').attr('checked', false);
		$('input[name=secondLevelDomains]').closest('span').removeClass('checked');
		$('input[name=secondLevelDomains]').closest('span').removeClass('disabled');
	}
	function checkCheckbox(){
		$('input[name=secondLevelDomains]').attr('checked', true);
		$('input[name=secondLevelDomains]').closest('span').addClass('checked');
	}
	checkCheckbox();
});
</script>
