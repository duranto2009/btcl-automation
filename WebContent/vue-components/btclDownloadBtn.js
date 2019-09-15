export default {
    name : 'BTCLDownloadBtn',
    template:
    `
     <div>
        <div class="row">
            <div :align="alignment">
                <div v-if="loaderController" class="spinner-loading"></div>
                <button v-else type="button" :class="class_list"  @click="execute">{{ btn_text }} </button>   
            </div>
        </div>
        <br>
     </div>
     `,
    data()  {
        return {
            loaderController : false
        }
    },
    props : {
        alignment : String,
        class_list: String,
        btn_text : String,
        url_method : String,
        url: String,
        url_param: Object,
        file_name: String,
    },
    methods : {
        execute () {
            this.loaderController = true;
            Promise.resolve(
                axios({
                    method : this.url_method,
                    url : this.url,
                    data : this.url_param,
                    responseType: 'blob'
                }).then(res=> {
                    const url = window.URL.createObjectURL(new Blob([res.data]));
                    const link = document.createElement('a');
                    link.href = url;
                    link.setAttribute('download', this.file_name || 'file');
                    document.body.appendChild(link);
                    link.click();

                }).catch(error => {
                    console.log(error);
                })
            ).then(()=> {
                this.loaderController = false;
            });
        }
    },
    watch: {
        loaderController(){
            this.$emit('download', this.loaderController);
        }
    }
}