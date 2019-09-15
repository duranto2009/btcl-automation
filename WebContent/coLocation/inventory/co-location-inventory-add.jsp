<%@ page import="coLocation.CoLocationConstants" %><%--
  Created by IntelliJ IDEA.
  User: forhad
  Date: 12/6/18
  Time: 11:49 AM
--%>
<div id=btcl-co-location-application>
    <btcl-body title="Co-Location" subtitle='Inventory'>

        <div class="portlet-body form">
            <div class="form-body">
                <div class="tabbable-custom ">
                    <ul class="nav nav-tabs ">

                        <li class="active">
                            <a href="#tab_1_1_4" data-toggle="tab" id="y_1_1_4" aria-expanded="false" @click="setCategory(<%=CoLocationConstants.INVENTORY_POWER%>)"> Power </a>
                        </li>
                        <li class="">
                            <a href="#tab_1_1_3" data-toggle="tab" id="y_1_1_3" aria-expanded="true" @click="setCategory(<%=CoLocationConstants.INVENTORY_RACK%>)"> Rack </a>
                        </li>
                        <li>
                            <a href="#tab_1_1_5" data-toggle="tab" id="y_1_1_5" @click="setCategory(<%=CoLocationConstants.INVENTORY_FIBER%>)">OFC</a>
                        </li>
                        <li>
                            <a href="#tab_1_1_7" data-toggle="tab" id="y_1_1_7" @click="setCategory(<%=CoLocationConstants.INVENTORY_CATEGORY_FLOOR_SPACE%>)">Floor Space</a>
                        </li>
                    </ul>
                    <div class="tab-content" style="padding: 0px;">

                        <div class="tab-pane active" id="tab_1_1_4">
                            <form class="form-horizontal inventory-add-form" method="post" action="../../AddInventory.do" novalidate="novalidate">
                                <div class="form-body">

                                    <btcl-field title="Power Type">
                                        <mounted-id-value-selection :model.sync="type" url='co-location/get-inventory-template.do?id=<%=CoLocationConstants.INVENTORY_CATEGORY_POWER_TYPE%>'></mounted-id-value-selection>
                                    </btcl-field>

                                    <btcl-input title="Power Supply" :text.sync="amount" placeholder="Enter Power Amount" :number="true"></btcl-input>
                                    <btcl-input title="Power Brand" :text.sync="brand" placeholder="Write Brand Name"></btcl-input>
                                    <btcl-input title="Power Model" :text.sync="model" placeholder="Write Model Name"></btcl-input>

                                </div>

                                <div class="form-actions text-center">
                                    <button type=button class="btn green-haze btn-block btn-lg" @click="submitInventoryAddForm">Submit</button>
                                </div>
                            </form>
                        </div>
                        <div class="tab-pane" id="tab_1_1_3" >
                            <form class="form-horizontal inventory-add-form">
                                <div class="form-body">
                                    <btcl-field title="Rack Size">
                                        <mounted-id-value-selection :model.sync="type" url='co-location/get-inventory-template.do?id=<%=CoLocationConstants.INVENTORY_CATEGORY_RACK_SIZE%>'></mounted-id-value-selection>
                                    </btcl-field>
                                    <%--<btcl-field title="Rack Space">--%>
                                    <%--<mounted-id-value-selection :model.sync="amount" url='co-location/get-inventory-template.do?id=<%=CoLocationConstants.INVENTORY_CATEGORY_RACK_SPACE%>'></mounted-id-value-selection>--%>
                                    <%--</btcl-field>--%>
                                    <btcl-input title="Rack Brand" :text.sync="brand" placeholder="Write Brand Name"></btcl-input>
                                    <btcl-input title="Rack Model" :text.sync="model" placeholder="Write Model Name"></btcl-input>
                                </div>
                                <div class="form-actions text-center">
                                    <button type=button class="btn green-haze btn-block btn-lg" @click="submitInventoryAddForm">Submit</button>
                                </div>
                            </form>



                        </div>
                        <div class="tab-pane " id="tab_1_1_5">
                            <form class="form-horizontal inventory-add-form" method="post" action="../../AddInventory.do" novalidate="novalidate">

                                <div class="form-body">
                                    <btcl-field title="Fiber Type">
                                        <mounted-id-value-selection :model.sync="type" url='co-location/get-inventory-template.do?id=<%=CoLocationConstants.INVENTORY_CATEGORY_FIBER_TYPE%>'></mounted-id-value-selection>
                                    </btcl-field>
                                    <btcl-input title="Fiber Core" :text.sync="amount" :number="true" placeholder="Enter Fiber Core number."></btcl-input>
                                    <btcl-input title="Fiber Brand" :text.sync="brand" placeholder="Write Brand Name"></btcl-input>
                                    <btcl-input title="Fiber Model" :text.sync="model" placeholder="Write Model Name"></btcl-input>

                                </div>

                                <div class="form-actions text-center">
                                    <button type=button class="btn green-haze btn-block btn-lg" @click="submitInventoryAddForm">Submit</button>
                                </div>
                            </form>
                        </div>
                        <div class="tab-pane " id="tab_1_1_7">
                            <form class="form-horizontal inventory-add-form" method="post" action="../../AddInventory.do" novalidate="novalidate">

                                <div class="form-body">
                                    <btcl-field title="Fiber Type">
                                        <mounted-id-value-selection :model.sync="type" url='co-location/get-inventory-template.do?id=<%=CoLocationConstants.INVENTORY_CATEGORY_FLOOR_SPACE%>'></mounted-id-value-selection>
                                    </btcl-field>
                                    <btcl-input title="Floor Space Amount(ft.)" :text.sync="amount" :number="true" placeholder="Enter Floor Space Amount."></btcl-input>
