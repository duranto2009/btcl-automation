export let moduleSelection = {
    name: "module-selection",
    template:
        `
    
    <select v-model="moduleId" @change="doSomething" class="form-control">
        <option value="" selected disabled hidden>Select Module</option>
        <option :value="item.key" v-for="item in modules">{{item.value}}</option>
    </select>
    </btcl-field>
    `,


    props: {
    },

    data() {
        return {
            moduleId: 0,
            modules: {
                key:0,
                value:'',
            },
        }
    },

    mounted() {
        this.getModules();
    },

    methods: {
        doSomething() {
            this.$emit("module_change", this.moduleId);
        },
        getModules() {
            axios.get(context + "ClientType/getAllModules.do")
                .then(res => {
                    if (res.data.responseCode === 1) {
                        this.modules = res.data.payload;
                    } else {
                        toastr.error("Error Loading modules", "Failure");
                    }
                });
        }
    },

    computed: {}
};
