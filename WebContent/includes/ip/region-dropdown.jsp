<%@ page import="java.util.List" %>
<%@ page import="util.KeyValuePair" %>
<%@ page import="util.ServiceDAOFactory" %>
<%@ page import="ip.ipRegion.IPRegionService" %>
<%@ page import="ip.MethodReferences" %>
<%@ page import="java.util.stream.Collectors" %>
<%
    String title = (String)request.getParameter("title");
    List<KeyValuePair<Long, String>>list = ServiceDAOFactory.getService(IPRegionService.class)
            .getAllIPRegion()
            .stream()
            .map(t-> MethodReferences.newKeyValue_Long_String.apply(t.getId(), t.getName_en()))
            .collect(Collectors.toList());
    request.setAttribute("regionMap", list
            .stream()
            .collect(Collectors.toMap(KeyValuePair::getKey, KeyValuePair::getValue)
    ));

    String requested = (String) session.getAttribute(title);
    if (requested == null) {
        requested = "";
    }
%>
<div class="form-group">
    <label  class="control-label col-sm-4 "><%=title%></label>
    <div class="col-sm-8" >
        <div class="input-group" style="width: 100%;">
            <select class="form-control pull-right" name="<%=title%>" >
                <option value="" >Select <%=title%></option>
                <%for (KeyValuePair<Long, String> s: list){%>
                <option value="<%=s.getKey()%>" <%=requested.equalsIgnoreCase(String.valueOf(s.getKey()))?" selected": ""%> ><%=s.getValue()%></option>
                <%}%>
            </select>

        </div>
    </div>
</div>

