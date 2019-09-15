<!-- Company -->
<%@page import="client.ClientTypeService" %>
<%@page import="common.ObjectPair" %>
<%@page import="util.ServiceDAOFactory" %>
<%@page import="vpn.clientContactDetails.ClientContactDetailsDTO" %>
<%@page import="java.util.List" %>

<%
    ClientContactDetailsDTO contactDetails = (ClientContactDetailsDTO) session.getAttribute("registrantContactDetails");
%>


<div id="regCompanyInfo">
    <h3 class="form-section">Registrant Info</h3>

    <div class="row">
        <div class="col-md-6">
            <div class=form-group>
                <label class="col-sm-4 control-label">Registrant Name<span class=required
                                                                           aria-required=true>*</span></label>
                <div class=col-sm-8>
                    <input type=text name="registrantContactDetails.registrantsName" class="form-control company regi">
                    <input type=hidden name="registrantContactDetails.registrantsLastName"
                           class="form-control company regi"><!-- unnecessarily added to match script -->
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class=form-group>
                <label class="col-sm-4 control-label">Web Address</label>
                <div class=col-sm-8>
                    <input type=text name="registrantContactDetails.webAddress" class="form-control">
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-6">
            <div class=form-group>
                <label class="col-sm-4 control-label">Email<span class=required aria-required=true>*</span></label>

                <div class=col-sm-8>

                    <%if (contactDetails != null) {%>

                    <%if (contactDetails.getIsEmailVerified() == 0) {%>
                    <input type=text name="registrantContactDetails.email" class="form-control company regi">
                    <%} else {%>
                    <input readonly type=text name="registrantContactDetails.email" class="form-control company regi">
                    <%}%>
                    <%} else {%>
                    <input type=text name="registrantContactDetails.email" class="form-control company regi">
                    <%}%>


                </div>

            </div>
        </div>
        <div class="col-md-6">
            <div class=form-group>
                <label class="col-sm-4 control-label">Mobile Number<span class=required
                                                                         aria-required=true>*</span></label>
                <div class=col-sm-8>

                    <%if (contactDetails != null) {%>

                    <%if (contactDetails.getIsPhoneNumberVerified() == 0) {%>
                    <input name="registrantContactDetails.phoneNumber" class="phoneNumber form-control company regi"
                           type="tel">
                    <%} else {%>
                    <input readonly name="registrantContactDetails.phoneNumber"
                           class="phoneNumber form-control company regi" type="tel">
                    <%}%>
                    <%} else {%>
                    <input name="intlMobileNumber" class="phoneNumber form-control company regi"
                           type="tel">

                    <%}%>

                    <span class="hide valid-msg"> Mobile number is valid</span>
                    <span class="hide error-msg"> Mobile number is invalid </span>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-6">
            <div class=form-group>
                <label class="col-sm-4 control-label">Telephone Number</label>
                <div class=col-sm-8>
                    <input type=text name="registrantContactDetails.landlineNumber" class="form-control company regi">
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class=form-group>
                <label class="col-sm-4 control-label">Fax</label>
                <div class=col-sm-8>
                    <input type=text name="registrantContactDetails.faxNumber" class="form-control company regi">
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-6">
            <div class=form-group>
                <label class="col-sm-4 control-label">Registrant Type<span class=required
                                                                           aria-required=true>*</span></label>
                <div class=col-sm-8>
                    <select name="clientDetailsDTO.registrantType" class="form-control border-radius">
                        <option value="0">Select</option>
                        <%
                            List<ObjectPair<Integer, String>> registrantTypeList = ServiceDAOFactory
                                    .getService(ClientTypeService.class).getRegistrantTypeListByModuleID(moduleID);
                            for (ObjectPair registrantType : registrantTypeList) {
                        %>
                        <option value="<%=registrantType.key%>"><%=registrantType.value%>
                        </option>
                        <%}%>
                    </select>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class=col-sm-12>
            <div class="form-group">
                <label class="col-md-2 control-label">Registrant Category<span class="required"
                                                                               aria-required="true"> * </span></label>
                <div class=col-md-10>
                    <div id=registrantCategoryContainer></div>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class=col-sm-12>
            <div class="form-group">
                <label class="col-md-2 control-label">Registrant Sub-Category</label>
                <div class=col-md-10>
                    <div id=registrantSubCategoryContainer></div>
                </div>
            </div>
        </div>
    </div>
</div>