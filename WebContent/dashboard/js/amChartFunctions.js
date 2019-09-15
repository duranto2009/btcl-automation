const colorSet = new am4core.ColorSet();
const colorList = [
    am4core.color("#6794dc"),
    am4core.color("#67b7dc"),
    am4core.color("#6771dc"),
    am4core.color("#FFC771"),
    am4core.color("#FF9671"),
    am4core.color("#F9F871"),

];
const timelineDefault = [{
    "category": "1",
    "value": 1,
    "text": "[bold]2018 Q1[/]\nThere seems to be some furry animal living in the neighborhood.",
    "center": "bottom"
}, {
    "category": "2",
    "value": 1,
    "text": "[bold]2018 Q2[/]\nWe're now mostly certain it's a fox.",
    "center": "top"
}, {
    "category": "3",
    "value": 1,
    "text": "[bold]2018 Q3[/]\nOur dog does not seem to mind the newcomer at all.",
    "center": "bottom"
}, {
    "category": "4",
    "value": 1,
    "text": "[bold]2018 Q4[/]\nThe quick brown fox jumps over the lazy dog.",
    "center": "top"
}];

const entityBrokenPieData = [
    {
        category: "LLI",
        value: 70,
        color: colorSet.next(),

        children: [{
            category: "Temporary",
            value: 15
        }, {
            category: "Regular",
            value: 35
        }, {
            category: "LT Contract",
            value: 20
        }]
    },
    {
        category: "VPN",
        value: 20,
        color: colorSet.next(),
        children: [{
            category: "Regular",
            value: 15
        }, {
            category: "Special",
            value: 10
        }, {
            category: "Other",
            value: 5
        }]
    },
    {
        category: "NIX",
        value: 10,
        color: colorSet.next(),
        children: [{
            category: "Regular",
            value: 15
        }, {
            category: "Special",
            value: 10
        }, {
            category: "Other",
            value: 5
        }]
    }
];

const applicationLayeredColumnData =  [
    {
        "category": "LLI",
        "value_2017": 90,
        "value_2018": 40,
        "value_2019": 10
    },
    {
        "category": "VPN",
        "value_2017": 80,
        "value_2018": 110,
        "value_2019": 20,

    },
    {
        "category": "NIX",
        "value_2017": 8,
        "value_2018": 29,
        "value_2019": 11
    }
];

const applicationStackedBarHorizontalData = [
    {
        "year": "2016",
        "LLI": 25,
        "VPN": 85,
        "NIX": 21,

    },
    {
        "year": "2017",
        "LLI": 26,
        "VPN": 27,
        "NIX": 22,

    },
    {
        "year": "2018",
        "LLI": 32,
        "VPN": 29,
        "NIX": 24,

    }
];

const userRadialHistogramData = [
    {category : 'admin',value : '4'}
    ,{category: 'Administrative Authority', value: '1'}
    ,{category : 'AM', value:	'12'}
    ,{category: 'AM Broadbrand-2', value: '2' }
    ,{category: 'AM Domain', value: '1' }
    ,{category: 'AM LLI', value: '1' }
    ,{category: 'AM VPN', value: '1' }
    ,{category: 'CDGM', value: '1' }
    ,{category: 'CGM (Overseas)', value: '1' }
    ,{category: 'Configure', value: '1' }
    ,{category: 'CRM_BASIC', value: '5' }
    ,{category: 'DGM (Core & Upstream)', value: '1' }
    ,{category: 'DGM Broadbrand-2', value: '1' }
    ,{category: 'DGM Domain', value: '1' }
    ,{category: 'DGM_CORE', value: '1' }
    ,{category: 'Director International', value: '1' }
    ,{category: 'Divisional Engineer', value: '4' }
    ,{category: 'finance', value: '1' }
    ,{category: 'GM (Data and Internet)', value: '1' }
    ,{category: 'GM (International)', value: '1' }
    ,{category: 'LDGM', value: '4' }
    ,{category: 'Nothing', value: '5' }
    ,{category: 'Outsourcing Company', value: '6' }
    ,{category: 'readonly', value: '1' }
    ,{category: 'Revenue', value: '3' }
    ,{category: 'ServerRoom', value: '2' }
    ,{category: 'Space&OFC', value: '1' }

];

