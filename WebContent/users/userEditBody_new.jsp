<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="user.form.UserForm"%>
<%@page import="role.RoleDTO"%>
<%@page import="exchange.ExchangeDTO"%>
<%@page import="exchange.ExchangeRepository"%>
<%@ include file="../includes/checkLogin.jsp" %>

<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page errorPage="../common/failure.jsp" %>

<%@ page import="java.util.ArrayList,java.sql.*,databasemanager.*,
 				 sessionmanager.SessionConstants,
				 user.*,regiontype.*,dslm.*,exchange.*" %>

<html:base/>


<link rel="stylesheet" type="text/css" href="../users/user.css">



<html:form  action="/UpdateUser" method="POST" onsubmit="selectAllOptions();return validate();">

  <table border="0" cellpadding="0" cellspacing="0" class="form1"  width="500">
              <tr>
                <td width="130" style="padding-left: 80px" height="22" align = "left"><!--Username-->User Name</td>
                <td width="302" height="22" style="padding-left: 80px" align = "left">
                <html:text property="username" size="21" /><label style="color:red; vertical-align: middle;" >*</label>
                </td>
              </tr>

              <tr>
                <td width="130"></td>
                <td width="302"><html:errors property="username" /></td>
              </tr>

              <tr>
                <td width="130" style="padding-left: 80px" height="22" align = "left"><!--Password-->Password</td>
                <td width="302" height="22" style="padding-left: 80px" align = "left">
                <html:password property="password" size="21" /><label style="color:red; vertical-align: middle;" >*</label>
                </td>
              </tr>
              
              <tr>
                <td width="130"></td>
                <td width="302"><html:errors property="password" /></td>
              </tr>
              <tr>
                <td width="130" style="padding-left: 80px" height="22" align = "left"><!--Confirm Password-->Confirm Password</td>
                <td width="302" height="22" align = "left" style="padding-left: 80px">
                <html:password property="repassword" size="21" /><label style="color:red; vertical-align: middle;" >*</label>
                </td>
              </tr>
              <tr>
                <td width="130" style="padding-left: 80px" height="22" align = "left"><!--Role-->Role</td>
                <td width="302" height="22" align = "left" style="padding-left: 80px">
                <html:select  size="1" property="roleID">
                  <%
                  role.RoleService roleService = new role.RoleService();
                  ArrayList<RoleDTO> data = roleService.getPermittedRoleListForSpAdmin();

                  if( data!= null)
                  {
                    int size = data.size();
                    for(int i= 0 ; i < size; i++)
                    {
                      role.RoleDTO row = (role.RoleDTO)data.get(i);
                      %>
                      <html:option value="<%=Long.toString(row.getRoleID())%>"><%=row.getRoleName() %></html:option>
                      <%
                    }
                  }
                  %>
	     </html:select>
                </td>
              </tr>

              <tr>
                <td width="130" style="padding-left: 80px" height="22" align = "left">Status</td>
                <td width="302" height="22" align = "left" style="padding-left: 80px"><html:radio property="status" value="<%=Integer.toString(UserDTO.STATUS_ACTIVE)%>" />Active
                  <html:radio property="status" value="<%=Integer.toString(UserDTO.STATUS_BLOCKED)%>" />Blocked
                </td>
              </tr>
				
				
			 
              <tr><td width="400" height="25" colspan="2"></td></tr>
              <tr>
                <td width="130" style="padding-left: 80px" height="22" align = "left"><!--Mail Address-->Mail Address</td>
                <td width="302" height="22" align = "left" style="padding-left: 80px">
                <html:text property="mailAddress" size="21" /><label style="color:red; vertical-align: middle;" >*</label>
                </td>
              </tr>
              
              <tr>
                <td width="130"></td>
                <td width="302"><html:errors property="mailAddress" /></td>
              </tr>
              <tr>
                <td width="130" style="padding-left: 80px" height="22" align = "left">Security Token</td>
                <td width="302" height="22" align = "left" style="padding-left: 80px">
                <html:text property="secToken" size="21"/>
                </td>
              </tr>
              
               <tr>
                <td width="130"></td>
                <td width="302"><html:errors property="secToken" /></td>
              </tr>

 
				<tr>
                        <td width="194" style="padding-left: 80px" height="21" align = "left">Area Code</td>
                        <td width="292" height="22" align = "left" style="padding-left: 80px"><div id = "area" ><select name="areaCode" onchange ="changeExchange(this.value)">                           
                            <%
					          
					          ArrayList regionList = RegionRepository.getInstance().getRegionList();
					          for(int i=0;i<regionList.size();i++)
					        	
					          {
					            RegionDTO regionDTO = (RegionDTO)regionList.get(i);
					            if(regionDTO.getStatus() == RegionConstants.REGION_STATUS_ACTIVE)
					            {
					            %>
                            <option value="<%=regionDTO.getRegionDesc()%>" <%=(regionDTO.getRegionDesc() == 2)?"selected = \"selected\"":""%>><%=regionDTO.getRegionName()%></option>
                            <%} } %>
                          </select></div> </td>
                      </tr>
              
              
              
                <tr >
                      <td width="200" style="padding-left: 80px" height="21" align = "left">Add Exchange</td>
                      <td width="300" style="padding-left: 80px" height="21" align = "left"> <div  id = "exchangeDiv"><select id="exchange"  size="1">                        
                        <%
						  
						  ArrayList<ExchangeDTO> list = (ArrayList<ExchangeDTO>)ExchangeRepository.getInstance().getExchangeList();
						  for(int i = 0; i<list.size();i++)
						  {
							ExchangeDTO exchangeDTO = (ExchangeDTO)list.get(i);
							if(exchangeDTO.getExStatus() == ExchangeConstants.EXCHANGE_ACTIVE && exchangeDTO.getExNWD() == 2)
							{
							
							%>
                        		<option value="<%=exchangeDTO.getExCode()%>"><%=exchangeDTO.getExName()%></option>
	                    <%
							}
						}
					    %>
                        </select></div> </td>
                        </tr>



 <tr>
                        <td width="200" style="padding-left: 80px" height="21" align = "left">
                        <table >
                        <tr><input class="cmd"  type="button" value="Add" name="AddExchange" onClick = "add();">&nbsp;</tr>
                        <tr><input class="cmd"  type="button" value="Remove" name="RemoveExchange" onClick = "remove();">&nbsp;</tr>
                        <tr><input class="cmd"  type="button" value="Add All" name="AddAll" onClick = "addAll();">&nbsp;</tr>
                        <tr><input class="cmd"  type="button" value="Remove All" name="RemoveAll" onClick = "removeAll()">&nbsp;</tr>
                        </table>
                        </td>
                        
                        <td width="300" style="padding-left: 80px" height="21" align = "left">
                        <html:select property = "exchanges" multiple = "true">
                        <logic:iterate name="userForm"  property = "exchanges" id="eList">
                       
                       <bean:define id="ename" scope="request"> <bean:write name="eList"/></bean:define>  
                        <option value = "<bean:write name="eList"/>"><%=ExchangeRepository.getInstance().getExchange(Integer.parseInt(ename)).getExName() %></option>
                        </logic:iterate>
                       </html:select>
                        
                        </td>
                    </tr>     
              <tr>
                <td width="130" height="22" align = "left" style="padding-left: 80px">Login IP</td>

                <td width="302" height="22" align = "left" style="padding-left: 80px">
                  <input type="text" name="LoginIP"  size="21"/>
                  <input type="button" onClick="addLoginIP();" value="Add" name="cmdAdd" class="cmd"/>
                  <input type="button" onClick="removeLoginIP();" value="Remove" name="cmdRemove" class="cmd"/>
                </td>
              </tr>
              <tr>
                <td width="130" align = "left" style="padding-left: 80px"></td>
                <td align = "left" style="padding-left: 80px">
                  <html:select  size="3" property="loginIPs" style="width: 135;" multiple="true">
                  <% String []loginIPList = (String [])session.getAttribute("LoginIPList");
                  session.removeAttribute("LoginIPList");
                  if(loginIPList != null)
                  for(int i=0; i<loginIPList.length;i++)
                  {%>
                  <html:option value="<%=loginIPList[i]%>"><%=loginIPList[i]%></html:option>
                  <%}%>
                  </html:select>
                </td>
              </tr>

              <tr><td width="432" height="40" colspan="2" align = "left">&nbsp;</td></tr>

              <tr>
                <td width="432" height="21" colspan="2" align="left" style="padding-left: 80px"><h4>Additional Information</h4></td>
              </tr>

              <tr>
                <td width="130" style="padding-left: 80px" height="22" align = "left">Full Name</td>
                <td width="302" height="22" align = "left" style="padding-left: 80px">
                  <html:text property="fullName" size="21" /><label style="color:red; vertical-align: middle;" >*</label>
                </td>
              </tr>
              
               <tr>
                <td width="130"></td>
                <td width="302"><html:errors property="fullName" /></td>
              </tr>
              <tr>
                <td width="130" style="padding-left: 80px" height="22" align = "left">Designation</td>
                <td width="302" height="22" align = "left" style="padding-left: 80px">
                  <html:text property="designation" size="21" />
                </td>
              </tr>
              
                <tr>
                <td width="130"></td>
                <td width="302"><html:errors property="designation" /></td>
              </tr>

              <tr>
                <td width="130" style="padding-left: 80px" height="22" align = "left">Address</td>
                <td width="302" height="22" align = "left" style="padding-left: 80px">
                  <html:textarea  property="address" cols="23" rows="4" />
                </td>
              </tr>
                <tr>
                <td width="130"></td>
                <td width="302"><html:errors property="address" /></td>
              </tr>
              
              <tr>
                <td width="130" style="padding-left: 80px" height="22" align = "left">Phone No</td>
                <td width="302" height="22" align = "left" style="padding-left: 80px">
                  <html:text property="phoneNo" size="21" /><label style="color:red; vertical-align: middle;" >*</label>
                </td>
              </tr>
              
               <tr>
                <td width="130"></td>
                <td width="302"><html:errors property="phoneNo" /></td>
              </tr>
              
              <%if(((UserForm)(request.getAttribute("userForm"))).getRoleID() == UserDTO.ROLE_DATA_ENTRY){ %>
              <tr>
                <td width="130" style="padding-left: 80px" height="22" align = "left">Balance</td>
                <td width="302" height="22" align = "left" style="padding-left: 80px">
                  <bean:write name="userForm" property="balance"/>
                </td>
              </tr>
              <%} %>

              <tr>
                <td width="130" style="padding-left: 80px" height="22" align = "left">Additional Info</td>
                <td width="302" height="22" align = "left" style="padding-left: 80px">
                  <html:textarea  property="additionalInfo" cols="23" rows="4"/>
                </td>
              </tr>
              
              <tr>
                <td width="130"></td>
                <td width="302"><html:errors property="additionalInfo" /></td>
              </tr>
              <tr>
                <td width="432" height="40" colspan="2">&nbsp;</td>
              </tr>

              <tr>
                <td width="130" height="21" align = "left">&nbsp;</td>
                <td width="302" height="21" align = "left"><html:hidden property="userID" />
                &nbsp;</td>
              </tr>
              <tr>
                <td width="130" height="24" align = "left"></td>
                <td width="302" height="24" align = "left">
                <input class="cmd"  type="reset" value="Reset" name="B1">
		<%
                int permission =loginDTO.getMenuPermission(login.PermissionConstants.MANAGE_USERS_SEARCH);
                if( permission==3||permission ==2){%>
                  &nbsp;<input class="cmd" type="submit" value="Update" name="B2">
                    <%}%>
                </td>
              </tr>
            </table>
            <br>
</html:form>
<script language="JavaScript" src="../scripts/util.js"></script>
<script language="JavaScript" src="../users/user.js"></script>