<%@ page import="coLocation.CoLocationConstants" %>
<%@ page import="common.ModuleConstants" %>
<div id=btcl-co-location-application>
    <btcl-body title="Co-Location" subtitle='New Connection Application' v-cloak="true" :loader="loader">
            <btcl-portlet>
        <jsp:include page="../../coLocation/application/co-location-application-form.jsp"/>
                <button type=button class="btn green-haze btn-block btn-lg" @click="submitNewConnection">Submit</button>
            </btcl-portlet>
    </btcl-body>
</div>
<script>
    var vue = new Vue({
        el: "#btcl-co-location-application",
        data: {
            connectionTypeList: [{id: 1, value: 'Regular'}, {id: 2, value: 'Special Connection'}],
            powerTypeList: [{id: 1, type:0, value: 'AC'}, {id: 2,type:0, value: 'DC'}],
            application: {
                client: null,
                connectionType: null,
                rackNeeded: true,
                rackSize: null,
                rackSpace: null,
                powerNeeded: true,
                powerAmount: null,
                powerType: null,
                fiberNeeded: true,
                fiberType: null,
                fiberCore: null,

                floorSpaceNeeded: true,

                comment: null,
                suggestedDate: null,
                description: null,
                skipPayment: null,
                state: {
                    name: '',
                },
            },
            moduleId: <%=ModuleConstants.Module_ID_COLOCATION%>,
            loader: false,
        },
        methods: {
            submitNewConnection: function () {
                if(this.validate()==false) return;

                this.application.fiberCore = parseInt(this.application.fiberCore);
                this.application.client.ID = this.application.client.key;
                let url = "new-connection-application";
                this.loader = true;
                axios.post(context + 'co-location/' + url + '.do', {'application': JSON.stringify(this.application)})
                    .then(result => {
                        if (result.data.responseCode == 2) {
                            toastr.error(result.data.msg);
                            toastr.options.timeOut = 2500;
                            this.loader = false;
                        } else if (result.data.responseCode == 1) {
                            toastr.success("Your request has been processed", "Success");
                            toastr.options.timeOut = 2500;
                            //redirect to specific page
                            window.location.href = context + 'co-location/search.do';
                        }
                        else {
                            toastr.error("Your request was not accepted", "Failure");
                            toastr.options.timeOut = 2500;
                            this.loader = false;
                        }

                    })
                    .catch(function (error) {
                    });
            },
            validate: function(){
                // start: form validation
                if (this.application.client == null) {
                    errorMessage("Client must be selected.");
                    return false;
                }
                if (this.application.connectionType == null) {
                    errorMessage("Connection Type must be selected.");
                    return false;
                }
                if (this.application.powerNeeded == false && this.application.rackNeeded == false && this.application.fiberNeeded == false && this.application.floorSpaceNeeded== false) {
                    errorMessage("Power or Rack or Fiber shouldn't be empty.");
                    return false;
                }
                if (this.application.rackNeeded) {
                    if (this.application.rackSize == null) {
                        errorMessage("Rack Size must be selected.");
                        return false;
                    }
                    if (this.application.rackSpace == null) {
                        errorMessage("Rack Space must be selected.");
                        return false;
                    }
                }
                if (this.application.powerNeeded) {
                    if (this.application.powerType == null) {
                        errorMessage("Power Type must be selected.");
                        return false;
                    }
                    if (this.application.powerAmount == null) {
                        errorMessage("Power Supply must be selected.");
                        return false;
                    }
                }
                if (this.application.fiberNeeded) {
                    if (this.application.fiberType == null) {
                        errorMessage("OFC Type must be selected.");
                        return false;
                    }
                }
                if (this.application.suggestedDate == null) {
                    errorMessage("Provide a suggested date.");
                    return false;
                }
                // end: form validation'
                return true;
            }
        },
    });
</script>