<%@page import="common.CommonService" %>
<%@page import="common.EntityDTO" %>
<%@page import="common.ModuleConstants" %>
<%@page import="common.bill.BillConstants" %>
<%@page import="common.bill.BillDTO" %>
<%@page import="common.bill.BillService" %>
<%@page import="common.repository.AllClientRepository" %>
<%@page import="login.LoginDTO" %>
<%@page import="org.apache.log4j.Logger" %>
<%@page import="request.EntityActionGenerator" %>
<%@page import="sessionmanager.SessionConstants" %>
<%@page import="util.ServiceDAOFactory" %>
<%@page import="util.TimeConverter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.Collections" %>
<%@page import="java.util.Comparator" %>
<%--<%@page import="domain.DomainNameDTO" %>--%>
<%@ page language="java" %>
<%
    String url = "bill/search";
    String navigator = SessionConstants.NAV_BILL;
    String context = request.getContextPath() + "/";
    String moduleID = request.getParameter("moduleID");

    Logger logger = Logger.getLogger(getClass());
    int module = Integer.parseInt(moduleID);


    LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
    boolean isAdmin = loginDTO.getIsAdmin();
    boolean isDomain = module == ModuleConstants.Module_ID_DOMAIN;
    boolean isLLI = module == ModuleConstants.Module_ID_LLI;
    boolean isVPN = module == ModuleConstants.Module_ID_VPN;
    boolean isCoLocation = module == ModuleConstants.Module_ID_COLOCATION;
    boolean isNIX = module == ModuleConstants.Module_ID_NIX;


    BillService billService = ServiceDAOFactory.getService(BillService.class);
    CommonService commonService = ServiceDAOFactory.getService(CommonService.class);
%>
<jsp:include page="../../includes/navBill.jsp" flush="true">
    <jsp:param name="url" value="<%=url%>"/>
    <jsp:param name="navigator" value="<%=navigator%>"/>
</jsp:include>


