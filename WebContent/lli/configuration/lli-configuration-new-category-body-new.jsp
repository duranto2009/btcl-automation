<div id="btcl-new-category" v-cloak="true">
    <btcl-body title="New Category Add">
        <btcl-portlet>
            <btcl-input title="Category Name" id="categoryName" :text.sync = "category.categoryName"></btcl-input>
            <btcl-grid >
                <button type=button class="btn green-haze btn-block btn-lg" @click="addNewCategory">Generate</button>
            </btcl-grid>
        </btcl-portlet>
    </btcl-body>

    <btcl-portlet>
        <div id="categoryTableDiv" style ="height: -webkit-fill-available;">
          <div class="col-md-12 col-sm-12 col-xs-12">
           	<div class="form-group col-md-5 col-xs-5 col-sm-5">
               	<h1>Category Name</h1>
           	</div>
          </div>
          <ul> <li class="form-group col-md-12 col-sm-12 col-xs-12"
           	 v-for="cat in categories" 
           	:key="cat.id">
               <div class="col-md-5 col-sm-5 col-xs-5">{{ cat.categoryName }} 
               	
               </div>
               <div class="col-md-5 col-sm-5 col-xs-5">
               	<button class="btn btn-submit-btcl" 
               			type="button"
               			@click="showEditForm(cat, $event)">Edit
               	</button>
               </div>
            </li></ul>
            <div id="categoryEdit" 
            	 style ="padding-top:10%;"
               	 class="hidden x_content">
                   <form action="../lli/configuration/edit-category.do"
                   	  class="form-horizontal form-label-left" 
                   	  method="post" 
                   	  id="editCategoryForm">
                       <div class="form-group">
                           <label class=" col-md-3 col-sm-3 col-xs-3" style="text-align:right;">Category Name
                           </label>
                           <input class="col-md-5 col-sm-5 col-xs-5" id="categoryNameId"/>
                       </div>

                       <div class="form-group">
                           <div class="col-md-9 col-sm-9 col-xs-12 col-md-offset-3">
                               <button type="button" 
                               		class="btn btn-success" 
                               		id="categoryEditButton" 
                               		@click="editCategory">
                                   <span> Submit</span>
                               </button>
                               <button class="btn btn-primary" 
                               		type="button" 
                               		id="cancel"
                               		@click="hideEdit">
                                   <span> Cancel</span>
                               </button>
                           </div>
                       </div>
                   </form>
               </div>
        </div>
    </btcl-portlet>
</div>


<script src="../../configuration/lli-configuration-new-category-body.js"></script>

<script>
    var catId = null;
    $(document).ready(function () {
        $("#cancel").click(function () {
            console.log('cancelling edit');
            $("#categoryEdit").addClass('hidden');
        });

        $("#editCategoryForm").on("submit", function(event) {
            event.preventDefault();
        });
    });

    function ResponseCallback(data) {
        console.log(data.payload);
        if (data.responseCode == 1){
            toastr.success(data['msg'], "Success");
        }
        else {
            toastr._error(data['msg']);
        }
    }
</script>