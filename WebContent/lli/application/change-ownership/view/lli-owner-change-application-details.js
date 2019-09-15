var vue = new Vue({
    el: "#btcl-application",
    data: {
        contextPath: context,
        application: {
        },
        officialLetterInfo: null,
        loading: false,
        an : null,
        dn : null,
    },
    methods: {

        viewAN: function () {
            var location = context + 'pdf/view/advice-note.do?appId=' + applicationID + "&module=7";
            window.open(
                location,
                '_blank'
            );
        },
        viewDN: function () {
            var location = context + 'pdf/view/demand-note.do?billId=' + this.application.demandNoteID;
            window.open(
                location,
                '_blank'
            );
        },

    },
    created() {
        this.loading = true;
        axios({method: 'GET', 'url': context + 'lli/ownershipChange/flow-data.do?id=' + applicationID})
            .then(result => {
                if(result.data.responseCode == 1){
                    if(result.data.payload.hasOwnProperty("members")){

                        this.application = result.data.payload.members;
                    }
                    else{
                        this.application = result.data.payload;
                    }
                }else {
                    errorMessage(result.data.msg);
                }


            }, error => {
            });

        axios({method: 'GET', 'url': context + 'lli/ownershipChange/get-ol-info.do?moduleId='+ 7 +'&appId=' + applicationID})
            .then(result => {
                if(result.data.payload.hasOwnProperty("elements")){

                    this.officialLetterInfo = result.data.payload.elements;
                }
                else{
                    this.officialLetterInfo = result.data.payload;
                }

                this.an = this.officialLetterInfo.find(i=> (i.type === "ADVICE_NOTE" && i.referType === "TO") );
                this.dn = this.officialLetterInfo.find(i=> (i.type === "DEMAND_NOTE" && i.referType === "TO") );
            }, error => {
            });

    },
    updated(){
        this.loading = false;
    }
});

