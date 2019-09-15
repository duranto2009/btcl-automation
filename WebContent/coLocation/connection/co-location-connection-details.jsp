<%@ page import="coLocation.CoLocationConstants" %>
<%@ page import="common.ModuleConstants" %>
<%@ page import="login.LoginDTO" %>
<%@ page import="sessionmanager.SessionConstants" %>


<div id="btcl-co-location-application" v-cloak="true">
    <btcl-body title="Co-Location" subtitle='Connection Details'>
        <div class="row">
            <div class="col-md-8">
                <btcl-portlet title=Connection>
                    <btcl-info title="Client" :text=connection.client.value></btcl-info>
                    <btcl-info title="Connection Name" :text=connection.name></btcl-info>
                    <btcl-info title="Connection Type" :text="connection.conTypeDescription.value"></btcl-info>
                    <btcl-info title="Active From" :text="connection.activeFrom" v-bind:is-date="true"></btcl-info>
                    <btcl-info title="Discount Rate" :text="connection.discountRate"></btcl-info>
                <%--<btcl-info title="Status" :text="connection.status.label"></btcl-info>--%>
                </btcl-portlet>
                <btcl-portlet title="Inventory Information">
                    <btcl-bounded title="">
                        <table class="table">
                            <tr  style="background: rgb(220, 220, 220);margin-left: 1px;margin-top: 7px;border-radius: 9px;padding:5px;margin-right:2px">
                                <th ><p style="text-align: center;">Name</p></th>
                                <th ><p style="text-align: center;">Model</p></th>
                                <%--<th ><p style="text-align: center;">Total Amount</p></th>--%>
                                <th ><p style="text-align: center;">Type</p></th>
                                <th ><p style="text-align: center;">Amount</p></th>
                                <%--<th ><p style="text-align: center;"></p></th>--%>
                            </tr>
                            <template v-for="(inventory, index) in connection.modifiedCoLocationInventoryDTOList">
                            <tr >
                                <td >
                                    <p style="text-align: center;border: 1px solid;padding: 10px;">{{(inventory.name === "" ? "N/A" : inventory.name )}}</p>
                                </td>
                                <td ><p style="text-align: center;border: 1px solid;padding: 10px;">{{inventory.model}}</p></td>
                                <td ><p style="text-align: center;border: 1px solid;padding: 10px;">
                                    <%--{{new Date(parseInt(ip.activeFrom)).toLocaleDateString("ca-ES")}}--%>
                                    {{inventory.type}}
                                </p></td>
                                <td ><p style="text-align: center;border: 1px solid;padding: 10px;">{{inventory.amount}}</p></td>
                                <%--<td ><p style="text-align: center;border: 1px solid;padding: 10px;">{{inventory.isUsed}}</p></td>--%>
                                <%--<td ><p style="text-align: center;border: 1px solid;padding: 10px;"></p></td>--%>
                            </tr>
                            </template>

                            <%--<template v-if="connection.powerNeeded"></template>--%>
                            <%--<template v-if="connection.fiberNeeded"></template>--%>
                            <%--<template v-if="connection.rackNeeded"></template>--%>


                        </table>
                    </btcl-bounded>
                </btcl-portlet>

                <%--<btcl-portlet title="Inventory Information">--%>
                    <%--<btcl-bounded title="">--%>
                        <%--<div class="form-body">--%>
                            <%--<div class="form-group"--%>
                                 <%--style="background: rgb(220, 220, 220);margin-left: 1px;margin-top: 7px;border-radius: 9px;padding:5px;margin-right:2px">--%>
                                <%--<div class="col-md-2"><p style="text-align: center;">Name</p></div>--%>
                                <%--<div class="col-md-2"><p style="text-align: center;">Model</p></div>--%>
                                <%--<div class="col-md-2"><p style="text-align: center;">Total Amount</p></div>--%>
                                <%--<div class="col-md-2"><p style="text-align: center;">Available Amount</p></div>--%>
                                <%--<div class="col-md-2"><p style="text-align: center;">Used</p></div>--%>
                                <%--<div class="col-md-2"><p style="text-align: center;"></p></div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group" v-for="(inventory, index) in connection.coLocationInventoryDTOList">--%>
                                <%--<div class="col-md-2">--%>
                                    <%--<p style="text-align: center;border: 1px solid;padding: 10px;">{{inventory.name}}</p>--%>
                                <%--</div>--%>
                                <%--<div class="col-md-2"><p style="text-align: center;border: 1px solid;padding: 10px;">{{inventory.model}}</p></div>--%>
                                <%--<div class="col-md-2"><p style="text-align: center;border: 1px solid;padding: 10px;">--%>
                                    <%--&lt;%&ndash;{{new Date(parseInt(ip.activeFrom)).toLocaleDateString("ca-ES")}}&ndash;%&gt;--%>
                                    <%--{{inventory.totalAmount}}--%>
                                <%--</p></div>--%>
                                <%--<div class="col-md-2"><p style="text-align: center;border: 1px solid;padding: 10px;">{{inventory.availableAmount}}</p></div>--%>
                                <%--<div class="col-md-2"><p style="text-align: center;border: 1px solid;padding: 10px;">{{inventory.isUsed}}</p></div>--%>
                                <%--<div class="col-md-2"><p style="text-align: center;border: 1px solid;padding: 10px;"></p></div>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                    <%--</btcl-bounded>--%>
                <%--</btcl-portlet>--%>

            </div>
            <div class="col-md-4">
                <btcl-portlet title=History>
                    <div>
                        <button type="button" class="btn btn-default btn-block" v-for="(history, historyIndex) in this.historyList" @click="getConnectionByHistoryID(historyIndex)">
                            <div align="left">
                                <b>{{history.incidentDescription}}</b>
                                <br/>
                                {{new Date(history.activeFrom).toLocaleDateString("ca-ES")}}
                            </div>
                        </button>
                    </div>
                </btcl-portlet>
            </div>
        </div>
    </btcl-body>
