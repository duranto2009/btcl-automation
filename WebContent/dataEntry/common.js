function connectionBuilder(vue) {
    debugger;
    //client id
    let clientId = 0;



    let connectionList = [];
    for (let i = 0; i < vue.connectionList.length; i++) {
        debugger;
        if (vue.connection.client.ID > 0) {
            clientId = vue.connection.client.ID;
        } else if (vue.connection.hasOwnProperty("client")) {
            if (vue.connection.client)
                clientId = vue.connection.client.key;
        }

        //officeList
        let officeList = [];
        for (let l = 0; l < vue.connectionList[i].officeList.length; l++) {
            let officeEnd = vue.connectionList[i].officeList[l];

            clientId: clientId;

            let office = null;
            let officeName;
            if (officeEnd.officeName != null) {
                officeName = officeEnd.officeName;
            } else {
                officeName = '';
            }
            office = {
                officeName: officeName,
                officeAddress: officeEnd.officeAddress,

                moduleId: vue.moduleId,
                loops:
                    [
                        {
                            loopProvider: vue.connectionList[i].loopProvider.ID,
                            BTCLDistances:officeEnd.btclLength,
                            OCDistances:officeEnd.ocLength,
                            OCID:officeEnd.oc.ID,
                            OCDistances:officeEnd.ocLength,
                            ofcType: officeEnd.ofcType.ID,
                        }
                    ]
            };

            officeList.push(office);
        }

        let con = {
            connectionType: vue.connection.connectionType.ID,
            offices: officeList,
            name:vue.connectionList[i].name,
            bandwidth: vue.connectionList[i].bandwidth,
            zoneID: vue.connectionList[i].zone.id,
            clientID: clientId,
            activeFrom: vue.connectionList[i].activeFrom,
        };

        connectionList.push(con);
    }






    return connectionList;
}

function getDataListByURL(url) {
    return axios.get(url)
        .then(result => {
            if (result.data.responseCode == 2) {
                toastr.error(result.data.msg);
            } else if (result.data.responseCode == 1) {
                // this.officeList =
                return result.data.payload;
            }
            return [];
            debugger;
        })
        .catch((err) => LOG(err));

}

function postObject(obj, url, redirectURL, vue) {
    debugger;
    vue.loading = true;
    axios.post(url, obj)
        .then(result => {
            if (result.data.responseCode == 2) {
                toastr.error(result.data.msg);
            } else if (result.data.responseCode == 1) {
                toastr.success("Your request has been processed", "Success");
                toastr.options.timeOut = 2500;
                if (redirectURL === '') {
                    vue.loading = false;
                    return;
                }
                window.location.href = redirectURL;
            } else {
                toastr.error("Your request was not accepted", "Failure");
                toastr.options.timeOut = 2500;
            }
        }).catch((err) => LOG(err));
}


function getNewRequestFormItems(vue) {
    let item_url = context + 'upstream/get-items-by-type.do?item_type=';
    getDataListByURL(item_url + TYPE_OF_BW).then(result => {
        vue.typeOfBandwidthList = result;
    });
    getDataListByURL(item_url + BTCL_SERVICE_LOCATION).then(result => {
        vue.btclServiceLocationList = result;
    });
    getDataListByURL(item_url + PROVIDER_LOCATION).then(result => {
        vue.providerLocationList = result;
    });
    getDataListByURL(item_url + MEDIA).then(result => {
        vue.mediaList = result;
    });
}


function getObjectValueFromObjectListByKey(objectList, key) {
    let obj = objectList.find(obj => obj.key === key);
    if (obj) {
        return obj.value;
    } else {
        return null;
    }
}


function deleteFromRepeater(obj, property, Index) {
    obj[property].splice(Index, 1);
}

function addEmptyObject(obj, property) {
    obj[property].push({});
}


function convertMillisecondsToYearMonthDay(time) {
    let durationString = "";
    let y = 1 * 365 * 24 * 60 * 60 * 1000;
    let m = 30 * 24 * 60 * 60 * 1000;
    let d = 24 * 60 * 60 * 1000;
    if (Math.floor(time / y) > 0) {
        durationString += time / y + " Years ";
        time = time % y;
    }

    if (Math.floor(time / m) > 0) {
        durationString += " ";
        durationString += time / m + " Months";
        time = time % m;
    }

    if (Math.floor(time / d) > 0) {
        durationString += " ";
        durationString += time / d + " Days";
        time = time % d;
    }
    return durationString;
}


function getOfficeByClientId(clientId) {
    let url = context + 'entity/get-office-by-client.do?clientId=' + clientId;
    return getDataListByURL(url);
}


function getVPNNetworkLinksByClientId(clientId) {
    let url = context + 'vpn/network/get-all-by-client.do?id=' + clientId;
    return getDataListByURL(url);
}