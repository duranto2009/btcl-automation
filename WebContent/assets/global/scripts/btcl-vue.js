
Vue.component('multiselect', window.VueMultiselect.default); //https://vue-multiselect.js.org
Vue.component('date-picker', window.DatePicker.default);
Vue.component('vue-upload-component', window.VueUploadComponent); //https://github.com/lian-yue/vue-upload-component //https://www.npmjs.com/package/vue-upload-component
Vue.config.devtools = true;



Vue.component('forward-button', {
    template: `
		<button type=button class="btn btn-default" @click="goURL">{{this.title}}</button>
	`,
    props: ['url', 'title'],
    methods: {
        goURL: function () {
            window.open(context + this.url);
        }
    }
});

Vue.component('btcl-datepicker', {
    template: `
		<date-picker v-model="dateValue" :format="typeof this.pattern == 'undefined' ? 'DD-MM-YYYY' : this.pattern" width=100% lang="en"></date-picker>
	`,
    props: ['date', 'pattern'],
    computed: {
        dateValue: {
            get() {
                return new Date(this.date);
            },
            set(val) {
                this.$emit('update:date', new Date(val).getTime());
            }
        }
    }
});

//month picker

Vue.component('btcl-monthpicker', {
    template: `
		<date-picker v-model="dateValue" format="yyyy-MM" width=100% lang="en"></date-picker>
	`,
    props: ['date'],
    computed: {
        dateValue: {
            get() {
                return new Date(this.date);
            },
            set(val) {
                this.$emit('update:date', new Date(val).getTime());
            }
        }
    }
});
//month picker ends


Vue.component('btcl-grid', {
    template: `
		<div :style="columnClass">
			<slot></slot>
		</div>
	`,
    props: ['column'],
    computed: {
        columnClass: function () {
            temp = "display:grid; padding:10px; grid-gap:10px; grid-template-columns:";
            for (i = 0; i < parseInt(this.column); i++) {
                temp += "auto ";
            }
            return temp + ";";
        }
    }
});

Vue.component('btcl-field', {
    template: `
		<div class="form-group">
			<div v-if="vertical">
                <label class="control-label">{{title}}<span v-if=required class=required aria-required=true>*</span></label>
                <slot></slot>
            </div>
			<div v-else class=row>
				<label class="col-sm-4 control-label">{{title}}<span v-if=required class=required aria-required=true>*</span></label>
				<div v-if=bare><slot></slot></div>
				<div v-else class=col-sm-6><slot></slot></div>
			</div>
		</div>
	`,
    props: {
        title: String,
        required: Boolean,
        vertical: Boolean,
        bare: Boolean
    },
    methods:{

    },

});

Vue.component('btcl-info', {
    template: `
		<div class="form-group">
			<div class=row>
				<label v-if="!left" class="col-sm-4 control-label" >{{title}}</label>
				<label v-else class="col-sm-4" style="padding-top:7px; margin-bottom:0px; text-align:left">{{title}}</label>
				<div class=col-sm-6>
					<p class="form-control" v-if="date" >{{new Date(parseInt(text)).toLocaleDateString("ca-ES")}}</p>
					<p class="form-control" v-else-if="isDate" >{{new Date(parseInt(text)).toLocaleDateString("ca-ES")}}</p>
					<p class="form-control" v-else-if="html" v-html="text"></p>
					<p class="form-control" v-else >{{text}}</p>
				</div>
			</div>
		</div>
	`,
    props:
        ['title', 'text', 'date', 'mountCallback', 'html', 'isDate', 'left'],
// 	{
// 		title: String,
// 		text: Object,
// 		date: Boolean,
// 		mountCallback: Object,
// 		html: Boolean
// 	},
    mounted() {
        this.$emit('loaded');
    },

});

//send props :textarea=true to get text area input
Vue.component('btcl-input', {
    template: `
		<div class="form-group">
			<div v-if="vertical">
                <label class="control-label">{{title}}<span v-if=required class=required aria-required=true>*</span></label>
                <input type=text class="form-control" v-model="textValue" />
            </div>
            <div v-else-if="textarea==true">
             <label class="col-sm-4 control-label">{{title}}<span v-if=required class=required aria-required=true>*</span></label>
             
              <div class=col-sm-6 v-if="placeholder"><textarea  class="form-control" v-model="textValue" v-bind:placeholder="placeholder"></textarea></div>
              <div class=col-sm-6 v-else><textarea  class="form-control" v-model="textValue" ></textarea></div>
              
            </div>
            
            <div v-else-if="radio==true">
            <label class="col-sm-4 control-label">{{title}}<span v-if=required class=required aria-required=true>*</span></label>
             <div class=col-sm-6>
                <label v-for="(listItem,listItemIndex) in radioList" class="radio-inline">
                    <input type="radio" :name="radioName" :value="listItem" :checked="listItem==1" v-model="textValue">{{listItem}}
                </label>
              
                </div>
            </div>
            
            
			<div v-else class=row>
				<label class="col-sm-4 control-label">{{title}}<span v-if=required class=required aria-required=true>*</span></label>
				<div class=col-sm-6>
					<input v-if="readonly" type=text class="form-control" v-model="textValue" readonly/>
					
					<input v-else-if="number&&placeholder" type=number class="form-control" v-model.number="textValue" v-bind:placeholder="placeholder"/>
					<input v-else-if="number" type=number class="form-control" v-model.number="textValue"/>
					<input v-else-if="placeholder" type=text class="form-control" v-model="textValue" v-bind:placeholder="placeholder"/>
					<input v-else type=text class="form-control" v-model="textValue"/>
				</div>
			</div>
		</div>
	`,
    // props: {
    //     title: String,
    //     required: Boolean,
    //     vertical: Boolean,
    //     text: String,
    //     readonly: Boolean,
    //     textarea: Boolean
    // },
    props: ['title', 'required', 'vertical', 'readonly', 'textarea', 'text', 'number', 'radio', 'radioList', 'radioName', 'placeholder'],
    computed: {
        textValue: {
            get() {
                return this.text;
            },
            set(val) {
                this.$emit('update:text', val);
            }
        }
    },
    mounted: function () {
        // debugger;
    }
});

