var identityTypeIDArray = [];

function showRequiredFileUploadButton(identityTypeList) {
    $(".fileType").hide();
    $.each(identityTypeList, function (index, value) {
        $("#file_" + value.id).css("display", "block").css("color", "red");
    });
}


function showAccountInfo(type) {
    $("#clientCategoryType").val(type);
    clearIdentityList();
    if (type == 1) {
        $("#regIndividualInfo").removeClass('hidden');
        $("#regCompanyInfo").addClass('hidden');

        $("#regCompanyInfo").find("input, select").attr("disabled", true);
        $("#regIndividualInfo").find("input, select").attr("disabled", false);

        $(".individual").addClass("regi");
        $(".company").removeClass("regi");

        generateIndividualIdentityList();

    } else {
        $("#regCompanyInfo").removeClass('hidden');
        $("#regIndividualInfo").addClass('hidden');

        $("#regIndividualInfo").find("input, select").attr("disabled", true);
        $("#regCompanyInfo").find("input, select").attr("disabled", false);

        $(".individual").removeClass("regi");
        $(".company").addClass("regi");
    }

    if (!(($('input[name="accountType"][value="' + type + '"]')).hasClass('checked'))) {
        $('input[name="accountType"][value="' + type + '"]').prop('checked', true);
        $('input[name="accountType"][value="' + type + '"]').parent().addClass('checked');
    }
    if (($('input[name="accountType"]:not([value="' + type + '"])')).parent().hasClass('checked')) {
        $('input[name="accountType"]:not([value="' + type + '"])').prop('checked', false);
        $('input[name="accountType"]:not([value="' + type + '"])').parent().removeClass('checked');
    }
}


