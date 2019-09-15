<%@page import="lli.constants.LliRequestTypeConstants"%>
<%@page import="vpn.constants.VpnRequestTypeConstants"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="permission.ActionStateFormDTO"%>
<%@page import="java.util.HashMap"%>
<%
HashMap<Integer,ActionStateFormDTO> actionStateFormHashMap = (HashMap<Integer,ActionStateFormDTO>) request.getAttribute("actionFormMap");
%>
<script type="text/javascript">

	var formNameForRequestForFarEndFR = '<%=VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_REQUEST_EXTERNAL_FR_FOR_FAR_END %>';
	var formNameForRequestForNearEndFR = '<%=VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_REQUEST_EXTERNAL_FR_FOR_NEAR_END %>';
	var formNameForRequestForFarEndFRLLI = '<%=LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_REQUEST_EXTERNAL_FR_FOR_FAR_END %>';
	
    $(document).ready(function() {
    
    	$("#fileupload" + formNameForRequestForFarEndFR).submit(function(){
    		
    		if( $(this).find( "input[name='requestToAccountID']" ).val() == 0 ){
    			return false;
    		}
    	});
    	
		$("#fileupload" + formNameForRequestForNearEndFR).submit(function(){
    		
    		if( $(this).find( "input[name='requestToAccountID']" ).val() == 0 ){
    			return false;
    		}
    	});
    	$("#fileupload" + formNameForRequestForFarEndFRLLI).submit(function(){
    		
    		if( $(this).find( "input[name='requestToAccountID']" ).val() == 0 ){
    			return false;
    		}
    	});
		$('input:radio[name=actionListRadio]').change(function() {		    	
				<%
				for (StateActionDTO stateActionDTO : stateActionDTOs) {
				for(int actionTypeID : stateActionDTO.getActionTypeIDs()){%>				
				 var elem = 'link_action_<%=stateActionDTO.getStateID()%>_<%=actionTypeID%>';
					if(this.value == elem)
					{
					    $('#' + elem).show();
					    $('#' + elem).removeClass("hidden");;
					}
					else
					{
						$('#' + elem).hide();
						 $('#' + elem).addClass("hidden")
					}
				<%}}%>
	    });
		
		var pieces = window.location.href.split("/");
		var name = pieces[pieces.length - 1];

		<%-- var name = <%=action%>; --%>
		$('input[type=radio]').attr('checked', false);
		/* 
		$(".formSubmit").submit(function() {
			console.log("submit**********");
		    $(this).find('.actionName').val(name);
		    return true;
		});
		 */
    });
</script>

