<%@ page
	import="java.util.ArrayList,java.sql.*,databasemanager.*,
 				 sessionmanager.SessionConstants,
				 user.*,role.*,regiontype.*,dslm.*,exchange.*"%>
<%@ page language="java"%>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic"%>


<style type="text/css">
.ui-menu  {
	border: 1px solid #999;
	    background: #f5f5f5;
}
</style>
<%
String context = "../../.."  + request.getContextPath() + "/";
%>

<script language="JavaScript" src="../scripts/util.js"></script>

<script language="JavaScript">


if (!String.prototype.trim) {
  	 String.prototype.trim = function() {
  	  return this.replace(/^\s+|\s+$/g,'');
  	 }
  }

   function GetXmlHttpObject()
   {
      var xmlHttp=null;
      try
      {
         xmlHttp=new XMLHttpRequest();
      }
      catch (e)
      {
         try
         {
            xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
         }
         catch (e)
         {
            xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
         }
      }
      return xmlHttp;
   }	
     


      function init()
      {
         var form = document.getElementById("addUserForm");
      }

	function validate()
	{
		selectAll();
		
		var f = document.forms[0];

		var ob = f.username;
		if( isEmpty(ob.value))
		{
			alert("Please enter username");
			ob.value = "";
			ob.focus();
			return false;
		}
		ob = f.password;
		if( isEmpty(ob.value))
		{
			alert("Please enter your password");
			ob.value = "";
			ob.focus();
			return false;
		}

		ob = f.repassword;
		if( isEmpty(ob.value))
		{
			alert("Please re-enter your password");
			ob.value = "";
			ob.focus();
			return false;
		}
		//match both password
		if( f.password.value != f.repassword.value)
		{
			alert("Two passwords do not match!!");
			f.repassword.focus();
			return false;
		}
		return true;
	}

      function addLoginIP()
      {
        var form = document.getElementById("addUserForm");
        var loginIP = form.LoginIP;
        val = form.LoginIP.value;
        ob = form.LoginIP;
    
        if(isEmpty(val))
        {
          alert("Please enter a Login IP");
          form.LoginIP.focus();
          return false;
        }


        valid = 1;
        dotCount = 0;
        ip = -1;
        for( i = 0; i < ob.value.length ; i++)
        {
          if(ob.value.charAt(i)=='.')
          {
            dotCount++;
            if( ip > 255 || ip < 0 )
            {
              valid = 0;
            }
            ip = -1;
          }
          else if (ob.value.charAt(i)>='0' && ob.value.charAt(i)<='9')
          {
            if( ip == -1)
            ip =0;
            ip = ip * 10 + parseInt(ob.value.charAt(i));
          }
          else
          {
            valid = 0;
          }
        }
        if( dotCount != 3|| ip >255 || ip < 0)
        {
          valid = 0;
        }
        if( valid == 0)
        {
          alert("Please enter valid IP address");
          ob.value = "";
          ob.focus();
          return false;
        }

        for(i=0;i<form.loginIPs.length;i++)
        {
          if( form.loginIPs[i].text == val)
          {
            alert("IP  "+val +" already added");
            return;
          }
        }

        form.loginIPs.add(new Option(val));
        form.LoginIP.value='';
        return true;
      }

      function removeLoginIP()
      {
         var form = document.getElementById("addUserForm");
        index = form.loginIPs.selectedIndex;
        if(index != -1)
        {
          form.loginIPs.options.remove(index);
        }
      }

      function selectAllOptions()
      {
        var len = document.forms[0].loginIPs.length;

        for( i = 0; i < len ; i++ )
        {
          document.forms[0].loginIPs.options[i].selected = true;
        }

        return true;
      }
      
      
      function changeExchange(v)
      {
    	 
    	 
    	  xmlHttp = GetXmlHttpObject();
    	  if(xmlHttp == null)
    	  {
    		  alert("Browser is not supported.");
    		  return false;
    	  } 
    	  var url="../dslm/GetExchange.do?areaCode="+v+"&requester=user";
    	 // alert(url);
    	  xmlHttp.onreadystatechange = ExchangeChanged;
    	  xmlHttp.open("GET",url,false);    	  	
    	  xmlHttp.send(null);    	  	
    	  return true;
      }
      
      function ExchangeChanged() 
      { 
    	 
         if (xmlHttp.readyState==4)
         { 
        	
          	 
           document.getElementById("exchangeDiv").innerHTML=xmlHttp.responseText;
     
         }
         
         
      }
      
      
      function selectAll()
      {    
    	 
    	  for (var i = 0; i< document.getElementsByName("exchanges")[0].options.length; i++)
    	  {
    		  document.getElementsByName("exchanges")[0].options[i].selected = true;
    	  }
    	  
    	 
    	  return true;
     	 
      }
                       
      
      
      
      
      
</script>
<style>
.infoblock
{
    font-size: 21px;
    margin-top: 5px;
    font-weight: 600;
    color: gray;
}
<!--

