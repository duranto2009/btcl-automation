import {moduleSelection} from "../../../vue-components/moduleSelection.js";

var vue = new Vue({
    el: "#btcl-application",

    components: {
        "module-selection": moduleSelection,
    },

    data: {
        moduleId: '',
        modules: [],
        typeName: '',
        typesInAModule: [],

    },
    methods: {
        addNewType: function () {
            let url = context + "client-classification/add-type-under-a-module.do";
            axios.post(url, {
                moduleId: this.moduleId,
                typeName: this.typeName
            }).then(res => {
                if (res.data.responseCode === 1) {
                    toastr.success("client type added");
                    this.getTypesInAModule(this.moduleId);
                } else {
                    toastr.error("Error in adding client type", "Failure");
                }
            }).catch(err => {
                LOG(err)
            });
        },

        reloadThePage: function () {
            location.reload()
        },
        getModules: function () {
            axios.get(context + "ClientType/getAllModules.do").then(res => {
                if (res.data.responseCode === 1) {
                    this.modules = res.data.payload;
                } else {
                    toastr.error("Error Loading modules", "Failure");
                }
            });
        },
        getTypesInAModule: function (moduleId) {
            // debugger;
            axios.get(context + "ClientType/GetRegistrantTypesInAModule.do?moduleID=" + moduleId).then(res => {
                if (res.data.responseCode === 1) {
                    this.typesInAModule = res.data.payload;
                } else {
                    toastr.error("Error getting types under this module", "Failure");
                }
            });
        }
    },
    mounted() {
        this.getModules();
    },
    computed: {}
});
