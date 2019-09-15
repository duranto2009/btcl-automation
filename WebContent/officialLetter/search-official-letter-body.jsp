<%@page import="coLocation.application.CoLocationApplicationDTO"%>
<%@page import="coLocation.application.CoLocationApplicationService"%>
<%@page import="coLocation.demandNote.CoLocationDemandNote"%>
<%@page import="common.ModuleConstants"%>
<%@ page import="common.repository.AllClientRepository" %>
<%@ page import="global.GlobalService" %>
<%@ page import="lli.Application.LLIApplication" %>
<%@ page import="lli.Application.LLIApplicationService" %>
<%@ page import="lli.Application.ReviseClient.ReviseDTO" %>
<%@ page import="lli.Application.ReviseClient.ReviseService" %>
<%@ page import="nix.application.NIXApplication" %>
<%@ page import="nix.application.NIXApplicationService" %>
<%@ page import="nix.demandnote.NIXDemandNote" %>
<%@ page import="officialLetter.*" %>
<%@ page import="sessionmanager.SessionConstants" %>
<%@ page import="util.ServiceDAOFactory" %>
<%@ page import="util.TimeConverter" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="vpn.application.VPNApplicationLink" %>
<%@ page import="vpn.application.VPNApplicationLinkConditionBuilder" %>
<%@ page import="lli.demandNote.LLIOwnerShipChangeDemandNote" %>
<%@ page import="lli.Application.ownership.LLIOwnerShipChangeApplicationConditionBuilder" %>
<%@ page import="exception.NoDataFoundException" %>
<%@ page import="vpn.demandNote.VPNDemandNote" %>
<%@ page import="vpn.application.VPNApplication" %>
<%@ page import="nix.revise.NIXReviseDTO" %>
<%@ page import="nix.revise.NIXReviseService" %>
<%
    String url = "eeSearch/official-letter";
    String navigator = SessionConstants.NAV_EE_OFFICIAL_LETTER;
    List<OfficialLetter> data = (ArrayList<OfficialLetter>) session.getAttribute(SessionConstants.VIEW_EE_OFFICIAL_LETTER);
    data = (data == null ? new ArrayList<>() : data);

    OfficialLetterService officialLetterService = ServiceDAOFactory.getService(OfficialLetterService.class);
    GlobalService globalService = ServiceDAOFactory.getService(GlobalService.class);


%>
<jsp:include page="/includes/nav.jsp" flush="true">
    <jsp:param name="url" value="<%=url%>" />
    <jsp:param name="navigator" value="<%=navigator%>" />
</jsp:include>

