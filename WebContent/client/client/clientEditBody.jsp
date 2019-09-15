<%@page import="client.ClientService" %>
<%@page import="login.LoginDTO" %>
<%@page import="sessionmanager.SessionConstants" %>
<%@page import="vpn.clientContactDetails.ClientContactDetailsDTO" %>
<%@page import="vpn.client.ClientDetailsDTO" %>
<%@page import="vpn.client.ClientForm" %>
<%@page import="vpn.client.ClientUpdateChecker" %>
<%@ page import="common.*" %>
<%@ page import="configuration.FileConfiguration" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="java.util.Map" %>
<%@ page import="clientdocument.ClientDocumentService" %>

<%
    String context = "../../.." + request.getContextPath() + "/";
    int moduleID = Integer.parseInt(request.getParameter("moduleID"));
    int id = Integer.parseInt(request.getParameter("entityID"));
    int entityTypeID = moduleID * EntityTypeConstant.MULTIPLIER2 + 51;
    ClientForm clientForm = (ClientForm) request.getAttribute("form");

    ClientContactDetailsDTO registrantContactDetails = clientForm.getRegistrantContactDetails();
    ClientContactDetailsDTO billingContactDetails = clientForm.getBillingContactDetails();
    ClientContactDetailsDTO adminContactDetails = clientForm.getAdminContactDetails();
    ClientDetailsDTO clientDetailsDTO = clientForm.getClientDetailsDTO();
    boolean isCorporate = clientDetailsDTO.isCorporate();

    LoginDTO loginDTO = (login.LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);

    boolean isClientUpdateAllowed = ClientUpdateChecker.isClientAllowedForUpdate(clientForm.getClientDetailsDTO().getCurrentStatus());
    int currentState = clientForm.getClientDetailsDTO().getCurrentStatus();
    boolean clientCanUpdateHimself = ClientUpdateChecker.isClientAllowedForUpdate(currentState);

    ClientService clientService = ServiceDAOFactory.getService(ClientService.class);

    FileConfiguration fileConfiguration = ServiceDAOFactory.getService(UniversalDTOService.class).get(FileConfiguration.class);

    request.getSession().setAttribute("registrantContactDetails", registrantContactDetails);
    Map<Integer, Boolean> map = ServiceDAOFactory.getService(ClientDocumentService.class).getAllDocsWithIsGlobalProperty();
    String billingFirstName = billingContactDetails.getRegistrantsName();

    Gson gson =  new Gson();

    String mapString = gson.toJson(map);
    String billingFirstNameJs = gson.toJson(billingFirstName);
    String regTypeJs = gson.toJson(clientDetailsDTO.getRegistrantType());
    String regCatJs = gson.toJson(clientDetailsDTO.getRegistrantCategory());

%>

