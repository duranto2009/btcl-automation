<%@page import="common.CategoryConstants"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="user.UserDTO"%>
<%String context = "../../.."  + request.getContextPath() + "/";%>

<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic"%>

<%-- <%@ page errorPage="failure.jsp"%> --%>


<%
    Logger logger = Logger.getLogger(this.getClass());
	int districtCategoryId = CategoryConstants.CATEGORY_ID_DISTRICT;
	int upazilaCategoryId = CategoryConstants.CATEGORY_ID_UPAZILA;
	int unionCategoryId = CategoryConstants.CATEGORY_ID_UNION;
%>


<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-gift"></i>Add User Form
		</div>

	</div>
	<div class="portlet-body form">
		<html:form styleClass="form-horizontal" action="/AddUser"	method="POST" onsubmit="selectAllOptions();return validate();">
			<div class="form-body">
				<div class="form-group">
					<label class="col-sm-3 control-label">BTCL Personnel</label>
					<label class="col-sm-6 control-label" style="text-align: left">
						<input  type="checkbox" name="isBTCLPersonnel" checked>
					</label>
				</div>
				<div class="form-group ">
					<label class="col-sm-3 control-label">User Name</label>

					<div class="col-sm-6 ">
						<input type="text" class="form-control" id="username"
							name="username" placeholder="Give user a name" required>
					</div>
					<label class="col-sm-2" id='availability' 
				style="padding-top: 7px;margin-top: 1px; 
				margin-bottom: 0px; text-align: left; font-weight: 400"></label>
				</div>
				<div class="form-group ">
					<label for="password" class="col-sm-3 control-label">Password</label>

					<div class="col-sm-6">
						<input type="password" class="form-control" id="password"
							name="password" placeholder="Give user a password" required>
					</div>
				</div>
				<div class="form-group ">
					<label for="userName" class="col-sm-3 control-label">Confirm
						Password</label>

					<div class="col-sm-6">
						<input type="password" class="form-control" id="repassword"
							name="repassword" placeholder="Repeat password" required>
					</div>
				</div>
				

				<div class="form-group ">
					<label class="col-sm-3 control-label">Role </label>
					<div class="col-sm-6">
						<input type="hidden" id="roleID" name="roleID"	class="search input-group input-sm  form-control"	autocomplete="off" /> 
						<input type="text" id="roleName" name="roleName" class="search input-group input-sm  form-control"	autocomplete="off" />
					</div>
				</div>

				<%--<div id="zone-assign">
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
								&lt;%&ndash;<btcl-info v-for="zone in userZoneList" :left="true" :title ="zone.nameEng" :html="true"&ndash;%&gt;
								&lt;%&ndash;text="<button class='btn btn-info' @click='removeZone(zone.id)'>Remove</button>">&ndash;%&gt;

								&lt;%&ndash;</btcl-info>&ndash;%&gt;
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
--%>
				<div class="form-group ">
					<label class="col-sm-3 control-label">Status </label>
					<div class="col-sm-8">
						<div class="radio-list">
							<div class="radio-inline" id="">
								<label class=""> <input type="radio" name="status"
									value="<%=Integer.toString(UserDTO.STATUS_ACTIVE)%>" checked>
									Active
								</label>
							</div>
							<div class="radio-inline" id="">
								<label class=""> <input type="radio" name="status"
									value="<%=Integer.toString(UserDTO.STATUS_BLOCKED)%>">
									Blocked
								</label>
							</div>
							
						</div>

					</div>
				</div>

				<div class="form-group ">
					<label class="col-sm-3 control-label">Mail Address</label>

					<div class="col-sm-6">
						<input type="text" class="form-control" id="mailAddress"
							name="mailAddress" placeholder="Give user a mailing address"
							>
					</div>
				</div>

				<div class="form-group ">
					<label class="col-sm-3 control-label">Security Token </label>
					<div class="col-sm-6">
						<input class="form-control" type="text" name="secToken"
							placeholder="security token">
					</div>

				</div>


				<div class="form-group">
					<label class="col-sm-3 control-label">Login IP</label>

					<div class="col-sm-6">
						<input type="text" class="form-control" id="LoginIP" name="LoginIP" placeholder="Give user a Login IP">
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-3"></div>
					<div class="col-sm-6">
						<input type="button" onClick="addLoginIP();" value="Add"
							name="cmdAdd" class="btn btn-sm cmd btn-info" style="width: 89px;"> <input
							type="button" onClick="removeLoginIP();" value="Remove"
							name="cmdAdd" class="btn btn-sm cmd btn-danger" style="width: 89px;">
					</div>
				</div>

				<div class="form-group ">
					<label class="col-sm-3 control-label">Login IPs</label>

					<div class="col-sm-6">
						<select style="width:100%;border:1px solid #c2cad8;padding: 6px 12px;"  multiple name="loginIPs" >
						
						</select>
					</div>
				</div>


				<div class="form-group">
					<div class="portlet light" style="margin-bottom: 0px;">
						<div class="col-sm-7 portlet-title">
							<div class="caption">
								 <span class="caption-subject  font-grey-gallery "> Additional Information</span>
							</div>
						</div>
						<div class="portlet-body form">
							<div class="form-body">
								<div class="form-group ">
									<label class="col-sm-3 control-label">Full Name</label>

									<div class="col-sm-6">
										<input type="text" class="form-control" id="fullName"
											name="fullName" placeholder="Give user's full name" >
									</div>
								</div>
								
								<div class="form-group up-down-path">
									<label class="col-sm-3 control-label" for="lnName">District</label>
									<div class="col-sm-6">
										<input class="fe-hide form-control category-item district"
											name="feDistrictStr" placeholder="Type to select district..." value="" id="district"
											type="text"> <input type="hidden"
											value="<%=districtCategoryId%>" name="districtCategoryId"
											class="category-id"> <input name="districtID" value="" class="invitId"
											type="hidden">
									</div>
								</div>
								<div class="form-group up-down-path">
									<label for="lnName" class="col-sm-3 control-label">Upazila</label>
									<div class="col-sm-6">
										<input class="fe-hide form-control category-item upazila"
											name="feUpazilaStr" placeholder="Type to select upazila..." id="upazilla"
											value="" type="text"> <input type="hidden"
											value="<%=upazilaCategoryId%>" name="upazilaCategoryId"
											class="category-id"> <input name="upazilaID" value="" class="invitId"
											type="hidden">
									</div>
								</div>
								<div class="form-group up-down-path">
									<label for="lnName" class="col-sm-3 control-label">Union</label>
									<div class="col-sm-6">
										<input class="fe-hide form-control category-item union" id="union"
											name="feUnionStr" placeholder="Type to select union..." value=""
											type="text"> <input type="hidden"
											value="<%=unionCategoryId%>" name="upazilaCategoryId"
											class="category-id"> <input name="unionID" value="" class="invitId"
											type="hidden">
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3 control-label">Department</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="deptName"
											name="departmentName" placeholder="Give user's department name" >
									</div>
								</div>
								
								<div class="form-group ">
									<label class="col-sm-3 control-label">Designation</label>

									<div class="col-sm-6">
										<input type="text" class="form-control" id="designation"
											name="designation" placeholder="Give user's designation"
											>
									</div>
								</div>

								
								<div class="form-group ">
									<label class="col-sm-3 control-label">Phone No</label>

									<div class="col-sm-6">
										<input type="text" class="form-control" id="phoneNo"
											name="phoneNo" placeholder="Give user's phone number"
											>
									</div>
								</div>

								<div class="form-group ">
									<label class="col-sm-3 control-label">Additional Info</label>

									<div class="col-sm-6">
										<input type="text" class="form-control" id="additionalInfo"
											name="additionalInfo" placeholder="Give user's phone number">
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="form-actions text-center">
				<button class="btn btn-reset-btcl" type="reset">Reset</button>
				<button class="btn btn-submit-btcl" type="submit">Submit</button>
			</div>
		</html:form>

	</div>
</div>

<script type="text/javascript" src="../scripts/util.js"></script>
<script type="text/javascript" src="../users/user.js"></script>
<script type="text/javascript">
$(document).ready(function() {
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
 	
	$("input[name='username']").keyup(function(){
		if($(this).val().length>0){
			data = {};
			data.username = $(this).val();
			callAjax(context + "Client/UsernameAvailability.do?username="+data.username, data, (data)=>{
				if(data.responseCode === 1) {
					if(data.payload == false){
						$('#availability').html('available');
						$('#availability').css('color', 'green');	
					}else if( data.payload == true) {
						$('#availability').html('unavailable');
						$('#availability').css('color', 'red');	
					}
				}else {
					toastr.error(data.msg);
				}
			},  "POST")
		}else {
			$('#availability').html('');
		}
	});
})
</script>

<%--
<script>
	var vue = new Vue({
		el: "#zone-assign",
		data: {
			contextPath: context,
			zoneList:[],
			userZoneList:[],
			selectedZoneList:[],
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
</script>--%>