<div id="btcl-application">
    <btcl-body title="Search" subtitle="Result">
        <btcl-portlet>
            <table class="table table-hover table-striped table-bordered">
                <thead>
                <tr>
                    <%--<th>Title</th>--%>
                    <th>Application ID</th>
                    <th>Module</th>
                    <th>Type</th>
                    <th>Client</th>
                    <th>Generation Date</th>
                    <th>Link</th>
                    <th>Forward</th>

                </tr>
                </thead>
                <tbody>
                    <%for (OfficialLetter ol: data){
                        String link = "";
                        boolean invalid = false;
                        if(ol.getOfficialLetterType() == OfficialLetterType.ADVICE_NOTE){
                            if(ol.getModuleId()== ModuleConstants.Module_ID_VPN) {

                                try {
                                    List<VPNApplicationLink> list = globalService.getAllObjectListByCondition(
                                            VPNApplicationLink.class, new VPNApplicationLinkConditionBuilder()
                                            .Where()
                                            .adviceNoteIdEquals(ol.getId())
                                            .getCondition()
                                    );
                                    if(list.size() > 1) {
                                        link+=request.getContextPath()+"/pdf/view/advice-note.do?appId="+ol.getApplicationId() + "&module=" + ol.getModuleId();
                                    }else if(list.size() == 1) {
                                        link += request.getContextPath() + "/pdf/view/link/advice-note.do?linkId=" + list.get(0).getId();
                                    }else {
                                        invalid = true;
                                    }
                                } catch (Exception e) {
                                    System.out.println("VPN AN : app -> "+ ol.getApplicationId());
                                    e.printStackTrace();
                                    invalid = true;
                                }
                            }
                            else {
                                link+=request.getContextPath()+"/pdf/view/advice-note.do?appId="+ol.getApplicationId() + "&module=" + ol.getModuleId();
                            }
                        }
                        else if(ol.getOfficialLetterType() == OfficialLetterType.DEMAND_NOTE){
                            try {
                                long demandNote = -1;
                                if(ol.getClassName().equalsIgnoreCase(LLIApplicationService.class.getCanonicalName())){
                                    LLIApplication app = ServiceDAOFactory.getService(LLIApplicationService.class).getLLIApplicationByApplicationID(ol.getApplicationId());
                                    demandNote = app.getDemandNoteID();
                                }else if(ol.getClassName().equalsIgnoreCase(ReviseService.class.getCanonicalName())) {
                                    ReviseDTO reviseDTO = ServiceDAOFactory.getService(ReviseService.class).getappById(ol.getApplicationId());
                                    demandNote = reviseDTO.getDemandNoteID();
                                }else if(ol.getClassName().equalsIgnoreCase(LLIOwnerShipChangeDemandNote.class.getCanonicalName())){
                                    LLIOwnerShipChangeDemandNote lliOwnerShipChangeDemandNote = globalService.getAllObjectListByCondition(
                                            LLIOwnerShipChangeDemandNote.class,
                                            new LLIOwnerShipChangeApplicationConditionBuilder()
                                            .Where()
                                            .idEquals(ol.getApplicationId())
                                            .getCondition()
                                            ).stream()
                                            .findFirst()
                                            .orElseThrow(()->new NoDataFoundException("No LLI Owner Change Application Found with app id " + ol.getApplicationId()));

                                    demandNote = lliOwnerShipChangeDemandNote.getOwnerShipChangeDemandNoteID();
                                }
                                else if(ol.getClassName().equalsIgnoreCase(CoLocationDemandNote.class.getCanonicalName())) {
                                    CoLocationApplicationDTO coLocationApplicationDTO = ServiceDAOFactory.getService(CoLocationApplicationService.class).getColocationApplication(ol.getApplicationId());
                                    demandNote = coLocationApplicationDTO.getDemandNoteID();
                                }else if(ol.getClassName().equalsIgnoreCase(NIXDemandNote.class.getCanonicalName())) {
                                    try {
                                        NIXApplication nixApplication = ServiceDAOFactory.getService(NIXApplicationService.class).getApplicationById(ol.getApplicationId());
                                        demandNote = nixApplication.getDemandNote();
                                    }catch (Exception e) {
                                        try {
                                            NIXReviseDTO nixReviseDTO = ServiceDAOFactory.getService(NIXReviseService.class).getappById(ol.getApplicationId());
                                        }catch (Exception e2 ) {
                                            invalid = true;
                                            System.out.println("NIX DN : app -> " + ol.getApplicationId());
                                            e2.printStackTrace();
                                        }
                                    }


                                }
                                else if(ol.getClassName().equalsIgnoreCase(VPNDemandNote.class.getCanonicalName())){
                                    List<VPNApplicationLink> list = globalService.getAllObjectListByCondition(
                                            VPNApplicationLink.class, new VPNApplicationLinkConditionBuilder()
                                                    .Where()
                                                    .demandNoteOfficialLetterIdEquals(ol.getId())
                                                    .getCondition()
                                    );
                                    if(list.isEmpty()){
                                        invalid = true;
                                    }else {
                                        demandNote = list.get(0).getDemandNoteId();
                                    }
                                }

                                link+=request.getContextPath()+"/pdf/view/demand-note.do?billId="+demandNote;
                            } catch (Exception e) {
                                invalid = true;
                                System.out.println("Global DN: app->" + ol.getApplicationId() + " module ->" + ol.getModuleId());
                                e.printStackTrace();
                            }

                        }
                        else if(ol.getOfficialLetterType() == OfficialLetterType.WORK_ORDER){
                            List<OfficialLetterConcern> list = null;
                            try {
                                list = ServiceDAOFactory.getService(OfficialLetterService.class).getRecipientListByOfficialLetterId(ol.getId());
                            } catch (Exception e) {
                                System.out.println("WO : app-> " + ol.getApplicationId() + " module->"+ol.getModuleId());
                                e.printStackTrace();
                            }
                            OfficialLetterConcern oc = null;
                            if(list != null){
                                oc = list.stream()
                                        .filter(t->t.getReferType()== ReferType.TO
                                                &&
                                                (
                                                        t.getRecipientType()==RecipientType.VENDOR
                                                        || t.getRecipientType() == RecipientType.BTCL_OFFICIAL
                                                )
                                        )
                                        .findFirst()
                                        .orElse(null);
                            }
                            if(oc!= null){
                                link+=request.getContextPath()+"/pdf/view/work-order.do?"+
                                        "appId=" +ol.getApplicationId()
                                        +"&vendorId="+oc.getRecipientId()
                                        + "&module=" + ol.getModuleId();
                            }else {
                                invalid = true;
                                System.out.println("WO : app->"+ol.getApplicationId() + "module ->"+ol.getModuleId());
                            }


                        }
                    %>
                        <tr>
                            <%--<td><%=OfficialLetterType.getName(ol.getOfficialLetterType())%></td>--%>
                            <td><%=ol.getApplicationId()%></td>
                            <td><%=ModuleConstants.ActiveModuleMap.get(ol.getModuleId())%></td>
                            <td><%=OfficialLetterType.getName(ol.getOfficialLetterType())%></td>
                            <td>
                                <a href="${context}lli/client/board.do?id=<%=ol.getClientId()%>" target="_blank">
                                    <%=AllClientRepository.getInstance().getClientByClientID(ol.getClientId()).getLoginName()%>
                                </a>
                            </td>
                            <td><%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(ol.getCreationTime(), "dd/MM/yyyy")%></td>
                            <td>
                                <a href="<%=link%>" target="_blank">
                                    <%="View " + OfficialLetterType.getName(ol.getOfficialLetterType())%>
                                </a>
                                <%--<%=invalid ? "(X) Delete this ROW " : "Valid"%>--%>

                            </td>

                                <td><button type=button class='btn form-control' @click="showForwardModal(<%=ol.getId()%>,<%=ol.getModuleId()%>)" >Forward</button></td>
                                <%--<button class="btn btn-default" @click="showForwardModal(<%=ol.getApplicationId()%>)">Forward</button>--%>

                        </tr>
                    <%}%>

                </tbody>
            </table>
        </btcl-portlet>

    </btcl-body>
    <jsp:include page="/lli/application/new-connection/view/modals/lli-application-new-connection-view-modal-advice-note.jsp"></jsp:include>