<div class="portlet box portlet-btcl">
    <div class="portlet-title">
        <div class=caption><i class="fa fa-user"></i>Client Information Edit (<b><span id="loginName"></span></b>)</div>
    </div>

    <div class="portlet-body form">
        <form id="fileupload" class="form-horizontal" action="<%=request.getContextPath()%>/Client/Update.do"
              method="POST">

            <input type=hidden name="clientDetailsDTO.clientID" value=<%=id%>>
            <input type=hidden name="clientDetailsDTO.id" value="<%=clientDetailsDTO.getId()%>">
            <input type=hidden name="registrantContactDetails.ID" value="<%=registrantContactDetails.getID()%>">
            <input type=hidden name="billingContactDetails.ID" value="<%=billingContactDetails.getID()%>">
            <input type=hidden name="adminContactDetails.ID" value="<%=adminContactDetails.getID()%>">
            <input type=hidden name="technicalContactDetails.ID" value="<%=clientForm.getTechnicalContactDetails().getID()%>">
            <input type=hidden name="clientDetailsDTO.clientCategoryType" id="clientCategoryType">
            <input type=hidden name="clientDetailsDTO.moduleID" value=<%=moduleID%>>
            <input type=hidden name="registrantContactDetails.detailsType"
                   value="<%=ClientConstants.DETAILS_TYPE_REGISTRANT%>"/>
            <input type=hidden name="registrantContactDetails.phoneNumber" value="<%=registrantContactDetails.getPhoneNumber()%>">

            <div class="form-body">

                <%if (loginDTO.getIsAdmin() || isClientUpdateAllowed) { %>
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
                <%}%>
                <%if (loginDTO.getIsAdmin()) { %>
                <div class="row form-group">
                    <label class="control-label col-md-3 text-center"> <span class="bold">Corporate client?</span>
                    </label>
                    <div class="col-md-4">
                        <div class="radio-list">

                            <% if (isCorporate) {%>
                            <label class="radio-inline">
                                <input type="radio" name="clientDetailsDTO.isCorporate" value="1" autocomplete="off"
                                       data-no-uniform="true" checked/>Yes
                            </label>


                            <label class="radio-inline">
                                <input type="radio" name="clientDetailsDTO.isCorporate" value="0" autocomplete="off"
                                       data-no-uniform="true"/>No
                            </label>
                            <%}%>

                            <% if (!isCorporate) {%>
                            <label class="radio-inline">
                                <input type="radio" name="clientDetailsDTO.isCorporate" value="1" autocomplete="off"
                                       data-no-uniform="true"/>Yes
                            </label>


                            <label class="radio-inline">
                                <input type="radio" name="clientDetailsDTO.isCorporate" value="0" autocomplete="off"
                                       data-no-uniform="true" checked/>No
                            </label>
                            <%}%>


                        </div>
                    </div>
                </div>
                <%}%>


                <!-- Individual -->
                <%if (clientService.isIndividualClientAccepted(moduleID)) {%>
                <%@include file="individual-client-form.jsp" %>
                <%} %>

                <!-- Company -->
                <%if (clientService.isCompanyClientAccepted(moduleID)) {%>
                <%@include file="company-client-form-edit.jsp" %>
                <%} %>

                <br>
                <br>
                <br>
                <br>

                <!-- Identification -->
                <div class=row id=identification></div>

                <!-- Address -->
                <%@include file="client-address-form.jsp" %>

                <h3 class="form-section">Contact Information<span><button class="btn btn-link" type=button
                                                                          id=copyAllContactInformationFromRegi>    (Use Registrant Contact Information for All Cases)</button></span>
                </h3>
                <div class="row">
                    <div class="col-md-4 col-sm-6 contact-info">
                        <%@include file="clientAddBody_billingContact.jsp" %>
                    </div>

                    <div class="col-md-4 col-sm-6 contact-info">
                        <%@include file="../client/clientAddBody_AdminContact.jsp" %>
                    </div>

                    <div class="col-md-4 col-sm-6 contact-info">
                        <%@include file="../client/clientAddBody_TechnicalContact.jsp" %>
                    </div>
                </div>
                <h6><b>Valid File Extension: jpg/png/pdf </b></h6>
                <h6><b>Max File Size: <%=fileConfiguration.getMAXIMUM_FILE_SIZE()/(1024*1024)%>MB</b></h6>
                <%@include file="identityTypeFileUploadForUpdate.jsp" %>
            </div>

            <div class="form-actions">
                <div class="row">
                    <div class="col-md-offset-4 col-md-8">
                        <a class="btn btn-cancel-btcl" type="button" href="<%=request.getHeader("referer")%>">Cancel</a>
                        <input type="button" class="btn btn-reset-btcl" value=Reset onClick="window.location.reload()">
                        <button type="button" id=formSubmitButton class="btn btn-submit-btcl">Submit</button>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>


<script type="text/javascript">
    let map = JSON.parse('<%=mapString%>');
    let regTypeJs = JSON.parse('<%=regTypeJs%>');
    let regCatJs = JSON.parse('<%=regCatJs%>');
    let billingFirstNameJs = JSON.parse('<%=billingFirstNameJs%>');
    function isNotAdmin() {
        return <%=!loginDTO.getIsAdmin()%>;
    }

    function isClientNotAllowedForUpdate() {
        return <%=!isClientUpdateAllowed%>;
    }

    function canNotUpdateHimself() {
        return <%=!clientCanUpdateHimself%>;
    }
