<%--
  Created by IntelliJ IDEA.
  User: forhad
  Date: 3/27/19
  Time: 9:41 AM
--%>

<%--<btcl-body title="Circuit Information" v-if="application.state.view === 'circuit-info'">--%>
<%--<btcl-portlet title ="Link info of BSCCL" >--%>
<%--<btcl-bounded>--%>
<%--<btcl-input title="Location" :text.sync="application.linkInfoBSCCL.location" placeholder="Write Location Name" ></btcl-input>--%>
<%--<btcl-input title="Device Name" :text.sync="application.linkInfoBSCCL.deviceName" placeholder="Write Device Name" ></btcl-input>--%>
<%--<btcl-input title="Shelf Number" :text.sync="application.linkInfoBSCCL.shelfNumber" placeholder="Write Shelf Number" :number="true"></btcl-input>--%>
<%--<btcl-input title="Card Number" :text.sync="application.linkInfoBSCCL.cardNumber" placeholder="Enter Card Number" :number="true"></btcl-input>--%>
<%--<btcl-input title="Port Number" :text.sync="application.linkInfoBSCCL.portNumber" placeholder="Enter Port Number" :number="true"></btcl-input>--%>
<%--</btcl-bounded>--%>
<%--</btcl-portlet>--%>

<%--<btcl-portlet title ="BTCL's work Order to Provider" >--%>
<%--<btcl-input title="Circuit ID" :text.sync="application.btclWOToProvider.circuitId" placeholder="Enter Circuit ID" :number="true"></btcl-input>--%>
<%--<btcl-input title="Location" :text.sync="application.btclWOToProvider.location" placeholder="Write Location Name" ></btcl-input>--%>
<%--<btcl-input title="Device Name" :text.sync="application.btclWOToProvider.deviceName" placeholder="Write Device Name" ></btcl-input>--%>
<%--<btcl-input title="Shelf Number" :text.sync="application.btclWOToProvider.shelfNumber" placeholder="Write Shelf Number" :number="true"></btcl-input>--%>
<%--<btcl-input title="Card Number" :text.sync="application.btclWOToProvider.cardNumber" placeholder="Enter Card Number" :number="true"></btcl-input>--%>
<%--<btcl-input title="Port Number" :text.sync="application.btclWOToProvider.portNumber" placeholder="Enter Port Number" :number="true"></btcl-input>--%>
<%--</btcl-portlet>--%>

<%--<btcl-portlet title ="BTCL's work Order to Provider" >--%>
<%--<btcl-input title="Location" :text.sync="application.letterOfbtclBackhaul.location" placeholder="Write Location Name" ></btcl-input>--%>
<%--<btcl-input title="Device Name" :text.sync="application.letterOfbtclBackhaul.deviceName" placeholder="Write Device Name" ></btcl-input>--%>
<%--<btcl-input title="Shelf Number" :text.sync="application.letterOfbtclBackhaul.shelfNumber" placeholder="Write Shelf Number" :number="true"></btcl-input>--%>
<%--<btcl-input title="Card Number" :text.sync="application.letterOfbtclBackhaul.cardNumber" placeholder="Enter Card Number" :number="true"></btcl-input>--%>
<%--<btcl-input title="Port Number" :text.sync="application.letterOfbtclBackhaul.portNumber" placeholder="Enter Port Number" :number="true"></btcl-input>--%>
<%--</btcl-portlet>--%>

<%--<button v-if="application.contractDetail" type=button class="btn green-haze btn-block btn-lg"--%>
<%--@click="updateCircuitInformation"--%>
<%-->Update Circuit Information</button>--%>

<%--</btcl-body>--%>