const demandNoteBrokenPieData = [
    {
        category: "LLI",
        value: 55,
        color: colorSet.next(),//1],
        children: [
            {
                category: "Paid And Verified",
                value: 75,

            },
            {
                category: "Unpaid",
                value: 23
            },
            {
                category: "Paid Not Verified",
                value: 2
            }
        ]
    },{
        category: "NIX",
        value: 15,
        color: colorSet.next(),
        children: [
            {
                category: "Paid And Verified",
                value: 75,

            },
            {
                category: "Unpaid",
                value: 23
            },
            {
                category: "Paid Not Verified",
                value: 2
            }
        ]
    },{
        category: "CoLocation",
        value: 20,
        color: colorSet.next(),//3],
        children: [
            {
                category: "Paid And Verified",
                value: 75,

            },
            {
                category: "Unpaid",
                value: 23
            },
            {
                category: "Paid Not Verified",
                value: 2
            }
        ]
    },{
        category: "Upstream",
        value: 5,
        color: colorSet.next(),//4],
        children: [
            {
                category: "Paid And Verified",
                value: 75,

            },
            {
                category: "Unpaid",
                value: 23
            },
            {
                category: "Paid Not Verified",
                value: 2
            }
        ]
    },{
        category: "Transmission",
        value: 10,
        color: colorSet.next(),//0],
        children: [
            {
                category: "Paid And Verified",
                value: 75,

            },
            {
                category: "Unpaid",
                value: 23
            },
            {
                category: "Paid Not Verified",
                value: 2
            }
        ]
    },
    {
        category: "VPN",
        value: 20,
        color: colorSet.next(),//5],
        children: [
            {
                category: "Paid And Verified",
                value: 70
            },
            {
                category: "Unpaid",
                value: 19
            },
            {
                category: "Paid Not Verified",
                value: 7
            },
            {
                category: "Skipped Not Paid",
                value: 3
            },
            {
                category: "Skipped Paid Not Verified",
                value: 1
            }
        ]
    },
];

const clientStackedBarVerticalData = [
    {
        category: "LLI",
        value_GOVT: 1,
        value_Private: 5,
        value_Military: 3
    },
    {
        category: "NIX",
        value_GOVT: 2,
        value_Private: 5,
        value_Military: 3
    },
];

const outsourcingBillVariableRadiusPieData = [
    {
        category: "Sigma",
        value: 260
    },
    {
        category: "SIAM",
        value: 230
    },
    {
        category: "fiber@Home",
        value: 200
    },
    {
        category: "IOL",
        value: 165
    },

];

const countrySimplePieChartData = [
        {
    "category": "Lithuania",
    "values": 501.9
},
        {
    "category": "Germany",
    "values": 165.8
},
        {
    "category": "Australia",
    "values": 139.9
},
        {
    "category": "Austria",
    "values": 128.3
},
        {
    "category": "UK",
    "values": 99
},
        {
    "category": "Belgium",
    "values": 60
}];

const userSimpleColumnData = [
    {"category" : 'admin',"value":4}
    ,{"category": 'Administrative Authority', "value":1}
    ,{"category" : 'AM', "value":12}
    ,{"category": 'AM Broadbrand-2', "value":2 }
    ,{"category": 'AM Domain', "value":1 }
    ,{"category": 'AM LLI', "value":1 }
    ,{"category": 'AM VPN', "value":1 }
    ,{"category": 'CDGM', "value":1 }
    ,{"category": 'CGM (Overseas)', "value":1 }
    ,{"category": 'Configure', "value":1 }
    ,{"category": 'CRM_BASIC', "value":5 }
    ,{"category": 'DGM (Core & Upstream)', "value":1 }
    ,{"category": 'DGM Broadbrand-2', "value":1 }
    ,{"category": 'DGM Domain', "value":1 }
    ,{"category": 'DGM_CORE', "value":1 }
    ,{"category": 'Director International', "value":1 }
    ,{"category": 'Divisional Engineer', "value":4 }
    ,{"category": 'finance', "value":1 }
    ,{"category": 'GM (Data and Internet)', "value":1 }
    ,{"category": 'GM (International)', "value":1 }
    ,{"category": 'LDGM', "value":4 }
    ,{"category": 'Nothing', "value":5 }
    ,{"category": 'Outsourcing Company', "value":6 }
    ,{"category": 'readonly', "value":1 }
    ,{"category": 'Revenue', "value":3 }
    ,{"category": 'ServerRoom', "value":2 }
    ,{"category": 'Space&OFC', "value":1 }

];

