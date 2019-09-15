<%@ page import="util.EnglishNumberToWords" %>
<style type="text/css">
    #customarInfoTable, #billPriorInfoTable, #monthBillCalculated {
        /*  width:auto !important;*/
    }

</style>
<div id="monthly-bill-summary" v-cloak="true">
    <btcl-body title="Multiple month bill Summary" subtitle="Multiple Month Bill Summary">
        <btcl-portlet>

            <btcl-field title="Module">
                <select v-model="moduleId" class="form-control">
                    <option value="" selected disabled hidden>Select Module</option>
                    <option :value="item.key" v-for="item in modules">{{item.value}}</option>
                </select>
            </btcl-field>

            <btcl-field title="Client ID">
                <user-search-by-module :id_first=true :client.sync="client" :module="moduleId"></user-search-by-module>
            </btcl-field>

            <btcl-field title="Select Month Range">
                <date-picker range type="month" format="YYYY MMMM" v-model="dateRange" width="100%" lang="en" placeholder="Select Month Range"></date-picker>
                <%--<btcl-datepicker-new :range="true" type="month" pattern="YYYY MMMM" :date.sync="fromDate"></btcl-datepicker-new>--%>
            </btcl-field>

            <%--<btcl-field title="To Date">--%>
                <%--<btcl-datepicker :date.sync="toDate"></btcl-datepicker>--%>
            <%--</btcl-field>--%>

            <btcl-field title="Last Payment Date">
<%--                <btcl-datepicker name='lastDate'  pattern="DD-MM-YYYY"></btcl-datepicker>--%>

                <btcl-datepicker pattern="DD-MM-YYYY" :date.sync="lastPayDate"></btcl-datepicker>
                <%--<input data-provide="datepicker" class="form-control " name='lastDate' data-date-format="yyyy-mm-dd" type="text">--%>
            </btcl-field>
            <!-- <btcl-field  title="Type">
                <Select>
                    <option>Type1</option>
                    <option>Type2</option>

                </Select>
            </btcl-field> -->
            <button type=button class="btn green-haze btn-block" @click="findSummaryOfMultipleMonth">Submit
            </button>
        </btcl-portlet>
    </btcl-body>

    <div v-if=" result && app.length>0" class="col-md-12 col-sm-12 col-xs-12" style="background:white;">

        <div  class="portlet-body" id ="multipleReport">
            <div class="col-md-12 col-sm-12 col-xs-12">
                <div class="col-md-12 col-sm-12 col-xs-12">
                    <h1 style="font-size:14px;">Customer Information</h1>
                    <table id="customarInfoTable"
                           class="table table-bordered table-striped table-hover">
                        <tr>
                            <td style="font-weight: bold">Customer Name</td>
                            <td>{{clientDetails.name}}</td>
                            <td style="font-weight: bold" >Customer Id</td>
                            <td >{{clientDetails.clientID}}</td>
                        </tr>

                        <tr>
                            <td style="font-weight: bold">Customer Type</td>
                            <td><span>{{clientDetails.clientType }}</span><span> ({{clientDetails.registrantType}})</span></td>
                            <td style="font-weight: bold">Customer Category</td>
                            <td><span>{{clientDetails.registrantCategory}}</span></td>
                        </tr>


                        <tr>
                            <td style="font-weight: bold">Date From</td>
                            <td><span>{{fromDateString}}</span></td>
                            <td style="font-weight: bold">Date To</td>
                            <td><span>{{toDateString}}</span></td>
                        </tr>

                        <tr>
                            <td >Contact Info</td>
                            <td>{{clientDetails.contact}}</td>
                            <td style="font-weight: bold">Last Pay Date</td>
                            <td><span>{{lastDate}}</span></td>
                        </tr>

                    </table>
                </div>
            </div>
            <div class="col-md-12 col-sm-12 col-xs-12"
                 id="monthBillCalculated">
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
                        <td>
                            <button class="btn green-haze" @click="selectAllElement">Select All</button>
                        </td>

                    </tr>
                    <tr v-for="(element,index) in app" :key="index">
                        <td>{{index+1}}</td>
                        <td style="text-align:right;">{{new Date(element.generationTime).toDateString()}}</td>

                        <td>{{takaFormat(element.totalPayable)}} Tk</td>
                        <td>{{takaFormat(element.VAT)}} Tk</td>
                        <td>{{takaFormat(element.netPayable)}} Tk</td>
                        <td style="text-align:right;">{{new Date(element.lastPaymentDate).toDateString()}}</td>

                        <td>
                            <div class="custom-control custom-checkbox">
                                <input v-model="selectedElement" :id="element.id" :value="element" type="checkbox"
                                       class="custom-control-input">
                                <label class="custom-control-label" :for="element.id"></label>
                            </div>
                        </td>

                    </tr>
                    <tr>
                        <td colspan="2"><b> Total Amount Tk:</b></td>
                        <td><b>{{takaFormat(grandBtclAmount)}} Tk</b></td>
                        <td><b>{{takaFormat(grandVat)}} Tk</b></td>
                        <td><b>{{takaFormat(grandTotal)}} Tk</b></td>
                        <td></td>
                    </tr>
                    <tr>
                        <td colspan="2"><b>Amount In Words:</b></td>

                        <td colspan="4" style="text-align: left"><b>{{grandTotalAmount}}</b></td>

                        <td></td>
                    </tr>


                </table>

            </div>
            <div class="text-center">
                <button type="button" class="btn green-haze btn-block" v-on:click="generateSelected">Generate</button>
            </div>



        </div>
        <%--<div>--%>

            <%--<button class="btn btn-default" onclick="javascript:demoFromHTML();">Export to PDF</button>--%>
        <%--</div>--%>


        <br>
        <btcl-download-btn v-if="app.length>0 && showDownload"
                           alignment="center"
                           class_list="btn btn-primary"
                           btn_text="Download"
                           url_method="POST"
                           :url='contextPath + "pdf/view/misc-document.do"'
                           :url_param="{type: 5, params}"
                           :file_name= '"multiple-duplicate-bill-" + client.key + ".pdf"'
        >
        </btcl-download-btn>


    </div>

</div>

<script src=../../assets/month-map-int-str.js></script>

<script src="${context}assets/report/jspdf.debug.js"></script>
<script src="https://unpkg.com/jspdf-autotable"></script>

<script src="${context}client/bill/multiple-monthly-bill-summary.js" type="module"></script>


<script>
    function demoFromHTML() {
        var doc = new jsPDF('l', 'pt', 'a4');
        source = $('#multipleReport')[0];
        <%--var img=new Image();--%>
        <%--img.onload = function() {--%>
        <%--    doc.addImage(this, 0, 0,img.width,img.height);--%>
        <%--};--%>
        <%--img.crossOrigin = "";--%>
        <%--img.src='${context}images/common/BTCL-small-Logo.png';--%>
        <%--doc.setFontSize(25)--%>
        <%--doc.text("BANGLADESH TELECOMUNICATION COMPANY LIMITED" ,100 ,100);--%>
        doc.autoTable({html: '#basicInfoTable'});
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