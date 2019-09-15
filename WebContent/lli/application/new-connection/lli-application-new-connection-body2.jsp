<%@page import="file.FileTypeConstants"%>
<%@page import="common.EntityTypeConstant"%>

<div id=btcl-application>
	<btcl-body title="New Connection" subtitle="Application">
		<btcl-form :action="contextPath + 'lli/application/new-connection.do'" :name="['application']" :form-data="[application]" :redirect="goView" id="fileupload" enctype="multipart/form-data">
        	<btcl-portlet>
           		<btcl-field title="Client">
					<lli-client-search :client.sync="application.client">Client</lli-client-search>           		
           		</btcl-field>
           		<btcl-field title="Bandwidth (Mbps)">
           			<input type=number class="form-control" v-model=application.bandwidth>
           		</btcl-field>
           		<btcl-field title="Connection Type">
           			<connection-type :data.sync=application.connectionType></connection-type>
           		</btcl-field>
           		<btcl-input title="Description" :text.sync="application.description"></btcl-input>
           		<btcl-input title="Connection Address" :text.sync="application.address"></btcl-input>
           		<btcl-field title="Loop Provider">
           			<multiselect v-model="application.loopProvider" :options="[{ID:1,label:'BTCL'},{ID:2,label:'Client'}]" 
			        	:track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom">
			        </multiselect>
           		</btcl-field>
           		<btcl-field v-if="application.connectionType && application.connectionType.ID==2" title="Duration (Days)">
           			<input type=number class="form-control" v-model=application.duration>
           		</btcl-field>
           		<btcl-input title="Comment" :text.sync="application.comment"></btcl-input>
           		<btcl-field title="Suggested Date">
           			<btcl-datepicker :date.sync="application.suggestedDate"></btcl-datepicker>
           		</btcl-field>
           		
           		
					<div class="row"> 
						<div class="col-md-12">
							<div class="table">
								<table class="table table-striped table-bordered table-checkable dataTable no-footer">
									<tbody>
										<tr>
										<td colspan="1" width="25%"><label class="control-label">Upload Required Documents<span class="required" aria-required="true"> * </span></label></td>
										<td colspan="1" width="75%">
											<!-- File upload -->
											<div class="row">
												<div class="col-md-12">
													 	<div class="col-md-3" style="padding: 0px;">
															<!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
															<span class="btn btn-warning-btcl  fileinput-button">
																<i class="fa fa-upload"></i>
																<span> Upload here </span> 
																<input class="jFile" id="doc" type="file" name="<%=FileTypeConstants.DOMAIN_BUY.DOMAIN_OWNERSHIP_CHANGE%>">
															</span>
														</div>
														<div class="col-md-9 has-error" id="fileRequired">
														</div>
							
													<div class="col-md-9">
														 <!-- The global file processing state -->
														 <span class="fileupload-process"></span>
												   		  <!-- The global progress state -->
												          <div class="col-lg-12 fileupload-progress fade">
												              <!-- The global progress bar -->
												              <div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100">
												                  <div class="progress-bar progress-bar-success" style="width:0%;"></div>
												              </div>
												              <!-- The extended global progress state -->
												              <div class="progress-extended">&nbsp;</div>
												          </div>
													</div>
													<!-- The table listing the files available for upload/download -->
													<table role="presentation" class="table table-striped margin-top-10">
														<tbody class="files"></tbody>
													</table>
												</div>
										 		<jsp:include page="../../../common/ajaxfileUploadTemplate.jsp" />
											</div>
											<!-- /File upload -->
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>           		
           		           		
           		
           	</btcl-portlet>
           	
		</btcl-form>
	</btcl-body>
</div>

<script src="../connection/lli-connection-components.js"></script>
<script src=lli-application-components.js></script>
<script src=new-connection/lli-application-new-connection.js></script>

