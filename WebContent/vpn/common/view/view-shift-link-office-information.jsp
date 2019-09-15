<%--
  Created by IntelliJ IDEA.
  User: forhad
  Date: 2/29/19
  Time: 11:41 AM
  To change this template use File | Settings | File Templates.
--%>
<div class="form-group">
    <div class=row>
        <div class="col-sm-6">
            <btcl-portlet title="Local End" v-if="this.localEndEdit">
                <btcl-field title="Office Selection Type">
                    <multiselect v-model="application.localEnd.officeSelectionType"
                                 :options="officeSelectionType"
                                 label=value :allow-empty="false" :searchable=true open-direction="bottom">
                    </multiselect>
                </btcl-field>


                <template v-if="application.localEnd.officeSelectionType">
                    <%--if new office create--%>
                    <template v-if="application.localEnd.officeSelectionType.id == 2">
                        <btcl-input title="Office Name" :text.sync="application.localEnd.officeName"
                                    placeholder="Write Office Name"></btcl-input>

                        <btcl-input title="Office Address" :text.sync="application.localEnd.officeAddress"
                                    placeholder="Write Office Address"></btcl-input>


                        <btcl-field title="Loop">
                            <multiselect v-model="application.localEnd.loop" :options="localEndLoopList"
                                         label=value :allow-empty="false" :searchable=true open-direction="bottom">
                            </multiselect>
                        </btcl-field>
                    </template>
                    <%--if existing office selected--%>
                    <template v-if="application.localEnd.officeSelectionType.id == 1">
                        <btcl-field title="Select Office">
                            <multiselect v-model="application.localEnd.office" :options="officeList"
                                         label=officeName :allow-empty="false" :searchable=true open-direction="bottom">
                            </multiselect>
                        </btcl-field>

                        <template v-if="application.localEnd.office">
                            <btcl-field title="Use Existing Local Loop">
                                <multiselect v-model="application.localEnd.useExistingLoop"
                                             :options="useExistingLoopList"
                                             label=value :allow-empty="false" :searchable=true open-direction="bottom">
                                </multiselect>
                            </btcl-field>
                            <template v-if="application.localEnd.useExistingLoop">
                                <%--if existing loop selected--%>
                                <template v-if="application.localEnd.useExistingLoop.id==1">
                                    <btcl-field title="Select Local Loop">
                                        <multiselect v-model="application.localEnd.office.localLoop"
                                                     :options="application.localEnd.office.localLoops"
                                                     label=popName :allow-empty="false" :searchable=true
                                                     open-direction="bottom">
                                        </multiselect>
                                    </btcl-field>
                                </template>
                            </template>
                        </template>
                    </template>
                </template>
                <btcl-field title="Terminal Device Provider">
                    <multiselect v-model="application.localEnd.terminalDeviceProvider"
                                 :options="localEndTerminalDeviceProviderList"
                                 label=value :allow-empty="false" :searchable=true open-direction="bottom">
                    </multiselect>
                </btcl-field>
            </btcl-portlet>
            <btcl-portlet title="Local End" v-else>
                <%--<btcl-field title="Office Selection Type">--%>
                <%--<multiselect v-model="application.localEnd.officeSelectionType"--%>
                <%--:options="officeSelectionType"--%>
                <%--label=value :allow-empty="false" :searchable=true open-direction="bottom">--%>
                <%--</multiselect>--%>
                <%--</btcl-field>--%>

                <btcl-info title="Office Selection Type" :text.sync="application.localEnd.officeSelectionType.value"></btcl-info>
                <btcl-info title="Office Name" :text.sync="application.localEnd.officeName"></btcl-info>
                <btcl-info title="Office Address" :text.sync="application.localEnd.officeAddress"></btcl-info>
                <btcl-info title="Local Loop" :text.sync="application.localEnd.office.localLoop.popName"></btcl-info>
                <btcl-info title="Terminal Device Provider" :text.sync="application.localEnd.terminalDeviceProvider.value"></btcl-info>
            </btcl-portlet>

            <div align=right v-if="application.hasOwnProperty('applicationType')">
                <button
                <%--v-if="application.remoteEnds.length>1"--%>
                        type=button
                        @click="toggleLocalEndEdit()"
                        class="btn green btn-outline"
                >
                    <span class="glyphicon glyphicon-pencil"></span>
                </button>
            </div>

        </div>
        <div class="col-sm-6">
            <template v-if="this.remoteEndEdit">
                <btcl-portlet
                        :title="'Remote End'"
                        v-for="(remoteOfficeEnd,remoteEndIndex) in application.remoteEnds"

                >


                    <btcl-field title="Office Selection Type">
                        <multiselect v-model="remoteOfficeEnd.officeSelectionType"
                                     :options="officeSelectionType"
                                     label=value :allow-empty="false" :searchable=true open-direction="bottom">
                        </multiselect>
                    </btcl-field>
                    <template v-if="remoteOfficeEnd.officeSelectionType">
                        <%--if new office create--%>
                        <template v-if="remoteOfficeEnd.officeSelectionType.id == 2">


                            <btcl-input title="Office Name" :text.sync="remoteOfficeEnd.officeName"
                                        placeholder="Write Office Name"></btcl-input>

                            <btcl-input title="Office Address" :text.sync="remoteOfficeEnd.officeAddress"
                                        placeholder="Write Office Address"></btcl-input>

                            <btcl-field title="Loop Provider">
                                <multiselect v-model="remoteOfficeEnd.loop" :options="remoteEndLoopList"
                                             label=value :allow-empty="false" :searchable=true open-direction="bottom">
                                </multiselect>
                            </btcl-field>
                        </template>
                        <%--if existing office selected--%>
                        <template v-if="remoteOfficeEnd.officeSelectionType.id == 1">
                            <btcl-field title="Select Office">
                                <multiselect v-model="remoteOfficeEnd.office" :options="officeList"
                                             label=officeName :allow-empty="false" :searchable=true
                                             open-direction="bottom">
                                </multiselect>
                            </btcl-field>

                            <template v-if="remoteOfficeEnd.office">
                                <btcl-field title="Use Existing Local Loop">
                                    <multiselect v-model="remoteOfficeEnd.useExistingLoop" :options="useExistingLoopList"
                                                 label=value :allow-empty="false" :searchable=true
                                                 open-direction="bottom">
                                    </multiselect>
                                </btcl-field>
                                <template v-if="remoteOfficeEnd.useExistingLoop">
                                    <%--if existing loop selected--%>
                                    <template v-if="remoteOfficeEnd.useExistingLoop.id==1">
                                        <btcl-field title="Select Local Loop">
                                            <multiselect v-model="remoteOfficeEnd.office.localLoop"
                                                         :options="remoteOfficeEnd.office.localLoops"
                                                         label=popName :allow-empty="false" :searchable=true
                                                         open-direction="bottom">
                                            </multiselect>
                                        </btcl-field>
                                    </template>
                                </template>
                            </template>
                        </template>
                    </template>
                    <btcl-input title="Bandwidth" :text.sync="remoteOfficeEnd.bandwidth" placeholder="Enter Bandwidth" :number="true"></btcl-input>
                    <btcl-field title="Terminal Device Provider">
                        <multiselect v-model="remoteOfficeEnd.terminalDeviceProvider"
                                     :options="remoteEndTerminalDeviceProviderList"
                                     label=value :allow-empty="false" :searchable=true open-direction="bottom">
                        </multiselect>
                    </btcl-field>
                    <div align=right>
                        <button
                                v-if="application.remoteEnds.length>1"
                                type=button
                                @click="deleteFromRepeater(application, 'remoteEnds',remoteEndIndex)"
                                class="btn red btn-outline">
                            <span class="glyphicon glyphicon-remove"></span>
                        </button>
                    </div>
                    <br>


                </btcl-portlet>
            </template>

            <template v-else>
                <btcl-portlet
                        :title="'Remote End'"
                        v-for="(remoteEnd,remoteEndIndex) in application.remoteEnds"
                >

                    <btcl-info title="Office Selection Type" :text.sync="remoteEnd.officeSelectionType.value"></btcl-info>
                    <btcl-info title="Office Name" :text.sync="remoteEnd.officeName"></btcl-info>
                    <btcl-info title="Office Address" :text.sync="remoteEnd.officeAddress"></btcl-info>
                    <btcl-input title="Bandwidth" :text.sync="remoteEnd.bandwidth" placeholder="Enter Bandwidth" :number="true"></btcl-input>
                    <btcl-info title="Local Loop" :text.sync="remoteEnd.office.localLoop.popName"></btcl-info>
                    <btcl-info title="Terminal Device Provider" :text.sync="remoteEnd.terminalDeviceProvider.value"></btcl-info>
                    <div align=right>
                        <button
                                v-if="application.remoteEnds.length>1"
                                type=button
                                @click="deleteFromRepeater(application, 'remoteEnds',remoteEndIndex)"
                                class="btn red btn-outline">
                            <span class="glyphicon glyphicon-remove"></span>
                        </button>
                    </div>
                    <br>


                </btcl-portlet>
            </template>
            <div align="right">

                <template
                        v-if="application.hasOwnProperty('vpnApplicationLinks')  || application.hasOwnProperty('applicationType')">
                    <template v-if="application.hasOwnProperty('vpnApplicationLinks')">
                        <template v-if="application.vpnApplicationLinks.length>0">
                            <template
                                    v-if="application.vpnApplicationLinks[0].state.name!='VPN_REQUESTED_CLIENT_FOR_CORRECTION'">
                                <button type=button
                                        @click="addRemoteEnd()"
                                        class="btn green-haze btn-info">
                                    <span class="glyphicon glyphicon-plus"></span>
                                </button>
                            </template>
                        </template>

                    </template>
                </template>
                <template v-else>
                    <button type=button
                            @click="addRemoteEnd()"
                            class="btn green-haze btn-info">
                        <span class="glyphicon glyphicon-plus"></span>
                    </button>
                </template>

            </div>

            <div align=right v-if="application.hasOwnProperty('applicationType')">
                <button
                <%--v-if="application.remoteEnds.length>1"--%>
                        type=button
                <%--@click="deleteFromRepeater(application, 'remoteEnds',remoteEndIndex)"--%>
                        @click="toggleRemoteEndEdit()"
                        class="btn green btn-outline"
                >
                    <span class="glyphicon glyphicon-pencil"></span>
                </button>
            </div>
        </div>
    </div>
</div>
<br>