Vue.component('btcl-form', {
    template: `
<div class="row" v-if="loading" style="text-align: center">
        <i class="fa fa-spinner fa-spin fa-5x"></i>
    </div>
		<form v-else :action="this.action" @submit.prevent="submit">
            <slot></slot>
        	<div style="padding-top:30px;">
            	<button type="submit" class="btn green-haze btn-block">Submit</button>
            </div>
        </form>
	`,
    props: ['action', 'name', 'formData', 'redirect', 'isJson','callback'],
    data: function () {
        return {
            submittedData: {},
            loading: false,
        }
    },
    methods: {
        submit: function (event) {
            if( typeof this.callback !== 'undefined'){
                if(this.callback() == false){
                    return;
                };
            }
            this.name.forEach(function (value, index, array) {
                this.submittedData[value] = this.formData[index];
            }, this);
            this.loading = true;
            // alert('before debug');debugger;
            if (this.isJson == true) {
                this.submittedData = {'application': JSON.stringify(this.submittedData.application)};
            }
            axios.post(event.target.action, this.submittedData)
                .then(result => {
                    this.loading =false;
                    if (typeof this.redirect !== 'undefined') {
                        this.redirect(result);
                    } else {
                        if (result.data.responseCode == 2) {
                            toastr.error(result.data.msg);
                        } else if (result.data.responseCode == 1) {
                            toastr.success("Your request has been processed", "Success");
                        } else {
                            toastr.error("Your request was not accepted", "Failure");
                        }
                    }
                    // this.loading = false;
                })
                .catch(function (error) {
                });
        }
    }
});

Vue.component('btcl-portlet', {
    template: `
        <div v-if="collapse==true" class="portlet light bordered">
			<div v-if="title!==undefined" class="portlet-title">
				
				<div style="width:100%" class="tools"><a style="width:100%;font-size:20px" href="" class="expand" data-original-title="" title="">{{title}}</a></div>
			</div>
			
            <div class="portlet-body portlet-collapsed" style="display: none">
            	<slot></slot>
            </div>
		</div>


		<div v-else class="portlet light bordered">
			<div v-if="title!==undefined" class="portlet-title">
				<div class="caption"><span class="caption-subject font-dark bold uppercase">{{title}}</span></div>
				<div class="tools"><a href="" class="collapse" data-original-title="" title=""></a></div>
			</div>
			
            <div class="portlet-body">
               
                <slot></slot>
                
            	
            </div>
		</div>
	`,
    props: ['title', 'collapse']
});

Vue.component('btcl-bounded', {
    template: `
		<div>
			<br><hr>
			<h4 v-if="title!==undefined">{{title}}</h4>
            <slot></slot>
            <br><hr><br>
		</div>
	`,
    props: ['title']
});


Vue.component('btcl-body', {

    template: `
        <div>
             <div v-if ="loader" class="spinner-loading"></div>
             <div v-else class="portlet light bordered">
                 <div class="portlet-title">
                    <div class="caption font-green-sharp">
                        <span class="caption-subject bold uppercase">{{title}}</span>
                        <span class="caption-helper">{{subtitle}}</span>
                    </div>
                                                
                    <div class="actions">
                        <!--<a href="javascript:;" class="btn btn-circle btn-default btn-sm"><i class="fa fa-pencil"></i> Edit </a>-->
                        <a class="btn btn-circle btn-icon-only btn-default fullscreen" href="javascript:;"> </a>
                    </div>
                </div>
		                                
                <div class="portlet-body">
                    <div class=form-horizontal>
                        <div class=form-body>
                            <slot></slot>
                        </div>
                    </div>
                </div>
            </div>
		</div> 
	`,
    data: function () {
        return {
        }
    },
    props: ['title', 'subtitle', 'loader'],
});

Vue.component('btcl-loader', {

    template: `
        <div v-if ="loader" class="spinner-loading">
        </div>
		<div v-else>
			<slot></slot>
		</div>
		
	`,
    data: function () {
        return {}
    },
    props: [ 'loader'],
});



Vue.component('vendor-search', {
    template: `
			<div>
			<!--<p v-if=this.clientFlag class=form-control>{{clientObject.label}}</p>-->
			<multiselect v-if="multiple==true" v-model="vendorObject" :options="this.vendorList" 
	        	track-by=label label=label :allow-empty="false" :searchable=true :loading=this.isLoading
	        	id=ajax :internal-search=false :options-limit="500" :limit="15" :multiple="true"
	    		@search-change="searchVendor" open-direction="bottom" @select="selectVendor">
	        </multiselect>
			<multiselect v-else v-model="vendorObject" :options="this.vendorList" 
	        	track-by=label label=label :allow-empty="false" :searchable=true :loading=this.isLoading
	        	id=ajax :internal-search=false :options-limit="500" :limit="15" 
	    		@search-change="searchVendor" open-direction="bottom" @select="selectVendor">
	        </multiselect>
	        </div>
	`,
    props: ['vendor', 'callback', 'multiple'],
    data: function () {
        return {
            isLoading: false,
            vendorList: [],
            clientFlag: true
        }
    },
    computed: {
        vendorObject: {
            get() {
                return this.vendor;
            },
            set(val) {
                this.$emit('update:vendor', val);
            }
        }
    },
    methods: {
        searchVendor: function (query) {
            this.isLoading = true;
            axios({method: 'GET', 'url': context + 'lli/client/get-loop-provider.do'})
                .then(result => {
                    this.vendorList = result.data.payload;
                    this.isLoading = false;
                }, error => {
                });
        },
        selectVendor: function (selectedOption) {
            if (typeof this.callback !== "undefined") {
                this.callback(selectedOption.ID);
            }
        }
    },
    mounted() {
        // this.clientFlag = isClientLoggedIn;
        // if (this.clientFlag) {
        //     this.clientObject = loggedInAccount;
        //     this.selectClient(loggedInAccount);
        // }
    },

});


