<btcl-portlet title="Address">
    <div class="row">
        <div class="col-xs-6">
            <btcl-input title="Country" :text="msg" required="true" :readonly="isClient"></btcl-input>
            <btcl-input title="City" :text="msg" required="true" :readonly="isClient"></btcl-input>
            <btcl-input title="Type" :text="msg" required="true" :readonly="isClient"></btcl-input>
            <btcl-input title="Category" :text="msg" required="true" :readonly="isClient"></btcl-input>
            <btcl-input title="Sub Category" :text="msg" required="true" :readonly="isClient"></btcl-input>
        </div>
        <div class="col-xs-6">
            <btcl-input title="Email" :text="msg" required="true" :readonly="isClient"></btcl-input>
            <btcl-input title="Mobile Number" :text="msg" required="true" :readonly="isClient"></btcl-input>
            <btcl-input title="Telephone Number" :text="msg" required="true" :readonly="isClient"></btcl-input>
            <btcl-input title="Fax Number" :text="msg" required="true" :readonly="isClient"></btcl-input>
            <btcl-input title="Web Address" :text="msg" required="true" :readonly="isClient"></btcl-input>

        </div>
    </div>
</btcl-portlet>