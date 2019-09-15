<%@page import="java.util.Map"%>
<%@page import="client.RegistrantTypeConstants"%>
<%@page import="domain.constants.DomainRegistrantsConstants"%>
<%@page import="domain.DomainContants"%>
<%@page import="util.MultipleTypeUtils"%>
<%@page import="vpn.client.ClientForm"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="util.SOP"%>
<%@page import="java.util.Set"%>
<%@page import="vpn.constants.VpnRegistrantsConstants"%>
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
	Logger logger =Logger.getLogger(getClass());
	String actionName = "/DomainPackageType";
	DomainPackageTypeDTO dto = (DomainPackageTypeDTO)request.getAttribute("form");
	Integer registrantCategory = (int)clientForm.getClientDetailsDTO().getRegistrantCategory();
	Set<Integer> companyTypes =MultipleTypeUtils.getArrayToUniqueSet(dto.getRegTypes());
	Set<Integer> clientTypes =MultipleTypeUtils.getArrayToUniqueSet(dto.getClientTypes());
	Set<Integer> secondLevelDomains =MultipleTypeUtils.getArrayToUniqueSet(dto.getSecondLevelDomains());
%>
<!-- BEGIN PAGE LEVEL PLUGINS -->
<!-- END PAGE LEVEL PLUGINS -->
<div class="portlet box portlet-btcl ">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-info"></i> Edit Domain Package Type Info
        </div>
    </div>
    <div class="portlet-body form">
    <html:form  styleId="fileupload" styleClass="form-horizontal" action="<%=actionName%>" enctype="multipart/form-data" method="POST">
            <div class="form-body">
               	<input type="hidden" name="mode" value="editPackageType"/>
               	<input type="hidden" name="ID" value="${form.ID}" />
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
                       <input type="checkbox" name="documentRequired" value="1" <% if(dto.isDocumentRequired()){ %>  checked <%} %> />
                    </div>
                </div>
                <div class="form-group">
					<label class="control-label col-md-3 text-center">Client Type</label>
					<div class="col-md-6">
					 <%
                       HashMap<Integer, String> clientTypeMap =ClientForm.CLIENT_TYPE_STR;
						for (Integer availableItem : clientTypeMap.keySet()) {
						%>
							<div class="col-md-5 col-sm-12 col-xs-12 lr-no-padding">
								<%if(clientTypes.contains(availableItem)){ %>
									<input type="checkbox" name="clientTypes" value="<%=availableItem%>" checked />
									<label class=""><%=clientTypeMap.get(availableItem)%></label>
								<%}else{ %>
									<input type="checkbox" name="clientTypes" value="<%=availableItem%>" />
									<label class=""><%=clientTypeMap.get(availableItem)%></label>
								<%} %>
							</div>
					<% } %>
					</div>
				</div>
                  <div class="form-group">
                    <label class="col-md-3 control-label">Company Type</label>
                   	<div class="col-md-6">
                        <%
                        Map<Integer, String> companyTypeMap = RegistrantTypeConstants.RegistrantTypeName;
							for (Integer availableItem : companyTypeMap.keySet()) {
							%>
								<div class="col-md-5 col-sm-12 col-xs-12 lr-no-padding">
									<%if(companyTypes.contains(availableItem)){ %>
										<input type="checkbox" name="regTypes" value="<%=availableItem%>" checked />
										<label class=""><%=companyTypeMap.get(availableItem)%></label>
									<%}else{ %>
										<input type="checkbox" name="regTypes" value="<%=availableItem%>" />
										<label class=""><%=companyTypeMap.get(availableItem)%></label>
									<%} %>
								</div>
						<% } %>
                   	</div>
                </div>
               <div class="form-group">
               		<label class="control-label col-md-3">Registrant Category </label>
                       	<div class="col-md-6">
                         <%
							HashMap<Integer, String> regMap = DomainRegistrantsConstants.domainRegTypeMap;
								for (Integer availableItem : regMap.keySet()) {
								%>
									<div class="col-md-5 col-sm-12 col-xs-12 lr-no-padding">
										<%if(registrantCategory == availableItem){ %>
											<input type="checkbox" name="regiCategories" value="<%=availableItem%>" checked />
											<label class=""><%=regMap.get(availableItem)%></label>
										<%}else{ %>
											<input type="checkbox" name="regiCategories" value="<%=availableItem%>" />
											<label class=""><%=regMap.get(availableItem)%></label>
										<%} %>
									</div>
							<% } %>
                   		</div>
                </div>
                 <div class="form-group">
               		<label class="control-label col-md-3">Second Level Domains </label>
                       	<div class="col-md-6">
                         <%
                         	HashMap<Integer, String> secondLevelMap=DomainContants.secondLevelDomainsMap;
								for (Integer availableItem : secondLevelMap.keySet()) {
								%>
									<div class="col-md-5 col-sm-12 col-xs-12 lr-no-padding">
										<%if(secondLevelDomains.contains(availableItem)){ %>
											<input type="checkbox" name="secondLevelDomains" value="<%=availableItem%>" checked />
											<label class="">.<%=secondLevelMap.get(availableItem)%></label>
										<%}else{ %>
											<input type="checkbox" name="secondLevelDomains" value="<%=availableItem%>" />
											<label class="">.<%=secondLevelMap.get(availableItem)%></label>
										<%} %>
									</div>
							<% } %>
                   		</div>
                </div>
            </div>
            <div class="form-actions center">
                <div class="row">
                     <div class="col-md-offset-4 col-md-8">
                      		<a class="btn btn-cancel-btcl" type="button" href="<%=request.getContextPath()%>/SearchPackageType.do">Cancel</a>
                     	 	<button type="reset" class="btn btn-reset-btcl">Reset</button>
         					<button type="submit" class="btn btn-submit-btcl">Save</button>
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
	
	if($('select[name=tld]').val()==domain_bangla){
		resetCheckbox();
	}
});
</script>