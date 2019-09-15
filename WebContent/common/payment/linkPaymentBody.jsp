<%@page import="common.ClientDTO" %>
<%@page import="common.CommonService" %>
<%@page import="common.ModuleConstants" %>
<%@page import="common.StringUtils" %>
<%@page import="common.bill.BillConstants" %>
<%@page import="common.bill.BillDTO" %>
<%@page import="common.bill.BillService" %>
<%@page import="common.payment.PaymentDTO" %>
<%@page import="common.payment.PaymentService" %>
<%@page import="common.payment.constants.PaymentConstants" %>
<%@page import="common.repository.AllClientRepository" %>
<%@page import="login.LoginDTO" %>
<%@page import="request.CommonRequestDTO" %>
<%@page import="request.RequestUtilService" %>
<%@page import="sessionmanager.SessionConstants" %>
<%@page import="util.ServiceDAOFactory" %>
<%@page import="util.TimeConverter" %>
<%@page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="accounting.AccountType" %>
<%@ page import="accounting.AccountingEntryService" %>
<%@ page language="java" %>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%
    String context = "../../.." + request.getContextPath() + "/";
    try {
        int moduleID = Integer.parseInt(request.getParameter("moduleID"));
        LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
        long paymentID = Long.parseLong(request.getParameter("paymentID"));
        PaymentService paymentService = ServiceDAOFactory.getService(PaymentService.class);
        BillService billService = new BillService();
        RequestUtilService requestUtilService = new RequestUtilService();
        PaymentDTO paymentDTO = paymentService.getPaymentDTObyID(paymentID);

        List<BillDTO> billDTOs = billService.getBillIDListByPaymentID(paymentID, moduleID);
        long clientID = billDTOs.get(0).getClientID();

        ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(clientID);
        CommonService commonService = new CommonService();
        AccountingEntryService accountingEntryService = ServiceDAOFactory.getService(AccountingEntryService.class);
        ;

        int entityTypeID = 0;
        double totalPayable = 0;
        double totalVat = 0;
        double totalPayableAmount = 0.0;

%>
<style>
    .itDeduction {
        display: none;
    }

    .md-column {
        width: 12%;
    }

    td {
        text-align: left;
    }

    .lg-column {
        width: 16%;
    }
</style>

<div class="portlet box portlet-btcl">
    <div class="portlet-title portlet-title-btcl">
        <div class="caption">
            <i class="fa fa-link" aria-hidden="true"></i> Bank Payment
        </div>
    </div>
    <div class="portlet-body portlet-body-btcl form">
        <html:form styleId="fileupload" styleClass="form-horizontal" method="post" enctype="multipart/form-data"
                   action="/MultipleBillPayment">
            <input type="hidden" name="actionType" value="approval">
            <input type="hidden" name="ID" value="<%=paymentDTO.getID()%>">

            <div class="form-body">

                <div class="form-group form-md-line-input">
                    <label class="col-md-3 control-label" for="form_control_1">Selected Client

                    </label>
                    <div class="col-md-6">
                        <input id="clientIdStr" class="form-control ui-autocomplete-input" name="clientIdStr"
                               autocomplete="off" type="text" value="<%=clientDTO.getLoginName() %>" readonly>
                        <input id="clientId" class="form-control" name="clientID" type="hidden"
                               value="<%=clientDTO.getClientID()%>">
                        <div class="form-control-focus"></div>
                        <span class="help-block"></span>
                    </div>
                </div>

                <!-- <div class="form-group">
                    <h3 class="text-center">Bill Details </h3>
                </div> -->
                <div class="form-group">
                    <label for="cnName" class="col-sm-3 ">Bill Details</label><br/>
                    <div class="col-sm-12">
                        <table class="table " id="sample_editable_1">
                            <thead>
                            <tr>
                                <th class="md-column">Bill ID</th>
                                <th class="md-column">Bill Type</th>
                                <th class="md-column">Client Name</th>
                                <th class="md-column">BTCL Amount(BDT)</th>
                                <th class="md-column">Vat Amount(BDT)</th>
                                <th class="md-column">Payable Amount(BDT)</th>
                            </tr>
                            </thead>
                            <tbody>
                                    <%

                                    List<BillDTO> billDTOList=new ArrayList<>();
                                    List<BillDTO> multipleBillDTOList=new ArrayList<>();
                                    billDTOList.addAll(billDTOs);
                                    double security = 0;
                                    boolean isSecurityNeeded=false;
                                    for(BillDTO billDTO: billDTOList)
                                    {
                                        if(billDTO.getBillType()==BillConstants.MULTIPLE_MONTH_BILL||
                                        billDTO.getBillType()==BillConstants.FINAL_BILL
                                        ){
                                            billDTOs.remove(billDTO);
                                            multipleBillDTOList.add(billDTO);
                                            if(billDTO.getBillType()==BillConstants.FINAL_BILL){
					                        isSecurityNeeded=true;
					                        security = (accountingEntryService
							                    .getBalanceByClientIDAndAccountID(billDTO.getClientID()
									                , AccountType.SECURITY.getID()));
				                                    }
                                        }
                                    }
                                    for(BillDTO billDTO: billDTOs)
                                    {
                                        String billType = "";
                                        if (billDTO.getBillType() == BillConstants.MONTHLY_BILL_ADVANCED) {
                                            billType = "Monthly Bill Advance";
                                        } else if (billDTO.getBillType() == BillConstants.MANUAL_BILL) {
                                            billType = "Manual Bill";
                                        }
                                        else if (billDTO.getBillType() == BillConstants.MULTIPLE_MONTH_BILL) {
                                            billType = "Multiple Month Bill";
                                        }
                                        else if (billDTO.getBillType() == BillConstants.FINAL_BILL) {
                                            billType = "Final Bill";
                                        }

                                        else {
                                            billType = "Not Applicable";
                                        }

                                        if(billDTO.getBillType()==BillConstants.MULTIPLE_MONTH_BILL
                                       	    ||billDTO.getBillType()==BillConstants.FINAL_BILL)
                                        {

                                        }
                                        else
                                        {
                                            entityTypeID = billDTO.getEntityTypeID() ;
                                            totalPayable += billDTO.getTotalPayable();
                                            totalVat += billDTO.getVAT();
                                            totalPayableAmount += billDTO.getNetPayable();
                                            CommonRequestDTO cDTO=requestUtilService.getRequestDTOByReqID(billDTO.getReqID());
                                        }
                                       	 	//EntityDTO entityDTO =  commonService.getEntityDTOByEntityIDAndEntityTypeID(billDTO.getEntityTypeID(), billDTO.getEntityID());

                                       	 	//int moduleIDTemp = billDTO.getEntityTypeID() / EntityTypeConstant.MULTIPLIER2;
                                    		String billLink = "";
                                    		/* if( moduleIDTemp == ModuleConstants.Module_ID_VPN )
                                    			billLink = "GetPdfBill.do?method=getPdf&id="; */

                                    		if(billDTO.getBillType() == BillConstants.DEMAND_NOTE) {
                                    		     billType = "Demand Note";
                                    		    billLink = context + "pdf/view/demand-note.do?billId="+billDTO.getID();
                                    		}else if(billDTO.getBillType() == BillConstants.MONTHLY_BILL) {
                                    		    billType = "Monthly Bill";
                                                billLink = context + "pdf/monthly-bill.do?billId="+billDTO.getID() + "&module="+moduleID;
                                    		}

                                       	%>
                            <tr>
                                <td><a target="_blank" href="<%=billLink %>">
                                    <%=billDTO.getID() %>
                                </a></td>

                                <td>
                                    <%=billType %>
                                </td>
                                    <%-- <td> <%=billDTO.getActivationTimeFromStr() %></td> --%>
                                    <%-- <td><%= EntityTypeConstant.entityNameMap.get( billDTO.getEntityTypeID()) %></td>
                                    <td><a href="<%=context+EntityActionGenerator.getAction(cDTO)%>"><%=entityDTO.getName()%></a></td> --%>

                                <td>
                                    <a target="_blank" href="<%=context %>GetClientForView.do?moduleID=<%=moduleID%>&entityID=<%=billDTO.getClientID()%>"><%= AllClientRepository.getInstance().getClientByClientID(billDTO.getClientID()).getLoginName() %>
                                    </a></td>

                                <td><%= billDTO.getTotalPayable()%>
                                </td>
                                <td><%=billDTO.getVAT() %>
                                </td>
                                <td><%= billDTO.getNetPayable()%>
                                </td>
                            </tr>
                                    <%} %>
                            <thead>
                            <%if (isSecurityNeeded) { %>
                            <tr>
                                <th class="md-column"><b>Security Deposit(-)</b></th>

                                <th class="md-column"></th>
                                <th class="md-column"></th>
                                <th class="md-column"><b><%= security%>
                                </b></th>
                                <th class="md-column"></th>
                                <th class="md-column"><b><%= security%>
                                </b></th>
                            </tr>
                            <%} %>
                            <tr>

                                <th class="md-column">
                                    <b>Total</b> (BDT)
                                </th>
                                <th class="md-column"></th>
                                <th class="md-column"></th>
                                <th class="md-column">
                                    <b><%= isSecurityNeeded ? totalPayable - security : totalPayable%></b>
                                </th>
                                <th class="md-column">
                                    <b><%= totalVat%></b>
                                </th>
                                <th class="md-column">
                                    <b><%=isSecurityNeeded ? totalPayableAmount - security : totalPayableAmount%>
                                    </b>
                                </th>
                            </tr>
                            </thead>
                            </tbody>
                        </table>

                        <%if (multipleBillDTOList.size() > 0) { %>

                        <p>The Above bill is part of following Bill: </p>

                        <table class="table " id="sample_editable_2">
                            <thead>
                            <tr>
                                <th class="md-column">Bill ID</th>
                                <th class="md-column">Bill Type</th>
                                <th class="md-column">Client Name</th>
                                <th class="md-column">BTCL Amount(BDT)</th>
                                <th class="md-column">Vat Amount(BDT)</th>
                                <th class="md-column">Payable Amount(BDT)</th>
                            </tr>

                            </thead>
                            <tbody>
                            <%
                                String billLink = "";
                                for (BillDTO billDTO : multipleBillDTOList) {
                                    String billType = "";

                                    if (billDTO.getBillType() == BillConstants.FINAL_BILL) {

                                        billType = "Final Bill";
                                        billLink = context + "pdf/view/multiple-bill.do?billId=" + billDTO.getID() + "&module=" + moduleID + "&type=" + 4;
                                    }
                                    if (billDTO.getBillType() == BillConstants.MULTIPLE_MONTH_BILL) {
                                        billType = "Multiple Month Bill";
                                        billLink = context + "pdf/view/multiple-bill.do?billId=" + billDTO.getID() + "&module=" + moduleID + "&type=" + 5;
                                    }
                            %>
                            <tr>
                                <td class="md-column"><a target="_blank" href="<%=billLink %>"><%= billDTO.getID()%>
                                </a></td>
                                <td class="md-column"><%= billType%>
                                </td>
                                <td class="md-column"><a
                                        href="<%=context %>GetClientForView.do?moduleID=<%=moduleID%>&entityID=<%=billDTO.getClientID()%>"><%= AllClientRepository.getInstance().getClientByClientID(billDTO.getClientID()).getLoginName() %>
                                </a></td>
                                <td class="md-column"><%= billDTO.getTotalPayable()%>
                                </td>
                                <td class="md-column"><%= billDTO.getVAT()%>
                                </td>
                                <td class="md-column"><%= billDTO.getNetPayable()%>
                                </td>
                            </tr>
                            <%}%>
                            </tbody>
                            <thead></thead>
                        </table>
                        <%} %>

                        <% %>
                    </div>
                </div>

                <div class="form-group form-md-line-input">
                    <label class="col-md-3 control-label " for="form_control_1"><b>Detail Info of Payment:</b> </label>

                </div>

                <div class="form-group form-md-line-input">
                    <label class="col-md-3 control-label" for="form_control_1">Total BTCL Payment

                    </label>
                    <div class="col-md-6">
                        <input class="form-control" placeholder="" name="paymentAmount" type="text"
                               value="<%= paymentDTO.getBtclAmount()%>"
                               readonly>
                        <div class="form-control-focus"></div>
                        <span class="help-block"></span>
                    </div>
                </div>

                <div class="form-group form-md-line-input">
                    <label class="col-md-3 control-label" for="form_control_1">VAT

                    </label>
                    <div class="col-md-2">
                        <input class="form-control" placeholder="" name="vatAmount" type="text"
                               value="<%= paymentDTO.getVatAmount()%>"
                               readonly>
                        <div class="form-control-focus"></div>
                    </div>
                    <div class="col-sm-4">
                        <%if (paymentDTO.getVatIncluded() == 1) { %>
                        <label class="label label-success">VAT included</label>
                        <%} else { %>
                        <label class="label label-warning">VAT not included</label>
                        <%} %>
                    </div>
                </div>

                    <%--<div class="form-group form-md-line-input">--%>
                    <%--<label class="col-md-3 control-label" for="form_control_1">IT Deduction Amount--%>
                    <%----%>
                    <%--</label>--%>
                    <%--<div class="col-md-6">--%>
                    <%--<input class="form-control" placeholder="" name="itDeduction1" type="text" value="<%= Math.ceil(paymentDTO.getItDeductionAmount())%>" readonly >--%>
                    <%--<div class="form-control-focus"> </div>--%>
                    <%--</div>--%>
                    <%--</div>--%>

                <jsp:include page="../../common/fileListHelper.jsp" flush="true">
                    <jsp:param name="entityTypeID" value="<%=entityTypeID%>"/>
                    <jsp:param name="entityID" value="<%=paymentDTO.getID()%>"/>
                    <jsp:param name="noFileMessage" value="no"/>
                </jsp:include>

                <div class="form-group form-md-line-input">
                    <label class="col-md-3 control-label" for="form_control_1">Total Payable

                    </label>
                    <div class="col-md-6">
                        <input class="form-control" placeholder="" name="payableAmount" type="text"
                               value="<%=  paymentDTO.getPayableAmount()%>"
                               readonly>
                        <div class="form-control-focus"></div>
                    </div>
                </div>

                <!-- <div class="form-group">
                    <h3 class="text-center">Payment Details </h3>
                </div>
     -->
                <%if (paymentDTO.getPaymentGatewayType() == 1) { %>
                <div class="form-group form-md-line-input">
                    <label class="col-md-3 control-label" for="form_control_1">Teletalk Amount

                    </label>
                    <div class="col-md-6">
                        <input class="form-control" placeholder="" name="teletalkAmount" type="text"
                               value="<%= paymentDTO.getPaidAmount() -paymentDTO.getPayableAmount() %>"
                               readonly>
                        <div class="form-control-focus"></div>
                    </div>
                </div>
                <%} %>
                <div class="form-group form-md-line-input">
                    <label class="col-md-3 control-label" for="form_control_1">Total Amount Paid

                    </label>
                    <div class="col-md-6">
                        <input class="form-control" placeholder="" name="payableAmount" type="text"
                               value="<%= paymentDTO.getPaidAmount()%>"
                               readonly>
                        <div class="form-control-focus"></div>
                    </div>
                </div>
                <%if (paymentDTO.getPaymentGatewayType() == 1) { %>
                <div class="form-group form-md-line-input">
                    <label class="col-md-3 control-label" for="form_control_1">Payment Method

                    </label>
                    <div class="col-md-6">
                        <input class="form-control" placeholder="" name="" type="text" value="Teletalk">
                    </div>
                </div>
                <%} else { %>
                <div class="form-group form-md-line-input">
                    <label class="col-md-3 control-label" for="form_control_1">Bank Name

                    </label>
                    <div class="col-md-6">
                        <input class="form-control" placeholder="" name="bankName" type="text"
                               value="<%= paymentDTO.getBankName()%>" readonly>
                        <div class="form-control-focus"></div>
                    </div>
                </div>

                <div class="form-group form-md-line-input">
                    <label class="col-md-3 control-label" for="form_control_1">Branch Name

                    </label>
                    <div class="col-md-6">
                        <input class="form-control" placeholder="" name="bankBranchName" type="text"
                               value="<%=paymentDTO.getBankBranchName() == null ? "" : paymentDTO.getBankBranchName()%>"
                               readonly>
                        <div class="form-control-focus"></div>
                    </div>
                </div>

                <div class="form-group form-md-line-input">
                    <label class="col-md-3 control-label" for="form_control_1">Payment Type

                    </label>
                    <div class="col-md-6">
                        <input class="form-control" placeholder="" name="paymentType1" type="text"
                               value="<%=PaymentConstants.paymentTypeMap.get( paymentDTO.getPaymentType()) == null ? "" : PaymentConstants.paymentTypeMap.get( paymentDTO.getPaymentType())%>"
                               readonly>
                        <div class="form-control-focus"></div>
                    </div>
                </div>

                <div class="form-group form-md-line-input">
                    <label class="col-md-3 control-label" for="form_control_1">Cheque or Pay Order No

                    </label>
                    <div class="col-md-6">
                        <input class="form-control" placeholder="" name="orderNo" type="text"
                               value="<%=paymentDTO.getOrderNo() == null ? "": paymentDTO.getOrderNo() %>" readonly>
                        <div class="form-control-focus"></div>
                    </div>
                </div>
                <%} %>
                <div class="form-group form-md-line-input">
                    <label class="col-md-3 control-label" for="form_control_1">Payment Date

                    </label>
                    <div class="col-md-6">
                        <%if (paymentDTO.getPaymentGatewayType() == 1) { %>
                        <input class="form-control" placeholder="" name="paymentDateStr" type="text"
                               value="<%= TimeConverter.getTimeStringByDateFormat( paymentDTO.getPaymentTime(),"dd/MM/yyyy HH:mm:ss") %>"
                               readonly>
                        <%} else { %>
                        <input class="form-control" placeholder="" name="paymentDateStr" type="text"
                               value="<%= TimeConverter.getTimeStringByDateFormat( paymentDTO.getPaymentTime(),"dd/MM/yyyy") %>"
                               readonly>
                        <%} %>
                        <div class="form-control-focus"></div>
                    </div>
                </div>
                <div class="form-group form-md-line-input">
                    <label class="col-md-3 control-label" for="form_control_1">Verification Message
                    </label>
                    <div class="col-md-6">
                        <input class="form-control" placeholder="" name="verificationMessage" type="text"
                               value="<%=paymentDTO.getVerificationMessage()%>" readonly>
                        <div class="form-control-focus"></div>
                    </div>
                </div>
                <% if (paymentDTO.getPaymentStatus() == PaymentConstants.PAYMENT_STATUS_VERIFIED) { %>
                <div class="form-group ">
                    <label class="col-md-3 control-label" for="form_control_1">Remarks
                    </label>
                    <div class="col-md-6">
                        <textarea class="form-control" name="description"></textarea>
                    </div>
                </div>
                <%} else {%>
                <div class="form-group form-md-line-input">
                    <label class="col-md-3 control-label" for="form_control_1">Verified Message

                    </label>
                    <div class="col-md-6">
                        <input class="form-control" name="description" type="text"
                               value="<%=StringUtils.trim( paymentDTO.getDescription())%>" readonly>
                        <div class="form-control-focus"></div>
                    </div>
                </div>
                <%} %>
            </div>
<%--            <% //todo: need to check effect;--%>
<%--                if (true//paymentDTO.getPaymentStatus()==PaymentConstants.PAYMENT_STATUS_VERIFIED--%>
<%--//                         && loginDTO.getColumnPermission(moduleID * ModuleConstants.MULTIPLIER + 115)--%>
<%--                ) { %>--%>
            <%if(paymentDTO.getPaymentStatus()==BillDTO.PAID_UNVERIFIED
            //                    && loginDTO.getColumnPermission(moduleID * ModuleConstants.MULTIPLIER + 115)
            ){ %>
            <div class="form-actions text-center">
                <input type="hidden" name="moduleID" id="moduleID" value="<%=moduleID%>">
                <button class="btn btn-cancel-btcl" name="action" value="reject" type="submit">Reject</button>
                <button class="btn btn-submit-btcl" name="action" value="approve" type="submit">Verify</button>
            </div>
            <%} %>
        </html:form>
    </div>
</div>

<script src="${context}assets/global/scripts/datatable.js" type="text/javascript"></script>
<script src="${context}assets/global/plugins/datatables/datatables.min.js" type="text/javascript"></script>
<script src="${context}assets/global/plugins/datatables/plugins/bootstrap/datatables.bootstrap.js"
        type="text/javascript"></script>
<script src="${context}assets/scripts/vpn/payment/payment-table-editable.js" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        $('#fileupload').submit(function () {
            if ($("#clientId").val() == '') {
                toastr.error("Please search and select a client");
                return false;
            }
            return true;
        })


    });

</script>

<%} catch (Exception ex) { %>
<div class="alert alert-danger alert-dismissable">
    <button type="button" class="close" data-dismiss="alert" aria-hidden="true"></button>
    <strong>Error</strong> <%=ex.toString() %>
</div>
<%} %>
