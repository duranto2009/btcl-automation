
var vue = new Vue({
    el: "#btcl-application",
    data: {
        contextPath: context,
        tasks : [],
        schedulerStatus : '',
    },
    methods: {
        manageTask(task, executable) {

            if(task.allowExecution ^ executable){
                axios.get(context + "scheduler/task/manage.do?taskId="+task.id + "&run="+executable)
                    .then(res=>{
                        if(res.data.responseCode == 1){ successMessage("Your Request Has Been Processed"); this.getAllTasks()}
                        else {errorMessage(res.data.msg)}
                    })
                    .catch(err=>LOG(err));
            }else {
                toastr.info("Task Already " + (executable? "Executable" : "Not Executable"));
            }
        },

        startScheduler(){
            axios.get(context+"scheduler/start.do").then(res=>{
                if(res.data.responseCode == 2) { errorMessage(res.data.msg)}
                else {successMessage("Your Request has been processed") ; this.schedulerStatus = true;}
            }).catch(err=>LOG(err));
        },
        stopScheduler(){
            axios.get(context+"scheduler/stop.do").then(res=>{
                if(res.data.responseCode == 2) { errorMessage(res.data.msg)}
                else {successMessage("Your Request has been processed"); this.schedulerStatus = false;}
            }).catch(err=>LOG(err));
        },
        getAllTasks() {
            axios.get(context + "scheduler/task/all.do")
                .then(res=>{
                    if(res.data.responseCode===1){
                        this.tasks = res.data.payload;
                    }else {
                        errorMessage("Scheduler Data Not Found");
                    }
                }).catch(err=>LOG(err));
        }
    },
    mounted(){

        this.getAllTasks();
        axios.get(context+"scheduler/status.do").then(res=>{
            if(res.data.responseCode==2){toastr.error(res.data.msg, "Failure")}
            else { this.schedulerStatus = res.data.payload;}
        }).catch(err=>LOG(err));
    },
});