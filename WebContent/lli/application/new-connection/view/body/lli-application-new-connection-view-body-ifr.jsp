<%--done--%>
<%--server room--%>
<btcl-body v-if="application.ifr.length>0 && ifrRequest && !isAdditionalIP" title="Requested POP Details">
    <btcl-portlet>

        <div class="form-body">
            <div class="form-group" style="background: rgb(220, 220, 220);
            margin-left: 1px; margin-top: 7px; border-radius: 9px; padding: 5px; margin-right: 2px;">
                <label class="control-label col-md-4" style="text-align: center;">POP Name</label>
                <div class="col-md-4" v-if="!isItNewLocalLoop"><p style="text-align: center;">Requested BW(Mbps)</p>
                </div>
                <div class="col-md-4" v-if="!isItNewLocalLoop"><p style="text-align: center;">Priority</p></div>
            </div>
            <div class="form-group" v-if="element.isIgnored==0" v-for="(element,index) in application.ifr" :key="index">
                <label class="control-label col-md-4"
                       style="text-align: center;">{{element.popName}}</label>
                <div class="col-md-4">
                    <p style="text-align: center;" v-if="!isItNewLocalLoop">{{element.requestedBW}}</p>
                </div>
                <div class="col-md-4" v-if="!isItNewLocalLoop">
                    <select disabled class="form-control" v-model="element.priority">
                        <option value="1">High</option>
                        <option value="2">Medium</option>
                        <option value="3">Moderate</option>
                        <option value="4">Not Applicable</option>
                    </select>
                </div>
            </div>
        </div>
    </btcl-portlet>
</btcl-body>


<%--!vendor!--%>
<%--LDGM Feasibility Report -- IFR RESPONSE --%>
<%--done--%>
<btcl-body v-if="ifrResponse" title="Feasibility Report">
    <% //todo : need check %>
    <%--||application.state==STATE_FACTORY.WITHOUT_LOOP_RETRANSFER_TO_LOCAL_DGM--%>
    <btcl-portlet title="IFR Responses">

        <div class="form-body">
            <div class="form-group" style="background: rgb(220, 220, 220);
            margin-left: 1px; margin-top: 7px; border-radius: 9px; padding: 5px; margin-right: 2px;">
                <label class="control-label col-md-3"
                       style="text-align: center;">POP Name</label>
                <div class="col-md-3" v-if="!isItNewLocalLoop">
                    <p style="text-align: center;">Connection Office Address</p>
                </div>
                <div class="col-md-2" v-if="!isItNewLocalLoop">
                    <p style="text-align: center;">Requested
                    Bandwidth(Mbps)</p>
                </div>
                <div class="col-md-2" v-if="!isItNewLocalLoop"><p style="text-align: center;">Priority</p></div>
               <%-- <div class="col-md-2" v-if="!isItNewLocalLoop"><p style="text-align: center;">Available
                    Bandwidth(Mbps)</p>
                </div>--%>
                <div class="col-md-2"><p style="text-align: center;">Feasibility</p></div>
            </div>
            <div class="form-group" v-for="(element,index) in application.ifr" :key="index">
                <label class="control-label col-md-3"
                       style="text-align: center;">{{element.popName}}</label>
                <div class="col-md-3" v-if="!isItNewLocalLoop">
                    <p style="text-align: center;" >{{element.officeAddress}}</p>

                </div>
                <div class="col-md-2" v-if="!isItNewLocalLoop">
                    <p style="text-align: center;" >{{element.requestedBW}}</p>

                </div>
                <div class="col-md-2" v-if="!isItNewLocalLoop">
                    <%--<p style="text-align: center;">{{element.priority}}</p>--%>
                    <select disabled class="form-control" id="sel1"
                            v-model="element.priority">

                        <option disabled value="-1">Select Priority</option>
                        <option v-bind:value="1" value="1">High</option>
                        <option v-bind:value="2" value="2">Medium</option>
                        <option v-bind:value="3" value="3">Moderate</option>
                        <!--<option>4</option>-->
                    </select>
                </div>
               <%-- <div class="col-md-2" v-if="!isItNewLocalLoop">
                    <p style="text-align: center;">{{element.availableBW}}</p>
                    &lt;%&ndash;<input type="number" required v-model="element.availableBW" v-bind:value="element.availalbleBW"/>&ndash;%&gt;

                </div>--%>
                <div class="col-md-2" align="center">
                    <input disabled type="checkbox" :true-value="1" v-bind:false-value="0"
                           v-model="element.isSelected">
                    <span v-if="element.isSelected">(Positive)</span>
                    <span v-else>(Negative)</span>
                </div>
            </div>
            <div class="form-group">
            </div>
        </div>
    </btcl-portlet>
</btcl-body>