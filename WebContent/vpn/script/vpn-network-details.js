var vue = new Vue({
    el: "#btcl-vpn-application",

    data: {
        linkList:[],
        link: {},
    },
    methods: {
        getConnectionByHistoryID: function(id){
            this.link = this.linkList[id];
        },


    },
    created() {
        this.loading = true;
        axios({method: 'GET', 'url': context + 'vpn/network/data.do?id=' + applicationID})
            .then(result => {
                // debugger;
                if (result.data.payload.hasOwnProperty("members")) {
                // this.application = result.data.payload.members;
                this.linkList = Object.assign(this.linkList, result.data.payload.members);
                }
                else {
                // this.application = result.data.payload;
                this.linkList = Object.assign(this.linkList, result.data.payload);
                }

                this.link = this.linkList[this.linkList.length-1];
            }, error => {
            });


    },
});