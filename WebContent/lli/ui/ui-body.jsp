<%@page import="file.FileTypeConstants" %>
<%@page import="common.EntityTypeConstant" %>
<%
    String context = "../.." + request.getContextPath() + "/";
%>
<div id="app">
    <div class="portlet light bordered">
        <div class="portlet-title">
            <div class="caption font-green-sharp"><span class="caption-subject bold uppercase">ui components</span>
                <span class="caption-helper">Application</span></div>
            <div class="actions"><a href="javascript:;" class="btn btn-circle btn-icon-only btn-default fullscreen"
                                    data-original-title="" title=""></a>
            </div>
        </div>
        <div class="portlet-body">
            <%--Repeater--%>
            <div class="form-horizontal">
                <i>Repeater</i>
                <h1>Vue JS Multiple Fields Repeater</h1>
                <div class="border" v-for="field in fields">
                    <input v-model="field.first" placeholder="Enter First Name">
                    <input v-model="field.last" placeholder="Enter Last Name">
                </div>
                <button @click="AddField">
                    New Field
                </button>
                <pre>{{ $data | json }}</pre>


            </div>
            <%----%>

            <%-- OFFICE ADDRESS--%>
            <div class="portlet box green">
                <div class="portlet-title">
                    <div class="caption">
                        <i class="fa fa-gift"></i>Office Address
                    </div>
                    <div class="tools">
                        <a href="javascript:;" class="collapse" data-original-title="" title=""> </a>
                    </div>
                </div>
                <div class="portlet-body">
                    <div class="portlet-body">
                        <!-- BEGIN FORM-->
                        <form action="#" id="form_sample_1" class="form-horizontal" novalidate="novalidate"
                              _lpchecked="1">
                            <div class="form-body">

                                <div class="form-group">
                                    <label class="control-label col-md-3">POP 1
                                        <span class="required" aria-required="true"> * </span>
                                    </label>
                                    <div class="col-md-4">
                                        <select class="form-control" name="select" aria-required="true"
                                                aria-invalid="false" aria-describedby="select-error">
                                            <option value="">Select...</option>
                                            <option value="Category 1">Category 1</option>
                                            <option value="Category 2">Category 2</option>
                                            <option value="Category 3">Category 5</option>
                                            <option value="Category 4">Category 4</option>
                                        </select><span id="select-error" class="help-block help-block-error"></span>

                                    </div>
                                    <div class="col-md-4">
                                        <div class="actions">
                                            <%--<a href="javascript:;" class="btn btn-circle btn-outline green"><i class="fa fa-pencil"></i> Edit </a>--%>
                                            <a href="javascript:;" class="btn btn-circle blue-steel btn-outline">
                                                <i class="fa fa-plus"></i> Add </a>
                                            <a href="javascript:;" data-repeater-delete=""
                                               class="btn btn-circle btn-danger mt-repeater-delete">
                                                <i class="fa fa-close"></i> Delete</a>
                                            <%--<a class="btn btn-circle btn-icon-only btn-default fullscreen" href="javascript:;" data-original-title="" title=""> </a>--%>
                                        </div>
                                    </div>

                                </div>
                                <div class="form-group">
                                    <label class="control-label col-md-3">POP 2
                                        <span class="required" aria-required="true"> * </span>
                                    </label>
                                    <div class="col-md-4">
                                        <select class="form-control" name="select" aria-required="true"
                                                aria-invalid="false" aria-describedby="select-error">
                                            <option value="">Select...</option>
                                            <option value="Category 1">Category 1</option>
                                            <option value="Category 2">Category 2</option>
                                            <option value="Category 3">Category 5</option>
                                            <option value="Category 4">Category 4</option>
                                        </select><span id="select-error" class="help-block help-block-error"></span>

                                    </div>
                                    <div class="col-md-4">
                                        <div class="actions">
                                            <%--<a href="javascript:;" class="btn btn-circle btn-outline green"><i class="fa fa-pencil"></i> Edit </a>--%>
                                            <a href="javascript:;" class="btn btn-circle blue-steel btn-outline">
                                                <i class="fa fa-plus"></i> Add </a>
                                            <a href="javascript:;" data-repeater-delete=""
                                               class="btn btn-circle btn-danger mt-repeater-delete">
                                                <i class="fa fa-close"></i> Delete</a>
                                            <%--<a class="btn btn-circle btn-icon-only btn-default fullscreen" href="javascript:;" data-original-title="" title=""> </a>--%>
                                        </div>
                                    </div>

                                </div>

                            </div>
                            <%--<div class="form-actions">--%>
                            <%--<div class="row">--%>
                            <%--<div class="col-md-offset-3 col-md-9">--%>
                            <%--<button type="submit" class="btn green">Submit</button>--%>
                            <%--<button type="button" class="btn grey-salsa btn-outline">Cancel</button>--%>
                            <%--</div>--%>
                            <%--</div>--%>
                            <%--</div>--%>
                        </form>
                        <!-- END FORM-->
                    </div>
                    <%--<ul>--%>
                    <%--<li> Lorem ipsum dolor sit amet </li>--%>
                    <%--<li> Consectetur adipiscing elit </li>--%>
                    <%--<li> Integer molestie lorem at massa </li>--%>
                    <%--<li> Facilisis in pretium nisl aliquet </li>--%>
                    <%--<li> Nulla volutpat aliquam velit--%>
                    <%--<ul>--%>
                    <%--<li> Phasellus iaculis neque </li>--%>
                    <%--<li> Purus sodales ultricies </li>--%>
                    <%--<li> Vestibulum laoreet porttitor sem </li>--%>
                    <%--<li> Ac tristique libero volutpat at </li>--%>
                    <%--</ul>--%>
                    <%--</li>--%>
                    <%--<li> Faucibus porta lacus fringilla vel </li>--%>
                    <%--<li> Aenean sit amet erat nunc </li>--%>
                    <%--<li> Eget porttitor lorem </li>--%>
                    <%--</ul>--%>
                </div>
            </div>
            <%----%>

            <%--FEASIBILITY REPORT--%>
            <div class="portlet box green">
                <div class="portlet-title">
                    <div class="caption">
                        <i class="fa fa-gift"></i>Local Loop 1
                    </div>
                    <div class="tools">
                        <a href="javascript:;" class="collapse" data-original-title="" title=""> </a>
                    </div>
                </div>
                <div class="portlet-body">
                    <div class="portlet-body">
                        <!-- BEGIN FORM-->
                        <form action="#" id="form_sample_1" class="form-horizontal" novalidate="novalidate"
                              _lpchecked="1">
                            <div class="form-body">

                                <div class="form-group">
                                    <label class="control-label col-md-3">POP 1
                                        <%--<span class="required" aria-required="true"> * </span>--%>
                                    </label>
                                    <div class="col-md-4">
                                        <p style="text-align: center;">Rajshahi</p>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-md-3">Switch/Router
                                        <%--<span class="required" aria-required="true"> * </span>--%>
                                    </label>
                                    <div class="col-md-4">
                                        <p style="text-align: center;">Raj_218asd</p>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-md-3">Port
                                        <%--<span class="required" aria-required="true"> * </span>--%>
                                    </label>
                                    <div class="col-md-4">
                                        <p style="text-align: center;">1/11</p>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-md-3">VLAN
                                        <%--<span class="required" aria-required="true"> * </span>--%>
                                    </label>
                                    <div class="col-md-4">
                                        <p style="text-align: center;">101</p>
                                    </div>
                                </div>

                            </div>
                            <%--<div class="form-actions">--%>
                            <%--<div class="row">--%>
                            <%--<div class="col-md-offset-3 col-md-9">--%>
                            <%--<button type="submit" class="btn green">Submit</button>--%>
                            <%--<button type="button" class="btn grey-salsa btn-outline">Cancel</button>--%>
                            <%--</div>--%>
                            <%--</div>--%>
                            <%--</div>--%>
                        </form>
                        <!-- END FORM-->
                    </div>
                    <%--<ul>--%>
                    <%--<li> Lorem ipsum dolor sit amet </li>--%>
                    <%--<li> Consectetur adipiscing elit </li>--%>
                    <%--<li> Integer molestie lorem at massa </li>--%>
                    <%--<li> Facilisis in pretium nisl aliquet </li>--%>
                    <%--<li> Nulla volutpat aliquam velit--%>
                    <%--<ul>--%>
                    <%--<li> Phasellus iaculis neque </li>--%>
                    <%--<li> Purus sodales ultricies </li>--%>
                    <%--<li> Vestibulum laoreet porttitor sem </li>--%>
                    <%--<li> Ac tristique libero volutpat at </li>--%>
                    <%--</ul>--%>
                    <%--</li>--%>
                    <%--<li> Faucibus porta lacus fringilla vel </li>--%>
                    <%--<li> Aenean sit amet erat nunc </li>--%>
                    <%--<li> Eget porttitor lorem </li>--%>
                    <%--</ul>--%>
                </div>
            </div>
            <%----%>

            <%--Office Address--%>
            <div class="portlet box green">
                <div class="portlet-title">
                    <div class="caption">
                        <i class="fa fa-gift"></i>Local Loop 1
                    </div>
                    <div class="tools">
                        <a href="javascript:;" class="collapse" data-original-title="" title=""> </a>
                    </div>
                </div>
                <div class="portlet-body">
                    <div class="portlet-body">
                        <!-- BEGIN FORM-->
                        <form action="#" id="form_sample_1" class="form-horizontal" novalidate="novalidate"
                              _lpchecked="1">
                            <div class="form-body">

                                <div class="form-group">
                                    <label class="control-label col-md-2">
                                        <%--<span class="required" aria-required="true"> * </span>--%>
                                    </label>
                                    <div class="col-md-2">

                                        <p style="text-align: center;">POP Name</p>
                                    </div>

                                    <div class="col-md-2">
                                        <p style="text-align: center;">MUX Name</p>
                                    </div>

                                    <div class="col-md-2">
                                        <p style="text-align: center;">LDP Name</p>
                                    </div>

                                    <div class="col-md-2">


                                    </div>


                                </div>
                                <div class="form-group">
                                    <label class="control-label col-md-2">POP 1
                                        <%--<span class="required" aria-required="true"> * </span>--%>
                                    </label>
                                    <div class="col-md-2">
                                        <select class="form-control" name="select" aria-required="true"
                                                aria-invalid="false" aria-describedby="select-error">
                                            <option value="">Select...</option>
                                            <option value="Category 1">Category 1</option>
                                            <option value="Category 2">Category 2</option>
                                            <option value="Category 3">Category 5</option>
                                            <option value="Category 4">Category 4</option>
                                        </select>
                                    </div>

                                    <div class="col-md-2">
                                        <p style="text-align: center;">Rajshahi</p>
                                    </div>

                                    <div class="col-md-2">
                                        <p style="text-align: center;">Rajshahi</p>
                                    </div>

                                    <div class="col-md-2">
                                        <select class="js-example-basic-multiple" name="states[]" multiple="multiple">
                                            <option value="AL">https://select2.org/getting-started/basic-usage</option>
                                            <%--<option value="AL">Alabama</option>--%>
                                            <%--...--%>
                                            <%--<option value="WY">Wyoming</option>--%>
                                        </select>

                                    </div>


                                </div>
                                <div class="form-group">
                                    <label class="control-label col-md-2">POP 2
                                        <%--<span class="required" aria-required="true"> * </span>--%>
                                    </label>
                                    <div class="col-md-2">
                                        <select class="form-control" name="select" aria-required="true"
                                                aria-invalid="false" aria-describedby="select-error">
                                            <option value="">Select...</option>
                                            <option value="Category 1">Category 1</option>
                                            <option value="Category 2">Category 2</option>
                                            <option value="Category 3">Category 5</option>
                                            <option value="Category 4">Category 4</option>
                                        </select>
                                    </div>

                                    <div class="col-md-2">
                                        <p style="text-align: center;">Rajshahi</p>
                                    </div>

                                    <div class="col-md-2">
                                        <p style="text-align: center;">Rajshahi</p>
                                    </div>

                                    <div class="col-md-2">
                                        <select class="js-example-basic-multiple" name="states[]" multiple="multiple">
                                            <option value="AL">https://select2.org/getting-started/basic-usage</option>
                                            <%--<option value="AL">Alabama</option>--%>
                                            <%--...--%>
                                            <%--<option value="WY">Wyoming</option>--%>
                                        </select>

                                    </div>


                                </div>
                                <div class="form-group">
                                    <label class="control-label col-md-2">Select Vendor
                                        <%--<span class="required" aria-required="true"> * </span>--%>
                                    </label>


                                    <div class="col-md-6">
                                        <select class="form-control" name="select" aria-required="true"
                                                aria-invalid="false" aria-describedby="select-error">
                                            <option value="">Select...</option>
                                            <option value="Category 1">Category 1</option>
                                            <option value="Category 2">Category 2</option>
                                            <option value="Category 3">Category 5</option>
                                            <option value="Category 4">Category 4</option>
                                        </select>
                                    </div>


                                </div>
                                <div class="form-group">
                                    <label class="control-label col-md-2">Comment
                                        <%--<span class="required" aria-required="true"> * </span>--%>
                                    </label>


                                    <div class="col-md-6">
                                        <input type="text" class="form-control input-lg" placeholder="Comment Here">
                                    </div>


                                </div>


                            </div>
                            <%--<div class="form-actions">--%>
                            <%--<div class="row">--%>
                            <%--<div class="col-md-offset-3 col-md-9">--%>
                            <%--<button type="submit" class="btn green">Submit</button>--%>
                            <%--<button type="button" class="btn grey-salsa btn-outline">Cancel</button>--%>
                            <%--</div>--%>
                            <%--</div>--%>
                            <%--</div>--%>
                        </form>
                        <!-- END FORM-->
                    </div>
                    <%--<ul>--%>
                    <%--<li> Lorem ipsum dolor sit amet </li>--%>
                    <%--<li> Consectetur adipiscing elit </li>--%>
                    <%--<li> Integer molestie lorem at massa </li>--%>
                    <%--<li> Facilisis in pretium nisl aliquet </li>--%>
                    <%--<li> Nulla volutpat aliquam velit--%>
                    <%--<ul>--%>
                    <%--<li> Phasellus iaculis neque </li>--%>
                    <%--<li> Purus sodales ultricies </li>--%>
                    <%--<li> Vestibulum laoreet porttitor sem </li>--%>
                    <%--<li> Ac tristique libero volutpat at </li>--%>
                    <%--</ul>--%>
                    <%--</li>--%>
                    <%--<li> Faucibus porta lacus fringilla vel </li>--%>
                    <%--<li> Aenean sit amet erat nunc </li>--%>
                    <%--<li> Eget porttitor lorem </li>--%>
                    <%--</ul>--%>
                </div>
            </div>
            <%----%>

            <%--START: Options--%>
            <div class="portlet box green">
                <div class="portlet-title">
                    <div class="caption">
                        <i class="fa fa-gift"></i>Office Address
                    </div>
                    <div class="tools">
                        <a href="javascript:;" class="collapse" data-original-title="" title=""> </a>
                    </div>
                </div>
                <div class="portlet-body">
                    <div class="portlet-body">
                        <!-- BEGIN FORM-->
                        <form action="#" id="form_sample_1" class="form-horizontal" novalidate="novalidate"
                              _lpchecked="1">
                            <div class="form-body">

                                <div class="form-group">
                                    <label class="control-label col-md-3">POP 1
                                        <span class="required" aria-required="true"> * </span>
                                    </label>
                                    <div class="col-md-4">

                                        <%--<input type="radio" id="one" value="One" v-model="picked">--%>
                                        <%--<label for="one">One</label>--%>
                                        <%--<br>--%>
                                        <%--<input type="radio" id="two" value="Two" v-model="picked">--%>
                                        <%--<label for="two">Two</label>--%>
                                        <%--<br>--%>
                                        <span>Picked: {{ picked }}</span>

                                        <div class="mt-radio-list" data-error-container="#form_2_membership_error">
                                            <label class="mt-radio">
                                                <input type="radio" id="one" v-model="picked" name="membership"
                                                       value="1"> Fee
                                                <span></span>
                                            </label> <br>
                                            <label class="mt-radio">
                                                <input type="radio" id="two" name="membership" v-model="picked"
                                                       value="2"> Professional
                                                <span></span>
                                            </label>
                                        </div>


                                    </div>

                                </div>


                            </div>
                        </form>
                        <!-- END FORM-->
                    </div>
                </div>
            </div>
            <%--END: Options--%>


        </div>
    </div>





