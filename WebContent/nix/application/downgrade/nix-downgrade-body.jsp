<%@page import="file.FileTypeConstants"%>
<%@page import="common.EntityTypeConstant"%>
<%
	String context = "../.." + request.getContextPath() + "/";
%>
<div id=btcl-application>
	<div class="row" v-if="loading" style="text-align: center">
		<i class="fa fa-spinner fa-spin fa-5x"></i>
	</div>
	<div v-else>
		<btcl-body title="NIX Downgrade" subtitle="Application">
			<btcl-portlet>
				<btcl-field required="true" title="Client">
					<user-search-by-module
							:client.sync="application.client"
							:module="9"
							:callback="clientSelectionCallback">Client
					</user-search-by-module>
				</btcl-field>

				<btcl-field v-if="application.client.registrantType==1"
							title="Want's to Pay DN(Connection Charge)"
							required="true">
					<multiselect v-model="application.skipPayment"
								 :options="[{ID:1,label:'First Month Bill'},{ID:0,label:'Before Connection'}]"
								 :track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom">
					</multiselect>
				</btcl-field>

				<btcl-field required="true" title="Connection">
					<multiselect v-model="application.connection"
								 :options="nixConnections"
								 :track-by=ID
								 label=label
								 :allow-empty="false"
								 :searchable=true
								 open-direction="bottom"
								 @select="connectionSelectionCallback">
					</multiselect>
				</btcl-field>

				<btcl-field required="true"  title="Office">
					<multiselect v-model="application.office"
								 :options="nixOffices"
								 :track-by=ID
								 label=label
								 :allow-empty="false"
								 :searchable=true
								 open-direction="bottom"
								 @select="officeSelectionCallback">
					</multiselect>
				</btcl-field>

				<%--todo a selection call back method for new port type selection--%>
				<btcl-field title="Existing Ports">
					<multiselect v-model="application.oldPort"
								 :options="nixExistingPorts"
								 :track-by=ID
								 label=label
								 :allow-empty="false"
								 :searchable=true
								 open-direction="bottom"
								 @select="portSelectionCallback">
					</multiselect>
				</btcl-field>
				<btcl-info v-if="exitingPortSelected" title="Existing Port Type"
							:text.sync="application.existingPortType">
				</btcl-info>
				<%--<btcl-field title="Loop Provider">
					<multiselect v-model="application.loopProvider"
								 :options="[{ID:1,label:'BTCL'},{ID:2,label:'Client'}]"
								 :track-by=ID
								 label=label
								 :allow-empty="false"
								 :searchable=true
								 open-direction="bottom">
					</multiselect>
				</btcl-field>--%>

				<btcl-field required="true" title="New Port Type">
					<multiselect v-model="application.newPortType"
								 :options="portTypeDropDown"
								 :track-by=ID
								 label=label
								 :allow-empty="false"
								 :searchable=true
								 open-direction="bottom">
					</multiselect>
				</btcl-field>


				<btcl-input title="Description"
							:text.sync="application.description">
				</btcl-input>

				<btcl-input required="true" title="Comment"
							:text.sync="application.comment">
				</btcl-input>
				<btcl-field required="true" title="Suggested Date">
					<btcl-datepicker :date.sync="application.suggestedDate"
									 pattern="DD-MM-YYYY">
					</btcl-datepicker>
				</btcl-field>
				<btcl-field>
					<div align="right">
						<button type="submit"
								class="btn green-haze"
								v-on:click="submitFormData" >Submit
						</button>
					</div>
				</btcl-field>
			</btcl-portlet>
		</btcl-body>
	</div>
</div>

<script src="downgrade/nix-downgrade.js"></script>


