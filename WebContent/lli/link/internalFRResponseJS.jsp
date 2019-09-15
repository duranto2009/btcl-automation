<%@page import="lli.constants.LliRequestTypeConstants"%>
<%@page import="common.CategoryConstants"%>
<script type="text/javascript">
	var vlanCategoryID=<%=CategoryConstants.CATEGORY_ID_VLAN%>;
	var routerCategoryID=<%=CategoryConstants.CATEGORY_ID_ROUTER%>;
	
	$(document).ready(function() {
		$("input[name=bandwidthAvailablity]").click(function(){
			if($(this).val()=='0'){
				$('#bandWidthComment').show();
			}else{
				$('#bandWidthComment').hide();
			}
		})
		
		$("select[name=fePortType]").change(function(){
			if($(this).val()=='0'){
				$('#fePortTypeComment').show();
				$('#fePortItemID').hide();
			}else{
				$('#fePortTypeComment').hide();
				$('#fePortItemID').show();
				loadPortItems($("input[name=feRouterID]").val(), $(this).val(), "#fePortItemID");
			}
		})
		
		$("#neRemoveAdditionalVlan").click(function(){
			$('#neAdditionalVlanIDsSelect option:selected').each(function() {
				$(this).remove();
			});
			$("input[name=neAdditionalVlanIDs]").val("");
			return false;
		})
		
		$("#feRemoveAdditionalVlan").click(function(){
			$('#feAdditionalVlanIDsSelect option:selected').each(function() {
				$(this).remove();
			});
			$("input[name=feAdditionalVlanIDs]").val("");
			return false;
		})
		
		$('#fileupload #updateBtn').click(function(e) {
			bootbox.confirm("Are you sure to Submit? After submission it can not be modified.", function(result) {
                   if (result) {
                	   var neAdditionIDs = [];
                	   $("#neAdditionalVlanIDsSelect > option").each(function(){
                		   neAdditionIDs.push(this.value);
                	   });
                	   
                	   var feAdditionIDs = [];
                	   $("#feAdditionalVlanIDsSelect > option").each(function(){
                		   feAdditionIDs.push(this.value);
                	   });
                	   
                	   $("input[name=neAdditionalVlanIDs]").val(neAdditionIDs.join(", "));
                	   $("input[name=feAdditionalVlanIDs]").val(feAdditionIDs.join(", "));
                	   
                       $("#fileupload").submit();
                   }
               });
		 	return false;
       	});
		
		function loadPortItems(parentItemID, portType, portContainer) {
			var url= '../../AutoInventoryItem.do';
	        var data= {
	            'categoryID': <%=CategoryConstants.CATEGORY_ID_PORT%>,
	            'parentItemID': parentItemID ,
	            'attributeName' : "Port Type",
	            'attributeValue': portType,
	            <%if(requestTypeID == LliRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR || requestTypeID == LliRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR){ %>
	            'lliLinkID' : <%=lliLinkDTO.getID()%>
	            <%}%>
	        };
			ajax(url, data, function(data){
		  		console.log(data);
            	var ports = '<option  selected disabled>---Select Port---</option>';
		        if(data.length > 0) {
	               $.each(data, function(i, port) {
	            	   ports += "<option value='" + port.ID + "'>" + port.name + "</option>";
	               })
	            } else {
	            	ports = '<option  selected disabled>---No Port Is Available---</option>';
	            }
		        $(portContainer).find('.portItem').html(ports);
			  }, "GET", [$(this)]);
		   }
			
		/***load mandatory vlan***/
		
		var vlanAutocomplete;
		$(".vlanAutocomplete").each(function() {
		      $(this).autocomplete({
		         source: function(request, response) {
		        	 vlanAutocomplete = this.element;
		        	 
		        	 //Mandatory Additional Exclusiveness
					if(vlanAutocomplete.hasClass('mandatoryVlan')){
						$("#feMandatoryVlanID").val("");
		        	}else{
	        			if($("#feMandatoryVlanID").val() == undefined || $("#feMandatoryVlanID").val() == ""){
	        				 toastr.error("Select Mandatory VLAN First");
	        			 }
		        	}
		        	 
		            var data={};
		            data['parentItemID'] =  $('input[name=feRouterID]').val();
		            data['categoryID'] = vlanCategoryID;  // for mandatory vlan
		            var url = '../../AutoInventoryItem.do?partialName=' + request.term;
		            callAjax(url, data, response, "GET")
		         },
		         minLength: 1,
		         select: function(e, ui) {
	        		 if(vlanAutocomplete.hasClass('mandatoryVlan')){
	        			var feAdditionalVlanValues = [];
	        			var isAlreadyUsedInAdditional = false;
        	    		$('select[name=feAdditionalVlanIDs] option').each(function(){
        	    			feAdditionalVlanValues.push($(this).val());
        	    		});
        				$.each(feAdditionalVlanValues, function(index, feAdditionalVlanID){
        					if(feAdditionalVlanID == ui.item.ID){
        						isAlreadyUsedInAdditional = true;
        					}
        				});
        				if(isAlreadyUsedInAdditional){
        					toastr.error("Already used as an Additional VLAN");
        				}else{
        					$("#feMandatoryVlanIdText").val(ui.item.name);
		        			 $("#feMandatoryVlanID").val(ui.item.ID);
        				}
	        		 }else{
	        			// d mandatory additional exclusiveness
	        			 if($("#feMandatoryVlanID").val() != undefined  && $("#feMandatoryVlanID").val() != ""){
	        				 if($("#feMandatoryVlanID").val() != ui.item.ID){
	        					 var option="<option value='"+ui.item.ID+"''>"+ui.item.name+"</option>";		        			 
			        			 $("#feAdditionalVlanIDsSelect").append(option);
			        			 vlanAutocomplete.val("");
	        				 }else{
	        					 toastr.error("Already used as the Mandatory VLAN");
	        				 }
	        			 }else{
	        				 toastr.error("Select Mandatory VLAN First");
	        			 }
	        		 }
		        	return false;
		         },
		      }).autocomplete("instance")._renderItem = function(ul, item) {
		         console.log(item);
		         return $("<li>").append("<a>" + item.name + "</a>").appendTo(ul);
		      }
		   });
		
		
		   var currentAutoComplete;
		   var categoryObj;
		   var parentItemObj;
		   var index;
		   $(".category-item").each(function() {
		      $(this).autocomplete({
		         source: function(request, response) {
		            currentAutoComplete = this.element;
		            categoryObj = this.element.next(".category-id");
		           	index=$(this.element).closest('td').index();
		            parentItemObj = $(this.element).closest("tr").prev("tr").find('td').eq(index-1).find(".category-item");;
		          	//var tdObj=$(this.element).closest("tr").prev("tr").find('td').eq(index-1);
		          
		            var map = {};
		            if(categoryObj.val()==routerCategoryID){// start from router
		            	map['parentItemID'] = $('input[name=fePopID]').val();
		            }else if(categoryObj.val()==<%=popCategoryId%>){// start from router
		            	map['isParentNeeded'] = "false";
		            }else{
		            	var tempVal=parentItemObj.next().next().val();
		            	map['parentItemID'] = tempVal; 
		            }
		            
		            map['categoryID'] = categoryObj.val();
		            var url = '../../AutoInventoryItem.do?partialName=' + request.term;
		            ajax(url, map, response, "GET", [$(this)]);
		            currentAutoComplete.closest("tr").nextAll("tr").find("td").find('.category-item').val('');
		            currentAutoComplete.closest("tr").nextAll("tr").find('td').find(".item-id").val("");
		            currentAutoComplete.closest("tr").nextAll("tr").find('td').find("select").val("0");
		         },
		         minLength: 1,
		         select: function(e, ui) {
		            currentAutoComplete.attr('data-category-item-id', ui.item.ID);
		            currentAutoComplete.val(ui.item.name);
		            currentAutoComplete.next().next().val(ui.item.ID);
		            
		            toastr.success( ui.item.name + "  is selected");
		            currentAutoComplete.closest("tr").nextAll("tr").find('td').eq(index-1).find(".category-item").val("");
		            currentAutoComplete.closest("tr").nextAll("tr").find('td').eq(index-1).find(".category-item").find(".category-item").removeAttr('data-category-item-id');
		            return false;
		         },
		      }).autocomplete("instance")._renderItem = function(ul, item) {
		         return $("<li>").append("<a>" + item.name + "</a>").appendTo(ul);
		      }
		   });
		   
		   
		   
	    <%if(lliFRResponseInternalDTO != null && lliFRResponseInternalDTO.isBandWidthIsAvailable()){%>
			$('#bandWidthComment').hide();
		<%}%>
	})
</script>
