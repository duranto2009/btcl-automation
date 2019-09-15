<div id=btcl-application>
	<btcl-body title="Request For Correction">
		<btcl-form :action="contextPath + 'lli/application/request-for-correction.do'" :name="['application']" :form-data="[application]" :redirect="goView">
        	<btcl-portlet>
           		<btcl-input title="Correction Reason" :text.sync="application.requestForCorrectionComment"></btcl-input>
           	</btcl-portlet>
           	
		</btcl-form>
	</btcl-body>
</div>

<script>
var applicationID = '<%=request.getParameter("id")%>';
var vue = new Vue({
	el: "#btcl-application",
	data: {
		contextPath: context,
		application: {}
	},
	mounted() {
		axios({ method: 'GET', 'url': context + 'lli/application/new-connection-get.do?id=' + applicationID})
		.then(result => {
			this.application = result.data.payload;
		}, error => {
		});
	},
	methods: {
		goView: function(result){
			if(result.data.responseCode == 2) {
				toastr.error(result.data.msg, "Failure");
			}else if(result.data.responseCode == 1){
				toastr.success("Your request has been processed", "Success");
				window.location.href = context + 'lli/application/view.do?id=' + result.data.payload;
			}
			else{
				toastr.error("Your request was not accepted", "Failure");
			}
		}
	}
});
</script>
