<div id="btcl-application" v-cloak>
    <btcl-body title="IP Block Add" subtitle="IP Address Inventory">
        <btcl-portlet>
            <div class="row">

                <div class="form-group" >
                    <label class="control-label col-sm-4">Version</label>
                    <div class="col-sm-6">
                        <select v-model="version"  class="form-control">
                            <option value="" selected disabled hidden>Select Version</option>
                            <option :value="item.key" v-for="item in versions">{{item.value}}</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="form-group" >
                    <label class="control-label col-sm-4">Type</label>
                    <div class="col-sm-6">
                        <select v-model="type"  class="form-control">
                            <option value="" selected disabled hidden>Select Type</option>
                            <option :value="item.key" v-for="item in types">{{item.value}}</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="form-group" >
                    <label class="control-label col-sm-4">Region</label>
                    <div class="col-sm-6">
                        <select v-model="region"  class="form-control">
                            <option value="" selected disabled hidden>Select Region</option>
                            <option :value="item.key" v-for="item in regions">{{item.value}}</option>
                        </select>
                    </div>
                </div>

            </div>

            <div class="row">
                <div class="form-group" >
                    <label class="control-label col-sm-4">Module</label>
                    <div class="col-sm-6">
                        <select v-model="module"  class="form-control">
                            <option value="" selected disabled hidden>Select Module</option>
                            <option :value="item.key" v-for="item in modules">{{item.value}}</option>
                        </select>
                    </div>
                </div>

            </div>



            <btcl-input title="From IP" :text.sync="block.fromIP"></btcl-input>
            <btcl-input title="To IP" :text.sync="block.toIP"></btcl-input>


            <btcl-input title="Remarks" :text.sync="block.remarks"></btcl-input>


            <button type="button" class="btn green-haze btn-block"  @click="submit">Submit</button>

        </btcl-portlet>
    </btcl-body>
</div>

<script src="../inventory/add-ipblock.js" type="module"></script>
<script src="../subnet-tool/BigInteger.min.js"></script>