<btcl-body title="Circuit Information" v-if="application.state.view === 'circuit-info'">
    <btcl-portlet title="Link info of BSCCL">
        <btcl-bounded v-for="(linkInfoBSCCL, linkInfoBSCCLIndex) in application.linkInfoBSCCLs">
            <btcl-input title="Location" :text.sync="linkInfoBSCCL.location"
                        placeholder="Write Location Name"></btcl-input>
            <btcl-input title="Device Name" :text.sync="linkInfoBSCCL.deviceName"
                        placeholder="Write Device Name"></btcl-input>
            <btcl-input title="Shelf Number" :text.sync="linkInfoBSCCL.shelfNumber" placeholder="Write Shelf Number"
                        :number="true"></btcl-input>
            <btcl-input title="Card Number" :text.sync="linkInfoBSCCL.cardNumber" placeholder="Enter Card Number"
                        :number="true"></btcl-input>
            <btcl-input title="Port Number" :text.sync="linkInfoBSCCL.portNumber" placeholder="Enter Port Number"
                        :number="true"></btcl-input>


            <div align=right>
                <button
                        v-if="application.linkInfoBSCCLs.length>1"
                        type=button
                        @click="deleteFromRepeater(application, 'linkInfoBSCCLs',linkInfoBSCCLIndex)"
                        class="btn red btn-outline">
                    <span class="glyphicon glyphicon-remove"></span>
                </button>
            </div>



        </btcl-bounded>

        <div align="right">
            <template>
                <button type=button
                        @click="addEmptyObject('linkInfoBSCCLs')"
                        class="btn green-haze btn-info">
                    <span class="glyphicon glyphicon-plus"></span>
                </button>
            </template>

        </div>
    </btcl-portlet>

    <btcl-portlet title="BTCL's work Order to Provider">
        <btcl-bounded v-for="(btclWOToProvider, btclWOToProviderIndex) in application.btclWOToProviders">
            <btcl-input title="Circuit ID" :text.sync="btclWOToProvider.circuitId" placeholder="Enter Circuit ID"
                        :number="true"></btcl-input>
            <btcl-input title="Location" :text.sync="btclWOToProvider.location"
                        placeholder="Write Location Name"></btcl-input>
            <btcl-input title="Device Name" :text.sync="btclWOToProvider.deviceName"
                        placeholder="Write Device Name"></btcl-input>
            <btcl-input title="Shelf Number" :text.sync="btclWOToProvider.shelfNumber" placeholder="Write Shelf Number"
                        :number="true"></btcl-input>
            <btcl-input title="Card Number" :text.sync="btclWOToProvider.cardNumber" placeholder="Enter Card Number"
                        :number="true"></btcl-input>
            <btcl-input title="Port Number" :text.sync="btclWOToProvider.portNumber" placeholder="Enter Port Number"
                        :number="true"></btcl-input>

            <div align=right>
                <button
                        v-if="application.btclWOToProviders.length>1"
                        type=button
                        @click="deleteFromRepeater(application, 'btclWOToProviders',btclWOToProviderIndex)"
                        class="btn red btn-outline">
                    <span class="glyphicon glyphicon-remove"></span>
                </button>
            </div>



        </btcl-bounded>
        <div align="right">
            <template>
                <button type=button
                        @click="addEmptyObject('btclWOToProviders')"
                        class="btn green-haze btn-info">
                    <span class="glyphicon glyphicon-plus"></span>
                </button>
            </template>

        </div>
    </btcl-portlet>

    <btcl-portlet title="BTCL's work Order to Provider">
        <btcl-bounded v-for="(letterOfbtclBackhaul, letterOfbtclBackhaulIndex) in application.letterOfbtclBackhauls">

            <btcl-input title="Location" :text.sync="letterOfbtclBackhaul.location"
                        placeholder="Write Location Name"></btcl-input>
            <btcl-input title="Device Name" :text.sync="letterOfbtclBackhaul.deviceName"
                        placeholder="Write Device Name"></btcl-input>
            <btcl-input title="Shelf Number" :text.sync="letterOfbtclBackhaul.shelfNumber"
                        placeholder="Write Shelf Number" :number="true"></btcl-input>
            <btcl-input title="Card Number" :text.sync="letterOfbtclBackhaul.cardNumber" placeholder="Enter Card Number"
                        :number="true"></btcl-input>
            <btcl-input title="Port Number" :text.sync="letterOfbtclBackhaul.portNumber" placeholder="Enter Port Number"
                        :number="true"></btcl-input>

            <div align=right>
                <button
                        v-if="application.letterOfbtclBackhauls.length>1"
                        type=button
                        @click="deleteFromRepeater(application, 'letterOfbtclBackhauls',letterOfbtclBackhaulIndex)"
                        class="btn red btn-outline">
                    <span class="glyphicon glyphicon-remove"></span>
                </button>
            </div>


        </btcl-bounded>

        <div align="right">
            <template>
                <button type=button
                        @click="addEmptyObject('letterOfbtclBackhauls')"
                        class="btn green-haze btn-info">
                    <span class="glyphicon glyphicon-plus"></span>
                </button>
            </template>

        </div>
    </btcl-portlet>

    <button v-if="application.contractDetail" type=button class="btn green-haze btn-block btn-lg"
            @click="updateCircuitInformation"
    >Update Circuit Information
    </button>

</btcl-body>