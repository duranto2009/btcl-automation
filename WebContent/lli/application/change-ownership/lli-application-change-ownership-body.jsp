<div id="btcl-application" v-cloak="true">

	<btcl-body title="Change Ownership" subtitle="Application" :loader="loading">
		<%--<btcl-form :action="contextPath + 'lli/application/change-lli.Application.ownership.do'" :name="['application']" :form-data="[application]" :redirect="goView">--%>
			<btcl-portlet>
				<btcl-field title="Source Client" :required="true">
					<lli-client-search :client.sync="application.srcClient"
									   :callback="clientSelectionCallback">
					</lli-client-search>
				</btcl-field>



				<btcl-field title="Destination Client" :required="true">
					<multiselect v-model="application.dstClient"
								 :options="destinationClientList"
								 <%--track-by=ID--%>
								 label=label
								 @search-change="searchDestinationClient"
								 :allow-empty="false"
								 :searchable=true
								 open-direction="bottom">
					</multiselect>
					<%--<lli-client-search :client.sync="application.dstClient">Destination Client
					</lli-client-search>--%>
				</btcl-field>


	<%--			<btcl-field title="Connection">
					<multiselect v-model="application.connection"
								 :options="connectionOptionListByClientID"
								 track-by=ID
								 label=label :allow-empty="false" :searchable=true open-direction="bottom"
								 @select="connectionSelectionCallback">
					</multiselect>
				</btcl-field>--%>
				<div v-if="connectionOptionListByClientID.length>0">
					<btcl-field title ="Active Connections" :required="true">
						<select class="form-control"
								v-model="connection"
								v-on:change="connectionSelectionCallback(connection)"
								style="margin-top: 7px;">
							<option value="0">Select Connection
							</option>
							<option v-bind:value="connection"
									v-for="connection in connectionOptionListByClientID">
								{{connection.label}}
								<template v-if="connection.label!==''">({{connection.label}})</template>
							</option>
						</select>
					</btcl-field>

				</div>

				<div v-if="application.selectedConnectionList.length>0">
					<btcl-field title ="Selected Connections">

						<div style="margin-top: 2px;border: 1px solid;"
							 class="form-control"
							 v-for="(selectedConnection,index) in application.selectedConnectionList">
							<div class="row">
								<div class="col-sm-10">
										{{selectedConnection.label}}
								</div>
								<div class="col-sm-2">
									<button  class="pull-right btn-primary" @click="removeSelectedConnection(index)"
											 type="button" >x</button>
								</div>
							</div>
						</div>

					</btcl-field>

				</div>


				<btcl-input title="Description"
							:text.sync="application.description">
				</btcl-input>
				<btcl-input title="Comment"
							:text.sync="application.comment">
				</btcl-input>

				<btcl-field title="Suggested Date">
					<btcl-datepicker :date.sync="application.suggestedDate"></btcl-datepicker>
				</btcl-field>

				<div style="padding-top:30px;">
					<button type="submit" class="btn green-haze btn-block" @click="submitFormData" :disable="disabled">Submit</button>
				</div>
			</btcl-portlet>

		<%--</btcl-form>--%>
	</btcl-body>
</div>

<script src="../connection/lli-connection-components.js"></script>
<script src=../application/change-ownership/lli-application-change-ownership.js></script>

