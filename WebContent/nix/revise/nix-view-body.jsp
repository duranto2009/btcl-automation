<%@ page import="login.LoginDTO" %>
<%@ page import="sessionmanager.SessionConstants" %>
<div id="btcl-application" v-cloak="true">
    <div id="appBody" style="display: none;">
        <btcl-body title="Revise Application" subtitle="Details" :loader="loading">
            <%--the following div is needed as one component can have one child--%>
            <div>
                <%--form elements description--%>
                <btcl-portlet>
                    <div>
                        <btcl-field v-for="(element,index) in application.formElements" :key="index">
                            <div class="form-group">
                                <div class=row>
                                    <label class="col-sm-4 control-label"
                                           style="text-align: left;">{{element.Label}}</label>
                                    <div class=col-sm-6>
                                        <p v-if="element.Label==='Status'" class="form-control" align="center"

                                           v-bind:style="{ background: application.color}"
                                        >
                                            <span style="color: white;font-weight: bold ;font-size: 18px!important;">{{element.Value}}</span>
                                        </p>
                                        <p v-else class="form-control"
                                           style="background:  #f2f1f1;text-align: center;">
                                            {{element.Value}}
                                        </p>


                                    </div>
                                </div>
                            </div>
                        </btcl-field>
                    </div>
                </btcl-portlet>


                <btcl-portlet v-if="
                application.state==STATE_FACTORY.DEMAND_NOTE_GENERATE.VALUE
                ">
                        <div align="center">
                            <button type="submit" class="btn green-haze" @click="redirect('demandnote')">View Demand
                                Note
                            </button>
                        </div>
                    </btcl-portlet>

                <%--action forward--%>
                <btcl-portlet>
                    <div>

                    <span><b>Available Actions :</b>

                        <%--{{ picked }}--%>

                    </span> <br/>

                        <div>
                            <%--<div v-for="element in application.action">--%>
                            <%--<label>--%>
                            <%--<span>--%>
                            <ul style="list-style-type:none">
                                <li v-for="element in application.action">
                                    <label><span><input type="radio" name="actionForwards" v-model="picked"
                                                        :value="element.Value"> {{element.Label}}</span></label>
                                </li>
                            </ul>
                            <%--</span>--%>
                            <%--</label> <br/>--%>
                            <%--</div>--%>
                        </div>
                        <jsp:include page="/nix/application/view/modals/nix-application-new-connection-view-modal-advice-note.jsp"/>
                        <button type="submit" class="btn green-haze btn-block" @click="nextStep">Submit</button>

                    </div>

                    <%--</btcl-field>--%>
                </btcl-portlet>

            </div>
        </btcl-body>
    </div>
</div>

<script src="../application/nix-application-components.js"></script>
<%
    LoginDTO loginDTO = (LoginDTO) session.getAttribute(SessionConstants.USER_LOGIN);
%>

