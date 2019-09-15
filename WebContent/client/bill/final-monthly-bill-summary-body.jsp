<%@ page import="util.EnglishNumberToWords" %>
<style type="text/css">
    #customarInfoTable, #billPriorInfoTable, #monthBillCalculated {
        /*  width:auto !important;*/
    }

</style>
<div id="monthly-bill-summary" v-cloak="true">
    <btcl-body title="Final bill Summary" subtitle="Final Bill Summary">
        <btcl-portlet>

            <btcl-field title="Module">
                <select v-model="moduleId" class="form-control">
                    <option value="" selected disabled hidden>Select Module</option>
                    <option :value="item.key" v-for="item in modules">{{item.value}}</option>
                </select>
            </btcl-field>
            <btcl-field title="Client">
                <user-search-by-module :client.sync="client" :module="moduleId">Client</user-search-by-module>
            </btcl-field>

            <btcl-field title="Last Payment Date">
                <%--                <btcl-datepicker name='lastDate'  pattern="DD-MM-YYYY"></btcl-datepicker>--%>

                <input data-provide="datepicker" class="form-control " name='lastDate' data-date-format="yyyy-mm-dd"
                       type="text">
            </btcl-field>


            <!-- <btcl-field  title="Type">
                <Select>
                    <option>Type1</option>
                    <option>Type2</option>

                </Select>
            </btcl-field> -->


            <button :disabled="submitClicked" type=button class="btn green-haze btn-block btn-lg" @click="getAllInfo">
                Submit
            </button>
            <%--            <button v-if="app.length<=0" type=button class="btn green-haze btn-block btn-lg" @click="findSummaryOfMultipleMonth">Submit--%>
            <%--            </button>--%>
        </btcl-portlet>
    </btcl-body>

    <div v-if="result" class="col-md-12 col-sm-12 col-xs-12" style="background:white;">

        <div v-if="app.length>0" class="portlet-body" id="multipleReport">
            <div class="col-md-12 col-sm-12 col-xs-12">
                <div class="col-md-12 col-sm-12 col-xs-12">
                    <h1 style="font-size:14px;">Customer Information</h1>
                    <table id="customarInfoTable"
                           class="table table-bordered table-striped table-hover">
                        <tr>
                            <td>Customer Name</td>
                            <td>{{clientDetails.name}}</td>
                            <td>Customer Id</td>
                            <td>{{clientDetails.clientID}}</td>
                        </tr>


                        <tr>
                            <td>Customer Type</td>
                            <td>
                                <span>{{clientDetails.clientType }}</span><span> ({{clientDetails.registrantType}})</span>
                            </td>
                            <td>Customer Category</td>
                            <td><span>{{clientDetails.registrantCategory}}</span></td>
                        </tr>

                        <tr>
                            <td>Contact Info</td>
                            <td>{{clientDetails.contact}}</td>
                            <td style="font-weight: bold">Last Pay Date</td>
                            <td><span>{{lastDate}}</span></td>
                        </tr>


                    </table>
                </div>
            </div>


            <div class="col-md-12 col-sm-12 col-xs-12"
                 id="monthBillCalculated">

                <h1 style="font-size:14px;">Pending Monthly Bills</h1>
                <p style="font-size:14px;font-weight: bold;">N:B : If Security Amount is bigger than final bill,
                    Security amount will be adjusted accordingly:</p>
                <table id="monthBillCalculatedTable"
                       class="table table-bordered table-striped table-hover"
                       style="text-align:right;">

                    <tr>
                        <td>SL.</td>
                        <td>Bill Issue Date</td>
                        <td>BTCL Amount Tk.</td>
                        <td>VAT Amount Tk.</td>
                        <td>Total Amount Tk.</td>
                        <td>Previous Payment Deadline</td>


                    </tr>
                    <tr v-for="(element,index) in app" :key="index">
                        <td>{{index+1}}</td>
                        <td style="text-align:right;">{{new Date(element.generationTime).toDateString()}}</td>

                        <td>{{takaFormat(element.totalPayable)}} Tk</td>
                        <td>{{takaFormat(element.VAT)}} Tk</td>
                        <td>{{takaFormat(element.netPayable)}} Tk</td>
                        <td style="text-align:right;">{{new Date(element.lastPaymentDate).toDateString()}}</td>


                    </tr>
                    <tr>
                        <td colspan="2"><b> Total Amount Tk:</b></td>
                        <td><b>{{takaFormat(grandBtclAmount)}} Tk</b></td>
                        <td><b>{{takaFormat(grandVat)}} Tk</b></td>
                        <td><b>{{takaFormat(grandTotal)}} Tk</b></td>
                        <td></td>
                    </tr>

                    <tr>
                        <td colspan="2"><b> Security Deposit Tk(-):</b></td>
                        <td><b>{{takaFormat(securityDeposit)}} Tk</b></td>
                        <td></td>
                        <td><b>{{takaFormat(securityDeposit)}} Tk</b></td>
                        <td></td>
                    </tr>

                    <tr>
                        <td colspan="2"><b> Sub Total Amount Tk:</b></td>
                        <td v-if="grandBtclAmount>securityDeposit"><b>{{takaFormat(grandBtclAmount-securityDeposit)}}
                            Tk</b></td>
                        <td v-if="securityDeposit> grandBtclAmount"><b>N/A</b></td>
                        <td><b>{{takaFormat(grandVat)}} Tk</b></td>
                        <td v-if="grandTotal>securityDeposit"><b>{{takaFormat(grandTotal-securityDeposit)}} Tk</b></td>
                        <td v-if="securityDeposit> grandTotal"><b>N/A</b></td>
                        <td></td>
                    </tr>
                    <tr>
                        <td colspan="2"><b>Amount In Words:</b></td>

                        <td v-if="grandTotal>securityDeposit" colspan="4" style="text-align: left"><b>{{grandTotalAmount}}</b>
                        </td>
                        <td v-if="securityDeposit>grandTotal" colspan="4" style="text-align: left"><b>Not Applicable</b>
                        </td>

                        <td></td>
                    </tr>


                </table>

            </div>

            <br>
            <btcl-download-btn v-if="this.app.length>0"
                               alignment="center"
                               class_list="btn btn-primary"
                               btn_text="Download"
                               url_method="POST"
                               :url='contextPath + "pdf/view/misc-document.do"'
                               :url_param="{type: 4, params}"
                               :file_name='"final-bill-" + client.key + ".pdf"'
            >
            </btcl-download-btn>


        </div>
    </div>


    <div v-if="finalGenerated" class="modal fade" id="detailinfomodal" role="dialog">
        <div class="modal-dialog modal-lg" style="width: 50%;">
            <!-- Modal local loop selection-->
            <div class="modal-content">
                <div class="modal-body">
                    <p><b> This Client Will be Discontinued for All Service From This Module </b></p>
                    <div v-if="existingConnections.length>0" class="portlet-body">


                        <div class="col-md-12 col-sm-12 col-xs-12">

                            <h1 style="font-size:14px;">Existing Connections That will Be Disabled:</h1>
                            <table
                                    class="table table-bordered table-striped table-hover"
                                    style="text-align:center;">

                                <tr>
                                    <td>SL.</td>
                                    <td>Connection ID</td>
                                    <td>Connection Name</td>
                                    <td>Active From</td>
                                    <td>Bandwidth</td>
                                    <td>Details</td>


                                </tr>
                                <tr v-for="(element,index) in existingConnections" :key="index">
                                    <td>{{index+1}}</td>
                                    <td>{{element.ID}}</td>
                                    <td>{{element.name}}</td>
                                    <td style="text-align:right;">{{new Date(element.validFrom).toDateString()}}</td>
                                    <td>{{element.bandwidth}} Mbps</td>
                                    <td><a :href="'${context}lli/connection/view.do?id='+element.ID" target="_blank">
                                        details </a></td>
                                </tr>

                            </table>

                        </div>

                    </div>
                    <div v-if="existingApplications.length>0" class="portlet-body">


                        <div class="col-md-12 col-sm-12 col-xs-12">

                            <h1 style="font-size:14px;">Existing Application That will Be Terminated:</h1>
                            <table
                                    class="table table-bordered table-striped table-hover"
                                    style="text-align:center;">

                                <tr>
                                    <td>SL.</td>
                                    <td>Application ID</td>
                                    <td style="text-align: center">Application State</td>
                                    <td>Application Date</td>
                                    <td>Details</td>


                                </tr>
                                <tr v-for="(element,index) in existingApplications" :key="index">
                                    <td>{{index+1}}</td>
                                    <td>{{element.applicationID}}</td>
                                    <td>{{element.stateDescription}}</td>
                                    <td style="text-align:right;">{{new Date(element.submissionDate).toDateString()}}
                                    </td>
                                    <td>
                                        <a :href="'${context}lli/application/new-connection-details.do?id='+element.applicationID"
                                           target="_blank"> details </a></td>

                                    <%--                                    <td>{{element.bandwidth}} Mbps</td>--%>
                                </tr>

                            </table>

                        </div>

                    </div>
                    <div v-if="existingReviseApplications.length>0" class="portlet-body">


                        <div class="col-md-12 col-sm-12 col-xs-12">

                            <h1 style="font-size:14px;">Existing Revise Application That will Be Terminated:</h1>
                            <table
                                    class="table table-bordered table-striped table-hover"
                                    style="text-align:center;">

                                <tr>
                                    <td>SL.</td>
                                    <td>Application ID</td>
                                    <td style="text-align: center">Application State</td>
                                    <td>Details</td>


                                </tr>
                                <tr v-for="(element,index) in existingReviseApplications" :key="index">
                                    <td>{{index+1}}</td>
                                    <td>{{element.id}}</td>
                                    <td>{{element.stateDescription}}</td>
                                    <td></td>
                                </tr>

                            </table>

                        </div>

                    </div>
                    <div v-if="existingOwnerApplications.length>0" class="portlet-body">


                        <div class="col-md-12 col-sm-12 col-xs-12">

                            <h1 style="font-size:14px;">Existing Owner Change Application That will Be Terminated:</h1>
                            <table
                                    class="table table-bordered table-striped table-hover"
                                    style="text-align:center;">

                                <tr>
                                    <td>SL.</td>
                                    <td>Application ID</td>
                                    <td>Application State</td>
                                    <td>Application Date</td>
                                    <td>Details</td>


                                </tr>
                                <tr v-for="(element,index) in existingOwnerApplications" :key="index">
                                    <td>{{index+1}}</td>
                                    <td>{{element.id}}</td>
                                    <td>{{element.stateDescription}}</td>
                                    <td>{{new Date(element.submissionDate).toDateString()}}</td>
                                    <td><a :href="'${context}lli/ownershipChange/details.do?id='+element.id"
                                           target="_blank"> details </a></td>
                                </tr>

                            </table>

                        </div>

                    </div>

                </div>
                <div class="modal-footer">
                    <button type=button class="btn btn-success"
                            @click="findSummaryOfMultipleMonth">
                        Agree
                    </button>
                    <button type="button" class="btn btn-danger" data-dismiss="modal">Close
                    </button>
                </div>
            </div>

        </div>
    </div>


    <div v-if="finalVpnGenerated" class="modal fade" id="detailvpninfomodal" role="dialog">
        <div class="modal-dialog modal-lg" style="width: 50%;">
            <!-- Modal local loop selection-->
            <div class="modal-content">
                <div class="modal-body">
                    <p><b> This Client Will be Discontinued for All Service From This Module </b></p>

                    <div v-if="existingVPNLinks.length>0" class="portlet-body">


                        <div class="col-md-12 col-sm-12 col-xs-12">

                            <h1 style="font-size:14px;">Existing Links That will Be Terminated:</h1>
                            <table
                                    class="table table-bordered table-striped table-hover"
                                    style="text-align:center;">

                                <tr>
                                    <td>SL.</td>
                                    <td>Link ID</td>
                                    <td style="text-align: center">Link Name</td>
                                    <td>Active From</td>
                                    <td>Details</td>


                                </tr>
                                <tr v-for="(element,index) in existingVPNLinks" :key="index">
                                    <td>{{index+1}}</td>
                                    <td>{{element.id}}</td>
                                    <td>{{element.linkName}}</td>
                                    <td style="text-align:right;">{{new Date(element.activeFrom).toDateString()}}</td>
                                    <td><a :href="'${context}vpn/network/details.do?id='+element.historyId"
                                           target="_blank"> details </a></td>

                                    <%--                                    <td>{{element.bandwidth}} Mbps</td>--%>
                                </tr>

                            </table>

                        </div>

                    </div>


                    <div v-if="existingVPNAppLinks.length>0" class="portlet-body">


                        <div class="col-md-12 col-sm-12 col-xs-12">

                            <h1 style="font-size:14px;">Existing Application Links That will Be Terminated:</h1>
                            <table
                                    class="table table-bordered table-striped table-hover"
                                    style="text-align:center;">

                                <tr>
                                    <td>SL.</td>
                                    <td>Link ID</td>
                                    <td>Application ID</td>
                                    <td style="text-align: center">Link Name</td>
                                    <td>Created Date</td>
                                    <td>Details</td>


                                </tr>
                                <template v-for="eachapp in existingVPNAppLinks" >
                                    <tr v-for="(element,index) in eachapp.vpnApplicationLinks" :key="index">
                                        <td>{{index+1}}</td>
                                        <td>{{element.id}}</td>
                                        <td>{{eachapp.applicationId}}</td>
                                        <td>{{element.linkName}}</td>
                                        <td >{{new Date(eachapp.submissionDate).toDateString()}}</td>
                                        <td><a :href="'${context}vpn/link/details.do?id='+eachapp.applicationId+'&type=details'"
                                               target="_blank"> details </a></td>

                                        <%--                                    <td>{{element.bandwidth}} Mbps</td>--%>
                                    </tr>
                                </template>

                            </table>

                        </div>

                    </div>

                </div>
                <div class="modal-footer">
                    <button type=button class="btn btn-success"
                            @click="findSummaryOfMultipleMonth">
                        Agree
                    </button>
                    <button type="button" class="btn btn-danger" data-dismiss="modal">Close
                    </button>
                </div>
            </div>

        </div>
    </div>


    <div v-if="finalNixGenerated" class="modal fade" id="detailnixinfomodal" role="dialog">
        <div class="modal-dialog modal-lg" style="width: 50%;">
            <!-- Modal local loop selection-->
            <div class="modal-content">
                <div class="modal-body">
                    <p><b> This Client Will be Discontinued for All Service From This Module </b></p>
                    <div v-if="existingNixConnections.length>0" class="portlet-body">


                        <div class="col-md-12 col-sm-12 col-xs-12">

                            <h1 style="font-size:14px;">Existing Connections That will Be Disabled:</h1>
                            <table
                                    class="table table-bordered table-striped table-hover"
                                    style="text-align:center;">

                                <tr>
                                    <td>SL.</td>
                                    <td>Connection ID</td>
                                    <td>Connection Name</td>
                                    <td>Active From</td>
                                    <td>Details</td>


                                </tr>
                                <tr v-for="(element,index) in existingNixConnections" :key="index">
                                    <td>{{index+1}}</td>
                                    <td>{{element.connectionId}}</td>
                                    <td>{{element.name}}</td>
                                    <td style="text-align:right;">{{new Date(element.validFrom).toDateString()}}</td>
                                    <td><a :href="'${context}nix/connection/view.do?id='+element.connectionId" target="_blank">
                                        details </a></td>
                                </tr>

                            </table>

                        </div>

                    </div>
                    <div v-if="existingNixApplications.length>0" class="portlet-body">


                        <div class="col-md-12 col-sm-12 col-xs-12">

                            <h1 style="font-size:14px;">Existing Application That will Be Terminated:</h1>
                            <table
                                    class="table table-bordered table-striped table-hover"
                                    style="text-align:center;">

                                <tr>
                                    <td>SL.</td>
                                    <td>Application ID</td>
                                    <td >Application State</td>
                                    <td>Application Date</td>
                                    <td>Port Count</td>
                                    <td>Details</td>


                                </tr>
                                <tr v-for="(element,index) in existingNixApplications" :key="index">
                                    <td>{{index+1}}</td>
                                    <td>{{element.id}}</td>
                                    <td>{{element.stateDescription}}</td>
                                    <td style="text-align:right;">{{new Date(element.submissionDate).toDateString()}}</td>
                                    <td>{{element.portCount}}</td>
                                    <td>
                                        <a :href="'${context}nix/application/new-connection-details.do?id='+element.id"
                                           target="_blank"> details </a></td>

                                    <%--                                    <td>{{element.bandwidth}} Mbps</td>--%>
                                </tr>

                            </table>

                        </div>

                    </div>
                    <div v-if="existingNixReviseApplications.length>0" class="portlet-body">


                        <div class="col-md-12 col-sm-12 col-xs-12">

                            <h1 style="font-size:14px;">Existing Revise Application That will Be Terminated:</h1>
                            <table
                                    class="table table-bordered table-striped table-hover"
                                    style="text-align:center;">

                                <tr>
                                    <td>SL.</td>
                                    <td>Application ID</td>
                                    <td style="text-align: center">Application State</td>
                                    <td>Details</td>


                                </tr>
                                <tr v-for="(element,index) in existingNixReviseApplications" :key="index">
                                    <td>{{index+1}}</td>
                                    <td>{{element.id}}</td>
                                    <td>{{element.stateDescription}}</td>
                                    <td></td>
                                </tr>

                            </table>

                        </div>

                    </div>

                </div>
                <div class="modal-footer">
                    <button type=button class="btn btn-success"
                            @click="findSummaryOfMultipleMonth">
                        Agree
                    </button>
                    <button type="button" class="btn btn-danger" data-dismiss="modal">Close
                    </button>
                </div>
            </div>

        </div>
    </div>


    <div v-if="finalColocGenerated" class="modal fade" id="detailcolocinfomodal" role="dialog">
        <div class="modal-dialog modal-lg" style="width: 50%;">
            <!-- Modal local loop selection-->
            <div class="modal-content">
                <div class="modal-body">
                    <p><b> This Client Will be Discontinued for All Service From This Module </b></p>
                    <div v-if="existingCoLocationConnections.length>0" class="portlet-body">


                        <div class="col-md-12 col-sm-12 col-xs-12">

                            <h1 style="font-size:14px;">Existing Connections That will Be Disabled:</h1>
                            <table
                                    class="table table-bordered table-striped table-hover"
                                    style="text-align:center;">

                                <tr>
                                    <td>SL.</td>
                                    <td>Connection ID</td>
                                    <td>Connection Name</td>
                                    <td>Active From</td>
                                    <td>Details</td>


                                </tr>
                                <tr v-for="(element,index) in existingCoLocationConnections" :key="index">
                                    <td>{{index+1}}</td>
                                    <td>{{element.historyID}}</td>
                                    <td>{{element.name}}</td>
                                    <td style="text-align:right;">{{new Date(element.validFrom).toDateString()}}</td>
                                    <td><a :href="'${context}co-location/connection-details.do?id='+element.ID+'&historyID='+element.historyID" target="_blank">
                                        details </a></td>
                                </tr>

                            </table>

                        </div>

                    </div>
                    <div v-if="existingCoLocationApplications.length>0" class="portlet-body">


                        <div class="col-md-12 col-sm-12 col-xs-12">

                            <h1 style="font-size:14px;">Existing Application That will Be Terminated:</h1>
                            <table
                                    class="table table-bordered table-striped table-hover"
                                    style="text-align:center;">

                                <tr>
                                    <td>SL.</td>
                                    <td>Application ID</td>
                                    <td >Application State</td>
                                    <td>Application Date</td>
                                    <td>Details</td>


                                </tr>
                                <tr v-for="(element,index) in existingCoLocationApplications" :key="index">
                                    <td>{{index+1}}</td>
                                    <td>{{element.applicationID}}</td>
                                    <td>{{element.stateDescription}}</td>
                                    <td style="text-align:right;">{{new Date(element.submissionDate).toDateString()}}</td>
                                    <td>
                                        <a :href="'${context}co-location/new-connection-application-details-full.do?id='+element.applicationID"
                                           target="_blank"> details </a></td>

                                    <%--                                    <td>{{element.bandwidth}} Mbps</td>--%>
                                </tr>

                            </table>

                        </div>

                    </div>

                </div>
                <div class="modal-footer">
                    <button type=button class="btn btn-success"
                            @click="findSummaryOfMultipleMonth">
                        Agree
                    </button>
                    <button type="button" class="btn btn-danger" data-dismiss="modal">Close
                    </button>
                </div>
            </div>

        </div>
    </div>


