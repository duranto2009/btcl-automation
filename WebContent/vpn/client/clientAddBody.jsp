<%@page import="common.RegistrantTypeConstants"%>
<%@page import="vpn.client.ClientForm"%>
<%@page import="common.ClientConstants"%>
<%@page import="util.Country2Phone"%>
<%@page import="java.util.Locale"%>
<%@page import="file.FileTypeConstants"%>
<%@page import="util.SOP"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="common.ModuleConstants"%>
<%@page import="java.util.HashMap"%>
<%@page import="vpn.constants.VpnRegistrantsConstants"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="sessionmanager.SessionConstants" %>
<%
    login.LoginDTO loginDTO = (login.LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
%>

<%
	int id = -1;
	String actionName = "/AddClient";
	String context = "../../.." + request.getContextPath() + "/";
	String message = (String) request.getAttribute("confirmation");
	SOP.print(message);
%>

<html:base />

<html:form styleId="fileupload" styleClass="form-horizontal" action="<%=actionName%>" enctype="multipart/form-data" method="POST">
	<input type="hidden" name="csrfPreventionSalt" value="${csrfPreventionSalt}"/>
	<%if(StringUtils.isNotBlank(message) || message !=null){ %>
	 <div class="note note-success">
         <h4 class="block">Success! </h4>
         <p> <%=message %>  </p>
     </div>
     <%}else{
    	 message="";
     } %>
	<div class="portlet box portlet-btcl">
		<div class="portlet-title">
			<div class="caption">
				<i class="fa fa-user"></i>VPN Registration Information
			</div>
		</div>
		<!-- /.box-header -->
		<div class="portlet-body form">
			<div class="form-body">
				<div class="alert alert-danger display-hide">
                   <button class="close" data-close="alert"></button> You have some form errors. Please check below. 
                </div>
               	<div class="alert alert-success display-hide">
                   <button class="close" data-close="alert"></button> Your form validation is successful! 
               	</div>
               	
               	<%@include file="../../includes/vpnclients/client-info-copy.jsp" %>
               	
               	<div class="row">
					<div class="form-group">
						<label class="control-label col-md-3 text-center"> <span  class="bold">Registration Type </span> </label>
						<div class="col-md-4">
							<div class="radio-list">
								 <label class="radio-inline"> 
								 	<input type="radio" name="accountType" value="2" autocomplete="off"  data-no-uniform="true" checked />Company
								</label>
							</div>
						</div>
					</div>
				</div>
				<hr>
            	<div id="regIndividualInfo">
                   	<h3 class="form-section">Person Info</h3>
                    <html:hidden property="clientDetailsDTO.registrantType" value="0"></html:hidden>
                    <html:hidden property="clientDetailsDTO.regiCat" value="0"></html:hidden>
					<div class="row">
                         <div class="col-md-6">
                             <div class="form-group">
                                 <label class="control-label col-md-3">First Name<span class="required" aria-required="true"> * </span></label>
                                 <div class="col-md-9">
                                      <html:text property="registrantContactDetails.registrantsName" styleClass="form-control individual regi"></html:text>
                                 </div>
                             </div>
                         </div>
                         <!--/span-->
                         <div class="col-md-6">
                             <div class="form-group ">
                                  <label class="control-label col-md-3">Last Name  </label>
                                 <div class="col-md-9">
                                       <html:text property="registrantContactDetails.registrantsLastName" styleClass="form-control individual regi"></html:text>
                                 </div>
                             </div>
                         </div>
                         <!--/span-->
                     </div>
                     <!--/row-->
                     <div class="row">
                         <div class="col-md-6">
                             <div class="form-group">
                                 <label class="control-label col-md-3">Father Name</label>
                                 <div class="col-md-9">
                                      <html:text property="registrantContactDetails.fatherName" styleClass="form-control"></html:text>
                                 </div>
                             </div>
                         </div>
                         <!--/span-->
                         <div class="col-md-6">
                             <div class="form-group ">
                                  <label class="control-label col-md-3">Mother Name</label>
                                 <div class="col-md-9">
                                      <html:text property="registrantContactDetails.motherName" styleClass="form-control"></html:text>
                                 </div>
                             </div>
                         </div>
                         <!--/span-->
                     </div>
                     <div class="row">
                     		<div class="col-md-6">
                             <div class="form-group">
                                 <label class="control-label col-md-3">Gender</label>
                                 <div class="col-md-9">
                                     <html:select property="registrantContactDetails.gender" styleClass="form-control" >
                                         <option value="male">Male</option>
                                         <option value="female">Female</option>
                                     </html:select>
                                 </div>
                             </div>
                         </div>
                         <!--/span-->
                         <div class="col-md-6">
                             <div class="form-group">
                                 <label class="control-label col-md-3">Email <span class="required" aria-required="true"> * </span></label>
                                 <div class="col-md-9">
                                     <html:text property="registrantContactDetails.email" styleClass="form-control individual regi" ></html:text>
                                 </div>
                           <!--/span-->
                             </div>
                         </div>
                     </div>
                     <div class="row">
                           	<div class="col-md-6">
                                 <div class="form-group">
                                     <label class="control-label col-md-3">Select <span class="required" aria-required="true"> * </span></label>
                                     <div class="col-md-9">
                                        	<div class="radio-list">
										<label class="radio-inline"> 
											<input type="radio" data-type="nid" data-text="<%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.VPN_CLIENT_ADD.VPN_CLIENT_ADD_NID) %>" name="clientDetailsDTO.identityType" value="<%=FileTypeConstants.VPN_CLIENT_ADD.VPN_CLIENT_ADD_NID %>" class="identityType identityTypeInd"  />
											<%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.VPN_CLIENT_ADD.VPN_CLIENT_ADD_NID) %>
										</label> 
										<label class="radio-inline"> 
											<input type="radio" data-type="passport" data-text="<%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.VPN_CLIENT_ADD.VPN_CLIENT_ADD_PASSPORT) %>" name="clientDetailsDTO.identityType" value="<%=FileTypeConstants.VPN_CLIENT_ADD.VPN_CLIENT_ADD_PASSPORT %>" class="identityType identityTypeInd" />
											<%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.VPN_CLIENT_ADD.VPN_CLIENT_ADD_PASSPORT) %>
										</label>
									</div>
                                     </div>
                               <!--/span-->
                                 </div>
                             </div>
                               <!--/span-->
                             <div class="col-md-6">
                                 <div class="form-group">
                                     <label class="control-label col-md-3"> <span class="identityNo"></span> No <span class="required" aria-required="true"> * </span></label>
                                     <div class="col-md-9">
                                         	<input type=text class=form-control name=clientDetailsDTO.identityNo>
                                     </div>
                               <!--/span-->
                                 </div>
                             </div>
                               <!--/span-->
                       </div>
                     <div class="row">
                         <div class="col-md-6">
                             <div class="form-group">
                                 <label class="control-label col-md-3">Mobile Number <span class="required" aria-required="true"> * </span></label>
                                 <div class="col-md-9">
										<input name="intlMobileNumber" class="phoneNumber form-control individual regi" type="tel"> 
										<span  class="hide valid-msg"> Mobile number is valid</span> 
										<span  class="hide error-msg"> Mobile number is invalid </span>
								 </div>
                             </div>
                		 </div>
                         <!--/span-->
                          <div class="col-md-6">
                             <div class="form-group">
                                 <label class="control-label col-md-3">Fax </label>
                                 <div class="col-md-9">
                                     <html:text property="registrantContactDetails.faxNumber" value="" styleClass="form-control individual regi"> </html:text>
                                 </div>
                           <!--/span-->
                             </div>
                         </div>
                           <!--/span-->
                     </div>
                     <div class="row">
                         <div class="col-md-6">
                             <div class="form-group">
                                 <label class="control-label col-md-3">Date of Birth</label>
                                 <div class="col-md-9">
                                 	 	<html:text property="registrantContactDetails.dateOfBirth" styleClass="form-control datepicker"></html:text>
                                  </div>
                             </div>
                         </div>
                         <!--/span-->
                         <div class="col-md-6">
                             <div class="form-group">
                                 <label class="control-label col-md-3">Occupation</label>
                                 <div class="col-md-9">
                                   	 <html:text property="registrantContactDetails.occupation" value="" styleClass="form-control"> </html:text>
                           		</div>
                             </div>
                         </div>
                     </div>
                     <!--/row-->
                    
                    </div>
                     <div id="regCompanyInfo" class="hidden">
                     	<h3 class="form-section">Company Info</h3>
                   	 
                   		<div class="row">
                         <div class="col-md-6">
                             <div class="form-group">
                                 <label class="control-label col-md-3">Company Name <span class="required" aria-required="true"> * </span> </label>
                                 <div class="col-md-9">
                                      <html:text property="registrantContactDetails.registrantsName" styleClass="form-control  company "></html:text>
                                     <span class="help-block"> Name of the company </span>
                                     <html:hidden property="registrantContactDetails.registrantsLastName" styleClass="form-control company "></html:hidden>
                                 </div>
                             </div>
                         </div>
                         <!--/span-->
                          <div class="col-md-6">
                            <div class="form-group">
                                 <label class="control-label col-md-3">Web Address</label>
                                 <div class="col-md-9">
                                      <html:text property="registrantContactDetails.webAddress" styleClass="form-control"></html:text>
                                 </div>
                             </div>
                         </div>
                         <!--/span-->
                     </div>
                     <!--/row-->
                      <div class="row">
                          <div class="col-md-6">
                             <div class="form-group">
                                 <label class="control-label col-md-3">Company Type</label>
                                 <div class="col-md-9">
                                 	<html:select property="clientDetailsDTO.registrantType" styleClass="form-control border-radius ">
										<option value="0">Select</option>
										 <%
										HashMap<Integer, String> companyMap = RegistrantTypeConstants.REGISTRANT_TYPE.get(ModuleConstants.Module_ID_VPN);
											for (Integer key : companyMap.keySet()) {
										%>
											<option  value="<%=key%>"><%=companyMap.get(key) %></option>
										<%
											}
										%>
									</html:select>
				
                                 </div>
                             </div>
                         </div>
                        <div class="col-md-6">
                             <div class="form-group">
                                 <label class="control-label col-md-3">Email <span class="required" aria-required="true"> * </span></label>
                                 <div class="col-md-9">
                                   <html:text property="registrantContactDetails.email" styleClass="form-control company " ></html:text>
                                 </div>
                           <!--/span-->
                             </div>
                         </div>
                     </div>
                     <!--/row-->
                      <div class="row">
                          	<div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label col-md-3">Select <span class="required" aria-required="true"> * </span> </label>
                                    <div class="col-md-9">
                                       	<div class="radio-list">
											<label class="radio-inline"> 
												<input type="radio" data-type="tin" data-text="<%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.VPN_CLIENT_ADD.VPN_CLIENT_ADD_TIN) %>" 
													 name="clientDetailsDTO.identityType"  value="<%=FileTypeConstants.VPN_CLIENT_ADD.VPN_CLIENT_ADD_TIN %>" class="identityType identityTypeCom" >
												<%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.VPN_CLIENT_ADD.VPN_CLIENT_ADD_TIN) %>
											</label> 
											<label class="radio-inline"> 
												<input type="radio" data-type="trade" data-text="<%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.VPN_CLIENT_ADD.VPN_CLIENT_ADD_TRADE) %>" 
													 name="clientDetailsDTO.identityType"  value="<%=FileTypeConstants.VPN_CLIENT_ADD.VPN_CLIENT_ADD_TRADE %>" class="identityType identityTypeCom" >
												<%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.VPN_CLIENT_ADD.VPN_CLIENT_ADD_TRADE) %>
											</label>
											<label class="radio-inline">
												<input id="forwardingLetter" type="radio"  data-type="forwardingLetterFile" data-text="<%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.VPN_CLIENT_ADD.VPN_CLIENT_ADD_FORWARDING_LETTER)%>"
													name="clientDetailsDTO.identityType" value="<%=FileTypeConstants.VPN_CLIENT_ADD.VPN_CLIENT_ADD_FORWARDING_LETTER%>"
													class="identityType identityTypeCom">
													<%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.VPN_CLIENT_ADD.VPN_CLIENT_ADD_FORWARDING_LETTER)%>
											</label>
										</div>
                                    </div>
                              <!--/span-->
                                </div>
                            </div>
                              <!--/span-->
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label col-md-3"> <span class="identityLabel"></span>  No <span class="required" aria-required="true"> * </span></label>
                                    <div class="col-md-9">
										<input type=text class=form-control name=clientDetailsDTO.identityNo>
                                    </div>
                              <!--/span-->
                                </div>
                            </div>
                              <!--/span-->
                        </div>
                      <div class="row">
                          <div class="col-md-6">
                             <div class="form-group">
                                 <label class="control-label col-md-3">Mobile Number <span class="required" aria-required="true"> * </span></label>
                                 <div class="col-md-9">
									<input name="intlMobileNumber" class="phoneNumber form-control company regi" type="tel"> 
									<span  class="hide valid-msg"> Mobile number is valid</span> 
									<span  class="hide error-msg"> Mobile number is invalid </span>
								</div>
                             </div>
                         </div>
                         <!--/span-->
                         <div class="col-md-6">
                             <div class="form-group">
                                 <label class="control-label col-md-3">Fax </label>
                                 <div class="col-md-9">
                                		<html:text property="registrantContactDetails.faxNumber" styleClass="form-control company " ></html:text>
                                 </div>
                           <!--/span-->
                             </div>
                         </div>
                         <!--/span-->
                     </div>
                      
                     <div class="row">
	                     <div class="col-md-6">
	                             <div class="form-group">
	                                 <label class="control-label col-md-3">Registrant Category  <span class="required" aria-required="true"> * </span></label>
	                                 <div class="col-md-9">
	                                     <%
											HashMap<Integer, String> regMap = VpnRegistrantsConstants.vpnRegTypeMap;
												for (Integer reg : regMap.keySet()) {
													String re = reg + "";
										%>
										<div class="col-md-6 col-sm-12 col-xs-12 lr-no-padding">
											<html:checkbox property="clientDetailsDTO.regiCategories" value="<%=re%>" />
											<label class=""><%=regMap.get(reg)%></label>
										</div>
					
										<%
											}
										%>
	                                </div>
	                             </div>
	                         </div>
                     	</div>
                    
               		</div>
                    <h3 class="form-section">Address</h3>
                    <!--/row-->
                    <div class="row">
                          <div class="col-md-6">
                            <div class="form-group">
                                <label class="control-label col-md-3">Country <span class="required" aria-required="true"> * </span></label>
                                <div class="col-md-9">
                                    <html:select styleClass="form-control select2" property="registrantContactDetails.country">
                                        <%
                                        String[] locales = Locale.getISOCountries();
                                    		for (String countryCode : locales) {
                                    			Locale obj = new Locale("", countryCode);
                                    			%>
                                    			<html:option value="<%= obj.getCountry() %>"><%=obj.getDisplayCountry() %></html:option>
										<% 
                                    		}
                                        %>
                                    </html:select>
                                </div>
                            </div>
                        </div>
                        <!--/span-->
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="control-label col-md-3">City <span class="required" aria-required="true"> * </span> </label>
                                <div class="col-md-9">
                                   <html:text property="registrantContactDetails.city" styleClass="form-control regi"></html:text>
                                </div>
                          <!--/span-->
                            </div>
                        </div>
                         <!--/span-->
                    </div>
                  	<div class="row">
                     	<div class="col-md-6">
                            <div class="form-group">
                                <label class="control-label col-md-3">Post Code</label>
                                <div class="col-md-9">
                                     <html:text property="registrantContactDetails.postCode" styleClass="form-control regi" ></html:text>
                                </div>
                          <!--/span-->
                            </div>
                        </div>
                          <div class="col-md-6">
                            <div class="form-group">
                                <label class="control-label col-md-3">Address <span class="required" aria-required="true"> * </span></label>
                                <div class="col-md-9">
                                   	<html:textarea property="registrantContactDetails.address" styleClass="form-control border-radius textarea regi"></html:textarea>
								</div>
                            </div>
                        </div>
                    </div>
                      

				<%if((!(loginDTO.getAccountID()>0))){%>
                 <div id="login-information" style="display:block">
                      <h3 class="form-section">Login Info</h3>
                    <!--/row-->
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="control-label col-md-3">Username <span class="required" aria-required="true"> * </span></label>
                                <div class="col-md-9">
                                    <input name="clientDetailsDTO.loginName"  class="form-control" type="text" autocomplete="off"> </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="control-label col-md-3">Password <span class="required" aria-required="true"> * </span></label>
                                <div class="col-md-9">
                                    <input name="clientDetailsDTO.loginPassword" id="password" class="form-control" type="password" autocomplete="off"> </div>
                            </div>
                        </div>
                    </div>
                     <div class="row">
                         <div class="col-md-6">
                            <div class="form-group">
                                <label class="control-label col-md-3">Confirm Password <span class="required" aria-required="true"> * </span></label>
                                <div class="col-md-9">
                                    <input name="confirmLoginPassword" id="cPassword" class="form-control" type="password" autocomplete="off"> </div>
                            </div>
                        </div>
                    </div>
                    </div>
                    <%} %>
                    
					<div class="col-md-12"><hr></div>
					<div class="row">
						<div class="col-md-4 col-sm-6 contact-info">
							<%@include file="../client/clientAddBody_billingContact.jsp"%>
						</div>
				
						<div class="col-md-4 col-sm-6 contact-info">
							<%@include file="../client/clientAddBody_AdminContact.jsp"%>
						</div>
				
						<div class="col-md-4 col-sm-6 contact-info">
							<%@include file="../client/clientAddBody_TechnicalContact.jsp"%>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12"><hr></div>
						<div class="col-md-12">
						 	<div  id="nid" class="col-md-3 fileType" style="padding: 0px;">
								<!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
								<span class="btn btn-warning-btcl  fileinput-button">
									<i class="fa fa-upload"></i>
									<span> Add <%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.VPN_CLIENT_ADD.VPN_CLIENT_ADD_NID) %> Documents </span> 
									<input class="jFile" type="file" name="<%=FileTypeConstants.VPN_CLIENT_ADD.VPN_CLIENT_ADD_NID %>" >
								</span>
							</div>
							<div  id="passport" class="col-md-3 fileType" style="padding: 0px;">
								<!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
								<span class="btn btn-warning-btcl  fileinput-button">
									<i class="fa fa-upload"></i>
									<span> Add <%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.VPN_CLIENT_ADD.VPN_CLIENT_ADD_PASSPORT) %> Documents </span> 
									<input class="jFile" type="file" name="<%=FileTypeConstants.VPN_CLIENT_ADD.VPN_CLIENT_ADD_PASSPORT %>" >
								</span>
							</div>
							<div  id="tin" class="col-md-3 fileType" style="padding: 0px; display: none">
								<!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
								<span class="btn yellow  fileinput-button">
									<i class="fa fa-upload"></i>
									<span> Add  <%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.VPN_CLIENT_ADD.VPN_CLIENT_ADD_TIN)  %>  Documents </span> 
									<input class="jFile" type="file" name="<%=FileTypeConstants.VPN_CLIENT_ADD.VPN_CLIENT_ADD_TIN %>" multiple>
								</span>
							</div>
							<div  id="trade" class="col-md-3 fileType" style="padding: 0px; display: none">
								<!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
								<span class="btn yellow  fileinput-button">
									<i class="fa fa-upload"></i>
									<span> Add  <%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.VPN_CLIENT_ADD.VPN_CLIENT_ADD_TRADE)  %>  Documents </span> 
									<input class="jFile" type="file" name="<%=FileTypeConstants.VPN_CLIENT_ADD.VPN_CLIENT_ADD_TRADE %>" multiple>
								</span>
							</div>
							<div  id="forwardingLetterFile" class="col-md-3 fileType" style="padding: 0px; display: none">
								<!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
								<span class="btn yellow  fileinput-button">
									<i class="fa fa-upload"></i>
									<span> Add  <%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.VPN_CLIENT_ADD.VPN_CLIENT_ADD_FORWARDING_LETTER)  %>  Documents </span> 
									<input class="jFile" type="file" name="<%=FileTypeConstants.VPN_CLIENT_ADD.VPN_CLIENT_ADD_FORWARDING_LETTER %>" multiple>
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
				 		<jsp:include page="../../common/ajaxfileUploadTemplate.jsp" />
					</div>
					<!-- /.box-body -->
				</div>
				<div class="form-actions">
					<div class="row">
	                     <div class="col-md-offset-4 col-md-8">
	                     	 	<html:hidden styleId="clientCategoryType" property="clientDetailsDTO.clientCategoryType"  value="1"/>
							 	<input type="hidden" name="clientDetailsDTO.moduleID" value="<%=ModuleConstants.Module_ID_VPN%>">
	                         	<input type="hidden" name="registrantContactDetails.detailsType" value="<%=ClientConstants.DETAILS_TYPE_REGISTRANT%>" />
								<input type="hidden" name="billingContactDetails.detailsType" value="<%=ClientConstants.DETAILS_TYPE_BILLING%>" /> 
								<input type="hidden" name="adminContactDetails.detailsType" value="<%=ClientConstants.DETAILS_TYPE_ADMIN%>" />
								<input type="hidden" name="technicalContactDetails.detailsType" value="<%=ClientConstants.DETAILS_TYPE_TECHNICAL%>" />
	                         	<html:hidden property="registrantContactDetails.phoneNumber"/>	 
	        
	        					<button type="reset" class="btn btn-reset-btcl">Reset</button>
	                         	<button type="submit" class="btn btn-submit-btcl">Submit</button>
	                     </div>
	                 </div>
	            </div>
		</div>
	</div>
	
