<%@page import="util.TimeConverter" %>
<%@page import="lli.connection.LLIConnectionConstants" %>
<%@page import="common.repository.AllClientRepository" %>
<%@page import="lli.Application.LLIApplication" %>
<%@page import="java.util.ArrayList" %>
<%@page import="lli.LLIConnectionInstance" %>
<%@page import="sessionmanager.SessionConstants" %>
<%
    String msg = null;
    String url = "lli/application/search";
    String navigator = SessionConstants.NAV_LLI_APPLICATION;
    String context = "../.." + request.getContextPath() + "/";
%>

<%--<%@page import="file.FileTypeConstants" %>--%>
<%--<%@page import="common.EntityTypeConstant" %>--%>

<%--<div id=btcl-application>--%>
<%--<btcl-body title="Location" subtitle="">--%>
<%--<btcl-form :action="contextPath + 'location.do'" :name="['application']" :form-data="[application]"--%>
<%--:redirect="goView">--%>
<%--<btcl-portlet>--%>
<%--<btcl-field title="Client">--%>
<%--<lli-client-search :client.sync="application.client">Client</lli-client-search>--%>
<%--</btcl-field>--%>
<%--&lt;%&ndash;<btcl-input title="Bandwidth (Mbps)" :text.sync="application.bandwidth"></btcl-input>&ndash;%&gt;--%>
<%--&lt;%&ndash;<btcl-field title="Connection Type">&ndash;%&gt;--%>
<%--&lt;%&ndash;<connection-type :data.sync=application.connectionType :client="application.client"></connection-type>&ndash;%&gt;--%>
<%--&lt;%&ndash;</btcl-field>&ndash;%&gt;--%>
<%--&lt;%&ndash;<btcl-input title="Connection Address" :text.sync="application.address"></btcl-input>&ndash;%&gt;--%>
<%--<btcl-field title="Loop Provider">--%>
<%--<multiselect v-model="application.loopProvider"--%>
<%--:options="[{ID:1,label:'BTCL'},{ID:2,label:'Client'}]"--%>
<%--:track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom">--%>
<%--</multiselect>--%>
<%--</btcl-field>--%>
<%--&lt;%&ndash;<btcl-field v-if="application.connectionType && application.connectionType.ID==2" title="Duration (Days)">&ndash;%&gt;--%>
<%--&lt;%&ndash;<input type=number class="form-control" v-model=application.duration>&ndash;%&gt;--%>
<%--&lt;%&ndash;</btcl-field>&ndash;%&gt;--%>
<%--&lt;%&ndash;<btcl-input title="Description" :text.sync="application.description"></btcl-input>&ndash;%&gt;--%>
<%--&lt;%&ndash;<btcl-input title="Comment" :text.sync="application.comment"></btcl-input>&ndash;%&gt;--%>
<%--&lt;%&ndash;<btcl-field title="Suggested Date">&ndash;%&gt;--%>
<%--&lt;%&ndash;<btcl-datepicker :date.sync="application.suggestedDate"></btcl-datepicker>&ndash;%&gt;--%>
<%--&lt;%&ndash;</btcl-field>&ndash;%&gt;--%>

<%--</btcl-portlet>--%>
<%--</btcl-form>--%>
<%--</btcl-body>--%>
<%--</div>--%>


