<div id="btcl-application" v-cloak="true">
	<btcl-body title="Reconnect" subtitle="Application">
		<%--<btcl-form :action="contextPath + 'lli/application/reconnect.do'" :name="['application']" :form-data="[application]" :redirect="goView">--%>
        	<btcl-portlet>
           		<btcl-field title="Client">
					<lli-client-search :client.sync="revise.client"></lli-client-search>
           		</btcl-field>         		
           		
           		<btcl-input title="Description" :text.sync="revise.description"></btcl-input>
           		<btcl-input title="Comment" :text.sync="revise.comment"></btcl-input>
				<btcl-field title="Suggested Date"><btcl-datepicker :date.sync="revise.suggestedDate"></btcl-datepicker></btcl-field>

				<div style="padding-top: 30px">
					<button  type="submit" class="btn green-haze btn-block" @click="submitData">Submit</button>
				</div>
           	</btcl-portlet>
           	
		<%--</btcl-form>--%>
	</btcl-body>
</div>

<script src="../connection/lli-connection-components.js"></script>
<script src=lli-application-components.js></script>
<%--<script src=reconnect/nix-application-reconnect.js></script>--%>


<script>
    var vue = new Vue({
        el: "#btcl-application",
        data: {
            revise: {}
        },
        methods: {
            submitData: function () {
                var url = "reconnectrequest";
                axios.post(context + 'lli/revise/' + url + '.do', {'data': JSON.stringify(this.revise)})
                    .then(result => {
                        if (result.data.responseCode == 2) {
                            toastr.error(result.data.msg);
                        } else if (result.data.responseCode == 1) {
                            toastr.success("Your request has been processed", "Success");
                            window.location.href = context + 'lli/revise/search.do';
                        }
                        else {
                            toastr.error("Your request was not accepted", "Failure");
                        }
                    })
                    .catch(function (error) {
                    });
            },
        }
    });

</script>