function populateIdentityTypeList(data) {

    var identityTypeHTML = "", identityTypeFileUploadHTML = "", numberIterator = 0, fileIterator = 0,
        dependentIdentityGroupArray = [], documentList = [];
    identityTypeIDArray = [];
    $.each(data, function (index, value) {
        if (value.alternativeDocumentIdList.length == 0) {
            if (value.fieldType == 1 || value.fieldType == 3) {
                if (value.name != "Photograph") {
                    if (numberIterator % 2 == 0) {
                        identityTypeHTML += "<div class=row>";
                    }
                    identityTypeHTML += "<div class=col-md-6>"
                        + "<div class=form-group>"
                        + "<label class='control-label col-md-4'>" + value.name + " No." + ((value.isMandatory) ? "<span class=required aria-required=true> * </span>" : "") + "</label>"
                        + "<div class=col-md-8>"
                        + "<input type=text class=form-control name=identityType_" + value.id + " " + ((value.isMandatory) ? "required" : "") + ">"
                        + "</div>"
                        + "</div></div>";
                    if (numberIterator % 2 == 1 || numberIterator == data.length - 1) {
                        identityTypeHTML += "</div>";
                    }
                    numberIterator++;
                }
                if (value.name == "Valid BTRC License") {
                    if (numberIterator % 2 == 0) {
                        identityTypeHTML += "<div class=row>";
                    }
                    identityTypeHTML += "<div class=col-md-6>"
                        + "<div class=form-group>"
                        + "<label class='control-label col-md-4'>" + "Valid BTRC License Date" + "</label>"
                        + "<div class=col-md-8>"
                        + "<input type=date class=form-control name=clientDetailsDTO.btrcLicenseDate" + " " + "value=" + btrcLicenseDate + ">"
                        + "</div>"
                        + "</div></div>";
                    if (numberIterator % 2 == 1 || numberIterator == data.length - 1) {
                        identityTypeHTML += "</div>";
                    }
                    numberIterator++;
                }
            }

            if (value.fieldType == 1 || value.fieldType == 2) {
                identityTypeFileUploadHTML += "<div class='col-md-4 fileType' style='padding:10px'>"
                    + "<div class='btn btn-warning-btcl fileinput-button' style='width:100%;'>"
                    + "<i class='fa fa-upload'></i><span>" + value.name + " Documents</span>"
                    + "<input class=jFile type=file name=" + value.id + " multiple></div>"
                    + "</div>";
                documentList.push(value.name + "");

                if (!billingFirstNameJs) {
                    identityTypeIDArray.push(value);
                } else {
                    if (!map[value.id]) {
                        identityTypeIDArray.push(value);
                    }
                }
            }
        } else {
            var doesIdentityGroupExist = false;
            $.each(dependentIdentityGroupArray, function (j, existingDependentGroup) {
                $.each(existingDependentGroup, function (k, dependentDescriptor) {
                    $.each(dependentDescriptor.alternativeDocumentIdList, function (l, alternativeID) {
                        if (value.id == alternativeID) {
                            doesIdentityGroupExist = true;
                        }
                    });
                });
            });

            if (doesIdentityGroupExist) {
                $.each(dependentIdentityGroupArray, function (j, existingDependentGroup) {
                    var dependentDescriptor = existingDependentGroup[0];
                    $.each(dependentDescriptor.alternativeDocumentIdList, function (k, alternativeID) {
                        if (value.id == alternativeID) {
                            existingDependentGroup.push(value);
                        }
                    });
                });
            } else {
                var newDependentGroup = [];
                newDependentGroup.push(value);
                dependentIdentityGroupArray.push(newDependentGroup);
            }
        }
    });

    $.each(dependentIdentityGroupArray, function (i, value) {
        identityTypeHTML += "<div class=col-md-6><div class=form-group>";
        identityTypeHTML += "<div class='radio-list col-md-4 control-label'>";
        var groupID = "";
        var groupName = "";
        $.each(value, function (j, dependentDescriptor) {
            groupID += "_" + dependentDescriptor.id;
            groupName += "_" + dependentDescriptor.name
        });
        $.each(value, function (j, dependentDescriptor) {
            identityTypeHTML += "<label class=radio-inline><input type=radio class=radio_dependent name=radio_dependent" + groupID + " value=" + dependentDescriptor.id + " required>" + dependentDescriptor.name + "</label>";
        });
        identityTypeHTML += "<span class=required aria-required=true> * </span></div>";

        identityTypeHTML += "<div class=col-md-8><input type=text class=form-control name=identityType_dependent" + groupID + " required readonly></div>"
            + "</div></div>";

        identityTypeFileUploadHTML += "<span class='col-md-4 fileType' style='padding:10px;'>"
            + "<div class='btn btn-warning-btcl fileinput-button' style='width:100%;'>"
            + "<i class='fa fa-upload'></i><span>" + groupName.slice(1).replace('_', '/') + " Documents</span>"
            + "<input class=jFile type=file data-name=file_dependent" + groupID + " multiple></div>"
            + "</span>";
        documentList.push(groupName.slice(1).replace('_', '/') + " Documents");
    });

    $("#identification").html(identityTypeHTML);
    $("#identification-upload").html(identityTypeFileUploadHTML);
    $("#document-list").html("<li>" + documentList.join("</li><li>") + "</li>");
    $(".radio_dependent").on("change", showDependentFileName);

    if (typeof globalVariableToStoreIdentity != "undefined") {
        setIdentityValues();
        globalVariableToStoreIdentity = "";
    }

    showRequiredFileUploadButton(identityTypeIDArray);
    $(".radio_dependent").each(function () {
        $(this).trigger("change");
    });
    if (typeof disableEdit != "undefined") {
        disableEdit();
    }

    $('.star_mark').remove();
    $.each(data, function (index, value) {
        if (value.isMandatory) {
            $('#file_' + value.id).find('span').after("<span class='star_mark' style='color:white'>&nbsp;*</span>");
        }
    });


}

function showDependentFileName(event) {
    if ($("input[name=" + event.target.name + "]:checked").val() != undefined) {
        $(event.target).closest(".form-group").find("input").prop("readonly", false);
    }

    var dependentIdentityGroupRadioArray = event.target.name.split("_");
    $.each(dependentIdentityGroupRadioArray, function (index, value) {
        if (index > 1) {
            $("#file_" + value).css("display", "none");
        }
    });
    $("#file_" + $("input[name=" + event.target.name + "]:checked").val()).css("display", "block");
}

