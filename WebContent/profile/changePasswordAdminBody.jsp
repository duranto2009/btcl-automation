<%@page import="common.CommonActionStatusDTO"%>
<%@page import="common.ModuleConstants"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="org.eclipse.jetty.util.StringUtil"%>
<%@page import="common.ClientRepository"%>
<%@page import="user.UserRepository"%>
<%@page import="file.FileDAO"%>
<%@page import="file.FileTypeConstants"%>
<%@page import="vpn.clientContactDetails.ClientContactDetailsDTO"%>
<%@page import="vpn.client.ClientForm"%>
<%@page import="login.LoginDTO"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page contentType="text/html;charset=utf-8"%>

<%
	LoginDTO loginDTO = (LoginDTO)request.getSession().getAttribute( SessionConstants.USER_LOGIN );

	if( loginDTO.getUserID() < 0 ){
		
		CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
		
		commonActionStatusDTO.setErrorMessage( "You don't have permission to see this page" , false, request );
		response.sendRedirect("../");
		return;
	}
%>
<%
	LoginDTO localLoginDTO =  (LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	String clientUserName = (String)request.getAttribute("clientUserName");
	clientUserName=StringUtils.trimToEmpty(clientUserName);
	
	String clientID = (String)request.getAttribute("clientID");
	clientID=StringUtils.trimToEmpty(clientID);
	
	String clientProfilePicture=request.getContextPath()+ "/ClientProfileInfo.do?type=showPicture&clientID="+clientID;
%>
<style>
	.ui-autocomplete{
	
		z-index : 999999 !important;
	}
</style>

<div class="row">
    <div class="col-md-12">
        <!-- BEGIN PROFILE SIDEBAR -->
        <div class="profile-sidebar">
            <!-- PORTLET MAIN -->
            <div class="portlet light profile-sidebar-portlet bordered">
                <!-- SIDEBAR USERPIC -->
                <div class="profile-userpic">
                    <img id="profilePicturePath" src="<%=clientProfilePicture %>" class="img-responsive" alt="Client Profile Picture will be here"></div>
                <!-- END SIDEBAR USERPIC -->
                <!-- SIDEBAR USER TITLE -->
                <div class="profile-usertitle">
                    <div class="profile-usertitle-name" id="clientUserNameLabel"><%=clientUserName==""?"Client Name":clientUserName %>  </div>
                    <div class="profile-usertitle-job">   </div>
                </div>
            </div>
        </div>
        <!-- END BEGIN PROFILE SIDEBAR -->
        <!-- BEGIN PROFILE CONTENT -->
     
        <div class="profile-content">
            <div class="row">
                <div class="col-md-12">
                    <div class="portlet light bordered">
                        <div class="portlet-title tabbable-line">
                            <div class="caption caption-md">
                                <i class="icon-globe theme-font hide"></i>
                                <span class="caption-subject font-blue-madison bold uppercase">Client Profile</span>
                            </div>
                            <ul class="nav nav-tabs">
                                <li class="active">
                                    <a href="#tab_1_3" data-toggle="tab">Change Client Password</a>
                                </li>
                            </ul>
                        </div>
                        <div class="portlet-body">
                            <div class="tab-content">
                                <!-- CHANGE PASSWORD TAB -->
                                <div class="tab-pane active" id="tab_1_3">
                                   	<div class="note note-danger">
   										<p><b>Alert: </b>Client will not be able to login into our system with their old password. </p>
										<p>Password length should be minimum 4 with a digit and letter</p>
									</div>
									
                                    <html:form action="ClientProfileInfo"> 
                                    	<%-- <input type="hidden" name="moduleID" id="moduleID" value="<%=ModuleConstants.Module_ID_DOMAIN %>" /> --%>
                                    	<input type="hidden" name="moduleID" id="moduleID" value="-1" />
                                    	<div class="form-group">
                                            <label class="control-label">Client Name  <span class="required"> * </span></label>
                                           	<input name="clientUserName" id="clientUserName" type="text" class="form-control" autocomplete="off" value="<%=clientUserName%>"/> 
                                           	<input name="clientID" id="clientID" type="hidden" class="form-control" value="<%=clientID %>" /> 
                                        </div>
                                    
                                      <%--   <div class="form-group">
                                            <label class="control-label">Current Password  <span class="required"> * </span></label>
                                            <input type="text" style="display:none"> <!-- disables autocomplete -->
                                           	<input name="currentPassword" type="password" class="form-control" autocomplete="off" /> 
                                           	<input name="username" id="username" type="hidden" class="form-control" value="<%=username %>" /> 
                                        </div> --%>
                                        
                                        <div class="form-group">
                                            <label class="control-label">New Password  <span class="required"> * </span> </label>
                                            <input type="text" style="display:none"> <!-- disables autocomplete -->
                                            <input name="password" type="password" id="password" class="form-control" /> 
                                           	<div style="margin-top: 5px;" class="pwstrength_viewport_progress"></div>
                                        </div>
                                        
                                        <div class="form-group">
                                            <label class="control-label">Confirm  New Password  <span class="required"> * </span></label>
                                            <input type="text" style="display:none"> <!-- disables autocomplete -->
                                            <input name="rePassword" type="password" class="form-control" />
                                        </div>
                                        
                                        <div class="margin-top-10">
                                        	<input name="type" value="updatePasswordAdmin" type="hidden">
                                        	<input type="reset" class="btn btn-cancel-btcl" value="Reset"> 
                                            <input type="submit" class="btn btn-submit-btcl" value=" Change Password" />
                                        </div>
                                       
                                    </html:form>
                                </div>
                                <!-- END CHANGE PASSWORD TAB -->
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- END PROFILE CONTENT -->
    </div>
</div>

<script type="text/javascript">
$(document).ready(function() {
	 "use strict";
	var options = {};
	options.ui = {
	   container: "#pwd-container",
	   showVerdictsInsideProgressBar: true,
	   showStatus: true,
	   viewports: {
	       progress: ".pwstrength_viewport_progress"
	   },
	   progressBarExtraCssClasses: "progress-bar-striped active",
	   showPopover: true,
	showErrors: true,
	showProgressBar: true
	};
	options.rules = {
	activated: {
	   wordTwoCharacterClasses: true,
	   wordRepetitions: true
	}
	};
	options.common = {
	   /*debug: true,
	   onLoad: function () {
	       $('#messages').text('Start typing password');
	   }*/
	};
	$('#password').pwstrength(options);
	$("#fileupload").submit(function(){
	    $('.jFile').attr('disabled','disabled');
	    return true;
	});
	
	
	//$("#clientID").val(-1);
	$("#clientUserName").autocomplete({
     	source: function(request, response) {
		
	         var term = request.term;
	         var url = '../AutoComplete.do?need=client&moduleID='+$("#moduleID").val()+"&status=active";
	         var formData = {};
	         formData['name']=term;
	         ajax(url, formData, response, "GET", [$(this)]);
	    },
	    minLength: 1,
	    select: function(e, ui) {
	    	
	         $('#clientUserName').val(ui.item.data);
	         $('#clientID').val(ui.item.id);
	         
	         $("#clientUserNameLabel").html(ui.item.data);
	         $("#profilePicturePath").attr('src',context+'ClientProfileInfo.do?type=showPicture&clientID='+ui.item.id);
	         return false;
	    },
   	}).autocomplete("instance")._renderItem = function(ul, item) {
		return $("<li>").append("<a>" + item.data + "</a>").appendTo(ul);   
  	};
})
</script>                 
                  
                
                  