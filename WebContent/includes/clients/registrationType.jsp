<%@page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="util.ServiceDAOFactory" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="client.ClientTypeService" %>
<%@ page import="common.RegistrantTypeConstants" %>
<%@ page import="util.KeyValuePair" %>
<%@ page import="client.classification.RegistrantTypeDTO" %>
<%@ page import="java.util.Collections" %>

<%
    int i=0;
    Map<Long, List<KeyValuePair<Long, RegistrantTypeDTO>>> map;
    Long module;
    List<KeyValuePair<Long, RegistrantTypeDTO>> pairs;
    try {
        module = Long.parseLong(request.getParameter("moduleID"));
        map = ServiceDAOFactory.getService(ClientTypeService.class).getAllRegTypeMappedByRegTypeId();
        pairs = map.getOrDefault( module, Collections.emptyList());
        String requested = (String) session.getAttribute("vclRegType");
        if (requested == null) {
            requested = "";
        }

%>

<div class="form-group">
    <label class="control-label col-md-4 col-sm-4 col-xs-4">Registration Type</label>
    <div class="col-md-8 col-sm-8 col-xs-8">
        <select name="vclRegType" class="form-control border-radius ">
            <option value="" <%if (requested.equals("")) {%> selected <%}%>>Select Registrant Type</option>
            <%
                for (KeyValuePair<Long, RegistrantTypeDTO> pair : pairs) {
                    boolean selected = (pair.getValue().getRegTypeId() + "").equalsIgnoreCase(requested);
            %>
            <option value="<%=pair.getValue().getRegTypeId()%>" <%=selected ? "selected" : ""%>><%=pair.getValue().getName()%>
            </option>
            <%
                }
            %>

        </select>
    </div>
</div>
<%
    } catch (Exception e) {
        e.printStackTrace();
    }


%>