-->
</style>
<div class="col-md-12">
	<div class="box box-success">






		<div class="box-header with-border">
			<h3 class="box-title">Add User Form</h3>
		</div>


		<!-- start of the form -->
		<div class="box-body ">
		<div class="col-md-offset-3  col-md-6">
			<form id="addUserForm" class="form-horizontal" action="/AddUser" method="POST"
				onsubmit="selectAllOptions();return validate();">



				<%
    String msg = null;
    if( (msg = (String)session.getAttribute(util.ConformationMessage.USER_ADD))!= null)
    {
      session.removeAttribute(util.ConformationMessage.USER_ADD);
      %>
				<div class="form-group ">
				<label>	<%=msg%></label>
				</div>
				<%}%>


				<div class="form-group ">
					<label  class="col-sm-3 control-label">User
						Name</label>

					<div class="col-sm-6 ">
						<input type="text" class="form-control" id="username"
							name="username" placeholder="Give user a name" required>
					</div>
				</div>
				<div class="form-group ">
					<label for="password" class="col-sm-3 control-label">Password</label>

					<div class="col-sm-6">
						<input type="password" class="form-control" id="password"
							name="password" placeholder="Give user a password" required>
					</div>
				</div>
				<div class="form-group ">
					<label for="userName" class="col-sm-3 control-label">Confirm
						Password</label>

					<div class="col-sm-6">
						<input type="password" class="form-control" id="repassword"
							name="repassword" placeholder="Repeat password" required>
					</div>
				</div>


				<div class="form-group ">
					<label class="col-sm-3 control-label">Role </label>
					<div class="col-sm-6">
					<input type="hidden"	id="roleID" name="roleID"	class="search input-group input-sm  form-control" autocomplete="off"/>
					<input type="text"	id="roleName" name="roleName"	class="search input-group input-sm  form-control" autocomplete="off"/>
<%-- 						<select class="form-control" name="roleID"	onchange="changeLook(this.value)">
							<%
                  role.RoleService roleService = new role.RoleService();
                  
				  ArrayList<RoleDTO> data = roleService.getPermittedRoleListForSpAdmin();
                  if( data!= null)
                  {
                    int size = data.size();
                    for(int i= 0 ; i < size; i++)
                    {
                      RoleDTO row = data.get(i);%>
							<option value="<%=Long.toString(row.getRoleID())%>"><%=row.getRoleName() %></option>
							<%
                    }
                  }%>
						</select> --%>
					</div>
				</div>

				<div class="form-group ">
					<label class="col-sm-3 control-label">Status </label>
					<div class="col-sm-8">
						<div class="radio-inline">
							<label><input type="radio"  name="status"
								value="<%=Integer.toString(UserDTO.STATUS_ACTIVE)%>" checked>
								Active</label>
						</div>

						<div class="radio-inline">
							<label> <input type = "radio" name="status"
								value="<%=Integer.toString(UserDTO.STATUS_BLOCKED)%>" >
								Blocked
							</label>
						</div>

						
					</div>

				</div>

				<div class="form-group ">
					<label class="col-sm-3 control-label">Mail Address</label>

					<div class="col-sm-6">
						<input type="text" class="form-control" id="mailAddress"
							name="mailAddress" placeholder="Give user a mailing address"
							required>
					</div>
				</div>




				<div class="form-group ">
					<label class="col-sm-3 control-label">Security Token </label>
					<div class="col-sm-6">
						<input class="form-control" type="text" name="secToken"
							placeholder="security token">
					</div>

				</div>







<%-- 				<div class="form-group hidden">
					<label class="col-sm-3 control-label">Area Code </label>
					<div class="col-sm-6 " id="Area">
						<select class="form-control" name="areaCode"
							onchange="changeExchange(this.value)">
							<%
					          
					          ArrayList regionList = RegionRepository.getInstance().getRegionList();
					          for(int i=0;i<regionList.size();i++)
					        	
					          {
					            RegionDTO regionDTO = (RegionDTO)regionList.get(i);
					            if(regionDTO.getStatus() == RegionConstants.REGION_STATUS_ACTIVE)
					            {
					            %>
							<option value="<%=regionDTO.getRegionDesc()%>"><%=regionDTO.getRegionName()%></option>
							<%} } %>
						</select>
					</div>

				</div> --%>






<%-- 				<div class="form-group">
					<label class="col-sm-3 control-label">Add Exchange </label>
					<div class="col-sm-6 " id="exchangeDiv">
						<select id="exchange" class="form-control">
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
						</select>
					</div>
				</div> --%>


