


<link rel="stylesheet" href="${context}dashboard/sweet-modal.css"/>
<!-- BEGIN NOTIFICATION DROPDOWN -->
<li   class="dropdown dropdown-extended dropdown-notification dropdown-light" id="header_notification_bar">

    <a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-close-others="true"
       aria-expanded="false">
        <i class="icon-bell"></i>
        <span class="badge badge-success"> {{notificationList.length}} </span>

    </a>
    <ul class="dropdown-menu">
        <li class="external">
            <h3>
                <span class="bold">{{notificationList.length}} pending</span> notifications &nbsp;
                <a @click="showModal" onclick="return false;" class="fa fa-expand pull-right"> </a> </h3>
<%--            <div class="actions">--%>
<%--                <!--<a href="javascript:;" class="btn btn-circle btn-default btn-sm"><i class="fa fa-pencil"></i> Edit </a>-->--%>
<%--                <a class="btn btn-circle btn-icon-only btn-default fullscreen" href="javascript:;"> </a>--%>
<%--            </div>--%>
<%--            <a href="page_user_profile_1.html">view all</a>--%>
        </li>

        <li>
            <div  class="slimScrollDiv" style="position: relative; overflow: hidden; width: auto; height: auto;">
<%--                <btcl-body>--%>
<%--                height: 250px;overflow: hidden;width: auto;overflow-y: scroll;--%>
                <ul class="dropdown-menu-list scroller" style="height: 250px; overflow: hidden; width: auto;overflow-y: scroll;"
                    data-handle-color="#637283" data-initialized="1" id="notificationDOM">
                    <li v-for="notification in notificationList" >
                        <a :href="context+notification.actionURL.substr(1)">
                            <span class="time">{{(new Date(notification.generationTime)).toLocaleString()}}</span>
                            <span class="details"><span class="label label-sm label-icon label-success">
                                                            <i class="fa fa-plus"></i>
                                                        </span> {{notification.description}} </span>
                        </a>
                    </li>

                </ul>
                <div class="slimScrollBar"
                     style="background: rgb(99, 114, 131); width: 7px; position: absolute; top: 0px; opacity: 0.4; display: none; border-radius: 7px; z-index: 99; right: 1px; height: 121.359px;"></div>
                <div class="slimScrollRail"
                     style="width: 7px; height: 100%; position: absolute; top: 0px; display: none; border-radius: 7px; background: rgb(234, 234, 234); opacity: 0.2; z-index: 90; right: 1px;"></div>

<%--                </btcl-body>--%>
            </div>
        </li>

    </ul>
</li>
<!-- END NOTIFICATION DROPDOWN -->
<%
//    LoginDTO loginDTO = (LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
    request.setAttribute("context", context);
%>

<script>
    var context = '${context}';
    var notification = new Vue({
        el: "#header_notification_bar",
        data: {
            id: <%=loginDTO.getRoleID()%>,
            accountId:<%=loginDTO.getAccountID()%>,

            notificationList: [],
            context: '',
        },
        methods: {
            getDataListByURL: function (url) {
                return axios.get(url)
                    .then(result => {
                        if (result.data.responseCode == 1) {
                            // this.officeList =
                            return result.data.payload;
                        }
                        return [];
                    })
                    .catch(function (error) {
                    });

            },
            showModal: function(){
                // let content = (document.getElementById("notificationDOM").innerHTML);

                var modalContent = "";
                for(let i = 0; i < this.notificationList.length; i++){
                    let notification = this.notificationList[i];
                    let url = context+notification.actionURL.substr(1);
                    let time = (new Date(notification.generationTime)).toLocaleString();
                    let description = notification.description;

                    modalContent+="<a href='"+url+"' style='font-size: medium;'>" + time + '&nbsp;: ' + description + "</a>" + "<br/><br/>";

                }

                $.sweetModal({
                    title: "Notifications",
                    // content: '<div style="width:100%; height: 500px; " id="chart-div"></div>'
                    content: modalContent
                });
            }

        },
        computed: {},
        mounted() {
        },
        created() {

            let url = context + 'notification/get-user-notification.do?id='+this.id+'&accountId='+this.accountId;
            this.getDataListByURL(url).then(result =>{notification.notificationList = result;});
            this.context = context;
        },
    });
</script>
<script src="${context}dashboard/sweet-modal.js"></script>
