<%@page import="common.EntityTypeConstant"%>


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
	
	button.form-control{
		height: 50% !important;
		width : 30% !important;
		padding : 2% !important;
		margin-top : 5% !important;
	}
	
	
	
	
	
</style>
<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-gift"></i><%=request.getParameter("title") %>
		</div>
	</div>
	
	
	<div class="portlet-body form">
		<form id = "form" class="form-horizontal">
			<div class="form-body">
				<div class="form-group">
					<label class="col-sm-2 control-label">Client<span class="required" aria-required="true"> * </span></label>
					<div class="col-sm-9">
						<input id="clientName" type="text" class="form-control"
							placeholder="Type to select..." name="clientName"> 
						<input id="clientID" type="hidden" value="-1" name="clientID">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">Domain</label>
					<div class="col-sm-9">
						<input type="text" class="form-control" id="dnsDomainName" 
						name="domainName" value="">
					</div>
				</div>
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

<script>
$(document).ready(function(){
	var addBtn = $('#addDnsSubDomain');
	var resetBtn = $('#reset-btn');
	var submitBtn = $('#submit-btn');
	var form = $('#form');
	var formBody = $('.form-body');
	var subDNSCount = 0;
	var subdomain = function getDNSSubDomainNameDivString(){
		return '<div class="form-group">' + 
					'<label class="col-sm-2 control-label">Sub Domain</label>' + 
					
					'<div class="col-sm-2">' + 
						'<input type="text" class="form-control" class="dns-sub-domain-name"' + 
						'name="dnsSubDomainName" value="">' + 
						
					'</div>' + 
					'<div class="col-sm-2">' +
						'<input type="text" class="subdomain-prefix form-control" value = "." readonly>' + 
					'</div>';
				  
				
	}
	var ip = function getDNSSubDomainIPDivString(){
		return 		'<label class="col-sm-1 control-label">IP</label>' + 
					'<div class="col-sm-2">' + 
						'<input type="text" class="form-control" id="dns-sub-domain-IP"' + 
						'name="dnsSubDomainIP" value="">' + 
					'</div>'; 
// 				'</div>';
	}
	var recordType = function getDNSSubDomainRecordType(){
// 		return '<label class="col-sm-1 control-label">Record</label>' + 
		return		'<div class="col-sm-2">' + 
					'<select name="recordType" class="form-control rec-type">' + 
						'<option value="-1" selected >Record</option>' + 
						'<option value="1"> A </option>' +
						'<option value="2"> AAAA </option>' +
						'<option value="3"> CNAME </option>' +
						'<option value="4"> NS </option>' +
						'<option value="5"> MX </option>' +
					'</select>' +
				'</div>';
	}
	var removeBtn = function getRemoveRowDiv(){
		return '<div class="col-sm-1" align=left>'+
					'<button type="button" class="btn btn-xs btn-danger form-control remove-row"><i class="fa fa-times"></i></button>' + 
				'</div>' + 
 			'</div>';
	}
	
	function getSubDomainInput(){
		return subdomain() + ip() + recordType() + removeBtn();
	}
	function validateForm(){
		var flag = true;
		if($('#clientID').val() < 0){
			$('#clientID').closest('div').addClass('has-error');
			toastr.info("Select Client");
			flag = false;
		}else {
			if($('#clientID').closest('div').hasClass('has-error')){
				$('#clientID').closest('div').removeClass('has-error');
				
			}
		}
		if($('#dnsDomainName').val().length == 0){
			$('#dnsDomainName').closest('div').addClass('has-error');
			toastr.info("Specify a Domain Name");
			flag = false;
		}else {
			if($('#dnsDomainName').closest('div').hasClass('has-error')){
				$('#dnsDoaminName').closest('div').removeClass('has-error');
			}
		}
		$('input[name=dnsSubDomainIP]').each(function(){
			if($(this).val().length == 0){
				$(this).closest('div').addClass('has-error');
				flag = false;
			}else {
				if($(this).closest('div').hasClass('has-error')){
					$(this).closest('div').removeClass('has-error');
				}
			}
		});
		$('.rec-type').each(function(){
			if($(this).val() < 1){
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
	

	$('#dnsDomainName').keyup(function(){
		$('.subdomain-prefix').val('.' + $('#dnsDomainName').val());
	});
	$(document).on('click', '.remove-row', function(){
		$(this).closest('.form-group').remove();
		
	});
	addBtn.click(function(){
		subDNSCount ++;
		var str = getSubDomainInput();
		formBody.append(str);
		var domainPart =  $('#dnsDomainName').val();
		$('.subdomain-prefix').val("." + domainPart);
		return false;
	});
	resetBtn.click(function(){
		form.trigger('reset');
		return false;
	});
	submitBtn.click(function(ev){
		ev.preventDefault();
		if(validateForm() == false){
			return false;
		}else {
			var formData = form.serialize();
			var url = context + "DNS/Domain/insert.do";
			ajax(url, formData, function(data){
				if(data.responseCode == 1){
					toastr.success(data.msg);
					setTimeout(function(){
						location.href = context + "DNS/Domain/search.do";
					}, 1500);
				}else {
					toastr.error(data.msg);
				}
			}, "POST", []);	
		}
			
	});
	$('#clientName').change(function(){
		if($.trim($('#clientName').val()).length == 0){
			$('#clientID').val('');
		}
	});
	var moduleID = CONFIG.get('module','dnshosting');
    $("#clientName").autocomplete(
    {
        source: function (request, response)
        {
            $("#clientID").val("-1");
            var term = request.term;
            var url = context + "DNS/Domain/getApprovedClients.do";
            var formData = {};
            formData['name'] = term;
            ajax(url, formData, response, "GET", ["clientName"]);
        },
        minLength: 1,
        select: function (e, ui)
        {
            $('#clientName').val(ui.item.loginName);
            $('#clientID').val(ui.item.clientID);
            return false;
        },
    }).autocomplete("instance")._renderItem = function (ul, item)
    {
        return $("<li>").append("<a>" + item.loginName + "</a>").appendTo(ul);
    };
	
})
</script>