const clientSunburstData = [
    {
        name: "LLI",
        children: [
            { name: "GOVT", value: 100 },
            { name: "Military", value: 60 },
            { name: "Private", value: 60 },
            { name: "Foreign", value: 60 },
            { name: "Others", value: 60 }
        ]
    },
    {
        name: "VPN",
        children: [
            { name: "GOVT", value: 135 },
            { name: "Military", value: 98 },
            { name: "Private", value: 98 },
            { name: "Foreign", value: 98 },
            { name: "Others", value: 98 }
        ]
    },
    {
        name: "CoLocation",
        children: [
            {
                name: "GOVT",
                children: [
                    { name: "EE1", value: 130 },
                    { name: "EE2", value: 87 },
                    { name: "EE3", value: 55 }
                ]
            },
            {
                name: "Military",
                value: 148 },
            {
                name: "Private",
                children: [
                    { name: "CC1", value: 53 },
                    { name: "CC2", value: 30 }
                ]
            },
            {
                name: "Foreign",
                value: 26
            },
            {
                name: "Others",
                value: 56
            }
        ]
    },
    {
        name: "NIX",
        children: [
            { name: "GOVT", value: 415 },
            { name: "Military", value: 148 },
            { name: "Private", value: 89 },
            { name: "Foreign", value: 89 },
            { name: "Others", value: 89 },

        ]
    },
    {
        name: "Transmission",
        children: [
            {
                name: "GOVT",
                children: [
                    { name: "EE1", value: 33 },
                    { name: "EE2", value: 40 },
                    { name: "EE3", value: 89 }
                ]
            },
            {
                name: "Military",
                value: 148
            },
            {
                name: "Private",
                value: 148
            },
            {
                name: "Foreign",
                value: 148
            },
            {
                name: "Others",
                value: 148
            },
        ]
    },
    {
        name: "Upstream",
        children: [
            {
                name: "GOVT",
                children: [
                    { name: "EE1", value: 33 },
                    { name: "EE2", value: 40 },
                    { name: "EE3", value: 89 }
                ]
            },
            {
                name: "Military",
                value: 148
            },
            {
                name: "Private",
                value: 148
            },{
                name: "Foreign",
                value: 148
            },{
                name: "Others",
                value: 148
            },
        ]
    }
];

const chartTypes = {
    brokenPie : "BrokenPie",
    layeredColumn : "LayeredColumn",
    stackedBarHorizontal : "StackedBarHorizontal",
    stackedBarVertical : "StackedBarVertical",
    radialHistogram : "RadialHistogram",
    variableRadiusPie : "VariableRadiusPie",
    simpleColumn: "SimpleColumn",
    simplePie : "SimplePie",
    timeline: "TimeLine",
    sankeyDiagram : "SankeyDiagram",
    sankeyVerticalDiagram : "SankeyVerticalDiagram",
    collapsibleForceDirectedTree: 'CollapsibleForceDirectedTree'
};

const dataTypes = {
    applicationLayeredColumnData,
    applicationStackedBarHorizontalData,
    userRadialHistogramData,
    entityBrokenPieData,
    demandNoteBrokenPieData,
    clientStackedBarVerticalData,
    outsourcingBillVariableRadiusPieData,
    userSimpleColumnData,
    clientSunburstData,
    countrySimplePieChartData,

};

am4core.useTheme(am4themes_material);
am4core.useTheme(am4themes_animated);

