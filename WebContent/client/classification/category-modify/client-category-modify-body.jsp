<div id="btcl-application">
    <btcl-body title="Client Management" subtitle="Category Modification">
        <btcl-body v-if="categories.length>0" title="Existing Categories in This System:">
            <h4 style="text-align: left" v-for="(category, i) in categories">
                {{(i + 1) +'. ' +  category.value}} <button v-on:click="startModification(category)">Edit</button>
            </h4>
        </btcl-body>



        <btcl-body v-if="beingEditted" title="Edit a Category">
            <btcl-input title="Client Category Name" :text.sync="selectedCategory.value" :required="true"></btcl-input>
            <button type="button"
                    class="btn btn-block green-haze"
                    @click="modifyCategory">Submit Modification
            </button>
        </btcl-body>
    </btcl-body>





</div>
<script src="${context}client/classification/category-modify/client-category-modify.js"></script>

