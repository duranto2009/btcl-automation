var btrcLicenseDate ;
function populateForm(data){
	//clientCategoryType
	btrcLicenseDate  = data['clientDetailsDTO.btrcLicenseDate'];
	var clientCategoryType = data['clientDetailsDTO.clientCategoryType'];
	$('input[name=accountType]').each(function(index, value){$(this).prop("checked", false);
	$(this).parent().removeClass("checked");});

	$('input[name="clientDetailsDTO.clientCategoryType"]').val(clientCategoryType);
	$('input[name=accountType]:checked').val(clientCategoryType);
	$($('input[name=accountType]')[parseInt(clientCategoryType) -1 ]).prop("checked", true);
	$($('input[name=accountType]')[parseInt(clientCategoryType) -1 ]).parent().addClass("checked");

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

	} else if (clientCategoryType==2){
		showAccountInfo(2);
		//Client Company
		// alert(data['clientDetailsDTO.btrcLicenseDate']);
        $('input[name="clientDetailsDTO.btrcLicenseDate"]').val(data['clientDetailsDTO.btrcLicenseDate']);
		$('input[name="registrantContactDetails.registrantsName"]').val(data['registrantContactDetails.registrantsName'])
			.attr('readonly', true);
		$('input[name="registrantContactDetails.registrantsLastName"]').val(data['registrantContactDetails.registrantsLastName'])
            .attr('readonly', true);
		$('input[name="registrantContactDetails.webAddress"]').val(data['registrantContactDetails.webAddress']);
		$('input[name="registrantContactDetails.email"]').val(data['registrantContactDetails.email']);

		$('select[name="clientDetailsDTO.registrantType"]').val(data['clientDetailsDTO.registrantType']).attr('disabled', true);

        let index = data['clientDetailsDTO.regSubCategory'];
        console.log($($('input[name="clientDetailsDTO.regSubCategory"]')[parseInt(index) -1 ]));
        $($('input[name="clientDetailsDTO.regSubCategory"]')[parseInt(index) -1 ]).prop("checked", true);
        $($('input[name="clientDetailsDTO.regSubCategory"]')[parseInt(index) -1 ]).parent().addClass('checked');

		$('select[name="clientDetailsDTO.registrantType"]').trigger("change");

		globalVariableToStoreSelectedRegistrantCategory = data['clientDetailsDTO.regiCategories'].split(",");
        globalVariableToStoreSelectedRegistrantSubCategory = data['clientDetailsDTO.regSubCategory'];

		var phoneNumber = data['registrantContactDetails.phoneNumber'];
		$('input[name="intlMobileNumber"]').intlTelInput("setNumber", phoneNumber+"");
		$('input[name="registrantContactDetails.faxNumber"]').val(data['registrantContactDetails.faxNumber']);

	}
	globalVariableToStoreIdentity = data['clientDetailsDTO.identity'];

	var countryInput = $('select[name="registrantContactDetails.country"]');
	countryInput.val(data['registrantContactDetails.country']);
	sortSelectBox(countryInput);
	$('input[name="registrantContactDetails.ID"]').val(data['registrantContactDetails.ID']);
	$('input[name="registrantContactDetails.landlineNumber"]').val(data['registrantContactDetails.landlineNumber']);
	$('input[name="registrantContactDetails.city"]').val(data['registrantContactDetails.city']);
	$('input[name="registrantContactDetails.postCode"]').val(data['registrantContactDetails.postCode']);
	$('textarea[name="registrantContactDetails.address"]').val(data['registrantContactDetails.address']);

	$('input[name="billingContactDetails.ID"]').val(data['billingContactDetails.ID']);
	$('input[name="billingContactDetails.registrantsName"]').val(data['billingContactDetails.registrantsName']);
	$('input[name="billingContactDetails.registrantsLastName"]').val(data['billingContactDetails.registrantsLastName']);
	$('input[name="billingContactDetails.email"]').val(data['billingContactDetails.email']);
	$('input[name="billingContactDetails.phoneNumber"]').val(data['billingContactDetails.phoneNumber']);
	$('input[name="billingContactDetails.landlineNumber"]').val(data['billingContactDetails.landlineNumber']);
	$('input[name="billingContactDetails.faxNumber"]').val(data['billingContactDetails.faxNumber']);
	$('input[name="billingContactDetails.city"]').val(data['billingContactDetails.city']);
	$('input[name="billingContactDetails.postCode"]').val(data['billingContactDetails.postCode']);
	$('textarea[name="billingContactDetails.address"]').val(data['billingContactDetails.address']);

	$('input[name="adminContactDetails.ID"]').val(data['adminContactDetails.ID']);
	$('input[name="adminContactDetails.registrantsName"]').val(data['adminContactDetails.registrantsName']);
	$('input[name="adminContactDetails.registrantsLastName"]').val(data['adminContactDetails.registrantsLastName']);
	$('input[name="adminContactDetails.email"]').val(data['adminContactDetails.email']);
	$('input[name="adminContactDetails.phoneNumber"]').val(data['adminContactDetails.phoneNumber']);
	$('input[name="adminContactDetails.landlineNumber"]').val(data['adminContactDetails.landlineNumber']);
	$('input[name="adminContactDetails.faxNumber"]').val(data['adminContactDetails.faxNumber']);
	$('input[name="adminContactDetails.city"]').val(data['adminContactDetails.city']);
	$('input[name="adminContactDetails.postCode"]').val(data['adminContactDetails.postCode']);
	$('textarea[name="adminContactDetails.address"]').val(data['adminContactDetails.address']);

	$('input[name="technicalContactDetails.ID"]').val(data['technicalContactDetails.ID']);
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



	//Login Details
	$('#loginName').html(data['clientDetailsDTO.loginName']);
}