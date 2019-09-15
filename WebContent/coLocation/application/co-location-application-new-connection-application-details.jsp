<%@ page import="sessionmanager.SessionConstants" %>
<%@ page import="login.LoginDTO" %>
<%@ page import="coLocation.CoLocationConstants" %>
<%@ page import="common.ModuleConstants" %>


<div id="btcl-co-location-application" v-cloak="true">
    <btcl-loader :loader="loader">
        <btcl-body :loader="loader" title="Co-Location" subtitle='Application Details'>
            <%--form elements--%>
                <btcl-portlet v-if="application.state.name=='CLIENT_CORRECTION'">
                    <jsp:include page="../../coLocation/application/co-location-application-form.jsp"/>
                </btcl-portlet>

            <btcl-portlet v-else>
<%--                <btcl-field v-for="(element,index) in application.formElements" :key="index">--%>
<%--                    <div class="form-group">--%>
<%--                        <div class="row">--%>
<%--                            <label class="col-sm-4" style="text-align: left;">{{element.Label}}</label>--%>
<%--                            <div v-if="element.Label!='Status'" class="col-sm-6 text-center" style="background:  #f2f1f1;padding:1%">--%>
<%--                                <template><span >{{element.Value}}</span></template>--%>
<%--                            </div>--%>
<%--                            <div v-else class="col-sm-6 text-center"  v-bind:style="{ background: application.color}">--%>
<%--                                <span style="color: white;font-weight: bold ;font-size: 18px!important;">{{element.Value}}</span>--%>
<%--                            </div>--%>