<%--                                    <btcl-input title="Floor Space Brand" :text.sync="brand" placeholder="Write Brand Name"></btcl-input>--%>
                                    <btcl-input title="Floor Space Model" :text.sync="model" placeholder="Write Floor Space Model Name."></btcl-input>

                                </div>

                                <div class="form-actions text-center">
                                    <button type=button class="btn green-haze btn-block btn-lg" @click="submitInventoryAddForm">Submit</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </btcl-body>
</div>
<script>
    var vue = new Vue({
        el: "#btcl-co-location-application",
        data: {
            categoryId: <%=CoLocationConstants.INVENTORY_POWER%>,
            type: null,
            amount: null,
            brand:'',
            model: '',
            inventoryData: {},

        },
        methods: {
            submitInventoryAddForm : function(){
                // alert('form submit clicked');
                this.inventoryData.catagoryID = this.categoryId;
                this.inventoryData.templateID = this.type.id;
                this.inventoryData.totalAmount = parseInt(this.amount);
                this.inventoryData.availableAmount = parseInt(this.amount);
                this.inventoryData.name = this.brand;
                this.inventoryData.model = this.model;

                    axios.post(context+"co-location/inventory-add.do",{
                        inventoryData: this.inventoryData
                    }).then(res=>{
                        if(res.data.responseCode===1){
                            toastr.success("Your Request has been processed", "Success");
                            setTimeout(this.reloadThePage, 1000);
                        }else {
                            toastr.error(res.data.msg, "Failure");
                        }
                    }).catch(err=>{LOG(err)})



            },
            setCategory: function(categoryId){
                this.categoryId = categoryId;

                this.type = null;
                this.amount = null;
                if( (categoryId == <%=CoLocationConstants.INVENTORY_RACK%>)
                    <%--|| (categoryId ==  <%=CoLocationConstants.INVENTORY_CATEGORY_FLOOR_SPACE%>)--%>
                ){
                    this.amount = 1;
                }
                this.model ='';
                this.brand ='';
            }
        },
    });

</script>