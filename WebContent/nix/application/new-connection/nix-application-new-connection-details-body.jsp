<style>

    .list-group.inner li {
        border: none;

    }

    .a {
        background: none;
        color: inherit;
        border: none;
        padding: 0;
        font: inherit;
        cursor: pointer;
        outline: inherit;
    }
</style>

<div id="btcl-application" v-cloak="true">
    <%--<div class="row" v-if="loading" style="text-align: center">--%>
    <%--<i class="fa fa-spinner fa-spin fa-5x"></i>--%>
    <%--</div>--%>
    <div class="row">
        <btcl-body title="Application Details" subtitle="" :loader="loading">
            <div class="row" <%--v-if="loading"--%> style="text-align: center">
                <%--<i class="fa fa-spinner fa-spin fa-5x"></i>--%>
            </div>
            <%--Form Elements to show--%>
            <btcl-portlet v-if="!loading">
                <btcl-field v-for="(element,index) in application.formElements" :key="index">
                    <div class="form-group">
                        <div class="row">
                            <label class="col-sm-4" style="text-align: left;">{{element.Label}}</label>
                            <div v-if="element.Label!='Status'" class="col-sm-6 text-center"
                                 style="background:  #f2f1f1;padding:1%">
                                <template><span>{{element.Value}}</span></template>
                            </div>
                            <div v-else class="col-sm-6 text-center" v-bind:style="{ background: application.color}">

                                <span style="color: white;font-weight: bold ;font-size: 18px!important;">{{element.Value}}</span>
                            </div>
                        </div>
                    </div>
                </btcl-field>
                <%--Office List Detail Button--%>
                <btcl-field>
                    <div class="form-group">
                        <div class=row>
                            <label class="col-sm-4 " style="text-align: left;">Connection Address</label>
                            <div class="col-sm-6 text-center" style="background:  #f2f1f1;padding:1%">
                                <a class="form-control"
                                   style="background:  #f2f1f1;text-align: center;color:blue"
                                   v-on:click="showOfficeDetails">
                                    Click Here for More
                                </a>


                            </div>
                        </div>
                    </div>
                </btcl-field>


            </btcl-portlet>

            <%--IFR SHOW--%>
            <div v-if="application.ifr.length>0" class="portlet-title">
                <btcl-portlet>
                    <div>
                        <div class="portlet-title">
                            <div class="caption" align="center" style="background: #fab74d;padding: 10px;">IFR
                                Feasibility Reports
                            </div>
                            <div class="tools"><a href="javascript:;" data-original-title="" title=""
                                                  class="collapse"></a>
                            </div>
                        </div>
                        <div class="table-responsive">
                            <table class="table table-fit table-bordered">
                                <thead>
                                <tr>
                                    <th><p style="text-align: center;">Office Name</p></th>
                                    <th><p style="text-align: center;">POP Name</p></th>
                                    <th><p style="text-align: center;">Submission Date</p></th>
                                    <th><p style="text-align: center;">Replied</p></th>
                                    <th><p style="text-align: center;">Feasibility</p></th>

                                </tr>
                                </thead>
                                <tbody>
                                <tr v-for="(element,index) in application.ifr" :key="index">
                                    <td><p style="text-align: center;">{{element.officeName}}</p></td>
                                    <td><p style="text-align: center;">{{element.popName}}</p></td>
                                    <td><p style="text-align: center;">{{(new
                                        Date(element.submissionDate)).toDateString()}}</p></td>
                                    <td>
                                        <select disabled class="form-control" v-model="element.replied">
                                            <option v-bind:value="0" value="1">No</option>
                                            <option v-bind:value="1" value="2">Yes</option>
                                        </select>
                                    </td>
                                    <td>
                                        <select disabled class="form-control" v-model="element.selected">
                                            <option v-bind:value="0" value="1">Not Possible</option>
                                            <option v-bind:value="1" value="2">Possible</option>
                                        </select>
                                    </td>

                                </tr>

                                </tbody>
                            </table>
                        </div>
                        </table>
                    </div>
                </btcl-portlet>
            </div>


            <%--EFR SHOW--%>
            <div v-if="application.efr.length>0" class="portlet-title">
                <btcl-portlet>
                    <div>
                        <div class="portlet-title">
                            <div class="caption" align="center" style="background: #fab74d;padding: 10px;">EFR
                                Feasibility Reports
                            </div>
                            <div class="tools"><a href="javascript:;" data-original-title="" title=""
                                                  class="collapse"></a>
                            </div>
                        </div>
                        <div class="table-responsive">
                            <table class="table table-fit table-bordered">
                                <thead>
                                <tr>

                                    <th><p style="text-align: center;">Vendor Name</p></th>
                                    <th><p style="text-align: center;">POP Name</p></th>
                                    <th><p style="text-align: center;">Source</p></th>
                                    <th><p style="text-align: center;">Destination</p></th>
                                    <th><p style="text-align: center;">OFC Type</p></th>
                                    <th><p style="text-align: center;">Feasibility</p></th>
                                    <th><p style="text-align: center;">Work Given</p></th>
                                    <th><p style="text-align: center;">Work Completed</p></th>


                                </tr>
                                </thead>


                                <tbody>
                                <tr v-for="(element,index) in application.efr" :key="index">
                                    <td ><p style="text-align: center;">{{element.vendorName}}</p></td>
                                    <td ><p style="text-align: center;">{{element.popName}}</p></td>
                                    <td ><p style="text-align: center;">{{element.source}}</p></td>
                                    <td ><p style="text-align: center;">{{element.destination}}</p></td>
                                    <td >
                                        <select disabled class="form-control" v-model="element.ofcType"
                                                style="margin-top: 7px;">
                                            <option value="0">N/A</option>
                                            <option value="1">Single</option>
                                            <option value="2">Double</option>
                                        </select>
                                    </td>

                                    <td >
                                        <select disabled class="form-control" v-model="element.quotationStatus">
                                            <option v-bind:value="1" value="1">Possible</option>
                                            <option v-bind:value="3" value="2">Not Possible</option>
                                        </select>
                                    </td>
                                    <td >
                                        <select disabled class="form-control" v-model="element.workGiven">
                                            <option v-bind:value="0" value="1">NO</option>
                                            <option v-bind:value="1" value="2">YES</option>
                                        </select>
                                    </td>


                                    <td >
                                        <select disabled class="form-control" v-model="element.workCompleted">
                                            <option v-bind:value="0" value="1">NO</option>
                                            <option v-bind:value="1" value="2">YES</option>
                                        </select>
                                    </td>


                                </tr>

                                </tbody>

                            </table>
                        </div>
                    </div>
                </btcl-portlet>


            </div>

            <%--EFR VENDOR SHOW--%>
            <div v-if="application.efr_vendor.length>0"
                 class="portlet-title">
                <btcl-portlet>
                    <div>
                        <div class="portlet-title">
                            <div class="caption" align="center" style="background: #fab74d;padding: 10px;">EFR</div>
                            <div class="tools"><a href="javascript:;" data-original-title="" title=""
                                                  class="collapse"></a>
                            </div>
                        </div>
                        <div class="form-body">
                            <div class="form-group">
                                <%--<div class="col-md-1"><p style="text-align: center;">Vendor Name</p></div>--%>
                                <div class="col-md-2"><p style="text-align: center;">POP Name</p></div>
                                <div class="col-md-2"><p style="text-align: center;">Source</p></div>
                                <div class="col-md-3"><p style="text-align: center;">Destination</p></div>
                                <div class="col-md-2"><p style="text-align: center;">OFC Type</p></div>
                                <div class="col-md-1"><p style="text-align: center;">Work Given</p></div>
                                <div class="col-md-1"><p style="text-align: center;">Work Completed</p></div>
                                <%--
                                                            <div class="col-md-1"><p style="text-align: center;">Work Deadline</p></div>
                                --%>

                            </div>
                            <hr/>

                            <div class="form-group" v-for="(element,index) in application.efr_vendor" :key="index">
                                <%--<div class="col-md-1"><p style="text-align: center;">{{element.popName}}</p></div>--%>
                                <div class="col-md-2"><p style="text-align: center;">{{element.popName}}</p></div>
                                <div class="col-md-2"><p style="text-align: center;">{{element.source}}</p></div>
                                <div class="col-md-3"><p style="text-align: center;">{{element.destination}}</p></div>
                                <div class="col-md-2">
                                    <select disabled class="form-control" v-model="element.ofcType"
                                            style="margin-top: 7px;">
                                        <option value="0">N/A</option>
                                        <option value="1">Single</option>
                                        <option value="2">Double</option>
                                    </select>
                                </div>


                                <div class="col-md-1">
                                    <select disabled class="form-control" v-model="element.workGiven">
                                        <option v-bind:value="0" value="1">NO</option>
                                        <option v-bind:value="1" value="2">YES</option>
                                    </select>
                                </div>


                                <div class="col-md-1">
                                    <select disabled class="form-control" v-model="element.workCompleted">
                                        <option v-bind:value="0" value="1">NO</option>
                                        <option v-bind:value="1" value="2">YES</option>
                                    </select>
                                </div>

                                <%-- <div class="col-md-1"><p style="text-align: center;">
                                     {{(new Date(element.workDeadline)).toDateString()}}</p>
                                 </div>--%>


                            </div>
                            <div class="form-group">
                            </div>
                        </div>
                    </div>
                </btcl-portlet>


            </div>
            <btcl-portlet title="Documents" v-if="!loading">
                <div class="row">
                    <div class="col-xs-12">
                        <ul class="list-group">
                            <li class="list-group-item" v-if="an"><a style="color:blue;font-size:large" @click="viewAN">View
                                Advice Note</a></li>
                            <li class="list-group-item" v-if="dn"><a style="color:blue;font-size:large" @click="viewDN">View
                                Demand Note</a></li>
                            <li class="list-group-item" style="font-size:large" v-if="wo.length>0">Work Order
                                <ul class="list-group inner">
                                    <li class="list-group-item" v-for="(o, oIndex) in wo">
                                        <a style="color:blue;font-size:large" @click="viewWO(o)">View Work Order
                                            {{oIndex + 1}}</a></li>
                                </ul>
                            </li>
                        </ul>
                    </div>
                </div>
            </btcl-portlet>
            <div class="modal fade"
                 id="showOfficeDetails"
                 role="dialog">
                <div class="modal-dialog modal-lg"
                     style="width: 50%;">
                    <div class="modal-content">
                        <div class="portlet-title">
                            <div class="caption"
                                 align="center"
                                 style="background: #fab74d;padding: 10px;">
                                <p>Office Details</p>
                            </div>
                            <div class="tools">
                                <a href="javascript:;" data-original-title="" title=""
                                   class="collapse"></a>
                            </div>
                            <div class="modal-body">
                                <div>
                                    <btcl-portlet>
                                        <ul class="list-group"
                                            v-for="(office,officeIndex) in application.officeList">
                                            <li class="list-group-item active"
                                                style="background-color: black">
                                                <b font-size="20px">{{office.name}},
                                                    {{office.address}}</b>
                                            </li>
                                            <li class="list-group-item"
                                                v-for="(loop, loopIndex) in office.loops">
                                                POP {{loopIndex+1}} : {{loop.popName}}
                                            </li>
                                        </ul>
                                    </btcl-portlet>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button"
                                        class="btn btn-danger"
                                        data-dismiss="modal">Close
                                </button>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
            <%--end: Office List Modal--%>

            <%--toDO have to show the new office details --%>


        </btcl-body>
    </div>
</div>

<script>var applicationID = '<%=request.getParameter("id")%>';</script>
<script src="new-connection/nix-application-new-connection-details.js"></script>