Vue.component('lli-client-search', {
    template: `
			<div>
			<!--<p v-if=this.clientFlag class=form-control>{{clientObject.label}}</p>-->
			<multiselect v-if="multiple==true" v-model="clientObject" :options="this.clientList" 
	        	track-by=label label=label :allow-empty="false" :searchable=true :loading=this.isLoading
	        	id=ajax :internal-search=false :options-limit="500" :limit="15" :multiple="true"
	    		@search-change="searchClient" open-direction="bottom" @select="selectClient">
	        </multiselect>
	         <multiselect v-else-if="id_first==true" v-model="clientObject" :options="this.clientList" 
	        	track-by=label label=label :custom-label="customLabelForIDFirst" :allow-empty="false" :searchable=true :loading=this.isLoading
	        	id=ajax :internal-search=false :options-limit="500" :limit="15" 
	    		@search-change="searchClient" open-direction="bottom" @select="selectClient">
	        </multiselect>
			<multiselect v-else v-model="clientObject" :options="this.clientList" 
	        	track-by=label label=label :allow-empty="false" :searchable=true :loading=this.isLoading
	        	id=ajax :internal-search=false :options-limit="500" :limit="15" 
	    		@search-change="searchClient" open-direction="bottom" @select="selectClient">
	        </multiselect>
	        </div>
	`,
    props: ['client', 'callback', 'multiple', 'id_first'],
    data: function () {
        return {
            isLoading: false,
            clientList: [],
            clientFlag: true
        }
    },
    computed: {
        clientObject: {
            get() {
                return this.client;
            },
            set(val) {
                this.$emit('update:client', val);
            }
        }
    },
    methods: {
        customLabelForIDFirst({ID, label}) {
            if(ID && label) {
                return `${ID} ( ${label} )`;
            }else {
                return 'Select Client ID';
            }
        },
        searchClient: function (query) {
            this.isLoading = true;
            let url = "";
            if(this.id_first){
                url = context + 'lli/client/get-all-client-by-id.do?val=' + query;
            }else {
                url = context + 'lli/client/get-client.do?val=' + query;
            }
            axios({method: 'GET', 'url':url})
                .then(result => {
                    this.clientList = result.data.payload;
                    this.isLoading = false;
                }, error => {
                });
        },
        selectClient: function (selectedOption) {
            if (typeof this.callback !== "undefined") {
                this.callback(selectedOption.ID);
            }
        }
    },
    mounted() {
        this.clientFlag = isClientLoggedIn;
        if (this.clientFlag) {
            this.clientObject = loggedInAccount;
            this.selectClient(loggedInAccount);
        }
    },

});


Vue.component('nix-client-search', {
    template: `
			<div>
			<!--<p v-if=this.clientFlag class=form-control>{{clientObject.label}}</p>-->
			<multiselect v-if="multiple==true" v-model="clientObject" :options="this.clientList" 
	        	track-by=label label=label :allow-empty="false" :searchable=true :loading=this.isLoading
	        	id=ajax :internal-search=false :options-limit="500" :limit="15" :multiple="true"
	    		@search-change="searchClient" open-direction="bottom" @select="selectClient">
	        </multiselect>
			<multiselect v-else v-model="clientObject" :options="this.clientList" 
	        	track-by=label label=label :allow-empty="false" :searchable=true :loading=this.isLoading
	        	id=ajax :internal-search=false :options-limit="500" :limit="15" 
	    		@search-change="searchClient" open-direction="bottom" @select="selectClient">
	        </multiselect>
	        </div>
	`,
    props: ['client', 'callback', 'multiple'],
    data: function () {
        return {
            isLoading: false,
            clientList: [],
            clientFlag: true
        }
    },
    computed: {
        clientObject: {
            get() {
                return this.client;
            },
            set(val) {
                this.$emit('update:client', val);
            }
        }
    },
    methods: {
        searchClient: function (query) {
            isLoading = true;
            axios({method: 'GET', 'url': context + 'nix/client/get-client.do?val=' + query})
                .then(result => {
                    this.clientList = result.data.payload;
                    isLoading = false;
                }, error => {
                });
        },
        selectClient: function (selectedOption) {
            if (typeof this.callback !== "undefined") {
                this.callback(selectedOption.ID);
            }
        }
    },
    mounted() {
        this.clientFlag = isClientLoggedIn;
        if (this.clientFlag) {
            this.clientObject = loggedInAccount;
            this.selectClient(loggedInAccount);
        }
    }
});


Vue.component('vpn-client-search', {
    template: `
			<div>
			<!--<p v-if=this.clientFlag class=form-control>{{clientObject.label}}</p>-->
			<multiselect v-if="multiple==true" v-model="clientObject" :options="this.clientList" 
	        	track-by=label label=label :allow-empty="false" :searchable=true :loading=this.isLoading
	        	id=ajax :internal-search=false :options-limit="500" :limit="15" :multiple="true"
	    		@search-change="searchClient" open-direction="bottom" @select="selectClient">
	        </multiselect>
			<multiselect v-else v-model="clientObject" :options="this.clientList" 
	        	track-by=label label=label :allow-empty="false" :searchable=true :loading=this.isLoading
	        	id=ajax :internal-search=false :options-limit="500" :limit="15" 
	    		@search-change="searchClient" open-direction="bottom" @select="selectClient">
	        </multiselect>
	        </div>
	`,
    props: ['client', 'callback', 'multiple'],
    data: function () {
        return {
            isLoading: false,
            clientList: [],
            clientFlag: true
        }
    },
    computed: {
        clientObject: {
            get() {
                return this.client;
            },
            set(val) {
                this.$emit('update:client', val);
            }
        }
    },
    methods: {
        searchClient: function (query) {
            isLoading = true;
            axios({method: 'GET', 'url': context + 'vpn/client/get-client.do?val=' + query})
                .then(result => {
                    this.clientList = result.data.payload;
                    isLoading = false;
                }, error => {
                });
        },
        selectClient: function (selectedOption) {
            if (typeof this.callback !== "undefined") {
                this.callback(selectedOption.ID);
            }
        }
    },
    mounted() {
        this.clientFlag = isClientLoggedIn;
        if (this.clientFlag) {
            this.clientObject = loggedInAccount;
            this.selectClient(loggedInAccount);
        }
    }
});


