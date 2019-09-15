<%@page import="common.EntityTypeConstant"%>
<%@page import="common.ModuleConstants"%>
<%@page import="common.RegistrantTypeConstants"%>
<%--<%@page import="domain.DomainNameDTO"%>--%>
<%@page import="vpn.client.ClientForm"%>
<%@page import="vpn.constants.EndPointConstants"%>
<script type="text/javascript">
var CONFIG = (function() {
    var constants = {
    	//domainTldConstants
        <%--domain:{ --%>
        	<%--&lt;%&ndash;bd : <%=DomainNameDTO.BD_EXT%>,&ndash;%&gt;--%>
        	<%--&lt;%&ndash;bangla : <%=DomainNameDTO.BANGLA_EXT%>&ndash;%&gt;--%>
        <%--},--%>
        
        //clientTypeConstants
        clientType: 
        {
        	ind :  <%=ClientForm.CLIENT_TYPE_INDIVIDUAL%>, 
        	com : <%=ClientForm.CLIENT_TYPE_COMPANY%>
        },
        entityType: 
        {
        	link :  <%=EntityTypeConstant.VPN_LINK%>, 
        	connection: <%=EntityTypeConstant.LLI_LINK%>
        },
        
        
        //moduleConstants
        module:
       	{
       		domain: <%=ModuleConstants.Module_ID_DOMAIN%>,
       		webhosting: <%=ModuleConstants.Module_ID_WEBHOSTING%>,
       		ipaddress: <%=ModuleConstants.Module_ID_IPADDRESS%>,
       		colocation: <%=ModuleConstants.Module_ID_COLOCATION%>,
       		iig: <%=ModuleConstants.Module_ID_IIG%>,
       		vpn: <%=ModuleConstants.Module_ID_VPN%>,
       		lli: <%=ModuleConstants.Module_ID_LLI%>,
       		adsl: <%=ModuleConstants.Module_ID_ADSL%>,
       		nix: <%=ModuleConstants.Module_ID_NIX%>,
       		dnshosting: <%=ModuleConstants.Module_ID_DNSHOSTING%>
       	},
        
        //endPoints
        endPoint : {
       		near: <%=EndPointConstants.NEAR_END_TYPE%>,
       		far	: <%=EndPointConstants.FAR_END_TYPE%>
       	},
       	
        //endPoints
        domRegType : { 
       		govt: <%=RegistrantTypeConstants.REGISTRANT_TYPE_GOVT_COMMON%>,
       		mil	: <%=RegistrantTypeConstants.REGISTRANT_TYPE_MILITARY%>,
       		foreign	: <%=RegistrantTypeConstants.REGISTRANT_TYPE_FOREIGN%>
       	},
    };

    return {
    	get : function(parent, name){
	 	   if(typeof constants[parent][name]=='undefined' || constants[parent][name]==null || constants[parent][name]==''){
	 		   var err='constant variable ['+parent+']['+name+'] is not found.';
	 		   console.log(err);
	 		   alert(err);
	 		   return ;
	 	  	 }
 	  		return constants[parent][name]; 
 		}
   };
})();
</script>