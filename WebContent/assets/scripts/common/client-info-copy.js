$(document).ready(function() {
	$("#checkbox-using-existing-client-info").css("display", "block");

	var swap="newClient";
	$("#existing-check").click(function(){
		if ($("#existing-check").parent().hasClass("checked")){
			$("#using-existing-client-div").hide();
			$("#login-information").show();
			revert();
		}else{
			$("#using-existing-client-div").show();
			$("#login-information").hide();

		}
	});

	function showAccountInfo(type){
		$("#clientCategoryType").val(type);
		$(".fileType").hide();
		$('.identityLabel').html("");
		clearIdentityList();
		if(type==1){
			$("#regIndividualInfo").removeClass('hidden');
			$("#regCompanyInfo").addClass('hidden');

			$("#regCompanyInfo").find("input, select").attr("disabled", true);
			$("#regIndividualInfo").find("input, select").attr("disabled", false);

			$(".individual").addClass("regi");
			$(".company").removeClass("regi");


		}else{
			$("#regCompanyInfo").removeClass('hidden');
			$("#regIndividualInfo").addClass('hidden');

			$("#regIndividualInfo").find("input, select").attr("disabled", true);
			$("#regCompanyInfo").find("input, select").attr("disabled", false);
			$("#regCompanyInfo").find(".checker").removeClass("disabled");
			$("#regCompanyInfo").find(".radio").removeClass("disabled");

			$(".individual").removeClass("regi");
			$(".company").addClass("regi");
		}

		if(!(($('input[name="accountType"][value="'+type+'"]')).hasClass('checked'))){
			$('input[name="accountType"][value="'+type+'"]').prop('checked', true);
			$('input[name="accountType"][value="'+type+'"]').parent().addClass('checked');
		}
		if(( $('input[name="accountType"]:not([value="'+type+'"])')).parent().hasClass('checked')){
			$('input[name="accountType"]:not([value="'+type+'"])').prop('checked', false);
			$('input[name="accountType"]:not([value="'+type+'"])').parent().removeClass('checked');
		}
		$('#forwardingLetter').closest('.radio-inline').hide();
	}


	function revert() {
		location.reload();
	}
	function populateIdentityNumberInCopyFromCase(data, moduleIdPrev, regType , regCategory) {

		let clientIdentityTypeMap = {};
		data['clientDetailsDTO.identity'].split(",").forEach(item=>{
			if(item !== ''){
				let item2 = item.split(":");
				clientIdentityTypeMap[item2[0]] = item2[1];
			}
		});
		console.log(clientIdentityTypeMap);

		return axios.get(context + "ClientType/GetCompanyIdentityList.do?moduleID="+moduleIdPrev +"&registrantType="+regType + "&registrantCategory="+regCategory)
			.then(res=>{
				let identityTypeHTML = "";
				if(res.data.responseCode === 1) {
					let arr = res.data.payload;
					let iterator = 0;
					arr.forEach((value)=>{
						if (value.fieldType === 1) {
							if (value.name !== "Photograph") {
								if (iterator % 2 === 0) {
									identityTypeHTML += "<div class=row>";
								}

								identityTypeHTML += "<div class=col-md-6>"
									+ "<div class=form-group>"
									+ "<label class='control-label col-md-4'>" + value.name + " No." + ((value.isMandatory) ? "<span class=required aria-required=true> * </span>" : "") + "</label>"
									+ "<div class=col-md-8>"
									+ "<input type=text class=form-control " +
									"value='" + clientIdentityTypeMap[value.id]+"' " +
									"name=identityType_" + value.id + " " +
									((value.isMandatory) ? "required" : "") +
									((map[value.id]) ? " disabled" : "") +
									">"
									+ "</div>"
									+ "</div></div>";
								if (iterator % 2 === 1 || iterator === arr.length - 1) {
									identityTypeHTML += "</div>";
								}
								iterator++;

							}
							if (value.name === "Valid BTRC License") {
								if (iterator % 2 === 0) {
									identityTypeHTML += "<div class=row>";
								}
								identityTypeHTML += "<div class=col-md-6>"
									+ "<div class=form-group>"
									+ "<label class='control-label col-md-4'>" + "Valid BTRC License Date (mm/dd/yyyy)" + "</label>"
									+ "<div class=col-md-8>"
									+ "<input type=text class=form-control" +
									" name=clientDetailsDTO.btrcLicenseDate" + " " + "value=" + data['clientDetailsDTO.btrcLicenseDate'] + ">"
									+ "</div>"
									+ "</div></div>";
								if (iterator % 2 === 1 || iterator === arr.length - 1) {
									identityTypeHTML += "</div>";
								}
								iterator++;

							}
							identityTypeIDArray.push(value);
						}
					});
					$("#identification").html(identityTypeHTML);
				}
			});

	}
	function populateRegistrantCategoryInCopyFromCase(regType, regCategory) {

		let moduleId = $("#client-module-selected").val();
		return axios.get(context + "ClientType/GetRegistrantCategory.do?moduleID="+moduleId +"&registrantType="+regType)
			.then((res)=> {
				if(res.data.responseCode === 1) {
					let registrantCategoryHTML="";
					res.data.payload.forEach(value=> {
						let checkedStatus = value.key == regCategory ? "checked" : '';
						registrantCategoryHTML +=
							"<div class='col-md-4' >" +
							"<label class=checkbox-inline>" +
							"<input type=checkbox disabled name='clientDetailsDTO.regiCategories' value=" + value.key + " " + checkedStatus + ">" +
							value.value + "</label>" +
							"</div>";
					});

					$("#registrantCategoryContainer").html(registrantCategoryHTML);
					$("#registrantCategoryContainer").find("input:checkbox").uniform();
				}

			});

	}
	function populateSubCategoryInCopyFromCase(selectedRegistrantCategory) {

		return axios.get(context + "ClientType/getSubCategoriesUnderACategory.do?registrantCategory="+selectedRegistrantCategory)
			.then(res=>{
				if(res.data.responseCode === 1) {
					// let checkedStatus = item.key == regCategory ? "checked" : '';

					let registrantSubCategoryHTML  = "" ;
					res.data.payload.forEach(item=> {
						let checked = (item.key == selectedRegistrantCategory) ? "checked" : "";
						registrantSubCategoryHTML+=
							"<div class='col-md-4'>" +
							"<label class=checkbox-inline>" +
							"<input type=checkbox name='clientDetailsDTO.regSubCategory' disabled value=" + item.key + " " + checked + ">" + item.value +
							"</label>" +
							"</div>";
					});


					$("#registrantSubCategoryContainer").html(registrantSubCategoryHTML);
					$("#registrantSubCategoryContainer").find("input:checkbox").uniform();
				}
			})
	}
	function getFilesInCopyFromCase(regType, regCategory) {
		let moduleIdPrev = $("#client-module-selected").val();
		let clientId = $("#client-id-autocomplete").val();
		let url = context + "client-document-type/get-required-docs-with-common-files.do?" +
		"module=" + moduleID + "&type=" +  regType + "&category=" + regCategory + "&prevModule=" + moduleIdPrev + "&client=" +clientId;
		// let url = context + "client-document-type/get-required-docs.do?module=" + moduleID + "&type=" + regType+ "&category=" +regCategory;
		$('.star_mark').remove();
		return axios.get(url)
			.then(res=>{
				if(res.data.responseCode ===1 ){
					$(".fileType").hide();
					res.data.payload.docTypes.forEach(item=>{
						let docType = item.key.docTypeId;
						// console.log(item );
						// console.log(docType);
						if(!item.key.isGlobal){
							// console.log("Required: " + item.key.name);
							$("#file_" + docType).css("display", "block");

							if(item.value){
								$("#file_" + docType).find("span").after("<span class='star_mark' style='color:red'>&nbsp;*</span>");
							}

						}
					});
					let commonFileHTML = "<table class='table table-bordered'><thead><tr><th>Document Type</th> <th>Document Name</th> <th>Size</th></tr></thead><tbody>";
					res.data.payload.commonFiles.forEach(item=>{
						commonFileHTML += "<tr>" +
							"<td> <a href=" + context +"DownloadFile.do?documentID="+ item.docID+">"+item.docTypeName+ "</a></td>" +
							"<td> " + item.docActualFileName+"</td>" +
							"<td> " + item.docSizeStr+"</td>" +
							"</tr>";

					});
					commonFileHTML+="</tbody></table>";
					console.log(commonFileHTML);

					$("#already-uploaded-docs-container").html(commonFileHTML);
				}
			}).catch(err=>console.log(err));
	}

	function populateForm(data){
		//clientCategoryType
		let moduleIdPrev = $("#client-module-selected").val();
		let clientCategoryType = data['clientDetailsDTO.clientCategoryType'];



		if(clientCategoryType==1){
			showAccountInfo(1);
			//Client Individual
			$('input[name="clientDetailsDTO.registrantType"]').val("0");
			$('input[name="clientDetailsDTO.regiCat"]').val("0");
			$('input[name="registrantContactDetails.registrantsName"]').val(data['registrantContactDetails.registrantsName']);
			$('input[name="registrantContactDetails.registrantsLastName"]').val(data['registrantContactDetails.registrantsLastName']);
			$('input[name="registrantContactDetails.fatherName"]').val(data['registrantContactDetails.fatherName']);
			$('input[name="registrantContactDetails.motherName"]').val(data['registrantContactDetails.motherName']);
			$('input[name="registrantContactDetails.gender"]').val(data['registrantContactDetails.gender']);
			$('input[name="registrantContactDetails.email"]').val(data['registrantContactDetails.email']);

			var phoneNumber = data['registrantContactDetails.phoneNumber'];
			$('input[name="intlMobileNumber"]').intlTelInput("setNumber", phoneNumber+"");
			$('input[name="registrantContactDetails.faxNumber"]').val(data['registrantContactDetails.faxNumber']);
			$('input[name="registrantContactDetails.dateOfBirth"]').val(data['registrantContactDetails.dateOfBirth']);
			$('input[name="registrantContactDetails.occupation"]').val(data['registrantContactDetails.occupation']);

			$('input[name="registrantContactDetails.landlineNumber"]').val(data['registrantContactDetails.landlineNumber']);
			$('input[name="registrantContactDetails.country"]').val(data['registrantContactDetails.country']);
			$('input[name="registrantContactDetails.city"]').val(data['registrantContactDetails.city']);
			$('input[name="registrantContactDetails.postCode"]').val(data['registrantContactDetails.postCode']);
			$('textarea[name="registrantContactDetails.address"]').val(data['registrantContactDetails.address']);

		} else if (clientCategoryType==2){
			showAccountInfo(2);
			//Client Company

			// mkr3/7/19 + ==>
			$('input[name="registrantContactDetails.registrantsName"]').val(data['registrantContactDetails.registrantsName']).attr('readonly', true);
			$('input[name="registrantContactDetails.registrantsLastName"]').val(data['registrantContactDetails.registrantsLastName']).attr('readonly', true);
			$('input[name="registrantContactDetails.webAddress"]').val(data['registrantContactDetails.webAddress']).attr('readonly', true);
			$('input[name="registrantContactDetails.email"]').val(data['registrantContactDetails.email']).attr('readonly', true);

			$('input[name="clientDetailsDTO.registrantType"]').val("0").attr('readonly', true);
			$('input[name="clientDetailsDTO.regiCat"]').val("0").attr('readonly', true);

			var phoneNumber = data['registrantContactDetails.phoneNumber'];
			$('input[name="intlMobileNumber"]').intlTelInput("setNumber", phoneNumber+"").attr('readonly', true);
			$('input[name="registrantContactDetails.faxNumber"]').val(data['registrantContactDetails.faxNumber']).attr('readonly', true);




			let regType = data['clientDetailsDTO.registrantType'];
			let regCategory = data['clientDetailsDTO.regiCategories'];
			$('select[name="clientDetailsDTO.registrantType"]').val(regType).attr('disabled', true);


			$('input[name="registrantContactDetails.landlineNumber"]').val(data['registrantContactDetails.landlineNumber']).attr('readonly', true);
			$('select[name="registrantContactDetails.country"]').val(data['registrantContactDetails.country']).attr('disabled', true);
			$('input[name="registrantContactDetails.city"]').val(data['registrantContactDetails.city']).attr('readonly', true);
			$('input[name="registrantContactDetails.postCode"]').val(data['registrantContactDetails.postCode']).attr('readonly', true);
			$('input[name="registrantContactDetails.isEmailVerified"]').val(data['registrantContactDetails.isEmailVerified']);
			$('input[name="registrantContactDetails.isPhoneNumberVerified"]').val(data['registrantContactDetails.isPhoneNumberVerified']);


			$('textarea[name="registrantContactDetails.address"]').val(data['registrantContactDetails.address']).attr('readonly', true);


			// mkr3/7/19 + <==

			populateRegistrantCategoryInCopyFromCase(regType, regCategory)
				.then(populateSubCategoryInCopyFromCase(regCategory))
				.then(getFilesInCopyFromCase(regType, regCategory))
				.then(populateIdentityNumberInCopyFromCase(data, moduleIdPrev, regType, regCategory))
				.catch(err=>{
					toastr.error("Something went wrong, check console", "Failure");
					console.log(err);
				})
			;


		}



		$('input[name="billingContactDetails.registrantsName"]').val(data['billingContactDetails.registrantsName']);
		$('input[name="billingContactDetails.registrantsLastName"]').val(data['billingContactDetails.registrantsLastName']);
		$('input[name="billingContactDetails.email"]').val(data['billingContactDetails.email']);
		$('input[name="billingContactDetails.phoneNumber"]').val(data['billingContactDetails.phoneNumber']);
		$('input[name="billingContactDetails.landlineNumber"]').val(data['billingContactDetails.landlineNumber']);
		$('input[name="billingContactDetails.faxNumber"]').val(data['billingContactDetails.faxNumber']);
		$('input[name="billingContactDetails.city"]').val(data['billingContactDetails.city']);
		$('input[name="billingContactDetails.postCode"]').val(data['billingContactDetails.postCode']);
		$('textarea[name="billingContactDetails.address"]').val(data['billingContactDetails.address']);

		$('input[name="adminContactDetails.registrantsName"]').val(data['adminContactDetails.registrantsName']);
		$('input[name="adminContactDetails.registrantsLastName"]').val(data['adminContactDetails.registrantsLastName']);
		$('input[name="adminContactDetails.email"]').val(data['adminContactDetails.email']);
		$('input[name="adminContactDetails.phoneNumber"]').val(data['adminContactDetails.phoneNumber']);
		$('input[name="adminContactDetails.landlineNumber"]').val(data['adminContactDetails.landlineNumber']);
		$('input[name="adminContactDetails.faxNumber"]').val(data['adminContactDetails.faxNumber']);
		$('input[name="adminContactDetails.city"]').val(data['adminContactDetails.city']);
		$('input[name="adminContactDetails.postCode"]').val(data['adminContactDetails.postCode']);
		$('textarea[name="adminContactDetails.address"]').val(data['adminContactDetails.address']);

		$('input[name="technicalContactDetails.registrantsName"]').val(data['technicalContactDetails.registrantsName']);
		$('input[name="technicalContactDetails.registrantsLastName"]').val(data['technicalContactDetails.registrantsLastName']);
		$('input[name="technicalContactDetails.email"]').val(data['technicalContactDetails.email']);
		$('input[name="technicalContactDetails.phoneNumber"]').val(data['technicalContactDetails.phoneNumber']);
		$('input[name="technicalContactDetails.landlineNumber"]').val(data['technicalContactDetails.landlineNumber']);
		$('input[name="technicalContactDetails.faxNumber"]').val(data['technicalContactDetails.faxNumber']);
		$('input[name="technicalContactDetails.city"]').val(data['technicalContactDetails.city']);
		$('input[name="technicalContactDetails.postCode"]').val(data['technicalContactDetails.postCode']);
		$('textarea[name="technicalContactDetails.address"]').val(data['technicalContactDetails.address']);

        $('input[name="registrantContactDetails.phoneNumber"]').val(data['registrantContactDetails.phoneNumber']);

	}

	$("#client-module-selected").change(function(){
		if((($("#client-module-selected")).val())=="-1"){
		}else{
			var formData={};
			formData.moduleID = $("#client-module-selected").val();
			formData.clientID = $("#client-id-autocomplete").val();
			var url=  context + 'GetClientFormData.do';
			ajax(url, formData, populateForm, "GET", []);
		}
	});


	function populateModuleList(data){
		var thisModule = ($('input[name="clientDetailsDTO.moduleID"]').val());
		var isClientAlreadyRegisteredInThisModule=false;

		$.each(data, function(index, value){
			if(value.moduleID==thisModule){
				isClientAlreadyRegisteredInThisModule = true;
			}
		});

		if(!isClientAlreadyRegisteredInThisModule){
			$("#client-module-selected").empty();
			$("#client-module-selected").append('<option value="-1">Select a Source Module</option>');
			$.each(data, function(index, value){
				$("#client-module-selected").append('<option value="'+value.moduleID+'">'+value.moduleName+'</option>');
			});
		}else{
			toastr.error("Client Already Registered in this Module!");
			$('#client-id-autocomplete').val('-1');
		}
	}

	function getModulesForSelectedClient(clientID){
		var url = context + 'AutoComplete.do?need=modulesForClient';
		var formData = {};
		formData.clientID = clientID;
		callAjax(url, formData, populateModuleList, "GET");
	}

	$("#client-name-autocomplete").autocomplete({
		source : function(request, response) {
			$("#client-id-autocomplete").val(-1);
			var term = request.term;
			var url = context + 'AutoComplete.do?need=allclient';
			var formData = {};
			formData['name'] = term;
			if (term.length >= 3) {
				callAjax(url, formData, response, "GET");
			} else {
				delay(function() {
					toastr.info("Your search name should be at lest 3 characters");
				}, systemConfig.getTypingDelay());
			}
		},
		minLength : 1,
		select : function(e, ui) {
			$('#client-id-autocomplete').val(ui.item.id);
			$('#client-name-autocomplete').val(ui.item.data);
			getModulesForSelectedClient(ui.item.id);
			return false;
		},
	}).autocomplete("instance")._renderItem = function(ul, item) {
		return $("<li>").append("<a>" + item.data + "</a>").appendTo(ul);
	};

	if(($('#client-id-autocomplete')).val()!="-1"){
		getModulesForSelectedClient(($('#client-id-autocomplete')).val());
	}



});