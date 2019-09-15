<%@ page import="login.LoginDTO" %>
<%@ page import="sessionmanager.SessionConstants" %>
<div id=btcl-application>
    <btcl-body title="TD" subtitle="">
        <%--the following div is needed as one component can have one child--%>
        <div>
            <%--form elements description--%>
            <btcl-portlet>
                <div>
                    <btcl-field v-for="(element,index) in application.formElements" :key="index">
                        <div class="form-group">
                            <div class=row>
                                <label class="col-sm-4 control-label"
                                       style="text-align: left;">{{element.Label}}</label>
                                <div class=col-sm-6>
                                    <p v-if="element.Label==='Status'" class="form-control" align="center"

                                       v-bind:style="{ background: application.color}"
                                    >
                                        <span style="color: white;font-weight: bold ;font-size: 18px!important;">{{element.Value}}</span>
                                    </p>
                                    <p v-else class="form-control"
                                       style="background:  #f2f1f1;text-align: center;">
                                        {{element.Value}}
                                    </p>


                                </div>
                            </div>
                        </div>
                    </btcl-field>
                </div>
            </btcl-portlet>

            <%--action forward--%>
            <btcl-portlet>
                <div>

                    <span><b>Available Actions :</b>

                        {{ picked }}

                    </span> <br/>

                    <div>
                        <%--<div v-for="element in application.action">--%>
                        <%--<label>--%>
                        <%--<span>--%>
                        <ul style="list-style-type:none">
                            <li v-for="element in application.action">
                                <label><span><input  type="radio" name="actionForwards" v-model="picked" :value="element.Value"> {{element.Label}}</span></label>
                            </li>
                        </ul>
                        <%--</span>--%>
                        <%--</label> <br/>--%>
                        <%--</div>--%>
                    </div>
                    <div align="right">
                        <button type="submit" class="btn green-haze" v-on:click="nextStep">Submit</button>
                    </div>

                </div>

                <%--</btcl-field>--%>
            </btcl-portlet>

        </div>
    </btcl-body>
</div>

<script src="../application/nix-application-components.js"></script>
<%
    LoginDTO loginDTO = (LoginDTO) session.getAttribute(SessionConstants.USER_LOGIN);
%>
<script>var applicationID = '<%=request.getParameter("id")%>';</script>
<script>var loggedInUserID = '<%=loginDTO.getUserID()%>'; console.log('vendorID: ' + loggedInUserID);</script>
<%--<script src="../td/view.js"></script>--%>
<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
<script>
    var vue = new Vue({
        el: "#btcl-application",
        data: {
            td: {},
            application: {},
            picked: '0',
            comment: '',
            STATE_FACTORY:{
                NO_ACTION_PICKED: 0,
                TD_ADVICE_NOTE_GENERATE: 83,
                COMPLETE_TD: 84,

            }
        },
        methods: {
            submitData: function () {
                var url = "tdrequest";
                axios.post(context + 'nix/revise/' + url + '.do', {'data': JSON.stringify(this.td)})
                    .then(result => {
                        if (result.data.responseCode == 2) {
                            toastr.error(result.data.msg);
                        } else if (result.data.responseCode == 1) {
                            toastr.success("Your request has been processed", "Success");
                            window.location.href = context + 'nix/application/search.do';
                        }
                        else {
                            toastr.error("Your request was not accepted", "Failure");
                        }
                    })
                    .catch(function (error) {
                    });
                alert('form submit');
            },
            nextStep: function () {
                if (this.picked == this.STATE_FACTORY.NO_ACTION_PICKED) {
                    toastr.options.timeOut = 3000;
                    toastr.error("Please Select an Action.");
                    return;
                }


                if (
                    this.picked == this.STATE_FACTORY.TD_ADVICE_NOTE_GENERATE
                    || this.picked == this.STATE_FACTORY.COMPLETE_TD
                ) {
                    swal("Comment:", {
                        content: "input",
                        showCancelButton: true,
                        buttons: true,
                    })
                        .then((value) => {
                            if (value === false) return false;
                            if (value === "") {
                                swal("Warning!", "You you need to write something!", "warning");
                                return false
                            }
                            if (value) {
                                this.comment = `${value}` + '';
                                var jsonData = {
                                    applicationID: applicationID,
                                    comment: this.comment,
                                    nextState: this.picked,
                                };
                                var url1 = "";
                                if(this.picked == this.STATE_FACTORY.COMPLETE_TD){
                                    url1 = "completetd";
                                }
                                else if(this.picked == this.STATE_FACTORY.TD_ADVICE_NOTE_GENERATE){
                                    url1 = "advicenotegenerate";
                                }
                                axios.post(context + 'nix/revise/' + url1 + '.do', {'data': JSON.stringify(jsonData)})
                                    .then(result => {
                                        if (result.data.responseCode == 2) {
                                            toastr.error(result.data.msg);
                                        } else if (result.data.responseCode == 1) {
                                            toastr.success("Your request has been processed", "Success");
                                            window.location.href = context + 'nix/revise/requestedtdlist.do';
                                        }
                                        else {
                                            toastr.error("Your request was not accepted", "Failure");
                                        }
                                    })
                                    .catch(function (error) {
                                    });

                            }
                        });
                }
            },
        },
        created() {
        this.loading = true;
        axios({method: 'GET', 'url': context + 'nix/revise/revise-client-get-flow.do?id=' + applicationID})
            .then(result => {
                // this.application = result.data.payload.members;
                if(result.data.payload.hasOwnProperty("members")){

                    this.application = result.data.payload.members;
                }
                else{
                    this.application = result.data.payload;
                }
                this.loading = false;
                $("#appBody").css("display", "block");
            }, error => {
            });
    }
    });
</script>