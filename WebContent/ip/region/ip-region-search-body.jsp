

<style type="text/css">

    #regionDetail,#addDistrictModal{
        position: absolute;
        top: 5%;
        margin-left: 30%;
        left: 0;
        width: 60%;
        height: 50%;
        z-index: 10;
        background-color: slategrey;
    }

    #searchFields {
        position: relative;
    }

    .modal-footer,.modal-body{
        border: none;
    }
</style>




<div id="searchFields">
    <btcl-body title="IP Region">
        <btcl-portlet>
            <div class="row">
                <div class="form-group col-md-12 col-sm-12 col-xs-12" id="searchFieldSelector">
                    <label class="control-label col-md-3 col-sm-3 col-xs-3"> Search By</label>
                    <div class="col-md-6 col-sm-6 col-xs-6">
                        <multiselect v-model="searchBy"
                                     :options="searchoptions"
                                     :searchable="false"
                                     :close-on-select="true"
                                     :show-labels="false"
                                     placeholder="Pick a value"
                        >

                        </multiselect>

                    </div>
                </div>

                <div v-show="searchBy === 'Region'" id="regionSearchField" class="col-md-12 col-sm-12 col-xs-12">
                    <%--<h1 style="text-decoration: underline;">Search By Region</h1>--%>
                    <label class="control-label col-md-3 col-sm-3 col-xs-3">Region Name</label>
                    <div class="col-md-6 col-sm-6 col-xs-6 form-group">
                        <multiselect v-model="selectedRegion"
                                     id="regionId"
                                     track-by="id"
                                     label="name_en"
                                     :show-labels="false"
                                     placeholder="Type to search"
                                     open-direction="bottom"
                                     :options="regions"
                                     :close-on-select="true"
                                     :options-limit="300"
                                     :limit="3"
                                     :max-height="600"
                                     v-on:search-change="getRegions">

                        </multiselect>
                        <%--<template slot="tag"
                                  slot-scope="{ option, remove }">
                          <span class="custom__tag">
                            <span>{{ option.name_en }}</span>
                         </span>
                        </template>--%>
                    </div>
                    <div class="form-group col-md-12 col-sm-12 col-xs-12">
                        <button type=button class="btn green-haze btn-block btn-lg" @click="searchRegionByRegionName">Submit
                        </button>
                    </div>

                </div>

                <div v-show="searchBy === 'District'" id="districtSearchField" class="col-md-12 col-sm-12 col-xs-12">
                    <label class="control-label col-md-3 col-sm-3 col-xs-3">District Name <span class="required">*</span></label>
                    <div class="form-group col-md-6 col-sm-6 col-xs-6">
                        <multiselect v-model="selectedDistrict"
                                     id="districtId"
                                     track-by="id"
                                     label="name_en"
                                     :show-labels="false"
                                     placeholder="Type to search"
                                     open-direction="bottom"
                                     :options="districts"
                                     :close-on-select="true"
                                     :options-limit="300"
                                     :limit="3"
                                     :max-height="600"
                                     v-on:search-change="getDistricts">

                        </multiselect>
                 <%--       <template slot="tag"
                                  slot-scope="{ option, remove }">
                          <span class="custom__tag">
                            <span>{{ option.name_en }}</span>
                         </span>
                        </template>--%>
                    </div>


                    <div class ="form-group col-md-12">
                        <button type=button class="btn green-haze btn-block btn-lg" @click="searchRegionByDistrict">Submit</button>
                    </div>
                </div>

                <div v-if="regionList.length>0" id="ipRegionSearchResultBody" class="portlet box">
                    <div class="portlet-body">
                        <div class="table-responsive">
                            <table class="table table-bordered table-striped">
                                <thead>
                                <tr>
                                    <th>Region Name</th>
                                    <th>Number of District</th>
                                    <th>Details</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr v-for="region in regionList">
                                    <td>{{region.name_en}}</td>
                                    <td>{{region.districts.length}}</td>
                                    <td><a target="_blank" @click ="goDetailsModal(region)">Details</a></td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>


                <div v-if="regionList.length>0" id="regionDetail"
                     style="display: none;width: 50%; min-width: 500px;" >

                    <div class="modal-content">

                        <div class="modal-header">

                            <button type="button"
                                    class="close"
                                    data-dismiss="modal"
                                    @click="closeRegionModal">
                                <span aria-hidden="true">×</span>
                            </button>

                            <h4 class="modal-title"
                                <%--style="background: #44b6ae;"--%>
                                id="myModalLabel">District List for Region:  {{region.name_en}}
                            </h4>
                        </div>
                        <form class="form-horizontal" style="padding: 15px">

                            <div class="modal-body" id ="detailModalBody" >

                              <%--  <div class="form-group">
                                    <label class="control-label">Region Name:</label>
                                    <span>{{region.name_en}}
                                    </span>

                                </div>--%>
                                <div>
