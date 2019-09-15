Vue.component('application-actions', {
	template: `
		<div class=row>
			<div class="col-md-3 col-md-offset-1" style="margin-bottom:20px;" v-for="action in this.actionList">
				<button type=button class="btn btn-block green-haze" @click=submit(action)>{{action.title}}</button>
			</div>
		</div>
	`,
	props: ['appid'],
	mounted() {
		axios({ method: 'GET', 'url': context + 'lli/application/get-actions.do?id=' + this.appid})
		.then(result => {
			this.actionList = result.data.payload;
		}, error => {
		});
	},
	data: function(){
		return {
			actionList: []
		}
	},
	methods: {
		submit: function(action){
			if(!action.isForward){
				axios({ method: 'GET', 'url': context + action.url + '?id=' + this.appid})
				.then(result => {
					if(result.data.responseCode == 2) {
						toastr.error(result.data.msg);
					}else if(result.data.responseCode == 1){
						toastr.success("Your request has been processed", "Success");
						location.reload();
					}
					else{
						toastr.error("Your request was not accepted", "Failure");
					}
				}, error => {
				});
			} else {
				window.location.href = context + action.url + '?id=' + this.appid;
			}
			
		}
	}
});