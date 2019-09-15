<div id="btcl-application">
    <btcl-body title="IP Management" subtitle="All Sub Regions">
        <div id="sub-region-list" class="col-md-12 col-sm-12 col-xs-12"
             v-if="allSubRegions.length>0">

            <table id="sub-region-table"
                   class="table table-bordered table-striped table-hover">
                <thead style="background:white;">
                <tr>
                    <th style="width:50%;">Sub Region Name</th>
                    <th style="text-align:left;">Parent Region Name</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="row in allSubRegions">
                    <td>{{row.key}}</td>
                    <td>{{row.value}}</td>
                </tr>

                </tbody>
            </table>
        </div>


    </btcl-body>



</div>
<script src="../sub-region/ip-all-sub-regions.js"></script>

