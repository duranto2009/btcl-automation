var vue = new Vue({
    el:"#btcl-application",
    data:{
        region:'',
        regions: [],
        subRegionName: ''
    },
    methods:{
        createSubRegion: function() {
            let url = context+"ip/region/addSubRegion.do";
            axios.post(url, {
                 subRegionName: this.subRegionName,
                 regionId : this.region
            }).then(res=>{
                if(res.data.responseCode === 1){
                    toastr.success("Sub region added");
                    setTimeout(this.reloadThePage, 1000);
                }else {
                    toastr.error("Error Loading Regions", "Failure");
                }
            }).catch(err=>{LOG(err)});
        },

        reloadThePage: function(){
            location.reload()
        },
        getRegions: function() {
            axios.get(context+"ip/region/getRegions.do").then(res=>{
                if(res.data.responseCode === 1){
                    this.regions = res.data.payload;
                }else {
                    toastr.error("Error Loading Regions", "Failure");
                }
            });
        }
    },
    mounted() {
        this.getRegions();
    },
    computed:{
    }
});
