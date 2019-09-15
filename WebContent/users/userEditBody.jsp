<%@page import="util.SOP"%>
<%@page import="user.form.UserForm"%>
<%@page import="inventory.InventoryConstants"%>
<%@page import="util.ServiceDAOFactory"%>
<%@page import="crm.service.CrmEmployeeService"%>
<%@page import="crm.*"%>
<%@page import="org.apache.commons.lang3.ArrayUtils"%>
<%@page import="java.util.*"%>
<%@page import="user.UserRepository"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="user.UserDTO"%>
<%@page import="common.CategoryConstants"%>
<%@page import="inventory.*"%>
<%@ page import="location.Zone" %>

<%String context = "../../.."  + request.getContextPath() + "/";%>

<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic"%>

<%-- <%@ page errorPage="failure.jsp"%> --%>
<style>
	.ul-box{
		width:65%;
		margin-left: 17%;
		margin-bottom: 1%;
		background-color: #eee;
		padding-top: 40px;
		padding-bottom: 30px;
	}
</style>
<%
Logger logger = Logger.getLogger(getClass());
String []ipList=(String[])request.getSession(true).getAttribute("LoginIPList");
//List<Zone>zoneList= (List<Zone>) request.getAttribute("zoneList");
//List<Zone>userZoneList= (List<Zone>)request.getAttribute("userZoneList");

ipList=ArrayUtils.nullToEmpty(ipList);

CrmEmployeeService crmEmployeeService = ServiceDAOFactory.getService(CrmEmployeeService.class);
List<CrmEmployeeDetails> crmEmployeeDetailsList = crmEmployeeService.getCrmEmployeeDetailsByUserID(Long.parseLong(request.getParameter("id")));
// List<CrmEmployeeDetails> crmEmployeeDetailsList = new ArrayList<CrmEmployeeDetails>();

boolean isCRMActive = ! crmEmployeeDetailsList.isEmpty();

int districtCategoryId = CategoryConstants.CATEGORY_ID_DISTRICT;
int upazilaCategoryId = CategoryConstants.CATEGORY_ID_UPAZILA;
int unionCategoryId = CategoryConstants.CATEGORY_ID_UNION;
UserForm userForm = (UserForm) request.getAttribute("userForm");
logger.debug(userForm);
Map<Integer, Map<Long, InventoryItem> > resultMap = crmEmployeeService.getMapOfInventoryItemToInventoryItemIDToCategoryTypeID();
InventoryItem districtInvItem = resultMap.get(districtCategoryId).get(userForm.getDistrictID());
String districtName = (districtInvItem == null ? "ALL": districtInvItem.getName());

InventoryItem upazilaInvItem = resultMap.get(upazilaCategoryId).get(userForm.getUpazilaID());
String upazilaName = (upazilaInvItem == null ? "ALL": upazilaInvItem.getName());

InventoryItem unionInvItem = resultMap.get(unionCategoryId).get(userForm.getUnionID());
String unionName = (unionInvItem == null ? "ALL": unionInvItem.getName());

