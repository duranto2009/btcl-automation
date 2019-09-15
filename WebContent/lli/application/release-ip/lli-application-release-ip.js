var vue = new Vue({
	el: "#btcl-application",
	data: {
		contextPath: context,
		
		application: {
		}
	},
	computed: {
		content: function(){
			var temp = {
				ID: 0,
				name: '',
				client: this.application.client,
				gbBandwidth: 0,
				mbBandwidth: this.application.bandwidth,
				connectionType: this.application.connectionType,
				officeList: [
					{
						ID: 0,
						name: '',
						address: this.application.address,
						localLoopList: []
					}
				],
				incident: {ID:1, label:'Shift Address'},
				CONTENTTYPE: 'connection'
			};
			return JSON.stringify(temp);
		}
	}
});