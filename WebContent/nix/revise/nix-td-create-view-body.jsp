<div id=btcl-application>
    <btcl-body title="TD" subtitle="">
            <btcl-portlet>
                <btcl-field title="Client">
                    <lli-client-search :client.sync="revise.client" :callback="clientSelectionCallback">Client</lli-client-search>
                </btcl-field>
                <btcl-field title="Adviced Date">
                    <btcl-datepicker :date.sync="revise.suggestedDate"></btcl-datepicker>
                </btcl-field>
                <btcl-input title="Comment" :text.sync="revise.comment"></btcl-input>
                <btcl-field>
                <div align="right">
                    <button type="submit" class="btn green-haze" v-on:click="submitData" >Submit</button>
                </div>
                </btcl-field>

            </btcl-portlet>
    </btcl-body>
</div>

<script src="../application/nix-application-components.js"></script>
<script>
    var vue = new Vue({
        el: "#btcl-application",
        data: {
            revise: {}
        },
        methods: {
            submitData: function () {
                var url = "tdrequest";
                axios.post(context + 'nix/revise/' + url + '.do', {'data': JSON.stringify(this.revise)})
                    .then(result => {
                        if (result.data.responseCode == 2) {
                            toastr.error(result.data.msg);
                        } else if (result.data.responseCode == 1) {
                            toastr.success("Your request has been processed", "Success");
                            window.location.href = context + 'nix/revise/search.do';
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