</script>
<script type="text/javascript">
    var nonEditables = [
        {
            'input': 'text',
            'names': ['registrantContactDetails.registrantsName',
                'registrantContactDetails.registrantsLastName',
                'clientDetailsDTO.loginName'
            ]
        },
        {
            'input': 'checkbox',
            'names': [
                'clientDetailsDTO.regiCategories'
            ]
        },
        {
            'input': 'select',
            'names': [
                'clientDetailsDTO.registrantType'
            ]
        },


    ];

    function editableFalse() {
        $.each(nonEditables, function (index, typeNameObj) {
            switch (typeNameObj['input']) {
                case 'text': {
                    $.each(typeNameObj['names'], function (index, nameAttr) {
                        $('input[name="' + nameAttr + '"]').prop("readonly", true);
                    });
                    break;
                }
                case 'select': {
                    $.each(typeNameObj['names'], function (index, nameAttr) {
                        $('select[name="' + nameAttr + '"]').prop("readonly", true);
                        $('select[name="' + nameAttr + '"]').attr("disabled", 'disabled');
                    });
                    break;
                }
                case 'checkbox': {
                    $.each(typeNameObj['names'], function (index, nameAttr) {
                        $('input[name="' + nameAttr + '"]').prop("readonly", true);
                        $('input[name="' + nameAttr + '"]').attr("disabled", 'disabled');
                    });
                    break;
                }
                case 'radio': {
                    $.each(typeNameObj['names'], function (index, nameAttr) {
                        $('input[name="' + nameAttr + '"]').prop("readonly", true);
                        $('input[name="' + nameAttr + '"]').attr("disabled", 'disabled');
                    });
                    break;
                }
            }
        });
    }

    function disableEdit() {
        if (isNotAdmin() && canNotUpdateHimself()) {
            editableFalse();
        }

        if (isNotAdmin() && isClientNotAllowedForUpdate()) {
            editableFalse();
        }
    }

</script>


<script>var moduleID = <%=moduleID%>;</script>
<script src="<%=request.getContextPath()%>/assets/scripts/client/client-add-validation.js"></script>
<script async=false src="${context}/assets/scripts/client/client-contact-copy.js"></script>
<script src="../../assets/scripts/client/client-account-type-utils-edit.js"></script>
<script src="../../assets/scripts/client/client-form-populate.js"></script>

<script>
    // Global Variables
    var globalVariableToStoreSelectedRegistrantCategory = [];
    var globalVariableToStoreIdentity = "";
    var globalVariableToStoreSelectedRegistrantSubCategory = [];

    $(document).ready(function () {
        ajax(context + 'GetClientFormData.do', {moduleID: <%=moduleID%>, clientID: <%=id%>}, populateForm, "GET", []);

        function getFilesInCopyFromCase(regType, regCategory) {
            let clientId = <%=id%>;

            let url = context + "client-document-type/get-required-docs-with-common-files.do?" +
                "module=" + moduleID + "&type=" +  regType + "&category=" + regCategory + "&prevModule=" + "-1" + "&client=" + clientId;

            $('.star_mark').remove();
            return axios.get(url)
                .then(res=>{
                    if(res.data.responseCode ===1 ){
                        $(".fileType").hide();
                        res.data.payload.docTypes.forEach(item=>{
                            let docType = item.key.docTypeId;
                            // console.log(item );
                            // console.log(docType);
                            if(!item.key.isGlobal){
                                // console.log("Required: " + item.key.name);
                                $("#file_" + docType).css("display", "block");

                                if(item.value){
                                    $("#file_" + docType).find("span").after("<span class='star_mark' style='color:red'>&nbsp;*</span>");
                                }

                            }
                        });
                        let commonFileHTML = "<table class='table table-bordered'><thead><tr><th>Document Type</th> <th>Document Name</th> <th>Size</th></tr></thead><tbody>";
                        res.data.payload.commonFiles.forEach(item=>{
                            commonFileHTML += "<tr>" +
                                "<td> <a href=" + context +"DownloadFile.do?documentID="+ item.docID+">"+item.docTypeName+ "</a></td>" +
                                "<td> " + item.docActualFileName+"</td>" +
                                "<td> " + item.docSizeStr+"</td>" +
                                "</tr>";

                        });
                        commonFileHTML+="</tbody></table>";
                        console.log(commonFileHTML);

                        $("#already-uploaded-docs-container").html(commonFileHTML);
                    }
                }).catch(err=>console.log(err));
        }


        console.info(regTypeJs, regCatJs);

        getFilesInCopyFromCase(regTypeJs, regCatJs);
    });
</script>

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