</div>

<div id="view">
    <%--start: view application type page--%>
    <div class="portlet light bordered">
        <div class="portlet-title">
            <div class="caption font-green-sharp"><span
                    class="caption-subject bold uppercase">{{captionSubject}}</span> <span class="caption-helper">{{captionHelper}}</span>
            </div>
            <div class="actions"><a href="javascript:;" class="btn btn-circle btn-icon-only btn-default fullscreen"></a>
            </div>
        </div>
        <div class="portlet-body">
            <div class="form-horizontal">
                <div class="form-body">
                    <div class="portlet light bordered"><!---->
                        <div class="portlet-body">


                            <div class="form-group" v-for="element in formElements">
                                <div class="row"><label class="col-sm-4 control-label">{{element.text}}</label>

                                    <div class="col-sm-6">
                                        <div v-if="element.text =='status'"><button type="button" class="btn btn-danger">{{element.value}}</button> </div>
                                        <div v-else ><p class="form-control">{{element.value}}</p> </div>
                                    </div>
                                </div>
                            </div>


                          <%--<div class="form-group">--%>
                              <%--<div class="row"><label class="col-sm-4 control-label"></label>--%>
                                    <%--<div class="col-sm-6">--%>
                                        <%--<button type="button" class="btn btn-default">Preview Processed Content</button>--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                            <%--</div> <br>--%>
                            <%--<div class="form-group">--%>
                                <%--<div class="row"><label class="col-sm-4 control-label">Client Name</label>--%>
                                    <%--<div class="col-sm-6"><p class="form-control">hello</p></div>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group">--%>
                                <%--<div class="row"><label class="col-sm-4 control-label">Bandwidth (Mbps)</label>--%>
                                    <%--<div class="col-sm-6"><p class="form-control">20</p></div>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group">--%>
                                <%--<div class="row"><label class="col-sm-4 control-label">Connection Type</label>--%>
                                    <%--<div class="col-sm-6"><p class="form-control">Regular</p></div>--%>
                                <%--</div>--%>
                            <%--</div>--%>



                            <%--<div class="form-group">--%>
                                <%--<div class="row"><label class="col-sm-4 control-label">Connection Address</label>--%>
                                    <%--<div class="col-sm-6"><p class="form-control">Uttar Badda, Dhaka</p></div>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group">--%>
                                <%--<div class="row"><label class="col-sm-4 control-label">Loop Provider</label>--%>
                                    <%--<div class="col-sm-6"><p class="form-control">BTCL</p></div>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group">--%>
                                <%--<div class="row"><label class="col-sm-4 control-label">Description</label>--%>
                                    <%--<div class="col-sm-6"><p class="form-control">test description</p></div>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group">--%>
                                <%--<div class="row"><label class="col-sm-4 control-label">Comment</label>--%>
                                    <%--<div class="col-sm-6"><p class="form-control">Nice system</p></div>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group">--%>
                                <%--<div class="row"><label class="col-sm-4 control-label">Suggested Date</label>--%>
                                    <%--<div class="col-sm-6"><p class="form-control">1/7/2018</p></div>--%>
                                <%--</div>--%>
                            <%--</div>--%>


                            <div class="row">
                                <div class="col-md-3 col-md-offset-1" style="margin-bottom: 20px;">
                                    <button type="button" class="btn btn-block green-haze">Complete Request</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <%--end: view application type page--%>
