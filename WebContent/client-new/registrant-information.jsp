<tab-content title="Registrant Information"
             icon="fa fa-book icon-state-danger"
             :before-change="beforeTabSwitch_3"
>
    <div class="view-height">
        <div class="row">
            <div class="col-xs-6">
                <btcl-input title="First Name"
                            :text.sync="clientContactDetails.registrantsName"
                            required="true"></btcl-input>
                <btcl-input title="Last Name"
                            :text.sync="clientContactDetails.registrantsLastName"></btcl-input>
                <br>
                <btcl-field><hr></btcl-field>
                <btcl-field title="Registrant Type" :required=true>
                    <select v-model="clientDetails.registrantType" @change="handleRegistrantTypeChange" class="form-control">
                        <option v-for="item in regTypes" :value="item.key">
                            {{item.value}}
                        </option>
                    </select>
                </btcl-field>

                <btcl-field v-if="regCategories.length>0" title="Registrant Category" :required=true>
                    <select v-model="clientDetails.regCategory" @change="handleRegistrantCategoryChange" class="form-control">
                        <option v-for="item in regCategories" :value="item.key">
                            {{item.value}}
                        </option>
                    </select>
                </btcl-field>
                <btcl-field v-if="regSubCategories.length>0" title="Registrant Sub-Category">
                    <select v-model="clientDetails.regSubCategory" class="form-control">
                        <option v-for="item in regSubCategories" :value="item.key">
                            {{item.value}}
                        </option>
                    </select>
                </btcl-field>

                <btcl-field><hr></btcl-field>

                <btcl-field  title="Country" :required=true>
                    <select v-model="clientContactDetails.country" class="form-control">
                        <option v-for="item in countries" :value="item.key">
                            {{item.value}}
                        </option>
                    </select>
                </btcl-field>

                <btcl-input title="City"
                            :text.sync="clientContactDetails.city" required=true></btcl-input>
                <btcl-input title="Address"
                            :text.sync="clientContactDetails.address" required="true"></btcl-input>
                <btcl-input title="Post Code"
                            :text.sync="clientContactDetails.postCode"></btcl-input>

            </div>
            <div class="col-xs-6">
                <btcl-field title="Email" :required=true>
                    <div class="row">
                        <div class="col-xs-12">

                            <input class="form-control" v-model="clientContactDetails.email" required="true"
                                   :readonly="redirected || clientAdded">
                        </div>
                    </div>
                </btcl-field>


                <btcl-field title="Mobile Number" :required=true>
                    <div class="row">
                        <div class="col-xs-2">
                            <label class="control-label">{{countryCode}}</label>
                        </div>
                        <div class="col-xs-10">
                            <input v-model="clientContactDetails.phoneNumber"
                                   class="form-control phoneNumber form-control company regi"
                                   type="tel" :readonly="redirected || clientAdded">
                        </div>
                    </div>
                </btcl-field>


                <btcl-input title="Web Address"
                            :text.sync="clientContactDetails.webAddress"></btcl-input>


                <btcl-input title="Fax Number" :text.sync="clientContactDetails.faxNumber"></btcl-input>
                <btcl-input title="Telephone Number"
                            :text.sync="clientContactDetails.landlineNumber"></btcl-input>
            </div>

        </div>


    </div>

</tab-content>