//search system user by module id
// mandatory props -
//   1. data variable where the list will be saved
//   2. module which is module id
// example:<user-search-by-module :client.sync="application.client" :module="moduleId">Client</user-search-by-module>
Vue.component('user-search-by-module', {
    template: `
			<div>
			<!--<p v-if=this.clientFlag class=form-control>{{clientObject.label}}</p>-->
			<multiselect v-if="multiple==true" v-model="clientObject" :options="this.clientList" 
	        	track-by=key label=value :allow-empty="false" :searchable=true :loading=this.isLoading
	        	id=ajax :internal-search=false :options-limit="500" :limit="15" :multiple="true"
	    		@search-change="searchClient" open-direction="bottom" @select="selectClient">
	        </multiselect>
	        <multiselect v-else-if="id_first==true" v-model="clientObject" :options="this.clientList" 
	        	track-by=key label="value" :custom-label="customLabelForIDFirst" :allow-empty="false" :searchable=true :loading=this.isLoading
	        	id=ajax :internal-search=false :options-limit="500" :limit="15" 
	    		@search-change="searchClient" open-direction="bottom" @select="selectClient">
	        </multiselect>
			<multiselect v-else v-model="clientObject" :options="this.clientList" 
	        	track-by=key label=value :allow-empty="false" :searchable=true :loading=this.isLoading
	        	id=ajax :internal-search=false :options-limit="500" :limit="15" 
	    		@search-change="searchClient" open-direction="bottom" @select="selectClient">
	        </multiselect>
	        </div>
	`,
    props: ['client', 'callback', 'multiple', 'module', 'id_first'],

    data: function () {
        return {
            isLoading: false,
            clientList: [],
            clientFlag: true
        }
    },
    computed: {
        clientObject: {
            get() {
                return this.client;
            },
            set(val) {
                this.$emit('update:client', val);
            }
        }
    },
    methods: {
        customLabelForIDFirst({key, value}) {
            if(key && value ) {
                return `${key} ( ${value} )`;
            }else {
                return 'Select Client ID';
            }
        },
        searchClient: function (query) {
            isLoading = true;
            let url = "";
            if(this.id_first){
                url = context + 'Client/all-by-id.do?query=' + query + '&module=' + this.module;
            }else {
                url = context + 'Client/all.do?query=' + query + '&module=' + this.module;
            }
            if (!this.clientFlag) {
                axios({
                    method: 'GET',
                    url: url

                })
                    .then(result => {
                        this.clientList = result.data.payload;
                        isLoading = false;
                    }, error => {
                    });
            }
        },
        selectClient: function (selectedOption) {
            if (typeof this.callback !== "undefined") {
                let id = selectedOption.hasOwnProperty("key") ? selectedOption.key : selectedOption.ID;
                this.isLoading = true;
                this.callback(id).then(()=>this.isLoading=false);
            }
        }
    },
    mounted() {
        this.clientFlag = isClientLoggedIn;
        if (this.clientFlag) {
            // this.clientObject = loggedInAccount;
            // client object with key value
            let clientObjectWithKeyValueAndRegistrantType = {
                key: loggedInAccount.ID,
                value: loggedInAccount.label,
                registrantType: loggedInAccount.registrantType
            };
            this.clientObject = clientObjectWithKeyValueAndRegistrantType;
            this.selectClient(clientObjectWithKeyValueAndRegistrantType);
        }
    },
    watch:{
        module: function(){
            this.clientList = [];
        }
    }
});

Vue.component('lli-user-search', {
    template: `
			<div>
			<!--<p v-if=this.clientFlag class=form-control>{{clientObject.label}}</p>-->
			<multiselect v-if="multiple==true" v-model="clientObject" :options="this.clientList" 
	        	track-by=label label=label :allow-empty="true" :searchable=true :loading=this.isLoading
	        	id=ajax :internal-search=false :options-limit="500" :limit="15" :multiple="true"
	    		@search-change="searchClient" open-direction="bottom" @select="selectClient">
	        </multiselect>
			<multiselect v-else v-model="clientObject" :options="this.clientList" 
	        	track-by=label label=label :allow-empty="true" :searchable=true :loading=this.isLoading
	        	id=ajax :internal-search=false :options-limit="500" :limit="15" 
	    		@search-change="searchClient" open-direction="bottom" @select="selectClient">
	        </multiselect>
	        </div>
	`,
    props: ['client', 'callback', 'multiple'],
    data: function () {
        return {
            isLoading: false,
            clientList: [],
            clientFlag: true
        }
    },
    computed: {
        clientObject: {
            get() {
                return this.client;
            },
            set(val) {
                this.$emit('update:client', val);
            }
        }
    },
    methods: {
        searchClient: function (query) {
            isLoading = true;
            axios({method: 'GET', 'url': context + 'get-user.do?val=' + query})
                .then(result => {
                    this.clientList = result.data.payload;
                    isLoading = false;
                }, error => {
                });
        },
        selectClient: function (selectedOption) {
            if (typeof this.callback !== "undefined") {
                this.callback(selectedOption.ID);
            }
        }
    },
    mounted() {
        this.clientFlag = isClientLoggedIn;
        if (this.clientFlag) {
            this.clientObject = loggedInAccount;
            this.selectClient(loggedInAccount);
        }
    }
});

