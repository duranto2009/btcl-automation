<%@page import="common.EntityTypeConstant"%>
<%@page import="common.ModuleConstants"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="dns.domain.*"%>
<%@page import="java.util.*"%>


<style>
	.fa-plus{
		color: green;
		vertical-align: middle;
	}
 	.fa-minus{ 
 		color: red; 
 		vertical-align: middle; 
 	}
 	.btn-default{
 		border-radius: 25px !important;
 		background: #fff !important;
 	}
 	.btn-default:hover, .btn-default:active, .btn-default:focus, .btn-default:target{
		background-color: #95A5A6  !important;
		border-color:#95A5A6  !important;
		color: #fff !important;
	}
	.btn-danger{
		margin-top: 5px;
	}
</style>

<%

	DNSDomainDTO dnsDomainDTO = (DNSDomainDTO) request.getAttribute("dnsDomain");
	List<DNSSubDomainDTO> dnsSubDomainDTOs = (List<DNSSubDomainDTO>) request.getAttribute("dnsSubDomains");
	

%>

<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-gift"></i><%=request.getParameter("title") %>
		</div>
	</div>
	
	
	<div class="portlet-body form">
		<form id = "form"  class="form-horizontal">
			<div class="form-body">
				<div class="form-group">
					<label class="col-sm-2 control-label">Client Name</label>
					<div class="col-sm-9">
						<label class="control-label">
						<a href="${context }GetClientForView.do?moduleID=<%=ModuleConstants.Module_ID_DNSHOSTING%>
							&entityID=<%=dnsDomainDTO.getClientID()%>" target="_blank">
							<%=AllClientRepository.getInstance().
							getClientByClientID(dnsDomainDTO.getClientID()).getLoginName()%>
						</a>
						</label>
						
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">DNS Domain</label>
					<div class="col-sm-9">
						<input type="text" class="form-control" id="dnsDomainName" 
						name="domainName" value="<%=dnsDomainDTO.getUnicodeDNSDomainName()%>">
						<input type="hidden" name="ID" value="<%=dnsDomainDTO.getID() %>">
						<input type="hidden" name="clientID" value="<%=dnsDomainDTO.getClientID()%>"> 
					</div>
				</div>
						<%
					for(DNSSubDomainDTO dnsSubDomainDTO: dnsSubDomainDTOs){
						%>
				<div class="form-group">
					<label class="col-sm-2 control-label">Sub Domain</label>
					<div class="col-sm-2">
						<input type="text" class="form-control" class="dns-sub-domain-name"
							name="dnsSubDomainNamePrev" id="dns-sub-domain-name.<%=dnsSubDomainDTO.getID() %>" 
							value="<%=dnsSubDomainDTO.getSubDomainName()%>">
						<input type="hidden" class="form-control" class="dns-sub-domain-ID"
							name="dnsSubDomainIDPrev" id="dns-sub-domain-id.<%=dnsSubDomainDTO.getID() %>" 
							value="<%=dnsSubDomainDTO.getID()%>">
					</div>
					<div class="col-sm-2">
						<input type="text" class="form-control subdomain-prefix" value=".<%=dnsDomainDTO.getDomainName() %>" readonly>
					</div>
					<label class="col-sm-1 control-label">IP</label>
					<div class="col-sm-2">
						<input type="text" class="form-control" class="dns-sub-domain-ip"
							name="dnsSubDomainIPPrev"id="dns-sub-domain-ip.<%=dnsSubDomainDTO.getID() %>" 
							value="<%=dnsSubDomainDTO.getIpAddress()%>">
					</div>
					<div class="col-sm-2">
					
						<select name="dnsSubDomainRecordTypePrev" class="form-control rec-type">
							<%
							int recordType = dnsSubDomainDTO.getRecordType();
							for(int i=1;i<=5;i++){
								if( i == recordType ){
						%>
								<option value="<%=i%>" selected>  <%=EntityTypeConstant.mapOfRecordTypeToRecordTypeStr.get(i)%> </option>
						<%					
								}else {
						%>
								<option value="<%=i%>" > <%=EntityTypeConstant.mapOfRecordTypeToRecordTypeStr.get(i)%> </option>
						<%
								}
							}
						%>
						</select>
					</div>
						
					<div class="col-sm-1">
						<button type="button" data-id=<%=dnsSubDomainDTO.getID() %> data-parent-id=<%=dnsSubDomainDTO.getDomainID() %> 
						class="btn btn-xs btn-danger existing-row">
							<span>
								<i class="fa fa-times"></i>
							</span>
						</button>			
					</div>
				</div>
				<%} %>
					
			</div>
			<div class="text-center">
				<button type="button" class="btn btn-default" id="addDnsSubDomain">SubDomain Add
					<i class="fa fa-plus" ></i>
				</button>
			</div>
			<br><br>
			
			<div class="form-actions text-center">
				<button class="btn btn-reset-btcl" type="reset" id="reset-btn">Reset</button>
				<button class="btn btn-submit-btcl" type="submit" id="submit-btn">Submit</button>
			</div>
		</form>
	</div>
