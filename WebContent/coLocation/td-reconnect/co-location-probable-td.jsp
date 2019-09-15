<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="util.ServiceDAOFactory"%>
<%@page import="lli.client.td.LLIClientTDService"%>
<%@page import="lli.LLIClientService"%>
<%@page import="org.joda.time.JodaTimePermission"%>
<%@page import="lli.client.td.LLIProbableTDClient"%>
<%@page import="util.TimeConverter"%>
<%@page import="common.ClientDTO"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="login.LoginDTO"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@ page import="lli.Application.ReviseClient.ReviseDTO" %>
<%@ page import="lli.connection.LLIConnectionConstants" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="java.util.Collections" %>
<%@ page import="coLocation.application.CoLocationApplicationDTO" %>
<%@ page import="coLocation.CoLocationConstants" %>
<%@ page import="coLocation.td.CoLocationProbableTDDTO" %>
<%
    String url = "co-location/probable-td";
    String context = request.getContextPath();
    String navigator = SessionConstants.NAV_COLOCATION_PROBABLE_TD;
    List<CoLocationProbableTDDTO> data = (ArrayList<CoLocationProbableTDDTO>) session.getAttribute(SessionConstants.VIEW_COLOCATION_PROBABLE_TD);
//	sorting the data
    data = (data == null ? new ArrayList<CoLocationProbableTDDTO>() : data);

%>
<jsp:include page="../../includes/nav.jsp" flush="true">
    <jsp:param name="url" value="<%=url%>" />
    <jsp:param name="navigator" value="<%=navigator%>" />
</jsp:include>

<div id=btcl-application>
    <btcl-body title="Probable TD">
        <table class="table table-hover table-striped table-bordered">
            <thead>
            <tr>
                <th>Connection ID</th>
                <th>Connection Name</th>
                <th>Client Name</th>
                <%--<th>TD Date</th>--%>
                <th>Days Left</th>
                <th>TD</th>
            </tr>
            </thead>
            <tbody>
            <% for(CoLocationProbableTDDTO listItem : data) {
                String clientName = "";
                ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(listItem.getClientID());
                if(clientDTO != null) {
                    clientName = clientDTO.getLoginName();
                }
                boolean isAlreadyTemporarilyDisconneced = ServiceDAOFactory.getService(LLIClientTDService.class).isClientTemporarilyDisconnected(listItem.getClientID());
            %>
            <tr>
                <td><%=listItem.getConnectionID() %></td>
                <td><%=listItem.getConnectionName() %></td>
                <td><%=clientName %></td>
                <%--<td><%=listItem.get() %></td>--%>
                <%--<td><%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(listItem.getDayLeft(), "dd/MM/yyyy") %></td>--%>
                <td>
                    <span style="color: <%=listItem.getClientID()%>;font-weight: bold"><%=listItem.getDayLeft()%></span>
                </td>
                <td><button type=button class='btn form-control' @click=td(<%=listItem.getConnectionID()%>,<%=listItem.getHistoryID()%>)>TD</button></td>
            </tr>
            <%}
            %>
            </tbody>
        </table>
    </btcl-body>
    <div class="modal fade" id="modal" role="dialog" data-keyboard="false" data-backdrop="static">
        <div class="modal-dialog modal-lg" style="width: 50%;">
            <div class="modal-content">
                <div class="modal-body" style="padding-left:10%;padding-top:5%">
                    <div>
                        <div class="form-body">
                            <btcl-field title="Adviced Date">
                                <btcl-datepicker :date.sync="revise.suggestedDate"></btcl-datepicker>
                            </btcl-field>
                                <btcl-input title="Comment" :text.sync="revise.comment"></btcl-input>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn green-haze" v-on:click="submitData" >Submit</button>
                    <button type="button" class="btn btn-danger" data-dismiss="modal" @click="closeModal">Close
                    </button>
                </div>
            </div>

        </div>
    </div>
    {{changeData}}

</div>
<script>
    var vue = new Vue({
        el : '#btcl-application',
        data : {
            context : context,
            revise: {
                suggestedDate: null,
                comment: null,
                connection_id: null,
                history_id: null,
            },
            connection_id: null,
            history_id: null,


        },
        methods : {
            td: function (id, historyId) {
                this.revise.connection_id = id;
                this.revise.history_id = historyId;
                $('#modal').modal({show: true});
            },
            submitData: function () {
                axios.post(this.context + "co-location/probable-td-request.do", {
                    // application: this.revise
                    'application': JSON.stringify(this.revise)
                })
                    .then(function(response){
                        if(response.data.responseCode == 2) {
                            toastr.error(response.data.msg); // application not successful
                        }else {
                            toastr.success(response.data.msg);
                            window.location.href = context + 'co-location/search.do';
                        }
                    })
                    .catch(function(error){
                        console.log(error);
                    });
            },
            closeModal: function(){
                this.revise.comment = '';
            }
        },
        computed: {
            changeData: function(){}
        }
    });
</script>
