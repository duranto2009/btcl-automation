<div id="app" v-cloak="true">
    <btcl-body  title="Client Registration" subtitle="Information">
        <jsp:include page="admin-client-add-registrant-info.jsp"/>
        <jsp:include page="admin-client-add-address.jsp"/>
        <jsp:include page="admin-client-add-contact-info.jsp"/>
        <jsp:include page="admin-client-add-file-upload.jsp"/>
    </btcl-body>
</div>
<script src="${context}client-new/admin-client-add.js" type="module"></script>