<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%@ page import="util.RecordNavigator"%>
<%
	System.out.println("Inside nav.jsp");
	String url = request.getParameter("url");
	String navigator = request.getParameter("navigator");
	String pageno = "";

	RecordNavigator rn = (RecordNavigator)session.getAttribute(navigator);
	pageno = ( rn == null ) ? "1" : "" + rn.getCurrentPageNo();

	System.out.println("rn " + rn);
	
	String action = "/" + url;
	String link = "../" + url + ".do";
	String searchFieldInfo[][] = rn.getSearchFieldInfo();
	String totalPage = "1";
	if(rn != null)
		totalPage = rn.getTotalPages() + "";

%>



		
				<!-- Pagination -->
		<div class="col-md-offset-4 col-md-4 col-sm-offset-4 col-sm-4 table-pagination" style="margin-top:10px">	
			<center>		
				<html:form action="<%=action %>" method="POST">
				  <table >
					<tbody>
						<tr>
						  <td id="td_arrow">
							<a href="<%=link%>?id=first" style="margin:0 2px"><i class="fa fa-backward fa-2x"></i></a>
							<a href="<%=link%>?id=previous" style="margin:0 2px"><i class="fa fa-caret-left fa-2x"></i></a>
							<a href="<%=link%>?id=next" style="margin:0 2px"><i class="fa fa-caret-right fa-2x"></i></a>
							<a href="<%=link%>?id=last" style="margin:0 2px"><i class="fa fa-forward fa-2x"></i></a>
						  </td>
						  <td>
							&nbsp;Page&nbsp;
						  </td>
						  <td>
							<input type="text" size="2" value="<%=rn.getCurrentPageNo() %>" name="pageno" style="text-align:right;font-family:verdana;color:blue;font-weight:bold;">&nbsp;
						  </td>
						  <td>
							of&nbsp;<%=totalPage%>&nbsp;
						  </td>
						  <td>
							<input type="hidden" value="yes" name="go">
							<input type="submit" value="Go" class="btn btn-primary">
						  </td>
						</tr>
					</tbody>
				  </table>
				</html:form>
			</center>	
		</div><!-- /.Pagination -->