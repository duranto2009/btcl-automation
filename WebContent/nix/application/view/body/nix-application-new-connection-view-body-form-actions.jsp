<%--action forward--%>
<btcl-body title="Available Actions">
    <btcl-portlet>
        <div>
            <ul style="list-style-type:none">
                <li v-for="element in application.action">
                    <%--<label v-if="demandNoteGeneratedSkip(element)"></label>--%>
                    <label ><%--v-else=>--%>
                                        <span><input type="radio" name="actionForwards" v-model="picked"
                                                     :value="element.Value"> {{element.Label}}</span>
                    </label>
                </li>
            </ul>
        </div>
        <hr>
        <button type="submit" class="btn green-haze btn-block" @click="nextStep">Submit</button>

    </btcl-portlet>


</btcl-body>