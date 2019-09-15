let vue = new Vue({
    el: "#btcl-nix-application",
    data: {
        contextPath: context,
        previousCode: false,
        shouldForward:true,
        application: {
            applicationType: {
                label: '',
            },
            action: [],
            connectionName: '#',
            officeList: [],
            ifr: [],
            state: 0,
            userList: [],
            loopProvider: '',
            zone:{},
            connectionType: '',
            content: "",
            localloops: [],
            portTypeString: null,
            existingPortType:null,
            exitingPortSelected:false,
            selectedNewPort:{},
            completeEFR:[],
            suggestedDate:''

        },
        //for correction
        nixOffices: [],
        nixConnections:[],
        nixExistingPorts:[],
        portTypeDropDown:[],

        //end correction
        isUpgrade: false,
        modifiedIFR: [],
        zoneList: [
            {nameEng: '', id: 1},
        ],
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
        content: "",
        comment: '',
        isIFR: false,
        isServerRoom: false,
        isAction: false,
        value: null,
        PORT_TYPE_FACTORY: {
            FE: 1,
            GE: 2,
            TEN_GE: 3,
        },
        TYPE_FACTORY: {
            NEW_CONNECTION: 1,
            UPGRADE_CONNECTION: 2,
            DOWNGRADE_CONNECTION: 3,
        },
        STATE_FACTORY: {
            NEW_APPLICATION_SUBMITTED: 5001,
            REJECT_APPLICATION: 5002,
            WITHOUT_LOOP_IFR_RESPONDED:5024,
            WITHOUT_LOOP_DEMAND_NOTE_GENERATED_SKIP: 5036,
            DEMAND_NOTE_GENERATED_SKIP: 5035,

            NIX_APPLICATION_IFR_RESPONDED: 5007,
            NIX_APPLICATION_EFR: 5008,
            NIX_EFR_REQUEST_BY_LDGM: 5029,
            NIX_APPLICATION_IFR: 5006,

            PAYMENT_VERIFIED: 5012,
            WITHOUT_LOOP_PAYMENT_VERIFIED: 5027,
            PAYMENT_VERIFIED_UPGRADE: 5037,
            PAYMENT_VERIFIED_DOWNGRADE: 5107,
            PAYMENT_VERIFIED_CLOSE: 5207,
            PAYMENT_VERIFIED_CLOSE_WITHOUT_WO:5257,
            PAYMENT_DONE: 5011,
            WITHOUT_LOOP_PAYMENT_DONE: 5026,
            PAYMENT_DONE_UPGRADE: 5036,
            PAYMENT_DONE_DOWNGRADE: 5106,
            PAYMENT_DONE_CLOSE: 5206,
            PAYMENT_DONE_CLOSE_WITHOUT_WO:5256,

            COMPLETE_TESTING: 5016,
            COMPLETE_TESTING_DOWNGRADE: 5109,

            CLOSE_CONNECTION_COMPLETE_APPLICATION: 5211,
            CLOSE_CONNECTION_COMPLETE_APPLICATION_WITHOUT_WO:5259,

            ADVICE_NOTE_GENERATE: 5015,
            WITHOUT_LOOP_ADVICE_NOTE_GENERATE: 5028,
            ADVICE_NOTE_GENERATE_UPGRADE: 5038,
            ADVICE_NOTE_GENERATE_DOWNGRADE: 5108,

            CLOSE_CONNECTION_ADVICE_NOTE_GENERATE: 5210,
            CLOSE_CONNECTION_ADVICE_NOTE_GENERATE_WITHOUT_WO:5258,

            WORK_ORDER_COMPLETE: 5014,
            WORK_ORDER_COMPLETE_FOR_LDGM: 5033,
            WORK_ORDER_FOR_CLOSE_COMPLETE: 5209,
            WORK_ORDER_GENERATE_BY_LDGM: 5032,
            WORK_ORDER_GENERATE: 5013,
            CLOSE_CONNECTION_WORK_ORDER_GENERATE: 5208,
            EFR_RESPONSE_FORWARD_TO_CDGM: 5031,
            WO_RESPONSE_FORWARD_TO_CDGM: 5034,
            NIX_APPLICATION_DEMAND_NOTE_GENERATED: 5010,
            NIX_UPGRADE_DEMAND_NOTE_GENERATED: 5035,
            REJECT_WO_AND_FORWARD_TO_VENDOR: 5023,
            WITHOUT_LOOP_DEMAND_NOTE: 5025,
            NIX_DOWNGRADE_DEMAND_NOTE: 5105,
            NIX_DOWNGRADE_DEMAND_NOTE_SKIP: 5110,
            NIX_CLOSE_DEMAND_NOTE: 5205,
            NIX_CLOSE_DEMAND_NOTE_SKIP: 5212,

            NIX_CLOSE_DEMAND_NOTE_WITHOUT_WO: 5255,

            NIX_EFR_RESPONSE: 5009,
            NIX_EFR_RESPONSE_TOLGDM: 5030,
            FORWARD_LDGM: 5017,
            FORWARD_LDGM_FOR_EFR: 5018,

            REQUESTED_CLIENT_FOR_CORRECTION: 5004,
            REQUESTED_CLIENT_FOR_CORRECTION_CLOSE: 5203,
            REQUESTED_CLIENT_FOR_CORRECTION_DOWNGRADE: 5103,

            APPLICATION_REOPEN: 5003,
            APPLICATION_REOPEN_CLOSE: 5202,
            APPLICATION_REOPEN_DOWNGRADE: 5102,

            CLIENT_CORRECTION_SUBMITTED: 5005,
            CLIENT_CORRECTION_SUBMITTED_CLOSE: 5204,
            CLIENT_CORRECTION_SUBMITTED_DOWNGRADE: 5104,

            ACCOUNT_CC_REQUEST: 5020,
            ACCOUNT_CC_RESPONSE_POSITIVE:5021,
            ACCOUNT_CC_RESPONSE_NEGETIVE:5022,
        },
        applicationFirstStateValue: -1,
        applicationSecondStateValue: -1,
        loading: true,
        display: 'none',
        ports: [],
        popList: [],
        alreadySelectedLoopLength: 0,
        ipRegionList: [],
        regionID: 0,
        ipVersion: 0,
        ipType: 0,
        ipBlockSize: 0,
        ipFreeBlockList: [],
        ipBlockID: null,
        ipRoutingInfo: '',
        ipAvailableRangleList: '',
        ipAvailableIPID: 0,
        ipAvailableSelected: [],
        loopProviderList: [{ID: 1, label: 'BTCL'}, {ID: 2, label: 'Client'}],
        region: [],
        isPageLoaded: false,
        fileUploadURL: '',
    },
    methods: {
        demandNoteGeneratedSkip: function (elem) {
            return (
                    elem.Value == this.STATE_FACTORY.WITHOUT_LOOP_DEMAND_NOTE_GENERATED_SKIP
                    || elem.Value == this.STATE_FACTORY.DEMAND_NOTE_GENERATED_SKIP
                )
                && this.application.skipPay == 0;
        },
        qDeadLine: function (elem) {
            return new Date(elem.quotationDeadline).toDateString();
        },
        previewConnection(connectionID) {
            window.location.href = context + 'nix/application/preview-connection.do?id=' + connectionID;
        },
        nextStep: function () {
            if (this.picked == this.STATE_FACTORY.NO_ACTION_PICKED) {
                toastr.options.timeOut = 3000;
                toastr.error("Please Select an Action.");
                return;
            }
            if (this.picked == this.STATE_FACTORY.REJECT_WO_AND_FORWARD_TO_VENDOR) {
                swal(" Comment:", {
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
                            this.loading = true;
                            this.comment = `${value}` + '';
                            var localLoops = {
                                applicationID: applicationID,
                                comment: this.comment,
                                nextState: this.picked,
                                application: this.application
                            };
                            var url1 = "rejectAndForwardtoVendor";

                            axios.post(context + 'nix/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                                .then(result => {
                                    if (result.data.responseCode == 2) {
                                        toastr.error(result.data.msg);
                                    } else if (result.data.responseCode == 1) {
                                        toastr.success("Your request has been processed", "Success");
                                        window.location.href = context + 'nix/application/search.do';
                                    } else {
                                        toastr.error("Your request was not accepted", "Failure");
                                    }
                                    this.loading = false;
                                })
                                .catch(function (error) {
                                    this.loading = false;

                                });
                        }
                    });
            }
            if (this.picked == this.STATE_FACTORY.CLOSE_CONNECTION_COMPLETE_APPLICATION||
                this.picked == this.STATE_FACTORY.CLOSE_CONNECTION_COMPLETE_APPLICATION_WITHOUT_WO
            ) {
                // alert("come");
                // this.connectionCompletionModal();
                swal(" Comment:", {
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
                            this.loading = true;
                            this.comment = `${value}` + '';
                            var localLoops = {
                                applicationID: applicationID,
                                comment: this.comment,
                                nextState: this.picked,
                                application: this.application
                            };
                            var url1 = "closeapplication";

                            axios.post(context + 'nix/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                                .then(result => {
                                    if (result.data.responseCode == 2) {
                                        toastr.error(result.data.msg);
                                    } else if (result.data.responseCode == 1) {
                                        toastr.success("Your request has been processed", "Success");
                                        window.location.href = context + 'nix/application/search.do';
                                    } else {
                                        toastr.error("Your request was not accepted", "Failure");
                                    }
                                    this.loading = false;
                                })
                                .catch(function (error) {
                                    this.loading = false;

                                });
                        }
                    });

            }
            if (this.picked == this.STATE_FACTORY.NIX_APPLICATION_IFR
                || this.picked == this.STATE_FACTORY.FORWARDED_IFR_REQUEST) {
                if (this.application.applicationType.ID == this.TYPE_FACTORY.UPGRADE_CONNECTION) {
                    $('#upgradeIFRModal').modal({show: true});
                } else {
                    $('#myModal').modal({show: true});
                }
            }
            if (this.picked == this.STATE_FACTORY.FORWARD_LDGM_FOR_EFR) {
                var isAnyLocalLoopSelected = false;
                for (var i = 0; i < this.application.ifr.length; i++) {
                    if (this.application.ifr[i].selected == 1) {
                        isAnyLocalLoopSelected = true;
                        break;
                    }
                }
                if(!isAnyLocalLoopSelected){
                    this.errorMessage("IFR is Negative or no POP is selected. If IFR is Negative, You can not proceed further. Please reject the application.");
                    return;
                }
                $('#requestToLocalDGMModal').modal({show: true});
            }
            if (this.picked == this.STATE_FACTORY.NIX_APPLICATION_EFR
                || this.picked == this.STATE_FACTORY.FORWARDED_REQUEST_EFR
                || this.picked == this.STATE_FACTORY.NIX_EFR_REQUEST_BY_LDGM) {
                var isAnyLocalLoopSelected = false;
                for (var i = 0; i < this.application.ifr.length; i++) {
                    if (this.application.ifr[i].selected == 1) {
                        isAnyLocalLoopSelected = true;
                        break;
                    }
                }
                if(!isAnyLocalLoopSelected){
                    this.errorMessage("IFR is Negative or no POP is selected. If IFR is Negative, You can not proceed further. Please reject the application.");
                    return;
                }
                if (this.application.applicationType.ID == this.TYPE_FACTORY.UPGRADE_CONNECTION) {
                    $('#ldgmModalUpgrade').modal({show: true});
                } else $('#ldgmModal').modal({show: true});
                //}
            }
            if (this.picked == this.STATE_FACTORY.NIX_APPLICATION_IFR_RESPONDED
                || this.picked == this.STATE_FACTORY.FORWARDED_IFR_RESPONSE
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_IFR_RESPONDED) {
                if (this.application.applicationType.ID == this.TYPE_FACTORY.UPGRADE_CONNECTION) {
                    $('#ifrrespondmodalUpgrade').modal({show: true});
                    return;
                } else $('#ifrrespondmodal').modal({show: true});
            }
            if (this.picked == this.STATE_FACTORY.NIX_EFR_RESPONSE
                || this.picked == this.STATE_FACTORY.FORWARDED_EFR_RESPONSE
                || this.picked == this.STATE_FACTORY.NIX_EFR_RESPONSE_TOLGDM) {
                $('#vendormodal').modal({show: true});
            }
            if (
                this.picked == this.STATE_FACTORY.PAYMENT_DONE
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_PAYMENT_DONE
                || this.picked == this.STATE_FACTORY.PAYMENT_DONE_CLOSE
                || this.picked == this.STATE_FACTORY.PAYMENT_DONE_CLOSE_WITHOUT_WO
                || this.picked == this.STATE_FACTORY.PAYMENT_DONE_UPGRADE
                || this.picked == this.STATE_FACTORY.PAYMENT_DONE_DOWNGRADE) {
                $('#paymentVerifiedModal').modal({show: true});
            }
            if (
                this.picked == this.STATE_FACTORY.PAYMENT_VERIFIED
                || this.picked == this.STATE_FACTORY.PAYMENT_VERIFIED_UPGRADE
                || this.picked == this.STATE_FACTORY.PAYMENT_VERIFIED_DOWNGRADE
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_PAYMENT_VERIFIED
                || this.picked == this.STATE_FACTORY.PAYMENT_VERIFIED_CLOSE
                || this.picked == this.STATE_FACTORY.PAYMENT_VERIFIED_CLOSE_WITHOUT_WO

            ) {
                $('#paymentVerifiedModal').modal({show: true});
            }
            if (this.picked == this.STATE_FACTORY.WORK_ORDER_GENERATE ||
                this.picked == this.STATE_FACTORY.WORK_ORDER_GENERATE_BY_LDGM ||
                this.picked == this.STATE_FACTORY.CLOSE_CONNECTION_WORK_ORDER_GENERATE) {
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
                            this.loading = true;
                            this.comment = `${value}` + '';
                            var localLoops = {
                                applicationID: applicationID,
                                comment: this.comment,
                                nextState: this.picked,
                            };
                            var url1 = "";
                            if (this.picked == this.STATE_FACTORY.WORK_ORDER_GENERATE ||
                                this.picked == this.STATE_FACTORY.WORK_ORDER_GENERATE_BY_LDGM) {
                                url1 = "workordergenerate";
                            } else if (this.picked == this.STATE_FACTORY.CLOSE_CONNECTION_WORK_ORDER_GENERATE) {
                                url1 = "closeworkordergenerate";
                            }
                            axios.post(context + 'nix/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                                .then(result => {
                                    if (result.data.responseCode == 2) {
                                        toastr.error(result.data.msg);
                                    } else if (result.data.responseCode == 1) {
                                        toastr.success("Your request has been processed", "Success");
                                        window.location.href = context + 'nix/application/search.do';
                                    } else {
                                        toastr.error("Your request was not accepted", "Failure");
                                    }
                                    this.loading = false;
                                })
                                .catch(function (error) {
                                    this.loading = false;
                                });
                        }
                    });
            }

            if (
                this.picked == this.STATE_FACTORY.CLIENT_CORRECTION_SUBMITTED
                || this.picked == this.STATE_FACTORY.CLIENT_CORRECTION_SUBMITTED_CLOSE
                || this.picked == this.STATE_FACTORY.CLIENT_CORRECTION_SUBMITTED_DOWNGRADE) {
                this.loading = true;
                this.application.nextState = this.picked;
                var url1 = "new-connection-edit";
                axios.post(context + 'nix/application/' + url1 + '.do', {'application': JSON.stringify(this.application)})
                    .then(result => {
                        if (result.data.responseCode == 2) {
                            toastr.error(result.data.msg);
                        } else if (result.data.responseCode == 1) {
                            toastr.success("Your request has been processed", "Success");
                            window.location.href = context + 'nix/application/search.do';
                        } else {
                            toastr.error("Your request was not accepted", "Failure");
                        }
                        this.loading = false;

                    })
                    .catch(function (error) {
                        this.loading = false;

                    });
            }
            if (this.picked == this.STATE_FACTORY.FORWARD_LDGM) {
                var commentTitle = "Write a Comment: ";
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
                            this.loading = true;
                            this.comment = `${value}` + '';
                            var data = {
                                applicationID: applicationID,
                                comment: this.comment,
                                nextState: this.picked,
                                application: this.application
                            };
                            var url1 = "forwardToLDGM";
                            axios.post(context + 'nix/application/' + url1 + '.do', {'data': JSON.stringify(data)})
                                .then(result => {
                                    if (result.data.responseCode == 2) {
                                        toastr.error(result.data.msg);
                                    } else if (result.data.responseCode == 1) {
                                        toastr.success("Your request has been processed", "Success");
                                        window.location.href = context + 'nix/application/search.do';
                                    } else {
                                        toastr.error("Your request was not accepted", "Failure");
                                    }
                                    this.loading = false;
                                })
                                .catch(function (error) {
                                    this.loading = false;

                                });
                        }
                    });
            }
            if (
                //reopen states
                this.picked == this.STATE_FACTORY.APPLICATION_REOPEN ||
                this.picked == this.STATE_FACTORY.APPLICATION_REOPEN_CLOSE ||
                this.picked == this.STATE_FACTORY.APPLICATION_REOPEN_DOWNGRADE ||
                //correction states
                this.picked == this.STATE_FACTORY.REQUESTED_CLIENT_FOR_CORRECTION ||
                this.picked == this.STATE_FACTORY.REQUESTED_CLIENT_FOR_CORRECTION_CLOSE ||
                this.picked == this.STATE_FACTORY.REQUESTED_CLIENT_FOR_CORRECTION_DOWNGRADE ||
                //forward states
                this.picked == this.STATE_FACTORY.RETRANSFER_TO_LOCAL_DGM ||
                this.picked == this.STATE_FACTORY.WITHOUT_LOOP_RETRANSFER_TO_LOCAL_DGM ||
                this.picked == this.STATE_FACTORY.FORWARDED_IFR_INCOMPLETE ||
                this.picked == this.STATE_FACTORY.WITHOUT_LOOP_FORWARDED_IFR_INCOMPLETE ||

                //Advice Note states
                this.picked == this.STATE_FACTORY.WORK_ORDER_FOR_CLOSE_COMPLETE
            ) {
                var commentTitle = "Write a Comment: ";
                if (
                    this.picked == this.STATE_FACTORY.APPLICATION_REOPEN
                    || this.picked == this.STATE_FACTORY.APPLICATION_REOPEN_CLOSE
                    || this.picked == this.STATE_FACTORY.APPLICATION_REOPEN_DOWNGRADE
                ) {
                    commentTitle = "Write Application Reopen Comment:";
                }
                if (this.picked == this.STATE_FACTORY.REQUESTED_CLIENT_FOR_CORRECTION)
                    commentTitle = "Write Reason for the Correction:";
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
                            this.loading = true;
                            this.comment = `${value}` + '';
                            var localLoops = {
                                applicationID: applicationID,
                                comment: this.comment,
                                nextState: this.picked,
                                applicationType: this.application.applicationType.ID
                            };
                            var url1 = "changestate";
                            if (
                                this.picked == this.STATE_FACTORY.RETRANSFER_TO_LOCAL_DGM ||
                                this.picked == this.STATE_FACTORY.WITHOUT_LOOP_RETRANSFER_TO_LOCAL_DGM
                            ) {
                                url1 = "retransfer";
                            }
                            axios.post(context + 'nix/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                                .then(result => {
                                    if (result.data.responseCode == 2) {
                                        toastr.error(result.data.msg);
                                    } else if (result.data.responseCode == 1) {
                                        toastr.success("Your request has been processed", "Success");
                                        window.location.href = context + 'nix/application/search.do';
                                    } else {
                                        toastr.error("Your request was not accepted", "Failure");
                                    }
                                    this.loading = false;
                                })
                                .catch(function (error) {
                                    this.loading = false;

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
                this.picked == this.STATE_FACTORY.ADVICE_NOTE_GENERATE
                || this.picked == this.STATE_FACTORY.ADVICE_NOTE_GENERATE_DOWNGRADE
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_ADVICE_NOTE_GENERATE
                || this.picked == this.STATE_FACTORY.CLOSE_CONNECTION_ADVICE_NOTE_GENERATE
                || this.picked == this.STATE_FACTORY.CLOSE_CONNECTION_ADVICE_NOTE_GENERATE_WITHOUT_WO
            ) {
                if(  this.picked == this.STATE_FACTORY.ADVICE_NOTE_GENERATE && this.application.state ==this.STATE_FACTORY.WORK_ORDER_COMPLETE ){
                    if(this.application.loopProvider.ID == 1){
                        let isWorkOrderSelected = false;

                        for(let i=0;i<this.application.completeEFR.length;i++){
                            if(this.application.completeEFR[i].approvedDistance == 1){
                                isWorkOrderSelected = true;
                                break;
                            }
                        }

                        if(!isWorkOrderSelected){
                            toastr.error("Atleast one workorder must be selected", "Failure");
                            return;
                        }
                    }
                }
                swal("Do you want to forward the advice note?", {
                    buttons: {
                        cancel: "NO",
                        proceed: {
                            text: "Proceed to select other user",
                            value: "YES",
                        },
                    },
                })
                    .then((value) => {
                        switch (value) {
                            case "YES":
                                this.shouldForward = true;
                                $('#forwardAdviceNote').modal({show: true});
                                break;
                            default:
                                this.shouldForward = false;
                                swal("Write Comment:", {
                                    content: "input",
                                    showCancelButton: true,
                                    buttons: true,
                                }).then((value) => {
                                    if (value === false) return false;
                                    if (value === "") {
                                        swal("Warning!", "You you need to write something!", "warning");
                                        return false
                                    }
                                    if (value) {
                                        this.loading = true;
                                        this.comment = `${value}` + '';
                                        this.forwardAdviceNote();
                                    }
                                });
                                break;


                        }
                    });
                //$('#forwardAdviceNote').modal({show: true});
            }


            if (
                this.picked == this.STATE_FACTORY.COMPLETE_TESTING
                || this.picked == this.STATE_FACTORY.COMPLETE_TESTING_DOWNGRADE
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_COMPLETE_TESTING
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_COMPLETE_TESTING_AND_CREATE_CONNECTION
            ) {
                if (this.application.applicationType.ID == this.TYPE_FACTORY.UPGRADE_CONNECTION
                    || this.application.applicationType.ID == this.TYPE_FACTORY.DOWNGRADE_CONNECTION) {
                    $('#serverRoomTestingCompleteUpgrade').modal({show: true});
                } else $('#serverRoomTestingComplete').modal({show: true});
            }
            if (this.picked == this.STATE_FACTORY.WORK_ORDER_COMPLETE
                || this.picked == this.STATE_FACTORY.WORK_ORDER_COMPLETE_FOR_LDGM) {
                $('#workOrderDetails').modal({show: true});
            }
            if (this.picked == this.STATE_FACTORY.NIX_APPLICATION_DEMAND_NOTE_GENERATED
                || this.picked == this.STATE_FACTORY.EFR_RESPONSE_FORWARD_TO_CDGM
                || this.picked == this.STATE_FACTORY.NIX_UPGRADE_DEMAND_NOTE_GENERATED
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_DEMAND_NOTE
                || this.picked == this.STATE_FACTORY.DEMAND_NOTE_GENERATED_SKIP
                || this.picked == this.STATE_FACTORY.NIX_DOWNGRADE_DEMAND_NOTE
                || this.picked == this.STATE_FACTORY.NIX_CLOSE_DEMAND_NOTE
                || this.picked == this.STATE_FACTORY.NIX_CLOSE_DEMAND_NOTE_WITHOUT_WO
                || this.picked == this.STATE_FACTORY.WO_RESPONSE_FORWARD_TO_CDGM

            ) {
                var url1 = "";
                var lebel = "Write A Comment";
                if (this.picked == this.STATE_FACTORY.NIX_APPLICATION_DEMAND_NOTE_GENERATED) {
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
                }

                else if (
                    this.picked == this.STATE_FACTORY.EFR_RESPONSE_FORWARD_TO_CDGM
                ) {

                        url1 = "forwarded-efr-select-only-loop";
                        lebel = "Write Forwarding Comment:"
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

                }
                else if (this.picked == this.STATE_FACTORY.WITHOUT_LOOP_DEMAND_NOTE) {

                    var isAnyLocalLoopSelected = false;
                    for (var i = 0; i < this.application.ifr.length; i++) {
                        if (this.application.ifr[i].selected == 1) {
                            isAnyLocalLoopSelected = true;
                            break;
                        }
                    }
                    if(!isAnyLocalLoopSelected){
                        this.errorMessage("IFR is Negative or no POP is selected. If IFR is Negative, You can not proceed further. Please reject the application.");
                        return;
                    }
                    url1 = "withoutloopdemandnotegenerate";
                    lebel = "Write Demand Note Generation Comment:"
                } else if (this.picked == this.STATE_FACTORY.WITHOUT_LOOP_DEMAND_NOTE_GENERATED_SKIP) {
                    url1 = "withoutloopskipdemandnotegenerate";
                    lebel = "Write Comment for Skipping Demand Note:"
                } else if (this.picked == this.STATE_FACTORY.DEMAND_NOTE_GENERATED_SKIP) {
                    url1 = "skipdemandnotegenerate";
                    lebel = "Write Comment for Skipping Demand Note:";
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
                } else if (this.picked == this.STATE_FACTORY.NIX_CLOSE_DEMAND_NOTE
                    || this.picked == this.STATE_FACTORY.NIX_CLOSE_DEMAND_NOTE_WITHOUT_WO
                ) {

                    url1 = "demandnoteforClose";
                    lebel = "Write Demand Note Generation Comment:"
                } else if (this.picked == this.STATE_FACTORY.NIX_CLOSE_DEMAND_NOTE_SKIP) {

                    url1 = "demandnoteskipforClose";
                    lebel = "Write Demand Note Generation Comment:"
                } else if (this.picked == this.STATE_FACTORY.NIX_UPGRADE_DEMAND_NOTE_GENERATED ||
                    this.picked == this.STATE_FACTORY.NIX_DOWNGRADE_DEMAND_NOTE) {

                    url1 = "without-IFR-EFR-WO-demandnotegenerate";
                    lebel = "Write Demand Note Generation Comment:"
                } else if (this.picked == this.STATE_FACTORY.NIX_DOWNGRADE_DEMAND_NOTE_SKIP) {

                    url1 = "demandnoteskipdowngrade";
                    lebel = "Write Demand Note Generation Comment:"
                }

                else if (this.picked == this.STATE_FACTORY.WO_RESPONSE_FORWARD_TO_CDGM) {
                    // alert();
                    if(this.application.loopProvider.ID==1) {
                        let oneWOSelected = false;

                        for (let i = 0; i < this.application.completeEFR.length; i++) {
                            if(this.application.completeEFR[i].hasOwnProperty("approvedDistance")){
                                if (this.application.completeEFR[i].approvedDistance == 1) {
                                    oneWOSelected = true;
                                    break;
                                }
                            }
                        }

                        if (!oneWOSelected) {
                            toastr.error("At least one work order must be selected to contnuew");
                            return;
                        }
                    }
                    url1 = "forward-after-work-order";
                    lebel = "Write Forwarding Comment:"
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
                            this.loading = true;
                            axios.post(context + 'nix/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                                .then(result => {
                                    if (result.data.responseCode == 2) {
                                        toastr.error(result.data.msg);
                                    } else if (result.data.responseCode == 1) {
                                        toastr.success("Your request has been processed", "Success");
                                        if (this.picked == this.STATE_FACTORY.NIX_APPLICATION_DEMAND_NOTE_GENERATED
                                            || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_DEMAND_NOTE
                                            || this.picked == this.STATE_FACTORY.DEMAND_NOTE_GENERATED_SKIP
                                            || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_DEMAND_NOTE_GENERATED_SKIP
                                            || this.picked == this.STATE_FACTORY.NIX_UPGRADE_DEMAND_NOTE_GENERATED
                                            || this.picked == this.STATE_FACTORY.NIX_DOWNGRADE_DEMAND_NOTE
                                            || this.picked == this.STATE_FACTORY.NIX_CLOSE_DEMAND_NOTE
                                            || this.picked == this.STATE_FACTORY.NIX_CLOSE_DEMAND_NOTE_WITHOUT_WO
                                        ) {
                                            let redirectUrl = context + 'nix/dn/preview.do?id=' + this.application.applicationID + '&nextstate=' + this.picked + "&appGroup=4";
                                            window.location.href = redirectUrl;
                                        }

                                        else if (
                                            this.picked == this.STATE_FACTORY.EFR_RESPONSE_FORWARD_TO_CDGM||
                                            this.picked == this.STATE_FACTORY.WO_RESPONSE_FORWARD_TO_CDGM
                                        ) {
                                            window.location.href = context + 'nix/application/search.do';
                                        }

                                    }

                                    else {
                                        toastr.error("Your request was not accepted", "Failure");
                                    }
                                    this.loading = false;
                                })
                                .catch(function (error) {
                                    this.loading = false;

                                });
                        }
                    });
            }


            if (this.picked == this.STATE_FACTORY.REJECT_APPLICATION
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_REJECT_APPLICATION) {
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
                            this.loading = true;
                            this.comment = `${value}` + '';
                            var localLoops = {
                                applicationID: applicationID,
                                comment: this.comment,
                                nextState: this.picked,
                            };
                            var url1 = "discard";
                            axios.post(context + 'nix/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                                .then(result => {
                                    if (result.data.responseCode == 2) {
                                        toastr.error(result.data.msg);
                                    } else if (result.data.responseCode == 1) {
                                        toastr.success("Your request has been processed", "Success");
                                        window.location.href = context + 'nix/application/search.do';
                                    } else {
                                        toastr.error("Your request was not accepted", "Failure");
                                    }
                                    this.loading = false;
                                })
                                .catch(function (error) {
                                    this.loading = false;

                                });

                        }
                    });
            }

            if (
                this.picked == this.STATE_FACTORY.ACCOUNT_CC_REQUEST
                || this.picked == this.STATE_FACTORY.CLOSE_CONNECTION_ACCOUNT_CC_REQUEST
            ) {
                swal("Comment", {
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
                            this.loading = true;
                            this.comment = `${value}` + '';
                            var localLoops = {
                                applicationID: applicationID,
                                comment: this.comment,
                                nextState: this.picked,
                            };
                            var url1 = "ccrequest";
                            axios.post(context + 'nix/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                                .then(result => {
                                    if (result.data.responseCode == 2) {
                                        toastr.error(result.data.msg);
                                    } else if (result.data.responseCode == 1) {
                                        toastr.success("Your request has been processed", "Success");
                                        window.location.href = context + 'nix/application/search.do';
                                    } else {
                                        toastr.error("Your request was not accepted", "Failure");
                                    }
                                    this.loading = false;
                                })
                                .catch(function (error) {
                                    this.loading = false;

                                });
                        }
                    });
            }
            if (
                this.picked == this.STATE_FACTORY.ACCOUNT_CC_RESPONSE_POSITIVE
                || this.picked == this.STATE_FACTORY.ACCOUNT_CC_RESPONSE_NEGETIVE
                || this.picked == this.STATE_FACTORY.CLOSE_CONNECTION_ACCOUNT_CC_RESPONSE_NEGATIVE
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_ACCOUNT_CC_RESPONSE_POSITIVE
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_ACCOUNT_CC_RESPONSE_NEGATIVE
                || this.picked == this.STATE_FACTORY.CLOSE_CONNECTION_ACCOUNT_CC_RESPONSE_POSITIVE
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
                            this.loading = true;
                            this.comment = `${value}` + '';
                            var localLoops = {
                                applicationID: applicationID,
                                comment: this.comment,
                                nextState: this.picked,
                            };
                            var url1 = "ccresponse";
                            axios.post(context + 'nix/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                                .then(result => {
                                    if (result.data.responseCode == 2) {
                                        toastr.error(result.data.msg);
                                    } else if (result.data.responseCode == 1) {
                                        toastr.success("Your request has been processed", "Success");
                                        window.location.href = context + 'nix/application/search.do';
                                    } else {
                                        toastr.error("Your request was not accepted", "Failure");
                                    }
                                    this.loading = false;
                                })
                                .catch(function (error) {
                                    this.loading = false;

                                });
                        }
                    });

            }
        },
        serverRoomNextStep: function () {
            var loopList = this.modifiedIFR;
            var localLoops = {
                applicationID: applicationID,
                comment: this.comment,
                pops: [],
                nextState: this.picked,
            };
            for (var i in loopList) {
                var item = loopList[i];
                if (item.office == '') {
                    this.errorMessage("Office must be selected.");
                    return;
                }
                if (item.pop == '') {
                    this.errorMessage("POP must be selected.");
                    return;
                }
                localLoops.pops.push({
                    "id": item.id,
                    "popID": item.pop.ID,
                    "selected": item.selected,
                    "officeId": item.office.id,
                });
            }
            if (localLoops.comment === "") {
                this.errorMessage("Please provide a comment.");
                return;
            }
            var url1 = "ifrupdate";
            // debugger;
            this.loading = true;
            axios.post(context + 'nix/application/' + url1 + '.do', {'ifr': JSON.stringify(localLoops)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = context + 'nix/application/search.do';
                    } else {
                        toastr.error("Your request was not accepted", "Failure");
                    }
                     this.loading = false;
                })
                .catch(function (error) {
                    console.log(error)
                    this.loading = false;
                });
        },
        testingCompleteNextStep: function () {
            if (this.comment == "") {
                this.errorMessage("Please Provide a Comment");
                return;
            }
            var loopList = this.application.ifr;
            for (var i = 0; i < this.application.localloops.length; i++) {
                var item = this.application.localloops[i];
                if (item.workCompleted==1&&(item.portID == null||item.portID==0)) {
                    this.errorMessage("Please Select a Port");
                    return;
                }
            }

            var localLoops = {
                applicationID: applicationID,
                comment: this.comment,
                localloops: this.application.localloops,
                nextState: this.picked,
                ip: this.ipAvailableSelected,
                application: this.application,
                connectionName: this.application.connectionName


            };
            // debugger;
            var url1 = "completetestingandcreateconnection";

            if (this.application.applicationType.ID == this.TYPE_FACTORY.UPGRADE_CONNECTION) {
                url1 = "completetestingandcreateconnectionUpgrade";
            }
            // var url1 = "completetesting";
            if (this.picked == this.STATE_FACTORY.WITHOUT_LOOP_COMPLETE_TESTING_AND_CREATE_CONNECTION) {
                url1 = "completetestingandcreateconnection";
            }
            this.loading = true;
            axios.post(context + 'nix/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = context + 'nix/application/search.do';
                    } else {
                        toastr.error("Your request was not accepted", "Failure");
                    }
                    this.loading = false;
                })
                .catch(function (error) {
                    this.loading = false;
                });
        },
        testingCompleteNextStepUpgrade: function () {

            if (this.comment == "") {
                this.errorMessage("Please Provide a Comment");
                return;
            }
            var item = this.application.localloops[0].portID;
            if (item == null) {
                this.errorMessage("Please Select new Port");
                return;
            }
            var localLoops = {
                applicationID: applicationID,
                comment: this.comment,
                newPortId: this.application.localloops[0].portID,
                popID: this.application.localloops[0].popId,
                nextState: this.picked,
                ip: this.ipAvailableSelected,
                application: this.application,
                localloops: this.application.localloops,
                connectionName: this.application.connectionName
            };
            // debugger;
            var url1 = "completetestingandcreateconnection";

            if (this.application.applicationType.ID == this.TYPE_FACTORY.UPGRADE_CONNECTION
                || this.application.applicationType.ID == this.TYPE_FACTORY.DOWNGRADE_CONNECTION) {
                url1 = "completetestingUpgradeOrDowngrade";
            }
            // var url1 = "completetesting";

            if (this.picked == this.STATE_FACTORY.WITHOUT_LOOP_COMPLETE_TESTING_AND_CREATE_CONNECTION) {
                url1 = "completetestingandcreateconnection";
            }
            this.loading = true;
            axios.post(context + 'nix/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = context + 'nix/application/search.do';
                    } else {
                        toastr.error("Your request was not accepted", "Failure");
                    }
                    this.loading = false;
                })
                .catch(function (error) {
                    this.loading = false;
                });
        },

        vendorNextStep: function () {
            var localLoops = {
                applicationID: applicationID,
                comment: this.comment,
                pops: this.application.incompleteEFR,
                nextState: this.picked,
            };
            for (var i in this.application.incompleteEFR) {
                var item = this.application.incompleteEFR[i];
                if (item.proposedDistance < 0) {
                    this.errorMessage("Positive Loop length must be provided.");
                    return;
                }
                else if (item.proposedDistance <= 0 && item.quotationStatus==1) {
                    this.errorMessage("Positive Loop length must be provided.");
                    return;
                } else if (item.ofcType == 0) {
                    this.errorMessage("Select an OFC Type.");
                    return;
                }
            }
            if (localLoops.comment === "") {
                this.errorMessage("Please provide a comment.");
                return;
            }
            var url1 = "efrupdate";
            this.loading = true;
            axios.post(context + 'nix/application/' + url1 + '.do', {'efr': JSON.stringify(localLoops)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = context + 'nix/application/search.do';
                    } else {
                        toastr.error("Your request was not accepted", "Failure");
                    }
                    this.loading = false;
                })
                .catch(function (error) {
                    this.loading = false;
                });
        },
        workOrderJobDoneNextState: function (completeEFR) {

            for(let i=0;i<completeEFR.length;i++){
                if(completeEFR[i].actualDistance <=0 && completeEFR.workGiven==1){
                    this.errorMessage("Please Provide a loop distance greater than zero");
                    return;
                }
            }
            if (this.comment == "") {
                this.errorMessage("Please Provide a Comment");
                return;
            }
            this.loading = true;
            var localLoops = {
                applicationID: applicationID,
                comment: this.comment,
                pops: completeEFR,
                nextState: this.picked,
            };
            // debugger;
            var url1 = "completeworkorder";
            axios.post(context + 'nix/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        toastr.options.timeOut = 3000;
                        window.location.href = context + 'nix/application/search.do';
                    } else {
                        toastr.error("Your request was not accepted", "Failure");
                    }
                    this.loading = false;

                })
                .catch(function (error) {
                    this.loading = false;
                });


        },
        paymentVerifiedNextStep: function () {
            this.loading = true;
            var localLoops = {
                applicationID: applicationID,
                comment: this.comment,
                nextState: this.picked,
            };
            var url1 = "demandnotepayment";
            axios.post(context + 'nix/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        var redirect = '';
                        if (this.picked == this.STATE_FACTORY.PAYMENT_DONE
                            || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_PAYMENT_DONE
                            || this.picked == this.STATE_FACTORY.PAYMENT_DONE_CLOSE
                            || this.picked == this.STATE_FACTORY.PAYMENT_DONE_CLOSE_WITHOUT_WO
                            || this.picked == this.STATE_FACTORY.PAYMENT_DONE_UPGRADE
                            || this.picked == this.STATE_FACTORY.PAYMENT_DONE_DOWNGRADE) {

                            redirect = context + 'MultipleBillPaymentView.do?billIDs=' + this.application.demandNoteID;
                        } else {
                            redirect = context + 'common/payment/linkPayment.jsp?paymentID=' + this.application.bill.paymentID + "&moduleID=" + this.application.moduleID;
                        }
                        window.location.href = redirect;
                    } else {
                        toastr.error("Your request was not accepted", "Failure");
                    }
                    this.loading = false;
                })
                .catch(function (error) {
                    this.loading = false;
                });
        },
        requestToLocalDGMModalSubmit: function () {
            this.loading = true;
            var localLoops = {
                applicationID: applicationID,
                comment: this.comment,
                nextState: this.picked,
                ifr: this.application.ifr,
                zoneID: this.application.zone.id,
            };
            var url1 = "applicationforward";
            axios.post(context + 'nix/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
            // axios.post(context + 'nix/application/' + url1 + '.do', {'ifr':JSON.stringify( this.content.officeList[0])})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = context + 'nix/application/search.do';
                    } else {
                        toastr.error("Your request was not accepted", "Failure");
                    }
                    this.loading = false;
                })
                .catch(function (error) {
                    this.loading = false;
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
        addLocalLoop: function (office) {
            office.localLoopList.push({
                ID: 0,
                vlanID: 0,
                OCDistance: 0,
                btclDistance: 0,
                clientDistance: 0,
                OC: undefined,
                ofcType: undefined,
                comment: '',
                officeid: 0,
                loopProvider: -1,
                popID: -1,
                src: -1,
                srcName: '',
                destination: -1,
                destinationName: '',
                vendorID: -1,


            });
        },
        serverRoomPOPAdd: function () {
            // alert('here');
            this.modifiedIFR.push({
                applicationID: 0,
                id: 0,
                isForwarded: 0,
                isReplied: 0,
                selected: 1,
                officeID: 0,
                parentIFRID: 0,
                popID: 0,
                pop: '',
                office: '',
                popName: "",
                serverRoomLocationID: 0,
                state: 0,
                submissionDate: 0

            });
        },
        serverRoomPOPDelete: function (index) {
            this.modifiedIFR.splice(index, 1);
        },
        ipAvailableDelete: function (index) {
            this.ipAvailableSelected.splice(index, 1);
        },
        deleteOffice: function (connection, officeIndex, event) {
            connection.officeList.splice(officeIndex, 1);
        },
        deleteLocalLoop: function (connection, officeIndex, localLoopIndex) {
            connection.officeList[officeIndex].localLoopList.splice(localLoopIndex, 1);
        },
        serverRoomNextStepUpgrade: function () {
            // debugger;
            var loopList = this.modifiedIFR;
            var localLoops = {
                applicationID: applicationID,
                comment: this.comment,
                pops: [],
                nextState: this.picked,
            };
            for (var i in loopList) {
                var item = loopList[i];
                if (item.office == '') {
                    this.errorMessage("Office must be selected.");
                    return;
                }
                if (item.pop == '') {
                    this.errorMessage("POP must be selected.");
                    return;
                }
                localLoops.pops.push({
                    "id": item.id,
                    "popID": item.pop.ID,
                    "selected": item.selected,
                    "officeId": item.office.id,
                });
            }
            if (localLoops.comment === "") {
                this.errorMessage("Please provide a comment.");
                return;
            }
            var url1 = "ifrupdateUpgrade";
            this.loading = true;
            axios.post(context + 'nix/application/' + url1 + '.do', {'ifr': JSON.stringify(localLoops)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = context + 'nix/application/search.do';
                    }
                    this.loading = false;

                })
                .catch(function (error) {
                    this.loading = false;

                });
        },
        process: function (url) {
            var loopList = this.content.officeList[0].localLoopList;
            if(loopList.length==0){
                this.errorMessage("Click Add Button on the right to select pop");
                return;
            }
            if (loopList.length < this.application.portCount)
            {
                $("#myModal").modal("hide");
                swal("Port count is not equal to pop count! What do you want to do?", {
                    buttons: {
                        cancel: "Select Again",
                        proceed: {
                            text: "Proceed with the selected pops",
                            value: "proceed",
                        },
                    },
                })
                    .then((value) => {
                        switch (value) {
                            case "proceed":
                                // swal("Proceed!", "with selected pops!", "success");
                                var localLoops = {
                                    applicationID: applicationID,
                                    comment: this.comment,
                                    pops: [],
                                    stateID: parseInt(this.picked),
                                    nextState: this.picked,


                                };
                                for (var i in loopList) {
                                    var item = loopList[i];
                                    if (item.officeid == 0) {
                                        this.errorMessage("Office must be selected.");
                                        return;
                                    } else if (!item.hasOwnProperty("pop")) {
                                        // toastr.options.timeOut = 3000;
                                        this.errorMessage("POP must be selected.");
                                        return;
                                    }
                                    item.ID = item.pop.ID;

                                    localLoops.pops.push({
                                        "popID": item.ID,
                                        "officeId": item.officeid,

                                    });
                                }
                                if (localLoops.comment === "") {
                                    this.errorMessage("Please provide a comment.");
                                    return;
                                }
                                // debugger;
                                this.loading = true;
                                var url1 = "ifrinsert";
                                axios.post(context + 'nix/application/' + url1 + '.do', {'ifr': JSON.stringify(localLoops)})
                                    .then(result => {
                                        if (result.data.responseCode == 2) {
                                            toastr.error(result.data.msg);
                                            toastr.options.timeOut = 3000;
                                        } else if (result.data.responseCode == 1) {
                                            toastr.success("Your request has been processed", "Success");
                                            toastr.options.timeOut = 3000;
                                            window.location.href = context + 'nix/application/search.do';
                                        } else {
                                            toastr.error("Your request was not accepted", "Failure");
                                            toastr.options.timeOut = 3000;
                                        }
                                        this.loading = false;

                                    })
                                    .catch(function (error) {
                                        this.loading = false;

                                    });
                                break;

                            default:
                                swal("Select again");
                        }
                    });

            }
            else if(loopList.length>this.application.portCount){
                this.errorMessage("Number of pop must be less then or equal to the number of port requested.");
                return;

            }
            else {
                var localLoops = {
                    applicationID: applicationID,
                    comment: this.comment,
                    pops: [],
                    stateID: parseInt(this.picked),
                    nextState: this.picked,


                };
                for (var i in loopList) {
                    var item = loopList[i];
                    if (item.officeid == 0) {
                        this.errorMessage("Office must be selected.");
                        return;
                    } else if (!item.hasOwnProperty("pop")) {
                        // toastr.options.timeOut = 3000;
                        this.errorMessage("POP must be selected.");
                        return;
                    }
                    item.ID = item.pop.ID;

                    localLoops.pops.push({
                        "popID": item.ID,
                        "officeId": item.officeid,

                    });
                }
                if (localLoops.comment === "") {
                    this.errorMessage("Please provide a comment.");
                    return;
                }
                // debugger;
                this.loading = true;
                var url1 = "ifrinsert";
                axios.post(context + 'nix/application/' + url1 + '.do', {'ifr': JSON.stringify(localLoops)})
                    .then(result => {
                        if (result.data.responseCode == 2) {
                            toastr.error(result.data.msg);
                            toastr.options.timeOut = 3000;
                        } else if (result.data.responseCode == 1) {
                            toastr.success("Your request has been processed", "Success");
                            toastr.options.timeOut = 3000;
                            window.location.href = context + 'nix/application/search.do';
                        } else {
                            toastr.error("Your request was not accepted", "Failure");
                            toastr.options.timeOut = 3000;
                        }
                        this.loading = false;

                    })
                    .catch(function (error) {
                        this.loading = false;
                    });
            }

        },
        processRequestForEFR: function (url) {
            // debugger;
            if (this.application.state == this.STATE_FACTORY.NIX_APPLICATION_IFR) {
                // alert('returned');
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
            var loopList = this.content.officeList[0].localLoopList;
            if (loopList.length == 0) {
                toastr.options.timeOut = 3000;
                toastr.error("Please click the green plus button to add a local loop.");
                return;
            }
            for (var i in loopList) {
                var item = loopList[i];
                if (item.popID == 0) {
                    this.errorMessage("POP must be selected.");
                    return;
                } else if (item.srcName == "" && item.src != 1) {
                    this.errorMessage("Source Name must be provided.");
                    return;
                } else if (item.destinationName == "" && item.destination != 3) {
                    this.errorMessage("Destination Name must be provided.");
                    return;
                } /*else if (item.vendorID == 0) {
                    this.errorMessage("Please Select A vendor");
                    return;
                } else if (item.ofcType == 0) {
                    this.errorMessage("Please Select A Ofc Type");
                    return;
                }*/

                else if (item.loopProvider == -1) {
                    this.errorMessage("Loop Provider must be provided.");
                    return;
                }
                else if (item.vendorID <= 0) {
                    this.errorMessage("Please Select A vendor");
                    return;
                }
                else if (item.ofcType <= 0) {
                    this.errorMessage("OFC type must be provided.");
                    return;
                }
                localLoops.pops.push({
                    "popID": item.popID,
                    "sourceType": item.src,
                    "source": item.srcName,
                    "destinationType": item.destination,
                    "destination": item.destinationName,
                    "vendorID": item.vendorID,
                    "vendorType": item.loopProvider,
                    "officeId": item.officeid,
                    "ofcType": item.ofcType,
                    "loopId": item.loopId,
                });
            }

            if (localLoops.comment === "") {
                this.errorMessage("Please provide a comment.");
                return;

            }
            var url1 = "efrinsert";
            this.loading = true;
            axios.post(context + 'nix/application/' + url1 + '.do', {'efr': JSON.stringify(localLoops)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        toastr.options.timeOut = 3000;
                        window.location.href = context + 'nix/application/search.do';
                    } else {
                        toastr.error("Your request was not accepted", "Failure");
                    }
                    this.loading = false;
                })
                .catch(function (error) {
                    this.loading = false;

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
            } else if (this.comment == "") {
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
            this.loading = true;
            axios.post(context + 'nix/application/' + url + '.do', {'data': JSON.stringify(localLoops)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = context + 'nix/application/search.do';
                    } else {
                        toastr.error("Your request was not accepted", "Failure");
                    }
                    this.loading = false;
                })
                .catch(function (error) {
                    this.loading = false;

                });
        },
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
        showOfficeDetails: function () {
            $('#showOfficeDetails').modal({show: true});
        },

        onChangeSwitchRouter: function (localLoop) {
            localLoop.filteredPorts = this.getFilteredPorts(localLoop);
            localLoop.filteredVlans = this.getFilteredVlans(localLoop);
            localLoop.vlanId = 0;
            localLoop.portId = 0;
        },

        getFilteredPorts(localLoop) {
            return localLoop.ports.filter(t=> t.parentID === localLoop.routerSwitchId);
        },

        getFilteredVlans(localLoop) {
            if(localLoop.vlanType === "2") {
                return localLoop.globalVlans;
            }
            return localLoop.vlans.filter( t=> t.parentID === localLoop.routerSwitchId);
        },

        onChangeVlanType: function(localLoop){
            localLoop.filteredVlans = this.getFilteredVlans(localLoop);
            localLoop.vlanId = 0;
        },
        searchIPBlock: function (data) {
            // this.loading = true;
            var regid = this.regionID;
            var ver = this.ipVersion;
            var typ = this.ipType;
            var blksz = this.ipBlockSize;
            if (regid == 0 || ver == 0 || typ == 0 || blksz == 0) {
                this.errorMessage("Please Provide all the information.");
                return;
            }
            var data = {
                "regionId": regid,
                "version": ver,
                "type": typ,
                "blockSize": parseInt(blksz),
                "moduleId": CONFIG.get("module", "nix")
            };
            var url = "inventory/suggestion";
           // this.loading = true;
            axios.post(context + 'ip/' + url + '.do', {'params': data})
                .then(result => {
                    this.ipBlockID = 0;
                    if (result.data.responseCode == 2) {
                        this.ipFreeBlockList = [];
                        this.ipRoutingInfo = '';
                        this.ipAvailableRangleList = '';
                        this.errorMessage(result.data.msg);
                    } else {
                        this.ipFreeBlockList = result.data.payload;
                        this.ipRoutingInfo = '';
                        this.ipAvailableRangleList = '';
                    }
                   // this.loading = false;

                })
                .catch(function (error) {
                   // this.loading = false;

                });
        },
        onchangeIPBlockRange: function (data) {
            var blockSize = this.ipBlockSize;
            var url = "get-free-block";
          //  this.loading = true;
            axios.post(context + 'ip/' + url + '.do', {
                "block": data.key,
                "size": blockSize
            })
                .then(result => {
                    this.ipAvailableRangleList = result.data.payload;
                    this.ipAvailableIPID = 0;
                    //this.loading = false;

                })
                .catch(function (error) {
                 //   this.loading = false;

                });
        },
        onchangeAvailableIP: function (data) {
            let isPresent = this.ipAvailableSelected.some(t => (t.fromIP === data.fromIP && t.toIP === data.toIP));
            if (isPresent) {
                return;
            }
            this.ipAvailableSelected.push(
                {
                    "fromIP": data.fromIP,
                    "toIP": data.toIP,
                    "version": this.ipVersion,
                    "type": this.ipType,
                    "regionId": this.regionID,
                }
            );
        },

        getPOPwithType: function (address, type) {
            if (type == 1) {
                return address + '(BTCL POP)';
            } else if (type == 2) {
                return address + '(BTCL LDP)';
            } else if (type == 3) {
                return address + '(Customer End)';

            } else if (type == 4) {
                return address + '(BTCL MUX)';
            }
        },
        forwardAdviceNote: function () {
            if(this.shouldForward) {
                if (this.application.userList.length == 0) {
                    this.errorMessage("At least one assigner should be selected.");
                    return;
                }
            }
            var comment = this.comment;
            var localLoops = {
                applicationID: applicationID,
                comment: comment,
                nextState: this.picked,
                userList: this.application.userList,
                pops: this.application.completeEFR,
                senderId: loggedInUserID
            };
            var url1 = "advicenotegenerate";
            this.loading = true;
            axios.post(context + 'nix/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        toastr.options.timeOut = 3000;
                        window.location.href = context + 'nix/application/search.do';
                    } else {
                        toastr.error("Your request was not accepted", "Failure");
                    }
                    this.loading = false;
                })
                .catch(function (error) {
                    this.loading = false;
                });
        },
        getPortOrVlanData: function (_category, _id) {
            var ports = [];
            var catagory = _category;
            var parent = _id;
            var data = {
                catagory: catagory,
                parent: parent
            };
            var url = "getchild";
            this.loading = true;
            axios.post(context + 'nix/application/' + url + '.do', {'data': JSON.stringify(data)})
                .then(result => {
                    console.log(result);

                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    } else if (result.data.responseCode == 1) {
                        ports = result.data.payload;
                        return ports;
                    } else {
                        toastr.error("Please Try Again.", "Failure");
                    }
                    this.loading = false;

                })
                .catch(function (error) {
                    this.loading = false;

                });
        },
        checkIfAlreadyToLDPSelected: function(currentLoopData, allLocalLoops, currentIndex){
            let returnValue = false;
            for(let i=0;i<currentIndex;i++){
                if(
                    allLocalLoops[i].officeid == currentLoopData.officeid
                    && allLocalLoops[i].popID == currentLoopData.popID
                    && allLocalLoops[i].destination == 2
                ){
                    this.content.officeList[0].localLoopList[currentIndex].srcName = allLocalLoops[i].destinationName;
                    returnValue = true;
                    break;
                }
            }
            return returnValue;
        },
        /*connectionSelectionCallback: function(connection){
            if(connection === undefined){
                this.officeList = [];
                this.application.office = undefined;
            } else{
                axios({ method: 'GET', 'url': context + 'nix/connection/get-officeObjectList-by-connection-id.do?id=' + connection.ID})
                    .then(result => {
                        if (result.data.responseCode == 2) {
                            toastr.error(result.data.msg);
                        } else if (result.data.responseCode == 1) {
                            this.nixOffices = result.data.payload;
                        }
                        else {
                            toastr.error("Your request was not accepted", "Failure");
                        }
                    }, error => {
                    });
            }
        },*/

       /* officeSelectionCallback: function(office){
            if(office === undefined){
                this.officeList = [];
                this.application.office = undefined;
            } else{
                axios({ method: 'GET', 'url': context + 'nix/connection/get-portList-by-connection-id.do?id=' + office.ID})
                    .then(result => {
                        if (result.data.responseCode == 2) {
                            toastr.error(result.data.msg);
                        } else if (result.data.responseCode == 1) {
                            this.nixExistingPorts = result.data.payload;
                        }
                        else {
                            toastr.error("Your request was not accepted", "Failure");
                        }
                    }, error => {
                    });
            }
        },
        portSelectionCallback: function(port){
            if(port === undefined){
                toastr.error("Your Port Is not found", "Failure");

            } else{
                this.exitingPortSelected = true;
                this.application.existingPortType = port.object.value;
            }
        },*/
    },
    created() {
        // this.loading = true;
        Promise.all(
             [
                 axios({method: 'GET', 'url': context + 'nix/application/new-connection-get-flow.do?id=' + applicationID})
                .then(result => {

                    if (result.data.responseCode == 1) {
                        if (result.data.payload.hasOwnProperty("members")) {
                            this.application = Object.assign(this.application, result.data.payload.members);
                        } else {
                            this.application = Object.assign(this.application, result.data.payload);
                        }
                        if (this.application.portType == this.PORT_TYPE_FACTORY.FE ||
                            this.application.newPortType == this.PORT_TYPE_FACTORY.FE) {
                            this.application.portTypeString = "FE";
                        }
                        if (this.application.portType == this.PORT_TYPE_FACTORY.GE ||
                            this.application.newPortType == this.PORT_TYPE_FACTORY.GE) {
                            this.application.portTypeString = "GE";
                        }
                        if (this.application.portType == this.PORT_TYPE_FACTORY.TEN_GE ||
                            this.application.newPortType == this.PORT_TYPE_FACTORY.TEN_GE) {
                            this.application.portTypeString = "10GE";
                        }
                        if (this.application.applicationType.ID == this.TYPE_FACTORY.UPGRADE_CONNECTION ||
                            this.application.applicationType.ID == this.TYPE_FACTORY.DOWNGRADE_CONNECTION) {
                            this.isUpgrade = true;
                        }
                        this.application.connectionName = '';
                        this.application.applicationID = applicationID;
                        this.fileUploadURL=context + "file/upload.do";
                        this.isPageLoaded = true;
                        this.application.vendorID = loggedInUserID;
                        if (this.application.hasOwnProperty("zone")) {
                            this.application.zone.id = this.application.zone.ID;
                            this.application.zone.nameEng = this.application.zone.label;
                            delete this.application.zone.ID;
                            delete this.application.zone.label;
                        }
                        if (this.application.action.length >= 2) {
                            this.applicationFirstStateValue = this.application.action[0].Value;
                            this.applicationSecondStateValue = this.application.action[1].Value;
                        }
                        this.setUpFreshContent();
                       /* if(this.application.state==this.STATE_FACTORY.REQUESTED_CLIENT_FOR_CORRECTION
                           || this.application.state==this.STATE_FACTORY.REQUESTED_CLIENT_FOR_CORRECTION_CLOSE
                           || this.application.state==this.STATE_FACTORY.REQUESTED_CLIENT_FOR_CORRECTION_DOWNGRADE){
                            axios({ method: 'GET', 'url': context + 'nix/connection/get-active-connection-by-client.do?id=' + this.application.client.ID})
                                .then(result => {
                                    if (result.data.responseCode == 2) {
                                        toastr.error(result.data.msg);
                                    } else if (result.data.responseCode == 1) {
                                        this.nixConnections = result.data.payload;
                                    }
                                    else {
                                        toastr.error("Your request was not accepted", "Failure");
                                    }
                                }, error => {
                                });
                        }*/

                        if(this.application.applicationType.ID==2||this.application.applicationType.ID==3){
                            this.application.existingPortType = this.application.oldPortInfo.value;
                        }
                        axios({method: 'GET', 'url': context + 'location/allzonesearch.do'})
                            .then(result => {
                                this.zoneList = result.data.payload;
                                //this.loading = false;
                            }, error => {
                            });
                        axios({method: "GET", "url": context + "lli/inventory/get-inventory-options.do?categoryID=" + 4})
                            .then(result => {
                                this.popList = result.data.payload;

                                    if (this.application.content === "") {
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
                                                    officeid: this.application.officeList[i].loops[j].officeId,
                                                    pop: this.popList.find(obj => {
                                                        return obj.ID === this.application.officeList[i].loops[j].popId
                                                    }),
                                                    src: -1,
                                                    srcName: '',
                                                    destination: -1,
                                                    destinationName: '',
                                                    vendorID: -1,
                                                    loopId: this.application.officeList[i].loops[j].id,
                                                });
                                            }
                                        }
                                    }
                                    this.officelist = this.application.officeList;
                                    if (this.application.hasOwnProperty("ifr")) {
                                        this.modifiedIFR = this.application.ifr.map(obj => {
                                            var newObj = obj;
                                            newObj.pop = this.popList.find(ob => {
                                                return ob.ID === obj.pop
                                            });
                                            newObj.office = this.officelist.find(ob => {
                                                return ob.id === obj.office
                                            });
                                            return newObj;
                                        });
                                    }
                                }, error => {
                                });
                            if (
                                this.application.state == this.STATE_FACTORY.ADVICE_NOTE_GENERATE
                                || this.application.state == this.STATE_FACTORY.ADVICE_NOTE_GENERATE_DOWNGRADE
                                || this.application.state == this.STATE_FACTORY.WITHOUT_LOOP_ADVICE_NOTE_GENERATE
                            ) {
                                var url = 'ip/region/getRegions.do';
                                axios({method: 'GET', 'url': context + url})
                                    .then(result => {
                                        this.ipRegionList = result.data.payload;
                                    }, error => {
                                    });
                            }



                        }
                        else {
                            toastr.error(result.data.msg,"Failure");
                        }


                    },
                    error => {
                        console.log(error);
                        // this.loading = false;
                    })]
        ).then((values)=> {
            this.application.localloops.forEach(localLoop=>{
                if(localLoop.routerSwitchId) {
                    localLoop.filteredPorts = this.getFilteredPorts(localLoop);
                    localLoop.filteredVlans = this.getFilteredVlans(localLoop);
                }

                let vlanId = localLoop.vlanId;
                if(localLoop.vlans.some(vlan=>vlan.ID === vlanId )){
                    localLoop.vlanType = 1;
                }else if(localLoop.globalVlans.some(gVlan=> gVlan.ID === vlanId)){
                    localLoop.vlanType = 2;
                }else {
                    localLoop.vlanType = 0;
                }
            });

            this.loading = false
        });


    },
    computed: {
        isUpgradeLoaded: function () {
            if (this.isUpgrade) {
                return true;
            } else return false;
        },
        noClientCorrectionNeeded: function () {
            return this.application.state != this.STATE_FACTORY.REQUESTED_CLIENT_FOR_CORRECTION
                && this.application.state != this.STATE_FACTORY.REQUESTED_CLIENT_FOR_CORRECTION_CLOSE
                && this.application.state != this.STATE_FACTORY.REQUESTED_CLIENT_FOR_CORRECTION_DOWNGRADE
        },
        ccCheck: function () {
            return this.application.state == this.STATE_FACTORY.ACCOUNT_CC_REQUEST
                || this.application.state == this.STATE_FACTORY.WITHOUT_LOOP_ACCOUNT_CC_REQUEST
        },
        ifrRequest: function () {
            return this.application.state == this.STATE_FACTORY.NIX_APPLICATION_IFR
                || this.application.state == this.STATE_FACTORY.FORWARDED_IFR_REQUEST


        },
        ifrResponse: function () {
            return this.application.state == this.STATE_FACTORY.NIX_APPLICATION_IFR_RESPONDED
                || this.application.state == this.STATE_FACTORY.FORWARD_LDGM_FOR_EFR
                || this.application.state == this.STATE_FACTORY.WITHOUT_LOOP_IFR_RESPONDED
                || this.application.state == this.STATE_FACTORY.WITHOUT_LOOP_RETRANSFER_TO_LOCAL_DGM
        },
        efrRequested: function () {
            return this.application.state == this.STATE_FACTORY.NIX_APPLICATION_EFR
                || this.application.state == this.STATE_FACTORY.ONLY_LOOP_FORWARDED_LDGM_REQUEST_EFR
                || this.application.state == this.STATE_FACTORY.FORWARDED_REQUEST_EFR
        },
        vendorResponseAndDemandNote: function () {
            return this.applicationFirstStateValue == this.STATE_FACTORY.DEMAND_NOTE
                || this.applicationSecondStateValue == this.STATE_FACTORY.DEMAND_NOTE
                || this.application.state == this.STATE_FACTORY.NIX_EFR_RESPONSE
                || this.application.state == this.STATE_FACTORY.NIX_EFR_RESPONSE_TOLGDM
                || this.application.state == this.STATE_FACTORY.EFR_RESPONSE_FORWARD_TO_CDGM
        },
        workOrderGenerate: function () {
            var x = parseInt(this.application.state) == parseInt(this.STATE_FACTORY.WORK_ORDER_GENERATE) ||
                parseInt(this.application.state) == parseInt(this.STATE_FACTORY.WORK_ORDER_GENERATE_BY_LDGM) ||
                parseInt(this.application.state) == parseInt(this.STATE_FACTORY.CLOSE_CONNECTION_WORK_ORDER_GENERATE)
            ;
            return x;
        },
        workOrderComplete: function () {
            return parseInt(this.application.state) == parseInt(this.STATE_FACTORY.WORK_ORDER_COMPLETE)
                || parseInt(this.application.state) == parseInt(this.STATE_FACTORY.WORK_ORDER_COMPLETE_FOR_LDGM)
                || parseInt(this.application.state) == parseInt(this.STATE_FACTORY.WO_RESPONSE_FORWARD_TO_CDGM);
        },
    },
    watch:{
        'application.existingPortType':function () {
            if(this.application.applicationType.ID==2){
                if(this.application.existingPortType == 'FE'){
                    vue.portTypeDropDown =[{ID:2,label:'GE'},{ID:3,label:'10GE'}];
                }
                else if(this.application.existingPortType == 'GE'){
                    vue.portTypeDropDown =[{ID:3,label:'10GE'}]
                }
                else {
                    vue.portTypeDropDown =[]
                }
            }
            else{
                if(this.application.existingPortType == 'FE'){
                    vue.portTypeDropDown =[];
                }
                else if(this.application.existingPortType == 'GE'){
                    vue.portTypeDropDown =[{ID:1,label:'FE'}]
                }
                else {
                    vue.portTypeDropDown =[{ID:1,label:'FE'},{ID:2,label:'GE'}]
                }
            }

        },
    }
});
