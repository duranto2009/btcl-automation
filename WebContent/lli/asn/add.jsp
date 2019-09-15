<div id="btcl-application" v-cloak="true">


    <btcl-body title="ASN" subtitle="Application" :loader="loading">

        <btcl-portlet>
            <btcl-field title="Client">
                <user-search-by-module
                        :client.sync="application.client"
                        :module="7">
                </user-search-by-module>
            </btcl-field>

            <btcl-input title="ASN No"
                        :text.sync="application.asnNo">
            </btcl-input>
            <btcl-field>
                <p style="color: #0a6aa1">Choose only CSV formatted files to add IPV4. File should have only one column Like Below.</p>
                <p style="color: #0a6aa1;font-weight:bold; ">X.X.X.X</p>
                <p style="color: #0a6aa1;font-weight:bold;">X.X.X.X/X</p>
            </btcl-field>
            <btcl-field  title="Add IPV4">
                <div class="col-md-5 col-sm-5">
                <span>
                    <input type="text"
                           v-model="inputIPV4"/>
                </span>
                    <span>
                    <button class="btn btn-primary btn-sm"
                            @click ="addIntoList()">Add
                    </button>
                </span>
                </div>
                <div class="col-md-5 col-sm-5">
                    <span class="col-md-1 col-sm-1">OR
                    </span>
                    <span class="col-md-4 col-sm-4">
                    <input type="file"
                           @change="onFileChangeIPV4"/>
                </span>
                </div>
            </btcl-field >

            <btcl-field v-if="application.ipV4List.length>0"
                            title ="IPV4s">
                <div style="margin-top: 2px;border: 1px solid;"
                     class="form-control"
                     v-for="(ip,index) in application.ipV4List">
                    <div class="row">
                        <div class="col-sm-6">{{ip}}
                        </div>
                        <div class="col-sm-2">
                            <button  v-on:click="removeIP(index)"
                                     type="button" style="color:red" >x
                            </button>
                        </div>
                    </div>
                </div>
            </btcl-field>

            <btcl-field>
                <p style="color: #0a6aa1">Choose only CSV formatted files to add  IPV6 . File should have only one column Like Below.</p>
                <p style="color: #0a6aa1;font-weight:bold;"> X.X.X.X.X.X.X.X</p>
            </btcl-field>
            <btcl-field title="Add IPV6">
                <div class="col-md-5 col-sm-5">
                    <span>
                        <input type="text"
                               v-model="inputIPV6"/>
                    </span>
                    <span>
                        <button class="btn btn-primary btn-sm"
                                @click ="addIPV6IntoList()">Add
                        </button>
                    </span>
                </div>
                <div class="col-md-5 col-sm-5">
                    <span class="col-md-1 col-sm-1">OR</span>
                    <span class="col-md-4 col-sm-4">
                        <input type="file" @change="onFileChangeIPV6"/>
                    </span>
                </div>
            </btcl-field>
            <btcl-field v-if="application.ipV6List.length>0"
                        title ="IPV6s">
                <div style="margin-top: 2px;border: 1px solid;"
                     class="form-control"
                     v-for="(ip,index) in application.ipV6List">
                    <div class="row">
                        <div class="col-sm-6">{{ip}}
                        </div>
                        <div class="col-sm-2">
                            <button  v-on:click="removeSelectedIPV6(index)"
                                     type="button" style="color:red" >x
                            </button>
                        </div>
                    </div>
                </div>
            </btcl-field>


            <btcl-input title="Description"
                        :text.sync="application.description">
            </btcl-input>
            <btcl-input title="Comment"
                        :text.sync="application.comment">
            </btcl-input>

            <btcl-field title="Suggested Date">
                <btcl-datepicker :date.sync="application.suggestedDate">
                </btcl-datepicker>
            </btcl-field>



                <div style="padding-top:30px">
                    <button type="submit" class="btn green-haze btn-block" @click="submitFormData" >Submit</button>
                </div>

        </btcl-portlet>

        <%--</btcl-form>--%>
    </btcl-body>
</div>