<div id="location">
    <div class="portlet light bordered">
        <div class="portlet-title">
            <div class="caption font-green-sharp"><span class="caption-subject bold uppercase">{{message}}</span> <span
                    class="caption-helper"></span></div>
            <div class="actions"><a href="javascript:;" class="btn btn-circle btn-icon-only btn-default fullscreen"
                                    data-original-title="" title=""></a></div>
        </div>
        <div class="portlet-body">
            <div class="form-horizontal">
                <div class="form-body">
                    <form action="#">
                        <div class="portlet light bordered"><!---->
                            <div class="portlet-body">
                                <div class="form-group">
                                    <div class="row"><label class="col-sm-4 control-label">Division<!----></label>
                                        <div class="col-sm-6">
                                            <select v-model="selectedDiv"
                                            <%--@change="renderDistrict({{division.ID}})"--%>
                                                    @change="renderDistrict()"
                                            <%--:value="{{divisioin.ID}}"--%>
                                                    class="form-control" name="divdata" id="divisiondropdown">
                                                <%--<option value="-1" disabled="disabled" selected="true"> --- Choose Division ---</option>--%>
                                                <option disabled value="-1"> Choose Division</option>
                                                <option :value="division.id" v-for="division in divisions.elements">
                                                    {{division.nameEng}}
                                                </option>

                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="row"><label class="col-sm-4 control-label">District</label>
                                        <div class="col-sm-6">
                                            <select class="form-control" v-model="selectedDis"

                                            <%--@change="renderZone()"--%>
                                                    class="form-control" name="disdata" id="districtdropdown">
                                                <%--<option value="-1" disabled="disabled" selected="true"> --- Choose--%>
                                                <%--District -----%>
                                                <%--</option>--%>
                                                <option disabled value="-1"> Choose District</option>
                                                <option v-bind:value="district.id"
                                                        v-for="district in districts.elements">
                                                    {{district.nameEng}}
                                                </option>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <%--<div style="padding-top: 30px;">--%>
                        <%--<button type="submit" class="btn green-haze btn-block btn-lg">Submit</button>--%>
                        <%--</div>--%>
                    </form>
                </div>
            </div>
        </div>
    </div>



    <%--<script src="../connection/lli-connection-components.js"></script>--%>
    <%--<script src=lli-application-components.js></script>--%>
    <%--<script src=new-connection/lli-application-new-connection.js></script>--%>

    <div class="portlet box">
        <div class="portlet-body">
            <div class="table-responsive">
                <table id="example1" class="table table-bordered table-striped">
                    <thead>
                    <tr>
                        <th>GEO ID</th>
                        <th>Client</th>
                        <th>Application Type</th>
                        <th>Status</th>
                        <th>App Date</th>
                        <th>Edit</th>
                    </tr>
                    </thead>
                    <tbody>

                    <tr v-for="division in divisions.elements">
                        <td> {{division.nameEng}}</td>
                        <td> {{division.nameEng}}</td>
                        <td> {{division.nameEng}}</td>
                        <td> {{division.nameEng}}</td>
                        <td> {{division.nameEng}}</td>
                        <td> {{division.nameEng}}</td>

                    </tr>

                    <%--<%--%>
                    <%--ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_LLI_APPLICATION);--%>
                    <%--if (data != null) {--%>
                    <%--for (int i = 0; i < data.size(); i++) {--%>
                    <%--LLIApplication lliApplication = (LLIApplication) data.get(i);--%>
                    <%--%>--%>
                    <%--<tr>--%>
                    <%--<td>--%>
                    <%--<a href="<%=context%>lli/application/view.do?id=<%=lliApplication.getApplicationID() %>"><%=lliApplication.getApplicationID()%>--%>
                    <%--</a>--%>

                    <%--</td>--%>
                    <%--<td><%=AllClientRepository.getInstance().getClientByClientID(lliApplication.getClientID()).getLoginName()%>--%>
                    <%--</td>--%>
                    <%--<td><%=LLIConnectionConstants.applicationTypeNameMap.get(lliApplication.getApplicationType())%>--%>
                    <%--</td>--%>
                    <%--<td><%=LLIConnectionConstants.applicationStatusMap.get(lliApplication.getStatus())%>--%>
                    <%--</td>--%>
                    <%--<td><%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(lliApplication.getSubmissionDate(), "dd-MM-YYYY")%>--%>
                    <%--</td>--%>
                    <%--<td><a href="#">Edit</a></td>--%>
                    <%--</tr>--%>
                    <%--<%--%>
                    <%--}--%>
                    <%--%>--%>
                    </tbody>
                    <%--<% } %>--%>
                </table>
            </div>
        </div>
    </div>
