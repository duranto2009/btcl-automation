<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html" %>
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
            <btcl-portlet title="Local End">
                <btcl-field title="Office Selection Type" required = "true">
                    <%--<select class="form-control" v-model="application.localEnd.officeSelectionType">
                        <option v-for="officeType in officeSelectionType" :value="officeType">{{officeType.value}}</option>
                    </select>--%>
                    <multiselect v-model="application.localEnd.officeSelectionType"
                                 :options="officeSelectionType"
                                 label=value :allow-empty="false" :searchable=true open-direction="bottom">
                    </multiselect>
                </btcl-field>


                <template v-if="application.localEnd.officeSelectionType">
                    <%--if new office create--%>
                    <template v-if="application.localEnd.officeSelectionType.id == 2">
                        <btcl-input title="Office Name" required = "true" :text.sync="application.localEnd.officeName "
                                    placeholder="Write Office Name"></btcl-input>

                        <btcl-input title="Office Address" required = "true" :text.sync="application.localEnd.officeAddress"
                                    placeholder="Write Office Address"></btcl-input>


                        <btcl-field title="Loop" required = "true">
                            <multiselect v-model="application.localEnd.loop" :options="localEndLoopList"
                                         label=value :allow-empty="false" :searchable=true open-direction="bottom">
                            </multiselect>
                        </btcl-field>
                    </template>
                    <%--if existing office selected--%>
                    <template v-if="application.localEnd.officeSelectionType.id == 1">
                        <btcl-field title="Select Office" required = "true">
                            <multiselect v-model="application.localEnd.office" :options="officeList"
                                         label=officeName :allow-empty="false" :searchable=true open-direction="bottom">
                            </multiselect>
                        </btcl-field>

                        <template v-if="application.localEnd.office">
                            <btcl-field title="Use Existing Local Loop" required = "true">
                                <multiselect v-model="application.localEnd.useExistingLoop"
                                             :options="useExistingLoopList"
                                             label=value :allow-empty="false" :searchable=true open-direction="bottom">
                                </multiselect>
                            </btcl-field>
                            <template v-if="application.localEnd.useExistingLoop">
                                <%--if existing loop selected--%>
                                <template v-if="application.localEnd.useExistingLoop.id==1">
                                    <btcl-field title="Select Local Loop" required = "true">
                                        <multiselect v-model="application.localEnd.office.localLoop"
                                                     :options="application.localEnd.office.localLoops"
                                                     label=popName :allow-empty="false" :searchable=true
                                                     open-direction="bottom">
                                        </multiselect>
                                    </btcl-field>
                                </template>


                                    <template v-if="application.localEnd.useExistingLoop.id==2">
                                        <btcl-field title="Loop" required = "true">
                                            <multiselect v-model="application.localEnd.loop" :options="localEndLoopList"
                                                         label=value :allow-empty="false" :searchable=true open-direction="bottom">
                                            </multiselect>
                                        </btcl-field>
                                    </template>
                            </template>
                        </template>
                    </template>
                </template>
                <btcl-field title="Terminal Device Provider" required = "true">
                    <multiselect v-model="application.localEnd.terminalDeviceProvider"
                                 :options="localEndTerminalDeviceProviderList"
                                 label=value :allow-empty="false" :searchable=true open-direction="bottom">
                    </multiselect>
                </btcl-field>
            </btcl-portlet>
        </div>
        <div class="col-sm-6">
            <btcl-portlet
                    :title="'Remote End ' + (remoteEndIndex+1)"
                    v-for="(remoteEnd,remoteEndIndex) in application.remoteEnds">


                <btcl-field title="Office Selection Type" required = "true">
                    <multiselect v-model="remoteEnd.officeSelectionType"
                                 :options="officeSelectionType"
                                 label=value :allow-empty="false" :searchable=true open-direction="bottom">
                    </multiselect>
                </btcl-field>
                <template v-if="remoteEnd.officeSelectionType">
                    <%--if new office create--%>
                    <template v-if="remoteEnd.officeSelectionType.id == 2">


                        <btcl-input title="Office Name" required = "true" :text.sync="remoteEnd.officeName"
                                    placeholder="Write Office Name"></btcl-input>

                        <btcl-input title="Office Address" required = "true" :text.sync="remoteEnd.officeAddress"
                                    placeholder="Write Office Address"></btcl-input>

                        <btcl-field title="Loop Provider" required = "true">
                            <multiselect v-model="remoteEnd.loop" :options="remoteEndLoopList"
                                         label=value :allow-empty="false" :searchable=true open-direction="bottom">
                            </multiselect>
                        </btcl-field>
                    </template>
                    <%--if existing office selected--%>
                    <template v-if="remoteEnd.officeSelectionType.id == 1">
                        <btcl-field title="Select Office" required = "true">
                            <multiselect v-model="remoteEnd.office" :options="officeList"
                                         label=officeName :allow-empty="false" :searchable=true open-direction="bottom">
                            </multiselect>
                        </btcl-field>

                        <template v-if="remoteEnd.office">
                            <btcl-field title="Use Existing Local Loop" required = "true">
                                <multiselect v-model="remoteEnd.useExistingLoop" :options="useExistingLoopList"
                                             label=value :allow-empty="false" :searchable=true open-direction="bottom">
                                </multiselect>
                            </btcl-field>
                            <template v-if="remoteEnd.useExistingLoop">
                                <%--if existing loop selected--%>
                                <template v-if="remoteEnd.useExistingLoop.id==1">
                                    <btcl-field title="Select Local Loop" required = "true">
                                        <multiselect v-model="remoteEnd.office.localLoop"
                                                     :options="remoteEnd.office.localLoops"
                                                     label=popName :allow-empty="false" :searchable=true
                                                     open-direction="bottom">
                                        </multiselect>
                                    </btcl-field>
                                </template>

                                    <template v-if="remoteEnd.useExistingLoop.id==2">
                                        <btcl-field title="Loop Provider" required = "true">
                                            <multiselect v-model="remoteEnd.loop" :options="remoteEndLoopList"
                                                         label=value :allow-empty="false" :searchable=true open-direction="bottom">
                                            </multiselect>
                                        </btcl-field>
                                    </template>
                            </template>
                        </template>
                    </template>
                </template>
                <btcl-input title="Bandwidth (Mbps)" :text.sync="remoteEnd.bandwidth" placeholder="Enter Bandwidth (Mbps)" required = "true"
                            :number="true"></btcl-input>
                <btcl-field title="Terminal Device Provider" required = "true">
                    <multiselect v-model="remoteEnd.terminalDeviceProvider"
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
            <div align="right">

                <template v-if="application.hasOwnProperty('vpnApplicationLinks')  || application.hasOwnProperty('applicationType')">
                    <template v-if="application.hasOwnProperty('vpnApplicationLinks')">
                        <template  v-if="application.vpnApplicationLinks.length>0">
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
        </div>
    </div>
</div>
<br>