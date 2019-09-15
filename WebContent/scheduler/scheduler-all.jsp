<div id="btcl-application" v-cloak="true">
    <btcl-body title="Scheduler" subtitle="View All">
        <btcl-portlet>
            <table class="table table-striped table-bordered">
                <thead >
                <tr class="text-center">
                    <td>Name</td>
                    <td>Last Running Time</td>
                    <td>Next Running Time</td>
                    <td>Interval Type</td>
                    <td>Scheduling Hour</td>
                    <td>Scheduling Minute</td>
                    <td>Status</td>
                    <td>Action</td>

                </tr>
                </thead>
                <tr v-for="(item, index) in tasks" class="text-center">
                    <td> {{ item.name}}</td>
                    <td> {{ new Date(parseInt(item.lastRunningTime)).toLocaleString() }}</td>
                    <td> {{ new Date(parseInt(item.nextRunningTime)).toLocaleString() }}</td>
                    <td> {{ item.intervalType}}</td>
                    <td> {{ item.schedulingHour}}</td>
                    <td> {{ item.schedulingMinute}}</td>
                    <td> {{ item.allowExecution == 1 ? "Executable" : "Not Executable"}}</td>
                    <td v-if="item.allowExecution"> <button class="btn btn-circle-left red" @click="manageTask(item, false)">Stop</button></td>
                    <td v-else> <button class="btn btn-circle-right green-haze" @click="manageTask(item, true)">Start</button></td>

                </tr>

            </table>
        </btcl-portlet>
        <hr>
        <btcl-grid column="2">
            <button class="btn green-haze" @click="startScheduler" :disabled="schedulerStatus">Start</button>
            <button class="btn red" @click="stopScheduler" :disabled="!schedulerStatus">Stop</button>

        </btcl-grid>
        <%--<div v-if="schedulerStatus">--%>

        <%--</div>--%>
        <%--<div>--%>
            <%----%>
        <%--</div>--%>
    </btcl-body>
</div>
<script src="../scheduler/scheduler.js"></script>