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
		<btcl-body title="NIX Close" subtitle="Application">
			<btcl-portlet>
				<btcl-field required="true" title="Client">
					<user-search-by-module
							:client.sync="application.client"
							:module="9"
							:callback="clientSelectionCallback">Client
					</user-search-by-module>
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

				<btcl-field required="true" title="Office">
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

				<btcl-input v-if="exitingPortSelected" title="Existing Port Type"
							:text.sync="application.existingPortType">
				</btcl-input>
				<btcl-input title="Description"
							:text.sync="application.description">
				</btcl-input>

				<btcl-input  required="true" title="Comment"
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

<script src="close/nix-close.js"></script>


