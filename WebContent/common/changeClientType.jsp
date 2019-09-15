<%@page import="vpn.client.ClientForm"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="alert alert-success">
  	<div class="row">
		<form name="clientForm" method="post" action="/BTCL_Automation/ClientProfileInfo.do">
			<!-- BEGIN FORM-->
			<div class="row">
				<div class="col-md-12">
					<div class="form-group">
						<label class="control-label col-md-6 .bg-font-green-meadow">Do you want change the client type?</label>
						<div class="radio-list">
							<label class="radio-inline">
								 <input type="radio" name="accountType1" value="<%=ClientForm.CLIENT_TYPE_INDIVIDUAL %>" autocomplete="off" data-no-uniform="true" checked /><%=ClientForm.CLIENT_TYPE_STR.get(ClientForm.CLIENT_TYPE_INDIVIDUAL) %>
							</label>
							 <label class="radio-inline"> 
							 	<input type="radio" name="accountType1" value="<%=ClientForm.CLIENT_TYPE_COMPANY %>" autocomplete="off"  data-no-uniform="true"  /><%=ClientForm.CLIENT_TYPE_STR.get(ClientForm.CLIENT_TYPE_COMPANY) %>
							</label>
						</div>
					</div>
				
				</div>
				<!--/span-->
			</div>
		</form>
	</div>                                     
</div>
