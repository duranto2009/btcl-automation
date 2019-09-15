<div id="btcl-application">
    <btcl-body title="Client Management" subtitle="Type Modification">
        <btcl-body v-if="types.length>0" title="Existing Types in This System:">
            <h4 style="text-align: left" v-for="(type, i) in types">
                {{(i + 1) +'. ' +  type.value}} <button v-on:click="startModification(type)">Edit</button>
            </h4>
        </btcl-body>



        <btcl-body v-if="beingEditted" title="Edit a Type">
            <btcl-input title="Client Type Name" :text.sync="selectedType.value" :required="true"></btcl-input>
            <button type="button"
                    class="btn btn-block green-haze"
                    @click="modifyType">Submit Modification
            </button>
        </btcl-body>
    </btcl-body>





</div>
<script src="${context}client/classification/type-modify/client-type-modify.js"></script>