Vue.component('lli-zone-search', {
    template: `
			<div>
			<!--<p v-if=this.clientFlag class=form-control>{{clientObject.nameEng}}</p>-->
			<multiselect  
				v-model="clientObject" 
				:options="this.clientList" 
				label=nameEng 
				open-direction="bottom" 
				@select="selectClient"
	    	>
	        </multiselect>
	        </div>
	`,
    props: ['client', 'callback'],
    data: function () {
        return {
            isLoading: false,
            clientList: [],
            clientFlag: true
        }
    },
    computed: {
        clientObject: {
            get() {
                return this.client;
            },
            set(val) {
                this.$emit('update:client', val);
            }
        }
    },
    methods: {
        searchClient: function (query) {
            // isLoading = true;
            // axios({ method: 'GET', 'url': context + 'location/zonesearch.do?val=' + query})
            // .then(result => {
            // this.clientList = result.data.payload.elements;
            // console.log('===============================================');
            // console.log(this.clientList);
            // console.log('===============================================');
            // isLoading = false;
            // }, error clientList=> {
            // });
        },
        selectClient: function (selectedOption) {
            if (typeof this.callback !== "undefined") {
                this.callback(selectedOption.id);
            }
        }
    },
    mounted() {
        this.clientFlag = isClientLoggedIn;
        if (this.clientFlag) {
            // this.clientObject = loggedInAccount;
            // this.selectClient(loggedInAccount);
        }


        isLoading = true;
        axios({method: 'GET', 'url': context + 'location/allzonesearch.do'})
            .then(result => {
                // this.clientList = result.data.payload.elements;
                this.clientList = result.data.payload;
                // console.log('===============================================');
                // console.log(this.clientList);
                // console.log('===============================================');
                isLoading = false;
            }, error => {
            });

    }
});
// props model & url
// multiselect with ID, label
Vue.component('mounted-selection', {
    props: ['model', 'url'],
    template: `
		<multiselect v-model="modelObject" :options="modelOptions" 
        	track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom">
        </multiselect>
	`,
    computed: {
        modelObject: {
            get() {
                return this.model;
            },
            set(val) {
                this.$emit('update:model', val);
            }
        }
    },
    data: function () {
        return {
            modelOptions: []
        }
    },
    mounted() {
        axios({method: 'GET', 'url': context + this.url})
            .then(result => {
                this.modelOptions = result.data.payload;
            }, error => {
            });
    }
});

// props model & url
// multiselect with id, value
Vue.component('mounted-id-value-selection', {
    props: ['model', 'url', 'description'],
    template: `
		<multiselect v-if="description==true" v-model="modelObject" :options="modelOptions" 
        	track-by=id label=description :allow-empty="false" :searchable=true open-direction="bottom">
        </multiselect>
        <multiselect v-else v-model="modelObject" :options="modelOptions" 
        	track-by=id label=value :allow-empty="false" :searchable=true open-direction="bottom">
        </multiselect>
	`,
    computed: {
        modelObject: {
            get() {
                return this.model;
            },
            set(val) {
                this.$emit('update:model', val);
            }
        }
    },
    data: function () {
        return {
            modelOptions: []
        }
    },
    created() {
        axios({method: 'GET', 'url': context + this.url}).then(result => {
            this.modelOptions = result.data.payload;
        }, error => {
        });
    }
});


