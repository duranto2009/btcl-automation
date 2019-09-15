<div id="btcl-application">
    <btcl-body title="Client Management" subtitle="Type Addition">
        <btcl-portlet>

            <btcl-field title="Module">
            <module-selection @module_change="getTypesInAModule"></module-selection>
            </btcl-field>
            <%----%>
            <%--<btcl-field title="Module">--%>
                <%--<select v-model="moduleId" @change="getTypesInAModule(moduleId)" class="form-control">--%>
                    <%--<option value="" selected disabled hidden>Select Module</option>--%>
                    <%--<option :value="item.key" v-for="item in modules">{{item.value}}</option>--%>
                <%--</select>--%>
            <%--</btcl-field>--%>

            <btcl-input title="Client Type Name" :text.sync="typeName" :required="true"></btcl-input>

            <button type="button"
                    class="btn btn-block green-haze"
                    @click="addNewType">Add New Type
            </button>
        </btcl-portlet>
    </btcl-body>


    <btcl-body v-if="typesInAModule.length>0" title="Existing Types in This Module:">
        <h4 style="text-align: left" v-for="(type, i) in typesInAModule">
            {{(i + 1) +'. ' +  type.value}}
        </h4>
    </btcl-body>


</div>
<script src="${context}client/classification/type/client-type-add.js" type="module"></script>

