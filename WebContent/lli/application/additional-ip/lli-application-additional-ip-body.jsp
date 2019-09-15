<div id=btcl-application v-cloak="true">

	<btcl-body title="Additional IP" subtitle="Application" :loader="loading">
		<%--<btcl-form :action="contextPath + 'lli/application/additional-ip.do'" :name="['application','content']" :form-data="[application,content]">--%>
			<btcl-portlet>
				<btcl-field title="Client" required ="true">
					<lli-client-search :client.sync="application.client" :callback="clientSelectionCallback"></lli-client-search>
				</btcl-field>

				<btcl-field title="Connection" required="true">
					<multiselect v-model="application.connection" :options="connectionOptionListByClientID"
								 track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom">
								 <%--@select="connectionSelectionCallback">--%>
					</multiselect>
				</btcl-field>

				<btcl-field title="IP Count" required="true">
					<input type=number class="form-control" v-model=application.ipCount>
				</btcl-field>

				<btcl-field title="Suggested Date" required="true">
					<btcl-datepicker :date.sync="application.suggestedDate"></btcl-datepicker>
				</btcl-field>
				<btcl-input title="Comment" :text.sync="application.comment"></btcl-input>

				<btcl-input title="Description" :text.sync="application.description"></btcl-input>

				<div style="padding-top:30px;">
					<button type="submit" class="btn green-haze btn-block" @click="submitData">Submit</button>
				</div>

			</btcl-portlet>

		<%--</btcl-form>--%>
	</btcl-body>
</div>

<script src="../connection/lli-connection-components.js"></script>
<script src=lli-application-components.js></script>
<script src=additional-ip/lli-application-additional-ip.js></script>


