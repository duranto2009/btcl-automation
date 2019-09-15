<%@page import="common.ClientDTO" %>
<%@page import="common.EntityDTO" %>
<%@page import="common.bill.BillConstants" %>
<%@page import="common.bill.BillDTO" %>
<%@page import="common.bill.BillSearchService" %>
<%@page import="common.bill.BillService" %>
<%@page import="common.payment.constants.PaymentConstants" %>
<%@page import="common.repository.AllClientRepository" %>
<%@page import="org.apache.log4j.Logger" %>
<%@page import="request.RequestUtilService" %>
<%@page import="java.math.BigDecimal" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.Collection" %>
<%@ page language="java" %>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%

    Logger logger = Logger.getLogger(getClass());
    String context = "../../.." + request.getContextPath() + "/";

    boolean isValidPayment = true;
    String invalidityMsg = "";
    try {

        String[] billIDs = request.getParameterValues("billIDs");
        Collection recordIDs = new ArrayList<Long>();
        ArrayList<BillDTO> billDTOs = null;

        BillService billService = new BillService();
        RequestUtilService requestUtilService = new RequestUtilService();
        BillSearchService billSearchService = new BillSearchService();

        if (billIDs != null) {
            for (int i = 0; i < billIDs.length; i++) {
                recordIDs.add(Long.parseLong(billIDs[i]));
            }
            billDTOs = (ArrayList<BillDTO>) billSearchService.getDTOs(recordIDs);


        } else {
            isValidPayment = false;
            ;
            invalidityMsg = "Invalid bill ID";
        }

        int moduleID = billDTOs.get(0).getEntityTypeID() / 100;

        logger.debug(billDTOs);

        double totalAmount = 0.0;
        double totalVatAmount = 0.0;
        double totalPayableAmount = 0.0;

        long clientID = billDTOs.get(0).getClientID();
        for (BillDTO billDTO : billDTOs) {
            totalAmount += billDTO.getTotalPayable();
            totalVatAmount += billDTO.getVAT();
            totalPayableAmount += billDTO.getNetPayable();
            if (clientID != billDTO.getClientID()) {
                isValidPayment = false;
                invalidityMsg = "All bills should of same client";
                break;
            }
        }
        //totalPayableAmount=totalAmount+totalVatAmount;
        ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(clientID);

        if (isValidPayment) {
%>
<link href="${context}assets/global/plugins/icheck/skins/all.css" rel="stylesheet">
<style>
    .itDeduction {
        display: none;
    }

    .md-column {
        width: 13%;
    }

    td {
        text-align: left;
    }

    .lg-column {
        width: 24%;
    }

    /* .payDraft{
        display: none;
    } */
</style>
<div id="loading" class="row" style="text-align: center">
    <i class="fa fa-spinner fa-spin fa-5x"></i>
</div>
<div class="portlet box portlet-btcl">
    <div class="portlet-title portlet-title-btcl">
        <div class="caption">
            <i class="fa fa-link" aria-hidden="true"></i> Add Bank Payment
        </div>
    </div>
    <div class="portlet-body portlet-body-btcl form">
        <html:form styleId="fileupload" styleClass="form-horizontal"
                   method="post" enctype="multipart/form-data"
                   action="/MultipleBillPayment">

            <%for (int i = 0; i < billIDs.length; i++) {%>
            <input type="hidden" name="billIDs" value="<%=billIDs[i] %>"/>
            <%}%>

            <input type="hidden" name="moduleID" value="<%=moduleID%>">
            <input type="hidden" name="actionType" value="verification">

            <div class="form-body">
                <div class="form-group form-md-line-input">
                    <label class="col-md-3 control-label" for="form_control_1">Selected Client <span class="required"
                                                                                                     aria-required="true">*</span>
                    </label>
                    <div class="col-md-6">

                        <input id="clientIdStr" class="form-control ui-autocomplete-input"
                               name="clientIdStr" autocomplete="off" type="text"
                               value="<%=clientDTO.getLoginName()%>"
                               style="font-weight: bold; color: red;"
                               readonly>

                        <input
                                id="clientId" class="form-control" name="clientID" type="hidden"
                                value="<%=clientDTO.getClientID()%>">

                        <div class="form-control-focus"></div>
                        <span class="help-block">enter your full name</span>
                    </div>
                </div>

                <div class="form-group">
                    <label for="cnName" class="col-sm-3">Bill
                        Details</label>
                    <br/><br/>
                    <div class="col-sm-12">
                        <table class="table table-striped table-hover table-bordered"
                               id="sample_editable_1">
                            <thead>
                            <tr>
                                <th class="md-column">Bill ID</th>
                                <!-- <th class="md-column" >Month</th> -->
                                <th class="lg-column"> Client</th>
                                <th class="md-column">BTCL Amount</th>
                                <th class="md-column">Vat Amount</th>
                                <th class="md-column">Payable Amount</th>
                                <!-- <th class="md-column">Remove</th> -->
                            </tr>
                            </thead>
                            <tbody>
                                    <%if(billDTOs!=null){
									double totalPayable = 0;
									double totalVat = 0;
									//double totalNetPayable = 0;
									EntityDTO entityDTO = null;
                                   	for(BillDTO billDTO: billDTOs){
                                   		totalPayable += billDTO.getTotalPayable();
                                   		totalVat += billDTO.getVAT();
                                   		//totalNetPayable += billDTO.getNetPayable();
                                   		//CommonRequestDTO cDTO=requestUtilService.getRequestDTOByReqID(billDTO.getReqID());
                                   		//entityDTO = new CommonService().getEntityDTOByEntityIDAndEntityTypeID( billDTO.getEntityTypeID(), billDTO.getEntityID() );
                                   		
                                   		//System.out.println("entityDTO " + entityDTO);
                                   		
                                   		//int moduleIDTemp = billDTO.getEntityTypeID() / EntityTypeConstant.MULTIPLIER2;
                                   		String billLink = "";
                                   		/* 
                                   		if( moduleIDTemp == ModuleConstants.Module_ID_VPN )
                                   			billLink = "GetPdfBill.do?method=getPdf&id=";*/
                                   		if(billDTO.getBillType() == BillConstants.DEMAND_NOTE){
                                   			billLink = context + "pdf/view/demand-note.do?billId=" + billDTO.getID();
                                   		}else if(billDTO.getBillType() == BillConstants.MONTHLY_BILL) {
                                   		    billLink = context + "pdf/view/monthly-bill.do?billId=" + billDTO.getID() + "&module=" + moduleID;
                                   		}

                                   	%>
                            <tr>
                                <td><a target="_blank"
                                       href="<%=billLink %>"><%=billDTO.getID() %>
                                </a></td>

                                    <%-- <td> <%=billDTO.getActivationTimeFromStr() %></td> --%>

                                <td>
                                    <a target="_blank" href="<%=context%>GetClientForView.do?moduleID=<%=moduleID%>&entityID=<%=billDTO.getClientID()%>"><%=AllClientRepository.getInstance().getClientByClientID(billDTO.getClientID()).getLoginName() %>
                                    </a>
                                </td>
                                <td><%=billDTO.getTotalPayable()%>
                                </td>
                                <td><%=billDTO.getVAT() %>
                                </td>
                                <td><%=billDTO.getNetPayable() %>
                                </td>
                                    <%-- <td><input type="hidden" value=" <%=billDTO.getID() %>"
                                        name="billIDs" /> <a class="delete" href="javascript:;">
                                            Remove </a></td> --%>
                            </tr>
                                    <%} %>
                            <thead>
                            <tr>
                                <th class="md-column"><b>Total</b> (BDT)</th>
                                <th class="md-column"></th>
                                <th class="md-column"><b><%=totalPayable%>
                                </b></th>
                                <th class="md-column"><b><%= totalVat%>
                                </b></th>
                                <th class="md-column"><b><%= totalPayableAmount%>
                                </b></th>
                            </tr>
                            </thead>
                            </tbody>
                        </table>
                            <%-- <table class="table ">
                                <tbody>
                                    <tr>
                                        <td class="md-column"><b>Total</b> (BDT)</td>
                                        <td class="md-column"></td>
                                        <td class="lg-column"></td>
                                        <td class="md-column"><b><%=Math.ceil(totalPayable)%></b></td>
                                        <td class="md-column"><b><%=Math.ceil(totalVat)%></b></td>
                                        <td class="md-column"><b><%=Math.ceil(totalPayableAmount)%></b></td>
                                        <!-- <td class="md-column"></td> -->
                                    </tr>
                                </tbody>
                            </table> --%>
                        <%} %>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-md-3 control-label" for="form_control_1">Total
                        BTCL Payment (BDT) <span class="required" aria-required="true">*</span>
                    </label>
                    <div class="col-md-6">
                        <input class="form-control" placeholder="" name="btclAmount"
                               type="text" value="<%=totalAmount%>" readonly>
                        <div class="form-control-focus"></div>
                        <span class="help-block"></span>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-3 control-label" for="form_control_1">VAT</label>
                    <div class="col-sm-2">
                        <input type="checkbox" id="vatCheck"/> <input type="hidden"
                                                                      name="vatIncluded" id="vatCheckHidden" value="1"/>
                        <label><span
                                class="box"></span> included </label>
                    </div>
                </div>

                <div class="form-group vat">
                    <label class="col-md-3 control-label" for="form_control_1">VAT Chalan No.
                    </label>
                    <div class="col-md-6">
                        <input class="form-control" placeholder="" name="vatChalan"
                               type="text">
                    </div>
                </div>

                <div class="form-group vat-amount">
                    <label class="col-md-3 control-label" for="form_control_1">VAT Amount (BDT)</label>
                    <div class="col-md-6">
                        <input class="form-control" placeholder="" name="vatAmount"
                               type="number" value="" readonly>
                    </div>
                </div>


                <div class="form-group vat">
                    <label class="control-label col-md-3">VAT Documents</label>
                    <div class="col-md-6">
                        <div class="fileinput fileinput-new" data-provides="fileinput">
                            <div class="input-group input-large">
                                <div
                                        class="form-control uneditable-input input-fixed input-medium"
                                        data-trigger="fileinput">
                                    <i class="fa fa-file fileinput-exists"></i>&nbsp; <span
                                        class="fileinput-filename"> </span>
                                </div>
                                <span class="input-group-addon btn default btn-file"> <span
                                        class="fileinput-new"> Select file </span> <span
                                        class="fileinput-exists"> Change </span> <input type="hidden"><input
                                        name="document" type="file">
								</span> <a href="javascript:;"
                                           class="input-group-addon btn red fileinput-exists"
                                           data-dismiss="fileinput"> Remove </a>
                            </div>
                        </div>
                    </div>
                </div>

                    <%--<div class="form-group">--%>
                    <%--<label class="col-md-3 control-label" for="form_control_1">IT Deduction</label>--%>
                    <%--<div class="col-md-2">--%>
                    <%--<input type="checkbox" id="itDeductionCheck" /> <input type="hidden"--%>
                    <%--name="itDeductionIncluded" id="itDeductionCheckHidden" value="1" /> <label><span--%>
                    <%--class="box"></span> included </label>--%>
                    <%--</div>--%>
                    <%--</div>--%>

                <div class="form-group itDeduction">
                    <label for="" class="col-sm-3 control-label">IT Deduction
                        Chalan No.</label>
                    <div class="col-md-6">
                        <html:text property="itDeductionChalan" styleClass="form-control"></html:text>
                    </div>
                </div>
                <div class="form-group itDeduction">
                    <label for="" class="col-sm-3 control-label">IT Deduction
                        Amount (BDT)</label>
                    <div class="col-md-6">
                        <input type="number" name="itDeductionAmount" class="form-control"/>
                    </div>
                </div>

                <div class="form-group itDeduction">
                    <label class="control-label col-md-3">IT Deduction Documents</label>
                    <div class="col-md-6">
                        <div class="fileinput fileinput-new" data-provides="fileinput">
                            <div class="input-group input-large">
                                <div
                                        class="form-control uneditable-input input-fixed input-medium"
                                        data-trigger="fileinput">
                                    <i class="fa fa-file fileinput-exists"></i>&nbsp; <span
                                        class="fileinput-filename"> </span>
                                </div>
                                <span class="input-group-addon btn default btn-file"> <span
                                        class="fileinput-new"> Select file </span> <span
                                        class="fileinput-exists"> Change </span> <input type="hidden"><input
                                        name="documentIT" type="file">
								</span> <a href="javascript:;"
                                           class="input-group-addon btn red fileinput-exists"
                                           data-dismiss="fileinput"> Remove </a>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-md-3 control-label" for="form_control_1">Total
                        Payable Amount (BDT) <span class="required" aria-required="true">*</span>
                    </label>
                    <div class="col-md-6">
                        <input class="form-control" placeholder="" name="payableAmount"
                               type="text" value="<%=totalPayableAmount%>" readonly>

                    </div>
                </div>

                <div class="form-group ">
                    <label class="col-md-3 control-label" for="form_control_1">Payment
                        Type<span class="required" aria-required="true"> * </span></label>
                    <div class="col-md-6">
                        <div class="md-radio-inline">
                            <div class="md-radio">
                                <input id="payment_type_check" name="paymentType"
                                       value="<%=PaymentConstants.PAYMENT_TYPE_CHECK%>"
                                       class="md-radiobtn" type="radio"> <label
                                    for="payment_type_check"> <span class="inc"></span> <span
                                    class="check"></span> <span class="box"></span> Cheque
                            </label>
                            </div>
                            <div class="md-radio">
                                <input id="payment_type_pay_order" name="paymentType"
                                       value="<%=PaymentConstants.PAYMENT_TYPE_PAYORDER%>"
                                       class="md-radiobtn" type="radio"> <label
                                    for="payment_type_pay_order"> <span class="inc"></span>
                                <span class="check"></span> <span class="box"></span> Pay Order
                            </label>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label for="cnName" class="col-md-3 control-label payOrCheckText">Pay Draft/Cheque No</label>
                    <div class="col-md-6">
                        <input name="orderNo" type="text" class="form-control payDraftNumber"
                               placeholder="Type here to find pay draft/cheque..." disabled/>
                        <input name="payDraftID" type="hidden" class="payDraftID"/>
                    </div>
                </div>

                <div class="form-group">
                    <label for="paidAmount" class="col-md-3 control-label"> Payment Amount (BDT) </label>
                    <div class="col-md-6">
                        <input id="" type="number" class="form-control" placeholder="" name="paidAmount" required>
                    </div>
                </div>

                <div class="payDraft">
                    <!-- populate at run time -->
                    <div class='form-group'>
                        <label class='col-md-3 control-label'>Bank Name
                            <span class="required" aria-required="true"> * </span>
                        </label>
                        <div class='col-md-6 pBankName'
                        >
                            <input id="payDraftBankName" type="text" class="form-control"
                                   placeholder="" name="bankName">
                            <input id="payDraftBankID" type="hidden" name="payDraftBankID">
                        </div>
                    </div>
                    <div class='form-group'>
                        <label class='col-md-3 control-label'>Branch Name
                            <span class="required" aria-required="true"> * </span>
                        </label>

                        <div class='col-md-6 pBranchName'
                        >
                            <input id="" type="text" class="form-control"
                                   placeholder="" name="bankBranchName">
                        </div>
                    </div>
                    <div class='form-group'>
                        <label class='col-md-3 control-label'>Payment Date
                            <span class="required" aria-required="true"> * </span>
                        </label>
                        <div class='col-md-6 pPaymentDate'
                        >
                            <input type="text" name="paymentDateStr"
                                   class="form-control datepicker" required/>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label for="description" class="col-md-3 control-label">Remark</label>

                    <div class="col-md-6">
						<textarea type="text" name="verificationMessage"
                                  class="form-control" rows="3"></textarea>
                    </div>
                </div>

            </div>
            <div class="form-actions text-center">
                <input type="hidden" name="moduleID" id="moduleID"
                       value="<%=moduleID%>">
                <button class="btn btn-reset-btcl" type="reset">Reset</button>
                <button class="btn btn-submit-btcl" id="action" name="action" type="submit">Submit
                    for Approval
                </button>
            </div>
        </html:form>
    </div>
</div>

<script src="${context}assets/global/scripts/datatable.js" type="text/javascript"></script>
<script src="${context}assets/global/plugins/datatables/datatables.min.js" type="text/javascript"></script>
<script src="${context}assets/global/plugins/datatables/plugins/bootstrap/datatables.bootstrap.js"
        type="text/javascript"></script>

<script src="${context}assets/scripts/vpn/payment/payment-table-editable.js" type="text/javascript"></script>
<script src="${context}assets/global/plugins/icheck/icheck.js"></script>
<script type="text/javascript">
    var tid = setInterval(function () {
        if (document.readyState !== 'complete') return;
        clearInterval(tid);
        $("#loading").hide();
    }, 200);
    var isSelectedPayDraftHasEnoughAmount = true;

    function processList(url, formData, callback, type) {
        $.ajax({
            type: typeof type != 'undefined' ? type : "POST",
            url: url,
            data: formData,
            dataType: 'JSON',
            success: function (data) {
                if (data.responseCode == 2) {
                    toastr.error(data.msg);
                } else {
                    if (data.payload.length == 0) {
                        toastr.error("No data found!");
                    } else {
                        callback(data.payload);
                    }
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                toastr.error("Error Code: " + jqXHR.status + ", Type:" + textStatus + ", Message: " + errorThrown);
            },
            failure: function (response) {
                toastr.error(response);
            }
        });
    }

    function getBank(bankDTO) {
        $(".pBankName").html(bankDTO.bankName);
        console.log("bankname: " + bankDTO.bankName);
    }

    function checkPayDraftAmount(payOrderDTO) {
        /*var moduleID = $("input[name=moduleID]").val();
         if(moduleID != payOrderDTO.payDraftModuleID){

             alert("This pay draft is not for vpn");
             return false;
         }*/
        var payableAmount = Math.ceil(parseFloat($('input[name=payableAmount]').val()).toFixed(2));
        var payDraftRemainingAmount = Math.ceil(parseFloat(payOrderDTO.payDraftRemainingAmount).toFixed(2));
        if (payDraftRemainingAmount < payableAmount) {
            isSelectedPayDraftHasEnoughAmount = false;
            alert("There is not enough money in your pay draft.");
            return false;
        }
        isSelectedPayDraftHasEnoughAmount = true;
        return true;


    }

    function populatePayOrder(payOrderDTO) {
        if (!checkPayDraftAmount(payOrderDTO)) {
            return false;
        }

        $(".pBankName").html('');
        $(".pBranchName").html('');
        $(".pPaymentDate").html('');

        var url = context + 'Bank/GetBankByID.do?';
        var param = {};
        param['bankID'] = payOrderDTO.payDraftBankID;

        processList(url, param, getBank, "GET");

        var date = new Date(payOrderDTO.payDraftPaymentDate);
        var dateFormatting = date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getFullYear();

        $(".pBranchName").html(payOrderDTO.payDraftBranchName);
        $(".pPaymentDate").html(dateFormatting);
        return true;

    }

    var table = $('#sample_editable_1');

    function getTotalVat() {
        var currentTotalVat = 0.0;
        table.find('tbody  > tr').each(function (i, row) {
            var vat = parseFloat(row.cells[3].innerHTML);
            currentTotalVat += vat;
        });
        return Math.ceil(currentTotalVat);
    }

    function getTotalAmount() {
        var currentTotal = 0.0;
        table.find('tbody  > tr').each(function (i, row) {
            var amount = parseFloat(row.cells[2].innerHTML);
            currentTotal += amount;
        });
        return Math.ceil(currentTotal);
    }

    function getTotalPayableAmount() {
        var currentTotal = 0.0;
        table.find('tbody  > tr').each(function (i, row) {
            var amount = parseFloat(row.cells[4].innerHTML);
            currentTotal += amount;
        });
        return Math.ceil(currentTotal);
    }

    function getTotalItDeduction(deduction) {
        var currentTotalDeduction = 0.0;
        currentTotalDeduction = parseFloat(deduction);
        return Math.ceil(currentTotalDeduction);
    }

    $(document).ready(function () {
        $('#vatCheck').attr('checked', 'checked');
        $(".vat").css("display", "none");

        $('.datepicker').datepicker({
            format: 'dd/mm/yyyy',
            endDate: '+0d',
            autoclose: true
        });

        $('input[name=vatAmount]').val(Math.ceil(parseFloat(getTotalVat()).toFixed(2)));

        $("#payDraftBankName").autocomplete({
            source: function (request, response) {
                $("#payDraftBankID").val(-1);
                var term = request.term;
                var url = context + 'Bank/Banks.do?';
                var param = {};
                param['payDraftBankName'] = $("#payDraftBankName").val();
                processList(url, param, response, "GET");
            },
            minLength: 1,
            select: function (e, ui) {
                $('#payDraftBankName').val(ui.item.bankName);
                $('#payDraftBankID').val(ui.item.ID);
                return false;
            },
        }).autocomplete("instance")._renderItem = function (ul, data) {
            return $("<li>").append("<a>" + data.bankName + "</a>").appendTo(ul);
        };

        $("input[name=paymentType]").change(function () {
            var checkedValue = $("input[name=paymentType]:checked").val();
            if (checkedValue == 1) {
                $(".payOrCheckText").html("Cheque No.");
                $("input[name=orderNo]").prop("disabled", false);
            } else if (checkedValue == 2) {
                $(".payOrCheckText").html("Pay Order No.");
                $("input[name=orderNo]").prop("disabled", false);
            }
        });

        /* $(".payDraftNumber").autocomplete({
            source: function (request, response) {
                //$("#payDraftBankID").val(-1);
                var term = request.term;
                var url = context + 'PayOrder/GetPayOrderListByPayorderTypeAndPartialMatch.do?';
                var param = {};
                param['payDraftNumber'] = $(".payDraftNumber").val();
                param['payDraftType'] = $("input[name=paymentType]:checked").val();
                processList(url, param, response, "GET");
            },
            minLength: 1,
            select: function (e, ui) {
                if(!populatePayOrder(ui.item)){
                    return false;
                }
                $('.payDraftNumber').val(ui.item.payDraftNumber);
                $('.payDraftID').val(ui.item.ID);
                $('.payDraft').css("display","block");

                return false;
            },
        }).autocomplete("instance")._renderItem = function (ul, data) {
            return $("<li>").append("<a>" + data.payDraftNumber + "</a>").appendTo(ul);
        }; */

        $(".payDraftNumber").on("change", function () {
            $('.payDraftID').val($(this).val());
        });

        $('#itDeductionCheck').change(function () {
            if ($(this).is(':checked')) {
                $('#itDeductionCheckHidden').val(1);
                $(".itDeduction").css("display", "block");
            } else {
                $('#itDeductionCheckHidden').val(0);
                $(".itDeduction").css("display", "none");
            }
        });

        $('input[name=itDeductionAmount]').on('input', calculateDeduction);

        function calculateDeduction() {
            var fixedTotalPayable = Math.ceil(getTotalPayableAmount());
            if ($("#vatCheck").prop('checked') == false) {
                //do something
                fixedTotalPayable = Math.ceil(getTotalAmount());
            }
            var newTotalPayable = fixedTotalPayable;
            /* $('#itDeductionCheckHidden').val(1); */
            var deduction = Math.ceil($(this).val());
            if (!isNaN(deduction) && deduction != "") {
                var deductionAmount = Math.ceil(deduction);
                newTotalPayable = fixedTotalPayable - deductionAmount;
                LOG("netPayable: ()-) " + newTotalPayable);
                $('input[name=payableAmount]').val(Math.ceil(parseFloat(newTotalPayable).toFixed(2)));
                if (newTotalPayable < 0) {
                    $('input[name=itDeductionAmount]').val('');
                    $('input[name=payableAmount]').val(Math.ceil(parseFloat(fixedTotalPayable).toFixed(2)));
                    alert("Total payable amount exception(Never be negative)");
                }

            } else {
                $('input[name=payableAmount]').val(Math.ceil(parseFloat(fixedTotalPayable).toFixed(2)));
            }
        };

        $('#vatCheck').change(function () {
            //alert($('#vatCheck').val());
            //var totalPayable = Math.ceil(parseFloat($('input[name=payableAmount]').val()));
            var tempDeduction = $("input[name=itDeductionAmount]").val();
            //alert('tempDeduction' + tempDeduction);
            if (isNaN(tempDeduction) || tempDeduction === '')
                tempDeduction = 0;

            var itDeductionAmount = Math.ceil(parseFloat(tempDeduction).toFixed(2));

            var totalPayable = getTotalPayableAmount();
            var totalBtclAmount = getTotalAmount();
            var totalVat = getTotalVat();
            if ($(this).is(':checked')) {

                $('#vatCheckHidden').val(1);

                $('input[name=vatAmount]').val(Math.ceil(parseFloat(totalVat).toFixed(2)));

                var netPayable = totalPayable - itDeductionAmount;

                $('input[name=payableAmount]').val(Math.ceil(parseFloat(netPayable).toFixed(2)));
                $(".vat").css("display", "none");
                $(".vat-amount").css("display", "block");
            } else {
                $('#vatCheckHidden').val(0);

                $('input[name=vatAmount]').val(parseFloat("0.0").toFixed(2));
                //totalPayable -= vatAmount;
                var netPayable = totalBtclAmount - itDeductionAmount;
                $('input[name=payableAmount]').val(Math.ceil(parseFloat(netPayable).toFixed(2)));
                $(".vat").css("display", "block");
                $(".vat-amount").css("display", "none");
            }
        });

        $("#action").click(function (event) {
                event.preventDefault();
                var payDraftID = $("input[name=payDraftID]").val();
                if (payDraftID == "") {
                    alert("No pay draft or check is selected");
                } else {
                    //parseFloat($('input[name=payableAmount]').val()).toFixed(2);
                    var payableAmount = Math.ceil(parseFloat($("input[name=payableAmount]").val()).toFixed(2));
                    var availableAmount = Math.ceil(parseFloat($("input[name=paidAmount]").val()).toFixed(2));
                    var bankName = $("input[name=bankName]").val();
                    var bankBranchName = $("input[name=bankBranchName]").val();
                    var bankID = $("input[name=payDraftBankID]").val();
                    var date = $("input[name=paymentDateStr]").val();
                    if (bankName == "") {
                        alert("Please select a bank name");
                    } else if (bankBranchName == "") {
                        alert("Please select a bank branch name");
                    } else if (bankID == "") {
                        alert("Please select a valid bank");
                    } else if (payableAmount != availableAmount) {
                        alert("Amount mismatch.");
                    } else if (date == "") {
                        alert("Select a valid date");
                    } else if (isSelectedPayDraftHasEnoughAmount) {
                        $("#fileupload").submit();
                    } else {
                        alert("Incomplete payment submitted");
                    }
                }
            }
        );

        $('.delete').click(function () {
            var currentDeleteAbleRow = table.find('tbody  > tr').length;
            if (currentDeleteAbleRow < 2) {
                alert("You can not remove last row.");
                return false;
            }
        });
    });

</script>
<%
    }

} catch (Exception ex) {
%>
<div class="alert alert-danger alert-dismissable">
    <button type="button" class="close" data-dismiss="alert" aria-hidden="true"></button>
    <strong>Error</strong> <%=invalidityMsg %>
</div>
<%} %>
