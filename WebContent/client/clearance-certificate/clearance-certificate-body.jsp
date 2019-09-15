<%@ page import="util.EnglishNumberToWords" %>
<style type="text/css">
    table {
        width: auto !important;
    }

    #thirdTable, #thirdTable > tbody > tr > td {
        border: 1px solid black;
    }

    #thirdTable > thead > tr > th {
        border: 1px solid black;
    }
</style>

<div id=clearance-certificate-search>
    <btcl-body title="Clearance Certificate" subtitle="Clearance Certificate">
        <btcl-portlet>

            <btcl-field title="Module">
                <select v-model="moduleId" class="form-control">
                    <option value="" selected disabled hidden>Select Module</option>
                    <option :value="item.key" v-for="item in modules">{{item.value}}</option>
                </select>
            </btcl-field>

            <btcl-field title="Client">

                <user-search-by-module :id_first=false :client.sync="client" :module="moduleId"></user-search-by-module>
            </btcl-field>

            <btcl-field title="From Date">
                <btcl-datepicker :date.sync="fromDate"></btcl-datepicker>
            </btcl-field>

            <btcl-field title="To Date">
                <btcl-datepicker :date.sync="toDate"></btcl-datepicker>
            </btcl-field>


            <btcl-input title="Letter Number" :text.sync="letterNumber"></btcl-input>


            <button type=button class="btn green-haze btn-block btn-lg" @click="findDueBillsOfClient">Submit</button>


            <br>
            <br>

            <div v-if="result" class="container" style="background:white;">
                <div class="col-md-12 col-sm-12 col-xs-12">
                    <div class="col-md-6 col-sm-6 col-xs-6">
                        <h1 style="font-size:14px;">Customer Information</h1>
                        <table id="customarInfoTable"
                               class="table table-bordered table-striped table-hover">
                            <tr>
                                <td>Client Name</td>
                                <td>{{clientDetails.loginName}}</td>
                            </tr>

                            <tr>
                                <td>Client Id</td>
                                <td>{{clientDetails.clientID}}</td>
                            </tr>

                            <tr>
                                <td>Client Type</td>
                                <td>
                                    <span>{{clientDetails.clientType}}</span>
                                    <span>({{clientDetails.registrantType}})</span>
                                    <span>({{clientDetails.registrantCategory}})</span>
                                </td>
                            </tr>

                        </table>
                    </div>
                </div>

                <br>
                <btcl-download-btn v-if="dueBills.length>0"
                                   alignment="center"
                                   class_list="btn btn-primary"
                                   btn_text="Download"
                                   url_method="POST"
                                   :url='contextPath + "pdf/view/misc-document.do"'
                                   :url_param="{type: 3, params}"
                                   :file_name= '"clearance-certificate-" + client.key + "-ref-" + letterNumber + ".pdf"'
                >
                </btcl-download-btn>


                <btcl-table
                        v-if="dueBills.length>0"
                        caption="Due Bills of Client"
                        :columns="headings"
                        :rows="createRow()"
                        :footer="createFooter()"
                >
                </btcl-table>

                <br>

                <div v-if="dueBills.length==0" class="container" style="background:white;">
                    <p align="center" style="color:green; font-size: x-large"> There are no due bills within the selected time range! Your Clearance
                        Certificate is ready.</p>

                    <br>
                    <btcl-download-btn
                                       alignment="center"
                                       class_list="btn btn-primary"
                                       btn_text="Download"
                                       url_method="POST"
                                       :url='contextPath + "pdf/view/misc-document.do"'
                                       :url_param="{type: 2, params}"
                                       :file_name= '"clearance-certificate-no-due-" + client.key + "-ref-" + letterNumber + ".pdf"'
                    >
                    </btcl-download-btn>
                </div>

            </div>

        </btcl-portlet>
    </btcl-body>


</div>
<script src="../assets/month-map-int-str.js"></script>


<script src="../client/clearance-certificate/clearance-certificate.js" type="module"></script>
       