%>
<%try{%>
<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-gift"></i>Edit User Form
		</div>

	</div>
	<div class="portlet-body form">
		<html:form styleClass="form-horizontal" action="/UpdateUser" method="POST" onsubmit="return validate();">
			<div class="form-body">
				<div class=form-group>
					<label class="col-sm-3 control-label">BTCL Personnel</label>
					<label class="col-sm-6 control-label" style="text-align: left">
						<html:checkbox styleClass="form-control" property="isBTCLPersonnel"></html:checkbox>
					</label>
				</div>
				<html:hidden property="userID"/> 
				<div class="form-group ">
					<label class="col-sm-3 control-label">User Name</label>

					<div class="col-sm-6 ">
						<html:text styleClass="form-control"  property="username" />
					</div>
				</div>
				<div class="form-group ">
					<label class="col-sm-3 control-label">Password</label>

					<div class="col-sm-6">
						<html:password styleClass="form-control" property="password" />
					</div>
				</div>
				<div class="form-group ">
					<label class="col-sm-3 control-label">Confirm
						Password</label>

					<div class="col-sm-6">
						<html:password styleClass="form-control" property="repassword" />
					</div>
				</div>

				<div id="zone-assign">
					<div class="form-group" >

						<div class="row">
						<label  class="col-sm-3 col-md-3 control-label">Zone Select</label>
						<div class="col-sm-6 col-md-6">
							<multiselect v-model="selectedZoneList" :options="zoneList"
										 track-by=id label=nameEng :allow-empty="true" :searchable=true :loading=this.isLoading
										 id=ajax :internal-search=false :options-limit="500" :limit="15" :multiple="true"
										 open-direction="bottom">
							</multiselect>
						</div>
						</div>
					</div>

					<div class="form-group">
						<div class="col-sm-3"></div>
						<div class="col-sm-6">
							<button @click="addZone()"
									class="btn btn-sm cmd btn-info"
									style="width: 89px;">Add
							</button>
						</div>
					</div>


					<div class="form-group ">
						<label class="col-sm-3 control-label">User Zones</label>
						<div class="col-sm-6">
							<%--<btcl-info v-for="zone in userZoneList" :left="true" :title ="zone.nameEng" :html="true"--%>
									   <%--text="<button class='btn btn-info' @click='removeZone(zone.id)'>Remove</button>">--%>

							<%--</btcl-info>--%>
							<div class='row' v-for="zone in userZoneList">
								<label class="control-label col-sm-6" style="text-align:left !important">{{zone.nameEng}}</label>
								<div class="col-sm-6">
									<button @click="removeZone(zone.id)"
											class="btn btn-block blue-ebonyclay-stripe"
											style="width: 89px;">Remove
									</button>
								</div>
								<br><hr/><br>
							</div>
						</div>
					</div>
				</div>
				<div class="form-group ">
					<label class="col-sm-3 control-label">Role </label>
					<div class="col-sm-6">
						<html:hidden property="roleID" styleClass="search input-group input-sm  form-control"	 /> 
						<html:text property="roleName" styleClass="search input-group input-sm  form-control"	/>
					</div>
				</div>

				<div class="form-group ">
					<label class="col-sm-3 control-label">Status </label>
					<div class="col-sm-8">
						<div class="radio-list">
							<div class="radio-inline">
								<label class=""> <html:radio  property="status" value="<%=Integer.toString(UserDTO.STATUS_ACTIVE)%>" ></html:radio>
									Active
								</label>
							</div>
							<div class="radio-inline">
								<label class=""> <html:radio  property="status" value="<%=Integer.toString(UserDTO.STATUS_BLOCKED)%>" ></html:radio>
									Blocked
								</label>
							</div>
							
						</div>

					</div>
				</div>

				<div class="form-group ">
					<label class="col-sm-3 control-label">Mail Address</label>

					<div class="col-sm-6">
						<html:text styleClass="form-control" styleId="mailAddress" property="mailAddress" ></html:text>
					</div>
				</div>

				<div class="form-group ">
					<label class="col-sm-3 control-label">Security Token </label>
					<div class="col-sm-6">
						<html:text styleClass="form-control" styleId="mailAddress" property="secToken" ></html:text>
					</div>

				</div>


				<div class="form-group">
					<label class="col-sm-3 control-label">Login IP</label>
					<div class="col-sm-6">
						<input type=text class="form-control" id="LoginIP">
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-3"></div>
					<div class="col-sm-6">
						<input type="button" onClick="addLoginIP();" value="Add" name="cmdAdd" class="btn btn-sm cmd btn-info" style="width: 89px;"> 
						<input type="button" onClick="removeLoginIP();" value="Remove" name="cmdAdd" class="btn btn-sm cmd btn-danger" style="width: 89px;">
					</div>
				</div>

				<div class="form-group ">
					<label class="col-sm-3 control-label">Login IPs</label>
					<div class="col-sm-6">
						<html:select property="loginIPs" style="width:100%;border:1px solid #c2cad8;padding: 6px 12px;" multiple="true">
							<%for(String ip: ipList){ 
								if("".equals(ip)){
									continue;
								}
							%>
								<html:option value="<%=ip %>"><%=ip %></html:option>
							<%} %>
						</html:select>
					</div>
				</div>


				<h4 class="col-sm-offset-2 col-sm-10">Additional Information</h4>
				<hr></hr>		
				<div id="additionalTab">	
					<ul class="nav nav-tabs col-sm-offset-2">
						<li class=" <%=isCRMActive?"":"active" %>"><a  href="#1" data-toggle="tab">General</a></li>
						<li class=" <%=isCRMActive?"active":"" %>"><a href="#2" data-toggle="tab">CRM</a></li>
					</ul>
					
					<div class="tab-content">
						<div class="tab-pane  <%=isCRMActive?"":"active" %>" id="1">
				        	<div class="form-group">
								<label class="col-sm-3 control-label">Full Name</label>
								<div class="col-sm-6">
									<html:text styleClass="form-control" styleId="fullName" property="fullName" ></html:text>
								</div>
							</div>
							<div class="form-group up-down-path">
								<label class="col-sm-3 control-label">District</label>
								<div class="col-sm-6">
									<input class="fe-hide form-control category-item district" name="feDistrictStr" placeholder="Type to select district..." value="" id="district" type="text"> 
									<input type="hidden" value="<%=districtCategoryId%>" name="districtCategoryId" class="category-id"> 
									<input name="districtID" value="" class="invitId" type="hidden">
								</div>
							</div>
							<div class="form-group up-down-path">
								<label class="col-sm-3 control-label">Upazila</label>
								<div class="col-sm-6">
									<input class="fe-hide form-control category-item upazila" name="feUpazilaStr" placeholder="Type to select upazila..." id="upazilla" value="" type="text"> 
									<input type="hidden" value="<%=upazilaCategoryId%>" name="upazilaCategoryId" class="category-id"> 
									<input name="upazilaID" value="" class="invitId" type="hidden">
								</div>
							</div>
							<div class="form-group up-down-path">
								<label class="col-sm-3 control-label">Union</label>
								<div class="col-sm-6">
									<input class="fe-hide form-control category-item union" id="union" name="feUnionStr" placeholder="Type to select union..." value="" type="text"> 
									<input type="hidden" value="<%=unionCategoryId%>" name="upazilaCategoryId" class="category-id"> 
									<input name="unionID" value="" class="invitId" type="hidden">
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-sm-3 control-label">Department</label>
								<div class="col-sm-6">
									<html:text styleClass="form-control" styleId="departmentName" property="departmentName" ></html:text>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label">Designation</label>
								<div class="col-sm-6">
									<html:text styleClass="form-control" styleId="designation" property="designation" ></html:text>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label">Phone No</label>
								<div class="col-sm-6">
									<html:text styleClass="form-control" styleId="phoneNo" property="phoneNo" ></html:text>
								</div>
							</div>
					
							<div class="form-group">
								<label class="col-sm-3 control-label">Additional Info</label>
								<div class="col-sm-6">
									<html:text styleClass="form-control" styleId="additionalInfo" property="additionalInfo" ></html:text>
								</div>
							</div>
						</div>
						<div class="tab-pane  <%=isCRMActive?"active":"" %>" id="2">
								
						<%
							for(CrmEmployeeDetails crmEmployeeDetails: crmEmployeeDetailsList){
						%>
							<div class="ul-box">
							<ul style="list-style:none">
								<li><strong>Employee Name</strong> <%=" : "%><%=crmEmployeeDetails.getEmployee().getName() %></li>
								<li><strong>Designation</strong><%=" : "%><%=crmEmployeeDetails.getDesignation().getName() %></li>
								<li><strong>Department</strong> <%=" : "%><%=crmEmployeeDetails.getDepartment().getDepartmentName() %></li>
								<li><strong>District </strong> <%=" : "%><%=crmEmployeeDetails.getDistrictName()%></li>
								<li><strong>Upazila</strong><%=" : "%><%=crmEmployeeDetails.getUpazilaName() %></li>
								<li><strong>Union</strong> <%=" : "%><%=crmEmployeeDetails.getUnionName()%></li>
							</ul>
							</div>
							<%} %>
							
				        </div>
					</div>
				</div>
								
							
			</div>
		
			<div class="form-actions text-center">
				<a class="btn btn-cancel-btcl" href="<%=request.getHeader("referer")%>">Cancel</a>
				<button class="btn btn-submit-btcl" type="submit">Submit</button>
			</div>
		</html:form>

	</div>
</div>
<%}catch(Exception ex){
	logger.debug("ex", ex);
}%>
<script  src="../scripts/util.js"></script>
<script  src="../users/user.js"></script>
<script>
	var userId =  <%= userForm.getUserID()%>
