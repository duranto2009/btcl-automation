var vue = new Vue({
    el: "#btcl-asn-application",
    data: {
        application: {
            client: null,
            comment: null,
            suggestedDate: null,
            description: null,
            formElements: [],
            action: [],
            color: "blue",
            comment: '',
            state: '',
        },
        isCollaborated: false,
        picked: {},
        NO_ACTION_PICKED: {},
        nextAction: {
            param: '',
        },
        comment: '',
        commentsList: null,
        loading:false,
    },
    methods: {
        nextStep: function (...arg) {
            if (Object.keys(this.picked).length === 0) {
                errorMessage("Please Select an Action.");
                return;
            }
            let nextAction = this.picked;
            this.nextAction = nextAction;
            if (nextAction) var commentTitle = " Comment on " + nextAction.description;
            if (Object.keys(this.picked).length === 0) {
                errorMessage("Please Select an Action.");
                return;
            }
            else swal(commentTitle, {
                    content: "input",
                    showCancelButton: true,
                    buttons: true,
                })
                    .then((value) => {
                        if (value === false) return false;
                        if (value === "") {
                            swal("Warning!", "You you need to write something!", "warning");
                            return false;
                        }
                        if (value) {
                            this.loading = true;
                            this.comment = value + '';
                            this.postRequest();
                        }
                    });
        },
        postRequest: function () {
            let apps = this.application;
            this.application['comment'] = this.comment;
            var url1 = this.nextAction.url;
            let postObject = {
                application: this.application,
            };
            this.postRequestedDataToServer(url1,postObject, this.redirectURLBuilder());
        },
        postRequestedDataToServer: function(url, postObject, redirect){
            this.loading = true;
            axios.post(context + 'asn/' + url + '.do', postObject)
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    }
                    else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = redirect;
                    }
                    else {
                        toastr.error("Your request was not accepted", "Failure");
                    }
                    this.loading = false;
                }).catch(function (error) {
                    this.loading = false;
            });
        },
        redirectURLBuilder: function(){
            var redirect = 'asn/search.do';
            return context + redirect;
        },
    },
    created() {
        this.loading = true;
        let flowURL  = 'asn/flow-data.do?id=' + applicationID;
        Promise.all(
            [
                axios({method: 'GET', 'url': context + flowURL})
                    .then(result => {
                        if (result.data.payload.hasOwnProperty("members")) {
                            this.application = Object.assign(this.application, result.data.payload.members);
                        }
                        else {
                            this.application = Object.assign(this.application, result.data.payload);
                            vue.loading = false;
                        }
                        this.application.applicationID = applicationID;
                        this.application.action.unshift({});
                    }, error => {
                    })]
        ).then(function (values) {
            vue.loading = false;
        });
    },
});