</div>
<script>

    var loc = new Vue({
        el: "#location",
        data: {
            message: 'Location',
            popOptions: [],
            divisions: [],
            districts: [],
            selectedDiv: '-1',
            selectedDis: ''

        }
        ,
        methods: {
            renderDistrict: function () {
                // alert('clicked ' + this.selected);
                axios({method: "GET", "url": context + "location/district.do?division=" + this.selectedDiv})
                    .then(result => {
                        this.districts = result.data.payload;
                        this.selectedDis = -1;
                        console.log('=====================!!!!!!!!! ===============');
                        console.log(this.districts.elements);
                        console.log('=====================!!!!!!!!! ===============');
                    }, error => {
                    });
            }
        },
        mounted() {
            axios({method: "GET", "url": context + "location/division.do?id=" + 0})
                .then(result => {
                    this.divisions = result.data.payload;

                    console.log(this.divisions.elements);

                }, error => {
                });
            if (typeof this.itemid !== 'undefined') {
                // this.refreshInventoryPath();
                alert('error!!');
            }
        },
        updated: function(){
            this.$nextTick(function(){

                $(document).ready(function () {
                    $('#example1').DataTable();
                });
                // this.selectedDis = "-1";
            });

        }
    });
    <%--example_table = $('#example1').DataTable({--%>
    <%--// "bServerSide" : true,--%>
    <%--// "sPaginationType": "full_numbers",--%>
    <%--// "sDom": '<"row" <"col-md-6 col-sm-12"l><"col-md-6 col-sm-12"<"toolbar">>><"table-scrollable"t><"row" <"col-md-5 col-sm-12"i><"col-md-7 col-sm-12"p>>',--%>
    <%--// "sEcho": 1,--%>
    <%--// "language":{--%>
    <%--//     "decimal":        "১",--%>
    <%--//     "emptyTable":     "থানা খুজে পাওয়া যায় নাই",--%>
    <%--//     "info":           " মোট TOTAL থানার মধ্যে START থেকে END পর্যন্ত দেখানো হচ্ছে",--%>
    <%--//     "infoEmpty":      "থানা খুজে পাওয়া যায় নাই",--%>
    <%--//     "infoFiltered":   "(filtered from MAX total entries)",--%>
    <%--//     "infoPostFix":    "",--%>
    <%--//     "thousands":      ",",--%>
    <%--//     "lengthMenu":     "_MENU_",--%>
    <%--//     "loadingRecords": "Loading...",--%>
    <%--//     "processing":     "Processing...",--%>
    <%--//     "search":         "Search:",--%>
    <%--//     "zeroRecords":    "থানা খুজে পাওয়া যায় নাই",--%>
    <%--//     "paginate": {--%>
    <%--//         "first":      "প্রথম",--%>
    <%--//         "last":       "শেষ",--%>
    <%--//         "next":       "পরে",--%>
    <%--//         "previous":   "আগে"--%>
    <%--//     },--%>
    <%--//     "aria": {--%>
    <%--//         "sortAscending":  ": activate to sort column ascending",--%>
    <%--//         "sortDescending": ": activate to sort column descending"--%>
    <%--//     }--%>
    <%--// },--%>
    <%--"sAjaxSource": "<%=request.getContextPath()%>/",--%>
    <%--"iDisplayLength": 10,--%>
    <%--"iDisplayStart": 0,--%>
    <%--"aoColumns": [--%>
    <%--{--%>
    <%--"mData": 'thanaNameEng',--%>
    <%--"bSortable": false--%>
    <%--},--%>
    <%--{--%>
    <%--"mData": 'thanaNameBng',--%>
    <%--"bSortable": false--%>
    <%--},--%>
    <%--{--%>
    <%--"mData": 'bbsCode',--%>
    <%--"bSortable": false--%>
    <%--},--%>
    <%--{--%>
    <%--"mRender": function (data, type, full) {--%>

    <%--var editdiv = $("#edit").clone();--%>
    <%--var editbtn = $('form', editdiv);--%>
    <%--editbtn.attr('action', "${context}/thanaedit");--%>
    <%--$('input[name="id"]', editbtn).val(full.id);--%>
    <%--$('input[name="thananameeng"]', editbtn).val(full.thanaNameEng);--%>
    <%--$('input[name="thananamebng"]', editbtn).val(full.thanaNameBng);--%>
    <%--$('input[name="bbscode"]', editbtn).val(full.bbsCode);--%>
    <%--$('input[name="divisionbbscode"]', editbtn).val(full.divisionBbsCode);--%>
    <%--$('input[name="districtbbscode"]', editbtn).val(full.districtBbsCode);--%>
    <%--$('input[name="divId"]', editbtn).val(full.geoDivisionId);--%>
    <%--$('input[name="disId"]', editbtn).val(full.geoDistrictId);--%>
    <%--return editdiv.html();--%>
    <%--},--%>
    <%--"bVisible": evisible,--%>
    <%--"bSortable": false--%>


    <%--},--%>
    <%--{--%>
    <%--"mRender": function (data, type, full) {--%>
    <%--return '<button class="btn btn-icon-only" style="background-color: black;color: white" onclick=showModal(' + full.id + ',\"/thanadelete\",msg.trim())>' +--%>
    <%--' <i class="fa fa-trash-o fa-lg"></i>' +--%>
    <%--'</button>';--%>
    <%--},--%>
    <%--"bVisible": dvisible,--%>
    <%--"bSortable": false--%>
    <%--},--%>
    <%--{--%>
    <%--"mRender": function (data, type, full) {--%>
    <%--var link = '<a href="/thanahistory?id=' + full.id + '&&name=' + full.thanaNameBng + '"><button class="btn btn-icon-only"><i class="fa fa-info" style="color: black"></i></button></a>';--%>
    <%--return link;--%>
    <%--},--%>
    <%--"bVisible": hvisible,--%>
    <%--"bSortable": false--%>
    <%--}--%>


    <%--],--%>
    <%--"autoWidth": false--%>
    <%--});--%>


</script>
<script src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.min.js" type="text/javascript"></script>
<link href="https://cdn.datatables.net/1.10.19/css/jquery.dataTables.min.css" rel="stylesheet" type="text/css">
