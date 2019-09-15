var vue = new Vue({
    el: "#btcl-application",
    data: {
        selectedType:'',
        types: [],
        beingEditted: false,
        typeName:'',
    },
    methods: {
        reloadThePage: function () {
            location.reload()
        },
        getTypes: function () {
            axios.get(context + "ClientType/getAllRegistrantTypes.do").then(res => {
                if (res.data.responseCode === 1) {
                    this.types = res.data.payload;
                } else {
                    toastr.error("Error Loading types", "Failure");
                }
            });
        },
        startModification: function (type) {
            this.beingEditted = true;
            this.selectedType = type;
        },
        modifyType: function () {
                let url = context+"client-classification/modify-client-type.do";
                axios.post(url, {
                     typeId: this.selectedType.key,
                     typeName : this.selectedType.value
                }).then(res=>{
                    if(res.data.responseCode === 1){
                        toastr.success("client type modified");
                        this.getTypes;
                        this.beingEditted=false;
                    }else {
                        toastr.error("Error in modifying client type", "Failure");
                    }
                }).catch(err=>{LOG(err)});


        }
    },
    mounted() {
        this.getTypes();
    },
    computed: {}
});
