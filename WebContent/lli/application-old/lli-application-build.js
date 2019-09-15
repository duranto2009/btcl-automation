Vue.component('application-builder', {
	props: ['localform'],
	template: `
		<div>
			<btcl-portlet title="Basic Information">
				<btcl-input title="Name" :text.sync="localform.name"></btcl-input>
			</btcl-portlet>
			<btcl-portlet title="Form Fields">
				<btcl-input v-for="(field, fieldIndex) in localform.fields" title="Field Name" :text.sync="field.title"></btcl-input>
				<button type=button class="btn btn-default btn-block" @click="addField">Add Field</button>
			</btcl-portlet>
		</div>
	`,
	methods: {
		addField: function(){
			this.localform.fields.push({});
		}
	}
});

var vue = new Vue({
	el: "#btcl-application",
	data: {
		contextPath: context,
		form: {
			name: "",
			fields: []
		}
	}
});