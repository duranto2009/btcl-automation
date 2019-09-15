       		$(document).ready(function() {
       		   $("#roleAddId").submit(function() {
       		      var roleName = $('input[name=rolename]').val();
       		      if(!$.trim(roleName)) {
       		         toastr.error("Please give a role name");
       		         $('input[name=rolename]').focus();
       		         return false;
       		      }
       		      var roleNameDesc = $('input[name=roleDesc]').val();
       		      if(!$.trim(roleNameDesc)) {
       		         toastr.error("Please give a role description");
       		         $('input[name=roleDesc]').focus();
       		         return false;
       		      }
       		   })
       		})

       		function validateSelection(ob, parentList, childList) {
       		   if(ob.checked == false) {
       		      checkByIds(childList, false);
       		   } else {
       		      checkByIds(parentList, true);
       		   }
       		}

       		function checkByIds(ids, checkValue) {
       		   var splitted = ids.split(";");
       		   if(ids == "") return;
       		   for(var i = 0; i < splitted.length; i++) {
       		      if(splitted[i] != "") {
       		         if(document.getElementById(splitted[i])) {
       		            document.getElementById(splitted[i]).checked = checkValue;
       		            if(checkValue) {
       		               $("#" + splitted[i]).parent().addClass("checked");
       		            } else $("#" + splitted[i]).parent().removeClass("checked");
       		         }
       		      }
       		   }
       		}

       		function checkPermissionType(ob) {
       		   if(ob.checked == false) {
       		      var id = ob.id;
       		      if(document.getElementById(id + "Full")) {
       		         document.getElementById(id + "Full").checked = false;
       		         $("#" + id + "Full").parent().removeClass("checked");
       		      }
       		      if(document.getElementById(id + "Modify")) {
       		         document.getElementById(id + "Modify").checked = false;
       		         $("#" + id + "Modify").parent().removeClass("checked");
       		      }
       		      if(document.getElementById(id + "Read")) {
       		         document.getElementById(id + "Read").checked = false;
       		         $("#" + id + "Read").parent().removeClass("checked");
       		      }
       		   } else {
       		      id = ob.id;
       		      if(document.getElementById(id + "Read")) {
       		         document.getElementById(id + "Read").checked = true;
       		         $("#" + id + "Read").parent().addClass("checked");
       		      } else {
       		         alert("can not find permission type Read");
       		      }
       		      if(document.getElementById(id + "Modify")) {
       		         document.getElementById(id + "Modify").checked = false;
       		      }
       		      if(document.getElementById(id + "Full")) {
       		         document.getElementById(id + "Full").checked = false;
       		      }
       		   }
       		}

       		function uncheckOther(objID, typeStr, parentList) {
       		   if(document.getElementById(objID + typeStr).checked == true) {
       		      if(typeStr != "Full") {
       		         if(document.getElementById(objID + "Full")) {
       		            document.getElementById(objID + "Full").checked = false;
       		            $("#" + objID + "Full").parent().removeClass("checked");
       		         }
       		      }
       		      if(typeStr != "Modify") {
       		         if(document.getElementById(objID + "Modify")) {
       		            document.getElementById(objID + "Modify").checked = false;
       		            $("#" + objID + "Modify").parent().removeClass("checked");
       		         }
       		      }
       		      if(typeStr != "Read") {
       		         if(document.getElementById(objID + "Read")) {
       		            document.getElementById(objID + "Read").checked = false;
       		            $("#" + objID + "Read").parent().removeClass("checked");
       		         }
       		      }
       		      checkByIds(parentList, true);
       		   } else {
       		      document.getElementById(objID).checked = false;
       		   }
       		}