<%--
                                    <label class="control-label">District Lists:</label>
--%>
                                    <span class="form-group col-md-12 col-sm-12 col-xs-12"
                                          v-for ="district in region.districts">
                                        <span class="col-md-5 col-sm-5 col-xs-5">{{district.nameEng}}</span>
                                        <button class="col-md-3 col-sm-3 col-xs-3 btn btn-delete-btcl"
                                                type="button"
                                                @click="removeDistrictFromRegion(district.id,region.id)">remove
                                        </button>
                                    </span>
                                </div>
                                <div class="col-md-12 col-sm-12 col-xs-12 form-group"
                                     style="text-align: center;">
                                    <a target="_blank" @click ="showAddDistrictModal" >Add New District to this Region</a>
                                </div>
                            </div>

                            <div class="modal-footer">

                                <button type="button"
                                        class="btn btn-cancel-btcl"
                                        data-dismiss="modal"
                                        @click="closeRegionModal">Close
                                </button>
                            </div>
                        </form>
                    </div>

                </div>
                <div v-if="regionList.length>0" id="addDistrictModal"
                     style="display: none;width: 50%; min-width: 500px;" >

                    <div class="modal-content">

                        <div class="modal-header">

                            <button type="button"
                                    class="close"
                                    data-dismiss="modal"
                                    @click="closeDistrictAddModal">
                                <span aria-hidden="true">×</span>
                            </button>

                            <h4 class="modal-title"
                                <%--style="background: #44b6ae;"--%>>District Add
                            </h4>
                        </div>
                        <form class="form-horizontal" style="padding: 15px">

                            <div class="modal-body">
                                <div>
                                    <multiselect v-model="selectedDistrictList"
                                                 id="ajax"
                                                 track-by="id"
                                                 label="name_en"
                                                 placeholder="Type to search"
                                                 open-direction="bottom"
                                                 :options="availabledistricts"
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
                                                 v-on:search-change="getAvailableDistrict">
                                        <%--<template slot="tag"
                                                  slot-scope="{ option, remove }">
                                              <span  class="custom__tag">
                                                <span>{{ option.name_en}}</span>
                                                <span class="custom__remove"
                                                      v-on:click="remove(option)">❌</span>
                                             </span>
                                        </template>
                                        <span slot="noResult">Oops! No elements found. Consider changing the search query.
					            </span>--%>
                                    </multiselect>

                                    <div class ="form-group col-md-12 col-sm-12 col-xs-12"
                                         style="text-align: center;">
                                        <button type="button"
                                            class="btn btn-success"
                                            @click ="assignDistrictToTheRegion">Update Region
                                        </button>
                                    </div>
                                </div>
                            </div>

                            <div class="modal-footer">

                                <button type="button"
                                        class="btn btn-cancel-btcl"
                                        data-dismiss="modal"
                                        @click="closeDistrictAddModal">Close
                                </button>
                            </div>
                        </form>
                    </div>

                </div>
            </div>

        </btcl-portlet>

    </btcl-body>
</div>




    <script src="ip-region-search-field.js"></script>