const createChart = (where, what, data, additionalOption) => {

    let chart;
    switch (what) {
        case chartTypes.brokenPie :
            chart = am4core.create(where, am4charts.PieChart);
            createBrokenPieFromAMChart(data, chart);
            break;
        case chartTypes.layeredColumn :
            chart = am4core.create(where, am4charts.XYChart);
            createLayeredColumnFromAMChart(data, chart);
            break;
        case chartTypes.stackedBarHorizontal :
            chart = am4core.create(where, am4charts.XYChart);
            createStackedBarHorizontalFromAMChart(data, chart);
            break;
        case chartTypes.radialHistogram :
            chart = am4core.create(where, am4charts.RadarChart);
            createRadialHistogramFromAMChart(data, chart);
            break;
        case chartTypes.stackedBarVertical :
            chart = am4core.create(where, am4charts.XYChart);
            createStackedBarVerticalFromAMChart(data, chart);
            break;
        case chartTypes.variableRadiusPie :
            chart = am4core.create(where, am4charts.PieChart);
            createVariableRadiusPieFromAMChart(data, chart);
            break;
        case chartTypes.simpleColumn :
            chart = am4core.create(where, am4charts.XYChart);
            createSimpleColumnFromAMChart(data, chart, additionalOption.xTitle, additionalOption.yTitle);
            break;
        case chartTypes.sunburst:
            chart = am4core.create(where, am4plugins_sunburst.Sunburst);
            createSunburstFromAMChart(data, chart);
            break;
        case chartTypes.simplePie:
            chart = am4core.create(where, am4charts.PieChart);
            createSimplePieFromAMChart(data, chart, additionalOption.innerRadius);
            break;
        case chartTypes.timeline:
            chart = am4core.create(where, am4charts.XYChart);
            createTimelineFromAMChart(data, chart);
            break;
        case chartTypes.sankeyDiagram:
            chart = am4core.create(where, am4charts.SankeyDiagram);
            createSankeyDiagram(data, chart);
            break;
        case chartTypes.sankeyVerticalDiagram:
            chart = am4core.create(where, am4charts.SankeyDiagram);
            createSankeyVerticalDiagram(data, chart);
            break;
        case chartTypes.collapsibleForceDirectedTree:
            chart = am4core.create(where, am4plugins_forceDirected.ForceDirectedTree);
            createCollapsibleForceDirectedTree(data, chart);
            break;
    }
};

const createLayeredColumnFromAMChart = (data, chart) => {

    chart.hiddenState.properties.opacity = 0; // this creates initial fade-in

    chart.data = data;
    chart.colors.list = colorList;
    // chart.colors.step = 2;

    createCategoryAxisForXYChart(chart );
    createValueAxisForXYChart(chart, false);



    let timeSeries = [];
    Object.keys(data[0]).forEach((item)=>{
       if(item.startsWith("value_")) {
           let nextPart = item.substr(item.lastIndexOf("_")+1);
           timeSeries.push(nextPart);
       }
    });
    for(let i=0;i<timeSeries.length;i++){
        createColumnSeries(chart, timeSeries[i],
            0,   true);
    }

    chart.scrollbarX = new am4core.Scrollbar();
};

const createBrokenPieFromAMChart = (data, chart) => {
    let selected;
    chart.hiddenState.properties.opacity = 0; // this creates initial fade-in
    chart.data = generateChartData();

    let pieSeries = createPieSeries(chart, "category", "value");
    pieSeries.slices.template.propertyFields.fill = "color";
    pieSeries.slices.template.propertyFields.isActive = "pulled";
    pieSeries.slices.template.strokeWidth = 0;
    setPieChartSeriesSettings(pieSeries);

    function generateChartData() {
        return generateBrokenPieData(data, selected);
    }

    pieSeries.slices.template.events.on("hit", function(event) {
        if (event.target.dataItem.dataContext.id !== undefined) {
            selected = event.target.dataItem.dataContext.id;
        } else {
            selected = undefined;
        }
        chart.data = generateChartData();
    });
    chart.legend = new am4charts.Legend();
};

const  createStackedBarHorizontalFromAMChart = (data, chart)=> {

    chart.data = data;
    chart.legend = new am4charts.Legend();
    chart.legend.position = "right";

// Create axes
    var categoryAxis = chart.yAxes.push(new am4charts.CategoryAxis());
    categoryAxis.dataFields.category = "year";
    categoryAxis.renderer.grid.template.opacity = 0;

    var valueAxis = chart.xAxes.push(new am4charts.ValueAxis());
    valueAxis.min = 0;
    valueAxis.renderer.grid.template.opacity = 0;
    valueAxis.renderer.ticks.template.strokeOpacity = 0.5;
    valueAxis.renderer.ticks.template.stroke = am4core.color("#495C43");
    valueAxis.renderer.ticks.template.length = 10;
    valueAxis.renderer.line.strokeOpacity = 0.5;
    valueAxis.renderer.baseGrid.disabled = true;
    valueAxis.renderer.minGridDistance = 40;

// Create series
    function createSeries(field, name) {
        var series = chart.series.push(new am4charts.ColumnSeries());
        series.dataFields.valueX = field;
        series.dataFields.categoryY = "year";
        series.stacked = true;
        series.name = name;
        series.columns.template.tooltipText = "{name} : {valueX}";

        var labelBullet = series.bullets.push(new am4charts.LabelBullet());
        labelBullet.locationX = 0.5;
        labelBullet.label.text = "{valueX}";
        labelBullet.label.fill = am4core.color("#fff");
    }

    createSeries("LLI", "Lease Line Internet");
    createSeries("VPN", "Virtual Private Network");
    createSeries("NIX", "National Interchange");
};

