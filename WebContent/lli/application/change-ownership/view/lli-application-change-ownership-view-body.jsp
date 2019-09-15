<%@ page import="login.LoginDTO" %>
<%@ page import="sessionmanager.SessionConstants" %>

<div id="btcl-application" v-cloak="true">
	<btcl-body title="Owner Change" subtitle='Application Details' :loader="loading">
		<%--form elements--%>
		<btcl-portlet v-if="application.state.name=='CLIENT_CORRECTION'">
			<jsp:include page="../../change-ownership/lli-owner-change-application-form.jsp"/>
		</btcl-portlet>

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

			<btcl-field>
				<div class="form-group">
					<div class="row">
						<label class="col-sm-4" style="text-align: left;">Connection Details</label>

						<div class="col-sm-6 text-center"  >
							<a class="form-control" v-on:click="showApplicationDetails">Click Here</a>
						</div>


					</div>
				</div>
			</btcl-field>
		</btcl-portlet>

		<btcl-portlet title="Documents" v-if="application.state.view=='ADVICE_NOTE'">
			<btcl-portlet>
				<div align="center">
					<button type="submit" class="btn green-haze" @click="redirect('advicenote')">View Advice Note</button>
				</div>
			</btcl-portlet>
		</btcl-portlet>
		<btcl-portlet title="Documents" v-if="application.state.view=='DEMAND_NOTE'">
			<btcl-portlet>
				<div align="center">
					<button type="submit" class="btn green-haze" @click="redirect('demandnote')">View Demand Note
					</button>
				</div>
			</btcl-portlet>
		</btcl-portlet>
		<btcl-portlet title="Available Actions" v-if="application.action.length>0">
			<btcl-portlet>
				<div>
					<ul style="list-style-type:none">
						<li v-for="(element,elementIndex) in application.action" :key="elementIndex" v-if="elementIndex>0">
							<label>
								<span><input type="radio" name="actionForwards" v-model="picked" :value="element.id"> {{element.description}}</span>
							</label>
						</li>
					</ul>
				</div>
				<hr>
				<button type="submit" class="btn green-haze btn-block" @click="nextStep">Submit</button>
			</btcl-portlet>
		</btcl-portlet>
	</btcl-body>


	<div class="modal fade" id="applicationDetailsModal" role="dialog">
		<div class="modal-dialog modal-lg" style="width: 50%;">

			<!-- Modal local loop selection-->
			<div class="modal-content">
				<div class="modal-body">
					<div>
						<div class="form-body">
							<btcl-portlet title=Connections>
								<btcl-field  v-for="(connection,index) in application.connections" :key="index">
									<btcl-info title="Connection Name" :text=connection.name></btcl-info>
									<btcl-info title="Connection Type" :text="connection.type"></btcl-info>
									<btcl-info title="Connection Bandwidth" :text="connection.bandwidth"></btcl-info>
								</btcl-field>
							</btcl-portlet>
						</div>
					</div>


				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
				</div>
			</div>

		</div>
	</div>
	<%--connection completion--%>
	<%--<jsp:include page="../../lli/application/new-connection/view/modals/lli-application-new-connection-view-modal-connection-complete.jsp"/>--%>
	<jsp:include page="../../../../lli/application/new-connection/view/modals/lli-application-new-connection-view-modal-advice-note.jsp"/>
</div>
<script>
	var applicationID = '<%=request.getParameter("id")%>';
</script>
		<%LoginDTO loginDTO = (LoginDTO) session.getAttribute(SessionConstants.USER_LOGIN);%>
	<script>
        var applicationID = parseInt('<%=request.getParameter("id")%>');
        var loggedInUserID = '<%=loginDTO.getUserID()%>';
        // debugger;

	</script>
<script>
    var vue = new Vue({
        el: "#btcl-application",
        data: {
            application:{
                srcClient: null,
                dstClient:null,
                comment: null,
                suggestedDate: null,
                description: null,
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
            comment: '',
			loading : false,
        },
        methods: {
            showApplicationDetails: function(){
                $('#applicationDetailsModal').modal({show: true});
            },
            nextStep: function(){
                const nextAction = this.application.action.find(obj => obj.id == this.picked);
                if(nextAction) var commentTitle = " Comment on " + nextAction.description;
                if (this.picked == this.NO_ACTION_PICKED) {errorMessage("Please Select an Action.");return;}
               	else if(nextAction.modal!="")$(nextAction.modal).modal({show: true});
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
                                this.loading = true;
                                this.comment = value + '';
                                this.postData(this.comment,nextAction);
                            }
                        });
            },
            redirect: function (url) {
                var location = context + 'lli/pdf/' + url + '.do?applicationid=' + applicationID;
                if (url === 'workorder') location += '&vendorid=' + loggedInUserID;
                if (url === 'demandnote') location = context + 'pdf/view/demand-note.do?billId=' + this.application.demandNoteID;
                if (url === 'advicenote') location = context + 'pdf/view/advice-note.do?appId=' + applicationID+"&module=7";
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
                var apps = {
                    applicationID: applicationID,
                    comment: this.comment,
                    nextState: this.picked,
                    application: this.application,
					// srcClient : this.application.srcClient,
					// dstClient: this.application.dstClient,
					// applicationType : this.application.applicationType,
                };
                var url1 = nextAction.url;
                // debugger;
				this.loading=true;
				Promise.resolve(
                axios.post(context + 'lli/ownershipChange/' + url1 + '.do', {'data': JSON.stringify(apps)})
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
                                redirect='lli/ownershipChange/search.do';
                            }
                            toastr.success("Your request has been processed", "Success");
                            window.location.href = context + redirect;
                        }
                        else {
                            toastr.error("Your request was not accepted", "Failure");
                        }
                        this.loading = false;
                    }).catch(function (error) {})).then(()=>this.loading=false);
            },
            forwardAdviceNote: function () {
                var comment = this.comment;
                var localLoops = {
                    applicationID: this.application.applicationID,
                    comment: comment,
                    nextState: this.picked,
                    userList: this.application.userList,
                    senderId: loggedInUserID
                };
                // debugger;
                var url1 = "advice-note-generate";
				this.loading=true;
				Promise.resolve(
                axios.post(context + 'lli/ownershipChange/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                    .then(result => {
                        if (result.data.responseCode == 2) {
                            toastr.error(result.data.msg);
                        } else if (result.data.responseCode == 1) {
                            toastr.success("Your request has been processed", "Success");
                            window.location.href = context + 'lli/ownershipChange/search.do';
                        } else {
                            toastr.error("Your request was not accepted", "Failure");
                        }
                        this.loading = false;
                    })
                    .catch(function (error) {
                    })).then(()=>this.loading=false);
            },
        },
        created() {
        	this.loading = true;
        	Promise.resolve(
            axios({method: 'GET', 'url': context + 'lli/ownershipChange/flow-data.do?id=' + applicationID})
                .then(result => {
                    if (result.data.payload.hasOwnProperty("members")) {
                        this.application = Object.assign(this.application, result.data.payload.members);
                    }
                    else {
                        this.application = Object.assign(this.application, result.data.payload);
                    }
                    this.application.connectionName = '';
                    this.application.applicationID = applicationID;
                    this.application.action.unshift({});
					/*if(loggedInAccount.registrantType != 1){
                        let DEMAND_NOTE_GENERATED_SKIP = 6019;
                        this.application.action = this.application.action.filter( a => a.id!=DEMAND_NOTE_GENERATED_SKIP);
                    }*/

                }, error => {
                })).then(()=>this.loading = false);
        },
    });
</script>