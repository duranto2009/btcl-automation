<btcl-portlet v-if="
                application.state==STATE_FACTORY.DEMAND_NOTE||
                application.state==STATE_FACTORY.WITHOUT_LOOP_DEMAND_NOTE
                || application.state==STATE_FACTORY.CLOSE_CONNECTION_DEMAND_NOTE
                ">
    <div align="center">
        <button type="submit" class="btn green-haze" @click="redirect('demandnote')">View Demand
            Note
        </button>
    </div>
</btcl-portlet>

<btcl-portlet v-if="application.state==STATE_FACTORY.WORK_ORDER_GENERATE
|| application.state == STATE_FACTORY.CLOSE_CONNECTION_WORK_ORDER_GENERATE
">
    <div align="center">
        <button type="submit" class="btn green-haze" @click="redirect('workorder')">View Work Order
        </button>
    </div>

</btcl-portlet>

<btcl-portlet v-if="
                application.state==STATE_FACTORY.ADVICE_NOTE_GENERATE||
                application.state==STATE_FACTORY.SEND_AN_TO_SERVER_ROOM||
                application.state==STATE_FACTORY.AN_GENERATE_AND_FORWARD||

                application.state==STATE_FACTORY.CLOSE_CONNECTION_ADVICE_NOTE_GENERATE||
                application.state==STATE_FACTORY.CLOSE_CONNECTION_SEND_AN_TO_SERVER_ROOM||
                application.state==STATE_FACTORY.CLOSE_CONNECTION_ADVICE_NOTE_GENERATE_AND_FORWARD||

                application.state==STATE_FACTORY.WITHOUT_LOOP_SEND_AN_TO_SERVER_ROOM||
                application.state==STATE_FACTORY.WITHOUT_LOOP_ADVICE_NOTE_GENERATE ||
                application.state==STATE_FACTORY.LLI_WITHOUT_LOOP_ADVICE_NOTE_PUBLISH_AND_FORWARD||

                application.state==STATE_FACTORY.SHIFT_BW_ADVICE_NOTE_GENERATE||
                application.state==STATE_FACTORY.SHIFT_BW_SEND_AN_TO_SERVER_ROOM||
                application.state==STATE_FACTORY.SHIFT_BW_ADVICE_NOTE_GENERATE_AND_FORWARD
                ">
    <div align="center">
        <button type="submit" class="btn green-haze" @click="redirect('advicenote')">View Advice
            Note
        </button>
    </div>
</btcl-portlet>