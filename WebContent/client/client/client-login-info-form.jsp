<div id="login-information" style="display:block">
	<h3 class="form-section">Login Info</h3>
	<div class="row">
		<div class="col-md-6">
			<div class=form-group>
				<label class="col-sm-4 control-label">Username<span class=required aria-required=true>*</span></label>
				<div class=col-sm-8>
					<input name="clientDetailsDTO.loginName"  class="form-control" type="text" autocomplete="off" <%=(request.getParameter("loginNameEditability").equals("false")) ? "disabled" : "" %>>
				</div>
			</div>
		</div>
		<div class="col-md-6">
			<div class=form-group>
				<label class="col-sm-2" id='availability' 
				style="padding-top: 7px;margin-top: 1px; 
				margin-bottom: 0px; text-align: left; font-weight: 400"></label>
				<label class="col-sm-2 control-label">Password<span class=required aria-required=true>*</span></label>
				<div class=col-sm-8>
					<input name="clientDetailsDTO.loginPassword" id="password" class="form-control" type="password" autocomplete="off">
				</div>
			</div>
		</div>
	</div>
	
	<div class="row">
		<%if (request.getParameter("captcha").equals("true")) {%>
		<div class="col-md-6">
			<div class="form-group">
				<label class="col-md-4 control-label">Captcha <span class="required" aria-required="true"> * </span></label>
				<div class="col-md-8 no-margin-padding" >
					<div class="col-xs-8 col-md-5" >
						<img class="img-thumbnail" style="width: 100%; max-height: 34px; padding: 2px;" id="captcha" src="<%=request.getContextPath()%>/simpleCaptcha.jpg"
							 alt="loading captcha...">
					</div>
					<div class="col-xs-4 col-md-2" style="line-height: 34px;">
						 <i id="reloadCaptcha" title="Refresh Captcha" class="fa fa-refresh" aria-hidden="true"></i>
					</div>
					<div class="col-xs-12 col-md-5">
						<input class="form-control" type="text" name="answer" />
					</div>
				</div>
			</div>
		</div>
		<script>
		$("#reloadCaptcha").click(function() {
			$("#captcha").attr('src', $("#captcha").attr('src') + '?' + Math.random());
			
		});

		$("input[name='clientDetailsDTO.loginName']").keyup(function(){
			if($(this).val().length>0){
				data = {};
				data.username = $(this).val();
				callAjax(context + "Client/UsernameAvailability.do?username="+data.username, data, (data)=>{
					if(data.responseCode === 1) {
						if(data.payload == false){
							$('#availability').html('available');
							$('#availability').css('color', 'green');	
						}else if( data.payload == true) {
							$('#availability').html('unavailable');
							$('#availability').css('color', 'red');	
						}
					}else {
						toastr.error(data.msg);
					}
				},  "POST")
			}else {
				$('#availability').html('');
			}
		});
		
		</script>
		<%}%>
		<div class="col-md-6">
			<div class=form-group>
				<label class="col-sm-4 control-label">Confirm Password<span class=required aria-required=true>*</span></label>
				<div class=col-sm-8>
					<input name="confirmLoginPassword" id="cPassword" class="form-control" type="password" autocomplete="off">
				</div>
			</div>
		</div>
	</div>
</div>