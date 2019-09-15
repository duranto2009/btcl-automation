<btcl-portlet v-if="
                application.state==STATE_FACTORY.NIX_APPLICATION_DEMAND_NOTE_GENERATED||
                application.state==STATE_FACTORY.WITHOUT_LOOP_DEMAND_NOTE||
                application.state==STATE_FACTORY.NIX_CLOSE_DEMAND_NOTE||
                application.state==STATE_FACTORY.NIX_UPGRADE_DEMAND_NOTE_GENERATED||
                application.state==STATE_FACTORY.NIX_DOWNGRADE_DEMAND_NOTE
                ">
    <div align="center">
        <button type="submit" class="btn green-haze" @click="redirect('demandnote')">View Demand
            Note
        </button>
    </div>
</btcl-portlet>

<btcl-portlet v-if="application.state==STATE_FACTORY.WORK_ORDER_GENERATE
|| application.state == STATE_FACTORY.CLOSE_CONNECTION_WORK_ORDER_GENERATE
|| application.state == STATE_FACTORY.WORK_ORDER_GENERATE_BY_LDGM
">
    <div align="center">
        <button type="submit" class="btn green-haze" @click="redirect('workorder')">View Work Order
        </button>
    </div>

</btcl-portlet>

<btcl-portlet v-if="
                application.state==STATE_FACTORY.ADVICE_NOTE_GENERATE||
                application.state==STATE_FACTORY.SEND_AN_TO_SERVER_ROOM||
                application.state==STATE_FACTORY.CLOSE_CONNECTION_ADVICE_NOTE_GENERATE||
                application.state==STATE_FACTORY.CLOSE_CONNECTION_SEND_AN_TO_SERVER_ROOM||
                application.state==STATE_FACTORY.WITHOUT_LOOP_SEND_AN_TO_SERVER_ROOM||
                application.state==STATE_FACTORY.WITHOUT_LOOP_ADVICE_NOTE_GENERATE||
                application.state==STATE_FACTORY.ADVICE_NOTE_GENERATE_UPGRADE||
                application.state==STATE_FACTORY.ADVICE_NOTE_GENERATE_DOWNGRADE
                ">
    <div align="center">
        <button type="submit" class="btn green-haze" @click="redirect('advicenote')">View Advice
            Note
        </button>
    </div>
</btcl-portlet>