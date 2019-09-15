<btcl-portlet title="Contract Information">
    <%--<btcl-info title="Client" :text=connection.client.value></btcl-info>--%>
        <div class="row">
            <div class="col-md-6" style=" border-right: 1px dashed #333;">
                <btcl-info title="Contract Id" :text=contract.contractId></btcl-info>
                <btcl-info title="media" :text="getObjectValueFromObjectListByKey(mediaList,contract.mediaId)"></btcl-info>
                <btcl-info title="Service Location"
                           :text="getObjectValueFromObjectListByKey(btclServiceLocationList,contract.btclServiceLocationId)"></btcl-info>
                <btcl-info title="Provider " :text="getObjectValueFromObjectListByKey(providerList,contract.selectedProviderId)"></btcl-info>
                <btcl-info title="Provider Location" :text="getObjectValueFromObjectListByKey(providerLocationList,contract.providerLocationId)"></btcl-info>
                <btcl-info title="Bandwidth Type"
                           :text="getObjectValueFromObjectListByKey(typeOfBandwidthList,contract.typeOfBandwidthId)"></btcl-info>
            </div>
            <div class="col-md-6">
                <btcl-info title="Contract Duration" :text="convertMillisecondsToYearMonthDay(contract.contractDuration)" ></btcl-info>
                <btcl-info title="SRF Date" :text="contract.srfDate" :date="true"></btcl-info>
                <btcl-info title="Bandwidth Capacity(GB)" :text="contract.bandwidthCapacity" :number="true"></btcl-info>

                <btcl-info title="Bandwidth Price(USD)" :text="contract.bandwidthPrice" :number="true"></btcl-info>
                <btcl-info title="OTC(USD)" :text="contract.otc" :number="true"></btcl-info>
                <btcl-info title="MRC(USD)" :text="contract.mrc" :number="true"></btcl-info>
            </div>
        </div>


    <%--<btcl-info title="Discount Rate" :text="connection.discountRate"></btcl-info>--%>
    <%--<btcl-info title="Status" :text="connection.status.label"></btcl-info>--%>

    <div class=row>

    </div>

</btcl-portlet>