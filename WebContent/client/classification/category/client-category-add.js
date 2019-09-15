var vue = new Vue({
    el: "#btcl-application",
    data: {
        moduleId: '',
        modules: [],
        typeId: '',
        typesInAModule:[],
        categoryName: '',
        categoriesInAType: [],
        tariffCatId:'',
        tariffCats:[],

    },
    methods: {
        addNewCategory: function () {
            let url = context + "client-classification/add-category-under-a-type.do";
            axios.post(url, {
                moduleId: this.moduleId,
                typeId: this.typeId,
                categoryName: this.categoryName,
                tariffCatId: this.tariffCatId,
            }).then(res => {
                if (res.data.responseCode === 1) {
                    toastr.success("client category added");
                    this.getCategoriesInAType(this.typeId);
                } else {
                    toastr.error("Error in adding client category", "Failure");
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

        getTariffCategories: function(){
            axios.get(context + "ClientType/getTariffCategories.do").then(res => {
                if (res.data.responseCode === 1) {
                    this.tariffCats = res.data.payload;
                } else {
                    toastr.error("Error Loading tariff categories", "Failure");
                }
            });
        },

        getTypesInAModule: function (moduleId) {
            axios.get(context + "ClientType/GetRegistrantTypesInAModule.do?moduleID=" + moduleId).then(res => {
                if (res.data.responseCode === 1) {
                    this.typesInAModule = res.data.payload;
                } else {
                    toastr.error("Error getting types under this module", "Failure");
                }
            });
        },



        getCategoriesInAType: function (typeId) {
            axios.get(context + "ClientType/GetRegistrantCategory.do?registrantType=" + typeId +"&moduleID=" + this.moduleId).then(res => {
                if (res.data.responseCode === 1) {
                    this.categoriesInAType = res.data.payload;
                } else {
                    toastr.error("Error getting categories under this type", "Failure");
                }
            });

        },


    },
    mounted() {
        this.getModules();
        this.getTariffCategories();
    },
    computed: {}
});
