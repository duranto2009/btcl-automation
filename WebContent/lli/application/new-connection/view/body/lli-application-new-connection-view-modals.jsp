<%--modals--%>
<div>
    <btcl-field>
        <div class="container">


            <%--LDGM POP SELECTION--%>
            <jsp:include page="../modals/lli-application-new-connection-view-modal-pop-selection.jsp"/>


            <!-- IFR RESPONSE Modal-->
            <jsp:include page="../modals/lli-application-new-connection-view-modal-ifr-response.jsp"/>


            <!-- Modal vendor selection-->
            <jsp:include page="../modals/lli-application-new-connection-view-modal-vendor-selection.jsp"/>

            <!--Vendor EFR RESPONSE -->
            <jsp:include page="../modals/lli-application-new-connection-view-modal-efr-response.jsp"/>

            <%--payment verification--%>
            <jsp:include page="../modals/lli-application-new-connection-view-modal-payment-verify.jsp"/>


            <%--router switch port selection--%>
            <jsp:include page="../modals/lli-application-new-connection-view-modal-server-room-testing-complete.jsp"/>


            <%--Connection Completion Modal--%>
            <jsp:include page="../modals/lli-application-new-connection-view-modal-connection-complete.jsp"/>


            <%--request to local dgm Modal--%>
            <jsp:include page="../modals/lli-application-new-connection-view-modal-zone-selection.jsp"/>


            <%--start: Office List Modal--%>
            <jsp:include page="../modals/lli-application-new-connection-view-modal-office-selection.jsp"/>

            <%--end: Office List Modal--%>

            <%--work order details length provide--%>
            <jsp:include page="../modals/lli-application-new-connection-view-modal-work-order-details.jsp"/>

            <%--forward advice note--%>
            <jsp:include page="../modals/lli-application-new-connection-view-modal-advice-note.jsp"/>

        </div>
    </btcl-field>
</div>