$(document).ready(function() {
	
	$('#district').val('<%=districtName%>');
	$('#upazilla').val('<%=upazilaName%>');
	$('#union').val('<%=unionName%>');
	
	var currentAutoComplete;
	var categoryObj;
	var parentItemObj;
	var currentInput;
	
	$(".category-item").each(function() {
		$(this).autocomplete({
		      source: function(request, response) {
		         currentAutoComplete = this.element;
		         categoryObj = currentAutoComplete.next(".category-id");
		         parentItemObj = $(currentAutoComplete).closest(".up-down-path").prev(".up-down-path").find(".category-item");
		         var map = {};
		         map['parentItemID'] = parentItemObj.next().next().val(); 
		         map['categoryID'] = categoryObj.val();
		         var url = context+'AutoInventoryItem.do?partialName=' + request.term;
		         callAjax(url, map, response, "GET")
		      },
		      minLength: 1,
		      select: function(e, ui) {
		         currentAutoComplete.attr('data-category-item-id', ui.item.ID);
		         currentAutoComplete.val(ui.item.name);
		         currentAutoComplete.next().next().val(ui.item.ID);
		         
		         toastr.success( ui.item.name + "  is selected");
		         currentAutoComplete.closest(".up-down-path").nextAll(".up-down-path").find(".category-item").val("");
		         currentAutoComplete.closest(".up-down-path").nextAll(".up-down-path").find(".category-item").removeAttr('data-category-item-id');
		         return false;
		      },
		   }).autocomplete("instance")._renderItem = function(ul, item) {
		      return $("<li>").append("<a>" + item.name + "</a>").appendTo(ul);
		   }
		});
 	$('#district, #upazilla, #union').change(function(){
 		var self = $(this);
 		if($.trim(self.val()).length == 0) {
 			self.closest('.up-down-path').find('.invitId').val('')
 			self.closest('.up-down-path').nextAll('.up-down-path').find('.invitId').val('');
 			self.closest('.up-down-path').nextAll('.up-down-path').find('.category-item').val('');
 			self.closest('.up-down-path').nextAll('.up-down-path').find('.category-item').removeAttr('data-category-item-id');
 		}
 		
 	});
})
</script>

