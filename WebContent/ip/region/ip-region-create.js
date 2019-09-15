var vue = new Vue({
    el: "#btcl-application",
    data: {
        contextPath: context,
        regionName: '',
        districts: [],
        selectedDistricts: [],
        isSuccess: false
    },
    methods: {
        assignDistrictToTheRegion: function () {

            if (this.regionName.length == 0) {
                alert("Insert a region Name first");
                return;
            }
            var te = this.selectedDistricts;
            var idList = this.getIdListFromDistrictObject(te);


            axios.post(context + "ip/region/createIPRegion.do", {
                regionName: this.regionName,
                districts: idList,
            })
                .then(result => {
                    if (result.data.responseCode == 1) {
                        toastr.success('Districts has been assigned to the region', 'Success');
                        this.isSuccess = true;
                        //window.location.href = context +"ip/region/getIPRegion.do";
                        this.selectedDistricts = [];

                    } else {
                        toastr.error(result.data.msg, 'Failure');
                    }
                })
                .catch(function (error) {
                    console.log(error);
                });
        },

        getDistrict: function (query) {
            isLoading = true;
            axios({method: 'GET', 'url': context + 'location/AllDistricts.do?query=' + query})
                .then(result => {
                    if (result.data.responseCode == 1) {
                        var temp = result.data.payload;
                        this.districts = this.prepareDistrictsForDropDown(temp);
                    } else {
                        var t = result.data.responseCode;
                        toastr.error('No District found', 'Failure');
                    }
                }, error => {
                    console.log("the requested data is not loading")
                });
        },
        prepareDistrictsForDropDown: function (list) {

            var district = [];

            for (var i = 0; i < list.length; i++) {
                var first = list[i].id;
                var second = list[i].nameEng;

                district.push({'id': first, 'name_en': second});
            }
            return district;
        }
        ,

        getIdListFromDistrictObject: function (list) {
            var id = [];
            for (var i = 0; i < list.length; i++) {
                id.push(list[i].id);
            }
            return id;
        }
    },

    computed: {},
});
