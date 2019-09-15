<%@page import="client.ClientService" %>
<%@page import="client.IdentityTypeConstants" %>
<%@page import="common.EntityTypeConstant" %>
<%@ page import="common.UniversalDTOService" %>
<%@ page import="configuration.FileConfiguration" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="sessionmanager.SessionConstants" %>
<%@ page import="java.util.Map" %>
<%@ page import="clientdocument.ClientDocumentService" %>
<%@ page import="com.google.gson.Gson" %>


<%
    login.LoginDTO loginDTO = (login.LoginDTO) request.getSession(true)
            .getAttribute(SessionConstants.USER_LOGIN);
    Logger logger = Logger.getLogger(getClass());

    int id = -1;
    String context = "../../.." + request.getContextPath() + "/";
    String message = (String) request.getAttribute("confirmation");

    int moduleID = Integer.parseInt(request.getParameter("moduleID"));
    ClientService clientService = ServiceDAOFactory.getService(ClientService.class);

    FileConfiguration fileConfiguration = ServiceDAOFactory.getService(UniversalDTOService.class).get(FileConfiguration.class);

    Boolean isClientRegisteringNewModule = false;

    request.getSession().removeAttribute("registrantContactDetails");
    Map<Integer, Boolean> map = ServiceDAOFactory.getService(ClientDocumentService.class).getAllDocsWithIsGlobalProperty();
    String mapString = new Gson().toJson(map);


%>

<script>
    let map = JSON.parse('<%=mapString%>');

    var globalVariableToStoreSelectedRegistrantSubCategory = [];
    var btrcLicenseDate = "";

</script>


<form id="fileupload" class="form-horizontal" action="<%=request.getContextPath()%>/Client/Add.do" method="POST">
    <input type="hidden" name="csrfPreventionSalt" value="${csrfPreventionSalt}"/>
    <div class="portlet box portlet-btcl">
        <div class="portlet-title">
            <div class="caption">
                <i class="fa fa-user"></i>Client Registration Information
            </div>
        </div>
        <div class="portlet-body form">
            <div class="form-body">


                <%@include file="../../includes/vpnclients/client-info-copy.jsp" %>

                <div class="row form-group">
                    <label class="control-label col-md-3 text-center"> <span class="bold">Registration Type </span>
                    </label>
                    <div class="col-md-4">
                        <div class="radio-list">
                            <%if (clientService.isIndividualClientAccepted(moduleID)) {%>
                            <label class="radio-inline">
                                <input type="radio" name="accountType" value="1" autocomplete="off"
                                       data-no-uniform="true" checked/>Individual
                            </label>
                            <%
                                }
                                if (clientService.isCompanyClientAccepted(moduleID)) {
                            %>
                            <label class="radio-inline">
                                <input type="radio" name="accountType" value="2" autocomplete="off"
                                       data-no-uniform="true"/>Company
                            </label>
                            <%} %>
                        </div>
                    </div>
                </div>


                <div class="row form-group">
                    <label class="control-label col-md-3 text-center"> <span class="bold">Corporate client?</span>
                    </label>
                    <div class="col-md-4">
                        <div class="radio-list">


                            <label class="radio-inline">
                                <input type="radio" name="clientDetailsDTO.isCorporate" value="1" autocomplete="off"
                                       data-no-uniform="true"/>Yes
                            </label>

                            <label class="radio-inline">
                                <input type="radio" name="clientDetailsDTO.isCorporate" value="0" autocomplete="off"
                                       data-no-uniform="true" checked/>No
                            </label>


                        </div>
                    </div>
                </div>


                <hr>
                <!-- Individual -->
                <%if (clientService.isIndividualClientAccepted(moduleID)) {%>
                <%@include file="individual-client-form.jsp" %>
                <%} %>

                <!-- Company -->
                <%if (clientService.isCompanyClientAccepted(moduleID)) {%>
                <%@include file="company-client-form.jsp" %>
                <%} %>

                <br>
                <br>
                <br>
                <br>


                <!-- Identification -->
                <div class=row id=identification></div>

                <!-- Address -->
                <%@include file="client-address-form.jsp" %>

                <!-- Login Information -->
                <%if ((!(loginDTO.getAccountID() > 0))) {%>
                <jsp:include page="client-login-info-form.jsp" flush="true">
                    <jsp:param name="loginNameEditability" value="true"/>
                    <jsp:param name="captcha" value="false"/>
                </jsp:include>
                <%}%>

                <!-- Contact Details -->
                <div>
                    <br>
                    <hr>
                    <br>
                    <h3 class="form-section">Contact Information</h3>
                    <div class="row">
                        <div class="col-md-4 col-sm-6 contact-info">
                            <%@include file="../client/clientAddBody_billingContact.jsp" %>
                        </div>

                        <div class="col-md-4 col-sm-6 contact-info">
                            <%@include file="../client/clientAddBody_AdminContact.jsp" %>
                        </div>

                        <div class="col-md-4 col-sm-6 contact-info">
                            <%@include file="../client/clientAddBody_TechnicalContact.jsp" %>
                        </div>
                    </div>
                </div>
                <h6><b>Valid File Extension: jpg/png/pdf</b></h6>
                <h6><b>Max File Size: <%=fileConfiguration.getMAXIMUM_FILE_SIZE() / (1024 * 1024)%>MB</b></h6>
                <%@include file="identityTypeFileUploadForAdd.jsp" %>

                <%--<jsp:include page="../../common/fileListHelper.jsp" flush="true">--%>
                    <%--<jsp:param name="entityTypeID" value="<%=7 * EntityTypeConstant.MULTIPLIER2 + 51%>"/>--%>
                    <%--<jsp:param name="entityID" value="<%=id%>"/>--%>
                <%--</jsp:include>--%>


            </div>

            <div class="form-actions">
                <div class="row">
                    <div class="col-md-offset-4 col-md-8">
                        <input type=hidden id="clientCategoryType" name="clientDetailsDTO.clientCategoryType"
                               value="1"/>
                        <input type="hidden" name="clientDetailsDTO.moduleID" value="<%=moduleID%>">
                        <input type="hidden" name="registrantContactDetails.detailsType"
                               value="<%=ClientConstants.DETAILS_TYPE_REGISTRANT%>"/>
                        <input type=hidden name="registrantContactDetails.phoneNumber"/>
                        <input type=hidden name="registrantContactDetails.isEmailVerified"/>
                        <input type=hidden name="registrantContactDetails.isPhoneNumberVerified"/>


                        <button type="reset" class="btn btn-reset-btcl">Reset</button>
                        <button type="button" class="btn btn-submit-btcl" id=formSubmitButton>Submit</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>

