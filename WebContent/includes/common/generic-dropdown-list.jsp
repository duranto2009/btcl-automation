<%@ page import="java.util.List" %>
<%@ page import="util.KeyValuePair" %>
<%
    String title = request.getParameter("title");
//    boolean isFromHyperlink = Boolean.valueOf(request.getParameter("isFromHyperlink"));
    List<KeyValuePair> list = (List<KeyValuePair>)request.getAttribute("dropdownList");
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
                <option value="" <%if (requested.equals("") ){%> selected <%}%>>Select <%=title%></option>
                <%for (KeyValuePair s: list){%>
                    <option value="<%=s.getKey()%>" <%=requested.equalsIgnoreCase(String.valueOf(s.getKey()))?" selected": ""%> ><%=s.getValue()%></option>
                <%}%>
            </select>

        </div>
    </div>
</div>