Vue.component('btcl-autocomplete', {
    template: `
		<multiselect v-model="autocompleteObject" :options="this.list" 
        	:track-by=ID label=label :allow-empty="false" :searchable=true :loading=this.isLoading
        	id=ajax :internal-search=false :options-limit="500" :limit="15" 
    		@search-change="searchObject" open-direction="bottom" @select="callback">
        </multiselect>
	`,
    props: ['model', 'url', 'selectcallback'],
    data: function () {
        return {
            isLoading: false,
            list: [],
        }
    },
    computed: {
        autocompleteObject: {
            get() {
                return this.model;
            },
            set(val) {
                this.$emit('update:model', val);
            }
        }
    },
    methods: {
        searchObject: function (query) {
            isLoading = true;
            axios({method: 'GET', 'url': context + this.url + '?val=' + query})
                .then(result => {
                    this.list = result.data.payload;
                    isLoading = false;
                }, error => {
                });
        },
        callback: function (selectedOption, id) {
            if (typeof this.selectcallback !== "undefined") {
                this.selectcallback(selectedOption.ID);
            }
        }
    }
});
Vue.component("inventory-autocomplete-pop", {
    props: {
        itemid: Number,
        fixed: Boolean,
        comment: String,
        readonly: Boolean,
        bw: Number,
        priority: Number,
        officeid: Number,
        application: Object,

    },
    template: `
		<div>
			<div class="row">
			<div class="col-md-3">
			        <select class="form-control" id="off" v-model="officeidValue">
                     <option disabled v-bind:value="0">Select Office</option> 
                     
                      <option v-bind:value="office.id" :value="office.id" v-for="office in application.officeList">
                                                                    {{office.officeName}}
                                                                </option>
                   </select> 
            </div>
			<div class="col-md-3"> 
				<multiselect v-model="selectedPop" :options="popOptions" 
		        	 label=label :allow-empty="false" :searchable=true :loading=popLoading
		        	:id="{index : 0, categoryID: 4}" :internal-search=true :options-limit="500" :limit="15" 
		    		open-direction="bottom" @select="selectInventory" placeholder="Select POP">
		        </multiselect> </div>
			<div class="col-md-3">
			 <input type="text" class="form-control" v-model="bwValue" placeholder="Comment" readonly/>
            </div>
			<div class="col-md-3">
                 <select class="form-control" id="sel1" v-model="priorityValue">
                     <option disabled value="0">Select Priority</option> 
                     <option v-bind:value="1" value="1">High</option> 
                     <option v-bind:value="2" value="2">Medium</option>
                     <option v-bind:value="3" value="3">Moderate</option>
                </select> 
            </div>
            </div>	
		</div>
		
	`,
    data: function () {
        return {
            categoryConstants: [
                {category: 4, label: "PoP"},
                {category: 5, label: "Router/Switch"},
                {category: 99, label: "Port"},
                {category: 6, label: "VLAN"},
            ],
            popOptions: [], routerOptions: [], portOptions: [], vlanOptions: [],
            selectedPop: undefined, selectedRouter: undefined, selectedPort: undefined, selectedVLAN: undefined,
            popLoading: false, routerLoading: false, portLoading: false, vlanLoading: false,
        }
    },
    computed: {

        commentValue: {
            get() {
                return this.comment;
            },
            set(val) {
                this.$emit('update:comment', val);
            }
        },
        bwValue: {
            get() {
                return this.bw;
            },
            set(val) {
                this.$emit('update:bw', val);
            }
        },
        priorityValue: {
            get() {
                return this.priority;
            },
            set(val) {
                this.$emit('update:priority', val);
            }
        },
        officeidValue: {
            get() {
                return this.officeid;
            },
            set(val) {
                this.$emit('update:officeid', val);
            }
        }

    },
    methods: {

        searchInventory: function (searchQuery, id) {
        },
        selectInventory: function (selectedOption, id, keepChildren) {
            var childCategoryID = id.index !== 3 ? this.categoryConstants[id.index + 1].category : false;
            if (childCategoryID) {
                switch (childCategoryID) {
                    case 5:
                        this.routerLoading = true;
                        break;
                    case 99:
                        this.portLoading = true;
                        break;
                    case 6:
                        this.vlanLoading = true;
                        break;
                }
                axios({
                    method: "GET",
                    "url": context + "lli/inventory/get-inventory-options.do?categoryID=" + childCategoryID + "&parentID=" + selectedOption.ID
                })
                    .then(result => {
                        switch (childCategoryID) {
                            case 5:
                                this.routerOptions = result.data.payload;
                                if (!keepChildren) {
                                    this.portOptions = [];
                                    this.vlanOptions = [];
                                    this.selectedRouter = undefined;
                                    this.selectedPort = undefined;
                                    this.selectedVLAN = undefined;
                                }
                                this.routerLoading = false;
                                break;
                            case 99:
                                this.portOptions = result.data.payload;
                                if (!keepChildren) {
                                    this.vlanOptions = [];
                                    this.selectedPort = undefined;
                                    this.selectedVLAN = undefined;
                                }
                                this.portLoading = false;
                                break;
                            case 6:
                                this.vlanOptions = result.data.payload;
                                if (!keepChildren) {
                                    this.selectedVLAN = undefined;
                                }
                                this.vlanLoading = false;
                                break;
                        }
                        if (!keepChildren) {
                            this.$emit('update:itemid', selectedOption.ID);
                        }
                    }, error => {
                    });
            } else {
                this.$emit('update:itemid', selectedOption.ID);
            }
        },
        refreshInventoryPath: function () {
            axios({
                method: "GET",
                "url": context + "lli/inventory/get-inventory-details-upto-pop.do?inventoryID=" + this.itemid
            })
                .then(result => {
                    var itemsUptoPop = result.data.payload;

                    if (typeof itemsUptoPop['4'] !== 'undefined') {/*pop*/
                        this.selectInventory({ID: itemsUptoPop['4'].ID, label: itemsUptoPop['4'].name}, {
                            index: 0,
                            categoryID: 4
                        }, true);
                        this.selectedPop = {ID: itemsUptoPop['4'].ID, label: itemsUptoPop['4'].name};
                    }
                    if (typeof itemsUptoPop['5'] !== 'undefined') {/*router*/
                        this.selectInventory({ID: itemsUptoPop['5'].ID, label: itemsUptoPop['5'].name}, {
                            index: 1,
                            categoryID: 5
                        }, true);
                        this.selectedRouter = {ID: itemsUptoPop['5'].ID, label: itemsUptoPop['5'].name};
                    }
                    if (typeof itemsUptoPop['99'] !== 'undefined') {/*port*/
                        this.selectInventory({ID: itemsUptoPop['99'].ID, label: itemsUptoPop['99'].name}, {
                            index: 2,
                            categoryID: 99
                        }, true);
                        this.selectedPort = {ID: itemsUptoPop['99'].ID, label: itemsUptoPop['99'].name};
                    }
                    if (typeof itemsUptoPop['6'] !== 'undefined') {/*vlan*/
                        this.selectInventory({ID: itemsUptoPop['6'].ID, label: itemsUptoPop['6'].name}, {
                            index: 3,
                            categoryID: 6
                        }, true);
                        this.selectedVLAN = {ID: itemsUptoPop['6'].ID, label: itemsUptoPop['6'].name};
                    }
                }, error => {
                });
        }
    },
    mounted() {
        axios({method: "GET", "url": context + "lli/inventory/get-inventory-options.do?categoryID=" + 4})
            .then(result => {
                this.popOptions = result.data.payload;
            }, error => {
            });
        if (typeof this.itemid !== 'undefined') {
            this.refreshInventoryPath();
        }
    },
    watch: {
        itemid: function () {
            if (this.readonly) {
                this.refreshInventoryPath();
            }
        }
    }
});

