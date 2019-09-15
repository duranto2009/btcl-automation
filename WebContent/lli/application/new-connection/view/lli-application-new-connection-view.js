import {ip2int} from "../../../../ip/js/ip-utility.js";

let vue = new Vue({
    el: "#btcl-application",
    data: {
        contextPath: context,
        previousCode: false,
        isNewLocalLoop: 7,
        shouldForward: true,
        application: {
            applicationType: {
                label: '',
            },
            action: [],
            connectionName: '#',
            officeList: [],
            newOfficeList: [],
            ifr: [],
            state: 0,
            userList: [],
            loopProvider: '',
            connectionType: '',
            content: "",
            localloops: [],
            zone: {},
            pop: {},
            suggestedDate: null,

        },
        modifiedIFR: [],
        modifiedIFRForNewLocalLoop: [],
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
        officeList: [],
        isServerRoom: false,
        isAction: false,
        value: null,
        TYPE_FACTORY: {
            ADDITIONAL_LOCAL_LOOP: 7,
            NEW_CONNECTION: 1,
            UPGRADE_CONNECTION: 2,
            NEW_CONNECTION_FROM_SHIFTING_BW: 101,
            ADDITIONAL_IP: 9,
            ADDITIONAL_PORT: 5,

        },

        STATE_FACTORY: {
            IFR_WIP: 5,
            FORWARD_CDGM: 8,
            WITHOUT_LOOP_FORWARD_CDGM: 64,
            FORWARD_FOR_WORK_ORDER: 19,


            FORWARDED_LDGM_FORWARD_CDGM: 16,
            WITHOUT_LOOP_FORWARDED_LDGM_FORWARD_CDGM: 79,
            ONLY_LOOP_FORWARDED_LDGM_FORWARD_CDGM: 3004,
            WORK_ORDER_FROM_LDGM_FORWARD_CDGM: 15,

            EFR_REQUESTED: 32,
            EFR_WIP: 20,
            EFR_DONE: 22,
            NEW_APPLICATION_SUBMITTED: 1,

            // LDGM_IFR_SUBMITTED: 30,
            // LDGM_FORWARDED_IFR_SUBMITTED: 47,
            ACCOUNT_CC_REQUEST: 34,
            WITHOUT_LOOP_ACCOUNT_CC_REQUEST: 58,
            CLOSE_CONNECTION_ACCOUNT_CC_REQUEST: 1003,

            ACCOUNT_CC_RESPONSE_POSITIVE: 37,
            WITHOUT_LOOP_ACCOUNT_CC_RESPONSE_POSITIVE: 61,
            ACCOUNT_CC_RESPONSE_POSITIVE_DOWNGRADE: 1006,
            ACCOUNT_CC_RESPONSE_NEGATIVE: 38,
            WITHOUT_LOOP_ACCOUNT_CC_RESPONSE_NEGATIVE: 62,
            CLOSE_CONNECTION_ACCOUNT_CC_RESPONSE_NEGATIVE: 1007,

            REJECT_APPLICATION: 7,
            WITHOUT_LOOP_REJECT_APPLICATION: 57,
            DOWNGRADE_POLICY_REJECT_APPLICATION: 1202,
            CLOSE_CONNECTION_REJECT_APPLICATION: 1002,
            CLOSE_CONNECTION_POLICY_REJECT_APPLICATION: 1103,
            CLOSE_CONNECTION_POLICY_BYPASS_REJECT_APPLICATION: 1105,
            SHIFT_BW_REJECT_APPLICATION: 2002,
            SHIFT_BW_REJECT_APPLICATION_NEW: 2102,
            WITHOUT_LOOP_SHIFT_BW_REJECT_APPLICATION_NEW: 2202,
            SHIFT_BW_REJECT_APPLICATION_CENTRAL: 2005,
            SHIFT_BW_REJECT_APPLICATION_CENTRAL_NEW: 2105,
            WITHOUT_LOOP_SHIFT_BW_REJECT_APPLICATION_CENTRAL_NEW: 2205,
            SHIFT_BW_REJECT_APPLICATION_DESINATION_DGM: 2013,

            IFR_REQUEST: 30,
            FORWARDED_IFR_REQUEST: 47,
            WITHOUT_LOOP_FORWARDED_IFR_REQUEST: 66,
            WITHOUT_LOOP_IFR_REQUEST: 55,
            SHIFT_BW_IFR_REQUEST: 2008,

            IFR_RESPONSE: 31,
            FORWARDED_IFR_RESPONSE: 48,
            WITHOUT_LOOP_IFR_RESPONSE: 59,
            WITHOUT_LOOP_FORWARDED_IFR_RESPONSE: 67,
            SHIFT_BW_IFR_RESPONSE: 2009,

            REQUEST_EFR: 32,
            FORWARDED_REQUEST_EFR: 49,
            ONLY_LOOP_FORWARDED_LDGM_REQUEST_EFR: 3002,

            EFR_RESPONSE: 22,
            FORWARDED_EFR_RESPONSE: 50,
            ONLY_LOOP_FORWARDED_EFR_RESPONSE: 3003,

            DEMAND_NOTE: 33,
            WITHOUT_LOOP_DEMAND_NOTE: 63,
            WITHOUT_LOOP_DEMAND_NOTE_GENERATED_SKIP: 92,
            DEMAND_NOTE_GENERATED_SKIP: 91,
            CLOSE_CONNECTION_DEMAND_NOTE: 1005,


            WORK_ORDER_GENERATE: 39,
            FORWARDED_WORK_ORDER_GENERATE: 17,
            CLOSE_CONNECTION_WORK_ORDER_GENERATE: 1012,


            WORK_ORDER_COMPLETE: 27,
            FORWARDED_WORK_ORDER_COMPLETE: 18,
            CLOSE_CONNECTION_WORK_ORDER_COMPLETE: 1013,

            ADVICE_NOTE_GENERATE: 29,
            AN_GENERATE_AND_FORWARD: 36,
            WITHOUT_LOOP_ADVICE_NOTE_GENERATE: 73,
            LLI_WITHOUT_LOOP_ADVICE_NOTE_PUBLISH_AND_FORWARD: 93,

            CLOSE_CONNECTION_ADVICE_NOTE_GENERATE: 1014,
            CLOSE_CONNECTION_ADVICE_NOTE_GENERATE_AND_FORWARD: 1018,

            SHIFT_BW_ADVICE_NOTE_GENERATE: 2010,
            SHIFT_BW_ADVICE_NOTE_GENERATE_AND_FORWARD: 2016,


            COMPLETE_TESTING: 41,
            SEND_AN_TO_SERVER_ROOM: 40,
            WITHOUT_LOOP_SEND_AN_TO_SERVER_ROOM: 74,
            CLOSE_CONNECTION_SEND_AN_TO_SERVER_ROOM: 1015,
            SHIFT_BW_SEND_AN_TO_SERVER_ROOM: 2011,


            WITHOUT_LOOP_COMPLETE_TESTING: 80,


            // CONNECTION_COMPLETED: 41,
            WITHOUT_LOOP_CONNECTION_COMPLETED: 76,

            SHIFT_BW_COMPLETE_PROCESS: 2012,

            PAYMENT_DONE: 24,
            WITHOUT_LOOP_PAYMENT_DONE: 71,
            CLOSE_CONNECTION_PAYMENT_DONE: 1008,

            PAYMENT_VERIFIED: 25,
            WITHOUT_LOOP_PAYMENT_VERIFIED: 72,
            CLOSE_CONNECTION_PAYMENT_VERIFIED: 1009,

            APPLICATION_REOPEN: 42,
            WITHOUT_LOOP_APPLICATION_REOPEN: 77,
            DOWNGRADE_POLICY_APPLICATION_REOPEN: 1203,
            CLOSE_CONNECTION_APPLICATION_REOPEN: 1011,
            CLOSE_CONNECTION_POLICY_APPLICATION_REOPEN: 1104,
            CLOSE_CONNECTION_POLICY_BYPASS_APPLICATION_REOPEN: 1106,
            SHIFT_BW_APPLICATION_REOPEN_LOCAL: 2006,
            SHIFT_BW_APPLICATION_REOPEN_LOCAL_NEW: 2106,
            WITHOUT_LOOP_SHIFT_BW_APPLICATION_REOPEN_LOCAL_NEW: 2206,
            SHIFT_BW_APPLICATION_REOPEN_CENTRAL: 2007,
            SHIFT_BW_APPLICATION_REOPEN_CENTRAL_NEW: 2107,
            WITHOUT_LOOP_SHIFT_BW_APPLICATION_REOPEN_CENTRAL_NEW: 2207,


            SHIFT_BW_APPROVE_LOCAL: 2003,
            SHIFT_BW_APPROVE_LOCAL_NEW: 2103,
            WITHOUT_LOOP_SHIFT_BW_APPROVE_LOCAL_NEW: 2203,
            SHIFT_BW_APPROVE_CENTRAL: 2004,
            SHIFT_BW_APPROVE_CENTRAL_NEW: 2104,
            WITHOUT_LOOP_SHIFT_BW_APPROVE_CENTRAL_NEW: 2204,


            NO_ACTION_PICKED: 0,

            REQUESTED_CLIENT_FOR_CORRECTION: 35,
            WITHOUT_LOOP_REQUESTED_CLIENT_FOR_CORRECTION: 56,

            CLIENT_CORRECTION_SUBMITTED: 3,
            WITHOUT_LOOP_CLIENT_CORRECTION_SUBMITTED: 60,

            FORWARD_TO_MUX_TEAM: 43,
            WITHOUT_LOOP_FORWARD_TO_MUX_TEAM: 75,
            COMPLETE_MUX_CONFIGURATION: 44,
            WITHOUT_LOOP_COMPLETE_MUX_CONFIGURATION: 78,
            CLIENT_NEW_CONNECTION: 19,
            TEST: -1,

            REQUESTED_TO_LOCAL_DGM: 51,
            WITHOUT_LOOP_REQUESTED_TO_LOCAL_DGM: 65,
            ONLY_LOOP_REQUESTED_TO_LOCAL_DGM: 3001,

            RETRANSFER_TO_LOCAL_DGM: 52,
            WITHOUT_LOOP_RETRANSFER_TO_LOCAL_DGM: 70,

            FORWARDED_IFR_INCOMPLETE: 53,
            WITHOUT_LOOP_FORWARDED_IFR_INCOMPLETE: 68,


            WITHOUT_LOOP_COMPLETE_TESTING_AND_CREATE_CONNECTION: 80,
            CLOSE_CONNECTION_COMPLETE_APPLICATION: 1016,

        },
        applicationFirstStateValue: -1,
        applicationSecondStateValue: -1,
        loading: false,
        display: 'none',
        ports: [],
        vlans: [],
        popList: [],
        alreadySelectedLoopLength: 0,
        ipRegionList: [],
        regionID: 0,
        ipVersion: 0,
        ipType: 0,
        ipBlockSize: 0,
        ipFreeBlockList: [],
        ipBlockID: 0,
        ipRoutingInfo: '',
        ipAvailableRangleList: '',
        ipAvailableIPID: 0,
        ipAvailableSelected: [],
        loopProviderList: [{ID: 1, label: 'BTCL'}, {ID: 2, label: 'Client'}],
        connectionOptionListByClientID: [],
        isPageLoaded: false,
        fileUploadURL: '',
        vlanOptions: [{ID: 1, label: 'Regular VLAN'}, {ID: 2, label: 'Global VLAN'}]

    },
    methods: {


        demandNoteCheck: function () {

            // debugger;

            for (let i = 0; i < this.application.action.length; i++) {
                if ((this.application.action[i].Value == this.STATE_FACTORY.WITHOUT_LOOP_DEMAND_NOTE_GENERATED_SKIP
                    || this.application.action[i].Value == this.STATE_FACTORY.DEMAND_NOTE_GENERATED_SKIP)
                    && this.application.skipPay == 0) {
                    this.application.action.splice(i, 1);
                } else if ((this.application.action[i].Value == this.STATE_FACTORY.WITHOUT_LOOP_DEMAND_NOTE
                        || this.application.action[i].Value == this.STATE_FACTORY.DEMAND_NOTE
                        || this.application.action[i].Value == this.STATE_FACTORY.CLOSE_CONNECTION_DEMAND_NOTE
                    )
                    && this.application.skipPay == 1) {
                    this.application.action.splice(i, 1);

                }

            }

        },

        qDeadLine: function (elem) {
            return new Date(elem.quotationDeadline).toDateString();
        },
        previewConnection(connectionID) {
            window.location.href = context + 'lli/application/preview-connection.do?id=' + connectionID;
        },

        /*  /!*new addition for additional local loop *!/
          connectionSelectionCallback: function(connection){
              if(connection === undefined){
                  this.officeList = [];
                  this.application.office = undefined;
              } else{
                  axios({ method: 'GET', 'url': context + 'lli/connection/get-officeObjectList-by-connection-id.do?id=' + connection.ID})
                      .then(result => {
                          this.officeList = result.data.payload;
                      }, error => {
                      });
              }
          },*/

        /*end new connection addition*/
        nextStep: function () {
            if (this.picked == this.STATE_FACTORY.NEW_LOCAL_LOOP_APPROVED) {
                //alert("this data is loading ");
                $('#localLoopModal').modal({show: true});
            }

            if (this.picked == this.STATE_FACTORY.NO_ACTION_PICKED) {
                toastr.options.timeOut = 3000;
                toastr.error("Please Select an Action.");
                return;
            }
            if (this.picked == this.STATE_FACTORY.CLOSE_CONNECTION_COMPLETE_APPLICATION) {
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

                            axios.post(context + 'lli/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                                .then(result => {
                                    if (result.data.responseCode == 2) {
                                        toastr.error(result.data.msg);
                                        setTimeout(() => {
                                            location.reload(1);
                                        }, 5000);

                                    } else if (result.data.responseCode == 1) {
                                        toastr.success("Your request has been processed", "Success");
                                        window.location.href = context + 'lli/application/search.do';
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
            if (this.picked == this.STATE_FACTORY.IFR_REQUEST
                || this.picked == this.STATE_FACTORY.FORWARDED_IFR_REQUEST
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_FORWARDED_IFR_REQUEST
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_IFR_REQUEST
                || this.picked == this.STATE_FACTORY.SHIFT_BW_IFR_REQUEST
            ) {

                if (this.application.applicationType.ID == this.TYPE_FACTORY.ADDITIONAL_LOCAL_LOOP
                    || this.application.applicationType.ID == this.TYPE_FACTORY.ADDITIONAL_PORT
                ) {

                    $('#newLocalLoopModal').modal({show: true});
                } else if (this.application.applicationType.ID == this.TYPE_FACTORY.ADDITIONAL_IP) {
                    $('#additoinalIPModal').modal({show: true});
                } else if (this.application.applicationType.ID == this.TYPE_FACTORY.NEW_CONNECTION) {

                    this.createSWAL("Comment");
                } else {

                    $('#myModal').modal({show: true});

                }
            }
            if (this.picked == this.STATE_FACTORY.REQUEST_EFR
                || this.picked == this.STATE_FACTORY.FORWARDED_REQUEST_EFR
                || this.picked == this.STATE_FACTORY.ONLY_LOOP_FORWARDED_LDGM_REQUEST_EFR
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_DEMAND_NOTE
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_FORWARDED_LDGM_FORWARD_CDGM
                || this.picked == this.STATE_FACTORY.ONLY_LOOP_REQUESTED_TO_LOCAL_DGM
            ) {
                var isAnyLocalLoopSelected = false;
                for (var i = 0; i < this.application.ifr.length; i++) {
                    if (this.application.ifr[i].isSelected == 1) {
                        isAnyLocalLoopSelected = true;
                        break;
                    }
                }
                if (
                    (
                        !isAnyLocalLoopSelected && this.STATE_FACTORY.IFR_RESPONSE == this.application.state
                    )
                    || (
                        !isAnyLocalLoopSelected && this.picked == this.STATE_FACTORY.WITHOUT_LOOP_DEMAND_NOTE &&
                        // this.application.applicationType.ID!= this.TYPE_FACTORY.ADDITIONAL_IP &&
                        this.application.state != this.STATE_FACTORY.WITHOUT_LOOP_RETRANSFER_TO_LOCAL_DGM
                    )
                    || (!isAnyLocalLoopSelected && this.picked == this.STATE_FACTORY.WITHOUT_LOOP_FORWARDED_LDGM_FORWARD_CDGM
                    )
                    || (!isAnyLocalLoopSelected && this.picked == this.STATE_FACTORY.ONLY_LOOP_REQUESTED_TO_LOCAL_DGM
                    )

                ) {
                    this.errorMessage("IFR is Negative or no POP is selected. If IFR is Negative, You can not proceed further. Please reject the application.");
                    return;
                }
                if (
                    !(this.picked == this.STATE_FACTORY.WITHOUT_LOOP_DEMAND_NOTE) &&
                    !(this.picked == this.STATE_FACTORY.WITHOUT_LOOP_FORWARDED_LDGM_FORWARD_CDGM) &&
                    !(this.picked == this.STATE_FACTORY.ONLY_LOOP_REQUESTED_TO_LOCAL_DGM)


                ) {
                    if (this.application.applicationType.ID == this.TYPE_FACTORY.ADDITIONAL_LOCAL_LOOP
                        || this.application.applicationType.ID == this.TYPE_FACTORY.ADDITIONAL_PORT
                    ) {

                        $('#efrForNewLocalLoop').modal({show: true});
                    } else if (this.application.applicationType.ID == this.TYPE_FACTORY.UPGRADE_CONNECTION && this.application.isNewLoop == true) {
                        $('#efrForNewLocalLoopForUpgrade').modal({show: true});
                    } else {
                        $('#ldgmModal').modal({show: true});
                    }

                }
            }
            if (this.picked == this.STATE_FACTORY.IFR_RESPONSE
                || this.picked == this.STATE_FACTORY.FORWARDED_IFR_RESPONSE
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_IFR_RESPONSE
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_FORWARDED_IFR_RESPONSE
                || this.picked == this.STATE_FACTORY.SHIFT_BW_IFR_RESPONSE
            ) {
                if (this.application.applicationType.ID == this.TYPE_FACTORY.ADDITIONAL_LOCAL_LOOP
                    || this.application.applicationType.ID == this.TYPE_FACTORY.ADDITIONAL_PORT) {
                    $('#ifrrespondmodalFORLocalLoop').modal({show: true});
                } else if (this.application.applicationType.ID == this.TYPE_FACTORY.ADDITIONAL_IP) {
                    $('#ifrrespondmodalAdditionalIP').modal({show: true});
                } else $('#ifrrespondmodal').modal({show: true});
            }
            if (this.picked == this.STATE_FACTORY.EFR_RESPONSE
                || this.picked == this.STATE_FACTORY.FORWARDED_EFR_RESPONSE
                || this.picked == this.STATE_FACTORY.ONLY_LOOP_FORWARDED_EFR_RESPONSE
            ) {
                $('#vendormodal').modal({show: true});
            }
            if (
                this.picked == this.STATE_FACTORY.PAYMENT_DONE
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_PAYMENT_DONE
                || this.picked == this.STATE_FACTORY.CLOSE_CONNECTION_PAYMENT_DONE

            ) {
                $('#paymentVerifiedModal').modal({show: true});
            }
            if (
                this.picked == this.STATE_FACTORY.PAYMENT_VERIFIED
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_PAYMENT_VERIFIED
                || this.picked == this.STATE_FACTORY.CLOSE_CONNECTION_PAYMENT_VERIFIED
            ) {
                $('#paymentVerifiedModal').modal({show: true});
            }
            if (
                this.picked == this.STATE_FACTORY.REQUESTED_TO_LOCAL_DGM
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_REQUESTED_TO_LOCAL_DGM
                || this.picked == this.STATE_FACTORY.ONLY_LOOP_REQUESTED_TO_LOCAL_DGM
            ) {
                // alert('came here');
                $('#requestToLocalDGMModal').modal({show: true});
            }
            if (
                this.picked == this.STATE_FACTORY.WORK_ORDER_GENERATE
                || this.picked == this.STATE_FACTORY.CLOSE_CONNECTION_WORK_ORDER_GENERATE
                || this.picked == this.STATE_FACTORY.FORWARDED_WORK_ORDER_GENERATE
            ) {
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
                            if (
                                this.picked == this.STATE_FACTORY.WORK_ORDER_GENERATE
                                || this.picked == this.STATE_FACTORY.FORWARDED_WORK_ORDER_GENERATE
                            ) {
                                url1 = "workordergenerate";
                            } else if (this.picked == this.STATE_FACTORY.CLOSE_CONNECTION_WORK_ORDER_GENERATE) {
                                url1 = "closeworkordergenerate";
                            }
                            axios.post(context + 'lli/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                                .then(result => {
                                    if (result.data.responseCode == 2) {
                                        toastr.error(result.data.msg);
                                        setTimeout(() => {
                                            location.reload(1);
                                        }, 5000);


                                    } else if (result.data.responseCode == 1) {
                                        toastr.success("Your request has been processed", "Success");
                                        window.location.href = context + 'lli/application/search.do';
                                    } else {

                                        toastr.error(result.data.msg);
                                        setTimeout(() => {
                                            location.reload(1);
                                        }, 5000);
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
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_CLIENT_CORRECTION_SUBMITTED

            ) {
                this.loading = true;
                this.application.nextState = this.picked;
                var url1 = "new-connection-edit";
                axios.post(context + 'lli/application/' + url1 + '.do', {'application': JSON.stringify(this.application)})
                    .then(result => {
                        if (result.data.responseCode == 2) {
                            toastr.error(result.data.msg);
                            setTimeout(() => {
                                location.reload(1);
                            }, 5000);

                        } else if (result.data.responseCode == 1) {
                            toastr.success("Your request has been processed", "Success");
                            window.location.href = context + 'lli/application/search.do';
                        } else {
                            toastr.error("Your request was not accepted", "Failure");
                        }
                        this.loading = false;
                    })
                    .catch(function (error) {
                        this.loading = false;
                    });
            }
            if (
                //reopen states
                this.picked == this.STATE_FACTORY.APPLICATION_REOPEN ||
                this.picked == this.STATE_FACTORY.WITHOUT_LOOP_APPLICATION_REOPEN ||
                this.picked == this.STATE_FACTORY.DOWNGRADE_POLICY_APPLICATION_REOPEN ||
                this.picked == this.STATE_FACTORY.CLOSE_CONNECTION_APPLICATION_REOPEN ||
                this.picked == this.STATE_FACTORY.CLOSE_CONNECTION_POLICY_APPLICATION_REOPEN ||
                this.picked == this.STATE_FACTORY.CLOSE_CONNECTION_POLICY_BYPASS_APPLICATION_REOPEN ||
                this.picked == this.STATE_FACTORY.SHIFT_BW_APPLICATION_REOPEN_LOCAL ||
                this.picked == this.STATE_FACTORY.SHIFT_BW_APPLICATION_REOPEN_LOCAL_NEW ||
                this.picked == this.STATE_FACTORY.WITHOUT_LOOP_SHIFT_BW_APPLICATION_REOPEN_LOCAL_NEW ||
                this.picked == this.STATE_FACTORY.SHIFT_BW_APPLICATION_REOPEN_CENTRAL ||
                this.picked == this.STATE_FACTORY.SHIFT_BW_APPLICATION_REOPEN_CENTRAL_NEW ||
                this.picked == this.STATE_FACTORY.WITHOUT_LOOP_SHIFT_BW_APPLICATION_REOPEN_CENTRAL_NEW ||

                this.picked == this.STATE_FACTORY.SHIFT_BW_APPROVE_LOCAL ||
                this.picked == this.STATE_FACTORY.SHIFT_BW_APPROVE_LOCAL_NEW ||
                this.picked == this.STATE_FACTORY.WITHOUT_LOOP_SHIFT_BW_APPROVE_LOCAL_NEW ||
                this.picked == this.STATE_FACTORY.SHIFT_BW_APPROVE_CENTRAL ||
                this.picked == this.STATE_FACTORY.SHIFT_BW_APPROVE_CENTRAL_NEW ||
                this.picked == this.STATE_FACTORY.WITHOUT_LOOP_SHIFT_BW_APPROVE_CENTRAL_NEW ||

                //correction states
                this.picked == this.STATE_FACTORY.REQUESTED_CLIENT_FOR_CORRECTION ||
                this.picked == this.STATE_FACTORY.WITHOUT_LOOP_REQUESTED_CLIENT_FOR_CORRECTION ||

                //mux config states
                this.picked == this.STATE_FACTORY.FORWARD_TO_MUX_TEAM ||
                this.picked == this.STATE_FACTORY.WITHOUT_LOOP_FORWARD_TO_MUX_TEAM ||
                this.picked == this.STATE_FACTORY.COMPLETE_MUX_CONFIGURATION ||
                this.picked == this.STATE_FACTORY.WITHOUT_LOOP_COMPLETE_MUX_CONFIGURATION ||

                //forward states
                this.picked == this.STATE_FACTORY.FORWARD_CDGM ||
                this.picked == this.STATE_FACTORY.WITHOUT_LOOP_FORWARD_CDGM ||
                this.picked == this.STATE_FACTORY.RETRANSFER_TO_LOCAL_DGM ||
                this.picked == this.STATE_FACTORY.WITHOUT_LOOP_RETRANSFER_TO_LOCAL_DGM ||
                this.picked == this.STATE_FACTORY.FORWARDED_IFR_INCOMPLETE ||
                this.picked == this.STATE_FACTORY.WITHOUT_LOOP_FORWARDED_IFR_INCOMPLETE ||
                this.picked == this.STATE_FACTORY.FORWARD_FOR_WORK_ORDER ||

                //Advice Note states
                this.picked == this.STATE_FACTORY.SEND_AN_TO_SERVER_ROOM ||
                this.picked == this.STATE_FACTORY.WITHOUT_LOOP_SEND_AN_TO_SERVER_ROOM ||
                this.picked == this.STATE_FACTORY.CLOSE_CONNECTION_SEND_AN_TO_SERVER_ROOM ||
                this.picked == this.STATE_FACTORY.SHIFT_BW_SEND_AN_TO_SERVER_ROOM ||

                this.picked == this.STATE_FACTORY.SHIFT_BW_COMPLETE_PROCESS ||

                this.picked == this.STATE_FACTORY.CLOSE_CONNECTION_WORK_ORDER_COMPLETE


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
                            } else if (
                                this.picked == this.STATE_FACTORY.SHIFT_BW_APPROVE_CENTRAL ||
                                this.picked == this.STATE_FACTORY.SHIFT_BW_APPROVE_CENTRAL_NEW
                            ) {
                                url1 = "retransfer"
                            } else if (
                                this.picked == this.STATE_FACTORY.FORWARD_CDGM ||
                                this.picked == this.STATE_FACTORY.WITHOUT_LOOP_FORWARD_CDGM
                            ) {
                                url1 = "retransfer"
                            } else if (this.picked == this.STATE_FACTORY.SHIFT_BW_COMPLETE_PROCESS) {
                                url1 = "complete-shift-bw"
                            } else if (this.picked == this.STATE_FACTORY.FORWARD_FOR_WORK_ORDER) {
                                // url1="forward-work-order";
                                url1 = "retransfer";
                            }
                            axios.post(context + 'lli/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                                .then(result => {
                                    if (result.data.responseCode == 2) {
                                        toastr.error(result.data.msg);
                                        setTimeout(() => {
                                            location.reload(1);
                                        }, 5000);

                                    } else if (result.data.responseCode == 1) {
                                        toastr.success("Your request has been processed", "Success");
                                        window.location.href = context + 'lli/application/search.do';
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
                || this.picked == this.STATE_FACTORY.AN_GENERATE_AND_FORWARD
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_ADVICE_NOTE_GENERATE
                || this.picked == this.STATE_FACTORY.LLI_WITHOUT_LOOP_ADVICE_NOTE_PUBLISH_AND_FORWARD
                || this.picked == this.STATE_FACTORY.CLOSE_CONNECTION_ADVICE_NOTE_GENERATE
                || this.picked == this.STATE_FACTORY.CLOSE_CONNECTION_ADVICE_NOTE_GENERATE_AND_FORWARD
                || this.picked == this.STATE_FACTORY.SHIFT_BW_ADVICE_NOTE_GENERATE
                || this.picked == this.STATE_FACTORY.SHIFT_BW_ADVICE_NOTE_GENERATE_AND_FORWARD
            ) {
                // alert('hi');
                if ((this.picked == this.STATE_FACTORY.ADVICE_NOTE_GENERATE
                    || this.picked == this.STATE_FACTORY.AN_GENERATE_AND_FORWARD)
                    && this.application.state == this.STATE_FACTORY.WORK_ORDER_COMPLETE) {
                    if (this.application.loopProvider.ID == 1) {
                        let isWorkOrderSelected = false;

                        for (let i = 0; i < this.application.completeEFR.length; i++) {
                            if (this.application.completeEFR[i].loopDistanceIsApproved == 1) {
                                isWorkOrderSelected = true;
                                break;
                            }
                        }

                        if (!isWorkOrderSelected) {
                            toastr.error("At least one work order must be selected", "Failure");
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
            }
            if (
                this.picked == this.STATE_FACTORY.COMPLETE_TESTING
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_COMPLETE_TESTING
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_COMPLETE_TESTING_AND_CREATE_CONNECTION
            ) {
                if (this.application.applicationType.ID == this.TYPE_FACTORY.ADDITIONAL_LOCAL_LOOP
                    || this.application.applicationType.ID == this.TYPE_FACTORY.ADDITIONAL_PORT) {
                    $('#serverRoomTestingCompleteForNewLocalLoop').modal({show: true});
                } else if (this.application.applicationType.ID == this.TYPE_FACTORY.ADDITIONAL_IP) {
                    $('#serverRoomTestingCompleteAdditionalIP').modal({show: true});
                } else $('#serverRoomTestingComplete').modal({show: true});

            }
            if (
                this.picked == this.STATE_FACTORY.WORK_ORDER_COMPLETE
                || this.picked == this.STATE_FACTORY.FORWARDED_WORK_ORDER_COMPLETE
            ) {
                // alert('here');
                $('#workOrderDetails').modal({show: true});
                // workOrderDetails
            }
            if (this.picked == this.STATE_FACTORY.DEMAND_NOTE
                || this.picked == this.STATE_FACTORY.FORWARDED_LDGM_FORWARD_CDGM
                || this.picked == this.STATE_FACTORY.ONLY_LOOP_FORWARDED_LDGM_FORWARD_CDGM
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_FORWARDED_LDGM_FORWARD_CDGM
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_DEMAND_NOTE
                || this.picked == this.STATE_FACTORY.DEMAND_NOTE_GENERATED_SKIP
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_DEMAND_NOTE_GENERATED_SKIP
                || this.picked == this.STATE_FACTORY.WORK_ORDER_FROM_LDGM_FORWARD_CDGM
                || this.picked == this.STATE_FACTORY.CLOSE_CONNECTION_DEMAND_NOTE
            ) {
                var url1 = "";
                var lebel = "Write A Comment";
                if (
                    this.picked == this.STATE_FACTORY.DEMAND_NOTE

                ) {
                    url1 = "demandnotegenerate";
                    lebel = "Write Demand Note Generation Comment:";
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

                } else if (
                    this.picked == this.STATE_FACTORY.FORWARDED_LDGM_FORWARD_CDGM
                    || this.picked == this.STATE_FACTORY.ONLY_LOOP_FORWARDED_LDGM_FORWARD_CDGM
                ) {
                    url1 = "forwardedefrselect";
                    lebel = "Write Forwarding Comment:";
                    if (this.picked == this.STATE_FACTORY.ONLY_LOOP_FORWARDED_LDGM_FORWARD_CDGM) {
                        url1 = "forwarded-efr-select-only-loop";
                        lebel = "Write Forwarding Comment:";
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

                } else if (this.picked == this.STATE_FACTORY.WITHOUT_LOOP_DEMAND_NOTE) {
                    url1 = "withoutloopdemandnotegenerate";
                    lebel = "Write Demand Note Generation Comment:"
                } else if (this.picked == this.STATE_FACTORY.WITHOUT_LOOP_DEMAND_NOTE_GENERATED_SKIP) {
                    url1 = "withoutloopskipdemandnotegenerate";
                    lebel = "Write Comment for Skipping Demand Note:"
                } else if (this.picked == this.STATE_FACTORY.WITHOUT_LOOP_FORWARDED_LDGM_FORWARD_CDGM) {
                    url1 = "forwardedifrselect";
                    lebel = "Write Comment for CDGM:"
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
                } else if (this.picked == this.STATE_FACTORY.WITHOUT_LOOP_FORWARDED_LDGM_FORWARD_CDGM) {
                    url1 = "withoutloopskipdemandnotegenerate";
                    lebel = "Write Comment for Skipping Demand Note:"
                } else if (this.picked == this.STATE_FACTORY.CLOSE_CONNECTION_DEMAND_NOTE) {
                    url1 = "demandnotegenerate";
                    lebel = "Write Demand Note Generation Comment:"
                } else if (this.picked == this.STATE_FACTORY.CLOSE_CONNECTION_DEMAND_NOTE) {
                    url1 = "demandnotegenerate";
                    lebel = "Write Demand Note Generation Comment:"
                } else if (this.picked == this.STATE_FACTORY.WORK_ORDER_FROM_LDGM_FORWARD_CDGM) {
                    // alert();
                    if (this.application.loopProvider.ID == 1) {
                        let oneWOSelected = false;

                        for (let i = 0; i < this.application.completeEFR.length; i++) {
                            if (this.application.completeEFR[i].hasOwnProperty("loopDistanceIsApproved")) {
                                if (this.application.completeEFR[i].loopDistanceIsApproved == 1) {
                                    oneWOSelected = true;
                                    break;
                                }
                            }
                        }

                        if (!oneWOSelected) {
                            toastr.error("At least one work order must be selected to continue");
                            return;
                        }
                    }
                    url1 = "forward-after-work-order";
                    lebel = "Write Forwarding Comment:"
                }
                // alert("ghorar dim");
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
                            axios.post(context + 'lli/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                                .then(result => {
                                    if (result.data.responseCode == 2) {
                                        toastr.error(result.data.msg);
                                        setTimeout(() => {
                                            location.reload(1);
                                        }, 5000);

                                    } else if (result.data.responseCode == 1) {
                                        toastr.success("Your request has been processed", "Success");
                                        if (this.picked == this.STATE_FACTORY.DEMAND_NOTE
                                            || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_DEMAND_NOTE
                                            || this.picked == this.STATE_FACTORY.DEMAND_NOTE_GENERATED_SKIP
                                            || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_DEMAND_NOTE_GENERATED_SKIP
                                        ) {
                                            window.location.href = context + 'lli/dn/new.do?id=' + this.application.applicationID + '&nextstate=' + this.picked;
                                        } else if (
                                            this.picked == this.STATE_FACTORY.FORWARDED_LDGM_FORWARD_CDGM
                                            || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_FORWARDED_LDGM_FORWARD_CDGM
                                            || this.picked == this.STATE_FACTORY.ONLY_LOOP_FORWARDED_LDGM_FORWARD_CDGM
                                            || this.picked == this.STATE_FACTORY.WORK_ORDER_FROM_LDGM_FORWARD_CDGM

                                        ) {
                                            window.location.href = context + 'lli/application/search.do';
                                        } else if (this.picked == this.STATE_FACTORY.CLOSE_CONNECTION_DEMAND_NOTE) {
                                            window.location.href = context + 'lli/dn/new.do?id=' + this.application.applicationID + '&nextstate=' + this.picked;
                                        }
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
                this.picked == this.STATE_FACTORY.REJECT_APPLICATION
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_REJECT_APPLICATION
                || this.picked == this.STATE_FACTORY.DOWNGRADE_POLICY_REJECT_APPLICATION
                || this.picked == this.STATE_FACTORY.CLOSE_CONNECTION_REJECT_APPLICATION
                || this.picked == this.STATE_FACTORY.CLOSE_CONNECTION_POLICY_REJECT_APPLICATION
                || this.picked == this.STATE_FACTORY.CLOSE_CONNECTION_POLICY_BYPASS_REJECT_APPLICATION
                || this.picked == this.STATE_FACTORY.SHIFT_BW_REJECT_APPLICATION
                || this.picked == this.STATE_FACTORY.SHIFT_BW_REJECT_APPLICATION_NEW
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_SHIFT_BW_REJECT_APPLICATION_NEW
                || this.picked == this.STATE_FACTORY.SHIFT_BW_REJECT_APPLICATION_CENTRAL
                || this.picked == this.STATE_FACTORY.SHIFT_BW_REJECT_APPLICATION_CENTRAL_NEW
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_SHIFT_BW_REJECT_APPLICATION_CENTRAL_NEW
                || this.picked == this.STATE_FACTORY.SHIFT_BW_REJECT_APPLICATION_DESINATION_DGM
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
                            this.loading = true;
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
                                        setTimeout(() => {
                                            location.reload(1);
                                        }, 5000);

                                    } else if (result.data.responseCode == 1) {
                                        toastr.success("Your request has been processed", "Success");
                                        window.location.href = context + 'lli/application/search.do';
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
                || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_ACCOUNT_CC_REQUEST
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
                            axios.post(context + 'lli/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                                .then(result => {
                                    if (result.data.responseCode == 2) {
                                        toastr.error(result.data.msg);
                                        setTimeout(() => {
                                            location.reload(1);
                                        }, 5000);

                                    } else if (result.data.responseCode == 1) {
                                        toastr.success("Your request has been processed", "Success");
                                        window.location.href = context + 'lli/application/search.do';
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
                || this.picked == this.STATE_FACTORY.ACCOUNT_CC_RESPONSE_POSITIVE_DOWNGRADE
                || this.picked == this.STATE_FACTORY.ACCOUNT_CC_RESPONSE_NEGATIVE
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
                            axios.post(context + 'lli/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                                .then(result => {
                                    if (result.data.responseCode == 2) {
                                        toastr.error(result.data.msg);
                                        setTimeout(() => {
                                            location.reload(1);
                                        }, 5000);

                                    } else if (result.data.responseCode == 1) {
                                        toastr.success("Your request has been processed", "Success");
                                        window.location.href = context + 'lli/application/search.do';
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
                if (item.availableBW == 0) {
                }
                if (item.office == '') {
                    this.errorMessage("Office must be selected.");
                    return;
                }
                if (item.pop == '') {
                    this.errorMessage("POP must be selected.");
                    return;
                }
                if (item.priority == 0) {
                    this.errorMessage("Priority must be selected.");
                    return;
                }
                localLoops.pops.push({
                    "id": item.id,
                    "popID": item.pop.ID,
                    "requestedBW": item.requestedBW,
                    "priority": item.priority,
                    "availableBW": parseInt(item.availableBW),
                    "isSelected": item.isSelected,
                    "officeId": item.office.id,
                });
            }
            // debugger;
            if (localLoops.comment === "") {
                this.errorMessage("Please provide a comment.");
                return;
            }
            var url1 = "ifrupdate";
            this.loading = true;
            axios.post(context + 'lli/application/' + url1 + '.do', {'ifr': JSON.stringify(localLoops)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                        setTimeout(() => {
                            location.reload(1);
                        }, 5000);

                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = context + 'lli/application/search.do';
                    } else {
                        toastr.error("Your request was not accepted", "Failure");
                    }
                    this.loading = false;
                })
                .catch(function (error) {
                    this.loading = false;
                });
        },
        serverRoomNextStepAdditoinalIP: function () {
            var ifr = {
                applicationID: applicationID,
                comment: this.comment,
                pops: [],
                nextState: this.picked,
                ipCount: this.application.ipCount,
                isSelected: this.application.isSelected,
            };
            if (ifr.comment === "") {
                this.errorMessage("Please provide a comment.");
                return;
            }
            var url1 = "ifrupdateForAdditionalIP";
            this.loading = true;
            Promise.resolve(axios.post(context + 'lli/application/' + url1 + '.do', {'ifr': JSON.stringify(ifr)})
                .then(result => {
                    if (result.data.responseCode === 2) {
                        errorMessage(result.data.msg);
                        setTimeout(() => {
                            location.reload();
                        }, 5000);

                    } else if (result.data.responseCode === 1) {
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = context + 'lli/application/search.do';
                    }

                })
                .catch((err) => {
                    console.log(err);
                })).then(() => this.loading = false);
        },
        serverRoomNextStepNewLocalLoop: function () {
            var loopList = this.modifiedIFRForNewLocalLoop;
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
                    "popID": item.pop.ID,
                    "isSelected": item.isSelected,
                    "officeId": item.office.id,
                });
            }
            // debugger;
            if (localLoops.comment === "") {
                this.errorMessage("Please provide a comment.");
                return;
            }
            var url1 = "ifrupdateForNewLocalLoop";
            this.loading = true;
            axios.post(context + 'lli/application/' + url1 + '.do', {'ifr': JSON.stringify(localLoops)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                        setTimeout(() => {
                            location.reload(1);
                        }, 5000);

                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = context + 'lli/application/search.do';
                    } else {
                        toastr.error("Your request was not accepted", "Failure");
                    }
                    this.loading = false;
                })
                .catch(function (error) {
                    this.loading = false;
                });
        },
        testingCompleteNextStepForIP: function () {
            if (this.comment === "") {
                this.errorMessage("Please Provide a Comment");
                return;
            }
            let srSelectedIPCount = 0;
            this.ipAvailableSelected.forEach((item) => {
                let count = (ip2int(item.toIP) - ip2int(item.fromIP) + 1);
                srSelectedIPCount += count;
            });
            console.log("Total IP Assigned : " + srSelectedIPCount);
            if (srSelectedIPCount !== this.application.ipCount) {
                errorMessage("You have assigned " + srSelectedIPCount + " IP Addresses, You have to assign " + this.application.ipCount + " IP Addresses.");
                return;
            }
            let loopList = this.application.ifr;
            let localLoops = {
                applicationID: applicationID,
                comment: this.comment,
                localloops: this.application.localloops,
                nextState: this.picked,
                ip: this.ipAvailableSelected,
                application: this.application,
                connectionName: this.application.connectionName

            };
            this.loading = true;
            let url1 = "completetestingandCreateAdditionalIP";

            if (this.picked == this.STATE_FACTORY.WITHOUT_LOOP_COMPLETE_TESTING_AND_CREATE_CONNECTION) {
                url1 = "completetestingandCreateAdditionalIP";
            }
            this.loading = true;

            axios.post(context + 'lli/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                .then(result => {
                    if (result.data.responseCode === 2) {
                        errorMessage(result.data.msg);
                        setTimeout(() => {
                            location.reload();
                        }, 5000);
                        this.loading = false;
                    } else if (result.data.responseCode === 1) {
                        toastr.success("Your request has been processed", "Success");
                        this.loading = false;
                        window.location.href = context + 'lli/application/search.do';
                    }

                })
                .catch((err) => {
                    console.log(err);
                });
        },
        testingCompleteNextStepForNewLocalLoop: function () {
            if (this.comment == "") {
                this.errorMessage("Please Provide a Comment");
                return;
            }
            var loopList = this.application.ifr;
            var ip = {
                fromIP: this.ipRoutingInfo.fromIP,
                toIP: this.ipRoutingInfo.toIP,
                broadcastIP: this.ipRoutingInfo.broadcastIP,
                gatewayIP: this.ipRoutingInfo.gatewayIP,
                networkIP: this.ipRoutingInfo.networkIP,
                type: this.ipType,
                version: this.ipVersion,
                purpose: -1,
            };
            var localLoops = {
                applicationID: applicationID,
                comment: this.comment,
                localloops: this.application.newlocalloops,
                nextState: this.picked,
                ip: ip,
                application: this.application,
                connectionName: this.application.connectionName


            };
            var url1 = "completetestingandCreateNewLocalLoop";

            if (this.picked == this.STATE_FACTORY.WITHOUT_LOOP_COMPLETE_TESTING_AND_CREATE_CONNECTION) {
                url1 = "completetestingandCreateNewLocalLoop";
            }
            this.loading = true;
            axios.post(context + 'lli/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                        setTimeout(() => {
                            location.reload(1);
                        }, 5000);

                    } else if (result.data.responseCode === 1) {
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = context + 'lli/application/search.do';
                    } else {
                        toastr.error("Your request was not accepted", "Failure");
                    }
                    this.loading = false;
                })
                .catch((error) => {
                    this.loading = false;
                });
        },
        testingCompleteNextStep: function () {
            // swal({
            //     title: "Are you sure?",
            //     text: "Once Confirmed, you will not be able to revert!",
            //     icon: "warning",
            //     buttons: true,
            //     dangerMode : true,
            // }).then((willProceed)=>{
            //     if(willProceed) {
            //
            //         swal("Your data has been saved!",{
            //             icon:"success",
            //         })
            //     }else {
            //         return;
            //     }
            // });
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
                ip: this.ipAvailableSelected,
                application: this.application,
                connectionName: this.application.connectionName
            };
            var url1 = "completetestingandcreateconnection";
            if (this.picked == this.STATE_FACTORY.WITHOUT_LOOP_COMPLETE_TESTING_AND_CREATE_CONNECTION) {
                url1 = "completetestingandcreateconnection";
            }
            this.loading = true;
            axios.post(context + 'lli/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                        setTimeout(() => {
                            location.reload(1);
                        }, 5000);

                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = context + 'lli/application/search.do';
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
            var loopList = this.application.ifr;
            var localLoops = {
                applicationID: applicationID,
                comment: this.comment,
                pops: this.application.incompleteEFR,
                nextState: this.picked,
            };
            for (var i in this.application.incompleteEFR) {
                var item = this.application.incompleteEFR[i];
                var result = (item.proposedLoopDistance - Math.floor(item.proposedLoopDistance)) !== 0;
                if (result) {
                    this.errorMessage('Distance Value Should be in Integer amount');
                    return;
                }
                if (item.proposedLoopDistance == 0 && item.quotationStatus == 1) {
                    this.errorMessage("Loop length must be provided.");
                    return;
                } else if (item.ofcType == 0) {
                    this.errorMessage("Select an OFC Type.");
                    return;
                } else if (item.quotationStatus == 0) {
                    this.errorMessage("Provide Quotation Status.");
                    return;
                }
            }
            if (localLoops.comment === "") {
                this.errorMessage("Please provide a comment.");
                return;
            }
            var url1 = "efrupdate";
            this.loading = true;
            axios.post(context + 'lli/application/' + url1 + '.do', {'efr': JSON.stringify(localLoops)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                        setTimeout(() => {
                            location.reload(1);
                        }, 5000);

                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = context + 'lli/application/search.do';
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

            for (let i = 0; i < completeEFR.length; i++) {
                if (completeEFR[i].actualLoopDistance <= 0) {
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
            axios.post(context + 'lli/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                        setTimeout(() => {
                            location.reload(1);
                        }, 5000);

                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = context + 'lli/application/search.do';
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
            if (this.comment == "") {
                this.errorMessage("Please Provide a Comment");
                return;
            }
            this.loading = true;
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
                        setTimeout(() => {
                            location.reload(1);
                        }, 5000);

                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        // window.location.href = context + 'lli/application/search.do'
                        var redirect = '';
                        if (
                            this.picked == this.STATE_FACTORY.PAYMENT_DONE
                            || this.picked == this.STATE_FACTORY.WITHOUT_LOOP_PAYMENT_DONE
                            || this.picked == this.STATE_FACTORY.CLOSE_CONNECTION_PAYMENT_DONE
                        ) {
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
            if (this.comment == "") {
                this.errorMessage("Please Provide a Comment");
                return;
            }
            this.loading = true;
            var localLoops = {
                applicationID: applicationID,
                comment: this.comment,
                // pops: this.application.incompleteEFR,
                nextState: this.picked,
                ifr: this.application.ifr,
                zoneID: this.application.zone.id,
            };
            var url1 = "applicationforward";
            if (this.picked == this.STATE_FACTORY.ONLY_LOOP_REQUESTED_TO_LOCAL_DGM) {
                url1 = "application-forward-for-loop"
            }
            axios.post(context + 'lli/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                        setTimeout(() => {
                            location.reload(1);
                        }, 5000);

                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = context + 'lli/application/search.do';
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
                newOfficeList: [
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
        addLocalLoop: function (office, bw) {
            // alert('bw - > ' + bw);
            // debugger;
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
                loopProvider: -1,
                popID: 0,
                src: -1,
                srcName: '',
                destination: -1,
                destinationName: '',
                vendorID: -1,
                pop: {
                    ID: 0,
                    label: '',
                }


            });
        },
        serverRoomPOPAdd: function () {
            // alert('here');
            this.modifiedIFR.push({
                applicationID: 0,
                availableBW: 0,
                id: 0,
                isForwarded: 0,
                isReplied: 0,
                isSelected: 0,
                officeID: 0,
                parentIFRID: 0,
                popID: 0,
                pop: '',
                office: '',
                popName: "",
                priority: 0,
                requestedBW: this.application.bandwidth,
                selectedBW: 0,
                serverRoomLocationID: 0,
                state: 0,
                submissionDate: 0

            });
        },
        serverRoomPOPAddForNewLocalLoop: function () {

            // debugger;
            var popID = 0;
            //
            // if(this.application.applicationType.ID==5){
            //
            //     popID=this.application.newlocalloops[0].popID;
            //
            // }
            this.modifiedIFRForNewLocalLoop.push({
                applicationID: 0,
                availableBW: 0,
                id: 0,
                isForwarded: 0,
                isReplied: 0,
                isSelected: 0,
                officeID: 0,
                parentIFRID: 0,
                popID: popID,

                pop: '',
                popName: "",
                priority: 0,
                requestedBW: this.application.bandwidth,
                selectedBW: 0,
                serverRoomLocationID: 0,
                state: 0,
                submissionDate: 0
            });


        },
        serverRoomPOPDelete: function (index) {
            this.modifiedIFR.splice(index, 1);
        },
        serverRoomPOPDeleteForNewLocalLoop: function (index) {
            this.modifiedIFRForNewLocalLoop.splice(index, 1);
        },
        deleteOffice: function (connection, officeIndex, event) {
            connection.officeList.splice(officeIndex, 1);
        },
        deleteLocalLoop: function (connection, officeIndex, localLoopIndex) {
            connection.officeList[officeIndex].localLoopList.splice(localLoopIndex, 1);
        },
        deleteNewLocalLoop: function (connection, officeIndex, localLoopIndex) {
            connection.newOfficeList[officeIndex].localLoopList.splice(localLoopIndex, 1);
        },
        process: function (url) {
            debugger;
            var loopList = this.content.officeList[0].localLoopList;
            if (loopList.length == 0) {
                toastr.error("Click Add Button for Office and POP Selection.");
                return;
            }
            var localLoops = {
                applicationID: applicationID,
                comment: this.comment,
                pops: [],
                stateID: parseInt(this.picked),
                nextState: this.picked,
            };
            for (var i in loopList) {
                var item = loopList[i];
                if (item.pop == null) {
                    toastr.error("Pop  must be provided.");
                    return;
                }
                item.ID = item.pop.ID;

                if (item.officeid == null || item.officeid == 0) {
                    toastr.error("Office must be provided.");
                    return;
                }

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
            this.loading = true;
            var url1 = "ifrinsert";
            axios.post(context + 'lli/application/' + url1 + '.do', {'ifr': JSON.stringify(localLoops)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);


                        setTimeout(() => {
                            location.reload(1);
                        }, 5000);

                        toastr.options.timeOut = 3000;
                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        toastr.options.timeOut = 3000;
                        window.location.href = context + 'lli/application/search.do';
                    } else {
                        toastr.error("Your request was not accepted", "Failure");
                        toastr.options.timeOut = 3000;
                    }
                    this.loading = false;
                })
                .catch(function (error) {
                    this.loading = false;
                });
        },
        processIFRRequestAdditionalIP: function (url) {
            var localLoops = {
                applicationID: applicationID,
                comment: this.comment,
                pops: [],
                stateID: parseInt(this.picked),
                nextState: this.picked,
                ipCount: this.application.ipCount
            };
            if (this.comment === "") {
                this.errorMessage("Please provide a comment.");
                return;
            }
            this.loading = true;
            var url1 = "ifrinsertAdditionalIP";
            axios.post(context + 'lli/application/' + url1 + '.do', {'ifr': JSON.stringify(localLoops)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                        setTimeout(() => {
                            location.reload(1);
                        }, 5000);

                        toastr.options.timeOut = 3000;
                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        toastr.options.timeOut = 3000;
                        window.location.href = context + 'lli/application/search.do';
                    }

                    this.loading = false;
                })
                .catch(function (error) {
                    this.loading = false;
                });
        },
        processLocalLoopIFRRequest: function (url) {

            var loopList = this.content.newOfficeList[0].localLoopList;
            if (loopList.length == 0) {
                toastr.options.timeOut = 3000;
                toastr.error("Please click the green plus button to add a local loop.");
                return;
            }
            if (loopList.length < this.application.portCount) {
                $("#myModal").modal("hide");
                $('#newLocalLoopModal').modal("hide");
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
                                var localLoops = {
                                    applicationID: applicationID,
                                    comment: this.comment,
                                    pops: [],
                                    stateID: parseInt(this.picked),
                                    nextState: this.picked,
                                };
                                for (var i in loopList) {
                                    var item = loopList[i];
                                    if (item.pop == null) {
                                        this.errorMessage("POP must be selected.");
                                        return;
                                    }
                                    if (item.pop != null) {
                                        item.ID = item.pop.ID;
                                        if (item.ID <= 0) {
                                            // toastr.options.timeOut = 3000;
                                            this.errorMessage("POP must be selected.");
                                            return;
                                        }
                                    }
                                    /* if (item.ID <= 0) {
                                         // toastr.options.timeOut = 3000;
                                         this.errorMessage("POP must be selected.");
                                         return;
                                     }*/
                                    if (item.officeid <= 0) {
                                        this.errorMessage("Office must be selected.");
                                        return;
                                    }
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
                                var url1 = "ifrinsertNewLocalLoop";
                                this.loading = true;
                                axios.post(context + 'lli/application/' + url1 + '.do', {'ifr': JSON.stringify(localLoops)})
                                    .then(result => {
                                        if (result.data.responseCode == 2) {
                                            toastr.error(result.data.msg);
                                            setTimeout(() => {
                                                location.reload(1);
                                            }, 5000);

                                            toastr.options.timeOut = 3000;
                                        } else if (result.data.responseCode == 1) {
                                            toastr.success("Your request has been processed", "Success");
                                            toastr.options.timeOut = 3000;
                                            window.location.href = context + 'lli/application/search.do';
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
                                // swal("Select again");
                                // swal("Select again");
                                $('#newLocalLoopModal').modal({show: true});
                        }
                    });

            } else if (loopList.length > this.application.portCount) {
                this.errorMessage("Number of pop must be less then or equal to the number of port requested.");
                return;

            } else {
                var localLoops = {
                    applicationID: applicationID,
                    comment: this.comment,
                    pops: [],
                    stateID: parseInt(this.picked),
                    nextState: this.picked,
                };
                for (var i in loopList) {
                    var item = loopList[i];
                    if (item.pop == null) {
                        this.errorMessage("POP must be selected.");
                        return;
                    }
                    if (item.pop != null) {
                        item.ID = item.pop.ID;
                        if (item.ID <= 0) {
                            // toastr.options.timeOut = 3000;
                            this.errorMessage("POP must be selected.");
                            return;
                        }
                    }
                    /* if (item.ID <= 0) {
                         // toastr.options.timeOut = 3000;
                         this.errorMessage("POP must be selected.");
                         return;
                     }*/
                    if (item.officeid <= 0) {
                        this.errorMessage("Office must be selected.");
                        return;
                    }
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
                var url1 = "ifrinsertNewLocalLoop";
                this.loading = true;
                axios.post(context + 'lli/application/' + url1 + '.do', {'ifr': JSON.stringify(localLoops)})
                    .then(result => {
                        if (result.data.responseCode == 2) {
                            toastr.error(result.data.msg);
                            setTimeout(() => {
                                location.reload(1);
                            }, 5000);

                            toastr.options.timeOut = 3000;
                        } else if (result.data.responseCode == 1) {
                            toastr.success("Your request has been processed", "Success");
                            toastr.options.timeOut = 3000;
                            window.location.href = context + 'lli/application/search.do';
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
                } else if (item.srcName == "" && item.src != 1) {
                    this.errorMessage("Source Name must be provided.");
                    return;
                } else if (item.destinationName == "" && item.destination != 3) {
                    this.errorMessage("Destination Name must be provided.");
                    return;
                } else if (item.loopProvider == -1) {
                    this.errorMessage("Loop Provider must be provided.");
                    return;
                } else if (item.vendorID <= 0) {
                    this.errorMessage("Please Select A vendor");
                    return;
                } else if (item.ofcType <= 0) {
                    this.errorMessage("OFC type must be provided.");
                    return;
                }
                //end: validation code

                if (item.destination == 3) {
                    for (var officeIndex = 0; officeIndex < this.application.officeList.length; officeIndex++) {
                        if (this.application.officeList[officeIndex].id == item.officeid)
                            item.destinationName = this.application.officeList[officeIndex].officeAddress;
                    }
                }
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
                    "ofcType": item.ofcType,
                });
            }
            if (localLoops.comment === "") {
                this.errorMessage("Please provide a comment.");
                return;
            }
            var url1 = "efrinsert";
            this.loading = true;
            axios.post(context + 'lli/application/' + url1 + '.do', {'efr': JSON.stringify(localLoops)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                        setTimeout(() => {
                            location.reload(1);
                        }, 5000);

                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = context + 'lli/application/search.do';
                    } else {
                        toastr.error("Your request was not accepted", "Failure");
                    }
                    this.loading = false;
                })
                .catch(function (error) {
                    this.loading = false;
                });
        },
        processEFRRequestForNewLocalLoop: function (url) {
            //debugger;
            var loopList = this.content.newOfficeList[0].localLoopList;
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
                } else if (item.srcName == "" && item.src != 1) {
                    this.errorMessage("Source Name must be provided.");
                    return;
                } else if (item.destinationName == "" && item.destination != 3) {
                    this.errorMessage("Destination Name must be provided.");
                    return;
                } else if (item.vendorID == 0) {
                    this.errorMessage("Please Select A vendor");
                    return;
                }
                //end: validation code
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
                });
            }
            if (localLoops.comment === "") {
                this.errorMessage("Please provide a comment.");
                return;
            }
            this.loading = true;
            var url1 = "efrinsertForLocalLoop";
            axios.post(context + 'lli/application/' + url1 + '.do', {'efr': JSON.stringify(localLoops)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                        setTimeout(() => {
                            location.reload(1);
                        }, 5000);

                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = context + 'lli/application/search.do';
                    } else {
                        toastr.error("Your request was not accepted", "Failure");
                    }
                    this.loading = false;
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
            axios.post(context + 'lli/application/' + url + '.do', {'data': JSON.stringify(localLoops)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                        setTimeout(() => {
                            location.reload(1);
                        }, 5000);

                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = context + 'lli/application/search.do';
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
            let location;
            if (url === 'workorder') location = context + 'pdf/view/work-order.do?appId=' + applicationID + '&module=7&vendorId=' + loggedInUserID;
            if (url === 'demandnote') location = context + 'pdf/view/demand-note.do?billId=' + this.application.demandNoteID;
            if (url === 'advicenote') location = context + 'pdf/view/advice-note.do?appId=' + applicationID + "&module=7";

            // window.location.href = location;
            window.open(
                location,
                '_blank'
            );
        },
        showOfficeDetails: function () {
            $('#showOfficeDetails').modal({show: true});
        },
        onChangeVlanType: function (app, vlanType, index) {
            if (vlanType === '2') this.onchangePort(app, index);
            else {
                if (this.application.applicationType.ID == this.TYPE_FACTORY.ADDITIONAL_LOCAL_LOOP ||
                    this.application.applicationType.ID == this.TYPE_FACTORY.ADDITIONAL_PORT) {
                    if (!app.newlocalloops[index].router_switchID) {
                        app.localloops[index].vlans = [];
                    } else {
                        this.onchangePort(app, index);
                    }
                } else {
                    if (!app.localloops[index].router_switchID) {
                        app.localloops[index].vlans = [];
                    } else this.onchangePort(app, index);
                }

            }
        },
        onChangeSwitchRouter: function (app, index) {
            var catagory = 99;
            var parent = null;
            if (this.application.applicationType.ID == this.TYPE_FACTORY.ADDITIONAL_LOCAL_LOOP ||
                this.application.applicationType.ID == this.TYPE_FACTORY.ADDITIONAL_PORT) {
                parent = app.newlocalloops[index].router_switchID;
            } else parent = app.localloops[index].router_switchID;
            var data = {
                catagory: catagory,
                parent: parent
            };
            // debugger;
            var url = "getchild";
            axios.post(context + 'lli/application/' + url + '.do', {'data': JSON.stringify(data)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    } else if (result.data.responseCode == 1) {
                        if (this.application.applicationType.ID == this.TYPE_FACTORY.ADDITIONAL_LOCAL_LOOP ||
                            this.application.applicationType.ID == this.TYPE_FACTORY.ADDITIONAL_PORT) {
                            this.application.newlocalloops[index].ports = result.data.payload;
                        } else this.application.localloops[index].ports = result.data.payload;
                        this.$forceUpdate();
                    } else {
                        toastr.error("Your request was not accepted", "Failure");
                    }
                })
                .catch(function (error) {
                });
            this.onchangePort(app, index);
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
                "moduleId": CONFIG.get("module", "lli")
            };
            // var url = "get-free-block";
            var url = "inventory/suggestion";
            axios.post(context + 'ip/' + url + '.do', {'params': data})
                .then(result => {
                    // debugger;
                    this.ipBlockID = 0;
                    if (result.data.responseCode == 2) {
                        this.ipFreeBlockList = [];
                        this.ipRoutingInfo = '';
                        this.ipAvailableRangleList = '';
                        this.errorMessage(result.data.msg);

                    } else {
                        // debugger;
                        this.ipFreeBlockList = result.data.payload;
                        if (this.ipFreeBlockList.length === 0) {

                            errorMessage("No free IP block found for the specified data ");

                        }
                        this.ipRoutingInfo = '';
                        this.ipAvailableRangleList = '';

                    }
                    // this.loading = false;
                })
                .catch(function (error) {
                });
        },
        onchangeIPBlockRange: function (data) {
            var blockSize = this.ipBlockSize;
            var postData = {
                "block": data,
                "size": blockSize
            };
            var url = "get-free-block";
            axios.post(context + 'ip/' + url + '.do', {
                "block": data,
                "size": blockSize
            })
                .then(result => {
                    if (result.data.responseCode === 1) {

                        this.ipAvailableRangleList = result.data.payload;
                        if (this.ipAvailableRangleList.length === 0) {
                            toastr.error("No IP Block of size " + blockSize + " is found");
                        }
                        this.ipAvailableIPID = 0;
                    } else {
                        errorMessage(result.data.msg);
                    }

                })
                .catch(function (error) {
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
        ipAvailableDelete: function (index) {
            this.ipAvailableSelected.splice(index, 1);
        },
        onchangePort: function (app, index) {
            let catagory = 6;
            let parent = null;
            if (this.application.applicationType.ID == this.TYPE_FACTORY.ADDITIONAL_LOCAL_LOOP ||
                this.application.applicationType.ID == this.TYPE_FACTORY.ADDITIONAL_PORT) {
                parent = app.newlocalloops[index].router_switchID;
            } else {
                parent = app.localloops[index].router_switchID;
            }
            let data = {
                catagory: catagory,
                parent: parent
            };
            if (this.application.applicationType.ID == this.TYPE_FACTORY.ADDITIONAL_LOCAL_LOOP ||
                this.application.applicationType.ID == this.TYPE_FACTORY.ADDITIONAL_PORT) {
                if (app.newlocalloops[index].vlanType === '2') {
                    catagory = 101;
                    data = {
                        catagory: catagory,
                    };
                }
            } else {
                if (app.localloops[index].vlanType === '2') {
                    catagory = 101;
                    data = {
                        catagory: catagory,
                    };
                }
            }
            var url = "getchild";
            axios.post(context + 'lli/application/' + url + '.do', {'data': JSON.stringify(data)})
                .then(result => {

                    if (this.application.applicationType.ID == this.TYPE_FACTORY.ADDITIONAL_LOCAL_LOOP ||
                        this.application.applicationType.ID == this.TYPE_FACTORY.ADDITIONAL_PORT) {
                        this.application.newlocalloops[index].vlans = result.data.payload;
                    } else this.application.localloops[index].vlans = result.data.payload;
                    this.$forceUpdate();
                    // this.loading = false;
                })
                .catch(function (error) {
                });

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
        getOfficeWithID: function (ID) {
            var result;
            if (this.application.applicationType.ID == this.TYPE_FACTORY.ADDITIONAL_LOCAL_LOOP
                || (this.application.applicationType.ID == this.TYPE_FACTORY.UPGRADE_CONNECTION && this.application.isNewLoop)
            ) {
                result = this.application.newOfficeList.filter(obj => {
                    return obj.id == ID
                });
            } else {
                result = this.application.officeList.filter(obj => {
                    return obj.id == ID
                });
            }
            // debugger;
            if (result.length > 0) return result[0].officeName;
        },
        forwardAdviceNote: function () {

            if (this.shouldForward) {
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
            // debugger;
            var url1 = "advicenotegenerate";
            axios.post(context + 'lli/application/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                        setTimeout(() => {
                            location.reload(1);
                        }, 5000);

                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = context + 'lli/application/search.do';
                    } else {
                        toastr.error("Your request was not accepted", "Failure");
                    }
                    this.loading = false;
                })
                .catch(function (error) {
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
            axios.post(context + 'lli/application/' + url + '.do', {'data': JSON.stringify(data)})
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
                })
                .catch(function (error) {
                });
        },
        forceUpdateDOM: function () {
            this.$forceUpdate();
        },
        checkIfAlreadyToLDPSelected: function (currentLoopData, allLocalLoops, currentIndex) {
            let returnValue = false;
            // debugger;

            if (
                this.application.applicationType.ID == this.TYPE_FACTORY.ADDITIONAL_LOCAL_LOOP ||
                this.application.applicationType.ID == this.TYPE_FACTORY.ADDITIONAL_PORT
            ) {
                for (let i = 0; i < currentIndex; i++) {
                    if (
                        allLocalLoops[i].officeid == currentLoopData.officeid
                        && allLocalLoops[i].popID == currentLoopData.popID
                        && allLocalLoops[i].destination == 2
                    ) {
                        this.content.newOfficeList[0].localLoopList[currentIndex].srcName = allLocalLoops[i].destinationName;
                        returnValue = true;
                        break;
                    }
                }
            } else {

                for (let i = 0; i < currentIndex; i++) {
                    if (
                        allLocalLoops[i].officeid == currentLoopData.officeid
                        && allLocalLoops[i].popID == currentLoopData.popID
                        && allLocalLoops[i].destination == 2
                    ) {
                        this.content.officeList[0].localLoopList[currentIndex].srcName = allLocalLoops[i].destinationName;
                        returnValue = true;
                        break;
                    }
                }
            }
            return returnValue;
        },

        createSWAL(msg) {
            swal(msg, {
                content: "input",
                showCancelButton: true,
                buttons: true,
            }).then((val) => {
                if (!val) {
                    errorMessage("Please leave a comment");
                    return;
                }

                let localLoops = {
                    // new connection
                    applicationID: applicationID,
                    comment: val,
                    nextState: this.picked, // common
                    applicationType: this.application.applicationType.ID,

                    // additional ip
                    pops: [],
                    stateID: parseInt(this.picked),
                    ipCount: this.application.ipCount
                };
                this.loading = true;
                Promise.resolve(axios.post(context + 'lli/application/changestate.do', {'data': JSON.stringify(localLoops)})
                    .then(result => {
                        if (result.data.responseCode == 2) {
                            toastr.error(result.data.msg);
                            setTimeout(() => {
                                location.reload(1);
                            }, 5000);

                        } else if (result.data.responseCode == 1) {
                            toastr.success("Your request has been processed", "Success");
                            window.location.href = context + 'lli/application/search.do';
                        }

                    })
                    .catch((err) => {
                        console.log(err);
                    })).then(() => this.loading = false);

            });
        }
    },
    created() {
        this.loading = true;
        axios({method: 'GET', 'url': context + 'lli/application/new-connection-get-flow.do?id=' + applicationID})
            .then(result => {
                this.fileUploadURL = context + "file/upload.do";
                this.isPageLoaded = true;


                if (result.data.payload.hasOwnProperty("members")) {
                    this.application = Object.assign(this.application, result.data.payload.members);
                } else {
                    this.application = Object.assign(this.application, result.data.payload);
                }
                // this.application.ifr = [];
                this.application.connectionName = '';
                this.application.applicationID = applicationID;
                this.application.vendorID = loggedInUserID;
                if (this.application.applicationType.ID == this.TYPE_FACTORY.ADDITIONAL_LOCAL_LOOP
                    || this.application.applicationType.ID == this.TYPE_FACTORY.ADDITIONAL_PORT) {
                    this.application.localloops = this.application.newOfficeList.loops;
                }

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
                this.demandNoteCheck();
                axios({method: 'GET', 'url': context + 'location/allzonesearch.do'})
                    .then(result => {
                        this.zoneList = result.data.payload;
                        this.loading = false;
                    }, error => {
                    });
                axios({method: "GET", "url": context + "lli/inventory/get-inventory-options.do?categoryID=" + 4})
                    .then(result => {
                        this.popList = result.data.payload;
                        if (this.application.content === "") {
                            // this.alreadySelectedLoopLength = this.application.officeList.length;
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
                                        priority: 4,
                                        officeid: this.application.officeList[i].loops[j].officeID,
                                        pop: this.popList.find(obj => {
                                            return obj.ID === this.application.officeList[i].loops[j].popID
                                        }),
                                        src: -1,
                                        srcName: '',
                                        destination: -1,
                                        destinationName: '',
                                        vendorID: -1,
                                    });
                                }
                            }
                            //addred by jami for new office List
                            for (var i = 0; i < this.application.newOfficeList.length; i++) {
                                for (var j = 0; j < this.application.newOfficeList[i].loops.length; j++) {
                                    this.content.newOfficeList[0].localLoopList.push({
                                        ID: 0,
                                        vlanID: 0,
                                        OCDistance: 0,
                                        btclDistance: 0,
                                        clientDistance: this.application.newOfficeList[i].loops[j].clientDistances,
                                        OC: undefined,
                                        ofcType: this.application.newOfficeList[i].loops[j].ofcType,
                                        comment: '',
                                        bandwidth: this.application.bandwidth,
                                        priority: 4,
                                        officeid: this.application.newOfficeList[i].loops[j].officeID,
                                        pop: this.popList.find(obj => {
                                            return obj.ID === this.application.newOfficeList[i].loops[j].popID
                                        }),
                                        src: -1,
                                        srcName: '',
                                        destination: -1,
                                        destinationName: '',
                                        vendorID: -1
                                    });
                                }
                            }
                        } else {
                            // this.content = JSON.parse(result.data.payload.content);
                        }
                        this.officelist = this.application.officeList;
                        //for newOfficeList
                        // alert('data ashche');
                        if (this.application.hasOwnProperty("ifr")) {
                            this.modifiedIFR = this.application.ifr.map(obj => {
                                var newObj = obj;
                                // debugger;
                                newObj.pop = this.popList.find(ob => {
                                    return ob.ID === obj.popID
                                });
                                newObj.office = this.officelist.find(ob => {
                                    return ob.id === obj.officeID
                                });
                                return newObj;
                            });
                            this.application.popList = this.popList;
                        }
                        //below part is for IFr for local loop add office list for ifr
                        this.newOfficeList = this.application.newOfficeList;
                        if (this.application.hasOwnProperty("ifr") &&
                            (this.application.applicationType.ID == this.TYPE_FACTORY.ADDITIONAL_LOCAL_LOOP
                                || this.application.applicationType.ID == this.TYPE_FACTORY.ADDITIONAL_PORT)) {
                            this.modifiedIFRForNewLocalLoop = this.application.ifr.map(obj => {
                                var newObj = obj;
                                // debugger;
                                newObj.pop = this.popList.find(ob => {
                                    return ob.ID === obj.popID
                                });
                                newObj.office = this.newOfficeList.find(ob => {
                                    return ob.id === obj.officeID
                                });
                                return newObj;
                            });
                        }
                    }, error => {
                    });
                // if (
                //     this.application.state == this.STATE_FACTORY.SEND_AN_TO_SERVER_ROOM
                //     || this.application.state == this.STATE_FACTORY.WITHOUT_LOOP_SEND_AN_TO_SERVER_ROOM
                //     || this.application.state == this.STATE_FACTORY.COMPLETE_MUX_CONFIGURATION
                //     || this.application.state == this.STATE_FACTORY.AN_GENERATE_AND_FORWARD
                //     || this.application.state == this.STATE_FACTORY.LLI_WITHOUT_LOOP_ADVICE_NOTE_PUBLISH_AND_FORWARD
                //     || this.application.state == this.STATE_FACTORY.WITHOUT_LOOP_COMPLETE_MUX_CONFIGURATION
                //
                // ) {
                var url = 'ip/region/getRegions.do';
                axios({method: 'GET', 'url': context + url})
                    .then(result => {
                        this.ipRegionList = result.data.payload;
                    }, error => {
                    });

                // }
                /*for client correction the following commented changes had been made*/
                /*let clientID = this.application.client.ID;
                if(clientID != undefined){
                    axios({ method: 'GET', 'url': context + 'lli/connection/get-active-connection-by-client.do?id=' + clientID})
                        .then(result => {
                            this.connectionOptionListByClientID = result.data.payload;
                        }, error => {
                        });
                }*/

                this.loading = false;

            }, error => {
                this.loading = false;

            });


    },
    computed: {
        noClientCorrectionNeeded: function () {
            return this.application.state != this.STATE_FACTORY.REQUESTED_CLIENT_FOR_CORRECTION
                && this.application.state != this.STATE_FACTORY.WITHOUT_LOOP_REQUESTED_CLIENT_FOR_CORRECTION
        },
        ccCheck: function () {
            return this.application.state == this.STATE_FACTORY.ACCOUNT_CC_REQUEST
                || this.application.state == this.STATE_FACTORY.WITHOUT_LOOP_ACCOUNT_CC_REQUEST
        },
        ifrRequest: function () {
            return this.application.state == this.STATE_FACTORY.IFR_REQUEST
                || this.application.state == this.STATE_FACTORY.FORWARDED_IFR_REQUEST
                || this.application.state == this.STATE_FACTORY.WITHOUT_LOOP_IFR_REQUEST
                || this.application.state == this.STATE_FACTORY.WITHOUT_LOOP_FORWARDED_IFR_REQUEST

        },
        ifrResponse: function () {
            return this.application.state == this.STATE_FACTORY.IFR_RESPONSE
                || this.application.state == this.STATE_FACTORY.FORWARDED_IFR_RESPONSE
                || this.application.state == this.STATE_FACTORY.WITHOUT_LOOP_IFR_RESPONSE
                || this.application.state == this.STATE_FACTORY.WITHOUT_LOOP_RETRANSFER_TO_LOCAL_DGM
                || this.application.state == this.STATE_FACTORY.WITHOUT_LOOP_FORWARDED_IFR_RESPONSE
                || this.application.state == this.STATE_FACTORY.SHIFT_BW_IFR_RESPONSE
        },
        efrRequested: function () {
            return this.application.state == this.STATE_FACTORY.REQUEST_EFR
                || this.application.state == this.STATE_FACTORY.ONLY_LOOP_FORWARDED_LDGM_REQUEST_EFR
                || this.application.state == this.STATE_FACTORY.FORWARDED_REQUEST_EFR
        },
        vendorResponseAndDemandNote: function () {
            return this.applicationFirstStateValue == this.STATE_FACTORY.DEMAND_NOTE
                || this.applicationSecondStateValue == this.STATE_FACTORY.DEMAND_NOTE
                || this.application.state == this.STATE_FACTORY.FORWARDED_EFR_RESPONSE
                || this.application.state == this.STATE_FACTORY.ONLY_LOOP_FORWARDED_EFR_RESPONSE
        },
        workOrderGenerate: function () {
            var x =
                parseInt(this.application.state) == parseInt(this.STATE_FACTORY.WORK_ORDER_GENERATE)
                || parseInt(this.application.state) == parseInt(this.STATE_FACTORY.CLOSE_CONNECTION_WORK_ORDER_GENERATE)
                || parseInt(this.application.state) == parseInt(this.STATE_FACTORY.FORWARDED_WORK_ORDER_GENERATE)
            ;
            return x;
        },
        workOrderComplete: function () {
            // debugger;
            // alert('wo complete');
            return (
                this.application.state == parseInt(this.STATE_FACTORY.WORK_ORDER_COMPLETE)
                || this.application.state == parseInt(this.STATE_FACTORY.FORWARDED_WORK_ORDER_COMPLETE)
            );
            // return false;
        },
        isItNewLocalLoop: function () {
            if (this.application.applicationType.ID == this.TYPE_FACTORY.ADDITIONAL_LOCAL_LOOP
                || this.application.applicationType.ID == this.TYPE_FACTORY.ADDITIONAL_PORT) {
                return true;
            } else return false;
        },

        isAdditionalIP: function () {
            if (this.application.applicationType.ID == this.TYPE_FACTORY.ADDITIONAL_IP) return true;
            return false;
        }

    },
    updated: function () {
        this.$nextTick(function () {
        })
    }
});
