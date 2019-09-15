<btcl-info v-if="application.state.name=='CLIENT_CORRECTION'" title="Client" :text=application.client.value></btcl-info>

<btcl-field title="Source Client">
    <lli-client-search :client.sync="application.srcClient"
                       :callback="clientSelectionCallback">Source Client
    </lli-client-search>
</btcl-field>



<btcl-field title="DestinationClient Client">
    <lli-client-search :client.sync="application.dstClient">Destination Client
    </lli-client-search>
</btcl-field>



<div v-if="connectionOptionListByClientID.length>0">
    <btcl-field title ="Active Connections">

        <div class=row>
            <%--<label class="col-sm-4 control-label"
                   style="text-align: left;">Connections</label>--%>
            <div <%--class=col-sm-8--%>>
                <select class="form-control"
                        v-model="connection"
                        v-on:change="connectionSelectionCallback(connection)"
                        style="margin-top: 7px;">
                    <option value="0">Select Connection
                    </option>
                    <option v-bind:value="connection"
                            v-for="connection in connectionOptionListByClientID">
                        {{connection.label}}
                        <template v-if="connection.label!==''">({{connection.label}})</template>
                    </option>
                </select>
            </div>
        </div>
    </btcl-field>

</div>

<div v-if="application.selectedConnectionList.length>0">
    <btcl-field title ="Selected Connections">

        <div class="row">
            <%--<label class="col-sm-4 control-label"
                   style="text-align: left;">Selected Connections</label>--%>
            <div <%--class=col-sm-8--%>>
                <div style="margin-top: 2px;border: 1px solid;"
                     class="form-control"
                     v-for="(selectedConnection,index) in application.selectedConnectionList">
                    <div class="row">
                        <div class="col-sm-10">{{selectedConnection.label}}</div>
                        <div class="col-sm-2">
                            <button  v-on:click="removeSelectedConnection(index)"
                                     type="button" style="color:red" >x</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </btcl-field>

</div>


<btcl-input title="Description"
            :text.sync="application.description">
</btcl-input>
<btcl-input title="Comment"
            :text.sync="application.comment">
</btcl-input>

<btcl-field title="Suggested Date">
    <btcl-datepicker :date.sync="application.suggestedDate">
    </btcl-datepicker>
</btcl-field>


<btcl-field>
    <div align="right">
        <button type="submit" class="btn green-haze" v-on:click="submitFormData" >Submit</button>
    </div>
</btcl-field>


