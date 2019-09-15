import {validate} from "../js/ip-utility.js";

let vue = new Vue({
    el:"#btcl-application",
    data:{
        versions:[],
        types:[],
        regions:[],
        modules:[],
        block:{


        },
        version:'',
        type:'',
        region:'',
        module:''

    },
    methods:{
        getIPVersions: function () {
            axios.get(context+"ip/get-ip-versions.do")
            .then(res=>{

                if(res.data.responseCode == 1){
                    this.versions = res.data.payload;
                }else {
                    toastr.error("Error Loading IP Versions", "Failure");
                }
            })
            .catch(err=>{ console.log(err) });
        },
        getIPTypes: function () {
            axios.get(context+"ip/get-ip-types.do")
                .then(res=>{

                    if(res.data.responseCode == 1){
                        this.types = res.data.payload;
                    }else {
                        toastr.error("Error Loading IP Types", "Failure");
                    }
                })
                .catch(err=>{ console.log(err) });
        },
        getRegions: function() {
            axios.get(context+"ip/region/getRegions.do").then(res=>{
                if(res.data.responseCode === 1){
                    this.regions = res.data.payload;
                }else {
                    toastr.error("Error Loading Regions", "Failure");
                }
            })
        },
        getModules: function() {
            axios.get(context+"ip/module/getModules.do").then(res=>{
                if(res.data.responseCode === 1){
                    this.modules = res.data.payload;
                }else {
                    toastr.error("Error Loading Modules", "Failure");
                }
            })
        },
        reloadThePage: function(){
            location.reload()
        },
        submit: function () {
            if(validate(this.block.fromIP, this.block.toIP, this.block.version)){
                axios.post(context+"ip/inventory/add.do",{
                    block: this.block
                }).then(res=>{
                    if(res.data.responseCode===1){
                        toastr.success("Your Request has been processed", "Success");
                        setTimeout(this.reloadThePage, 1000);
                    }else {
                        toastr.error(res.data.msg, "Failure");
                    }
                }).catch(err=>{LOG(err)})
            }

        }
    },
    mounted:function () {
        this.getIPVersions();
        this.getIPTypes();
        this.getRegions();
        this.getModules();
    },
    watch:{
        version: function(newVersion, oldVersion){
            this.block.version = newVersion;
        },
        type: function(newType, oldType){
            this.block.type= newType;
        },
        region: function(newRegion, oldRegion){
            this.block.regionId = newRegion;
        },
        module: function(newModule, oldModule){
            this.block.moduleId = newModule;
        }
    }
});