<%--                        </div>--%>
<%--                    </div>--%>
<%--                </btcl-field>--%>



                <div class=row>
                    <%--left column--%>
                    <div class="col-sm-6" style="padding-left:2%">
                        <template v-for="(element,index) in application.formElements"
                                  v-if="index<application.formElements.length/2">
                            <div class="form-group">
                                <div class="row">
                                    <label class="col-sm-4"
                                           style="text-align: left;"><b>{{element.Label}}</b></label>
                                    <div v-if="element.Label!='Status'" class="col-sm-6 text-center"
                                         style="background:  #f2f1f1;padding:1%">
                                        <template><span>{{element.Value}}</span></template>
                                    </div>
                                    <div v-else class="col-sm-6 text-center"
                                         v-bind:style="{ background: application.color}">
                                        <span style="color: white;font-weight: bold ;font-size: 18px!important;">{{element.Value}}</span>
                                    </div>
                                </div>
                            </div>
                        </template>
                    </div>
                    <%--right column--%>
                    <div class="col-sm-6" style="padding-left: 2%">
                        <template v-for="(element,index) in application.formElements"
                                  v-if="index>=application.formElements.length/2">
                            <div class="form-group">
                                <div class="row">
                                    <label class="col-sm-4"
                                           style="text-align: left;"><b>{{element.Label}}</b></label>
                                    <div v-if="element.Label!='Status'" class="col-sm-6 text-center"
                                         style="background:  #f2f1f1;padding:1%">
                                        <template><span>{{element.Value}}</span></template>
                                    </div>
                                    <div v-else class="col-sm-6 text-center"
                                         v-bind:style="{ background: application.color}">
                                        <span style="color: white;font-weight: bold ;font-size: 18px!important;">{{element.Value}}</span>
                                    </div>
                                </div>
                            </div>
                        </template>
                    </div>
                </div>



            </btcl-portlet>
        </btcl-body>

        <%--application details information--%>
        <btcl-body :loader="loader" title="Feasibility Report" v-if="application.state.view=='IFR_REQUEST_VIEW' || application.state.view=='IFR_DETAILS_VIEW'">

            <%--<btcl-portlet  v-for="element in application.ifr">--%>
                <%--<div class="form-body">--%>
                    <%--<div class="form-group"--%>
                         <%--style="--%>
                         <%--/*background: rgb(220, 220, 220);*/--%>

                         <%--margin-left: 1px;margin-top: 7px;border-radius: 9px;padding:5px;margin-right:2px">--%>

                        <%--<div class="col-md-3"><p style="text-align: center;"></p></div>--%>
                        <%--<div class="col-md-3"><p style="text-align: center;">{{element.categoryLabel}}</p></div>--%>
                        <%--<div class="col-md-3"><p style="text-align: center;">{{element.amountLabel}}</p></div><div class="col-md-3"><p style="text-align: center;">Feasibility</p></div>--%>
                    <%--</div>--%>
                    <%--<div class="form-group">--%>
                        <%--<div class="col-md-3"><p style="text-align: center;padding: 10px;">{{element.typeName}}</p></div>--%>
                        <%--<div class="col-md-3"><p style="text-align: center;padding: 10px;">{{element.categoryName}}</p></div>--%>
                            <%--<div class="col-md-3"><p style="text-align: center;padding: 10px;">{{element.amountName}}</p></div>--%>
                            <%--<div class="col-md-3">--%>
                                <%--<select class="form-control" v-model="element.isSelected" style="margin-top: 7px;">--%>
                                    <%--<option :value="true">Possible</option>--%>
                                    <%--<option :value="false">Not Possible</option>--%>
                                <%--</select>--%>
                            <%--</div>--%>
                    <%--</div>--%>
                    <%--<br>--%>



                <%--</div>--%>
            <%--</btcl-portlet>--%>

            <btcl-portlet >
                <table class="table" >

                    <tbody v-for="element in application.ifr">
                    <tr>

                        <th scope="col"><p style="text-align: center;"></p></th>
                        <th scope="col"><p style="text-align: center;">{{element.categoryLabel}}</p></th>
                        <th scope="col"><p style="text-align: center;">{{element.amountLabel}}</p></th>
                        <th scope="col"><p style="text-align: center;">Feasibility</p></th>

                        <template v-if="application.state.view=='IFR_DETAILS_VIEW'">
                            <th scope="col"><p style="text-align: center;">Completed</p></th>
                            <th scope="col"><p style="text-align: center;">Replied</p></th>
                        </template>

                    </tr>
                    <tr>
                        <td scope="row"><p style="text-align: center;padding: 10px;">{{element.typeName}}</p></td>
                        <td><p style="text-align: center;padding: 10px;">{{element.categoryName}}</p></td>
                        <td><p style="text-align: center;padding: 10px;">{{element.amountName}}</p></td>
                        <td>
                            <select class="form-control" v-model="element.isSelected" style="margin-top: 7px;" disabled v-if="application.state.view=='IFR_DETAILS_VIEW'">
                                <option :value="true">Possible</option>
                                <option :value="false">Not Possible</option>
                            </select>
                            <select class="form-control" v-model="element.isSelected" style="margin-top: 7px;" v-else>
                                <option :value="true">Possible</option>
                                <option :value="false">Not Possible</option>
                            </select>
                        </td>

                        <template v-if="application.state.view=='IFR_DETAILS_VIEW'">
                            <td><p style="text-align: center;padding: 10px;"><template v-if="element.isCompleted">Yes</template> <template v-else>No</template></p></td>
                            <td><p style="text-align: center;padding: 10px;"><template v-if="element.isReplied">Yes</template> <template v-else>No</template></p></td>
                        </template>



                    </tr>
                    <br/>
                    <br/>

                    </tbody>
                    <%--<br>--%>


                </table>
            </btcl-portlet>

        </btcl-body>


        <btcl-body :loader="loader" title="Documents" v-if="application.state.view=='ADVICE_NOTE'">
            <btcl-portlet>
                <div align="center">
                    <button type="submit" class="btn green-haze" @click="redirect('advicenote')">View Advice Note</button>
                </div>
            </btcl-portlet>
        </btcl-body>
        <btcl-body :loader="loader" title="Documents" v-if="application.state.view=='DEMAND_NOTE'">
            <btcl-portlet>
                <div align="center">
                    <button type="submit" class="btn green-haze" @click="redirect('demandnote')">View Demand Note
                    </button>
                </div>
            </btcl-portlet>
        </btcl-body>
        <%--action forward--%>
        <btcl-body :loader="loader" title="Available Actions" v-if="application.action.length>0">
            <btcl-portlet>
                <div>
                    <ul style="list-style-type:none">
                        <li v-for="(element,elementIndex) in application.action" :key="elementIndex" v-if="elementIndex>0">
                            <%--<label v-if="demandNoteGeneratedSkip(element)"></label>--%>
                            <label
                                    <%--v-else--%>
                            >
                            <span><input type="radio" name="actionForwards" v-model="picked" :value="element.id"> {{element.description}}</span>
                            </label>
                        </li>
                    </ul>
                </div>
                <hr>
                <button type="submit" class="btn green-haze btn-block" @click="nextStep">Submit</button>

            </btcl-portlet>


        </btcl-body>


        <%--connection completion--%>
        <jsp:include page="../../lli/application/new-connection/view/modals/lli-application-new-connection-view-modal-connection-complete.jsp"/>
        <jsp:include page="../../lli/application/new-connection/view/modals/lli-application-new-connection-view-modal-advice-note.jsp"/>


    </btcl-loader>





    <%--<btcl-body title="Practice">--%>
        <%--<btcl-portlet>--%>

        <%--</btcl-portlet>--%>
    <%--</btcl-body>--%>