<script>

    var vue = new Vue({
        el: "#btcl-application",
        data: {
            contextPath: context,
            connectionOptionListByClientID: [],
            application: {
                client:{},
                fileDataIPV4:[],
                ipV4List:[],
                ipV6List:[],
                fileDataIPV6:[],
            },
            module:7,
            inputIPV4:"",
            inputIPV6:"",
            loading:false,
        },
        methods: {

            validateIPV4Address: function(address) {
                address = address.trim().replace(/\s/g, "");
                //const regex =  /^(?=\d+\.\d+\.\d+\.\d+$)(?:(?:25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.?){4}$/;
                const regex = /^(?=\d+\.\d+\.\d+\.\d+($|\/))(([1-9]?\d|1\d\d|2[0-4]\d|25[0-5])\.?){4}(\/([0-9]|[1-2][0-9]|3[0-2]))?$/;
                if(!regex.test(address)){
				toastr.error("Enter a valid IP Address in X.X.X.X or X.X.X.X/X format");
                    return false;
                }
                return true;
            },

            validateIPV6Address: function(a) {
                return (/^\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:)))\s*$/.test(a))
            },
            addIntoList:function(){
                if(this.inputIPV4!=""&& this.validateIPV4Address(this.inputIPV4)){
                    this.application.ipV4List.push(this.inputIPV4);
                }

            },
            removeIP: function (index) {
                this.application.ipV4List.splice(index, 1);
            },
            addIPV6IntoList:function(){
                if(this.inputIPV6!=""&& this.validateIPV6Address(this.inputIPV6)) {
                    this.application.ipV6List.push(this.inputIPV6);
                    return;
                }
                else{
                    toastr.error("Please Insert a valid IPV6 Address", "Failure");
                }
            },
            removeSelectedIPV6: function (index) {
                this.application.ipV6List.splice(index, 1);
            },
            onFileChangeIPV4:function(e){
                var files = e.target.files || e.dataTransfer.files;
                if (!files.length)
                    return;
                var reader = new FileReader();
                var regex = new RegExp("(.*?)\.(csv)$");

                if (!(regex.test(files[0].name))) {
                    toastr.error("Please Select Only CSV formatted files", "Failure");
                    return;
                }
                reader.addEventListener('load', function (e) {
                    var textContent = e.target.result;
                    var fileContent = textContent;
                    var lines=fileContent.split("\n");

                    var result = [];


                    for(var i=1;i<lines.length;i++){
                        var headers=lines[i].split(",");

                        var obj = [];
                        var k= 0;
                        for(var j=0;j<headers.length;j++){
                            headers[j] = headers[j].trim();
                            if(headers[j] !="") {
                                obj[k] = headers[j];
                                k++;
                            }
                        }

                        if(obj.length>0)result.push(obj);

                    }
                    var str = JSON.stringify(result).replace(/]|[[]/g, '');
                    var strAry = str.split(",");
                    for(var i=0;i<strAry.length;i++) {
                        strAry[i] = strAry[i].replace(/"/g, "");
                    }
                    vue.application.fileDataIPV4 =   strAry
                });

                reader.readAsBinaryString(files[0]);
            },
            onFileChangeIPV6:function(e){
                var files = e.target.files || e.dataTransfer.files;
                if (!files.length)
                    return;
                var reader = new FileReader();
                var regex = new RegExp("(.*?)\.(csv)$");

                if (!(regex.test(files[0].name))) {
                    toastr.error("Please Select Only CSV formatted files", "Failure");
                    return;
                }
                reader.addEventListener('load', function (e) {
                    var textContent = e.target.result;
                    var fileContent = textContent;
                    var lines=fileContent.split("\n");

                    var result = [];


                    for(var i=1;i<lines.length;i++){
                        var headers=lines[i].split(",");

                        var obj = [];
                        var k= 0;
                        for(var j=0;j<headers.length;j++){
                            headers[j] = headers[j].trim();
                            if(headers[j] !="") {
                                obj[k] = headers[j];
                                k++;
                            }
                        }

                        if(obj.length>0)result.push(obj);

                    }
                    var str = JSON.stringify(result).replace(/]|[[]/g, '');
                    var strAry = str.split(",");
                    for(var i=0;i<strAry.length;i++) {
                        strAry[i] = strAry[i].replace(/"/g, "");
                    }
                    vue.application.fileDataIPV6=   strAry
                });

                reader.readAsBinaryString(files[0]);
            },

            submitFormData:function () {
                var url1 = "add";

                if(this.application.asnNo!=null && !$.isNumeric(this.application.asnNo)) {
                    toastr.error("Insert Numeric values for ASN No", "Failure");
                    return;
                }
                if(this.application.ipV4List.length>0){
                    for(var i=0;i<this.application.ipV4List.length;i++){
                        if(!this.validateIPV4Address(this.application.ipV4List[i])){
                            return;
                        }
                    }
                }
                if(this.application.ipV6List.length>0){
                    for(var i=0;i<this.application.ipV6List.length;i++){
                        if(!this.validateIPV6Address(this.application.ipV6List[i])) {
                            toastr.error("Insert Valid IPV6 Address", "Failure");
                            return;
                        }
                    }
                }
                if(this.application.suggestedDate==null){
                    toastr.error("Insert Date", "Failure");
                    return;
                }
                this.loading = true;
                Promise.resolve(
                axios.post(context + 'asn/' + url1 + '.do', {'application': JSON.stringify(this.application)})
                    .then(result => {
                        if (result.data.responseCode == 2) {
                            toastr.error(result.data.msg);
                        } else if (result.data.responseCode == 1) {
                            toastr.success("Your request has been processed", "Success");
                            window.location.href = context + 'asn/search.do';
                        }
                        else {
                            toastr.error("Your request was not accepted", "Failure");
                        }
                    })
                    .catch(function (error) {
                    })).then(()=>this.loading = false);
            },
            clearConnection: function(){
                this.connectionOptionListByClientID = [];
            },
            errorMessage: function (msg) {
                toastr.options.timeOut = 3000;
                toastr.error(msg);
                return;
            },
        },
        watch:{
            'application.fileDataIPV4':function () {
                if(this.application.fileDataIPV4.length>0){
                    for(var i=0;i<this.application.fileDataIPV4.length;i++){
                        this.application.ipV4List.push(this.application.fileDataIPV4[i]);
                    }
                    this.application.fileDataIPV4 = [];
                }
            },
            'application.fileDataIPV6':function () {
                if(this.application.fileDataIPV6.length>0){
                    for(var i=0;i<this.application.fileDataIPV6.length;i++){
                        this.application.ipV6List.push(this.application.fileDataIPV6[i]);
                    }
                    this.application.fileDataIPV6 = [];

                }
            },

        }
    });

</script>

