import {countryCodeSelection} from "../vue-components/country-code-selection.js";
import {serviceComponent} from "../vue-components/serviceType.js";

let formWizard = window.VueFormWizard.FormWizard;
let tabContent = window.VueFormWizard.TabContent;

new Vue({
    el: "#btcl-app",

    components: {
        "form-wizard": formWizard,
        "tab-content": tabContent,
        "service-component": serviceComponent,
        "btcl-select-country": countryCodeSelection,
    },

    data: {
        minimumPasswordLength:5,
        time: 0,
        timeUpperLimit: 60,
        timeLowerLimit: 0,
        context,
        confirmBtnDisable: false,
        emailVerifyBtnDisable: false,
        mobileVerifyBtnDisable: false,
        otpResendBtnDisable: false,
        otpSubmitBtnDisable:false,
        isEmailUsed: false,
        // moduleId: 7,
        countryCode: "+880",
        modules: [
            {
                id: 6,
                name: 'VPN',
                pdf: context + 'assets/static/vpnTerms.pdf'
            }, {
                id: 7,
                name: 'LLI',
                pdf: context + 'assets/static/lliTerms.pdf'
            }, {
                id: 4,
                name: 'CoLocation',
                pdf: context + 'assets/static/lliTerms.pdf'
            }, {
                id: 9,
                name: 'NIX',
                pdf: context + 'assets/static/lliTerms.pdf'
            }

        ],

        termAndConditionCheck: false,
        otpFormShow: false,

        regTypes: [],
        // selectedRegType: 0,

        regCategories: [],
        // selectedRegCategory: 0,

        regSubCategories: [],
        // selectedRegSubCategory: 0,

        captchaAnswer: '',
        confirmPassword: '',


        client: {
            loginName: '',
            loginPassword: '',
        },

        clientDetails: {
            moduleID: 0,
            registrantType: 0,
            regCategory: 0,
            regSubCategory: 0,
            clientCategoryType: 2,
        },


        clientContactDetails: {
            registrantsName: '',
            registrantsLastName: '',
            phoneNumber: '',
            isPhoneNumberVerified: 0,
            email: '',
            isEmailVerified: 0,
            webAddress: '',
            city: '',
            postCode: '',
            address: '',
            country: 'BD',
            detailsType: 0,
            landlineNumber: '',
            faxNumber: '',

        },

        adminContactDetails: {
            registrantsName: '',
            registrantsLastName: '',
            phoneNumber: '',
            isPhoneNumberVerified: 0,
            email: '',
            isEmailVerified: 0,
            webAddress: '',
            city: '',
            postCode: '',
            address: '',
            country: '',
            detailsType: 2,
            landlineNumber: '',
            faxNumber: '',

        },

        technicalContactDetails: {
            registrantsName: '',
            registrantsLastName: '',
            phoneNumber: '',
            isPhoneNumberVerified: 0,
            email: '',
            isEmailVerified: 0,
            webAddress: '',
            city: '',
            postCode: '',
            address: '',
            country: '',
            detailsType: 3,
            landlineNumber: '',
            faxNumber: '',

        },

        billingContactDetails: {
            registrantsName: '',
            registrantsLastName: '',
            phoneNumber: '',
            isPhoneNumberVerified: 0,
            email: '',
            isEmailVerified: 0,
            webAddress: '',
            city: '',
            postCode: '',
            address: '',
            country: '',
            detailsType: 1,
            landlineNumber: '',
            faxNumber: '',

        },


        clientAdded: false,
        emailVerified: false,
        mobileVerified: false,

        temporaryClientId: temporaryClientId,
        temporaryClient: {},

        redirected: false,
        index: 0,
        OTP: '',
        OTPSent: false,


        countries: [],
        captchaSource: context + '/simpleCaptcha.jpg',


    },
    methods: {
        showOTPTimeRangeMsg() {
            this.time --;
            if(this.time < this.timeLowerLimit){
                clearInterval(this.$options.interval);
                this.otpFormShow = !this.otpFormShow;
                this.otpInvalidated = !this.otpInvalidated;
                this.otpResendBtnDisable = !this.otpResendBtnDisable;
            }
        },
        reloadCaptcha() {
            this.captchaSource = this.captchaSource + "?" + Math.random();
        },
        clickedTermsAndCondition() {
            this.termAndConditionCheck = !this.termAndConditionCheck;
        },
        handleModuleChange(val) {
            this.clientDetails.moduleID = val;
        },
        firstNameValidation() {
            if (!this.clientContactDetails.registrantsName) {
                toastr.error("Please provide a First Name", "Failure");
                return false;
            }
            return true;
        },
        emailValidation() {

            if (!this.clientContactDetails.email) {
                //empty
                toastr.error("Please provide a valid email", "Failure");
                return false;
            } else {

                let reg = systemConfig.getEmailRegExpr();
                let result = reg.test(this.clientContactDetails.email);

                if (!result) {
                    //didn't match regex
                    toastr.error("Please provide a valid email", "Failure");
                }
                return result;
            }
        },
        mobileValidation() {
            if (!this.clientContactDetails.phoneNumber) {
                //empty
                toastr.error("Please provide a valid Mobile Number", "Failure");
                return false;
            } else {
                //not empty
                let reg = systemConfig.getMobileNumRegExpr();
                let result = reg.test(this.clientContactDetails.phoneNumber);

                if (this.countryCode === "+880") {
                    if (this.clientContactDetails.phoneNumber.length < 10 && this.clientContactDetails.phoneNumber.length > 11) {
                        toastr.error("Please provide a valid Mobile Number", "Failure");
                        return false;
                    }
                }
                if (!result) {
                    //didn't match regex
                    toastr.error("Please provide a valid Mobile Number", "Failure");
                }
                return result;
            }
        },
        verify(resource, type) {
            toastr.options.timeOut = 5000;
            toastr.info("Please Wait");
            if (type === 'email') {

                let url = context + "Client/send-verification-mail.do";
                this.emailVerifyBtnDisable = true;
                if (!this.emailValidation()) {
                    toastr.error("Invalid Email", "Failure");
                    return false;
                }

                Promise.resolve(
                    axios.get(
                        url + "?email=" + resource + "&clientId=" + this.temporaryClientId
                    ).then((res) => {
                        if(res.data.responseCode === 1) {
                            toastr.info("Please check your email")
                        }else {
                            toastr.error("Something went wrong, Please try again later")
                        }
                    })
                ).then(() => this.emailVerifyBtnDisable = false);

            } else if (type === 'mobile') {
                if (!this.mobileValidation()) {
                    toastr.error("Invalid Email", "Failure");
                    return false;
                }
                resource = this.countryCode + resource;
                this.mobileVerifyBtnDisable = true;
                Promise.resolve(
                    axios.get(
                        context + "Client/send-verification-sms.do" + "?phoneNo=" + resource
                        + "&clientId=" + this.temporaryClientId
                    ).then((res) => {
                        if (res.data.responseCode === 1) {
                            this.otpFormShow = true;
                            this.OTPSent = true;
                            this.time = this.timeUpperLimit;
                            this.otpInvalidated = false;
                            this.$options.interval = setInterval(this.showOTPTimeRangeMsg, 1000);
                            toastr.info("Please check your message")
                        } else {
                            toastr.error("Something went wrong, Please try again later")
                        }

                    })
                ).then(() => this.mobileVerifyBtnDisable = false);

            }
        },
        beforeTabSwitch_1() {
            let flag = !(this.clientDetails.moduleID > 0);

            if (flag) {
                toastr.info("Please Select a Service Type", "Info");
                return false;
            }

            flag = !this.termAndConditionCheck;

            if (flag) {
                toastr.info("Please Agree with Terms and Conditions", "Info");
                return false;
            }

            return true;
        },
        beforeTabSwitch_2() {
            toastr.options.timeOut = 5000;
            if(!this.emailVerified) {
                toastr.error("Please Verify your Email Address");

            }
            if(!this.mobileVerified) {
                toastr.error("Please Verify your Mobile Number");
            }
            return this.emailVerified && this.mobileVerified;

        },
        registrantTypeValidation() {

            if (!this.clientDetails.registrantType) {
                toastr.error("Please provide a Valid Registrant Type", "Failure");
                return false;
            }
            return true;
        },
        registrantCategoryValidation() {
            if ( this.clientDetails.registrantType && !this.clientDetails.regCategory) {
                toastr.error("Please provide a Valid Registrant Category", "Failure");
                return false;
            }
            return true;
        },
        addressValidation() {
            let result = true;
            if(!this.clientContactDetails.city) {
                toastr.error("Please provide your city");
                result = false;
            }
            if(!this.clientContactDetails.address){
                toastr.error("Please provide your address");
                result = false;
            }
            return result;


        },
        webAddressValidation() {
            let reg = systemConfig.getUrlRegExpr();
            if(!reg.test(this.clientContactDetails.webAddress)){
                toastr.error("Invalid Web Address");
                return false;
            }
            return true;

        },
        beforeTabSwitch_3() {
            toastr.options.timeOut = 5000;
            if (!this.firstNameValidation()){
                return false;
            }

            if(!this.registrantTypeValidation()) {
                return false;
            }

            if(!this.registrantCategoryValidation()) {
                return false;
            }
            if(!this.addressValidation()){
                return false;
            }

            if(this.clientContactDetails.webAddress) {
                if(!this.webAddressValidation()) {
                    return false;
                }
            }

            return true;
        },


        finishTasks() {

            toastr.options.timeOut = 5000;

            toastr.info("Please wait");


            if (!this.userNameValidation()) {
                return false;
            }
            if(!this.passwordValidation()) {
                return false;
            }

            if(!this.captchaAnswer) {
                toastr.error("Captcha must be provided");
                return false;
            }

            this.checkUsernameAvailability()
                .then(() => this.addNewClient())
                .then(val=>{
                    toastr.success(val, "Success");
                    window.open(context, "_self");
                }).then(()=>{
                    toastr.info("Done");
                })
                .catch(err=> {
                    console.log(err);
                    toastr.error(err, "Failure");
                    return false;
                });



            return true;
        },

        addNewClient() {

            this.populateClientObjects();


            let client = {
                loginName: this.client.loginName,
                loginPassword: this.client.loginPassword,
                confirmPassword : this.confirmPassword,

                moduleID: this.clientDetails.moduleID,
                registrantType: this.clientDetails.registrantType,
                regCategory: this.clientDetails.regCategory,
                regSubCategory: this.clientDetails.regSubCategory,
                clientCategoryType: 2,

                contactInfoList: [
                    this.clientContactDetails,
                    this.billingContactDetails,
                    this.adminContactDetails,
                    this.technicalContactDetails,
                ],



                captchaAnswer: this.captchaAnswer,
            };

            if(this.countryCode === '+880') {
                let contactDetails = client.contactInfoList[0];
                if(contactDetails.phoneNumber.startsWith('0')) {
                    contactDetails.phoneNumber = contactDetails.phoneNumber.substr(1);
                    contactDetails.phoneNumber = this.countryCode + contactDetails.phoneNumber;

                }else {
                    contactDetails.phoneNumber = this.countryCode + contactDetails.phoneNumber;
                }

            }


            return new Promise((resolve, reject)=> {
                axios.post(
                    context + "Client/new-client-registration-by-client.do", {
                        client
                    }
                ).then(res => {
                    if (res.data.responseCode === 1) {

                       resolve("Client Registration is successful");
                        // no redirection needed here. it will be done in the finish task method inside long promise chain.
                    } else {
                       reject("Client Registration is not Successful");
                    }
                })
            });

        },

        populateClientObjects() {

            //clientDetails
            // this.clientDetails.moduleID = this.moduleId;
            // this.clientDetails.registrantType = this.selectedRegType;
            // this.clientDetails.regCategory = this.selectedRegCategory;
            // this.clientDetails.regSubCategory = this.selectedRegSubCategory;
            //contact details objects

            this.clientContactDetails.isEmailVerified = 1;
            this.clientContactDetails.isPhoneNumberVerified = 1;

        },

        passwordValidation() {
            if(!this.client.loginPassword){
                toastr.error("Password can not be empty");
                return false;
            }
            if(this.client.loginPassword.length<this.minimumPasswordLength){
                toastr.error("Password Length must be at least " + this.minimumPasswordLength + " characters");
                return false;
            }

            if(this.client.loginPassword !== this.confirmPassword) {
                toastr.error("Password and Confirm Password did not match");
                return false;
            }
            return true;
        },

        userNameValidation() {
            if(!this.client.loginName){
                toastr.error("Username must be given");
                return false;
            }
            return true;
        },
        checkUsernameAvailability() {

            return new Promise((resolve, reject) => {
                axios.get(context + "Client/UsernameAvailability.do?username=" + this.client.loginName)
                    .then(res => {
                        if (res.data.responseCode === 1) {
                            if (res.data.payload === true) {
                                // toastr.error("Username already taken", "Failure");
                                reject("Username already taken");

                            } else {
                                resolve(true);
                            }
                        } else {
                            // toastr.error("f", "Failure");
                            reject("Failed to check username availability");
                        }
                    })
            });
        },


        receiveCountryCode(countryCode) {
            this.countryCode = countryCode;
        },
        getTypesInAModule: function () {

            axios.get(context + "ClientType/GetRegistrantTypesInAModule.do?moduleID=" + this.clientDetails.moduleID).then(res => {
                if (res.data.responseCode === 1) {
                    this.regTypes = res.data.payload;
                } else {
                    toastr.error("Error getting types under this module", "Failure");
                }
            });
        },
        getCategoriesInAType: function () {
            axios.get(context + "ClientType/GetRegistrantCategory.do?registrantType=" +
                this.clientDetails.registrantType + "&moduleID=" + this.clientDetails.moduleID)
                .then(res => {
                    if (res.data.responseCode === 1) {
                        this.regCategories = res.data.payload;
                    } else {
                        toastr.error("Error getting categories under this type", "Failure");
                    }
                });
        },
        getSubCategoriesUnderACategory: function () {
            axios.get(context + "ClientType/getSubCategoriesUnderACategory.do?registrantCategory=" +
                this.clientDetails.regCategory)
                .then(res => {
                    if (res.data.responseCode === 1) {
                        this.regSubCategories = res.data.payload;
                    } else {
                        toastr.error("Error getting sub-categories under this category", "Failure");
                    }
                });
        },
        emailAvailable() {
            return axios.post(context + 'Client/check-availability.do', {
                    param : this.clientContactDetails.email,
                    type : 'email'
                }).then(res => {
                        if (res.data.responseCode === 1) {
                            if (res.data.payload === false) {
                                toastr.error(this.clientContactDetails.email + " is already in use", "Failure");
                                return false;
                            } else {
                                return true;
                            }
                        } else {
                            toastr.error("Something went wrong", "Failure");
                            return false;
                        }
                    }).catch(err => console.log(err));

        },
        mobileAvailable() {
            let tempMobile = this.clientContactDetails.phoneNumber;
            if(this.countryCode === '+880'){
                if(tempMobile.startsWith('0')) {
                    tempMobile = tempMobile.substr(1);
                }
            }
            return axios.post(context + 'Client/check-availability.do', {
                    param :this.countryCode + tempMobile,
                    type :'mobile'
                }) .then(res => {
                        if (res.data.responseCode === 1) {
                            if (res.data.payload === false) {
                                toastr.error(this.countryCode + tempMobile+ " is already in use", "Failure");
                               return false;
                            } else {
                                return true;
                            }
                        } else {
                            toastr.error("Something went wrong", "Failure");
                            return false;
                        }
                    }).catch(err => console.log(err));

        },
        checkPreConditionForTempClientAdd() {
            let validateEmail = this.emailValidation();
            let validateMobile = this.mobileValidation();

            if (validateEmail && validateMobile) {
                return new Promise((resolve, reject) => {
                    if (this.emailAvailable() && this.mobileAvailable()) {
                        resolve(true);
                    } else {
                        reject(false);
                    }
                })
            }
        },
        saveTemporaryClientData() {
            return axios.post(
                context + "Client/temporaryAdd.do", {
                    intlMobileNumber: this.clientContactDetails.phoneNumber,
                    email: this.clientContactDetails.email,
                    countryCode: this.countryCode,
                    moduleId: this.clientDetails.moduleID,
                }
            ).then(res => {
                if (res.data.responseCode === 1) {
                    this.clientAdded = true;
                    this.temporaryClientId = res.data.payload;
                    toastr.info("Please Verify your Email and Mobile");

                } else {
                    toastr.error("Error in adding temporary client", "Failure");
                }
            })
        },

        async addTemporaryClient() {
            toastr.options.timeOut=5000;
            toastr.info("Please Wait");
            this.confirmBtnDisable = true;

            let validateEmail = this.emailValidation();
            let validateMobile = this.mobileValidation();

            if (validateEmail && validateMobile) {

                try {
                    let isEmailAvailable = await this.emailAvailable();
                    let isMobileAvailable = await this.mobileAvailable();
                    if(isEmailAvailable && isMobileAvailable) {
                        await this.saveTemporaryClientData();

                    }
                    this.confirmBtnDisable = false;
                }catch(e) {
                    toastr.error("Catch: Error " + e);
                    this.confirmBtnDisable = false;
                    return false;
                }

            }

        },
        getTemporaryClientInfo() {
            return axios.get(
                context + "Client/get-temporary-client.do?clientId=" + this.temporaryClientId
            ).then(res => {
                if (res.data.responseCode === 1) {
                    this.temporaryClient = res.data.payload;
                    this.clientDetails.moduleID = this.temporaryClient.moduleId;
                    this.clientContactDetails.email = this.temporaryClient.emailId;
                    this.clientContactDetails.phoneNumber = this.temporaryClient.mobileNumber;
                    this.countryCode = this.temporaryClient.countryCode;
                    this.redirected = true;
                    this.clientAdded = true;
                    this.emailVerified = this.temporaryClient.isEmailVerified;
                    this.mobileVerified = this.temporaryClient.isMobileNumberVerified;

                    this.termAndConditionCheck = true;
                } else {
                    toastr.error("Error in getting temporary client", "Failure");
                }
            })

        },

        verifyOTP() {
            toastr.info("Please wait");
            this.otpSubmitBtnDisable = !this.otpSubmitBtnDisable;
            Promise.resolve(
                axios.post(
                    context + "Client/verify-sms.do", {
                        clientId: this.temporaryClientId,
                        token: this.OTP,
                        mobile: this.clientContactDetails.phoneNumber,
                    }
                ).then(res => {
                    if (res.data.responseCode === 1) {

                        toastr.success("OTP verified successfully");
                        this.mobileVerified = true;

                    } else {
                        toastr.error(res.data.msg, "Failure");
                    }
                })
            ).then(()=>{
                this.otpSubmitBtnDisable = !this.otpSubmitBtnDisable;
            });

        },
        resendOTP() {
            this.OTP = '';
            this.otpFormShow = false;
            toastr.info("Please Wait");
            this.otpResendBtnDisable = !this.otpResendBtnDisable;
            clearInterval(this.$options.interval);

            Promise.resolve(
                axios.get(
                context + "Client/send-verification-sms.do" + "?phoneNo=" + this.countryCode + this.clientContactDetails.phoneNumber
                    + "&clientId=" + this.temporaryClientId
                ).then((res) => {
                    if (res.data.responseCode === 1) {
                        this.otpFormShow = true;
                        this.OTPSent = true;
                        this.time = this.timeUpperLimit;
                        this.otpInvalidated = false;

                        this.$options.interval = setInterval(this.showOTPTimeRangeMsg, 1000);
                        toastr.info("Please check your message");
                    }else {
                        toastr.error("Something went wrong, Please try again later")
                    }
                })
            ).then(()=>{

            });
        },
        handleRegistrantTypeChange() {
            this.regCategories = [];
            this.clientDetails.regCategories = 0;
            this.getCategoriesInAType();
        },
        handleRegistrantCategoryChange(){
            this.regSubCategories = [];
            this.clientDetails.regSubCategories= 0;
            this.getSubCategoriesUnderACategory();
        },
        getAllCountries() {
            axios.get(
                context + "Client/get-all-countries.do"
            ).then(res => {
                if (res.data.responseCode === 1) {
                    this.countries = res.data.payload;
                } else {
                    toastr.error("Error in getting countries", "Failure");
                }
            })
        }
    },
    mounted() {
    },
    watch: {


    },
    computed: {
        checkRedirection() {
            return this.redirected;
        }
    },
    created() {
        if (this.temporaryClientId !== "null") {
            this.index = 1; // TODO
            Promise.resolve(
                this.getTemporaryClientInfo()
            ).then(()=>{
            this.getTypesInAModule();
            this.getAllCountries();
            });
        }
    },
    beforeDestroy() {
        if(this.$options.interval !== null){
            clearInterval(this.$options.interval)
        }
    }
});