<%@page import="common.EntityTypeConstant"%>
<%@page import="common.bill.BillConstants"%>
<%@page import="java.util.Set"%>
<%@page import="common.note.CommonNoteConstants" %>
<%@page import="common.ModuleConstants"%>
<div class="portlet box portlet-btcl">
	
	<div class="portlet-title portlet-title-btcl">
		
		<div class="caption">
			<i class="fa fa-link" aria-hidden="true"></i>
			Bill Configuration
		</div>
		
	</div>
	
	<div class="portlet-body portlet-body-btcl form">
		
		<form id="confForm" class="form-horizontal" method="post" action="<%=request.getContextPath() %>/AddBillConfiguration.do" >
			
			<input type="hidden" name="method" value="insert" />
			
			<div class="form-body">
			
				<div class="form-group">
					<label for="cnName" class="col-sm-3 control-label"></label>
					<div class="col-sm-4">
				     	<h3>Add Custom Bill Text</h3>
					</div>
				</div>
				
				<div class="form-group">
					<label for="cnName" class="col-sm-3 control-label"> Select Module </label>
					<div class="col-sm-4">
				     	<select id="moduleID" name="moduleID" class="form-control">
				     		<% for( Integer i : ModuleConstants.ActiveModuleMap.keySet() ){ %>
				     			<option value="<%=i%>"> <%=ModuleConstants.ActiveModuleMap.get(i) %></option>
				     		<%} %>
				     	</select>
					</div>
				</div>
				
				<div class="form-group">
					<label for="cnName" class="col-sm-3 control-label"> Select Document Type </label>
					<div class="col-sm-4">
				     	<div class="radio-list">
							 <label class="radio-inline"> 
							 	<input type="radio" name="docType" value="bill" autocomplete="off"  data-no-uniform="true" />  Bill
							 	<input type="radio" name="docType" value="other" autocomplete="off"  data-no-uniform="true" />  Other Document
							</label>
						</div>
					</div>
				</div>
				
				<div class="form-group">
					<label for="cnName" class="col-sm-3 control-label"> Select Bill Type </label>
					<div class="col-sm-4">
				     	<select id="billTypeID" name="billTypeID" class="form-control">
				     		<option value="2"> Demand Note </option>
				     	</select>
					</div>
				</div>
				<%System.out.println(BillConstants.formFieldName); %>
				
				<% for( String fieldName: BillConstants.formFieldName.keySet() ){ %>
				
					<div class="form-group">
					
						<label for="cnName" class="col-sm-3 control-label"> <%=BillConstants.formFieldText.get( fieldName ) %> </label>
						<div class="col-sm-4">
					     	<textarea class="form-control" name="<%=fieldName %>" id="<%=fieldName %>"></textarea>
						</div>
						
					    <%-- <label for="cnName" class="control-label col-sm-1 col-md-1 col-lg-1 col-xs-2 col-xxs-2"> Font Size </label>
						<div class="col-sm-1 col-xs-1 col-xxs-1 col-md-1 col-lg-1 ">
					     	<select class="form-control" name="<%=fieldName %>_font"  id="<%=fieldName %>_font">
					     		<option value="6">6</option>
					     		<option value="8">8</option>
					     		<option value="10">10</option>
					     	</select>
						</div> --%>
					</div>
				<%} %>
				
				<div class="form-actions text-center">
					<a class="btn btn-cancel-btcl" type="button" href="<%=request.getHeader("referer")%>">Back</a>
					<button class="btn btn-reset-btcl" type="reset" >Cancel</button>
					<button id="updateBtn" class="btn btn-submit-btcl" type="submit" >Submit</button>
					<button id="demoBtn" class="btn btn-submit-btcl" type="button">Demo</button>
				</div>
			</div>	
					
		</form>
		
	</div>
