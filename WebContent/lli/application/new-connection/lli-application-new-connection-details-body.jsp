<style>
    
    .list-group.inner li{
        border:none;

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
<%--
<div id="btcl-application" v-cloak="true">

    <btcl-body title="Application Details" subtitle="" :loader="loading">

        &lt;%&ndash;Form Elements to show&ndash;%&gt;
        <btcl-portlet>
            <btcl-field v-for="(element,index) in application.formElements" :key="index">
                <div class="form-group">
                    <div class="row">
                        <label class="col-sm-4" style="text-align: left;">{{element.Label}}</label>
                        <div v-if="element.Label!='Status'" class="col-sm-6 text-center" style="background:  #f2f1f1;padding:1%">
                            <template><span >{{element.Value}}</span></template>
                        </div>
                        <div v-else class="col-sm-6 text-center"  v-bind:style="{ background: application.color}">

                            <span style="color: white;font-weight: bold ;font-size: 18px!important;">{{element.Value}}</span>
                        </div>
                    </div>
                </div>
            </btcl-field>
            &lt;%&ndash;Office List Detail Button&ndash;%&gt;
            <btcl-field>
                <div class="form-group">
                    <div class=row>
                        <label class="col-sm-4 " style="text-align: left;">Connection Address</label>
                        <div class="col-sm-6 text-center" style="background:  #f2f1f1;padding:1%">

                            <a class="form-control"
                               style="background:  #f2f1f1;text-align: center;color:blue"
                               v-on:click="showOfficeDetails"

                            >
                                &lt;%&ndash;<span  style="background-color:#f99a2f;color:white;">{{element.Value}}</span>&ndash;%&gt;
                                &lt;%&ndash;<span v-else>{{element.Value}}</span>&ndash;%&gt;
                                &lt;%&ndash;<button type="button">&ndash;%&gt;
                                Click Here for More
                                &lt;%&ndash;</button>&ndash;%&gt;

                            </a>


                        </div>
                    </div>
                </div>
            </btcl-field>


        </btcl-portlet>

        &lt;%&ndash;IFR SHOW&ndash;%&gt;
        <div v-if="application.ifr.length>0" class="portlet-title">
            <btcl-portlet>
                <div>
                    <div class="portlet-title">
                        <div class="caption" align="center" style="background: #fab74d;padding: 10px;">IFR</div>
                        <div class="tools"><a href="javascript:;" data-original-title="" title="" class="collapse"></a>
                        </div>
                    </div>
                    <div class="form-body">
                        <div class="form-group">
                            <div class="col-md-2"><p style="text-align: center;">Office Name</p></div>
                            <div class="col-md-2"><p style="text-align: center;">POP Name</p></div>
                            <div class="col-md-1"><p style="text-align: center;">Requested Bandwidth(Mbps)</p></div>
                            <div class="col-md-1"><p style="text-align: center;">Avaialble Bandwidth(Mbps)</p></div>
                            <div class="col-md-2"><p style="text-align: center;">Priority</p></div>
                            <div class="col-md-2"><p style="text-align: center;">Submission Date</p></div>
                            <div class="col-md-1"><p style="text-align: center;">Replied</p></div>
                            <div class="col-md-1"><p style="text-align: center;">Feasibility</p></div>

                        </div>
                        <hr/>

                        <div class="form-group" v-for="(element,index) in application.ifr" :key="index">
                            <div class="col-md-2"><p style="text-align: center;">{{element.officeName}}</p></div>
                            <div class="col-md-2"><p style="text-align: center;">{{element.popName}}</p></div>
                            <div class="col-md-1"><p style="text-align: center;">{{element.requestedBW}}</p></div>
                            <div class="col-md-1"><p style="text-align: center;">{{element.availableBW}}</p></div>
                            <div class="col-md-2">
                                <select disabled class="form-control"  v-model="element.priority">
                                    <option disabled value="-1">Select Priority</option>
                                    <option v-bind:value="1" value="1">High</option>
                                    <option v-bind:value="2" value="2">Medium</option>
                                    <option v-bind:value="3" value="3">Moderate</option>
                                </select>
                            </div>
                            <div class="col-md-2"><p style="text-align: center;">{{(new
                                Date(element.submissionDate)).toDateString()}}</p></div>
                            <div class="col-md-1" align="center">
                                <select disabled class="form-control"  v-model="element.isReplied">
                                    <option v-bind:value="0" value="1">No</option>
                                    <option v-bind:value="1" value="2">Yes</option>
                                </select>
                            </div>
                            <div class="col-md-1" align="center">
                                <select disabled class="form-control"  v-model="element.isSelected">
                                    <option v-bind:value="0" value="1">No</option>
                                    <option v-bind:value="1" value="2">Yes</option>
                                </select>
                            </div>


                        </div>
                        <div class="form-group">
                        </div>
                    </div>
                </div>
            </btcl-portlet>
        </div>


        &lt;%&ndash;EFR SHOW&ndash;%&gt;
        <div v-if="application.efr.length>0"  class="portlet-title">
            <btcl-portlet>
                <div>
                    <div class="portlet-title">
                        <div class="caption" align="center" style="background: #fab74d;padding: 10px;">EFR</div>
                        <div class="tools"><a href="javascript:;" data-original-title="" title="" class="collapse"></a>
                        </div>
                    </div>
                    <div class="form-body">
                        <div class="form-group">
                            <div class="col-md-1"><p style="text-align: center;">Vendor Name</p></div>
                            <div class="col-md-1"><p style="text-align: center;">POP Name</p></div>
                            <div class="col-md-2"><p style="text-align: center;">Source</p></div>
                            <div class="col-md-2"><p style="text-align: center;">Destination</p></div>
                            <div class="col-md-1"><p style="text-align: center;">OFC Type</p></div>
                            <div class="col-md-1"><p style="text-align: center;">Quotation Status</p></div>
                            <div class="col-md-1"><p style="text-align: center;">Quotation Deadline</p></div>
                            <div class="col-md-1"><p style="text-align: center;">Work Given</p></div>
                            <div class="col-md-1"><p style="text-align: center;">Work Completed</p></div>
                            <div class="col-md-1"><p style="text-align: center;">Work Deadline</p></div>

                        </div>
                        <hr/>

                        <div class="form-group" v-for="(element,index) in application.efr" :key="index">
                            <div class="col-md-1"><p style="text-align: center;">{{element.vendorName}}</p></div>
                            <div class="col-md-1"><p style="text-align: center;">{{element.popName}}</p></div>
                            <div class="col-md-2"><p style="text-align: center;">{{element.source}}</p></div>
                            <div class="col-md-2"><p style="text-align: center;">{{element.destination}}</p></div>
                            <div class="col-md-1">
                                <select disabled class="form-control" v-model="element.ofcType"
                                        style="margin-top: 7px;">
                                    <option value="0">N/A</option>
                                    <option value="1">Single</option>
                                    <option value="2">Double</option>
                                </select>
                            </div>
                            <div class="col-md-1"><p style="text-align: center;">{{element.quotationStatus}}</p></div>
                            <div class="col-md-1"><p style="text-align: center;">
                                {{(new Date(element.quotationDeadline)).toDateString()}}</p>
                            </div>

                            <div class="col-md-1">
                                <select disabled class="form-control"  v-model="element.workGiven">
                                    <option v-bind:value="0" value="1">NO</option>
                                    <option v-bind:value="1" value="2">YES</option>
                                </select>
                            </div>


                            <div class="col-md-1">
                                <select disabled class="form-control"  v-model="element.workCompleted">
                                    <option v-bind:value="0" value="1">NO</option>
                                    <option v-bind:value="1" value="2">YES</option>
                                </select>
                            </div>

                            <div class="col-md-1"><p style="text-align: center;">
                                {{(new Date(element.workDeadline)).toDateString()}}</p>
                            </div>


                        </div>
                        <div class="form-group">
                        </div>
                    </div>
                </div>
            </btcl-portlet>


        </div>

        &lt;%&ndash;EFR VENDOR SHOW&ndash;%&gt;
        <div v-if="application.efr_vendor.length>0" class="portlet-title">
            <btcl-portlet>
                <div>
                    <div class="portlet-title">
                        <div class="caption" align="center" style="background: #fab74d;padding: 10px;">EFR</div>
                        <div class="tools"><a href="javascript:;" data-original-title="" title="" class="collapse"></a>
                        </div>
                    </div>
                    <div class="form-body">
                        <div class="form-group">
                            &lt;%&ndash;<div class="col-md-1"><p style="text-align: center;">Vendor Name</p></div>&ndash;%&gt;
                            <div class="col-md-2"><p style="text-align: center;">POP Name</p></div>
                            <div class="col-md-2"><p style="text-align: center;">Source</p></div>
                            <div class="col-md-2"><p style="text-align: center;">Destination</p></div>
                            <div class="col-md-1"><p style="text-align: center;">OFC Type</p></div>
                            <div class="col-md-1"><p style="text-align: center;">Quotation Status</p></div>
                            <div class="col-md-1"><p style="text-align: center;">Quotation Deadline</p></div>
                            <div class="col-md-1"><p style="text-align: center;">Work Given</p></div>
                            <div class="col-md-1"><p style="text-align: center;">Work Completed</p></div>
                            <div class="col-md-1"><p style="text-align: center;">Work Deadline</p></div>

                        </div>
                        <hr/>

                        <div class="form-group" v-for="(element,index) in application.efr_vendor" :key="index">
                            &lt;%&ndash;<div class="col-md-1"><p style="text-align: center;">{{element.popName}}</p></div>&ndash;%&gt;
                            <div class="col-md-2"><p style="text-align: center;">{{element.popName}}</p></div>
                            <div class="col-md-2"><p style="text-align: center;">{{element.source}}</p></div>
                            <div class="col-md-2"><p style="text-align: center;">{{element.destination}}</p></div>
                            <div class="col-md-1">
                                <select disabled class="form-control" v-model="element.ofcType"
                                        style="margin-top: 7px;">
                                    <option value="0">N/A</option>
                                    <option value="1">Single</option>
                                    <option value="2">Double</option>
                                </select>
                            </div>
                            <div class="col-md-1"><p style="text-align: center;">{{element.quotationStatus}}</p></div>
                            <div class="col-md-1"><p style="text-align: center;">
                                {{(new Date(element.quotationDeadline)).toDateString()}}</p>
                            </div>

                            <div class="col-md-1">
                                <select disabled class="form-control"  v-model="element.workGiven">
                                    <option v-bind:value="0" value="1">NO</option>
                                    <option v-bind:value="1" value="2">YES</option>
                                </select>
                            </div>


                            <div class="col-md-1">
                                <select disabled class="form-control"  v-model="element.workCompleted">
                                    <option v-bind:value="0" value="1">NO</option>
                                    <option v-bind:value="1" value="2">YES</option>
                                </select>
                            </div>

                            <div class="col-md-1"><p style="text-align: center;">
                                {{(new Date(element.workDeadline)).toDateString()}}</p>
                            </div>


                        </div>
                        <div class="form-group">
                        </div>
                    </div>
                </div>
            </btcl-portlet>


        </div>
        <btcl-portlet title="Documents" v-if="!loading">
            &lt;%&ndash;<div align="center" v-if="an">&ndash;%&gt;
                &lt;%&ndash;<template>&ndash;%&gt;
                    &lt;%&ndash;<button type="button" class="btn green-haze" @click="viewAN">View Advice Note</button>&ndash;%&gt;
                &lt;%&ndash;</template>&ndash;%&gt;
            &lt;%&ndash;</div>&ndash;%&gt;
            &lt;%&ndash;<br><hr><br/>&ndash;%&gt;
            &lt;%&ndash;<div align="center" v-if="dn">&ndash;%&gt;
                &lt;%&ndash;<template>&ndash;%&gt;
                    &lt;%&ndash;<button type="button" class="btn green-haze" @click="viewDN">View Demand Note</button>&ndash;%&gt;
                &lt;%&ndash;</template>&ndash;%&gt;
            &lt;%&ndash;</div>&ndash;%&gt;

            &lt;%&ndash;<br><hr><br/>&ndash;%&gt;
            &lt;%&ndash;<div align="center" v-if="wo.length>0">&ndash;%&gt;
                &lt;%&ndash;<template v-for="(o, oIndex) in wo">&ndash;%&gt;
                    &lt;%&ndash;<button type="button" class="btn green-haze" @click="viewWO(o)">View Work Order {{oIndex + 1}}</button>&nbsp;&ndash;%&gt;
                &lt;%&ndash;</template>&ndash;%&gt;
            &lt;%&ndash;</div>&ndash;%&gt;

                <div class="row">
                    <div class="col-xs-12">
                        <ul class="list-group">

                            &lt;%&ndash;<a class="form-control"&ndash;%&gt;
                               &lt;%&ndash;style="background:  #f2f1f1;text-align: center;color:blue"&ndash;%&gt;
                               &lt;%&ndash;v-on:click="showOfficeDetails"&ndash;%&gt;

                            &lt;%&ndash;>&ndash;%&gt;
                            <li class="list-group-item"  v-if="an"><a style="color:blue;font-size:large" @click="viewAN">View Advice Note</a></li>
                            <li class="list-group-item"  v-if="dn"><a  style="color:blue;font-size:large" @click="viewDN">View Demand Note</a></li>
                            <li class="list-group-item" style="font-size:large" v-if="wo.length>0">Work Order
                                <ul class="list-group inner">
                                    <li class="list-group-item" v-for="(o, oIndex) in wo"><a  style="color:blue;font-size:large" @click="viewWO(o)">View Work Order {{oIndex + 1}}</a></li>
                                    &lt;%&ndash;<li class="list-group-item">Item 2b</li>&ndash;%&gt;
                                </ul>
                            </li>
                            &lt;%&ndash;<li class="list-group-item">Three&ndash;%&gt;
                                &lt;%&ndash;<ul class="list-group inner">&ndash;%&gt;
                                    &lt;%&ndash;<li class="list-group-item">Item 3a</li>&ndash;%&gt;
                                    &lt;%&ndash;<li class="list-group-item">Item 3b</li>&ndash;%&gt;
                                &lt;%&ndash;</ul>&ndash;%&gt;
                            &lt;%&ndash;</li>&ndash;%&gt;
                        </ul>
                    </div>
                </div>


        </btcl-portlet>


        &lt;%&ndash;modals&ndash;%&gt;
        &lt;%&ndash;<jsp:include page="lli-application-new-connection-view-modals.jsp"/>&ndash;%&gt;

        &lt;%&ndash;start: Office List Modal&ndash;%&gt;
        <div class="modal fade" id="showOfficeDetails" role="dialog">
            <div class="modal-dialog modal-lg" style="width: 50%;">
                <div class="modal-content">
                    <div class="portlet-title">
                        <div class="caption" align="center"
                             style="background: #fab74d;padding: 10px;"><p>Office Details</p>
                        </div>
                        <div class="tools">
                            <a href="javascript:;" data-original-title="" title=""
                               class="collapse"></a>
                        </div>
                        <div class="modal-body">
                            <div>
                                <btcl-portlet>
                                    <ul class="list-group" v-for="(office,officeIndex) in application.officeList">
                                        <li class="list-group-item active"
                                            style="background-color: black"><b font-size="20px">{{office.officeName}},
                                            {{office.officeAddress}}</b></li>
                                        <li class="list-group-item" v-for="(loop, loopIndex) in office.loops">
                                            POP {{loopIndex+1}} : {{loop.popName}}
                                        </li>
                                    </ul>
                                </btcl-portlet>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-danger" data-dismiss="modal">Close
                            </button>
                        </div>
                    </div>

                </div>
            </div>
        </div>
        &lt;%&ndash;end: Office List Modal&ndash;%&gt;

        &lt;%&ndash;toDO have to show the new office details &ndash;%&gt;


    </btcl-body>
</div>
--%>
<div id="btcl-application" v-cloak="true">

    <btcl-body title="Application Details" subtitle="" :loader="loading">

        <%--Form Elements to show--%>
        <btcl-portlet>
            <btcl-field v-for="(element,index) in application.formElements" :key="index">
                <div class="form-group">
                    <div class="row">
                        <label class="col-sm-4" style="text-align: left;">{{element.Label}}</label>
                        <div v-if="element.Label!='Status'" class="col-sm-6 text-center" style="background:  #f2f1f1;padding:1%">
                            <template><span >{{element.Value}}</span></template>
                        </div>
                        <div v-else class="col-sm-6 text-center"  v-bind:style="{ background: application.color}">

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
                               v-on:click="showOfficeDetails"
                            >   Click Here for More
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
                        <div class="caption" align="center" style="background: #fab74d;padding: 10px;">IFR</div>
                        <div class="tools"><a href="javascript:;" data-original-title="" title="" class="collapse"></a>
                        </div>
                    </div>
                    <div class="form-body">
                        <div class="form-group">
                            <div class="col-md-2"><p style="text-align: center;">Connection Office Name</p></div>
                            <div class="col-md-2"><p style="text-align: center;">POP Name</p></div>
                            <div class="col-md-1"><p style="text-align: center;">Requested Bandwidth(Mbps)</p></div>
                            <div class="col-md-1"><p style="text-align: center;">Available Bandwidth(Mbps)</p></div>
                            <div class="col-md-2"><p style="text-align: center;">Priority</p></div>
                            <div class="col-md-2"><p style="text-align: center;">Submission Date</p></div>
                            <div class="col-md-1"><p style="text-align: center;">Replied</p></div>
                            <div class="col-md-1"><p style="text-align: center;">Feasibility</p></div>

                        </div>
                        <hr/>

                        <div class="form-group" v-for="(element,index) in application.ifr" :key="index">
                            <div class="col-md-2"><p style="text-align: center;">{{element.officeName}}</p></div>
                            <div class="col-md-2"><p style="text-align: center;">{{element.popName}}</p></div>
                            <div class="col-md-1"><p style="text-align: center;">{{element.requestedBW}}</p></div>
                            <div class="col-md-1"><p style="text-align: center;">{{element.availableBW}}</p></div>
                            <div class="col-md-2">
                                <select disabled class="form-control"  v-model="element.priority">
                                    <option disabled value="-1">Select Priority</option>
                                    <option v-bind:value="1" value="1">High</option>
                                    <option v-bind:value="2" value="2">Medium</option>
                                    <option v-bind:value="3" value="3">Moderate</option>
                                </select>
                            </div>
                            <div class="col-md-2"><p style="text-align: center;">{{(new
                                Date(element.submissionDate)).toDateString()}}</p></div>
                            <div class="col-md-1" align="center">
                                <select disabled class="form-control"  v-model="element.isReplied">
                                    <option v-bind:value="0" value="1">No</option>
                                    <option v-bind:value="1" value="2">Yes</option>
                                </select>
                            </div>
                            <div class="col-md-1" align="center">
                                <select disabled class="form-control"  v-model="element.isSelected">
                                    <option v-bind:value="0" value="1">No</option>
                                    <option v-bind:value="1" value="2">Yes</option>
                                </select>
                            </div>


                        </div>
                        <div class="form-group">
                        </div>
                    </div>
                </div>
            </btcl-portlet>
        </div>


        <%--EFR SHOW--%>
        <div v-if="application.efr.length>0"  class="portlet-title">
            <btcl-portlet>
                <div>
                    <div class="portlet-title">
                        <div class="caption" align="center" style="background: #fab74d;padding: 10px;">EFR</div>
                        <div class="tools"><a href="javascript:;" data-original-title="" title="" class="collapse"></a>
                        </div>
                    </div>
                    <div class="table-responsive">
                        <table class="table table-fit table-bordered">
                        <thead>
                            <tr>
                                <th>Vendor Name</th>
                                <th>POP Name</th>
                                <th>Source</th>
                                <th>Destination</th>
                                <th>OFC Type</th>
                                <th>Quotation Status</th>
                                <th>Quotation Deadline</th>
                                <th>Work Given</th>
                                <th>Work Completed</th>
                                <th>Work Deadline</th>
                                <th>Proposed Length</th>
                                <th>Final Length</th>

                            </tr>
                        </thead>
                        <tbody>
                        <tr v-for="(element,index) in application.efr" :key="index">
                            <td>{{element.vendorName}}</td>
                            <td>{{element.popName}}</td>
                            <td>{{element.source}}</td>
                            <td>{{element.destination}}</td>
                            <td>
                                <select disabled v-model="element.ofcType"
                                        style="margin-top: 7px;">
                                    <option value="0">N/A</option>
                                    <option value="1">Single</option>
                                    <option value="2">Double</option>
                                </select>
                            </td>
                            <td>
                                <span v-if="element.quotationStatus == 0 ">NO</span>
                                <span v-else>YES</span>

                            </td>
                            <td>
                                {{(new Date(element.quotationDeadline)).toDateString()}}
                            </td>

                            <td>
                                <select disabled  v-model="element.workGiven">
                                    <option v-bind:value="0" value="1">NO</option>
                                    <option v-bind:value="1" value="2">YES</option>
                                </select>
                            </td>


                            <td>
                                <select disabled  v-model="element.workCompleted">
                                    <option v-bind:value="0" value="1">NO</option>
                                    <option v-bind:value="1" value="2">YES</option>
                                </select>
                            </td>

                            <td>
                                {{(new Date(element.workDeadline)).toDateString()}}
                            </td>
                            <td>
                                <span v-if="element.quotationStatus == 0">Not Given Yet</span>
                                <span v-else> {{element.proposedLoopDistance}} m</span>
                            </td>
                            <td>

                                <span v-if="element.actualLoopDistance == 0">Not Given Yet</span>
                                <span v-else> {{element.actualLoopDistance}} m</span>
                            </td>
                        </tr>

                        </tbody>
                        <%--<div class="form-group">
                        </div>--%>
                    </table>
                    </div>
                </div>
            </btcl-portlet>


        </div>

        <%--EFR VENDOR SHOW--%>
        <div v-if="application.efr_vendor.length>0" class="portlet-title">
            <btcl-portlet>
                <div>
                    <div class="portlet-title">
                        <div class="caption" align="center" style="background: #fab74d;padding: 10px;">EFR</div>
                        <div class="tools"><a href="javascript:;" data-original-title="" title="" class="collapse"></a>
                        </div>
                    </div>
                    <div class="form-body">
                        <div class="form-group">
                            <%--<div class="col-md-1"><p style="text-align: center;">Vendor Name</p></div>--%>
                            <div class="col-md-2"><p style="text-align: center;">POP Name</p></div>
                            <div class="col-md-2"><p style="text-align: center;">Source</p></div>
                            <div class="col-md-2"><p style="text-align: center;">Destination</p></div>
                            <div class="col-md-1"><p style="text-align: center;">OFC Type</p></div>
                            <div class="col-md-1"><p style="text-align: center;">Quotation Status</p></div>
                            <div class="col-md-1"><p style="text-align: center;">Quotation Deadline</p></div>
                            <div class="col-md-1"><p style="text-align: center;">Work Given</p></div>
                            <div class="col-md-1"><p style="text-align: center;">Work Completed</p></div>
                            <div class="col-md-1"><p style="text-align: center;">Work Deadline</p></div>

                        </div>
                        <hr/>

                        <div class="form-group" v-for="(element,index) in application.efr_vendor" :key="index">
                            <%--<div class="col-md-1"><p style="text-align: center;">{{element.popName}}</p></div>--%>
                            <div class="col-md-2"><p style="text-align: center;">{{element.popName}}</p></div>
                            <div class="col-md-2"><p style="text-align: center;">{{element.source}}</p></div>
                            <div class="col-md-2"><p style="text-align: center;">{{element.destination}}</p></div>
                            <div class="col-md-1">
                                <select disabled class="form-control" v-model="element.ofcType"
                                        style="margin-top: 7px;">
                                    <option value="0">N/A</option>
                                    <option value="1">Single</option>
                                    <option value="2">Double</option>
                                </select>
                            </div>
                            <div class="col-md-1"><p style="text-align: center;">
                                {{element.quotationStatus}}</p>
                            </div>
                            <div class="col-md-1"><p style="text-align: center;">
                                {{(new Date(element.quotationDeadline)).toDateString()}}</p>
                            </div>

                            <div class="col-md-1">
                                <select disabled class="form-control"  v-model="element.workGiven">
                                    <option v-bind:value="0" value="1">NO</option>
                                    <option v-bind:value="1" value="2">YES</option>
                                </select>
                            </div>


                            <div class="col-md-1">
                                <select disabled class="form-control"  v-model="element.workCompleted">
                                    <option v-bind:value="0" value="1">NO</option>
                                    <option v-bind:value="1" value="2">YES</option>
                                </select>
                            </div>

                            <div class="col-md-1"><p style="text-align: center;">
                                {{(new Date(element.workDeadline)).toDateString()}}</p>
                            </div>


                        </div>
                        <div class="form-group">
                        </div>
                    </div>
                </div>
            </btcl-portlet>


        </div>
        <btcl-portlet title="Documents" v-if="!loading">
            <%--<div align="center" v-if="an">--%>
            <%--<template>--%>
            <%--<button type="button" class="btn green-haze" @click="viewAN">View Advice Note</button>--%>
            <%--</template>--%>
            <%--</div>--%>
            <%--<br><hr><br/>--%>
            <%--<div align="center" v-if="dn">--%>
            <%--<template>--%>
            <%--<button type="button" class="btn green-haze" @click="viewDN">View Demand Note</button>--%>
            <%--</template>--%>
            <%--</div>--%>

            <%--<br><hr><br/>--%>
            <%--<div align="center" v-if="wo.length>0">--%>
            <%--<template v-for="(o, oIndex) in wo">--%>
            <%--<button type="button" class="btn green-haze" @click="viewWO(o)">View Work Order {{oIndex + 1}}</button>&nbsp;--%>
            <%--</template>--%>
            <%--</div>--%>

            <div class="row">
                <div class="col-xs-12">
                    <ul class="list-group">

                        <%--<a class="form-control"--%>
                        <%--style="background:  #f2f1f1;text-align: center;color:blue"--%>
                        <%--v-on:click="showOfficeDetails"--%>

                        <%-->--%>
                        <li class="list-group-item"  v-if="an"><a style="color:blue;font-size:large" @click="viewAN">View Advice Note</a></li>
                        <li class="list-group-item"  v-if="dn"><a  style="color:blue;font-size:large" @click="viewDN">View Demand Note</a></li>
                        <li class="list-group-item" style="font-size:large" v-if="wo.length>0">Work Order
                            <ul class="list-group inner">
                                <li class="list-group-item" v-for="(o, oIndex) in wo"><a  style="color:blue;font-size:large" @click="viewWO(o)">View Work Order {{oIndex + 1}}</a></li>
                                <%--<li class="list-group-item">Item 2b</li>--%>
                            </ul>
                        </li>
                        <%--<li class="list-group-item">Three--%>
                        <%--<ul class="list-group inner">--%>
                        <%--<li class="list-group-item">Item 3a</li>--%>
                        <%--<li class="list-group-item">Item 3b</li>--%>
                        <%--</ul>--%>
                        <%--</li>--%>
                    </ul>
                </div>
            </div>


        </btcl-portlet>


        <%--modals--%>
        <%--<jsp:include page="lli-application-new-connection-view-modals.jsp"/>--%>

        <%--start: Office List Modal--%>
        <div class="modal fade" id="showOfficeDetails" role="dialog">
            <div class="modal-dialog modal-lg" style="width: 50%;">
                <div class="modal-content">
                    <div class="portlet-title">
                        <div class="caption" align="center"
                             style="background: #fab74d;padding: 10px;"><p>Connection Office Details</p>
                        </div>
                        <div class="tools">
                            <a href="javascript:;" data-original-title="" title=""
                               class="collapse"></a>
                        </div>
                        <div class="modal-body">
                            <div>
                                <btcl-portlet>
                                    <ul class="list-group" v-for="(office,officeIndex) in application.officeList">
                                        <li class="list-group-item active"
                                            style="background-color: black"><b font-size="20px">{{office.officeName}},
                                            {{office.officeAddress}}</b></li>
                                        <li class="list-group-item" v-for="(loop, loopIndex) in office.loops">
                                            POP {{loopIndex+1}} : {{loop.popName}}
                                        </li>
                                    </ul>
                                </btcl-portlet>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-danger" data-dismiss="modal">Close
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

<script>var applicationID = '<%=request.getParameter("id")%>';</script>
<script src="new-connection/lli-application-new-connection-details.js"></script>
