Vue.component('dashboard-counter-card', {
    template: `
         <div :class="dashboard" :style="{dropShadow: '.2em .3.em  .75em red !important'}" @click="showModal">
                <div :class="display">
                    <div :class="number">
                        <h3 :class="textFontClass">
                            <span data-counter="counterup" :data-value="dataValue">{{dataValue}}</span>
                            <small :class="textFontClass" v-html="additionalText"></small>
                        </h3>
                        <small>{{subText}}</small>
                    </div>
                    <div :class="icon">
                        <i :class="iconClass"></i>
                    </div>
                </div>
            </div>
    `,
    props : [
        'textFontClass',
        'dataValue',
        'subText',
        'iconClass',
        'additionalText',
        'chartType',
        'cardData'
    ],
    data () {
        return {
            display : 'display',
            number : 'number',
            icon : 'icon',
            dashboard : 'dashboard-stat2',
            btnClass:
                ' btn btn-circle blue-steel btn-sm'

        }
    },
    methods:{

        showModal() {
            if(this.cardData.length===0)return;
            $.sweetModal({
                title: this.subText,
                content: '<div style="width:100%; height: 500px; " id="chart-div"></div>'
            });
            createChart("chart-div", this.chartType, this.cardData, {'yTitle' : this.subText, 'innerRadius': 0});

        }
    },
});

Vue.component('main-frame', {
    template :
        `
            <btcl-portlet :title="frameTitle">
                <div :id="frameId"></div>
            </btcl-portlet>
        `,
    props : {
        frameTitle : String,
        frameId : String,
        frameData : Array,
        chartType: String
    }  ,
    data () {
        return {

        }
    },
    methods: {
        create() {
            createChart(this.frameId, this.chartType, this.frameData, {'innerRadius' : 0});
        }
    },
    mounted() {
        this.create();
    }
});