const  createRadialHistogramFromAMChart= (data, chart)=> {


// Create chart instance

    chart.scrollbarX = new am4core.Scrollbar();

    chart.data = data;
    chart.radius = am4core.percent(100);
    chart.innerRadius = am4core.percent(100);

// Create axes
    var categoryAxis = chart.xAxes.push(new am4charts.CategoryAxis());
    categoryAxis.dataFields.category = "category";
    categoryAxis.renderer.grid.template.location = 0;
    categoryAxis.renderer.minGridDistance = 30;
    categoryAxis.tooltip.disabled = false;
    categoryAxis.renderer.minHeight = 110;
    categoryAxis.renderer.grid.template.disabled = true;
//categoryAxis.renderer.labels.template.disabled = true;
    let labelTemplate = categoryAxis.renderer.labels.template;
    labelTemplate.radius = am4core.percent(-60);
    labelTemplate.location = 0.5;
    labelTemplate.relativeRotation = 90;

    var valueAxis = chart.yAxes.push(new am4charts.ValueAxis());
    valueAxis.renderer.grid.template.disabled = true;
    valueAxis.renderer.labels.template.disabled = true;
    valueAxis.tooltip.disabled = true;

// Create series
    var series = chart.series.push(new am4charts.RadarColumnSeries());
    series.sequencedInterpolation = true;
    series.dataFields.valueY = "value";
    series.dataFields.categoryX = "category";
    series.columns.template.strokeWidth = 0;
    series.tooltipText = "{valueY}";
    series.columns.template.radarColumn.cornerRadius = 10;
    series.columns.template.radarColumn.innerCornerRadius = 0;

    series.tooltip.pointerOrientation = "vertical";

// on hover, make corner radiuses bigger
    let hoverState = series.columns.template.radarColumn.states.create("hover");
    hoverState.properties.cornerRadius = 0;
    hoverState.properties.fillOpacity = 1;


    series.columns.template.adapter.add("fill", function(fill, target) {
        return chart.colors.getIndex(target.dataItem.index);
    })

// Cursor
    chart.cursor = new am4charts.RadarCursor();
    chart.cursor.innerRadius = am4core.percent(50);
    chart.cursor.lineY.disabled = true;
};

const  createStackedBarVerticalFromAMChart = (data , chart) => {

    chart.hiddenState.properties.opacity = 0; // this creates initial fade-in
    chart.data = data;
    chart.colors.step = 2;
    chart.padding(30, 30, 10, 30);
    chart.legend = new am4charts.Legend();

    createCategoryAxisForXYChart(chart);
    createValueAxisForXYChart(chart, true);

    let clientCategories = [];
    Object.keys(data[0]).forEach((item)=>{
        if(item.startsWith("value_")) {
            let nextPart = item.substr(item.lastIndexOf("_")+1);
            clientCategories.push(nextPart);
        }
    });
    for(let i=0;i<clientCategories.length;i++){
        let series = createColumnSeries(chart, clientCategories[i],
            70,  false, true);
        series.dataFields.valueYShow = "totalPercent";
        series.dataItems.template.locations.categoryX = 0.5;
        series.tooltip.pointerOrientation = "vertical";
        createBullet(series, true, "{valueY.totalPercent.formatNumber('#.00')}%", .7);
    }
    chart.scrollbarX = new am4core.Scrollbar();
};

const createVariableRadiusPieFromAMChart = (data, chart)=> {

    chart.hiddenState.properties.opacity = 0; // this creates initial fade-in
    chart.data = data;

    let series = createPieSeries(chart, "category", "value");
    series.dataFields.radiusValue = "value";
    series.slices.template.cornerRadius = 6;
    series.colors.step = 3;

    series.hiddenState.properties.endAngle = -90;

    chart.legend = new am4charts.Legend();
};

const  createSimpleColumnFromAMChart = (data, chart, xTitle, yTitle)=> {
    chart.data = data;

    let categoryAxis = createCategoryAxisForXYChart(chart);
    categoryAxis.title.text = "";
    categoryAxis.renderer.labels.template.rotation = 270;
    categoryAxis.renderer.labels.template.horizontalCenter = "left";
    categoryAxis.renderer.labels.template.verticalCenter = "middle";
    categoryAxis.renderer.minGridDistance = 30;
    // categoryAxis.renderer.labels.template.disabled = true ;

    let valueAxis = createValueAxisForXYChart(chart, false);
    valueAxis.title.text = yTitle;
    valueAxis.title.fontWeight = "bold";

    let series = chart.series.push(new am4charts.ColumnSeries());
    series.dataFields.valueY = "value";
    series.dataFields.categoryX = "category";
    series.name = "Value";
    series.columns.template.tooltipText = "{categoryX}: [bold]{valueY}[/]";
    series.columns.template.fillOpacity = .3;

    let columnTemplate = series.columns.template;
    columnTemplate.strokeWidth = 2;
    columnTemplate.strokeOpacity = 1;
};

