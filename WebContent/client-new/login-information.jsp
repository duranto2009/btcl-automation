<tab-content title="Login Information"
             icon="fa fa-link icon-state-danger"

>
    <div class="view-height">
        <form>
        <div class="row">
            <div class="col-xs-12 col-sm-12 col-md-8 col-lg-8 col-lg-offset-2 col-md-offset-2">
                <btcl-field title="Username" :required=true>
                    <div class="row">
                        <div class="col-xs-8">
                            <input type="text" class="form-control" v-model="client.loginName" autocomplete="username" >
                        </div>
                    </div>
                </btcl-field>
            </div>
        </div>

        <div class="row">
            <div class="col-xs-12 col-sm-12 col-md-8 col-lg-8 col-lg-offset-2 col-md-offset-2">
                <btcl-field title="Password" :required=true>
                    <div class="row">
                        <div class="col-xs-8">
                            <input v-model="client.loginPassword" required type="password" class="form-control" autocomplete="new-password">
                        </div>
                    </div>
                </btcl-field>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-sm-12 col-md-8 col-lg-8 col-lg-offset-2 col-md-offset-2">
                <btcl-field title="Confirm Password" :required=true>
                    <div class="row">
                        <div class="col-xs-8">
                            <input v-model="confirmPassword" required type="password" class="form-control" autocomplete="new-password">
                        </div>
                    </div>
                </btcl-field>
            </div>
        </div>
        </form>
        <div class="row">
            <div class="col-xs-12 col-sm-12 col-md-8 col-lg-8 col-lg-offset-2 col-md-offset-2">
                <btcl-field title="Captcha" :required=true>
                    <div class="row">
                        <div class="col-xs-6">
                            <img class="img-thumbnail " style="width: 100%; max-height: 34px; padding: 2px;" id="captcha" :src="captchaSource" alt="loading captcha...">
                        </div>
                        <div class="col-sm-4">
                            <i @click="reloadCaptcha" title="Refresh Captcha" class="fa fa-refresh" aria-hidden="true"></i>
                        </div>
                    </div>
                </btcl-field>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-sm-12 col-md-8 col-lg-8 col-lg-offset-2 col-md-offset-2">
                <btcl-field title="Answer" :required=true>
                    <div class="row">
                        <div class="col-xs-8">
                            <input v-model="captchaAnswer" type="text"  required="true" class="form-control"/>
                        </div>
                    </div>
                </btcl-field>
            </div>
        </div>
    </div>
</tab-content>