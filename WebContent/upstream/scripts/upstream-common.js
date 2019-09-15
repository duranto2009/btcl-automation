function getDataListByURL(url){
    return axios.get(url)
        .then(result => {
            if (result.data.responseCode == 1) {
                // this.officeList =
                return result.data.payload;
            }
            return [];
            // debugger;
        })
        .catch(function (error) {});

}

function postObject(obj, url, redirectURL,vue){
    // debugger;
    vue.loading = true;
    axios.post(url, obj)
        .then(result => {
            if (result.data.responseCode == 2) {
                toastr.error(result.data.msg);
            } else if (result.data.responseCode == 1) {
                toastr.success("Your request has been processed", "Success");toastr.options.timeOut = 2500;
                if(redirectURL === ''){
                    vue.loading = false;
                    return;
                }
                window.location.href = redirectURL;
            } else {
                toastr.error("Your request was not accepted", "Failure");toastr.options.timeOut = 2500;
            }
        }).catch(function (error) {});
}






const LETTERS_OF_BTCL_BACKHAUL = "LETTERS_OF_BTCL_BACKHAUL";
const LINK_INFO_OF_BSCCL = "LINK_INFO_OF_BSCCL";
const BTCLS_WORK_ORDER_TO_PROVIDER = "BTCLS_WORK_ORDER_TO_PROVIDER";

function getNewRequestFormItems(vue){
    let item_url = context + 'upstream/get-items-by-type.do?item_type=';
    getDataListByURL(item_url+ TYPE_OF_BW).then(result =>{vue.typeOfBandwidthList = result;});
    getDataListByURL(item_url+ BTCL_SERVICE_LOCATION).then(result =>{vue.btclServiceLocationList = result;});
    getDataListByURL(item_url+ PROVIDER_LOCATION).then(result =>{vue.providerLocationList = result;});
    getDataListByURL(item_url+ PROVIDER).then(result =>{vue.providerList = result;});
    getDataListByURL(item_url+ MEDIA).then(result =>{vue.mediaList = result;});
}

function newUpstreamRequestApplicationBuilder(vue){
    let comment = '';
    if(vue.application.comment){
        comment = vue.application.comment;
    }
    let app = {
        typeOfBandwidthId : vue.application.typeOfBandwidth.key,
        bandwidthCapacity : vue.application.bandwidthCapacity,
        mediaId : vue.application.media.key,
        btclServiceLocationId : vue.application.btclServiceLocation.key,
        providerLocationId : vue.application.providerLocation.key,
        selectedProviderId : vue.application.provider.key,
        comment: comment
    };
    return app;
}


function getObjectValueFromObjectListByKey(objectList, key){
    let obj =  objectList.find(obj => obj.key === key);
    if(obj){
        return obj.value;
    }else{
        return null;
    }
}


function deleteFromRepeater(obj, property, Index){
    obj[property].splice(Index, 1);
}

function addEmptyObject(obj,property){
    obj[property].push({});
}







function convertMillisecondsToYearMonthDay(time){
    let durationString = "";
    let y = 1 * 365 * 24 * 60 * 60 * 1000;
    let m = 30 * 24 * 60 * 60 * 1000 ;
    let d = 24 * 60 * 60 * 1000 ;
    if(Math.floor(time / y)> 0){
        durationString += parseInt(time / y) + " Years ";
        time = time % y;
    }

    if( Math.floor(time / m )> 0){
        durationString += " " ;
        durationString += parseInt(time / m) + " Months";
        time = time %m;
    }

    if(Math.floor(time / d ) > 0){
        durationString += " ";
        durationString += parseInt(time / d) + " Days";
        time = time % d;
    }
    return durationString;
}
















function getVPNOfficeByClientId(clientId) {
    let url = context + 'vpn/link/get-office-by-client.do?clientId=' + clientId;
    return getDataListByURL(url);
}



function getVPNNetworkLinksByClientId(clientId){
    let url = context + 'vpn/network/get-all-by-client.do?id=' + clientId;
    return getDataListByURL(url);
}

function submitBandwidthChangeForm(vue){
    if(vue.application.hasOwnProperty("vpnApplicationLinks")){
        vue.application.vpnApplicationLinks[0].linkBandwidth = vue.changedBandwidth;
        let app = vue.application;
        submitData(app, vue.link.linkBandwidth);
        return;

    }
    let userId;
    if (isClientLoggedIn) {
        userId = 0;
    } else {
        userId = loggedInAccount.ID;
    }



    let app = {

        clientId: vue.application.client.key,

        userId: userId,

        moduleId: vue.moduleId,

        suggestedDate: vue.application.suggestedDate,

        comment: vue.application.comment,


        // description: this.application.description,

        // layerType: vue.application.layerType.id,

        // skipPayment: skipPayment,

        vpnApplicationLinks: [{
            networkLinkId: vue.link.id,
            linkBandwidth: vue.changedBandwidth,
            linkName: vue.link.linkName,
            localOfficeId: vue.link.localOfficeId,
            remoteOfficeId: vue.link.remoteOfficeId,
            localOfficeTerminalDeviceProvider: vue.link.localOfficeTerminalDeviceProvider,
            remoteOfficeTerminalDeviceProvider: vue.link.remoteOfficeTerminalDeviceProvider,
            localZoneId:  vue.link.localZoneId,
            remoteZoneId: vue.link.remoteZoneId,
            localOfficeLoopId: vue.link.localEndOffice.localLoops[0].id,
            remoteOfficeLoopId: vue.link.remoteEndOffice.localLoops[0].id,


        }],
    };
    submitData(app, vue.link.linkBandwidth);
}

const submitData = (app, linkBW) => {
    let url = "bandwidth-change";
    axios.post(context + 'vpn/link/' + url + '.do', {
        // 'application': JSON.stringify(this.application)
        application: app,
        previousBandwidth: linkBW
    }).then(result => {
        // debugger;
        if (result.data.responseCode == 2) {
            toastr.error(result.data.msg);
        } else if (result.data.responseCode == 1) {
            toastr.success("Your request has been processed", "Success");
            toastr.options.timeOut = 2500;
            //redirect to specific page
            window.location.href = context + 'vpn/link/search.do';
        } else {
            toastr.error("Your request was not accepted", "Failure");
            // window.location.href = context + 'vpn/link/add.do';
            toastr.options.timeOut = 2500;

        }
    }).catch((err) => LOG(err));
}