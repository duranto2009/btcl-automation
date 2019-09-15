<div id="btcl-application">
    <btcl-body title="Client Management" subtitle="Tariff Category Modification">
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


            <btcl-field title="Category">
                <select v-model="categoryId" @change="getTariffCat" class="form-control">
                    <option value="" selected disabled hidden>Select Category</option>
                    <option :value="item.key" v-for="item in categoriesInAType">{{item.value}}</option>
                </select>
            </btcl-field>



            <btcl-field title="Tariff Category">
                <select v-model="tariffCatId"  class="form-control">
                    <option value="" selected disabled hidden>Select Type</option>
                    <option :value="item.key" v-for="item in tariffCats">{{item.value}}</option>
                </select>
            </btcl-field>

            <btcl-field v-if="initialTariffCat!=tariffCatId">
                <button type="button"
                        class="btn btn-block green-haze"
                        @click="modifyTariffCategory">Submit Modification
                </button>
            </btcl-field>

        </btcl-portlet>
    </btcl-body>





</div>
<script src="${context}client/classification/tariff-category-modify/client-tariff-category-modify.js"></script>

