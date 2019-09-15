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
<div id="btcl-asn-application" <%--v-cloak--%>>

    <template>
        <btcl-body title="ASN" subtitle='Application Details'>
            <template>
                <btcl-portlet>
                    <div class=row>
                        <div class="col-sm-6" style="padding-left:2%">
                            <template v-for="(element,index) in application.formElements"
                                      v-if="index<application.formElements.length/2">
                                <div class="form-group">
                                    <div class="row">
                                        <label class="col-sm-4"
                                               style="text-align: left;"><b>{{element.Label}}</b></label>
                                        <div v-if="element.Label!='Status'" class="col-sm-6 text-center"
                                             style="background:  #f2f1f1;padding:1%">
                                            <template><span>{{element.Value}}</span></template>
                                        </div>
                                        <div v-else class="col-sm-8 text-center"
                                             v-bind:style="{ background: application.color}">
                                            <span style="color: white;font-weight: bold ;font-size: 18px!important;">{{element.Value}}</span>
                                        </div>
                                    </div>
                                </div>
                            </template>
                        </div>
                        <div class="col-sm-6" style="padding-left: 2%">
                            <template v-for="(element,index) in application.formElements"
                                      v-if="index>=application.formElements.length/2">
                                <div class="form-group">
                                    <div class="row">
                                        <label class="col-sm-4"
                                               style="text-align: left;"><b>{{element.Label}}</b></label>
                                        <div v-if="element.Label!='Status'" class="col-sm-6 text-center"
                                             style="background:  #f2f1f1;padding:1%">
                                            <template><span>{{element.Value}}</span></template>
                                        </div>
                                        <div v-else class="col-sm-6 text-center"
                                             v-bind:style="{ background: application.color}">
                                            <span style="color: white;font-weight: bold ;font-size: 18px!important;">{{element.Value}}</span>
                                        </div>
                                    </div>
                                </div>
                            </template>
                        </div>
                    </div>
                </btcl-portlet>
            </template>

            <%--ASN Information--%>
            <template v-for="(asn, asnIndex) in application.asn" >
                <template>
                    <btcl-portlet :title="ASN" :collapse="collapse">
                        <div class="form-group">
                            <btcl-portlet>
                                <div class=row>
                                    <label class="col-sm-2"
                                           style="text-align: left;"><b>ASN Number</b></label>
                                    <div class = "col-md-6 col-xs-6 col-sm-6">
                                        <span style="font-weight: bold ;font-size: 18px!important;text-align: left;">{{asn.asnNo}}</span>
                                    </div>
                                </div>
                            </btcl-portlet>
                            <btcl-portlet>
                                <div class="row">
                                    <label style="text-align: left;"><b>ASN Associated IPs</b>
                                    </label>
                                </div>
                                <div class=row>
                                    <div class="col-sm-6" style="padding-left:2%">
                                        <template v-for="(element,index) in asn.asNmapToIPS"
                                                  v-if="element.ipVersion == 1">
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
                                        <template v-for="(element,index) in asn.asNmapToIPS"
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
                        </div>
                    </btcl-portlet>
                </template>
            </template>
        </btcl-body>
    </template>
</div>
<%--<script src="../script/vpn-common.js" type="text/javascript"></script>--%>
<script src="../lli/asn/details-view.js" type="text/javascript"></script>