</div>
<script>
    /*let appId =null;
    function showForwardModal(application) {
        appId = application;
        $("#forwardAdviceNote").modal({show: true});
    }
*/
    new Vue({
        el:"#btcl-application",
        data: {
            application:{
                userList:[],
                officialLetterId:null,
                moduleId:null,
            },
            comment: '',
        },
        methods:{
          forwardAdviceNote:function () {
              if(this.application.officialLetterId>0){
                  if (this.application.userList.length == 0) {
                      this.errorMessage("At least one assigner should be selected.");
                      return;
                  }
                  var comment = this.comment;
                  var localLoops = {
                      officialLetterId: this.application.officialLetterId,
                      comment: comment,
                      module: this.application.moduleId,
                      userList: this.application.userList,
                  };
                  // debugger;
                  var url1 = "forward-letter";
                  axios.post(context + 'eeSearch/' + url1 + '.do', {'data': JSON.stringify(localLoops)})
                      .then(result => {
                          if (result.data.responseCode == 2) {
                              toastr.error(result.data.msg);
                          } else if (result.data.responseCode == 1) {
                              toastr.success("Your Official letter has been forwarded", "Success");
                              toastr.options.timeOut = 5000;

                              window.location.href = context + 'eeSearch/official-letter.do';
                          }
                          else {
                              toastr.error("This document cant not be forwarded", "Failure");
                          }
                          this.loading = false;
                      })
                      .catch(function (error) {
                      });

              }
              else{
                  toastr.error("This document cant not be forwarded.");
              }
          },
            showForwardModal:function (officialLetterId,module) {
                this.application.officialLetterId = officialLetterId;
                this.application.moduleId = module;
                $("#forwardAdviceNote").modal({show: true});

            }
        }
    });

</script>
