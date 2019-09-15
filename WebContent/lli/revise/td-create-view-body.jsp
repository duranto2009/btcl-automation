<div id=btcl-application>
    <btcl-body title="TD" subtitle="">
        <%--<btcl-form :action="contextPath + 'lli/application/upgrade-bandwidth.do'" :name="['application']" :form-data="[application]" :redirect="goView">--%>
            <btcl-portlet>
                <btcl-field title="Client">
                    <lli-client-search :client.sync="revise.client" :callback="clientSelectionCallback">Client</lli-client-search>
                </btcl-field>
                <%--<btcl-field title="Connection">--%>
                    <%--<multiselect v-model="application.connection" :options="connectionOptionListByClientID"--%>
                                 <%--:track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom"--%>
                                 <%--@select="getConnectionByID">--%>
                    <%--</multiselect>--%>
                <%--</btcl-field>--%>
                <%--<btcl-info v-if="Object.keys(existingConnection).length !== 0" title="Existing Bandwidth" :text="existingConnection.bandwidth + ' Mbps'"></btcl-info>--%>
                <%--<btcl-input title="Extra Bandwidth (Mbps)" :text.sync="application.bandwidth"></btcl-input>--%>
                <%--<btcl-input title="Description" :text.sync="application.description"></btcl-input>--%>

                <btcl-field title="Adviced Date">
                    <btcl-datepicker :date.sync="revise.suggestedDate"></btcl-datepicker>
                </btcl-field>


                <%--<btcl-field title="TD Expire Date">--%>
                    <%--<btcl-datepicker :date.sync="application.tdExpireDate"></btcl-datepicker>--%>
                <%--</btcl-field>--%>
                <btcl-input title="Comment" :text.sync="revise.comment"></btcl-input>
                <btcl-field>
                <div align="right">
                    <button type="submit" class="btn green-haze" v-on:click="submitData" >Submit</button>
                </div>
                </btcl-field>

            </btcl-portlet>

        <%--</btcl-form>--%>
    </btcl-body>
</div>

<script src="../connection/lli-connection-components.js"></script>
<script src="../application/lli-application-components.js"></script>
<script>
    var vue = new Vue({
        el: "#btcl-application",
        data: {
            revise: {}
        },
        methods: {
            submitData: function () {
                var url = "tdrequest";
                axios.post(context + 'lli/revise/' + url + '.do', {'data': JSON.stringify(this.revise)})
                    .then(result => {
                        if (result.data.responseCode == 2) {
                            toastr.error(result.data.msg);
                        } else if (result.data.responseCode == 1) {
                            toastr.success("Your request has been processed", "Success");
                            window.location.href = context + 'lli/revise/search.do';
                        }
                        else {
                            toastr.error("Your request was not accepted", "Failure");
                        }
                    })
                    .catch(function (error) {
                    });
                // alert('form submit');
            },
        }
    });
    mounted: {
        // alert('hell world');
    }
</script>