<script>
    let applicationID = '<%=request.getParameter("id")%>';
    let loggedInUserID = '<%=loginDTO.getUserID()%>';
    let vue = new Vue({
        el: "#btcl-application",
        data: {
            td: {},
            application: {},
            picked: '0',
            comment: '',
            STATE_FACTORY: {
                NO_ACTION_PICKED: 0,
                TD_ADVICE_NOTE_GENERATE: {
                    URL: 'advicenotegenerate',
                    VALUE: 83,
                },
                DEMAND_NOTE_GENERATE: {
                    URL: 'nix/dn/preview.do?id=',
                    VALUE: 86,
                },
                COMPLETE_TD: {
                    URL: 'completetd',
                    VALUE: 84,
                },
                VERIFY_PAYMENT: {
                    URL: 'changenostate',
                    VALUE: 88,
                },
                RECONNECT_ADVICE_NOTE_GENERATE: {
                    URL: 'advicenotegenerate',
                    VALUE: 89,
                },
                COMPLETE_RECONNECTION: {
                    URL: 'completereconnection',
                    VALUE: 90,
                },

                GIVE_PAYMENT: {
                    URL: 'changenostate',
                    VALUE: 87
                },

                COMPLETE_LONG_TERM: {
                    URL: 'complete-longterm',
                    VALUE: 108
                },
                REJECT: {
                    URL: 'reject-longterm',
                    VALUE: 109
                },
                REOPEN: {
                    URL: 'reopen-longterm',
                    VALUE: 110
                },
            },
            loading:false,
        },
        methods: {
            redirect: function (url) {
                let location = '';
                if (url === 'workorder') location = context + 'pdf/view/work-order.do?appId=' + applicationID + '&module=9&vendorId=' + loggedInUserID;
                if (url === 'demandnote') location = context + 'pdf/view/demand-note.do?billId=' + this.application.demandNoteID;
                if (url === 'advicenote') location = context + 'pdf/view/advice-note.do?appId=' + applicationID + "&module=9";

                // window.location.href = location;
                window.open(
                    location,
                    '_blank'
                );
            },
            forwardAdviceNote: function() {
                let comment = this.comment;
                let params = {
                    applicationID: applicationID,
                    comment: comment,
                    nextState: this.picked,
                    userList: this.application.userList,
                    // pops: this.application.completeEFR,
                    senderId: loggedInUserID

                };
                // debugger;
                let desiredKey = Object.keys(this.STATE_FACTORY).filter(e=>this.STATE_FACTORY[e].VALUE == this.picked).shift();
                let url1 = this.STATE_FACTORY[desiredKey].URL;

                axios.post(context + 'nix/revise/' + url1 + '.do', {'data': JSON.stringify(params)})
                    .then(result => {
                        if (result.data.responseCode == 2) {
                            toastr.error(result.data.msg);
                        } else if (result.data.responseCode == 1) {
                            toastr.success("Your request has been processed", "Success");
                            window.location.href = context + 'nix/revise/search.do';
                        }
                        else {
                            toastr.error("Your request was not accepted", "Failure");
                        }
                        this.loading = false;
                    })
                    .catch(function (error) {
                    });
            },
            submitData: function () {
                let url = "tdrequest";
                axios.post(context + 'nix/revise/' + url + '.do', {'data': JSON.stringify(this.td)})
                    .then(result => {
                        if (result.data.responseCode == 2) {
                            toastr.error(result.data.msg);
                        } else if (result.data.responseCode == 1) {
                            toastr.success("Your request has been processed", "Success");
                            window.location.href = context + 'nix/revise/search.do';
                        }
                        else {
                            toastr.error("Your request was not accepted", "Failure");
                        }
                    })
                    .catch(function (error) {
                    });
                alert('form submit');
            },
            nextStep: function () {
                if (this.picked == this.STATE_FACTORY.NO_ACTION_PICKED) {
                    toastr.options.timeOut = 3000;
                    toastr.error("Please Select an Action.");
                    return;
                }

                if (this.picked == this.STATE_FACTORY.DEMAND_NOTE_GENERATE.VALUE) {
                    url1 = this.STATE_FACTORY.DEMAND_NOTE_GENERATE.URL + applicationID;
                    window.location.href = context + url1+"&nextstate="+this.STATE_FACTORY.DEMAND_NOTE_GENERATE.VALUE +"&appGroup=5";
                    return;
                }

                if(this.picked == this.STATE_FACTORY.TD_ADVICE_NOTE_GENERATE.VALUE
                    || this.picked == this.STATE_FACTORY.RECONNECT_ADVICE_NOTE_GENERATE.VALUE

                ){
                    $('#forwardAdviceNote').modal({show: true});
                    return;
                }


                if (

                     this.picked == this.STATE_FACTORY.COMPLETE_TD.VALUE
                    || this.picked == this.STATE_FACTORY.GIVE_PAYMENT.VALUE
                    || this.picked == this.STATE_FACTORY.VERIFY_PAYMENT.VALUE
                    || this.picked == this.STATE_FACTORY.COMPLETE_RECONNECTION.VALUE

                    || this.picked == this.STATE_FACTORY.REJECT.VALUE
                    || this.picked == this.STATE_FACTORY.REOPEN.VALUE

                ) {
                    swal("Comment:", {
                        content: "input",
                        showCancelButton:
                            true,
                        buttons:
                            true,
                    }).then((value) => {
                        if (value === false) return false;
                        if (value === "") {
                            swal("Warning!", "You need to write something!", "warning");
                            return false
                        }
                        if (value) {
                            this.comment = `${value}` + '';
                            let jsonData = {
                                applicationID: applicationID,
                                comment: this.comment,
                                nextState: this.picked,
                            };
                            let url1 = "";
                            if (this.picked == this.STATE_FACTORY.COMPLETE_TD.VALUE) {
                                url1 = this.STATE_FACTORY.COMPLETE_TD.URL;
                            }

                            else if (this.picked == this.STATE_FACTORY.COMPLETE_RECONNECTION.VALUE) {
                                url1 = this.STATE_FACTORY.COMPLETE_RECONNECTION.URL;
                            }
                            else if (
                                this.picked == this.STATE_FACTORY.GIVE_PAYMENT.VALUE
                                || this.picked == this.STATE_FACTORY.VERIFY_PAYMENT.VALUE
                            ) {
                                url1 = this.STATE_FACTORY.GIVE_PAYMENT.URL;
                            }

                            else if (this.picked == this.STATE_FACTORY.REJECT.VALUE) {
                                url1 = this.STATE_FACTORY.REJECT.URL;
                            }
                            else if (this.picked == this.STATE_FACTORY.REOPEN.VALUE) {
                                url1 = this.STATE_FACTORY.REOPEN.URL;
                            }
                           axios.post(context + 'nix/revise/' + url1 + '.do', {'data': JSON.stringify(jsonData)})
                                .then(result => {
                                    // debugger;
                                    console.log(result);
                                    if (result.data.responseCode == 2) {
                                        toastr.error(result.data.msg);
                                    } else if (result.data.responseCode == 1) {
                                        let redirect = '';
                                        if (this.picked == this.STATE_FACTORY.GIVE_PAYMENT.VALUE) {
                                            toastr.success("Your request has been processed", "Success");
                                            redirect = context + 'MultipleBillPaymentView.do?billIDs=' + this.application.demandNoteID;
                                        } else if (this.picked == this.STATE_FACTORY.VERIFY_PAYMENT.VALUE) {
                                            toastr.success("Your request has been processed", "Success");
                                            redirect = context + 'common/payment/linkPayment.jsp?paymentID=' + this.application.bill.paymentID + "&moduleID=" + this.application.moduleID;
                                        }
                                        else {
                                            toastr.success("Your request has been processed", "Success");
                                            redirect = context + 'nix/revise/search.do';
                                        }
                                        window.location.href = redirect;
                                    }
                                    else {
                                        toastr.error("Your request was not accepted", "Failure");
                                    }
                                }).catch(function (error) {
                            });
                        }
                    });
                }
            },
            viewDN: function () {
                window.location.href = context + "nix/pdf/view.do?type=demand-note&id=" + this.application.demandNoteID;
                //window.location.href = context + "nix/revise/search.do";
            }
        },
        created() {
            this.loading = true;
            Promise.resolve(
                axios({method: 'GET',    'url': context + 'nix/revise/revise-client-get-flow.do?id=' + applicationID})
                    .then(result => {
                        // this.application = result.data.payload.members;
                        if (result.data.payload.hasOwnProperty("members")) {

                            this.application = result.data.payload.members;
                        }
                        else {
                            this.application = result.data.payload;
                        }

                        $("#appBody").css("display", "block");
                    }, error => {
                    })
            ).then(()=>this.loading = false);

        }
    });
</script>