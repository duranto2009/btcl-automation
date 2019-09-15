<tab-content title="Select Service Type"
             icon="fa fa-user icon-state-danger"
             :before-change="beforeTabSwitch_1"
>

    <div class="view-height">
        <service-component @module_change="handleModuleChange"
                           :modules="modules">

        </service-component>
        <br><br><br>
        <hr style="background-color:#14565c">
        <br><br><br>

        <label style="font-size: 18px;">
            <input type="checkbox"  v-model="termAndConditionCheck">
            I Agree to the Terms & Condition and Privacy Policy
        </label>

        <br>
    </div>

</tab-content>