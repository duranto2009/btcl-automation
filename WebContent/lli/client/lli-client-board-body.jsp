<div id=btcl-application>
    <btcl-body title="LLI" :subtitle="'Client Board ('+ clientDetails.clientID +')'">


        <btcl-portlet>
            <btcl-info title="Login Name" :text="clientDetails.loginName"></btcl-info>
            <btcl-info title="Registrant Type" :text="clientDetails.registrantType"></btcl-info>
            <btcl-info title="Registrant Category" :text="clientDetails.registrantCategory"></btcl-info>
        </btcl-portlet>


        <btcl-portlet title="Contact Information">
            <div class="table-responsive">
                <table class="table">
                    <thead>
                    <tr>
                        <th></th>
                        <th>Registrant</th>
                        <th>Billing</th>
                        <th>Admin</th>
                        <th>Technical</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <th>Name</th>
                        <td>{{registrantContactDetails.registrantsName}}
                            {{registrantContactDetails.registrantsLastName}}
                        </td>
                        <td>{{billingContactDetails.registrantsName}} {{billingContactDetails.registrantsLastName}}</td>
                        <td>{{adminContactDetails.registrantsName}} {{adminContactDetails.registrantsLastName}}</td>
                        <td>{{technicalContactDetails.registrantsName}}
                            {{technicalContactDetails.registrantsLastName}}
                        </td>
                    </tr>
                    <tr>
                        <th>Mobile</th>
                        <td>{{registrantContactDetails.phoneNumber}}</td>
                        <td>{{billingContactDetails.phoneNumber}}</td>
                        <td>{{adminContactDetails.phoneNumber}}</td>
                        <td>{{technicalContactDetails.phoneNumber}}</td>
                    </tr>
                    <tr>
                        <th>Phone</th>
                        <td>{{registrantContactDetails.landlineNumber}}</td>
                        <td>{{billingContactDetails.landlineNumber}}</td>
                        <td>{{adminContactDetails.landlineNumber}}</td>
                        <td>{{technicalContactDetails.landlineNumber}}</td>
                    </tr>
                    <tr>
                        <th>E-Mail</th>
                        <td>{{registrantContactDetails.email}}</td>
                        <td>{{billingContactDetails.email}}</td>
                        <td>{{adminContactDetails.email}}</td>
                        <td>{{technicalContactDetails.email}}</td>
                    </tr>
                    <tr>
                        <th>Fax</th>
                        <td>{{registrantContactDetails.faxNumber}}</td>
                        <td>{{billingContactDetails.faxNumber}}</td>
                        <td>{{adminContactDetails.faxNumber}}</td>
                        <td>{{technicalContactDetails.faxNumber}}</td>
                    </tr>
                    <tr>
                        <th>Web Address</th>
                        <td>{{registrantContactDetails.webAddress}}</td>
                        <td>{{billingContactDetails.webAddress}}</td>
                        <td>{{adminContactDetails.webAddress}}</td>
                        <td>{{technicalContactDetails.webAddress}}</td>
                    </tr>
                    <tr>
                        <th>Address</th>
                        <td>{{registrantContactDetails.address}}</td>
                        <td>{{billingContactDetails.address}}</td>
                        <td>{{adminContactDetails.address}}</td>
                        <td>{{technicalContactDetails.address}}</td>
                    </tr>
                    <tr>
                        <th>City</th>
                        <td>{{registrantContactDetails.city}}</td>
                        <td>{{billingContactDetails.city}}</td>
                        <td>{{adminContactDetails.city}}</td>
                        <td>{{technicalContactDetails.city}}</td>
                    </tr>
                    <tr>
                        <th>Post Code</th>
                        <td>{{registrantContactDetails.postCode}}</td>
                        <td>{{billingContactDetails.postCode}}</td>
                        <td>{{adminContactDetails.postCode}}</td>
                        <td>{{technicalContactDetails.postCode}}</td>
                    </tr>
                    <tr>
                        <th>Country</th>
                        <td>{{registrantContactDetails.country}}</td>
                        <td>{{billingContactDetails.country}}</td>
                        <td>{{adminContactDetails.country}}</td>
                        <td>{{technicalContactDetails.country}}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </btcl-portlet>

        <div class=row>
            <div class=col-md-7>
                <btcl-portlet title="Connection">
                    <div class="table-responsive" style="max-height:300px">
                        <table class="table">
                            <thead>
                            <tr>
                                <th>#</th>
                                <th>Name</th>
                                <th>Bandwidth (Mbps)</th>
                                <th>Status</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr v-for="(connection, connectionIndex) in connectionList">
                                <td>{{ connectionIndex + 1 }}</td>
                                <td><a :href="contextPath + 'lli/connection/view.do?id=' + connection.ID">{{connection.name}}</a>
                                </td>
                                <td>{{connection.bandwidth}}</td>
                                <td>{{connection.status.label}}</td>
                            </tr>
                            <tr v-if="connectionList.length == 0">
                                <td colspan=4>No Connections</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </btcl-portlet>

                <btcl-portlet title="Long Term Contract">
                    <div class="table-responsive" style="max-height:300px">
                        <table class="table">
                            <thead>
                            <tr>
                                <th>#</th>
                                <th>ID</th>
                                <th>Start Date</th>
                                <th>Bandwidth (Mbps)</th>
                                <th>Status</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr v-for="(contract, contractIndex) in contractList">
                                <td>{{ contractIndex + 1 }}</td>
                                <td>
                                    <a :href="contextPath + 'lli/longterm/view.do?id=' + contract.ID">{{contract.ID}}</a>
                                </td>
                                <td>{{ new Date(contract.contractStartDate).toLocaleDateString("ca-ES")}}</td>
                                <td>{{contract.bandwidth}}</td>
                                <td>{{contract.status.label}}</td>
                            </tr>
                            <tr v-if="contractList.length == 0">
                                <td colspan=5>No Contracts</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </btcl-portlet>
            </div>

            <div class=col-md-5>
                <btcl-portlet title="Temporary Disconnection">
                    <div class="table-responsive" style="max-height:300px">
                        <table class="table">
                            <thead>
                            <tr>
                                <th>#</th>
                                <th>From</th>
                                <th>To</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr v-for="(tdHistory, tdHistoryIndex) in tdHistoryList">
                                <td>{{ tdHistoryIndex + 1 }}</td>
                                <td>{{ new Date(tdHistory.tdFrom).toLocaleDateString("ca-ES")}}</td>
                                <td>{{ new Date(tdHistory.tdTo).toLocaleDateString("ca-ES")}}</td>
                            </tr>
                            <tr v-if="tdHistoryList.length == 0">
                                <td colspan=3>Never Temporarily Disconnected</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class=well>
                        <h4>TD Date: {{ (clientTDDate == 0 || typeof clientTDDate == "undefined" ) ? "Uncalculated" :
                            new Date(clientTDDate).toLocaleDateString("ca-ES")}}</h4>
                    </div>
                </btcl-portlet>

                <btcl-portlet title="Account Summary">
                    <div class="table-responsive" style="max-height:300px">
                        <table class="table">
                            <thead>
                            <tr>
                                <th>Account</th>
                                <th>Balance (BDT)</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr v-for="(account, accountIndex) in accountList">
                                <td>{{ account.name }}</td>
                                <td>{{ parseFloat(account.balance).toFixed(2) }}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </btcl-portlet>

            </div>
        </div>

    </btcl-body>
</div>

<script>
    var clientID = '<%=request.getParameter("id")%>';
    if (clientID === 'null') goHome();
</script>
<script src=lli-client-board.js></script>