<script>
	var vue = new Vue({
		el: "#zone-assign",
		data: {
			contextPath: context,
			zoneList:[],
			userZoneList:[],
			selectedZoneList:[],
			userId:userId,
		},

		methods: {
			addZone:function () {
				axios.post(context + "location/addZoneByUser.do", {
					userId: this.userId,
					zoneList : this.selectedZoneList,
				})
						.then(result => {
							if(result.data.responseCode == 1) {
								toastr.success('Zones has been successfully assigned to user', 'Success');
								window.location.href = context + "/GetUser.do?id" + this.userId;
							}else {
								toastr.error(result.data.msg, 'Failure');
							}
						})
						.catch(function (error) {
							console.log(error);
						});
			},
			removeZone:function (zoneId) {
				axios.post(context + "location/deleteZoneByUser.do", {
					userId: this.userId,
					zoneId : zoneId,
				})
						.then(result => {
							if(result.data.responseCode == 1) {
								toastr.success('Zones has been successfully removed from  user', 'Success');
								window.location.href = context + "/GetUser.do?id" + this.userId;
							}else {
								toastr.error(result.data.msg, 'Failure');
							}
						})
						.catch(function (error) {
							console.log(error);
						});
			},
			allZone:function () {
				Promise.all(
						[
							axios({ method: 'GET', 'url': context+ 'location/allzonesearch.do'})
									.then(result => {
										if(result.data.responseCode == 1) {
											if(result.data.payload.hasOwnProperty("members")){

												this.zoneList = result.data.payload.members;
											}
											else{
												this.zoneList = result.data.payload;
											}
										}else {
											toastr.error(result.data.msg, 'Failure');
										}
									}).catch( error => {
								console.log(error);
							}),

							axios({ method: 'GET', 'url': context+ 'location/zoneByUser.do?id=' + this.userId})
									.then(result => {
										if(result.data.responseCode == 1) {
											if(result.data.payload.hasOwnProperty("members")){

												this.userZoneList = result.data.payload.members;
											}
											else{
												this.userZoneList = result.data.payload;
											}
										}else {
											toastr.error(result.data.msg, 'Failure');
										}
									}).catch( error => {
								console.log(error);
							})

						]
				).then(()=> {
					for (let i=0; i<this.zoneList.length;i++){
						for (let j=0;j<this.userZoneList.length;j++){
							if(this.zoneList[i].id == this.userZoneList[j].id){
								this.zoneList.splice(i,1);
							}
						}
					}
				});


			},

		},
		mounted() {
			this.allZone();
		},
	});
</script>