<div class="portlet box">
    <div class="portlet-body">
        <jsp:include page='/common/flushActionStatus.jsp' />
        <form action="<%=context %>MultipleBillPaymentView.do" method="POST" id="billTableForm">

            <input type="hidden" name="method" value="getPdf"/>
            <input type="hidden" name="moduleID" value="<%=moduleID %>"/>
            <%--<%if (isAdmin && (isVPN || isCoLocation || isLLI)) { %>--%>
            <%if ((isVPN || isCoLocation || isLLI || isNIX)) { %>

            <div class="row" style="margin-bottom: 5px;">
                <div class="col-md-12">
                    <!-- <input id="cancelBill" type="submit" class="btn  btn-sm  btn-danger    pull-right billAction" value="Cancel Bill" />
                    <span class="pull-right">&nbsp; &nbsp;</span> -->

                    <!-- 					<input id="billShow" type="submit" class="btn  btn-sm  green-meadow    pull-right billAction" value="Show Bill" /> -->
                    <!-- 					<span class="pull-right">&nbsp; &nbsp;</span> -->

                    <%--<%--%>


                    <%--if (loginDTO.getColumnPermission(module * ModuleConstants.MULTIPLIER + 115) ||--%>
                    <%--loginDTO.getColumnPermission(module * ModuleConstants.MULTIPLIER + 114)) { %>--%>
                    <input id="billPayment" type="submit" class="btn btn-sm btn-success pull-right billAction"
                           value="Pay Bill"/>
                    <span class="pull-right">&nbsp; &nbsp;</span>

                    <%--<input id="billVerify" type="submit" class="btn btn-sm btn-success pull-right billAction"--%>
                           <%--value="Verify Bill"/>--%>
                    <%--<span class="pull-right">&nbsp; &nbsp;</span>--%>
                    <%--<%} --%>
                    <%--%>--%>
                    <%-- <%
                    if( module != ModuleConstants.Module_ID_DOMAIN &&  loginDTO.getColumnPermission( module * ModuleConstants.MULTIPLIER + 194 ))
                    {%>
                        <input id="skipPayment" type="submit" class="btn btn-sm btn-info pull-right billAction" value="Skip Payment" />
                    <%}
                    %> --%>
                </div>
            </div>
            <%} %>

            <div class="table-responsive">
                <table id="billTableID" class="table table-bordered table-striped table-hover">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <%if (isDomain) {%>
                        <th>Entity Name</th>
                        <%} %>

                        <th>Client Name</th>
                        <!--th>Entity Type</th-->
                        <%if (isVPN || isLLI || isNIX) {%>
                        <th>Bill Month</th>
                        <%} else if (isDomain) {%>
                        <th>Bill View</th>
                        <%}%>
                        <th>Bill Type</th>
                        <th>Total Bill (BDT)</th>
                        <th>Last Payment</th>
                        <th>Payment Status</th>
                        <%--<%if (isAdmin && (isVPN || isLLI || isCoLocation)) { %>--%>
                        <%if ((isVPN || isLLI || isCoLocation || isNIX)) { %>
                        <th class="text-center">
                            <input type="checkbox" value="CheckAll" class="group-checkable pull-left"/>
                        </th>
                        <%} %>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_BILL);
                        if (data != null) {
                            Collections.sort(data, new Comparator<BillDTO>() {
                                public int compare(BillDTO o1, BillDTO o2) {
//
                                    return Long.compare(o1.getID(), o2.getID());
                                }
                            });
                            Collections.reverse(data);
                        }
                        BillDTO billDTO;
                        if (data != null) {
                            int size = data.size();
                            for (int i = 0; i < size; i++) {
                                BillDTO row = (BillDTO) data.get(i);
                                try {
                                    //CommonRequestDTO cDTO=billService.getCommonRequestDTOByBillID(row.getID());
                                    EntityDTO entityDTO = commonService.getEntityDTOByEntityIDAndEntityTypeID(row.getEntityTypeID(), row.getEntityID());
                    %>
                    <tr>
                        <td>
                            <%if (isDomain) {%>
                            <a target="_blank"
                               href="<%=context%>GetPdfBill.do?method=getDomainPdf&id=<%=row.getID() %>"><%=row.getID() %>
                            </a>
                            <%}else {%>
                            <%if(row.getBillType() == BillConstants.DEMAND_NOTE){%>
                            <a target="_blank" href=${context }pdf/view/demand-note.do?billId=<%=row.getID() %>><%=row.getID() %>
                            </a>
                            <%}else if (row.getBillType() == BillConstants.MANUAL_BILL) { %>
                            <a target="_blank"
                               href=${context }lli/bill/manual/view.do?id=<%=row.getID() %>><%=row.getID() %>
                            </a>
                            <%} else if (row.getBillType() == BillConstants.MONTHLY_BILL) {%>
                            <a target="_blank"
                               href=${context }pdf/view/monthly-bill.do?billId=<%=row.getID() %>&module=<%=module%>><%=row.getID() %>
                            </a>

                            <%} else if (row.getBillType() == BillConstants.MULTIPLE_MONTH_BILL) {%>
                            <a target="_blank"
                               href=${context }pdf/view/multiple-bill.do?billId=<%=row.getID() %>&module=<%=module%>&type=5><%=row.getID() %>
                            </a>
                            <%} else if (row.getBillType() == BillConstants.FINAL_BILL) {%>
                            <a target="_blank"
                               href=${context }pdf/view/multiple-bill.do?billId=<%=row.getID() %>&module=<%=module%>&type=4><%=row.getID() %>
                            </a>
                            <%} else if (row.getBillType() == BillConstants.YEARLY_BILL) {%>
                            <a target="_blank"
                               href=${context }pdf/view/demand-note.do?billId=<%=row.getID() %>&module=<%=module%>><%=row.getID() %>
                            </a>
                            <%} %>
                            <%} %>
                        </td>
                        <%if (isDomain) { %>
                        <td>
                            <a href="<%=context+EntityActionGenerator.getAction( Integer.parseInt(moduleID), row.getEntityID(), (long)row.getEntityTypeID())%>">
                                <%=entityDTO != null ? entityDTO.getName() : ""%>
                            </a>
                        </td>
                        <%} %>

                        <td><input type="hidden" name="clientID" value="<%=row.getClientID()%>"><a
                                href="<%=context %>GetClientForView.do?moduleID=<%=moduleID %>&entityID=<%=row.getClientID()%>"><%= AllClientRepository.getInstance().getClientByClientID(row.getClientID()).getLoginName() %>
                        </a></td>
                        <%if (isVPN || isLLI || isNIX) {%>
                        <td><%=row.getActivationTimeFromStr() %>
                        </td>
                        <%} else if (isDomain) {%>
                        <td>
                            <a target="_blank"
                               href="<%=context%>GetPdfBill.do?method=getDomainPdf&id=<%=row.getID() %>">View</a>
                        </td>
                        <%}%>
                        <%
                            String billType = "";
                            if (row.getBillType() == BillConstants.DEMAND_NOTE) {
                                billType = "Demand Note";
                            } else if (row.getBillType() == BillConstants.MONTHLY_BILL) {
                                billType = "Monthly";
                            } else if (row.getBillType() == BillConstants.MONTHLY_BILL_ADVANCED) {
                                billType = "Monthly Bill Advance";
                            } else if (row.getBillType() == BillConstants.MANUAL_BILL) {
                                billType = "Manual Bill";
                            }
                            else if (row.getBillType() == BillConstants.MULTIPLE_MONTH_BILL) {
                                billType = "Multiple Month Bill";
                            }
                            else if (row.getBillType() == BillConstants.FINAL_BILL) {
                                billType = "Final Bill";
                            }

                            else {
                                billType = "Demand Note";
                            }
                        %>
                        <td><%=billType %>
                        </td>
                        <td><%=Math.round(row.getNetPayable())%>
                        </td>
                        <td><%= TimeConverter.getTimeStringFromLong(row.getLastPaymentDate()) %>
                        </td>
                        <td>
                            <%if (row.getPaymentStatus() == BillDTO.PAID_VERIFIED) { %>
                            <label class='label label-success'>Paid</label>
                            <%} else if (row.getPaymentStatus() == BillDTO.PAID_UNVERIFIED) { %>
                            <label class='label label-warning'>Paid Unapproved</label>
                            <%} else if (row.getPaymentStatus() == BillDTO.UNPAID) { %>
                            <label class='label label-danger'>Unpaid</label>
                            <%} else if (row.getPaymentStatus() == BillDTO.SKIPPED) {%>
                            <label class='label label-primary'>Skipped</label>
                            <%} %>
                        </td>
                        <%--<%if (isAdmin && (isVPN || isCoLocation || isLLI)) { %>--%>
                        <%if ((isVPN || isCoLocation || isLLI || isNIX)) { %>

<%--                        <%if (row.getPaymentID() == null || row.getPaymentID() == 0) { %>--%>
                        <%if (row.getPaymentStatus()==0) { %>

                        <td class="text-center">
                            <input class="checkboxes" type="checkbox" name="billIDs"
                                   data-client-id="<%=row.getClientID() %>"
                                   value="<%=row.getID() %>" data-bill-type="<%=row.getBillType() %>">
                        </td>
                        <%} else if (row.getPaymentStatus() == 1 && isAdmin && moduleID.equals(ModuleConstants.Module_ID_LLI)) { %>


                        <%--<td class="text-center">--%>
                            <%--<input id="varifybillbox" class="checkboxes" type="checkbox" name="verifybox"--%>
                                   <%--data-bill-id="<%=row.getID() %>"--%>
                                   <%--value="<%=row.getPaymentID() %>" data-bill-type="<%=row.getBillType() %>">--%>
                        <%--</td>--%>

                        <%} else { %>
                        <td class="text-center">N/A</td>
                        <%} %>
                        <%} %>
                    </tr>
                    <%
                            } catch (Exception ex) {
                                logger.fatal(ex);
                            }
                        }
                    } else {%>
                    <tr>
                        <td colspan='15' class="text-center">No record is found</td>
                    </tr>
                    <%} %>
                    </tbody>
                </table>
            </div>
        </form>
    </div>
</div>

<script src="<%=context%>assets/global/plugins/bootbox/bootbox.min.js" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        var table = $("#billTableID");
        var moduleID = "<%=moduleID %>";
        $('.group-checkable', table).change(function () {
            var set = table.find('tbody > tr > td:last-child input[type="checkbox"]');
            var checked = $(this).prop("checked");
            $(set).each(function () {
                $(this).prop("checked", checked);
            });
            $.uniform.update(set);
        });
        $('.billAction').click(function (e) {
            e.preventDefault();
            // debugger;
            var currentForm = $('#billTableForm');
            var selected = false;
            var actionType = $(this).attr("id");
            if (actionType == 'billPayment') {
                // debugger;
                currentForm.attr("action", context + "MultipleBillPaymentView.do");
                currentForm.attr("target", "");
                currentForm.attr("method", "POST");
            } else if (actionType == 'billShow') {

                if (moduleID == "<%=ModuleConstants.Module_ID_VPN %>")
                    currentForm.attr("action", context + "GetPdfBill.do?method=getPdf");
                else if (moduleID == "<%=ModuleConstants.Module_ID_LLI %>")
                    currentForm.attr("action", context + "GetPdfBillLli.do?method=getPdf");
                else if (moduleID == "<%=ModuleConstants.Module_ID_DOMAIN %>")
                    currentForm.attr("action", context + "GetPdfBill.do?method=getDomainPdf");

                else if (moduleID == "<%=ModuleConstants.Module_ID_NIX %>")
                    currentForm.attr("action", context + "GetPdfBill.do?method=getPdf");

                currentForm.attr("target", "_blank");
                currentForm.attr("method", "GET");
            } else if (actionType == 'skipPayment') {
                currentForm.attr("action", context + "MultipleVpnBillSkip.do?moduleID=" + moduleID);
                currentForm.attr("target", "");
                currentForm.attr("method", "POST");
            } else if (actionType == 'cancelBill') {
                currentForm.attr("action", context + "CancelBill.do");
                currentForm.attr("target", "");
                currentForm.attr("method", "POST");
            } else if (actionType == 'billVerify') {

                var selectedSet = table.find('tbody > tr > td:last-child input[type="checkbox"]:checked');

                if (selectedSet.length > 1) {

                    toastr.error("Multiple Demand Note Verify not allowed");
                    return;
                }else{
                    var val = $('input[name=verifybox]:checked').val();
                    // alert(val);
                    var url = context + "common/payment/linkPayment.jsp?paymentID=" + val + "&moduleID=7";
                    // alert(url);

                    window.location.href = url;
                }


                //
                // currentForm.attr("action",url);
                // currentForm.attr("target", "");
                // currentForm.attr("method", "POST");
            }
            else {
                toastr.error(actionType + " is found");
            }
            var set = table.find('tbody > tr > td:last-child input[type="checkbox"]');
            var selectedSet = table.find('tbody > tr > td:last-child input[type="checkbox"]:checked');

            var multipleDemandNote = false;

            $(set).each(function () {
                if ($(this).prop('checked')) {
                    selected = true;
                }
            });

            $(selectedSet).each(function () {

                if ($(this).attr("data-bill-type") == "<%=BillConstants.DEMAND_NOTE%>" && selectedSet.length > 1) {

                    multipleDemandNote = true;
                }
            });

            if (!selected) {
                bootbox.alert("Select  Bill!", function () {
                });
            }
            else if (multipleDemandNote) {

                bootbox.alert("Selecting multiple bill including a Demand Note not allowed", function () {
                });
            }
            else {

                var isModalBoxShown = true;
                var set = $("#billTableID").find('input[name=clientID]');
                var prevClientID = -1;

                $(set).each(function () {
                    var currentClientID = $(this).val();
                    LOG(currentClientID);
                    if ($(this).parent().parent().find('input[type="checkbox"]').parent().hasClass('checked')) {
                        if (prevClientID != -1 && prevClientID != currentClientID) {
                            alert("Multi-Bill payment can not handle different client!");
                            isModalBoxShown = false;
                        }
                        prevClientID = currentClientID;
                    }
                });
                if (isModalBoxShown) {
                    bootbox.confirm("Are you sure?", function (result) {
                        if (result) {
                            currentForm.submit();
                        }
                    });
                }
            }
        });
    })
</script>
