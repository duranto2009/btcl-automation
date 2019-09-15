<%@ page import="coLocation.CoLocationConstants" %>

<btcl-info v-if="application.state.name=='CLIENT_CORRECTION'" title="Client" :text=application.client.value></btcl-info>
<%--<btcl-info v-if="application.state.name=='CLIENT_CORRECTION'" title="Client" :text=application.client.value></btcl-info>--%>
<btcl-field v-else title="Client" required="true">
    <user-search-by-module :client.sync="application.client" :module="moduleId"></user-search-by-module>
</btcl-field>
<btcl-field title="Connection Type" required="true">
    <multiselect placeholder="Select Connection Type" v-model="application.connectionType" :options="connectionTypeList" label=value :allow-empty="false" :searchable=true open-direction="bottom"></multiselect>
</btcl-field>
<template v-if="application.client!=null">
    <btcl-field v-if="application.client.registrantType==1" title="Want's to Pay DN(Connection Charge)">
        <multiselect v-model="application.skipPayment" :options="[{id:1,value:'Yearly Bill'},{id:0,value:'Instantly'}]"
                     label=value :allow-empty="false" :searchable=true open-direction="bottom">
        </multiselect>
    </btcl-field>
</template>

<btcl-field title="Rack" required="true">
    <label class="radio-inline"><input type="radio" name="rackRadio" :value="true" checked v-model="application.rackNeeded">Yes</label>
    <label class="radio-inline"><input type="radio" name="rackRadio" :value="false" v-model="application.rackNeeded">No</label>
</btcl-field>
<template v-if="application.rackNeeded">
    <btcl-field title="Rack Size" required="true">
        <mounted-id-value-selection :model.sync="application.rackSize" url='co-location/get-inventory-template.do?id=<%=CoLocationConstants.INVENTORY_CATEGORY_RACK_SIZE%>'></mounted-id-value-selection>
    </btcl-field>
    <btcl-field title="Rack Space" required="true">
        <mounted-id-value-selection :model.sync="application.rackSpace" url='co-location/get-inventory-template.do?id=<%=CoLocationConstants.INVENTORY_CATEGORY_RACK_SPACE%>' :description="true"></mounted-id-value-selection>
    </btcl-field>
</template>
<btcl-field title="Power" required="true">
    <label class="radio-inline"><input type="radio" name="powerRadio" :value="true" checked v-model="application.powerNeeded">Yes</label>
    <label class="radio-inline"><input type="radio" name="powerRadio" :value="false" v-model="application.powerNeeded">No</label>

</btcl-field>
<template v-if="application.powerNeeded">
    <%--<btcl-field title="Power Type">--%>
    <%--<multiselect v-model="application.powerType" :options="powerTypeList" label=value :allow-empty="false" :searchable=true open-direction="bottom"></multiselect>--%>
    <%--</btcl-field>--%>
    <btcl-field title="Power Type" required="true">
        <mounted-id-value-selection :model.sync="application.powerType" url='co-location/get-inventory-template.do?id=<%=CoLocationConstants.INVENTORY_CATEGORY_POWER_TYPE%>'>

        </mounted-id-value-selection>
    </btcl-field>

    <btcl-input title="Power Supply" required="true" :text.sync="application.powerAmount" placeholder="Enter Power Amount" :number="true"></btcl-input>


</template>
<btcl-field title="Fiber" required="true">
    <label class="radio-inline"><input type="radio" name="fiberRadio" :value="true" checked v-model="application.fiberNeeded">Yes</label>
    <label class="radio-inline"><input type="radio" name="fiberRadio" :value="false" v-model="application.fiberNeeded">No</label>
</btcl-field>
<template v-if="application.fiberNeeded">
    <btcl-field title="Fiber Type" required="true">
        <mounted-id-value-selection :model.sync="application.fiberType" url='co-location/get-inventory-template.do?id=<%=CoLocationConstants.INVENTORY_CATEGORY_FIBER_TYPE%>'></mounted-id-value-selection>
    </btcl-field>
    <btcl-input title="No. of Fiber Core" required="true" :text.sync="application.fiberCore" :number="true" placeholder="Enter Fiber Core number."></btcl-input>
</template>

<btcl-field title="Floor Space" required="true">
    <label class="radio-inline"><input type="radio" name="floorSpaceRadio" :value="true" checked v-model="application.floorSpaceNeeded">Yes</label>
    <label class="radio-inline"><input type="radio" name="floorSpaceRadio" :value="false" v-model="application.floorSpaceNeeded">No</label>
</btcl-field>
<template v-if="application.floorSpaceNeeded">
    <btcl-field title="Floor Space Type" required="true">
        <mounted-id-value-selection :model.sync="application.floorSpaceType" url='co-location/get-inventory-template.do?id=<%=CoLocationConstants.INVENTORY_CATEGORY_FLOOR_SPACE%>'></mounted-id-value-selection>
    </btcl-field>
    <btcl-input title="Floor Space Amount(ft)" required="true" :text.sync="application.floorSpaceAmount" :number="true" placeholder="Enter Floor Space Amount."></btcl-input>
</template>


<btcl-input title="Description" :text.sync="application.description" placeholder="Write Description"></btcl-input>
<btcl-input title="Comment" :text.sync="application.comment" placeholder="Write Comment"></btcl-input>
<btcl-field title="Probable Connection Date" required="true">
    <btcl-datepicker :date.sync="application.suggestedDate" pattern="DD-MM-YYYY"></btcl-datepicker>
</btcl-field>