<!-- Client -->
<script>var moduleID = <%=moduleID%>;
var fileUploadURL = "<%=request.getContextPath()%>/JqueryFileUpload"</script>
<script src="${context}/assets/scripts/client/client-add-validation.js"></script>
<script src="${context}/assets/scripts/client/client-contact-copy.js"></script>
<script src="../../assets/scripts/client/client-account-type-utils-add.js"></script>
<script src="../../assets/scripts/common/client-info-copy.js"></script>

<script>

    function submitCallback(data) {
        window.location.href = data.location;
    }

    function validateDocumentUpload() {
        var isAllDocumentsUploaded = true;
        $.each($(".fileType"), function () {
            if ($(this).is(':visible')) {
                var neededDocumentID = $(this).attr("id").split("_")[1];
                var isDocumentUploaded = false;
                $.each($("input[name=documents]"), function () {
                    if ($(this).attr("data-documenttype") == neededDocumentID) {
                        isDocumentUploaded = true;
                    }
                });
                if (!isDocumentUploaded) {
                    var i;
                    var doc;
                    for (i = 0; i < identityTypeIDArray.length; i++) {
                        if (identityTypeIDArray[i].id == neededDocumentID) {
                            doc=identityTypeIDArray[i];
                        }
                    }
                    if(doc.isMandatory)
                        isAllDocumentsUploaded = false;
                }
            }
        });
        if (!isAllDocumentsUploaded) {
            toastr.error("Please upload all mandatory documents");
        }
        return isAllDocumentsUploaded;
    }

    $("#formSubmitButton").click(function (event) {
        if (validateDocumentUpload() && $("#fileupload").valid()) {
            $("#fileupload").find(":input").prop("disabled", false);
            ajax($("#fileupload").attr("action"), $("#fileupload").serialize(), submitCallback, "POST", []);
        }
    });
</script>