</div>
<script src="../../assets/global/plugins/bootbox/bootbox.min.js" type="text/javascript"></script>
<script>
$(document).ready(function(){
	var addBtn = $('#addDnsSubDomain');
	var resetBtn = $('#reset-btn');
	var submitBtn = $('#submit-btn');
	var form = $('#form');
	var formBody = $('.form-body');
	var subDNSCount = 0;
	var existingRow = $('.existing-row');
	var appendedRow = $('.appended-row');
	var dnsDomainName = $("#dnsDomainName");
	var formData = {};
	var deletedIDs = [];
	function getDNSSubDomainNameDivString(){
		return '<div class="form-group">' + 
					'<label class="col-sm-2 control-label">Sub Domain</label>' + 
					'<div class="col-sm-2">' + 
						'<input type="text" class="form-control postName" class="dns-sub-domain-name"' + 
						'name="dnsSubDomainNamePost" value="">' + 
					'</div>' + 
					'<div class="col-sm-2">' + 
						'<input type="text" readonly class="form-control subdomain-prefix" value=".">' + 
					'</div>';
	}
	function getDNSSubDomainIPDivString(){
		return 		'<label class="col-sm-1 control-label">IP</label>' + 
					'<div class="col-sm-2">' + 
						'<input type="text" class="form-control postIP" id="dns-sub-domain-IP"' + 
						'name="dnsSubDomainIPPost" value="">' + 
					'</div>'; 
	}
	
	function getRemoveRowDiv (){
		return '<div class="col-sm-1">' + 
					'<button class="btn btn-xs btn-danger appended-row" data-id=" " data-parent-id=" ">' + 
						'<i class="fa fa-times"></i>' + 
					'</button>' + 
				'<div>' + 
			'</div>';
	}
	function getDNSSubDomainRecordType(){
		return		'<div class="col-sm-2">' + 
					'<select name="dnsSubDomainRecordTypePost" class="form-control post-rec-type">' + 
						'<option value="-1" selected >Record</option>' + 
						'<option value="1"> A </option>' +
						'<option value="2"> AAAA </option>' +
						'<option value="3"> CNAME </option>' +
						'<option value="4"> NS </option>' +
						'<option value="5"> MX </option>' +
					'</select>' +
				'</div>';
	}
	
	function getSubDomainInput(){
		return getDNSSubDomainNameDivString() + 
		getDNSSubDomainIPDivString() + getDNSSubDomainRecordType() + 
		getRemoveRowDiv(); 
	}
	function validateForm(){
		var flag = true;
		$('.postIP').each(function(){
			if($(this).val().length == 0){
				toastr.error("IP can not be empty");
				$(this).closest('div').addClass('has-error');
				flag = false;
			}else{
				if($(this).closest('div').hasClass('has-error')){
					$(this).closest('div').removeClass('has-error');
				}
			}
		});
		$('.post-rec-type').each(function(){
			if($(this).val() < 1){
				toastr.error('Record Type can not be empty');
				$(this).closest('div').addClass('has-error');
				flag = false;
			}else {
				if($(this).closest('div').hasClass('has-error')){
					$(this).closest('div').removeClass('has-error');
				}
			}
		});
		return flag;
	}
	
	function getValues(selector1, selector2) {
		var values = [];
		$(selector1).each(function(){
			values.push($(this).val());
		});
		$(selector2).each(function(){
			values.push($(this).val());
		});
		return values;
	}
	function checkDuplicate(){
		var subdomNames  = getValues('input[name=dnsSubDomainNamePrev]', 'input[name=dnsSubDomainNamePost]');
		var subdomIPs  = getValues('input[name=dnsSubDomainIPPrev]', 'input[name=dnsSubDomainIPPost]');
		var subdomRecTypes = getValues('select[name=dnsSubDomainRecordTypePrev]', 'select[name=dnsSubDomainRecordTypePost]');
		var duplicate ={};
		for(var i=0;i<subdomNames.length-1;i++){
			for(var j=i+1;j<subdomNames.length; j++){
				var subdomNameI = subdomNames[i];
				var subdomNameJ = subdomNames[j];
				var subdomIPI = subdomIPs[i];
				var subdomIPJ = subdomIPs[j];
				var subdomRecTypeI = subdomRecTypes[i];
				var subdomRecTypeJ = subdomRecTypes[j];
				if(subdomNameI == subdomNameJ && subdomIPI == subdomIPJ && subdomRecTypeI == subdomRecTypeJ){
					duplicate['isTrue'] = true;
					duplicate['I'] = i + 2 ; // shift param : client info , dns domain info: kicchu korar nai. :(
					duplicate['J'] = j + 2 ;
					return duplicate;
				}
				
			}
		}
		duplicate['isTrue'] = false;
		return duplicate;
	}
	
	
	existingRow.click(function(){
		var self = $(this);
		deletedIDs.push(self.attr('data-id'));
		self.closest('.form-group').remove();
	});
	dnsDomainName.keyup(function(){
		$('.subdomain-prefix').val('.' + dnsDomainName.val());
	});
	addBtn.click(function(){
		subDNSCount ++;
		var str = getSubDomainInput();
		formBody.append(str);
		$('.subdomain-prefix').val('.' + dnsDomainName.val());
	});
	var firstDuplicate;
	var secondDuplicate;
	submitBtn.click(function(ev){
		ev.preventDefault();
		if(validateForm() == false){
			return false;
		}else{
			var duplicate = checkDuplicate();
			if(duplicate.isTrue){
				toastr.error("There are duplicate entries");
				if(typeof firstDuplicate != 'undefined' && typeof secondDuplicate != 'undefined'){
					firstDuplicate.removeClass('has-error');
					secondDuplicate.removeClass('has-error');
				}
				firstDuplicate = $('.form-group:eq(' + duplicate.I + ')');
				secondDuplicate = $('.form-group:eq(' + duplicate.J + ')');
				firstDuplicate.addClass('has-error');
				secondDuplicate.addClass('has-error');
				return false;
			}else{
				var url = context + "DNS/Domain/update.do"
			    var formData = form.serializeArray();
			    var obj = {};
			    obj.name = "dnsSubDomainIDDeleted";
			    obj.value = deletedIDs;
			    formData.push(obj);
			    callAjax(url, formData, function(data){
			    	if(data.responseCode == 1){
			    		toastr.success(data.msg);
			    		setTimeout(function(){
			    			location.href = context + "DNS/Domain/view.do?domainID=<%=dnsDomainDTO.getID()%>";
			    		}, 1500);
			    	}else {
			    		toastr.error(data.msg);
			    	}
			    }, "POST");	
			}
		}
	});
	resetBtn.click(function(){
		form.trigger('reset');
		return false;
	});
	
	$(document).on('click', '.appended-row' , function(){
		var self = $(this);
		self.closest('.form-group').remove();
		return false;
	});
	
});
</script>
