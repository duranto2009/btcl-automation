<%@page import="file.FileTypeConstants" %>
<%@page import="common.EntityTypeConstant" %>
<%@ page import="common.ModuleConstants" %>
<%
    String context = "../.." + request.getContextPath() + "/";

%>
<script>
    var moduleID =<%=ModuleConstants.Module_ID_LLI%>;

</script>
<div id=btcl-connection>
    <btcl-body title="New Connection" subtitle="Entry">
        <%--        <btcl-form :action="contextPath + 'lli/connection/new-connection.do'" :name="['connection']" :form-data="[connection]" :redirect="goView">--%>
        <div>

            <%--<form>--%>
            <btcl-portlet>
                <btcl-field title="Client">
                    <lli-client-search :client.sync="connection.client">Client</lli-client-search>
                </btcl-field>

                <%--                <btcl-field title="Client">--%>
                <%--                    <user-search-by-module :client.sync="connection.client" :module="moduleId">Client--%>
                <%--                    </user-search-by-module>--%>
                <%--                </btcl-field>--%>

                <btcl-field title="Connection Type">

                    <div>
                        <multiselect v-if="connection.client!=null" v-model="connection.connectionType"
                                     :options="connectionTypeOptions"
                                     label=label :allow-empty="false" :searchable=true open-direction="bottom">
                        </multiselect>
                        <p v-else class=form-control>Select Client First</p>
                    </div>


                    <%--                    <connection-type :data.sync=connection.connectionType--%>
                    <%--                                     :client="connection.client"></connection-type>--%>
                </btcl-field>

            </btcl-portlet>
            <btcl-bounded>
                <btcl-portlet :title="'connection ' + (connectionIndex + 1)"
                              v-for="(connection, connectionIndex) in connectionList">

                    <btcl-input title="Connection Name" :text.sync="connection.name"
                                placeholder="Enter Name">

                    </btcl-input>
                    <btcl-input title="Bandwidth (Mbps)" :text.sync="connection.bandwidth" :number="true"
                                placeholder="Enter Bandwidth"></btcl-input>


                    <btcl-field title="Zone">
                        <lli-zone-search :client.sync="connection.zone">Zone</lli-zone-search>
                    </btcl-field>
                    <btcl-field title="Loop Provider">
                        <multiselect v-model="connection.loopProvider"
                                     :options="[{ID:1,label:'BTCL'},{ID:2,label:'Client'}]"
                                     label=label :allow-empty="false" :searchable=true
                                     open-direction="bottom">
                        </multiselect>
                    </btcl-field>

                    <btcl-field title="Start Date">
                        <btcl-datepicker :date.sync="connection.activeFrom"></btcl-datepicker>
                    </btcl-field>

                    <btcl-bounded v-for="(office,officeIndex) in connection.officeList"
                                  :title="'Office '+(officeIndex+1)"
                                  :key="officeIndex">


                        <%--if new office create--%>


                        <btcl-input title="Office Name" :text.sync="office.officeName"
                                    placeholder="Write Office Name"></btcl-input>

                        <btcl-input title="Office Address" :text.sync="office.officeAddress"
                                    placeholder="Write Office Address"></btcl-input>

                        <%--                                <btcl-field title="Loop Provider">--%>
                        <%--                                    <multiselect v-model="office.loop" :options="loopProviderList"--%>
                        <%--                                                 label=value :allow-empty="false" :searchable=true open-direction="bottom">--%>
                        <%--                                    </multiselect>--%>
                        <%--                                </btcl-field>--%>

<%--                        <template v-if="connection.loopProvider.ID==1">--%>
                            <btcl-input title="BTCL Length" :text.sync="office.btclLength" :number="true"
                                        placeholder="Insert BTCL Length"></btcl-input>

                            <btcl-input title="OC Length" :text.sync="office.ocLength" :number="true"
                                        placeholder="Insert OC Length"></btcl-input>
                            <btcl-field title="OFC Type">
                                <multiselect v-model="office.ofcType"
                                             :options="[{ID:1,label:'Single'},{ID:2,label:'Dual'}]"
                                             label=label :allow-empty="false" :searchable=true
                                             open-direction="bottom">
                                </multiselect>
                            </btcl-field>
<%--                        </template>--%>


                        <template v-if="office.ocLength>0">
                            <btcl-field title="Vendor">
                                <vendor-search :vendor.sync="office.oc">Vendor</vendor-search>
                            </btcl-field>
                        </template>


                        <div align=right v-if="officeIndex!=0">
                            <button type=button
                            <%--@click="deleteLocalLoop(content, officeIndex, officeIndex, $event)"--%>
                                    @click="deleteOffice(connection,officeIndex, $event)"
                                    class="btn red btn-outline">
                                x
                            </button>
                        </div>
                    </btcl-bounded>
                    <div align=right>
                        <button type=button
                        <%--@click="addLocalLoop(office,content.bandwidth, $event)" --%>
                                @click="addOffice(connection,  $event)"
                                class="btn green-haze btn-outline">
                            +
                        </button>
                    </div>
                    <%--</btcl-portlet>--%>


                </btcl-portlet>
                <div align=right>
                    <button type=button
                    <%--@click="addLocalLoop(office,content.bandwidth, $event)" --%>
                            @click="addNewconnection()"
                            class="btn green-haze btn-outline">
                        Add More connection
                    </button>
                </div>
            </btcl-bounded>

            <div style="padding-top:30px;">
                <button v-on:click="submitFormData" type="submit" class="btn green-haze btn-block btn-lg">Submit
                </button>
            </div>
        </div>

        <%--        </btcl-form>--%>
        <%--<div style="padding-top:30px;">--%>
        <%--<button v-on:click="submitFormData" type="submit" class="btn green-haze btn-block btn-lg">Submit</button>--%>
        <%--</div>--%>
        <%--</form>--%>
    </btcl-body>


</div>
<script src="${context}dataEntry/common.js" type="text/javascript"></script>
<%--<script src="../connection/lli-connection-components.js"></script>--%>
<%--<script src=lli-connection-components.js></script>--%>
<script src="${context}dataEntry/connection-entry.js"></script>