var vue = new Vue({
    el: "#btcl-application",
    data: {
        selectedCategory:'',
        categories: [],
        beingEditted: false,
        categoryName:'',
    },
    methods: {

        reloadThePage: function () {
            location.reload()
        },
        getCategories: function () {
            axios.get(context + "ClientType/getAllRegistrantCategories.do").then(res => {
                if (res.data.responseCode === 1) {
                    this.categories = res.data.payload;
                } else {
                    toastr.error("Error Loading categories", "Failure");
                }
            });
        },
        startModification: function (category) {
            this.beingEditted = true;
            this.selectedCategory = category;
        },
        modifyCategory: function () {
                let url = context+"client-classification/modify-client-category.do";
                axios.post(url, {
                    categoryId: this.selectedCategory.key,
                    categoryName : this.selectedCategory.value
                }).then(res=>{
                    if(res.data.responseCode === 1){
                        toastr.success("client category modified");
                        this.getCategories;
                        this.beingEditted=false;
                    }else {
                        toastr.error("Error in modifying client category", "Failure");
                    }
                }).catch(err=>{LOG(err)});


        }
    },
    mounted() {
        this.getCategories();
    },
    computed: {}
});