const createSunburstFromAMChart = (data, chart)=> {

    chart.data = data;


    chart.colors.step = 2;
    chart.fontSize = 11;
    chart.innerRadius = am4core.percent(2);
    chart.radius = am4core.percent(100);

    // define data fields
    chart.dataFields.value = "value";
    chart.dataFields.name = "name";
    chart.dataFields.children = "children";

    var level1SeriesTemplate = new am4plugins_sunburst.SunburstSeries();
    chart.seriesTemplates.setKey("1", level1SeriesTemplate);
    level1SeriesTemplate.fillOpacity = 0.75;
    level1SeriesTemplate.hiddenInLegend = true;

    var level2SeriesTemplate = new am4plugins_sunburst.SunburstSeries();
    chart.seriesTemplates.setKey("2", level2SeriesTemplate);
    level2SeriesTemplate.fillOpacity = 0.5;
    level2SeriesTemplate.hiddenInLegend = true;

    chart.legend = new am4charts.Legend();
};

const createSimplePieFromAMChart = (data, chart, innerRadiusPercentage)=> {

    chart.data = data;
    let pieSeries = createPieSeries(chart, "category", "value");
    // Let's cut a hole in our Pie chart
    chart.innerRadius = am4core.percent(innerRadiusPercentage);
    // set pieSeries properties including shadow, hover, cursor
    setPieChartSeriesSettings(pieSeries);
    // Add a legend
    chart.legend = new am4charts.Legend();
};

