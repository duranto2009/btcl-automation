var vue = new Vue({
    el: "#btcl-new-category",
    data: {
        contextPath: context,
        category: {},
        categories: {},
        showDiv : false,
        catId: null,
        newCategory: {},
    },
    methods: {
        showEditForm: function(cat, event){
            this.newCategory.id = cat.id;
            $("#categoryNameId").val(cat.categoryName);
            $("#categoryEdit").removeClass('hidden');
        },
        hideEdit:function(){
            $("#categoryEdit").addClass('hidden');

        },
        toggle : function () {
            this.showDiv = true;
        },
        editCategory: function(){
            var name = $("#categoryNameId").val();
            this.newCategory.categoryName = name;
            console.log(this.newCategory);
            var url = context+'lli/configuration/edit-category.do?id='+this.newCategory.id+'&';
            url += 'categoryName='+this.newCategory.categoryName;
            axios({
                method: 'POST',
                url: url,
                // data: this.newCategory
            }).then(result => {
                if (result.data.responseCode == 1){
                    toastr.success('Category has been edited');
                    $("#categoryEdit").addClass('hidden');
                    this.reload();
                }
                else if (result.data.responseCode == 2){
                    toastr.error('Category not edited');
                    $("#categoryEdit").addClass('hidden');
                }
            });
        },
        addNewCategory: function () {
            var url = context+'lli/configuration/add-new-category.do?categoryName='+this.category["categoryName"];
            console.log(url);
            axios({
                method: 'POST',
                url: url
            }).then(result => {
                if (result.data.responseCode == 1){
                    toastr.success('Your data has been submitted', "Success");
                    this.reload();
                }
                else if (result.data.responseCode == 2){
                    toastr.error('Data not submitted', "Error");
                }
            });
        },
        getAllCategories: function(){
            var url = context+'lli/configuration/get-all-categories.do';

            axios.get(url).then(response => {
                if (response.data.responseCode == 1){
                    this.categories = response.data.payload;
                    toastr.success('All categories retrieved', 'Success');
                }
                else if (response.data.responseCode == 2){
                    toastr.error('Error retrieving categories', 'Error');
                }
            });
        },
        reload: function(){
            window.location.href = context+'lli/configuration/new-category-ui/get.do';
        }
    },
    mounted() {
        this.getAllCategories();
    }
});

