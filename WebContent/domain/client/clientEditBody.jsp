<%@page import="common.RegistrantTypeConstants"%>
<%@page import="common.LanguageConstants"%>
<%@page import="domain.constants.DomainRegistrantsConstants"%>
<%@page import="util.MultipleTypeUtils"%>
<%@page import="vpn.client.ClientUpdateChecker"%>
<%@page import="request.StateRepository"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="login.LoginDTO"%>
<%@page import="common.ClientConstants"%>
<%@page import="file.FileService"%>
<%@page import="util.Country2Phone"%>
<%@page import="java.util.Locale"%>
<%@page import="util.SOP"%>
<%@page import="file.FileDTO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="file.FileDAO"%>
<%@page import="file.FileTypeConstants"%>
<%@page import="common.ModuleConstants"%>
<%@page import="java.util.Set"%>
<%@page import="vpn.clientContactDetails.ClientContactDetailsDTO"%>
<%@page import="vpn.client.ClientForm"%>
<%@page import="vpn.constants.VpnRegistrantsConstants"%>
<%@page import="java.util.HashMap"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<html:base />


<%
	String context = "../../.." + request.getContextPath() + "/";
	String editableStr = request.getParameter("isEditable");
	String actionName = "/EditClient";
	int moduleID = Integer.parseInt(request.getParameter("moduleID"));
	int id = Integer.parseInt(request.getParameter("entityID"));
	ClientForm clientForm = (ClientForm)request.getAttribute("form");
	ClientContactDetailsDTO registrantContactDetails = clientForm.getRegistrantContactDetails();
	Integer registrantCategory = (int)clientForm.getClientDetailsDTO().getRegistrantCategory();
	LoginDTO adminDTO=  (login.LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	boolean isClientUpdateAllowed=ClientUpdateChecker.isClientAllowedForUpdate(clientForm.getClientDetailsDTO().getCurrentStatus());
	int currentState=clientForm.getClientDetailsDTO().getCurrentStatus();
	boolean clientCanUpdateHimself =ClientUpdateChecker.isClientAllowedForUpdate(currentState);
	
%>
<%-- <%@include file="../../common/changeClientType.jsp"%> --%>
<html:form  styleId="fileupload" styleClass="form-horizontal" action="<%=actionName%>" enctype="multipart/form-data" method="POST">
	<input type="hidden" name="csrfPreventionSalt" value="${csrfPreventionSalt}"/>
	<html:hidden property="clientDetailsDTO.clientID" />
	<html:hidden property="clientDetailsDTO.id" />
	<html:hidden property="registrantContactDetails.ID" />
	<html:hidden property="billingContactDetails.ID" />
	<html:hidden property="adminContactDetails.ID" />
	<html:hidden property="technicalContactDetails.ID" />
	<html:hidden property="clientDetailsDTO.clientCategoryType" styleId="clientCategoryType" />
	<input type=hidden name="clientDetailsDTO.identityType" id="identityType" />
	<html:hidden property="clientDetailsDTO.moduleID"/>
 	<html:hidden property="registrantContactDetails.detailsType"/>
 	<html:hidden property="billingContactDetails.detailsType" /> 
 	<html:hidden property="adminContactDetails.detailsType" />
 	<html:hidden property="technicalContactDetails.detailsType" />
	<html:hidden property="registrantContactDetails.phoneNumber"/>	
	
	<div class="portlet box portlet-btcl">
		<div class="portlet-title">
			<div class="caption">
				<i class="fa fa-user"></i>Domain Registrant Information Edit
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
               	<%if(adminDTO.getIsAdmin() || isClientUpdateAllowed){ %>
	               	<div class="row ">
						<div class="form-group">
							<label class="control-label col-md-2"> <span  class="bold">Registration Type </span> </label>
							<div class="col-md-10">
								<div class="radio-list">
									<label class="radio-inline">
										 <input type="radio" name="accountType" value="<%=ClientForm.CLIENT_TYPE_INDIVIDUAL %>" autocomplete="off" data-no-uniform="true"  />
										 <%=ClientForm.CLIENT_TYPE_STR.get(ClientForm.CLIENT_TYPE_INDIVIDUAL) %>
									</label>
									 <label class="radio-inline"> 
									 	<input type="radio" name="accountType" value="<%=ClientForm.CLIENT_TYPE_COMPANY %>" autocomplete="off"  data-no-uniform="true"  />
									 	 <%=ClientForm.CLIENT_TYPE_STR.get(ClientForm.CLIENT_TYPE_COMPANY) %>
									</label>
								</div>
							</div>
						</div>
					</div>
				<%} %>
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
                                  <label class="control-label col-md-3">Last Name</label>
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
										<input type="radio" data-type="nid" data-text="<%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.DOMAIN_CLIENT_ADD.DOMAIN_CLIENT_ADD_NID) %>" name="clientDetailsDTO.identityType" value="<%=FileTypeConstants.DOMAIN_CLIENT_ADD.DOMAIN_CLIENT_ADD_NID %>" class="identityType identityTypeInd"  />
										<%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.DOMAIN_CLIENT_ADD.DOMAIN_CLIENT_ADD_NID) %>
									</label> 
									<label class="radio-inline"> 
										<input type="radio" data-type="passport" data-text="<%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.DOMAIN_CLIENT_ADD.DOMAIN_CLIENT_ADD_PASSPORT) %>" name="clientDetailsDTO.identityType" value="<%=FileTypeConstants.DOMAIN_CLIENT_ADD.DOMAIN_CLIENT_ADD_PASSPORT %>" class="identityType identityTypeInd" />
										<%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.DOMAIN_CLIENT_ADD.DOMAIN_CLIENT_ADD_PASSPORT) %>
									</label>
								</div>
                                    </div>
                              <!--/span-->
                                </div>
                            </div>
                              <!--/span-->
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label col-md-3"> <span class="identityLabel"></span> No <span class="required" aria-required="true"> * </span></label>
                                    <div class="col-md-9">
                                        	<input type=text class=form-control name='clientDetailsDTO.identityNo'>
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
										<input name="intlMobileNumber" value="<%=registrantContactDetails.getPhoneNumber() %>" class="phoneNumber form-control individual regi" />
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
                                     <html:text property="registrantContactDetails.faxNumber" styleClass="form-control individual regi"> </html:text>
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
                                    <html:text property="registrantContactDetails.occupation" styleClass="form-control"> </html:text>
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
                                 <label class="control-label col-md-3"><%=LanguageConstants.COMPANY %> Name <span class="required" aria-required="true"> * </span> </label>
                                 <div class="col-md-9">
                                     <html:text property="registrantContactDetails.registrantsName" styleClass="form-control  company "></html:text>
                                     <span class="help-block"> Name of the <%=LanguageConstants.COMPANY_TYPE %> </span>
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
                                 <label class="control-label col-md-3"><%=LanguageConstants.COMPANY %> Type <span class="required" aria-required="true"> * </span></label>
                                 <div class="col-md-9">
                                 		<html:select property="clientDetailsDTO.registrantType" styleClass="form-control border-radius ">
											<html:option value="0">Select</html:option>
										 	<%
											HashMap<Integer, String> companyMap = RegistrantTypeConstants.REGISTRANT_TYPE.get(ModuleConstants.Module_ID_DOMAIN);
												for (int key : companyMap.keySet()) {
													String keyStr=key+"";
											%>
													<html:option  value="<%=keyStr%>"><%=companyMap.get(key) %></html:option>
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
                                 <label class="control-label col-md-3">Email <span class="required" aria-required="true"> * </span></label>
                                 <div class="col-md-9">
                                   <html:text property="registrantContactDetails.email" styleClass="form-control company " ></html:text>
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
											<input type="radio" data-type="tin" data-text="<%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.DOMAIN_CLIENT_ADD.DOMAIN_CLIENT_ADD_TIN) %>" name="clientDetailsDTO.identityType" value="<%=FileTypeConstants.DOMAIN_CLIENT_ADD.DOMAIN_CLIENT_ADD_TIN %>" class="identityType identityTypeCom"  />
											<%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.DOMAIN_CLIENT_ADD.DOMAIN_CLIENT_ADD_TIN) %>
										</label> 
										<label class="radio-inline"> 
											<input type="radio" data-type="trade" data-text="<%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.DOMAIN_CLIENT_ADD.DOMAIN_CLIENT_ADD_TRADE) %>" name="clientDetailsDTO.identityType" value="<%=FileTypeConstants.DOMAIN_CLIENT_ADD.DOMAIN_CLIENT_ADD_TRADE %>" class="identityType identityTypeCom" />
											<%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.DOMAIN_CLIENT_ADD.DOMAIN_CLIENT_ADD_TRADE) %>
										</label>
										<label class="radio-inline">
											<input id="forwardingLetter" type="radio"  data-type="forwardingLetterFile" data-text="<%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.DOMAIN_CLIENT_ADD.DOMAIN_CLIENT_ADD_FORWARDING_LETTER)%>"
													name="clientDetailsDTO.identityType" value="<%=FileTypeConstants.DOMAIN_CLIENT_ADD.DOMAIN_CLIENT_ADD_FORWARDING_LETTER%>"
													class="identityType identityTypeCom">
												<%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.DOMAIN_CLIENT_ADD.DOMAIN_CLIENT_ADD_FORWARDING_LETTER)%>
										</label>
									</div>
                                     </div>
                               <!--/span-->
                                 </div>
                             </div>
                             <!--/span-->
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label col-md-3"> <span class="identityLabel"></span> No <span class="required" aria-required="true"> * </span></label>
                                    <div class="col-md-9">
                                        	<input type=text class=form-control name='clientDetailsDTO.identityNo'>
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
										<input name="intlMobileNumber" value="<%=registrantContactDetails.getPhoneNumber() %>" class="phoneNumber form-control company regi" type="tel"> 
										<span  class="hide valid-msg"> Mobile number is valid</span> 
										<span  class="hide error-msg"> Mobile number is invalid </span>
									</div>
                             	</div>
                         </div>	
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
										HashMap<Integer, String> regMap = DomainRegistrantsConstants.domainRegTypeMap;
										for (Integer availableItem : regMap.keySet()) {
									%>
									<div class="col-md-6 col-sm-12 col-xs-12 lr-no-padding">
										<%if (registrantCategory == availableItem){ %>
											<input type="checkbox" name="clientDetailsDTO.regiCategories" value="<%=availableItem%>" checked />
											<label class=""><%=regMap.get(availableItem)%></label>
										<%}else{ %>
											<input type="checkbox" name="clientDetailsDTO.regiCategories" value="<%=availableItem%>" />
											<label class=""><%=regMap.get(availableItem)%></label>
										<%} %>
									</div>
				
									<%
                                   		}
									%>
                                </div>
                             </div>
                         </div>
                       
                     </div>
                     <!--/row-->
                		</div>
                    <h3 class="form-section">Address</h3>
                    <!--/row-->
                    <div class="row">
                          <div class="col-md-6">
                            <div class="form-group">
                                <label class="control-label col-md-3">Country <span class="required" aria-required="true"> * </span></label>
                                <div class="col-md-9">
                                    <html:select styleClass="form-control " property="registrantContactDetails.country">
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
                                <label class="control-label col-md-3">Post Code </label>
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
                      <h3 class="form-section">Login Info</h3>
                    <!--/row-->
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="control-label col-md-3">Username <span class="required" aria-required="true"> * </span></label>
                                <div class="col-md-9">
                                    <html:text property="clientDetailsDTO.loginName" styleClass="form-control" /> 
                                 </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="control-label col-md-3">Password <span class="required" aria-required="true"> * </span></label>
                                <div class="col-md-9">
                                    <html:password property="clientDetailsDTO.loginPassword" styleId="password" styleClass="form-control" />
                                </div>
                            </div>
                        </div>
                    </div>
                     <div class="row">
                         <div class="col-md-6">
                            <div class="form-group">
                                <label class="control-label col-md-3">Confirm Password <span class="required" aria-required="true"> * </span></label>
                                <div class="col-md-9">
                                    <input name="confirmLoginPassword" class="form-control" id="cPassword"  type="password" autocomplete="off"> 
                                </div>
                            </div>
                        </div>
                    </div>
                    
				<div class="col-md-12"><hr></div>
				<div class="row"> 
					<div class="col-md-4 col-sm-6 contact-info">
						<%@include file="clientAddBody_billingContact.jsp"%>
					</div>
			
					<div class="col-md-4 col-sm-6 contact-info">
						<%@include file="../client/clientAddBody_AdminContact.jsp"%>
					</div>
			
					<div class="col-md-4 col-sm-6 contact-info">
						<%@include file="../client/clientAddBody_TechnicalContact.jsp"%>
					</div>
				</div>
				<div class="row">
					<%if(adminDTO.getIsAdmin() || isClientUpdateAllowed){ %>
					<div class="col-md-12">
					 	<div  id="nid" class="col-md-3 fileType" style="padding: 0px;">
								<!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
								<span class="btn btn-warning-btcl  fileinput-button">
									<i class="fa fa-upload"></i>
									<span> Add <%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.DOMAIN_CLIENT_ADD.DOMAIN_CLIENT_ADD_NID) %> Documents </span> 
									<input class="jFile" type="file" name="<%=FileTypeConstants.DOMAIN_CLIENT_ADD.DOMAIN_CLIENT_ADD_NID %>" >
								</span>
							</div>
							<div  id="passport" class="col-md-3 fileType" style="padding: 0px;">
								<!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
								<span class="btn btn-warning-btcl  fileinput-button">
									<i class="fa fa-upload"></i>
									<span> Add <%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.DOMAIN_CLIENT_ADD.DOMAIN_CLIENT_ADD_PASSPORT) %> Documents </span> 
									<input class="jFile" type="file" name="<%=FileTypeConstants.DOMAIN_CLIENT_ADD.DOMAIN_CLIENT_ADD_PASSPORT %>" >
								</span>
							</div>
							<div  id="tin" class="col-md-3 fileType" style="padding: 0px; display: none">
								<!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
								<span class="btn yellow  fileinput-button">
									<i class="fa fa-upload"></i>
									<span> Add  <%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.DOMAIN_CLIENT_ADD.DOMAIN_CLIENT_ADD_TIN)  %>  Documents </span> 
									<input class="jFile" type="file" name="<%=FileTypeConstants.DOMAIN_CLIENT_ADD.DOMAIN_CLIENT_ADD_TIN %>" multiple>
								</span>
							</div>
							<div  id="trade" class="col-md-3 fileType" style="padding: 0px; display: none">
								<!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
								<span class="btn yellow  fileinput-button">
									<i class="fa fa-upload"></i>
									<span> Add  <%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.DOMAIN_CLIENT_ADD.DOMAIN_CLIENT_ADD_TRADE)  %>  Documents </span> 
									<input class="jFile" type="file" name="<%=FileTypeConstants.DOMAIN_CLIENT_ADD.DOMAIN_CLIENT_ADD_TRADE %>" multiple>
								</span>
							</div>
							<div  id="forwardingLetterFile" class="col-md-3 fileType" style="padding: 0px; display: none">
								<!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
								<span class="btn yellow  fileinput-button">
									<i class="fa fa-upload"></i>
									<span> Add  <%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.DOMAIN_CLIENT_ADD.DOMAIN_CLIENT_ADD_FORWARDING_LETTER)  %>  Documents </span> 
									<input class="jFile" type="file" name="<%=FileTypeConstants.DOMAIN_CLIENT_ADD.DOMAIN_CLIENT_ADD_FORWARDING_LETTER %>" multiple>
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
						<jsp:include page="../../common/fileListHelperEdit.jsp" flush="true">
							<jsp:param name="entityTypeID" value="<%=EntityTypeConstant.DOMAIN_CLIENT%>" />	
							<jsp:param name="entityID" value="<%=id%>" />	
						</jsp:include>				
					</div>
			 		<jsp:include page="../../common/ajaxfileUploadTemplate.jsp" />
			 		<%}else{ %>
			 			<jsp:include page="../../common/fileListHelper.jsp" flush="true">
							<jsp:param name="entityTypeID" value="<%=EntityTypeConstant.DOMAIN_CLIENT%>" />	
							<jsp:param name="entityID" value="<%=id%>" />	
						</jsp:include>	
			 		<%} %>
				</div>
				<!-- /.box-body -->
				
			</div>
			<div class="form-actions">
				<div class="row">
                     <div class="col-md-offset-4 col-md-8">
                    	 <a class="btn btn-cancel-btcl" type="button" href="<%=request.getHeader("referer")%>">Cancel</a>
                         <button type="reset" class="btn btn-reset-btcl">Reset</button>
                         <button type="submit" class="btn btn-submit-btcl">Submit</button>
		            </div>
                 </div>
            </div>
		</div>
	</div>
 
