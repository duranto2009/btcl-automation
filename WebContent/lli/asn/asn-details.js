var vue = new Vue({
    el: "#btcl-asn",
    data: {
        application: {
        },
        loading:false,
    },
    methods: {},
    created() {
        let flowURL  = 'asn/flow-data-view.do?id=' + asnId;
        Promise.all(
            [
                axios({method: 'GET', 'url': context + flowURL})
                    .then(result => {
                        if (result.data.payload.hasOwnProperty("members")) {
                            this.application = Object.assign(this.application, result.data.payload.members);
                        }
                        else {
                            this.application = Object.assign(this.application, result.data.payload);
                        }
                        this.loading = true;
                    }, error => {
                    })]
        ).then(function (values) {
        });
    },
});