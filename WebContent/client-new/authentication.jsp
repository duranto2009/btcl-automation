<tab-content title="Authentication"
             icon = "fa fa-paper-plane icon-state-danger"
             :before-change="beforeTabSwitch_2">
    <div class="view-height">
        <div class="row">
            <div class="col-xs-12 col-sm-12 col-md-8 col-lg-8 col-lg-offset-2 col-md-offset-2">
                <btcl-field title="Email" :required=true >
                    <div class="row">
                        <div class="col-sm-8">
                            <input class="form-control" v-model="clientContactDetails.email" required="true"
                                   :readonly="redirected || clientAdded">
                        </div>
                        <div class="col-sm-4">
                            <button v-if="clientAdded && !emailVerified" type="button"
                                    class="btn green-haze btn-block"
                                    :disabled="emailVerifyBtnDisable"
                                    @click="verify(clientContactDetails.email, 'email')">Verify</button>

                            <label v-else-if="emailVerified" class="badge badge-success"
                                   title="Email is verified">
                                <i class="fa fa-check"></i> Verified
                            </label>

                        </div>
                    </div>
                </btcl-field>
            </div>
        </div>

        <div class="row">
            <div class="col-xs-12 col-sm-12 col-md-8 col-lg-8 col-lg-offset-2 col-md-offset-2">
                <btcl-field title="Select Country" :required=true>
                    <div class="row">
                        <div class="col-sm-8">
                            <btcl-select-country @send-country-code="receiveCountryCode"
                                                 :disabled="redirected"></btcl-select-country>
                        </div>
                    </div>

                </btcl-field>
            </div>
        </div>


        <div class="row">
            <div class="col-xs-12 col-sm-12 col-md-8 col-lg-8 col-lg-offset-2 col-md-offset-2">
                <btcl-field title="Mobile Number" :required=true>
                    <div class="row">
                        <div class="col-xs-2">
                            <label class="control-label">{{countryCode}}</label>
                        </div>
                        <div class="col-xs-6">
                            <input v-model="clientContactDetails.phoneNumber"
                                   class="phoneNumber form-control company regi"
                                   type="tel" :readonly="redirected || clientAdded">
                        </div>
                        <div class="col-xs-4">
                            <button v-if="clientAdded && !mobileVerified && !OTPSent"
                                    type="button"
                                    class="btn green-haze btn-block"
                                    :disabled="mobileVerifyBtnDisable"
                                    @click="verify(clientContactDetails.phoneNumber, 'mobile')">Verify</button>

                            <button v-else-if="clientAdded && !mobileVerified && OTPSent"
                                    type="button"
                                    class="btn green-haze btn-block"
                                    :disabled="!otpResendBtnDisable"
                                    @click="resendOTP">Resend</button>


                            <label v-else-if="mobileVerified"
                                   class="badge badge-success"
                                   title="Mobile Number is verified">
                                <i class="fa fa-check"></i> Verified
                            </label>

                        </div>
                    </div>
                </btcl-field>
            </div>
        </div>


        <div class="row" v-if="otpFormShow && !mobileVerified">
            <div class="col-xs-12 col-sm-12 col-md-8 col-lg-8 col-lg-offset-2 col-md-offset-2">
                <btcl-field title="OTP">
                    <div class="row">
                        <div class="col-sm-8">
                            <input class="form-control" type="text" v-model="OTP">
                        </div>

                    </div>
                    <div v-if="clientAdded && !mobileVerified && OTPSent" class="row">
                        <div class="col-sm-8">
                            <p v-if="!otpInvalidated" class="text-center"><strong>You have {{this.time}} seconds before this OTP invalidates</strong></p>
                        </div>
                    </div>
                </btcl-field>
            </div>
        </div>

        <br><br>
        <div class="row" v-if="otpFormShow && !mobileVerified">
            <div class="col-xs-12 col-sm-8 col-md-8 col-lg-8 col-lg-offset-2 col-sm-offset-2 col-md-offset-2">
                <btcl-field>
                    <div class="row">
                        <div class="col-sm-8">
                            <button class="btn btn-primary btn-block" type="button" @click="verifyOTP" :disabled="otpSubmitBtnDisable">Submit</button>
                        </div>
                    </div>
                </btcl-field>
            </div>
        </div>
        <div class="row" v-if="!clientAdded">
            <div class="col-xs-12 col-md-8 col-lg-8 col-lg-offset-2 col-md-offset-2">
                <btcl-field>
                    <div class="row">
                        <div class="col-sm-8">
                            <button class="btn btn-block green-haze" @click="addTemporaryClient" :disabled="confirmBtnDisable">Confirm</button>
                        </div>
                    </div>
                </btcl-field>
            </div>
        </div>
    </div>
</tab-content>