</html:form>
<script src="${context}/assets/scripts/client/client-add-validation.js" type="text/javascript"></script>
<script src="${context}/assets/scripts/client/client-contact-copy.js" type="text/javascript"></script>
<script src="${context}assets/scripts/client/client-guidelines.js" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function() {
  
	var accountType="<%=ClientForm.CLIENT_TYPE_COMPANY%>";
	var countryDefault="BD";
	
	$("input[name='accountType']").change(function(){
	    accountType=$("input[name='accountType']:checked").val();
	    console.log(accountType);
	    showAccountInfo(accountType);
	})
	
	function showAccountInfo(type){
	   	$("#clientCategoryType").val(type);
	   	$(".fileType").hide();
	   	$('.identityLabel').html("");
	   
	   	$("#regCompanyInfo .checker").removeClass('disabled');
		$("#regCompanyInfo .radio").removeClass('disabled');
		
	   if(type==1){
			$("#regIndividualInfo").removeClass('hidden');
			$("#regCompanyInfo").addClass('hidden');
			
			$("#regCompanyInfo").find("input, select").attr("disabled", true);
			$("#regIndividualInfo").find("input, select").attr("disabled", false);
			
			$(".individual").addClass("regi");
			$(".company").removeClass("regi");
		}else{
	   	 	$("#regCompanyInfo").removeClass('hidden');
	   	 	$("#regIndividualInfo").addClass('hidden');
	   	 	
	   	 	$("#regIndividualInfo").find("input, select").attr("disabled", true);
	   	 	$("#regCompanyInfo").find("input, select").attr("disabled", false);
	   	 	
	   	 	$(".individual").removeClass("regi");
			$(".company").addClass("regi");
		}
	   $('#forwardingLetter').closest('.radio-inline').hide();
	}
	
	$('.identityType').change(function(){
		$('.identityLabel').html($(this).data('text'));
		$('input[name="clientDetailsDTO.identityNo"]').val('');
		$(".fileType").hide();
		var fileTypeId=$(this).data('type');
		$("#"+fileTypeId).show();
	});
	function init(){
	    var countrySelector=$('select[name="registrantContactDetails.country"]')
		countrySelector.val(countryDefault);
		sortSelectBox(countrySelector);
	    showAccountInfo(accountType);
	}
 	init();
	 	
});
</script>

<script src="../../assets/scripts/common/client-info-copy.js"></script>