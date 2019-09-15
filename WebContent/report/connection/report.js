let vue = new Vue({
    el: "#btcl-application-report",
    data: {
        module:null,
        contextPath: context,
        application: {
            criteria:[],
            display:[],
            orderby:[],
        },
        reportData:{
        },
        dataTable:null,
        selectedCriteria:[],
        selectedCriteriaList:[],
        selectedDisplay:[],
        selectedDisplayList:[],
        selectedOrder:[],
        selectedOrderByList:[],
        loading: false,
    },
    computed: {
        csvData() {
            return this.reportData.mapArrayList;
        }
    },
    methods: {
        csvExport(arrData) {
            let csvContent = "data:text/csv;charset=utf-8,";
            csvContent += [
                Object.keys(arrData[0]).join(";"),
                ...arrData.map(item => Object.values(item).join(";"))
            ]
                .join("\n")
                .replace(/(^\[)|(\]$)/gm, "");

            const data = encodeURI(csvContent);
            const link = document.createElement("a");
            link.setAttribute("href", data);
            link.setAttribute("download", "export.csv");
            link.click();
        },
        submitFormData: function(){
            vue.reportData = {};

            let url1 = "fetch-connection-report";
            if(this.selectedDisplayList.length==0){
                toastr.error("Select at least one display item", 'Failure');
                return;

            }
            let postObject = {
                selectedSearchCriteria:this.selectedCriteriaList,
                selectedDisplayItems: this.selectedDisplayList,
                selectedOrderItems: this.selectedOrderByList
            };
            this.loading = true;
            Promise.resolve(
            axios.post(context + 'report/' + url1 + '.do', {'application': postObject,'module':this.module})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    } else if (result.data.responseCode == 1) {
                        this.reportData = result.data.payload;
                        if(this.reportData.mapArrayList ==null && this.reportData.mapArrayList.length ==0 ){
                            toastr.error("No Data Found", "Failure");
                            vue.reportData = {};

                        }
                        else toastr.success("Your request has been processed", "Success");
                    }
                    else {
                        toastr.error("Your request was not accepted", "Failure");
                    }
                })
                .catch(function (error) {
                })).then(()=>this.loading=false);
        },

    },
    created() {
        //this.dataTable = null;
        let url_string=location.href;
        let url = new URL(url_string);
        this.module = url.searchParams.get("module");
        axios({ method: 'GET', 'url': context + 'report/get-connection-check-boxes.do?module=' + this.module})
            .then(result => {
                if(result.data.payload.hasOwnProperty("members")){

                    this.application = result.data.payload.members;
                }
                else{
                    this.application = result.data.payload;
                }
            }, error => {
            });
    },

    watch: {
        'selectedCriteria': function () {
            this.selectedCriteriaList = [];
            for (let i = 0; i < this.selectedCriteria.length; i++) {
                let index = this.selectedCriteria[i];
                this.selectedCriteriaList.push( this.application.criteria[index]);
            }
        },
        'selectedDisplay': function () {
            this.selectedDisplayList = [];
            for (let i = 0; i < this.selectedDisplay.length; i++) {
                let index = this.selectedDisplay[i];
                this.selectedDisplayList.push( this.application.display[index]);
            }
        },
        'selectedOrder': function () {
            this.selectedOrderByList = [];
            for (let i = 0; i < this.selectedOrder.length; i++) {
                let index = this.selectedOrder[i];
                this.selectedOrderByList.push( this.application.orderby[index]);
            }
        }
    },
});

