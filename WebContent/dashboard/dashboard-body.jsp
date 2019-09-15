<link rel="stylesheet" href="${context}dashboard/sweet-modal.css"/>
<style>
    #chart-div-left, #chart-div-right{
        height: 600px;
    }

    .dashboard-stat2:hover,
    .dashboard-stat2:focus {
        transition: background-color 500ms, cursor 20ms, box-shadow 500ms;
        /*box-shadow: 0 0.5em 0.5em -0.4em #a2a2a2;*/
        /*transform: translateX(-0.5em);*/
        background-color: #222930;
        cursor:pointer;
        box-shadow: inset 400px 0 0 0 #1B1B1B;

    }

    .dashboard-stat2:hover h3,
    .dashboard-stat2:focus h3,
    .dashboard-stat2:hover h3>small,
    .dashboard-stat2:focus h3>small{
        color:#4EB1BA !important
    }
    .dashboard-stat2:hover i,
    .dashboard-stat2:hover i,
    .dashboard-stat2:hover small,
    .dashboard-stat2:focus small {
        color:#E9E9E9 !important
    }



</style>
<div id="app" v-cloak="true">
    <div class="row">
        <div class="col-xs-12 col-sm-6 col-md-3" v-for="(item,index) in dashboardData.key">
            <dashboard-counter-card
                    :text-font-class="item.textClass"
                    :additional-text="item.additionalText"
                    :data-value="item.cardValue"
                    :sub-text="item.subText"
                    :icon-class="item.iconClass"
                    :chart-type="item.chartType"
                    :card-data="JSON.parse(item.cardData)"
            >
            </dashboard-counter-card>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-6" v-for="mainFrame in dashboardData.value">
            <main-frame
                    :frame-title="mainFrame.title"
                    :frame-id="mainFrame.div"
                    :frame-data="JSON.parse(mainFrame.frameData)"
                    :chart-type="mainFrame.chartType"
            >

            </main-frame>
        </div>
    </div>
</div>
<script src="${context}dashboard/dashboard-components.js"></script>
<script src="${context}assets/global/plugins/counterup/jquery.waypoints.min.js"></script>
<script src="${context}assets/global/plugins/counterup/jquery.counterup.min.js"></script>
<script src="${context}dashboard/sweet-modal.js"></script>
<!-- Resources -->
<script src="${context}dashboard/amChartJS/core.js"></script>
<script src="${context}dashboard/amChartJS/charts.js"></script>
<%--<script src="${context}dashboard/amChartJS/sunburst.js"></script>--%>
<script src="${context}dashboard/amChartJS/forceDirected.js"></script>
<script src="${context}dashboard/amChartJS/material.js"></script>
<script src="${context}dashboard/amChartJS/animated.js"></script>
<script src="${context}dashboard/js/amChartFunctions.js"></script>
<script>
    new Vue({
        el: "#app",
        data: {
            dashboardData : {

            },
            chartTypes,
        },
        methods: {

        },
        mounted() {


            axios.get(context + 'dashboard/get-data.do')
                .then(r=> {
                    if(r.data.responseCode === 1) {
                        this.dashboardData = r.data.payload;
                    }else {
                        errorMessage(r.data.msg);
                    }
                });
        }
    });
</script>