var vue = new Vue({
    el:"#searchFields",
    data:{
        contextPath:context,
        regions:[],
        districts:[],
        searchBy:"Region",
        isSuccess:false,
        regionList:[],
        region:{},
        selectedDistrict:{},
        selectedRegion:{},
        searchoptions:['Region','District'],
        availabledistricts:[],
        selectedDistrictList:[],
        isLoading:false,
    },
    methods:{
        searchRegionByDistrict:function () {
            var district_id = this.selectedDistrict.id;
            if(district_id == null){
                alert("Select a District first");
            }
            axios({ method: 'GET', 'url': context+'ip/region/getRegionByDistrict.do?dId='+district_id})
                .then(result => {
                    if(result.data.responseCode == 1) {
                        this.region = result.data.payload;
                        this.regionList = [];
                        this.regionList.push(this.region);

                    }
                    else{
                        var t = result.data.responseCode;
                        console.log('No Region found Failure');
                    }
                }, error => {
                    console.log("the requested data is not loading")
                });
        },
        searchRegionByRegionName:function () {
            var region_id = this.selectedRegion.id;
            if(region_id == null)region_id =-1;
            axios({ method: 'GET', 'url': context+'ip/region/getRegionById.do?rid='+region_id})
                .then(result => {
                    if(result.data.responseCode == 1) {
                        this.regionList = result.data.payload;

                    }
                    else{
                        var t = result.data.responseCode;
                        console.log('No Region found Failure');
                    }
                }, error => {
                    console.log("the requested data is not loading")
                });
        }
        ,
        getRegions: function(query){
            isLoading = true;
            axios({ method: 'GET', 'url': context+'ip/region/getRegions.do?query='+query})
                .then(result => {
                    if(result.data.responseCode == 1) {
                        var temp = result.data.payload;
                        this.regions = this.prepareRegionsForDropDown(temp);
                    }
                    else{
                        var t = result.data.responseCode;
                        console.log('No Region found Failure');
                    }
                }, error => {
                    console.log("the requested data is not loading")
                });
        },
        prepareRegionsForDropDown:function (list) {

            var region = [];

            for(var i=0;i<list.length;i++){
                var first = list[i].key;
                var second = list[i].value;

                region.push({'id':first,'name_en':second});
            }
            return region;
        }
        ,
        getDistricts: function(query){
            isLoading = true;
            axios({ method: 'GET', 'url': context+'location/AllDistricts.do?query='+query})
                .then(result => {
                    if(result.data.responseCode == 1) {
                        var temp = result.data.payload;
                        this.districts = this.prepareDistrictsForDropDown(temp);
                    }
                    else{
                        var t = result.data.responseCode;
                        toastr.error('No District found', 'Failure');
                    }
                }, error => {
                    console.log("the requested data is not loading")
                });
        },
        prepareDistrictsForDropDown:function (list) {

            var district = [];

            for(var i=0;i<list.length;i++){
                var first = list[i].id;
                var second = list[i].nameEng;

                district.push({'id':first,'name_en':second});
            }
            return district;
        }
        ,
        goDetailsModal:function (region) {
            $("#regionDetail").show();
            this.region = region;

        }
        ,
        closeRegionModal:function () {
            $("#regionDetail").hide();
        },
        showAddDistrictModal:function () {
            $("#regionDetail").hide();
            $("#addDistrictModal").show();
        }
        ,
        closeDistrictAddModal:function () {
            $("#addDistrictModal").hide();
            this.selectedDistrictList = [];

        },
        getAvailableDistrict: function(query){
            isLoading = true;
            axios({ method: 'GET', 'url': context+'ip/region/getDistrictsNotAssignedToRegion.do?query='+query})
                .then(result => {
                    if(result.data.responseCode == 1) {
                        var temp = result.data.payload;
                        this.availabledistricts = this.prepareDistrictsForDropDown(temp);
                    }
                    else{
                        var t = result.data.responseCode;
                        toastr.error('No District found', 'Failure');
                    }
                }, error => {
                    console.log("the requested data is not loading")
                });
        },
        assignDistrictToTheRegion:function(){
            var te = this.selectedDistrictList;
            var idList = this.getIdListFromDistrictObject(te);
            axios.post(context +"ip/region/insertDistrictToIPRegion.do", {
                id: this.region.id,
                districts : idList,
            })
                .then(result => {
                    if(result.data.responseCode == 1) {
                        toastr.success('Districts has been assigned to the region', 'Success');
                        this.isSuccess = true;
                        this.region.districts= $.merge( $.merge( [], this.region.districts), result.data.payload.districts );

                        this.selectedDistrictList = [];
                        //this.closeDistrictAddModal();
                       // window.location.href = context+"ip/region/searchIPRegion.do";
                    }else {
                        toastr.error(result.data.msg, 'Failure');
                    }
                })
                .catch(function (error) {
                    console.log(error);
                });
        },
        getIdListFromDistrictObject:function (list) {
            var id=[];
            for(var i=0;i<list.length;i++){
                id.push(list[i].id);
            }
            return id;
        },
        removeDistrictFromRegion:function (districtId,regionId) {
            axios.post(context +"ip/region/deleteDistrictFromRegion.do", {
                districtId : districtId,
            })
                .then(result => {
                    if(result.data.responseCode == 1) {
                        toastr.success('Districts has been deleted from the region', 'Success');
                        this.removeDistrictfromDistrictList (districtId);
                        this.goDetailsModal(this.region);
                        this.isSuccess = true;
                    }else {
                        toastr.error(result.data.msg, 'Failure');
                    }
                })
                .catch(function (error) {
                    console.log(error);
                });
        },
        removeDistrictfromDistrictList:function (districtId) {
            for(var i=0;i<this.region.districts.length;i++){
                if(this.region.districts[i].id === districtId){
                    this.region.districts.splice(i,1);
                }
            }
        }
    },

});