</html:form>
<script src="<%=request.getContextPath()%>/assets/scripts/client/client-add-validation.js" type="text/javascript"></script>
<script src="${context}/assets/scripts/client/client-contact-copy.js" type="text/javascript"></script>
<script src="${context}assets/scripts/client/client-guidelines.js" type="text/javascript"></script>
<script type="text/javascript">
 function isNotAdmin(){
	 return <%=!adminDTO.getIsAdmin()%>;
 }
 
 function isClientNotAllowedForUpdate(){
	 return <%=!isClientUpdateAllowed%>;
 }
 
 function canNotUpdateHimself(){
	 return <%=!clientCanUpdateHimself%>;
 }
</script>


 <script type="text/javascript">
 	var nonEditables=[
 	                  {
 	                	'input':'text',
 	                	'names':['registrantContactDetails.registrantsName',
	 	                 		'registrantContactDetails.registrantsLastName',
	 	                 		'clientDetailsDTO.loginName',
	 	                		'registrantContactDetails.webAddress'
	 	                 	]
 					 },
 	                 {	
  						'input':'checkbox',
  	                	'names':[
  	                 		'registrantContactDetails.regiCat'
  	                 	]
  	                 },
 	                 {
 	                	'input':'select',
 	                	 'names':[
							'clientDetailsDTO.registrantType'
 	                 	]
 	                 }
                ];
 	
  	var  clientDetailsDTO=$("input[name='clientDetailsDTO.identityNo']");
    if(!clientDetailsDTO.val().trim()==''){
    	clientDetailsDTO.removeAttr('readonly');
    	var identityTypeInput={	
					'input':'radio',
	                	'names':[
	                	   	'clientDetailsDTO.identityType'
	                 	]
	                 };
        var identityInput= {	
			'input':'text',
           	'names':[
           	   	'clientDetailsDTO.identityNo'
            	]
            };
        
        nonEditables.push(identityTypeInput);
        nonEditables.push(identityInput);
	}
 	
    $(document).ready(function() {
		var mobileNumArray="";
	 	var prefixPhoneCode="";
		var accountType=$("#clientCategoryType").val();
	 	var identityTypeVal=$('#identityType').val();
	 	
		$("input[name='accountType']").change(function(){
		    accountType=$("input[name='accountType']:checked").val();
		    console.log(accountType);
		    showAccountInfo(accountType);
		})
		
		function showAccountInfo(type){
			$(".fileType").hide();
		   	$('.identityLabel').html("");
		   	$("#clientCategoryType").val(type);
		   	
		  	if(type==CONFIG.get('clientType','ind')){
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
		   
		   if($('#forwardingLetter').val()==identityTypeVal) {
			   $('#regCompanyInfo input[name="clientDetailsDTO.identityNo"]').closest('.form-group').hide();
			   $('.identityTypeCom').closest('.radio-inline').hide();
			   $('#forwardingLetter').closest('.radio-inline').show();
	  	 	}else{
			   $('#forwardingLetter').closest('.radio-inline').hide();
		  	}
		};
		
		$('.identityType').change(function(){
			$('.identityLabel').html($(this).data('text'));
			$('input[name="clientDetailsDTO.identityNo"]').val('');
			$(".fileType").hide();
			var fileTypeId=$(this).data('type');
			$("#"+fileTypeId).show();
			$("#identityType").val($(this).val());
		});

		
		function editableFalse(){
			$.each(nonEditables, function( index, typeNameObj ) {
				  //alert( index + ": " + nameAttr );
				  console.log(index+" "+typeNameObj['input']);
				  switch(typeNameObj['input']){
					  case 'text':{
						  $.each(typeNameObj['names'], function( index, nameAttr ) {
						  	$('input[name="'+nameAttr+'"]').prop("readonly", true);
						  });
						  break;
					  }
					  case 'select':{
						  $.each(typeNameObj['names'], function( index, nameAttr ) {
						  	$('select[name="'+nameAttr+'"]').prop("readonly", true);
						  	$('select[name="'+nameAttr+'"]').attr("disabled", 'disabled');
						  });
						  break;
					  }
					  case 'checkbox':{
						  $.each(typeNameObj['names'], function( index, nameAttr ) {
						  	$('input[name="'+nameAttr+'"]').prop("readonly", true);
						  	$('input[name="'+nameAttr+'"]').attr("disabled", 'disabled');
						  });
						  break;
					  }
					  case 'radio':{
						  $.each(typeNameObj['names'], function( index, nameAttr ) {
						  	$('input[name="'+nameAttr+'"]').prop("readonly", true);
						  	$('input[name="'+nameAttr+'"]').attr("disabled", 'disabled');
						  });
						  break;
					  }
					  default:{
						  console.log("all fields are editable");
					  }
				  }
				});
			$("#fieldName").prop("readonly", true);
		}
		function init(){
		    $('input[name="confirmLoginPassword"]').val($("input[name='clientDetailsDTO.loginPassword']").val());
		    $("input[name='clientDetailsDTO.loginName']").attr('disabled','disabled');
		   	$('input[name="accountType"][value="'+accountType+'"]').attr('checked',true);
		    showAccountInfo(accountType);
		    console.log(prefixPhoneCode);
		    
		    $('.identityType').each(function(){
		    	if($(this).val()==identityTypeVal){
		    		$(this).prop('checked',true);
		    		$("#"+$(this).data('type')).show();
		    		$('.identityLabel').html($(this).data('text'));
		    	}
		    })
	     	
		    
		    $.uniform.update(".identityType");
		    
		    var countrySelector=$('select[name="registrantContactDetails.country"]')
			sortSelectBox(countrySelector);
			
		    if(isNotAdmin() && canNotUpdateHimself()){
				editableFalse();
			}
		    
		    if(isNotAdmin() && isClientNotAllowedForUpdate()){
				editableFalse();
			}
		    
		}
		init();
		
  });
</script>