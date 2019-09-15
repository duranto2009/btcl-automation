<%--
  Created by IntelliJ IDEA.
  User: forhad
  Date: 2/26/19
  Time: 9:46 AM
  To change this template use File | Settings | File Templates.
--%>
<%--<btcl-body title="Documents" v-if="application.state.view=='DEMAND_NOTE'">--%>
<template v-if="application.vpnApplicationLinks!=null && application.vpnApplicationLinks.length>0">
    <btcl-body title="Documents" v-if="globalDemandNote()">
        <btcl-portlet>
            <div align="center">

                <button type="submit" class="btn green-haze" @click="redirect('demandnote')">View Demand Note
                </button>
            </div>
        </btcl-portlet>
    </btcl-body>


    <btcl-portlet
    <%--v-if="application.state==STATE_FACTORY.WORK_ORDER_GENERATE--%>
    <%--|| application.state == STATE_FACTORY.CLOSE_CONNECTION_WORK_ORDER_GENERATE"--%>
            v-if="globalWorkOrder()"
    >
        <div align="center">
            <button type="submit" class="btn green-haze" @click="redirect('workorder')">View Work Order</button>
        </div>

    </btcl-portlet>

    <btcl-portlet
    <%--v-if="application.state==STATE_FACTORY.WORK_ORDER_GENERATE--%>
    <%--|| application.state == STATE_FACTORY.CLOSE_CONNECTION_WORK_ORDER_GENERATE"--%>
            v-if="globalAdviceNote()"
    >
        <div align="center">
            <button type="submit" class="btn green-haze" @click="redirect('advicenote')">View Advice Note</button>
        </div>

    </btcl-portlet>
</template>

<template v-if="detailsPageLoading == true">

    <div class="row">
        <div class="col-xs-12">
            <ul class="list-group">

                <li class="list-group-item"  v-if="an"><a style="color:blue;font-size:large" @click="viewAN">View Advice Note</a></li>
                <li class="list-group-item"  v-if="dn"><a  style="color:blue;font-size:large" @click="viewDN">View Demand Note</a></li>
                <li class="list-group-item" style="font-size:large" v-if="wo!=null &&wo.length>0">Work Order
                    <ul class="list-group inner">
                        <li class="list-group-item" v-for="(o, oIndex) in wo"><a  style="color:blue;font-size:large" @click="viewWO(o)">View Work Order {{oIndex + 1}}</a></li>
                    </ul>
                </li>

            </ul>
        </div>
    </div>
</template>