</div>
<%LoginDTO loginDTO = (LoginDTO) session.getAttribute(SessionConstants.USER_LOGIN);%>
<script>
    var applicationID = parseInt('<%=request.getParameter("id")%>');
    var loggedInUserID = '<%=loginDTO.getUserID()%>';
    var requestID = "<%=request.getParameter("id")%>";
    var historyID = "<%=request.getParameter("historyID")%>";
</script>
<script>
    var vue = new Vue({
        el: "#btcl-co-location-application",
        data: {
            connection: {
                client: {},
                modifiedCoLocationInventoryDTOList: null,
            },
            moduleId: <%=ModuleConstants.Module_ID_COLOCATION%>,
            historyList: [],
        },
        methods: {
            getRackSpaceDescription: function(space){
                if( parseFloat(space) == 0.25){
                    return "Quarter";
                }
                else if(parseFloat(space)== 0.5){
                    return "Half";
                }
                else if(parseFloat(space)== 1){
                    return "Full";
                }
                return "N/A";
            },
            getConnectionByHistoryID: function(id){
                this.connection = this.historyList[id];
                this.createModifiedCoLocationInventoryDTOList();
            },
            createModifiedCoLocationInventoryDTOList: function(){
                this.connection.modifiedCoLocationInventoryDTOList = this.connection.coLocationInventoryDTOList.map(function(data){
                    if(data.templateID == vue.connection.powerType){
                        data['type'] = vue.connection.powerTypeDescription;
                        data['amount'] = vue.connection.powerAmount;
                    }
                    else if(data.templateID == vue.connection.rackSize){
                        data['type'] = vue.connection.rackSizeDescription;
                        data['amount'] = vue.getRackSpaceDescription(vue.connection.rackSpaceDescription);
                    }
                    else if(data.templateID == vue.connection.fiberType){
                        data['type'] = vue.connection.ofcTypeDescription;
                        data['amount'] = vue.connection.fiberCore;
                    }
                    else if(data.templateID == vue.connection.floorSpaceType){
                        data['type'] = vue.connection.floorSpaceTypeDescription;
                        data['amount'] = vue.connection.floorSpaceAmount;
                    }
                    return data;
                });
            },

        },
        created() {
            axios({
                method: "GET",
                "url": context + "co-location/history-list-json.do?id=" + historyID
            }).then(result => {
                this.historyList = result.data.payload;
                if(this.historyList.length>0){
                    this.connection = this.historyList[0];
                    this.createModifiedCoLocationInventoryDTOList();

                }
            }, error => {
            });

        },
    });
</script>