</div>

<%--<script src="../connection/lli-connection-components.js"></script>--%>
<%--<script src=../application/lli-application-components.js></script>--%>
<%--<script src=../application/new-connection/lli-application-new-connection.js></script>--%>
<script src=ui.js></script>
<script>
    new Vue({
        el: '#app',
        data: {
            fields: [{first: '', last: ''}],
            picked: '',
        },
        created: function () {
        },
        methods: {
            AddField: function () {
                this.fields.push({first: '', last: ''});
            }
        }
    });


    $(document).ready(function () {
        // $('.js-example-basic-multiple').select2();
    });

    new Vue({
       el: '#view',
       data:{
           captionSubject: 'Cap - New Connection',
           captionHelper: 'Cap - Information',
           formElements: [
               { text: 'status', value: 'STATUS...TAB!!!!!!!!!!!!!!'},
               { text: 'One', value: 'A'},
               { text: 'Two', value: 'B'},
               { text: 'Three', value: 'C'},
               { text: 'Four', value: 'D'},
               { text: 'Five', value: 'E'},

           ],
           actionElements:[
               {text: '1', value: 'A'},
               {text: '2', value: 'B'},
               {text: '3', value: 'C'},
               {text: '4', value: 'D'},
           ],
       }
    });

</script>





<style>
    .border {
        border: 1px solid black;
        padding: 3px;
        margin-bottom: 5px;
    }</style>


<!-- Latest compiled and minified CSS -->
<%--<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.2/css/bootstrap-select.min.css">--%>

<!-- Latest compiled and minified JavaScript -->
<%--<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.2/js/bootstrap-select.min.js"></script>--%>

<!-- (Optional) Latest compiled and minified JavaScript translation files -->
<%--<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.2/js/i18n/defaults-*.min.js"></script>--%>