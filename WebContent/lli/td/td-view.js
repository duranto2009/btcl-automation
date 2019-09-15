var vue = new Vue({
    el: "#btcl-application",
    data: {
        contextPath: context,
        previousCode: false,
        application: {
            applicationType: {
                label: '',
            },
            action: [],
            connectionName: '#',
            officeList: [],
        },
        formElements: [
            {text: 'status', value: 'STATUS...TAB'},
            {text: 'One', value: 'A'},
        ],
        popsArray: [
            [
                {text: 'status', value: 'p1'},
                {text: 'One', value: 'A'},
            ],
            [
                {text: 'status', value: 'p2'},
                {text: 'Two', value: 'B'},
            ],
        ],
        picked: '0',
        content: {},
        comment: '',
        isIFR: false,
        isServerRoom: false,
        isAction: false,
        value: null,
        STATE_FACTORY: {
            IFR_WIP: 5,
            FORWARD_CDGM: 8,
            WITHOUT_LOOP_FORWARD_CDGM: 64,

            FORWARDED_LDGM_FORWARD_CDGM: 16,
            WITHOUT_LOOP_FORWARDED_LDGM_FORWARD_CDGM: 79,

            EFR_REQUESTED: 32,
            EFR_WIP: 20,
            EFR_DONE: 22,
            DEMAND_NOTE_GENERATED: 33,

            NEW_APPLICATION_SUBMITTED: 1,

            // LDGM_IFR_SUBMITTED: 30,
            // LDGM_FORWARDED_IFR_SUBMITTED: 47,
            ACCOUNT_CC_REQUEST: 34,
            WITHOUT_LOOP_ACCOUNT_CC_REQUEST: 58,

            ACCOUNT_CC_RESPONSE_POSITIVE: 37,
            WITHOUT_LOOP_ACCOUNT_CC_RESPONSE_POSITIVE: 61,

            ACCOUNT_CC_RESPONSE_NEGATIVE: 38,
            WITHOUT_LOOP_ACCOUNT_CC_RESPONSE_NEGATIVE: 62,

            REJECT_APPLICATION: 7,
            WITHOUT_LOOP_REJECT_APPLICATION: 57,

            IFR_REQUEST: 30,
            FORWARDED_IFR_REQUEST: 47,
            WITHOUT_LOOP_FORWARDED_IFR_REQUEST: 66,
            WITHOUT_LOOP_IFR_REQUEST: 55,

            IFR_RESPONSE: 31,
            FORWARDED_IFR_RESPONSE: 48,
            WITHOUT_LOOP_IFR_RESPONSE: 59,
            WITHOUT_LOOP_FORWARDED_IFR_RESPONSE: 67,

            REQUEST_EFR: 32,
            FORWARDED_REQUEST_EFR: 49,

            EFR_RESPONSE: 22,
            FORWARDED_EFR_RESPONSE: 50,

            DEMAND_NOTE: 33,
            WITHOUT_LOOP_DEMAND_NOTE: 63,

            WORK_ORDER_GENERATE: 39,
            WORK_ORDER_COMPLETE: 27,

            TD_ADVICE_NOTE_GENERATE: 29,
            WITHOUT_LOOP_ADVICE_NOTE_GENERATE: 73,


            COMPLETE_TESTING: 40,
            WITHOUT_LOOP_COMPLETE_TESTING: 74,


            CONNECTION_COMPLETED: 41,
            WITHOUT_LOOP_CONNECTION_COMPLETED: 76,

            PAYMENT_DONE: 24,
            WITHOUT_LOOP_PAYMENT_DONE: 71,

            PAYMENT_VERIFIED: 25,
            WITHOUT_LOOP_PAYMENT_VERIFIED: 72,

            APPLICATION_REOPEN: 42,
            WITHOUT_LOOP_APPLICATION_REOPEN: 77,

            NO_ACTION_PICKED: 0,

            REQUESTED_CLIENT_FOR_CORRECTION: 35,
            WITHOUT_LOOP_REQUESTED_CLIENT_FOR_CORRECTION: 56,

            CLIENT_CORRECTION_SUBMITTED: 3,
            WITHOUT_LOOP_CLIENT_CORRECTION_SUBMITTED: 60,

            FORWARD_TO_MUX_TEAM: 43,
            COMPLETE_MUX_CONFIGURATION: 44,
            CLIENT_NEW_CONNECTION: 19,
            TEST: -1,

            REQUESTED_TO_LOCAL_DGM: 51,
            WITHOUT_LOOP_REQUESTED_TO_LOCAL_DGM: 65,

            RETRANSFER_TO_LOCAL_DGM: 52,
            WITHOUT_LOOP_RETRANSFER_TO_LOCAL_DGM: 70,

            FORWARDED_IFR_INCOMPLETE: 53,
            WITHOUT_LOOP_FORWARDED_IFR_INCOMPLETE: 68,


            WITHOUT_LOOP_COMPLETE_TESTING_AND_CREATE_CONNECTION: 80,


        },
        applicationFirstStateValue: -1,
        applicationSecondStateValue: -1,
        loading: true,
        display: 'none',
        ports: [],
        vlans: [],
    },
    methods: {
        previewConnection(connectionID) {
            window.location.href = context + 'lli/application/preview-connection.do?id=' + connectionID;
        },
        nextStep: function () {
            if (this.picked == this.STATE_FACTORY.NO_ACTION_PICKED) {
                toastr.options.timeOut = 3000;
                toastr.error("Please Select an Action.");
                return;
            }
            if (this.picked == this.STATE_FACTORY.IFR_REQUEST
                || this.picked == this.STATE_FACTORY.FORWARDED_IFR_REQUEST
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_FORWARDED_IFR_REQUEST
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_IFR_REQUEST
            ) {
                $('#myModal').modal({show: true});
            }
            if (this.picked == this.STATE_FACTORY.REQUEST_EFR
                || this.picked == this.STATE_FACTORY.FORWARDED_REQUEST_EFR
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_DEMAND_NOTE
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_FORWARDED_LDGM_FORWARD_CDGM
            ) {
                var isAnyLocalLoopSelected = false;
                for (var i = 0; i < this.application.ifr.length; i++) {
                    if (this.application.ifr[i].isSelected == 1) {
                        isAnyLocalLoopSelected = true;
                        break;
                    }
                }
                if ((!isAnyLocalLoopSelected && this.STATE_FACTORY.IFR_RESPONSE == this.application.state)
                    || (!isAnyLocalLoopSelected && this.picked == this.STATE_FACTORY.WITHOUT_LOOP_DEMAND_NOTE &&this.application.state!=this.STATE_FACTORY.WITHOUT_LOOP_RETRANSFER_TO_LOCAL_DGM)
                    || (!isAnyLocalLoopSelected && this.picked == this.STATE_FACTORY.WITHOUT_LOOP_FORWARDED_LDGM_FORWARD_CDGM )
                ) {
                    this.errorMessage("At least one Local Loop must be selected.");
                    return;
                }
                if (
                    !(this.picked == this.STATE_FACTORY.WITHOUT_LOOP_DEMAND_NOTE) &&
                    !(this.picked == this.STATE_FACTORY.WITHOUT_LOOP_FORWARDED_LDGM_FORWARD_CDGM)

                ) {
                    $('#ldgmModal').modal({show: true});
                }
            }
            if (this.picked == this.STATE_FACTORY.IFR_RESPONSE
                || this.picked == this.STATE_FACTORY.FORWARDED_IFR_RESPONSE
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_IFR_RESPONSE
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_FORWARDED_IFR_RESPONSE
            ) {
                $('#ifrrespondmodal').modal({show: true});
            }
            if (this.picked == this.STATE_FACTORY.EFR_RESPONSE || this.picked == this.STATE_FACTORY.FORWARDED_EFR_RESPONSE) {
                $('#vendormodal').modal({show: true});
            }
            if (
                this.picked == this.STATE_FACTORY.PAYMENT_DONE
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_PAYMENT_DONE

            ) {
                $('#paymentVerifiedModal').modal({show: true});
            }
            if (
                this.picked == this.STATE_FACTORY.PAYMENT_VERIFIED
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_PAYMENT_VERIFIED
            ) {
                $('#paymentVerifiedModal').modal({show: true});
            }
            if (
                this.picked == this.STATE_FACTORY.REQUESTED_TO_LOCAL_DGM
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_REQUESTED_TO_LOCAL_DGM
            ) {
                // alert('came here');
                $('#requestToLocalDGMModal').modal({show: true});
            }
            if (this.picked == this.STATE_FACTORY.WORK_ORDER_GENERATE) {
                swal("Write Work Order Generate Comment:", {
                    content: "input",
                    showCancelButton: true,
                    buttons: true,
                })
                    .then((value) => {
                        if (value === false) return false;
                        if (value === "") {
                            swal("Warning!", "You you need to write something!", "warning");
                            return false
                        }
                        if (value) {
                            this.comment = `${value}` + '';
                            var localLoops = {
                                applicationID: applicationID,
                                comment: this.comment,
                                nextState: this.picked,
                            };
                            var url1 = "workordergenerate";
                            axios.post(context + 'lli/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                                .then(result => {
                                    if (result.data.responseCode == 2) {
                                        toastr.error(result.data.msg);
                                    } else if (result.data.responseCode == 1) {
                                        toastr.success("Your request has been processed", "Success");
                                        window.location.href = context + 'lli/application/search.do';
                                    }
                                    else {
                                        toastr.error("Your request was not accepted", "Failure");
                                    }
                                })
                                .catch(function (error) {
                                });
                        }
                    });
            }

            if (
                this.picked == this.STATE_FACTORY.CLIENT_CORRECTION_SUBMITTED
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_CLIENT_CORRECTION_SUBMITTED

            ) {
                this.application.nextState = this.picked;
                var url1 = "new-connection-edit";
                axios.post(context + 'lli/application/' + url1 + '.do', {'application': JSON.stringify(this.application)})
                    .then(result => {
                        if (result.data.responseCode == 2) {
                            toastr.error(result.data.msg);
                        } else if (result.data.responseCode == 1) {
                            toastr.success("Your request has been processed", "Success");
                            window.location.href = context + 'lli/application/search.do';
                        }
                        else {
                            toastr.error("Your request was not accepted", "Failure");
                        }
                    })
                    .catch(function (error) {
                    });
            }


            if (
                this.picked == this.STATE_FACTORY.APPLICATION_REOPEN ||
                this.picked == this.STATE_FACTORY.WITHOUT_LOOP_APPLICATION_REOPEN ||
                this.picked == this.STATE_FACTORY.REQUESTED_CLIENT_FOR_CORRECTION ||
                this.picked == this.STATE_FACTORY.WITHOUT_LOOP_REQUESTED_CLIENT_FOR_CORRECTION ||
                this.picked == this.STATE_FACTORY.FORWARD_TO_MUX_TEAM ||
                this.picked == this.STATE_FACTORY.COMPLETE_MUX_CONFIGURATION ||
                this.picked == this.STATE_FACTORY.FORWARD_CDGM ||
                this.picked == this.STATE_FACTORY.WITHOUT_LOOP_FORWARD_CDGM ||
                this.picked == this.STATE_FACTORY.RETRANSFER_TO_LOCAL_DGM ||
                this.picked == this.STATE_FACTORY.WITHOUT_LOOP_RETRANSFER_TO_LOCAL_DGM ||
                this.picked == this.STATE_FACTORY.FORWARDED_IFR_INCOMPLETE ||
                this.picked == this.STATE_FACTORY.WITHOUT_LOOP_FORWARDED_IFR_INCOMPLETE


            ) {
                var commentTitle = "Write a Comment: ";
                if (
                    this.picked == this.STATE_FACTORY.APPLICATION_REOPEN
                    || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_APPLICATION_REOPEN

                ) {
                    commentTitle = "Write Application Reopen Comment:";
                }
                if (this.picked == this.STATE_FACTORY.REQUESTED_CLIENT_FOR_CORRECTION) commentTitle = "Write Reason for the Correction:";
                // if (this.picked == this.STATE_FACTORY.FORWARD_TO_MASK_TEAM) commentTitle = "Write a Comment :";

                swal(commentTitle, {
                    content: "input",
                    showCancelButton: true,
                    buttons: true,
                })
                    .then((value) => {
                        if (value === false) return false;
                        if (value === "") {
                            swal("Warning!", "You you need to write something!", "warning");
                            return false
                        }
                        if (value) {
                            this.comment = `${value}` + '';


                            var localLoops = {
                                applicationID: applicationID,
                                comment: this.comment,
                                nextState: this.picked,
                            };
                            var url1 = "changestate";
                            if (this.picked == this.STATE_FACTORY.RETRANSFER_TO_LOCAL_DGM) {
                                url1 = "retransfer";
                            }
                            axios.post(context + 'lli/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                                .then(result => {
                                    if (result.data.responseCode == 2) {
                                        toastr.error(result.data.msg);
                                    } else if (result.data.responseCode == 1) {
                                        toastr.success("Your request has been processed", "Success");
                                        window.location.href = context + 'lli/application/search.do';
                                    }
                                    else {
                                        toastr.error("Your request was not accepted", "Failure");
                                    }
                                })
                                .catch(function (error) {
                                });
                        }
                    });
            }

            if (
                this.picked == this.STATE_FACTORY.CONNECTION_COMPLETED
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_CONNECTION_COMPLETED
            ) {
                $('#connectionCompletionModal').modal({show: true});

            }
            if (
                this.picked == this.STATE_FACTORY.TD_ADVICE_NOTE_GENERATE
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_ADVICE_NOTE_GENERATE

            ) {
                swal("Advice Note Generate Comment:", {
                    content: "input",
                    showCancelButton: true,
                    buttons: true,
                })
                    .then((value) => {
                        if (value === false) return false;
                        if (value === "") {
                            swal("Warning!", "You you need to write something!", "warning");
                            return false
                        }
                        if (value) {
                            this.comment = `${value}` + '';
                            var localLoops = {
                                applicationID: applicationID,
                                comment: this.comment,
                                nextState: this.picked,
                            };
                            var url1 = "advicenotegenerate";
                            axios.post(context + 'lli/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                                .then(result => {
                                    if (result.data.responseCode == 2) {
                                        toastr.error(result.data.msg);
                                    } else if (result.data.responseCode == 1) {
                                        toastr.success("Your request has been processed", "Success");
                                        window.location.href = context + 'lli/application/search.do';
                                    }
                                    else {
                                        toastr.error("Your request was not accepted", "Failure");
                                    }
                                })
                                .catch(function (error) {
                                });
                        }
                    });
            }

            if (
                this.picked == this.STATE_FACTORY.COMPLETE_TESTING
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_COMPLETE_TESTING
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_COMPLETE_TESTING_AND_CREATE_CONNECTION
            ) {
                $('#serverRoomTestingComplete').modal({show: true});
            }
            if (this.picked == this.STATE_FACTORY.WORK_ORDER_COMPLETE) {
                swal("Write Work Order Generate Comment:", {
                    content: "input",
                    showCancelButton: true,
                    buttons: true,
                })
                    .then((value) => {
                        if (value === false) return false;
                        if (value === "") {
                            swal("Warning!", "You you need to write something!", "warning");
                            return false
                        }
                        if (value) {
                            this.comment = `${value}` + '';
                            var localLoops = {
                                applicationID: applicationID,
                                comment: this.comment,
                                pops: this.application.completeEFR,
                                nextState: this.picked,
                            };
                            var url1 = "completeworkorder";
                            axios.post(context + 'lli/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                                .then(result => {
                                    if (result.data.responseCode == 2) {
                                        toastr.error(result.data.msg);
                                    } else if (result.data.responseCode == 1) {
                                        toastr.success("Your request has been processed", "Success");
                                        window.location.href = context + 'lli/application/search.do';
                                    }
                                    else {
                                        toastr.error("Your request was not accepted", "Failure");
                                    }
                                })
                                .catch(function (error) {
                                });
                        }
                    });
            }
            if (this.picked == this.STATE_FACTORY.DEMAND_NOTE
                || this.picked == this.STATE_FACTORY.FORWARDED_LDGM_FORWARD_CDGM
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_FORWARDED_LDGM_FORWARD_CDGM
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_DEMAND_NOTE
            ) {


                var url1 = "";
                var lebel = "Write A Comment";
                if (this.picked == this.STATE_FACTORY.DEMAND_NOTE) {
                    url1 = "demandnotegenerate";
                    lebel = "Write Demand Note Generation Comment:"
                    var isWorkEven = false;
                    for (var i = 0; i < this.application.completeEFR.length; i++) {
                        if (this.application.completeEFR[i].workGiven == 1) {
                            isWorkEven = true;
                            break;
                        }
                    }
                    if (!isWorkEven) {
                        this.errorMessage("At least one vendor must be selected to give work order.");
                        return;
                    }

                } else if (this.picked == this.STATE_FACTORY.FORWARDED_LDGM_FORWARD_CDGM) {
                    url1 = "forwardedefrselect";
                    lebel = "Write Forwarding Comment:"

                } else if (this.picked == this.STATE_FACTORY.WITHOUT_LOOP_DEMAND_NOTE) {
                    url1 = "withoutloopdemandnotegenerate";
                    lebel = "Write Demand Note Generation Comment:"
                } else if (this.picked == this.STATE_FACTORY.WITHOUT_LOOP_FORWARDED_LDGM_FORWARD_CDGM) {
                    url1 = "forwardedifrselect";
                    lebel = "Write Comment for CDGM:"
                }


                swal(lebel, {
                    content: "input",
                    buttons: true,
                })
                    .then((value) => {
                        if (value === false) return false;
                        if (value === "") {
                            swal("Warning!", "You you need to write something!", "warning");
                            return false
                        }


                        if (value) {
                            this.comment = `${value}` + '';

                            var localLoops = {
                                applicationID: applicationID,
                                comment: this.comment,
                                pops: this.application.completeEFR,
                                ifr: this.application.ifr,
                                nextState: this.picked,
                            };
                            axios.post(context + 'lli/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                                .then(result => {
                                    if (result.data.responseCode == 2) {
                                        toastr.error(result.data.msg);
                                    } else if (result.data.responseCode == 1) {
                                        toastr.success("Your request has been processed", "Success");
                                        if (this.picked == this.STATE_FACTORY.DEMAND_NOTE
                                            || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_DEMAND_NOTE
                                        ) {
                                            window.location.href = context + 'lli/dn/new.do?id=' + this.application.applicationID;
                                        } else if (
                                            this.picked == this.STATE_FACTORY.FORWARDED_LDGM_FORWARD_CDGM
                                            || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_FORWARDED_LDGM_FORWARD_CDGM

                                        ) {
                                            window.location.href = context + 'lli/application/search.do';

                                        }
                                    }
                                    else {
                                        toastr.error("Your request was not accepted", "Failure");
                                    }
                                })
                                .catch(function (error) {
                                });
                        }
                    });
            }


            if (
                this.picked == this.STATE_FACTORY.REJECT_APPLICATION
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_REJECT_APPLICATION
            ) {
                swal("Write Rejection Comment:", {
                    content: "input",
                    showCancelButton: true,
                    buttons: true,
                })
                    .then((value) => {
                        if (value === false) return false;
                        if (value === "") {
                            swal("Warning!", "You you need to write something!", "warning");
                            return false
                        }
                        if (value) {
                            this.comment = `${value}` + '';
                            var localLoops = {
                                applicationID: applicationID,
                                comment: this.comment,
                                nextState: this.picked,
                            };
                            var url1 = "discard";
                            axios.post(context + 'lli/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                                .then(result => {
                                    if (result.data.responseCode == 2) {
                                        toastr.error(result.data.msg);
                                    } else if (result.data.responseCode == 1) {
                                        toastr.success("Your request has been processed", "Success");
                                        window.location.href = context + 'lli/application/search.do';
                                    }
                                    else {
                                        toastr.error("Your request was not accepted", "Failure");
                                    }
                                })
                                .catch(function (error) {
                                });

                        }
                    });
            }

            if (
                this.picked == this.STATE_FACTORY.ACCOUNT_CC_REQUEST
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_ACCOUNT_CC_REQUEST
            ) {
                swal("Payment Request Comment", {
                    content: "input",
                    buttons: true,
                })
                    .then((value) => {
                        if (value === false) return false;
                        if (value === "") {
                            if (value === "") {
                                // swal.showInputError("You need to write something!");
                                swal("Warning!", "You you need to write something!", "warning");
                                return false
                            }
                            return false
                        }

                        if (value) {
                            this.comment = `${value}` + '';
                            var localLoops = {
                                applicationID: applicationID,
                                comment: this.comment,
                                nextState: this.picked,
                            };
                            var url1 = "ccrequest";
                            axios.post(context + 'lli/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                                .then(result => {
                                    if (result.data.responseCode == 2) {
                                        toastr.error(result.data.msg);
                                    } else if (result.data.responseCode == 1) {
                                        toastr.success("Your request has been processed", "Success");
                                        window.location.href = context + 'lli/application/search.do';
                                    }
                                    else {
                                        toastr.error("Your request was not accepted", "Failure");
                                    }
                                })
                                .catch(function (error) {
                                });
                        }
                    });
            }
            if (
                this.picked == this.STATE_FACTORY.ACCOUNT_CC_RESPONSE_POSITIVE
                || this.picked == this.STATE_FACTORY.ACCOUNT_CC_RESPONSE_NEGATIVE
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_ACCOUNT_CC_RESPONSE_POSITIVE
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_ACCOUNT_CC_RESPONSE_NEGATIVE
            ) {
                swal("Comment:", {
                    content: "input",
                    buttons: true,
                })
                    .then((value) => {
                        if (value === false) return false;
                        if (value === "") {
                            swal("Warning!", "You you need to write Comment!", "warning");
                            return false
                        }
                        if (value) {
                            this.comment = `${value}` + '';
                            var localLoops = {
                                applicationID: applicationID,
                                comment: this.comment,
                                nextState: this.picked,
                            };
                            var url1 = "ccresponse";
                            axios.post(context + 'lli/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                                .then(result => {
                                    if (result.data.responseCode == 2) {
                                        toastr.error(result.data.msg);
                                    } else if (result.data.responseCode == 1) {
                                        toastr.success("Your request has been processed", "Success");
                                        window.location.href = context + 'lli/application/search.do';
                                    }
                                    else {
                                        toastr.error("Your request was not accepted", "Failure");
                                    }
                                })
                                .catch(function (error) {
                                });
                        }
                    });

            }
        },
        serverRoomNextStep: function () {
            var loopList = this.application.ifr;
            var localLoops = {
                applicationID: applicationID,
                comment: this.comment,
                pops: [],
                nextState: this.picked,
            };
            for (var i in loopList) {
                var item = loopList[i];
                if (item.availableBW == 0) {
                }
                localLoops.pops.push({
                    "id": item.id,
                    "popID": item.popID,
                    "requestedBW": item.requestedBW,
                    "priority": item.priority,
                    "availableBW": parseInt(item.availableBW),
                    "isSelected": item.isSelected,
                    "officeId": item.officeID,
                });
            }
            if (localLoops.comment === "") {
                this.errorMessage("Please provide a comment.");
                return;
            }
            var url1 = "ifrupdate";
            axios.post(context + 'lli/application/' + url1 + '.do', {'ifr': JSON.stringify(localLoops)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = context + 'lli/application/search.do';
                    }
                    else {
                        toastr.error("Your request was not accepted", "Failure");
                    }
                })
                .catch(function (error) {
                });
        },
        testingCompleteNextStep: function () {
            if (this.comment == "") {
                this.errorMessage("Please Provide a Comment");
                return;
            }


            var loopList = this.application.ifr;

            var localLoops = {
                applicationID: applicationID,
                comment: this.comment,
                localloops: this.application.localloops,
                nextState: this.picked,


            };
            var url1 = "completetesting";

            if (this.picked == this.STATE_FACTORY.WITHOUT_LOOP_COMPLETE_TESTING_AND_CREATE_CONNECTION) {
                url1 = "completetestingandcreateconnection";
            }

            axios.post(context + 'lli/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = context + 'lli/application/search.do';
                    }
                    else {
                        toastr.error("Your request was not accepted", "Failure");
                    }
                })
                .catch(function (error) {
                });
        },
        vendorNextStep: function () {
            var loopList = this.application.ifr;
            var localLoops = {
                applicationID: applicationID,
                comment: this.comment,
                pops: this.application.incompleteEFR,
                nextState: this.picked,
            };
            for (var i in this.application.incompleteEFR) {
                var item = this.application.incompleteEFR[i];
                if (item.proposedLoopDistance == 0) {
                    this.errorMessage("Loop length must be provided.");
                    return;
                }
                else if (item.ofcType == 0) {
                    this.errorMessage("Select an OFC Type.");
                    return;
                }
            }
            if (localLoops.comment === "") {
                this.errorMessage("Please provide a comment.");
                return;
            }
            var url1 = "efrupdate";
            axios.post(context + 'lli/application/' + url1 + '.do', {'efr': JSON.stringify(localLoops)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = context + 'lli/application/search.do';
                    }
                    else {
                        toastr.error("Your request was not accepted", "Failure");
                    }
                })
                .catch(function (error) {
                });
        },
        paymentVerifiedNextStep: function () {
            var localLoops = {
                applicationID: applicationID,
                comment: this.comment,
                // pops: this.application.incompleteEFR,
                nextState: this.picked,


            };
            var url1 = "demandnotepayment";
            axios.post(context + 'lli/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
            // axios.post(context + 'lli/application/' + url1 + '.do', {'ifr':JSON.stringify( this.content.officeList[0])})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = context + 'lli/application/search.do';
                    }
                    else {
                        toastr.error("Your request was not accepted", "Failure");
                    }
                })
                .catch(function (error) {
                });


        },
        requestToLocalDGMModalSubmit: function () {
            var localLoops = {
                applicationID: applicationID,
                comment: this.comment,
                // pops: this.application.incompleteEFR,
                nextState: this.picked,
                zoneID: this.application.zone.id,


            };
            var url1 = "applicationforward";
            axios.post(context + 'lli/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
            // axios.post(context + 'lli/application/' + url1 + '.do', {'ifr':JSON.stringify( this.content.officeList[0])})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = context + 'lli/application/search.do';
                    }
                    else {
                        toastr.error("Your request was not accepted", "Failure");
                    }
                })
                .catch(function (error) {
                });


        },
        setUpFreshContent: function () {
            this.content = {
                ID: 0,
                name: '',
                client: this.application.client,
                bandwidth: this.application.bandwidth,
                connectionType: this.application.connectionType,
                officeList: [
                    {
                        ID: 0,
                        name: '',
                        address: this.application.address,
                        localLoopList: []
                    }
                ],
                incident: {ID: 1, label: 'New Connection'},
                CONTENTTYPE: 'connection'
            };
        },
        addOffice: function (connection, event) {
            connection.officeList.push({
                ID: 0, name: '', address: '', localLoopList: [],
            });
        },
        addLocalLoop: function (office, bw, event) {
            // alert('bw - > ' + bw);
            office.localLoopList.push({
                ID: 0,
                vlanID: 0,
                OCDistance: 0,
                btclDistance: 0,
                clientDistance: 0,
                OC: undefined,
                ofcType: undefined,
                comment: '',
                bandwidth: bw,
                priority: 0,
                officeid: -1,
                loopProvider: -1,


                popID: -1,
                src: -1,
                srcName: '',
                destination: -1,
                destinationName: '',
                vendorID: -1,


            });
        },
        deleteOffice: function (connection, officeIndex, event) {
            connection.officeList.splice(officeIndex, 1);
        },
        deleteLocalLoop: function (connection, officeIndex, localLoopIndex, event) {
            connection.officeList[officeIndex].localLoopList.splice(localLoopIndex, 1);
        },
        process: function (url) {
            var loopList = this.content.officeList[0].localLoopList;
            //start: validation code
            if (loopList.length == 0) {
                // alert('ehane dhuksi');
                toastr.options.timeOut = 3000;
                toastr.error("POP must be added.");
                return;
            }
            //end: validation code

            var localLoops = {
                applicationID: applicationID,
                comment: this.comment,
                pops: [],
                stateID: parseInt(this.picked),
                nextState: this.picked,


            };
            for (var i in loopList) {
                var item = loopList[i];
                //start: validation code
                if (item.ID == 0) {
                    // toastr.options.timeOut = 3000;
                    this.errorMessage("POP must be selected.");
                    return;
                }
                else if (item.priority == 0) {
                    this.errorMessage("Priority must be selected.");
                    return;
                }
                else if (item.officeid == 0) {
                    this.errorMessage("Office must be selected.");
                    return;
                }
                //end: validation code

                localLoops.pops.push({
                    "popID": item.ID,
                    "requestedBW": item.bandwidth,
                    "priority": item.priority,
                    "officeId": item.officeid,

                });
            }
            if (localLoops.comment === "") {
                this.errorMessage("Please provide a comment.");
                return;
            }
            var url1 = "ifrinsert";
            axios.post(context + 'lli/application/' + url1 + '.do', {'ifr': JSON.stringify(localLoops)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                        toastr.options.timeOut = 3000;
                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        toastr.options.timeOut = 3000;
                        window.location.href = context + 'lli/application/search.do';
                    }
                    else {
                        toastr.error("Your request was not accepted", "Failure");
                        toastr.options.timeOut = 3000;
                    }
                })
                .catch(function (error) {
                });


        },
        processRequestForEFR: function (url) {
            var loopList = this.content.officeList[0].localLoopList;
            if (loopList.length == 0) {
                toastr.options.timeOut = 3000;
                toastr.error("Please click the green plus button to add a local loop.");
                return;
            }

            var localLoops = {
                applicationID: applicationID,
                comment: this.comment,
                pops: [],
                stateID: parseInt(this.picked),
                nextState: this.picked,
                ifr: this.application.ifr,
            };
            for (var i in loopList) {
                var item = loopList[i];

                //start: validation code
                if (item.popID == 0) {
                    // toastr.options.timeOut = 3000;
                    this.errorMessage("POP must be selected.");
                    return;
                }
                else if (item.srcName == "" && item.src != 1) {
                    this.errorMessage("Source Name must be provided.");
                    return;
                }
                else if (item.destinationName == "" && item.destination != 3) {
                    this.errorMessage("Destination Name must be provided.");
                    return;
                }
                else if (item.vendorID == 0) {
                    this.errorMessage("Please Select A vendor");
                    return;
                }
                //end: validation code


                localLoops.pops.push({
                    "popID": item.popID,
                    "bandwidth": item.bandwidth,
                    "sourceType": item.src,
                    "source": item.srcName,
                    "destinationType": item.destination,
                    "destination": item.destinationName,
                    "vendorID": item.vendorID,
                    "vendorType": item.loopProvider,
                    "officeId": item.officeid,


                });
            }
            if (localLoops.comment === "") {
                this.errorMessage("Please provide a comment.");
                return;

            }
            var url1 = "efrinsert";
            axios.post(context + 'lli/application/' + url1 + '.do', {'efr': JSON.stringify(localLoops)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = context + 'lli/application/search.do';
                    }
                    else {
                        toastr.error("Your request was not accepted", "Failure");
                    }
                })
                .catch(function (error) {
                });
        },
        errorMessage: function (msg) {
            toastr.options.timeOut = 3000;
            toastr.error(msg);
            return;
        },
        connectionCompletionModal: function () {
            if (this.application.connectionName == "" && this.application.state == this.STATE_FACTORY.CONNECTION_COMPLETED) {
                this.errorMessage("A Connection Name must be provided.");
                return;
            }
            else if (this.comment == "") {
                this.errorMessage("Please Provide a Comment.");
                return;
            }
            var localLoops = {
                applicationID: applicationID,
                comment: this.comment,
                nextState: this.picked,
                application: this.application,
                connectionName: this.application.connectionName,

            };
            var url = "completeconnection";
            axios.post(context + 'lli/application/' + url + '.do', {'data': JSON.stringify(localLoops)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = context + 'lli/application/search.do';
                    }
                    else {
                        toastr.error("Your request was not accepted", "Failure");
                    }
                })
                .catch(function (error) {
                });
        },
        redirect: function (url) {
            var location = context + 'lli/pdf/' + url + '.do?applicationid=' + applicationID;
            if (url === 'workorder') location += '&vendorid=' + loggedInUserID;
            // http://localhost:8082/lli/pdf/view.do?type=demand-note&id=299009
            if (url === 'demandnote') location = '/lli/pdf/view.do?type=demand-note&id=' + this.application.demandNoteID;
            window.location.href = location;
        },
        showOfficeDetails: function(){
            // alert('Details of Office');
            $('#showOfficeDetails').modal({show: true});
            // showOfficeDetails
        },

        //In testing complete step, server room can select switch/router then related port and related vlan, here is the onchange for those fields
        onChangeSwitchRouter: function (data) {
            // alert('onchange switch/router:'+JSON.stringify(data));
            var catagory = 5;
            var parent = data;
            var data = {
                catagory: catagory,
                parent: parent
            };
            var url = "getchild";
            axios.post(context + 'lli/application/' + url + '.do', {'data': JSON.stringify(data)})
                .then(result => {
                    console.log(result);

                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    } else if (result.data.responseCode == 1) {
                        // toastr.success("Your request has been processed", "Success");
                        // this.application.ports=[];
                        // this.application.ports.concat(result.data.payload);
                        // Array.prototype.push.apply(this.application.ports,result.data.payload);
                        // console.log(result.data.payload);
                        // this.application.vlans = [];
                        // window.location.href = context + 'lli/application/search.do';
                        this.ports = result.data.payload;
                    }
                    else {
                        toastr.error("Your request was not accepted", "Failure");
                    }
                })
                .catch(function (error) {
                });


        },
        onchangePort: function (data) {

            // alert('onchange switch pop called -: ' + data);
            // alert('onchange prot:' + JSON.stringify(data));
            var catagory = 99;
            var parent = data;
            var data = {
                catagory: catagory,
                parent: parent
            };
            var url = "getchild";
            axios.post(context + 'lli/application/' + url + '.do', {'data': JSON.stringify(data)})
                .then(result => {
                    console.log(result);
                    // this.vlans = [];
                    // this.vlans.concat(result.data.payload);
                    this.vlans = result.data.payload;
                    // if (result.data.responseCode == 2) {
                    //     toastr.error(result.data.msg);
                    // } else if (result.data.responseCode == 1) {
                    //     // toastr.success("Your request has been processed", "Success");
                    //     this.application.vlan = result.data.payload;
                    //     // window.location.href = context + 'lli/application/search.do';
                    // }
                    // else {
                    //     toastr.error("Your request was not accepted", "Failure");
                    // }
                })
                .catch(function (error) {
                });

        },


    },
    created() {
        this.loading = true;
        axios({method: 'GET', 'url': context + 'lli/application/new-connection-get-flow.do?id=' + applicationID})
            .then(result => {
                // this.application = result.data.payload;
                // this.application = result.data.payload.members;
                if(result.data.payload.hasOwnProperty("members")){

                    this.application = result.data.payload.members;
                }
                else{
                    this.application = result.data.payload;
                }
                this.application.connectionName = "";
                this.application.vendorID = loggedInUserID;
                if (this.application.action.length >= 2) {
                    this.applicationFirstStateValue = this.application.action[0].Value;
                    this.applicationSecondStateValue = this.application.action[1].Value;
                }
                if (this.application.content === "") {
                    this.setUpFreshContent();


                    for (var i = 0; i < this.application.officeList.length; i++) {
                        for (var j = 0; j < this.application.officeList[i].loops.length; j++) {
                            this.content.officeList[0].localLoopList.push({
                                ID: 0,
                                vlanID: 0,
                                OCDistance: 0,
                                btclDistance: 0,
                                clientDistance: this.application.officeList[i].loops[j].clientDistances,
                                OC: undefined,
                                ofcType: this.application.officeList[i].loops[j].ofcType,
                                comment: '',
                                bandwidth: this.content.bandwidth,
                                priority: 0,
                                officeid: this.application.officeList[i].loops[j].officeID,


                                popID: 0,
                                src: -1,
                                srcName: '',
                                destination: -1,
                                destinationName: '',
                                vendorID: -1,
                            });
                        }
                    }



                } else {
                    // this.content = JSON.parse(result.data.payload.content);


                    /*
                     office.localLoopList.push({
                ID: 0,
                vlanID: 0,
                OCDistance: 0,
                btclDistance: 0,
                clientDistance: 0,
                OC: undefined,
                ofcType: undefined,
                comment: '',
                bandwidth: bw,
                priority: 0,
                officeid: 0,


                popID: 0,
                src: 0,
                srcName: '',
                destination: 0,
                destinationName: '',
                vendorID: 0,


            });
                     */
                }
                this.loading = false;
                $("#appBody").css("display", "block");
            }, error => {
            });
    }
});
