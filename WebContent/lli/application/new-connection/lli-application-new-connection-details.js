let vue = new Vue({
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
            let location = context + 'pdf/view/advice-note.do?appId=' + applicationID + "&module=7";
            window.open(
                location,
                '_blank'
            );
        },
        viewDN: function () {
            let location = context + 'pdf/view/demand-note.do?billId=' + this.application.demandNoteID;
            window.open(
                location,
                '_blank'
            );
        },
        viewWO: function (o) {
            // link+=request.getContextPath()+"/lli/pdf/workorder.do?applicationid="+ol.getApplicationId() +"&vendorid="+oc.getRecipientId();
            let location = context + 'pdf/view/work-order.do?appId='+o.appId +'&module=7&vendorId='+o.recipientId;
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
        axios({method: 'GET', 'url': context + 'lli/application/connection-view.do?id=' + applicationID})
            .then(result => {
                if(result.data.payload.hasOwnProperty("members")){

                    this.application = Object.assign(this.application,result.data.payload.members);
                }
                else{
                    this.application = Object.assign(this.application,result.data.payload);
                }

            }, error => {
            })
        ,

        axios({method: 'GET', 'url': context + 'lli/application/get-ol-info.do?moduleId='+ 7 +'&appId=' + applicationID})
            .then(result => {
                if(result.data.payload.hasOwnProperty("elements")){

                    this.officialLetterInfo = result.data.payload.elements;
                }
                else{
                    this.officialLetterInfo = result.data.payload;
                }

                this.an = this.officialLetterInfo.find(i=> (i.type === "ADVICE_NOTE" && i.referType === "TO") );
                this.dn = this.officialLetterInfo.find(i=> (i.type === "DEMAND_NOTE" && i.referType === "TO") );
                this.wo = this.officialLetterInfo.filter(i=> (
                    i.type === "WORK_ORDER"
                    && i.referType === "TO"
                    &&
                        (i.recipientType == "VENDOR"
                        || i.recipientType == "BTCL_OFFICIAL"
                        )
                    && loggedInAccount.ID == i.recipientId
                    )

                );


            }, error => {
            })
        ]).then(()=>this.loading=false);

    },
    updated(){
        // alert('called updated');
        this.loading = false;
    }
});

