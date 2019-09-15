var vue = new Vue({
    el: "#btcl-application",
    data: {
        moduleId: '',
        modules: [],
        typeId: '',
        typesInAModule:[],
        categoriesInAType: [],
        categoryId:'',
        initialTariffCat:'',
        tariffCatId:'',
        tariffCats:[],

    },
    methods: {
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

        getTariffCat:function () {
            let url = context + "client-classification/get-tariff-category.do";
            axios.post(url, {
                moduleId: this.moduleId,
                typeId: this.typeId,
                categoryId: this.categoryId,
            }).then(res => {
                if (res.data.responseCode === 1) {
                    // toastr.success("client tariff cat fetched");
                    this.tariffCatId = res.data.payload;
                    this.initialTariffCat = res.data.payload;
                } else {
                    toastr.error("Error in fetching client tariff category", "Failure");
                }
            }).catch(err => {
                LOG(err)
            });
        },

        modifyTariffCategory: function () {
            let url = context + "client-classification/modify-tariff-category.do";
            axios.post(url, {
                moduleId: this.moduleId,
                typeId: this.typeId,
                categoryId: this.categoryId,
                tariffCatId: this.tariffCatId,
            }).then(res => {
                if (res.data.responseCode === 1) {
                    toastr.success("client tariff cat modified");
                    this.reloadThePage();
                } else {
                    toastr.error("Error in modifying client tariff category", "Failure");
                }
            }).catch(err => {
                LOG(err)
            });
        }


    },
    mounted() {
        this.getModules();
        this.getTariffCategories();
    },
    computed: {}
});
