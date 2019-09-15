<%@ page import="login.LoginDTO" %>
<%@ page import="sessionmanager.SessionConstants" %>
<div id="btcl-application" v-cloak="true">
    <div id="appBody" style="display: none;">
        <btcl-body title="Revise" subtitle="">
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
                application.state == STATE_FACTORY.BREAK_LONG_TERM_APPROVED.VALUE
                || application.state == STATE_FACTORY.COMPLETE_RECONNECTION.VALUE

                ">
                    <div align="center">
                        <button type="submit" class="btn green-haze" @click="viewDN">View Demand Note</button>
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
                        <jsp:include page="/lli/application/new-connection/view/modals/lli-application-new-connection-view-modal-advice-note.jsp"/>
                        <button type="submit" class="btn green-haze btn-block" @click="nextStep">Submit</button>

                    </div>

                    <%--</btcl-field>--%>
                </btcl-portlet>

            </div>
        </btcl-body>
    </div>
</div>

<script src="../connection/lli-connection-components.js"></script>
<script src="../application/lli-application-components.js"></script>
<%
    LoginDTO loginDTO = (LoginDTO) session.getAttribute(SessionConstants.USER_LOGIN);
%>
<script>
    var applicationID = '<%=request.getParameter("id")%>';
    var loggedInUserID = '<%=loginDTO.getUserID()%>';

    var vue = new Vue({
        el: "#btcl-application",
        data: {
            td: {},
            application: {},
            picked: '0',
            comment: '',
            STATE_FACTORY: {
                NO_ACTION_PICKED: 0,
                BILLING_ADDRESS_CHANGE_APPROVED: {
                    URL: 'billing-address-change-complete',
                    VALUE: 121,
                },
                BILLING_ADDRESS_CHANGE_REJECTED: {
                    URL: 'billing-address-change-rejected',
                    VALUE: 122,
                },
                TD_ADVICE_NOTE_GENERATE: {
                    URL: 'advicenotegenerate',
                    VALUE: 83,
                },
                DEMAND_NOTE_GENERATE: {
                    URL: 'lli/dn/new-revise.do?id=',
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

                BREAK_LONG_TERM_APPROVED: {
                    URL: 'approve-break-longterm',
                    VALUE: 112
                },
                BREAK_LONG_TERM_REJECTED: {
                    URL: 'reject-break-longterm',
                    VALUE: 113
                },
                BREAK_LONG_TERM_REOPENED: {
                    URL: 'reopen-break-longterm',
                    VALUE: 114
                },
                LONG_TERM_PAYMENT_DONE: {
                    URL: 'changenostate',
                    VALUE: 115
                },
                LONG_TERM_PAYMENT_VERIFIED: {
                    URL: 'changenostate',
                    VALUE: 116
                },
                BREAK_LONG_TERM_COMPLETE: {
                    URL: 'complete-break-longterm',
                    VALUE: 117
                }
            }
        },
        methods: {
            forwardAdviceNote: function() {
                var comment = this.comment;
                var params = {
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

                axios.post(context + 'lli/revise/' + url1 + '.do', {'data': JSON.stringify(params)})
                    .then(result => {
                        if (result.data.responseCode == 2) {
                            toastr.error(result.data.msg);
                        } else if (result.data.responseCode == 1) {
                            toastr.success("Your request has been processed", "Success");
                            window.location.href = context + 'lli/revise/search.do';
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
                var url = "tdrequest";
                axios.post(context + 'lli/revise/' + url + '.do', {'data': JSON.stringify(this.td)})
                    .then(result => {
                        if (result.data.responseCode == 2) {
                            toastr.error(result.data.msg);
                        } else if (result.data.responseCode == 1) {
                            toastr.success("Your request has been processed", "Success");
                            window.location.href = context + 'lli/revise/search.do';
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

                if (this.picked == this.STATE_FACTORY.DEMAND_NOTE_GENERATE.VALUE
                    || this.picked == this.STATE_FACTORY.BREAK_LONG_TERM_APPROVED.VALUE
                ) {
                    url1 = this.STATE_FACTORY.DEMAND_NOTE_GENERATE.URL + applicationID;
                    window.location.href = context + url1;
                    return;
                }

                if (this.picked == this.STATE_FACTORY.LONG_TERM_PAYMENT_DONE.VALUE) {
                    url1 = 'MultipleBillPaymentView.do?billIDs=' + this.application.demandNoteID;
                    window.location.href = context + url1;
                    return;
                }

                if (this.picked == this.STATE_FACTORY.LONG_TERM_PAYMENT_VERIFIED.VALUE) {
                    url1 = 'common/payment/linkPayment.jsp?paymentID=' + this.application.bill.paymentID + "&moduleID=" + this.application.moduleID;
                    window.location.href = context + url1;
                    return;
                }

                if(this.picked == this.STATE_FACTORY.COMPLETE_LONG_TERM.VALUE
                    || this.picked == this.STATE_FACTORY.BREAK_LONG_TERM_COMPLETE.VALUE
                    || this.picked == this.STATE_FACTORY.TD_ADVICE_NOTE_GENERATE.VALUE
                    || this.picked == this.STATE_FACTORY.RECONNECT_ADVICE_NOTE_GENERATE.VALUE

                ){
                    // changed
                    $('#forwardAdviceNote').modal({show: true});
                    return;
                    // changed
                }


                if (

                     this.picked == this.STATE_FACTORY.COMPLETE_TD.VALUE
                    || this.picked == this.STATE_FACTORY.GIVE_PAYMENT.VALUE
                    || this.picked == this.STATE_FACTORY.VERIFY_PAYMENT.VALUE
                    || this.picked == this.STATE_FACTORY.COMPLETE_RECONNECTION.VALUE

                    || this.picked == this.STATE_FACTORY.REJECT.VALUE
                    || this.picked == this.STATE_FACTORY.REOPEN.VALUE
                    || this.picked == this.STATE_FACTORY.BREAK_LONG_TERM_APPROVED.VALUE
                    || this.picked == this.STATE_FACTORY.BREAK_LONG_TERM_REJECTED.VALUE
                    || this.picked == this.STATE_FACTORY.BREAK_LONG_TERM_REOPENED.VALUE
                    || this.picked == this.STATE_FACTORY.LONG_TERM_PAYMENT_DONE.VALUE
                    || this.picked == this.STATE_FACTORY.LONG_TERM_PAYMENT_VERIFIED.VALUE

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
                            var jsonData = {
                                applicationID: applicationID,
                                comment: this.comment,
                                nextState: this.picked,
                            };
                            var url1 = "";
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
                            else if (this.picked == this.STATE_FACTORY.BREAK_LONG_TERM_REJECTED.VALUE) {
                                url1 = this.STATE_FACTORY.BREAK_LONG_TERM_REJECTED.URL;
                            }
                            else if (this.picked == this.STATE_FACTORY.BREAK_LONG_TERM_REOPENED.VALUE) {
                                url1 = this.STATE_FACTORY.BREAK_LONG_TERM_REOPENED.URL;
                            }
                            else if (this.picked == this.STATE_FACTORY.LONG_TERM_PAYMENT_DONE.VALUE) {
                                url1 = this.STATE_FACTORY.LONG_TERM_PAYMENT_DONE.URL;
                            }
                            else if (this.picked == this.STATE_FACTORY.LONG_TERM_PAYMENT_VERIFIED.VALUE) {
                                url1 = this.STATE_FACTORY.LONG_TERM_PAYMENT_VERIFIED.URL;
                            }

                            // debugger;

                            axios.post(context + 'lli/revise/' + url1 + '.do', {'data': JSON.stringify(jsonData)})
                                .then(result => {
                                    // debugger;
                                    console.log(result);
                                    if (result.data.responseCode == 2) {
                                        toastr.error(result.data.msg);
                                    } else if (result.data.responseCode == 1) {
                                        var redirect = '';
                                        if (this.picked == this.STATE_FACTORY.GIVE_PAYMENT.VALUE) {
                                            toastr.success("Your request has been processed", "Success");
                                            redirect = context + 'MultipleBillPaymentView.do?billIDs=' + this.application.demandNoteID;
                                        } else if (this.picked == this.STATE_FACTORY.VERIFY_PAYMENT.VALUE) {
                                            toastr.success("Your request has been processed", "Success");
                                            redirect = context + 'common/payment/linkPayment.jsp?paymentID=' + this.application.bill.paymentID + "&moduleID=" + this.application.moduleID;
                                        }
                                        else {
                                            toastr.success("Your request has been processed", "Success");
                                            redirect = context + 'lli/revise/search.do';
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

                if (this.picked == this.STATE_FACTORY.BILLING_ADDRESS_CHANGE_APPROVED.VALUE
                || this.picked == this.STATE_FACTORY.BILLING_ADDRESS_CHANGE_REJECTED.VALUE) {
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
                            var jsonData = {
                                applicationID: applicationID,
                                comment: this.comment,
                                nextState: this.picked,
                            };
                            var url1='';
                            if (this.picked == this.STATE_FACTORY.BILLING_ADDRESS_CHANGE_APPROVED.VALUE ){
                                url1 = this.STATE_FACTORY.BILLING_ADDRESS_CHANGE_APPROVED.URL;
                            }
                            else if (this.picked == this.STATE_FACTORY.BILLING_ADDRESS_CHANGE_REJECTED.VALUE) {
                                url1 = this.STATE_FACTORY.BILLING_ADDRESS_CHANGE_REJECTED.URL;
                            }
                            axios.post(context + 'lli/revise/' + url1 + '.do', {'application': JSON.stringify(jsonData)})
                                .then(result => {
                                    console.log(result);
                                    if (result.data.responseCode == 2) {
                                        toastr.error(result.data.msg);
                                    } else if (result.data.responseCode == 1) {
                                        toastr.success("Your request has been processed", "Success");
                                        var redirect = context + 'lli/revise/search.do';
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
                window.location.href = context + "pdf/view/demand-note.do?billId=" + this.application.demandNoteID;
                //window.location.href = context + "lli/revise/search.do";
            }
        },
        created() {
            this.loading = true;
            axios({method: 'GET',    'url': context + 'lli/revise/revise-client-get-flow.do?id=' + applicationID})
                .then(result => {
                    // this.application = result.data.payload.members;
                    if (result.data.payload.hasOwnProperty("members")) {

                        this.application = result.data.payload.members;
                    }
                    else {
                        this.application = result.data.payload;
                    }
                    this.loading = false;
                    $("#appBody").css("display", "block");
                }, error => {
                });
        }
    });
</script>