</div>
<%--<div id="parent">--%>
    <%--p-> {{p}}--%>
    <%--<div id="child" v-pre>c-> {{c}}--%>
        <%--<div id="ins"></div>--%>
    <%--</div>--%>

    <%--<children></children>--%>
<%--</div>--%>

<%--Connection Completion Modal--%>





<%LoginDTO loginDTO = (LoginDTO) session.getAttribute(SessionConstants.USER_LOGIN);%>
<script>
    var applicationID = parseInt('<%=request.getParameter("id")%>');
    var loggedInUserID = '<%=loginDTO.getUserID()%>';
    // debugger;

</script>
<script>
    var vue = new Vue({
        el: "#btcl-co-location-application",
        data: {
            connectionTypeList: [{id: 1, value: 'Regular'}, {id: 2, value: 'Special Connection'}],
            powerTypeList: [{id: 1, type:0, value: 'AC'}, {id: 2,type:0, value: 'DC'}],
            application:{
                client: null,
                connectionType: null,
                rackNeeded: true,
                rackSize: null,
                rackSpace: null,
                powerNeeded: true,
                powerAmount: null,
                powerType: null,
                fiberNeeded: true,
                fiberType: null,
                fiberCore: 0,
                comment: null,
                suggestedDate: null,
                description: null,
                formElements:[
                    {Label: '', Value: ''},
                    // {Label: 'Rack Size', Value: '20'},
                    // {Label: 'Ofc Type', Value: 'Dual'},
                    // {Label: 'Date', Value: '20MAY'},
                ],
                action:[
                    {Label: '', Value: ''},
                    // {Label: 'Rack Size', Value: '20'},
                    // {Label: 'Ofc Type', Value: 'Dual'},
                    // {Label: 'Date', Value: '20MAY',quotationStatus: 1,},
                ],
                color: "blue",
                comment: '',
                state:'',
                connection: null,
            },
            picked: 1,
            NO_ACTION_PICKED: 1,
            comment: '',
            moduleId: <%=ModuleConstants.Module_ID_COLOCATION%>,
            loader: false,
        },
        methods: {
            // submitNewConnection : function(){
            //     alert('form submit clicked');
            // },

            nextStep: function(){
                const nextAction = this.application.action.find(obj => obj.id == this.picked);
                if(nextAction) var commentTitle = " Comment on " + nextAction.description;
                if (this.picked == this.NO_ACTION_PICKED) {errorMessage("Please Select an Action.");return;}
                //if connection compelete step then take load modal to take connection name as input
                <%--if(this.picked == <%=CoLocationConstants.STATE.COMPLETE_CONNECTION.getValue()%>)  $('#connectionCompletionModal').modal({show: true});--%>
                <%--else if(this.picked ==  <%=CoLocationConstants.STATE.ADVICE_NOTE_PUBLISH.getValue()%>)    $('#forwardAdviceNote').modal({show: true});--%>
                else if(nextAction.modal!="")$(nextAction.modal).modal({show: true});
                //load sweet alert elseswhere
                else swal(commentTitle, {
                    content: "input",
                    showCancelButton: true,
                    buttons: true,
                })
                    .then((value) => {
                        if (value === false) return false;
                        if (value === "") {
                            swal("Warning!", "You you need to write something!", "warning");return false;
                        }
                        if (value) {
                            this.comment = value + '';
                            // debugger;
                            this.postData(this.comment,nextAction);
                        }
                    });
            },
            redirect: function (url) {
                var location = context + 'lli/pdf/' + url + '.do?applicationid=' + applicationID;
                if (url === 'workorder') location += '&vendorid=' + loggedInUserID;
                // http://localhost:8082/lli/pdf/view.do?type=demand-note&id=299009

                // debugger;
                if (url === 'demandnote') location = context + 'pdf/view/demand-note.do?billId=' + this.application.demandNoteID;
                if (url === 'advicenote') location = context + 'pdf/view/advice-note.do?appId=' + applicationID+"&module=4";

                // window.location.href = location;
                window.open(
                    location,
                    '_blank'
                );
            },
            connectionCompletionModal: function(){
                if (this.picked == this.NO_ACTION_PICKED) {errorMessage("Please Select an Action.");return;};
                const nextAction = this.application.action.find(obj => obj.id == this.picked);
                this.postData(this.comment,nextAction);
            },

            postData: function(comment,nextAction){
                // debugger;
                var apps = {
                    applicationID: applicationID,
                    comment: this.comment,
                    nextState: this.picked,
                    application: this.application,
                    ifr:this.application.ifr
                };
                var url1 = nextAction.url;//"#";// "closeapplication";
                // debugger;
                this.loader = true;
                axios.post(context + 'co-location/' + url1 + '.do', {'data': JSON.stringify(apps)})
                    .then(result => {
                        if (result.data.responseCode == 2) {
                            toastr.error(result.data.msg);
                            this.loader = false;
                        } else if (result.data.responseCode == 1) {
                            var redirect="";
                            if(nextAction.redirect!=""){
                                if(nextAction.param!=""){
                                    var paramArray=nextAction.param.split('_');

                                    var paramVal="";

                                    for (var i=0;i<paramArray.length;i++){

                                        if(paramArray[i]=="billIDs"){
                                            paramVal += paramArray[i]+"="+this.application.demandNoteID;
                                        }else if(paramArray[i]=="paymentID"){

                                            paramVal += paramArray[i]+"="+ this.application.bill.paymentID;

                                        }else if(paramArray[i]=="moduleID"){

                                            paramVal += paramArray[i]+"="+this.application.moduleID;
                                        }
                                        else if(paramArray[i]=="applicationID"){

                                            paramVal += paramArray[i]+"="+this.application.applicationID;
                                        }
                                        else if(paramArray[i]=="nextState"){

                                            paramVal += paramArray[i]+"="+nextAction.id;
                                        }

                                        if(i==paramArray.length-1){

                                        }else{

                                            paramVal+="&"
                                        }
                                    }

                                    redirect=nextAction.redirect+"?"+paramVal
                                }else{
                                    redirect=nextAction.redirect;
                                }
                            }else{
                                redirect='co-location/search.do';
                            }
                            toastr.success("Your request has been processed", "Success");
                            window.location.href = context + redirect;
                        }
                        else {
                            toastr.error("Your request was not accepted", "Failure");
                            this.loader = false;
                        }

                    }).catch(function (error) {});
            },


            forwardAdviceNote: function () {
                var comment = this.comment;
                var localLoops = {
                    applicationID: this.application.applicationID,
                    comment: comment,
                    nextState: this.picked,
                    userList: this.application.userList,
                    // pops: this.application.completeEFR,
                    senderId: loggedInUserID

                };
                var url1 = "advice-note-generate";
                this.loader = true;
                axios.post(context + 'co-location/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                    .then(result => {
                        if (result.data.responseCode == 2) {
                            toastr.error(result.data.msg);
                            this.loader = false;
                        } else if (result.data.responseCode == 1) {
                            toastr.success("Your request has been processed", "Success");
                            window.location.href = context + 'co-location/search.do';
                        }
                        else {
                            toastr.error("Your request was not accepted", "Failure");
                            this.loader = false;
                        }

                    })
                    .catch(function (error) {
                    });


            },


        },
        computed : {},
        mounted() {},
        created() {
            this.loader = true;
            axios({method: 'GET', 'url': context + 'co-location/flow-data.do?id=' + applicationID})
                .then(result => {
                    // debugger;
                    if (result.data.payload.hasOwnProperty("members")) {
                        // this.application = result.data.payload.members;
                        this.application = Object.assign(this.application, result.data.payload.members);
                    }
                    else {
                        // this.application = result.data.payload;
                        this.application = Object.assign(this.application, result.data.payload);
                    }
                    this.application.connectionName = '';
                    this.application.applicationID = applicationID;
                    this.application.action.unshift({});

                    // generate DN & skip remove if not govt. client
                    if(loggedInAccount.registrantType != 1){
                        let DEMAND_NOTE_GENERATED_SKIP = 6019;
                        this.application.action = this.application.action.filter( a => a.id!=DEMAND_NOTE_GENERATED_SKIP);
                    }

                    vue.loader = false;




                }, error => {
                });


        },
    });
    // var x = new Vue({
    //     el: "#parent",
    //     data: {
    //         p: "I'm the parent",
    //
    //     },
    // });
    // var y = new Vue({
    //     el: "#child",
    //     data: {
    //         c: "blah blah blah",
    //     },
    //     mounted(){
    //       // window.getElementById("ins").innerHTML = "blah blah blah");
    //         var x = 10;
    //
    //         // alert('blah');
    //     }
    // });

</script>