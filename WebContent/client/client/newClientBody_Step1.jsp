<%@page import="common.LanguageConstants" %>
<%@page import="common.ObjectPair" %>
<%@page import="java.util.List" %>
<%@page import="common.ModuleConstants" %>
<%@page import="util.ServiceDAOFactory" %>
<%@page import="client.ClientService" %>
<%
    ClientService clientService = ServiceDAOFactory.getService(ClientService.class);
%>


<form class="form-horizontal" action="<%=request.getContextPath()%>/Client/Registration.do" method="GET"
      id="fileupload">
    <h2 class="text-center" style="padding-top:20px;padding-bottom:10px;">BTCL Client Registration</h2>
    <div id="step_1" style="padding-left:40px;padding-right:40px;padding-bottom:30px;">
        <div class="row" style="padding-bottom:15px">
            <div class="form-group ">
                <label class="control-label col-md-2 bold"> Service Type <span class="required"
                                                                               aria-required="true"> * </span></label>
                <div class="col-md-10">
                    <div class="radio-list">
                        <%
                            List<ObjectPair<Integer, String>> activeModuleMap = moduleService.getActiceModuleList();
                            for (ObjectPair<Integer, String> module : activeModuleMap) {%>

                            <%
                            if (module.key == ModuleConstants.Module_ID_IPADDRESS)
                                continue;
                             %>


                        <div class=col-md-3 style="margin-bottom:10px">
                            <label class="radio-inline"><input type="radio" name="moduleID"
                                                               value="<%=module.key%>" <%=(module.key==1)?"checked":"" %>><%=module.value%>
                            </label>
                        </div>
                        <%} %>
                    </div>
                </div>
            </div>
        </div>
        <div class="row" style="padding-bottom:15px">
            <div class="form-group ">
                <label class="control-label col-md-2 bold"> Registrant Type <span class="required" aria-required="true"> * </span></label>
                <div class="col-md-10">
                    <div class="radio-list">
                        <div class=col-md-4>
                            <label class="radio-inline account indAccount">
                                <input type="radio" name="accountType" value="1" autocomplete="off" checked/>Individual
                            </label>
                        </div>
                        <div class=col-md-4>
                            <label class="radio-inline account comAccount">
                                <input type="radio" name="accountType" value="2"
                                       autocomplete="off"/><%=LanguageConstants.COMPANY_TYPE %>
                            </label>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <hr>
        <h4>Terms & Conditions and Privacy Policy</h4>
        <hr>

        <div>
            Please read our terms and conditions
            <a id="domainTerms" class="terms" target="_blank"
               href="<%=request.getContextPath()%>/assets/static/domainTerms.jsp">from here.</a>
            <a id="vpnTerms" style="display: none" class="terms " target="_blank"
               href="<%=request.getContextPath()%>/assets/static/vpnTerms.pdf">from here.</a>
            <a id="lliTerms" style="display: none" class="terms " target="_blank"
               href="<%=request.getContextPath()%>/assets/static/lliTerms.pdf">from here.</a>
            <a id="nixTerms" style="display: none" class="terms " target="_blank"
               href="<%=request.getContextPath()%>/assets/static/nixTerms.pdf">from here.</a>
            <a id="colocationTerms" style="display: none" class="terms " target="_blank"
               href="<%=request.getContextPath()%>/assets/static/colocationTerms.pdf">from here.</a>
        </div>

        <div class="row">
            <div class="col-md-8 text-left" style="padding-top:15px">
                <input type="checkbox" value="check" id="regAgree"/>
                <label for="regAgree">I have read and agree to the Terms and Conditions and Privacy Policy</label>
            </div>
            <div class="col-md-4 text-right">
                <a href="<%=context%>">
                    <button type="button" class="btn btn-cancel-btcl" id="regBack">Back</button>
                </a>
                <button type=submit id='btn-continue' class="btn btn-submit-btcl">Continue</button>
            </div>
        </div>
    </div>

</form>
N
<script>

    $(document).ready(function () {
        $("input[name=moduleID]").click(function () {
            $('.terms').hide();
            $('.account').hide();
            switch ($("input[name=moduleID]:checked").val()) {
                case "<%=ModuleConstants.Module_ID_DOMAIN%>" :
                <%=(clientService.isIndividualClientAccepted(ModuleConstants.Module_ID_DOMAIN)) ? "$('.indAccount').show();" : ""%>
                <%=(clientService.isCompanyClientAccepted(ModuleConstants.Module_ID_DOMAIN)) ? "$('.comAccount').show();" : ""%>
                    $('#domainTerms').show();
                    break;
                case "<%=ModuleConstants.Module_ID_VPN%>" :
                <%=(clientService.isIndividualClientAccepted(ModuleConstants.Module_ID_VPN)) ? "$('.indAccount').show();" : ""%>
                <%=(clientService.isCompanyClientAccepted(ModuleConstants.Module_ID_VPN)) ? "$('.comAccount').show();" : ""%>
                    $('#vpnTerms').show();
                    break;
                case "<%=ModuleConstants.Module_ID_LLI%>" :
                <%=(clientService.isIndividualClientAccepted(ModuleConstants.Module_ID_LLI)) ? "$('.indAccount').show();" : ""%>
                <%=(clientService.isCompanyClientAccepted(ModuleConstants.Module_ID_LLI)) ? "$('.comAccount').show();" : ""%>
                    $('#lliTerms').show();
                    break;
                case "<%=ModuleConstants.Module_ID_NIX%>" :
                <%=(clientService.isIndividualClientAccepted(ModuleConstants.Module_ID_LLI)) ? "$('.indAccount').show();" : ""%>
                <%=(clientService.isCompanyClientAccepted(ModuleConstants.Module_ID_LLI)) ? "$('.comAccount').show();" : ""%>
                    $('#nixTerms').show();
                    break;
                case "<%=ModuleConstants.Module_ID_COLOCATION%>" :
                <%=(clientService.isIndividualClientAccepted(ModuleConstants.Module_ID_LLI)) ? "$('.indAccount').show();" : ""%>
                <%=(clientService.isCompanyClientAccepted(ModuleConstants.Module_ID_LLI)) ? "$('.comAccount').show();" : ""%>
                    $('#colocationTerms').show();
                    break;
            }


            $("input[name='accountType']").attr("checked", false);
            $("input[name='accountType']").parent('span').removeClass('checked');
        });

        $('#regBack').click(function () {
            window.location = context;
        });

        $("#btn-continue").on('click', function (e) {
            e.preventDefault();
            if (typeof $("input[name=accountType]:checked").val() == 'undefined') {
                toastr.error('Please Select Client Type');
                return false;
            }
            if (!$("#regAgree:checked").val()) {
                toastr.error("Please read all the terms and conditions");
                return false;
            }
            $('#fileupload').submit();
            return true;
        });
    });
</script>