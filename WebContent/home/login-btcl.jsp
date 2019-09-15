<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page errorPage="../common/failure.jsp" %>
<!DOCTYPE html>
<%
String context = "../../.."  + request.getContextPath() + "/";
%>
<html lang="en">
    <head><html:base/>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Welcome to BTCL Bangladesh Limited</title>


		<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css" rel="stylesheet" />
		<%@ include file="../skeleton_btcl/head.jsp"%>
<script type="text/javascript">
        
        function forgotPassword()
        {
        	alert("forgotPassword()");
            var f = document.forms[0];
            f.mailPassword.value = 1;
        }
    </script>


<style type="text/css">
@import url(https://fonts.googleapis.com/css?family=Lato:400,300,100);

html > body * {
    outline:none;
}
body{font-family: 'Lato', sans-serif;background: #ffffff; color: #000000;font-size:14px;font-weight: 400;line-height: 1.6;}
a:focus{
    text-decoration: none;
      outline: medium none;
}
.btn:focus, .btn:active:focus, .btn.active:focus {
    outline: none;
    outline-offset: -2px;
}    
    
    
.login-top-bar {
  background: #f7f7f7;
  border-bottom: 3px solid #d7d7d7;
  padding-top: 15px;
  padding-bottom: 15px;
}
.login-company-name img{float: right;}
.login-content{margin-top: 40px;}
.login-content .address p{margin-bottom:0 !important;font-size:13px;color: #737373;}
.login-content .address p:first-child {
  color: #86c833;
  font-size: 20px;
  font-weight: 400;
  margin-bottom: 10px;
}
.vertical-bar {
  border-left: 2px dashed #d7d7d7;
  margin-top: 28px;
  min-height: 330px;
}
.login-form{margin-top: 50px;}
.login-form p {
  color: #aaa;
  font-size: 16px;
  margin-bottom: 15px;
  text-align: right;
}
.login-form .input-group {
  margin-bottom: 30px;
}
.login-form input {
  border:  1px solid #86c833;
  border-radius: 0;
  height:40px;
   box-shadow: none;
   
     background-color: #fff;
    background-image: none
    color: #555;
    display: block;
    font-size: 14px;
    line-height: 1.42857;
    padding: 6px 12px;
    transition: border-color 0.15s ease-in-out 0s, box-shadow 0.15s ease-in-out 0s;
    width: 100%;
}
.login-form .input-group-addon {
  background-color: #017a32;
  border: 1px solid #017a32;
  border-radius: 0;
  color: #ffffff;
  font-size: 25px;
  min-width: 46px;
}
.login-form .btn {
  background: #86c833 none repeat scroll 0 0;
  border-color: #86c833;
  border-radius: 0;
  font-size: 25px;
  height: 50px;
}
.login-form .reset-btn{
  font-family: "Lato",sans-serif;
  background: #fff none repeat scroll 0 0;
  border-color: #fff;
  color: #017a32;
  border-radius: 0;
  font-size: 14px;
}
.login-form ul li {
  display: inline-flex;
}
.login-form ul li:last-child{
  border-left:1px solid #d7d7d7;
  padding-left:15px;
}
.login-form ul li a{
	color: #ff5a00;  
	font-size: 14px;
	font-family: "Lato",sans-serif;
}
.login-footer{border-top:1px solid #d7d7d7;margin-top:40px;}
.login-footer img{margin-top: 10px;float:right;margin-bottom:10px;}

@media(max-width:767px){
.login-form{margin-top: 5px;}
}    
    
    </style>   
    </head>
    <body>
		
		
		<div class="container">
			<div class="col-md-12 login-top-bar">
				<div class="row">
					<div class="col-md-3 col-sm-3">
						<img src="<%=context%>/assets/images/BTCL-Logo.png" title="" alt="" class="img-responsive"/>
					</div>
					<div class="col-md-9 col-sm-9 login-company-name">
						<img src="<%=context%>/assets/images/company-name.png" title="" alt="" class="img-responsive"/>
					</div>
				</div>
			</div>
			
		</div>
		<%if(StringUtils.isNotBlank((String)request.getAttribute("confirmation"))){ 
			String cMessage=(String)request.getAttribute("confirmation");
			
		%>
		<br>
 		<div class="container">
 			<div class="col-md-12">
				<div class="note note-success">
			         <h4 class="block">Success! </h4>
			         <p> <%=cMessage%>  </p>
		     	</div>
	     	</div>
		</div>
		<%} %>
		<div class="container">
			<div class="col-md-12 login-content">
				<div class="row">
					<div class="col-md-5 col-sm-5 hidden-xs">
						<img src="<%=context%>/assets/images/pic_frame.png" title="" alt="" class="img-responsive"/>
						
						<div class="address">
							<p>Address</p>
							<p>Bangladesh Telecommunications Company Ltd. (BTCL)</p>
							<p>Telejogajog Bhaban, 37/E, Eskaton Garden</p>
							<p>Dhaka-1000, Bangladesh.</p>
							<p>TEL-PABX: (880) 2 9320075-6, 9320080-3</p>
						</div>
						
					</div>
					
					<div class="col-md-2 col-sm-2 hidden-xs">
						<div class="col-md-offset-5 col-md-4 col-sm-offset-4 col-sm-4 vertical-bar"></div>
					</div>
					
					<div class="col-md-5 col-sm-5 login-form">
					
						<p >Login to BTCL Automation System</p>						
					<html:form  action="/Login"  method="POST">
							<div class="input-group">
								<span class="input-group-addon"><i class="fa fa-user"></i></span>
								<html:text   property="username" />
							</div>
							
							<div class="input-group">
								<span class="input-group-addon"><i class="fa fa-lock"></i></span>
								<html:password   property="password" />
							</div>
							<div>
							<html:errors property="loginFailure" />
							</div>
							<input class="btn btn-lg btn-primary btn-block" type="submit" value="Login"/>
							
							<div class="pull-right">
								<ul class="list-unstyled list-inline-block">
									<li><a href="<%=context%>addNewClient.jsp">Sign Up</a></li>
									<li><input class="btn btn-lg btn-primary reset-btn" type="reset" value="Reset"/></li>
									<li><a href="#" onclick="forgotPassword();">Forgot password?</a></li>
								</ul>
							</div>
							
						</html:form>
					</div>
				</div>
			</div>	
        </div>
	
		<footer>
			<div class="container">
				<div class="login-footer">
					<div class="col-md-12">
						<img src="<%=context%>/assets/images/powered-by.png" title="" alt="" class="img-responsive"/>
					</div>
				</div>
			</div>
		</footer>

    </body>
</html>