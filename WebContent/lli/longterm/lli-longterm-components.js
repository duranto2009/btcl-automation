Vue.component('btcl-longterm-view', {
	template: `
		<btcl-portlet :title=this.longterm.contractStartDate>
			
		</btcl-portlet>
	`,
	props: ['id'],
	data: function(){
		return {
			longterm: {}
		}
	},
	mounted: function(){
		axios({ method: "GET", "url": context + "lli/longterm/get-longterm.do?id="+this.id}).then(result => {
            this.longterm = result.data.payload;
        }, error => {
            window.reload();
        });
	}
});