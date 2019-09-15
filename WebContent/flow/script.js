var app = new Vue({
    el: "#app",
    data: {
        modules: null,
        selectedModule: "",
        moduleOptions: [],
        flows: null,
        selectedFlow: "",
        flowOptions: [],
        roles: null,
        states: null,
        flowInit: null,
        transitions: null,
        transitionRoles: null,
        graphNodes: null,
        graphEdges: null,
        graphOptions: {
            physics: {
                enabled: false,
            },
            nodes: {
                // color: '#cccccc',
                font: {
                    color: '#000055',
                }
            },
            edges:{
                font: {
                    align: 'horizontal'
                },
                color: '#FF0000',
                arrows: 'to',
                scaling:{
                    label: true,
                },
                smooth: true,
            },
            interaction: {
                dragNodes: true,
                zoomView: true,
                dragView: true
            }
        }
    },
    created: function () {
        console.log("vue init");
    },
    mounted: function () {
        this.fetchModules();
        this.fetchRoles();
        this.fetchStates();
        this.fetchTransitions();
        this.fetchTransitionRoles();
    },
    methods: {
        fetchModules: function () {
            var url = "api/modules.do";
            axios.get(url)
                .then(response => {
                    this.modules = response.data.payload.members;
                    this.renderModulesList();
                })
                .catch(error => {console.log(error);})
                .finally(() => {});
        },
        fetchRoles: function () {
            var url = "api/roles.do";
            axios.get(url)
                .then(response => {
                    this.roles = response.data.payload.members;
                })
                .catch(error => {console.log(error);})
                .finally(() => {});
        },
        fetchStates: function () {
            var url = "api/states.do";
            axios.get(url)
                .then(response => {
                    this.states = response.data.payload.elements;
                })
                .catch(error => {console.log(error);})
                .finally(() => {});
        },
        fetchTransitions: function () {
            var url = "api/transitions.do";
            axios.get(url)
                .then(response => {
                    this.transitions = response.data.payload.elements;
                })
                .catch(error => {console.log(error);})
                .finally(() => {});
        },
        fetchTransitionRoles: function () {
            var url = "api/transition-roles.do";
            axios.get(url)
                .then(response => {
                    this.transitionRoles = response.data.payload.elements;
                })
                .catch(error => {console.log(error);})
                .finally(() => {});
        },
        fetchFlows: function (id) {
            var url = "api/flows.do?module=" + id;
            axios.get(url)
                .then(response => {
                    this.flows = response.data.payload.elements;
                    this.renderFlowsList();
                })
                .catch(error => {console.log(error);})
                .finally(() => {});
        },
        fetchFlowInit: function (id) {
            var url = "api/states.do?flow=" + id;
            axios.get(url)
                .then(response => {
                    this.flowInit = response.data.payload.elements[0];
                    this.renderGraph();
                })
                .catch(error => {console.log(error);})
                .finally(() => {});
        },

        renderModulesList: function () {
            var options = [];
            for(var id in this.modules){
                options.push({text: this.modules[id], value: id});
            }
            this.moduleOptions = options;
        },
        renderFlowsList: function () {
            var options = [];
            for(var i = 0; i < this.flows.length; i++){
                options.push({text: app.flows[i]["name"], value: app.flows[i]["id"]});
            }
            this.flowOptions = options;
        },

        onSelectModule: function () {
            this.fetchFlows(this.selectedModule);
        },
        onSelectFlow: function () {
           this.fetchFlowInit(this.selectedFlow);
        },
        
        renderGraph: function () {
            this.graphNodes = [];
            this.graphEdges = [];

            var state = this.states.find(o => o.id == this.flowInit.id);
            this.graphNodes.push({id: state.id, label: state.name, shape: "circle", color: state.color});
            this.createEdges(state);

            setTimeout(function () {
                var container = document.getElementById('flow-graph');
                var graphData = {
                    nodes: app.graphNodes,
                    edges: app.graphEdges
                };
                var network = new vis.Network(container, graphData, app.graphOptions);
            }, 2000);
        },
        
        createNode: function (state) {
            if(!this.graphNodes.find(o => o.id == state.id))this.graphNodes.push({id: state.id, label: state.name, color: state.color});
            this.createEdges(state);
        },
        
        createEdges: function (state) {
            var transitions = this.transitions.filter(o => o.source == state.id);
            for(var i = 0; i < transitions.length ; i++){
                var dest = this.states.find(o => o.id == transitions[i].destination);
                if(this.graphEdges.find(o => o.from == state.id && o.to == dest.id)) continue;
                var transitionRoles = this.transitionRoles.filter(o => o.flowStateTransition == transitions[i].id);
                var roleStr = ".";
                for(var tr in transitionRoles)roleStr.concat(", " + this.roles[tr.role]);
                this.graphEdges.push({from: state.id, to: dest.id, label: roleStr.replace("., ", "")});
                this.createNode(dest);
            }
        },
    }
})