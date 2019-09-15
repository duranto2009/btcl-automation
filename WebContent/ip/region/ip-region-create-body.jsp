<div id="btcl-application">
    <btcl-body title="Region Assign" subtitle="IP Region">
        <btcl-portlet>
            <btcl-input title="IP Region Name" :text.sync="regionName" :required="true"></btcl-input>
            <btcl-field title="District">
                <div>
                    <multiselect v-model="selectedDistricts"
                                 id="ajax"
                                 track-by="id"
                                 label="name_en"
                                 placeholder="Type to search"
                                 open-direction="bottom"
                                 :options="districts"
                                 :multiple="true"
                                 :searchable="true"
                                 :loading="isLoading"
                                 :internal-search="false"
                                 :clear-on-select="false"
                                 :close-on-select="false"
                                 :options-limit="300"
                                 :limit="3"
                                 :max-height="600"
                                 :show-no-results="false"
                                 :hide-selected="true"
                                 v-on:search-change="getDistrict">
                    </multiselect>
                </div>

            </btcl-field>
            <button type="button"
                    class="btn btn-block green-haze"
                    v-on:click="assignDistrictToTheRegion()">Create Region
            </button>
        </btcl-portlet>
    </btcl-body>
</div>
<script src="ip-region-create.js"></script>