<!-- 				<div class="form-group hidden">
				<div class="col-sm-9">
					<div class=" col-sm-offset-4 col-sm-2">
						<button type="button" name="AddExchange"
							class="btn btn-block btn-primary" onClick="add();">Add</button>
					</div>

					<div class="col-sm-2">
						<button type="button" class="btn btn-block btn-primary "
							name="RemoveExchange" onClick="removeExchange();">Remove</button>
					</div>
					<div class="col-sm-2">
						<button type="button" class="btn btn-block btn-primary "
							name="addAll" onClick="addAllExchange();">Add All</button>
					</div>
					<div class="col-sm-2">
						<button type="button" class="btn btn-block btn-primary "
							name="RemoveAll" onClick="removeAll()">Remove All</button>
					</div> 
					</div>
				</div>

				
				<div class="form-group hidden">
					<label for="lnName" class="col-sm-3 control-label">Exchanges</label>

					<div class="col-sm-6">
						<select class="form-control" name="exchanges">
							<option>-</option>

						</select>
					</div>
				</div> -->


				<div class="form-group">
					<label class="col-sm-3 control-label">Login IP</label>

					<div class="col-sm-6">
						<input type="text" class="form-control" id="LoginIP"
							name="LoginIP" placeholder="Give user a Login IP" required>
					</div>
<!-- 					<div class="  col-sm-2">
						<button type="button" name="cmdAdd"
							class="btn btn-block btn-success" onClick="addLoginIP();">Add</button>
					</div> -->					
										
				</div>
				<div class="form-group">
					<div class="col-sm-3"></div>
					<div class="col-sm-6">	
						<input type="button" onClick="addLoginIP();"	value="Add" name="cmdAdd" class="cmd" style="width: 89px;">
						<input type="button" onClick="removeLoginIP();" value="Remove" name="cmdAdd" class="cmd" style="width: 89px;">
					</div>
				</div>
			



				<div class="form-group ">
					<label class="col-sm-3 control-label">Login IPs</label>

					<div class="col-sm-6">
						<select class="form-control" size="5" name="loginIPs" multiple="true">							
						</select>
					</div>
				</div>




				<div class="form-group ">
					<h3 class="infoBlock">Additional Information</h3>
					<hr>
				</div>
				<div class="form-group ">
					<label class="col-sm-3 control-label">Full Name</label>

					<div class="col-sm-6">
						<input type="text" class="form-control" id="fullName"
							name="fullName" placeholder="Give user's full name" required>
					</div>
				</div>

				<div class="form-group ">
					<label class="col-sm-3 control-label">Designation</label>

					<div class="col-sm-6">
						<input type="text" class="form-control" id="designation"
							name="designation" placeholder="Give user's designation" required>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">Address</label>

					<div class="col-sm-6">
						<input type="text" class="form-control" id="address"
							name="address" placeholder="Give user's address" required>
					</div>
				</div>
				<div class="form-group " >
					<label class="col-sm-3 control-label">Phone No</label>

					<div class="col-sm-6">
						<input type="text" class="form-control" id="phoneNo"
							name="phoneNo" placeholder="Give user's phone number" required>
					</div>
				</div>

				<div class="form-group ">
					<label class="col-sm-3 control-label">Additional Info</label>
						
					<div class="col-sm-6">
						<input type="text" class="form-control" id="additionalInfo"
							name="additionalInfo" placeholder="Give user's phone number">
					</div>
				</div>

				<div class="form-group">
									<div class="text-center">
										
											
										    <input type="reset" value="Reset" class="btn  btn-info">
										    
										    <input type="submit" value="Submit" class="btn btn-success">
									
									</div>
								</div>

				<div class="box-footer ">
				
			</div>

</form>
</div>
			
		

		</div>

	</div>
</div>

<script type="text/javascript">
	$(document).ready(function() {
		
		var cache = {};
		$('#roleName').autocomplete({

			source : function(request, response) {
				var term = request.term;
				if (term in cache) {
					response(cache[term]);
					return;
				}
				$.ajax({
					url : '../roles/roleListAjax.jsp?name=' + request.term,
					data : "",
					dataType : "json",
					type : "POST",
					contentType : "application/json",
					success : function(data) {

						cache[term] = data;
						response(data);
					},
					error : function(response) {

					},
					failure : function(response) {

					}
				});
			},
			minLength : 1,
			select : function(e, ui) {
				 $("#roleID").val(ui.item.m_roleID);
		            $("#roleName").val(ui.item.m_roleName);
		return false;
			},

		}).autocomplete("instance")._renderItem = function(ul, item) {
			console.log(item);
/* 	        return $("<li>")
            .append("<a>" + item.m_roleID + "<br>" + item.m_roleName + "</a>")
            .appendTo(ul); */
			return $("<li>")
            .append("<a>" + item.m_roleName + "</a>")
            .appendTo(ul);
    };


		
/* 		$('#roleID').on('change', function(request, response) {

			var term = $(this).val();

			$.ajax({
				url : '../AutoDistance.do?name=' + term,
				data : "",
				dataType : "json",
				type : "POST",
				contentType : "application/json",
				success : function(data) {

					var els = document.getElementsByName("roleID");
					for (var i = 0; i < els.length; i++) {
						els[i].value = data[i];
					}
				},
				error : function(response) {

				},
				failure : function(response) {

				}
			});
		}); */
	});
</script>