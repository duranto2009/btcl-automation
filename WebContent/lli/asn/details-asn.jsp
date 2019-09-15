<style>
    .portlet>.portlet-title>.tools>a.collapse {
        background-image: url(blah) !important;
        width: 14px;
        visibility: visible;
    }

    .portlet>.portlet-title>.tools>a.expand {
        background-image: url(blah) !important;
        width: 14px;
        visibility: visible;
    }

</style>
<div id="btcl-asn" <%--v-cloak--%>>
        <btcl-body v-if="loading" title="ASN" subtitle='ASN Details'>

            <%--ASN Information--%>
            <btcl-portlet>
                <div class="row">
                    <div class="col-md-6 col-sm-6 col-xs-6">
                        <span style="font-weight: bold ;font-size: 18px!important;">ASN NO:</span>
                        <span style="font-weight: bold ;font-size: 18px!important;">{{application.asnNo}}</span>
                    </div>
                    <div class="col-md-6 col-sm-6 col-xs-6">
                        <span style="font-weight: bold ;font-size: 18px!important;">Client Name:</span>
                        <span style="font-weight: bold ;font-size: 18px!important;">{{application.client}}</span>
                    </div>
                </div>
            </btcl-portlet>
                <btcl-portlet >
                    <btcl-portlet>

                        <div class=row>
                            <div class="col-sm-6" style="padding-left:2%">
                                <div class="row">
                                    <label style="text-align: left;"><b>ASN Associated IPV4s</b>
                                    </label>
                                </div>
                                <template  v-for="(element,index) in application.iPs"
                                           v-if="element.ipVersion==1">
                                    <div class="form-group">
                                        <div class="row">
                                            <div class="col-sm-6 text-center"
                                                 style="background:  #f2f1f1;padding:1%">
                                                <template><span>{{element.ip}}</span></template>
                                            </div>
                                        </div>
                                    </div>
                                </template>
                            </div>
                            <div class="col-sm-6" style="padding-left:2%">
                                <div class="row">
                                    <label style="text-align: left;"><b>ASN Associated IPV6s</b>
                                    </label>
                                </div>
                                <template v-for="(element,index) in application.iPs"
                                          v-if="element.ipVersion==2">
                                    <div class="form-group">
                                        <div class="row">
                                            <div class="col-sm-6 text-center"
                                                 style="background:  #f2f1f1;padding:1%">
                                                <template><span>{{element.ip}}</span></template>
                                            </div>
                                        </div>
                                    </div>
                                </template>
                            </div>
                        </div>

                    </btcl-portlet>
                </btcl-portlet>
        </btcl-body>
</div>
<script>
    var asnId= <%=request.getParameter("id")%>
</script>
<script src="../lli/asn/asn-details.js" type="text/javascript"></script>
