<%@ page import="coLocation.CoLocationConstants" %><%--
  Created by IntelliJ IDEA.
  User: forhad
  Date: 12/6/18
  Time: 11:49 AM
--%>
<div id=btcl-co-location-application>
    <btcl-body title="Co-Location" subtitle='Cost Configuration'>

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
                            <a href="#tab_1_1_6" data-toggle="tab" id="y_1_1_6" @click="setCategory(<%=CoLocationConstants.INVENTORY_CATEGORY_FLOOR_SPACE%>)">FLoor Space</a>
                        </li>
                    </ul>
                    <div class="tab-content" style="padding: 0px;">

                        <div class="tab-pane active" id="tab_1_1_4">
                            <form class="form-horizontal inventory-add-form" method="post" action="../../AddInventory.do" novalidate="novalidate">
                                <div class="form-body">

                                    <btcl-field title="Power Type">
                                        <mounted-id-value-selection :model.sync="type" url='co-location/get-inventory-template.do?id=<%=CoLocationConstants.INVENTORY_CATEGORY_POWER_TYPE%>'></mounted-id-value-selection>
                                    </btcl-field>

                                    <%--<btcl-input title="Power Supply" :text.sync="amount" placeholder="Enter Power Amount" :number="true"></btcl-input>--%>
                                    <btcl-input title="Per Unit Cost" :text.sync="price" :number="true" placeholder="Enter Cost."></btcl-input>

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
                                    <btcl-field title="Rack Space">
                                    <mounted-id-value-selection :model.sync="amount" url='co-location/get-inventory-template.do?id=<%=CoLocationConstants.INVENTORY_CATEGORY_RACK_SPACE%>'></mounted-id-value-selection>
                                    </btcl-field>

                                    <btcl-input title="Cost" :text.sync="price" :number="true" placeholder="Enter Cost."></btcl-input>
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
                                    <%--<btcl-input title="Fiber Core" :text.sync="amount" :number="true" placeholder="Enter Fiber Core number."></btcl-input>--%>
                                    <btcl-input title="Cost per Core" :text.sync="price" :number="true" placeholder="Enter Cost."></btcl-input>
                                    <%--<btcl-input title="Fiber Model" :text.sync="model" placeholder="Write Model Name"></btcl-input>--%>

                                </div>

                                <div class="form-actions text-center">
                                    <button type=button class="btn green-haze btn-block btn-lg" @click="submitInventoryAddForm">Submit</button>
                                </div>
                            </form>
                        </div>
                        <div class="tab-pane " id="tab_1_1_6">
                            <form class="form-horizontal inventory-add-form" method="post" action="../../AddInventory.do" novalidate="novalidate">

                                <div class="form-body">
                                    <btcl-field title="Fiber Type">
                                        <mounted-id-value-selection :model.sync="type" url='co-location/get-inventory-template.do?id=<%=CoLocationConstants.INVENTORY_CATEGORY_FLOOR_SPACE%>'></mounted-id-value-selection>
                                    </btcl-field>
                                    <%--<btcl-input title="Fiber Core" :text.sync="amount" :number="true" placeholder="Enter Fiber Core number."></btcl-input>--%>
                                    <btcl-input title="Cost per Core" :text.sync="price" :number="true" placeholder="Enter Cost."></btcl-input>
                                    <%--<btcl-input title="Fiber Model" :text.sync="model" placeholder="Write Model Name"></btcl-input>--%>

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
            price:null,
            cost: {},
            costData: null,
        },
        methods: {
            submitInventoryAddForm : function(){
                if(this.type==null){
                    errorMessage("Select Type before form submit.");
                    return;
                }
                if(this.price == null){
                    errorMessage("Enter Price before form submit.");
                    return;
                }
                this.cost.typeID = this.type.id;
                this.cost.price = parseInt(this.price);
                this.cost.quantityID = 1;
                if(this.categoryId == <%=CoLocationConstants.INVENTORY_RACK%>) {
                    this.cost.quantityID =parseInt(this.amount.id);
                }
                    axios.post(context+"co-location/inventory-cost-config.do",{
                        cost: this.cost
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
                this.cost = {};
                this.model ='';
                this.brand ='';
            },
            getCostData: function(){
                //if type not null then select cost data from server
                if(this.type!=null){
                var url = context + 'co-location/get-cost-config-info.do?typeid=' + this.type.id;
                if(this.amount!=null)url+="&quantityid="+ parseFloat(this.amount.id);
                else url+="&quantityid="+ 1;
                axios.get(url)
                    .then(result => {
                        if(result.data.responseCode===1){
                            //if success then set costData & ID in cost and set the price
                            this.costData = result.data.payload;
                            this.cost.ID = this.costData.ID;
                            this.price = this.costData.price;
                            this.$forceUpdate();
                        }else{
                            //if multiple config found the clear previously set data in cost
                            this.costData = null;
                            this.price = null;
                            if(this.cost!=null)if(this.cost.hasOwnProperty("ID"))delete this.cost.ID;
                        }
                    }).catch(error => {
                    console.log(error)
                });
                }else{
                    // if type null then clear already set data.
                    this.costData = null;
                    this.price = null;
                    if(this.cost!=null)if(this.cost.hasOwnProperty("ID"))delete this.cost.ID;
                }
            }
        },
        watch: {
            //when type change then take the cost data from server
            type: function(){
               this.getCostData();
            },
            //for rack space cost data need to be loaded from server
            amount: function(){
               this.getCostData();
            }
        }

    });

</script>