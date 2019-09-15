export let serviceComponent = {
    name: 'service-component',
    template: `
    <div>
        <!--<div style="display: flex; justify-content: space-between;">-->
            <!--<div v-for="item in modules" style="-->
                    <!--height: 20vh;-->
                   <!---->
                    <!--background-color: #41b3a3;-->
                 <!--display: flex;-->
                    <!--justify-content: center;-->
                    <!--align-items: center;-->
                    <!--" :style="{ width: widthProperty}"-->

                <!--</h2>-->
                <!--<h2 v-else-if="redirectedModule===item.id"  style="color:white">-->
                    <!--<i class="fa fa-check-circle"></i>-->
                    <!--<b>{{item.name}}</b></b>-->
                <!--</h2>-->
                <!--<h2 v-else style="color:white">-->
                    <!--<b>{{item.name}}</b>-->
                <!--</h2>-->
                <!---->
            <!--</div>-->
        <!--</div>-->
        <!--<div  style="display: flex; height: 5vh;justify-content: space-between; ">-->
               <!--<a v-for="item in modules" -->
                <!---->
               <!--style="display: flex; align-items: center; justify-content: center; background-color: #8D8741;" -->
               <!--:style="{ width: widthProperty}"-->
                <!--class="terms" :href="item.pdf" target="_blank"-->
               <!--&gt;-->
                <!--<span style="color:white">Terms & Conditions and Privacy Policy</span>-->
            <!--</a>  -->
        <!--</div>-->
    <!--</div>-->
    <template v-if="modules.length<=4">
        <div class="row">
            <div v-for="item in modules" :class="computedColumns">
               <div style="display: flex;flex-direction: column;">
                    <div style="display: flex;justify-content: center;align-items: center;height: 20vh;background-color: #41b3a3; cursor: pointer;"@click="selectModule(item)">
                         <h2 style="color:white" v-if="selectedModule === item.id">
                            <i class="fa fa-check-circle"></i>
                            <b>{{item.name}}</b>
                        </h2>
                        <h2 v-else style="color:white">
                            <b>{{item.name}}</b>
                        </h2>
                    </div>
                    <div style="display: flex;justify-content: center;align-items: center;height:5vh;margin-bottom: 2vh; background-color: #8D8741; cursor: pointer;color:wheat">
                         <a :href="item.pdf" target="_blank">
                            <h5 style="color:white">Terms & Conditions and Privacy Policy</h5>
                        </a>    
                    </div>
                </div>
            </div>
        </div>
    </template>
    <template v-else>
        <div v-for="i in computedRows" class="row">
            <div v-for="item in getRowModules(i)" :class="computedColumns">
                <div style="display: flex;flex-direction: column;">
                    <div style="display: flex;justify-content: center;align-items: center;height: 20vh;background-color: #41b3a3; cursor: pointer;"@click="selectModule(item)">
                         <h2 style="color:white" v-if="selectedModule === item.id">
                            <i class="fa fa-check-circle"></i>
                            <b>{{item.name}}</b>
                        </h2>
                        <h2 v-else style="color:white">
                            <b>{{item.name}}</b>
                        </h2>
                     </div>
                    <div style="display: flex;justify-content: center;align-items: center;height:5vh;margin-bottom: 2vh; background-color: #8D8741; cursor: pointer;color:wheat">
                         <a :href="item.pdf" target="_blank">
                            <h5 style="color:white">Terms & Conditions and Privacy Policy</h5>
                        </a>    
                    </div>
                </div>
             </div>
        </div>
        
    </template>
    </div>
    `,
    props: {
        modules: Array,
        redirectedModule: Number,
    },
    methods: {
        selectModule(item) {
            this.$emit('module_change', item.id);
            this.selectedModule = item.id;
        },
        getRowModules(row) {
            // debugger;
            let startIndex = (row-1)*4;
            return this.modules.slice(startIndex, startIndex+4);
        }
    },
    data() {
        return {
            widthProperty: 'calc(100% / ' + this.modules.length + ' - 100px)',
            // clickedProperty : ,
            selectedModule: 0
        }
    },
    computed: {
        computedColumns() {
            let count = this.modules.length <= 4 ? this.modules.length : 4;
            let size = Math.ceil(12/count);
            let laptopColumn = "col-sm-" + size;
            let monitorColumn = "col-md-" + size + " col-lg-" + size;
            let mobileColumn = "col-xs-" + 12;
            return mobileColumn + " " + laptopColumn + " " + monitorColumn;
        },
        computedRows() {
            let count = this.modules.length;
            let rowCount = Math.ceil(count/ 4);
            return rowCount;
        },

    }
};