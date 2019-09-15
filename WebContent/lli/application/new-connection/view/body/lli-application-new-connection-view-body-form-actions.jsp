<%--action forward--%>
<btcl-body title="Available Actions">
    <btcl-portlet>
        <div>
            <ul style="list-style-type:none">
                <li v-for="element in application.action">
<%--                    <template v-if="demandNoteGeneratedSkip(element)"></template>--%>
<%--                    <template v-else>--%>
<%--                        <template v-if="demandNoteGenerated(element)"></template>--%>
                        <label >
                                        <span><input type="radio" name="actionForwards" v-model="picked"
                                                     :value="element.Value"> {{element.Label}}</span>
                        </label>
<%--                    </template>--%>
                </li>
            </ul>
        </div>
        <hr>
        <button type="submit" class="btn green-haze btn-block" @click="nextStep">Submit</button>

    </btcl-portlet>


</btcl-body>