const createTimelineFromAMChart = (data, chart)=>{
    chart.data = data;

    // Create axes

    // Create axes
    let xAxis = chart.xAxes.push(new am4charts.CategoryAxis());
    xAxis.dataFields.category = "category";
    xAxis.renderer.grid.template.disabled = true;
    xAxis.renderer.labels.template.disabled = true;
    xAxis.tooltip.disabled = true;

    let yAxis = chart.yAxes.push(new am4charts.ValueAxis());
    yAxis.min = 0;
    yAxis.max = 1.99;
    yAxis.renderer.grid.template.disabled = true;
    yAxis.renderer.labels.template.disabled = true;
    yAxis.renderer.baseGrid.disabled = true;
    yAxis.tooltip.disabled = true;


// Create series
    let series = chart.series.push(new am4charts.LineSeries());
    series.dataFields.categoryX = "category";
    series.dataFields.valueY = "value";
    series.strokeWidth = 4;
    series.sequencedInterpolation = true;

    let bullet = series.bullets.push(new am4charts.CircleBullet());
    bullet.setStateOnChildren = true;
    bullet.states.create("hover");
    bullet.circle.radius = 10;
    bullet.circle.states.create("hover").properties.radius = 15;

    let labelBullet = series.bullets.push(new am4charts.LabelBullet());
    labelBullet.setStateOnChildren = true;
    labelBullet.states.create("hover").properties.scale = 1.2;
    labelBullet.label.text = "{text}";
    labelBullet.label.maxWidth = 150;
    labelBullet.label.wrap = true;
    labelBullet.label.truncate = false;
    labelBullet.label.textAlign = "middle";
    labelBullet.label.paddingTop = 20;
    labelBullet.label.paddingBottom = 20;
    labelBullet.label.fill = am4core.color("#999");
    labelBullet.label.states.create("hover").properties.fill = am4core.color("#000");

    labelBullet.label.propertyFields.verticalCenter = "center";


    chart.cursor = new am4charts.XYCursor();
    chart.cursor.lineX.disabled = true;
    chart.cursor.lineY.disabled = true;

};
const createCollapsibleForceDirectedTree=(data, chart)=>{
    chart.legend = new am4charts.Legend();
    let networkSeries = chart.series.push(new am4plugins_forceDirected.ForceDirectedSeries());
    networkSeries.data = data;

    networkSeries.dataFields.linkWith = "linkWith";

    networkSeries.dataFields.name = "name";
    networkSeries.dataFields.id = "name";
    networkSeries.dataFields.value = "value";
    networkSeries.dataFields.children = "children";

    networkSeries.nodes.template.tooltipText = "[bold]{name}[/]: {value}";
    networkSeries.nodes.template.fillOpacity = 1;

    networkSeries.nodes.template.label.text = "[bold]{name}[/]";
    networkSeries.fontSize = 10;
    networkSeries.maxLevels = 2;
    networkSeries.maxRadius = am4core.percent(15);
    networkSeries.minRadius = am4core.percent(5);
    networkSeries.manyBodyStrength = -26;
    networkSeries.nodes.template.label.hideOversized = true;
    networkSeries.nodes.template.label.truncate = false;

};
const createSankeyVerticalDiagram = (data, chart)=> {
    chart.data = data;
    chart.minNodeSize = 0.001;
    chart.nodeAlign = "bottom";
    chart.paddingLeft = 80;
    chart.paddingRight = 80;
    chart.dataFields.fromName = "from";
    chart.dataFields.toName = "to";
    chart.dataFields.value = "value";
    chart.dataFields.color = "color";

    chart.orientation = "vertical";
    chart.sortBy = "none";

    chart.nodes.template.togglable = false;

    var linkTemplate = chart.links.template;
    linkTemplate.colorMode = "gradient";
    linkTemplate.fillOpacity = 0.95;

    linkTemplate.cursorOverStyle = am4core.MouseCursorStyle.pointer;
    linkTemplate.readerTitle = "drag me!";
    linkTemplate.showSystemTooltip = true;
    linkTemplate.tooltipText = "";
    linkTemplate.propertyFields.zIndex = "zIndex";
    linkTemplate.tension = 0.6;

//dragging
    chart.links.template.events.on("down", function (event) {
        var fromNode = event.target.dataItem.fromNode;
        var toNode = event.target.dataItem.toNode;

        var distanceToFromNode = am4core.math.getDistance(event.pointer.point, { x: fromNode.pixelX, y: fromNode.pixelY });
        var distanceToToNode = Infinity;
        if (toNode) {
            distanceToToNode = am4core.math.getDistance(event.pointer.point, { x: toNode.pixelX, y: toNode.pixelY });
        }

        if (distanceToFromNode < distanceToToNode) {
            fromNode.dragStart(event.pointer);
        }
        else {
            toNode.dragStart(event.pointer);
        }
    })

    chart.nodes.template.draggable = true;
    chart.nodes.template.inert = true;
    chart.nodes.template.width = 0;
    chart.nodes.template.height = 0;
    chart.nodes.template.nameLabel.disabled = true;
    chart.nodes.template.clickable = false;

    var labelBullet = chart.links.template.bullets.push(new am4charts.LabelBullet());
    labelBullet.label.propertyFields.text = "labelText";
    labelBullet.propertyFields.locationX = "labelLocation";
    labelBullet.propertyFields.rotation = "labelRotation";
    labelBullet.label.rotation = -90;
    labelBullet.propertyFields.dy = "dy";
    labelBullet.label.propertyFields.horizontalCenter = "center";
    labelBullet.label.textAlign = "middle";
};
const createSankeyDiagram = (data, chart) => {

    chart.hiddenState.properties.opacity = 0; // this creates initial fade-in

    chart.data = data;

    let hoverState = chart.links.template.states.create("hover");
    hoverState.properties.fillOpacity = 0.6;

    let linkTemplate = chart.links.template;
    linkTemplate.tooltipText = "{from} => {to} : {value}";

    chart.dataFields.fromName = "from";
    chart.dataFields.toName = "to";
    chart.dataFields.value = "value";

// for right-most label to fit
    chart.paddingRight = 30;

// make nodes draggable
    var nodeTemplate = chart.nodes.template;
    nodeTemplate.inert = true;
    nodeTemplate.readerTitle = "Drag me!";
    nodeTemplate.showSystemTooltip = true;
    nodeTemplate.width = 20;

// make nodes draggable
    var nodeTemplate = chart.nodes.template;
    nodeTemplate.readerTitle = "Click to show/hide or drag to rearrange";
    nodeTemplate.showSystemTooltip = true;
    nodeTemplate.cursorOverStyle = am4core.MouseCursorStyle.pointer



};
const createPieSeries = (chart, category, value)=>{
    let pieSeries = chart.series.push(new am4charts.PieSeries());
    pieSeries.dataFields.category = category;
    pieSeries.dataFields.value = value;
    pieSeries.colors.list = colorList;
    return pieSeries;
};