function setFileInputName(event) {
    $("input[data-name=" + event.target.name.replace("radio", "file") + "]").attr("name", event.target.value);
}


function populateIdentityType(event) {
    $("#registrantCategoryContainer").find("input:checkbox:checked").not($(event.target)).each(function () {
        $(this).prop("checked", false);
    });
    $("#registrantCategoryContainer").find("input:checkbox").not($(event.target)).each(function () {
        $(this).parent().removeClass("checked");
    });

    if ($("#registrantCategoryContainer").find("input:checkbox:checked").length > 0) {
        selectedRegistrantCategory = $($("#registrantCategoryContainer").find("input:checkbox:checked")[0]).val();
        ajax(context + "ClientType/GetCompanyIdentityList.do",
            {
                moduleID: moduleID,
                registrantType: $("select[name='clientDetailsDTO.registrantType']").val(),
                registrantCategory: selectedRegistrantCategory
            },
            populateIdentityTypeList, "GET", []);
    } else {
        $("#identification").html("");
        $("#identification-upload").html("");
        toastr.info("Select valid Registrant Category");
    }
}

function populateRegistrantSubCategory(event) {
    $("#registrantCategoryContainer").find("input:checkbox:checked").not($(event.target)).each(function () {
        $(this).prop("checked", false);
    });
    $("#registrantCategoryContainer").find("input:checkbox").not($(event.target)).each(function () {
        $(this).parent().removeClass("checked");
    });

    if ($("#registrantCategoryContainer").find("input:checkbox:checked").length > 0) {
        selectedRegistrantCategory = $($("#registrantCategoryContainer").find("input:checkbox:checked")[0]).val();
        ajax(context + "ClientType/getSubCategoriesUnderACategory.do",
            {
                registrantCategory: selectedRegistrantCategory
            },
            populateRegistrantSubCategoryList, "GET", []);
    } else {
        $("#identification").html("");
        $("#identification-upload").html("");
        toastr.info("Select valid Registrant Category");
    }
}


function populateRegistrantSubCategoryList(data) {
    var registrantSubCategoryHTML = "", selectedRegistrantSubCategory;

    $.each(data, function (index, value) {
        if (index === parseInt(globalVariableToStoreSelectedRegistrantSubCategory, 10) - 1) {
            registrantSubCategoryHTML += "<div class='col-md-4'>" +
                "<label class=checkbox-inline><input disabled type=checkbox name='clientDetailsDTO.regSubCategory' value=" + value.key + " checked>" + value.value + "</label>" +
                "</div>";
        } else {
            registrantSubCategoryHTML += "<div class='col-md-4'>" +
                "<label class=checkbox-inline><input disabled type=checkbox name='clientDetailsDTO.regSubCategory' value=" + value.key + ">" + value.value + "</label>" +
                "</div>";
        }

    });
    $("#registrantSubCategoryContainer").html(registrantSubCategoryHTML);
    $("#registrantSubCategoryContainer").find("input:checkbox").uniform();

    $("input[name='clientDetailsDTO.regSubCategory']").on("change", clearRegSubCategory);

    if (typeof globalVariableToStoreSelectedRegistrantSubCategory != "undefined") {
        setRegistrantSubCategoryValue();
        globalVariableToStoreSelectedRegistrantSubCategory = [];
    }
}

function clearRegSubCategory(event) {
    $("#registrantSubCategoryContainer").find("input:checkbox:checked").not($(event.target)).each(function () {
        $(this).prop("checked", false);
    });
    $("#registrantSubCategoryContainer").find("input:checkbox").not($(event.target)).each(function () {
        $(this).parent().removeClass("checked");
    });
}

//Company Client Registration

function populateRegistrantCategory(data) {
    var registrantCategoryHTML = "", selectedRegistrantCategory;
    $.each(data, function (index, value) {
        registrantCategoryHTML += "<div class='col-md-4'>" +
            "<label class=checkbox-inline><input disabled type=checkbox name='clientDetailsDTO.regiCategories' value=" + value.key + ">" + value.value + "</label>" +
            "</div>";
    });
    $("#registrantCategoryContainer").html(registrantCategoryHTML);
    $("#registrantCategoryContainer").find("input:checkbox").uniform();

    $("input[name='clientDetailsDTO.regiCategories']").on("change", populateIdentityType);
    $("input[name='clientDetailsDTO.regiCategories']").on("change", populateRegistrantSubCategory);

    if (typeof globalVariableToStoreSelectedRegistrantCategory != "undefined") {
        setRegistrantCategoryValues();
        globalVariableToStoreSelectedRegistrantCategory = [];
    }
}

