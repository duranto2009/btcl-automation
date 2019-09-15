<%--done--%>
<%--server room--%>
<btcl-body v-if="application.ifr.length>0 && ifrRequest" title="Requested POP Details">
    <btcl-portlet>

        <div class="form-body">
            <div class="form-group" style="background: rgb(220, 220, 220);
            margin-left: 1px; margin-top: 7px; border-radius: 9px; padding: 5px; margin-right: 2px;">
                <label class="control-label col-md-3" style="text-align: center;">POP Name</label>
                <label class="control-label col-md-3" style="text-align: center;">Office Name</label>
                <label class="control-label col-md-3" style="text-align: center;">Port type</label>
                <label class="control-label col-md-3" style="text-align: center;">Is Replied</label>


            </div>
            <div class="form-group" v-for="(element,index) in application.ifr" :key="index">
                <label class="control-label col-md-3"
                       style="text-align: center;">{{element.popName}}</label>
                <label class="control-label col-md-3"
                       style="text-align: center;">{{element.office.name}}</label>
                <label class="control-label col-md-3"
                       style="text-align: center;">{{application.portTypeString}}</label>


                <label v-if ="element.replied==0" class="control-label col-md-3"
                       style="text-align: center;">No</label>

                <label v-if ="element.replied==1" class="control-label col-md-3"
                       style="text-align: center;">Yes -> Feasibility:
                    <label v-if="element.selected==0"> Negetive</label>
                </label>


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
                <label class="control-label col-md-5"
                       style="text-align: center;">Office Name</label>
                <label class="control-label col-md-5"
                       style="text-align: center;">POP Name</label>
                <%--<div class="col-md-3"><p style="text-align: center;">Priority</p></div>--%>
                <div class="col-md-1"><p style="text-align: center;">Feasibility</p></div>
            </div>
            <div class="form-group" v-for="(element,index) in application.ifr" :key="index">
                <label class="control-label col-md-5"
                       style="text-align: center;">{{element.office.name}}</label>
                <label class="control-label col-md-5"
                       style="text-align: center;">{{element.popName}}</label>
                <div class="col-md-1" align="center">
                    <input disabled type="checkbox" :true-value="1" v-bind:false-value="0"
                           v-model="element.selected">
                </div>
            </div>
            <div class="form-group">
            </div>
        </div>
    </btcl-portlet>
</btcl-body>