Vue.component('btcl-file-upload', {
    template: `
		<btcl-portlet>
        <div class="table-responsive" v-show="files.length > 0">
            <table class="table">
                <thead>
                <tr>
                    <th>#</th>
                    <th>Name</th>
                    <th>Download Link</th>
                    <th>Size</th>
                    <th>Progress</th>
                    <th>Status</th>
                    <th>Delete</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="(file, fileIndex) in files">
                    <td>{{fileIndex+1}}</td>
                    <td>{{file.name}}</td>
                    <td v-if="typeof file.response.payload !== 'undefined'">
                        <button v-on:click="downloadFile(file)">Download</button>
                        
                    <td v-else>N/A</td>
                    <td>{{isNaN(file.size) ? "--" : (Number.isInteger(file.size/(1024*1024)) ? file.size/(1024*1024) :
                        (file.size/(1024*1024)).toFixed(3) ) + ' Mb'}}
                    </td>
                    <td>{{parseInt(file.progress) + "%"}}</td>
                    <td>{{file.success ? "Complete" : "Pending"}}</td>
                    <td v-if="typeof file.response.payload !== 'undefined'">
                        <button v-on:click="deleteFile(file)">Delete</button>
                        
                    <td v-else>N/A</td>
                </tr>

                </tbody>
            </table>
        </div>
        <div class=row>
            <div class="col-md-3 col-md-offset-6">
                <vue-upload-component
                        ref="upload"
                        v-model="files"
                        :post-action="params['fileUploadURL']"
                        :multiple=true
                        :data='params'
                        accept="image/png,image/gif,image/jpg,image/jpeg,application/pdf"
                        extensions="png,gif,jpg,jpeg,pdf"
                        :thread="5"
                        @input-filter="inputFilter"
                        class="btn btn-block btn-default">
                    Select Files
                </vue-upload-component>
            </div>
            <div class=col-md-3>
                <button  class="btn btn-block green-haze"
                        v-show="!$refs.upload || !$refs.upload.active"
                        @click.prevent="$refs.upload.active = true" type="button">
                    Start upload
                </button>
            </div>
        </div>
    </btcl-portlet>
	`,

    props: ['params'],
    data: function () {
        return {
            files: [],
            fileNameArray: [],
            filesFromDB: []
        }
    },
    watch: {
        files: {
            deep: true,
            handler: function (val) {
                this.fileNameArray = [];
                this.files.forEach(function (currentValue, index, array) {
                    this.fileNameArray.push(currentValue.name);
                }, this);
            }
        }
    },
    methods: {
        remove: function (file) {
            this.$refs.upload.remove(file);
        },
        inputFilter(newFile, oldFile, prevent) {
            if (newFile && !oldFile) {
                if (/(\/|^)(Thumbs\.db|desktop\.ini|\..+)$/.test(newFile.name)) {
                    return prevent();
                }
                if (/\.(php5?|html?|jsx?)$/i.test(newFile.name)) {
                    return prevent();
                }
                if (!(/\.(png?|gif?|jpg?|jpeg?|pdf?)$/i.test(newFile.name))) {
                    toastr.error(newFile.name + " has invalid file extension");
                    return prevent();
                }
            }
            if (newFile && oldFile) {
                //return prevent();
            }
            if (!newFile && oldFile) {
                // alert("removed");
                console.log('remove', oldFile)
            }
        },

        deleteFile: function (file) {
            let  url;
            if(file.response.payload.elements)url = file.response.payload.elements[0].deleteUrl;
            else url = file.response.payload[0].deleteUrl;
            // var url = file.response.payload.elements[0].deleteUrl;
            axios({method: 'GET', 'url': context + url})
                .then(result => {
                    var index = this.files.indexOf(file);
                    this.files.splice(index, 1);
                    // alert("removed");
                    console.log(result);
                }, error => {
                });

        },
        downloadFile: function (file) {
            let url;
            if(file.response.payload.elements) url = file.response.payload.elements[0].downloadUrl;
            else url = file.response.payload[0].downloadUrl;
            // var url = file.response.payload.elements[0].downloadUrl;
            window.location.href = context + url;

        }


    },
    mounted: function () {
        axios.post(context + "file/get-db-files.do", {
            'moduleId': this.params.moduleId,
            'applicationId': this.params.applicationId,
            'componentId': this.params.componentId,
            'stateId': this.params.stateId

        }).then(res => {
            if (res.data.responseCode == 1) {
                this.filesFromDB = res.data.payload;
                this.filesFromDB.map(r => {
                    let newFileObject = {};
                    newFileObject.name = r.realName;
                    newFileObject.id = r.id;
                    newFileObject.success = true;
                    newFileObject.size = r.size;
                    newFileObject.progress = "100%";
                    newFileObject.response = {

                        payload: {
                            elements: [
                                {
                                    'downloadUrl': 'file/download.do?id=' + r.id,
                                    'deleteUrl': 'file/delete.do?id=' + r.id
                                }
                            ]
                        }
                    },
                        this.files.push(newFileObject);

                });
            } else {
                toastr.error(res.data.msg, "Failure");
            }
        }).catch(err => {
            console.log(err);
        })
    }
});


Vue.component('btcl-uploaded-file-view', {
    template: `
		<btcl-portlet title=" Uploaded Files" v-show="files.length > 0">
        <div class="table-responsive" >
            <table class="table">
                <thead>
                <tr>
                    <th>#</th>
                    <th>Name</th>
                    <th>Download Link</th>
                    <th>Size</th>

                </tr>
                </thead>
                <tbody>
                <tr v-for="(file, fileIndex) in files">
                    <td>{{fileIndex+1}}</td>
                    <td>{{file.name}}</td>
                    <td v-if="typeof file.response.payload !== 'undefined'">
                        <button v-on:click="downloadFile(file)">Download</button>
                        
                    <td v-else>N/A</td>
                    <td>{{isNaN(file.size) ? "--" : (Number.isInteger(file.size/(1024*1024)) ? file.size/(1024*1024) :
                        (file.size/(1024*1024)).toFixed(3) ) + ' Mb'}}
                    </td>
                </tr>

                </tbody>
            </table>
        </div>
    </btcl-portlet>
	`,

    props: ['params'],
    data: function () {
        return {
            files: [],
            fileNameArray: [],
            filesFromDB: []
        }
    },
    watch: {
        files: {
            deep: true,
            handler: function (val) {
                this.fileNameArray = [];
                this.files.forEach(function (currentValue, index, array) {
                    this.fileNameArray.push(currentValue.name);
                }, this);
            }
        }
    },
    methods: {
        downloadFile: function (file) {
            let url;
            if(file.response.payload.elements) url = file.response.payload.elements[0].downloadUrl;
            else url = file.response.payload[0].downloadUrl;
            // var url = file.response.payload.elements[0].downloadUrl;
            window.location.href = context + url;

        }
    },
    mounted: function () {
        axios.post(context + "file/get-db-files-by-applicationId.do", {
            'applicationId': this.params.applicationId,
        }).then(res => {
            if (res.data.responseCode == 1) {
                this.filesFromDB = res.data.payload;
                this.filesFromDB.map(r => {
                    let newFileObject = {};
                    newFileObject.name = r.realName;
                    newFileObject.id = r.id;
                    newFileObject.success = true;
                    newFileObject.size = r.size;
                    newFileObject.progress = "100%";
                    newFileObject.response = {

                        payload: {
                            elements: [
                                {
                                    'downloadUrl': 'file/download.do?id=' + r.id,
                                    'deleteUrl': 'file/delete.do?id=' + r.id
                                }
                            ]
                        }
                    },
                        this.files.push(newFileObject);

                });
            } else {
                toastr.error(res.data.msg, "Failure");
            }
        }).catch(err => {
            console.log(err);
        })
    }
});


