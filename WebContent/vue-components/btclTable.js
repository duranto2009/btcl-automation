export default {
    name : "BTCLTable",
    template:
    `
    <div> 
        <btcl-portlet :title="caption">
            <div class="table-responsive">
                <table class="table table-bordered table-hover table-striped">
                <thead>
                    <tr>
                        <th v-for="column in columns"> {{column}}</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="row in rows">
                        <td v-for="item in row">{{item}}</td>
                    </tr>
                </tbody>
                <tfoot v-if="rows.length>0">
                    <tr>
                        <td v-for="item in footer">{{item}}</td>
                    </tr>
                </tfoot>
            </table>
            </div>
            
        </btcl-portlet>
    </div>
    `,
    props: {
        columns: Array,
        rows : Array,
        footer : Array,
        caption: String,
    },
    data() {
        return {}
    } ,

    mounted() {},
    methods: {},
    computed : {}
}