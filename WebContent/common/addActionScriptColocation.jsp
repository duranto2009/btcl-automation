<%@page import="coLocation.ColocationRequestTypeConstants"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="permission.ActionStateFormDTO"%>
<%@page import="java.util.HashMap"%>

<%
HashMap<Integer,ActionStateFormDTO> actionStateFormHashMap = (HashMap<Integer,ActionStateFormDTO>) request.getAttribute("actionFormMap");
%>
<script type="text/javascript">

	
    $(document).ready(function() {
    
    	
    	$(".requestToAccountID_select").on( "change", function(){
    		
    		var requestedToID = $(this).val(); 
    		$(this).closest( "form" ).find( "input[name='requestToAccountID']" ).val( requestedToID );
    		console.log( $(this).closest( "form" ).find( "input[name='requestToAccountID']" ) );
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
		$(".formSubmit").submit(function() {
			console.log("submit**********");
		    $(this).find('.actionName').val(name);
		    return true;
		});
    });
</script>