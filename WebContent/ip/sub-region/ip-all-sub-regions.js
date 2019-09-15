var vue = new Vue({
    el:"#btcl-application",
    data:{
        allSubRegions:[]
    },
    methods:{
        reloadThePage: function(){
            location.reload()
        },
        getSubRegions: function() {
            axios.get(context+"ip/region/getAllSubRegionsWithParentRegion.do").then(res=>{
                if(res.data.responseCode === 1){
                    this.allSubRegions = res.data.payload;
                }else {
                    toastr.error("Error Loading Sub Regions", "Failure");
                }
            });
        }
    },
    mounted() {
        this.getSubRegions();
    },
    computed:{
    }
});