</div>
<script>
	
	$(document).ready(function(){
		
		var idToFormFieldID = {};
		var fieldName = new Array();
		
		<%for( String field: BillConstants.formFieldName.keySet() ){ %>	
			fieldName.push("<%=field %>");
		<%}%>
		<%for( Integer id: BillConstants.idToFormField.keySet() ){ %>	
			idToFormFieldID['<%=id %>'] = "<%=BillConstants.idToFormField.get(id) %>";
		<%}%>
		
		var context = "<%=request.getContextPath() %>";
		var url = context + "/AddBillConfiguration.do";
		
		var param = {};
		param['moduleID'] = $("#moduleID").val();
		param['billTypeID'] = $("#billTypeID").val();
		param['method'] = "get";
		
		callAjax( url, param, billConfGetCallback, "GET" );
		
		function billConfInsertCallback(data ){
			
			if( data['responseCode'] == 2 ){
				
				toastr.error( data['msg'] );
			}
			else if(data['responseCode'] == 1 ){
				
				toastr.success( "Header and footers are inserted successfully" );
			}
		}
		
		function billConfGetCallback( data ){
			
			if( data['responseCode'] == 2 ){
				
				toastr.error( data['msg'] );
			}
			else if(data['responseCode'] == 1 ){
				
				var payload = data['payload'];
				
				if( payload.length == 0 ){
					
					var moduleID = $("#moduleID").val();
					var billTypeID = $("#billTypeID").val();
					
					toastr.info( "No data found for this module and bill type" );
					document.getElementById("confForm").reset();
					
					$("#moduleID").val( moduleID );
					$("#billTypeID").val( billTypeID );
				}
				else{
					
					for( var i = 0; i < payload.length; i++ ){
						
						var formFieldID = idToFormFieldID[payload[i]["headerFooterID"]];
						
						$( "#" + formFieldID ).val( payload[i]["text"]);
						$( "#" + formFieldID + "_font" ).val( payload[i]["fontSize"]);
					}
					
					$("#moduleID").val( payload[0]['moduleID']);
				    $("#billTypeID").val( payload[0]['billTypeID']);
				}
			}
		}
		
		$("#moduleID").on( "change", function(){

			if( $("#moduleID").val() == "<%=ModuleConstants.Module_ID_VPN %>" || $("#moduleID").val() == "<%=ModuleConstants.Module_ID_LLI %>" ){
				
				renderDocTypeForVpnOrLli();
			}
			else{
				$("#billTypeID").children('option').remove();
				$("#billTypeID").append( '<option value="<%=BillConstants.DEMAND_NOTE %>"> Demand Note </option>' );
			}
		});
		
		function renderDocTypeForVpnOrLli(){
			
			var docType = $($("span.checked").children( "input" )[0]).val();
			
			if( docType == undefined ){
				
				toastr.error( "Please, select a document type" );
				return;
			}
			
			$("#billTypeID").children('option').remove();
			
			if( docType == "bill" ){
				$("#billTypeID").append( '<option value="<%=BillConstants.DEMAND_NOTE %>"> Demand Note </option>' );
				$("#billTypeID").append( '<option value="<%=BillConstants.POSTPAID %>"> Monthly Bill </option>' );
			}
			else if( docType == "other" ){
				
				if( $("#moduleID").val() == "<%=ModuleConstants.Module_ID_VPN %>" ){
					
					$("#billTypeID").append( '<option value="<%=CommonNoteConstants.ADVICE_NOTE_FOR_VPN_LINK %>"> Advice Note </option>' );
					$("#billTypeID").append( '<option value="<%=CommonNoteConstants.VPN_INTERNAL_FR_RESPONSE %>"> Internal FR </option>' );
					$("#billTypeID").append( '<option value="<%=CommonNoteConstants.VPN_EXTERNAL_FR_RESPONSE %>"> External FR </option>' );
				}
				else if( $("#moduleID").val() == "<%=ModuleConstants.Module_ID_LLI %>" ){
					
					$("#billTypeID").append( '<option value="<%=CommonNoteConstants.ADVICE_NOTE_FOR_LLI_LINK %>"> Advice Note </option>' );
					$("#billTypeID").append( '<option value="<%=CommonNoteConstants.LLI_INTERNAL_FR_RESPONSE %>"> Internal FR </option>' );
					$("#billTypeID").append( '<option value="<%=CommonNoteConstants.LLI_EXTERNAL_FR_RESPONSE %>"> External FR </option>' );
				}
			}
		}
		
		$( "#moduleID, #billTypeID" ).on( "change", function(){
			getDataByModuleAndDocTypeID();
		});
		
		$('input:radio[name="docType"]').change(function(){

			if( $("#moduleID").val() == "<%=ModuleConstants.Module_ID_VPN %>" || $("#moduleID").val() == "<%=ModuleConstants.Module_ID_LLI %>" ){
				renderDocTypeForVpnOrLli();
			}
			getDataByModuleAndDocTypeID();
		});
		
		function getDataByModuleAndDocTypeID(){
			
			param = {};
			param['moduleID'] = $("#moduleID").val();
			param['billTypeID'] = $("#billTypeID").val();
			param['method'] = "get";
			
			callAjax( url, param, billConfGetCallback, "GET" );			
		}
		
		$("#confForm").on( "submit", function(){
			
			param['moduleID'] = $("#moduleID").val();
			param['billTypeID'] = $("#billTypeID").val();
			param['method'] = "insert";
			
			for( var i = 0; i<fieldName.length; i++ ){
				
				param[fieldName[i]] = $( "#" + fieldName[i] ).val();
				param[fieldName[i] + "_font"] = $( "#" + fieldName[i] +"_font" ).val();
			}
			
			console.log( param );
			callAjax( url, param, billConfInsertCallback, "POST" );
			
			return false;
		});
		
		$("#demoBtn").on( "click", function(){
			
			var moduleID = $("#moduleID").val();
			var params = $("#confForm").serialize();
			var billTypeID = $("#billTypeID").val();
			
			if( billTypeID != "<%=BillConstants.DEMAND_NOTE %>" && billTypeID != "<%=BillConstants.MONTHLY_BILL %>" )
				toastr.info( "Demo only available for demand note and monthly bill");
			else{
				window.open( 
						context + "/GetPdfBill.do?method=dummyBill&moduleID="+moduleID+"&"+params,
						"bill Demo",
						"width=800px,height=700px,top=100px,left=400px" 
					);
			}
		});
	});
</script>