Vue.component('btcl-file-upload-upstream', {
    template: `

<div class="portlet light bordered" style="border: 4px solid #44b6ae!important;">
            <div class="portlet-body">
            	

		
        <div class="table-responsive" v-if="files.length > 0">
            <table class="table">
                <thead>
                <tr>
                    <th>#</th>
                    <th>Name</th>
                    <th>Download Link</th>
                    <th>Size</th>
                    
                    <template v-if = "params.isAllowed"> 
                    <th>Progress</th>
                    <th>Status</th>
                    <th>Delete</th>
                    </template>
                </tr>
                </thead>
                <tbody>
                <tr v-for="(file, fileIndex) in files">
                    <td>{{fileIndex+1}}</td>
                    <td>{{file.name}}</td>
                    <td v-if="typeof file.response.payload !== 'undefined'">
                        <button v-on:click="downloadFile(file)">Download</button>
                        
                    <td v-else>N/A</td>
                    <td>{{isNaN(file.size) ? "--" : (Number.isInteger(file.size/(1024*1024)) ? file.size/(1024*1024) :
                        (file.size/(1024*1024)).toFixed(3) ) + ' Mb'}}
                    </td>
                    <template v-if = "params.isAllowed"> 
                    
                    <td>{{parseInt(file.progress) + "%"}}</td>
                    <td>{{file.success ? "Complete" : "Pending"}}</td>
                    <td v-if="(typeof file.response.payload !== 'undefined')">
                        <button v-on:click="deleteFile(file)">Delete</button>
                        
                        
                    <td v-else>N/A</td>
                    
                    </template>
                </tr>

                </tbody>
            </table>
        </div>
        <div v-else>
            <p style="text-align: center;"> No File Found.</p>
        </div>
        <div class=row>
        <template v-if = "params.isAllowed"> 
            <div class="col-md-3 col-md-offset-6">
                          
             <vue-upload-component
                        ref="upload"
                        v-model="files"
                        :post-action="params['fileUploadURL']"
                        :multiple=true
                        :data='params'
                        accept="image/png,image/gif,image/jpg,image/jpeg,application/pdf"
                        extensions="png,gif,jpg,jpeg,pdf"
                        :thread="5"
                        @input-filter="inputFilter"
                        class="btn btn-block btn-default">
                    Select Files
              </vue-upload-component>

            </div>
            <div class=col-md-3>
 
                <button  class="btn btn-block green-haze"
                        v-show="!$refs.upload || !$refs.upload.active"
                        @click.prevent="$refs.upload.active = true" type="button">
                    Start upload 
                </button>
             
            </div>
            
            </template>
              
        </div>
      
   
            </div>
            
		</div>
	`,

    props: ['params'],
    data: function () {
        return {
            files: [],
            fileNameArray: [],
            filesFromDB: []
        }
    },
    watch: {
        files: {
            deep: true,
            handler: function (val) {
                this.fileNameArray = [];
                this.files.forEach(function (currentValue, index, array) {
                    this.fileNameArray.push(currentValue.name);
                }, this);
            }
        }
    },
    methods: {
        remove: function (file) {
            this.$refs.upload.remove(file);
        },
        inputFilter(newFile, oldFile, prevent) {
            if (newFile && !oldFile) {
                if (/(\/|^)(Thumbs\.db|desktop\.ini|\..+)$/.test(newFile.name)) {
                    return prevent();
                }
                if (/\.(php5?|html?|jsx?)$/i.test(newFile.name)) {
                    return prevent();
                }
                if (!(/\.(png?|gif?|jpg?|jpeg?|pdf?)$/i.test(newFile.name))) {
                    toastr.error(newFile.name + " has invalid file extension");
                    return prevent();
                }
            }
            if (newFile && oldFile) {
                //return prevent();
            }
            if (!newFile && oldFile) {
                // alert("removed");
                console.log('remove', oldFile)
            }
        },

        deleteFile: function (file) {

            let  url;
            if(file.response.payload.elements)url = file.response.payload.elements[0].deleteUrl;
            else url = file.response.payload[0].deleteUrl;

            axios({method: 'GET', 'url': context + url})
                .then(result => {
                    var index = this.files.indexOf(file);
                    this.files.splice(index, 1);
                    // alert("removed");
                    console.log(result);
                }, error => {
                });

        },
        downloadFile: function (file) {
            let url;
            if(file.response.payload.elements) url = file.response.payload.elements[0].downloadUrl;
            else url = file.response.payload[0].downloadUrl;
            window.location.href = context + url;
        }
    },
    mounted: function () {
        axios.post(context + "file/get-db-files-by-applicationId.do", {
            'applicationId': this.params.applicationId,
        }).then(res => {
            if (res.data.responseCode == 1) {
                this.filesFromDB = res.data.payload;
                this.filesFromDB.map(r => {
                    let newFileObject = {};
                    newFileObject.name = r.realName;
                    newFileObject.id = r.id;
                    newFileObject.success = true;
                    newFileObject.size = r.size;
                    newFileObject.progress = "100%";
                    newFileObject.response = {

                        payload: {
                            elements: [
                                {
                                    'downloadUrl': 'file/download.do?id=' + r.id,
                                    'deleteUrl': 'file/delete.do?id=' + r.id
                                }
                            ]
                        }
                    },
                        this.files.push(newFileObject);

                });
            } else {
                toastr.error(res.data.msg, "Failure");
            }
        }).catch(err => {
            console.log(err);
        })
    }
});

