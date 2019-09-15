<div id="btcl-application">
    <btcl-body title="IP Management" subtitle="Sub Region">
        <btcl-portlet>
            <btcl-field title="Region">
                <select v-model="region" class="form-control">
                    <option value="" selected disabled hidden>Select Region</option>
                    <option :value="item.key" v-for="item in regions">{{item.value}}</option>
                </select>
            </btcl-field>

            <btcl-input title="IP Sub Region Name" :text.sync="subRegionName" :required="true"></btcl-input>

            </btcl-field>
            <button type="button"
                    class="btn btn-block green-haze"
                    @click="createSubRegion">Create Sub Region
            </button>
        </btcl-portlet>
    </btcl-body>
</div>
<script src="../sub-region/ip-sub-region-create.js"></script>

