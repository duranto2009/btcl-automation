var vue = new Vue({
    el: "#btcl-application",
    data: {
        contextPath: context,
        application: {
            applicationType: {
                label: '',
            },
            action: [],
            connectionName: '#',
            officeList: [],
            ifr:[],
            efr: [],
            efr_vendor:[],


        },
        officialLetterInfo: null,
        loading: false,
        an : null,
        dn : null,
        wo : null,

    },
    methods: {
        showOfficeDetails: function(){
            // alert('Details of Office');
            $('#showOfficeDetails').modal({show: true});
            // showOfficeDetails
        },
        viewAN: function () {
            var location = context + 'pdf/view/advice-note.do?appId=' + applicationID + "&module=9";
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
        viewWO: function (o) {
            // link+=request.getContextPath()+"/lli/pdf/workorder.do?applicationid="+ol.getApplicationId() +"&vendorid="+oc.getRecipientId();
            var location = context + 'pdf/view/work-order.do?appId='+o.appId +'&vendorId='+o.recipientId+"&module=9";
            window.open(
                location,
                '_blank'
            );
        }
    },
    created() {
        this.loading = true;
        Promise.all(
            [
                axios({method: 'GET', 'url': context + 'nix/application/connection-view.do?id=' + applicationID})
            .then(result => {
                if(result.data.payload.hasOwnProperty("members")){

                    this.application = Object.assign(this.application,result.data.payload.members);
                }
                else{
                    this.application = Object.assign(this.application,result.data.payload);
                }
                this.loading = false;
            }, error => {
                this.loading = false;
            })
,
        axios({method: 'GET', 'url': context + 'nix/application/get-ol-info.do?moduleId='+ 9 +'&appId=' + applicationID})
            .then(result => {
                if(result.data.payload.hasOwnProperty("elements")){

                    this.officialLetterInfo = result.data.payload.elements;
                }
                else{
                    this.officialLetterInfo = result.data.payload;
                }

                this.an = this.officialLetterInfo.find(i=> (i.type === "ADVICE_NOTE" && i.referType === "TO") );
                this.dn = this.officialLetterInfo.find(i=> (i.type === "DEMAND_NOTE" && i.referType === "TO") );
                this.wo = this.officialLetterInfo.filter(i=> (i.type === "WORK_ORDER" && i.referType === "TO" && i.recipientType == "VENDOR") );


            }, error => {
            })
            ]).then((value => this.loading=false))

    },
    updated(){
        this.loading = false;
    }
});

