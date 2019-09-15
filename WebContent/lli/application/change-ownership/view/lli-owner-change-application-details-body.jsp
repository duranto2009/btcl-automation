<style>
    [v-cloak] {
        display: none;
    }
    .list-group.inner li{
        border:none;

    }
    .a {
        background: none;
        color: inherit;
        border: none;
        padding: 0;
        font: inherit;
        cursor: pointer;
        outline: inherit;
    }
</style>
<div id="btcl-application">

    <btcl-body title="Application Details" subtitle="">
        <div class="row" style="text-align: center">
        </div>
        <%--Form Elements to show--%>
        <btcl-portlet>
            <btcl-field v-for="(element,index) in application.formElements" :key="index">
                <div class="form-group">
                    <div class="row">
                        <label class="col-sm-4" style="text-align: left;">{{element.Label}}</label>
                        <div v-if="element.Label!='Status'" class="col-sm-6 text-center" style="background:  #f2f1f1;padding:1%">
                            <template><span >{{element.Value}}</span></template>
                        </div>
                        <div v-else class="col-sm-6 text-center"  v-bind:style="{ background: application.color}">
                            <span style="color: white;font-weight: bold ;font-size: 18px!important;">{{element.Value}}</span>
                        </div>


                    </div>
                </div>
            </btcl-field>
        </btcl-portlet>
            <btcl-portlet title="Documents" v-if="!loading">
                <div class="row">
                    <div class="col-xs-12">
                        <ul class="list-group">
                            <li class="list-group-item"  v-if="an"><a style="color:blue;font-size:large" @click="viewAN">View Advice Note</a></li>
                            <li class="list-group-item"  v-if="dn"><a  style="color:blue;font-size:large" @click="viewDN">View Demand Note</a></li>
                        </ul>
                    </div>
                </div>
        </btcl-portlet>
    </btcl-body>
</div>

<script>var applicationID = '<%=request.getParameter("id")%>';</script>
<script src=../application/change-ownership/view/lli-owner-change-application-details.js></script>
