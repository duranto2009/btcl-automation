<%@page import="file.FileTypeConstants"%>
<%@page import="common.EntityTypeConstant"%>
<%
	String context = "../.." + request.getContextPath() + "/";
%>
<div id=btcl-application v-cloak="true">
	<btcl-body title="New Connection" subtitle="Application" :loader="loading">
			<btcl-portlet>
			<%--	<btcl-field title="Client">
					<lli-client-search :client.sync="application.client">Client</lli-client-search>
				</btcl-field>--%>
				<btcl-field title="Client" :required="true">
					<user-search-by-module :client.sync="application.client" :module="9">Client
					</user-search-by-module>
				</btcl-field>

<%--				<btcl-field v-if="application.client.registrantType==1"--%>
<%--							title="Want's to Pay DN(Connection Charge)">--%>
<%--					<multiselect v-model="application.skipPayment"--%>
<%--								 :options="[{ID:1,label:'First Month Bill'},{ID:0,label:'Before Connection'}]"--%>
<%--								 :track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom">--%>
<%--					</multiselect>--%>
<%--				</btcl-field>--%>

				<btcl-field title="Loop Provider" required="true">
					<multiselect v-model="application.loopProvider" :options="[{ID:1,label:'BTCL'},{ID:2,label:'Client'}]"
								 :track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom">
					</multiselect>
				</btcl-field>

				<btcl-field title="Zone" required="true">
					<lli-zone-search :client.sync="application.zone">Zone</lli-zone-search>
				</btcl-field>

				<btcl-field title="Port Type" required="true">
					<multiselect v-model="application.portType" :options="[{ID:1,label:'FE'},{ID:2,label:'GE'},{ID:3,label:'10GE'}]"
								 :track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom">
					</multiselect>
				</btcl-field>

				<btcl-input required="true" title="Port Count" :text.sync="application.portCount" :number="true"></btcl-input>
				<%--<btcl-input title="Description" :text.sync="application.description"></btcl-input>--%>
				<btcl-input required="true" title="Comment" :text.sync="application.comment"></btcl-input>
				<btcl-field title="Suggested Date" required="true">
					<btcl-datepicker :date.sync="application.suggestedDate" pattern="DD-MM-YYYY"></btcl-datepicker>
				</btcl-field>
				<btcl-bounded v-for="(office,officeIndex) in application.officeList" :title="'Office '+(officeIndex+1)"
							  :key="officeIndex">
					<btcl-input required="true" title="Office Name" :text.sync="office.officeName"></btcl-input>
					<btcl-input required="true"  title="Office Address" :text.sync="office.officeAddress"></btcl-input>
<%--					<div align=right v-if="officeIndex!=0">--%>
<%--						<button type=button--%>
<%--								@click="deleteOffice(application,officeIndex, $event)"--%>
<%--								class="btn red btn-outline">--%>
<%--							Remove this Office--%>
<%--						</button>--%>
<%--					</div>--%>
				</btcl-bounded>
<%--				<div align=right>--%>
<%--					<button type=button--%>
<%--							@click="addOffice(application,  $event)"--%>
<%--							class="btn green-haze btn-outline">--%>
<%--						Add More Office--%>
<%--					</button>--%>
<%--				</div>--%>
<%--				<btcl-field>--%>
<%--					<div align="right">--%>
<%--						<button type="submit" class="btn green-haze" v-on:click="submitFormData" >Submit</button>--%>
<%--					</div>--%>
<%--				</btcl-field>--%>

				<btcl-bounded>
					<button type=button class="btn green-haze btn-block btn-lg" @click="submitFormData" :disabled="submitDisable">Submit</button>
				</btcl-bounded>

			</btcl-portlet>
	</btcl-body>


</div>

<script src="new.js"></script>


