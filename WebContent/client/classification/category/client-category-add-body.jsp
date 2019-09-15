<div id="btcl-application">
    <btcl-body title="Client Management" subtitle="Category Addition">
        <btcl-portlet>
            <btcl-field title="Module">
                <select v-model="moduleId" @change="getTypesInAModule(moduleId)" class="form-control">
                    <option value="" selected disabled hidden>Select Module</option>
                    <option :value="item.key" v-for="item in modules">{{item.value}}</option>
                </select>
            </btcl-field>


            <btcl-field title="Type">
                <select v-model="typeId" @change="getCategoriesInAType(typeId)" class="form-control">
                    <option value="" selected disabled hidden>Select Type</option>
                    <option :value="item.key" v-for="item in typesInAModule">{{item.value}}</option>
                </select>
            </btcl-field>




            <btcl-input title="Client Category Name" :text.sync="categoryName" :required="true"></btcl-input>



            <btcl-field title="Tariff Category">
                <select v-model="tariffCatId"  class="form-control">
                    <option value="" selected disabled hidden>Select Type</option>
                    <option :value="item.key" v-for="item in tariffCats">{{item.value}}</option>
                </select>
            </btcl-field>



            </btcl-field>
            <button type="button"
                    class="btn btn-block green-haze"
                    @click="addNewCategory">Add New Category
            </button>
        </btcl-portlet>
    </btcl-body>


    <btcl-body v-if="categoriesInAType.length>0" title="Existing Categories in This Type Under This Module:">
        <h4 style="text-align: left" v-for="(category, i) in categoriesInAType">
            {{(i + 1) +'. ' +  category.value}}
        </h4>
    </btcl-body>


</div>
<script src="${context}client/classification/category/client-category-add.js"></script>

