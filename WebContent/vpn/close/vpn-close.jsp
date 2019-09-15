<div id="btcl-application" v-cloak="true">
    <btcl-body title="Link Close" subtitle="Application">

        <btcl-portlet>
            <btcl-field title="Client" :required=true>

                <user-search-by-module
                        :client.sync="application.client"
                        :callback="clientSelectionCallback"
                        :module="6">
                </user-search-by-module>

            </btcl-field>


            <btcl-field title="Active Links" :required=true v-if="connectionOptionListByClientID.length>0">
                <multiselect
                        v-model="application.selectedLink"
                        :options="connectionOptionListByClientID "
                <%--:track-by=ID--%>
                        label=linkName
                        :allow-empty="false"
                        :searchable=true
                        open-direction="bottom">
                </multiselect>
            </btcl-field>

            <btcl-input title="Description"
                        :text.sync="application.description">
            </btcl-input>
            <btcl-input title="Comment"
                        :text.sync="application.comment">
            </btcl-input>

            <btcl-field title="Suggested Date" :required=true>
                <btcl-datepicker :date.sync="application.suggestedDate">
                </btcl-datepicker>
            </btcl-field>


            <btcl-field>
                <div align="right">
                    <button type="submit" class="btn green-haze" v-on:click="submitFormData">Submit</button>
                </div>
            </btcl-field>
        </btcl-portlet>

        <%--</btcl-form>--%>
    </btcl-body>
</div>

<script>

    var vue = new Vue({
        el: "#btcl-application",
        data: {
            contextPath: context,
            connectionOptionListByClientID: [],
            application: {
                client: {},
                selectedLink: {},
            },
            module: 6,
        },
        methods: {
            submitFormData: function () {
                if (!this.validateFormData()) {
                    return;
                }
                var url1 = "insert";
                axios.post(context + 'vpn/close/' + url1 + '.do', {'application': JSON.stringify(this.application)})
                    .then(result => {
                        if (result.data.responseCode == 2) {
                            toastr.error(result.data.msg);
                        } else if (result.data.responseCode == 1) {
                            toastr.success("Your request has been processed", "Success");
                            window.location.href = context + 'vpn/link/search.do';
                        } else {
                            toastr.error("Your request was not accepted", "Failure");
                        }
                    })
                    .catch(function (error) {
                    });
            },
            clearConnection: function () {
                this.connectionOptionListByClientID = [];
            },
            clientSelectionCallback: function (clientID) {
                this.clearConnection();
                if (clientID === undefined) {

                } else {
                    //todo active link fetch by client id
                    return axios({method: 'GET', 'url': context + 'vpn/network/get-all-by-client.do?id=' + clientID})
                        .then(result => {
                            if(result.data.responseCode===1) {


                                this.connectionOptionListByClientID = result.data.payload;
                            }else {
                                errorMessage(result.data.msg);
                            }
                        }, error => {
                        });

                }
            },
            connectionSelectionCallback: function (connection) {

                this.application.selectedConnectionList.push(connection);
            },
            removeSelectedConnection: function (index) {
                this.application.selectedConnectionList.splice(index, 1);
            },
            errorMessage: function (msg) {
                toastr.options.timeOut = 3000;
                toastr.error(msg);
                return;
            },
            validateFormData: function () {
                if (this.application.client.key == null) {
                    errorMessage("Please select a client");
                    return false;
                }
                if (this.application.selectedLink.linkName == null) {
                    errorMessage("Please select a link to close");
                    return false;
                }
                if(this.application.suggestedDate == null){
                    errorMessage("Please select a suggested date");
                    return false;
                }

                return true;
            },
            errorMessage: function (msg) {
                toastr.options.timeOut = 3000;
                toastr.error(msg, "Failure");
                return;
            }
        },
        watch: {
            'application.dstClient.ID': function () {
                if (this.application.srcClient.ID == this.application.dstClient.ID) {
                    this.errorMessage("You can't transfer to same client");
                    this.application.dstClient = null;
                    return;
                }
            },
        }
    });

</script>

