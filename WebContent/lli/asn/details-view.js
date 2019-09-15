var vue = new Vue({
    el: "#btcl-asn-application",
    data: {
        application: {
            client: null,
            comment: null,
            suggestedDate: null,
            description: null,
            formElements: [],
            color: "blue",
            comment: '',
            state: '',
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