</div>


</div>

<script src=../../assets/month-map-int-str.js></script>

<script src="${context}assets/report/jspdf.debug.js"></script>
<script src="https://unpkg.com/jspdf-autotable"></script>

<script src="${context}client/bill/final-monthly-bill-summary.js" type="module"></script>


<script>
    function exportTableToExcel() {
        var downloadLink;
        var dataType = 'application/vnd.ms-excel';
        var tableSelect = document.getElementById('reportTable');
        var tableHTML = tableSelect.outerHTML.replace(/ /g, '%20');
        var filename = 'excel_data.xls';
        downloadLink = document.createElement("a");
        document.body.appendChild(downloadLink);
        if (navigator.msSaveOrOpenBlob) {
            var blob = new Blob(['\ufeff', tableHTML], {
                type: dataType
            });
            navigator.msSaveOrOpenBlob(blob, filename);
        } else {
            downloadLink.href = 'data:' + dataType + ', ' + tableHTML;
            downloadLink.download = filename;
            downloadLink.click();
        }
    }

    function demoFromHTML() {
        var doc = new jsPDF('l', 'pt', 'a4');
        source = $('#multipleReport')[0];
        var img = new Image();
        img.onload = function () {
            doc.addImage(this, 0, 0, img.width, img.height);
        };
        img.crossOrigin = "";
        img.src = '${context}images/common/BTCL-small-Logo.png';
        doc.setFontSize(25)
        doc.text("BANGLADESH TELECOMUNICATION COMPANY LIMITED", 100, 100);
        doc.autoTable({html: '#customarInfoTable'});
        doc.autoTable({html: '#monthBillCalculatedTable'});
        doc.save('duplicateBill.pdf');
        // specialElementHandlers = {
        //     '#bypassme': function (element, renderer) {
        //         return true
        //     }
        // };
        // margins = {
        //     top: 80,
        //     bottom: 60,
        //     left: 10,
        //     width: 1200
        // };
        // doc.fromHTML(
        //     // source, // HTML string or DOM elem ref.
        //     margins.left, // x coord
        //     margins.top, { // y coord
        //         'width': margins.width, // max width of content on PDF
        //         'elementHandlers': specialElementHandlers
        //     },
        //     function (dispose) {
        //         doc.save('duplicateBill.pdf');
        //     }, margins);
    }
</script>


</div>
