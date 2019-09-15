<style>
    hr{
        border: 1px dashed #bbb;
    }
</style>
<div id ="app" v-cloak="true">
    <btcl-body title="Local Loop Charge Breakdown" :loader="loading" >

        <btcl-portlet v-if="localLoopData.length === 0">
            <div align="center">
                <h3> No Local Loop Information Found</h3>
            </div>
        </btcl-portlet>
        <btcl-portlet v-else v-for="(item,index) in localLoopData" :title='"Local Loop "+(index+1)'>

            <btcl-portlet>
                <btcl-info title="POP Name" :text="item.popName"></btcl-info>
                <btcl-info title="OFC Type" :text="item.ofcType"></btcl-info>
            </btcl-portlet>

            <div class="row">
                <div class="col-xs-12 col-sm-6">
                    <btcl-portlet title="ESTIMATED LOOP BREAKDOWN">
                        <btcl-info title="BTCL Distance (m)" :text="item.after_btclDistance"></btcl-info>
                        <btcl-info title="OC Distance (m)" :text="item.before_ocDistance"></btcl-info>
                        <hr>
                        <btcl-info title="Total Distance (m)" :text="item.before_totalDistance"></btcl-info>
                        <br><br>
                        <btcl-info :title="'First ' + item.minimumLength + ' (m) Cost (BDT)'" :text="firstCost(item)"></btcl-info>
                        <btcl-info v-if="(parseFloat(item.before_totalDistance) > parseFloat(item.minimumLength))"
                                   :title="'Next ' + (item.before_totalDistance - item.minimumLength) + ' (m) Cost (BDT)'" :text="nextCost((item.before_totalDistance - item.minimumLength))"></btcl-info>
                        <hr>
                        <btcl-info title="Total Charge (BDT)" :text="fixedTwoDigit(Math.round(item.before_totalCost))"></btcl-info>
                    </btcl-portlet>
                </div>
                <div class="col-xs-12 col-sm-6">
                    <btcl-portlet title="ACTUAL LOOP BREAKDOWN">
                        <btcl-info title="BTCL Distance (m)" :text="item.after_btclDistance"></btcl-info>
                        <btcl-info title="OC Distance (m)" :text="item.after_ocDistance"></btcl-info>
                        <hr>
                        <btcl-info title="Total Distance (m)" :text="item.after_totalDistance"></btcl-info>
                        <br><br>
                        <btcl-info :title="'First ' + item.minimumLength + ' (m) Cost (BDT)'" :text="firstCost(item)"></btcl-info>
                        <btcl-info v-if="(parseFloat(item.after_totalDistance) > parseFloat(item.minimumLength))"
                                   :title="'Next ' + (item.after_totalDistance - item.minimumLength) + ' (m) Cost (BDT)'" :text="nextCost((item.after_totalDistance - item.minimumLength))"></btcl-info>
                        <hr>
                        <btcl-info title="Total Charge (BDT)" :text="fixedTwoDigit(Math.round(item.after_totalCost))"></btcl-info>
                    </btcl-portlet>
                </div>
            </div>
        </btcl-portlet>
    </btcl-body>
</div>
<script type="module">
    import { fixedTwoDigit } from '../../vpn/demand-note/vpn-dn.js';
    let appId = <%=request.getParameter("appId")%>;
    new Vue ( {
        el: "#app",
        data : {
            loading : false,
            appId: appId,
            localLoopData : {

            },
        },
        methods: {
            firstCost(item) {

                return item.ofcType==='Single' ?
                    item.minimumCost +" x 1 = " + fixedTwoDigit(item.minimumCost) :
                        item.ofcType === 'Double' ?
                            item.minimumCost + " x 2 = " + fixedTwoDigit(item.minimumCost * 2) :
                                item.minimumCost + " x 0 = " + fixedTwoDigit(item.minimumCost * 0);
            },
            nextCost(distance) {
                let firstInstance = this.localLoopData[0];
                let ofcType = (firstInstance.ofcType === 'Single' ? 1 : 2);
                return (distance) + " x " + firstInstance.factor + " x " + ofcType
                    + " = " + fixedTwoDigit(Math.round((distance*firstInstance.factor*ofcType)))
                    ;
            },
            fixedTwoDigit ,

        },
        mounted() {
            this.loading = true;
            Promise.resolve(
            axios.get(context + "lli/dn/local-loop-breakdown-data.do?appId="  + this.appId)
                .then(res=>{
                    if(res.data.responseCode === 1) {
                        this.localLoopData = res.data.payload;
                    }else {
                        errorMessage(res.data.msg);
                    }
                }).catch(err=>console.log(err))
        ).then(()=>this.loading = false);
        }
    } );
</script>