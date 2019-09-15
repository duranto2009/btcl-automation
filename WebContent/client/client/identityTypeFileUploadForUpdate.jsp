<%@page import="client.IdentityTypeConstants" %>
<div class="row">
    <%if (loginDTO.getIsAdmin() || isClientUpdateAllowed) { %>

    <div class="col-md-12">
        <div id="already-uploaded-docs-container">

        </div>
    </div>

    <div class="col-md-12">
        <%
            for (int identityType : IdentityTypeConstants.IdentityTypeName.keySet()) {
                if (identityType != 0) {
        %>
        <div class="row">
            <div id="file_<%=identityType%>" class="col-md-4 fileType"
                 style="padding:0px 10px 0px 10px;margin-bottom:10px;display:none">
                <button class="btn btn-search-btcl fileinput-button" style="width:100%">
                    <i class="fa fa-upload"></i>
                    <span>
                    <%=IdentityTypeConstants.IdentityTypeName.get(identityType)%>
                </span>
                    <input class="jFile" type="file" name="<%=identityType%>">
                </button>
            </div>
        </div>

        <% }
        }%>
        <div class="col-md-9">
            <span class="fileupload-process"></span>
            <div class="col-lg-12 fileupload-progress fade">
                <div class="progress progress-striped active"
                     role="progressbar" aria-valuemin="0" aria-valuemax="100">
                    <div class="progress-bar progress-bar-success"
                         style="width: 0%;"></div>
                </div>
                <div class="progress-extended">&nbsp;</div>
            </div>
        </div>

        <!-- 		<table role="presentation" class="table table-striped margin-top-10"><tbody class="files"></tbody></table> -->

        <jsp:include page="../../common/fileListHelperEdit.jsp" flush="true">
            <jsp:param name="entityTypeID" value="<%=moduleID * EntityTypeConstant.MULTIPLIER2 + 51%>"/>
            <jsp:param name="entityID" value="<%=id%>"/>
        </jsp:include>
    </div>
    <jsp:include page="../../common/ajaxfileUploadTemplate.jsp"/>
    <%} else { %>
    <jsp:include page="../../common/fileListHelper.jsp" flush="true">
        <jsp:param name="entityTypeID" value="<%=moduleID * EntityTypeConstant.MULTIPLIER2 + 51%>"/>
        <jsp:param name="entityID" value="<%=id%>"/>
    </jsp:include>
    <%} %>
</div>

<script>
    // if($("#uploadField").val() == ""){
    //     alert("Attachment Required");
    //     $('#uploadField').focus();
    // }
</script>