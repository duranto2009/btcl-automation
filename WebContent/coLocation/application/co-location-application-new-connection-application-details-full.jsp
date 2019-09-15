<%@ page import="sessionmanager.SessionConstants" %>
<%@ page import="login.LoginDTO" %>
<div id="btcl-co-location-application" v-cloak="true">
    <btcl-body title="Co-Location" subtitle='Application Details'>
        <%--form elements--%>
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
        </btcl-portlet>
    </btcl-body>
    <%--application details information--%>
    <btcl-body title="Feasibility Report" v-if="application.state.view=='IFR_REQUEST_VIEW'">
        <btcl-portlet  v-for="element in application.ifr">
            <div class="form-body">
                <div class="form-group" style="margin-left: 1px;margin-top: 7px;border-radius: 9px;padding:5px;margin-right:2px">
                    <div class="col-md-3"><p style="text-align: center;"></p></div>
                    <div class="col-md-3"><p style="text-align: center;">{{element.categoryLabel}}</p></div>
                    <div class="col-md-3"><p style="text-align: center;">{{element.amountLabel}}</p></div>
                    <div class="col-md-3"><p style="text-align: center;">Feasibility</p></div>
                </div>
                <div class="form-group">
                    <div class="col-md-3">
                        <p style="text-align: center;padding: 10px;">
                            {{element.typeName}}</p>
                    </div>
                    <div class="col-md-3"><p style="text-align: center;padding: 10px;">
                        {{element.categoryName}}</p></div>

                        <div class="col-md-3"><p style="text-align: center;padding: 10px;">
                        {{element.amountName}}</p></div>
                        <div class="col-md-3">
                            <select class="form-control" v-model="element.isSelected"
                                    style="margin-top: 7px;">
                                <option :value="true">Possible</option>
                                <option :value="false">Not Possible</option>
                            </select>
                        </div>
                </div>
                <br>
            </div>
        </btcl-portlet>

    </btcl-body>

    <btcl-body title="Documents" v-if="application.state.view=='ADVICE_NOTE'">
        <btcl-portet>
            <div align="center">
                <button type="submit" class="btn green-haze" @click="redirect('advicenote')">View Advice Note</button>
            </div>
        </btcl-portet>
    </btcl-body>
    <%--<btcl-body title="Documents" v-if="application.state.view=='DEMAND_NOTE'">--%>
        <%--<btcl-portet>--%>
            <%--<div align="center">--%>
                <%--<button type="submit" class="btn green-haze" @click="redirect('workorder')">View Work Order--%>
                <%--</button>--%>
            <%--</div>--%>
        <%--</btcl-portet>--%>
    <%--</btcl-body>--%>
</div>
<%LoginDTO loginDTO = (LoginDTO) session.getAttribute(SessionConstants.USER_LOGIN);%>
<script>
    var applicationID = parseInt('<%=request.getParameter("id")%>');
    var loggedInUserID = '<%=loginDTO.getUserID()%>';
</script>
<script>
    var vue = new Vue({
        el: "#btcl-co-location-application",
        data: {
            application:{
                formElements:[
                    {Label: '', Value: ''},
                ],
                action:[
                    {Label: '', Value: ''},
                ],
                color: "blue",
                comment: '',
                state:'',
            },
            picked: 1,
            NO_ACTION_PICKED: 1,
        },
        methods: {
            nextStep: function(){
                if (this.picked == this.NO_ACTION_PICKED) {errorMessage("Please Select an Action.");return;}
                const nextAction = this.application.action.find(obj => obj.id == this.picked);
                const commentTitle = " Comment on " + nextAction.description;
                swal(commentTitle, {
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
                            this.loading = true;
                            this.comment = `${value}` + '';
                            var apps = {
                                applicationID: applicationID,
                                comment: this.comment,
                                nextState: this.picked,
                                application: this.application,
                                ifr:this.application.ifr
                            };
                            var url1 = nextAction.url;//"#";// "closeapplication";
                            // debugger;
                            axios.post(context + 'co-location/' + url1 + '.do', {'data': JSON.stringify(apps)})
                                .then(result => {
                                    if (result.data.responseCode == 2) {
                                        toastr.error(result.data.msg);
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
                                    }
                                    this.loading = false;
                                }).catch(function (error) {});
                        }
                    });
            },
            redirect: function (url) {
                var location = context + 'lli/pdf/' + url + '.do?applicationid=' + applicationID;
                if (url === 'workorder') location += '&vendorid=' + loggedInUserID;
                // http://localhost:8082/lli/pdf/view.do?type=demand-note&id=299009

                if (url === 'demandnote') location = context + 'pdf/view/demand-note.do?billId=' + this.application.demandNoteID;
                if (url === 'advicenote') location = context + 'pdf/view/advice-note.do?appId=' + applicationID + "&module=4";

                // window.location.href = location;
                window.open(
                    location,
                    '_blank'
                );
            },
        },
        computed : {},
        mounted() {},
        created() {
            // this.loading = true;
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

                }, error => {
                });
        },
    });
</script>