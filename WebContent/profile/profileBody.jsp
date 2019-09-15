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
	
%>
<%
	LoginDTO localLoginDTO =  (LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	String username = (String)request.getAttribute("username");
%>
<div class="row">
    <div class="col-md-12">
        <!-- BEGIN PROFILE SIDEBAR -->
        <div class="profile-sidebar">
            <!-- PORTLET MAIN -->
            <div class="portlet light profile-sidebar-portlet bordered">
                <!-- SIDEBAR USERPIC -->
                <div class="profile-userpic">
                    <img src="<%=request.getContextPath() %>/ClientProfileInfo.do?type=showPicture" class="img-responsive" alt=""></div>
                <!-- END SIDEBAR USERPIC -->
                <!-- SIDEBAR USER TITLE -->
                <div class="profile-usertitle">
                    <div class="profile-usertitle-name"><%=username %>  </div>
                    <div class="profile-usertitle-job">   </div>
                </div>
                <!-- END SIDEBAR USER TITLE -->
                <!-- SIDEBAR MENU -->
            </div>
            <!-- END PORTLET MAIN -->
            <!-- PORTLET MAIN -->
            <!-- <div class="portlet light bordered">
                STAT
                <h5>Service taken</h5><hr>
                <div class="row list-separated profile-stat">
                    <div class="col-md-4 col-sm-4 col-xs-6">
                        <div class="uppercase profile-stat-title"> 37 </div>
                        <div class="uppercase profile-stat-text"> Domain </div>
                    </div>
                    <div class="col-md-4 col-sm-4 col-xs-6">
                        <div class="uppercase profile-stat-title"> 51 </div>
                        <div class="uppercase profile-stat-text"> VPN </div>
                    </div>
                      <div class="col-md-4 col-sm-4 col-xs-6">
                        <div class="uppercase profile-stat-title"> 51 </div>
                        <div class="uppercase profile-stat-text"> Other </div>
                    </div>
                </div>
                END STAT
            </div> -->
            <!-- END PORTLET MAIN -->
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
                                <span class="caption-subject font-blue-madison bold uppercase">Profile Account</span>
                            </div>
                            <ul class="nav nav-tabs">
                                <li class="active">
                                    <a href="#tab_1_3" data-toggle="tab">Change Password</a>
                                </li>
                                <%if(localLoginDTO.getIsAdmin() || localLoginDTO.getProfilePicturePath().equals(FileTypeConstants.DEFAULT_PROFILE_PIC)) {%>
                                <li class="">
                                    <a href="#tab_1_2" data-toggle="tab" aria-expanded="true">Change Picture</a>
                                </li>
                                <%} %>
                            </ul>
                        </div>
                        <div class="portlet-body">
                            <div class="tab-content">
                                <!-- CHANGE PASSWORD TAB -->
                                <div class="tab-pane active" id="tab_1_3">
                                   	<div class="note note-warning custom-alert">
										<p>Password length should be minimum 4 with a digit and letter</p>
									</div>
                                    <html:form action="ClientProfileInfo">
                                        <div class="form-group">
                                            <label class="control-label">Current Password  <span class="required"> * </span></label>
                                            	<input name="currentPassword" type="password" class="form-control" autocomplete="off" /> 
                                            	<input name="username" id="username" type="hidden" class="form-control" value="<%=username %>" /> 
                                            </div>
                                        <div class="form-group">
                                            <label class="control-label">New Password  <span class="required"> * </span> </label>
                                            <input name="password" type="password" id="password" class="form-control" /> 
                                            	<div style="margin-top: 5px;" class="pwstrength_viewport_progress"></div>
                                            </div>
                                        <div class="form-group">
                                            <label class="control-label">Confirm  New Password  <span class="required"> * </span></label>
                                            <input name="rePassword" type="password" class="form-control" /> </div>
                                        <div class="margin-top-10">
                                        	<input name="type" value="updatePassword" type="hidden">
                                        	<input type="reset" class="btn btn-cancel-btcl" value="Reset"> 
                                            <input type="submit" class="btn btn-submit-btcl" value=" Change Password" />
                                        </div>
                                       
                                    </html:form>
                                </div>
                                <!-- END CHANGE PASSWORD TAB -->
                                <%if (localLoginDTO.getIsAdmin() || localLoginDTO.getProfilePicturePath().equals(FileTypeConstants.DEFAULT_PROFILE_PIC)) {%>
                                 <div class="tab-pane " id="tab_1_2">
                                 	<p> Please attach a recently captured photo</p>
	                                  <html:form action="ClientProfileInfo" styleId="fileupload"  enctype="multipart/form-data" method="POST">
                                   		<div class="row">
											<div class="col-md-12"><hr></div>
											<div class="col-md-12">
											 	<div  id="nid" class="col-md-3" style="padding: 0px;">
													<!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
													<span class="btn btn-warning-btcl  fileinput-button">
														<i class="fa fa-upload"></i>
														<span> Add Profile Picture </span> 
														<input class="jFile" type="file" name="<%=FileTypeConstants.CLIENT.CLIENT_PROFILE_PICTURE %>" >
													</span>
												</div>
												
												<div class="col-md-9">
													 <!-- The global file processing state -->
													 <span class="fileupload-process"></span>
											   		  <!-- The global progress state -->
											          <div class="col-lg-12 fileupload-progress fade">
											              <!-- The global progress bar -->
											              <div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100">
											                  <div class="progress-bar progress-bar-success" style="width:0%;"></div>
											              </div>
											              <!-- The extended global progress state -->
											              <div class="progress-extended">&nbsp;</div>
											          </div>
												</div>
												<!-- The table listing the files available for upload/download -->
												<table role="presentation" class="table table-striped margin-top-10">
													<tbody class="files"></tbody>
												</table>
											</div>
									 		<jsp:include page="../common/ajaxfileUploadTemplate.jsp" />
										</div>
                                     	<div class="margin-top-10">
                                     		<input name="type" value="updatePicture" type="hidden">
                                            <input type="submit" class="btn btn-submit-btcl" value=" Change Picture" />
                                     	</div>
                                 	</html:form>
                             	</div>
                             	<%} %>
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
})
</script>                 
                  
                
                  