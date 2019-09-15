<%--
  Created by IntelliJ IDEA.
  User: forhad
  Date: 3/31/19
  Time: 11:47 AM
--%>
<btcl-field title="Type of Bandwidth">
    <multiselect placeholder="Select Type" v-model="application.typeOfBandwidth"
                 :options="typeOfBandwidthList" label=value :allow-empty="false" :searchable=true
                 open-direction="bottom">

    </multiselect>
</btcl-field>

<btcl-input title="Required Bandwidth Capacity(GB)" :text.sync="application.bandwidthCapacity"
            placeholder="Enter Bandwidth Capactiy Amount" :number="true"></btcl-input>

<btcl-field title="Provider(Upstream)">
    <multiselect placeholder="Select Type" v-model="application.provider"
                 :options="providerList" label=value :allow-empty="false" :searchable=true
                 open-direction="bottom">

    </multiselect>
</btcl-field>

<btcl-field title="Provider(Upstream) Location">
    <multiselect placeholder="Select Type" v-model="application.providerLocation"
                 :options="providerLocationList" label=value :allow-empty="false" :searchable=true
                 open-direction="bottom">

    </multiselect>
</btcl-field>


<btcl-field title="BTCL Service Location">
    <multiselect placeholder="Select Type" v-model="application.btclServiceLocation"
                 :options="btclServiceLocationList" label=value :allow-empty="false" :searchable=true
                 open-direction="bottom">

    </multiselect>
</btcl-field>


<btcl-field title="Media">
    <multiselect placeholder="Select Type" v-model="application.media"
                 :options="mediaList" label=value :allow-empty="false" :searchable=true
                 open-direction="bottom">

    </multiselect>
</btcl-field>