<%@page import="file.FileTypeConstants"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="common.ModuleConstants"%>

<%
int commentFileType=0;
String entityTypeID= request.getParameter("entityTypeID");
if((EntityTypeConstant.VPN_LINK+"").equals(entityTypeID)){
	commentFileType=FileTypeConstants.VPN_LINK_VIEW.VPN_LINK_COMMENT;
}else if((EntityTypeConstant.VPN_CLIENT+"").equals(entityTypeID)){
	commentFileType=FileTypeConstants.VPN_CLIENT_VIEW.VPN_CLIENT_COMMENT;
}if((EntityTypeConstant.DOMAIN_CLIENT+"").equals(entityTypeID)){
	commentFileType=FileTypeConstants.DOMAIN_CLIENT_VIEW.DOMAIN_CLIENT_COMMENT;
}else{
	
}
%>
<div class="portlet" style="margin-bottom: 5px;padding-bottom: 0px;">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-comments-o"></i>Comments
		</div>
	</div>
	<div class="portlet-body ">
		<form id="fileupload" method="post" action="../../Comment.do?mode=add"  enctype="multipart/form-data">
			<div class="form-body">
				<input type="hidden" name="moduleID"
					value='<%=request.getParameter("moduleID")%>' id="moduleID">
				<input type="hidden" name="entityTypeID"
					value='<%=request.getParameter("entityTypeID")%>' id="entityTypeID">
				<input type="hidden" name="entityID"
					value='<%=request.getParameter("entityID")%>' id="entityID">
				<input type="hidden" name="currentTab"
					value='<%=request.getParameter("currentTab")%>' id="currentTab">
				<div class="form-group">
					<input type="submit" class=" btn btn-circle btn-sm   btn-warning-btcl sbold uppercase pull-right" id="submit-comment" value="Post Comment">
				</div>
				<!--div class="form-group">
					<input class="form-control spinner" type="text" id="heading" name="heading" placeholder="Subject">
				</div-->
				<div class="form-group">
					<div class="inbox-editor-open inbox-form-group">
						<textarea id="description" class="inbox-editor inbox-wysihtml5 form-control" name="description" rows="7"  placeholder="Comment" required></textarea>
					</div>
				</div>
			
				<%-- <div class="form-group">
				 	<div class="col-md-3" style="padding: 0px;">
						<!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
						<span class="btn yellow  fileinput-button">
							<i class="fa fa-upload"></i>
							<span> Add Files </span> 
							<input class="commentFile" type="file" name="<%=commentFileType %>" multiple>
						</span>
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
				</div> --%>
				
			</div>
		</form>
	</div>
</div>
<br>

<script type="text/javascript">
	$(document).ready(function(){
	   $("#fileupload").submit(function(){
	       $(".commentFile").attr('disabled',true);
	       return true;
	   }) 
	});

</script>

