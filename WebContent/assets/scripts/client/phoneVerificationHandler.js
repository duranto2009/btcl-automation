function OTP() {

	var clientId;
	var phoneNo;
	var token;
	var resendButton;
	var counter = 0;
	var t;
	var contextPath;
	var OTPForm;
	var method;
	var phoneVerifiedIcon;
	var verifyPhoneIcon;
	
	this.setPhoneVerifiedIcon = function( pv ){
		
		phoneVerifiedIcon = pv;
	}
	
	this.setVerifyPhoneIcon = function( vp ){
		
		verifyPhoneIcon = vp;
	}
	
	this.setMethod = function( m ){
		
		method = m;
	}
	
	this.setOTPForm = function( form ){
	
		OTPForm = form;
	}
	
	this.setClientID = function( id ) {

		clientId = id;
	}

	this.setPhoneNo = function( phn ) {

		phoneNo = phn;
	}

	this.setToken = function( tk ) {

		token = tk;
	}

	this.setContextPath = function( cp ) {

		contextPath = cp;
	}
	
	this.setResendButton = function( rb ){
		
		resendButton = rb;
	}

	validate = function(){
		
		console.log( "Resebd button " + this.resendButton );
		
		if ( phoneNo.length == 0 ) {

			toastr.error("Invalid Phone number");
			return false;
		}

		/*if ( !contextPath ) {

			toastr.error("Context path not set");
			return false;
		}*/
		
		if( !resendButton ){
			
			toastr.error( "Resend button not set" );
			return false;
		}
		
		if( !method ){

			toastr.error( "Method not set" );
			return false;
		}
		
		if( token == '' ){
			
			toastr.error( "OTP field can not be empty", "Error");
			return;
		}
	}
	
	this.sendOTP = function() {

		validate();
		
		var param = {};
		param['method'] = method;
		param['id'] = clientId;
		param['phoneNo'] = phoneNo;

		var url = contextPath + "/api/client.do";

		call( url, param, freezeResend );
	}
	
	this.verifyOTP = function(){
		
		if( token == '' ){
			
			toastr.error( "OTP field can not be empty", "Error");
			return;
		}
		
		var param = {};
		param['method'] = method;
		param['id'] = clientId;
		param['phoneNo'] = phoneNo;
		param['token'] = token;
		
		var url = contextPath + "/api/client.do";
		
		call( url, param, phoneVerifiedCallback );
	}

	countDown = function() {

		console.log( "Inside Count down" );
		
		var text = "Resend Code " + (60 - counter);

		$( resendButton ).text(text);

		if ( counter == 60 ) {

			this.clearTimeout( t );
			
			$( resendButton ).text("Resend Code");
			$( resendButton ).prop('disabled', false);
			
			counter = 0;
			
		} else {

			t = this.setTimeout( countDown, 1000 );
		}
		counter++;
	}

	freezeResend = function() {
		
		console.log( "Inside freeze resend" );
		
		$("#OTPForm").css('display', 'inline');
		$("#sendVerificationSMS").hide();

		$( resendButton ).prop('disabled', true);
		
		countDown();
	}

	phoneVerifiedCallback = function(){
		
		console.log( "executing phone verified" );
		$( OTPForm ).hide();
		$( phoneVerifiedIcon ).show(500);
		$( verifyPhoneIcon ).hide(500);
	}

	call = function( url, params, successCallback ) {

		$.ajax({
			url : url,
			dataType : 'json',
			data : params,
			success : function(data) {

				console.log(data);
				if (data['statusCode'] == '200') {

					toastr.success(data['message'], "Success");

					if ( successCallback ) {

						console.log("Success block executing");

						successCallback();
					}

				} else if (data['statusCode'] == '400') {

					toastr.error(data['message'], "Error");
				}
			},
			error : function() {
				alert('error');
			}
		});
	}
}