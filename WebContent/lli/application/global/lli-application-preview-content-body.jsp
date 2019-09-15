<div id=btcl-application>
	<btcl-body title="Preview Processed Content">
		
		<div v-if="Object.keys(content).length == 0 || typeof content.CONTENTTYPE == 'undefined'">
			Either the processing has not started yet or this application does not need further processing.<br>Thank You!<br>
		</div>
		<lli-connection-view v-else-if="content.CONTENTTYPE == 'connection'" :lliconnection=content readonly></lli-connection-view>
		<div class=row v-else-if="content.CONTENTTYPE == 'two-connection'">
	       	<div class=col-md-6>
	       		<lli-connection-view :lliconnection=content.sourceConnection readonly></lli-connection-view>
	       	</div>
	       	<div class=col-md-6>
	       		<lli-connection-view :lliconnection=content.destinationConnection readonly></lli-connection-view>
	       	</div>
		</div>
		<div v-else>Processing is complete.</div>

	</btcl-body>
</div>
<script src=../connection/lli-connection-components.js></script>
<script>
var applicationID = '<%=request.getParameter("id")%>';
var vue = new Vue({
	el: "#btcl-application",
	data: {
		contextPath: context,
		content: {}
	},
	mounted() {
		axios({ method: 'GET', 'url': context + 'lli/application/new-connection-get.do?id=' + applicationID})
		.then(result => {
			this.content = JSON.parse(result.data.payload.content);
		}, error => {
		});
	}
});
</script>
