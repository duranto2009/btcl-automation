<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>

<%@ page import="util.RecordNavigator" %>

<%
	String url = request.getParameter("url");
	String navigator = request.getParameter("navigator");
	String pageno = "";

	RecordNavigator rn = (RecordNavigator)session.getAttribute(navigator);
	pageno = ( rn == null ) ? "1" : "" + rn.getCurrentPageNo();

	String action = "/" + url;
	String link = "../" + url + ".do";
	String searchFieldInfo[][] = rn.getSearchFieldInfo();

%>

<%
	if(searchFieldInfo != null && searchFieldInfo.length > 0)
	{
%>
<center>
<!-- search control -->
<html:form action = "<%=action %>" method = "POST"  >
<table cellspacing="1" style="border-collapse: collapse" bordercolor="#111111" cellpadding="0">
<tr>
	<td bgcolor="#DCDCED" style="font-family: Verdana; font-size: 10pt; font-weight: bold; padding-left: 5; padding-right: 2; padding-top: 2; padding-bottom: 2" >Search</td>
    <td bgcolor="#DCDCED" style="padding: 2" >&nbsp;</td>
</tr>
	<% for(int i = 0; i < searchFieldInfo.length;i++) { %>
	<% String jspfile = searchFieldInfo[i][0] ;
	   int ipos = jspfile.indexOf(".jsp");
       int jpos = jspfile.length()-4; 
	   if ((ipos!=-1) && ( ipos == jpos) ) { jspfile = "other/" + jspfile ;%> 
	 
	  <jsp:include page="<%=jspfile%>" flush="true"/>
	 
	  <%continue ;} %>
<tr>
   	<td bgcolor="#EFEFEF" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3" >
   		<%=searchFieldInfo[i][0]%>&nbsp;
   	</td>
    <td bgcolor="#EFEFEF">
    	<input type="text" name="<%=searchFieldInfo[i][1]%>" size="25"
          <%
          String value = (String)session.getAttribute(searchFieldInfo[i][1]);
          if( value != null){%>
          value = "<%=value%>"
          <%}

          %>


          >
    </td>
</tr>
	<%}%>

<tr>
   	<td bgcolor="#EFEFEF" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3" >
   		Records Per Page
   	</td>
    <td bgcolor="#EFEFEF">
    	<input type="text" name=<%=sessionmanager.SessionConstants.RECORDS_PER_PAGE%> size="25" value="<%=rn.getPageSize()%>">
    </td>
</tr>

<tr>
	<td bgcolor="#EFEFEF" style="font-family: Verdana; font-size: 8pt; font-weight: bold">&nbsp;</td>
    <td bgcolor="#EFEFEF" align="right" >
      	<html:hidden property="search" value="yes" />
      	<input type="reset" value="Reset" >
        <input type="submit" value="Search" >
	</td>
</tr>
</table>
</html:form>
<!-- search control-->
</center>
<%
	}
%>

<center>
<!-- navigation control-->
        <html:form action = "<%=action %>" method = "POST" >
<!--
		<div style="margin:0;padding:1;border:1px solid rgb(127,157,185); width:245; height:28;background-color:#EBF2FE">
-->
		<table border="0"  style="border-collapse: collapse; font-family:Verdana; font-size:8pt" bordercolor="#111111" cellpadding="0" cellspacing="0"   >
                <tr>
                  <td >
                  	<a href="<%=link%>?id=first" >
                  	<img border="0" src="../images/first.gif" alt="Move First" width="24" height="24"  ></a>

                  	<a href="<%=link%>?id=previous" >
                  	<img border="0" src="../images/prev.gif" alt="Move Previous" width="24" height="24"  ></a>

                  	<a href="<%=link%>?id=next" >
                  	<img border="0" src="../images/next.gif" alt="Move Next" width="24" height="24"></a>

                  	<a href="<%=link%>?id=last" >
                  	<img border="0" src="../images/last.gif" alt="Move Last" width="24" height="24"></a>
				  </td>

				  <td>
					&nbsp;Page&nbsp;
		  	     </td>

				  <td>
                  	<input type="text" style="text-align:right;font-family:verdana;color:blue;font-weight:bold;"  name="pageno" value = '<%= pageno %>' size="2"  >&nbsp;
				  </td>

                  <td>
                  of&nbsp;<%= rn.getTotalPages()%>&nbsp;
	              </td>

                  <td>
                   <html:hidden property="go" value="yes" />
                   <input type="submit" value=" Go " >
                  </td>
                </tr>
        </table>
<!--
        </div>
-->
		</html:form>
<!-- navigation control-->
</center>