const setPieChartSeriesSettings = (pieSeries) =>{
    // Put a thick white border around each Slice
    pieSeries.slices.template.stroke = am4core.color("#fff");
    pieSeries.slices.template.strokeWidth = 2;
    pieSeries.slices.template.strokeOpacity = 1;
    pieSeries.slices.template
        // change the cursor on hover to make it apparent the object can be interacted with
        .cursorOverStyle = [
        {
            "property": "cursor",
            "value": "pointer"
        }
    ];

    // pieSeries.alignLabels = true;
    // pieSeries.labels.template.bent = true;
    // pieSeries.labels.template.radius = 5;
    pieSeries.labels.template.padding(2, 2, 2, 2);
    pieSeries.ticks.template.disabled = true;

    pieSeries.labels.template.disabled = true;
    // Create a base filter effect (as if it's not there) for the hover to return to
    let shadow = pieSeries.slices.template.filters.push(new am4core.DropShadowFilter);
    shadow.opacity = 0;

    // Create hover state
    let hoverState = pieSeries.slices.template.states.getKey("hover"); // normally we have to create the hover state, in this case it already exists

    // Slightly shift the shadow and make it more prominent on hover
    let hoverShadow = hoverState.filters.push(new am4core.DropShadowFilter);
    hoverShadow.opacity = 0.7;
    hoverShadow.blur = 5;
};

const generateBrokenPieData = (data, selected)=>{
    let chartData = [];
    for (let i = 0; i < data.length; i++) {

        let dataItem = data[i];
        let selectedChildren = dataItem.children;
        if (i === selected) {
            for (let j = 0; j < selectedChildren.length; j++) {
                chartData.push({
                    category : selectedChildren[j].category,
                    value : selectedChildren[j].value,
                    color : dataItem.color,
                    pulled : true
                });
            }
        } else {
            chartData.push({
                category : dataItem.category,
                value : dataItem.value,
                color : dataItem.color,
                id : i
            });
        }
    }
    return chartData;
};

const createCategoryAxisForXYChart = (chart)=>{
    let categoryAxis = chart.xAxes.push(new am4charts.CategoryAxis());
    categoryAxis.dataFields.category = "category";
    categoryAxis.renderer.grid.template.location = 0;
    return categoryAxis;
    // categoryAxis.renderer.minGridDistance = 30;

};

const createValueAxisForXYChart = (chart, strictMinMax)=>{
    let valueAxis = chart.yAxes.push(new am4charts.ValueAxis());
    if(strictMinMax) {
        valueAxis.min = 0;
        valueAxis.max = 100;
        valueAxis.strictMinMax = true;
        valueAxis.calculateTotals = true;
        valueAxis.renderer.minWidth = 50;
    }
    return valueAxis;
};

const createColumnSeries = (chart, valueY, widthPercentage, isClustered, isStacked)=>{
    let series = chart.series.push(new am4charts.ColumnSeries());
    series.name =  valueY;
    series.dataFields.valueY = "value_" + valueY;
    series.dataFields.categoryX = "category";
    series.clustered = isClustered;
    series.stacked = isStacked;
    if(widthPercentage>0){
        series.columns.template.width = am4core.percent(widthPercentage);
    }
    //

    series.columns.template.tooltipText = "{categoryX}({name}) : [bold]{valueY}[/]";
    // series.tooltipText = tooltipDescription;

    series.columns.template
        // change the cursor on hover to make it apparent the object can be interacted with
        .cursorOverStyle = [
        {
            "property": "cursor",
            "value": "pointer"
        }
    ];

    let shadow = series.columns.template.filters.push(new am4core.DropShadowFilter);
    shadow.opacity = 0;

    let hoverState = series.columns.template.states.create("hover"); // normally we have to create the hover state, in this case it already exists

    // Slightly shift the shadow and make it more prominent on hover
    let hoverShadow = hoverState.filters.push(new am4core.DropShadowFilter);
    hoverShadow.opacity = 0.7;
    hoverShadow.blur = 5;
    return series;
};

const createBullet = (series, enableInteraction, bulletText, locationY) =>{
    let bullet = series.bullets.push(new am4charts.LabelBullet());
    bullet.interactionsEnabled = enableInteraction;
    bullet.label.text = bulletText;
    bullet.label.fill = am4core.color("#ffffff");
    bullet.locationY = locationY;

};