function populateRegistrantType() {
    clearIdentityList();

    if ($("select[name='clientDetailsDTO.registrantType']").val() != 0) {
        ajax(context + "ClientType/GetRegistrantCategory.do", {
            moduleID: moduleID,
            registrantType: $("select[name='clientDetailsDTO.registrantType']").val()
        }, populateRegistrantCategory, "GET", []);
    } else {
        toastr.info("Select valid Registrant Type");

    }
}

$("select[name='clientDetailsDTO.registrantType']").on("change", populateRegistrantType);

//Individual Client Registration
function generateIndividualIdentityList() {
    ajax(context + "ClientType/GetIndividualIdentityList.do", {moduleID: moduleID}, populateIdentityTypeList, "GET", []);
}

function clearIdentityList() {
    $("#registrantCategoryContainer").html("");
    $("#registrantSubCategoryContainer").html("");
    $("#identification").html("");
    $("#identification-upload").html("");
}

function setRegistrantCategoryValues() {
    $.each(globalVariableToStoreSelectedRegistrantCategory, function (index, value) {
        $('input[name="clientDetailsDTO.regiCategories"][value="' + value + '"]').prop("checked", true);
    });
    $("#registrantCategoryContainer").find("input:checkbox").uniform();
    $("input[name='clientDetailsDTO.regiCategories']:checked").trigger("change");
}

function setRegistrantSubCategoryValue() {
    $.each(globalVariableToStoreSelectedRegistrantSubCategory, function (index, value) {
        $('input[name="clientDetailsDTO.regSubCategory"][value="' + value + '"]').prop("checked", true);
    });
    $("#registrantSubCategoryContainer").find("input:checkbox").uniform();
    $("input[name='clientDetailsDTO.regSubCategory']:checked").trigger("change");
}


function setIdentityValues() {
    var dependentIdentityTypeIDList = [], radioElement, dependentIdentityMap = {};
    // 1. Set values to Independent Identity Types
    $.each(globalVariableToStoreIdentity.split(','), function (index, value) {
        if ($("input[name=identityType_" + value.split(":")[0] + "]").length == 0 && value.split(":")[0].length > 0) {
            dependentIdentityTypeIDList.push(value.split(":")[0]);
            dependentIdentityMap[value.split(":")[0]] = value.split(":")[1];
        } else {
            $("input[name=identityType_" + value.split(":")[0] + "]").val(value.split(":")[1]);
        }
    });

    // 2. Set Values to Dependent Identity Types
    $.each(dependentIdentityTypeIDList, function (index, value) {
        // 2.1 Get Radio Element with Dependent Identity
        $("#identification input[type=radio]").each(function (i) {
            if ($(this).attr("name").includes(value + "")) {
                radioElement = $(this);
            }
        });
        // 2.2 Check selected Identity
        $('input[name=' + radioElement.attr("name") + '][value=' + value + ']').prop("checked", true);
        $('input[name=' + radioElement.attr("name").replace("radio", "identityType") + ']').val(dependentIdentityMap[value]);
    });

}

$(document).ready(function () {
    $("input[name='accountType']").change(function () {
        showAccountInfo($("input[name='accountType']:checked").val());
    });

    function setDefaultCountry() {
        var countrySelector = $('select[name="registrantContactDetails.country"]')
        countrySelector.val("BD");
        sortSelectBox(countrySelector);
    };

    setDefaultCountry();
    if (typeof accountType != "undefined") {
        showAccountInfo(accountType);
    } else {
        if ($("input[name='accountType']").length > 1 || $("input[name='accountType']").val() == "1") {
            showAccountInfo(1);
        } else {
            showAccountInfo(2);
        }
    }
});
